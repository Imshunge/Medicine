
package com.shssjk.global;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shssjk.CrashHandler.CrashHandler;
import com.shssjk.activity.R;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.model.SPCategory;
import com.shssjk.model.SPPlugin;
import com.shssjk.model.SPServiceConfig;
import com.shssjk.model.SPUser;
import com.shssjk.model.shop.SPCollect;
import com.soubao.tpshop.utils.SPMyFileTool;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * @author
 *
 */
public class MobileApplication extends Application {
	private static MobileApplication instance ;
	public List<SPCollect> collects;
	public boolean isLogined ;
	private SPUser loginUser ;
	private String deviceId ;
	DisplayMetrics mDisplayMetrics;
	public JSONObject json;
	public JSONObject json1;
	public Map<String , String> map;
	public List list;
	public JSONArray jsonArray;
	public int productListType = 1; //1: 商品列表, 2: 产品搜索结果列表
	private long cutServiceTime;
	private List<SPCategory> topCategorys;//分类左边菜单一级分类
	private List<SPServiceConfig> serviceConfigs;
	private Map<String , SPPlugin> servicePluginMap;
	private TelephonyManager telephonyManager;
	private List<Activity> activityList = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		super.onCreate();
		//在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		/** 初始化 Vollery 网络请求 */
		SPMobileHttptRequest.init(getApplicationContext());
		/** 初始化 Facebook SimpleDraweeView 网络请求 */
		loginUser = SPSaveData.loadUser(getApplicationContext());
		if (SPStringUtils.isEmpty(loginUser.getUserID()) || loginUser.getUserID().equals("-1")){
			isLogined = false;
		}else{
			isLogined = true;
		}
		instance = this;
		//初始化购物车管理类
//		SPShopCartManager.getInstance(getApplicationContext());
		PackageManager manager = this.getPackageManager();
		mDisplayMetrics = getResources().getDisplayMetrics();
		telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		if(telephonyManager!=null){
			String deviceId = telephonyManager.getDeviceId();
			SPMyFileTool.cacheValue(this, SPMyFileTool.key1, deviceId);
	}
		initUIL();
		LitePal.initialize(this);
	}
	private void initUIL() {
		DisplayImageOptions userOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).
						showImageForEmptyUri(R.drawable.image_not_exist).
						showImageOnLoading(R.drawable.image_not_exist).
						showImageOnFail(R.drawable.image_not_exist).
						imageScaleType(ImageScaleType.EXACTLY).
						bitmapConfig(Bitmap.Config.RGB_565).
						considerExifParams(true)
				.build();
		long maxMemory = Runtime.getRuntime().maxMemory()/8;
		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "ssjk/cache/image");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2) //线程池中线程的个数，一般配置1-5，这里配的是3
				.memoryCache(new LRULimitedMemoryCache((int) maxMemory))
				.memoryCacheSize((int) maxMemory)
				.memoryCacheExtraOptions(480, 800) //即保存的每个缓存文件的最大长宽，大小自行配制
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheSize(80 * 1024 * 1024)
				.imageDownloader(new BaseImageDownloader(this, 20 * 60 * 60, 30 * 60 * 60))
				.diskCacheFileCount(100)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(userOptions)
				.build();
		ImageLoader.getInstance().init(config);
	}

	public DisplayMetrics getDisplayMetrics(){
		return mDisplayMetrics;
	}

	public static MobileApplication getInstance(){
		return instance;
	}

	public List<SPServiceConfig> getServiceConfigs() {
		return serviceConfigs;
	}

	public void setServiceConfigs(List<SPServiceConfig> serviceConfigs) {
		this.serviceConfigs = serviceConfigs;
	}

	public Map<String, SPPlugin> getServicePluginMap() {
		return servicePluginMap;
	}

	public void setServicePluginMap(Map<String, SPPlugin> servicePluginMap) {
		this.servicePluginMap = servicePluginMap;
	}
	public SPUser getLoginUser() {
		SPUser	loginUser = SPSaveData.loadUser(getApplicationContext());
		return 	loginUser ;
	}
	public void setLoginUser(SPUser loginUser) {
		this.loginUser = loginUser;
		if (this.loginUser !=null){
			SPSaveData.saveUser(getApplicationContext() , "user" ,this.loginUser);
			isLogined = true;
		}else{
			isLogined = false;
		}
	}
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	public String getSystemPackage(){
		return this.getPackageName();
	}

	//退出登录
	public void exitLogin(){
		loginUser = null;
		isLogined = false;
		SPSaveData.clearUser(getApplicationContext());

	}

	public List<SPCategory> getTopCategorys() {
		return topCategorys;
	}

	public void setTopCategorys(List<SPCategory> topCategorys) {
		this.topCategorys = topCategorys;
	}

	/**
	 * 获取设备IMEI
	 *唯一的设备ID：
	 * GSM手机的 IMEI 和 CDMA手机的 MEID.
	 * @return
	 */
	public String getDeviceId() {
		if (telephonyManager!=null){
			deviceId = telephonyManager.getDeviceId();//String
		}else{
			deviceId = "unionid001";
		}
		return deviceId;
	}

	public long getCutServiceTime() {
		return cutServiceTime;
	}

	public void setCutServiceTime(long cutServiceTime) {
		this.cutServiceTime = cutServiceTime;
	}
}
