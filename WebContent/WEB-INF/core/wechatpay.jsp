<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>在线支付跳转</title>
</head>
<body>
<div style="margin: 2em auto;width:60%">
	<img src="/assets/img/wechatlogo.png" width="100%" />
</div>
<script type="text/javascript" src="/assets/js/jquery.min.js"></script>
<script type="text/javascript">
function onBridgeReady() {
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : "${jsapiParam.appId}", //公众号名称，由商户传入     
		"timeStamp" : "${jsapiParam.timeStamp}", //时间戳，自1970年以来的秒数     
		"nonceStr" : "${jsapiParam.nonceStr}", //随机串     
		"package" : "${jsapiParam["package"]}",
		"signType" : "${jsapiParam.signType}", //微信签名方式：     
		"paySign" : "${jsapiParam.paySign}" //微信签名 
	}, function(res) {
		alert(JSON.stringify(res))
		if (res.err_msg == "get_brand_wcpay_request:ok") {
			alert('恭喜你，支付成功！');
			
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
		else{
			alert('支付失败');
		}
	});
}

if (typeof WeixinJSBridge == "undefined") {
	if (document.addEventListener) {
		document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
				false);
	} else if (document.attachEvent) {
		document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
		document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
	}
} else {
	onBridgeReady();
}
</script>
</body>
</html>
