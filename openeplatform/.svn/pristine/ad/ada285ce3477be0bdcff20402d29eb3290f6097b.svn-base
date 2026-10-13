$(function() {
	
	$("#disabled").on("change", function() {
		
		const checkboxes = document.getElementById("required").parentElement.querySelectorAll('input');
		
		if($("#disabled").is(":checked")) {
			
			$("#default-value").show();
			$("#contains-price").show();
			$("#disable-hide-fields").hide();

			for(checkbox in checkboxes) {
				checkbox.disabled = true;
			}
				
		} else {
			
			for(checkbox in checkboxes) {
				checkbox.disabled = false;
			}
			
			$("#default-value").hide();
			$("#contains-price").hide();		
			$("#disable-hide-fields").show();
		}
	});
	
	if($("#disabled").is(":checked")) {
		
		$("#disable-hide-fields").hide();
		
		
	} else {
		$("#default-value").hide();
		$("#contains-price").hide();
	}
	
	function toggleInactiveAutocompleteSetting(){
		
		const inactivateAutocompleteCheckbox = document.getElementById("inactivateAutocomplete");	
		const infoText = document.getElementById("disableinactivateiutocompleteinfo");
		const checked = maskFieldContentCheckbox.checked;
		
	     if (inactivateAutocompleteCheckbox) {
        	inactivateAutocompleteCheckbox.disabled = checked;
        	checked ? showInfo() : hideInfo();
    	}

	    function showInfo() {
	        infoText.classList.remove("hidden");
	        inactivateAutocomplete.parentElement.setAttribute("aria-describedby", "inactivateAutocompleteContainer");
	    }

	    function hideInfo() {
	        infoText.classList.add("hidden");
	        inactivateAutocomplete.parentElement.removeAttribute("aria-describedby");
	    }
		  
	};
	
	const maskFieldContentCheckbox = document.getElementById("maskFieldContent");
	
	if(maskFieldContentCheckbox) {
		
		toggleInactiveAutocompleteSetting();
		
		maskFieldContentCheckbox.addEventListener("change", toggleInactiveAutocompleteSetting);
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
			
			$invalidFormatMessageInput.parent().show();
			
			
			var placeholder = $("#placeholder-" + className.replace(/\./g,"_")).val();
			
			if(placeholder != null) {
				$placeholderInput.val(placeholder);
			} else {
				$placeholderInput.val("");
			}			
			
		} else {
			
			$invalidFormatMessageInput.parent().hide();
			$placeholderInput.val("");
		}
		
	});
	
	const showHideAttributeName = function(){
		
		const checked = $("#setAsAttribute").prop("checked");
		$("#attribute-container").toggle(checked).find("input").prop("disabled", !checked);
	};

	showHideAttributeName();
	
	$("#setAsAttribute").on("change", function(){showHideAttributeName()});
	
	var selectedValue = $("#formatValidator option:selected").val();
	
	if(selectedValue == "") {
		$("#invalidFormatMessage").parent().hide();
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