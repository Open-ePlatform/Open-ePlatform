<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>	

	<xsl:variable name="scripts">
		/js/d3.v3.min.js
		/js/c3.min.js
		/js/feedbacksurvey.js?v=2
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/c3.min.css
		/css/feedbacksurvey.css?v=2
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:choose>
			<xsl:when test="validationError">
				<xsl:apply-templates select="validationError" />
			</xsl:when>
			<xsl:otherwise>
				
				<div id="FeedbackFlowSubmitSurvey">
					<xsl:apply-templates select="FeedbackSurveySuccess"/>
					<xsl:apply-templates select="FeedbackSurveyForm"/>
					<xsl:apply-templates select="ShowFlowFeedbackSurveys"/>
				</div>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="FeedbackSurveySuccess">
		
		<h1 class="title-border"><xsl:value-of select="$i18n.FeedbackSurveySuccess" />!</h1>
		
	</xsl:template>
		
	<xsl:template match="FeedbackSurveyForm">
	
		<h1 class="title-border"><xsl:value-of select="$i18n.FeedbackSurveyTitle" /></h1>
		
		<form id="feedbackForm" name="feedbackForm" method="post" action="{ModuleURI}">
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name" select="'flowInstanceID'" />
				<xsl:with-param name="value" select="flowInstanceID" />
			</xsl:call-template>
			
			<div class="alternative">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'very_satisfied'"/>
					<xsl:with-param name="name" select="'answer'"/>
					<xsl:with-param name="title" select="$i18n.VerySatisfied"/>
					<xsl:with-param name="value" select="'VERY_SATISFIED'" />
				</xsl:call-template>
				<label for="very_satisfied" class="radio"><xsl:value-of select="$i18n.VerySatisfied" /></label>
			</div>
			
			<div class="alternative">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'satisfied'"/>
					<xsl:with-param name="name" select="'answer'"/>
					<xsl:with-param name="title" select="$i18n.Satisfied"/>
					<xsl:with-param name="value" select="'SATISFIED'" />
				</xsl:call-template>
				<label for="satisfied" class="radio"><xsl:value-of select="$i18n.Satisfied" /></label>
			</div>
			
			<div class="alternative">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'neither'"/>
					<xsl:with-param name="name" select="'answer'"/>
					<xsl:with-param name="title" select="$i18n.Neither"/>
					<xsl:with-param name="value" select="'NEITHER'" />
				</xsl:call-template>
				<label for="neither" class="radio"><xsl:value-of select="$i18n.Neither" /></label>
			</div>
			
			<div class="alternative">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'dissatisfied'"/>
					<xsl:with-param name="name" select="'answer'"/>
					<xsl:with-param name="title" select="$i18n.Dissatisfied"/>
					<xsl:with-param name="value" select="'DISSATISFIED'" />
				</xsl:call-template>
				<label for="dissatisfied" class="radio"><xsl:value-of select="$i18n.Dissatisfied" /></label>
			</div>
			
			<div class="alternative">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'very_dissatisfied'"/>
					<xsl:with-param name="name" select="'answer'"/>
					<xsl:with-param name="title" select="$i18n.VeryDissatisfied"/>
					<xsl:with-param name="value" select="'VERY_DISSATISFIED'"/>
				</xsl:call-template>
				<label for="very_dissatisfied" class="radio"><xsl:value-of select="$i18n.VeryDissatisfied" /></label>
			</div>
			
			<div class="comment-wrapper">
			
				<xsl:if test="ShowCommentField = 'true'">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'feedback_comment'"/>
						<xsl:with-param name="name" select="'comment'"/>
						<xsl:with-param name="title" select="$i18n.Comment"/>
						<xsl:with-param name="class" select="'hidden bigmarginbottom'"/>
						<xsl:with-param name="rows" select="'3'"/>
						<xsl:with-param name="placeholder" select="$i18n.CommentPlaceHolder"/>
					</xsl:call-template>					
				</xsl:if>
				
				<div class="validationerrors floatleft" />
			
				<xsl:if test="ShowCommentField = 'true'">
					<a href="#" class="comment-btn bigmarginright"><xsl:value-of select="$i18n.LeaveComment" /></a>
				</xsl:if>
			
				<input type="button" name="sendButton" class="submit-btn btn btn-green xl" value="{$i18n.Send}" />
			
			</div>
			
		</form>
		
	</xsl:template>

	<xsl:template match="ShowFlowFeedbackSurveys">
		
		<p class="nomargin"><strong><xsl:value-of select="$i18n.FeedbackSurveyTitle" /></strong></p>
		
		<xsl:choose>
			<xsl:when test="ChartData">
				
				<script type="text/javascript">
					chartData = <xsl:value-of select="ChartData" />;
				</script>
				
				<div class="chart">
					<div id="chart"></div>
				</div>
				
				<xsl:if test="Comments/FeedbackSurvey">
				
					<a href="#" class="show-comments-trigger clearboth floatright"><xsl:value-of select="$i18n.ShowComments" /></a>
					<a href="#" class="hide-comments-trigger clearboth floatright hidden"><xsl:value-of select="$i18n.HideComments" /></a>
					
					<table class="full coloredtable sortabletable oep-table hidden">
						<thead>
							<tr>
								<th width="10" />
								<th width="200"><xsl:value-of select="$i18n.Answer" /></th>
								<th><xsl:value-of select="$i18n.Comment" /></th>
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="Comments/FeedbackSurvey" mode="list" />
						</tbody>					
					</table>
					
				</xsl:if>
				
			</xsl:when>
			<xsl:otherwise>
				<p><xsl:value-of select="$i18n.NoFlowFeedbackSurveys" /></p>
			</xsl:otherwise>
		</xsl:choose>
		
		
	</xsl:template>

	<xsl:template match="FeedbackSurvey" mode="list">
		
		<tr>
			<td />
			<td>
				<xsl:choose>
					<xsl:when test="answer = 'VERY_DISSATISFIED'"><xsl:value-of select="$i18n.VeryDissatisfied" /></xsl:when>
					<xsl:when test="answer = 'DISSATISFIED'"><xsl:value-of select="$i18n.Dissatisfied" /></xsl:when>
					<xsl:when test="answer = 'NEITHER'"><xsl:value-of select="$i18n.Neither" /></xsl:when>
					<xsl:when test="answer = 'SATISFIED'"><xsl:value-of select="$i18n.Satisfied" /></xsl:when>
					<xsl:when test="answer = 'VERY_SATISFIED'"><xsl:value-of select="$i18n.VerySatisfied" /></xsl:when>
					<xsl:otherwise><xsl:value-of select="$i18n.Unkown" /></xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="comment"/>
				</xsl:call-template>
			</td>
		</tr>

	</xsl:template>

	<xsl:template match="validationError[fieldName = 'answer']">
		
		<p class="error"><xsl:value-of select="$i18n.NoAnswer" /></p>
		
	</xsl:template>

	<xsl:template match="validationError">
		
		<xsl:if test="fieldName and validationErrorType">
			
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validationError.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.validationError.TooLong" />
					</xsl:when>
					<xsl:when test="validationErrorType='Other'">
						<xsl:value-of select="$i18n.validationError.Other" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#x20;</xsl:text>
				<xsl:choose>
					<xsl:when test="fieldName = 'comment'">
						<xsl:value-of select="$i18n.Comment" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName" />
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>!</xsl:text>
				
			</p>

		</xsl:if>

	</xsl:template>

</xsl:stylesheet>