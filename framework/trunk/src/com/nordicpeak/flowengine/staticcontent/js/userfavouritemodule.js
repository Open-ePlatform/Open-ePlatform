var flowFamilyOverviewURI;
var userFavouriteModuleURI;
var userFavouriteModuleMode;
var notificationDialogDelay = 5000;

$(document).ready(function() {
	
	initFavourites($("#UserFavouriteBackgroundModule ul.list-table li i.delete_favourite"));
	
	$("#UserFavouriteBackgroundModule ul.list-table li.disabled a").click(function(e) {
		e.preventDefault();
		return false;
	});
	
});

function reloadUserFavourites(favourites) {
	
	var $list = $("#UserFavouriteBackgroundModule ul.list-table");
	
	$list.find("li:not(.always-keep)").remove();
	
	if(favourites != undefined) {
	
		$list.find("li.no-favourites").hide();
		
		$.each(favourites.reverse(), function(key, favourite) {
			
			var icon = userFavouriteModuleMode == "SHOW" ? '<i data-icon-after="*" class="favourite">&#x20;</i>' : '<i data-icon-after="t" class="delete_favourite">&#x20;</i>';
			
			var $li = $('<li id="flowFamily_' + favourite.flowFamilyID + ' "><a href="' + flowFamilyOverviewURI + '/' + favourite.flowFamilyID + '"><span data-icon-before="&gt;" class="text">' + favourite.flowName + '</span>' + icon + '</a>');
			
			$list.prepend($li);
			
		});
	
		initFavourites($list.find("li i.delete_favourite"));
			
	} else {
		$list.find("li.no-favourites").show();
	}
	
}

function initFavourites($favourites) {
	
	if(userFavouriteModuleMode == "EDIT") {
	
		$favourites.click(function(e) {
			
			e.preventDefault();
			e.stopPropagation();
			
			var $this = $(this);
			
			var flowFamilyID = $this.closest("li").attr("id").split("_")[1];
			
			deleteUserFavourite($this, flowFamilyID);
			
		});
	
	}
	
}

function deleteUserFavourite($trigger, flowFamilyID) {
	
	$.ajax({
		cache: false,
		url: userFavouriteModuleURI + "/deletefavourite/" + $.trim(flowFamilyID),
		dataType: "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		error: function (xhr, ajaxOptions, thrownError) { },
		success: function(response) {

			var result = eval(response);
			
			if(result.DeleteSuccess) {
				
				showNotificationDialog("success", notificationDialogDelay, "Borttagen som favorit.");
				$trigger.addClass("gray");
				$("i.favourite#flowFamily_" + flowFamilyID).addClass("gray");
				
				reloadUserFavourites(result.UserFavourites);
				
				if(typeof reloadUserFavouritesMenu === 'function') {
					reloadUserFavouritesMenu(result.UserFavourites);
				}
				
			}		
			
		}
	});
	
}

function showNotificationDialog(type, delay, msg) {
	var modal = $("<div>").addClass("mini-modal " + type);
	var inner = $("<div>").addClass("inner");
	var span = $("<span>").text(msg);

	inner.append(span);
	modal.append(inner);
	modal.prependTo("body");

	modal.fadeIn("fast").delay(delay).fadeOut("fast", function() {
		modal.remove();
	});
}