
package com.shssjk.model;

import java.io.Serializable;

/**
 * @author 无风
 *
 */
public class SPHomeBanners implements Model, Serializable{
	
	private String adLink ;				//该商品所在链接
	private String adName ;			//商品名称
	private String adCode ;				//图片路径
	
	
	public String getAdLink() {
		return adLink;
	}
	public void setAdLink(String adLink) {
		this.adLink = adLink;
	}
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public String getAdCode() {
		return adCode;
	}
	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}
	
	@Override
	public String[] replaceKeyFromPropertyName() {
		return new String[]{
				"adLink" , "ad_link",
				"adName" , "ad_name",
				"adCode" , "ad_code",
		};
	}


}
