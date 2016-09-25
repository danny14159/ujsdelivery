package com.express.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadHelper {

	/**处理上传的请求，返回含有原请求参数的map对象，对于文件类型，则保存到服务器上并返回其相对地址
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> processUploadRequest(HttpServletRequest request) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		if (ServletFileUpload.isMultipartContent(request)) {
			// 1 工厂
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			// 2 工厂获得解析器
			ServletFileUpload fileUpload = new ServletFileUpload(
					diskFileItemFactory);
			// 3 设置解析器参数
			fileUpload.setFileSizeMax(1024 * 1024 * 5);// 5MB 文件不能超过5MB
			fileUpload.setHeaderEncoding("utf-8");// 处理上传附件名乱码
			// 4 解析器解析请求 request
			try {
				List<FileItem> list = fileUpload.parseRequest(request);
				// 5 遍历 list 获得每一个FileItem
				for (FileItem fileItem : list) {
					// 6 判断FileItem 是文件上传项、普通form项
					if (fileItem.isFormField()) {
						// 普通form 项
						// 获得name 和 value
						String name = fileItem.getFieldName();
						String value = fileItem.getString("utf-8");
						parameterMap.put(name, value);// 手动将普通form输入参数 封装自定义map
					} else {
						// 文件上传项
						// 判断用户是否上传文件

						String fileName = fileItem.getName();
						if (fileName == null || fileName.trim().equals("")) {
							// 没有上传图片
							throw new RuntimeException("修改信息，请上传图片！");
						}
						// 获得真实文件名 --- 早期浏览器上传文件时 带有路径
						fileName = UploadUtils.subFileName(fileName);
						// 校验上传文件 格式 -- 根据文件扩展名
						String contentType = fileItem.getContentType();
						boolean isValid = UploadUtils.checkImgType(contentType);
						if (!isValid) {
							// 格式无效
							throw new RuntimeException("上传图片格式不正确的！");
						}
						// 唯一UUID 随机文件名
						String uuidname = UploadUtils
								.generateUUIDName(fileName);

						// 分散目录生成
						String randomDir = UploadUtils
								.generateRandomDir(uuidname);
						// 创建随机目录
						File dir = new File(request.getServletContext()
								.getRealPath("/static/upload" + randomDir));
						dir.mkdirs();

						// 文件上传
						InputStream in = new BufferedInputStream(
								fileItem.getInputStream());
						OutputStream out = new BufferedOutputStream(
								new FileOutputStream(new File(dir, uuidname)));
						int b;
						while ((b = in.read()) != -1) {
							out.write(b);
						}
						in.close();
						out.close();

						String itemField = fileItem.getFieldName();
						String itemParam = parameterMap.get(itemField);
						
						//如果是多文件上传，则使用分号将多个文件路径分开
						if(itemParam == null)
							parameterMap.put(itemField, "/static/upload" + randomDir + "/" + uuidname);
						else 
							parameterMap.put(itemField, itemParam + ";/static/upload" + randomDir + "/" + uuidname);

						// 删除临时文件
						fileItem.delete();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return parameterMap;
	}
	
	/**和上面一个函数的区别是，增加了对象封装的操作
	 * @param request
	 * @param rawObj _In_ 在内存中有值的对象
	 */
	public static <T> void processUploadRequest(HttpServletRequest request,T rawObj){
		try {
			BeanUtils.populate(rawObj,processUploadRequest(request));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
