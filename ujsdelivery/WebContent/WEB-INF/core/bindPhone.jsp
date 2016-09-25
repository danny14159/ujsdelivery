<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<link rel="stylesheet" href="/static/css/menu.css">
<style type="text/css">
.content{height: 35px;width: 100%;line-height: 35px}
</style>
<title>手机绑定</title>
</head>
<body>
	<div class='menu'>
		<a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
	</div>
	<h2 class="content">${info }
			
	</h2>

	<div class="form-group">
		<div class="input-group">
			<div class="input-group-addon">
				<span class='glyphicon glyphicon-signal'></span>
			</div>
			<input class="form-control" type="text" name='phone'
				placeholder='唯一手机号码，请仔细核对再提交' value="${phone }" id="phone">
		</div>
	</div>
	
	<div id="reset-modal" class="" style="display: none;">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<button onclick="showresend(this);" class="btn btn-primary">点击发送短信验证码</button>
					<div class="form-group" id="dispcount" style="display: none;">
                <p>
                   <span class="text-info">我们已向您的手机号：<strong id="md-phone"></strong>发送短信验证码。请在下方填写收到的验证码。</span><br/>
                </p>
                <div class="input-group">
                    短信验证码：<input type="text" name="msg" id="msg"/>
				<span class="label label-primary" id="resend" >发送验证码<strong id="countdown"></strong></span>
                </div>
                <div class="input-group">
                    <button class="btn btn-xs btn-primary" onclick="nextstep();">下一步</button>
                </div>
                </div>
            </div>
				</div>
			</div>
		</div>
	
	<button onclick="bindPhone()" class="btn btn-primary"
				type="button">绑定手机</button>
	<script type="text/javascript" src="/static/js/jquery.min.js"></script>
	<script type="text/javascript">
		function bindPhone() {
			var phone = $('#phone').val();
			if (!phone || !phone.match(/^\d{11}$/)) {
				$('#reset-modal').hide();
				alert('请在登录窗口输入有效的手机号码，再重试一次！');
			} else {
				//$('#reset-modal').show();
				$.get('/app/authmobile', {
					phone : $('#phone').val()
					//msg : msg
				}, function(data) {
					if (data.ok) {
						alert('恭喜你身份认证成功');
						location.href = "/app"
					} else
						alert(data.msg);
				}, 'json');
			}
		}
		function showresend(btn) {

			$('#resend').removeClass('label-primary').addClass('label-default');
			$('#md-phone').text($('#phone').val());
			if (window.iv_count)
				clearInterval(window.iv_count);
			var total = 60;
			$('#countdown').text(total);
			window.iv_count = setInterval(function() {
				$('#countdown').text(--total);
				if (total <= 0) {
					clearInterval(window.iv_count);
					$('#countdown').text('');
					$('#resend').removeClass('label-default').addClass(
							'label-primary');
				}
			}, 1000);
			$(btn).hide(200, function() {
				$('#dispcount').show(200, function() {
					$.get('/app/regi', {
						phoneNum : $('#phone').val()
					}, function(data) {
						if (data && data.ok) {
							$('form').unbind('submit');
							alert('短信验证码已发送，请注意查收！');
						} else
							alert(data.msg);
					}, 'json');
				});
			});

		}
		function nextstep() {
			var msg = $('#msg').val();
			if (!msg) {
				alert('请输入手机收到的验证码！');
				return;
			}
			//if(!$('#rnewpass').val()){ alert('请输入新密码！');return;}
			//if(!$('#rnewpass').val() == $('#cnewpass').val() ){ alert('两次密码输入不一致！');return;}
			$.get('/app/authmobile', {
				phone : $('#phone').val(),
				msg : msg
			}, function(data) {
				if (data.ok) {
					alert('恭喜你身份认证成功');
					location.href = "/app"
				} else
					alert(data.msg);
			}, 'json');
		}
	</script>
</body>
</html>