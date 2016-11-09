package com.shssjk.http.condition;

/**
 * @author
 *
 */
public class ProductCondition extends Condition {

	public int categoryID = -1 ;	//分类ID
	public String goodsName;	//分类ID
	public String orderdesc;	//排序方式: desc , asc
	public String orderby;		//排序字段
	public int goodsID;			//商品ID
	public String href;			//请求URL
}
