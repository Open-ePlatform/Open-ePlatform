$(document).ready(function() {
	
	var init = true;
	
	$("#DummyPaymentProvider").find("input[type='radio']").change(function(e) {
		
		var $this = $(this);
		
		if(!init) {
			$("#DummyPaymentProvider").find(".paymentmethods").hide();
		}
		
		if($this.is(":checked")) {
			
			$("#" + $this.attr("id") + "Payment").show();
			
		}
		
	}).trigger("change");
	
	init = false;
	
});