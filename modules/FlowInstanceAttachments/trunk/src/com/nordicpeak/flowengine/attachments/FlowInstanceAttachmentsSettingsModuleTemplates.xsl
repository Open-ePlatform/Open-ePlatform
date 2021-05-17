<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/plugins/jquery.qloader.js
		/js/confirmpost.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/attachmentssettings.js
	</xsl:variable>
	
	<xsl:template match="Document">
	
		<xsl:apply-templates select="ShowSettings"/>
		<xsl:apply-templates select="ShowUpdateSettings"/>

	</xsl:template>
	
	
	
	<xsl:template match="ShowSettings">
	
	<xsl:variable name="extensionImgPath"><xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="extensionRequestURL" />/static/pics</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="//FlowInstanceAttachmentsSettings/flowFamilyID">
				<xsl:value-of select="$i18n.Enabled" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.NotEnabled" />
			</xsl:otherwise>
		</xsl:choose>
		
		<a href="{/Document/ModuleURI}/showupdatesettings/{Flow/flowID}" class="floatright">
			<xsl:value-of select="$i18n.UpdateSettings" />
			<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/pen.png" alt="" />
		</a>
		
		<xsl:if test="//FlowInstanceAttachmentsSettings/flowFamilyID">
			<a href="{/Document/ModuleURI}/deletesettings/{Flow/flowID}" class="floatright clearboth" onclick="return confirmPost(this.href, '{$i18n.DeleteSettings.Confirm}');">
				<xsl:value-of select="$i18n.DeleteSettings" />
				<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/delete.png" alt="" />
			</a>		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="ShowUpdateSettings">
		
		<xsl:variable name="moduleEnabled" select="//FlowInstanceAttachmentsSettings/moduleEnabled"/>
		<xsl:variable name="emailEnabled" select="//FlowInstanceAttachmentsSettings/emailEnabled"/>
		<xsl:variable name="smsEnabled" select="//FlowInstanceAttachmentsSettings/smsEnabled"/>
		
		<div id="Settings" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.adminExtensionViewTitle"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="Flow/name"/>
			</h1>
	
			<!-- <xsl:apply-templates select="ValidationErrors/validationError"/> -->
	
			<p>
				<xsl:value-of select="$i18n.UpdateSettings.description" />
			</p>
	
			<form method="post" action="{/Document/ModuleURI}/updatesettings/{Flow/flowID}">
				
				<fieldset>
					<legend> <xsl:value-of select="$i18n.Settings.Active" /></legend>
					
					<div class="floatleft full bigmarginbottom">
	
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'moduleEnabled'" />
								<xsl:with-param name="name" select="'moduleEnabled'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="$moduleEnabled" />
								<xsl:with-param name="element" select="Settings" />
								<xsl:with-param name="onclick" select="'notificationHandler()'" />
							</xsl:call-template>
							
							<label for="GetModuleEnabled">
								<xsl:value-of select="$i18n.Settings.Active.description" />
							</label>
						</div>
		
					</div>
				</fieldset>
				
				<fieldset id="notificationFieldset">
					<legend> <xsl:value-of select="$i18n.Settings.NotificationTitle" /></legend>
					
					<div class="floatleft full bigmarginbottom">
	
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'emailEnabled'" />
								<xsl:with-param name="name" select="'emailEnabled'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="$emailEnabled" />
								<xsl:with-param name="element" select="Settings" />
							</xsl:call-template>
							
							<label for="GetEmailDisabled">
								<xsl:value-of select="$i18n.Settings.sendEmail" />
							</label>
						</div>
		
					</div>
					
					<div class="floatleft full bigmarginbottom">
	
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'smsEnabled'" />
								<xsl:with-param name="name" select="'smsEnabled'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="$smsEnabled" />
								<xsl:with-param name="element" select="Settings" />
							</xsl:call-template>
							
							<label for="GetSmsDisabled">
								<xsl:value-of select="$i18n.Settings.sendSms" />
							</label>
						</div>
		
					</div>
					
				</fieldset>
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
</xsl:stylesheet>