<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/static/css/bootstrap.min.css" rel="stylesheet"/>
<title>${initParam.project_name } - 访问请求记录</title>
<style type="text/css">
.red{color:red;}
.green{color:green;}
.blue{color:blue;}
.orange{color:orange;}
</style>
</head>
<body>
<h1>访问请求记录</h1>
<h2 style="color:#999">订单状态说明:D删除订单，F标记已完成，P标记已取件，S标记已提交，I标记入库，E标记派送，id表示订单号</h2>
<form action="" method="get">
<input type="text" class="form-control" value="${param.key }" name="key" placeholder="搜索单号/用户名"/>
<button>Go</button>
</form>
<table class="table table-striped table-condensed table-bordered" width="100%">
	<tr>
		<td>电话</td>
		<td>姓名</td>
		<td>请求uri</td>
		<td>参数</td>
		<td>IP地址</td>
		<td>请求时间</td>
	</tr>
	<c:forEach items="${list }" var="item">
		<tr>
			<td>${item.userid }</td>
			<td>${item.username }</td>
			<td>${item.req_url }
				<c:if test="${item.req_url == '/usign/ins'}"><span class="red">下单</span></c:if>
				<c:if test="${item.req_url == '/usign/upd'}"><span class="blue">修改订单状态</span></c:if>
				<c:if test="${item.req_url == '/usign/batch'}"><span class="orange">批量操作</span></c:if>
			</td>
			<td>${item.params }</td>
			<td>${item.target_ip }</td>
			<td><fmt:formatDate value="${item.opt_time }" type="date" pattern="yyyy年MM月dd日 HH:mm:ss"/></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>