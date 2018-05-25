$(function() {
	
	var $alwaysShowOtherGuardians = $("#alwaysShowOtherGuardians");
	var $useMultipartSigning = $("#useMultipartSigning");
	
	var toggleCommonFields = (function(checked) {
		
		$("#otherGuardiansDescription").prop("disabled", !checked).parent().parent().toggle(checked);
		$("#showGuardianAddress").prop("disabled", !checked).parent().toggle(checked);
	});
	
	$useMultipartSigning.change(function() {
		
		var checked = $useMultipartSigning.prop("checked");
		
		$alwaysShowOtherGuardians.prop("disabled", checked);

		$("#contactWays, #requireGuardianContactInfoVerification, #setMultipartsAsOwners").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields(checked);
		
	}).change();
	
	$alwaysShowOtherGuardians.change(function() {
		
		var checked = $alwaysShowOtherGuardians.prop("checked");
		
		$useMultipartSigning.prop("disabled", checked);
		
		$("#hideSSNForOtherGuardians").prop("disabled", !checked).parent().toggle(checked);
		
		toggleCommonFields(checked);
		
	}).change();
});