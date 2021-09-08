$(function() {

	setQueryRequiredFunctions["TreeQueryInstance"] = makeTreeQueryRequired;
});

function initTreeQuery(queryID, previouslySelected) {

	waitUntilTreeQueryScriptsAreLoaded(queryID, previouslySelected);
}

function waitUntilTreeQueryScriptsAreLoaded(queryID, previouslySelected) {
	
	if(typeof $.ui.fancytree === "undefined" || typeof $.ui.fancytree._FancytreeClass.prototype.clearFilter === "undefined" || typeof $.ui.fancytree._FancytreeClass.prototype.getPersistData === "undefined") {
		
		setTimeout(function() { waitUntilTreeQueryScriptsAreLoaded(queryID, previouslySelected); }, 5);
		
	} else {
		
		initTreeQuery2(queryID, previouslySelected);
	}
}

function initTreeQuery2(queryID, previouslySelected) {
	
	var query = $("#query_" + queryID);
	var $tree = $("#tree" + queryID);
	
	if ($tree.length > 0) {
		
		var selectedInput = query.find("input.selectedkey");
		
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
			source: window["TreeQueryTree" + queryID],
			
			// https://github.com/mar10/fancytree/wiki/ExtPersist
			persist: {
				cookiePrefix: "fancytree-treequery-" + queryID,
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
				if(onlyleafselection && data.node.hasChildren()){
					return false;
				}
			},
			
			activate: function(event, data) {
				selectedInput.val(data.node.key);
				
				if (query.hasClass("enableAjaxPosting")) {
					var parameters = {};
					parameters[selectedInput.attr("name")] = data.node.key;
					
					runQueryEvaluators(queryID, parameters);
				}
			},
		});
		
		var tree = $tree.fancytree("getTree");
		
		if (previouslySelected != '') {
			tree.activateKey(previouslySelected, {activeVisible: true});
		}
		
		var KEYCODE_ENTER = 13;
		var KEYCODE_ESC = 27;
			
		$("input[name='filterField" + queryID + "']").keyup(function(event){
			
			var $this = $(this);
			var filter = $this.val();

			if (event && event.keyCode == KEYCODE_ESC || $.trim(filter) === "") {
				
				$this.val("");
				
				tree.clearFilter();
				return;
			}
			
			tree.filterNodes(filter, {autoExpand: true});
			
		}).keypress(function(event){
			
			if(event.keyCode == KEYCODE_ENTER){
				event.preventDefault();
			}
		});
		
		query.closest("form").submit(function(){
			tree.clearFilter();
		});
	}
}

function treeQueryCreateShowTree(queryID) {

	var tree = $("#tree" + queryID);
	
	if (tree.length > 0) {
		
		// https://github.com/mar10/fancytree/wiki
		tree.fancytree({
			extensions: [],
			autoActivate: false, 	// Automatically activate a node when it is focused (using keys).
			autoScroll: true, 		// Automatically scroll nodes into visible area.
			clickFolderMode: 1, 	// 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
			debugLevel: 1, 			// 0:quiet, 1:normal, 2:debug
			minExpandLevel: 99, 	// x: nodes x from invisible root (inclusive) is not collapsible
			titlesTabbable: false,  // Add all node titles to TAB chain
			quicksearch: false,		// Jump to nodes when pressing first character
			keyboard: false,
			source: window["TreeQueryTree" + queryID],
			
			beforeActivate: function(event, data) {
				return false;
			},
		
			beforeSelect: function(event, data) {
				return false;
			}
		});
	}
}

function makeTreeQueryRequired(queryID) {

	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
}