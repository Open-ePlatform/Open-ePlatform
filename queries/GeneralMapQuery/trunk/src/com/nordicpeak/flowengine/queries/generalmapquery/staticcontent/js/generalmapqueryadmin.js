$(function() {
	
	$("#mapConfigurationSelector").change(function() {
		
		var $this = $(this);
		
		$(".tools").hide();
		
		if($this.val() == "") {
			
			$(".mapquery-wrapper").hide();
			
		} else {
			
			$("#tools_" + $this.val()).show();

			var $mapPrints = $(".mapprint").show();
			
			var printConfigurationID = $("input[name='mapconfig_" + $this.val() + "']").val();
			
			if(printConfigurationID) {
				$("div.mapprint[data-printconfigurationid != " + printConfigurationID + "]").hide();
			}
			
			$(".mapquery-wrapper").show();
			
		}
		
	});
	
	$("#mapConfigurationSelector").trigger("change");
	
	$(".tools input[type='checkbox']").change(function() {
		
		var $this = $(this);
		
		if($this.is(":checked")) {
			
			$("#" + $this.attr("id") + "_form").show();
			
		} else {
			
			$("#" + $this.attr("id") + "_form").hide();
			
		}
		
	});
	
	$(".tools:visible input[type='checkbox']").trigger("change");
	
	$("#minimalDrawingScale").change(function() {
		
		var $this = $(this);
		
		if($this.val() != "") {
			
			$("#incorrectDrawingMessage").removeAttr("disabled").parent().parent().show();
			
		} else {
			
			$("#incorrectDrawingMessage").attr("disabled", "disabled").parent().parent().hide();
		}
		
	});
	
	$("#minimalDrawingScale").trigger("change");
	
	$("#allowOnlyOneGeometry").change(function() {
		
		var $this = $(this);
		
		var $inputs = $this.parent().parent().find("input[name$='_onlyOneGeometry']");
		
		if($this.is(":checked")) {
			
			$inputs.attr("disabled", "disabled").parent().hide();
			
		} else {
			
			$inputs.removeAttr("disabled").parent().show();
			
		}
		
	});
	
	$("#allowOnlyOneGeometry").trigger("change");
	
});