$(document).ready(function() {

	$("#useMultipartSigning").change(function() {
		showHideGuardianOptions();
	});
	
	$("#alwaysShowOtherGuardians").change(function() {
		showHideGuardiansDescription();
	});

	showHideGuardianOptions();
	showHideGuardiansDescription();
});

function showHideGuardianOptions() {

	var checked = $("#useMultipartSigning").prop("checked");

	$("#contactWays, #requireGuardianContactInfoVerification, #setMultipartsAsOwners").parent().toggle(checked);
	$("#alwaysShowOtherGuardians").prop("disabled", checked).parent().toggle(!checked);
	
	showHideGuardiansDescription();
}

function showHideGuardiansDescription() {

	var checked = $("#useMultipartSigning, #alwaysShowOtherGuardians").prop("checked");

	$("#otherGuardiansDescription").parent().parent().toggle(checked);
	$("#showGuardianAddress").parent().toggle(checked);
}
