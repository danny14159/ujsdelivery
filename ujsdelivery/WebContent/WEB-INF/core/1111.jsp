<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<title>${initParam.project_name } - 5·20幸运大转盘，100%有奖</title>
<style type="text/css">
body { font-family: "Microsoft Yahei"; background-color: #15734b; margin: 0; padding: 0;}
ul{list-style: none;}
h1 { margin: 10 auto; font: 16px "Microsoft Yahei"; text-align: center; color: #fff;}
.dowebok { height: 563px; margin: 0 auto;}
.rotary { position: relative;  height: 504px;text-align: center;}
.hand {width: 20%; cursor: pointer;position: relative;top:-40%;}
.list {  width: 80%;color:#fff;font-size: 12px; }
.list strong {display: block; line-height: 1.5em; font-size: 20px; color: #ffe63c;}
.list h4 { height: 45px; margin: 30px 0 10px; line-height: 45px; font-size: 24px; color: #fff;}
.list ul { line-height:1.5em;list-style-type: none; font-size: 12px; color: #fff;}
.list span { display: inline-block; }
.list label{color: #ffe63c;}
.follow{background: #fff;width:100%;padding: 10px 0;margin-top: 10px;}
.follow img{width:80%;display: block;margin: 0 auto;}
.follow p{color:#999;width:100%;text-align: center;line-height: 1em;font-size: 12px;}
.shadow{display: none;background: rgba(0,0,0,0.7);width:100%;position: absolute;z-index: 1000;top:0;color:#FFF;}
.shadow img.arrow{float: right;margin-right: 20px;}
.shadow p{width: 100%;display: block;text-align: center;}
.shadow .share-text{margin-top: 146px;}
.shadow a{background: #fff;text-decoration: none;color:red}
</style>

</head>
<body>

<h1>
<c:if test="${drawTimes<=0 || empty drawTimes}">下单即可享有一次抽奖机会噢~

<div class="follow">
	<img src="/static/img/pressfollow.jpg"/>
	
	<p>长按指纹识别二维码 带你体验不一样的“${initParam.project_name }”</p>
</div>
</c:if>
<c:if test="${drawTimes>0 }">您的订单已经成功提交！试试手气吧:)</c:if>
</h1>
<div class="dowebok">
	<marquee direction="up" scrollamount="3"   contenteditable="true">
	<ul class="list">
		<li>恭喜邓*，183****2373获得一等奖</li>
	</ul>
	</marquee>
	
	<div class="list">
		<strong>100%中奖</strong>
		<ul>
			<li><label>一等奖</label> <span>价值148元苏州周庄水乡一日游，共5位</span></li>
			<li><label>二等奖</label> <span>价值130元大型公仔，共4位</span></li>
			<li><label>三等奖</label> <span>价值25元剑龙钥匙扣 Or 暴暴龙零钱包，共16位</span></li>
			<li><label>四等奖</label> <span>价值15元恐龙爪零钱包，共40位</span></li>
			<li><label>幸运奖</label> <span>${initParam.project_name } 20-100积分，名额不限</span></li>
<!-- 			<li><label>注意事项</label><span>本活动秉持公正公开公平的原则，不允许用户出现“刷单”现象，一经发现回收刷单所得，情节严重者将会被永久封号。</span></li> -->
		<li><label>友情提示</label> &nbsp;为防止刷单，每人当日抽奖次数仅限一次。</li>
			<li>本次活动的最终解释权归“${initParam.project_name }”所有。</li>
		</ul>
	</div>
	<div class="rotary">
		<img src="/static/img/g2.png" style="margin: 0px auto;width:95%;display: block;" class="bg"/>
		<img class="hand" src="/static/img/z.png" alt="" >
	</div>
</div>
<div class="shadow">
	<img src="/static/img/arrow.png" class="arrow"/>
	<div class="share-text">
		<p>分享到朋友圈一起来试试手气吧~<img src="/static/img/emoji-happy.png"/></p>
		<p>或者查看<a href="/app/myback">我的订单</a></p>
	</div>
</div>

<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/jquery.rotate.min.js"></script>
<script>
function popShadow(){
	$('.shadow').height($(window).height()).show();
}
//popShadow();
$(function(){
	var $hand = $('.hand');
	var $bg = $('.bg');
	var times = '${drawTimes}';

	$hand.click(function(){
		if(!times || times<=0) return alert('很抱歉，您暂时还没有抽奖机会，下单即可享有一次抽奖机会噢~');
		$.post('/app/act/draw',function(data){
			if(!data.ok){
				alert(data.msg);
			}
			else{
				rotateFunc(1,220,data.msg);
			}
		},'json');
	});

	var rotateFunc = function(awards,angle,text){
		
		$hand.stopRotate();
		$hand.rotate({
			angle: 0,
			duration: 6000,
			animateTo: angle + 1440,
			callback: function(){
				window.scrollTo(0,0);
				alert(text);
				popShadow();
				//location.href="/app/myback";
			}
		});
	};
	
	//计算中间节点所在的位置
	
	$('.hand').css({
		bottom:($('.bg').width()+$('.hand').width())/2
	})
});
</script>


</body>
</html>