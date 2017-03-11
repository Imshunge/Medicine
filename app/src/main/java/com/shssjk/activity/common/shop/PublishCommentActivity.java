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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.model.shop.CommentCondition;
import com.shssjk.model.shop.Image;
import com.shssjk.model.shop.ProductCommnet;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.StarView;

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

//发表评论

public class PublishCommentActivity extends BaseActivity {
    public static final int REQUEST_CODE_CHOOSE = 1;
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
    @Bind(R.id.et_image_detail_description)
    EditText etImageDetailDescription;
    @Bind(R.id.comment_star_des)
    StarView commentStarDes;
    @Bind(R.id.comment_star_quality)
    StarView commentStarQuality;
    @Bind(R.id.comment_star_logistics)
    StarView commentStarLogistics;
    @Bind(R.id.ivbtn_add)
    ImageView ivbtnAdd;
    @Bind(R.id.product_list_gallery_lyaout)
    LinearLayout productListGalleryLyaout;
    @Bind(R.id.order_product_scrollv)
    HorizontalScrollView orderProductScrollv;
    @Bind(R.id.ll_image_container)
    LinearLayout llImageContainer;
    @Bind(R.id.comnnent_btn)
    Button couponUseBtn;
    private List<Uri> mSelected;
    ProductCommnet mOrder;
    private Context mContext;
    private CommentCondition commentCondition    = new CommentCondition();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_goods_comment));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comment);
        ButterKnife.bind(this);
        mContext=this;
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
        String imgUrlheader = MobileConstants.BASE_HOST+mOrder.getOriginalImg();
        Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picImgv);
        nameTxtv.setText( mOrder.getGoodsName());
        numTxtv.setText("x" +mOrder.getGoodsNum());
        shopPriceTxtv.setText("¥"+mOrder.getShippingPrice());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            List<Image> list = new ArrayList<Image>();
            List<File> fileslist = new ArrayList<File>();

            Image image = null;
            for (Uri u : mSelected) {
                Log.e("picture", u.getPath());

                image = new Image("content://media" + u.getPath(), 50, 50);
                list.add(image);

            }
            displayImg(list);





        }
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

    @OnClick({R.id.ivbtn_add, R.id.comnnent_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivbtn_add:
                addEvent();
                break;
            case R.id.comnnent_btn:
                uploadData();
                break;
        }
    }

    private void uploadData() {
        String content=etImageDetailDescription.getText().toString().trim();
        if(SSUtils.isEmpty(content)){
            showToast("请填写评价内容");
            return;
        }
        int commentDes=    commentStarDes.getRank();
        int quality=    commentStarQuality.getRank();
        int starLogistics=    commentStarLogistics.getRank();
        commentCondition.setGoods_id(mOrder.getGoodsId());
        commentCondition.setOrder_id(mOrder.getOrderId());

        commentCondition.setContent(content);
        commentCondition.setService_rank(quality+"");
        commentCondition.setDeliver_rank(starLogistics+"");
        commentCondition.setGoods_rank(commentDes+"");
        SPPersonRequest.commentGoodsWithGoodsID(commentCondition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if("评论成功".equals(msg)){
                    PublishCommentActivity.this.setResult(RESULT_OK);
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


}
