var flowInstanceAdminURI;
var i18nChooseManager = "Choose manager";
var i18nChooseFlowInstance = "Choose";
var i18nFlow= "Flow";
var i18nFlowInstanceID = "Flow instance id";
var i18nFlowInstanceStatus = "Status";
var i18nFlowInstanceDescription = "Description";
var i18nFlowInstanceFirstSubmitted = "Added";
var i18nFlowInstancePoster = "Poster";
var showSearchDecriptionColumn = true;

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
	
	$("#new-message, #notes").find("form").on("submit", function(e) {
		
		var $this = $(this);
		
		var $message = $this.find("textarea");
		
		if($message.val() == "") {
			e.preventDefault();
			$this.find(".info-box.error").remove();
			$message.before($("<div class='info-box error'><span><strong data-icon-before='!'>" + $message.data("requiredmessage") + "</strong></span></div>"));
			return false;
		}
		
		return true;
	});
	
	/* Change managers scripts */
	
	var MAX_SEARCH_COUNT_EMPTY_SEARCH = 100;
	
	$('#groupID-search, #userID-search').each(function(){
		
		if ($(this).closest('.user-group-list-container').data('search-count') < MAX_SEARCH_COUNT_EMPTY_SEARCH) {
			
			$(this).on('focus', function(){
				
				var searchValue = this.value === '' ? ' ' : this.value;
				
				$(this).autocomplete('search', searchValue);
				
			});
			
			$(this).on('autocompleteclose', function(){
				
				$(this).is(':focus') && $(this).autocomplete('search', ' ');
				
			});
		}
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
		
		$('#search').val('');
	});
	
	$("textarea.mentionable").each(function() {
		var $textarea = $(this);
		$.get($textarea.data("mentionableendpoint"), function (data) {
			$textarea.mentionable({
				users: data
			});
	    });
	});
	
});

$(window).on('load', function(){
	
	if ($("#search").val()) {
		
		searchFlowInstance();
	}
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

				var result = response;
				
				$searchResultTable.html("");
				
				if(response.hitCount > 0) {
				
					$.each(result.hits, function( key, flowInstance) {

						if(showSearchDecriptionColumn == true){
							
							var $flowInstanceRow = 
								'<tr onclick="location.href=\'' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '\'">' + 
								'<td class"icon" />' +
								'<td data-title="' + i18nFlow +  '" class="service">' + flowInstance.name + '</td>' +
								'<td data-title="' + i18nFlowInstanceID + '" class="errandno">' + flowInstance.id + '</td>' +
								'<td data-title="' + i18nFlowInstanceStatus + '" class="status">' + flowInstance.status + '</td>' +
								'<td data-title="' + i18nFlowInstanceDescription + '" class="description">' + flowInstance.managerDescription + '</td>' +
								'<td data-title="' + i18nFlowInstanceFirstSubmitted + '" class="date">' + flowInstance.firstSubmitted + '</td>' +
								'<td data-title="' + i18nFlowInstancePoster + '" class="poster">' + flowInstance.poster + '</td>' +
								'<td class="link"><a href="' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '" class="btn btn-dark btn-inline">' + i18nChooseFlowInstance + '</a></td>' +
								'</tr>';
				        	
							$searchResultTable.append($flowInstanceRow);
							
						}else{
							
							var $flowInstanceRow = 
								'<tr onclick="location.href=\'' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '\'">' + 
								'<td class"icon" />' +
								'<td data-title="' + i18nFlow +  '" class="service">' + flowInstance.name + '</td>' +
								'<td data-title="' + i18nFlowInstanceID + '" class="errandno">' + flowInstance.id + '</td>' +
								'<td data-title="' + i18nFlowInstanceStatus + '" class="status">' + flowInstance.status + '</td>' +
								'<td data-title="' + i18nFlowInstanceFirstSubmitted + '" class="date">' + flowInstance.firstSubmitted + '</td>' +
								'<td data-title="' + i18nFlowInstancePoster + '" class="poster">' + flowInstance.poster + '</td>' +
								'<td class="link"><a href="' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '" class="btn btn-dark btn-inline">' + i18nChooseFlowInstance + '</a></td>' +
								'</tr>';
				        	
							$searchResultTable.append($flowInstanceRow);							
						}
			        	
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