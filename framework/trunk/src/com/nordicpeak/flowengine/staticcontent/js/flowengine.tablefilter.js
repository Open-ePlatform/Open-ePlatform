$(document).ready(function() {
	
	var $filterTableRows = {};
	
	$("input[type='text'].filter-input").each(function() {
		var id = $(this).data("tableid");
		$filterTableRows[id] = $("#" + id).find("tbody tr");
	});
	
	$("input[type='text'].filter-input").keyup(function() {
		
		var $this = $(this);
		var val = $.trim($this.val()).replace(/ +/g, ' ').toLowerCase();
	    
		$filterTableRows[$this.data("tableid")].show().filter(function() {
	        var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        return !~text.indexOf(val);
	    }).hide();
	
		$("#" + $this.data("tableid")).trigger("change");
		
	});
	
});