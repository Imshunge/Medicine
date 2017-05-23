package com.shssjk.activity.shop;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;

import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;

import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.SSUtils;

import org.json.JSONObject;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static com.shssjk.utils.SSUtils.getFromAssets;


/**
 * 描述: 扫描2维码界面
 */
public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {


    private static final float BEEP_VOLUME = 0.50f;
    private static final int NUMBER = 1;

    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;

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
    /**
     * Called when the activity is first created.
     */
    private TextView tv_titlebar_right;
    private Button rightBtn;
    private Context mContext;
    private QRCodeView mQRCodeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.activity_catureactivity_title), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captureactivity);
        mContext=this;
        // 初始化
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
        super.init();
    }

    @Override
    public void initSubViews() {
        tv_titlebar_right = (TextView) findViewById(R.id.ritht_title_txtv);
        rightBtn = (Button) findViewById(R.id.titlebar_menu_btn);
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
//            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
//            CameraManager.get().offLight();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();

        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        showDialog(result);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast("打开相机出错!");
    }
    private void addGoodsCarbyId(String goodsid, String spc, int number) {
        ShopRequest.shopCartGoodsOperation(goodsid, spc, number, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    showToast(msg);
                    mQRCodeView.startSpot();
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private static final long VIBRATE_DURATION = 200L;


    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void getGoodsspec(final int goodsId) {
        specJson = new JSONObject();//清理之前的缓存数据
        ProductCondition condition = new ProductCondition();
        condition.goodsID = goodsId;
        ShopRequest.getProductByID(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                try {
                    mDataJson = (JSONObject) response;
                    if (mDataJson != null && mDataJson.has("product")) {
                        SPProduct mProduct = (SPProduct) mDataJson.get("product");
                        List<SPProductSpec> specList = null;
                        if (mProduct != null) {
                            if (mProduct.getSpecArr() != null && mProduct.getSpecArr().size() > 0) {
                                //排序
                                specList = mProduct.getSpecArr();
                                if (specList.size() >= 0) {
                                    addGoodsCarbyId(goodsId + "", specList.get(0).getItemID(), NUMBER);
                                } else {
                                    showToast("此商品无法添加到购物车");
                                }
                            }
                        }else{
                            showToast("此商品无法添加到购物车");
                        }
                        if (mProduct.getSpecArr() != null) {

                        }
//                        showToast("此商品无法添加到购物车");
                        mQRCodeView.startSpot();
                    }

                } catch (Exception e) {
                    showToast(e.getMessage());
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
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
    public void showDialog(String msg) {
        int goodsID = 0;
        String goodsid="";
        String[] strarray1 = new String[0];
        boolean isUrl=false;
//        判断是否为URL类型
        if (Patterns.WEB_URL.matcher(msg).matches()) {
            //符合标准
            isUrl=true;
        } else{
            //不符合标准
            isUrl=false;
        }
        if(isUrl){
            strarray1 = new String[]{"", "该商品"};
        }else{

            String[] strarray = msg.split("[&]");
            strarray1 = strarray[0].split("[=]");
        }
        goodsid = SSUtils.getNumbers(msg);
        goodsID = SSUtils.str2Int(goodsid);
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        if(strarray1.length>=2){
            builder.setMessage(String.format("是否添加:%1$s 到购物车?", strarray1[1]));
        }else {
            builder.setMessage("是否添加到购物车?");
        }
        builder.setTitle("系统提示");
        final int finalGoodsID = goodsID;
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getGoodsspec(finalGoodsID);
            }
        });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mQRCodeView.startSpot();
                    }
                });
        builder.create().show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ritht_title_txtv:
                showConfirmDialog(getFromAssets(("guid_capture.txt"), mContext),"使用说明");
//                showDialog("http://www.shssjk.com/index.php/Mobile/Goods/goodsInfo/id/333.html");
//                showDialog("title=猴头菇&goodid=333");
               break;
        }
    }
}