<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<!-- Modal -->
<div class="modal fade" id="mdlogin" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="$('#submitButton').attr('disabled',null).text('提交订单')"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">请登录</h4>
      </div>
      <div class="modal-body" id="mdbody">
        <form action="/app/login" method="POST" id="fmlog">

            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-user'></span>
                    </div>
                    <input class="form-control" type="text" name='phone' placeholder='注册电话号码' value="">
                </div>
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-tag'></span>
                    </div>
                    <input class="form-control" type="password" name='password' placeholder='密码' value="">
                </div>
            </div>
			<input type="hidden" id="phone" value="${user.phone }">
        </form>
            <input class="btn btn-success btn-block" type="button" value='登录' onclick="onLogin();">
            <input class="btn btn-warning btn-block" type="button" value='新用户入口->' onclick="onSignup();">
      </div>
      
      <iframe name="fmsignup" id="fmsignup" style="display: none;width:100%;height: 400px;border:none;" onload="onSignupCallback(window.frames['fmsignup'].location.href)"></iframe>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="$('#submitButton').attr('disabled',null).text('提交订单')">关闭</button>
      </div>
    </div>
  </div>
</div>
<script>
function onLogin(){
	$.get('/app/ajaxlogin?'+$('#fmlog').serialize(),function(data){
		if(data && data.ok){
			$('#mdlogin').modal('toggle');order();
		}
		else alert(data.msg);
	},'json');
}
function onSignup(){
	$('#myModalLabel').text('新用户注册');
	$('#mdbody').html('');
	$('#fmsignup').attr('src','/signup.jsp').show();
	//$.get('/signup.jsp','text')
	//.done(function(data){
	//});
}

function onSignupCallback(href){
	if(href && href.lastIndexOf("/app?info=1001") > 0){
		//注册成功
		//1.关闭对话框
		$('#mdlogin').modal('hide');
		//2.提交订单
		order();
	}
}
</script>