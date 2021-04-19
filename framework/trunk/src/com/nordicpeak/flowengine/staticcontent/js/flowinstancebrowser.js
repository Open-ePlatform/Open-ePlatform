var searchFlowURI;
var userFavouriteModuleURI;
var notificationDialogDelay = 5000;

$(document).ready(function() {
	
	$(document).on("click", function(e) {
		$(".select-wrapper .select-box").removeClass("active");
	});
	
	$(".select-wrapper .select-box").each(function(i) {
		
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
		
		var flowTypeID = $selectBox.attr("id").split("_")[1];
		
		var $flowList = $("section#flowtype_" + flowTypeID  + " ul.previews");
		
		var $categories = $selectBox.find(".options ul li");
		
		$categories.each(function(j) {
			
			var $category = $(this);

			$category.find("a").click(function(e) {
				
				e.preventDefault();
				
				showCategoryFlows($category, $categories, $flowList);
				
				$categories.removeClass("selected");
				$category.addClass("selected");
				
				$selectBox.find("> span.text").text($(this).find(".text").text());
				$selectBox.find("> span.arrow").text("_");
				
			});
			
			if($category.hasClass("selected")) {
				
				showCategoryFlows($category, $categories, $flowList);
				
			}
			
		});
		
	});
	
	$(".filters a").click(function(e) {
        
		e.preventDefault();

        var elements = $('.filters').find('a');

        $(this).toggleClass('btn-blue').toggleClass('btn-light');
        elements.showAndHide();
        
    });
	
	$(".filter-wrapper > a").click( function(e) {
       
		e.preventDefault();
        var el = $(this);
        
        el.toggleClass('active');
        el.attr('data-icon-after', el.hasClass('active') ? '^' : '_');

    });
	
	$("a.show-all-link").each(function() {
		
		var $this = $(this);
		
		var $popularFlowsWrapper = $this.parent().find(".popularflows-wrapper");
		var $allFlowsWrapper = $this.parent().find(".allflows-wrapper");
		var $flowsList = $allFlowsWrapper.find("ul.previews");
		
		var $popularFlows = $flowsList.find("li.popular:lt(4)").clone();
		
		if($popularFlows.length == 0) {
			
			$popularFlows = $flowsList.find("li:lt(4)").clone();
			
		}
	
		if($popularFlows.length == 4) {
			
			$popularFlows.removeAttr("id");
			
			$popularFlowsWrapper.find("ul.previews").append($popularFlows);
			
			fixFlowList($popularFlowsWrapper.find("ul.previews"));
			
			$allFlowsWrapper.hide();
			
		} else {
			
			$popularFlowsWrapper.hide();
			$allFlowsWrapper.show();
			$this.hide();
		}
		
		fixFlowList($flowsList);
		
		$this.click(function(e) {
			
			e.preventDefault();
			
			$allFlowsWrapper.show();
			
			var currentScroll = $(document).scrollTop();
			var allFlowsPosition = $this.parent().find(".allflows-wrapper").offset().top - 20;
			
			$this.remove();
			
			if(currentScroll < allFlowsPosition) {
				$("html, body").animate({ scrollTop: allFlowsPosition }, "fast");
			}
			
		});
		
	});
	
	$("#search").keyup(function () {
		
		$(this).parent().removeClass("searching");
		searchFlow();
		
    }).keydown(function() {
    	$(this).parent().addClass("searching");
    }).bind('focus blur', function() {
    	$(this).parent().toggleClass('focus');
	});
	
	$("section.search-results").find(".info .close").click(function(e) {
		$(this).parent().parent().slideUp("fast").removeClass("active");
	});
	
	$(".tags-wrapper .tags a").click(function(e) {
		e.preventDefault();
		$("#search").val($(this).html().replace("#", ""));
		searchFlow();
		
	});
	
	$(document).on("click", ".accordion .accordion-toggler", function(e) {
        e.preventDefault();
        e.returnValue = false;
        $(this).parents("section").toggleClass("active");
    });
	
	var $readmoreText = $(".readmore-text");

	if ($readmoreText.length > 0 && $readmoreText.text().length <= 110) {	
		  $(".btn-readmore").remove();
	}
	
	// Copy exists in flowadminmodule.js
	$(".section-inside div.description .use-expandable .readmore-text").expander({
		slicePoint : 110,
		expandText : "",
		userCollapseText : "",
		expandEffect: "show",
		collapseEffect: "hide",
		expandSpeed: 0,
		collapseSpeed: 0,
		onSlice: function(options){
			
			var div = $(this);
			var noExpandElements = div.find(".noexpander");
			
			if (noExpandElements.length > 0) {
			
				var belowSummary = $('<div class="after-description-summary only-mobile description"/>');
				noExpandElements.clone().appendTo(belowSummary);
				belowSummary.insertAfter(div);
			}
		}
	});
	
	// Copy exists in flowadminmodule.js
	$(document).on("click", ".btn-readmore", function(e) {
        e.preventDefault();
        e.returnValue = false;
        
        var descriptionDiv = $(this).parents(".description");
        descriptionDiv.addClass("active");
        
		var belowSummaryDiv = descriptionDiv.siblings(".after-description-summary");
		
		if (belowSummaryDiv.length > 0) {
			
			belowSummaryDiv.remove();
		}
    });
	
});

function fixFlowList($flowsList) {
	
	$flowsList.find("li").each(function(i) {
		
		var $li = $(this);
		var position = $li.index();
		if(i % 2 != 0) {
			$li.css("clear", "none").addClass("odd");
			$li.before('<li class="separator" />');
		} else {
			$li.css("clear", "left").removeClass("odd");
		}
		
		var $description = $li.find("span.description");
		
		$description.find("span").removeAttr("style");
		$description.find("[face]").removeAttr("face");
		
	});
	
}

function showCategoryFlows($category, $categories, $flowList) {
	
	var categoryID = $category.attr("id");
	
	$flowList.find("li.separator").remove();

	$flowList.find("li:not(." + categoryID + ")").hide();
	
	$flowList.find("li." + categoryID).each(function(i) {
		var $this = $(this);
		var position = $this.index();
		if(i % 2 != 0) {
			$this.css("clear", "none").addClass("odd");
			$this.before('<li class="separator" />');
		} else {
			$this.css("clear", "left").removeClass("odd");
		}
	}).show();
	
}

function notifyUserFavouriteBackgroundModules(result) {
	
	if(typeof reloadUserFavourites === 'function') {
		reloadUserFavourites(result.UserFavourites);
	}
	if(typeof reloadUserFavouritesMenu === 'function') {
		reloadUserFavouritesMenu(result.UserFavourites);
	}
	
}

function searchFlow() {
	
	var searchStr = $('#search').val();

	var $searchResultWrapper = $("section.search-results");
	var $searchResultList = $searchResultWrapper.find("ul.previews");
	var $searchResultTitle = $searchResultWrapper.find(".search-results-title ");
	
	if(searchStr != "") {
		
		$.ajax({
			
			cache: false,
			url: searchFlowURI,
			data : {
				q : encodeURIComponent(searchStr)
			},
			dataType: "json",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) { },
			success: function(response) {

				var result = response;
				var hits = 0;
				
				$searchResultList.html("");
				
				if(response.hitCount > 0) {
				
					$.each(result.hits, function( key, flowID) {
			        	
						var $flow = $("section ul.previews li#flow_" + flowID);
						
						if($flow.length > 0){
							hits++;
							
				        	var $newFlow = $("<li>" + $flow.html() + "</li>")
				        	
				        	$newFlow.attr("id", $flow.attr("id"));
				        	
				        	if($flow.hasClass("disabled")) {
				        		$newFlow.addClass("disabled");
				        	}
				        	
				        	$searchResultList.append($newFlow);
						}
					});
				
				}
				
				$searchResultTitle.find(".title").text(searchStr);
		        $searchResultTitle.find(".hits").text(hits);
		        
		        $searchResultWrapper.addClass("active");
		        $searchResultWrapper.show();
				
			}
			
		});
		
        
    } else {
    	
    	$searchResultTitle.find(".title").text("");
    	$searchResultTitle.find(".hits").text(0);
    	$searchResultWrapper.removeClass("active");
    	$searchResultWrapper.hide();
    	$searchResultList.html("");
 
    }
	
}

function searchFlowInDOM() {
	
	var searchStr = $('#search').val();
	
	var $searchResultWrapper = $("div.search-results");
	var $searchResultList = $searchResultWrapper.find("ul.previews");
	var $searchResultTitle = $searchResultWrapper.find(".search-results-title ");
	
	if(searchStr != "") {
		
    	var filter = searchStr, count = 0;
        var result = new Array();
        
        $searchResultList.html("");
        
        $("section ul.previews li").each(function () {
            if ($(this).find("h2").text().search(new RegExp(filter, "i")) > -1) {
            	result.push($(this).clone());
                count++;
            }
        });
        
        $.each( result, function( key, value ) {
        	
        	var $item = $("<li>" + value.html() + "</li>")
        	
        	if(value.hasClass("disabled")) {
        		$item.addClass("disabled");
        	}
        	
        	$searchResultList.append($item);
    	});
        
        $searchResultTitle.find(".title").text(searchStr);
        $searchResultTitle.find(".hits").text(count);
        
        $searchResultWrapper.show();
        
    } else {
    	
    	$searchResultTitle.find(".title").text("");
    	$searchResultTitle.find(".hits").text(0);
    	$searchResultWrapper.hide();
    	$searchResultList.html("");
 
    }
	
}