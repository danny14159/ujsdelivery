<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<c:if test="${param.type eq 'sign' or empty param.type}">
	<c:set var="type" value="代取快递"></c:set>
</c:if>
<c:if test="${param.type eq 'send' }">
	<c:set var="type" value="代寄快递"></c:set>
</c:if>
<title>${initParam.project_name } - ${type } - 后台</title>

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/menu.css">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<style type="text/css">
body{font-size: 12px;font-family: 'Microsoft YaHei';}
.row{border-bottom: 1px dashed #eee;margin:0.8em 0;}
hr{;background: #eee;border-top:1px solid #315081;margin:4px 0;height: 3px}
strong{color:#794040}
.col-xs-3,.col-xs-4,.col-xs-5,.col-xs-8,.col-xs-12{padding: 0}
.container-fluid{padding: 0}
.glyphicon{margin-right: 5px;}
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
	display: none;
}
#batchControl button{
	border-color:#fff;
}
.optPanel{
	border-top:1px dashed #BEBEBE;
	background: #ededed;
		padding-bottom: 10px;
	padding-top: 5px;
	text-align: center;
}
.blue{color:#428bca;}
.red{color:red;}
.hide{display: none;}
#newOrderMsg{position: fixed;bottom:0;left:0;width:100%;margin-bottom: 0;border-radius:0;z-index: 1000;background-color: rgba(242, 222, 222, 0.74);padding: 5px;text-align: center;}
</style>
</head>

<body>

	<div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页&nbsp;&nbsp;&nbsp;欢迎你，${me.name }</a>
	</div>
	
<c:if test="${isManager }">
	<span>今日订单：</span><span class="label label-danger">${today_total }</span>
	<a href="javascript:;">未处理代取：</a><span class="label label-danger">${processed_sign }</span>
	<%-- <a href="#">注册用户数：</a><span class="label label-danger">${sign_people_num }</span> --%>
 	<a href="javascript:;">未处理代寄：</a><span class="label label-danger">${processed_send }</span>
	<span>今日免单数：</span><span class="label label-danger">${todayFreeOrders }</span>
	<span>今日签到人数：</span><span class="label label-danger">${todaySignNum }</span>
	<span>今日已完成单数：</span><span class="label label-danger">${todayFinishedNum }</span>
</c:if>
 	
<form action="/app/back/?type=${param.type }" method="post" id="fmain" class="form-inline" style="margin: 15px;">

<c:if test="${isManager }">
<select id="state" class="form-control input-sm" name="state" onchange="submitFM();">
	<option value="">所有状态</option>
	<option value="S">已提交</option>
	<option value="I">取件中</option>
	<option value="P">已取件</option>
	<option value="E">派件中</option>
	<option value="F">已完成</option>
	<option value="C">用户取消</option>
	<option value="D">已删除</option>
</select>
<select id="addr_region" class="form-control input-sm" name="addr_region" onchange="submitFM();">
	<option value="">所有地址</option>
	<option>宿舍楼</option>
	<option>教学楼</option>
	<option>办公楼</option>
	<option>教职工家属楼</option>
	<option>老生楼</option>
	<option>其它区域</option>
</select>
<select id="addr_building" class="form-control input-sm" name="addr_building" onchange="submitFM();">
	<option value="">所有号楼</option>
	<option>1号楼</option>
	<option>2号楼</option>
	<option>3号楼</option>
	<option>4号楼</option>
	<option>5号楼</option>
	<option>6号楼</option>
	<option>7号楼</option>
	<option>8号楼</option>
	<option>9号楼</option>
	<option>10号楼</option>
	<option>11号楼</option>
</select>
<select class="form-control input-sm" onchange="submitFM();" name="is_free">
	<option value="">不限</option>
	<option value="Y">免单</option>
	<option value="N">不免单</option>
</select>
 <select class="form-control input-sm" onchange="submitFM();" name="sys_remark">
	<option value="">系统备注</option>
	<option value="Y">有</option>
</select>

	<select class="form-control input-sm" name="express" id="express" onchange="submitFM();">
					<option data-value="0" value="">所有公司</option>
					<option exp1="菜鸟驿站">菜鸟驿站</option>
					<option exp1="顺丰">顺丰</option>
					<option exp1="申通">申通</option>
					<option exp1="韵达">韵达</option>
					<option exp1="中通">中通</option>
					<option exp1="天天">天天</option>
					<option exp1="百世汇通">百世汇通</option>
					<option exp1="邮政">邮政</option>
	</select>

<select class="form-control input-sm" name="send_time" id="send_time" onchange="submitFM();">
	<option value="">不限送件时间</option>
	<option>当天中午13:00—14:00</option>
	<option>当天下午17:00—18:00</option>
	<option>明天中午13:00—14:00</option>
</select>
				 <div class="form-group">
				<input type="text" value="${param.name }" name="name" class="form-control" placeholder="搜索用户名/收件人/单号"/>
				</div>
				
				日期：
				<input onclick="laydate()" name="from_sign_time" class="laydate-icon" type="text" value="${from_sign_time }">至
				<input onclick="laydate()" name="to_sign_time" class="laydate-icon" type="text" value="${to_sign_time }">
				
					<button class="btn btn-primary" type="submit">Go</button>
				<div class="clear" style="clear:both;"></div>
				
<c:if test="${type eq '代取快递'}">
	<button class="btn btn-danger" onclick="oneKey('S','I','已提交','取件');" type="button">一键取件</button>
	<button class="btn btn-primary" onclick="oneKey('I','P','取件中','入库');" type="button">一键入库</button>
	<button class="btn btn-warning" onclick="oneKey('P','E','已取件','派送');" type="button">一键派送</button>
	<button class="btn btn-success" onclick="oneKey('E','F','已取件','完成');" type="button">一键完成</button>
</c:if>
	<button class="btn btn-default" onclick="onPrintOrders();" type="button">打印订单</button>
	<a class="btn btn-default"  href="/app/back?repeat=1" type="button">重复订单
		<c:if test="${!empty cntDuplicate and cntDuplicate!=0 }">
			<span class="red cntDuplicate" style="font-weight: bold;">(${cntDuplicate })</span>	
		</c:if>	
	</a>
</c:if>
	<c:if test="${me.type == 'P' }">
	<button class="btn btn-default" onclick="$('#mp-modal').modal();" type="button">积分管理</button>
	</c:if>
<c:if test="${isManager }">	
	<%-- <c:if test="${applicationScope.suspend==false or empty applicationScope.suspend }">
		<button class="btn btn-default" onclick="onSuspend(true);" type="button"><span class="glyphicon glyphicon-pause" aria-hidden="true"></span>暂停服务</button>
	</c:if> 
	<c:if test="${applicationScope.suspend==true}">
		<button class="btn btn-danger" onclick="onSuspend(false);" type="button"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>启动服务</button>
	</c:if>--%>
	<a class="btn btn-default" href="/app/analysis?queryDate=2015-09-01" target="_blank"><span class="glyphicon glyphicon-stats" aria-hidden="true"></span>订单分析</a>
	<!-- <a class="btn btn-default"  href="/app/req" type="button" target="_blank">操作记录</a> -->
	<a class="btn btn-default"  href="/excel/comment.xls" type="button">导出评论</a>
	<a class="btn btn-default"  href="javascript:;" type="button" onclick="exportOrders()">导出订单</a>
	<a class="btn btn-default"  href="/app/signTimes" type="button" target="_blank">下单次数</a>
	<!-- <button class="btn btn-default" type="button">锁定后台</button> -->
	<button class="btn btn-primary" onclick="onBatch();" style="display: none;">批量处理</button>
</c:if>

<ul class="nav nav-tabs" style="margin-top:15px; ">
  <li role="presentation" <c:if test="${type eq '代取快递' }">class="active"</c:if> ><a href="/app/back?type=sign">代取快递</a></li>
  <li role="presentation" <c:if test="${type eq '代寄快递' }">class="active"</c:if> ><a href="/app/back?type=send">代寄快递</a></li>

	<c:if test="${isManager }">
	  <li role="presentation"><a href="/app/back/tv">江大旅游</a></li>
	</c:if>
</ul>

<div style="padding-top: 15px;font-size: 1.5em;margin: 0;padding-bottom: 2px;">
					当前页<span class="red">${fn:length(list)}</span>条记录，每页显示
					<select name="ps" onchange="$('input[name=pn]').val(1);submitFM();">
						<option>50</option>
						<option>100</option>
						<option>200</option>
						<option>300</option>
					</select>条，一共${totalCount }条记录
					
					<input name="pn" value="${pn }" type="hidden">
<div id="pager"></div>
				</div>
				
				</form>
 <div class="container-fluid" style="margin: 0 1%;" id="container_seize">
 	<hr/>
 	
 	<c:if test="${fn:length(list) == 0}">
 		<div style="width:100%;text-align: center;margin-top: 30px;font-size: 2em;color:red;">没有找到符合条件的订单╮(╯▽╰)╭</div>
 	</c:if>
 	
 <c:if test="${type eq '代取快递' }">
	<c:forEach var="r" items="${list }" varStatus="status">
	<div class="list-item">
	<div class="hide state" value="${r.state }" data-id="${r.id }">${r.id }</div>
	<div class="row">
		<div class="col-xs-3"><strong>单号：</strong><span class="data-id">${r.secretId }</span>-<c:out value="${r.userid }"></c:out></div>
		<div class="col-xs-5"><strong>用/收：</strong>
			<c:if test="${r.sign_name != r.name }">
				<c:out value="${r.name }"></c:out>/<c:out value="${r.sign_name }"></c:out>
			</c:if>
			<c:if test="${r.sign_name == r.name }">
				<c:out value="${r.name }"></c:out>
			</c:if>
		</div>
		<div class="col-xs-4"><strong>电话：</strong><c:out value="${r.phone }"></c:out></div>
	</div>
	<div class="row">
		<div class="col-xs-8"><strong>派送地址：</strong>
		<%-- <c:out value="${r.address }"></c:out> --%>
		<c:out value="${r.addr_region }"></c:out>-
		<c:out value="${r.addr_building }"></c:out>
		<span id="order_extra">
		<c:if test="${r.isFirst==89 }">（首）</c:if>
		<c:if test="${r.is_free == 89}">（免）</c:if>
<%-- 		<c:if test="${r.id <= YesterdayMaxId + 520 and act_begin and r.sign_time_value gt act_begin_time}">（520）</c:if>
 --%>		</span>
		</div>
		<div class="col-xs-4"><strong>快递：</strong><c:out value="${r.express }"></c:out></div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<strong>备注：</strong><span class="blue showRemark"><c:out value="${r.remark }"></c:out></span>
		</div>
	</div>
	<div class="row1">
		<div class="col-xs-12">
			<strong>送件时间：</strong><span class="send_time"><c:out value="${r.send_time }"></c:out></span>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-4"><strong>状态：</strong>
	<c:choose>  
    <c:when test="${r.state==83}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${r.state==80}"><span class="label label-warning">已取件</span></c:when>  
    <c:when test="${r.state==73}"><span class="label label-warning">取件中</span></c:when>  
    <c:when test="${r.state==69}"><span class="label label-primary">派送中</span></c:when>  
    <c:when test="${r.state==70}"><span class="label label-success">已完成</span></c:when>  
    <c:when test="${r.state==68}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${r.state==67}"><span class="label label-default">用户取消</span></c:when>
    <c:otherwise>未知</c:otherwise>  
	</c:choose>
</div>
		<div class="col-xs-8"><strong>发布时间：</strong><fmt:formatDate value="${r.sign_time}" type="date" pattern="yyyy年MM月dd日 HH:mm"/></div>
		</div>
	 <c:if test="${!empty r.comment }">
            	<div class="row red">客户评价：<c:out value="${r.comment }"/></div>
       </c:if>
	 <c:if test="${!empty r.sys_remark }">
            	<div class="row red">系统备注：<c:out value="${r.sys_remark }"/></div>
       </c:if>
       <c:if test="${isManager }">
		<div class="row optPanel">
		<div class="col-xs-12">
<%-- <c:choose>
	<c:when test="${r.state==83}">
		<button class="btn btn-primary s" onclick="process('/usign/upd',${r.id},'P')" style="float:right"><span class="glyphicon glyphicon-random" aria-hidden="true"></span>取件</button>
	</c:when>
	<c:when test="${r.state==80}">
		<button class="btn btn-primary " onclick="process('/usign/upd',${r.id},'F')" style="float:right"><span class="glyphicon glyphicon-send" aria-hidden="true"></span>派送</button>
		<button class="btn btn-default " onclick="process('/usign/upd',${r.id},'S')">撤销</button>
	</c:when>
	<c:when test="${r.state==70}">
		<button class="btn btn-default " onclick="process('/usign/upd',${r.id},'P')">撤销</button>
	</c:when>
	
</c:choose>
	<button class="btn btn-default " onclick="process('/usign/upd',${r.id},'D')" style="float:left"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</button>
 --%>
 	<c:choose>
		<c:when test="${r.state==83}">
			<button class="btn btn-primary " onclick="process('next',${r.id})" style="float: right;">取件</button>
		</c:when>
		<c:when test="${r.state==73}">
			<button class="btn btn-default " onclick="process('prev',${r.id})">撤销</button>
			<button class="btn btn-primary " onclick="process('next',${r.id})" style="float: right;">入库</button>
		</c:when>
		<c:when test="${r.state==80}">
			<button class="btn btn-default " onclick="process('prev',${r.id})">撤销</button>
			<button class="btn btn-primary " onclick="process('next',${r.id})" style="float: right;">派送</button>
		</c:when>
		<c:when test="${r.state==69}">
			<button class="btn btn-default " onclick="process('prev',${r.id})">撤销</button>
			<button class="btn btn-primary " onclick="process('next',${r.id})" style="float: right;">完成</button>
		</c:when>
		<c:when test="${r.state==70}">
			<button class="btn btn-default " onclick="process('prev',${r.id})">撤销</button>
		</c:when>
		<c:when test="${r.state==68}">
			<button class="btn btn-danger " onclick="process('prev',${r.id})" style="float: right;">恢复为已提交</button>
		</c:when>
	</c:choose>
	
	<button class="btn btn-default " onclick="sys_remark(${r.id})" style="float: left;">系统备注</button>
	<button class="btn btn-default " onclick="process('delete',${r.id})" style="float: left;">删除</button>
 </div>
	</div></c:if>
	</div>
	<hr/>
	
	</c:forEach>
</c:if>


 <c:if test="${type eq '代寄快递' }">
	<c:forEach var="r" items="${list }" varStatus="status">
	<div class="list-item">
	<div class="hide state" value="${r.state }" data-id="${r.id }">${r.id }</div>
	<div class="row">
		<div class="col-xs-3"><strong>单号：</strong>${r.id }</div>
		<div class="col-xs-5"><strong>用户名：</strong>
				<c:out value="${r.name }"></c:out>
		</div>
		<div class="col-xs-4"><strong>电话：</strong><c:out value="${r.userid }"></c:out></div>
	</div>
	<div class="row">
		<div class="col-xs-8"><strong>取件地址：</strong>
		<%-- <c:out value="${r.address }"></c:out> --%>
		<c:out value="${r.addr_region }"></c:out>-
		<c:out value="${r.addr_building }"></c:out>
		</div>
		<div class="col-xs-4"><strong>快递：</strong><c:out value="${r.express }"></c:out></div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<strong>备注：</strong><span class="blue showRemark"><c:out value="${r.remark }"></c:out></span>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-4"><strong>状态：</strong>
	<c:choose>  
    <c:when test="${r.state=='S'}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${r.state=='P'}"><span class="label label-info">已取件</span></c:when>  
    <c:when test="${r.state=='I'}"><span class="label label-warning">已发件</span></c:when>  
    <c:when test="${r.state=='F'}"><span class="label label-success">已完成</span></c:when>  
    <c:when test="${r.state=='D'}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${r.state=='C'}"><span class="label label-default">用户取消</span></c:when>
    <c:otherwise>未知</c:otherwise>  
	</c:choose>
</div>
		<div class="col-xs-8"><strong>送件时间：</strong><c:out value="${r.sign_time}"/></div>
		</div>
		<div class="row optPanel">
		<div class="col-xs-12">
	<c:choose>
		<c:when test="${r.state=='S'}">
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'P')">取件</button>
		</c:when>
		<c:when test="${r.state=='P'}">
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'S')">撤销</button>
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'I')">发件</button>
		</c:when>
		<c:when test="${r.state=='I'}">
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'P')">撤销</button>
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'F')">完成</button>
		</c:when>
		<c:when test="${r.state=='F'}">
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'I')">撤销</button>
		</c:when>
		<c:when test="${r.state=='D'}">
			<button class="btn btn-default " onclick="updateSendOrder(${r.id},'S')">恢复为已提交</button>
		</c:when>
	</c:choose>
	
	<button class="btn btn-default " onclick="updateSendOrder(${r.id},'D')">删除</button>
</div>
	</div>
	</div>
	<hr/>
	
	</c:forEach>
</c:if>
</div> 



<div id="susp-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<label>暂停原因（支持HTML）：</label><br/>
				<textarea style="width:100%;height:320px;" id="susReason"></textarea>
				<button class="btn btn-primary" onclick="postSuspend();">确定</button>	
				<button class="btn btn-default" onclick="$('#susp-modal').modal('hide');">取消</button>	
				</div>
			</div>
		</div>
	</div>
	<canvas id="qrcode"></canvas>
	<script type="text/javascript" src="/static/js/jquery.min.js"></script>
	<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/static/js/laypage.js"></script>
	<script type="text/javascript" src="/static/js/jquery.qrcode.min.js"></script>
	<script type="text/javascript" src="/static/laydate/laydate.js"></script>
	<script type="text/javascript">
	Date.prototype.format = function(format){ 
		var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
		} 

		if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		} 

		for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
		format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
		} 
		return format; 
		} 
	
	var regOrder = /${order_pattern}/;
	//订单号识别正则表达式

	function updateSendOrder(id,state){
		$.post('/usend/upd',{
			id:id,
			state:state
		},function(data){
			if(data){
				location.reload();
			}
			else{
				alert('处理失败，请稍后重试！');
			}
		},'json');
	}
	
	function process(url,id){
		var params={
				'next':'/usign/nexts',
				'prev':'/usign/prevs',
				'cancel':'/usign/cancelorder',
				'delete':'/usign/delorder'
		};
		$.post(params[url],{
			id:id
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
		//重新显示remark
		$('span.showRemark').each(function(i,ele){
			var _remark = $(ele).html().split('&amp;nbsp;');
			$(ele).html(_remark[0].replace(regOrder,function(word){
				return '<span class="red">'+word+'</span>';
			}) + '<br/><span class="red">' +( _remark[1] || '')+ '</span>');
		});
		
		var express='${us.express}',state='${us.state}',
		addr_region='${us.addr_region}',addr_building='${us.addr_building}',
		ps='${ps}',isFirst='${us.isFirst}',is_free='${us.is_free}',send_time='${send_time}',sys_remark='${sys_remark}';
		
		if(express) $('#express').val(express);
		if(state) $('#state').val(state);
		if(addr_region) $('#addr_region').val(addr_region);
		if(addr_building) $('#addr_building').val(addr_building);
		if(addr_building) $('#addr_building').val(addr_building);
		if(ps) $('select[name="ps"]').val(ps);
		if(isFirst) $('select[name="isFirst"]').val(isFirst);
		if(is_free) $('select[name="is_free"]').val(is_free);
		if(send_time) $('select[name="send_time"]').val(send_time);
		if(sys_remark) $('select[name="sys_remark"]').val(sys_remark);
		
		
		//开启线程获取服务器状态
		/* window.refresh_state ='${refresh_state}';
		var last_refresh = new Date();
		last_refresh.setTime(refresh_state);
		$('.last-refresh').html(last_refresh.format("yyyy-MM-dd hh:mm:ss") );
		window.thread_refresh = setInterval(function(){
			$.get('/app/rb',{currState:window.refresh_state},'json')
			.done(function(data){
				if(data){
					clearInterval(window.thread_refresh);
					$('#newOrderMsg').fadeIn(300);
					playSound();
				}
			});
		},1000); */
		
		laypage({
		    cont: 'pager',
		    pages: '${pageCount }', //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
		    curr: '${pn }', //通过url获取当前页，也可以同上（pages）方式获取 
		    jump: function(e, first){ //触发分页后的回调
		        if(!first){ //一定要加此判断，否则初始时会无限刷新
		            //location.href = '?key=${param.key}&ps=4&pn='+e.curr;
		            $('input[name="pn"]').val(e.curr);
		        	submitFM();
		        }
		    },
		    skin: '#AF0000',
		});
		
		$('select').each(function(){
			if(!$(this).find("option:eq(0)").is(":checked")){
				$(this).css({
					borderColor:'red',
					color:'red'
				});
			}
		});

		$('input').each(function(){
			if($(this).val()){
				$(this).css({
					borderColor:'red',
				});
			}
		})
		
	});
	function submitFM(){
		$('#fmain').submit();
	}
	function playSound(){
		//$('body').append('<embed src="/static/ding.mp3" width="0" height="0" id="player"/>');
		/* setTimeout(function(){
			location.reload();
		},5000); */
	}
	function onBatch(){
		$('#batchControl').animate({bottom:0});
		location.hash='#anchor_sign';
		//为listitem添加事件
		$('.list-item').on('click',function(){
			$(this).toggleClass('active');
		});
	}
	function exportOrders(){
		location.href='/excel/export.xls?'+$('#fmain').serialize();
	}
	function exitBatch(){
		$('.list-item').removeClass('active');
		$('#batchControl').animate({bottom:'-50px'});
		$('.list-item').off('click');
		location.hash='';
	}
	function batchFetch(){
		//alert($('.container-fluid:eq(0) .list-item.active').each);
	}
	//一键取件S->P
	function oneKeySeize(){
		oneKey('S','P','已取件','派送');
	}
	function oneKey(currstate,nextstate,statename,optname){
		var nexpress = $('#container_seize .state[value="'+currstate+'"]').length;
		if(!nexpress) {alert('该筛选条件下没有'+statename+'的提单，请筛选其他条件。');return;}
		if(!confirm('“一键'+optname+'”'+($('#express').val() || '所有')+'公司的共'+nexpress+'件快递，确定继续？')) return;
		
		var ids = [];
		$('#container_seize .state[value="'+currstate+'"]').each(function(){
			ids.push($(this).attr('data-id'));
		});
		$.get('/usign/batch',{
			ids:ids,
			state:nextstate
		}).done(function(data){
			if(data) {
				alert('一键处理成功');location.reload();
			}
			else alert('处理失败，请稍后再试。');
		})
	}
	//一键派送P->F
	function oneKeySend(){
		oneKey('P','F','已取件','派送');
	}
	function onSuspend(suspend){
		if(suspend){
			$('#susp-modal').modal('show');
		}
		else{
			if(confirm('将继续服务？')){
				$.get('/app/sus',{
					suspend:suspend
				},'json')
				.done(function(data){
					if(data && data.ok){
						alert('服务已启动');
						location.reload();
					}
					else alert('启动失败，请稍后重试'+data.msg || '');
				});
			}
		}
		$('#susReason').val()
	}
	
	function postSuspend(){
		$.post('/app/sus',{
			suspend:true,
			reason:$('#susReason').val() || '抱歉，我们暂停营业啦~'
		},'json')
		.done(function(data){
			if(data && data.ok){
				alert('服务已暂停');
				location.reload();
			}
			else alert('操作失败，请稍后重试'+data.msg || '');
		});
	}
	
	function getSendTimeCN(order_send_time){
		if(!order_send_time) return null;
		var now = new Date(),month=now.getMonth()+1,day=now.getDate();
		var orders = order_send_time.split('-');
		if(now.getTime() > new Date(order_send_time).getTime()){
			return null;
		}
		else{
			return orders[1]+'月'+orders[2]+'号';
		}
	}
	
	//将dom元素转化成可以使用的html文本
	function generateHtmlForPrint(dom){
		var jokes = [
/* "我们应该有梦 有酒 有写不完的诗歌 有坦荡荡的远方",
"怕黑开灯 孤独听歌 心塞了去跑步 矫情了就去吃 我们得学会照顾好自己",
"有人靠考入清华北大复旦用环境救赎自己,有人发挥失误进入大专也能只身一人闯出一片天地.千万不要认为高考,中考,或一次面试就会决定自己的终身.只要内心有盏灯能够为自己点燃,持续燃烧,每当失去方向的时候,看看内心的光亮,一切都不会惶恐.新学期加油加油！",
"我终于相信，每一条走上来的路，都有它不得不那样跋涉的理由。每一条要走下去的路，都有它不得不那样选择的方向。 —席慕容",
"Life is a journey.What we should care about is not where it's headed but what we see and how we feel.", */
"老师：小明，能回答一下什么是幸运吗？ 小明：老师从高空坠落，正下方有个草垛。 老师：那什么是不幸呢？ 小明：草垛上有个叉子。 老师：那什么是希望？ 小明：你没落在叉子上。 老师：那什么是绝望 小明：你也没落在草垛上 老师：……",
"幼儿园开学，许多孩子被送来，家长走后，孩子们哭闹着，简直跟宰猪场差不多！ 这时候，唯独有一个小孩蹲在墙角巨蛋定，老师准备好好夸一下他，刚走近，那个小孩以迅雷不及掩耳之势，抢过老师手机，连号码都没拨，拿起手机就对着手机哭喊着：“爸爸，快来救我啊！我被妈妈卖了！”",
"在淘宝上买东西不敢用真名，于是起名叫小默！今日一快递员送货到公司喊到：“小黑犬在吗，签收一下快递。”",
"高中一化学老师，只要听到下课铃，马上丢了粉笔走人，就算是题目刚刚讲了一半也停下来走人！他有句至理名言就是：拖堂的老师都是傻逼，讲不完是老师备课的失败～#这样的老师请给我来一打#",
"小明上课玩苹果手机，被老师发现，老师说：“富二代了不起啊，上课就能玩手机了？” 小明说：“我不是富二代，我爸爸只是教育局长。” 老师说：“教育局长了不起啊，玩手机就可以不贴膜？万一刮花了怎么办？来老师给你贴膜。”",
"还依稀记得那一年夏天，我带着女友去舞厅跳舞。女友第一次去，不敢跳，就坐在外面磕瓜子。我在舞池里跳舞看见一个男生去邀请她跳舞，男生已经把一只手优雅地伸出来做邀请状。我特么亲眼看见她给了男的一把瓜子！瓜子。",
"老公：今晚吃饭时可能得罪你爸了。 老婆：怎么了？ 老公：吃饭时我讲了一个成语：“狗仗人势”时，被饭噎着了，只讲了前三个字。你爸问我：“你是说我吗？”，我这时才说出最后一个字……",
"一天在网吧上网。看见一个妇女拿一个木板。走到一个玩英雄联盟的小学生后面。就开始打，打了2分钟我看着都疼。那小学生硬是把那波团战打完才转了过来。</br>妇女：不好意思认错人了。",
"看到朋友圈里的好友一个接一个地成了代购，我的内心无比失落。难道彼此之间原本简简单单的好友关系，非要这样残酷地变成竞争关系吗？",
"老师：“假如我从五楼跳下去，你们希望怎样救我？”小明：“我想时光倒流。”老师：“好感动啊！”小明：“我想再看一遍。”老师：“滚出去！”",
"唐三藏：“八戒，你跑两步给为师看看。” 猪八戒：“师父，你为啥突然想看徒儿跑步？” 唐三藏：“哎，说来惭愧啊！为师自幼在寺中长大，没吃过猪肉，也没见过猪跑。”",
"学数学的同学给我说了这么一个牛逼的公式 ：nAAA+mABC+DD就赢了，mn可以等于0， 让我一分钟就学会了麻将。",
"吃货看武侠剧最虐心的镜头：一桌好菜还没吃，就被打翻了。。。",
"老婆：老公，台风要来了！你可要抱紧我，万一我吹到别人家，别人不还怎么办？[抓狂]老公：你可拉倒吧！就你这样，人家顶着风也要把你送回来！",
"今天买了一盆含羞草，回去怎么动也不害羞，去问老板，老板说，你买的这盆可能不要脸 ",
"仔细回想了一下，感觉小时候最常听到的两种声音“倒车，请注意” 和“归零，归零，归归归归归归归归归归归归归归零”</br>用的是同一个声优……",
"下课时，老师让一个学生帮忙关电脑，忘了他的名字，就冲他喊：“小胖子，帮老师关一下那排电脑。”那同学回头，哀怨的说：“我不胖。”老师：“你不胖？你不胖你回什么头。”",
"刚在小卖部看到一个小孩拿着一瓶可乐，一包薯片和一瓶酱油去结账，当老板说钱不够时，这熊孩子在难以抉择的情况下把酱油放回架子上了，真希望他回到家后会依然安好。",
"无垠的沙漠热烈追求一叶绿草的爱，她摇摇头笑着飞开了。</br>The mighty desert is burning for the love of a bladeof grass who shakes her head and laughs and flies away. 《飞鸟集》",
"你看不见你自己，你所看见的只是你的影子。 </br>What you are you do not see, what you see is your shadow. 《飞鸟集》",
"那些把灯背在背上的人，把他们的影子投到了自己前面。</br>They throw their shadows before them who carry their lantern on their back.  《飞鸟集》",
"麻雀看见孔雀负担着它的翎尾，替它担忧。 </br>The sparrow is sorry for the peacock at the burden of its tail. ",
"Stay hungry,stay foolish. </br>求知若饥，虚心若愚。",
"今天遇到个富二代，显摆说自己是什么集团公司的接班人，将来要继承公司的事业。 我都忍不住笑出声了。一个集团公司的事业能有多大？我从小就是共产主义接班人，我都没说什么。 做人要低调！"
		];
		
		$dom = dom.clone();
		
		$dom.find('.row:gt(3)').remove();
		
		$dom.find('.row:eq(3) .col-xs-4').remove();
		
		//派送地址放在最顶上
		var $rowAdress = $dom.find('.row:eq(1)');
		$rowAdress.clone().prependTo($dom).children('.col-xs-8').css({
			fontSize:'1.5em',
			width:'50%'
		}).find('strong').css({fontSize:'12px'}); //标记送货地址
		$rowAdress.remove();
		
		//(\u53d6\u8d27\u7801|\u7f16\u53f7)\(?\w{2,4}\)?
		
		$dom.find('.row:eq(1) .col-xs-5').css({fontSize:'1.5em'}).find('strong').css({fontSize:'12px'}); //标记收货人
		
		var showRemark = $dom.find('.showRemark');
		showRemark.html(showRemark.html().replace(regOrder,function(word){
			return '<span style="font-size:1.5em">'+word+'</span>';
		}));
		var sendTimeCN = getSendTimeCN($dom.find('.send_time').text().split(' ')[0]);
		
		
		$dom.append($('<p>').html( jokes[Math.round(Math.random() * (jokes.length-1) )] ));
		$dom.append($('<div>').css({
			position:'absolute',
			right:20,
			top:20
		})
		.append('<center style="font-size:14px;margin-bottom:5px;">${initParam.project_name }&nbsp;优质贴心</center>')
		.append('<div class="qrcode"/>')
		//.append('<img src="http://ujsdelivery.com/static/img/logo_order.png" width="180px" />')
		.append('<img src="" width="100px" height="100px"/>')
		);
		
		if(sendTimeCN){
			$dom.append('<div class="send_time_order">'+sendTimeCN+'送</div>');
		}
	    
	    return $dom;
	}
	
	//将html输出到订单上，并加上style样式
	function printOrder()  
	{  
		var style="<style>body{padding:0;margin:0;}.list-item{padding:0.5cm;width:10cm;height:10cm;position:relative;}\
		strong,.blue,.red,.row{color:#000;border-color:#ccc}.red{text-decoration:underline;}.data-id{font-size:1.5em}\
		.qrcode{display:none;}.send_time_order{border:2px dashed #333;position:absolute;padding:3px 15px;bottom:50px;right:15px;font-size:2em;transform:rotate(-45deg)}\
		</style><script type=\"text/javascript\" src=\"/static/js/jquery.min.js\"><\/script>";
		
		    w=window.open("","_blank","k");  
		    
		    $('body',w.document)
		    	.append($('style').clone())
		    	.append(style)
		    ;
		    
		    $('.state[value="S"]').each(function(){
		    	$('body',w.document).append(generateHtmlForPrint($(this).closest('.list-item')));
		    	$(this).closest('.list-item').find('.qrcode').qrcode({
	    			text:'http://ujsdelivery.com/app/qr/'+$(this).attr('data-id')
	    		});
//				printOrder($(this).closest('.list-item'));
			});
		    $('.qrcode',w.document).each(function(){
		    	var dataid = $(this).closest('.list-item').find('.state').attr('data-id');
		    	$(this).qrcode({
		    		/* width:100,
		    		height:100, */
	    			text:'http://ujsdelivery.com/app/qr/'+ dataid
	    		});
		    	$(this).next('img').attr('src',$(this).find('canvas').get(0).toDataURL('image/png'));
		    });
		    //w.document.write(html);  
		   w.print(); 
		   w.close(); 
	    return false;     
	} 
	
	function onPrintOrders(){
		
		var numOfOrder = $('.state[value="S"]').length;
		if(! numOfOrder ) return alert('本页没有未处理的订单。');
		
		var cntDuplicate = $('.cntDuplicate').text() || 0;
		if(cntDuplicate){
			alert('有重复订单，请先处理重复订单！');
			location.href='/app/back?repeat=1';
			return;
		}
		
		imgLogoOrder = new Image();
		imgLogoOrder.onload=function(){
			
			if(confirm('将打印本页所有已提交的'+numOfOrder+'条订单，是否继续？请确保所有要打印的订单都在本页显示！')){
				printOrder();
			}
		}
		imgLogoOrder.src="/static/img/logo_order.png";
	}
	function sys_remark(id){
		var comment;
		if(comment = prompt('请在这里输入系统备注')){
			$.post('/usign/syscmt',{
				id:id,
				comment : comment
			},function(data){
				if(data){
					location.reload();
				}
				else{
					alert('系统繁忙');
				}
			})
		}
	}
</script>
<div id="batchControl">
<img src="/static/img/logo_order.png"/>
<button class="btn btn-primary" onclick="batchFetch()">批量取件</button>
<button class="btn btn-warning" onclick="batchDispatch();">批量派送</button>
<button class="btn btn-default" onclick="exitBatch();">退出批量操作</button>
</div>
</body>
</html>
