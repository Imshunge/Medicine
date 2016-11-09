package com.shssjk.capture;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Hashtable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import com.google.zxing.ChecksumException;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;



import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;

/**
 * 扫一扫
 */
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, OnClickListener {

	private static final String TAG = CaptureActivity.class.getSimpleName();
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private TextView common_title_TV_left;
	private TextView common_title_TV_right;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private TitleView title;
	private Button rithtBtn;  //右侧按钮
		//private Button from_gallery;
	private final int from_photo = 010;
	static final int PARSE_BARCODE_SUC = 3035;
	static final int PARSE_BARCODE_FAIL = 3036;
	String photoPath;
	ProgressDialog mProgress;
	Context mContext;
	// Dialog dialog;

	enum IntentSource {

		ZXING_LINK, NONE

	}

	Handler barHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				//viewfinderView.setRun(false);
				showDialog((String) msg.obj);
				break;
			case PARSE_BARCODE_FAIL:
				showToast((String) msg.obj);
				showDialog((String) msg.obj);
				if (mProgress != null && mProgress.isShowing()) {
					mProgress.dismiss();
				}
				new AlertDialog.Builder(CaptureActivity.this).setTitle("提示").setMessage("扫描失败！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.setCustomerTitle(true, true, "扫描二维码", true);
		super.onCreate(icicle);
		mContext=this;
		// Window window = getWindow();
		// window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);
		statusView = (TextView) findViewById(R.id.status_view);
//		common_title_TV_left = (TextView) findViewById(R.id.common_title_TV_left);
		common_title_TV_right = (TextView) findViewById(R.id.common_title_TV_right);


		findViewById(R.id.com_guid).setOnClickListener(this);
		//title = (TitleView) findViewById(R.id.decode_title);
		//from_gallery = (Button) findViewById(R.id.from_gallery);
		// 为标题和底部按钮添加监听事件
		setListeners();
		super.init();
	}

	@Override
	public void initSubViews() {
		rithtBtn = (Button) findViewById(R.id.titlebar_menu_btn);
		rithtBtn.setOnClickListener(this);
		rithtBtn.setBackground(null);
		rithtBtn.setText(R.string.scan_explain);
//		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) rithtBtn.getLayoutParams(); //取控件textView当前的布局参数
//		linearParams.height = 20;// 控件的高强制设成20
//		linearParams.width =LinearLayout.LayoutParams.WRAP_CONTENT;// 控件的宽强制设成30
//		rithtBtn.setLayoutParams(linearParams);

	}

	@Override
	public void initData() {

	}

	@Override
	public void initEvent() {

	}

	//显示结果
	public void showDialog(final String msg) {
			showToast(msg);
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.dismiss();
		}
		if(msg.startsWith("http")){
			new AlertDialog.Builder(CaptureActivity.this).setTitle(getString(R.string.memo)).
//			setMessage(String.format(getString(R.string.barcode_tow_dimen_success), msg)).
			setMessage("是否加入购物车？").
			setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//	String testUrl = "http://www.on-con.com/oncon/pages/download.jsp";
//					getClient().getJson_T(
//							msg,
//							TYPE.POST,
//							null, 2);
//						Intent intent = new Intent();        
//						intent.setAction("android.intent.action.VIEW");    
//						Uri content_url = Uri.parse(msg);
//						intent.setData(content_url);
//						startActivity(intent);
//						finish();
				}
			}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
						restartPreviewAfterDelay(0L);
					}
				}
			}).show();
		}else{
			String[] strarray = msg.split("[&]");
			String[] strarray1 = strarray[0].split("[=]");
			final String[] strarray2 = strarray[1].split("[=]");
			new AlertDialog.Builder(CaptureActivity.this).setTitle(getString(R.string.memo)).
			setMessage(
					String.format("是否添加:%1$s 到购物车?", strarray1[1])).
			setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
//					getClient().getJson_T(
//							Urls.SHOPCART.ADD,
//							TYPE.POST,
//							Client.StringToNameValuePair("goodid=" + strarray2[1] + "&num=1"), 2);
						
				}
			}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
						restartPreviewAfterDelay(0L);
					}
				}
			}).show();
		}

	}

	public void setListeners() {
//		common_title_TV_left.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});

		common_title_TV_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringUtils.showPictures(CaptureActivity.this, from_photo);
			}
		});
	}

	public String parsLocalPic(String path) {
		String parseOk = null;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		// 缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + "   " + h);
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader2 = new QRCodeReader();
		Result result;
		try {
			result = reader2.decode(bitmap1, hints);
			Log.i("steven", "result:" + result);
			parseOk = result.getText();

		} catch (NotFoundException e) {
			parseOk = null;
		} catch (ChecksumException e) {
			parseOk = null;
		} catch (FormatException e) {
			parseOk = null;
		}
		return parseOk;
	}
//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("steven", "data.getData()" + data);
		if (data != null) {
			mProgress = new ProgressDialog(CaptureActivity.this);
			mProgress.setMessage("正在扫描...");
			mProgress.setCancelable(false);
			mProgress.show();
			final ContentResolver resolver = getContentResolver();
			if (requestCode == from_photo) {
				if (resultCode == RESULT_OK) {
					Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
					if (cursor.moveToFirst()) {
						photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							String result = parsLocalPic(photoPath);
							if (result != null) {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_SUC;
								m.obj = result;
								barHandler.sendMessage(m);
							} else {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_FAIL;
								m.obj = "扫描失败！";
								barHandler.sendMessage(m);
							}
							Looper.loop();
						}
					}).start();
				}

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler = null;
		lastResult = null;
		resetStatusView();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		inactivityTimer.onResume();
		source = IntentSource.NONE;
		decodeFormats = null;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		if (mProgress!= null) {
			mProgress.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 这里初始化界面，调用初始化相机
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	private static ParsedResult parseResult(Result rawResult) {
		return ResultParser.parseResult(rawResult);
	}

	// 解析二维码
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;

		ResultHandler resultHandler = new ResultHandler(parseResult(rawResult));

		boolean fromLiveScan = barcode != null;
		if (barcode == null) {
			Log.i("steven", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
			Log.i("steven", "resultHandler.getType().toString():" + resultHandler.getType().toString());
			Log.i("steven", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());
		} else {
			showDialog(resultHandler.getDisplayContents().toString());
		}
	}

	// 初始化照相机，CaptureActivityHandler解码
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}
	//打开扫描器失败提示
	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.confirm, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
//
//	@Override
//	public void onNetWorkResult(Message msg, String url, String result, NetWorkHandler handler) {
//		// TODO Auto-generated method stub
//		super.onNetWorkResult(msg, url, result, handler);
//		JsonNode status = handler.getData(msg.what);
//		if (status.path("status").asInt(1) != 0) {
////			Toast(status.path("msg").asText(""));
//			new AlertDialog.Builder(CaptureActivity.this).setTitle(status.path("msg").asText("")).
//			setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
//				}
//			}).show();
//			return;
//		}
//		new AlertDialog.Builder(CaptureActivity.this).setTitle("商品添加成功!").
//		setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
////				dialog.dismiss();
//			}
//		}).show();
//	}

//	@Override
//	public void onNetWorkError(Message msg, int errorCode, String error, String result) {
//		// TODO Auto-generated method stub
//		super.onNetWorkError(msg, errorCode, error, result);
//		new AlertDialog.Builder(CaptureActivity.this).setTitle("网络异常，商品添加失败!").
//		setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
////				dialog.dismiss();
//				finish();
//			}
//		}).show();
//	}

	@Override
	public void onClick(View v) {
		// Auto-generated method stub
		LayoutInflater flat = LayoutInflater.from(mContext);
		// 将R.layout.quake_details填充到Layout
		View layout_exp = flat.inflate(R.layout.dialog_text, null);
		TextView expl = (TextView) layout_exp.findViewById(R.id.tv_msg);
		expl.setText(getFromAssets("guid_capture.txt"));

		new AlertDialog.Builder(mContext).setTitle("使用说明").setView(layout_exp)
				.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}
	// 从assets 文件夹中获取文件并读取数据
			public String getFromAssets(String fileName) {
				String result = "";
				try {
					InputStream in = getResources().getAssets().open(fileName);
					// 获取文件的字节数
					int lenght = in.available();
					// 创建byte数组
					byte[] buffer = new byte[lenght];
					// 将文件中的数据读到byte数组中
					in.read(buffer);
					// result = EncodingUtils.getString(buffer, "UTF-8");
					result = new String(buffer, "GB2312");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
}
