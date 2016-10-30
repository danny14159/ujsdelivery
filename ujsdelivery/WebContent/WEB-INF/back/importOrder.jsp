<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<title>订单导入</title>
<style type="text/css">
body{font-family: 'Microsoft Yahei'}
</style>
</head>
<body>
<div class="container">
<div class="page-header">
  <h1>${initParam.project_name }<small>订单导入</small></h1>
</div>
<div class="well">
	<ul class="list-group">
	  <li class="list-group-item text-danger">1.上传的文件必须为XLS格式，XLSX格式不支持！</li>
	  <li class="list-group-item text-danger">2.请按下图所示对应列录入订单数据，第一行数据不会识别！</li>
	  <li class="list-group-item text-danger">3.不要刷新本页面，可能会导致订单重复导入！</li>
	  <li class="list-group-item"><img src="/static/img/helper.jpg"/></li>
	</ul>
</div>
<form action="" method="post" enctype="multipart/form-data">
	<input class="form-control" name="file" type="file"/>
	<button class="btn btn-success" type="button" onclick="document.forms[0].action='/excel/upload?opt=preview';document.forms[0].submit();">预览</button> 
	<button class="btn btn-danger" type="button" onclick="document.forms[0].action='/excel/upload?opt=insert';document.forms[0].submit();">开始上传</button> 
	<a href="/app/back" class="btn btn-default">返回后台</a>
</form>
<div class="well">
	<c:if test="${successCount != null }"><span class="text-success">成功：${successCount }条</span></c:if>
	<c:if test="${failCount != null }"><span class="text-danger">失败：${failCount }条</span></c:if>
	<c:if test="${failList!= null }">
		<h4>失败记录</h4>
		<table class="table table-striped">
			<tr>
				<th>快递公司</th>
				<th>用户名</th>
				<th>手机号</th>
				<th>派件地址</th>
				<th>备注</th>
			</tr>
		<c:forEach items="${failList }" var="i">
			<tr>
				<td>${i.express }</td>
				<td>${i.sign_name }</td>
				<td>${i.phone }</td>
				<td>${i.addr_region }-${i.addr_building }</td>
				<td>${i.remark }</td>
			</tr>	
		</c:forEach>
		
		</table>			
	</c:if>
	<c:if test="${not empty failMessage }">失败日志：${failMessage }</c:if>
</div>

<c:if test="${param.opt=='preview' }">
<h4>预览（ ${fn:length(list)}条记录）：</h4>
<table class="table table-striped">
	<tr>
		<th>快递公司</th>
		<th>用户名</th>
		<th>手机号</th>
		<th>派件地址</th>
		<th>备注</th>
	</tr>
<c:forEach items="${list }" var="i">
	<tr>
		<td>${i.express }</td>
		<td>${i.sign_name }</td>
		<td>${i.phone }</td>
		<td>${i.addr_region }-${i.addr_building }</td>
		<td>${i.remark }</td>
	</tr>	
</c:forEach>

</table>
</c:if>
</div>
</body>
</html>