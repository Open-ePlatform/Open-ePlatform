<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:template match="Document">

		<div id="StopQueryProvider" class="contentitem">

			<xsl:apply-templates select="validationError" />
			<xsl:apply-templates select="UpdateStopQuery" />

		</div>

	</xsl:template>

	<xsl:template match="UpdateStopQuery">

		<h1>
			<xsl:value-of select="$i18n.UpdateQuery" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="StopQuery/QueryDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="updateStopQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="StopQuery" />
			</xsl:call-template>

			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'displayValidationError'" />
						<xsl:with-param name="name" select="'displayValidationError'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="StopQuery" />
					</xsl:call-template>
					
					<label for="displayValidationError">
						<xsl:value-of select="$i18n.DisplayValidationError" />
					</label>
				</div>

			</div>			

			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedStopQueryNotFound']">

		<p class="error">
			<xsl:value-of select="$i18n.StopQueryNotFound" />
		</p>

	</xsl:template>
	
	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />

		<xsl:choose>
			<xsl:when test="$fieldName = ''">
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

</xsl:stylesheet>