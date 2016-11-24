var actionURL;
var currentAjaxOptions;
var currentAjaxRequest;
var setQueryRequiredFunctions = {};
var setQueryValidationErrorFunctions = {};
var initQueryFunctions = {};
var hideQueryFunctions = new Array();
var loadedScriptsCounters = {};
var flowEngineDOMListeners = new Array();

$(document).ready(function() {
	
	var queryAnchorName = $(".validationerrors").first().attr("id");
	
	if(queryAnchorName) {
		window.location.hash = queryAnchorName.split("-")[0];
	}
	
	actionURL = $("#FlowBrowser form").attr("action");
	
	$(".modal .close").click(function(e) {
		e.preventDefault();
		$(this).parent().fadeOut("fast", function() {
			$(this).remove();
		});
	});
	
	$(".info-box .close").click(function(e) {
		e.preventDefault();
		var $wrapper = $(this).parent().parent();
		$wrapper.next().fadeOut("fast", function() {
			$wrapper.remove();
		});
	});
	
	$("i[title]").tooltip({
		position: {
			my: "right+29 bottom-22",
			collision: "flipfit"
		},
		track: false,
		content: function () {
          return $(this).prop('title') + "<span class=\"marker\"></span>";
      	},
      	show: {
      		effect: 'none'
      	},
      	hide: {
      		effect: 'none'
      	}
	});
	
	$("section .section-full").find("article:first").addClass("first");
	
    $(window).on('resize', function() {
    	helpBoxHeight = $('body').height();
        $('div[data-help-box]').find('> div > div').attr('style', 'max-height: ' + (helpBoxHeight - 80) + 'px !important;');
    });
	
	//initFlowInstanceControlPanel();
	
	var $activeStep = $("section.service .service-navigator.primary li.active");
    
	$activeStep.prev().addClass("latest completed");
    
    var $futureSteps = $activeStep.nextAll();
    $futureSteps.clone().appendTo($("#futureNavigator"));
    $futureSteps.addClass("future");
    
    $(document).on('click', '[data-toggle-menu]', function(e) {
        e.stopPropagation();
        e.preventDefault();
        var menu = $(this).data('toggle-menu');

        $('[data-menu="' + menu + '"]').toggleClass('menu-active');
        
    }).on('click', '[data-menu] li a', function(e) {
        e.stopPropagation();
    }).on('click', function(e) {
        $('[data-menu]').removeClass('menu-active');
    });
    
    var $startPanel = $(".start-flow-panel");
    
    if($startPanel.length > 0) {
    	
	    $(window).scroll(checkStartFlowPanelPosition);
	    $(window).resize(checkStartFlowPanelPosition);
	    
    }
	
});

function checkStartFlowPanelPosition() {
	
	var $startPanel = $(".start-flow-panel");
	
	if ($(window).width() > 1004 && $(this).scrollTop() >= ($("header").height() + 40)) {
		var rightMargin = ($(window).width() - $startPanel.closest("section").width()) / 2;
		$startPanel.css("right", rightMargin);
		$startPanel.addClass("fixed");
	} else {
		$startPanel.removeClass("fixed");
	}
	 
}

function submitStep(mode, e) {

	if (e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}

	if ($("#submitLoadingMessage").length > 0) {
		$.blockUI({
			message : $("#submitLoadingMessage")
		});
	}

	$("#submitmode").attr("name", mode);

	$("section.service form").submit();

	var target = e.target || e.srcElement;

	if ($(target).hasClass("unblock-submit")) {
		$.unblockUI();
	}

}


function redirectFromPreview(e, parameters) {
	
	if(e.preventDefault) {
		e.preventDefault();
	} else {
		e.returnValue = false;
	}
	
	if($("#submitLoadingMessage").length > 0) {
		$.blockUI({ message: $("#submitLoadingMessage") });
	}
	
	var params = "";
	
	if(parameters) {
		params = "?" + parameters
	}
	
	window.location = $("section.service form").attr("action") + params;
	
}

function toggleQueryHelpText(queryID) {
	
	$("#help_" + queryID).toggle();
	$("#closehelp_" + queryID).toggle();
	$("#openhelp_" + queryID).toggle();
	
}

function toggleFlowDescription(element, showLink, hideLink) {
	
	var $element = $(element);
	
	var $description = $(element).parent().next();
	
	$description.toggle();
	
	if($description.is(":visible")) {
		$element.html("- " + hideLink);
	} else {
		$element.html("+ " + showLink);
	}
	
}

function runQueryEvaluators(queryID, parameters) {
	
	parameters["queryID"] = queryID;
	
	currentAjaxOptions = {
		type: "POST",
		cache: false,
		url: actionURL,
		data: parameters,
		dataType: "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		error: function (xhr, ajaxOptions, thrownError) { 
			showErrorDialog(thrownError);
		},
		beforeSend: function ( xhr ) {  
			setTimeout(function() {
				if(currentAjaxRequest != undefined && currentAjaxRequest.readyState != 4) {
					$.blockUI({ message: $("#ajaxLoadingMessage") });
				}
			}, 1000);
        },
		success: function(response) {
			if(currentAjaxRequest.getResponseHeader("AjaxPostValid") == "true") {
				parseResponse(response);
			} else {
				showErrorDialog();
			}
		}
	};
	
	currentAjaxRequest = $.ajax(currentAjaxOptions);
	
}

function parseResponse(response) {
	
	if(response.QueryModifications) {
		
		$.each(response.QueryModifications, function(i, queryModification) {
			
			loadedScriptsCounters[queryModification.queryID] = 0;
			
			var actionHandled = false;
			
			appendDependencies(queryModification);
			
			if(queryModification.Scripts) {
				
				$.each(queryModification.Scripts, function(i, script) {
					
					if(!scriptIsLoaded(script)) {
						
						$.getScript(script.src).done(function(loadedScript, textStatus) {

							loadedScriptsCounters[queryModification.queryID]++;
							
						}).fail(function(jqxhr, settings, exception) {

							showErrorDialog(exception);
							
							return false;
						
						});
						
					} else {
						
						loadedScriptsCounters[queryModification.queryID]++;
						
					}
					
				});
				
				setTimeout(function() { waitUntilScriptsAreLoaded(queryModification); }, 5);
				
			} else {
				
				runAction(queryModification);
					
			}
			
			if(queryModification.Links) {
				
				$.each(queryModification.Links, function(i, link) {
					
					
				});
				
			}
			
		});
		
	}
	
	if(response.ValidationErrors) {
		
		if(typeof setQueryValidationErrorFunctions[response.ValidationErrors.queryType] == "function") {
			
			setQueryValidationErrorFunctions[response.ValidationErrors.queryType](response.ValidationErrors.queryID, response.ValidationErrors.errors);
		}
		
	}
	
	$.unblockUI();
	
}

function waitUntilScriptsAreLoaded(queryModification) {
	
	if(queryModification.Scripts.length == loadedScriptsCounters[queryModification.queryID]) {

		runAction(queryModification);
		
	} else {
		
		setTimeout(function() { waitUntilScriptsAreLoaded(queryModification); }, 5);
	
	}
	
}

function runAction(queryModification) {
	
	var action = queryModification.action;
	var queryID = queryModification.queryID;
	
	if(action == "SHOW") {

		showQuery(queryID, queryModification.formHTML);
		
	} else if(action == "MAKE_REQUIRED") {
		
		if($("#query_" + queryID).html().length > 0) {
			
			if(typeof setQueryRequiredFunctions[queryModification.queryType] == "function") {
				
				setQueryRequiredFunctions[queryModification.queryType](queryID);
				
			}
		
		} else {

			showQuery(queryID, queryModification.formHTML);
			
		}
		
	} else if(action == "HIDE") {
		
		hideQuery(queryID);
		
	} else if(action == "RELOAD") {
		
		reloadQuery(queryID, queryModification.formHTML);
		
	}
	
	
	
}

function showErrorDialog(error) {
	
	if(console != undefined && error != undefined){
		console.error(error);
	}
	
	$.blockUI({ message: $("#ajaxErrorMessage") });
	
}

function showQuery(queryID, html) {
	
	if($("#query_" + queryID).length == 0) {
		
		$(".queries").append(html);
		
	} else {
		
		reloadQuery(queryID, html);
		
	}
	
}

function hideQuery(queryID) {
	
	if(hideQueryFunctions.length > 0) {
		
		$.each(hideQueryFunctions, function(index, listener) {
			
			if(typeof listener == "function") {
				
				listener($("#query_" + queryID));
				
			}
			
		});
		
	}
	
	$("#query_" + queryID).empty().removeAttr("class").addClass("hidden");

}

function reloadQuery(queryID, html) {
	
	$("#query_" + queryID).replaceWith(html);
	
	if(flowEngineDOMListeners.length > 0) {
		
		$.each(flowEngineDOMListeners, function(index, listener) {
			
			if(typeof listener == "function") {
				
				listener($("#query_" + queryID));
				
			}
			
		});
		
	}
	
}

function reloadCurrentStep() {
	
	$("form").submit();
	
}

function appendDependencies(queryModification) {
	
	var initializeQuery = false;
	
	if(queryModification.Scripts) {
		
		$.each(queryModification.Scripts, function(i, script) {
			
			if(!scriptIsLoaded(script)) {
				
				$.getScript(script.src)
				.done(function(loadedScript, textStatus) {

					initializeQuery = true;
					
				}).fail(function(jqxhr, settings, exception) {

					showErrorDialog(exception);
					
					return false;
				
				});
				
			}
			
		});
		
	}
	
	if(queryModification.Links) {
		
		$.each(queryModification.Links, function(i, link) {
			
			var $head = $("head");
			
			if(!linkIsLoaded(link)) {
				
				$("<link/>", {
					   href: link.href,
					   media: link.media,
					   rel: link.rel,
					   type: link.type
				}).appendTo($head);
				
			}
			
		});
		
	}
	
	return initializeQuery;
	
}

function retryAjaxPost() {
	
	$.blockUI({ message: $("#ajaxLoadingMessage") });
	
	currentAjaxRequest = $.ajax(currentAjaxOptions);
	
}

function cancelAjaxPost() {
	
	if(currentAjaxRequest != undefined) {
	
		currentAjaxRequest.abort();
	
	}
	
	$.unblockUI();
	
}

function closeErrorDialog() {
	
	$.unblockUI();
	
}

function linkIsLoaded(link) {
	
	if ($("link[href='" + link.href + "']").length == 0) {
		return false;
	} 
	
	return true;
	
}

function scriptIsLoaded(script) {
	
	if ($("script[src='" + script.src + "']").length == 0) {
		return false;
	} 
	
	return true;
	
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

function initFlowInstanceControlPanel() {
	
	var $controlPanel = $(".panel-wrapper")
	
	if($controlPanel.length > 0) {
	
		var $window = $(window);
	
		var $header = $("body header");
		
		$window.bind("scroll resize", function(e) {
			
			var pos = $window.scrollTop();
			
			var hh = $header.height();
			
			if(pos > hh) {
				
				$controlPanel.css({position: "absolute", top: (pos-hh) + "px"});
				
			} else {
				
				$controlPanel.css({position: "static"});
				
			}
			
		});
	
	}
	
}