package com.example.mongodbdemo.mongodb.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: AbstractRepository  
 * @Description: MongoDb 数据操作接口  
 * @author daizhiqing daizhiqing8@163.com
 * @date 2016年6月6日 上午11:56:59  
 *  
 * @param <T>
 */
public interface AbstractRepository<T extends Serializable> {
	
	/**
	 * 
	* @Title: insert  
	* @Description: TODO 添加对象
	* @param @param object    设定文件  
	* @return void    返回类型  
	* @throws
	 */
	public void insert(T object); 
	
	/**
	 * 
	 * @Title: findOneByID
	 * @Description: TODO 根据ID查找对象 
	 * @Return T
	 * @Data 2016年6月6日
	 * @throws
	 */
    public T findOneById(String id);   
   
    /**
     * 
     * @Title: findByRegex  
     * @Description: TODO 正则匹配
     * @param filedName 字段名
     * @param regex		匹配规则
     * @return
     * @Data 2016年6月6日 下午2:51:47
     * @throws
     */
    public List<T> findByRegex(String filedName, String regex);
    /**
     * 
     * @Title: findAll  
     * @Description: TODO  查询所有
     * @return
     * @Data 2016年6月6日 下午12:07:31
     * @throws
     */
    public List<T> findAll();   
    
    /**
     * 
     * @Title: removeOne  
     * @Description: TODO 删除指定的ID对象
     * @param id
     * @Data 2016年6月6日 下午12:07:49
     * @throws
     */
    public void removeOne(String id);   
    /**
     * 
     * @Title: removeAll  
     * @Description: TODO 删除所有
     * @Data 2016年6月6日 下午12:07:59
     * @throws
     */
    public void removeAll();   
    /**
     * 
     * @Title: findAndModify  
     * @Description: TODO 删除所有
     * @param id
     * @Data 2016年6月6日 下午12:08:28
     * @throws
     */
    public void findAndModify(String id);   

	
}