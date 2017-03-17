
package com.shssjk.model;

/**
 * @author
 *
 */
public interface Model {

	/**
	 * 
	* @Description: 返回用实体属性与之对于的json属性 
	* @return    设定文件 
	* @return String[] 属性i为实体属性, i+i 为json对应的属性 
	* @throws
	 */
	public String[] replaceKeyFromPropertyName() ;
}
