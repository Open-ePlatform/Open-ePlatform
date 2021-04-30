function showRemovedGroupsDialog(){

	var config = {
	
			afterContent: function(){
				var feather = this;
				feather.$content.removeClass("no-sections");
			}
	};

	$.featherlight($("#removedGroupsModal"), config);
}