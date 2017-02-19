<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.express.core.module.SystemModule,com.express.core.bean.User" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>${initParam.project_name }</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<style type="text/css">
a:hover{text-decoration: none!important;}
#wrapper{width:94%;margin: 0 auto;/* background: url(/static/img/back.jpg) */}
#banner{
	margin-bottom: 25px;
}
.clear { 
	clear:both; 
}
.row{width:100%;}
.option{line-height: 150px;font-size: 1.5em;color:#fff;
padding-left:30px;padding-top:34px;padding-bottom:4px;
	display:block;
}
.fr{float: right;}
.fl{float: left;}
.row{margin:0 0 24px;margin-left: 0;margin-right: 0;}
.row div{background-size:50px!important;}
.row .green{
	background:url(/static/img/sign.png) 90% 50% no-repeat #00aeef;
}
.row .red{background:url(/static/img/send.png) 90% 50% no-repeat #f67542}
.row .orange{background: url(/static/img/guohezi.gif) 90% 50% no-repeat #f8a900;}
.row .blue{background: url(/static/img/profile.png) 90% 50% no-repeat #01cf97}
.logout{font-size: 2em}
.alert-info {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #bce8f1;
}
.alert {
  padding: 15px;
  margin-bottom: 20px;
  border: 1px solid transparent;
  border-radius: 4px;
}
.logo{width:250px;margin: 10px auto; }
.logo img{width:100%;}
</style>

</head>
<body>
<div class="logo">
	<img src="/static/img/logo${state }.jpg"/>
<h3>${info }</h3>
</div>
<div id="wrapper">
<!-- 	<div id="banner">
		<img src="/static/img/back.jpg" width="100%"/>
	</div> -->
	<div class="row">
		<a href="/app/sign"><div class="option  green"><h3>代领快递</h3></div></a>
	</div>
	<div class="row">
 		<a href="/app/send"><div class="option  red"><h3>代寄快递</h3></div></a>
 	</div>
	<div class="row">
		<a href="/app/myback"><div class="option  orange"><h3>我的快递</h3></div></a>
	</div>
	<div class="row">
		<a href="/app/comments"><div class="option  blue"><h3>评价建议</h3></div></a>
	</div>
</div>
<%-- <center>Copyright&copy;2015- 镇江一里信息科技有限公司 版权所有</center> --%>
</body>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>

<div class="bdsharebuttonbox"><a href="#" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a></div>
<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"16"},"share":{}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>
</html>