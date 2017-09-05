<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/stopquery.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', StopQueryInstance/StopQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', StopQueryInstance/StopQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="StopQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
		
			<xsl:if test="StopQueryInstance/StopQuery/displayValidationError = 'true'">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
						<xsl:text>&#160;</xsl:text>
						<div class="marker"></div>
					</div>
				</div>			
			</xsl:if>
	
			<xsl:choose>
				<xsl:when test="StopQueryInstance/StopQuery/displayValidationError = 'true'">
				
				</xsl:when>
				<xsl:otherwise>
				
				</xsl:otherwise>
			</xsl:choose>
	
			<article class="stopquery">
			
				<xsl:attribute name="class">
					<xsl:text>stopquery</xsl:text>
				
					<xsl:if test="StopQueryInstance/StopQuery/displayValidationError = 'true'">
						<xsl:text> error</xsl:text>
					</xsl:if>				
				
				</xsl:attribute>

			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:value-of select="StopQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>					
					
					<xsl:if test="StopQueryInstance/StopQuery/helpText">		
						<xsl:apply-templates select="StopQueryInstance/StopQuery/helpText" />
					</xsl:if>
				</div>
				
				<span class="italic">
					<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
					<xsl:value-of select="StopQueryInstance/StopQuery/description" disable-output-escaping="yes" />
				</span>
				
				<div class="clearboth"/>
				
			</article>			
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<i data-icon-after="!" title="{$i18n.InvalidFormat}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>	
		
</xsl:stylesheet>