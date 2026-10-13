<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common" exclude-result-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="commonImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/common/pics</xsl:variable>

	<xsl:template name="createCommonFieldsForm">
	
		<xsl:param name="element" />
	
		<div class="panel-wrapper follow" style="display: none">
	 		<div class="inner">
	 			<div class="title"></div>
	 			<div class="buttons">
	 				<a href="#" data-action="save_errand" class="btn btn-green" onclick="event.preventDefault(); $(this).closest('form').trigger('submit')"><xsl:value-of select="$i18n.SaveChanges" /></a>
	 			</div>
	 		</div>
	 	</div>
	 	
	 	<script type="text/javascript">
	 		
	 		$(window).on('scroll touchmove', function() {
		
		        if ($(window).scrollTop() > $('header').height()) {
		            $('.follow').attr('style', 'display: block !important');
		            return;
		        }
		
		        $('.follow').attr('style', 'display: none !important');
		
		    });
			
			var $savePanel = $('.panel-wrapper.follow');
			
			$savePanel.find('.title').text($savePanel.closest('.contentitem').find('> h1:first').text());
	 		
	 	</script>
	
		<div class="floatleft full bigmarginbottom">

			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.Name" />
			</label>

			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'" />
					<xsl:with-param name="name" select="'name'" />
					<xsl:with-param name="element" select="$element/EvaluatorDescriptor" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
		
			<label for="comment" class="floatleft full">
				<xsl:value-of select="$i18n.Comment" /><xsl:text> (</xsl:text><xsl:value-of select="$i18n.commentVisibility" /><xsl:text>)</xsl:text>
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'comment'"/>
					<xsl:with-param name="name" select="'comment'"/>
					<xsl:with-param name="element" select="$element/EvaluatorDescriptor" />
					<xsl:with-param name="rows" select="1" /> 
				</xsl:call-template>
			</div>
		</div>
	
		<div class="floatleft full marginbottom">
	
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'enabled'" />
					<xsl:with-param name="name" select="'enabled'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="$element/EvaluatorDescriptor" />
				</xsl:call-template>

				<label for="enabled">
					<xsl:value-of select="$i18n.enabled" />
				</label>
			</div>
		</div>
	
	</xsl:template>

	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<p class="error">
			<xsl:value-of select="$i18n.TooLongFieldContent"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="fieldName" mode="common" />!
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<p class="error">
			<xsl:value-of select="$i18n.InvalidFormat"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="fieldName" mode="common" />!
		</p>
		
	</xsl:template>

	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<p class="error">
			<xsl:value-of select="$i18n.RequiredField"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="fieldName" mode="common" />!
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName" mode="common">
	
		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'name'">
				<xsl:value-of select="$i18n.name" />
			</xsl:when>
			<xsl:when test="$fieldName = 'comment'">
				<xsl:value-of select="$i18n.comment"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
</xsl:stylesheet>