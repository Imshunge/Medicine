package com.shssjk.activity.common.person;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.view.SPMoreImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    Spinner sexSpi;
    TextView ageTxt;

    SPMoreImageView headImage;

    Button btnSave;

    SPUser mUser = null;
    private String[] sexA = null;
    private String strSex;
    long birthday = 0;
    Calendar calendar;
    Bitmap mBitmap;
   private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_user_info));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mContext=this;
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
        sexSpi = (Spinner) findViewById(R.id.sex_spinner);
        ageTxt = (TextView) findViewById(R.id.age_txtv);
        headImage = (SPMoreImageView) findViewById(R.id.head_mimgv);

        btnSave = (Button) findViewById(R.id.btn_save);
    }
    @Override
    public void initData() {
        sexA = getResources().getStringArray(R.array.user_sex_name);
        mUser = MobileApplication.getInstance().getLoginUser();
        if (mUser != null) {
            phoneNum.setText(mUser.getMobile());
//            try {
//                birthday = Long.parseLong(mUser.getBirthday() == null ? "0" : mUser.getBirthday());
//                calendar = Calendar.getInstance();
////                if (birthday != 0) {
////                    calendar.setTimeInMillis(birthday);
////                }
//
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
            ageTxt.setText(mUser.getBirthday());
            strSex = mUser.getSex();

            //        民族
            for (int i = 0; i < sexSpi.getCount(); i++) {
                if (sexSpi.getItemAtPosition(i).toString().trim().equals(mUser.getSex())) {
                    sexSpi.setSelection(i);
                } else {
                }
            }



//            sexTxt.setText(sexA[stringToInt(mUser.getSex(), 0)]);
            nickName.setText(mUser.getNickname());
            path= Environment.getExternalStorageDirectory().getPath();
            //showToast(path);
            mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");// 从sdcard中获取本地图片,通过BitmapFactory解码,转成bitmap
            if (mBitmap != null) {
                @SuppressWarnings("deprecation")
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), mBitmap);
                circularBitmapDrawable.setCornerRadius(getResources().getDimension(R.dimen.head_corner_35));
                headImage.setImageDrawable(circularBitmapDrawable);

            } else {
                /** 从服务器取,同时保存在本地 ,后续的工作 */
                if (MobileApplication.getInstance().isLogined){
                    SPUser    spUser=   MobileApplication.getInstance().getLoginUser();
                    String url = MobileConstants.BASE_HOST+ MobileApplication.getInstance().getLoginUser().getHeadPic();
                    Glide.with(mContext).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(headImage);
                }
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
//                Intent ageIntent = new Intent(this, SPPopuListViewActivity.class);
//                ageIntent.putExtra("data", sexA);
//                ageIntent.putExtra("defaultIndex", stringToInt(mUser.getSex(), 0));
//                startActivityForResult(ageIntent, 102);
                break;
            case R.id.nickname_txtv:
//                Intent invokeIntent = new Intent(this, SPTextFieldViewActivity.class);
//                invokeIntent.putExtra("value", mUser.getNickname());
//                startActivityForResult(invokeIntent, 101);
                break;
            case R.id.age_txtv:
                showDateDialog();
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
                    Intent intent_pat = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent_pat.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "head.jpg")));
                    startActivityForResult(intent_pat, REQUEST_CODE_CAMERA);
                } else if (item == 1) {
                    /*
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 150);
                    try{
                        intent.putExtra("return-data", true);
                        startActivityForResult(Intent.createChooser(intent, "Select File"),104);
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                    Intent intent_photo = new Intent(Intent.ACTION_PICK, null);
                    intent_photo
                            .setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                    startActivityForResult(intent_photo, REQUEST_CODE_PHOTO);
                }
            }
        });
        builder.show();
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
                        headImage.setImageBitmap(mBitmap);// 显示
                         updateUserHeader(mBitmap);
                    }
                }
                break;
        }

    }

    /**
     * 上传头像
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
                showToast(msg);
                finish();
            }
        }, new SPFailuredListener(UserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                finish();
            }
        });
    }

    private String path =  "/sdcard/headPhoto";// 存放本地的头像照片
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
     *
     * @Title: shearPhoto
     * @Description:直接调用系统的剪切功能
     * @param: @param uri
     * @return: void
     * @throws
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
        String nickNameStr = nickName.getText().toString();
//        String sex = strSex;//sexTxt.getText().toString();
        String sex = sexSpi.getSelectedItem().toString();;//sexTxt.getText().toString();

        String age = ageTxt.getText().toString() + ""; //ageTxt.getText().toString();
        SPUser user = mUser;
        user.setNickname(nickNameStr);
        user.setSex(sex);
        user.setBirthday(age);
        SPUserRequest.updateUserInfo(user, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showToast(msg);
                finish();
            }
        }, new SPFailuredListener(UserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                finish();
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


}
