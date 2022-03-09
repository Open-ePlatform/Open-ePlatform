var chartData = null;

var i18nFeedbackFlowSubmitSurveyModule = {
	"VERYSATISFIED": "Very satisfied",
	"SATISFIED": "Satisfied",
	"DISSATISFIED": "Dissatisfied",
	"VERYDISSATISFIED": "Very dissatisfied",
	"NEITHER": "Neither"
};

$(function() {
	
	$("#FeedbackFlowSubmitSurvey a.comment-btn").on("click", function(e) {
		
		e.preventDefault();
		
		$(this).parent().find("textarea").slideDown("fast");
		$(this).remove();
		
	});
	
	$("#sendEmail").change(function() {
      if($(this).is(":checked")) {
        $("#notification-email").show();
      }
      else if($(this).is(":not(:checked)")) {
        $("#notification-email").hide();
      }
    });
	
	if($("#sendEmail").is(":checked")) {
            $("#notification-email").show();
          }
	
	$("#FeedbackFlowSubmitSurvey .submit-btn").on("click", function(e) {
		
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
	
	$("#FeedbackFlowSubmitSurvey a.show-comments-trigger").on("click", function(e) {
		
		e.preventDefault();
		
		var $this = $(this);
		
		$this.parent().find("table").slideDown("fast", function() {
			
			$this.hide();
			$("#FeedbackFlowSubmitSurvey a.hide-comments-trigger").show();
			
		});
		
	});
	
	$("#FeedbackFlowSubmitSurvey a.hide-comments-trigger").on("click", function(e) {
		
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
		            categories: [i18nFeedbackFlowSubmitSurveyModule.VERYDISSATISFIED, i18nFeedbackFlowSubmitSurveyModule.DISSATISFIED, i18nFeedbackFlowSubmitSurveyModule.NEITHER, i18nFeedbackFlowSubmitSurveyModule.SATISFIED, i18nFeedbackFlowSubmitSurveyModule.VERYSATISFIED]
		        }
		    },
		    legend: {
		        hide: true
		    }
		});
		
	}
	
});