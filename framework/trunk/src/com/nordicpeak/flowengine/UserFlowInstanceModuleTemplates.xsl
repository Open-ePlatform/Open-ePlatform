<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
		/jquery/plugins/jquery.qloader.js
		/js/confirmpost.js
		/tablesorter/js/jquery.tablesorter.min.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.blockui.js
		/js/flowengine.helpdialog.js
		/js/flowengine.js?v=1
		/js/flowengine.tablesorter.js
		/js/userflowinstancemodule.js?v=1
		/js/jquery.qloader-init.js
		/js/js.cookie-2.0.4.min.js
		/js/messages.js?v=3
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css?v=1
		/css/messages.css
		/css/userflowinstancemodule.css
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="FlowBrowser" class="contentitem userflowinstances">
			
			<xsl:apply-templates select="ListFlowInstances" />
			<xsl:apply-templates select="ImmutableFlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerForm"/>
			<xsl:apply-templates select="FlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerSubmitted"/>
			<xsl:apply-templates select="ShowFlowInstanceOverview" />
			<xsl:apply-templates select="SigningForm"/>
			<xsl:apply-templates select="MultiSigningStatusForm"/>
			<xsl:apply-templates select="StandalonePaymentForm"/>
			<xsl:apply-templates select="InlinePaymentForm"/>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerForm">
	
		<xsl:if test="FlowInstance/Status/contentType != 'NEW'">
			<xsl:call-template name="showFlowInstanceControlPanel">
				<xsl:with-param name="flowInstance" select="FlowInstance" />
				<xsl:with-param name="view" select="'FLOWINSTANCE'" />
			</xsl:call-template>
		</xsl:if>
	
		<xsl:apply-imports/>
	
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerPreview">
	
		<xsl:if test="FlowInstance/Status/contentType != 'NEW'">
			<xsl:call-template name="showFlowInstanceControlPanel">
				<xsl:with-param name="flowInstance" select="FlowInstance" />
				<xsl:with-param name="view" select="'FLOWINSTANCE'" />
			</xsl:call-template>
		</xsl:if>
	
		<xsl:apply-imports/>
	
	</xsl:template>
	
	<xsl:template match="ListFlowInstances">
	
		<xsl:apply-templates select="validationError"/>
		
		<xsl:variable name="changedFlowInstances" select="SubmittedFlowInstances/FlowInstance[newEvents/FlowInstanceEvent]" />
			
		<xsl:if test="$changedFlowInstances">
		
			<section class="my-errands">
				<div class="heading-wrapper">
						<h2 class="h1"><xsl:value-of select="$i18n.MyMessages" /></h2>
					</div>
				<div class="errands-wrapper messages">
						<table class="oep-table errand-table">
							<thead>
								<tr>
									<th class="icon"></th>
									<th class="errando">
										<span>
											<xsl:value-of select="$i18n.FlowInstanceID"/>
										</span>
									</th>
									
									<th class="status">
										<span>
											<xsl:value-of select="$i18n.Message"/>
										</span>
									</th>
									<th class="link"></th>
								</tr>
							</thead>
							<tbody>
								<xsl:apply-templates select="$changedFlowInstances" mode="changed" />
							</tbody>
						</table>
					</div>
			</section>
		</xsl:if>
		
		<section class="my-errands">

			<xsl:if test="ShowMyErrandsInformationBlock">
				<div class="errands-wrapper">
					<xsl:value-of select="MyErrandsInformation" disable-output-escaping="yes" />
				</div>
			</xsl:if>
		
			<xsl:for-each select="ViewFragment">
				<xsl:value-of select="HTML" disable-output-escaping="yes"/>
			</xsl:for-each>
			
			<xsl:if test="SavedFlowInstances/FlowInstance">

				<xsl:variable name="flowInstanceCount" select="count(SavedFlowInstances/FlowInstance)" />

				<div class="errands-wrapper draft">
					<div class="heading-wrapper">
						<h2><xsl:value-of select="$i18n.SavedFlowInstancesTitle" /></h2>
						<h3 class="clearboth"><xsl:value-of select="$i18n.SavedFlowInstances.Part1" />
						<xsl:text>&#160;</xsl:text>
						<strong>
							<xsl:value-of select="$flowInstanceCount" />
							<xsl:text>&#160;</xsl:text>
							<xsl:choose>
								<xsl:when test="$flowInstanceCount > 1">
									<xsl:value-of select="$i18n.SavedFlowInstances.Part2.Plural" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.SavedFlowInstances.Part2" />
								</xsl:otherwise>
							</xsl:choose>
						</strong>
						<xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.SavedFlowInstances.Part3" /></h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'saved'" />
							<xsl:with-param name="text" select="$i18n.SavedFlowInstancesHelp" />
						</xsl:call-template>
					</div>
					<table class="oep-table errand-table">
						<thead>
							<tr>
								<th class="icon"></th>
								<th class="service"><span><xsl:value-of select="$i18n.FlowName" /></span></th>
								
								<xsl:if test="SiteProfiles">
									<th class="status"><span><xsl:value-of select="$i18n.SiteProfile" /></span></th>
								</xsl:if>
								
								<xsl:if test="ShowDescriptionColumn">
									<th class="errando">
										<span>
											<xsl:value-of select="$i18n.Description"/>
										</span>
									</th>
								</xsl:if>
								
								<th class="status"><span><xsl:value-of select="$i18n.Status" /></span></th>
								<th class="date"><span><xsl:value-of select="$i18n.Updated" /></span></th>
								<th class="link"></th>
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="SavedFlowInstances/FlowInstance" mode="saved" />
						</tbody>
					</table>
				</div>
			
				<div class="divider errands"></div>
			
			</xsl:if>

			<xsl:variable name="sortOrder">
				<xsl:choose>
					<xsl:when test="FlowInstanceSortOrder = 'ASC'">
						ascending
					</xsl:when>
					<xsl:otherwise>
						descending
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<div class="errands-wrapper">
				<div class="heading-wrapper">
					<h2><xsl:value-of select="$i18n.SubmittedFlowInstances" /></h2>
					<xsl:call-template name="createHelpDialog">
						<xsl:with-param name="id" select="'submitted'" />
						<xsl:with-param name="text" select="$i18n.SubmittedFlowInstancesHelp" />
					</xsl:call-template>
				</div>
				<table class="oep-table errand-table">
					<thead class="sortable">
						<tr>
							<th class="icon no-sort"></th>
							<th class="service"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowName" /></span></th>
							
							<xsl:if test="SiteProfiles">
								<th class="status"><span><xsl:value-of select="$i18n.SiteProfile" /></span></th>
							</xsl:if>
							
							<th class="errando"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowInstanceID" /></span></th>

							<xsl:if test="ShowDescriptionColumn">
								<th class="errando">
									<span>
										<xsl:value-of select="$i18n.Description"/>
									</span>
								</th>
							</xsl:if>

							<th class="status"><span data-icon-after="_"><xsl:value-of select="$i18n.Status" /></span></th>
							<th class="date default-sort {$sortOrder}"><span data-icon-after="_"><xsl:value-of select="$i18n.LastEvent" /></span></th>
							<th class="link no-sort"></th>
						</tr>
					</thead>
					<tbody>
						<xsl:choose>
							<xsl:when test="SubmittedFlowInstances/FlowInstance">
								<xsl:apply-templates select="SubmittedFlowInstances/FlowInstance" mode="submitted" />
							</xsl:when>
							<xsl:otherwise>
								<tr><td class="icon" /><td colspan="6"><xsl:value-of select="$i18n.NoSubmittedFlowInstances" /></td></tr>
							</xsl:otherwise>
						</xsl:choose>
					</tbody>
				</table>
			</div>
			
			<div class="divider errands"></div>
			
			<div class="errands-wrapper">
				<div class="heading-wrapper">
					<h2><xsl:value-of select="$i18n.ArchivedFlowInstances" /></h2>
					<xsl:call-template name="createHelpDialog">
						<xsl:with-param name="id" select="'archived'" />
						<xsl:with-param name="text" select="$i18n.ArchivedFlowInstancesHelp" />
					</xsl:call-template>
				</div>
				<table class="oep-table errand-table">
					<thead class="sortable">
						<tr>
							<th class="icon no-sort"></th>
							<th class="service"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowName" /></span></th>
							
							<xsl:if test="SiteProfiles">
								<th class="status"><span><xsl:value-of select="$i18n.SiteProfile" /></span></th>
							</xsl:if>
							
							<th class="errando"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowInstanceID" /></span></th>

							<xsl:if test="ShowDescriptionColumn">
								<th class="errando">
									<span>
										<xsl:value-of select="$i18n.Description"/>
									</span>
								</th>
							</xsl:if>	

							<th class="status"><span data-icon-after="_"><xsl:value-of select="$i18n.Status" /></span></th>
							<th class="date default-sort {$sortOrder}"><span data-icon-after="^"><xsl:value-of select="$i18n.Date" /></span></th>
							<th class="link no-sort"></th>
						</tr>
					</thead>
					<tbody>
						<xsl:choose>
							<xsl:when test="ArchivedFlowInstances/FlowInstance">
								<xsl:apply-templates select="ArchivedFlowInstances/FlowInstance" mode="archived" />
							</xsl:when>
							<xsl:otherwise>
								<tr><td class="icon" /><td colspan="6"><xsl:value-of select="$i18n.NoArchivedFlowInstances" /></td></tr>
							</xsl:otherwise>
						</xsl:choose>
					</tbody>
				</table>
			</div>
			
			
		</section>
		
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="saved">
	
		<tr>
			<td class="icon"><i data-icon-before="w"></i></td>
			<td data-title="{$i18n.FlowName}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>			
			
			<xsl:call-template name="printDescription"/>
			
			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.Updated}" class="date">
				<xsl:choose>
					<xsl:when test="updated"><xsl:value-of select="updated" /></xsl:when>
					<xsl:otherwise><xsl:value-of select="added" /></xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="link">
			
			<xsl:variable name="baseURL">
			
				<xsl:choose>
					<xsl:when test="Attributes/Attribute/Name = 'UserFlowInstanceModuleURL'">
					
						<xsl:value-of select="Attributes/Attribute[Name = 'UserFlowInstanceModuleURL']/Value"/>
						
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="/Document/requestinfo/currentURI"/>
						<xsl:text>/</xsl:text>
						<xsl:value-of select="/Document/module/alias"/>
					</xsl:otherwise>
				</xsl:choose>
			
			</xsl:variable>
			
				<xsl:choose>
					<xsl:when test="Status/contentType = 'WAITING_FOR_MULTISIGN'">
						<a class="btn btn-green vertical-align-middle" href="{$baseURL}/multisign/{flowInstanceID}"><xsl:value-of select="$i18n.WaitingForOthersSignatures" /></a>
					</xsl:when>
					<xsl:when test="Status/contentType = 'WAITING_FOR_PAYMENT'">
						<a class="btn btn-green vertical-align-middle" href="{$baseURL}/pay/{flowInstanceID}"><xsl:value-of select="$i18n.WaitingForPayment" /></a>
					</xsl:when>
					<xsl:when test="stopSubmitForSavedFlowIfUnpublished='true'">
						<a class="btn btn-light vertical-align-middle" href="#" onclick="alert('{$i18n.SavedFlow.DisabledMessage}'); return false;"><xsl:value-of select="$i18n.Continue.Disabled" /></a>
					</xsl:when>
					<xsl:when test="not(Flow/enabled = 'true')">
						<a class="btn btn-light vertical-align-middle" href="#" onclick="alert('{$i18n.Continue.DisabledMessage}'); return false;"><xsl:value-of select="$i18n.Continue.Disabled" /></a>
					</xsl:when>
					<xsl:otherwise>

						<a class="btn btn-green vertical-align-middle" href="{$baseURL}/flowinstance/{Flow/flowID}/{flowInstanceID}"><xsl:value-of select="$i18n.Continue" /></a>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:apply-templates select="ExtensionLink" mode="flow-list"/>
			
				<xsl:if test="Status/isUserDeletable = 'true'">
					<a class="btn btn-red vertical-align-middle" href="{$baseURL}/delete/{flowInstanceID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteFlowInstanceConfirm}: {Flow/name}"><xsl:value-of select="$i18n.Delete" /></a>
				</xsl:if>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="submitted">
		
		<tr>
			<xsl:if test="Flow/enabled = 'false'">
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
			<td class="icon">
				<xsl:call-template name="printFlowInstanceIcon" />
			</td>
			<td data-title="{$i18n.FlowName}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>
			
			<td data-title="{$i18n.FlowInstanceID}" class="errando">
				<xsl:if test="not(Flow/hideFlowInstanceIDFromUser = 'true')">
					<xsl:value-of select="flowInstanceID" />
					<xsl:call-template name="printExternalID"/>
				</xsl:if>
			</td>

			<xsl:call-template name="printDescription"/>

			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.LastEvent}" class="date nowrap">
				<xsl:value-of select="firstSubmitted" />
			</td>
			
			<td class="link">
				<xsl:apply-templates select="ExtensionLink" mode="flow-list"/>
				<xsl:call-template name="printFlowInstanceButton" />
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="archived">
		
		<tr>
			<xsl:if test="Flow/enabled = 'false'">
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
			<td class="icon">
				<xsl:call-template name="printFlowInstanceIcon" />
			</td>
			<td data-title="{$i18n.FlowName}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>
			
			<td data-title="{$i18n.FlowInstanceID}" class="errando">
				<xsl:if test="not(Flow/hideFlowInstanceIDFromUser = 'true')">
					<xsl:value-of select="flowInstanceID" />
					<xsl:call-template name="printExternalID"/>
				</xsl:if>
			</td>

			<xsl:call-template name="printDescription"/>

			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.Date}" class="date nowrap">
				<xsl:value-of select="firstSubmitted" />				
			</td>
			<td class="link">
				<xsl:apply-templates select="ExtensionLink" mode="flow-list"/>
				<xsl:call-template name="printFlowInstanceButton" />
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template name="printSiteProfile">
	
		<xsl:if test="../../SiteProfiles">
			
			<td data-title="{$i18n.SiteProfile}" class="siteProfile">
				
				<xsl:choose>
					<xsl:when test="Attributes/Attribute/Name = 'siteProfileName'">
					
						<xsl:value-of select="Attributes/Attribute[Name = 'siteProfileName']/Value"/>
					
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:variable name="profileID" select="profileID"/>
						
						<xsl:value-of select="../../SiteProfiles/Profile[profileID = $profileID]/name" />
					
					</xsl:otherwise>
				</xsl:choose>
			</td>
			
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="printExternalID">

		<xsl:if test="../../ShowExternalID or ../ShowExternalID">
		
			<xsl:variable name="externalID" select="Attributes/Attribute[Name = 'integrationExternalID']/Value"/>
			
			<xsl:if test="$externalID">
			
				<xsl:if test="flowInstanceID">
					<xsl:text> / </xsl:text>
				</xsl:if>
				
				<xsl:value-of select="$externalID"/>
			
			</xsl:if>
		
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="printDescription">
	
		<xsl:if test="../../ShowDescriptionColumn">
			
			<td data-title="{$i18n.Description}" class="description">
				
				<xsl:value-of select="userDescription"/>
				
			</td>
					
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="changed">
		
		<tr>
			<xsl:if test="Flow/enabled = 'false'">
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
			<td class="icon">
				<xsl:call-template name="printFlowInstanceIcon">
					<xsl:with-param name="iconClass" select="'icon-blue'" />
				</xsl:call-template>
			</td>
			<td data-title="{$i18n.FlowInstanceID}" class="errando">
				<xsl:if test="not(Flow/hideFlowInstanceIDFromUser = 'true')">
					<xsl:value-of select="flowInstanceID" />
				</xsl:if>
			</td>
			<td data-title="{$i18n.Status}" class="status">
				<xsl:for-each select="newEvents/FlowInstanceEvent">
					<xsl:value-of select="shortDate" /><xsl:text>&#160;-&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="statusDescription"><xsl:value-of select="statusDescription" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="status" /></xsl:otherwise>
					</xsl:choose>
					<br/>
				</xsl:for-each>
			</td>
			<td class="link">
				<xsl:call-template name="printFlowInstanceButton">
					<xsl:with-param name="buttonText" select="$i18n.ToFlowInstance" />
				</xsl:call-template>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="ShowFlowInstanceOverview">
	
		<xsl:apply-templates select="FlowInstance" mode="overview" />
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="overview">
			
		<section id="UserFlowInstanceModule">
			<div class="section-inside step full">
					<div class="heading-wrapper">
						<div class="inner inner-less-padding">
							<figure>
								<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{Flow/flowID}?{Flow/IconLastModified}" alt="" />
							</figure>
							<div class="heading">
								<h1 class="xl">
									<xsl:value-of select="Flow/name" />
									<b>
										<xsl:text>&#160;(</xsl:text>
										<xsl:value-of select="Status/name" />
										<xsl:text>)</xsl:text>
									</b>
								</h1>
							
							<xsl:if test="not(Flow/hideFlowInstanceIDFromUser = 'true')">
							
								<span class="errandno hide-mobile">
									<xsl:value-of select="$i18n.FlowInstanceID" />
									<xsl:text>:&#160;</xsl:text>
								</span>
							
								<span class="hide-mobile">
									<xsl:value-of select="flowInstanceID" />
									<xsl:call-template name="printExternalID"/>
								</span>
								
								<br/>
								
							</xsl:if>
							
							<xsl:if test="../ShowDescriptionColumn and userDescription">
								
								<span class="errandno hide-mobile">
									<xsl:value-of select="$i18n.Description" />
									<xsl:text>:&#160;</xsl:text>
								</span>
						
								<span class="hide-mobile">
									<xsl:value-of select="userDescription"/>
								</span>
										
								<br/>
								
							</xsl:if>
							
							<span class="errandno hide-mobile">
								<xsl:value-of select="$i18n.CurrentStatus" />
								<xsl:text>:&#160;</xsl:text>
							</span>
							
							<span class="hide-mobile">
								<xsl:value-of select="Status/name" />
							</span>
							
							<xsl:if test="Status/description">
								<span class="hide-mobile">
									<xsl:if test="string-length(Status/description) > 120">
										<xsl:attribute name="title">
											<xsl:value-of select="Status/description" />
										</xsl:attribute>
									</xsl:if>
									<xsl:call-template name="truncateWithEllipsis">
										<xsl:with-param name="text" select="Status/description" />
										<xsl:with-param name="maxLength" select="120" />
									</xsl:call-template>
								</span>
							</xsl:if>
						</div>
					</div>
					</div>
				</div>
				
				<div class="section-inside header-full no-pad-top">
					<div class="description">
						<xsl:variable name="submittedEvents" select="events/FlowInstanceEvent[eventType='SUBMITTED']" />
						
						<xsl:if test="not(Flow/hideFlowInstanceIDFromUser = 'true')">
							
							<p class="only-mobile">
								<strong class="overview">
									<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text>
								</strong>
								
								<xsl:value-of select="flowInstanceID" />
								<xsl:call-template name="printExternalID"/>
							</p>
							
						</xsl:if>
						
						<xsl:if test="../ShowDescriptionColumn and userDescription">
							
							<p class="only-mobile">	
								
								<strong class="overview">
									<xsl:value-of select="$i18n.Description" />
									<xsl:text>:&#160;</xsl:text>
								</strong>
								
								<xsl:value-of select="userDescription"/>	
							</p>
							
						</xsl:if>
						
						<xsl:if test="../Profile">
							
							<p>
								<strong class="overview"><xsl:value-of select="$i18n.SiteProfile" /><xsl:text>:&#160;</xsl:text></strong>
								<xsl:value-of select="../Profile/name" />
							</p>
							
						</xsl:if>
							
						<p>
							<strong class="overview">
								<xsl:value-of select="$i18n.FirstSubmitted" />
								<xsl:text>:&#160;</xsl:text>
							</strong>
							<xsl:value-of select="$submittedEvents[position() = 1]/added" />
								
							<xsl:if test="$submittedEvents[position() = 1]/poster/user/userID != /Document/user/userID">
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
								<xsl:call-template name="PrintPostedBy">
									<xsl:with-param name="poster" select="$submittedEvents[position() = 1]/poster/user"/>
									<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
								</xsl:call-template>
							</xsl:if>
						</p>
					
						<xsl:if test="count(owners/user) > 1">
							<p>
								<strong class="overview"><xsl:value-of select="$i18n.Owners" /><xsl:text>:&#160;</xsl:text></strong>
								<xsl:apply-templates select="owners/user" mode="owner" />
							</p>
						</xsl:if>
						
						<xsl:if test="count($submittedEvents) > 1">
							<p>
								<xsl:variable name="lastSubmit" select="$submittedEvents[position() = last()]" />
								
								<strong class="overview"><xsl:value-of select="$i18n.LastSubmitted" /><xsl:text>:&#160;</xsl:text></strong>
								<xsl:value-of select="$lastSubmit/added" /><xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
								<xsl:call-template name="printUser">
									<xsl:with-param name="user" select="$lastSubmit/poster/user" />
								</xsl:call-template>
							
							</p>
						</xsl:if>
						
						<xsl:if test="updated">
							  <p>
									<strong class="overview"><xsl:value-of select="$i18n.LastChanged" /><xsl:text>:&#160;</xsl:text></strong>
									<xsl:value-of select="updated" /><xsl:text>&#160;</xsl:text>
									
									<xsl:if test="Flow/hideManagerDetails = 'false'">
										<xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
										<xsl:call-template name="printUser">
											<xsl:with-param name="user" select="editor/user" />
											<xsl:with-param name="hideUsername" select="true()"/>
										</xsl:call-template>
									</xsl:if>
								</p>
						</xsl:if>
						
						<xsl:if test="Flow/hideManagerDetails = 'false'">
							<p>
								<strong class="overview"><xsl:value-of select="$i18n.Managers" /><xsl:text>:&#160;</xsl:text></strong>
								<xsl:choose>
									<xsl:when test="managers/user">
										<xsl:apply-templates select="managers/user" mode="manager" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.NoManager" />
									</xsl:otherwise>
								</xsl:choose>
							</p>
						</xsl:if>

					</div>
				</div>
				<!-- 
  			<div class="aside-inside header-full">
  				<div class="section noborder">
  					<div class="inner">
	  					Extra fields from flow here
  					</div>
  				</div>
  			</div>
			 -->
			<div id="tabs">
				<ul class="tabs">
					
					<li data-tabid="#showflow">
						<a data-icon-before="S" href="#showflow" title="{$i18n.ShowFlowInstance}"><xsl:value-of select="$i18n.ShowFlowInstance" /></a>
					</li>
					
					<li class="active" data-tabid="#history">
						<a data-icon-before="o" href="#history" title="{$i18n.FlowInstanceEvents}"><xsl:value-of select="$i18n.FlowInstanceEvents" /></a>
					</li>
					
					<xsl:if test="not(hideExternalMessages)">
						<li data-tabid="#messages">
							<a data-icon-before="m" href="#messages" title="{$i18n.ExternalMessages}">
								<xsl:value-of select="$i18n.ExternalMessages" />
								<xsl:text>&#160;(</xsl:text>
								<xsl:value-of select="count(externalMessages/ExternalMessage)"/>
								<xsl:text>)</xsl:text>
								
								<xsl:if test="false()">
									<span class="count">0</span>
								</xsl:if>
							</a>
						</li>
					</xsl:if>
					
					<xsl:apply-templates select="../TabHeaders/ExtensionLink" mode="tab-header"/>
				</ul>
				
				<div id="showflow" class="tabs-content">
					
					<div class="editbutton">
						<xsl:if test="Status/isUserDeletable = 'true'">
							<a class="btn btn-red next bigmarginright" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{flowInstanceID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteFlowInstanceConfirm}: {Flow/name}" ><i data-icon-before="x"/><xsl:value-of select="$i18n.Delete" /></a>
							</xsl:if>
						
					<xsl:if test="Status/isUserMutable = 'true'">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowinstance/{Flow/flowID}/{flowInstanceID}" class="btn btn-light next"><i data-icon-before="W"></i><xsl:value-of select="$i18n.UpdateFlowInstance" /></a>
						</xsl:if>
						
						</div>
					
					<xsl:apply-templates select="../ImmutableFlowInstanceManagerPreview" />
					
					<xsl:if test="PreviewError">
						<div class="heading-wrapper bigmargin"><h3><xsl:value-of select="$i18n.PreviewErrorMessage"/></h3></div>
					</xsl:if>
				
				</div>
				
				<xsl:if test="not(hideExternalMessages)">
					<div id="messages">
						
						<div id="new-message" class="tabs-content">
							
							<div class="heading-wrapper">
								<h2><xsl:value-of select="$i18n.NewMessage" /></h2>
								<a href="#" class="btn btn-light btn-right close_message"><xsl:value-of select="$i18n.Close" /><i data-icon-after="x"></i></a>
							</div>
							
							<form action="{/Document/requestinfo/uri}#messages" method="post" enctype="multipart/form-data">
							
								<label class="required" for="message"><xsl:value-of select="$i18n.Message" /></label>
								<xsl:apply-templates select="../validationError[fieldName = 'externalmessage']" />
								<textarea id="message" name="externalmessage" class="full" rows="10">
									<xsl:value-of select="../requestparameters/parameter[name='externalmessage']/value" />
								</textarea>
								
								<xsl:if test="not(Flow/hideExternalMessageAttachments = 'true')">
								
									<div class="heading-wrapper">
										<label><xsl:value-of select="$i18n.AttachFiles" /></label>
									</div>
									
									<script>
									imagePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
									deleteFile = '<xsl:value-of select="$i18n.DeleteFile" />';
								</script>
								
								<xsl:apply-templates select="../validationError[messageKey = 'FileSizeLimitExceeded' or messageKey = 'UnableToParseRequest' or messageKey = 'InvalidFileExtension']" />
								
								<div class="full">
									
									<div class="upload clearboth">
										<span class="btn btn-upload btn-blue">
											<xsl:value-of select="$i18n.ChooseFiles" />
											<input id="external-message" type="file" name="externalmessage-attachments" multiple="multiple" size="55" class="qloader externalmessages bigmarginbottom" />
										</span>
										<span><xsl:value-of select="$i18n.MaximumFileSize" />: <xsl:value-of select="../FormattedMaxFileSize" /></span>
										
										<xsl:if test="../AllowedExternalMessageFileExtensions">
											<span>
												<xsl:value-of select="$i18n.AllowedFilextentions" />
												<xsl:text>: </xsl:text>
												<xsl:apply-templates select="../AllowedExternalMessageFileExtensions/FileExtension"/>
											</span>
										</xsl:if>
									</div>
									
									<ul id="external-message-qloader-filelist" class="files" />
									
								</div>
								
							</xsl:if>
							
								
								<div class="floatright">
									<a href="#" class="btn btn-light btn-inline close_message marginright"><xsl:value-of select="$i18n.Cancel" /></a>
									<input type="submit" value="{$i18n.SubmitMessage}" name="addmessage" class="btn btn-green btn-inline" />
								</div>
								
								<div class="clearboth"/>
								
							</form>
							
						</div>
						
						<div class="tabs-content">
							
							<div class="heading-wrapper">
								<h2><xsl:value-of select="$i18n.ExternalMessages" /></h2>

							<xsl:if test="not(hideSendExternalMessage)">
									<a href="#" class="btn btn-blue btn-right open_message"><i data-icon-before="+"></i><xsl:value-of select="$i18n.NewMessage" /></a>
							</xsl:if>
							</div>
							
							<xsl:choose>
								<xsl:when test="externalMessages/ExternalMessage">
								
								<xsl:call-template name="sortMessagesButton" />
								
									<ul class="messages">
										<xsl:apply-templates select="externalMessages/ExternalMessage" mode="user" />
									</ul>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.NoExternalMessages" />
								</xsl:otherwise>
							</xsl:choose>
							
						</div>
						
					</div>
				</xsl:if>
				
				<div id="history" class="tabs-content nopadding" >
					
					<div class="errands-wrapper">
							
							<div class="heading-wrapper">
								<h2><xsl:value-of select="$i18n.FlowInstanceEvents" /></h2>
							</div>
							
							<table class="oep-table">
								<thead class="errand">
									<tr>
										<th></th>
										<th class="service active"><span><xsl:value-of select="$i18n.Action" /></span></th>
										
										<xsl:if test="Flow/hideManagerDetails = 'false'">
											<th class="details"><span><xsl:value-of select="$i18n.Details" /></span></th>
										</xsl:if>
										
										<th class="date"><span><xsl:value-of select="$i18n.Date" /></span></th>
										<th class="status"><span><xsl:value-of select="$i18n.Status" /></span></th>
										
										<xsl:if test="Flow/hideManagerDetails = 'false'">
											<th class="person"><span><xsl:value-of select="$i18n.Person" /></span></th>
										</xsl:if>
									</tr>
								</thead>
								<tbody>
									<xsl:choose>
										<xsl:when test="events/FlowInstanceEvent">
											<xsl:apply-templates select="events/FlowInstanceEvent" />
										</xsl:when>
										<xsl:when test="Flow/hideManagerDetails = 'true'">
											<tr><td class="icon" /><td colspan="3"><xsl:value-of select="$i18n.NoEvents" /></td></tr>
										</xsl:when>
										<xsl:otherwise>
											<tr><td class="icon" /><td colspan="5"><xsl:value-of select="$i18n.NoEvents" /></td></tr>
										</xsl:otherwise>
									</xsl:choose>
								</tbody>
							</table>
							
						</div>
						
					</div>
					
					<xsl:for-each select="../TabContents/ViewFragment">
						<xsl:value-of select="HTML" disable-output-escaping="yes"/>
					</xsl:for-each>
					
				</div>
			
		</section>
	
	</xsl:template>

	<xsl:template match="FileExtension">
	
		<xsl:value-of select="."/>
		
		<xsl:if test="position() != last()">
			<xsl:text>, </xsl:text>
		</xsl:if>
	
	</xsl:template>

	<xsl:template name="truncateWithEllipsis">
		<xsl:param name="text" />
		<xsl:param name="maxLength" />
		
		<xsl:choose>
			<xsl:when test="string-length($text) > $maxLength">
				<xsl:text> - </xsl:text>
				<xsl:value-of select="substring($text, 1, $maxLength)" />
				<xsl:text>...</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="$text" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="FlowInstanceEvent">
		
		<xsl:variable name="odd" select="(position() mod 2) != 0" />
		
		<tr>
			<xsl:if test="$odd">
				<xsl:attribute name="class">odd</xsl:attribute>
			</xsl:if>

			<td class="link img-link">
				<xsl:if test="Attributes/Attribute[Name='pdf']/Value = 'true'">
					<xsl:choose>
						<xsl:when test="eventType = 'SIGNED' or eventType = 'SIGNING_SKIPPED'">
							<a class="display-inline-block" style="width:16px;" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/pdf/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstanceSignPDF}">
								<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf_lock.png" />
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a class="display-inline-block" style="width:16px;" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/pdf/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstancePDF}">
								<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf.png" />
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				
				<xsl:if test="Attributes/Attribute[Name='xml']/Value = 'true' and not(../../../hideEventXMLFromUser)">
					<a class="display-inline-block" style="width:16px;" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/xml/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstanceXML}">
						<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/xml.png" />
					</a>
				</xsl:if>
				
				<xsl:if test="Attributes/Attribute[Name='signingData']">
					<a class="display-inline-block" style="width:16px;" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/signature/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadSignature}">
						<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/lock.png" />
					</a>
				</xsl:if>
			</td>

			<td data-title="{$i18n.Action}" class="service">
				<xsl:call-template name="getEventTypeText" />
			</td>
			
			<xsl:if test="../../Flow/hideManagerDetails = 'false'">
				<td data-title="{$i18n.Details}">
					<xsl:choose>
						<xsl:when test="Attributes/Attribute[Name='externalMessageID']">
							<xsl:variable name="messageID" select="Attributes/Attribute[Name='externalMessageID']/Value"></xsl:variable>
							<a class="messagelink" href="#messages-{$messageID}">
								<xsl:value-of select="Attributes/Attribute[Name='externalMessageFragment']/Value"></xsl:value-of>
							</a>
						</xsl:when>
						<xsl:when test="details"><xsl:value-of select="details" /></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<td data-title="{$i18n.Date}" class="date"><xsl:value-of select="added" /></td>
			<td data-title="{$i18n.Status}" class="status">
				<xsl:value-of select="status" />
				<xsl:if test="statusDescription">
					<xsl:call-template name="createHelpDialog">
						<xsl:with-param name="id" select="status" />
						<xsl:with-param name="class" select="'floatright'" />
						<xsl:with-param name="text" select="statusDescription" />
					</xsl:call-template>
				</xsl:if>
			</td>
			
			<xsl:if test="../../Flow/hideManagerDetails = 'false'">
				<td data-title="{$i18n.Person}" class="person">
					<xsl:call-template name="printUser">
						<xsl:with-param name="user" select="poster/user" />
						<xsl:with-param name="hideUsername" select="true()"/>
					</xsl:call-template>
				</td>
			</xsl:if>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="ExternalMessage" mode="user">
	
		<xsl:call-template name="createUserMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="hideManagerDetails" select="../../Flow/hideManagerDetails = 'true'" />
			<xsl:with-param name="hideUsername" select="true()"/>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="user" mode="manager">
		
		<xsl:if test="not(../../../HideManagerEmailInOverview)">
			<i title="{email}" data-icon-before="p" class="sender"></i>
		</xsl:if>
		<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" /> 
		
		<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
		
	</xsl:template>
	
	<xsl:template match="user" mode="owner">
		
		<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
		
		<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
			
	</xsl:template>
	
	<xsl:template name="printFlowInstanceIcon">
	
		<xsl:param name="iconClass" select="''" />
	
		<xsl:choose>
			<xsl:when test="Flow/enabled = 'false'">
				<xsl:attribute name="class">icon</xsl:attribute>
				<i data-icon-before="l" title="{i18n.NotEnabledTitle}"></i>
			</xsl:when>
			<xsl:when test="events/FlowInstanceEvent[eventType='MANAGER_MESSAGE_SENT']">
				<xsl:attribute name="class">icon</xsl:attribute>
				<i class="{$iconClass}" data-icon-before="M"></i>
			</xsl:when>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="printFlowInstanceButton">
	
		<xsl:param name="buttonText" select="$i18n.Choose" />
		<xsl:param name="buttonClass" select="'btn-green vertical-align-middle'" />
	
		<xsl:choose>
			
			<xsl:when test="Flow/enabled = 'false'">
				<xsl:value-of select="$i18n.NotEnabled" />
			</xsl:when>

			<xsl:when test="remote = 'true' and Attributes/Attribute/Name = 'RemoteFlowInstanceURL'">
			
				<a class="btn {$buttonClass}" href="{Attributes/Attribute[Name = 'RemoteFlowInstanceURL']/Value}">
					<xsl:value-of select="$buttonText" />
				</a>
			
			</xsl:when>

			<xsl:otherwise>

				<xsl:variable name="baseURL">
				
					<xsl:choose>
						<xsl:when test="remote = 'true' and Attributes/Attribute/Name = 'UserFlowInstanceModuleURL'">
						
							<xsl:value-of select="Attributes/Attribute[Name = 'UserFlowInstanceModuleURL']/Value"/>
							
						</xsl:when>
						
						<xsl:otherwise>
							<xsl:value-of select="/Document/requestinfo/currentURI"/>
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/Document/module/alias"/>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:variable>

				<a class="btn {$buttonClass}" href="{$baseURL}/overview/{Flow/flowID}/{flowInstanceID}">
					<xsl:value-of select="$buttonText" />
				</a>
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template match="ImmutableFlowInstanceManagerPreview">
		
		<xsl:call-template name="showFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'PREVIEW'" />
		</xsl:call-template>
		
		<section class="service child">
			
			<div class="section-inside step full">
				<div class="heading-wrapper">
					<div class="inner inner-less-padding">
						<figure>
							<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
						</figure>
						<div class="heading">
							<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /></h1>
							<span class="errandno">
								<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" />
								
								<xsl:variable name="submittedEvents" select="FlowInstance/events/FlowInstanceEvent[eventType='SUBMITTED']" />
								
								<b class="pipe">|</b>
								<xsl:value-of select="$i18n.FirstSubmitted" />
								<xsl:text>:&#160;</xsl:text>
								<xsl:value-of select="FlowInstance/firstSubmitted" />
								
								<xsl:if test="$submittedEvents[position() = 1]/poster/user/userID != /Document/user/userID">
									
									<xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
									
									<xsl:call-template name="PrintPostedBy">
										<xsl:with-param name="poster" select="$submittedEvents[position() = 1]/poster/user"/>
										<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
									</xsl:call-template>
									
								</xsl:if>
								
								<xsl:if test="count($submittedEvents) > 1">
									
									<xsl:variable name="lastSubmit" select="$submittedEvents[position() = last()]" />
										
									<b class="pipe">|</b>
									<xsl:value-of select="$i18n.LastSubmitted" /><xsl:text>:&#160;</xsl:text>
									<xsl:value-of select="$lastSubmit/added" />
									
									<xsl:if test="$lastSubmit/poster/user/userID != /Document/user/userID">
									
										<xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
										
										<xsl:call-template name="printUser">
											<xsl:with-param name="user" select="$lastSubmit/poster/user" />
										</xsl:call-template>
									
									</xsl:if>
									
								</xsl:if>
							</span>
						</div>
					</div>
				</div>
			</div>
			
			<div class="section-inside header-full">
				<div class="description">
					<h2><xsl:value-of select="$i18n.Description" /></h2>
					<xsl:value-of select="FlowInstance/Flow/shortDescription" disable-output-escaping="yes" />
				</div>
			</div>
			
			<div class="divider" />
				
			<div class="queries">
				<xsl:apply-templates select="ManagerResponses/ManagerResponse"/>
			</div>
				
			<div class="navigator-buttons centered">
				<xsl:if test="FlowInstance/Status/isUserMutable = 'true'">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowinstance/{FlowInstance/Flow/flowID}/{FlowInstance/flowInstanceID}" class="btn btn-light xl prev"><i data-icon-before="W" class="xl"></i><xsl:value-of select="$i18n.UpdateFlowInstance" /></a>
				</xsl:if>
			</div>
			
		</section>
	
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
	
	<xsl:template match="ExtensionLink" mode="tab-header">
		
		<li>
			<a href="{url}" title="{name}">

				<xsl:if test="icon">
					<xsl:text> </xsl:text>
					<img src="{icon}"/>
				</xsl:if>
				
				<xsl:value-of select="name"/>
			
			</a>
		</li>
	
	</xsl:template>
	
	<xsl:template match="ExtensionLink" mode="flow-list">
		
		<a href="{url}">
			<xsl:attribute name="class">
				<xsl:text>btn vertical-align-middle </xsl:text>
				<xsl:choose>
					<xsl:when test="icon = 'green'">btn-green</xsl:when>
					<xsl:when test="icon = 'red'">btn-red</xsl:when>
					<xsl:when test="icon = 'yellow'">btn-yellow</xsl:when>
					<xsl:when test="icon = 'light'">btn-light</xsl:when>
					<xsl:when test="icon = 'dark'">btn-dark</xsl:when>
					<xsl:otherwise>btn-green</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		
			<xsl:value-of select="name" />
		</a>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowInstancePreviewError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstancePreviewError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FileSizeLimitExceeded']">
	
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part1"/>
					<xsl:value-of select="filename"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part2"/>
					<xsl:value-of select="size"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part3"/>
					<xsl:value-of select="maxFileSize"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part4"/>
				</strong>
			</span>
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToParseRequest']">
	
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.validationError.UnableToParseRequest" />
				</strong>
			</span>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SavedFlowUnpublished']">
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.SavedFlow.DisabledMessage"></xsl:with-param>
		</xsl:call-template>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidFileExtension']">
	
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.InvalidFileExtension.part1"/>
					<xsl:value-of select="filename"/>
					<xsl:value-of select="$i18n.InvalidFileExtension.part2"/>
				</strong>
			</span>
		</div>
			
	</xsl:template>
	
</xsl:stylesheet>