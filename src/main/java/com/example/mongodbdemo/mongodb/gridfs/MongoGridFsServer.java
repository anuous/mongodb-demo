package com.example.mongodbdemo.mongodb.gridfs;

import com.example.mongodbdemo.mongodb.utils.FileUtil;
import com.example.mongodbdemo.mongodb.utils.UUIDUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class MongoGridFsServer {

	public Logger log = LogManager.getLogger(this.getClass());
	

	@Autowired
    MongoTemplate mongoTemplateFS;
	
	GridFS gridFs = null;
	
	private void initGridFs(){
		log.info("初始化GridFS");
		if(null == gridFs ){
			gridFs = new GridFS(mongoTemplateFS.getMongoDbFactory().getLegacyDb() );
			log.info("gridFs 的数据库名称为："+mongoTemplateFS.getDb().getName());
		}
	}
	
	public String saveFile(File file) throws FileNotFoundException{
		initGridFs();
		if(null != file && file.exists() && null != gridFs) {
			// 获取文件后缀
			String endSuff = FileUtil.getFileSuffixes(file.getName());
			// 文件前缀
			String startSuff = UUIDUtils.getFileUUID();
			// 文件名称
			String fileName = file.getName();
			if(StringUtils.isEmpty(fileName)){
				fileName = startSuff + endSuff;
			}
			//根据MD5来判断是否为同一文件
			DBObject queryMd5 = new BasicDBObject("md5", FileUtil.getMd5ByFile(file));
			GridFSDBFile gridFSDBFile = gridFs.findOne(queryMd5);
			if(null != gridFSDBFile){
				log.info("MD5匹配查询到有相同的文件");
				return gridFSDBFile.getFilename();
			}
			// 根据名称查询该名称是否已保存
			log.info("文件在数据库中未重复，查看是否重名：" + startSuff);
			DBObject query = new BasicDBObject("filename", startSuff);
			gridFSDBFile = gridFs.findOne(query);
			
			// 如果该文件名称已存储则再次生成
			if(null != gridFSDBFile) {
				startSuff = UUIDUtils.getFileUUID();
			}
			InputStream iptStm = new FileInputStream(file);
			return saveFileByIptStm(iptStm, startSuff , fileName);
		}
		return null;	
	}
	
	/**
	 * 功能描述：将文件流保存到Mongdb数据库中
	 * 模块作者：daizhiqing
	 * 开发时间：2015年10月31日 下午12:15:41
	 * 更新记录：
	 * 返回数据：String
	 */
	public  String saveFileByIptStm(InputStream iptStm, String id ,String fileName) {
		initGridFs();
		if(null != gridFs) {
			GridFSInputFile gridFSInputFile = gridFs.createFile(iptStm);
			gridFSInputFile.setFilename(id);
			gridFSInputFile.put("aliases", fileName);
			gridFSInputFile.save();
			return id;
		}
		return null;
	}
	
	/**
	 * 功能描述：删除Mongodb数据库中指定文件名称的文件数据
	 * 模块作者：daizhiqing
	 * 开发时间：2015年10月31日 下午12:19:39
	 * 更新记录：
	 * 返回数据：boolean
	 */
	public  boolean removeFile(String id) {
		initGridFs();
		if(null != gridFs) {
			// 据文件名从gridfs中读取到文件
			DBObject query  = new BasicDBObject("filename", id);
	        gridFs.remove(query);
		}
		return false;
	}

	/**
	 * 功能描述：根据文件名称获取Mongodb数据库中文件流
	 * 模块作者：daizhiqing
	 * 开发时间：2015年10月31日 下午12:03:46
	 * 更新记录：
	 * 返回数据：InputStream
	 */
	public  InputStream getMongoFileIptStm(String id) {
		
        GridFSDBFile gridFSDBFile = getGridFSDBFile(id);
        if(gridFSDBFile != null){
        	log.info("filename:" + gridFSDBFile.getFilename());
        	log.info("md5:" + gridFSDBFile.getMD5());
        	log.info("length:" + gridFSDBFile.getLength());
        	log.info("uploadDate:" + gridFSDBFile.getUploadDate());
            return gridFSDBFile.getInputStream();
        }
		return null;
	}
	/**
	 * 获取文件真实名称
	 * @Title: getRealName  
	 * @Description: TODO
	 * @param id
	 * @return
	 * @Data 2016年6月14日 下午5:43:08
	 * @throws
	 */
	public String getRealName(String id){
		 GridFSDBFile gridFSDBFile = getGridFSDBFile(id);
	        if(gridFSDBFile != null){
	        	
	            return gridFSDBFile.getAliases().get(0);
	        }
			return null;
	}
	
	/**
	 * 获取数据库文件
	 * @Title: getGridFSDBFile  
	 * @Description: TODO
	 * @param id
	 * @return
	 * @Data 2016年6月14日 下午5:41:52
	 * @throws
	 */
	private GridFSDBFile getGridFSDBFile(String id){
		initGridFs();
		if(null == gridFs) {
			return null;
		}
		// 据文件名从gridfs中读取到文件
		DBObject query  = new BasicDBObject("filename", id);
        GridFSDBFile gridFSDBFile = gridFs.findOne(query);
        return gridFSDBFile;
	}
}
