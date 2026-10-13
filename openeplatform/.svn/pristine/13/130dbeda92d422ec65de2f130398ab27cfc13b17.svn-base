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
	
	const $messageAttachments = $('input[name="externalmessage-attachments"]');

	$messageAttachments.parents(".upload").on("drop", function(e) {

		e.preventDefault();

		$(this).removeClass('active');

		const $input = $(this).find('input:last');

		const droppedFiles = e.originalEvent.dataTransfer.files;

		$input.prop('files', droppedFiles);

		// Some browsers triggers 'change' on property change, if not we trigger it manually

		if ($input.is(':last-child')) {

			$input.trigger('change');

		}

	}).on('dragenter dragover', function(e) {

		e.preventDefault();

		$(this).addClass('active');

	}).on('dragleave', function(e) {

		$(this).removeClass('active');

	});
	
});