$(function() {

	$(".modal .close").on("click", function(e) {
		e.preventDefault();
		$(this).parent().fadeOut("fast", function() {
			$(this).remove();
		});
	});
	
	$.tablesorter.addWidget({
		id : "onSortEvent",
		format : function(table) {
			$("thead th.tablesorter-headerDesc span", table).attr("data-icon-after", "^");
			$("thead th.tablesorter-headerAsc span", table).attr("data-icon-after", "_");
		}
	});

	$("table thead.sortable").each(function() {

		var $table = $(this).parent();

		var rows = $table.find("tbody tr");

		if (rows.length > 1) {
			var columns = $table.find("thead tr th");

			var sortColumn = columns.length - 3;
			
			var headers = {};
			headers[0] = { sorter: false };
			headers[columns.length - 1] = { sorter: false };
			
			$table.tablesorter({
				widgets : [ 'zebra', 'onSortEvent' ],
				headers : headers,
				sortList : [ [ sortColumn, 1 ] ]
			});
		}

	});

	$("#mobilePhone").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$("#contactBySMS").removeAttr("disabled").next("label").removeClass("disabled");
		} else {
			$("#contactBySMS").removeAttr("checked").attr("disabled", "disabled").next("label").addClass("disabled");
		}
		
	});
	
	$("#email").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$("#contactByEmail").attr("checked", true);
		} else {
			$("#contactByEmail").removeAttr("checked");
		}
		
	});
	
	$("#mobilePhone").trigger("change");
	$("#email").trigger("change");

});