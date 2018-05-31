package com.example.mongodbdemo.mongodb.dao.impl;

import com.example.mongodbdemo.mongodb.dao.AbstractRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class MongoRepository<T extends Serializable> implements AbstractRepository<T> {

	public Logger log = LogManager.getLogger(this.getClass());

	
	@Autowired
    MongoTemplate mongoTemplate;
	
	private Class<T> entityClass; 
	
	
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	
	public Class<T> getEntityClass() {
//		try {
//			Type genType = getClass().getGenericSuperclass();  
//			log.info("获取的GenericSuperclass："+genType);
//			if(genType instanceof ParameterizedType){ 
//				//参数化类型  
//	            ParameterizedType parameterizedType= (ParameterizedType) genType; 
//	          //返回表示此类型实际类型参数的 Type 对象的数组  
//	            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//	            this.entityClass = (Class<T>) actualTypeArguments[0]; 
//			}else{
//				this.entityClass= (Class<T>)genType;  
//			}
//	        log.info("entityClass="+entityClass);
//		} catch (Exception e) {
//			log.warn("没有指定泛型变量");
//		}
		return entityClass;
	}


	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: </p>
	 */
	public MongoRepository() {
		log.info("初始化MongoRepository");
	}
	
	@Override
	public void insert(T object) {
		log.info("执行mogoDB_insert:"+object);
		getMongoTemplate().save(object);   
	}

	@Override
	public T findOneById(String id) {
		log.info("执行mogoDB_findOneById:"+id);
		return getMongoTemplate().findOne(new Query(Criteria.where("id").is(id)), getEntityClass());
	}

	@Override
	public List<T> findAll() {
		log.info("执行mogoDB_findAll:");
		List<T> result = getMongoTemplate().find(new Query(), getEntityClass());
		if(null != result){
			log.info("findAll查询出数据条数："+result.size()+"==>"+result);
		}
		return result;  
	}

	@Override
	public List<T> findByRegex(String filedName , String regex) {
		log.info("执行mogoDB_findByRegex:"+filedName +" && "+regex);
		 Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);     
         Criteria criteria = new Criteria(filedName).regex(pattern.toString());
         Query query = new Query(criteria);
       
         List<T> result = getMongoTemplate().find( query,getEntityClass());
         if(null != result){
        	 log.info("findByRegex查询出数据条数："+result.size()+"==>"+result);
         }
         return result;    
	}

	public List<T> findByRegexPage(String filedName , String regex , Integer page ,Integer pageSize) {
		log.info("执行mogoDB_findByRegex:"+filedName +" && "+regex+"，查询第"+page+"页，的"+pageSize+"条数据");
		 Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);     
         Criteria criteria = new Criteria(filedName).regex(pattern.toString());
         Query query = new Query(criteria);
         query.skip(pageSize * (page-1));
         query.limit(pageSize);
         List<T> result = getMongoTemplate().find( query,getEntityClass());
         if(null != result){
        	 log.info("findByRegex查询出数据条数："+result.size()+"==>"+result);
         }
         return result;    
	}
	
	@Override
	public void removeOne(String id) {
		Criteria criteria = Criteria.where("id").in(id);
        if(criteria == null){     
             Query query = new Query(criteria);
             if(query != null && getMongoTemplate().findOne(query, entityClass) != null){
            	 getMongoTemplate().remove(getMongoTemplate().findOne(query, getEntityClass()));     
             }
        }
	}

	@Override
	public void removeAll() {
		log.warn("危险操作！！删除所有数据，暂时不开放");
//		List<T> list = this.findAll();     
//        if(list != null){     
//            for(T person : list){  
//                getMongoTemplate().remove(person);     
//            }     
//        }   
	}

	@Override
	public void findAndModify(String id) {
		// TODO Auto-generated method stub
		
	}


	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
