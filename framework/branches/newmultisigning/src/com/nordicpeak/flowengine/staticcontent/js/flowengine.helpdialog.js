$(document).ready(function() {

	var helpBoxHeight = $('body').height();

    $(document).on('click', 'a[data-help-box]', function(e) {
        e.preventDefault();
        
        var help_box = $(this).data('help-box');


        $('div[data-help-box]').removeClass('active');

        $("div[data-help-box='" + help_box + "'].help-backdrop").show();
        $('div[data-help-box="' + help_box + '"]').addClass('active').find('> div > div').attr('style', 'max-height: ' + (helpBoxHeight - 80) + 'px !important;');

        $("body > header").css("position", "inherit");

    }).on('click touchend', '.help-backdrop, div[data-help-box] a.close', function(e) {
        e.preventDefault();

        $('div[data-help-box]').removeClass('active');
        $(".help-backdrop").hide();

        $("body > header").css("position", "relative");
        
    }).on('keyup', function(e) {
        var key = e.keyCode ? e.keyCode : e.which;

        if (key === 27) {
        	
            $('div[data-help-box]').removeClass('active');
            $(".help-backdrop").hide();
            
            $("body > header").css("position", "relative");
        }
    });

});