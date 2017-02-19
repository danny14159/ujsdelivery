<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<style type="text/css">
.input table{text-align: center;font-size: 1.5em;margin-bottom: 0;}
.input table td{width:33.33%;}
.input table td:hover{background:#999;color:#fff;}
.input{position: fixed;bottom: 0;left:0;width:100%;background: #000; color:#fff;opacity:0.5}
#preview{font-weight: bold;padding-right: 2em;float: right;font-size: 2em;color:#000;height: 1.5em;line-height: 1.5em;width:100%;background: #fff;opacity:1;}
#main{margin-bottom: 231px;text-align: center;} 
.label{padding: 0;}
  .shadow{
	width: 100%;
  height: 26em;
  position: fixed;
  top: 0;left:0;
  background-color:rgba(0,0,0,0.2);
  display: none;
  color:#fff;
  }
.shadow h2{color:#fff;margin-top: 200px;background: #000;}
</style>
<title>${initParam.project_name } - 已扫描二维码</title>
</head>
<body>
<div id="main">
	<h2>已扫单号</h2>
	<p>如果第一次没有发送成功，请尝试再次点击发送短信按钮，“派送中”的单不会再发短信</p>
	<button class="btn btn-info" onclick="sendMsg()">发送短信</button>
	<button class="btn btn-warning" onclick="batchstore();">入库</button>
	<button class="btn btn-danger"  onclick="clearList();">清空列表</button>
	<button class="btn btn-success" onclick="beginIndex();">初始编号</button>
	<table class="table table-bordered table-striped table-condensed">
	
	<tr>
		<th width="20%">编号</th>
		<th width="20%">单号</th>
		<th width="20%">区</th>
		<th width="20%">特殊备注</th>
		<th></th>
	</tr>
	<c:forEach items="${collected }" var="item" varStatus="s">
	<tr>
		<td>${s.index+1+beginIndex }
	<c:choose>  
    <c:when test="${item.state==83}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${item.state==80}"><span class="label label-warning">已取件</span></c:when>  
    <c:when test="${item.state==73}"><span class="label label-warning">取件中</span></c:when>  
    <c:when test="${item.state==69}"><span class="label label-primary">派送中</span></c:when>  
    <c:when test="${item.state==70}"><span class="label label-success">已完成</span></c:when>  
    <c:when test="${item.state==68}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${item.state==67}"><span class="label label-default">用户取消</span></c:when>
    <c:otherwise>已置空</c:otherwise>  
	</c:choose>
		</td>
		<td>${item.id }</td>
		<td data="${item.addr_region }-${item.addr_building }">${item.addr_region }-${item.addr_building }</td>
		<td>${item.remark }</td>
		<td> 
		<a  class="" href="/app/qr/${item.id}?del=true">删除</a>
		<a  class="" href="/app/qr/${item.id}?setnull=true">置空</a>
		</td>
	</tr>
	</c:forEach>
	</table>
</div>

<div class="input">
	<div id="preview"></div>${msg }
	<table class="table table-bordered ">
		<tr>
			<td>1</td>
			<td>2</td>
			<td>3</td>
		</tr>
		<tr>
			<td>4</td>
			<td>5</td>
			<td>6</td>
		</tr>
		<tr>
			<td>7</td>
			<td>8</td>
			<td>9</td>
		</tr>
		<tr>
			<td>X</td>
			<td>0</td>
			<td>OK</td>
		</tr>
	</table>
</div>
<div id="bottom"></div>
<form>
	<input type="hidden" name="bi"/>
</form>
<div id="shadow" class="shadow">
<h2>正在发送：<span id="success"></span>/<span id="total"></span>，失败<span id="fail"></span></h2>
</div>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
function clearList(){
	if(confirm('确认清空列表？')){
		location.href="/app/qr/a?clear=true"
	}
}
$(function(){
	$('.input table td').click(function(){
		var txt = $('#preview').text();
		if($(this).text()=='X'){
			if(txt){
				txt = txt.substr(0,txt.length-1);
			}
			$('#preview').text(txt);
		}
		else if($(this).text()=='OK'){
			if(txt)
				location.href="/app/qr/"+txt;
		}
		else{
			$('#preview').text($('#preview').text() + $(this).text());
		}
	});
	location.hash='';
	location.hash='bottom';
	
	wx.config({
	    appId: 'wx041974fbeae57af2', // 必填，公众号的唯一标识
	    timestamp: 1421142450, // 必填，生成签名的时间戳
	    nonceStr: 'fdafafafds', // 必填，生成签名的随机串
	    signature: '',// 必填，签名，见附录1
	    jsApiList: [
	      'scanQRCode'
	    ] 
	});
	
	wx.scanQRCode({
	      needResult: 1,
	      desc: 'scanQRCode desc',
	      success: function (res) {
	        alert(JSON.stringify(res));
	      }
	});
});
function sendMsg(){
	//先统计一下
	var data={},len=$('td[data]').length;
	$('#total').text(len);
	$('td[data]').each(function(){
		var attr_data = $(this).attr('data');
		if(attr_data in data){
			data[attr_data] ++;
		}
		else{
			data[attr_data]=1;
		}
	});
	var msg = "";
	for(var i in data){
		msg+=i+":"+data[i]+'单，';
	}
	if(confirm(msg+"确认发送短信？")){
		CNT_SUCCESS_MSG=0;CNT_FAILED_MSG=0,FAIL_ID="，失败单号：";
		
		//$('#shadow').height($(window).height()).show();
		
		//for(var i=0;i<len;i++){
			$.post('/usign/sendmsgs',{index:i}).
			done(function(data){
				if(data.ok){
					alert('短信请求已成功提交！');
				}
				//onMsgSended(len,data.ok,data.msg);
			},'json');
		//}
	}
}
var CNT_SUCCESS_MSG=0,CNT_FAILED_MSG=0,FAIL_ID="，失败单号：";
function onMsgSended(len ,success,orderid){
	if(success) CNT_SUCCESS_MSG++;
	else{
		CNT_FAILED_MSG++;
		if(orderid){
			FAIL_ID+=orderid+","
		}
	}
	
	$('#success').text(CNT_SUCCESS_MSG);
	$('#fail').text(CNT_FAILED_MSG);
	
	if(CNT_SUCCESS_MSG +CNT_FAILED_MSG == len){
		var msg = '发送完成。成功：'+CNT_SUCCESS_MSG+",失败："+CNT_FAILED_MSG;
		alert(msg+FAIL_ID+"请务必牢记！");
		location.href="/app/qr/-1?clear=true";
	}
}
function beginIndex(){
	var beginIndex=parseInt(prompt('输入第一单编号(如21)：'))-1;
	$('input[name="bi"]').val(beginIndex).closest('form').submit();
}
function batchstore(){
	if(confirm("确认入库？")){
		$.post('/usign/batchstore').
		done(function(data){
			if(data && data.ok){
				alert(data.msg);
				location.href="/app/qr/-1";
			}
			else{
				alert('发送失败，请稍后重试')
			}
		},'json');
	}
}
</script>
</body>
</html>