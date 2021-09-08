$(function() {
	
	$('#hideNotificationChannelSettings').on("change", function(){
		
		$('#allowSMS').prop("disabled", this.checked).parent().toggle(!this.checked);
		
	}).trigger("change");
	
});