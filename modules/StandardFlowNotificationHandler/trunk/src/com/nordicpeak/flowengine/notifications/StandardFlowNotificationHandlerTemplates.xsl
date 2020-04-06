<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/notificationhandler.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowSettings"/>
		<xsl:apply-templates select="UpdateSettings"/>
		
	</xsl:template>

	<xsl:template match="ShowSettings">
	
		<xsl:if test="NotificationSettings/HasEnabledUserNotifications">
		
			<span class="bold"><xsl:value-of select="$i18n.UserNotifications"/></span>
		
			<ul class="nomargin">

				<xsl:if test="NotificationSettings/sendStatusChangedUserSMS = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendStatusChangedUserSMS"/>
					</li>
				</xsl:if>

				<xsl:if test="NotificationSettings/sendExternalMessageReceivedUserSMS = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendExternalMessageReceivedUserSMS"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceSubmittedUserSMS = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceSubmittedUserSMS"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceArchivedUserSMS = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceArchivedUserSMS"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendStatusChangedUserEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendStatusChangedUserEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendExternalMessageReceivedUserEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendExternalMessageReceivedUserEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceSubmittedUserEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceSubmittedUserEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceArchivedUserEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceArchivedUserEmail"/>
					</li>
				</xsl:if>

  			</ul>
		
		</xsl:if>
		
		<xsl:if test="NotificationSettings/HasEnabledManagerNotifications">
		
			<xsl:if test="NotificationSettings/HasEnabledUserNotifications">
				<br/>
			</xsl:if>
		
			<span class="bold"><xsl:value-of select="$i18n.ManagerNotifications"/></span>
		
			<ul class="nomargin">

				<xsl:if test="NotificationSettings/sendExternalMessageReceivedManagerEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendExternalMessageReceivedManagerEmail"/>
					</li>
				</xsl:if>

				<xsl:if test="NotificationSettings/sendInternalMessageAddedManagerEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendInternalMessageAddedManagerEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceAssignedManagerEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceAssignedManagerEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendStatusChangedManagerEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendStatusChangedManagerEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceSubmittedManagerEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceSubmittedManagerEmail"/>
					</li>
				</xsl:if>
			
			</ul>
		
		</xsl:if>
		
		<xsl:if test="NotificationSettings/HasEnabledGroupNotifications">
		
			<xsl:if test="NotificationSettings/HasEnabledUserNotifications or NotificationSettings/HasEnabledManagerNotifications">
				<br/>
			</xsl:if>
		
			<span class="bold"><xsl:value-of select="$i18n.GroupNotifications"/></span>
		
			<ul class="nomargin">

				<xsl:if test="NotificationSettings/sendFlowInstanceAssignedGroupEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceAssignedGroupEmail"/>
					</li>
				</xsl:if>

			</ul>
		
		</xsl:if>
		
		<xsl:if test="NotificationSettings/HasEnabledGlobalNotifications">
		
			<xsl:if test="NotificationSettings/HasEnabledUserNotifications or NotificationSettings/HasEnabledManagerNotifications or NotificationSettings/HasEnabledGroupNotifications">
				<br/>
			</xsl:if>
		
			<span class="bold"><xsl:value-of select="$i18n.GlobalNotifications"/></span>
		
			<ul class="nomargin">

				<xsl:if test="NotificationSettings/sendFlowInstanceSubmittedGlobalEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceSubmittedGlobalEmail"/>
					</li>
				</xsl:if>

				<xsl:if test="NotificationSettings/sendFlowInstanceAssignedGlobalEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceAssignedGlobalEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendFlowInstanceArchivedGlobalEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendFlowInstanceArchivedGlobalEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendExternalMessageReceivedGlobalEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendExternalMessageReceivedGlobalEmail"/>
					</li>
				</xsl:if>
				
				<xsl:if test="NotificationSettings/sendManagerExpiredGlobalEmail = 'true'">
					<li>
						<xsl:value-of select="$i18n.SendManagerExpiredGlobalEmail"/>
					</li>
				</xsl:if>
				
			</ul>
		
		</xsl:if>
		
		<xsl:if test="not(NotificationSettings/HasEnabledUserNotifications) and not(NotificationSettings/HasEnabledManagerNotifications) and not(NotificationSettings/HasEnabledGroupNotifications) and not(NotificationSettings/HasEnabledGlobalNotifications)">
		
			<p class="nomargin"><xsl:value-of select="$i18n.NoNotificationsEnabled"/></p>
		
		</xsl:if>
		
	</xsl:template>

	<xsl:template match="UpdateSettings">
	
		<script>
			
			function toggleUserStatus(){
				
				$("#user-status-subject, #user-status-message").toggleClass("hidden");
			}
		
			function toggleUserExternalMessage(){
				
				$("#user-message-subject, #user-message-message").toggleClass("hidden");
			}
		
			function toggleSubmitEmail(){
				
				$("#submit-email-subject, #submit-email-message").toggleClass("hidden");
			}
			
			function checkAttachPDF(){
				
				var $tagRow = $("#submit-email-message").find("table tr.attached-pdf-tag");
				$tagRow.toggle($("#flowInstanceSubmittedUserEmailAttachPDF").is(":checked"));
			}
			
			function toggleSubmitSMS(){
				
				$("#submit-sms").toggleClass("hidden");
			}
			
			function toggleArchivedSMS(){
				
				$("#archived-sms").toggleClass("hidden");
			}
			
			function toggleArchived(){
				
				$("#archived-subject, #archived-message").toggleClass("hidden");
			}
			
			function toggleManagerExternalMessage(){
				
				$("#manager-external-message-subject, #manager-external-message-message").toggleClass("hidden");
			}
			
			function toggleManagerInternalMessage(){
				
				$("#manager-internal-message-subject, #manager-internal-message-message").toggleClass("hidden");
			}
			
			function toggleManagerAssignMessage(){
				
				$("#manager-assign-subject, #manager-assign-message").toggleClass("hidden");
			}
			
			function toggleGroupAssignMessage(){
				
				$("#group-assign-subject, #group-assign-message").toggleClass("hidden");
			}
			
			function toggleGlobalSubmit(){
				
				$("#global-submit-subject, #global-submit-message").toggleClass("hidden");
			}
			
			function toggleGlobalAssigned(){
				
				$("#global-assigned-subject, #global-assigned-message").toggleClass("hidden");
			}
			
			function toggleGlobalArchived(){
				
				$("#global-archived-subject, #global-archived-message").toggleClass("hidden");
			}
			
			function toggleText(element) {
				$(element).parent().parent().parent().next().toggleClass("hidden");
			}
			
			function toggleTexts(element) {
				$(element).parent().parent().parent().next().toggleClass("hidden").next().toggleClass("hidden");
			}
			
		</script>
	
		<br/>
	
		<xsl:apply-templates select="validationException/validationError"/>
		
		<xsl:if test="validationException/validationError">
		
			<br/>
		
		</xsl:if>
		
		<xsl:variable name="errFieldNames" select="validationException/validationError/fieldName" />
	
		<h2><xsl:value-of select="$i18n.UserNotifications"/></h2>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendStatusChangedUserSMS'" />
					<xsl:with-param name="id" select="'sendStatusChangedUserSMS'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendStatusChangedUserSMS">
					<xsl:value-of select="$i18n.SendStatusChangedUserSMS" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendExternalMessageReceivedUserSMS'" />
					<xsl:with-param name="id" select="'sendExternalMessageReceivedUserSMS'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendExternalMessageReceivedUserSMS">
					<xsl:value-of select="$i18n.SendExternalMessageReceivedUserSMS" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceSubmittedUserSMS'" />
					<xsl:with-param name="id" select="'sendFlowInstanceSubmittedUserSMS'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceSubmittedUserSMS">
					<xsl:value-of select="$i18n.SendFlowInstanceSubmittedUserSMS" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleSubmitSMS();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="submit-sms">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceSubmittedUserSMS') and not($errFieldNames = 'flowInstanceSubmittedNotLoggedInUserSMS')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceSubmittedUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedUserSMS" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<label for="flowInstanceSubmittedNotLoggedInUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedNotLoggedInUserSMS" />
			</label>
			
			<div class="floatleft full marginbottom">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedNotLoggedInUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedNotLoggedInUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable">
				<xsl:with-param name="sms">true</xsl:with-param>
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceArchivedUserSMS'" />
					<xsl:with-param name="id" select="'sendFlowInstanceArchivedUserSMS'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceArchivedUserSMS">
					<xsl:value-of select="$i18n.SendFlowInstanceArchivedUserSMS" />
				</label>
				
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleArchivedSMS();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom" id="archived-sms">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceArchivedUserSMS') and not($errFieldNames = 'flowInstanceArchivedNotLoggedInUserSMS')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceArchivedUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedUserSMS" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<label for="flowInstanceArchivedNotLoggedInUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedNotLoggedInUserSMS" />
			</label>
			
			<div class="floatleft full marginbottom">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedNotLoggedInUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedNotLoggedInUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable">
				<xsl:with-param name="sms">true</xsl:with-param>
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendStatusChangedUserEmail'" />
					<xsl:with-param name="id" select="'sendStatusChangedUserEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendStatusChangedUserEmail">
					<xsl:value-of select="$i18n.SendStatusChangedUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleUserStatus();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="user-status-subject">
		
			<xsl:if test="not($errFieldNames = 'statusChangedUserEmailSubject') and not($errFieldNames = 'statusChangedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="statusChangedUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.StatusChangedUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'statusChangedUserEmailSubject'"/>
					<xsl:with-param name="name" select="'statusChangedUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="user-status-message">
			
			<xsl:if test="not($errFieldNames = 'statusChangedUserEmailSubject') and not($errFieldNames = 'statusChangedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="statusChangedUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.StatusChangedUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'statusChangedUserEmailMessage'"/>
					<xsl:with-param name="name" select="'statusChangedUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendExternalMessageReceivedUserEmail'" />
					<xsl:with-param name="id" select="'sendExternalMessageReceivedUserEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendExternalMessageReceivedUserEmail">
					<xsl:value-of select="$i18n.SendExternalMessageReceivedUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleUserExternalMessage();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="user-message-subject">
		
			<xsl:if test="not($errFieldNames = 'externalMessageReceivedUserEmailSubject') and not($errFieldNames = 'externalMessageReceivedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceSubmittedUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.ExternalMessageReceivedUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'externalMessageReceivedUserEmailSubject'"/>
					<xsl:with-param name="name" select="'externalMessageReceivedUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="user-message-message">
			
			<xsl:if test="not($errFieldNames = 'externalMessageReceivedUserEmailSubject') and not($errFieldNames = 'externalMessageReceivedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceSubmittedUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.ExternalMessageReceivedUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'externalMessageReceivedUserEmailMessage'"/>
					<xsl:with-param name="name" select="'externalMessageReceivedUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
						
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceSubmittedUserEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceSubmittedUserEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceSubmittedUserEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceSubmittedUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleSubmitEmail();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
			
		</div>
		
		<div class="floatleft full bigmarginbottom" id="submit-email-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceSubmittedUserEmailSubject') and not($errFieldNames = 'flowInstanceSubmittedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceSubmittedUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceSubmittedUserEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="submit-email-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceSubmittedUserEmailSubject') and not($errFieldNames = 'flowInstanceSubmittedUserEmailMessage') and not($errFieldNames = 'flowInstanceSubmittedNotLoggedInUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceSubmittedUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<label for="flowInstanceSubmittedNotLoggedInUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedNotLoggedInUserEmailMessage" />
			</label>
			
			<div class="floatleft full marginbottom">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedNotLoggedInUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedNotLoggedInUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
						
			</div>
			
			<div class="floatleft full marginbottom">
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'flowInstanceSubmittedUserEmailAttachPDF'" />
						<xsl:with-param name="id" select="'flowInstanceSubmittedUserEmailAttachPDF'" />
						<xsl:with-param name="element" select="NotificationSettings" />
						<xsl:with-param name="onclick" select="'checkAttachPDF()'" />
					</xsl:call-template>
					
					<label for="flowInstanceSubmittedUserEmailAttachPDF">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedUserEmailAttachPDF" />
					</label>
				</div>
			</div>

			<xsl:call-template name="addUserTagsTable"/>

			<script>checkAttachPDF();</script>
			
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceArchivedUserEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceArchivedUserEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceArchivedUserEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceArchivedUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleArchived();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="archived-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceArchivedUserEmailSubject') and not($errFieldNames = 'flowInstanceArchivedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceArchivedUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceArchivedUserEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="archived-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceArchivedUserEmailSubject') and not($errFieldNames = 'flowInstanceArchivedUserEmailMessage') and not($errFieldNames = 'flowInstanceArchivedNotLoggedInUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceArchivedUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<label for="flowInstanceArchivedNotLoggedInUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedNotLoggedInUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedNotLoggedInUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedNotLoggedInUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="clearboth marginbottom">
			<br/>
		</div>
		
		<h2><xsl:value-of select="$i18n.ManagerNotifications"/></h2>
	
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendExternalMessageReceivedManagerEmail'" />
					<xsl:with-param name="id" select="'sendExternalMessageReceivedManagerEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendExternalMessageReceivedManagerEmail">
					<xsl:value-of select="$i18n.SendExternalMessageReceivedManagerEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleManagerExternalMessage();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>			
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom" id="manager-external-message-subject">
		
			<xsl:if test="not($errFieldNames = 'externalMessageReceivedManagerSubject') and not($errFieldNames = 'externalMessageReceivedManagerMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="externalMessageReceivedManagerSubject" class="floatleft full">
				<xsl:value-of select="$i18n.ExternalMessageReceivedManagerSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'externalMessageReceivedManagerSubject'"/>
					<xsl:with-param name="name" select="'externalMessageReceivedManagerSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="manager-external-message-message">
			
			<xsl:if test="not($errFieldNames = 'externalMessageReceivedManagerSubject') and not($errFieldNames = 'externalMessageReceivedManagerMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="externalMessageReceivedManagerMessage" class="floatleft full">
				<xsl:value-of select="$i18n.ExternalMessageReceivedManagerMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'externalMessageReceivedManagerMessage'"/>
					<xsl:with-param name="name" select="'externalMessageReceivedManagerMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addManagerTagsTable"/>
		</div>
	
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendInternalMessageAddedManagerEmail'" />
					<xsl:with-param name="id" select="'sendInternalMessageAddedManagerEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendInternalMessageAddedManagerEmail">
					<xsl:value-of select="$i18n.SendInternalMessageAddedManagerEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleManagerInternalMessage();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>			
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom" id="manager-internal-message-subject">
		
			<xsl:if test="not($errFieldNames = 'internalMessageAddedManagerEmailSubject') and not($errFieldNames = 'internalMessageAddedManagerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="internalMessageAddedManagerEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.internalMessageAddedManagerEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'internalMessageAddedManagerEmailSubject'"/>
					<xsl:with-param name="name" select="'internalMessageAddedManagerEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="manager-internal-message-message">
			
			<xsl:if test="not($errFieldNames = 'internalMessageAddedManagerEmailSubject') and not($errFieldNames = 'internalMessageAddedManagerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="internalMessageAddedManagerEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.internalMessageAddedManagerEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'internalMessageAddedManagerEmailMessage'"/>
					<xsl:with-param name="name" select="'internalMessageAddedManagerEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addManagerTagsTable"/>
		</div>
	
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceAssignedManagerEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceAssignedManagerEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceAssignedManagerEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceAssignedManagerEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleManagerAssignMessage();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="manager-assign-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedManagerEmailSubject') and not($errFieldNames = 'flowInstanceAssignedManagerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceAssignedManagerEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedManagerEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceAssignedManagerEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedManagerEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="manager-assign-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedManagerEmailSubject') and not($errFieldNames = 'flowInstanceAssignedManagerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceAssignedManagerEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedManagerEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceAssignedManagerEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedManagerEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addManagerTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendStatusChangedManagerEmail'" />
					<xsl:with-param name="id" select="'sendStatusChangedManagerEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendStatusChangedManagerEmail">
					<xsl:value-of select="$i18n.SendStatusChangedManagerEmail" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceSubmittedManagerEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceSubmittedManagerEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceSubmittedManagerEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceSubmittedManagerEmail" />
				</label>
			</div>
		</div>
	
		<div class="clearboth marginbottom">
			<br/>
		</div>
		
		<h2><xsl:value-of select="$i18n.GroupNotifications"/></h2>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceAssignedGroupEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceAssignedGroupEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceAssignedGroupEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceAssignedGroupEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleGroupAssignMessage();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="group-assign-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedGroupEmailSubject') and not($errFieldNames = 'flowInstanceAssignedGroupEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceAssignedGroupEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedGroupEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceAssignedGroupEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedGroupEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="group-assign-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedGroupEmailSubject') and not($errFieldNames = 'flowInstanceAssignedGroupEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceAssignedGroupEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedGroupEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceAssignedGroupEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedGroupEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="clearboth marginbottom">
			<br/>
		</div>
	
		<h2><xsl:value-of select="$i18n.MultisigningNotifications"/></h2>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignInitiatedUserSMS'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignInitiatedUserSMS">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignInitiatedUserSMS" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleText(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignInitiatedUserSMS')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignInitiatedUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserSMS" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignInitiatedUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignInitiatedUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable">
				<xsl:with-param name="sms">true</xsl:with-param>
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignCanceledUserSMS'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignCanceledUserSMS">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignCanceledUserSMS" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleText(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledUserSMS')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignCanceledUserSMS" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserSMS" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledUserSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledUserSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable">
				<xsl:with-param name="sms">true</xsl:with-param>
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignCanceledOwnerSMS'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignCanceledOwnerSMS">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignCanceledOwnerSMS" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleText(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledOwnerSMS')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignCanceledOwnerSMS" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerSMS" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledOwnerSMS'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledOwnerSMS'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
					<xsl:with-param name="rows" select="'4'" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable">
				<xsl:with-param name="sms">true</xsl:with-param>
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignInitiatedUserEmail'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignInitiatedUserEmail">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignInitiatedUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleTexts(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignInitiatedUserEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignInitiatedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceMultiSignInitiatedUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceMultiSignInitiatedUserEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignInitiatedUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignInitiatedUserEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignInitiatedUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignInitiatedUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignInitiatedUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignInitiatedUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable"/>
		</div>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignCanceledUserEmail'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignCanceledUserEmail">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignCanceledUserEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleTexts(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledUserEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignCanceledUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceMultiSignCanceledUserEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledUserEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledUserEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledUserEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignCanceledUserEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignCanceledUserEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledUserEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledUserEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable"/>
		</div>
		
		<div class="floatleft full marginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'sendFlowInstanceMultiSignCanceledOwnerEmail'" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="disabled" select="'true'" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceMultiSignCanceledOwnerEmail">
					<xsl:value-of select="$i18n.sendFlowInstanceMultiSignCanceledOwnerEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleTexts(this);"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledOwnerEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignCanceledOwnerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceMultiSignCanceledOwnerEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledOwnerEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledOwnerEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceMultiSignCanceledOwnerEmailSubject') and not($errFieldNames = 'flowInstanceMultiSignCanceledOwnerEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceMultiSignCanceledOwnerEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceMultiSignCanceledOwnerEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceMultiSignCanceledOwnerEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addSigningTagsTable"/>
		</div>
		
		<div class="clearboth marginbottom">
			<br/>
		</div>
	
		<h2><xsl:value-of select="$i18n.GlobalNotifications"/></h2>
		
		<!-- FlowInstanceSubmittedGlobalEmail -->
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceSubmittedGlobalEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceSubmittedGlobalEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceSubmittedGlobalEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceSubmittedGlobalEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleGlobalSubmit();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-submit-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceSubmittedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceSubmittedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceSubmittedGlobalEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-submit-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceSubmittedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceSubmittedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceSubmittedGlobalEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<label for="flowInstanceSubmittedGlobalEmailAddresses" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailAddresses" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailAddresses'"/>
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailAddresses'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="separateListValues" select="'true'"/>
					<xsl:with-param name="element" select="NotificationSettings/FlowInstanceSubmittedGlobalEmailAddresses/address" />
				</xsl:call-template>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailAttachPDF'" />
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailAttachPDF'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="flowInstanceSubmittedGlobalEmailAttachPDF">
					<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailAttachPDF" />
				</label>
			</div>
		</div>
		
		<div id="flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparatelyDiv" class="floatleft full marginbottom" style="display: none;">
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately'" />
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately">
					<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'flowInstanceSubmittedGlobalEmailAttachXML'" />
					<xsl:with-param name="id" select="'flowInstanceSubmittedGlobalEmailAttachXML'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="flowInstanceSubmittedGlobalEmailAttachXML">
					<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailAttachXML" />
				</label>
			</div>
		</div>
		
		<!-- FlowInstanceAssignedGlobalEmail -->
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceAssignedGlobalEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceAssignedGlobalEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceAssignedGlobalEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceAssignedGlobalEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleGlobalAssigned();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-assigned-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceAssignedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceAssignedGlobalEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceAssignedGlobalEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedGlobalEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-assigned-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceAssignedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceAssignedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceAssignedGlobalEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceAssignedGlobalEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedGlobalEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<label for="flowInstanceAssignedGlobalEmailAddresses" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailAddresses" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceAssignedGlobalEmailAddresses'"/>
					<xsl:with-param name="name" select="'flowInstanceAssignedGlobalEmailAddresses'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="separateListValues" select="'true'"/>
					<xsl:with-param name="element" select="NotificationSettings/FlowInstanceAssignedGlobalEmailAddresses/address" />
				</xsl:call-template>
			</div>
		</div>
		
		<!-- FlowInstanceArchivedGlobalEmail -->
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendFlowInstanceArchivedGlobalEmail'" />
					<xsl:with-param name="id" select="'sendFlowInstanceArchivedGlobalEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendFlowInstanceArchivedGlobalEmail">
					<xsl:value-of select="$i18n.SendFlowInstanceArchivedGlobalEmail" />
				</label>
				<xsl:text>&#160;</xsl:text>
				<span class="tiny"><a onclick="toggleGlobalArchived();"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-archived-subject">
		
			<xsl:if test="not($errFieldNames = 'flowInstanceArchivedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceArchivedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
		
			<label for="flowInstanceArchivedGlobalEmailSubject" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailSubject" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'flowInstanceArchivedGlobalEmailSubject'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedGlobalEmailSubject'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="global-archived-message">
			
			<xsl:if test="not($errFieldNames = 'flowInstanceArchivedGlobalEmailSubject') and not($errFieldNames = 'flowInstanceArchivedGlobalEmailMessage')">
				<xsl:attribute name="class">floatleft full bigmarginbottom hidden</xsl:attribute>
			</xsl:if>
			
			<label for="flowInstanceArchivedGlobalEmailMessage" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailMessage" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedGlobalEmailMessage'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedGlobalEmailMessage'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
			</div>
			
			<xsl:call-template name="addUserTagsTable"/>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<label for="flowInstanceArchivedGlobalEmailAddresses" class="floatleft full">
				<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailAddresses" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'flowInstanceArchivedGlobalEmailAddresses'"/>
					<xsl:with-param name="name" select="'flowInstanceArchivedGlobalEmailAddresses'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="separateListValues" select="'true'"/>
					<xsl:with-param name="element" select="NotificationSettings/FlowInstanceArchivedGlobalEmailAddresses/address" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'flowInstanceArchivedGlobalEmailAttachPDF'" />
					<xsl:with-param name="id" select="'flowInstanceArchivedGlobalEmailAttachPDF'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="flowInstanceArchivedGlobalEmailAttachPDF">
					<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailAttachPDF" />
				</label>
			</div>
		</div>
	
		<!-- ExternalMessageReceivedGlobalEmail -->
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendExternalMessageReceivedGlobalEmail'" />
					<xsl:with-param name="id" select="'sendExternalMessageReceivedGlobalEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendExternalMessageReceivedGlobalEmail">
					<xsl:value-of select="$i18n.SendExternalMessageReceivedGlobalEmail" />
				</label>
			</div>
		</div>	
	
		<div class="floatleft full bigmarginbottom">
		
			<label for="externalMessageReceivedGlobalEmailAddresses" class="floatleft full">
				<xsl:value-of select="$i18n.ExternalMessageReceivedGlobalEmailAddresses" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'externalMessageReceivedGlobalEmailAddresses'"/>
					<xsl:with-param name="name" select="'externalMessageReceivedGlobalEmailAddresses'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="separateListValues" select="'true'"/>
					<xsl:with-param name="element" select="NotificationSettings/ExternalMessageReceivedGlobalEmailAddresses/address" />
				</xsl:call-template>
			</div>
		</div>
		
		
		<!-- ManagerExpiredGlobalEmail -->
		<div class="floatleft full bigmarginbottom margintop internal">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'sendManagerExpiredGlobalEmail'" />
					<xsl:with-param name="id" select="'sendManagerExpiredGlobalEmail'" />
					<xsl:with-param name="element" select="NotificationSettings" />
				</xsl:call-template>
				
				<label for="sendManagerExpiredGlobalEmail">
					<xsl:value-of select="$i18n.SendManagerExpiredGlobalEmail" />
				</label>
			</div>
		</div>	
	
		<div class="floatleft full bigmarginbottom">
		
			<label for="managerExpiredGlobalEmailAddresses" class="floatleft full">
				<xsl:value-of select="$i18n.ManagerExpiredGlobalEmailAddresses" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'managerExpiredGlobalEmailAddresses'"/>
					<xsl:with-param name="name" select="'managerExpiredGlobalEmailAddresses'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="separateListValues" select="'true'"/>
					<xsl:with-param name="element" select="NotificationSettings/ManagerExpiredGlobalEmailAddresses/address" />
				</xsl:call-template>
			</div>
		</div>
	
		<xsl:call-template name="initializeFCKEditor">
			<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
			<xsl:with-param name="customConfig">config.js</xsl:with-param>
			<xsl:with-param name="editorContainerClass">flow-ckeditor</xsl:with-param>
			<xsl:with-param name="editorHeight">150</xsl:with-param>
			<xsl:with-param name="contentsCss">
				<xsl:if test="/Document/cssPath">
					<xsl:value-of select="/Document/cssPath"/>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template name="addUserTagsTable">
		<xsl:param name="sms" select="'false'"/>
	
		<div class="floatleft margintop full">

			<p>
				<xsl:choose>
					<xsl:when test="$sms = 'true'">
						<xsl:value-of select="$i18n.UserTagsTable.smsDescription"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.UserTagsTable.emailDescription"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		
			<table class="full border">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Tag"/>
					</th>
					<th>
						<xsl:value-of select="$i18n.Description"/>
					</th>
				</tr>
				<tr>
					<td>
						<xsl:text>$flow.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowNameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.flowInstanceID</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceIDTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$attribute{integrationExternalID}</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.FlowInstance.ExternalID"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.url</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.messagesUrl</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceMessagesURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.description</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusDescriptionTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterFirstnameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterLastnameTag"/>
					</td>
				</tr>
				<tr class="hidden attached-pdf-tag">
					<td>
						<xsl:text>$flowInstance.pdfAttachedText</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FLowInstanceAttachedPDFTextTag"/>
					</td>
				</tr>
			</table>
		
		</div>
	
	</xsl:template>
	
	<xsl:template name="addManagerTagsTable">
	
		<div class="floatleft margintop full">
			
			<p>
				<xsl:value-of select="$i18n.UserTagsTable.emailDescription"/>
			</p>
			
			<table class="full border">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Tag"/>
					</th>
					<th>
						<xsl:value-of select="$i18n.Description"/>
					</th>
				</tr>
				<tr>
					<td>
						<xsl:text>$flow.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowNameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.flowInstanceID</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceIDTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$attribute{integrationExternalID}</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.FlowInstance.ExternalID"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.url</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.messagesUrl</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceMessagesURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.notesUrl</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceNotesURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusTag"/>
					</td>
				</tr>
				
				<tr>
					<td>
						<xsl:text>$status.description</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusDescriptionTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$manager.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.Manager.Firstname"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$manager.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.Manager.Lastname"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterFirstnameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterLastnameTag"/>
					</td>
				</tr>
			</table>
		
		</div>
	
	</xsl:template>
	
	<xsl:template name="addSigningTagsTable">
		<xsl:param name="sms" select="'false'"/>
	
		<div class="floatleft margintop full">

			<p>
				<xsl:choose>
					<xsl:when test="$sms = 'true'">
						<xsl:value-of select="$i18n.UserTagsTable.smsDescription"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.UserTagsTable.emailDescription"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		
			<table class="full border">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Tag"/>
					</th>
					<th>
						<xsl:value-of select="$i18n.Description"/>
					</th>
				</tr>
				<tr>
					<td>
						<xsl:text>$flow.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowNameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.flowInstanceID</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceIDTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.url</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.messagesUrl</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceMessagesURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.description</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusDescriptionTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterFirstnameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterLastnameTag"/>
					</td>
				</tr>
				
				<tr>
					<td>
						<xsl:text>$signingParty.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.SigningParty.firstname"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$signingParty.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.SigningParty.lastname"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstanceSign.url</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tags.SigningURL"/>
					</td>
				</tr>
			</table>
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validation.requiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validation.tooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.validation.tooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validation.unknownError" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<!-- User -->
					<xsl:when test="fieldName = 'flowInstanceSubmittedUserEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedUserEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedNotLoggedInUserEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedNotLoggedInUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedUserSMS'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedNotLoggedInUserSMS'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedNotLoggedInUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedUserSMS'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedNotLoggedInUserSMS'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedNotLoggedInUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedUserEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedUserEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedNotLoggedInUserEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedNotLoggedInUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'statusChangedUserEmailSubject'">
						<xsl:value-of select="$i18n.StatusChangedUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'statusChangedUserEmailMessage'">
						<xsl:value-of select="$i18n.StatusChangedUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalMessageReceivedUserEmailSubject'">
						<xsl:value-of select="$i18n.ExternalMessageReceivedUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalMessageReceivedUserEmailMessage'">
						<xsl:value-of select="$i18n.ExternalMessageReceivedUserEmailMessage"/>
					</xsl:when>
					
					<!-- Manager -->
					<xsl:when test="fieldName = 'flowInstanceAssignedManagerEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceAssignedManagerEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceAssignedManagerEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceAssignedManagerEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalMessageReceivedManagerSubject'">
						<xsl:value-of select="$i18n.ExternalMessageReceivedManagerSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalMessageReceivedManagerMessage'">
						<xsl:value-of select="$i18n.ExternalMessageReceivedManagerMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'internalMessageAddedManagerEmailSubject'">
						<xsl:value-of select="$i18n.internalMessageAddedManagerEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'internalMessageAddedManagerEmailMessage'">
						<xsl:value-of select="$i18n.internalMessageAddedManagerEmailMessage"/>
					</xsl:when>
					
					<!-- Multisigning -->
					<xsl:when test="fieldName = 'flowInstanceMultiSignInitiatedUserSMS'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignInitiatedUserEmailSubject'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignInitiatedUserEmailMessage'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignInitiatedUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledUserSMS'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledUserEmailSubject'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledUserEmailMessage'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledUserEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledOwnerSMS'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerSMS"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledOwnerEmailSubject'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceMultiSignCanceledOwnerEmailMessage'">
						<xsl:value-of select="$i18n.flowInstanceMultiSignCanceledOwnerEmailMessage"/>
					</xsl:when>
					
					<!-- Global -->
					<xsl:when test="fieldName = 'flowInstanceSubmittedGlobalEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedGlobalEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceSubmittedGlobalEmailAddresses'">
						<xsl:value-of select="$i18n.FlowInstanceSubmittedGlobalEmailAddresses"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceAssignedGlobalEmailAddresses'">
						<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailAddresses"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceAssignedGlobalEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceAssignedGlobalEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceAssignedGlobalEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedGlobalEmailSubject'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedGlobalEmailMessage'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowInstanceArchivedGlobalEmailAddresses'">
						<xsl:value-of select="$i18n.FlowInstanceArchivedGlobalEmailAddresses"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalMessageReceivedGlobalEmailAddresses'">
						<xsl:value-of select="$i18n.ExternalMessageReceivedGlobalEmailAddresses"/>
					</xsl:when>
					<xsl:when test="fieldName = 'managerExpiredGlobalEmailAddresses'">
						<xsl:value-of select="$i18n.ManagerExpiredGlobalEmailAddresses"/>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.validation.unknownFault" />
			</p>
		</xsl:if>
		
	</xsl:template>

</xsl:stylesheet>