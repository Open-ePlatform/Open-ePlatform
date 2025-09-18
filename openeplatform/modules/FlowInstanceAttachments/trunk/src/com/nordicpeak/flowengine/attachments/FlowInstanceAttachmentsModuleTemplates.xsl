<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/plugins/jquery.qloader.js
		/js/confirmpost.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/attachments.js
		/js/attachmentssettings.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/attachments.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<xsl:apply-templates select="TabContents" />
		<xsl:apply-templates select="ShowSettings"/>
		<xsl:apply-templates select="ShowUpdateSettings"/>
		<xsl:apply-templates select="ValidationErrors" />
	
	</xsl:template>
	
	<xsl:template match="TabContents">
	
		<div id="attachments-container" data-flowinstanceid="{FlowInstance/flowInstanceID}">
	
			<div id="attachments">
			
				<xsl:if test="Manager or AllowUserUpload">
					<div id="new-attachment" class="tabs-content" style="display: none">
					
						<script type="text/javascript">
							AttachmentsModule = {
								"URL": "<xsl:value-of select="ModuleURL"/>",
								"TabTitle": "<xsl:value-of select="TabTitle"/>",
								"PicPending": "<xsl:value-of select="$imagePath"/>/ajax-loader.gif",
							};
						</script>
			 						
						<div class="heading-wrapper">
							<h2><xsl:value-of select="$i18n.NewAttachment" /></h2>
							<a href="#" class="btn btn-light btn-right close_attachment"><xsl:value-of select="$i18n.Close" /><i data-icon-after="x"></i></a>
						</div>
						
						<form id="new-attachments-form" action="">
						
							<div class="mask"/>
							<div class="validationerrors"/>
							
							<div class="heading-wrapper">
								<label class="required" ><xsl:value-of select="$i18n.AttachFiles" /></label>
							</div>
							
							<script>
								imagePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
								deleteFile = '<xsl:value-of select="$i18n.DeleteFile" />';
							</script>
							
							<xsl:apply-templates select="validationError[messageKey = 'FileSizeLimitExceeded' or messageKey = 'UnableToParseRequest']" />
							
							<div class="full">
								
								<div class="upload clearboth">
									<span class="btn btn-upload btn-blue">
										<xsl:value-of select="$i18n.ChooseFiles" />
										<input id="external-attachment" type="file" name="attachments" aria-label="{$i18n.ChooseFiles}" aria-describedby="fileUploadInfo" multiple="multiple" size="55" class="qloader bigmarginbottom" />
									</span>
									<span id="fileUploadInfo"><xsl:value-of select="$i18n.MaximumFileSize" />: <xsl:value-of select="FormatedMaxFileSize" /></span>
								</div>
								
								<ul id="external-attachment-qloader-filelist" class="files" />
								
							</div>
					
							<a href="#" class="btn btn-green btn-inline submit" onclick="$(this).closest('form').submit(); return false;">
								<xsl:value-of select="$i18n.SubmitAttachment" />
								<img class="pending marginleft" src="{$imagePath}/ajax-loader.gif" />
							</a>
							
							<a href="#" class="btn btn-light btn-inline close_attachment"><xsl:value-of select="$i18n.Cancel" /></a>
							
						</form>
						
						<!-- Preload images -->
						<div style="display: none;">
							<img src="{$imagePath}/ajax-loader.gif"/>
						</div>
						
					</div>
				</xsl:if>
			
				<div class="tabs-content">
					<div class="heading-wrapper">
						<h2><xsl:value-of select="$i18n.Attachments" /></h2>
						
						<xsl:if test="Manager or AllowUserUpload">
							<a href="#" class="btn btn-blue btn-right open_attachment"><i data-icon-before="+"></i><xsl:value-of select="$i18n.NewAttachment" /></a>
						</xsl:if>
					</div>
					
					<p>
						<xsl:choose>
							<xsl:when test="AllowUserUpload"><xsl:value-of select="$i18n.Description.AllowUserUpload" /></xsl:when>
							<xsl:otherwise><xsl:value-of select="$i18n.Description" /></xsl:otherwise>
						</xsl:choose>
					</p>
					
					<xsl:choose>
						<xsl:when test="Attachments/Attachment">
							<ul class="attachments messages">
								<xsl:apply-templates select="Attachments/Attachment" />
							</ul>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.NoAttachments" />
						</xsl:otherwise>	  					
					</xsl:choose>
					
				</div>
				
				<script type="text/javascript">
					initAttachments();
				</script>
			</div>
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="Attachment">
		
		<li class="official">
			
			<div class="attachment message">
			
				<a href="{../../ModuleURL}?action=download&amp;attachmentID={attachmentID}" target="blank">
					<i data-icon-before="d"/>
					<xsl:value-of select="filename" />
					<xsl:text>&#160;</xsl:text>
					<span class="size">(<xsl:value-of select="FormatedSize" />)</span>
				</a>
			
				<span class="author">
				
					<i data-icon-before="m"/>
					
					<xsl:call-template name="printUser">
						<xsl:with-param name="user" select="poster" />
					</xsl:call-template>
					
				 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="added" /></span>
				 	
				 	<xsl:if test="../../Manager">
				 	
				 		<span class="time"><xsl:text>&#160;·&#160;</xsl:text></span>
				 	
					 	<a class="delete" href="{../../ModuleURL}?action=delete&amp;attachmentID={attachmentID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteFile.confirm}: {filename}">
							<span data-icon-before="x">
								<xsl:value-of select="$i18n.DeleteFile" />
							</span>
						</a>
						
					</xsl:if>
					
				</span>
				
			</div>
		</li>
	
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
		
		<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/showupdatesettings" class="floatright">
			<xsl:value-of select="$i18n.UpdateSettings" />
			<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/pen.png" alt="" />
		</a>
		
		<xsl:if test="//FlowInstanceAttachmentsSettings/flowFamilyID">
			<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/deletesettings" class="floatright clearboth" onclick="return confirmPost(this.href, '{$i18n.DeleteSettings.Confirm}');">
				<xsl:value-of select="$i18n.DeleteSettings" />
				<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/delete.png" alt="" />
			</a>		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="ShowUpdateSettings">
		
		<xsl:variable name="moduleEnabled" select="//FlowInstanceAttachmentsSettings/moduleEnabled"/>
		<xsl:variable name="allowUserUpload" select="//FlowInstanceAttachmentsSettings/allowUserUpload"/>
		<xsl:variable name="userEmailEnabled" select="//FlowInstanceAttachmentsSettings/userEmailEnabled"/>
		<xsl:variable name="userSmsEnabled" select="//FlowInstanceAttachmentsSettings/userSmsEnabled"/>
		<xsl:variable name="managerEmailEnabled" select="//FlowInstanceAttachmentsSettings/managerEmailEnabled"/>
		
		<div id="Settings" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.adminExtensionViewTitle"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="Flow/name"/>
			</h1>
	
			<form method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/updatesettings">
				
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
						
						<label for="moduleEnabled">
							<xsl:value-of select="$i18n.Settings.Active.description" />
						</label>
					</div>
	
				</div>
				
				<div id="settings-wrapper">
				
					<div class="floatleft full bigmarginbottom">
	
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'allowUserUpload'" />
								<xsl:with-param name="name" select="'allowUserUpload'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="$allowUserUpload" />
								<xsl:with-param name="element" select="Settings" />
							</xsl:call-template>
							
							<label for="allowUserUpload">
								<xsl:value-of select="$i18n.Settings.AllowUserUpload" />
							</label>
						</div>
		
					</div>
					
					<fieldset id="notificationFieldset">
						<legend> <xsl:value-of select="$i18n.Settings.NotificationTitle" /></legend>
						
						<div class="floatleft full bigmarginbottom">
		
							<div class="floatleft full">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="'userEmailEnabled'" />
									<xsl:with-param name="name" select="'userEmailEnabled'" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="checked" select="$userEmailEnabled" />
									<xsl:with-param name="element" select="Settings" />
								</xsl:call-template>
								
								<label for="userEmailEnabled">
									<xsl:value-of select="$i18n.Settings.sendEmail" />
								</label>
							</div>
			
						</div>
						
						<div class="floatleft full bigmarginbottom">
		
							<div class="floatleft full">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="'userSmsEnabled'" />
									<xsl:with-param name="name" select="'userSmsEnabled'" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="checked" select="$userSmsEnabled" />
									<xsl:with-param name="element" select="Settings" />
								</xsl:call-template>
								
								<label for="userSmsEnabled">
									<xsl:value-of select="$i18n.Settings.sendSms" />
								</label>
							</div>
			
						</div>
						
						<div class="floatleft full bigmarginbottom">
		
							<div class="floatleft full">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="'managerEmailEnabled'" />
									<xsl:with-param name="name" select="'managerEmailEnabled'" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="checked" select="$managerEmailEnabled" />
									<xsl:with-param name="element" select="Settings" />
								</xsl:call-template>
								
								<label for="managerEmailEnabled">
									<xsl:value-of select="$i18n.Settings.sendManagerEmail" />
								</label>
							</div>
			
						</div>
						
					</fieldset>
				
				</div>
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template match="ValidationErrors">
		
		<div class="bigmarginbottom">
			<xsl:apply-templates select="validationError" />
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToParseRequest']">
		
		<p class="error">
			<xsl:value-of select="$i18n.UnableToParseRequest"/>
		</p>
		
	</xsl:template>
	
	
	
	<xsl:template match="validationError[messageKey='FileSizeLimitExceeded']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part1"/>
			<xsl:value-of select="filename"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part2"/>
			<xsl:value-of select="size"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part3"/>
			<xsl:value-of select="maxFileSize"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part4"/>
		</p>
			
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<span data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.validationError.RequiredField" />
						</xsl:when>
						<xsl:when test="validationErrorType='InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.validationError.TooShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong" />
						</xsl:when>
						<xsl:when test="validationErrorType='Other'">
							<xsl:value-of select="$i18n.validationError.Other" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.validationError.unknownValidationErrorType" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>&#x20;</xsl:text>
					<xsl:choose>
						<xsl:when test="fieldName = 'attachment'">
							<xsl:value-of select="$i18n.Attachment" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="fieldName" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>!</xsl:text>
				</span>
			</p>
		</xsl:if>
		<xsl:if test="messageKey">
			<p class="error">
				<span data-icon-before="!">
					<xsl:value-of select="$i18n.validationError.unknownMessageKey" />!
				</span>
			</p>
		</xsl:if>
		<xsl:apply-templates select="attachment" />
		
	</xsl:template>
	
</xsl:stylesheet>