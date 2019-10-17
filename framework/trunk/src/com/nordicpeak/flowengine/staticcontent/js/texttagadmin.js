$(document).ready(function() {
	
	$("input[name='textTagID']").change(function() {
		
		var checked = $("input[name='textTagID']:checked").length > 0;
		
		if(checked){
			
			$(".share-button").removeClass("disabled");
			
		}else{
		
			$(".share-button").addClass("disabled");
		}
		
	});
	
	$("input[name='target']").change(function() {
		
		var checked = $("input[name='target']:checked").length > 0;
		
		if(checked){
			
			$("#share-submit-button").removeAttr("disabled");
			
		}else{
		
			$("#share-submit-button").attr("disabled","true");
		}
		
	});
	
});

function checkState(event){
	
	if($(".share-button").hasClass("disabled")){
		
		event.stopPropagation();
		
		event.preventDefault();
	}
}