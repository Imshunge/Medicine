package com.shssjk.activity.common.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.soubao.tpshop.utils.SPStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 添加门派
 */
public class AddSchoolActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView;    //图片
    private EditText editTextTitle;  //标题
    private EditText editTextContent;//内容
    private Button rithtBtn;
    private String cid;     //门派类别
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "添加门派", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_com_school);
        super.init();
    }

    @Override
    public void initSubViews() {
        this.cid= getIntent().getStringExtra("category_id");
        imageView = (ImageView) findViewById(R.id.articl_image);
        editTextTitle = (EditText) findViewById(R.id.tv_content_title);
        editTextContent = (EditText) findViewById(R.id.tv_content);
        rithtBtn = (Button) findViewById(R.id.titlebar_menu_btn);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        imageView.setOnClickListener(this);
        rithtBtn.setOnClickListener(this);

        editTextTitle.addTextChangedListener(watcher);//

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.articl_image:
                selectImage();
                break;
            case R.id.titlebar_menu_btn:
                    /** 上传到服务器 --- */
                    if (SPStringUtils.isEditEmpty(editTextTitle)) {
                        editTextTitle.setError(Html.fromHtml("<font color='red'>"+getString(R.string.jh_menpai_name_null)+"</font>"));
                        return;
                    }
                    if (SPStringUtils.isEditEmpty(editTextContent)) {
                        editTextContent.setError(Html.fromHtml("<font color='red'>"+getString(R.string.jh_menpai_logo_null)+"</font>"));
                        return;
                    }
                    if (mBitmap ==null) {
                        showToast("请选择图片");
                        return;
                    }
//                    分类id
                    addSchooll(mBitmap,cid,editTextTitle.getText().toString().trim(),editTextContent.getText().toString().trim());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


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
                        imageView.setImageBitmap(mBitmap);// 显示
//                        addSchooll(mBitmap);
                    }
                }
                break;
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
        imageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
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
     * 上传头像
     * cid （门派类别id）、title （门派名称）、logo（门派logo）、remark（门派描述）
     *
     * @param mBitmap
     */
    private void addSchooll(Bitmap mBitmap,String cid,String title,String remark) {
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

        CommunityRequest.CompuPlishSchool(cid, title, remark, header, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showToast(msg);
                finish();
            }
        }, new SPFailuredListener(AddSchoolActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                finish();
            }
        });
    }
    TextWatcher watcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
//            mSendComTxtv.setBackgroundColor(MyColor.COLOR7);
//            mSendComTxtv.setTextColor(MyColor.WHITE);
            // Auto-generated method stub
            String.valueOf(8 - editTextTitle.length());
            if(editTextTitle.length()>8){
                showToast("门派标题不能超过8个字");
                editTextTitle.setText("");
            }
        }
    };

}
