$(function() {
    var $form = $('form');
    var $submit = $form.find('input[type=submit]');
    var form = $form[0];
    var phoneValidator = /^\d{11}$/;
    
	
    $form.bind('submit',function(e) {
        e.preventDefault();
        
        var addr = getAddr();
        $('input[name=addr_region]').val(addr[0]);
        $('input[name=addr_building]').val(addr[1]);

/*        if (!form.name.value) {
            return alert("请输入姓名");
        }*/

        if (!form.phone.value) {
            return alert("请输入手机号码");
        }

/*        if (!isAddrValid()) {
            return alert("请输入有效的宿舍地址");
        }*/

        if (!phoneValidator.test(form.phone.value)) {
            return alert("请输入正确格式的手机号码 : )");
        }
/*        if(!form.password.value) return alert('请填写密码');
        if(form.password.value != form.passwordc.value){
        	return alert('密码确认不一致');
        }*/
        
        //showresend();
        $("#address").val(getAddr());
        
        $submit.val($submit.attr('data-loading-text')).attr('disabled','disabled');
        
        form.submit();
    });
});
function subFrom(){
	var form = $('form')[0];
	form.submit();
}
function showresend(){
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
    $('#dispcount').show(200);
    
    $.get('/app/regi',{phoneNum:$('#phone').val()},function(data){
    	if(data && data.ok){
    		$('form').unbind('submit');
    	}
    	else alert(data.ok);
    },'json');
}
