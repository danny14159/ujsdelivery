package com.express.core.module;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.express.core.bean.User;
import com.express.core.bean.UserSign;
import com.express.core.extend.Mapper;
import com.express.core.extend.Strings;
import com.express.core.extend.Webs;
import com.express.core.service.UserService;
import com.express.core.service.UserSignService;
import com.express.core.util.Commons;
import com.express.core.util.ExcelReader;
import com.express.core.util.LoginUtil;

@RequestMapping("/excel")
@Controller
public class ExcelModule {

	@Autowired
	private UserSignService userSignService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserSignModule userSignModule;
	
	/**
	 * 导出所有的评价 包含单号，评论内容，日期，姓名，区，栋
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/comment.xls")
	public void exportComment(HttpServletResponse response, HttpServletRequest request) throws IOException {
		User u = LoginUtil.getLoginUser(userService,request);
		if (null == u || !u.isManager()) {
			response.sendRedirect("/app/login");
			return;
		}

		try {
			exportExcel(Commons.PROJECT_NAME+"用户评论数据", new String[] { "单号", "评论", "发布日期", "姓名", "区", "栋" },
					userSignService.exportComment(), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/export.xls")
	public void exportOrders(HttpServletResponse response, @ModelAttribute UserSign us, Integer ps, Integer pn,
			HttpServletRequest request) throws IOException {
		User u = LoginUtil.getLoginUser(userService,request);
		if (null == u || !u.isManager()) {
			response.sendRedirect("/app/login");
			return;
		}
		Pattern r = Pattern.compile(Webs.ORDER_PATTERN);
		Mapper param = Mapper.make().put("state", us.getState())
				.put("express", Strings.isBlank(us.getExpress()) ? null : us.getExpress())
				.put("name", Strings.isBlank(us.getName()) ? null : us.getName())
				.put("addr_region", Strings.isBlank(us.getAddr_region()) ? null : us.getAddr_region())
				.put("addr_building", Strings.isBlank(us.getAddr_building()) ? null : us.getAddr_building())
				;
		if (us.getIs_free() != null) {
			param.put("is_free", us.getIs_free().equals('Y') ? 1 : null).put("is_not_free",
					us.getIs_free().equals('N') ? 1 : null);
		}
		List<UserSign> list = userSignService.list(param.toHashMap());
		for (UserSign item : list) {
			item.setComment(null);
			item.setIs_free(null);
			item.setIsFirst(null);
			item.setState(null);
			Matcher m = r.matcher(item.getRemark());
			if (m.find()) {
				item.setRemark(m.group(0));
			}
		}

		try {
			exportExcel("数据", new String[] { "单号", "用户名", "收件人", "快递公司", "备注", "收件人电话", "收件人姓名", "address", "发布日期",
					"状态", "区", "栋" }, list, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出数据excel文件
	 * 
	 * @param title
	 *            文档标题
	 * @param headers
	 *            文档头
	 * @param dataset
	 *            数据集
	 * @param out
	 */
	@SuppressWarnings({ "deprecation" })
	public void exportExcel(String title, String[] headers, Collection<?> dataset, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		// style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("江大快 - 数据导出"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("邓雷");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<?> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class<?> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (null == value)
						value = "";

					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
								index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							HSSFFont font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLACK.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/upload")
	public String uploadOrderFile(MultipartFile file,String opt,Model model,HttpServletRequest request) throws Exception{
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || !u.isManager())
			return "redirect:/app/back";
		String failMessage = null ;
		if(null == file || file.isEmpty()){
			failMessage = "请选择文件！";
			model.addAttribute("failMessage", failMessage);
			return "back/importOrder";
		}
		ExcelReader excelReader = new ExcelReader();
		String[][] content = null;
		try{
			content = excelReader.readExcelContent(file.getInputStream());
		}
		catch(Exception e){
			e.printStackTrace();
			failMessage = "不支持的文件格式";
			model.addAttribute("failMessage", failMessage);
			return "back/importOrder";
		}
		List<UserSign> list = getUserSignFromArray(content);
		List<UserSign> failList = new ArrayList<>();
		int successCount = 0;
		int failCount = 0;
		if("preview".equals(opt)){
			model.addAttribute("list", list);
		}
		else if("insert".equals(opt)){
			failMessage = "";
			for(UserSign userSign:list){
				try{
					userSign.setUserid(u.getPhone());
					userSign.setName(u.getName());
					userSign.setState('S');
					userSignService.insert(userSign);
					successCount ++ ;
				}
				catch(Exception e){
					e.printStackTrace();
					failList.add(userSign);
					failMessage += e.getMessage();
					failCount ++ ;
				}
			}
			model.addAttribute("failList", failList);
			model.addAttribute("successCount", successCount);
			model.addAttribute("failCount", failCount);
		}
		model.addAttribute("failMessage", failMessage);
		return "back/importOrder";
	}
	
	public String getNextDay(int add){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Calendar calendar = java.util.Calendar.getInstance();  
		calendar.add(java.util.Calendar.DAY_OF_MONTH , add);
		java.util.Date next = calendar.getTime();
		String nextDay = sdf.format(next);
		
		return nextDay;
	} 
	
	public List<UserSign> getUserSignFromArray(String[][] array){
		List<UserSign> list = new ArrayList<>();
		if(array == null){
			return list;
		}
		for(String[] item:array){
			if(item == null) continue;
			UserSign userSign = new UserSign();
			userSign.setExpress(item[0]);
			userSign.setSign_name(item[1]);
			userSign.setPhone(item[2]);
			userSign.setAddr_region(item[3]);
			userSign.setAddr_building(item[4]);
			userSign.setRemark(item[5]);
			userSign.setSend_time(getNextDay(0)+" 20:30-22:00");
			userSign.setSign_time(new Date());
			list.add(userSign);
		}
		return list;
	}
}
