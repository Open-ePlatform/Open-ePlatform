$(document).ready(function() {
	
	
	var fieldMobilePhone = $("#fieldMobilePhone");
	var fieldAddress = $("#fieldAddress");
	var fieldEmail = $("#fieldEmail");
	var allowSMS = $("#allowSMS");
	
	var showHideRequireAtLeastOneContactWay = function() {
		
		var visible = !allowSMS.prop("disabled") && allowSMS.prop("checked") && fieldMobilePhone.val() != "HIDDEN" && fieldEmail.val() == "VISIBLE";
		$("#requireAtLeastOneContactWay").prop("disabled", !visible).parent().toggle(visible);
	}
	
	var showHideAllowSMS = function() {
	
		var visible = fieldMobilePhone.val() != "HIDDEN";
		allowSMS.prop("disabled", !visible).parent().toggle(visible);
		
		showHideRequireAtLeastOneContactWay();
	}
	
	var showHideLockAddress = function() {
		
		var visible = fieldAddress.val() != "HIDDEN";
		$("#useOfficalAddress").prop("disabled", !visible).parent().toggle(visible);
		$("#fieldCareOf").prop("disabled", !visible).parent().parent().parent().toggle(visible);
	}
	
	showHideAllowSMS();
	showHideLockAddress();
	showHideRequireAtLeastOneContactWay();
	
	fieldMobilePhone.change(showHideAllowSMS);
	fieldAddress.change(showHideLockAddress);
	allowSMS.change(showHideRequireAtLeastOneContactWay);
	fieldEmail.change(showHideRequireAtLeastOneContactWay);
});

