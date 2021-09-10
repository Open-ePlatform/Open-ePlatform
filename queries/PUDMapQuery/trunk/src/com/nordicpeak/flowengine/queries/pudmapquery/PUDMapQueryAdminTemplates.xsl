<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-ui.js?v=1
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="PUDMapQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdatePUDMapQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdatePUDMapQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="PUDMapQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updatePUDMapQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="PUDMapQuery" />
			</xsl:call-template>
			
			<xsl:call-template name="createMapQueryCommonFieldsForm">
				<xsl:with-param name="element" select="PUDMapQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<label for="minimumScale" class="floatleft clearboth"><xsl:value-of select="$i18n.MinimumScale" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createScaleDropDown">
						<xsl:with-param name="id" select="'minimumScale'" />
						<xsl:with-param name="name" select="'minimumScale'" />
						<xsl:with-param name="selectedValue" select="PUDMapQuery/minimumScale" />
					</xsl:call-template>
				</div>
			</div>
			
			<h2 class="floatleft full bigmargintop"><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="PUDMapQuery" /> 
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
						<xsl:with-param name="element" select="PUDMapQuery" />
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
						<td>prefix.propertyObjectIdentity</td>
						<td><xsl:value-of select="$i18n.attributes.propertyObjectIdentity" /></td>
					</tr>
					<tr>
						<td>prefix.propertyUnitDesignation</td>
						<td><xsl:value-of select="$i18n.attributes.propertyUnitDesignation" /></td>
					</tr>
					<tr>
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

	<xsl:template match="validationError[messageKey = 'UpdateFailedPUDMapQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.PUDMapQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'minimumScale'">
				<xsl:value-of select="$i18n.minimumScale" />
			</xsl:when>
			<xsl:when test="$fieldName = 'attributeName'">
				<xsl:value-of select="$i18n.attributeName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>