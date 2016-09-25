<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${initParam.project_name } - 积分充值记录</title>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
<h3>欢迎你，${me.name }</h3>
<div id="mp-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<label>用户手机号：</label><input type="text" id="phone"/><button class="btn btn-xs btn-primary" onclick="queryPoint()">查询积分</button>	
				<span id="querypoint" class="red"></span>	<br/>
				<label>积分（输入正数增加积分，负数减少积分）：</label><input type="text" id="point"/>	
				<button class="btn btn-xs btn-primary" onclick="minuspoint()">确定</button>	
				</div>
			</div>
		</div>
	</div>
	<form action="" method="post">
	<div class="">
		<input  type="text" placeholder="输入用户手机号或者" name="target_user" value="${target_user }"/>
		<input type="submit" value="Go" class="btn btn-primary">
	</div>
	</form>
	<button class="btn btn-default" onclick="$('#mp-modal').modal();" type="button">积分操作</button>

<table class="table table-striped table-condensed table-bordered">
	<tr>
		<th>流水号</th>
		<th>操作时间</th>
		<th>操作人</th>
		<th>积分</th>
		<th>目标用户</th>
	</tr>
<c:forEach items="${list }" var="i">
	<tr>
		<td>${i.id }</td>
		<td><fmt:formatDate value="${i.create_time }" pattern="yyyy-MM-dd hh:mm"/></td>
		<td>${i.create_user }</td>
		<td
		 <c:if test="${i.point gt 0 }">style="color: green"</c:if>
		 <c:if test="${i.point lt 0 }">style="color: red"</c:if>
		>
		
		<c:if test="${i.point gt 0 }">+</c:if>
		${i.point }</td>
		<td>${i.target_user }</td>
	</tr>
</c:forEach>
</table>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript">
function queryPoint(){
	$.get('/app/back/charge/get',{
		phone:$('#phone').val(),
	},function(data){
		if(data.ok) $('#querypoint').text(data.msg);
		else alert('失败')
	},'json');
}
function minuspoint(){
	$.get('/app/back/charge/ins',{
		target_user:$('#phone').val(),
		point:$('#point').val()
	},function(data){
		if(data.ok) {
			alert("成功，该用户剩余积分："+data.msg);
			location.reload();
		}
		else alert('失败')
	},'json');
}
</script>
</body>
</html>