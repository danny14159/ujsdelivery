<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>江大团游 - [${region.name }]${goods.title }</title>
<meta name="HandheldFriendly" content="true">
<meta name="MobileOptimized" content="width">
<meta name="viewport" id="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="applicable-device"content="mobile">
<meta name="format-detection" content="telephone=no">
<link href="/static/css/travel.css" rel="stylesheet"/>
<link href="/static/css/jquery.bootstrap-touchspin.min.css" rel="stylesheet" />
<!-- <link rel="stylesheet" href="/static/css/bootstrap.min.css"> -->
<style type="text/css">
.goods_cover{width:96%;margin:0 auto;position: relative;}
.arr_l,.arr_r{opacity:0.1;position: absolute;top:5em;width:3.4em;height: 3.4em;display: none;}
/* .arr_l{background: url('/static/img/arr_left.png');left:1em;background-size:3.4em;}
.arr_r{background: url('/static/img/arr_right.png');right:1em;background-size:3.4em;} */
.goods_cover img{width:100%;margin: 1em 0;display: block;}
.goods_title{background: #fff;padding: 1em;border: 1px solid #eee;}
.goods_title .icon-logo{width: 3.6em;
  height: 3.1em;
  background-size: 5.3em 9.4em;
  background-position: -0.82em -5.13em;
  margin: 0;float: left}
.goods_title h2{width:100%;font-size: 1.5em;float: right;position: relative;left:-1em;}
.goods_price{font-size: 1.5em;color:#FF6105;font-weight: bold;display: inline;}
.origin_price{color:#aaa;text-decoration: line-through;display: inline;}
.goods_desc{padding: 1em;}
.shade{position:absolute;width:150%;height: 1000px;top:0;left:0;background: rgba(0,0,0,0.4);}
.shade img{width:100%;}
button.buy-now{position: fixed;bottom: -0.2em;width:100%;left:0;max-width: 640px;border-radius:0;background-color: #fff;padding-top: 11px;padding-bottom: 11px;z-index: 1000}
button.btn-add-to-cart{position: fixed;bottom: -0.2em;width:50%;left:50%;max-width: 640px;border-radius:0;padding-top: 11px;padding-bottom: 11px;z-index: 1000}
button{background: #fff;border: 1px solid #FF6105;color:#FF6105}
button.active{background: #FF6105;border: 1px solid #FF6105;color:#fff;}
h2{font-weight: bold;font-size: 14px;}
.clear{clear: both;}
.brief{color:#999;}
body{margin-top: 41px;}
p,dd{line-height: 2em;}
img{width:100%!important;height:auto!important;margin: 0.5em 0}
.select-level{width:100%;bottom:0px;left:0;background: #fff;}
.shade-select{position: fixed;width: 100%;z-index: 499;display: none;opacity:0.5;top:0;background: #000;}
.level-item{display: inline-block;margin-left: 1em;border:1px solid #ccc;padding: 3px 15px;}
.select-level ul li{margin: 15px}
.select-level button{padding: 3px 15px;margin:0 15px 0 0;}
.level-item.active{color:#FF6105;border-color:#FF6105;}
.select-title{line-height: 2.1em;height: 2.1em;background: #eee;color:#999;padding-left: 30px;font-weight: bold;font-size: 1.5em;}
.totalPrice{color:#FF6105}
.select-level input{width:2em;padding: 5px;display: inline-block!important;border: none;}
.input-group.bootstrap-touchspin{display: inline-block;margin-left: 1em;}
</style>
<link href="/static/css/menu.css" rel="stylesheet"/>

<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.bootstrap-touchspin.min.js"></script>
</head>
<body>

<div class='menu'>
    <a class='btn btn-default text-warning' href='javascript:;' onclick="history.go(-1);"><span class='icon-back'></span>返回</a>
</div>

	<div class="goods_cover">
		<a class="arr_l" href="javascript:;" onclick="prevImage();"></a>
		<div id="renderImg"><img src="${goods.coverpath }" onclick="largeImage('${goods.coverpath}')"/></div>
		<a class="arr_r" href="javascript:;" onclick="nextImage();"></a>
	</div>
	<div class="goods_title">
		<div style="width: 95%;float: right;">
			<h2>${goods.title }</h2>
			<div class="brief">${goods.brief_descn }</div>
			<div class="goods_price">&yen;${goods.lowest_price }</div>&nbsp;起，已有58人预定
		</div>
		<div class="clear"></div>
		<div class="divider"></div>
		<button onclick="orderNow();" class="buy-now active">详情咨询</button>
		<!-- <button class="btn-add-to-cart" onclick="/*location.href='tel:15695111211'*/alert('联系电话：15695111211，qq2761059824')">联系客服</button> -->
	</div>
	<div class="goods_desc">
		
		<div class="select-level" id="select-level">
	<div class="select-title">请选择价格&nbsp;<span class="totalPrice"></span></div>
	<form class=""></form>
	<ul>
		<li style="color:red">预约后当天10点前会有工作人员与您联系，电话：<a href="tel:15695111211">15695111211</a>,QQ:<a href="javascript:;">2761059824</a></li>
		<li>
			<label>人数：</label>
			<input type="text" value="1" name="people_num"/>
		</li>
		<li class="guide">
			<label>车费：</label>
			<c:forEach items="${param1 }" var="item">
				<div class="level-item" data-value="${item.param_value }">${item.paramName }</div>
			</c:forEach>
			<input type="hidden" name="fare-level"/>
		</li>	
		<li class="fare">
			<label>门票：</label>
			<c:forEach items="${param2 }" var="item">
				<div class="level-item" data-value="${item.param_value }">${item.paramName }</div>
			</c:forEach>
			<input type="hidden" name="ticket-level"/>
		</li>	
		<li class="ticket">
			<label>导游：</label>
			<c:forEach items="${param3 }" var="item">
				<div class="level-item" data-value="${item.param_value }">${item.paramName }</div>
			</c:forEach>
			<input type="hidden" name="guide-level"/>
		</li>	
	</ul>
<!-- 	<button onclick="cancelSelect();" style="margin-left:80px; ">取消</button> -->
<!-- 	<button class="active ok" onclick="confirmOrder();">确认预定</button> -->
	<!-- <a href="#">有疑问？</a> -->
</div>
	${goods.detail_descn }
		<div class="clear" style="margin-bottom: 44px;"></div>
	</div>
<script type="text/javascript" src="/static/js/jquery.cookie.js"></script>
<!-- <script type="text/javascript" src="/static/js/index.js"></script>	 -->
	
<script type="text/javascript">

function largeImage(imgSrc){
	if(imgSrc)
		$('<div class="shade" onclick="closeLargeImage();"><img src="'+imgSrc+'"/></div>').appendTo('body');
}
function closeLargeImage(){
	$('.shade').fadeOut(300,function(){
		$(this).remove();
	});
}
function nextImage(){
	currentIndex = (currentIndex+1)%(pics.length+1);
	renderImage();
}
function prevImage(){
	currentIndex = (currentIndex-1 + pics.length+1)%(pics.length+1);
	renderImage();
}
function renderImage(){
	var img = $('<img>');
	if(currentIndex == 0) 
		img.attr({
			src:cover,
			onclick:'largeImage(\"'+largeCover+'\")'
		});
	else img.attr({
		src:pics[currentIndex-1]['pic_url']+'thumb640-425',
		onclick:'largeImage(\"'+pics[currentIndex-1]['pic_url']+'\")'
	});
	$('#renderImg').html(img);
}
function orderNow(){
	var login = '${me}';
	if(!login){
		location.href='/app/login?redirect=/app/tv/view/${goods.id}';
	}
	else{
		confirmOrder();
		//onSelect();
	}
}
function onSelect(){
	
	$('<div class="shade-select">').height($(document).height()).show().appendTo($('body'));
	$('.select-level').css({
		bottom:-1*$('.select-level').height(),
		display:'block'
	})/* .animate({
		bottom:0
	},300) */;
}
function cancelSelect(){
	$('.shade-select').remove();
	$('.select-level').animate({
		bottom:-1*$('.select-level').height()
	},300);
}

function getTotalPrice(){
	var param = getSelectedParam();
	return param[0]+param[1]+param[2]*param[3];
}
function getAvaragePrice(){
	return (getTotalPrice()/parseInt($('input[name="people_num"]').val()||0)).toFixed(2);
}
function getSelectedParam(){
	return [
		parseInt($('input[name="guide-level"]').val()||0),
		parseInt($('input[name="fare-level"]').val()||0),
		parseInt($('input[name="ticket-level"]').val()||0),
		parseInt($('input[name="people_num"]').val()||0),
	];
}
$(function(){
	$('.level-item').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		$(this).closest('li').find('input').val($(this).attr('data-value'));
		$('.totalPrice').text('人均￥'+getAvaragePrice());
	});
	$('input[name="people_num"]').change(function(){
		$('.totalPrice').text('人均￥'+getAvaragePrice());
	});
	
	$("input[name='people_num']").TouchSpin({
        min: 1,
        max: 100,
        step: 1//增量或减量
    });
	
	$('.guide .level-item:first').click();
	$('.fare .level-item:first').click();
	$('.ticket .level-item:first').click();
});
function confirmOrder(){

	$('body').animate({scrollTop:  $('body')[0].scrollHeight},'slow', function(){
	
		if(!confirm('确认预约？')) return;
		var guide = $('input[name="guide-level"]').val(),
			fare = $('input[name="fare-level"]').val(),
			ticket = $('input[name="ticket-level"]').val();
		if(!guide){return alert('请选择导游档');}
		if(!fare){return alert('请选择车费');}
		if(!ticket){return alert('请选择门票');}
		$.post('/app/tv/order',{
			gid:'${goods.id}',
			guide_level:guide,
			fare_level:fare,
			ticket_level:ticket
		},function(data){
			if(data.ok){
				alert('恭喜你预定成功，我们的客服将主动您联系！');location.href='/app/tv';
			}
			else {
				alert(data.msg);
			}
		},'json');
	
	});
	
}
</script>
</body>
</html>