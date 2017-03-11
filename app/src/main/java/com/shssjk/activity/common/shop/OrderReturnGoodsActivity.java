package com.shssjk.activity.common.shop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.shop.CommentCondition;
import com.shssjk.model.shop.Image;
import com.shssjk.model.shop.ProductCommnet;
import com.shssjk.utils.SSUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.ImageLoaderEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class OrderReturnGoodsActivity extends BaseActivity {
    ProductCommnet mOrder;
    @Bind(R.id.position)
    TextView position;
    @Bind(R.id.pic_imgv)
    ImageView picImgv;
    @Bind(R.id.name_txtv)
    TextView nameTxtv;
    @Bind(R.id.shop_price_txtv)
    TextView shopPriceTxtv;
    @Bind(R.id.num_txtv)
    TextView numTxtv;
    @Bind(R.id.swipe)
    FrameLayout swipe;
    @Bind(R.id.ivbtn_add)
    ImageView ivbtnAdd;
    @Bind(R.id.product_list_gallery_lyaout)
    LinearLayout productListGalleryLyaout;
    @Bind(R.id.order_product_scrollv)
    HorizontalScrollView orderProductScrollv;
    @Bind(R.id.ll_image_container)
    LinearLayout llImageContainer;
    @Bind(R.id.add_btn)
    Button addBtn;
    @Bind(R.id.et_description)
    EditText etDescription;
    private Context mContext;
    public static final int REQUEST_CODE_CHOOSE = 1;
    private CommentCondition commentCondition    = new CommentCondition();
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.activity_order_returned));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_return_goods);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        if (getIntent() == null || getIntent().getSerializableExtra("order") == null) {
            showToast(getString(R.string.toast_no_data));
            this.finish();
            return;
        }
        mOrder = (ProductCommnet) getIntent().getSerializableExtra("order");
        showData(mOrder);
    }

    private void showData(ProductCommnet mOrder) {
        String imgUrlheader = MobileConstants.BASE_HOST + mOrder.getOriginalImg();
        Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picImgv);
        nameTxtv.setText(mOrder.getGoodsName());
        numTxtv.setText("x" + mOrder.getGoodsNum());
        shopPriceTxtv.setText("¥" + mOrder.getShippingPrice());

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.ivbtn_add, R.id.add_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivbtn_add:
                addEvent();
                break;
            case R.id.add_btn:
                uploadData();
                break;
        }
    }

    /**
     * 选择图片
     */
    private void addEvent() {
        Picker.from(this)
                .count(4)
                .enableCamera(true)
                .setEngine(new GlideEngine())
                .setEngine(new ImageLoaderEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    private void uploadData() {
        String content=etDescription.getText().toString().trim();
        if(SSUtils.isEmpty(content)){
            showToast("请填写问题描述内容");
            return;
        }

        commentCondition.setGoods_id(mOrder.getGoodsId());
        commentCondition.setOrder_id(mOrder.getOrderId());
        String sss=mOrder.getOrderSn();
        commentCondition.setOrder_sn(mOrder.getOrderSn());

        commentCondition.setContent(content);
        commentCondition.setType("0");
        SPPersonRequest.exchangeApplyWithParameter(commentCondition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                int result =(int) response;

                if (1==result) {
                    OrderReturnGoodsActivity.this.setResult(RESULT_OK);
                    finish();
                }
                showToast(msg);
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });

    }
    private void displayImg(List<Image> products) {
        for (int i = 0; i < products.size(); i++){
            String url = products.get(i).getUrl();
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, productListGalleryLyaout, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            productListGalleryLyaout.addView(view);
        }

//保存图片路径
        List<File> fileList = new ArrayList<File>();
        for (int i = 0; i < products.size(); i++) {
            Uri uri =  Uri.parse(products.get(i).getUrl());
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                return;
            }
            cursor.moveToFirst();
            File file = new File(cursor.getString(1));
            if (file.exists()) {
                fileList.add(file);
            } else {
//                Toast.makeText(this, "文件不存在:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        }
        if (fileList.size() > 0) {
            commentCondition.setImages(fileList);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            List<Image> list = new ArrayList<Image>();
            List<File> fileslist = new ArrayList<File>();

            Image image = null;
            for (Uri u : mSelected) {

                image = new Image("content://media" + u.getPath(), 50, 50);
                list.add(image);

            }
            displayImg(list);

        }
    }
}
