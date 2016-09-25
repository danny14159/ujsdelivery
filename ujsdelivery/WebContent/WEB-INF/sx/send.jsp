<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat,java.util.Date" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">

<title>代寄快递</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<script type='text/javascript' src='/static/js/jquery.min.js'></script>
	<script type='text/javascript' src='/static/js/jquery.cookie.js'></script>
	<script type='text/javascript' src='/static/js/bootstrap.min.js'></script>
<style>
.bd-share-popup, .bd-share-popup li {
	box-sizing: content-box;
}

.well {
	background-color: #fff;
	border: none;
}

#signup {
	margin: 0px 15px;
}

.tips {
	margin: -5px 0 5px 0;
	color: gray;
	font-size: 13px;
}

#destination {
	border-radius: 0 3px 3px 0;
}

.feeRule-link {
	font-size: 13px;
	margin-left: 15px;
	position: absolute;
	bottom: 0;
}

.warning {
	margin: 10px -15px;
	color: red;
	font-size: 12px;
}

.form-group {
	clear: both;
}

.gray {
	color: gray;
	font-size: 12px;
}
.board{
	background: #FFEFF4;
  padding: 15px;

  border: 1px solid #FDD2E0;
  border-radius: 8px;
  color:#891313;
}
</style>

<link rel="stylesheet" href="/static/css/menu.css">

</head>
<body>

	<div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
	</div>

	<div class="container well">
		<form id="fmain">
			<p class="board">下单后，${initParam.project_name }工作人员将会与您联系，并上门收件。中午寄件当天可发，晚上寄件次日可发。</p>
			<div class="form-group">
				<select id="express" class='form-control' name="express">

				<!-- 	<option data-value="1">圆通</option>

					<option data-value="3">申通</option>

					<option data-value="4">韵达</option>

					<option data-value="5">顺丰</option>
					<option data-value="5">北京宅急送</option>
					<option data-value="5">优速</option> -->
					<option data-value="none">请选择快递公司</option>
					<%-- <c:forEach items="${bacco }" var="name">
						<option>${name }</option>
					</c:forEach> --%>
					<option data-value="5">申通</option>
				</select>
			<p>
				<a onclick="$('#price-modal').modal();" class="btn btn-default"  style="margin-top: 15px;color: #428bca" href="javascript:;">查看寄件价格</a>
			</p>
			</div>
			<div class="form-group">
				<div class="input-group input-group-sm pull-left">
					
					<select class="form-control input-sm" style="width:120px;" id="addr_sel"></select>
					<select class="form-control input-sm" style="width:120px;display: none;" id="addr_sel_dorm"></select>
					<input class="form-control" type="text" placeholder='请填写其他区域' value="" style="display:none;" id="addr_sel_other">
				</div>												
			</div>
			<div class="form-group clearfix" style="margin-bottom: 0px;">
				<p class="normal">
					选择上门收件时间:
				</p>
				<select style="width: 48%; float: left" id="fetchdate" name="fetchdate"
					class='form-control'>
					<%java.util.Date today = new java.util.Date(); 
					long todayms = today.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
					for(int i = 0;i<5;i++){
						out.print("<option data-value=\"1\">"+sdf.format(new Date(todayms+i*24*60*60*1000))+"</option>");
					}
					%>

				</select> <select style="width: 48%; float: right" id="fetchperiod" name="fetchperiod"
					class='form-control'>
					<option>12:00-13:30</option>
					<option>18:00-20:00</option>
				</select>
			</div>
			
			
			
			<div class="form-group">
				<p></p>
				<span>寄件信息:</span> <br> <span class="gray"></span> <br>
				<textarea class="form-control" rows="3" id="remark" name="remark" placeholder="填写收件人的姓名、电话、地址（我们将在上门收件之前把快递单填好，方便顾客）"></textarea>
				<span>特殊要求备注:</span> <br> <span class="gray"> （可选） </span> <br>
				<textarea class="form-control" rows="3" id="remark2" name="remark" placeholder="例如：我的是超大件，需开卡车过来 / 我要帅哥上门收件 / ......"></textarea>
			</div>

			<p class="warning"></p>

		</form>
		<input type="submit" onclick="order()"
			class="btn btn-success btn-block" value='提交订单'>
	</div>
	


<div id="price-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<!-- <button class="btn btn-default" onclick="retrieveStandard(this)" id="yunda">韵达</button>
				<button class="btn btn-default" onclick="retrieveStandard(this)">宅急送</button> -->
				<button class="btn btn-default send_business" onclick="retrieveStandard(this)">申通</button>
				<div id="stdcontent">选择快递公司查看</div>
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th colspan="2" width="50%">申通价目表（单件）</th><th colspan="3">上门取件</th>
								</tr>
							</thead>
							<tbody>
							<tr>
								<td>寄往区域</td>
								<td>首重元/kg</td>
								<td>续重元/kg</td>
							</tr>
							<tr>
								<td class="text-primary">省内</td>
								<td>10</td>
								<td>2</td>
							</tr>
							<tr>
								<td class="text-primary">江浙沪安、北京、天津</td>
								<td>12</td>
								<td>5</td>
							</tr>
							<tr>
								<td class="text-primary">广东、福建、河南、河北</td>
								<td>15</td>
								<td>8</td>
							</tr>
							<tr>
								<td class="text-primary">湖南、湖北、陕西、江西、山西、辽宁、吉林</td>
								<td>15</td>
								<td>10</td>
							</tr>
							<tr>
								<td class="text-primary">黑龙江、重庆、云南</td>
								<td>16</td>
								<td>10</td>
							</tr>
							<tr>
								<td class="text-primary">四川、广西、贵州</td>
								<td>15</td>
								<td>10</td>
							</tr>
							<tr>
								<td class="text-primary">海南、内蒙</td>
								<td>16</td>
								<td>14</td>
							</tr>
							<tr>
								<td class="text-primary">青海、甘肃、宁夏</td>
								<td>20</td>
								<td>20</td>
							</tr>
							<tr>
								<td class="text-primary">西藏 新疆</td>
								<td>28</td>
								<td>28</td>
							</tr></tbody>
						</table>
						<p class="text-primary">（部分偏远地区可能收费不同）</p>
						<p class="text-primary">保价费率：声明价值的3%；保价仅限单票声明价值在5000元（含）以内的快件。</p>
					<div align="center">
						<button class="btn btn-primary" style="margin: 5px 0"
							data-dismiss="modal">确 定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/static/js/common.js"></script>
	<script type='text/javascript'>
function order(){
	if($('#express>option:selected').attr('data-value') == 'none'){
		alert('请选择快递公司。');return;
	}
	
	if(!isAddrValid()){
		alert('请填写有效的取件地址。');return;
	}
	if(!$('#remark').val()){
		alert('请填写寄件信息。');return;
	}
	

	var $fm = $('#fmain'),
		addr = getAddr(),
		fm = $fm.get(0);
		sdata={
				sign_time:$('#fetchdate').val()+$('#fetchperiod').val(),
				remark:'寄件信息：'+$('#remark').val() + '；特殊要求备注：'+$('#remark2').val(),
				express:$('#express').val(),
				addr_region:addr[0],
				addr_building:addr[1]
			};
	 $.post('/usend/ins',sdata,function(data){
		if(data===1){
			alert('提交成功。恭喜你获得30积分，到我的订单查看');location.href="/app/myback#anchor_send";
		}
		else if(data===-1){
			$('#mdlogin').modal();
			//location.href="/app/login?decode=true&redirect=/delay/send.jsp?sdata="+JSON.stringify(sdata).replace("{","[").replace("}","]");
		}
		else alert('提交失败，请稍后再试。');
	},'json'); 
	return false;
}
	
function retrieveStandard(btn){
	var $btn = $(btn);
	$btn.removeClass('btn-default').addClass('btn-primary').siblings('button').removeClass('btn-primary').addClass('btn-default');
	$.get('/app/std',{express:$btn.html()})
	.done(function(data){
		if(data && data.ok){
			$('#stdcontent').html(data.msg || '暂未获取到收费标准，详细咨询客服。');
		}
		else{
			$('#stdcontent').html('请选择快递公司。');
		}
	})
}
$(function(){
	//retrieveStandard($('.send_business').get(0));
	
	renderAddress(addrRegion2,'号楼')
})
</script>

</body>
</html>
