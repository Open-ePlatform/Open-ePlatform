$(document).ready(function() {
	
	$.tablesorter.addWidget({ 
	    id: "onSortEvent", 
	    format: function(table) { 
	    	$("thead th.headerSortDown span", table).attr("data-icon-after", "^");
	    	$("thead th.headerSortUp span", table).attr("data-icon-after", "_");
	    }
	});
	
	$("table thead.sortable").each(function() {
		
		var $table = $(this).parent();

		var rows = $table.find("tbody tr");
		
		if(rows.length > 1) {
			
			var $sortColumn = $table.find("thead tr th.default-sort");
			var $noSort = $table.find("thead tr th.no-sort");
			
			var headers = {};
			
			$noSort.each(function() {
				
				headers[$(this).index()] = { sorter: false };
				
			});
			
			var defaultSort = null;

			if ($sortColumn.length > 0) {
				defaultSort = [ [ $sortColumn.index(), 0 ] ];
			} else {
				defaultSort = [];
			}
			
			$table.tablesorter({
				widgets: ['zebra','onSortEvent'],
				headers: eval(headers),
				sortList: defaultSort
			});
		}
		
	});
	
});