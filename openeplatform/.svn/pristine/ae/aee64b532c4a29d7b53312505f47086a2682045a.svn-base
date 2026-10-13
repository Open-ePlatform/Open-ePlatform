$(function() {

	$(document).on("click", "#MyPagesMenu a:first", function(e) {
        e.stopPropagation();
        e.preventDefault();

        var $nav = $(this).parent();
        
        $nav.toggleClass('menu-active');
        
        if($nav.hasClass('menu-active')) {
        	$(this).attr("aria-expanded", "true");
        } else {
        	$(this).attr("aria-expanded", "false");
        }
        
    }).on('click', '[data-menu] li a', function(e) {
        e.stopPropagation();
    }).on('click', function(e) {
    	$(this).parent().removeClass('menu-active');
    });

});