<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>用户登录</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/menu.css">
    <style type="text/css">#resend.label-primary:hover{cursor: pointer;}</style>
    </head>
    <body>
<div class='menu'>
    <a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
</div>
<div class="container login">
    <div class="edit">
        <form action="/app/login" method="POST">

			<input type="hidden" name="redirect" value=${redirect }/>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-user'></span>
                    </div>
                    <input class="form-control" type="text" name='phone' placeholder='注册电话号码' value="" id="phone">
                </div>
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-tag'></span>
                    </div>
                    <input class="form-control" type="password" name='password' placeholder='密码' value="" id="password">
                </div>
            </div>
            <input type="checkbox" name="rememberMe" checked="checked"/>记住我

            <input class="btn btn-success btn-block" type="submit" value='登录' onclick="storeCookie($('#phone').val(),$('#password').val())"
                    data-loading-text='登录...'>
                    <hr/>
            <p><a href="/signup.jsp" style="font-size: 2em;">只需3秒，完成注册</a>
            <button class="btn btn-default btn-xs" onclick="forgetpassword();" type="button">忘记密码</button>
            </p>
        </form>

    </div>
</div>

<div id="reset-modal" class="modal fade">
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
                    新的密码：<input type="password" id="rnewpass" />
                </div>
                <div class="input-group">
                    密码确认：<input type="password" id="cnewpass" />
                </div>
                <div class="input-group">
                    <button class="btn btn-xs btn-primary" onclick="nextstep();">下一步</button>
                </div>
            </div>
				</div>
			</div>
		</div>
	</div>

<script type='text/javascript' src='/static/js/jquery.min.js'></script>
<script type='text/javascript' src='/static/js/bootstrap.min.js'></script>
<script type='text/javascript' src='/static/js/jquery.cookie.js'></script>
<script type="text/javascript">

function forgetpassword(){
	var phone = $('#phone').val();
	if(!phone || !phone.match(/^1[3|4|5|8][0-9]\d{4,8}$/)){
		$('#reset-modal').modal('hide');
		alert('请在登录窗口输入有效的手机号码，再点击“忘记密码”按钮！');
	}
	else{
		$('#reset-modal').modal();
	}
}
function showresend(btn){
	
	$('#resend').removeClass('label-primary').addClass('label-default');
    $('#md-phone').text($('#phone').val());
    if(window.iv_count) clearInterval(window.iv_count);
    var total = 60;
    $('#countdown').text(total);
    window.iv_count = setInterval(function(){
    	$('#countdown').text(--total);
    	if(total<=0){
    		clearInterval(window.iv_count);
    		$('#countdown').text('');
    		$('#resend').removeClass('label-default').addClass('label-primary');
    	}
    },1000);
    $(btn).hide(200,function(){
	    $('#dispcount').show(200,function(){
	    	$.get('/app/regi',{phoneNum:$('#phone').val()},function(data){
		    	if(data && data.ok){
		    		$('form').unbind('submit');
		    		alert('短信验证码已发送，请注意查收！');
		    	}
		    	else alert(data.msg);
		    },'json');
	    });
    });
    
}
function nextstep(){
	var msg = $('#msg').val();
	if(!msg){ alert('请输入手机收到的验证码！');return;}
	if(!$('#rnewpass').val()){ alert('请输入新密码！');return;}
	if(!$('#rnewpass').val() == $('#cnewpass').val() ){ alert('两次密码输入不一致！');return;}
	$.get('/app/resetpass',{phone:$('#phone').val(),msg:msg,newpass:$('#rnewpass').val()},function(data){
		if(data){
			alert('修改成功，请输入新密码登录');
			$('#reset-modal').modal('hide');
		}
		else alert("系统繁忙，请稍后再试！");
	},'json');
}
function storeCookie(phone,password){
	$.cookie('YHM', phone, {expires: 7*365, path: '/'});
	$.cookie('MIM', password, {expires: 7*365, path: '/'});
}
</script>
</body>
</html>