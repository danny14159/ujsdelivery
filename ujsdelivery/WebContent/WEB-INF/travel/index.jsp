<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>江大团游 - 江大学子首选的旅游平台</title>
<meta name="HandheldFriendly" content="true">
<meta name="MobileOptimized" content="width">
<meta name="viewport" id="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="applicable-device"content="mobile">
<meta name="format-detection" content="telephone=no">
<link href="/static/css/travel.css" rel="stylesheet"/>
<style type="text/css"></style>
</head>
<body>
<header class="search_header" id="J_header-search">
           <div class="search_bar">
               <div class="change">
                   <div class="logo"></div>
                   <div class="search_cancel" onclick="cancelSearch();">取消</div>
               </div>
               <span class="person icon-category"></span>
               <div class="center">
                   <a class="gps" id="city" href="javascript:;">镇江</a>
                <input type="hidden" id="nowGPSCityCode" value="1626">
                <input type="hidden" id="nowGPSCityLetter" value="zhj">
            
                   <input type="text" placeholder="西津渡" class="J_hs-input">
                   <span class="search_clear icon-delete"></span>
                   <span class="search_button icon-search-big"></span>
               </div>
           </div>
           <div class="search_pop">
               <div class="search-result no-result J_hs-result hide">
               </div>
               <div class="search_hot J_hs-hotword">
                   <div class="title">热门搜索</div>
                   <ul class="keys clearfix"><li><div class="item">泰国</div></li><li><div class="item">香港</div></li><li><div class="item">欧洲</div></li><li><div class="item">马尔代夫</div></li><li><div class="item">巴厘岛</div></li><li><div class="item">九寨沟</div></li><li><div class="item">三亚</div></li><li><div class="item">厦门</div></li><li><div class="item">西藏</div></li><li><div class="item">黄山</div></li></ul>
               </div>
           </div>
           <ul class="search_suggestion J_hs-suggestion hide">
           </ul>
       </header>
       
       <!-- 轮播图开始 -->
<div class="main_visual">
	<!-- <div class="flicking_con">
		<a href="#">1</a>
		<a href="#">2</a>
		<a href="#">3</a>
		<a href="#">4</a>
		<a href="#">5</a>
	</div> -->
	<div class="main_image">
		<ul>
			<a href="/intro.jsp"><li><span class="img_4"></span></li></a>
			<a href="/app/tv/view/7"><li><span class="img_1"></span></li></a>
			<a href="/app/tv/view/6"><li><span class="img_2"></span></li></a>
			<!-- <a href="/app/tv/view/7"><li><span class="img_3"></span></li></a>
			<a href="/app/tv/view/6"><li><span class="img_5"></span></li></a> -->
		</ul>
		<a href="javascript:;" id="btn_prev"></a>
		<a href="javascript:;" id="btn_next"></a>
	</div>
</div>
    
       <!-- 轮播图结束 -->
       
<div class="ad_active_list">
        <ul class="clearfix">
                        <!-- <li class="thin-border">
                <a href="javascript:;" node-type="周边游" data-type="ga">
                    <img src="/static/img/zby.png">
                </a>
            </li>
                        <li class="thin-border">
                <a href="javascript:;" node-type="国内游" data-type="ga">
                    <img src="/static/img/gny.png">
                </a>
            </li>
                        <li class="thin-border">
                <a href="javascript:;" node-type="出境游" data-type="ga">
                    <img src="/static/img/cjy.png">
                </a>
            </li>
                        <li class="thin-border">
                <a href="javascript:;" node-type="当地玩乐" data-type="ga">
                    <img src="/static/img/ddwl.png">
                </a>
            </li> -->
                    </ul>
    </div>
       
<ul class="product-list current" data-loaded="true">
   
  <c:forEach items="${list }" var="item">         
   <li class="m-product-item">
       <a href="/app/tv/view/${item.id }">
       <div class="item-top">
           <div class="product-image">
               <img data-src="/static/img/item0.jpg" src="${item.coverpath }">
           </div>
           
 		<div class="product-price price-schema-1">
               <div class="product-promotion">立省</div>
               <div class="separator"></div>
               <div class="product-price-now" style="font-size: 10px;"><%-- ￥${item.lowest_price } --%>附赠${initParam.project_name }5000积分</div>
               <%-- <div class="product-price-original">￥${item.lowest_price  + 50}</div> --%>
           </div>
           
       </div>
       <div class="item-bottom">
               <h5 class="product-title">${item.title }</h5>
               <p class="product-describe">
                  ${item.brief_descn }
               </p>
       </div>
       </a>
   </li>
      </c:forEach>      
            
            </ul>      
       
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.event.drag-1.5.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.touchSlider.js"></script>
<script type="text/javascript">
$(document).ready(function(){

/* 	$(".main_visual").hover(function(){
		$("#btn_prev,#btn_next").fadeIn()
	},function(){
		$("#btn_prev,#btn_next").fadeOut()
	});
	 */
	$dragBln = false;
	
	$(".main_image").touchSlider({
		flexible : true,
		speed : 400,
		btn_prev : $("#btn_prev"),
		btn_next : $("#btn_next"),
		paging : $(".flicking_con a"),
		counter : function (e){
			$(".flicking_con a").removeClass("on").eq(e.current-1).addClass("on");
		}
	});
	
	$(".main_image").bind("mousedown", function() {
		$dragBln = false;
	});
	
	$(".main_image").bind("dragstart", function() {
		$dragBln = true;
	});
	
	$(".main_image a").click(function(){
		if($dragBln) {
			return false;
		}
	});
	
	timer = setInterval(function(){
		$("#btn_next").click();
	}, 3000);
	
	$(".main_visual").hover(function(){
		clearInterval(timer);
	},function(){
		timer = setInterval(function(){
			$("#btn_next").click();
		},5000);
	});
	
	$(".main_image").bind("touchstart",function(){
		clearInterval(timer);
	}).bind("touchend", function(){
		timer = setInterval(function(){
			$("#btn_next").click();
		}, 3000);
	});
	
	
	//搜索栏
	$('.J_hs-input').focus(function(){
		$('.search_pop,.icon-delete,.search_button,.search_cancel,.search_header .hide').show();
		$('.search_header').addClass('search');
	});
});

function cancelSearch(){
	$('.search_pop,.icon-delete,.search_button,.search_cancel,.search_header .hide').hide();
	$('.search_header').removeClass('search');
}

</script>
</body>
</html>