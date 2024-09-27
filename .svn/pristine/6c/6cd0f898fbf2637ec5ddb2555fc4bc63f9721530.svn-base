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

$(function() {
	
	/* Flowinstance overview scripts */
	
	var $tabs = $("#tabs");
	
	$tabs.tabs();
	
	$("ul.summary-buttons li a").on("click", function(e) {
		
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
	
	$('#groupID-search').each(function(){
		
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
	
	const newManagersForm = document.querySelector("#newManagersForm");
	const newManagerField = newManagersForm?.querySelector("#managerField");
	const newManagersInfoLabel = newManagersForm?.querySelector("#managerSearchInfolabel");

	if (newManagerField) {
		let isManagerListTooLarge = false;
		const getManagersURL = newManagerField.parentElement.dataset.url;
		const flowInstanceID = newManagerField.parentElement.dataset.flowinstanceid;

		const autocomplete = new Autocomplete({
			containerID: "managerField",
			confirmOnBlur: false,
			showAllValues: true,
			resetStyle: false,
			delayInMillis: 400,
			async sourceHandler(query, showResultsCallback) {
				showResultsCallback = showResultsCallback.bind(autocomplete);

				if (isManagerListTooLarge && query) {
					autocomplete.startSpinner();
					autocomplete.closeDropdown();

					const response = await fetch(getManagersURL + `/searchmanagers?flowInstanceID=${flowInstanceID}&searchQuery=` + query);
					const data = await response.json();

					showResultsCallback(data.managers);

					autocomplete.stopSpinner();
					autocomplete.openDropdown();
				} else {
					const filteredResults = autocomplete.results.filter((result) => result.Name.toLowerCase().includes(query.toLowerCase()));
					
					showResultsCallback(filteredResults);
					autocomplete.openDropdown();
				}
			},
			itemValue: (user) => user.ID,
			itemText: (user) => {
				if (user.Username) {
					return user.Name + " (" + user.Username + ")";
				}
				
				return user.Name;
			},
		});

		fetch(getManagersURL + `/fetchmanagers?flowInstanceID=${flowInstanceID}`).then((response) => {
			response.json().then((data) => {
				newManagersForm.querySelector("#loadingManagersText")?.classList.add("hidden");

				autocomplete.createTemplate();
				
				// Need to manually add icon for autocomplete because icon-arrow-down class from fontAwessome is not used in OEP
				const arrowIcon = newManagersForm.querySelector(".autocomplete__dropdown-arrow i")
				arrowIcon.textContent = "_";
				
				if (data.listTooLarge) {
					isManagerListTooLarge = true;
					autocomplete.settings.showAllValues = false;
					
					newManagersInfoLabel.classList.remove("hidden");
					newManagerField.setAttribute("aria-describedby", "managerSearchInfolabel");
				} else {
					autocomplete.setResults(data.managers);
				}
			});
		});

		autocomplete.on("itemSelected", (user) => {
			autocomplete.clearInput();

			const $managerList = $("#userID-list");
			const showUserURL = $managerList.find("input[name='showUserURL']").val();

			addUserGroupEntry(user, $managerList, getUserGroupPrefix($managerList), showUserURL);

			autocomplete.closeDropdown();
		});
	}

	/* Search flow instance scripts */
	
	$("#search").on("keyup", function () {
		
		$(this).parent().removeClass("searching");
		searchFlowInstance();
		
    }).on("keydown", function() {
    	$(this).parent().addClass("searching");
    }).on('focus blur', function() {
    	$(this).parent().toggleClass('focus');
	});
	
	$("div.search-results").find(".info .close").on("click", function(e) {
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
	
	$manager.find("a.delete").on("click", function(e) {
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
					
					result.hits.sort((a, b) => Date.parse(a.firstSubmitted) - Date.parse(b.firstSubmitted));
				
					$.each(result.hits, function( key, flowInstance) {
					
						var collectedFlowInstanceID = flowInstance.id;
						
						if(flowInstance.integrationExternalID) {
							collectedFlowInstanceID += " / "+flowInstance.integrationExternalID;
						}

						if(showSearchDecriptionColumn == true){
							
							
							
							
							var $flowInstanceRow = 
								'<tr onclick="location.href=\'' + flowInstanceAdminURI + "/overview/" + flowInstance.id + '\'">' + 
								'<td class"icon" />' +
								'<td data-title="' + i18nFlow +  '" class="service">' + flowInstance.name + '</td>' +
								'<td data-title="' + i18nFlowInstanceID + '" class="errandno">' + collectedFlowInstanceID + '</td>' +
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
								'<td data-title="' + i18nFlowInstanceID + '" class="errandno">' + collectedFlowInstanceID + '</td>' +
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