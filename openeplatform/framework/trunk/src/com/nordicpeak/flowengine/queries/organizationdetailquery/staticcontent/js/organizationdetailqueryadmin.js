$(function() {

	$("#hideNotificationChannelSettings").on("change", function() {

		checkAllowSMS();

	}).trigger("change");

	$("#fieldAddress").on("change", function() {

		var fieldAddress = $("#fieldAddress").find(":selected").val();

		if (fieldAddress == "HIDDEN") {

			$("#validateZipCode").prop("disabled", true).parent().hide();

		} else {

			$("#validateZipCode").prop("disabled", false).parent().show();
		}
	}).trigger("change");

	$("#fieldEmail").on("change", function() {

		checkRequireEmailAndMobile();
		showHideNotificationChannelSettings();

	}).trigger("change");

	$("#fieldMobilePhone").on("change", function() {

		checkRequireEmailAndMobile();
		checkAllowSMS();
		showHideNotificationChannelSettings();
		
	}).trigger("change");
	
	$("#allowSMS").on("change", function() {
	
		checkForceSMS();
		
	}).trigger("change");
	
	$("#setAsAttribute").on("change", showHideAttributeName).trigger("change");
});

function showHideAttributeName() {
	const checked = $("#setAsAttribute").prop("checked");
	$("#attribute-container").toggle(checked).find("input").prop("disabled", !checked);
}

function showHideNotificationChannelSettings() {
	const fieldEmailValue = $("#fieldEmail").val();		
	const fieldMobilePhoneValue = $("#fieldMobilePhone").val();
	const visible = fieldMobilePhoneValue != "HIDDEN" || fieldEmailValue != "HIDDEN";
	$("#hideNotificationChannelSettings").prop("disabled", !visible).parent().toggle(visible);
}

function checkRequireEmailAndMobile() {

	var fieldEmail = $("#fieldEmail").find(":selected").val();
	var fieldMobilePhone = $("#fieldMobilePhone").find(":selected").val();

	if (fieldEmail === "VISIBLE" && fieldMobilePhone === "VISIBLE") {
		$("#requireEmailOrMobile").prop("disabled", false).parent().show();

	} else {
		$("#requireEmailOrMobile").prop("disabled", true).parent().hide();
	}
}

function checkAllowSMS() {

	var fieldMobilePhone = $("#fieldMobilePhone").find(":selected").val();

	if (fieldMobilePhone == "HIDDEN" || $("#hideNotificationChannelSettings").prop("checked") == true) {

		$("#allowSMS").prop("disabled", true).parent().hide();
		$("#forceSMS").prop("disabled", true).parent().hide();

	} else {

		$("#allowSMS").prop("disabled", false).parent().show();
		checkForceSMS();
	}
}

function checkForceSMS() {

	var visible = $("#allowSMS").prop("checked");
	
	$("#forceSMS").prop("disabled", !visible).parent().toggle(visible);
}