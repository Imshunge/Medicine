
package com.shssjk.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shssjk.activity.R;
import com.shssjk.activity.common.IViewController;
import com.shssjk.activity.common.health.BindDeviceActivity;
import com.shssjk.activity.common.shop.ProductListActivity;
import com.shssjk.activity.common.user.LoginActivity;
import com.shssjk.global.MobileApplication;
import com.shssjk.utils.SMobileLog;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.SPDialogUtils;
import com.shssjk.utils.SPLoadingDialog;
import com.shssjk.utils.SSUtils;


import org.json.JSONObject;


/**
 * @author admin
 *
 */
public abstract class BaseFragment extends Fragment implements IViewController {

	SPLoadingDialog mLoadingDialog;
	JSONObject mDataJson;
	private String categoryId="850";

	/**
	 * 跳转登录界面
	 */
	public void gotoLogin(){

	}

	public void init(View view){
		initSubView(view);
		initEvent();
		initData();
	}

	/**
	 * 取消网络请求
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param obj    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void cancelRequest(Object obj){

	}

	public void showToast(String msg){

		SPDialogUtils.showToast(getActivity(), msg);
	}

	public void showToastUnLogin(){

		showToast(getString(R.string.toast_person_unlogin));
	}
	public void  toLoginPage(){
		Intent loginIntent = new Intent(getActivity() , LoginActivity.class);
		getActivity().startActivity(loginIntent);
	}

	public void showLoadingToast(){
		showLoadingToast(null);
	}

		public void showLoadingToast(String title){
		mLoadingDialog = new SPLoadingDialog(getActivity() , title);
		mLoadingDialog.setCanceledOnTouchOutside(false);
		mLoadingDialog.show();
	}

	public void hideLoadingToast(){
		if(mLoadingDialog !=null){
			mLoadingDialog.dismiss();
		}
	}

	public void showConfirmDialog(String message , String title , final ConfirmDialog.ConfirmDialogListener confirmDialogListener , final int actionType){
		ConfirmDialog.Builder builder = new ConfirmDialog.Builder(getActivity());
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
			}
		});

		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
	/**
	 * 
	* @Description: 初始化子类视图 
	* @param view    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public abstract void initSubView(View view);
	
	public abstract void initEvent();

	public abstract void initData();

	@Override
	public void gotoLoginPage() {
		/*if (!SPStringUtils.isEmpty(msg)){
			showToast(msg);
		}*/
		SMobileLog.i("SPBaseFragment", "gotoLoginPage : " + this);
		toLoginPage();
	}
	/**
	 * 绑定设备
	 */
	public void startupBindDeviceActivity(){
		if (!MobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}
		Intent carIntent = new Intent(getActivity() , BindDeviceActivity.class);
		getActivity().startActivity(carIntent);
	}

	/**
	 * 去够购买
	 */
	public void getToBy(){
		Intent intent = new Intent(getActivity() , ProductListActivity.class);
		intent.putExtra("category", categoryId);
		getActivity().startActivity(intent);
	}

}
