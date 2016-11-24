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
	
	<xsl:variable name="links">
		/css/treequery.css
	</xsl:variable>

	<xsl:template match="Document">

		<div id="TreeQueryProvider" class="contentitem">

			<xsl:apply-templates select="validationError" />
			<xsl:apply-templates select="UpdateTreeQuery" />

		</div>

	</xsl:template>

	<xsl:template match="UpdateTreeQuery">

		<h1>
			<xsl:value-of select="$i18n.UpdateQuery" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="TreeQuery/QueryDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="updateTreeQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="TreeQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">

				<label for="providerIdentifier">
					<xsl:value-of select="$i18n.ProviderIdentifier" />
				</label>

				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'providerIdentifier'" />
						<xsl:with-param name="name" select="'providerIdentifier'" />
						<xsl:with-param name="labelElementName" select="'Name'" />
						<xsl:with-param name="valueElementName" select="'ID'" />
						<xsl:with-param name="element" select="Providers/Provider" />
						<xsl:with-param name="selectedValue" select="TreeQuery/providerIdentifier" />
					</xsl:call-template>
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'onlyAllowSelectingLeafs'" />
					<xsl:with-param name="name" select="'onlyAllowSelectingLeafs'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="TreeQuery" />
				</xsl:call-template>
				
				<label for="onlyAllowSelectingLeafs">
					<xsl:value-of select="$i18n.OnlyAllowSelectingLeafs" />
				</label>
			</div>

			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedTreeQueryNotFound']">

		<p class="error">
			<xsl:value-of select="$i18n.TreeQueryNotFound" />
		</p>

	</xsl:template>
	
	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />

		<xsl:choose>
			<xsl:when test="$fieldName = 'providerIdentifier'">
				<xsl:value-of select="$i18n.ProviderIdentifier" />
			</xsl:when>
			<xsl:when test="$fieldName = 'onlyAllowSelectingLeafs'">
				<xsl:value-of select="$i18n.OnlyAllowSelectingLeafs" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

</xsl:stylesheet>