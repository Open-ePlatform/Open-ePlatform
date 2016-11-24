var flowFamilyOverviewURI;

$(document).ready(function() {
	
	$("#UserFavouriteMenuModule ul li.disabled a").click(function(e) {
		e.preventDefault();
		return false;
	});
		
});

function reloadUserFavouritesMenu(favourites) {
	
	var $list = $("#UserFavouriteMenuModule ul");
	
	$list.find("li:not(.always-keep)").remove();
	
	if(favourites != undefined) {
		
		$list.find("li.no-favourites").hide();
		
		$.each(favourites, function(key, favourite) {
			
			var $li = $('<li id="flowFamily_' + favourite.flowFamilyID + ' "><a href="' + flowFamilyOverviewURI + '/' + favourite.flowFamilyID + '"><span class="icon arrow"><i data-icon-after=">"></i></span><span class="text">' + favourite.flowName + '</span></a>');
			
			$list.prepend($li);
			
		});
		
	} else {
		$list.find("li.no-favourites").show();
	}
	
	
}