$(function() {
	
	var $alwaysShowOtherGuardians = $("#alwaysShowOtherGuardians");
	var $useMultipartSigning = $("#useMultipartSigning");
	
	var toggleCommonFields = (function() {
		
		var checked = $useMultipartSigning.prop("checked") || $alwaysShowOtherGuardians.prop("checked");
		
		$("#otherGuardiansDescription").parent().parent().toggle(checked);
		$("#showGuardianAddress").prop("disabled", !checked).parent().toggle(checked);
	});
	
	var toggleSkipMultipartSigningIfSameAddressWarning = (function() {
		
		var checked = $("#skipMultipartSigningIfSameAddress").prop("checked");
		
		$("#skipMultipartSigningIfSameAddressWarning").toggle(checked);

		// If VH2 lives on the same address and no signing is needed in that case, VH2 cannot be co-owner: not suppored in query since only signers can be co-owners. Show info! 		
		$("#setMultipartsAsOwnersSameAddressWarning").prop("hidden", !checked);
	});
	$("#skipMultipartSigningIfSameAddress").on("change", toggleSkipMultipartSigningIfSameAddressWarning);
	
	$useMultipartSigning.on("change", function() {
		
		var checked = $useMultipartSigning.prop("checked");
		
		$alwaysShowOtherGuardians.prop("disabled", checked);

		$("#contactWays, #requireGuardianContactInfoVerification, #setMultipartsAsOwners, #skipMultipartSigningIfSameAddress").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields();
		toggleSkipMultipartSigningIfSameAddressWarning();
		
	}).trigger("change");
	
	$alwaysShowOtherGuardians.on("change", function() {
		
		var checked = $alwaysShowOtherGuardians.prop("checked");
		
		$useMultipartSigning.prop("disabled", checked);
		
		$("#hideSSNForOtherGuardians").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields();
		
	}).trigger("change");
	
	var filterEndpointInput = $("#filterEndpoint");
	
	filterEndpointInput.on("change", function() {
		
		var endpointAttributes = $(".filterEndpoint");
		
		endpointAttributes.hide().find("select").prop("disabled", true);
		
		var endpoint = filterEndpointInput.val();
		
		if (endpoint != "") {
			
			$("#endpoint-attributes, #emptyFilterDescriptionContainer").show();
			$("#useFilteredChildrenDescription").trigger("change").parent().parent().show();
			
			endpointAttributes.filter(function(idx, element){
				return $(element).data("endpoint") == endpoint;
				
			}).show().find("select").prop("disabled", false);
			
		} else {
			
			$("#endpoint-attributes, #emptyFilterDescriptionContainer, #filteredChildrenDescriptionContainer").hide();
			$("#useFilteredChildrenDescription").parent().parent().hide();
		}
		
	}).trigger("change");
	
	$('#useFilteredChildrenDescription').on("change", function() {
		
		$('#filteredChildrenDescriptionContainer').toggle(this.checked);
		$("#filteredChildrenDescription").prop("disabled", !this.checked);
		
	}).trigger("change");
	
	$('#setAsAttribute').on("change", function() {
		
		$('#attributeContainer').toggle(this.checked);
		
	}).trigger("change");
	
	$('#setSecondGuardianAsAttribute').on("change", function() {
		
		$('#secondGuardianAttributeTableContainer').toggle(this.checked);
		
	}).trigger("change");
	
	$('#useMultipartSigning').on("change",function(e) {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			$(".guardianContactAttribute").show();
		} else {
			$(".guardianContactAttribute").hide();
		}
		
		showGuardianCitizenIdentifierAttribute();
		showGuardianAttributeContainer();
		
	}).trigger("change");
	
	$('#alwaysShowOtherGuardians').on("change", function() {
		
		showGuardianAttributeContainer();
		
	}).trigger("change");
	
	$("#hideSSNForOtherGuardians").on("change", function(e) {
		
		showGuardianCitizenIdentifierAttribute();
		
	}).trigger("change");
	
	$("#showGuardianAddress").on("change",function(e) {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			$(".secondGuardianAddressAttribute").show();
		} else {
			$(".secondGuardianAddressAttribute").hide();
		}
		
	}).trigger("change");
	
	$("#showAddress").on("change",function(e) {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			$(".childAddressAttribute").show();
		} else {
			$(".childAddressAttribute").hide();
		}
		
	}).trigger("change");
	
});

function showGuardianAttributeContainer() {
	
	if($('#useMultipartSigning').is(':checked') || $('#alwaysShowOtherGuardians').is(':checked')) {			
		$('#secondGuardianAttributeContainer').show();
	} else {
		$('#secondGuardianAttributeContainer').hide();
	}
	
}

function showGuardianCitizenIdentifierAttribute() {
	
	if($('#useMultipartSigning').is(':checked') || !$('#hideSSNForOtherGuardians').is(':checked')) {			
		$('#guardianCitizenIdentifierAttribute').show();
	} else {
		$('#guardianCitizenIdentifierAttribute').hide();
	}
	
}