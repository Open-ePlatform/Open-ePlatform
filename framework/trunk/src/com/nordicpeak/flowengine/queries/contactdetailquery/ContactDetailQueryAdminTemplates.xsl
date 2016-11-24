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
		
		<div id="ContactDetailQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateContactDetailQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateContactDetailQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="ContactDetailQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateContactDetailQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="ContactDetailQuery" />
			</xsl:call-template>
			
			<div class="floatleft clearboth">
			
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.ContactChannelSettings" /></label>
							
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowSMS'" />
					<xsl:with-param name="name" select="'allowSMS'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="allowSMS"><xsl:value-of select="$i18n.AllowSMS" /></label>
			</div>
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'useOfficalAddress'" />
					<xsl:with-param name="name" select="'useOfficalAddress'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="useOfficalAddress"><xsl:value-of select="$i18n.OfficalAddress" /></label>
			</div>			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireAddress'" />
					<xsl:with-param name="name" select="'requireAddress'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="requireAddress"><xsl:value-of select="$i18n.RequireAddress" /></label>
			</div>
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireEmail'" />
					<xsl:with-param name="name" select="'requireEmail'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="requireEmail"><xsl:value-of select="$i18n.RequireEmail" /></label>
			</div>			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requirePhone'" />
					<xsl:with-param name="name" select="'requirePhone'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="requirePhone"><xsl:value-of select="$i18n.RequirePhone" /></label>
			</div>
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireMobilePhone'" />
					<xsl:with-param name="name" select="'requireMobilePhone'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="requireMobilePhone"><xsl:value-of select="$i18n.RequireMobilePhone" /></label>
			</div>						
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'showSocialSecurityNumberField'" />
					<xsl:with-param name="name" select="'showSocialSecurityNumberField'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="showSocialSecurityNumberField"><xsl:value-of select="$i18n.ShowSocialSecurityNumberField" /></label>
			</div>
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'disableProfileUpdate'" />
					<xsl:with-param name="name" select="'disableProfileUpdate'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="disableProfileUpdate"><xsl:value-of select="$i18n.DisableProfileUpdate" /></label>
			</div>					
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedContactDetailQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.ContactDetailQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'maxLength'">
				<xsl:value-of select="$i18n.maxLength" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>