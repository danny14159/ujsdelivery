<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>${initParam.project_name }——评价建议</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    

    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <style>
    .bd-share-popup, .bd-share-popup li {
        box-sizing: content-box;
    }
    span.gray {
    color: gray;
}

.edit {
    margin: 20px 0px;
}

.edit select.form-control {
    width: 48%;
    float: left;
}


.edit select.form-control:first-child {
    margin-right: 4%;
}

form {
    margin-bottom: 15px;
}
    </style>
    

</head>
<body>
    
<link rel="stylesheet" href="/static/css/menu.css">
<div class='menu'>
    <a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
</div>

<div class="container" style="margin-top: 10px">
        <!--高速版，加载速度快，使用前需测试页面的兼容性-->
<div id="SOHUCS" sid="d0229d9c-7c89-11e4-ac65-00163e0017ad"></div>
<script>
  (function(){
    var appid = 'cyrDdnQvN',
    conf = 'prod_fa1e6f5ab0efc226fb432323bcfc78a6';
    var doc = document,
    s = doc.createElement('script'),
    h = doc.getElementsByTagName('head')[0] || doc.head || doc.documentElement;
    s.type = 'text/javascript';
    s.charset = 'utf-8';
    s.src =  'http://assets.changyan.sohu.com/upload/changyan.js?conf='+ conf +'&appid=' + appid;
    h.insertBefore(s,h.firstChild);
    window.SCS_NO_IFRAME = true;
  })()
</script>                

    </div>

<script type='text/javascript' src='/static/js/jquery.min.js'></script>
<script type='text/javascript' src='/static/js/bootstrap.min.js'></script>
<script type='text/javascript'>
function onModify(){
	$.post('/u/upd',{name:$('#name').val(),address:$('#address').val()},function(data){
		if(data){
			alert("修改成功！");location.reload();
		}
		else alert('修改失败，请稍后再试！');
	},'json')
}
</script>

</body>
</html>

