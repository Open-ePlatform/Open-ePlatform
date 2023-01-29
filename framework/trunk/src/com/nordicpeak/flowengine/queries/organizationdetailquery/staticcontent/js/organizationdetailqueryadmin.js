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

	}).trigger("change");

	$("#fieldMobilePhone").on("change", function() {

		checkRequireEmailAndMobile();
		checkAllowSMS();
		
	}).trigger("change");
	
});

function checkRequireEmailAndMobile() {

	var fieldEmail = $("#fieldEmail").find(":selected").val();
	var fieldMobilePhone = $("#fieldMobilePhone").find(":selected").val();

	if (fieldEmail == "HIDDEN" || fieldMobilePhone == "HIDDEN") {

		$("#requireEmailOrMobile").prop("disabled", true).parent().hide();

	} else {

		$("#requireEmailOrMobile").prop("disabled", false).parent().show();
	}
}

function checkAllowSMS() {

	var fieldMobilePhone = $("#fieldMobilePhone").find(":selected").val();

	if (fieldMobilePhone == "HIDDEN" || $("#hideNotificationChannelSettings").prop("checked") == true) {

		$("#allowSMS").prop("disabled", true).parent().hide();

	} else {

		$("#allowSMS").prop("disabled", false).parent().show();
	}
}