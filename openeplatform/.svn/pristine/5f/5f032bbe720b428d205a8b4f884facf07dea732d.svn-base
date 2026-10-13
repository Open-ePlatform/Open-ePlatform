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
	
	<xsl:variable name="scripts">
		/js/pudqueryadmin.js
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
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'useAddressAsResult'" />
					<xsl:with-param name="name" select="'useAddressAsResult'" />
					<xsl:with-param name="element" select="PUDQuery" />
				</xsl:call-template>
				<label for="useAddressAsResult"><xsl:value-of select="$i18n.useAddressAsResult" /></label>
			</div>

			<h2 class="floatleft full bigmargintop"><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="PUDQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="setAsAttribute">
						<xsl:value-of select="$i18n.setAsAttribute" />
					</label>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="attributeName" class="floatleft clearboth"><xsl:value-of select="$i18n.attributeName" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attributeName'"/>
						<xsl:with-param name="name" select="'attributeName'"/>
						<xsl:with-param name="title" select="$i18n.attributeName"/>
						<xsl:with-param name="element" select="PUDQuery" />
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
						<td>prefix</td>
						<td><xsl:value-of select="$i18n.attributes.legacy" /></td>
					</tr>
					<tr>
						<td>prefix.address</td>
						<td><xsl:value-of select="$i18n.attributes.address" /></td>
					</tr>
					<tr>
						<td>prefix.propertyObjectIdentity</td>
						<td><xsl:value-of select="$i18n.attributes.propertyObjectIdentity" /></td>
					</tr>
					<tr>
						<td>prefix.propertyUnitNumber</td>
						<td><xsl:value-of select="$i18n.attributes.propertyUnitNumber" /></td>
					</tr>
					<tr class="notforaddress">
						<td>prefix.propertyUnitDesignation</td>
						<td><xsl:value-of select="$i18n.attributes.propertyUnitDesignation" /></td>
					</tr>
					<tr class="notforaddress">
						<td>prefix.propertyUnitDesignationNoMunicipality</td>
						<td><xsl:value-of select="$i18n.attributes.propertyUnitDesignationNoMunicipality" /></td>
					</tr>
				</tbody>
			</table>
			
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
			<xsl:when test="$fieldName = 'attributeName'">
				<xsl:value-of select="$i18n.attributeName" />
			</xsl:when>
			<xsl:when test="$fieldName = 'useAddressAsResult'">
				<xsl:value-of select="$i18n.useAddressAsResult" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>