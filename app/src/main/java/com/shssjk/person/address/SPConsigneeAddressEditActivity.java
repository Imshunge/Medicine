package com.shssjk.person.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.dao.SPPersonDao;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.utils.SMobileLog;
import com.shssjk.view.SwitchButton;
import com.soubao.tpshop.utils.SPStringUtils;

public class SPConsigneeAddressEditActivity extends BaseActivity implements View.OnClickListener{

    private String TAG = "SPConsigneeAddressEditActivity";
    private SPConsigneeAddress consignee;
    private SPConsigneeAddress selectRegionConsignee;

    //收货人姓名
    EditText consigneeEdtv;

    //收货人电话
    EditText mobileEdtv;

    //收货地址
    TextView regionTxtv;

    //收货地址
   EditText addressEdtv;
       //默认地址开关
    SwitchButton setdefaultSth;

    Button submitBtn;
    String fullRegion;
    Context mContext;

//    标题
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_consignee));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spconsignee_address_edit);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
        //       标题
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(mContext, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        titleTxtv.setText(getString(R.string.title_consignee_list));


        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
        //收货人姓名
         consigneeEdtv= (EditText) findViewById(R.id.consignee_name_edtv);

        //收货人电话
         mobileEdtv= (EditText) findViewById(R.id.consignee_mobile_edtv);;

        //收货地址
        regionTxtv= (TextView) findViewById(R.id.consignee_region_txtv);;

        //收货地址
        addressEdtv= (EditText) findViewById(R.id.consignee_address_edtv);

        submitBtn= (Button) findViewById(R.id.submit_btn);
        //默认地址开关
         setdefaultSth  = (SwitchButton) findViewById(R.id.consignee_setdefault_sth);
        ;setdefaultSth.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void onChange(SwitchButton sb, boolean state) {
                if (state) {
                    consignee.setIsDefault("1");
                } else {
                    consignee.setIsDefault("0");
                }
            }
        });
    }

    @Override
    public void initData() {
        if(getIntent()!=null && getIntent().getSerializableExtra("consignee") != null){
            consignee = (SPConsigneeAddress)getIntent().getSerializableExtra("consignee");
        }

        if (consignee == null){
            consignee = new SPConsigneeAddress();
            consignee.setIsDefault("0");
        }else{
            consigneeEdtv.setText(consignee.getConsignee());
            mobileEdtv.setText(consignee.getMobile());
            addressEdtv.setText(consignee.getAddress());
            if ("1".equals(consignee.getIsDefault())){
                setdefaultSth.setSwitchOn(true);
            }else{
                setdefaultSth.setSwitchOn(false);
            }
            String firstPart = SPPersonDao.getInstance(this).queryFirstRegion(consignee.getProvince() , consignee.getCity() , consignee.getDistrict() , consignee.getTown());
            regionTxtv.setText(firstPart);
        }
    }

    @Override
    public void initEvent() {
        submitBtn.setOnClickListener(this);
        regionTxtv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address_btn :
            case  R.id.submit_btn :
                if (!checkData()){
                    return;
               }
                RequestParams params = new RequestParams();
                params.put("consignee" , consignee.getConsignee());
                params.put("province" , consignee.getProvince());
                params.put("city" , consignee.getCity());
                params.put("district" , consignee.getDistrict());
                params.put("street" , consignee.getTown());
                params.put("address" , consignee.getAddress());
                params.put("mobile" , consignee.getMobile());
                params.put("is_default" , consignee.getIsDefault());

                if (!SPStringUtils.isEmpty(consignee.getAddressID())) {//编辑
                    params.put("address_id" , consignee.getAddressID());
                }
                showLoadingToast("正在保存数据");
                SPPersonRequest.saveUserAddressWithParameter(params, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        showToast(msg);
                        setResult(MobileConstants.Result_Code_Refresh);
                        SPConsigneeAddressEditActivity.this.finish();
                    }
                }, new SPFailuredListener(SPConsigneeAddressEditActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

                break;
            case R.id.consignee_region_txtv :
                Intent regionIntent = new Intent(this, SPCitySelectActivity.class);
                regionIntent.putExtra("consignee" , consignee);
                startActivityForResult(regionIntent , MobileConstants.Result_Code_GetValue);
                break;

        }
    }
    //是否为空数据
    private boolean checkData(){
        if (SPStringUtils.isEmpty(consigneeEdtv.getText().toString())){
            showToast("请输入收货人");
            return false;
        }
        consignee.setConsignee(consigneeEdtv.getText().toString());

        if (SPStringUtils.isEmpty(mobileEdtv.getText().toString())){
            showToast("请输入联系方式");
            return false;
        }
        consignee.setMobile(mobileEdtv.getText().toString());

        if (SPStringUtils.isEmpty(addressEdtv.getText().toString())){
            showToast("请输入详细地址");
            return false;
        }
        consignee.setAddress(addressEdtv.getText().toString());

        return true;
    }
    @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MobileConstants.Result_Code_GetValue){
            if (data==null || data.getSerializableExtra("consignee") == null){
                return;
            }
            selectRegionConsignee = (SPConsigneeAddress)data.getSerializableExtra("consignee");
            fullRegion = selectRegionConsignee.getProvinceLabel() + selectRegionConsignee.getCityLabel() + selectRegionConsignee.getDistrictLabel() + selectRegionConsignee.getTownLabel();
            regionTxtv.setText(fullRegion);
            consignee.setProvince(selectRegionConsignee.getProvince());
            consignee.setCity(selectRegionConsignee.getCity());
            consignee.setDistrict(selectRegionConsignee.getDistrict());
            consignee.setTown(selectRegionConsignee.getTown());
            SMobileLog.i(TAG, " province : " + selectRegionConsignee.getProvinceLabel() + " " + selectRegionConsignee.getCityLabel() + " " + selectRegionConsignee.getCityLabel() + " " + selectRegionConsignee.getTownLabel());
        }
    }
}
