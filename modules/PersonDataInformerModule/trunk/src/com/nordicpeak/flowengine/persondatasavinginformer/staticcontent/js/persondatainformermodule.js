
$(document).ready(function() {
	
	$("table.coloredtable").each(function() {
		var $this = $(this);
		
		$this.bind("change", function() {
			$this.find("tr:not(:first):visible:even").removeClass("odd").addClass("even");
			$this.find("tr:not(:first):visible:odd").removeClass("even").addClass("odd");
		});
		
		$this.trigger("change");
	});
	
	$("#person-data-filter").change(function() {
		
		var $this = $(this);
		
		$("#flowlist tbody tr").removeClass("status-filtered").hide();
		
		var val = $this.val();
		var name = $this.find("option:selected").text();
		
		if (val != "") {
			
			$("#flowlist tbody tr").each(function(){
				var row = $(this);
				var td = row.children("td.persondata");
				var text = td.text();
				if (td.text().includes(name)) {
					
					row.addClass("status-filtered").show();
				}
			})
			
		} else {
			
			$("#flowlist tbody tr").addClass("status-filtered").show();
		}
		
		$("input[type='text'].flow-filter-input").trigger("keyup");
		
	});
	
	$("input[type='text'].flow-filter-input").keyup(function() {
		
		var $this = $(this);
		var val = $.trim($this.val()).replace(/ +/g, ' ').toLowerCase();
	    
		$("#flowlist").find(".status-filtered").show().filter(function() {
	        var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        return !~text.indexOf(val);
	    }).hide();
	
		$("#flowlist").trigger("change");
		
	});
	
	$("#person-data-filter").trigger("change");
});

function exportPersonData(event, button) {
	event.preventDefault();
	event.stopPropagation();
	
	var form = $(button).closest('form');
	
	form.find("input[name='flowFamilyID']").prop('disabled', true).closest("tr").filter(":visible").find("input[name='flowFamilyID']").prop('disabled', false);
	
	form.submit();
}
