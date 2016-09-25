<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">

<title>${initParam.project_name } - 代取服务</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">

<link rel="stylesheet" href="/static/css/menu.css">

<link rel="stylesheet" href="/static/css/bootstrap.min.css">
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


.warning {
	margin: 10px -15px;
	color: red;
	font-size: 12px;
}

.form-group {
	clear: both;
}

.free_date {
	left: 40px;
	top: -40px;
	position: relative;
	width: 60px;
	height: 60px;
	background-image: url(../images/free3.png);
	background-size: 60px 60px;
}
.board{
	background: #FFEFF4;
  padding: 15px;
  margin-left: -15px;
  margin-right: -15px;
  border: 1px solid #FDD2E0;
  border-radius: 8px;
  color:#891313;
}
span.gray{
font-size: 0.85em;
color:#888;
}
</style>



</head>
<body>

<!-- <div id="spinner"></div> -->

	 <div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
	</div>

	<div class="well">
		<form role="form" id="signup" class="form-horizontal" method="post"
			action="" onsubmit="return false;">
			<p class="board">
			<strong>中午件请在12:00前下单。</strong>
			<a href="javascript:;" class="text-danger feeRule-link"style="font-size: 14px;text-decoration: underline;">收费规则</a>
			</br>
			</p>
			<div class="form-group">
				<select class="form-control input-sm" name="express" id="express">
					<option value="-1">选择快递公司(可先粘贴短信让系统识别)</option>
					<option exp1="圆通" exp2="中门">圆通 - 中门</option>
					<option exp1="圆通" exp2="香江">圆通 - 香江</option>
					<option exp1="申通" value="申通">校内申通</option>
					<option exp1="韵达" exp2="中门">韵达 - 中门</option>
					<option exp1="韵达" exp2="香江">韵达 - 香江</option>
					<option exp1="百世快递">百世快递</option>
					<option value="快递超市" exp1="快递超市">快递超市(中通等)</option>
					<option value="其它公司" exp1="其它">其它公司(中午件等)</option>
					<option exp1="天天">天天</option>
					<option exp1="顺丰" exp2="京江">顺丰 - 京江</option>
					<option exp1="顺丰" exp2="香江">顺丰 - 香江</option>
					<option exp1="EMS">EMS</option>
					<option exp1="中国邮政">中国邮政</option>
				</select>
			</div>
<!-- exp1="亚马逊" exp2="京东" exp3="赛澳递" exp4="南京晟邦" exp5="快捷快递" exp6="优速快递" exp7="国通" exp8="全峰快递" -->
			<div class="tips">收件人姓名及号码需与快递单上的一致</div>

			<div class="form-group">
				<div class="input-group input-group-sm col-xs-5 pull-left">
					<div class="input-group-addon" style="height: 100%">
						<span class="glyphicon glyphicon-user"></span>
					</div>
					<input type="text" id="name" name="sign_name" maxlength="16"
						placeholder="收件人姓名" class="form-control input-sm"
						value='${me.name }'>
				</div>
				<div class="input-group input-group-sm col-xs-push-1 col-xs-6">
					<div class="input-group-addon" style="height: 100%;">
						<span class="glyphicon glyphicon-phone"></span>
					</div>
					<input type="tel" id="phone" name="phone" maxlength="11"
						placeholder="手机号码" class="form-control" value="${me.phone }">
				</div>
			</div>
	
			<input type="hidden" id="address" name="address" value="${me.addr_region }"/>
			<div class="form-group">
				<div class="input-group input-group-sm pull-left" 
				
				<c:if test="${not empty me.addr_region }">
					style="display:none;"
				</c:if>
				
				id="selectAddress">
					<select class="form-control input-sm" style="width:120px;" id="addr_sel"></select>
					<select class="form-control input-sm" style="width:120px;display: none;" id="addr_sel_dorm"></select>
					<input class="form-control" type="text" placeholder='请填写其他区域' value="" style="display:none;" id="addr_sel_other">
				</div>												
			</div>
			<c:if test="${empty me.addr_region }">
			<script type="text/javascript">window.addressType = 2;</script>
			</c:if>
			<c:if test="${not empty me.addr_region }">
				<p id="defaultAddress">快递包裹将为您送至：<span class="text-primary">${me.addr_region }-${me.addr_building }</span>
				<input type="hidden" name="addr_region" value="${me.addr_region }"/>
				<input type="hidden" name="addr_building" value="${me.addr_building }"/>
				<button class="btn btn-primary btn-xs" onclick="changeAddr();">换个地址</button>
				</p>
			</c:if>
			<%!public String getNextDay(){
				
				calendar.add(java.util.Calendar.DAY_OF_MONTH , 1);
				java.util.Date next = calendar.getTime();
				String nextDay = sdf.format(next);
				
				return nextDay;
			} %>
			
			<%!java.util.Calendar calendar = java.util.Calendar.getInstance();   %>
			<%!java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
			<%
				java.util.Date now = new java.util.Date();
				String today = sdf.format(now);
				calendar.setTime(now);
				int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
				int minute = calendar.get(java.util.Calendar.MINUTE);
				boolean after17 = /* (hour >= 16 && minute >=30) || */ hour >= 17;
				
				String tomorrowStr = getNextDay();
				String AfterTommorrow = getNextDay();
			%>
			
			<div class="form-group">
				<span>送件时间：</span><span class="gray">17:00前下单当天取送，之后下单的隔天取送。</span>
				<select name="send_time" class="form-control">
					<%if(!after17){
					%>	
						<option value="<%=today+" 20:30-22:00"%>" selected="selected">今天20:30-22:00</option>
					<%} %>
					<option value="<%=tomorrowStr+" 20:30-22:00"%>">明天20:30-22:00</option>
					<option value="<%=AfterTommorrow+" 20:30-22:00"%>">后天20:30-22:00</option>
				</select>
			</div>

			<div class="form-group" style="display: none">
				<label class="checkbox"> <input id="fast" name="fast"
					type="checkbox" class="form-control input-sm">加急派送(加收50%)
				</label>
			</div>

			<div class="form-group">
				<span>取件信息:</span>
				<span class="gray">如需代取多个，请重复下单</span>
					
				<textarea class="form-control" rows="4" name="remark" id="remark" placeholder="复制快递通知短信，例如：请于18:00点前凭取货码####领取快递。"></textarea>
			</div>
			<div class="form-group">
				<span>特殊要求备注：</span><span class="gray">可选</span>
					
				<textarea class="form-control" rows="4" name="remark2" id="remark2" placeholder="例如：这是昨天（前天）的快递 ；送件请打下面电话110……"></textarea>
			</div>
			
			<%-- <div class="form-group">
				<p class="text-primary"><input type="checkbox" id="agree"/>授权${initParam.project_name }团队帮您代取本次快递
				<span id="pointForFree"> </span>
				</p>
			</div> --%>
			
			<div class="form-group" >
				<div class="col-xs-4"style="padding-left:0px;">
				<button id="freeOrderBtn" class="btn btn-warning" onclick="freeOrder();"><span class="glyphicon glyphicon-list-alt"></span>&nbsp;积分换免单</button> 
			</div>
				<div class="col-xs-8" style="padding-right:0px;">
				<button id="submitButton" type="button"
					class="btn btn-success btn-large btn-block" class="form-control">
					提交订单</button>
				</div>
				</div>
		</form>
	</div>
	<div id="price-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<p style="font-size: 14px; margin: 10px 3px">代领快递
						3kg以下2元；超重件2-5元不等，特大件如沙发自行车等另算</p>
					<div align="center">
						<button class="btn btn-primary" style="margin: 5px 0"
							data-dismiss="modal">确 定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="success-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="msg"></div>
					<div>您的包裹将尽快送至您手中( ^_^ )</div>
					<img src="/static/img/success.jpg" width="100%"/>
					<div align="center">
						<a class="btn btn-primary" style="margin: 5px 0"
							href="/app/myback">确 定</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="message-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<p>亲爱的用户们，很抱歉因期末和寒冬故，为继续保持高品质服务，自28日起至本学期结束，平台作出一些调整，请大家在下单之前务必了解清楚——</p>
 <p>   一、下单截止时间调整为16:30截止；</p>
 <p>    二、派件时间统一为19:30-21:30；</p>
  <p class="text-primary" style="font-weight:bold;">   三、代取价格更改为：3kg以下3元；3kg以上5元，特大件如沙发自行车等另算；</p>
   <p>  （${initParam.project_name }温馨提示：期末将至，预祝大家考试顺利！）</p>
 <p>感谢大家一如既往的支持，我们也将无论任何风雨，都竭诚为您服务！</p>
 <p style="width:100%;text-align: right;" class="text-muted">
 <span>${initParam.project_name }团队 2015.11.27</span><br/>
  <span>${initParam.project_name }，优质贴心</span>
 </p>

					<div align="center">
						<button class="btn btn-primary" style="margin: 5px 0" data-dismiss="modal">确 定</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="/static/js/jquery.min.js"></script>
	<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
<!-- 	<script type="text/javascript" src="/static/js/sign.js"></script>
 --><script type="text/javascript" src="/static/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/static/js/common.js"></script>
<script type="text/javascript">
function onLogin(){
	$('#mdlogin').modal('toggle'); 
	$.get('/app/ajaxlogin?'+$('#fmlog').serialize(),function(data){
		if(data && data.ok){
			storeCookie($('#loginphone').val(),$('#loginpassword').val());
			 $("#submitButton").click();
		}
		else{
			$('#submitButton').attr('disabled',null).text('提交订单')
			alert(data.msg);
		}
	},'json');
}
var phoneValidator = /^\d{11}$/;
var $form = $('form');
var form = $form[0];
//获取地址的类型，1表示default,2表示select出来的
function getAddressType(){
	return window.addressType || 1;
}
function order(){
        if ($("#express>option:selected").index() == 0) {
            alert("请选择快递公司");
            return;
        };
        if ($("#name").val() == "") {
            alert("请输入收件人姓名");
            return;
        };
        if (!$("#phone").val() || !$("#phone").val().match(/\d{11}/)) {
            alert("请输入有效的收件人手机号");
            return;
        };
        if (getAddressType()==2 && !isAddrValid()) {
            alert("请输入有效的宿舍地址");
            return;
        };
        if ($("#remark").val() == "") {
            alert("麻烦在备注栏标注快递区位和编号，如“申通快递编号S1”，也可直接复制快递公司的短信");
            return;
        };
        //if(!$('#agree').is(':checked')){alert('请允许授权再提交订单！');return;}

        //使按钮失效
        $('#submitButton').attr('disabled','disabled').text('订单正在提交，请稍后...');
        
        //填写隐藏的表单域
        $('#address').val(getAddressType()==1 ? form.address.value : getAddr());
        
        //使用&nbsp;作为备注和特殊要求的分隔符
        var _remark ='&nbsp;'+( form.remark2.value || "" ),addr=getAddr();
        
        $.ajax({
        	data:{
            	express:form.express.value,
            	name:form.name.value,
            	phone:form.phone.value,
            	address:form.address.value,
            	remark:form.remark.value + _remark,
            	sign_name:form.sign_name.value,
            	addr_region:getAddressType()==1 ? form.addr_region.value : addr[0],
            	addr_building:getAddressType()==1 ? form.addr_building.value : addr[1],
            	point4free:form.point4free ? form.point4free.value : "",
            	send_time:form.send_time.value
            },
            dataType:'json',
            timeout : 3000,
            url :'/usign/ins'
        }).done(function(data){
        	if(data.ok){
        		/* location.href="/app/act/1111"; */
        		$('#success-modal .msg').html(data.msg);
        		$('#success-modal').modal();
        	}
        	else {
        		alert(data.msg);
        	}
        }).fail(function(data){
        	alert('系统异常，请重试或联系客服。--fail');
        });
        return false;
}
EXP = {};
function getExp(attrs,express){
	EXP[express] = {};
	EXP[express]['exp'] = [];
	for(var i =0;i < attrs.length;i++){
		var name = attrs[i].name;
		if(name.indexOf('exp') === 0){
			EXP[express]['exp'].push(attrs[i].nodeValue);
		}
	}
}
function launch(){
	renderAddress(addrRegion,'栋')
	//init express data
	$('#express>option:gt(0)').each(function(i,v){
		getExp(v.attributes,$(v).val(),$(this));
	});

    $("#submitButton").bind("click", function(){
    	try{
    		order();
    	}
    	catch(e){
    		alert('抱歉系统出现异常，请截图并联系客服。谢谢配合。'+e);
    	}
    });

    $(".feeRule-link").bind("click", function() {
        $('#price-modal').modal({
            backdrop: true,
            keyboard: true,
            show: true
        });
    });
    
    $("#remark").change(function(){
    	try{
	    	var remark = $(this).val();
	    	if(!remark) return;
	    	
	    	var hasChanged = false;
	    	for(var i in EXP){
	    		var exp = EXP[i],result = 1;
	    		for(var j in exp.exp){
	    			if(remark.indexOf(exp.exp[j]) > -1){
	    				result *= 1;
	    			}
	    			else{
	    				result *= 0;
	    			}
	    		}
	    		
	    		if(result === 1){
	    			if(confirm('检测到您的订单来自"'+i+'",是否自动切换？或者识别有误，请立刻向我们反馈。')){
		    			$('#express').val(i)
	    			}
	    			hasChanged = true;
	    			break;
	    		}
	    	}
	    	
	    	if(!hasChanged){
	    		alert('未检测到您的订单来自哪个公司。-_-，请手动选择快递公司，或者识别有误，请立刻向我们反馈。');
	    			//$('#express').val('其它公司');
	    	}
    	}catch(e){
    		alert(e)
    	}
    });
}
$(function() {
	try{
		launch();
	}catch(e){
		alert(e)
	}	
	
});
function changeAddr(){
	window.addressType = 2;
	$('#addr_sel').focus();
	$('#selectAddress').show();
	$('#defaultAddress').hide();
}
function freeOrder(){
	//获取当前积分
	var point  = '${me.point}';
	if(!point){
		alert('Sorry，您还没有登录。登录即可享受积分优惠~');
	}
	else{
		if(parseInt(point) >= 200){
			if(confirm('您当前拥有积分'+point+'，确定扣取200积分换取一次免单机会吗？')){
				$('#pointForFree').html('<span class="gray">该订单将使用200积分换取免单机会</span><input type="hidden" name="point4free" value="true"/>');
			}
		}
		else{
			alert('Sorry，您当前积分'+point+"，要有200积分才能免单哦~");
		}
	}
}
</script>
<img src="/static/img/success.jpg" style="display: none;"/>
</body>
</html>
