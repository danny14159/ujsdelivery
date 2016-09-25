<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>${initParam.project_name } - 下单次数统计</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <style>
.row{margin-bottom: 15px;margin-top: 15px;}
body{font-family: '微软雅黑'}
    </style>
    

</head>
<body>


<div class="container">

<div class="row">
	<form action="" method="post">
	从：
	<input name="from_date" onclick="laydate()" type="text" value="${from_date }" />
	至：
	<input name="to_date" onclick="laydate()" type="text" value="${to_date }"/>
	
	下单次数超过：
	<input name="overTimes" type="number" value="${overTimes }">
	次
	<button class="btn btn-xs btn-primary">开始查找</button>
	
	</form> 
</div>
<div class="row">
<table class="table table-bordered table-striped">
<tr>
	<th>下单次数</th>
	<th>用户名</th>
	<th>手机号</th>
	<th>地址</th>
</tr>
<c:forEach items="${list }" var="i">
<tr>
	<td>${i.times }</td>
	<td>${i.username }</td>
	<td>${i.phone }</td>
	<td>${i.addr_region }-${addr_building }</td>
	</tr>
</c:forEach>
</table>
</div>
</div>
 <script type="text/javascript" src="/static/js/jquery.min.js"></script>
 <script type="text/javascript" src="/static/laydate/laydate.js"></script>
</body>
</html>

