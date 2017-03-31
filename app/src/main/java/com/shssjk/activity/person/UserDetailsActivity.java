package com.shssjk.activity.person;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.model.person.UploadPic;
import com.shssjk.utils.DatePickerUtil;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.GlideCircleTransform;
import com.shssjk.view.SPMoreImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 用户详情信息
 */
public class UserDetailsActivity extends BaseActivity implements View.OnClickListener {
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题

    TextView phoneNum;  //电话
    EditText nickName;
    TextView sexSpi;
    TextView ageTxt;
    ImageView headImage;
    Button btnSave;
    SPUser mUser = null;
    private String[] sexA = null;
    private String strSex;
    long birthday = 0;
    Calendar calendar;
    Bitmap mBitmap;
    private Context mContext;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private DatePickerUtil datePickDialog;
    private static final int MY_PERMISSIONS_REQUEST_CHOSE_PHOTO = 7;
    private static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 6;
    String[] sexArry = new String[] {"保密" ,"男", "女" };//性别选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_user_info));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));
//            个人信息
        phoneNum = (TextView) findViewById(R.id.phone_num_editx);
        nickName = (EditText) findViewById(R.id.nickname_editx);
        sexSpi = (TextView) findViewById(R.id.sex_spinner);
        ageTxt = (TextView) findViewById(R.id.age_txtv);
        headImage = (ImageView) findViewById(R.id.head_mimgv);
        btnSave = (Button) findViewById(R.id.btn_save);
    }
    @Override
    public void initData() {
        sexA = getResources().getStringArray(R.array.user_sex_name);
        mUser = MobileApplication.getInstance().getLoginUser();
        if (mUser != null) {
            phoneNum.setText(mUser.getUserID());
            ageTxt.setText(mUser.getBirthday());
            strSex = mUser.getSex();
            String str =mUser.getBirthday();
            Logger.e("str ",str);
            if (!SSUtils.isEmpty(strSex)) {
                int index = SSUtils.str2Int(strSex);
                sexSpi.setText(sexArry[index]);
            }
             if(!TextUtils.isEmpty(mUser.getNickname()))   {
                 nickName.setText(mUser.getNickname());
             }else{
                 if(SSUtils.isNumber(mUser.getMobile())){
                     nickName.setText(SSUtils.userNameReplaceWithStar(mUser.getMobile()));
                 }else{
                     nickName.setText("");
                 }
             }
            if (MobileApplication.getInstance().isLogined){
                String url = MobileConstants.BASE_HOST+ mUser.getHeader_pic();
                Glide.with(this)
                        .load(url).transform(new GlideCircleTransform(mContext)).
                        into(headImage);
            }else{
                Glide.with(this)
                        .load(R.drawable.person_default_head).transform(new GlideCircleTransform(mContext)).
                        into(headImage);
            }
        }
    }

    @Override
    public void initEvent() {
        ageTxt.setOnClickListener(this);
//        sexTxt.setOnClickListener(this);
        nickName.setOnClickListener(this);
        headImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        sexSpi.setOnClickListener(this);
    }
    private int stringToInt(String str, int defaultValue) {
        int res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex_spinner:
                showChooseDialog();
                break;
            case R.id.nickname_txtv:
                break;
            case R.id.age_txtv:
//                showDateDialog();
                format = new SimpleDateFormat("yyyy年MM月dd日");
                String dateTime = format.format(System.currentTimeMillis());
                datePickDialog = new DatePickerUtil(this, dateTime);
                datePickDialog.dateTimePicKDialog(ageTxt, 0);
                break;
            case R.id.head_mimgv:
                selectImage();
                //Crop.pickImage(this);
                break;
            case R.id.btn_save:
                updateUserInfo();
                break;
        }
    }
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    /* 性别选择框 */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中
            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
//                showToast(which+"");
                sexSpi.setText(sexArry[which]);
                dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder.show();// 让弹出框显示
    }
    private void selectImage() {
        final String[] items = getResources().getStringArray(R.array.user_head_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.user_head_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = Utility.checkPermission(MainActivity.this);
                if (item == 0) {
                    /*
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                    // ******** code for crop image
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 150);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 103);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                    }*/
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_TAKE_PHOTO);

                    } else {
                        takePhoto();
                    }
//                    takePhoto();
                } else if (item == 1) {
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CHOSE_PHOTO);
                    } else {
                        chosePhpto();
                    }
                }
            }
        });
        builder.show();
    }

    private void chosePhpto() {
        Intent intent_photo = new Intent(Intent.ACTION_PICK, null);
        intent_photo
                .setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
        startActivityForResult(intent_photo, REQUEST_CODE_PHOTO);
    }

    private void takePhoto() {
        Intent intent_pat = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_pat.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                .fromFile(new File(Environment
                        .getExternalStorageDirectory(), "head.jpg")));
        startActivityForResult(intent_pat, REQUEST_CODE_CAMERA);
    }

    private void showDateDialog() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            if (birthday != 0) {
                calendar.setTimeInMillis(birthday);
            }
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                ageTxt.setText(getString(R.string.user_age_format, year, month + 1, day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == MobileConstants.Result_Code_GetValue) {
                    nickName.setText(data.getStringExtra("value"));
                }
                break;
            case 102:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra("index", 0);
                    strSex = "" + index;
//                    sexTxt.setText(sexA[index]);
                }
                break;
            case 103:
                onCaptureImageResult(data);
                break;
            case 104:
                onSelectFromGalleryResult(data);
                break;
            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK) {
                    shearPhoto(data.getData());// 剪切图片
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    File fileTemp = new File(
                            Environment.getExternalStorageDirectory() + "/head.jpg");
                    shearPhoto(Uri.fromFile(fileTemp));// 剪切图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    mBitmap = mBundle.getParcelable("data");
                    if (mBitmap != null) {
                        /** 上传到服务器 待--- */
                        setPictureToSD(mBitmap);// 保存本地
//                        headImage.setImageBitmap(mBitmap);// 显示


                        updateUserHeader(mBitmap);
                    }
                }
                break;
        }
    }

    /**
     * 上传头像
     *
     * @param mBitmap
     */
    private void updateUserHeader(Bitmap mBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File header = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            header.createNewFile();
            fo = new FileOutputStream(header);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SPUserRequest.uploadHeader(header, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                UploadPic uploadPic = (UploadPic) response;
                SPUser user = MobileApplication.getInstance().getLoginUser();
                if (!SSUtils.isEmpty(user.getHeader_pic())) {
                    user.setHeader_pic(uploadPic.getPath());
                    SPSaveData.putValue(mContext, "header_pic", uploadPic.getPath());
                    String url = MobileConstants.BASE_HOST+ uploadPic.getPath();
                    Glide.with(mContext)
                            .load(url).transform(new GlideCircleTransform(mContext)).
                            into(headImage);
                }
                showToast(msg);
            }
        }, new SPFailuredListener(UserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
//                finish();
            }
        });
    }

    private String path = "/sdcard/headPhoto";// 存放本地的头像照片

    private void setPictureToSD(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {// 检测SD卡的可用性
            return;
        }
        FileOutputStream fos = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "/head.jpg";// 图片名字
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩后写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @throws
     * @Title: shearPhoto
     * @Description:直接调用系统的剪切功能
     * @param: @param uri
     * @return: void
     */
    private void shearPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void updateUserInfo() {
        final String nickNameStr = nickName.getText().toString();
        String sex = sexSpi.getText().toString();//sexTxt.getText().toString();
//        0 保密， 1男 ，2 女
        if (!SSUtils.isEmpty(sex)) {
            switch (sex) {
                case "保密":
                    sex = "0";
                    break;
                case "男":
                    sex = "1";
                    break;
                case "女":
                    sex = "2";
                    break;
                default:
                    sex = "0";
                    break;
            }
        }
        final String age = ageTxt.getText().toString() + ""; //ageTxt.getText().toString();
        SPUser user = mUser;
        user.setNickname(nickNameStr);
        user.setSex(sex);
        user.setBirthday(age);
        final String finalSex = sex;
        SPUserRequest.updateUserInfo(user, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                int status = (int) response;
                if (status == 1) {
                    SPSaveData.putValue(mContext, "birthday", age);
                    SPSaveData.putValue(mContext, "nickName", nickNameStr);
                    SPSaveData.putValue(mContext, "sex", finalSex);
                    setResult(RESULT_OK);
                    finish();
                } else {

                }
                showToast(msg);
            }
        }, new SPFailuredListener(UserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
//                finish();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImage.setImageBitmap(photo);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        Bitmap thumbnail = data.getExtras().getParcelable("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headImage.setImageBitmap(thumbnail);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CHOSE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chosePhpto();
            } else {
                // Permission Denied
                showToast("禁止选择图片将会导致上传图片功能异常!");
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
                showToast("禁止使用相机权限将会导致上传图片功能异常!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
