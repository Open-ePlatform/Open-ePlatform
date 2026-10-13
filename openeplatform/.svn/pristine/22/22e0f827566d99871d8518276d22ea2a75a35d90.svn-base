$(function() {

	const fieldMobilePhone = $("#fieldMobilePhone");
	const fieldPhone = $("#fieldPhone");
	var fieldAddress = $("#fieldAddress");
	var fieldEmail = $("#fieldEmail");
	var allowSMS = $("#allowSMS");
	var forceSMS = $("#forceSMS");
	var hideNotificationChannelSettings = $("#hideNotificationChannelSettings");
	
	const showHideAttributeName = function(){
		const checked = $("#setAsAttribute").prop("checked");
		$("#attribute-container").toggle(checked).find("input").prop("disabled", !checked);
	};

	showHideAttributeName();
	
	$("#setAsAttribute").on("change", function(){showHideAttributeName()});
	
	var showHideRequireAtLeastOneContactWay = function() {
		
		var visible = !allowSMS.prop("disabled") && allowSMS.prop("checked") && fieldMobilePhone.val() == "VISIBLE" && fieldEmail.val() == "VISIBLE" && !hideNotificationChannelSettings.prop("checked");
		$("#requireAtLeastOneContactWay").prop("disabled", !visible).parent().toggle(visible);
	}
	
	var showHideForceSMS = function() {
		
		const visible = allowSMS.prop("checked");
		forceSMS.prop("disabled", !visible).parent().toggle(visible);
	}
	
	var showHideAllowSMS = function() {
	
		var visible = fieldMobilePhone.val() != "HIDDEN" && !hideNotificationChannelSettings.prop("checked");
		allowSMS.prop("disabled", !visible).parent().toggle(visible);
		
		showHideRequireAtLeastOneContactWay();
		showHideForceSMS();
	}
	
	const showHideRequireMobilePhoneOrPhone = function() {

		const fieldPhoneValue = fieldPhone.find(":selected").val();		
		const fieldMobilePhoneValue = fieldMobilePhone.find(":selected").val();
		const visible = fieldMobilePhoneValue != "HIDDEN" && fieldMobilePhoneValue != "REQUIRED"  && fieldPhoneValue != "HIDDEN" && fieldPhoneValue != "REQUIRED";
		$("#requireMobilePhoneOrPhone").prop("disabled", !visible).parent().toggle(visible);

	}
	
	const showRequireContactDetailInfoVerification = function() {
		const fieldEmailValue = fieldEmail.find(":selected").val();		
		const fieldMobilePhoneValue = fieldMobilePhone.find(":selected").val();
		const visible = fieldMobilePhoneValue != "HIDDEN" || fieldEmailValue != "HIDDEN";
		$("#requireContactDetailInfoVerification").prop("disabled", !visible).parent().toggle(visible);
	}
	
	var showHideLockAddress = function() {
		
		var visible = fieldAddress.val() != "HIDDEN";
		$("#useOfficalAddress").parent().toggle(visible);
		$("#fieldCareOf").parent().parent().toggle(visible);
		
		if (!visible) {
			$("#fieldCareOf").val("HIDDEN");
		}
		
	}
	
	const showHideNotificationChannelSettings = function() {
		const fieldEmailValue = fieldEmail.find(":selected").val();		
		const fieldMobilePhoneValue = fieldMobilePhone.find(":selected").val();
		const visible = fieldMobilePhoneValue != "HIDDEN" || fieldEmailValue != "HIDDEN";
		$("#hideNotificationChannelSettings").prop("disabled", !visible).parent().toggle(visible);
	}
	
	showHideAllowSMS();
	showHideLockAddress();
	showHideRequireAtLeastOneContactWay();
	showHideRequireMobilePhoneOrPhone();
	showRequireContactDetailInfoVerification();
	showHideNotificationChannelSettings();

	$("#useOfficalAddress").on("change", function(e) {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			$(".officialAddressMissingAttribute").removeClass("hidden");
		} else {
			$(".officialAddressMissingAttribute").addClass("hidden");
		}
		
	}).trigger("change");
	
	fieldMobilePhone.on("change", showHideAllowSMS);
	fieldMobilePhone.on("change", showHideRequireMobilePhoneOrPhone);
	fieldMobilePhone.on("change", showRequireContactDetailInfoVerification);
	fieldMobilePhone.on("change", showHideNotificationChannelSettings);
	fieldPhone.on("change", showHideRequireMobilePhoneOrPhone);
	fieldAddress.on("change", showHideLockAddress);
	allowSMS.on("change", showHideRequireAtLeastOneContactWay);
	allowSMS.on("change", showHideForceSMS);
	fieldEmail.on("change", showHideRequireAtLeastOneContactWay);
	fieldEmail.on("change", showRequireContactDetailInfoVerification);
	fieldEmail.on("change", showHideNotificationChannelSettings);
	hideNotificationChannelSettings.on("change", showHideAllowSMS);
	hideNotificationChannelSettings.on("change", showHideRequireAtLeastOneContactWay);
	hideNotificationChannelSettings.on("change", showHideRequireMobilePhoneOrPhone);
});
