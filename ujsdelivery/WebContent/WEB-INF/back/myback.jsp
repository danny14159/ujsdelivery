<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.express.core.module.SystemModule,com.express.core.bean.User,java.util.Date" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>${initParam.project_name } - 我的订单</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

     

    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/recent.css?v=0.01">
    <style>
    .bd-share-popup, .bd-share-popup li {
        box-sizing: content-box;
    }
.blue{color:#428bca;}
.red{color:red;}
.board{
	background: #FFEFF4;
  padding: 15px;
  margin-left: 0px;
  margin-right: 0px;
  border: 1px solid #FDD2E0;
  border-radius: 8px;
  color:#891313;
}
.info, .order{margin: 10px 0}
    </style>
     


<link rel="stylesheet" href="/static/css/menu.css">

</head>
<body>
    
<div class='menu'>
    <a class='btn btn-default' href='/app'><span class='icon-back'></span>首页</a>
</div>

<div class="container">
    <div class="info">
        
            <span class="username"><b>用户名:</b> ${me.name }${signed }</span>
        
       <!--  <span class="label label-success">余额: ￥0.0</span> -->
       
       <c:if test="${project_name == '江大快递' }">
       
        <c:if test="${signed == true }">
        	<span class="label label-success" onclick="register(this)">签到</span>
        </c:if>
         <c:if test="${!signed }">
         	<span class="label label-default" onclick="register(this)">签到</span>
         </c:if>
         
         </c:if>
         
        <span class="label label-primary">积分: ${me.point }</span>
    </div>
    
    <p class="board">${initParam.project_name }积分说明：下单得10积分，每日签到得5积分，评论已完成的订单得10积分，攒满200积分即可换取一次免单（超过2元的免单最高可免4元！）机会。</p>

    <div class="order">
        <a href='/app/profile' class="history btn btn-default">个人信息</a>
       <!--  <a href='/rule.jsp' class="rule btn btn-default">余额积分规则</a> -->
        <!-- <a href='javascript:;' class="pass btn btn-default" onclick="modPass();">修改密码</a> -->
    </div>

    <a href="#anchor_sign"><button class="btn btn-default">我的代领订单</button></a>
    <a href="#anchor_send"><button class="btn btn-default">我的代寄订单</button></a>
    
    <div id="anchor_sign"></div>
    <c:forEach var="r" items="${list }" varStatus="status">
        <div class="sign order">
            <input name="orderid" value="1735" style="display:none">
            <div class="tips">订单号: ${r.id }</div>
            <p class='meta'>
                <b class="company-field"></b> 
                <span class="num-field">1&nbsp;件</span>&nbsp;&nbsp;

                
                <span class="status-field label-sm">
	<c:choose>  
    <c:when test="${r.state==83}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${r.state==80}"><span class="label label-warning">已取件</span></c:when>  
    <c:when test="${r.state==73}"><span class="label label-warning">取件中</span></c:when>  
    <c:when test="${r.state==69}"><span class="label label-primary">派送中</span></c:when>  
    <c:when test="${r.state==70}"><span class="label label-success">已完成</span>
    	<c:if test="${!empty r.comment }">
    		<span class="label label-default">已评价</span>
    	</c:if>
    	<c:if test="${empty r.comment }">
    		<button class="btn btn-xs btn-default" onclick="oncomment(this);">评价有礼</button>
    	</c:if>
    </c:when>  
    <c:when test="${r.state==68}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${r.state==67}"><span class="label label-default">用户取消</span></c:when>
    <c:otherwise>未知</c:otherwise>  
	</c:choose>
				</span>
				<c:if test="${r.state==83}">
                <span class="btn-danger label deletePackage" onclick="onCancel('/usign/cancelorder',${r.id});">取消订单</span>
                </c:if>
            </p>

            
            <div>收件人: ${r.sign_name }</div>
            <div>快递公司: ${r.express }
            <c:if test="${r.isFirst==89 }">（首）</c:if>
			<c:if test="${r.is_free == 89}">（免）</c:if>
            </div>
            <div>地址: 
            <c:out value="${r.addr_region }"></c:out>-<c:out value="${r.addr_building }"></c:out>
            </div>
            
                <div class="warning-large1 1blue">备注: 
                <span class="showRemark blue">
                <c:out value="${r.remark }"></c:out>
                </span>
               </div>
            
            

            <div class="tips">
            <fmt:formatDate value="${r.sign_time}" type="date" pattern="yyyy-MM-dd hh:mm:ss"/>
            &nbsp;送件时间：<c:out value="${r.send_time }"></c:out>
            </div>
            <div class="comment" style="display: none;">
            	<textarea rows="" cols="" placeholder="在这里写下您对本单的评论或建议，提交成功将获得10点${initParam.project_name }积分" class="form-control"></textarea>
         	   <button class="btn btn-xs btn-danger" onclick="submitComment(${r.id},this);">提交</button>
         	   <button class="btn btn-xs btn-default" onclick="cancel(this);">取消</button>
            </div>
            <c:if test="${!empty r.comment }">
            评价：<c:out value="${r.comment }"/>
            </c:if>
        </div>
    </c:forEach>
    <br>

    <h4 id="anchor_send">我的代寄订单</h4>
    
     <c:forEach var="r" items="${list2 }" varStatus="status">
        <div class="send order">
            <input name="orderid" value="91" style='display: none'>
            <div class="tips">订单号: ${r.id }</div>
            <p class='meta'>
                <b class="company-field"></b> 
                <span class="num-field">
                    1&nbsp;件
                </span>&nbsp;&nbsp;

                
                <span class="status-field label-sm">
				<c:choose>  
				   <%--  <c:when test="${r.state==83}"><span class="label label-warning">已提交</span></c:when>  
				    <c:when test="${r.state==80}"><span class="label label-success">已处理</span></c:when>  
				    <c:when test="${r.state==68}"><span class="label label-default">已删除</span></c:when>
				    <c:when test="${r.state==67}"><span class="label label-default">已手动取消</span></c:when> --%>
				    
				    <c:when test="${r.state=='S'}"><span class="label label-danger">已提交</span></c:when>  
    <c:when test="${r.state=='P'}"><span class="label label-info">已取件</span></c:when>  
    <c:when test="${r.state=='I'}"><span class="label label-warning">已发件</span></c:when>  
    <c:when test="${r.state=='F'}"><span class="label label-success">已完成</span></c:when>  
    <c:when test="${r.state=='D'}"><span class="label label-default">已删除</span></c:when>
    <c:when test="${r.state=='C'}"><span class="label label-default">用户取消</span></c:when>
				    <c:otherwise>未知</c:otherwise>  
				</c:choose>
                </span>
                <c:if test="${r.state!='C' && r.state!='D'}">
               <span class="btn-danger label deletePackage" onclick="onCancel('/usend/upd',${r.id});">取消订单</span>
                </c:if>
            </p>

            
           <!--  <div>快递单号: 未录入</div> -->
            <div>快递公司: ${r.express }</div>
            <div>地址: ${r.address }
            
            </div>
			<div class="warning-large">备注: 
                <c:out value="${r.remark }"></c:out>
                
               </div>
            <span class="tips">送件时间： <c:out value="${r.sign_time }"></c:out></span>
        </div>
    </c:forEach>
    <p class="warning">*注 本页最多显示您最近的七条订单记录</p>
    <a class="btn btn-danger btn-block" href="/app/logout">退出</a>
    <br>
</div>
    
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">签到成功，恭喜你获得5积分</h4>
      </div>
      <div class="modal-body">
        <table class="table table-condensed" id="sign_order">
  		<tr>
  			<th>#</th>
  			<th>用户名</th>
  			<th>签到时间</th>
  		</tr>
		</table>
		<p class="text-muted">签到排名从6点开始统计，每日前十签到和连续签到的用户将得到额外奖励</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/js/recent.js?v=0.01"></script>
<script type="text/javascript">
function fixString(str){
	if(str < 10){
		return '0'+str;
	}
	return str;
}
function register(span){
	$.get('/app/reg',function(data){
		if(data){
			/* alert(data.msg);
			if(data.ok)location.reload(); */
			$('#myModalLabel').html(data.msg);
			$('#sign_order tr:gt(0)').remove();
			for(var i =0;i<data.data.length;i++){
				var colors=['red','blue','orange'];
				var d = data.data[i];
				var dt = new Date(d.last_sign);
				$('<tr>')
				.append($('<td>').html(i+1).css('color',colors[i]))
				.append($('<td>').html(d.name))
				.append($('<td>').html( fixString(dt.getHours())+':'+fixString(dt.getMinutes())+':'+fixString(dt.getSeconds())+'.'+dt.getMilliseconds() ))
				.appendTo($('#sign_order'));
			}
			$('#myModal').modal('show');
		}
	},'json');
}
function modPass(){
	$('#mdpass-modal').modal();
}
function onmodpass(){
	var oldpass = $('#oldpass').val(),
	newpass = $('#newpass').val(),
	confirmpass = $('#confirmpass').val();
	if(newpass!=confirmpass) return alert("密码确认不一致！");
	$.get('/u/mdpass',{
		oldpass:oldpass,newpass:newpass
	},function(data){
		if(data) alert("修改成功！");
		else alert("修改失败，请稍后再试");
		$('#mdpass-modal').modal('hide');
	},'json');
}
function onCancel(url,id){
	if(confirm('确认取消订单？取消后将不可恢复。')){
		$.post(url,{
			state:'C',id:id
		},function(data){
			if(data.ok || data===1){
				location.reload();
			}
			else{
				alert('抱歉~'+data.msg);
			}
		},'json');
	}
}
$(function(){
	$('span.showRemark').each(function(i,ele){
		var _remark = $(ele).html().split('&amp;nbsp;');
		$(ele).html(_remark[0] + '<br/><span class="red">' +( _remark[1] || '')+ '</span>');
	});
	<c:if test="${param.state eq 'SIGN' }">register();</c:if>
})
function oncomment(btn){
	$(btn).closest('.order.sign').find('.comment').slideDown(300).find('textarea').focus();
}
function cancel(btn){
	$(btn).closest('.comment').slideUp(300);
}
function submitComment(id,btn){
	var cmt = $(btn).prev().val();
	if(!cmt) return alert('写点什么再发表吧');
	$.post('/usign/comment',{
		id:id,
		comment:cmt
	},function(data){
		if(data){
			alert('提交成功！');
			location.reload();
		}
		else{
			alert('提交失败');
		}
	},'json')
}
function onPay(orderNo){
	var gateway = '${gateway}';
	var redirect_url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx041974fbeae57af2&redirect_uri='+gateway+'/app/wechatp/unifiedorder/url/\
		&response_type=code&scope=snsapi_base&state='+orderNo+'#wechat_redirect';
	alert(redirect_url)
	location.href = redirect_url;
}
//onPay(0)
</script>


</body>
</html>

