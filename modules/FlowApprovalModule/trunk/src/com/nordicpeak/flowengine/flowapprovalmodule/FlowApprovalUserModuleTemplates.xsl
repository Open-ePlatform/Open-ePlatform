<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
		/tablesorter/js/jquery.tablesorter.min.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/flowengine.helpdialog.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
		/css/flowapprovaluser.css
	</xsl:variable>

	<xsl:template match="Document">
			
			<xsl:apply-templates select="ListPendingActivities"/>
			<xsl:apply-templates select="ListCompletedActivities"/>
			<xsl:apply-templates select="ShowActivity"/>
			<xsl:apply-templates select="Signing"/>
			
	</xsl:template>
	
	<xsl:template match="ListPendingActivities">
		
		<xsl:apply-templates select="validationError"/>
		<xsl:apply-templates select="ValidationErrors/validationError"/>
		
		<div id="flow-approval" class="contentitem">
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
								
									<xsl:apply-templates select="FlowInstances/FlowInstance/ActivityGroups/ActivityGroup" mode="list" >
										<xsl:with-param name="showCompleted" select="'false'"/>
									</xsl:apply-templates>
									
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
					
					<div class="floatright bigmargintop">
						<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/listcompleted" title="{$i18n.ShowCompleted}">
							<xsl:value-of select="$i18n.ShowCompleted"/>
						</a>
					</div>
					
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ListCompletedActivities">
		
		<xsl:apply-templates select="validationError"/>
		
		<div id="flow-approval" class="contentitem">
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
								
									<xsl:apply-templates select="FlowInstances/FlowInstance/ActivityGroups/ActivityGroup" mode="list" >
										<xsl:with-param name="showCompleted" select="'true'"/>
									</xsl:apply-templates>
									
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
					
					<div class="floatright bigmargintop">
						<a class="btn btn-light btn-inline margintop" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" title="{$i18n.ShowPending}">
							<xsl:value-of select="$i18n.ShowPending"/>
						</a>
					</div>
					
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityGroup" mode="list">
		<xsl:param name="showCompleted"/>
		
		<tr class="activitygroup">
			<td colspan="2">
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
						<xsl:value-of select="$i18n.ActivityRound.added" />
						<xsl:text>: </xsl:text>
					</strong>
					
					<xsl:value-of select="ActivityRounds/ActivityRound[1]/added" />
				</div>
				
				<xsl:if test="ActivityRounds/ActivityRound[1]/cancelled">
					<div>
						<strong>
							<xsl:value-of select="$i18n.ActivityRound.cancelled" />
							<xsl:text>: </xsl:text>
						</strong>
						
						<xsl:value-of select="ActivityRounds/ActivityRound[1]/cancelled" />
					</div>
				</xsl:if>
				
			</td>
			
			<xsl:if test="$showCompleted = 'true'">
				<td data-title="{$i18n.ActivityProgress.completed}">
					<xsl:choose>
						<xsl:when test="ActivityRounds/ActivityRound[1]/completed">
							<div>
								<xsl:value-of select="ActivityRounds/ActivityRound[1]/completed" />
							</div>
						</xsl:when>
						<xsl:otherwise>&#160;</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<td/>
		</tr>
		
		<xsl:apply-templates select="ActivityRounds/ActivityRound" mode="list" >
			<xsl:with-param name="showCompleted" select="$showCompleted"/>
		</xsl:apply-templates>
		
	</xsl:template>
	
	<xsl:template match="ActivityRound" mode="list">
		<xsl:param name="showApproveDeny"/>
		<xsl:param name="showCompleted"/>
		
		<xsl:if test="position() > 1">
			<tr class="activityround">
				<td colspan="2">
					<div>
						<strong>
							<xsl:value-of select="$i18n.ActivityRound.added" />
							<xsl:text>: </xsl:text>
						</strong>
						
						<xsl:value-of select="added" />
					</div>
					
					<xsl:if test="cancelled">
						<div>
							<strong>
								<xsl:value-of select="$i18n.ActivityRound.cancelled" />
								<xsl:text>: </xsl:text>
							</strong>
							
							<xsl:value-of select="cancelled" />
						</div>
					</xsl:if>
					
				</td>
				<xsl:if test="$showCompleted = 'true'">
					<td data-title="{$i18n.ActivityProgress.completed}">
						<xsl:choose>
							<xsl:when test="completed">
								<div>
									<xsl:value-of select="completed" />
								</div>
							</xsl:when>
							<xsl:otherwise>&#160;</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:if>
				<td/>
			</tr>
		</xsl:if>
		
		<xsl:apply-templates select="ActivityProgresses/ActivityProgress" mode="list" >
			<xsl:with-param name="showCompleted" select="$showCompleted"/>
		</xsl:apply-templates>
		
	</xsl:template>
	
	<xsl:template match="ActivityProgress" mode="list">
		<xsl:param name="showCompleted"/>
		
		<tr class="activity">
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{../../../../../../Flow/flowID}?{../../../../../../Flow/IconLastModified}" width="25" alt="" />
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
			<xsl:if test="$showCompleted = 'true'">
				<td data-title="{$i18n.ActivityProgress.completed}">
					<xsl:choose>
						<xsl:when test="completed">
							<div>
								<xsl:value-of select="completed" />
							</div>
						</xsl:when>
						<xsl:otherwise>&#160;</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
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
					
					<xsl:variable name="disabled">
						<xsl:if test="ActivityProgress/completed or ActivityProgress/ActivityRound/completed or ActivityProgress/ActivityRound/cancelled">true</xsl:if>
					</xsl:variable>
					
					<div class="full bigmarginbottom">
						<xsl:apply-templates select="ValidationErrors/validationError" />
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
								
								<xsl:if test="$disabled = 'true'">
									
									<strong>
										<xsl:value-of select="$i18n.ActivityProgress.completed" />
										<xsl:text>:&#160;</xsl:text>
									</strong>
									<xsl:value-of select="ActivityProgress/completed" />
									
									<br/>

									<strong>
										<xsl:value-of select="$i18n.ActivityProgress.result" />
										<xsl:text>:&#160;</xsl:text>
									</strong>
								
									<xsl:choose>
										<xsl:when test="ActivityProgress/Activity/ActivityGroup/useApproveDeny = 'true' and ActivityProgress/completed">
	
											<xsl:choose>
												<xsl:when test="ActivityProgress/denied = 'true'">
													<xsl:choose>
														<xsl:when test="ActivityProgress/Activity/ActivityGroup/deniedText">
															<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/deniedText" />
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="$i18n.ActivityProgress.denied" />
														</xsl:otherwise>
													</xsl:choose>
												</xsl:when>
												<xsl:otherwise>
													<xsl:choose>
														<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
															<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="$i18n.ActivityProgress.approved" />
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
									
										<xsl:when test="ActivityProgress/Activity/ActivityGroup/useApproveDeny = 'false' and ActivityProgress/completed">
											<xsl:choose>
												<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
													<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$i18n.ActivityProgress.approved" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>

									</xsl:choose>
									
									<br/>
									
								</xsl:if>
								
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
										<xsl:value-of select="$i18n.Activity.description"/>
									</label>
									
									<p style="margin-top: 0;">
									
										<xsl:call-template name="replaceLineBreaksAndLinks">										
											<xsl:with-param name="string" select="ActivityProgress/Activity/description" />
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
							
							<xsl:if test="ActivityProgress/completed and ActivityProgress/signedDate">
								<div>
									<strong>
										<xsl:value-of select="$i18n.ActivityProgress.signedDate" />
										<xsl:text>:&#160;</xsl:text>
									</strong>
									<xsl:value-of select="ActivityProgress/signedDate" />
									
									<br/>
								</div>
							</xsl:if>
							
							<div class="bigmarginbottom">
								
								<label for="comment">
								
									<xsl:if test="ActivityProgress/Activity/requireComment = 'true' and ActivityProgress/Activity/whenToComment = 'ALWAYS'">
										<xsl:attribute name="class">required</xsl:attribute>
									</xsl:if>
									
									<xsl:value-of select="$i18n.ActivityProgress.comment" />
									
								</label>
								
								<div>
									<xsl:call-template name="createTextArea">
										<xsl:with-param name="id" select="'comment'" />
										<xsl:with-param name="name" select="'comment'" />
										<xsl:with-param name="element" select="ActivityProgress" />
										<xsl:with-param name="rows" select="'4'" />
										<xsl:with-param name="disabled" select="$disabled" />
									</xsl:call-template>
								</div>
							
							</div>
							
						</article>
						
						<div class="divider" />
						


						<xsl:variable name="approvedText">
							<xsl:choose>
								<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
									<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.ActivityProgress.approvebutton" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>

						<xsl:variable name="deniedTex">
							<xsl:choose>
								<xsl:when test="ActivityProgress/Activity/ActivityGroup/deniedText">
									<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/deniedText" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.ActivityProgress.deniebutton" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>

						<xsl:variable name="completedText">
							<xsl:choose>
								<xsl:when test="ActivityProgress/Activity/ActivityGroup/approvedText">
									<xsl:value-of select="ActivityProgress/Activity/ActivityGroup/approvedText" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.ActivityProgress.completebutton" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						
						<xsl:variable name="class">
							<xsl:if test="$disabled != 'true'">
								<xsl:if test="string-length($approvedText) + string-length($deniedTex) > 80">block</xsl:if>
							</xsl:if>
						</xsl:variable>

						<article class="buttons flexright {$class}">
													
							<xsl:if test="$disabled != 'true'">
							
								<xsl:choose>
									<xsl:when test="ActivityProgress/Activity/ActivityGroup/useApproveDeny = 'true'">
										
										<div class="">
											<input type="submit" value="{$approvedText}" name="approved" class="btn btn-green btn-inline" title="Är du säker på att du vill godkänna denna aktivitet?" onclick="return confirm('{$i18n.ActivityProgress.confirm.approve}');" />
										</div>
										
										<div class="">
											<input type="submit" value="{$deniedTex}" name="denied" class="btn btn-red btn-inline" title="Är du säker på att du vill avslå denna aktivitet?" onclick="return confirm('{$i18n.ActivityProgress.confirm.denie}');" />
										</div>
										
									</xsl:when>
									<xsl:otherwise>
										
										<div class="">
											<input type="submit" value="{$completedText}" name="completed" class="btn btn-green btn-inline" title="Är du säker på att du vill godkänna denna aktivitet?" onclick="return confirm('{$i18n.ActivityProgress.confirm.complete}');" />
										</div>
									
									</xsl:otherwise>
								
								</xsl:choose>
							</xsl:if>
						
							<div>
								<xsl:choose>
									<xsl:when test="$disabled = 'true'">
										<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/listcompleted" class="btn btn-light btn-inline"><xsl:value-of select="$i18n.Back" /></a>
									</xsl:when>
									<xsl:otherwise>
										<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline"><xsl:value-of select="$i18n.Back" /></a>
									</xsl:otherwise>
								</xsl:choose>
							</div>
							
						</article>
						
					</form>
			
				</div>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="Signing">
	
		<div class="contentitem">
	
			<h1 class="bigmarginbottom">
				<xsl:value-of select="$i18n.SignActivity"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="ActivityProgress/Activity/name"/>
			</h1>
			
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
					
				</div>
			</article>

			<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
			
			<div class="navigator-buttons">
			
				<a class="btn btn-light prev arrow-mobile" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{ActivityProgress/activityProgressID}">
					
					<span class="only-mobile">
						<xsl:value-of select="$i18n.SignActivity.Back"/>
					</span>
					
					<span class="hide-mobile">
						<xsl:value-of select="$i18n.SignActivity.Back"/>
					</span>
				</a>
			</div>
	
		</div>
		
	</xsl:template>
	
	<xsl:template match="ManagerResponse">

		<xsl:variable name="stepID" select="currentStepID"/>
			
		<div class="section-full preview" style="padding-left: 0; padding-right: 0;">
			
			<h2 data-icon-before="c" class="h1">
				<xsl:value-of select="currentStepIndex + 1"/>
				<xsl:text>. </xsl:text>
				<xsl:value-of select="../../FlowInstance/Flow/Steps/Step[stepID = $stepID]/name"/>
				
				<xsl:if test="../../ActivityProgress/Activity/pdfDownloadActivation='true'">
					<xsl:if test="position() = 1">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/downloadpdf/{../../ActivityProgress/activityProgressID}" title="{$i18n.DownloadPDF.title}" class="btn btn-light btn-inline btn-right"><xsl:value-of select="$i18n.DownloadPDF" /></a>
					</xsl:if>
				</xsl:if>
				
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
	
	<xsl:template match="validationError[messageKey='FlowDisabled']">
	
		<section class="modal error">
			<span data-icon-before="!">
				<xsl:value-of select="$i18n.FlowDisabled" />
			</span>
			<i class="icon close">x</i>
		</section>
		
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
					<xsl:when test="fieldName = 'comment'">
						<xsl:value-of select="$i18n.ActivityProgress.comment"/>
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