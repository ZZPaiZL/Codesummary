//在加载页面时加载
$(function(){
	var size=$("#diary li").length;
	$("#show").attr("data-size",size);
	for(var i=0;i<size;i++){
//     $('#diary li').eq(i).css("display":"block");//设置css样式	
      if(i>2){
        $('#diary li').eq(i).hide(); 
      }
     
	}
	
	
	
	
});

function nav_pagemores(obj){
//	var page=jQuery(obj).attr("data-page");
//	page=parseInt(page) + 1
//	//alert(url+page);
	
	var index = jQuery(obj).attr("data-index");
	var size = parseInt(jQuery(obj).attr("data-size"));
	
	var start=parseInt(index)+1;
	var end=parseInt(index)+3;
	
	if(start>size){
		alert("以加载完");
		return;
	}else if(end>size){
		end=size;
		$(obj).find("a").text("已完结");
	}
		for(start;start<=end;start++){
		    $('#diary li').eq(start).show();     
	}
	
	jQuery(obj).attr("data-index",end);

//	$.ajax({
//		url:"http://localhost:8084"+url,
//		type:"post",
//		data:{"page":page},// 传递数组前得将数组转换成json数组{"infos":JSON.stringify(infos)}
//		dataType:"json",
//		success:function(data){
//			//alert(data);
//			var info=data.data.diaryResult.list;	
//			for(var i=0;i<info.length;i++){
//		
//				
//			}
//			
//			
//		},
//		error:function(){
//			alert("登录失败");
//		}
//		
//	});
	
}


function showMenu(obj){
	//alert(123);
	$(".menu-open").text("关闭");
	$(".dl-container").show();
	$(".dl-menu").find("li").show();
	
}







function connect(){
	
	 
	
}
