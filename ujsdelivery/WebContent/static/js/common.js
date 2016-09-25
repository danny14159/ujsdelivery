var addrRegion={
		'选择宿舍地址':null,
		'A区':null,
		'B区':null,
		'C区':11,
		'D区':10,
		'E区':12,
		'F区':9,
		'G区':10,
		'老生楼':null,
		'其它区域':'input'
};
var addrRegion2={
		'选择宿舍地址':null,
		'宿舍楼':11,
		'教学楼':3,
		'办公楼':null,
		'教职工家属楼':22,
		'其它区域':'input'
};

function renderAddress(addr,unit){
	$('#addr_sel').empty();
	function appendDormSel(sel,maxnum){
		if(maxnum){
			sel.empty();
			for(var i = 0;i<maxnum;i++){
				sel.append($('<option>').html(i+1+unit));
			}
		}
	}
	$.each(addr,function(i,v){
		$('#addr_sel')
		.append($('<option>').html(i).val(v));
	});
	$('#addr_sel').on('change',function(){
		var v = $(this).val();
		if(v){
			if(!isNaN(v)){
				appendDormSel($('#addr_sel_dorm').show(),v);
				$('#addr_sel_other').hide();
			}
			else if('input' == v){
				$('#addr_sel_other').show();
				$('#addr_sel_dorm').hide();
			}
		}
		else{
			$('#addr_sel_dorm').hide();
			$('#addr_sel_other').hide();
		}
	});
}
$(function(){
	
});
function isAddrValid(){
	if($('#addr_sel>option:selected').index() == 0) return false;
	var val = $('#addr_sel').val();
	if('input'==val && !$('#addr_sel_other').val().trim()) return false;
	return true;
}
function getAddr(){
	var addr_sel_dorm = $('#addr_sel_dorm').val();
	var addr_sel_other = $('#addr_sel_other').val();
	
	return [$('#addr_sel>option:selected').html(),addr_sel_dorm || addr_sel_other];  //新格式的返回地址
	/*return $('#addr_sel>option:selected').html()
		+ (addr_sel_dorm?'-'+addr_sel_dorm:'')
		+ (addr_sel_other?'-'+addr_sel_other:'');*/
}
