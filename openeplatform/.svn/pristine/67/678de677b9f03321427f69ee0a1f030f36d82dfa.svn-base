<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="SinglePolygonMapQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateSinglePolygonMapQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateSinglePolygonMapQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="SinglePolygonMapQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateSinglePolygonMapQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="SinglePolygonMapQuery" />
			</xsl:call-template>
			
			<xsl:call-template name="createMapQueryCommonFieldsForm">
				<xsl:with-param name="element" select="SinglePolygonMapQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<label for="minimumScale" class="floatleft clearboth"><xsl:value-of select="$i18n.MinimumScale" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createScaleDropDown">
						<xsl:with-param name="id" select="'minimumScale'" />
						<xsl:with-param name="name" select="'minimumScale'" />
						<xsl:with-param name="selectedValue" select="SinglePolygonMapQuery/minimumScale" />
					</xsl:call-template>
			    </div>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedSinglePolygonMapQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.SinglePolygonMapQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'minimumScale'">
				<xsl:value-of select="$i18n.minimumScale" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>