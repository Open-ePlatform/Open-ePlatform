$(document).ready(function() {
	
	$("#disabled").change(function() {
		
		if($("#disabled").is(":checked")){
			
			$("#default-value").show();
			$("#disable-hide-fields").hide();
			
		}else{
			
			$("#default-value").hide();
			$("#disable-hide-fields").show();
		}
	});
	
	if($("#disabled").is(":checked")){
		
		$("#disable-hide-fields").hide();
		
	}else{
		
		$("#default-value").hide();
	}
	
	$("#formatValidator").change(function() {
		
		var className = $("#formatValidator option:selected").val();
		
		var $invalidFormatMessageInput = $("#invalidFormatMessage");
		
		var $placeholderInput = $("#placeholderText");
		
		if(className != "") {
		
			var validationMessage = $("#validatorMessage-" + className.replace(/\./g,"_")).val();
	
			if(validationMessage != null) {
				$invalidFormatMessageInput.val(validationMessage);
			} else {
				$invalidFormatMessageInput.val("");
			}
			
			$invalidFormatMessageInput.parent().parent().show();
			
			
			var placeholder = $("#placeholder-" + className.replace(/\./g,"_")).val();
			
			if(placeholder != null) {
				$placeholderInput.val(placeholder);
			} else {
				$placeholderInput.val("");
			}			
			
		} else {
			
			$invalidFormatMessageInput.parent().parent().hide();
			$placeholderInput.val("");
		}
		
	});
	
	var selectedValue = $("#formatValidator option:selected").val();
	
	if(selectedValue == "") {
		$("#invalidFormatMessage").parent().parent().hide();
	}
	
});