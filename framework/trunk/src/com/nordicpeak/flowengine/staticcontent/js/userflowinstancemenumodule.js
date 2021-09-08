$(function() {

	$(document).on("click", "#MyPagesMenu a:first", function(e) {
        e.stopPropagation();
        e.preventDefault();

        $(this).parent().toggleClass('menu-active');
        
    }).on('click', '[data-menu] li a', function(e) {
        e.stopPropagation();
    }).on('click', function(e) {
    	$(this).parent().removeClass('menu-active');
    });

});