$(document).ready(function() {
	
	$("input[name='global']").change(function() {
		
		var $flowFamilies = $("#chooseFlowFamilies");
		
		if(this.value == "true") {

			$flowFamilies.find("input[type='checkbox']").attr("disabled", "disabled");
			$flowFamilies.hide();
			
		} else if(this.value == "false") {
			
			$flowFamilies.find("input[type='checkbox']").removeAttr("disabled");
			$flowFamilies.show();
			
		}
		
	});

	$("input[name='profileFilter']").change(function() {
		
		var $profiles = $("#chooseProfiles");
		
		if(this.value == "true") {

			$profiles.find("input[name='profileID']").removeAttr("disabled");
			$profiles.show();
			
		} else if(this.value == "false") {
			
			$profiles.find("input[name='profileID']").attr("disabled", "disabled");
			$profiles.hide();
		}
		
	});
	
	$("#disableFlows").change(function() {
		
		$("#allowManagingOfInstances, #allowUserHandlingOfSubmittedInstances").parent().parent().toggle($(this).prop("checked"));
	});	
	
	var $flowFamilies = $("#chooseFlowFamilies").find(".flowfamily");
	
	$flowFamilies.sort(function(item1, item2) {
		return $(item1).data("name").toString().toUpperCase().localeCompare($(item2).data("name").toString().toUpperCase());
	});
	
	$("#chooseFlowFamilies").html($flowFamilies);
	
	$("input[name='global']:checked").trigger("change");
	
	$("input[name='profileFilter']:checked").trigger("change");
	
	$("#disableFlows").trigger("change");
	
	$("#startTime, #endTime").timePicker({
		startTime: "00:00",
		endTime: "23:59",
		step: 15
	});
	
});