package com.example.mongodbdemo.mongodb.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 系统名称：小事儿
 * 模块名称：
 * 模块描述：文件处理工具类
 * 功能列表：文件处理相关方法
 * 模块作者：戴智青
 * 开发时间：2015年10月31日 上午11:44:39
 * 模块路径：com.xiao4r.mongo.utils.FileUtil
 * 更新记录：
 */
public class FileUtil {

	public static Logger log = LogManager.getLogger(FileUtil.class);

	/**
	 * 获取文件的MD5签名
	 * @Title: getMd5ByFile  
	 * @Description: TODO
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @Data 2016年6月14日 下午5:54:35
	 * @throws
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
			log.info("获取文件的MD5值："+value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取文件名称-不带后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileNameSfs(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(0, index);
	}

	/**
	 * 获取文件后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffixes(String fileName) {
		int index = fileName.lastIndexOf(".");
		String suffixes = fileName.substring(index, fileName.length());
		return suffixes;
	}

	/**
	 * 获取文件名称-不带后缀
	 * @param fileName
	 * @return
	 */
	public static String getFullFileName(String fileName) {
		int index = fileName.lastIndexOf("\\");
		if(index < 0) {
			index = fileName.lastIndexOf("/");
		}
		if(index >= 0) {
			return fileName.substring(index+1, fileName.length());
		}
		return fileName;
	}

	/**
	 * 获取文件类型
	 * @param filePath
	 * @return
	 */
	public static String getFileType(String filePath) {
		String suffixes = getFileSuffixes(filePath);
		if (".bmp".equalsIgnoreCase(suffixes) || ".jpg".equalsIgnoreCase(suffixes) || ".gif".equalsIgnoreCase(suffixes)
				|| ".png".equalsIgnoreCase(suffixes)) {

			return "image";
		} else if (".pdf".equalsIgnoreCase(suffixes)) {

			return "pdf";
		} else if (".doc".equalsIgnoreCase(suffixes) || ".docx".equalsIgnoreCase(suffixes)) {

			return "doc";
		} else if (".txt".equalsIgnoreCase(suffixes)) {

			return "txt";
		} else if (".xls".equalsIgnoreCase(suffixes) || ".xlsx".equalsIgnoreCase(suffixes)) {

			return "xls";
		}
		return "";
	}

	/**
	 * 创建单个文件
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 *            true 表示如果文件夹存在就不再创建了-false是重新创建
	 * @throws IOException
	 */
	public static void createFile(String dir, boolean ignoreIfExitst) throws IOException {

		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		if (!file.createNewFile()) {
			throw new IOException("Cannot create the file = " + dir);
		}
	}

	/**
	 * 创建单个文件夹
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 *            true 表示如果文件夹存在就不再创建了-false是重新创建
	 * @throws IOException
	 */
	public static void createDir(String dir, boolean ignoreIfExitst) throws IOException {

		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		if (!file.mkdir()) {
			throw new IOException("Cannot create the directory = " + dir);
		}
	}

	/**
	 * 创建多个文件夹
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 * @throws IOException
	 */
	public static void createDirs(String dir, boolean ignoreIfExitst) throws IOException {

		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		if (!file.mkdirs()) {
			throw new IOException("Cannot create directories = " + dir);
		}
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static void deleteFile(String filename) throws IOException {

		File file = new File(filename);
		log.info("Delete file = " + filename);
		if (file.isDirectory()) {
			throw new IOException("IOException -> BadInputException: not a file.");
		}
		if (!file.exists()) {
			throw new IOException("IOException -> BadInputException: file is not exist.");
		}
		if (!file.delete()) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDir(String dir) throws IOException {

		File file = new File(dir);
		if (file.exists()) {
			deleteDir(file);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDir(File dir) throws IOException {

		if (dir.isFile())
			throw new IOException("IOException -> BadInputException: not a directory.");
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}
		dir.delete();
	}

	/**
	 * 获取到目录下面文件的大小(包含了子目录)
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static long getDirLength(File dir) throws IOException {

		if (dir.isFile())
			throw new IOException("BadInputException: not a directory.");

		long size = 0;
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				long length = 0;
				if (file.isFile()) {
					length = file.length();
				} else {
					length = getDirLength(file);
				}
				size += length;
			}
		}
		return size;
	}

	/**
	 * 将文件清空
	 * @param srcFilename
	 * @throws IOException
	 */
	public static void emptyFile(String srcFilename) throws IOException {

		File srcFile = new File(srcFilename);
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the file: " + srcFile.getAbsolutePath());
		}
		if (!srcFile.canWrite()) {
			throw new IOException("Cannot write the file: " + srcFile.getAbsolutePath());
		}
		FileOutputStream outputStream = new FileOutputStream(srcFilename);
		outputStream.close();
	}
	
	/**
	 * 功能描述：压缩指定目录成zip文件
	 * 模块作者：ZOUYONG-221464 
	 * 开发时间：2015-7-8 上午11:32:40
	 * 更新记录：
	 * 返回数据：void
	 */
	public static void compresZip(String souceFileName, String destFileName) {
		File file = new File(souceFileName);
		try {
			compresZipByFile(file, destFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能描述：压缩指定文件成zip文件
	 * 模块作者：ZOUYONG-221464 
	 * 开发时间：2015-7-8 上午11:33:15
	 * 更新记录：
	 * 返回数据：void
	 */
	public static void compresZipByFile(File souceFile, String destFileName) throws IOException {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(destFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ZipOutputStream out = new ZipOutputStream(fileOut);
		compresZipBySourceFile(souceFile, out);
		out.close();
	}

	/**
	 * 功能描述：压缩指定文件成zip文件
	 * 模块作者：ZOUYONG-221464 
	 * 开发时间：2015-7-8 上午11:35:29
	 * 更新记录：
	 * 返回数据：void
	 */
	public static void compresZipBySourceFile(File souceFile, ZipOutputStream out)
			throws IOException {

		if (souceFile.isDirectory()) {
			File[] files = souceFile.listFiles();
			for (File file : files) {
				compresZipBySourceFile(file, out);
			}
		} else {
			out.putNextEntry(new ZipEntry(souceFile.getName()));
			FileInputStream in = new FileInputStream(souceFile);
			int b;
			byte[] by = new byte[1024];
			while ((b = in.read(by)) != -1) {
				out.write(by, 0, b);
			}
			in.close();
		}
	}

	/**
	 * 功能描述：将InputStream流写入文件中
	 * 模块作者：ZOUYONG-221464 
	 * 开发时间：2015-7-8 上午9:36:18
	 * 更新记录：
	 * 返回数据：void
	 */
	public static void writeFileByInptStm(InputStream inputStream,
			String srcFilePath, String encoding) {
		OutputStream outputStream = null;
		try {
			byte[] block = new byte[1024];
			outputStream = new BufferedOutputStream(new FileOutputStream(srcFilePath));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1)
					break;
				outputStream.write(block, 0, readLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 写文件-如果此文件不存在就创建一个
	 * @param content String
	 * @param fileName String
	 * @param destEncoding String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFile(String content, String fileName, String destEncoding) throws IOException {

		File file = new File(fileName);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("create file '" + fileName + "' failure.");
			}
		}
		if (!file.isFile()) {
			throw new IOException("'" + fileName + "' is not a file.");
		}
		if (!file.canWrite()) {
			throw new IOException("'" + fileName + "' is a read-only file.");
		}
		
		BufferedWriter out = null;
		try {

			FileOutputStream fos = new FileOutputStream(fileName);
			out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
			out.write(content);
			out.flush();

		} catch (FileNotFoundException fe) {
			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				log.error("Error", ex);
				throw ex;
			}
		}
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回
	 * @param fileName
	 * @param srcEncoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile(String fileName, String srcEncoding) throws IOException {
		File file = new File(fileName);
		if (!file.isFile()) {
			throw new IOException("'" + fileName + "' is not a file.");
		}
		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1)
					break;
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				log.error("Error", ex);
				throw ex;
			}
		}
	}

	/**
	 * 单个文件拷贝
	 * @param srcFilename
	 * @param destFilename
	 * @param overwrite
	 * @throws IOException
	 */
	public static void copyFile(String srcFilename, String destFilename, boolean overwrite) throws IOException {

		copyFile(new File(srcFilename), new File(destFilename), overwrite);
	}

	/**
	 * 单个文件拷贝
	 * @param srcFile
	 * @param destFile
	 * @param overwrite
	 *            是否覆盖目的文件
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean overwrite) throws IOException {
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}
		if (!overwrite) {
			// 目标文件存在就不覆盖
			if (destFile.exists())
				return;
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 不存在就创建一个新的空文件
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1)
					break;
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					log.error("Error", ex);
					throw ex;
				}
			}
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					log.error("Error", ex);
					throw ex;
				}
			}
		}
	}

	/**
	 * 拷贝文件，从源文件夹拷贝文件到目的文件夹 <br>
	 * 参数源文件夹和目的文件夹，最后都不要带文件路径符号，例如：c:/aa正确，c:/aa/错误
	 * @param srcDirName
	 *            源文件夹名称 ,例如：c:/test/aa 或者c:\\test\\aa
	 * @param destDirName
	 *            目的文件夹名称,例如：c:/test/aa 或者c:\\test\\aa
	 * @param overwrite
	 *            是否覆盖目的文件夹下面的文件
	 * @throws IOException
	 */
	public static void copyFiles(String srcDirName, String destDirName, boolean overwrite) throws IOException {
		File srcDir = new File(srcDirName);// 声明源文件夹
		// 首先判断源文件夹是否存在
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Cannot find the source directory: " + srcDir.getAbsolutePath());
		}
		File destDir = new File(destDirName);
		if (!overwrite) {
			if (!destDir.exists()) {
				if (!destDir.mkdirs()) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		} else {
			// 覆盖存在的目的文件夹
			if (!destDir.exists()) {
				// create a new directory
				if (!destDir.mkdirs()) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		}
		// 循环查找源文件夹目录下面的文件（屏蔽子文件夹），然后将其拷贝到指定的目的文件夹下面
		File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null || srcFiles.length < 1) {
			return;
		}
		// 开始复制文件
		int SRCLEN = srcFiles.length;
		for (int i = 0; i < SRCLEN; i++) {
			File destFile = new File(destDirName + File.separator + srcFiles[i].getName());
			// 注意构造文件对象时候，文件名字符串中不能包含文件路径分隔符";".
			if (srcFiles[i].isFile()) {
				copyFile(srcFiles[i], destFile, overwrite);
			} else {
				// 在这里进行递归调用，就可以实现子文件夹的拷贝
				copyFiles(srcFiles[i].getAbsolutePath(), destDirName + File.separator + srcFiles[i].getName(),
						overwrite);
			}
		}
	}

	/**
	 * 压缩文件-注意：中文文件名称和中文的评论会乱码
	 * 
	 * @param srcFilename
	 * @param destFilename
	 * @param overwrite
	 * @throws IOException
	 */
	public static void zipFile(String srcFilename, String destFilename, boolean overwrite) throws IOException {

		File srcFile = new File(srcFilename);
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}
		String newDestFileName = "";
		if (destFilename == null || destFilename.trim().equals("")) {
			newDestFileName = srcFilename + ".zip";
		} else {
			newDestFileName = destFilename + ".zip";
		}
		File destFile = new File(newDestFileName);
		if (!overwrite) {
			// 目标文件存在就不覆盖
			if (destFile.exists())
				return;
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			} else {
				// 不存在就创建一个新的空文件
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
				}
			}
		}
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		ZipOutputStream zipOutputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			zipOutputStream = new ZipOutputStream(outputStream);

			zipOutputStream.setComment("通过java程序压缩的");
			ZipEntry zipEntry = new ZipEntry(srcFile.getName());
			zipEntry.setComment(" zipEntry通过java程序压缩的");
			zipOutputStream.putNextEntry(zipEntry);
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1)
					break;
				zipOutputStream.write(block, 0, readLength);
			}
			zipOutputStream.flush();
			zipOutputStream.finish();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					log.error("Error", ex);
					throw ex;
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					log.error("Error", ex);
					throw ex;
				}
			}
			if (zipOutputStream != null) {
				try {
					zipOutputStream.close();
				} catch (IOException ex) {
					log.error("Error", ex);
					throw ex;
				}
			}
		}
	}

	/**
	 * 设置文件路径
	 */
	public static String setFilePosition(String codeAutoWebSverDeployPath, boolean type) {

		File file = new File(codeAutoWebSverDeployPath);
		if (file.exists()) {
			if (!type) {
				return file.getParent() + File.separator;
			}
		}
		return codeAutoWebSverDeployPath;
	}

	/**
	 * 遍历文件路径中的文件，返回文件列表
	 * @param filePath
	 * @return
	 */
	public static List<File> getFileByFilePath(String filePath, String suffixes) {
		List<File> fileList = new ArrayList<File>();

		File file = getFileByPath(filePath);
		if (!file.exists()) {
			log.info("获取文件夹中文件列表失败，该文件夹不存在！");
			return fileList;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File fs : files) {
				if (fs.isFile()) {
					if(!StringUtils.isEmpty(suffixes)) {
						if(suffixes.equals(getFileSuffixes(fs.getName()))) {
							fileList.add(fs);
						}
					} else {
						fileList.add(fs);
					}
				} else {
					String flpt = filePath + File.separator + fs.getName();
					fileList.addAll(getFileByFilePath(flpt, suffixes));
				}
			}
		} else {
			if(!StringUtils.isEmpty(suffixes)) {
				if(suffixes.equals(getFileSuffixes(file.getName()))) {
					fileList.add(file);
				}
			} else {
				fileList.add(file);
			}
		}
		return fileList;
	}

	/**
	 * 功能描述：在文件末尾写入字符串
	 * 模块作者：ZOUYONG-221464 
	 * 开发时间：2015-7-3 下午2:12:38
	 * 更新记录：
	 * 返回数据：void
	 */
    public static void writerFileEnd(String fileName, String content) {
    	FileWriter writer = null;
    	try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(null != writer) {
        		try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    
    /**
     * 功能描述：在指定文件末尾写入内容
     * 模块作者：zouyong-ocean 
     * 开发时间：2015年9月2日 下午6:42:12
     * 更新记录：
     * 返回数据：void
     */
    public static void writeMsgToFileEnd(String message, String filePath) {
    	FileWriter fw = null;
    	BufferedWriter bf = null;
    	boolean append = false;
    	File file = new File(filePath);
    	try{
    		if(file.exists()) {
    			append =true;  
    		}
    		fw = new FileWriter(filePath, append); // 同时创建新文件
    		// 创建字符输出流对象  
    		bf = new BufferedWriter(fw);
    		// 创建缓冲字符输出流对象 
		 	bf.append(message);
    		bf.flush();
    		bf.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if(null != fw) {
        		try {
        			fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
    		if(null != bf) {
    			try {
    				bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
	}

	/**
	 * 根据路径获取文件对象
	 * @param filePath
	 * @return
	 */
	public static File getFileByPath(String filePath) {

		return new File(filePath);
	}

	public static String getPathSeparator() {
		return java.io.File.pathSeparator;
	}

	public static String getFileSeparator() {
		return java.io.File.separator;
	}

}
