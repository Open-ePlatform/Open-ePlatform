<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="imgPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="scripts">
		/js/featherlight.min.js
		/js/flowapprovalmanager.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowapprovalmanager.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<xsl:apply-templates select="TabContents" />
	
	</xsl:template>
	
	<xsl:template match="TabContents">
		
		<div id="flow-approval" class="tabs-content errands-wrapper">
			
			<xsl:choose>
				<xsl:when test="ActivityGroups">
					
					<table class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>
							<tr>
								<th><xsl:value-of select="$i18n.Activity" /></th>
								<th><xsl:value-of select="$i18n.ActivityProgress.completed" /></th>
								<xsl:if test="ActivityGroups/ActivityGroup/useApproveDeny = 'true'">
									<th>
										<xsl:choose>
											<xsl:when test="ActivityGroups/ActivityGroup/approvedText">
												<xsl:value-of select="ActivityGroups/ActivityGroup/approvedText" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$i18n.ActivityProgress.approved" />
											</xsl:otherwise>
										</xsl:choose>
										
										<xsl:text>/</xsl:text>
										
										<xsl:choose>
											<xsl:when test="ActivityGroups/ActivityGroup/deniedText">
												<xsl:value-of select="ActivityGroups/ActivityGroup/deniedText" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$i18n.ActivityProgress.denied" />
											</xsl:otherwise>
										</xsl:choose>
									</th>
								</xsl:if>
								<th><xsl:value-of select="$i18n.ActivityProgress.CompletingUser" /></th>
								<th><xsl:value-of select="$i18n.Activity.responsible" /></th>
								<th style="width: 37px;" />
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="ActivityGroups/ActivityGroup" mode="list" >
								<xsl:with-param name="showApproveDeny" select="ActivityGroups/ActivityGroup/useApproveDeny = 'true'" />
							</xsl:apply-templates>
						</tbody>
					</table>
					
					<div style="display: none;">
					
						<div id="flow-approval-comment-modal" class="comment-modal contentitem">
							<div class="modal-content">
							
								<div class="modal-header bigmarginbottom">
									<h1>
										<xsl:value-of select="$i18n.ViewComment" />
									</h1>
								</div>
								
								<div class="modal-body">
									
									<p/>
									
									<input class="btn btn-light close bigmargintop floatright" type="button" value="{$i18n.Close}" />
									
								</div>
								
							</div>
						</div>
					
					</div>
					
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:value-of select="$i18n.NoActivityGroups"/>
					
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityGroup" mode="list">
		<xsl:param name="showApproveDeny"/>
		
		<tr class="activitygroup">
			<td colspan="6">
				<h3>
					<xsl:value-of select="name" />
				</h3>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.ActivityProgress.added" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:choose>
						<xsl:when test="Activities/Activity/ActivityProgresses">
							<xsl:value-of select="Activities/Activity/ActivityProgresses/ActivityProgress/added"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.ActivityProgress.notStarted" />
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</td>
		</tr>
			
		<xsl:choose>
			<xsl:when test="Activities">
				
				<xsl:apply-templates select="Activities/Activity[ActivityProgresses]" mode="list" >
					<xsl:with-param name="showApproveDeny" select="$showApproveDeny" />
				</xsl:apply-templates>
				
			</xsl:when>
			<xsl:otherwise>
				
				<tr>
					<td colspan="6">
						<xsl:value-of select="$i18n.NoActivities"/>
					</td>
				</tr>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="Activity" mode="list">
		<xsl:param name="showApproveDeny"/>
		
		<tr>
			<xsl:attribute name="class">
				<xsl:text>activity</xsl:text>
				<xsl:if test="position() = last()"> last</xsl:if>
			</xsl:attribute>
			
			<td>
				<xsl:value-of select="name" />
			</td>
			<td>
				<xsl:value-of select="ActivityProgresses/ActivityProgress/completed"/>
				
				<xsl:if test="ActivityProgresses/ActivityProgress/completed and ActivityProgresses/ActivityProgress/comment">
				
					<a class="bigmarginleft" href="#" title="{$i18n.ViewComment}" onclick="commentModal(event, this)">
						<img src="{$imgPath}/comment.png" alt="" />
						
						<span style="display: none;">
							<xsl:call-template name="replaceLineBreak">
								<xsl:with-param name="string" select="ActivityProgresses/ActivityProgress/comment"/>
							</xsl:call-template>
						</span>
					</a>
					
				</xsl:if>
				
			</td>
			<xsl:if test="$showApproveDeny = 'true'">
				<td>
				
					<xsl:if test="../../useApproveDeny = 'true' and ActivityProgresses/ActivityProgress/completed">
						<xsl:choose>
							<xsl:when test="ActivityProgresses/ActivityProgress/denied = 'true'">
								<xsl:choose>
									<xsl:when test="../../deniedText">
										<xsl:value-of select="../../deniedText" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.ActivityProgress.denied" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="../../approvedText">
										<xsl:value-of select="../../approvedText" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.ActivityProgress.approved" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					
				</td>
			</xsl:if>
			<td>
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="ActivityProgresses/ActivityProgress/CompletingUser" />
				</xsl:call-template>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="ResponsibleUsers or ResponsibleGroups">
						
						<xsl:choose>
							<xsl:when test="ResponsibleUserAttributeNames and not(ActivityProgresses/ActivityProgress/ResponsibleAttributedUsers)">
								
								<xsl:apply-templates select="ResponsibleUsers/ResponsibleUser/user" mode="inline-list"/>
								
								<xsl:if test="ResponsibleUsers and ResponsibleGroups">
									<xsl:text>, </xsl:text>
								</xsl:if>
								
							</xsl:when>
							<xsl:otherwise>
								
								<xsl:apply-templates select="ResponsibleUsers/ResponsibleUser[fallback = 'false']/user" mode="inline-list" />
								
								<xsl:if test="ResponsibleUsers/ResponsibleUser[fallback = 'false'] and ActivityProgresses/ActivityProgress/ResponsibleAttributedUsers">
									<xsl:text>, </xsl:text>
								</xsl:if>
								
								<xsl:apply-templates select="ActivityProgresses/ActivityProgress/ResponsibleAttributedUsers/user" mode="inline-list" />
								
								<xsl:if test="(ResponsibleUsers/ResponsibleUser[fallback = 'false'] or ActivityProgresses/ActivityProgress/ResponsibleAttributedUsers) and ResponsibleGroups">
									<xsl:text>, </xsl:text>
								</xsl:if>
								
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:apply-templates select="ResponsibleGroups/group" mode="inline-list"/>
					
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:value-of select="$i18n.Activity.noResponsibles"/>
						
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:if test="../../../../UserModuleURL">
					<a class="marginright" href="{../../../../UserModuleURL}/show/{ActivityProgresses/ActivityProgress/activityProgressID}" title="{$i18n.ShowActivity}">
						<img src="{$imgPath}/magnifying_glass.png" alt="" />
					</a>
				</xsl:if>
				
				<xsl:if test="not(ActivityProgresses/ActivityProgress/completed)">
					<a href="{../../../../PostURL}?sendreminder={ActivityProgresses/ActivityProgress/activityProgressID}" title="{$i18n.SendReminder}">
						<img src="{$imgPath}/mail_reload.png" alt="" />
					</a>
				</xsl:if>
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
	
	<xsl:template name="printUser">
		<xsl:param name="user" />
		
		<xsl:value-of select="$user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$user/lastname" />
		
		<xsl:if test="$user/username">
			<xsl:text>&#160;(</xsl:text>
			<xsl:value-of select="$user/username" />
			<xsl:text>)</xsl:text>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>