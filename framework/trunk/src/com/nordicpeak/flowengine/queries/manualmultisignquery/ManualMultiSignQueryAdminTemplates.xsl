<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

		<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>	

	<xsl:template match="Document">	
		
		<div id="ManualMultiSignQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateManualMultiSignQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateManualMultiSignQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="ManualMultiSignQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateManualMultiSignQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="ManualMultiSignQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideCitizenIdetifierInPDF'" />
						<xsl:with-param name="name" select="'hideCitizenIdetifierInPDF'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
					</xsl:call-template>
						
					<label for="hideCitizenIdetifierInPDF">
						<xsl:value-of select="$i18n.hideCitizenIdetifierInPDF" />
					</label>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setMultipartsAsOwners'" />
						<xsl:with-param name="name" select="'setMultipartsAsOwners'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
					</xsl:call-template>
						
					<label for="setMultipartsAsOwners">
						<xsl:value-of select="$i18n.SetMultipartsAsOwners" />
					</label>
			    </div>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedManualMultiSignQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.ManualMultiSignQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:value-of select="$fieldName" />
		
	</xsl:template>

</xsl:stylesheet>