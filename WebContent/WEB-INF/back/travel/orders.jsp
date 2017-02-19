<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>江大旅游 - 订单管理</title>
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<style type="text/css">
body{padding: 15px;font-family:'Microsoft Yahei';}
</style>
</head>
<body>

<table class="table table-bordered table-hover tabled-striped table-condensed">
	<tr>
		<th>单号</th>
		<th>状态</th>
		<th>用户名</th>
		<th>联系方式</th>
		<th>商品名称</th>
		<th>导游</th>
		<th>车费</th>
		<th>门票</th>
		<th>总价</th>
		<th>发布时间</th>
		<th>操作</th>
	</tr>
	<c:forEach items="${list }" var="item">
		<tr>
			<td>${item.id }</td>
			<td>
				<c:if test="${item.status=='S' }"><span class="label label-danger">已提交</span></c:if>
				<c:if test="${item.status=='P' }"><span class="label label-warning">已付款</span></c:if>
				<c:if test="${item.status=='F' }"><span class="label label-success">已完成</span></c:if>
				<c:if test="${item.status=='D' }"><span class="label label-default">已删除</span></c:if>
				<c:if test="${item.status=='C' }"><span class="label label-default">已取消</span></c:if>
			</td>
			<td>${item.user.name }</td>
			<td>${item.connection_methods }</td>
			<td>${item.goods.title }</td>
			<td>${item.guide_level }</td>
			<td>${item.fare_level }</td>
			<td>${item.ticket_level }</td>
			<td>${item.total_price }</td>
			<td>
				<p class="text-muted">
				<fmt:formatDate value="${item.create_time }" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>
				</p>
			</td>
			<td>
				<button class="btn btn-primary btn-xs">标记完成</button>
				<button class="btn btn-info btn-xs">标记付款</button>
				<button class="btn btn-default btn-xs">删除</button>
			</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>