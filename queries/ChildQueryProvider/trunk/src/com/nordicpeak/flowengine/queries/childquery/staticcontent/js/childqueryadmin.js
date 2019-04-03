$(function() {
	
	var $alwaysShowOtherGuardians = $("#alwaysShowOtherGuardians");
	var $useMultipartSigning = $("#useMultipartSigning");
	
	var toggleCommonFields = (function() {
		
		var checked = $useMultipartSigning.prop("checked") || $alwaysShowOtherGuardians.prop("checked");
		
		$("#otherGuardiansDescription").parent().parent().toggle(checked);
		$("#showGuardianAddress").prop("disabled", !checked).parent().toggle(checked);
	});
	
	$useMultipartSigning.change(function() {
		
		var checked = $useMultipartSigning.prop("checked");
		
		$alwaysShowOtherGuardians.prop("disabled", checked);

		$("#contactWays, #requireGuardianContactInfoVerification, #setMultipartsAsOwners").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields();
		
	}).change();
	
	$alwaysShowOtherGuardians.change(function() {
		
		var checked = $alwaysShowOtherGuardians.prop("checked");
		
		$useMultipartSigning.prop("disabled", checked);
		
		$("#hideSSNForOtherGuardians").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields();
		
	}).change();
	
	var filterEndpointInput = $("#filterEndpointID");
	
	filterEndpointInput.change(function() {
		
		$(".filterEndpoint").hide().find("select").prop("disabled", true);
		
		var endpointID = filterEndpointInput.val();
		
		if (endpointID != "") {
			
			$("#endpoint-attributes").show();
			$("#endpoint-" + endpointID).show().find("select").prop("disabled", false);
			
		} else {
			
			$("#endpoint-attributes").hide();
		}
		
	}).change();
	
	$('#setAsAttribute').change(function(){
		
		$('#attributeContainer').toggle(this.checked);
		
	}).change();
	
});