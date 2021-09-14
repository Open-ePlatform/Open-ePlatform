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
	
	$('#useMultipartSigning').on("change", function() {
		
		showGuardianAttributeContainer();
	}).trigger("change");
	
	$('#alwaysShowOtherGuardians').on("change", function() {
		
		showGuardianAttributeContainer();
	}).trigger("change");
	
	showGuardianAttributeContainer();
});

function showGuardianAttributeContainer()
{
	if($('#useMultipartSigning').is(':checked') || $('#alwaysShowOtherGuardians').is(':checked'))
		{			
			$('#secondGuardianAttributeContainer').show();
		}
		else
		{
			$('#secondGuardianAttributeContainer').hide();
		}
}