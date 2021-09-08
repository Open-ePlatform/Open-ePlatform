$(function() {
	
	$("#disabled").on("change", function() {
		
		if($("#disabled").is(":checked")){
			
			$("#default-value").show();
			$("#contains-price").show();
			$("#disable-hide-fields").hide();
			
		}else{
			
			$("#default-value").hide();
			$("#contains-price").hide();
			$("#disable-hide-fields").show();
		}
	});
	
	if($("#disabled").is(":checked")){
		
		$("#disable-hide-fields").hide();
		
	}else{
		
		$("#default-value").hide();
		$("#contains-price").hide();
	}
	
	$("#formatValidator").on("change", function() {
		
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
	
	$('#endpointID').on("change", function(){
		
		var selectedEndpointID = this.value;
		
		$('#endpoint-fields').toggle(!!selectedEndpointID);
			
		$('.endpoint').each(function(){
			
			var isSelected = this.id === 'endpoint-' + selectedEndpointID;
			
			$(this).toggle(isSelected).find('select').prop('disabled', !isSelected);
			
		});
		
	}).trigger("change");
	
});