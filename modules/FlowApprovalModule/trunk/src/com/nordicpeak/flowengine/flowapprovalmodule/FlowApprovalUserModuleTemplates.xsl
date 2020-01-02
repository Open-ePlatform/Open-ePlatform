<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/flowengine.helpdialog.js
		/js/jquery.tablesorter.min.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
		/css/flowapprovaluser.css
	</xsl:variable>

	<xsl:template match="Document">	
			
			<xsl:apply-templates select="ListPendingActivities"/>
			<xsl:apply-templates select="ListCompletedActivities"/>
			<xsl:apply-templates select="ShowActivity"/>
			
	</xsl:template>
	
	<xsl:template match="ListPendingActivities">
		
		<xsl:apply-templates select="validationError"/>
		
		<div class="contentitem">
			<section>
				<div class="errands-wrapper">
				
					<div class="heading-wrapper">
						<h2><xsl:value-of select="$i18n.PendingActivities" /></h2>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'bookings-list'" />
							<xsl:with-param name="text" select="$i18n.PendingActivities.help" />
						</xsl:call-template>
					</div>
					
					<table class="oep-table errand-table">
						<thead class="sortable">
							<tr>
								<th class="icon no-sort"></th>
								<th><span data-icon-after="_"><xsl:value-of select="$i18n.ActivityProgress" /></span></th>
								<th class="no-sort"></th>
							</tr>
						</thead>
						<tbody>
							<xsl:choose>
								<xsl:when test="FlowInstances">
								
									<xsl:apply-templates select="FlowInstances/FlowInstance/ActivityGroups/ActivityGroup" mode="list-pending" />
									
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td class="icon" />
										<td colspan="2">
											<xsl:value-of select="$i18n.NoActivities" />
										</td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
						</tbody>
					</table>
					
					<div class="floatleft bigmargintop">
						<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/listcompleted" title="{$i18n.ShowCompleted}">
							<xsl:value-of select="$i18n.ShowCompleted"/>
						</a>
					</div>
					
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityGroup" mode="list-pending">
		
		<tr class="activitygroup">
			<td colspan="3">
				<h3>
					<xsl:value-of select="name" />
				</h3>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.FlowInstance.flowInstanceID" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="../../flowInstanceID"/>
				</div>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.Flow.name" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="../../Flow/name"/>
				</div>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.ActivityProgress.added" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="Progresses/ActivityProgress[1]/added"/>
				</div>
			</td>
		</tr>
		
		<xsl:apply-templates select="Progresses/ActivityProgress" mode="list-pending" />
		
	</xsl:template>
	
	<xsl:template match="ActivityProgress" mode="list-pending">
		
		<tr>
			<xsl:attribute name="class">
				<xsl:text>activity</xsl:text>
				<xsl:if test="position() = last()"> last</xsl:if>
			</xsl:attribute>
			
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{../../../../Flow/flowID}?{../../../../Flow/IconLastModified}" width="25" alt="" />
			</td>
			<td data-title="{$i18n.Activity}">
				<div>
					<xsl:value-of select="Activity/name" />
				</div>
				
				<xsl:if test="ShortDescription">
					<div>
						<strong>
							<xsl:value-of select="$i18n.ActivityProgress.shortDescription" />
							<xsl:text>: </xsl:text>
						</strong>
						
						<xsl:value-of select="ShortDescription"/>
					</div>
				</xsl:if>
			</td>
			<td class="link">
				<a class="btn btn-green" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{activityProgressID}"><xsl:value-of select="$i18n.ShowActivity" /></a>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="ListCompletedActivities">
		
		<xsl:apply-templates select="validationError"/>
		
		<div class="contentitem">
			<section>
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h2><xsl:value-of select="$i18n.CompletedActivities" /></h2>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'archivedbookings-list'" />
							<xsl:with-param name="text" select="$i18n.CompletedActivities.help" />
						</xsl:call-template>
					</div>
					
					<table class="oep-table errand-table">
						<thead class="sortable">
							<tr>
								<th class="icon no-sort"></th>
								<th><span data-icon-after="_"><xsl:value-of select="$i18n.ActivityProgress" /></span></th>
								<th><span data-icon-after="_"><xsl:value-of select="$i18n.ActivityProgress.completed" /></span></th>
								<th class="no-sort"></th>
							</tr>
						</thead>
						<tbody>
							
							<xsl:choose>
								<xsl:when test="FlowInstances">
								
									<xsl:apply-templates select="FlowInstances/FlowInstance/ActivityGroups/ActivityGroup" mode="list-completed" />
									
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td class="icon" />
										<td colspan="3">
											<xsl:value-of select="$i18n.NoActivities" />
										</td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
							
						</tbody>
					</table>
					
					<div class="floatleft bigmargintop">
						<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" title="{$i18n.ShowPending}">
							<xsl:value-of select="$i18n.ShowPending"/>
						</a>
					</div>
					
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityGroup" mode="list-completed">
		
		<tr class="activitygroup">
			<td colspan="4">
				<h3>
					<xsl:value-of select="name" />
				</h3>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.FlowInstance.flowInstanceID" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="../../flowInstanceID"/>
				</div>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.Flow.name" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="../../Flow/name"/>
				</div>
				
				<div>
					<strong>
						<xsl:value-of select="$i18n.ActivityProgress.added" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="Progresses/ActivityProgress[1]/added"/>
				</div>
				
				<xsl:if test="ShortDescription">
					<div>
						<strong>
							<xsl:value-of select="$i18n.ActivityProgress.shortDescription" />
							<xsl:text>: </xsl:text>
						</strong>
						
						<xsl:value-of select="ShortDescription"/>
					</div>
				</xsl:if>
			</td>
		</tr>
		
		<xsl:apply-templates select="Progresses/ActivityProgress" mode="list-completed" />
		
	</xsl:template>
	
	<xsl:template match="ActivityProgress" mode="list-completed">
		
		<tr>
			<xsl:attribute name="class">
				<xsl:text>activity</xsl:text>
				<xsl:if test="position() = last()"> last</xsl:if>
			</xsl:attribute>
			
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{../../../../Flow/flowID}?{../../../../Flow/IconLastModified}" width="25" alt="" />
			</td>
			<td data-title="{$i18n.Activity}">
				<xsl:value-of select="Activity/name" />
			</td>
			<td data-title="{$i18n.ActivityProgress.completed}">
				<xsl:value-of select="completed" />
			</td>
			<td class="link">
				<a class="btn btn-green" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{activityProgressID}"><xsl:value-of select="$i18n.ShowActivity" /></a>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="ShowActivity">

		<div class="contentitem">
			<section>
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h1 class="xl nopadding">
							<xsl:value-of select="ActivityProgress/Activity/name" />
						</h1>
					</div>
					
					<form method="POST" action="{/Document/requestinfo/uri}" name="userform">
					
						<article class="clearfix">
						
							<div>
								<strong>
									<xsl:value-of select="$i18n.ActivityProgress.added" />
									<xsl:text>:&#160;</xsl:text>
								</strong>
								<xsl:value-of select="ActivityProgress/added" />
								
								<br/>
								
								<strong>
									<xsl:value-of select="$i18n.Flow.name" />
									<xsl:text>:&#160;</xsl:text>
								</strong>
								<xsl:value-of select="FlowInstance/Flow/name" />
								
								<br/>
								
								<strong>
									<xsl:value-of select="$i18n.FlowInstance.flowInstanceID" />
									<xsl:text>:&#160;</xsl:text>
								</strong>
								<xsl:value-of select="FlowInstance/flowInstanceID" />
								
								<xsl:if test="ShortDescription">
									<br/>
									
									<strong>
										<xsl:value-of select="$i18n.ActivityProgress.shortDescription" />
										<xsl:text>:&#160;</xsl:text>
									</strong>
									<xsl:value-of select="ShortDescription" />
								</xsl:if>
								
							</div>
							
							<xsl:if test="ActivityProgress/Activity/description">
								<div class="bigmarginbottom">
									
									<label>
										<xsl:value-of select="$i18n.Activity.description" />
									</label>
									
									<p style="margin-top: 0;">
										<xsl:call-template name="replaceLineBreak">
											<xsl:with-param name="string" select="ActivityProgress/Activity/description"/>
										</xsl:call-template>
									</p>
								</div>
							</xsl:if>
							
							<xsl:if test="ActivityProgress/Activity/showFlowInstance = 'true'">
								<div class="service">
									<div class="queries bigpaddingbottom">
										<xsl:apply-templates select="ManagerResponses/ManagerResponse"/>
									</div>
								</div>
							</xsl:if>
								
							<xsl:choose>
								<xsl:when test="ActivityProgress/Activity/ActivityGroup/useApproveDeny = 'true'">
									
									<div>
										
										<xsl:call-template name="createRadio">
											<xsl:with-param name="id" select="'approved'" />
											<xsl:with-param name="name" select="'completed'" />
											<xsl:with-param name="value" select="'approved'" />
											<xsl:with-param name="checked">
												<xsl:if test="ActivityProgress/completed != '' and ActivityProgress/denied != 'true'">true</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="disabled">
												<xsl:if test="ActivityProgress/completed != ''">true</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<label for="approved">
											<xsl:attribute name="class">
												<xsl:text>radio</xsl:text>
												<xsl:if test="ActivityProgress/completed != '' and ActivityProgress/denied = 'true'"> disabled</xsl:if>
											</xsl:attribute>
											
											<xsl:choose>
												<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
													<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$i18n.ActivityProgress.approved" />
												</xsl:otherwise>
											</xsl:choose>
										</label>
										
									</div>
									
									<div>
										
										<xsl:call-template name="createRadio">
											<xsl:with-param name="id" select="'denied'" />
											<xsl:with-param name="name" select="'completed'" />
											<xsl:with-param name="value" select="'denied'" />
											<xsl:with-param name="checked">
												<xsl:if test="ActivityProgress/completed != '' and ActivityProgress/denied = 'true'">true</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="disabled">
												<xsl:if test="ActivityProgress/completed != ''">true</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<label for="denied">
											<xsl:attribute name="class">
												<xsl:text>radio</xsl:text>
												<xsl:if test="ActivityProgress/completed != '' and ActivityProgress/denied != 'true'"> disabled</xsl:if>
											</xsl:attribute>
											
											<xsl:choose>
											<xsl:when test="ActivityProgress/Activity/ActivityGroup/deniedText">
												<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/deniedText" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$i18n.ActivityProgress.denied" />
											</xsl:otherwise>
										</xsl:choose>
										</label>
										
									</div>
									
								</xsl:when>
								<xsl:otherwise>
									
									<div>
										
										<xsl:call-template name="createCheckbox">
											<xsl:with-param name="id" select="'completed'" />
											<xsl:with-param name="name" select="'completed'" />
											<xsl:with-param name="value" select="'true'" />
											<xsl:with-param name="checked">
												<xsl:if test="ActivityProgress/completed != ''">true</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="disabled">
												<xsl:if test="ActivityProgress/completed != ''">true</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<label for="completed">
											<xsl:attribute name="class">
												<xsl:text>checkbox</xsl:text>
												<xsl:if test="ActivityProgress/completed != ''"> disabled</xsl:if>
											</xsl:attribute>
											
											<xsl:choose>
												<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
													<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$i18n.ActivityProgress.complete" />
												</xsl:otherwise>
											</xsl:choose>
										</label>
										
									</div>
								
								</xsl:otherwise>
							
							</xsl:choose>
							
							<div class="bigmarginbottom">
								
								<label for="comment">
									<xsl:value-of select="$i18n.ActivityProgress.comment" />
								</label>
								
								<div>
									<xsl:call-template name="createTextArea">
										<xsl:with-param name="id" select="'comment'" />
										<xsl:with-param name="name" select="'comment'" />
										<xsl:with-param name="element" select="ActivityProgress" />
										<xsl:with-param name="rows" select="'4'" />
										<xsl:with-param name="disabled">
											<xsl:if test="ActivityProgress/completed != ''">true</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							
							</div>
							
						</article>
						
						<div class="divider" />
						
						<article class="buttons">
						
							<xsl:if test="not(ActivityProgress/completed != '')">
								<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline" />
							</xsl:if>
							
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline"><xsl:value-of select="$i18n.Back" /></a>
						</article>
						
					</form>
			
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ManagerResponse">

		<xsl:variable name="stepID" select="currentStepID"/>
	
		<div class="section-full preview" style="padding-left: 0; padding-right: 0;">
			
			<h2 data-icon-before="c" class="h1">
				<xsl:value-of select="currentStepIndex + 1"/>
				<xsl:text>. </xsl:text>
				<xsl:value-of select="../../FlowInstance/Flow/Steps/Step[stepID = $stepID]/name"/>
			</h2>
			
			<xsl:choose>
				<xsl:when test="QueryResponses">
					<xsl:apply-templates select="QueryResponses/QueryResponse"/>
				</xsl:when>
				<xsl:otherwise>
					<p><xsl:value-of select="$i18n.noAnsweredQueriesInThisStep"/></p>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	
		<xsl:if test="position() != last()">
			<div class="divider preview" />
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="QueryResponse">
	
		<xsl:choose>
			<xsl:when test="HTML">
				<xsl:value-of select="HTML" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:otherwise>
				<div id="query_{QueryDescriptor/queryID}" class="hidden" />
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="createHelpDialog">
		
		<xsl:param name="id" />
		<xsl:param name="text" />
		<xsl:param name="class" select="''" />
		
		<div class="help {$class}">
			<a class="open-help" href="#" data-icon-after="?" data-help-box="helpdialog_{$id}"><span><xsl:value-of select="$i18n.Help" /></span></a>
			<div class="help-box" data-help-box="helpdialog_{$id}">
				<div>
		  			<div> 
		  				<a class="close" href="#" data-icon-after="x"></a>
		  				<xsl:copy-of select="$text" />
		  			</div> 
				</div>
			</div>
		</div>
		
		<div class="help-backdrop" data-help-box="helpdialog_{$id}" />
		
	</xsl:template>
	
</xsl:stylesheet>