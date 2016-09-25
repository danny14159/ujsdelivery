<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>江大旅游 - 添加产品</title>
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<style type="text/css">
body{padding: 15px;font-family:'Microsoft Yahei';}
</style>
</head>
<body>

<form class="form-horizontal" id="main" action="/goods/ins" method="post" enctype="multipart/form-data" onsubmit="return OnAddGoods();">
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">产品标题：</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" name="title" placeholder="输入产品标题" required="required">
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">简单描述：</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" name="brief_descn" placeholder="输入产品的简单描述">
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">封面图片：</label>
    <div class="col-sm-10">
      <input type="file" class="form-control" name="coverpath">
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">地区：</label>
    <div class="col-sm-2">
      <select name="region_id" class="form-control" >
      	<c:forEach items="${rgns }" var="rgn">
      	<option value="${rgn.id }">${rgn.name }</option>
      	</c:forEach>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">分类：</label>
    <div class="col-sm-2">
      <select name="category_id" class="form-control" >
      <c:forEach items="${cates }" var="cate">
      		<option value="${cate.id }">${cate.name }</option>
      	</c:forEach>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">详细描述：</label>
    <div class="col-sm-10">
      <textarea class="form-control" name="detail_descn"></textarea>
    </div>
  </div>
  <div class="form-group">
    <label for="" class="col-sm-2 control-label">最低价格：</label>
    <div class="col-sm-10">
      <input type="number" class="form-control" name="lowest_price" placeholder="" required="required">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-success">提交</button>
    </div>
  </div>
  
</form>

<script type="text/javascript" src="/static/js/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="/static/js/kindeditor/zh_CN.js"></script>

<script type="text/javascript">

var editor;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="detail_descn"]', {
		resizeType : 1,
		uploadJson:'/app/tv/upload',
		items : [
			'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
			'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
			'insertunorderedlist', '|', 'emoticons', 'image', 'link']
	});
});
function OnAddGoods(){
	$('textarea[name="detail_descn"]').val(editor.html());
	return true;
}
</script>
</body>
</html>