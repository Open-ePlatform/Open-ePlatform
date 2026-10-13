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
		/css/multitreequery.css
	</xsl:variable>

	<xsl:template match="Document">

		<div id="MultiTreeQueryProvider" class="contentitem">

			<xsl:apply-templates select="validationError" />
			<xsl:apply-templates select="UpdateMultiTreeQuery" />

		</div>

	</xsl:template>

	<xsl:template match="UpdateMultiTreeQuery">

		<h1>
			<xsl:value-of select="$i18n.UpdateQuery" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="MultiTreeQuery/QueryDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="updateMultiTreeQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="MultiTreeQuery" />
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
						<xsl:with-param name="selectedValue" select="MultiTreeQuery/providerIdentifier" />
					</xsl:call-template>
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">

				<label for="previewMode">
					<xsl:value-of select="$i18n.Query.previewMode" />
				</label>

				<div class="floatleft full">
				

					<xsl:call-template name="createPreviewModeDropdown"></xsl:call-template>
					
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'onlyAllowSelectingLeafs'" />
					<xsl:with-param name="name" select="'onlyAllowSelectingLeafs'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="MultiTreeQuery" />
				</xsl:call-template>
				
				<label for="onlyAllowSelectingLeafs">
					<xsl:value-of select="$i18n.OnlyAllowSelectingLeafs" />
				</label>
			</div>
			
			<div class="floatleft full bigmargintop">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="MultiTreeQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="setAsAttribute">
						<xsl:value-of select="$i18n.Query.setAsAttribute" />
					</label>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="attributeName" class="floatleft clearboth"><xsl:value-of select="$i18n.Query.attributeName" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attributeName'"/>
						<xsl:with-param name="name" select="'attributeName'"/>
						<xsl:with-param name="title" select="$i18n.Query.attributeName"/>
						<xsl:with-param name="element" select="MultiTreeQuery" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
			  </div>
			</div>
			
			<p class="floatleft clearboth"><xsl:value-of select="$i18n.attributeDescription" /></p>
			
			<table class="floatleft clearboth border">
				<thead>
					<th><xsl:value-of select="$i18n.attributes.name" /></th>
					<th><xsl:value-of select="$i18n.attributes.value" /></th>
				</thead>
				<tbody>
					<tr>
						<td>prefix.ID</td>
						<td><xsl:value-of select="$i18n.attribute.ID" /></td>
					</tr>
					<tr>
						<td>prefix.Name</td>
						<td><xsl:value-of select="$i18n.attribute.Name" /></td>
					</tr>
				</tbody>
			</table>

			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>
	
	<xsl:template name="createPreviewModeDropdown">

		<select id="previewMode" name="previewMode">
			
			<xsl:call-template name="createPreviewModeDropdownOption">
				<xsl:with-param name="label" select="$i18n.Query.previewMode.Tree" />
				<xsl:with-param name="value" select="'TREE'" />
			</xsl:call-template>
			
			<xsl:call-template name="createPreviewModeDropdownOption">
				<xsl:with-param name="label" select="$i18n.Query.previewMode.List" />
				<xsl:with-param name="value" select="'LIST'" />
			</xsl:call-template>
			
		</select>
		
	</xsl:template>	

	<xsl:template name="createPreviewModeDropdownOption">
		<xsl:param name="label" />
		<xsl:param name="value" />
			
		<xsl:variable name="paramValue" select="requestparameters/parameter[name='previewMode']/value" />
		<xsl:variable name="selectedValue" select="MultiTreeQuery/previewMode" />
	
		<option value="{$value}">
			<xsl:choose>
				<xsl:when test="requestparameters">
					<xsl:if test="$paramValue = $value">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="$selectedValue = $value">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>				
				
			<xsl:value-of select="$label" />							
		</option>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedMultiTreeQueryNotFound']">

		<p class="error">
			<xsl:value-of select="$i18n.MultiTreeQueryNotFound" />
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