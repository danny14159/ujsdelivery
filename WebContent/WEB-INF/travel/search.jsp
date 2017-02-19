<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>江大团游 - 旅游列表</title>
<meta name="HandheldFriendly" content="true">
<meta name="MobileOptimized" content="width">
<meta name="viewport" id="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="applicable-device"content="mobile">
<meta name="format-detection" content="telephone=no">
<link href="/static/css/travel.css" rel="stylesheet"/>
<style type="text/css">
.header-box {
    height: 50px
}
</style>
</head>
<body>
<div class="header-box app-header">
     <header class="fixed">
         
     <span class="header-back back">
         <i class="icon-title-back effect-active" onclick="history.go(-1)"></i>
     </span>
         <span class="person icon-category"></span>
         <div class="title">${param.key } 产品推荐</div>
         <div class="sub_title">${recordCount }个结果</div>
     </header>
</div>
        
        <!-- 搜索结果 -->
        <div class="search_result" id="box">
	<div class="sort-box" id="sort" style="display: block;">
		<div class="sort">
			<div class="scroll" id="scrollTab" style="overflow: hidden;">
				<ul class="moving" id="tab" style="transition-property: transform; transform-origin: 0px 0px 0px; transform: translate(0px, 0px) scale(1) translateZ(0px);">
 <!-- 放上所有的分类和搜索按钮 -->
 		<c:if test="${empty param.key}">
				<c:forEach items="${cates }" var="cate">
					<li data-url="" 
						<c:if test="${param.cid==cate.id }">class="active"</c:if>
					><a href="/app/tv/search?cid=${cate.id }">${cate.name }</a></li>
				</c:forEach>
		</c:if>
				<c:if test="${!empty param.key}"><li data-url="" class="active">搜索</li></c:if> 
 </ul>
			</div>
			<!-- <div class="drop_down icon-arrow-down" id="dropDown"></div>
			<div class="drop_box" id="dropBox"> -->
				
				<!-- <ul class="drop_ul">
				
				</ul> -->
				<div class="more-box">
				<div class="more" style="display: none;"></div>
				</div>
			</div>
		</div>
	</div>
	<a class="img-box" id="imgBox" href="" style="display: none;"><img src=""></a>
	<div class="result">
		<div class="add"></div>
		<div id="hasResult" style="display: block;">
		<!-- 搜索结果 -->
			<ul id="result">
		
		<c:forEach items="${list }" var="item">
			<a href="/app/tv/view/${item.id }">
				<li class="drive">
					<div class="img">
						<img src="${item.coverpath }" width="85px" height="85px">
					</div>
					<div class="title">
						<span class="category">组团</span>&lt;${item.title }
					</div>
					
						<div class="basic_info">
							<span class="people icon-people">${item.brief_descn }</span>
							<!-- <span class="satisfaction icon-like_nor">95%满意</span> -->
						</div>
					
					<!-- <div class="advanced_info">

						<div class="price">
							<span>￥<strong>225</strong></span>起
						</div>
					</div> -->
				</li>
			</a>
		</c:forEach>
	</ul>

		</div>
		
		<c:if test="${fn:length(list) eq 0}">
			<div id="empty" class="empty">
				<img src="/static/img/question.png" width="100" height="100" alt="" class="empty-img">
				<div class="empty-title">暂时没有搜索结果</div>
				<div class="empty-content">删除筛选条件或扩大搜索范围，您一定会找到合适您的产品</div>
				<div class="research-btn" id="researchBtn">
					重新搜索
				</div>
			</div>
		</c:if>
	</div>
</div>

<!-- 展现商品列表 -->
<c:forEach items="${list }" var="item">
	${item.id }-${cate.title }
</c:forEach>
</body>
</html>