<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>${initParam.project_name }——订单分析</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

    

    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <style>
.main{
	margin: 15px auto;
	width:1100px;
}
    </style>
    

</head>
<body>
  <div class="main">
<p class="text-info">建议在电脑环境下观看，以便获得最佳浏览体验</p>
<form action="" method="get" id="fm">
<h1>${initParam.project_name }
<select name="year"id="year">
<option>2016</option>
<option>2017</option>
</select>
年
<select name="month" onchange="document.getElementById('fm').submit();" id="month">
<option></option>
<option>1</option>
<option>2</option>
<option>3</option>
<option>4</option>
<option>5</option>
<option>6</option>
<option>7</option>
<option>8</option>
<option>9</option>
<option>10</option>
<option>11</option>
<option>12</option>
</select>
月订单折线统计图
</h1>
</form>


    <div id="canvas"></div>
    
    <h1>24小时下单量统计折线图</h1>
    <div id="ordersPerHour"></div>
</div>  
    
    <script src="/static/js/acharts-min.js"></script>
   <script type="text/javascript">
   function getCata(cdays){
   	var cata = [];
   	for(var i = 0;i<cdays;i++){
   		cata.push(i+1);
   	}
   	return cata;
   }
   </script>
   
   
   <c:if test="${!empty year && !empty month }">
    <script type="text/javascript">
    var cdays = '${cdays}'||30;
    document.getElementById('year').value='${year}';
    document.getElementById('month').value='${month}';
    
    if('${year}' && '${month}'){
	    new AChart({
	        theme : AChart.Theme.SmoothBase,
	        id : 'canvas',
	        width : 1100,
	        height : 500,
	        plotCfg : {
	          margin : [50,50,80] //画板的边距
	        },
	        xAxis : {
	          categories : getCata(cdays)
	        },
	        tooltip : {
	          valueSuffix : '单',
	          shared : true, //是否多个数据序列共同显示信息
	          crosshairs : true //是否出现基准线
	        },
	        series : [{
	            name: '代取订单',
	            data: ${csignList }
	        }, {
	            name: '代寄订单',
	            data: ${csendList }
	        }]
	    }).render();
    }
</script>
</c:if>

<script type="text/javascript">
new AChart({
    theme : AChart.Theme.SmoothBase,
    id : 'ordersPerHour',
    width : 1100,
    height : 500,
    plotCfg : {
      margin : [50,50,80] //画板的边距
    },
    xAxis : {
      categories : getCata(24)
    },
    tooltip : {
      valueSuffix : '单',
      shared : true, //是否多个数据序列共同显示信息
      crosshairs : true //是否出现基准线
    },
    series : [{
        name: '代取订单',
        data: ${numsOf24h }
    }]
}).render(); 
</script>
</body>
</html>

