$(function() {
	
	$("table.coloredtable").each(function() {
		var $this = $(this);
		$this.find("tr:not(:first):visible:even").removeClass("odd").addClass("even");
		$this.find("tr:not(:first):visible:odd").removeClass("even").addClass("odd");
	});
	
	$("#tabs").tabs();
	
	$(".heading span[title]").tooltip({
		position: {
			my: "right bottom-22",
			at: "center",
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
	
});