<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>${initParam.project_name } - 商家管理后台</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">

<link rel="stylesheet" href="/static/css/menu.css">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<style type="text/css">
body{font-size: 12px;font-family: 'Microsoft YaHei';}
.row{border-bottom: 1px dashed #eee;margin:0.8em 0;}
hr{;background: #eee;border-top:1px solid #315081;margin:4px 0;height: 3px}
strong{color:#794040}
.col-xs-3,.col-xs-4,.col-xs-5,.col-xs-8,.col-xs-12{padding: 0}
.container-fluid{padding: 0}
.danger{background-color: #ebcccc}
.list-item{
	padding-top: 10px;
}
.fadeTheme{
	color:#eee!important;
	background: #eee!important;
}
.list-item{background:#fff ;}
#anchor_sign,#anchor_send{
	text-align: center;
	font-size: 16px;
	font-weight: bold;
	padding-top: 5px;
	padding-bottom: 5px;
	margin-bottom:5px;
}
#batchControl{
	position: fixed;
	bottom: -50px;
	left:0;
	width:100%;
	text-align: center;
	background: #000;
	border-top:3px solid #d9534f;
	padding-top: 5px;
	padding-bottom: 5px;
}
#batchControl button{
	border-color:#fff;
}
.optPanel{
	border-top:1px dashed #BEBEBE;
	background: #ededed;
		padding-bottom: 10px;
	padding-top: 5px;
	text-align: right;
}
</style>
</head>

<body>

<div class="alert alert-info" id="anchor_send">${initParam.project_name }-商家管理后台-${me.name }</div>
		
<%-- 	今日订单：<span class="label label-danger">${today_total }</span>
	<a href="#anchor_sign">未处理代取：</a><span class="label label-danger">${processed_sign }</span>
	<a href="#anchor_send">未处理代寄：</a><span class="label label-danger">${processed_send }</span> --%>
	<p>&nbsp;</p>
<!-- <a href="/app/back?state=S" class="btn btn-danger">已提交</a>
<a href="/app/back?state=F" class="btn btn-success">已完成</a>
<a href="/app/back?state=P" class="btn btn-warning">已取件</a>
<a href="/app/back?state=D" class="btn btn-default">已删除</a> -->
<form action="/app/bback/" method="get" id="fmain">
<select id="state" class="form-control input-sm" name="state" onchange="submitFM();">
	<option value="">筛选订单状态(全部)</option>
	<option value="S">已提交</option>
	<option value="F">已处理</option>
	<option value="D">已删除</option>
</select>
				</form>
				<p>&nbsp;</p>


 <div class="container-fluid" style="margin: 0 1%;">
 	<hr/>
 	<c:if test="${fun:length(list) <= 0}">
 	<center>
 	<img src="/static/img/loading.gif"/>
 		<p class="text-primary">
 		<c:choose>  
 		<c:when test="${state==83}">没有找到&nbsp;<span class="label label-danger">已提交</span>&nbsp;的订单</c:when>  
    	<c:when test="${state==70}">没有找到&nbsp;<span class="label label-success">已处理</span>&nbsp;的订单</c:when>  
    	<c:when test="${state==68}">没有找到&nbsp;<span class="label label-default">已删除</span>&nbsp;的订单</c:when>
    	<c:otherwise><span class="label label-success">耐心等待一下，订单马上到来！</span></c:otherwise>  
 		</c:choose>
 		</p>
 	</center>
 	</c:if>
	<c:forEach var="r" items="${list }" varStatus="status">
	<div class="list-item">
	<div class="row">
		<div class="col-xs-3"><strong>订单号：</strong>${r.id }</div>
		<div class="col-xs-5"><strong>发件人：</strong>${r.name }
		</div>
		<div class="col-xs-4"><strong>电话：</strong>${r.userid }</div>
	</div>
	<div class="row">
		<div class="col-xs-8"><strong>取件地址：</strong><c:out value="${r.address }"></c:out></div>
		<div class="col-xs-4"><strong>快递：</strong><c:out value="${r.express }"></c:out></div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<strong>备注：</strong><span style="color:red"><c:out value="${r.remark }"></c:out></span>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-4"><strong>状态：</strong>
	<c:choose>  
    <c:when test="${r.state==83}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${r.state==70}"><span class="label label-success">已处理</span></c:when>  
    <c:when test="${r.state==68}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${r.state==67}"><span class="label label-default">用户取消</span></c:when>
    <c:otherwise>未知</c:otherwise>  
</c:choose>

</div>
		<div class="col-xs-8"><strong>上门收件时间：</strong>${r.sign_time }</div>
	</div>
	<div class="row optPanel">
		<div class="col-xs-12">
<c:choose> 
<c:when test="${r.state==83}">
	<button class="btn btn-primary" onclick="process('/usend/upd',${r.id},'F')">取件</button>
</c:when>
<c:when test="${r.state==70}">
	<button class="btn btn-default" onclick="process('/usend/upd',${r.id},'S')">撤销</button>
</c:when>
</c:choose>
	<button class="btn btn-default" onclick="process('/usend/upd',${r.id},'D')">删除</button>
</div>
	</div>
	</div>
	<hr/>
	
	</c:forEach>
</div> 

<div id="mp-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<label>用户手机号：</label><input type="text" id="phone"/>	<br/>
				<label>扣除积分：</label><input type="text" id="point"/>	
				<button class="btn btn-xs btn-primary" onclick="minuspoint()">确定</button>	
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/static/js/jquery.min.js"></script>
	<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	function selectExpress(){
		console.log(location)
		var query = location.search,
			express=$('#express').val();
		if(query.search(/express/g)>=0){
			//query+='&express='+express;
			alert(query)
			var nquery = query.replace(/^&express=(.*)/,'&express='+express);
		}
		else{
			query+='&express='+express;
		}
		alert(nquery)
			location.search = nquery;
	}
	function process(url,id,state){
		$.post(url,{
			state:state,id:id
		},function(data){
			if(data){
				location.reload();
			}
			else{
				alert('处理失败，请稍后重试！');
			}
		},'json');
	}
	$(function(){
		var state='${state}';
		if(state){
			$('#state').get(0).value=state;
		}
		
		//开启线程获取服务器状态
/* 		setInterval(function(){
			$.get('/app/rb',{currState:new Date()},'json')
			.done(function(data){
				if(data){
					//$('#newOrderMsg').show();
					//playSound();
					location.reload();
				}
			});
		},1000); */
		
	});
	function submitFM(){
		$('#fmain').submit();
	}
	function playSound(){
		$('body').append('<embed src="/static/ding.mp3" width="0" height="0" id="player"/>');
		setTimeout(function(){
			location.reload();
		},3500);
	}
	</script>
</body>
</html>
