package com.zbar.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.com.zbar.lib.camera.CameraManager;
import com.com.zbar.lib.decode.CaptureActivityHandler;
import com.com.zbar.lib.decode.InactivityTimer;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.activity.common.IViewController;

import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPShopCartManager;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.Condition;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;

import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.utils.SSUtils;

import org.json.JSONObject;


/**

 * 
 * 描述: 扫描界面
 */
public class CaptureActivity extends BaseActivity implements Callback, View.OnClickListener {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private static final int NUMBER = 1;

	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	
	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}
	private JSONObject specJson;//Map<String , List<SPProductSpec>>
	/** Called when the activity is first created. */
	private TextView tv_titlebar_right;
	private Button rightBtn;
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.setCustomerTitle(true, true, getString(R.string.activity_catureactivity_title), true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_scan);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		mContext=this;
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		super.init();
	}

	@Override
	public void initSubViews() {
		tv_titlebar_right= (TextView) findViewById(R.id.ritht_title_txtv);
		rightBtn= (Button) findViewById(R.id.titlebar_menu_btn);
		rightBtn.setVisibility(View.GONE);
		tv_titlebar_right.setVisibility(View.VISIBLE);
		tv_titlebar_right.setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initEvent() {

	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
//		String goodsid= SSUtils.getNumbers(result);
////		Toast.makeText(getApplicationContext(), result+" goodsid "+goodsid, Toast.LENGTH_SHORT).show();
//		int goodsID =SSUtils.str2Int(goodsid);
//
//		getGoodsspec(goodsID);
		showDialog(result);
//		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
//		handler.sendEmptyMessage(R.id.restart_preview);

	}

	private void addGoodsCarbyId(String goodsid,String spc, int number) {
		SPShopCartManager.getInstance(this).shopCartGoodsOperation(goodsid,spc,  number, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				showToast(getString(R.string.toast_shopcart_action_success));

			}
		}, new SPFailuredListener((IViewController) this) {
			@Override
			public void onRespone(String msg, int errorCode) {
				showToast(msg);

			}
		});
		//		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		handler.sendEmptyMessage(R.id.restart_preview);

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);
			

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};



	private void getGoodsspec(final int goodsId){
		specJson = new JSONObject();//清理之前的缓存数据
		ProductCondition condition = new ProductCondition();
		condition.goodsID=goodsId;
		ShopRequest.getProductByID(condition, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				hideLoadingToast();
				try {
					mDataJson = (JSONObject) response;
					if (mDataJson != null && mDataJson.has("product")) {
						SPProduct	mProduct = (SPProduct) mDataJson.get("product");
						List<SPProductSpec> specList = null;
						if (mProduct != null) {
							if (mProduct.getSpecArr() != null && mProduct.getSpecArr().size() > 0) {
								//排序
//								Collections.sort(mProduct.getSpecArr());
								specList=mProduct.getSpecArr();

								//循环获取商品规格
								//并将商品规格以specName为key进行分类
								//specName相同为一组
//								for (SPProductSpec productSpec : mProduct.getSpecArr()) {
//
//									if (specJson.has(productSpec.getSpecName())) {
//										specList = (List<SPProductSpec>) specJson.get(productSpec.getSpecName());
//										if (!(specList.contains(productSpec))) {
//											specList.add(productSpec);
//										}
//									} else {
//										specList = new ArrayList<SPProductSpec>();
//										specList.add(productSpec);
//									}
//								}

								addGoodsCarbyId(goodsId+"",specList.get(0).getItemID(),NUMBER);
							}
						}
						if (mProduct.getSpecArr() != null) {
                    }
					}

				} catch (Exception e) {
					showToast(e.getMessage());
				}
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				showToast( msg);
			}
		});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				light();
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:

				light();
				return true;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}



	//显示结果
	public void showDialog(final String msg) {
		showToast(msg);
			String[] strarray = msg.split("[&]");
			String[] strarray1 = strarray[0].split("[=]");
			new AlertDialog.Builder(CaptureActivity.this).setTitle(getString(R.string.memo)).
					setMessage(
							String.format("是否添加:%1$s 到购物车?", strarray1[1])).
					setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							String goodsid= SSUtils.getNumbers(msg);
							int goodsID =SSUtils.str2Int(goodsid);

							getGoodsspec(goodsID);
						}
					}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			}).show();
		}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ritht_title_txtv:
//				showToast("说明");
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle("使用说明")
//						.setMessage("Dialog content.")
//											.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
////							@Override
//							public void onClick(DialogInterface dialog, int which) {
//							}
//						}).show();


				LayoutInflater flat = LayoutInflater.from(this);
				// 将R.layout.quake_details填充到Layout
				View layout_exp = flat.inflate(R.layout.dialog_text, null);
				TextView expl = (TextView) layout_exp.findViewById(R.id.tv_msg);
				expl.setText(SSUtils.getFromAssets("guid_capture.txt",mContext));

				new AlertDialog.Builder(this).setTitle("使用说明").setView(layout_exp)
						.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();

							break;
						}
		}
}