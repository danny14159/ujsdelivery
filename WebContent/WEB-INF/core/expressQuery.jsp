<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">

<title>${initParam.project_name } - 快递查询</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">

<style>
</style>

<link rel="stylesheet" href="/static/css/menu.css">

</head>
<body>

	<div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
	</div>

	<div class="container well" style="position: static;">
	最近查询：
	<ul id="latest" class="list-inline">
	</ul>
	
		<form action="" method="get" class="form-inline" id="fm">
		<div class="form-group">
			<input class="form-control" type="text" name="orderNo" value="${orderNo }" placeholder="请输入快递单号" id="orderNo"/>
		  </div>
			<button class="btn btn-primary" onclick="onquery();" type="button">查询</button>
		</form>
		<c:if test="${data!=null }">
			<table class="table table-striped tabled-condensed">
				<tr>
					<th>时间</th>
					<th>跟踪进度</th>
				</tr>
				
				<c:forEach items="${data }" var="i" varStatus="s">
						<tr 
					<c:if test="${s.index == 0 }">
							class="text-danger"
					</c:if>
						>
						<td>${i.ftime }</td>
						<td>${i.context }</td>
						</tr>
				</c:forEach>
			</table>
		</c:if>
		<c:if test="${data == null && !empty orderNo}">
			未找到该订单的状态，请检查单号是否正确
		</c:if>
		<ul>
		</ul>
	</div>
	
<script type='text/javascript' src='/static/js/jquery.min.js'></script>
<script type='text/javascript' src='/static/js/jquery.cookie.js'></script>
<script type='text/javascript' src='/static/js/jquery.json.min.js'></script>
<script type="text/javascript">
$(function(){
	var latest = $.parseJSON( $.cookie('latest') || "[]"); 
	latest = latest || [];
	
	for(var i in latest){
		var d = latest[i];
		$('#latest').append('<li><a href="?orderNo='+d+'">'+d+'</a></li>')
	}
});
function onquery(){
	var latest = $.parseJSON( $.cookie('latest') || "[]" ); 
	latest = (latest && latest instanceof Array)? latest : [];
	
	if(latest.length >=5 ){
		latest.shift(1);
	}
	latest.push($('#orderNo').val());
	
	$.cookie('latest',$.toJSON(latest),{ expires: 70 });
	$('#fm').submit();
}
</script>
</body>
</html>
