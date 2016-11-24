$(document).ready(function() {
	
	$("#newEventsMenu a.submenu-trigger").click(function(e) {
		
		e.stopPropagation();
		e.preventDefault();
		
		var $menu = $(this).parent();
		
		var $submenu = $menu.find(".submenu");
		
		$menu.toggleClass("active");
		
		if ($menu.hasClass("active")) {
			$(".primary").removeClass("active");
			$submenu.show();
		} else {
			$submenu.hide();
		}
		
	});
	
});