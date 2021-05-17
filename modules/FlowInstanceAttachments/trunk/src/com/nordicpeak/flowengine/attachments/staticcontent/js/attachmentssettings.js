$(function() {
	notificationHandler();
});
function notificationHandler() {
	var checkBox = document.getElementById("moduleEnabled");
	if (checkBox.checked == false) {
		$("#notificationFieldset").hide();
	} else {
		$("#notificationFieldset").show();
	}
}