
$(document).ready(function() {
	
	$(".main.container").before($(".filters-wrapper"));
	
	var $flowTypeFilter = $("section.filter-list.persondata");
	
	var flowTypeCount = $flowTypeFilter.find("ul li a").length;
	
	var updateFilterStates = function($filter) {
		
		var $ul = $filter.find("ul");
		
		$ul.each(function() {
			
			var $clearFilterBtn = $(this).parent().find("h2 a.clear-filter-btn");
			
			if($(this).find("li a.selected").length > 0) {
				$clearFilterBtn.removeClass("disabled");
			} else {
				$clearFilterBtn.addClass("disabled");
			}
		});

		if($(".filter-list ul li a.selected").length > 0) {
			$(".clear-all-filter, .clear-all-filter-btn").slideDown("fast", function() { $(this).removeClass("hidden"); });
			$(".views-wrapper section.flowtype").addClass("showall");
			
		} else {
			$(".clear-all-filter, .clear-all-filter-btn").slideUp("fast", function() { $(this).addClass("hidden"); })
			$(".views-wrapper section.flowtype").removeClass("showall").find(".clear-filter-btn").hide();
		}
		
	};
	
	var updateFlowCounts = function() {
		
		$(".views-wrapper section.flowtype").each(function() {
			
			var $this = $(this);
			var count = $this.find("li.visible").length;
			
			$this.find("> .heading-wrapper h2 .count").text(count);
		});
		
	};
	
	var runFlowTypeFilter = function() {
		
		var $selectedFlowTypes = $flowTypeFilter.find("ul li a.selected");
		
		if ($selectedFlowTypes.length == 0) {
			
			$(".views-wrapper section.flowtype li").addClass("visible").show();
			
			$flowTypeFilter.find(".toggle-list-btn .count").text(flowTypeCount);

			$flowTypeFilter.removeClass("filtered");
			
		} else {
			
			var selectedPersonDataTypes = [];
			
			$flowTypeFilter.find("ul li a").each(function() {
				
				if ($(this).hasClass("selected")) {
					
					var personDataID = $(this).data("flowtypeid");
					selectedPersonDataTypes.push(personDataID.toString())
				}
				
			});
			
			$(".views-wrapper section.flowtype li").each(function() {
				
				var flow = $(this);
				flow.removeClass("visible").hide();
				
				flow.find("input[name='persondataid']").each(function(){
					
					var value = $(this).val();
					
					if (selectedPersonDataTypes.indexOf(value) != -1) {
						
						flow.addClass("visible").show();
						return false;
					}
				});
				
			});
			
			$flowTypeFilter.find(".toggle-list-btn .count").text($selectedFlowTypes.length + "/" + flowTypeCount);
			
			$flowTypeFilter.addClass("filtered");
			
		}
		
		updateFlowCounts();
	};
	
	$(".clear-all-filter a, a.clear-all-filter-btn").click(function(e) {
		e.preventDefault();
		
		$(".filter-list ul li a.selected").removeClass("selected");
		
		runFlowTypeFilter();
		
		updateFlowCounts();
		
		updateFilterStates($(".filter-list"));
		
		return false;
	});
	
	$flowTypeFilter.find("ul li a").click(function(e) {
		
		e.preventDefault();
		
		var $this = $(this);

		if($this.hasClass("disabled")) {
			return false;
		}
		
		if($this.hasClass("selected")) {
			$this.removeClass("selected");
		} else {
			$this.addClass("selected");
		}
		
		runFlowTypeFilter();
		
		updateFlowCounts();
		
		updateFilterStates($this.closest(".filter-list"));
		
		return false;
	});
});

function exportPersonData(event, button) {
	event.preventDefault();
	event.stopPropagation();
	
	var form = $(button).closest('form');
	
	form.find("input[name='flowFamilyID']").prop('disabled', true).closest("li").filter(":visible").find("input[name='flowFamilyID']").prop('disabled', false);
	
	form.submit();
}