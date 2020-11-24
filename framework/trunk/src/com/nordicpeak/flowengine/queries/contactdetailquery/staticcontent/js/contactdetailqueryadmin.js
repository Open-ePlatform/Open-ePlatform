$(document).ready(function() {
	
	
	var fieldMobilePhone = $("#fieldMobilePhone");
	var fieldAddress = $("#fieldAddress");
	var fieldEmail = $("#fieldEmail");
	var allowSMS = $("#allowSMS");
	var hideNotificationChannelSettings = $("#hideNotificationChannelSettings");
	
	var showHideRequireAtLeastOneContactWay = function() {
		
		var visible = !allowSMS.prop("disabled") && allowSMS.prop("checked") && fieldMobilePhone.val() != "HIDDEN" && fieldEmail.val() == "VISIBLE" && !hideNotificationChannelSettings.prop("checked");
		$("#requireAtLeastOneContactWay").prop("disabled", !visible).parent().toggle(visible);
	}
	
	var showHideAllowSMS = function() {
	
		var visible = fieldMobilePhone.val() != "HIDDEN" && !hideNotificationChannelSettings.prop("checked");
		allowSMS.prop("disabled", !visible).parent().toggle(visible);
		
		showHideRequireAtLeastOneContactWay();
	}
	
	var showHideLockAddress = function() {
		
		var visible = fieldAddress.val() != "HIDDEN";
		$("#useOfficalAddress").parent().toggle(visible);
		$("#fieldCareOf").parent().parent().parent().toggle(visible);
		
		if (!visible) {
			$("#fieldCareOf").val("HIDDEN");
		}
		
	}
	
	showHideAllowSMS();
	showHideLockAddress();
	showHideRequireAtLeastOneContactWay();

	$("#useOfficalAddress").change(function(e) {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			$("#officialAddressMissingAttribute").removeClass("hidden");
		} else {
			$("#officialAddressMissingAttribute").addClass("hidden");
		}
		
	}).trigger("change");
	
	fieldMobilePhone.change(showHideAllowSMS);
	fieldAddress.change(showHideLockAddress);
	allowSMS.change(showHideRequireAtLeastOneContactWay);
	fieldEmail.change(showHideRequireAtLeastOneContactWay);
	hideNotificationChannelSettings.change(showHideAllowSMS);
	hideNotificationChannelSettings.change(showHideRequireAtLeastOneContactWay);
});

