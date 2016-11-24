var flowInstanceAdminURI;
var i18nChooseManager = "Choose manager";
var i18nChooseFlowInstance = "Choose";
var i18nFlow= "Flow";
var i18nFlowInstanceID = "Flow instance id";
var i18nFlowInstanceStatus = "Status";
var i18nFlowInstanceAdded = "Added";

$(document).ready(function() {
	
	/* Flowinstance overview scripts */
	
	var $tabs = $("#tabs");
	
	$tabs.tabs();
	
	$("ul.summary-buttons li a").click(function(e) {
		
		e.stopPropagation();
		e.preventDefault();
		
		var tabID = $(this).attr("href");

		var idx = $tabs.find(tabID).index();

		$tabs.tabs("option", "active", idx-1);
		
	});
	
	initMessageTab("#messages", "#new-message");
	
	initMessageTab("#notes", "#new-note");
	
	/* Change managers scripts */
	
	var $managerList = $("ul.manager-list");
	
	$managerList.find("li").each(function(i) {
		initDeleteManagerButton($(this), $managerList);
	});
	
	updateListRowColors($managerList);
	
	$(".select-box.addmanagers").each(function(i) {
		
		var $selectBox = $(this);
		
		$selectBox.click(function(e) {
			
			e.stopPropagation();
			
			if($selectBox.hasClass("active")) {
				$selectBox.removeClass("active");
				$selectBox.find("> span.arrow").text("_");
			} else {
				$selectBox.addClass("active");
				$selectBox.find("> span.arrow").text("^");
			}
			
		});
		
		$searchInput = $selectBox.find(".search input");
		
		var $users = $selectBox.find(".options ul li");
		
		$users.each(function(j) {
			
			var $user = $(this);
			
			if($("#manager_" + $user.attr("id")).length > 0) {
				$user.addClass("disabled").hide();
			}
			
			$user.find("a").click(function(e) {
				
				e.preventDefault();
				
				$users.removeClass("selected");
				$user.addClass("selected");
				
				$selectBox.find("> span.text").text($(this).find(".text").text());
				$selectBox.find("> span.arrow").text("_");
				$searchInput.val("").keyup();
				
			});
			
		});
		
		$searchInput.click(function(e) {
			e.stopPropagation();
		});
		
		$searchInput.keyup(function () {
			
			var searchStr = $(this).val();
			
			var $items = $selectBox.find("ul li:not(.disabled)");
			
			$items.hide();
			
			if(searchStr != "") {
				
				$items.each(function () {
		            if ($(this).find("span.text").text().search(new RegExp(searchStr, "i")) < 0) {
		            	$(this).hide();
		            } else {
		            	$(this).show();
		            }
		        });
		        
		    } else {
		    	
		    	$items.show();
		 
		    }
			
	    });
		
		$selectBox.next("a.btn").click(function(e) {
			
			e.preventDefault();
			
			var $manager = $selectBox.find(".options ul li.selected");
			
			if($manager.length > 0) {
				
				var $clone = $("#manager_template").clone();
				
				$clone.find("span.text").text($manager.find("span.text").text());
				$clone.find("input[type='hidden']").removeAttr("disabled").val($manager.attr("id").split("_")[1]);
				$clone.attr("id", "manager_" + $manager.attr("id"));
				
				$manager.addClass("disabled").removeClass("selected").hide();
				$selectBox.find("> span.text").text(i18nChooseManager);
				
				initDeleteManagerButton($clone, $managerList);
				
				$managerList.find("li:last").before($clone);
				$clone.show();
				
				updateListRowColors($managerList);
				
			}
						
		});
		
	});
	
	$(document).on("click", function(e) {
		$(".select-box.with-search").removeClass("active");
	});
	
	/* Search flow instance scripts */
	
	$("#search").keyup(function () {
		
		$(this).parent().removeClass("searching");
		searchFlowInstance();
		
    }).keydown(function() {
    	$(this).parent().addClass("searching");
    }).bind('focus blur', function() {
    	$(this).parent().toggleClass('focus');
	});
	
	$("div.search-results").find(".info .close").click(function(e) {
		$(this).parent().parent().slideUp("fast");
	});
	
});

function initDeleteManagerButton($manager, $managerList) {
	
	$manager.find("a.delete").click(function(e) {
		$("#user_" + $manager.find("input[type='hidden']").val()).removeClass("disabled").show();
		$manager.remove();
		updateListRowColors($managerList);
	});
	
}

function updateListRowColors($list) {
	
	$list.find("li:visible:odd").attr("class", "odd");
	$list.find("li:visible:even").attr("class", "even");
	
}

function initMessageTab(tabID, messagePanelID) {
	
	$(tabID + " a.open_message").click(function(e) {
		e.preventDefault();
		
		$(messagePanelID).show();
		scrollToMessages(messagePanelID);
		$("#message").focus();
	});

	$(messagePanelID + " a.close_message").click(function(e) {
		e.preventDefault();
		$(messagePanelID).hide();
	});
	
	if($(tabID + " div.info-box.error").length > 0) {
		var $tabs = $("#tabs");
		var idx = $tabs.find(tabID).index();
		$tabs.tabs("option", "active", idx-1);
		$(tabID + " a.open_message").trigger("click");
	}

	if(window.location.hash == messagePanelID) {
		var $tabs = $("#tabs");
		var idx = $tabs.find(tabID).index();
		$tabs.tabs("option", "active", idx-1);
		scrollToMessages(tabID + " ul.messages li");
	}
	
}

function scrollToMessages(selector) {
	
	$('html, body').animate({
		scrollTop : ($(selector).last().offset().top - 43)
	}, 'fast');
	
}

function toggleBookmark(e, trigger, uri) {
	
	if(e.preventDefault) {
		e.preventDefault();
	}
	
	var $trigger = $(trigger);
	
	$.ajax({
		cache: false,
		url: uri,
		dataType: "text",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		error: function (xhr, ajaxOptions, thrownError) { },
		success: function(response) {

			if(response == 1) {
			
				$trigger.removeClass("btn-light").addClass("green");
				
			} else if(response == 0) {
				
				$trigger.removeClass("green").addClass("btn-light");
				
			}
			
		}
		
	});
	
	return false;	
}

function searchFlowInstance() {
	
	var searchStr = $('#search').val();

	var $searchResultWrapper = $("div.search-results");
	var $searchResultTitle = $searchResultWrapper.find(".search-results-title");
	var $searchResultTableWrapper = $searchResultWrapper.find(".errands-wrapper");
	var $searchResultTable = $searchResultTableWrapper.find("table tbody");
	
	if(searchStr != "") {
		
		$.ajax({
			
			cache: false,
			url: flowInstanceAdminURI + "/search",
			data : {
				q : encodeURIComponent(searchStr)
			},			
			dataType: "json",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) { },
			success: function(response) {

				var result = eval(response);
				
				$searchResultTable.html("");
				
				if(response.hitCount > 0) {
				
					$.each(result.hits, function( key, flowInstance) {

						var $flowInstanceRow = $(
							'<tr>' + 
							'<td class"icon" />' +
							'<td data-title="' + i18nFlow +  '" class="service">' + flowInstance.name + '</td>' +
							'<td data-title="' + i18nFlowInstanceID + '" class="errandno">' + flowInstance.id + '</td>' +
							'<td data-title="' + i18nFlowInstanceStatus + '" class="status">' + flowInstance.status + '</td>' +
							'<td data-title="' + i18nFlowInstanceAdded + '" class="date">' + flowInstance.added + '</td>' +
							'<td class="link"><a href="' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '" class="btn btn-dark btn-inline">' + i18nChooseFlowInstance + '</a></td>' +
							'</tr>'
						);
			        	
						$searchResultTable.append($flowInstanceRow);
			        	
					});
				
					$searchResultTableWrapper.show();
					
				} else {
					
					$searchResultTableWrapper.hide();
					
				}
				
				$searchResultTitle.find(".title").text(searchStr);
		        $searchResultTitle.find(".hits").text(response.hitCount);
		        
		        $searchResultWrapper.show();
				
			}
			
		});
		
        
    } else {
    	
    	$searchResultTitle.find(".title").text("");
    	$searchResultTitle.find(".hits").text(0);
    	$searchResultWrapper.hide();
    	$searchResultTableWrapper.hide();
    	$searchResultTable.html("");
 
    }
	
}