function initFileInfoQuery(queryID) {

	var query = $("#query_" + queryID);

	query.find("div.file").on("click", function(event) {
		
		if(event.target === this){
			var a = $(this).find("a");
			window.open(a.attr('href'), a.attr('target'));
		}
	});
}
