
package com.shssjk.model.shop;


import com.shssjk.model.Model;

/**
 * @author 飞龙
 *
 */
public class GoodsSpec implements Model {

	private String specName;	//规格大类
	private String itemID;		//规格ID
	private String item;		//规格名称
	private String src;			//图片URL, 如果该项为空, 则为文本
	
	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public String[] replaceKeyFromPropertyName() {
		return new String[]{
				"specName","spec_name",
				"itemID","item_id"
				};
	}

}
