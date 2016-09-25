<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<c:set var="hasLogin" value="${me != null }"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
	
    <title>${initParam.project_name } - <c:if test="${hasLogin }">个人信息</c:if><c:if test="${!hasLogin }">首次使用</c:if></title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/menu.css">
    <style type="text/css">
    #resend.label-primary:hover{cursor: pointer;}
    .container{margin-top: 15px;}
    </style>
    </head>
    <body>
<div class="container">
    <div class="edit">
        <form action="/app/signup" method="POST" onsubmit="return submitForm(this)">
        	<c:if test="${!hasLogin }">
        		<h4 style="height: 4em;line-height: 4em;"><a>距离成功就差这一步了~</a></h4>
        	</c:if>
        	<c:if test="${!empty info }">
	            <div class="alert1 alert-danger1" role="alert" id="info" style="margin-top: 15px;">
				  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				  <span class="sr-only">Error:</span>
				  <span class="content">${info }
				  <%-- <c:if test="${code==10086 }">
				 	 <button onclick="forgetpassword()" class="btn btn-primary btn-xs" type="button">点我发送短信认证</button>
				  </c:if> --%>
				  </span>
				</div>
			</c:if>
            
            <div class="form-group clearfix" style="display: none;">
                    <select class="form-control" name="univ">
                        <option>江苏大学</option>
                    </select>
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-signal'></span>
                    </div>
                    <input class="form-control" type="text" name='phone' 
                    	<c:if test="${hasLogin }">disabled</c:if>
                    placeholder='唯一手机号码，填写之后不可更改' value="${me.phone }${phone}" id="phone">
                </div>
            </div>
            
            <c:if test="${profile == true }">
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon">
                        <span class='glyphicon glyphicon-user'></span>
                    </div>
                    <input class="form-control" type="text" name='name' placeholder='姓 名' value="${me.name }">
                </div>
            </div>

			<div class="form-group">
				<div class="input-group input-group-sm">
					<select class="form-control input-sm" style="width:120px;" id="addr_sel"></select>
					<select class="form-control input-sm" style="width:120px;display: none;" id="addr_sel_dorm"></select>
					<input class="form-control" type="text" placeholder='请填写其他区域' value="" style="display:none;" id="addr_sel_other">
				</div>
				<input name="address" type="hidden" id="address"/>												
			</div>
                <input type="hidden" name="addr_region">
                <input type="hidden" name="addr_building">
            </c:if>

            <input class="btn btn-success btn-block" type="submit" value="
			<c:if test="${profile == true }">
	            修改信息
            </c:if>
			<c:if test="${profile != true }">
	            手机认证
            </c:if>" data-loading-text='正在处理，请稍后...'>
                    
                    
        </form>

    </div>
</div>


<script type='text/javascript' src='/static/js/jquery.min.js'></script>
<script type='text/javascript' src='/static/js/bootstrap.min.js'></script>
<script type='text/javascript' src='/static/js/common.js'></script>
<script type='text/javascript' src='/static/js/profile.js'></script>
<script type="text/javascript">
function submitForm(form){
	 if (!isAddrValid()) {
         alert("请输入有效的宿舍地址");
         return false;
     }
	return true;
}
$(function(){
	renderAddress(addrRegion,'栋')
});
</script>
</body>
</html>