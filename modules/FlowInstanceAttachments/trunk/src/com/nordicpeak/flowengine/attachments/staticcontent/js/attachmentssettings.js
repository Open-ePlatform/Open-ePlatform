$(function() {
	notificationHandler();
});
function notificationHandler() {
	var checkBox = document.getElementById("moduleEnabled");
	if(checkBox != null)
	{
		if (checkBox.checked == false) {
			$("#notificationFieldset").hide();
		} else {
			$("#notificationFieldset").show();
		}
	}
}