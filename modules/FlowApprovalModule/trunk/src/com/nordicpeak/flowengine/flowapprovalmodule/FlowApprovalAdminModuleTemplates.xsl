<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>
	
	<xsl:variable name="imgPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/UserGroupList.js
		/js/flowapprovaladmin.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
		/css/UserGroupList.css
		/css/flowapprovaladmin.css
	</xsl:variable>
	
	<xsl:template match="Document">
	
		<xsl:apply-templates select="FlowOverviewExtension" />
		<xsl:apply-templates select="List" />
		
		<xsl:apply-templates select="ShowActivityGroup" />
		<xsl:apply-templates select="AddActivityGroup" />
		<xsl:apply-templates select="UpdateActivityGroup" />
		
		<xsl:apply-templates select="ShowActivity" />
		<xsl:apply-templates select="AddActivity" />
		<xsl:apply-templates select="UpdateActivity" />
			
	</xsl:template>
	
	<xsl:template match="FlowOverviewExtension">
		
		<div id="flowapprovalsettings" />
		
		<xsl:apply-templates select="ValidationErrors/validationError" />
		
		<xsl:choose>
			<xsl:when test="ActivityGroups">
			
				<table class="full coloredtable sortabletable oep-table" cellspacing="0">
					<thead>
						<tr>
							<th><xsl:value-of select="$i18n.ActivityGroup" /></th>
							<th><xsl:value-of select="$i18n.ActivityGroup.startStatus" /></th>
							<th><xsl:value-of select="$i18n.ActivityGroup.completeStatus" /></th>
							<th><xsl:value-of select="$i18n.ActivityGroup.denyStatus" /></th>
							<th><xsl:value-of select="$i18n.ActivityGroup.activityCount" /></th>
							<th width="37" />
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates select="ActivityGroups/ActivityGroup" mode="list" />
					</tbody>
				</table>

			</xsl:when>
			<xsl:otherwise>
			
				<xsl:value-of select="$i18n.NoActivityGroups"/>
				
			</xsl:otherwise>
		</xsl:choose>
		
		<div class="floatright">
			
			<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/addactivitygroup" title="{$i18n.AddActivityGroup}">
				<xsl:value-of select="$i18n.AddActivityGroup"/>
				<img class="marginleft" src="{$imgPath}/add.png" alt="" />
			</a>
			
		</div>
		
	</xsl:template>
	
	<!-- Only used for showing errors -->
	<xsl:template match="List">
	
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="Flow/name"/>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="Title"/>
			</h1>
	
			<xsl:apply-templates select="ValidationErrors/validationError" />
			
			<xsl:choose>
				<xsl:when test="ActivityGroups">
				
					<table class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>
							<tr>
								<th><xsl:value-of select="$i18n.ActivityGroup" /></th>
								<th><xsl:value-of select="$i18n.ActivityGroup.startStatus" /></th>
								<th><xsl:value-of select="$i18n.ActivityGroup.completeStatus" /></th>
								<th><xsl:value-of select="$i18n.ActivityGroup.denyStatus" /></th>
								<th><xsl:value-of select="$i18n.ActivityGroup.activityCount" /></th>
								<th width="37" />
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="ActivityGroups/ActivityGroup" mode="list" />
						</tbody>
					</table>
	
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:value-of select="$i18n.NoActivityGroups"/>
					
				</xsl:otherwise>
			</xsl:choose>
			
			<div class="floatright">
				
				<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/addactivitygroup" title="{$i18n.AddActivityGroup}">
					<xsl:value-of select="$i18n.AddActivityGroup"/>
					<img class="marginleft" src="{$imgPath}/add.png" alt="" />
				</a>
				
			</div>
			
			<div class="floatleft">
				<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/toflow" title="{$i18n.BackToFlow}">
					<xsl:value-of select="$i18n.BackToFlow"/>
				</a>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityGroup" mode="list">
		
		<tr>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/showactivitygroup/{activityGroupID}" title="{$i18n.ShowActivityGroup}: {name}">
					<xsl:value-of select="name" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/showactivitygroup/{activityGroupID}" title="{$i18n.ShowActivityGroup}: {name}">
					<xsl:value-of select="startStatus" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/showactivitygroup/{activityGroupID}" title="{$i18n.ShowActivityGroup}: {name}">
					<xsl:value-of select="completeStatus" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/showactivitygroup/{activityGroupID}" title="{$i18n.ShowActivityGroup}: {name}">
					<xsl:value-of select="denyStatus" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/showactivitygroup/{activityGroupID}" title="{$i18n.ShowActivityGroup}: {name}">
					<xsl:value-of select="count(Activities/Activity)" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/updateactivitygroup/{activityGroupID}" title="{$i18n.UpdateActivityGroup}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
			
				<a class="marginleft" href="{/Document/requestinfo/contextpath}{../../extensionRequestURL}/deleteactivitygroup/{activityGroupID}" onclick="return confirm('{$i18n.DeleteActivityGroup.Confirm}: {name}?');" title="{$i18n.DeleteActivityGroup}: {name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="AddActivityGroup">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.AddActivityGroup"/>
			</h1>
	
			<xsl:apply-templates select="validationException/validationError" />
	
			<form id="activityGroupForm" method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/addactivitygroup">
			
				<xsl:call-template name="activityGroupForm" />
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.AddActivityGroup}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template match="UpdateActivityGroup">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.UpdateActivityGroup"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="ActivityGroup/name"/>
			</h1>
	
			<xsl:apply-templates select="validationException/validationError" />
	
			<form id="activityGroupForm" method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateactivitygroup/{ActivityGroup/activityGroupID}">
			
				<xsl:call-template name="activityGroupForm" />
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template name="activityGroupForm">
		
		<div class="floatleft full bigmarginbottom">

			<label class="floatleft full required" for="name">
				<xsl:value-of select="$i18n.ActivityGroup.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'" />
					<xsl:with-param name="name" select="'name'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
			</div>

		</div>
		
		<input name="statusURL" disabled="true" hidden="true" value="{/Document/requestinfo/contextpath}{extensionRequestURL}/statuses/{Flow/FlowFamily/flowFamilyID}"/>
		
		<div class="floatleft full bigmarginbottom">

			<label class="floatleft full required" for="startStatus">
				<xsl:value-of select="$i18n.ActivityGroup.startStatus" />
			</label>
			
			<p class="nomargin marginbottom">
				<xsl:value-of select="$i18n.ActivityGroup.startStatus2" />
			</p>

			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'startStatus'" />
					<xsl:with-param name="name" select="'startStatus'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
			</div>

		</div>
		
		<div class="floatleft full bigmarginbottom">

			<label class="floatleft full required" for="completeStatus">
				<xsl:value-of select="$i18n.ActivityGroup.completeStatus" />
			</label>
			
			<p class="nomargin marginbottom">
				<xsl:value-of select="$i18n.ActivityGroup.completeStatus2" />
			</p>

			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'completeStatus'" />
					<xsl:with-param name="name" select="'completeStatus'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
			</div>

		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useApproveDeny'" />
					<xsl:with-param name="id" select="'useApproveDeny'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
				
				<label class="marginleft" for="useApproveDeny">
					<xsl:value-of select="$i18n.ActivityGroup.useApproveDeny" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">

			<label class="floatleft full required" for="denyStatus">
				<xsl:value-of select="$i18n.ActivityGroup.denyStatus" />
			</label>
			
			<p class="nomargin marginbottom">
				<xsl:value-of select="$i18n.ActivityGroup.denyStatus2" />
			</p>

			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'denyStatus'" />
					<xsl:with-param name="name" select="'denyStatus'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
			</div>

		</div>
		
		<xsl:call-template name="notificationEmail">
			<xsl:with-param name="toggleField" select="'sendActivityGroupStartedEmail'"/>
			<xsl:with-param name="toggleLabel" select="$i18n.ActivityGroup.sendActivityGroupStartedEmail"/>
			<xsl:with-param name="subjectField" select="'activityGroupStartedEmailSubject'"/>
			<xsl:with-param name="subjectLabel" select="$i18n.ActivityGroup.activityGroupStartedEmailSubject"/>
			<xsl:with-param name="messageField" select="'activityGroupStartedEmailMessage'"/>
			<xsl:with-param name="messageLabel" select="$i18n.ActivityGroup.activityGroupStartedEmailMessage"/>
			<xsl:with-param name="tagsTable" select="'manager'"/>
		</xsl:call-template>
		
		<div class="floatleft full bigmarginbottom">

			<label class="floatleft full" for="userDescription">
				<xsl:value-of select="$i18n.ActivityGroup.userDescriptionTemplate" />
			</label>
			
			<p class="nomargin marginbottom">
				<xsl:value-of select="$i18n.ActivityGroup.attributesDescription" />
			</p>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'userDescriptionTemplate'" />
					<xsl:with-param name="name" select="'userDescriptionTemplate'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
			</div>

		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'appendCommentsToExternalMessages'" />
					<xsl:with-param name="id" select="'appendCommentsToExternalMessages'" />
					<xsl:with-param name="element" select="ActivityGroup" />
				</xsl:call-template>
				
				<label class="marginleft" for="appendCommentsToExternalMessages">
					<xsl:value-of select="$i18n.ActivityGroup.appendCommentsToExternalMessages" />
				</label>
			</div>
		</div>
		
		<xsl:call-template name="initializeFCKEditor">
			<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
			<xsl:with-param name="customConfig">config.js</xsl:with-param>
			<xsl:with-param name="editorContainerClass">flow-ckeditor</xsl:with-param>
			<xsl:with-param name="editorHeight">150</xsl:with-param>
			
			<xsl:with-param name="contentsCss">
				<xsl:if test="cssPath">
					<xsl:value-of select="cssPath"/>
				</xsl:if>
			</xsl:with-param>
			
			<xsl:with-param name="filebrowserBrowseUri">
				
				<xsl:choose>
					<xsl:when test="ckConnectorModuleAlias">
						<xsl:text>filemanager/index.html?Connector=</xsl:text>
						<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="ckConnectorModuleAlias" />
						<xsl:text>/connector</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="''"/>
					</xsl:otherwise>
				</xsl:choose>

			</xsl:with-param>
			
			<xsl:with-param name="filebrowserImageBrowseUri">
			
				<xsl:choose>
					<xsl:when test="ckConnectorModuleAlias">
						<xsl:text>filemanager/index.html?Connector=</xsl:text>
						<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="ckConnectorModuleAlias" />
						<xsl:text>/connector</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="''"/>
					</xsl:otherwise>
				</xsl:choose>
			
			</xsl:with-param>
			
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="ShowActivityGroup">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<div class="floatright">
				
				<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateactivitygroup/{ActivityGroup/activityGroupID}" title="{$i18n.UpdateActivityGroup}">
					<img class="marginleft" src="{$imgPath}/pen.png" alt="" />
				</a>
			
				<a class="marginleft" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/deleteactivitygroup/{activityGroupID}" onclick="return confirm('{$i18n.DeleteActivityGroup.Confirm}: {ActivityGroup/name}?');" title="{$i18n.DeleteActivityGroup}: {ActivityGroup/name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
				
			</div>
			
			<h1>
				<xsl:value-of select="$i18n.ShowActivityGroup"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="ActivityGroup/name"/>
			</h1>
			
			<xsl:apply-templates select="ValidationErrors/validationError" />
			
			<div class="bigmarginbottom">
				
				<strong>
					<xsl:value-of select="$i18n.ActivityGroup.startStatus" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				<xsl:value-of select="ActivityGroup/startStatus" />
				
				<br/>
				
				<strong>
					<xsl:value-of select="$i18n.ActivityGroup.completeStatus" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				<xsl:value-of select="ActivityGroup/completeStatus" />
				
				<xsl:if test="ActivityGroup/useApproveDeny = 'true'">
					
					<br/>
					
					<strong>
						<xsl:value-of select="$i18n.ActivityGroup.useApproveDeny" />
						<xsl:text>:&#160;</xsl:text>
					</strong>
					<xsl:value-of select="$i18n.Yes" />
					
					<br/>
					
					<strong>
						<xsl:value-of select="$i18n.ActivityGroup.denyStatus" />
						<xsl:text>:&#160;</xsl:text>
					</strong>
					<xsl:value-of select="ActivityGroup/denyStatus" />
					
				</xsl:if>
				
				<br/>
				
				<strong>
					<xsl:value-of select="$i18n.ActivityGroup.userDescriptionTemplate" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				<xsl:value-of select="ActivityGroup/userDescriptionTemplate" />
				
				<xsl:if test="ActivityGroup/appendCommentsToExternalMessages = 'true'">
					
					<br/>
					
					<strong>
						<xsl:value-of select="$i18n.ActivityGroup.appendCommentsToExternalMessages" />
						<xsl:text>:&#160;</xsl:text>
					</strong>
					<xsl:value-of select="$i18n.Yes" />
					
				</xsl:if>
				
			</div>
			
			<xsl:choose>
				<xsl:when test="ActivityGroup/Activities">
				
					<table class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>
							<tr>
								<th><xsl:value-of select="$i18n.Activity" /></th>
								<th><xsl:value-of select="$i18n.Activity.responsible" /></th>
								<th><xsl:value-of select="$i18n.Activity.attributeName" /></th>
								<th><xsl:value-of select="$i18n.Activity.inverted" /></th>
								<th><xsl:value-of select="$i18n.Activity.attributeValues" /></th>
								<th width="37" />
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="ActivityGroup/Activities/Activity" mode="list" />
						</tbody>
					</table>
	
				</xsl:when>
				<xsl:otherwise>
				
					<p><xsl:value-of select="$i18n.NoActivities"/></p>
					
				</xsl:otherwise>
			</xsl:choose>
			
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/addactivity/{ActivityGroup/activityGroupID}" title="{$i18n.AddActivity}">
					<xsl:value-of select="$i18n.AddActivity"/>
					<img class="marginleft" src="{$imgPath}/add.png" alt="" />
				</a>
			</div>
			
			<div class="floatleft">
				<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/toflow" title="{$i18n.BackToFlow}">
					<xsl:value-of select="$i18n.BackToFlow"/>
				</a>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="Activity" mode="list">
		
		<tr>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/showactivity/{activityID}" title="{$i18n.ShowActivity}: {name}">
					<xsl:value-of select="name" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/showactivity/{activityID}" title="{$i18n.ShowActivity}: {name}">
					<xsl:choose>
						<xsl:when test="ResponsibleUsers or ResponsibleGroups">
						
							<xsl:apply-templates select="ResponsibleUsers/user" mode="inline-list"/>
							
							<xsl:if test="ResponsibleUsers and ResponsibleGroups">
									<xsl:text>, </xsl:text>
							</xsl:if>
							
							<xsl:apply-templates select="ResponsibleGroups/group" mode="inline-list"/>
						
						</xsl:when>
						<xsl:otherwise>
							
							<xsl:value-of select="$i18n.Activity.noResponsibles"/>
							
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/showactivity/{activityID}" title="{$i18n.ShowActivity}: {name}">
					<xsl:value-of select="attributeName" />
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/showactivity/{activityID}" title="{$i18n.ShowActivity}: {name}">
					<xsl:if test="invert = 'true'">
						<xsl:value-of select="$i18n.Activity.inverted" />
					</xsl:if>
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/showactivity/{activityID}" title="{$i18n.ShowActivity}: {name}">
					
					<xsl:for-each select="AttributeValues/*">
						<xsl:if test="position() > 1">
							<xsl:text>, </xsl:text>
						</xsl:if>
						
						<xsl:value-of select="."/>
					</xsl:for-each>
					
				</a>
			</td>
			<td>
			
				<a href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/updateactivity/{activityID}" title="{$i18n.UpdateActivity}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
			
				<a class="marginleft" href="{/Document/requestinfo/contextpath}{../../../extensionRequestURL}/deleteactivity/{activityID}" onclick="return confirm('{$i18n.DeleteActivity.Confirm}: {name}?');" title="{$i18n.DeleteActivity}: {name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
				
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="user" mode="inline-list">
		
		<xsl:if test="position() > 1">
			<xsl:text>, </xsl:text>
		</xsl:if>
	
		<xsl:choose>
			<xsl:when test="enabled='true'">
				<img class="marginright" src="{$imgPath}/user.png" alt="" />
			</xsl:when>
			<xsl:otherwise>
				<img class="marginright" src="{$imgPath}/user_disabled.png" alt="" />
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:value-of select="firstname"/>
		
		<xsl:text>&#x20;</xsl:text>
		
		<xsl:value-of select="lastname"/>
		
		<xsl:if test="username">
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:text>(</xsl:text>
				<xsl:value-of select="username"/>
			<xsl:text>)</xsl:text>
		</xsl:if>
			
	</xsl:template>
	
	<xsl:template match="group" mode="inline-list">
		
		<xsl:if test="position() > 1">
			<xsl:text>, </xsl:text>
		</xsl:if>

		<xsl:choose>
			<xsl:when test="enabled='true'">
				<img class="marginright" src="{$imgPath}/group.png" alt="" />
			</xsl:when>
			<xsl:otherwise>
				<img class="marginright" src="{$imgPath}/group_disabled.png" alt="" />
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:value-of select="name"/>
		
	</xsl:template>
	
	<xsl:template match="ShowActivity">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<div class="floatright">
			
				<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateactivity/{Activity/activityID}" title="{$i18n.UpdateActivity}: {Activity/name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
			
				<a class="marginleft" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/deleteactivity/{Activity/activityID}" onclick="return confirm('{$i18n.DeleteActivity.Confirm}: {Activity/name}?');" title="{$i18n.DeleteActivity}: {Activity/name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
				
			</div>
			
			<h1>
				<xsl:value-of select="$i18n.ShowActivity"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="Activity/name"/>
			</h1>
			
			<xsl:apply-templates select="ValidationErrors/validationError" />
			
			<strong>
				<xsl:value-of select="$i18n.Activity.description" />
			</strong>
			<p>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="Activity/description" />
				</xsl:call-template>
			</p>
			
			<div class="bigmarginbottom">
				<strong>
					<xsl:value-of select="$i18n.Activity.responsibleGroups" />
				</strong>
				
				<xsl:apply-templates select="Activity/ResponsibleGroups/group" mode="list"/>
			</div>
			
			<div class="bigmarginbottom">
				<strong class="bigmargintop">
					<xsl:value-of select="$i18n.Activity.responsibleUsers" />
				</strong>
				
				<xsl:apply-templates select="Activity/ResponsibleUsers/user" mode="list"/>
			</div>
			
			<div class="floatleft">
				<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/showactivitygroup/{Activity/ActivityGroup/activityGroupID}" title="{$i18n.BackToActivityGroup}">
					<xsl:value-of select="$i18n.BackToActivityGroup"/>
				</a>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="user" mode="list">
		
		<div>
			
			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="marginright" src="{$imgPath}/user.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="marginright" src="{$imgPath}/user_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:value-of select="firstname"/>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="lastname"/>
			
			<xsl:if test="username">
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:text>(</xsl:text>
					<xsl:value-of select="username"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="group" mode="list">
		
		<div>
			
			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="marginright" src="{$imgPath}/group.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="marginright" src="{$imgPath}/group_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:value-of select="name"/>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddActivity">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.AddActivity"/>
			</h1>
	
			<xsl:apply-templates select="validationException/validationError" />
	
			<form method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/addactivity/{ActivityGroup/activityGroupID}">
			
				<xsl:call-template name="activityForm" />
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.AddActivity}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template match="UpdateActivity">
		
		<div id="FlowApprovalAdminModule" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.UpdateActivity"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="Activity/name"/>
			</h1>
	
			<xsl:apply-templates select="validationException/validationError" />
	
			<form method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateactivity/{Activity/activityID}">
			
				<xsl:call-template name="activityForm" />
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template name="activityForm">
		
		<div class="floatleft full bigmarginbottom">
	
			<label class="floatleft full required" for="name">
				<xsl:value-of select="$i18n.Activity.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'" />
					<xsl:with-param name="name" select="'name'" />
					<xsl:with-param name="element" select="Activity" />
				</xsl:call-template>
			</div>
	
		</div>
		
		<div class="floatleft full bigmarginbottom">
	
			<label class="floatleft full" for="description">
				<xsl:value-of select="$i18n.Activity.description" />
			</label>
			
			<p class="nomargin marginbottom">
				<xsl:value-of select="$i18n.ActivityGroup.attributesDescription" />
			</p>
	
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'description'" />
					<xsl:with-param name="name" select="'description'" />
					<xsl:with-param name="element" select="Activity" />
					<xsl:with-param name="rows" select="'8'" />
				</xsl:call-template>
			</div>
	
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.Activity.responsibleGroups" />
			</label>
			
			<xsl:call-template name="GroupList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/contextpath"/>
					<xsl:value-of select="extensionRequestURL"/>
					<xsl:text>/groups</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'responsibleGroup'"/>
				<xsl:with-param name="groups" select="Activity/ResponsibleGroups" />
			</xsl:call-template>
			
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.Activity.responsibleUsers" />
			</label>
			
			<xsl:call-template name="UserList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/contextpath"/>
					<xsl:value-of select="extensionRequestURL"/>
					<xsl:text>/users</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'responsibleUser'"/>
				<xsl:with-param name="users" select="Activity/ResponsibleUsers" />
			</xsl:call-template>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="globalEmailAddress" class="floatleft full">
				<xsl:value-of select="$i18n.Activity.globalEmailAddress" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'globalEmailAddress'"/>
					<xsl:with-param name="name" select="'globalEmailAddress'"/>
					<xsl:with-param name="element" select="Activity" />
				</xsl:call-template>
			</div>
		</div>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.Activity.AttributeFilter"/></legend>
			
			<p>
				<xsl:value-of select="$i18n.Activity.AttributeFilterDescription" />
			</p>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="attributeName" class="floatleft full">
					<xsl:value-of select="$i18n.Activity.AttributeName" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attributeName'"/>
						<xsl:with-param name="name" select="'attributeName'"/>
						<xsl:with-param name="element" select="Activity" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'invert'" />
						<xsl:with-param name="id" select="'invert'" />
						<xsl:with-param name="element" select="Activity" />
					</xsl:call-template>
					
					<label class="marginleft" for="invert">
						<xsl:value-of select="$i18n.Activity.invert" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="attributeValues" class="floatleft">
					<xsl:value-of select="$i18n.Activity.AttributeValues" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'attributeValues'"/>
						<xsl:with-param name="name" select="'attributeValues'"/>
						<xsl:with-param name="element" select="Activity/AttributeValues/value" />
						<xsl:with-param name="separateListValues" select="'true'"/>
					</xsl:call-template>
				</div>
			</div>
			
		</fieldset>
		
	</xsl:template>
	
	<xsl:template name="notificationEmail">
		<xsl:param name="toggleField" select="null"/>
		<xsl:param name="toggleFieldChecked" select="null"/>
		<xsl:param name="toggleLabel" />
		<xsl:param name="subjectField" />
		<xsl:param name="subjectLabel" />
		<xsl:param name="messageField" select="null" />
		<xsl:param name="messageLabel" select="null" />
		<xsl:param name="valueField" select="null" />
		<xsl:param name="valueElement" select="null" />
		<xsl:param name="addressesField" select="null" />
		<xsl:param name="addressesLabel" select="null" />
		<xsl:param name="addressesElement" select="null" />
		<xsl:param name="tagsTable" select="'user'" />
		
		<div class="notification">
		
			<div class="floatleft full bigmarginbottom margintop">
			
				<div class="floatleft">
					
					<xsl:choose>
						<xsl:when test="$toggleField and $toggleFieldChecked">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="$toggleField" />
								<xsl:with-param name="id" select="$toggleField" />
								<xsl:with-param name="checked" select="'true'" />
								<xsl:with-param name="class">
									<xsl:if test="$valueField">notification-value-toggle</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="$toggleField">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="$toggleField" />
								<xsl:with-param name="id" select="$toggleField" />
								<xsl:with-param name="element" select="ActivityGroup" />
								<xsl:with-param name="class">
									<xsl:if test="$valueField">notification-value-toggle</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="checked" select="'true'" />
								<xsl:with-param name="disabled" select="'true'" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				
					<label class="nomargin" for="{$toggleField}">
						<xsl:value-of select="$toggleLabel" />
					</label>
					<xsl:text>&#160;</xsl:text>
					<span class="tiny"><a class="notification-text-toggle"><xsl:value-of select="$i18n.ToggleTexts" /></a></span>
				</div>
				
				<xsl:if test="$valueField">
					
					<div class="notification-value floatleft full">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="$valueField" />
							<xsl:with-param name="id" select="$valueField" />
							<xsl:with-param name="size" select="40" />
							<xsl:with-param name="value" select="$valueElement" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
			</div>
			
			<div class="notification-text floatleft full">
			
				<xsl:choose>
					<xsl:when test="$messageField">
						<xsl:if test="not(validationException/validationError/fieldName = $subjectField) and not(validationException/validationError/fieldName = $messageField)">
							<xsl:attribute name="class">notification-text floatleft full hidden</xsl:attribute>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="not(validationException/validationError/fieldName = $subjectField)">
							<xsl:attribute name="class">notification-text floatleft full hidden</xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			
				<div class="floatleft full bigmarginbottom">
			
					<label class="nomargin" for="{$subjectField}">
						<xsl:value-of select="$subjectLabel" />
					</label>
			
					<div class="floatleft full">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$subjectField"/>
							<xsl:with-param name="name" select="$subjectField"/>
							<xsl:with-param name="title" select="$subjectLabel"/>
							<xsl:with-param name="element" select="ActivityGroup" />
						</xsl:call-template>
					</div>
					
					<xsl:if test="not($messageField) or not($messageLabel)">
					
						<xsl:choose>
							<xsl:when test="$tagsTable = 'manager'">
								<xsl:call-template name="addManagerTagsTable"/>
							</xsl:when>
							<xsl:otherwise>
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:if>
					
				</div>
				
				<xsl:if test="$messageField and $messageLabel">
				
					<div class="floatleft full bigmarginbottom">
						
						<label for="{$messageField}" class="floatleft full">
							<xsl:value-of select="$messageLabel" />
						</label>
						
						<div class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="$messageField"/>
								<xsl:with-param name="name" select="$messageField"/>
								<xsl:with-param name="class" select="'ckeditor'"/>
								<xsl:with-param name="element" select="ActivityGroup" />
							</xsl:call-template>
						</div>
						
						<xsl:choose>
							<xsl:when test="$tagsTable = 'manager'">
								<xsl:call-template name="addManagerTagsTable"/>
							</xsl:when>
							<xsl:otherwise>
							</xsl:otherwise>
						</xsl:choose>
						
					</div>
					
				</xsl:if>
				
			</div>
			
			<xsl:if test="$addressesField and $addressesLabel">
			
				<div class="floatleft full bigmarginbottom">
				
					<label for="{$addressesField}" class="floatleft full nomargin">
						<xsl:value-of select="$addressesLabel" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="$addressesField"/>
							<xsl:with-param name="name" select="$addressesField"/>
							<xsl:with-param name="rows" select="5"/>
							<xsl:with-param name="separateListValues" select="'true'"/>
							<xsl:with-param name="element" select="$addressesElement" />
						</xsl:call-template>
					</div>
					
				</div>
					
			</xsl:if>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="addManagerTagsTable">
	
		<div class="floatleft margintop full">

			<p><xsl:value-of select="$i18n.TagsTable.Description.Email"/></p>
			
			<table class="full border">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Tag"/>
					</th>
					<th>
						<xsl:value-of select="$i18n.TagDescription"/>
					</th>
				</tr>
				<tr>
					<td>
						<xsl:text>$activitygroup.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.ActivityGroup.name"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$activities</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.Activities"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$myActivitiesURL</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.MyActivities"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.flowInstanceID</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.FlowInstance.flowInstanceID"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flow.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.Flow.name"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$manager.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.Manager.Firstname"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$manager.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.Tag.Manager.Lastname"/>
					</td>
				</tr>
			</table>
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ResponsibleRequired']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.ResponsibleRequired"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='MultipleCompletionStatusesForSameStartStatus']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.MultipleCompletionStatusesForSameStartStatus"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='MultipleDenyStatusesForSameStartStatus']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.MultipleDenyStatusesForSameStartStatus"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ActivityGroupInvalidStatus']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.ActivityGroupInvalidStatus.1"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="activityGroupName" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="$i18n.Validation.ActivityGroupInvalidStatus.2"/>
			<xsl:text> "</xsl:text>
			<xsl:value-of select="invalidStatus" />
			<xsl:text>" </xsl:text>
			<xsl:value-of select="$i18n.Validation.ActivityGroupInvalidStatus.3"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidStatus' and fieldName]">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.InvalidStatus"/>
			<xsl:text> </xsl:text>
			<xsl:choose>
				<xsl:when test="fieldName = 'startStatus'">
					<xsl:value-of select="$i18n.ActivityGroup.startStatus"/>
				</xsl:when>
				<xsl:when test="fieldName = 'completeStatus'">
					<xsl:value-of select="$i18n.ActivityGroup.completeStatus"/>
				</xsl:when>
				<xsl:when test="fieldName = 'denyStatus'">
					<xsl:value-of select="$i18n.ActivityGroup.denyStatus"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="fieldName"/>
				</xsl:otherwise>
			</xsl:choose>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ShowFailedActivityNotFound' or messageKey='DeleteFailedActivityotFound' or messageKey='UpdateFailedActivityNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.ActivityNotFound"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ShowFailedActivityGroupNotFound' or messageKey='DeleteFailedActivityGroupNotFound' or messageKey='UpdateFailedActivityGroupNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.Validation.ActivityGroupNotFound"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.Validation.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.Validation.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.Validation.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.Validation.TooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.Validation.UnknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startStatus'">
						<xsl:value-of select="$i18n.ActivityGroup.startStatus"/>
					</xsl:when>
					<xsl:when test="fieldName = 'completeStatus'">
						<xsl:value-of select="$i18n.ActivityGroup.completeStatus"/>
					</xsl:when>
					<xsl:when test="fieldName = 'denyStatus'">
						<xsl:value-of select="$i18n.ActivityGroup.denyStatus"/>
					</xsl:when>
					<xsl:when test="fieldName = 'activityGroupStartedEmailSubject'">
						<xsl:value-of select="$i18n.ActivityGroup.activityGroupStartedEmailSubject"/>
					</xsl:when>
					<xsl:when test="fieldName = 'activityGroupStartedEmailMessage'">
						<xsl:value-of select="$i18n.ActivityGroup.activityGroupStartedEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'userDescriptionTemplate'">
						<xsl:value-of select="$i18n.ActivityGroup.userDescriptionTemplate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$i18n.Activity.description"/>
					</xsl:when>
					<xsl:when test="fieldName = 'responsibleUsers'">
						<xsl:value-of select="$i18n.Activity.responsibleUsers"/>
					</xsl:when>
					<xsl:when test="fieldName = 'responsibleGroups'">
						<xsl:value-of select="$i18n.Activity.responsibleGroups"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attributeName'">
						<xsl:value-of select="$i18n.Activity.AttributeName"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attributeValues'">
						<xsl:value-of select="$i18n.Activity.AttributeValues"/>
					</xsl:when>
					<xsl:when test="fieldName = 'appendCommentsToExternalMessages'">
						<xsl:value-of select="$i18n.ActivityGroup.appendCommentsToExternalMessages"/>
					</xsl:when>
					<xsl:when test="fieldName = 'globalEmailAddress'">
						<xsl:value-of select="$i18n.Activity.globalEmailAddress"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.Validation.UnknownMessageKey" />
			</p>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>