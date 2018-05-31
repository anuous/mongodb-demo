package com.example.mongodbdemo.mongodb.utils;


import java.util.UUID;

/**
 * 系统名称：小事儿
 * 模块名称：
 * 模块描述：
 * 功能列表：
 * 模块作者：daizhiqing
 * 开发时间：2015年10月31日 上午11:53:21
 * 模块路径：com.xiao4r.mongo.utils.UUIDUtils
 * 更新记录：
 */
public class UUIDUtils {
	
	/**
	 * 设置图片前缀
	 */
	public static String PRE = "mg_";
	/**
	 * 
	 * 通过jdk自带的uuid生成器生成36位的uuid；
	 * @author daizhiqing
	 * @date 2012-10-17 上午11:43:55
	 */
	public static String getFileUUID() {
		// 使用JDK自带的UUID生成器
		return PRE+UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
