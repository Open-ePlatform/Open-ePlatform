$(function() {

	var helpBoxHeight = $('body').height();
	
	const helpBoxElements = document.querySelectorAll('div[data-help-box]');

	for (const helpBox of helpBoxElements) {
		helpBox.focusTrapper = new FocusTrapperHelpDialog(helpBox);
	}
	
    $(document).on('click', 'a[data-help-box]', function(e) {
        e.preventDefault();
        
        var help_box = $(this).data('help-box');
        
        const $targetHelpBox = $('div[data-help-box="' + help_box + '"]');

        $('div[data-help-box]').removeClass('active');
        $targetHelpBox.focusTrapper?.untrapFocus();

        $("div[data-help-box='" + help_box + "'].help-backdrop").show();
        $targetHelpBox.addClass('active').find('> div > div').attr('style', 'max-height: ' + (helpBoxHeight - 80) + 'px !important;');
        
        $targetHelpBox.focusTrapper?.trapFocus();

        $("body > header").css("position", "inherit");

    }).on('click touchend', '.help-backdrop, div[data-help-box] a.close', function(e) {
        e.preventDefault();

		$('div[data-help-box]')[0].focusTrapper?.untrapFocus();
		
        $('div[data-help-box]').removeClass('active');
        $(".help-backdrop").hide();

        $("body > header").css("position", "relative");
        
    }).on('keyup', function(e) {
        var key = e.keyCode ? e.keyCode : e.which;

        if (key === 27) {
        	$('div[data-help-box]')[0]?.focusTrapper.untrapFocus();
        	
            $('div[data-help-box]').removeClass('active');
            $(".help-backdrop").hide();
            
            $("body > header").css("position", "relative");
        }
    });

});

class FocusTrapperHelpDialog {
	#element = null

	#FOCUSABLE_SELECTORS = [
		'[contenteditable]',
		'[tabindex="0"]:not([disabled])',
		'a[href]',
		'audio[controls]',
		'button:not([disabled])',
		'iframe',
		"input:not([disabled]):not([type='hidden'])",
		'select:not([disabled])',
		'summary',
		'textarea:not([disabled])',
		'video[controls]',
	];

	constructor(element) {
		if (!element) {
			throw new Error("Element missing on instantiation");
		}

		this.#element = element;
	}

	trapFocus() {
		const firstFocusableElem = this.#element.querySelectorAll(this.#FOCUSABLE_SELECTORS)[0];

		firstFocusableElem.focus();

		this.#element.addEventListener("keydown", this.#keydownEventListener);
	}

	untrapFocus() {
		this.#element.removeEventListener("keydown", this.#keydownEventListener);
	}

	#keydownEventListener = (event) => {
		const focusableContent = this.#element.querySelectorAll(this.#FOCUSABLE_SELECTORS);
		const firstFocusableElem = focusableContent[0];
		let lastFocusableElem;

		if (event.key !== "Tab") return;

		for (const focusableElem of focusableContent) {
			if (this.#isVisible(focusableElem)) {
				lastFocusableElem = focusableElem;
			}
		}

		if (event.shiftKey) {
			if (document.activeElement === firstFocusableElem) {
				lastFocusableElem.focus();
				event.preventDefault();
			}
		} else if (document.activeElement === lastFocusableElem) {
			firstFocusableElem.focus();
			event.preventDefault();
		}
	}

	#isVisible(element) {
		return !!(element.offsetWidth || element.offsetHeight || element.getClientRects().length);
	}
}