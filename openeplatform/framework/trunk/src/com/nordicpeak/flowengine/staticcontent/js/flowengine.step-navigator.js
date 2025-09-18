$(function() {
	
	$(window).on("scroll touchmove", function() {
		
        if (/*$(".section.yellow").length === 0 || */$('.service-navigator .active').length === 0)
            return;

        if ($(window).scrollTop() > $(".service-navigator .active").offset().top + $(".service-navigator .active").height()) {
            $(".follow").attr('style', 'display: block !important');
            return;
        }

        $(".follow").attr('style', 'display: none !important');

    });
	
    if($('.service-navigator-wrap .service-navigator.primary').length > 0) {
        $('.service-navigator-wrap .service-navigator.primary').serviceNavigator();
    }
	
});

$.fn.showAndHide = function() {
    
	var elements = $(this),
        countElements = elements.length,
        countBlue = 0;

    elements.each(function() {
        var el = $(this),
            id = el.attr('href').replace('#', '');

        if(el.hasClass('btn-blue')) {
            $('.accordion[data-filter="' + id + '"]').show();
            ++countBlue;

            return true;
        }

        $('.accordion[data-filter="' + id + '"]').hide();
    });

    if(countBlue === 0 || countBlue === countElements) {
        $('.accordion[data-filter]').show();
    }


    return;

}

$.fn.serviceNavigator = function() {
    var el = $(this),
        parent = el.parent().parent(),
        hasNavigated = false,
        isAnimating = false;

        // Run a check immidietly
        check();

        // Center current step on init.

        // Bind UI Actions
        $(window).on('resize', check);
        
        //Bind load event when all images are also loaded
		$(window).on('load', check);        
        
        parent.find('.js-next').on('click tap', function(e) {
            e.preventDefault();

            if(isAnimating)
                return false;

            isAnimating = true;

            if($(this).hasClass('disabled')) {
                isAnimating = false;
                return false;
            }

            $('.js-prev').removeClass('disabled');

            hasNavigated = true;
            el.addClass('navigated');
                
            el.moveChildren(getLastVisible(), 0, function() {
                isAnimating = false;
            });

            return true;
        });
        parent.find('.js-prev').on('click tap', function(e) {
            e.preventDefault();


            if(isAnimating)
                return false;

            isAnimating = true;

            if($(this).hasClass('disabled')) {
                isAnimating = false;
                return false;
            }

            $('.js-next').removeClass('disabled');

            hasNavigated = true;
            el.addClass('navigated');

            el.moveChildren(getFirstVisible(), 0, function() {
                isAnimating = false;
            });

            return true;
        });

        // Check if anything has to be done on init/resize
        function check(e) {
            // If window is smaller than 1005px, block-style navigation will be shown.
            // Hence remove everything affected and return.
            
        	if($(window).width() < 987) {
                parent.removeClass('shadow-left shadow-right');
                return;
            }

            // Count total width of children
            var btnWidths = parent.hasClass('steps-fit') ? 0 : parent.find('.js-next').width() + parent.find('.js-prev').width();
                t = el.width() + btnWidths;
            
            var stepWidth = 0;
            
            el.find('li').each(function() {
            	stepWidth += $(this).outerWidth();
            });
            
            //t = (t - stepWidth) + 6;
            t = (t - stepWidth);
            
            parent.toggleClass('steps-fit', (t > 0));

            if(parent.hasClass('steps-fit')) {
                el.find('li').removeAttr('style');
            }

            // Initialize variables
            el.setShadows();

            if(!hasNavigated) {
                
            	if(typeof e === 'undefined' && !parent.hasClass("steps-fit")) {
            		centerCurrentStep();
                }

                if(el.find('li').first().attr('data-offset') === 0 || typeof el.find('li').first().attr('data-offset') === 'undefined') {
                    $('.js-prev').addClass('disabled');
                }

                //var t = stepWidth + 6;
                var t = stepWidth;
                
                //el.find('li').outerWidth(function(i,w){t+=w;});

                if(parseInt(el.find('li').first().attr('data-offset') * -1) + el.find('li').last().outerWidth() >= t) {
                    $('.js-next').addClass('disabled');
                }
            }
        }

        // Function to show the current step as the most center one
        function centerCurrentStep() {

            if(el.find('.active').length === 0)
                return;

            var lastVisible = el.find('.active'),
                upcoming = lastVisible.nextAll().length;

            if(lastVisible.length > 0) {

                var a = lastVisible.prev().length !== 0 ? lastVisible.prev().position().left : 0,
                    b = Math.ceil(lastVisible.position().left + lastVisible.outerWidth()),
                    cOff = el.find('li').first().attr('data-offset'),
                    c = typeof cOff !== 'undefined' ? parseInt(cOff) : 0,
                    d = el.width();

                    var extra = 0;

                    if(lastVisible.nextAll().length === 0) {
                        $('.js-next').addClass('disabled');
                        extra = -1;

                        el.moveChildren(c - b + d - extra, 0, undefined, false);
                        return;
                    }

                    el.moveChildren(c - a + extra, 0, function() {

                        if(extra < 0) {
                            parent.toggleClass('shadow-right', false);
                        }
                    }, true);

            }

            return false;
        }

        function getFirstVisible() {
            var w = 0,
                firstVisible = el.find('li').filter(function(i) {
                    var li = $(this),
                    l = li.position().left;
                    w += li.outerWidth();

                    return l < 0;
                });

            if(firstVisible.length > 0) {
                firstVisible = firstVisible.last();
                var a = firstVisible.position().left,
                    bEl = el.find('li').last(),
                    b = bEl.position().left,
                    cOff = el.find('li').first().attr('data-offset'),
                    c = typeof cOff !== 'undefined' ? parseInt(cOff) : 0,
                    firstChild = el.find('li').first(),
                    extra = 70;

                if(firstVisible.prevObject.length == 1) {
                    $('.js-prev').addClass('disabled');
                    extra = 0;
                }

                return c - a + extra;
            }

            return false;
        }

        function getLastVisible() {

            var lastVisible = el.find('li').filter(function() {
                var li = $(this),
                l = li.width() + li.position().left;

                return l >= el.width();
            });

            if(lastVisible.length > 0) {

                var a = el.width(),
                    b = Math.ceil(lastVisible.position().left + lastVisible.outerWidth()),
                    cOff = el.find('li').first().attr('data-offset'),
                    c = typeof cOff !== 'undefined' ? parseInt(cOff) : 0;

                    var last = el.find('li').last().position().left + el.find('li').last().outerWidth(),
                        elWidth = el.outerWidth(),
                        extra = 70;

                    if(lastVisible.length == 1) {
                        $('.js-next').addClass('disabled');
                        extra = 0;
                    }

                    return c-b+a - extra;

            }

            return false;
        }

}

$.fn.setShadows = function() {
    var el = $(this),
        parent = el.parent().parent(),
        t = 0,
        shadows = {
            left: false,
            right: false
        },
        offset = {
            left: el.find('li').first().attr('data-offset'),
            right: 0
        };
    
    // Count total width of children
    el.children().outerWidth(function(i,w){t+=w;});

    // If total child width is less than or equal to parent width
    // remove shadows.
    if(t <= el.width()) {
        parent.removeClass('shadow-left shadow-right');
        el.removeAttr('style');
        return;
    }

    // Toggle shadows
    parent.toggleClass('shadow-left', shadows.left);
    parent.toggleClass('shadow-right', shadows.right);
}

$.fn.moveChildren = function(x, y, callback, init) {

    if(x === false)
        return;

    var el = $(this);

    init = typeof init !== 'undefined' ? init : false

    x = typeof x !== 'undefined' ? Math.ceil(x) : 0;
    y = typeof y !== 'undefined' ? Math.ceil(y) : 0;

    var _x = x != 0 ? x + 'px' : x,
        _y = y != 0 ? y + 'px' : y,
        z = x * -1;

    if(z > el.width() && init !== false) {

        x = _x = el.width() - el.find('.active').position().left - el.find('.active').outerWidth();

        if(el.find('.active').next().length > 0) {
            x -= el.find('.active').next().outerWidth(),
            _x -= el.find('.active').next().outerWidth();
        }

        if(el.find('.active').nextAll().length < 2) {
            $('.js-next').addClass('disabled');
        }
    }

    if(x === 0) {
        $('.js-prev').addClass('disabled');
    }

    if(typeof callback === 'function')
        setTimeout(callback, 300);

    el.find('li').each(function() {

        $(this).attr('data-offset', x);
        $(this).animate({
            'left': _x
        }, 300, 'easeOutQuad');

    });

    el.setShadows();
}