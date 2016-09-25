<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/static/css/menu.css">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<link rel="stylesheet" href="/static/css/dashboard.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<style>
body{font-size: 12px;font-family: 'Microsoft YaHei';}
iframe{border: none;padding: 0;margin: 0;width: 100%;height: 600px;}
</style>
<title>江大旅游 - 后台管理</title>
</head>
<body>

	<div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页&nbsp;&nbsp;&nbsp;欢迎你，${me.name }</a>
	</div>

<ul class="nav nav-tabs" style="margin-top:15px; ">
  <li role="presentation"><a href="/app/back?type=sign">代取快递</a></li>
  <li role="presentation"><a href="/app/back?type=send">代寄快递</a></li>
	<li role="presentation" class="active"><a href="/app/back/tv">江大旅游</a></li>
</ul>

<script type="text/javascript">
$(function(){
	$('.nav.nav-sidebar li').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	});
})
</script>

<div class="container-fluid">
      <div class="row">
        <div class="col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li><a href="/app/back/tv/orders" target="main">订单管理</a></li>
            <li><a href="/app/back/tv/listgoods" target="main">旅游产品查询</a></li>
            <li><a href="/app/back/tv/addgoods"target="main" >添加旅游产品</a></li>
            <li><a href="/app/back/tv/categories" target="main">分类管理</a></li>
            <li><a href="/app/back/tv/regions" target="main">区域管理</a></li>
          </ul>
        </div>
        <div class="col-md-10">
			<iframe name="main" onLoad="iw=this.contentWindow;$(this).height(iw.$(iw.document).height())" id="main"></iframe>
        </div>
      </div>
    </div>

</body>
</html>