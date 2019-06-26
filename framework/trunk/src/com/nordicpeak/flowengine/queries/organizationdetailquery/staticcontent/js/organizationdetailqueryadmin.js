$(document).ready(function() {
	
	$('#hideNotificationChannelSettings').change(function(){
		
		$('#allowSMS').prop("disabled", this.checked).parent().toggle(!this.checked);
		
	}).change();
	
});