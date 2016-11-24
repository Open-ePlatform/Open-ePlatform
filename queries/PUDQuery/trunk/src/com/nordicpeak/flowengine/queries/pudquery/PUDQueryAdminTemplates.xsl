<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>	

	<xsl:template match="Document">	
		
		<div id="PUDQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdatePUDQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdatePUDQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="PUDQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updatePUDQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="PUDQuery" />
			</xsl:call-template>
			
			<div class="floatleft clearboth marginbottom">
			
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.AllowedSearchServices" /></label>
				
				<div class="floatleft clearboth"><xsl:value-of select="$i18n.AllowedSearchServicesDescription" />.</div>
			
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowedSearchService_PUD'" />
					<xsl:with-param name="name" select="'allowedSearchService'" />
					<xsl:with-param name="value" select="'PUD'" />
					<xsl:with-param name="element" select="PUDQuery/AllowedSearchServices" />
				</xsl:call-template>
				<label for="allowedSearchService_PUD"><xsl:value-of select="$i18n.PUD" /></label>
			</div>
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowedSearchService_Address'" />
					<xsl:with-param name="name" select="'allowedSearchService'" />
					<xsl:with-param name="value" select="'ADDRESS'" />
					<xsl:with-param name="element" select="PUDQuery/AllowedSearchServices" />
				</xsl:call-template>
				<label for="allowedSearchService_Address"><xsl:value-of select="$i18n.Address" /></label>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedPUDQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.PUDQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'allowedSearchService'">
				<xsl:value-of select="$i18n.NoSearchService" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>