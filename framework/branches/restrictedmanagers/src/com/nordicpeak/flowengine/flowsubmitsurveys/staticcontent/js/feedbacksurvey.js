var chartData = null;

$(document).ready(function() {
	
	$("#FeedbackFlowSubmitSurvey a.comment-btn").click(function(e) {
		
		e.preventDefault();
		
		$(this).parent().find("textarea").slideDown("fast");
		$(this).remove();
		
	});
	
	$("#FeedbackFlowSubmitSurvey .submit-btn").click(function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		
		var $form = $this.closest("form");
		
		$.ajax({
			type: "POST",
			cache: false,
			url: $form.attr("action"),
			data: $form.serialize(),
			dataType: "html",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) {  },
			success: function(response) {
				
				if(response.indexOf("error") != -1) {
					
					$("#FeedbackFlowSubmitSurvey .validationerrors").html(response);
					
				} else {
					
					$("#FeedbackFlowSubmitSurvey").replaceWith($(response));
					
				}
				
			}
		});
		
	});
	
	$("#FeedbackFlowSubmitSurvey a.show-comments-trigger").click(function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		
		$this.parent().find("table").slideDown("fast", function() {
			
			$this.hide();
			$("#FeedbackFlowSubmitSurvey a.hide-comments-trigger").show();
			
		});
		
	});
	
	$("#FeedbackFlowSubmitSurvey a.hide-comments-trigger").click(function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		
		$this.parent().find("table").slideUp("fast", function() {
			
			$this.hide();
			$("#FeedbackFlowSubmitSurvey a.show-comments-trigger").show();
			
		});
		
	});
	
	
	if(chartData != null) {
		
		var cols = new Array();
		cols.push(chartData);
		
		var chart = c3.generate({
			bindto: "#chart",
		    data: {
		        columns: cols,
		        type: 'bar'
		    },
		    axis: {
		        x: {
		            type: 'category',
		            categories: ['Mycket missnöjd', 'Missnöjd', 'Varken eller', 'Nöjd', 'Mycket nöjd']
		        }
		    },
		    legend: {
		        hide: true
		    }
		});
		
	}
	
});