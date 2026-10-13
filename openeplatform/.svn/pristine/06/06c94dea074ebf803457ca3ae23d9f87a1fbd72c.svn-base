var i18n = {
		NoChosenTrees: "NoChosenTrees",
};

$(function() {

	setQueryRequiredFunctions["MultiTreeQueryInstance"] = makeMultiTreeQueryRequired;
});

function initMultiTreeQuery(queryID) {
	
	var nonVarargParametersCount = initMultiTreeQuery.length;
	
	if (arguments.length > nonVarargParametersCount) {
		
		var previouslySelected = new Array(arguments.length - nonVarargParametersCount);
		
		for (var i = 0; i < previouslySelected.length; i++) {
			
			previouslySelected[i] = arguments[i + nonVarargParametersCount];
		}
	}

	waitUntilMultiTreeQueryScriptsAreLoaded(queryID, previouslySelected);
}

function waitUntilMultiTreeQueryScriptsAreLoaded(queryID, previouslySelected) {
	
	if(typeof $.ui.fancytree === "undefined" || typeof $.ui.fancytree._FancytreeClass.prototype.clearFilter === "undefined" || typeof $.ui.fancytree._FancytreeClass.prototype.getPersistData === "undefined") {
		
		setTimeout(function() { waitUntilMultiTreeQueryScriptsAreLoaded(queryID, previouslySelected); }, 5);
		
	} else {
		
		initMultiTreeQuery2(queryID, previouslySelected);
	}
}

function initMultiTreeQuery2(queryID, previouslySelected) {
	
	var $query = $("#query_" + queryID);
	var $tree = $("#tree" + queryID);
	
	if ($tree.length > 0) {
		
		var selectedInputTemplate = $query.find("input.selectedkey-template");
		
		var onlyleafselection = $tree.data("nofolders") == true;
		
		// https://github.com/mar10/fancytree/wiki
		$tree.fancytree({
			extensions: ["persist", "filter"],
			autoActivate: false,	// Automatically activate a node when it is focused (using keys).
			autoScroll: true,		// Automatically scroll nodes into visible area.
			clickFolderMode: 4,		// 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
			debugLevel: 1,			// 0:quiet, 1:normal, 2:debug
			minExpandLevel: 2,		// x: nodes x from invisible root (inclusive) is not collapsible
			titlesTabbable: true,	// Add all node titles to TAB chain
			quicksearch: true,		// Jump to nodes when pressing first character
			keyboard: false,
			source: window["MultiTreeQueryTree" + queryID],
			
			// https://github.com/mar10/fancytree/wiki/ExtPersist
			persist: {
				cookiePrefix: "fancytree-multitreequery-" + queryID,
				overrideSource: false,  // true: cookie takes precedence over `source` data attributes.
				store: "session",	 // 'cookie': use cookie, 'local': use localStore, 'session': use sessionStore
				types: "expanded",  // which status types to store "active expanded focus selected"
				expandLazy: true
			},
			
			// https://github.com/mar10/fancytree/wiki/ExtFilter
			filter: {
				mode: 'hide', // 'dimm' | 'hide'
				highlight: true,
				nodata: 'Ingen sökträff'
			},
			
			focus: function(event, data) {
				data.node.scrollIntoView(true);
			},
			
			beforeActivate: function(event, data){
				return false;
			},
			
			select: function(event, data) {
				
				var key = data.node.key;
				
				var selectedInput = $query.find("input.selectedkey[value='" + key + "']");
				
				if (data.node.isSelected()) {
					
					if (!selectedInput.length) {
						
						selectedInput = selectedInputTemplate.clone();
						selectedInput.removeClass("selectedkey-template");
						selectedInput.addClass("selectedkey");
						selectedInput.prop("disabled", false);
						selectedInput.val(key);
						
						selectedInputTemplate.parent().append(selectedInput);
					}
					
				} else {
					
					selectedInput.remove();
				}
				
				if ($query.find(".chosen-tree-list").length) {

					updatePreviewList(queryID);
					
				} else {
					
					filterPreviewTree(queryID);
				}
				
				if ($query.hasClass("enableAjaxPosting")) {
					
					var parameters = {};
					
					selectedInputTemplate.siblings(".selectedkey").each(function(){
						
						parameters[this.name] = this.key;
					});
					
					runQueryEvaluators(queryID, parameters);
				}
			},
			
			checkbox: function(event, data) {
				if (onlyleafselection && data.node.hasChildren()){
					return false;
				}
				return true;
			},

		});
		
		var tree = $tree.fancytree("getTree");
		
		if (previouslySelected) {
			
			for (var i = 0; i < previouslySelected.length; i++) {
				
				var key = previouslySelected[i];
				var node = tree.getNodeByKey(key);
				
				if (node) {
					
					node.setSelected(true);
					
					expandNodeHierarchy(node);
				}
			}
		}
		
		var KEYCODE_ENTER = 13;
		var KEYCODE_ESC = 27;
			
		$("input[name='filterField" + queryID + "']").on("keyup", function(event){
			
			var $this = $(this);
			var filter = $this.val();

			if (event && event.keyCode == KEYCODE_ESC || $.trim(filter) === "") {
				
				$this.val("");
				
				tree.clearFilter();
				return;
			}
			
			tree.filterNodes(filter, {autoExpand: true});
			
		}).on("keypress", function(event){
			
			if(event.keyCode == KEYCODE_ENTER){
				event.preventDefault();
			}
		});
		
		$query.closest("form").on("submit", function(){
			tree.clearFilter();
		});
	}
}

function multiTreeQueryCreatePreviewTree(queryID) {
	
	var $tree = $("#tree-preview" + queryID);
	
	if ($tree.length > 0) {
		
		// https://github.com/mar10/fancytree/wiki
		$tree.fancytree({
			extensions: ["filter"],
			autoActivate: false, 	// Automatically activate a node when it is focused (using keys).
			autoScroll: true, 		// Automatically scroll nodes into visible area.
			clickFolderMode: 1, 	// 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
			debugLevel: 1, 			// 0:quiet, 1:normal, 2:debug
			minExpandLevel: 99, 	// x: nodes x from invisible root (inclusive) is not collapsible
			titlesTabbable: false,  // Add all node titles to TAB chain
			quicksearch: false,		// Jump to nodes when pressing first character
			keyboard: false,
			source: window["MultiTreeQueryPreviewTree" + queryID],
			strings: {
				noData: i18n.NoChosenTrees
			},
			
			beforeActivate: function(event, data) {
				return false;
			},
			
			beforeSelect: function(event, data) {
				return false;
			}
		});
		
		filterPreviewTree(queryID);
	}
}

function multiTreeQueryCreateShowTree(queryID) {

	var $tree = $("#tree" + queryID);
	
	if ($tree.length > 0) {
		
		// https://github.com/mar10/fancytree/wiki
		$tree.fancytree({
			extensions: [],
			autoActivate: false, 	// Automatically activate a node when it is focused (using keys).
			autoScroll: true, 		// Automatically scroll nodes into visible area.
			clickFolderMode: 1, 	// 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
			debugLevel: 1, 			// 0:quiet, 1:normal, 2:debug
			minExpandLevel: 99, 	// x: nodes x from invisible root (inclusive) is not collapsible
			titlesTabbable: false,  // Add all node titles to TAB chain
			quicksearch: false,		// Jump to nodes when pressing first character
			keyboard: false,
			source: window["MultiTreeQueryTree" + queryID],
			
			beforeActivate: function(event, data) {
				return false;
			},
		
			beforeSelect: function(event, data) {
				return false;
			}
		});
	}
}

function makeMultiTreeQueryRequired(queryID) {

	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}

function filterPreviewTree(queryID) {
	
	var $query = $("#query_" + queryID);
	
	$.ui.fancytree.getTree("#tree-preview" + queryID).filterNodes(
		function(node) {
		
			return !!$query.find("input.selectedkey[value='" + node.key + "']").length;
		},
		{
			mode: "hide"
		}
	);
}

function updatePreviewList(queryID) {
	
	var $query = $("#query_" + queryID);
	var $chosenTreeList = $query.find(".chosen-tree-list");
	var tree = $.ui.fancytree.getTree("#tree" + queryID);
	
	$chosenTreeList.find(".chosen-tree").remove();
	
	$query.find("input.selectedkey").each(function(){
		
		var key = this.value;
		var node = tree.getNodeByKey(this.value)
		
		if (!node) {
			
			return;
		}
		
		var $chosenTree = $chosenTreeList.find(".chosen-tree-template").clone();
		$chosenTree.removeClass("chosen-tree-template hidden");
		$chosenTree.addClass("chosen-tree");
		
		$chosenTree.data("key", key);
		
		var nodeHierarchy = getNodeHierarchy(node); 
		
		$chosenTree.find(".chosen-tree-name").html(nodeHierarchy);

		$chosenTree.find(".remove-chosen-tree").on("click", function(){
			
			var removedTreeKey = $(this).closest(".chosen-tree").data("key");
			
			var nodeToUnselect = tree.getNodeByKey(removedTreeKey);
			
			if (nodeToUnselect) {
				
				nodeToUnselect.setSelected(false);
			}
			
			updatePreviewList(queryID);
		});
		
		if ($chosenTreeList.children(".chosen-tree").length) {
			
			$chosenTree.insertAfter($chosenTreeList.children(".chosen-tree").last());
			
		} else {
			
			$chosenTree.insertAfter($chosenTreeList.children(".chosen-tree-template"));
		}
		
	});
	
}

function getNodeHierarchy(node, truncate) {
	
	var title = truncate && node.title.length > 10 ? node.title.substring(0, 10) + "..." : node.title;
	
	if (node.parent && node.parent.title != "root") {
		
		return getNodeHierarchy(node.parent, true) + " > " + title;
	}
	
	return title;
}

function expandNodeHierarchy(node) {
	
	if (node.isExpanded()) {
		
		return;
	}
	
	if (node.parent) {
		
		expandNodeHierarchy(node.parent);
	}
	
	node.setExpanded(true);
}