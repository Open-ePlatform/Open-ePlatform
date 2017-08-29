<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/plugins/jquery.qloader.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/mentionable/bootstrap-typeahead.js
		/js/mentionable/mentionable.js
		/js/jquery.ui.touch.min.js
		/js/jquery.blockui.js
		/js/flowengine.helpdialog.js
		/js/flowengine.js
		/js/flowengine.step-navigator.js
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/flowinstanceadminmodule.js
		/js/jquery.qloader-init.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css
		/js/mentionable/mentionable.css
	</xsl:variable>

	<xsl:template match="Document">		
		
		<div id="FlowInstanceAdminModule" class="contentitem">
		
			<xsl:apply-templates select="OverviewElement" />
			<xsl:apply-templates select="ShowFlowInstanceOverview"/>
			<xsl:apply-templates select="ImmutableFlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerForm"/>
			<xsl:apply-templates select="FlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerSubmitted"/>		
			<xsl:apply-templates select="UpdateInstanceStatus"/>
			<xsl:apply-templates select="UpdateInstanceManagers"/>
			<xsl:apply-templates select="SigningForm"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerForm">
	
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'FLOWINSTANCE'" />
		</xsl:call-template>
	
		<section class="modal warning child modal-marginbottom">
			<xsl:value-of select="$i18n.FlowInstanceFormNotificationTitle.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="FlowInstance/Flow/name" />
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$i18n.FlowInstanceFormNotificationTitle.Part2" />
			<xsl:text>&#160;</xsl:text>
			<strong><xsl:value-of select="FlowInstance/flowInstanceID" /></strong>
			<i class="icon close">x</i>
		</section>
	
		<xsl:apply-imports/>
	
	</xsl:template>

	<xsl:template name="createFlowInstanceManagerFormHeader">
		
		<div class="section-full padtop">
			<div class="heading-wrapper">
				<figure>
					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
				</figure>
				<div class="heading">
					<xsl:if test="loggedIn">
						<a class="btn btn-green btn-right xl" id="save_errand" href="#" onclick="submitStep('save-close', event)"><xsl:value-of select="$i18n.SaveAndClose" /></a>
					</xsl:if>
					<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /></h1>
					<span class="errandno">
						<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" />
						<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
						<xsl:value-of select="$i18n.PostedBy" /><xsl:text>:&#160;</xsl:text>
						<xsl:call-template name="PrintPostedBy">
							<xsl:with-param name="poster" select="FlowInstance/events/FlowInstanceEvent[eventType='SUBMITTED'][position() = 1]/poster/user"/>
							<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
						</xsl:call-template>
						<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
						<xsl:value-of select="FlowInstance/firstSubmitted" />
					</span>
				</div>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerPreview">
	
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'FLOWINSTANCE'" />
		</xsl:call-template>

		<section class="modal warning child modal-marginbottom">
			<xsl:value-of select="$i18n.FlowInstanceFormNotificationTitle.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="FlowInstance/Flow/name" />
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$i18n.FlowInstanceFormNotificationTitle.Part2" />
			<xsl:text>&#160;</xsl:text>
			<strong><xsl:value-of select="FlowInstance/flowInstanceID" /></strong>
			<i class="icon close">x</i>
		</section>

		<xsl:apply-imports/>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewHeader">
	
		<div class="section-full padtop">
			<div class="heading-wrapper">
				<figure>
					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
				</figure>
				<div class="heading">
					<xsl:if test="loggedIn">
						<a class="btn btn-green btn-right xl" id="save_errand" href="#" onclick="submitStep('save-close', event)"><xsl:value-of select="$i18n.SaveAndClose" /></a>
					</xsl:if>
					<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /></h1>
					<span class="errandno">
						<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" />
						<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
						<xsl:value-of select="$i18n.PostedBy" /><xsl:text>:&#160;</xsl:text>
						<xsl:call-template name="PrintPostedBy">
							<xsl:with-param name="poster" select="FlowInstance/events/FlowInstanceEvent[eventType='SUBMITTED'][position() = 1]/poster/user"/>
							<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
						</xsl:call-template>
						<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
						<xsl:value-of select="FlowInstance/firstSubmitted" />
					</span>
				</div>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewSubmitButton">
	
		<a href="#" class="btn btn-green xl next" onclick="submitStep('save-close', event)"><xsl:value-of select="$i18n.SaveAndClose" /></a>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewSigningButton">
	
		<xsl:call-template name="createFlowInstanceManagerPreviewSubmitButton" />
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerFormSubmitButton">
	
		<a href="#" class="btn btn-green xl next" onclick="submitStep('save-close', event)"><xsl:value-of select="$i18n.SaveAndClose" /></a>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerFormSigningButton">
	
		<xsl:call-template name="createFlowInstanceManagerFormSubmitButton" />
	
	</xsl:template>
	
	<xsl:template name="createMobileSavePanel" />
	
	<xsl:template name="createSubmitStep" />
	
	<xsl:template match="OverviewElement">
		
		<section>
 				
			<xsl:variable name="prioritizedInstancesCount" select="count(PrioritizedInstances/FlowInstance)" />
			<xsl:variable name="userAssignedInstancesCount" select="count(UserAssignedInstances/FlowInstance)" />
			<xsl:variable name="bookmarkedInstancesCount" select="count(BookmarkedInstances/FlowInstance)" />
			<xsl:variable name="activeInstancesCount" select="count(ActiveInstances/FlowInstance)" />
			<xsl:variable name="unassignedInstancesCount" select="count(UnassignedInstances/FlowInstance)" />
 				
			<div class="section-full padtop pull-left">
				
				<div class="split-left">
					<div class="inner">
						<div class="heading-wrapper">
							<h2 class="h1"><xsl:value-of select="$i18n.Summary" /></h2>
						</div>
						<ul class="summary-buttons">
							<li class="emergency">
								<a href="#emergency"><span><xsl:value-of select="$prioritizedInstancesCount" /></span><xsl:value-of select="$i18n.Emergency" /></a>
							</li>
							<li class="owned">
								<a href="#owned"><span><xsl:value-of select="$userAssignedInstancesCount" /></span><xsl:value-of select="$i18n.Owned" /></a>
							</li>
							<li class="flagged">
								<a href="#flagged"><span><xsl:value-of select="$bookmarkedInstancesCount" /></span><xsl:value-of select="$i18n.Flagged" /></a>
							</li>
							<li>
								<a href="#active"><span><xsl:value-of select="$activeInstancesCount" /></span><xsl:value-of select="$i18n.Active" /></a>
							</li>
							<li>
								<a href="#unassigned"><span><xsl:value-of select="$unassignedInstancesCount" /></span><xsl:value-of select="$i18n.UnAssigned" /></a>
							</li>

						</ul>
					</div>
				</div>
				
				<script type="text/javascript">
					flowInstanceAdminURI = '<xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />';
					i18nChooseFlowInstance = '<xsl:value-of select="$i18n.Choose" />';
					i18nFlow = '<xsl:value-of select="$i18n.Flow" />';
					i18nFlowInstanceID = '<xsl:value-of select="$i18n.FlowInstanceID" />';
					i18nFlowInstanceStatus = '<xsl:value-of select="$i18n.Status" />';
					i18nFlowInstanceAdded = '<xsl:value-of select="$i18n.Date" />';
				</script>
				
				<div class="split-right search-wrapper">
					
					<xsl:variable name="extensionLiks" select="ExtensionLink[slot='top-right']"/>
					
					<xsl:if test="$extensionLiks">
					
						<div class="top-right-slot">
							<xsl:apply-templates select="$extensionLiks"/>
						</div>
					
					</xsl:if>
					
					<div class="inner">
						
						<h2 class="h1"><xsl:value-of select="$i18n.SearchFlowInstance" /></h2>
						
						<div class="search">
							<form action="javascript:void(0);" method="post">
							
								<span class="italic"><xsl:value-of select="$i18n.SearchFlowInstanceDescription" /></span>
								<div class="input-wrapper">
									<input type="text" placeholder="{$i18n.SearchFormTitle}" name="search" class="noborder" id="search" />
		
									<input type="button" value="s" class="btn btn-search" onclick="searchFlowInstance()" />
								</div>
								
							</form>
						</div>
						
					</div>
					
				</div>
				
			</div>
			
  			<div class="section-full search-results" style="display: none;">
  				
  				<div class="info">
  					<span class="message"><i>c</i><xsl:value-of select="$i18n.SearchDone" />.</span>
  					<span class="close"><a href="#"><xsl:value-of select="$i18n.close" /><xsl:text>&#160;</xsl:text><i>x</i></a></span>
  				</div>
  				
  				<h2 class="h1 search-results-title"><span class="title" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Hits.Part1" /><xsl:text>&#160;</xsl:text><span class="hits" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Hits.Part2" /></h2>
  				<div class="errands-wrapper" style="display: none">
  					<table class="oep-table flowinstance-table">
  						<thead>
  							<tr>
  								<th class="icon no-sort"></th>
  								<th class="service active"><span data-icon-after="_"><xsl:value-of select="$i18n.Flow" /></span></th>
  								<th class="errando"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowInstanceID" /></span></th>
  								<th class="status"><span data-icon-after="_"><xsl:value-of select="$i18n.Status" /></span></th>
  								<th class="date default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.Date" /></span></th>
  								<th class="link no-sort"></th>
  							</tr>
  						</thead>
  						<tbody />
  					</table>
  				</div>
  				
			</div>
 			
 			<xsl:apply-templates select="validationError"/>
 				
			<div class="section-full">
  				
  				<div id="tabs">
	  				
	  				<ul class="tabs pull-left">
	  					<li class="active"><a href="#emergency" data-icon-before="!"><xsl:value-of select="$i18n.EmergencyTab" /><span class="count"><xsl:value-of select="$prioritizedInstancesCount" /></span></a></li>
	  					<li><a href="#owned" data-icon-before="&#58894;"><xsl:value-of select="$i18n.OwnedTab" /></a></li>
	  					<li><a href="#flagged" data-icon-before="*"><xsl:value-of select="$i18n.FlaggedTab" /></a></li>
	  					<li><a href="#active" data-icon-before="o"><xsl:value-of select="$i18n.ActiveTab" /></a></li>
	  					<li><a href="#unassigned" data-icon-before="o"><xsl:value-of select="$i18n.UnAssignedTab" /><span class="count"><xsl:value-of select="$unassignedInstancesCount" /></span></a></li>
	  				</ul>
	  				
	  				<xsl:call-template name="PrioritizedInstances">
	  					<xsl:with-param name="instancesCount" select="$prioritizedInstancesCount" />
	  				</xsl:call-template>
	  				
	  				<xsl:call-template name="UserAssignedInstances">
	  					<xsl:with-param name="instancesCount" select="$userAssignedInstancesCount" />
	  				</xsl:call-template>
	  				
	  				<xsl:call-template name="BookmarkedInstances">
	  					<xsl:with-param name="instancesCount" select="$bookmarkedInstancesCount" />
	  				</xsl:call-template>
	  				
	  				<xsl:call-template name="ActiveInstances">
	  					<xsl:with-param name="instancesCount" select="$activeInstancesCount" />
	  				</xsl:call-template>
	  				
	  				<xsl:call-template name="UnassignedInstances">
	  					<xsl:with-param name="instancesCount" select="$unassignedInstancesCount" />
	  				</xsl:call-template>
	  				
	  			</div>
	  		</div>
  		</section>
	
	</xsl:template>
	
	<xsl:template name="PrioritizedInstances">
	
		<xsl:param name="instancesCount" />
	
		<div id="emergency">
	  					
			<div class="tabs-content official">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.PrioritizedInstancesDescription.Part1" /><xsl:text>&#160;</xsl:text>
							<strong><xsl:value-of select="$instancesCount" /></strong><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.PrioritizedInstancesDescription.Part2" />
						</h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'prioritized'" />
							<xsl:with-param name="text" select="$i18n.PrioritizedInstancesHelp" />
						</xsl:call-template>
					</div>
					
					<table class="oep-table flowinstance-table">
						<thead class="sortable">
							<tr>
								<th class="icon no-sort"></th>
								<th class="service"><span data-icon-after="_"><xsl:value-of select="$i18n.Flow" /></span></th>

								<xsl:if test="SiteProfiles">
									<th class="siteProfile"><span data-icon-after="_"><xsl:value-of select="$i18n.SiteProfile" /></span></th>
								</xsl:if>

								<th class="errando"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowInstanceID.short" /></span></th>
								
								<xsl:if test="ShowDescriptionColumn">
									<th class="errando">
										<span>
											<xsl:value-of select="$i18n.Description"/>
										</span>
									</th>
								</xsl:if>
								
								<th class="status"><span data-icon-after="_"><xsl:value-of select="$i18n.Status" /></span></th>
								<th class="date default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.Date" /></span></th>
								<th class="priority"><span data-icon-after="_"><xsl:value-of select="$i18n.Priority" /></span></th>
								<th class="link no-sort"></th>
							</tr>
						</thead>
						<tbody>
							<xsl:choose>
								<xsl:when test="PrioritizedInstances/FlowInstance">
									<xsl:apply-templates select="PrioritizedInstances/FlowInstance" mode="prioritized" />
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td />
										<td colspan="{6 + count(SiteProfiles) + count(ShowDescriptionColumn)}">
											<xsl:value-of select="$i18n.NoFlowInstances" />
										</td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
						</tbody>
					</table>
					
				</div>
				
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="UserAssignedInstances">
		
		<xsl:param name="instancesCount" />
	
		<div id="owned">
	  					
			<div class="tabs-content official">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.UserAssignedInstancesDescription.Part1" /><xsl:text>&#160;</xsl:text>
							<strong><xsl:value-of select="$instancesCount" /></strong><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.UserAssignedInstancesDescription.Part2" />
						</h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'assigned'" />
							<xsl:with-param name="text" select="$i18n.UserAssignedInstancesHelp" />
						</xsl:call-template>
					</div>
					
					<xsl:call-template name="createFlowInstanceList">
						<xsl:with-param name="flowInstances" select="UserAssignedInstances/FlowInstance" />
					</xsl:call-template>
					
				</div>
				
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="BookmarkedInstances">
	
		<xsl:param name="instancesCount" />
	
		<div id="flagged">
	  					
			<div class="tabs-content official">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.BookmarkedInstancesDescription.Part1" /><xsl:text>&#160;</xsl:text>
							<strong><xsl:value-of select="$instancesCount" /></strong><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.BookmarkedInstancesDescription.Part2" />
						</h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'bookmarked'" />
							<xsl:with-param name="text" select="$i18n.BookmarkedInstancesHelp" />
						</xsl:call-template>
					</div>
					
					<xsl:call-template name="createFlowInstanceList">
						<xsl:with-param name="flowInstances" select="BookmarkedInstances/FlowInstance" />
					</xsl:call-template>
					
				</div>
				
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="ActiveInstances">
	
		<xsl:param name="instancesCount" />
	
		<div id="active">
	  					
			<div class="tabs-content official">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.ActiveInstancesDescription.Part1" /><xsl:text>&#160;</xsl:text>
							<strong><xsl:value-of select="$instancesCount" /></strong><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.ActiveInstancesDescription.Part2" />
						</h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'active'" />
							<xsl:with-param name="text" select="$i18n.ActiveInstancesHelp" />
						</xsl:call-template>
					</div>
					
					<xsl:call-template name="createFlowInstanceList">
						<xsl:with-param name="flowInstances" select="ActiveInstances/FlowInstance" />
					</xsl:call-template>
					
				</div>
				
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="UnassignedInstances">
	
		<xsl:param name="instancesCount" />
	
		<div id="unassigned">
	  					
			<div class="tabs-content official">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.UnassignedInstancesDescription.Part1" /><xsl:text>&#160;</xsl:text>
							<strong><xsl:value-of select="$instancesCount" /></strong><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.UnassignedInstancesDescription.Part2" />
						</h3>
						<xsl:call-template name="createHelpDialog">
							<xsl:with-param name="id" select="'unassigned'" />
							<xsl:with-param name="text" select="$i18n.UnassignedInstancesHelp" />
						</xsl:call-template>
					</div>
					
					<xsl:call-template name="createFlowInstanceList">
						<xsl:with-param name="flowInstances" select="UnassignedInstances/FlowInstance" />
					</xsl:call-template>
					
				</div>
				
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="prioritized">
	
		<tr class="emergency" onclick="location.href='{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{flowInstanceID}'">
			<td class="icon"><i data-icon-before="!"></i></td>
			<td data-title="{$i18n.Flow}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>
			
			<td data-title="{$i18n.FlowInstanceID.short}" class="errandno">
				
				<xsl:value-of select="flowInstanceID" />
			
				<xsl:call-template name="printExternalID"/>
			</td>
			
			<xsl:call-template name="printDescription"/>
			
			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.Date}" class="date"><xsl:value-of select="lastStatusChange" /></td>
			<td data-title="{$i18n.Priority}" class="priority">
				<xsl:choose>
					<xsl:when test="priority = 'HIGH'"><xsl:value-of select="$i18n.High" /></xsl:when>
					<xsl:when test="priority = 'MEDIUM'"><xsl:value-of select="$i18n.Medium" /></xsl:when>
				</xsl:choose>
			</td>
			<td class="link"><a class="btn btn-dark btn-inline" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{flowInstanceID}"><xsl:value-of select="$i18n.Choose" /></a></td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="list">
	
		<tr onclick="location.href='{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{flowInstanceID}'">
			<td class="icon"><i data-icon-before="!"></i></td>
			<td data-title="{$i18n.Flow}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>
			
			<td data-title="{$i18n.FlowInstanceID.short}" class="errandno">
			
				<xsl:value-of select="flowInstanceID" />
			
				<xsl:call-template name="printExternalID"/>
				
			</td>
			
			<xsl:call-template name="printDescription"/>
			
			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.Date}" class="date"><xsl:value-of select="lastStatusChange" /></td>
			<td class="link"><a class="btn btn-dark btn-inline" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{flowInstanceID}"><xsl:value-of select="$i18n.Choose" /></a></td>
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
			
				<xsl:text> / </xsl:text>
				
				<xsl:value-of select="$externalID"/>
			
			</xsl:if>					
		
		</xsl:if>
	
	</xsl:template>		
	
	<xsl:template name="printDescription">
	
		<xsl:if test="../../ShowDescriptionColumn">
			
			<td data-title="{$i18n.Description}" class="description">
				
				<xsl:value-of select="Attributes/Attribute[Name = 'description']/Value"/>			
				
			</td>
					
		</xsl:if>	
	
	</xsl:template>	
	
	<xsl:template name="createFlowInstanceList">
		
		<xsl:param name="flowInstances" />
		
		<table class="oep-table flowinstance-table">
			<thead class="sortable">
				<tr>
					<th class="icon no-sort"></th>
					<th data-title="{$i18n.Flow}" class="service"><span data-icon-after="_"><xsl:value-of select="$i18n.Flow" /></span></th>
					
					<xsl:if test="SiteProfiles">
						<th class="siteProfile"><span data-icon-after="_"><xsl:value-of select="$i18n.SiteProfile" /></span></th>
					</xsl:if>						
					
					<th data-title="{$i18n.FlowInstanceID.short}" class="errando"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowInstanceID.short" /></span></th>
					
					<xsl:if test="ShowDescriptionColumn">
						<th class="errando">
							<span>
								<xsl:value-of select="$i18n.Description"/>
							</span>
						</th>
					</xsl:if>
					
					<th data-title="{$i18n.Status}" class="status"><span data-icon-after="_"><xsl:value-of select="$i18n.Status" /></span></th>
					<th data-title="{$i18n.Date}" class="date default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.Date" /></span></th>
					<th class="link no-sort"></th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="$flowInstances">
						<xsl:apply-templates select="$flowInstances" mode="list" />
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td />
							<td colspan="{5 + count(SiteProfiles) + count(ShowDescriptionColumn)}">
								<xsl:value-of select="$i18n.NoFlowInstances" />
							</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		
	</xsl:template>
	
	<xsl:template match="ShowFlowInstanceOverview">
	
		<xsl:apply-templates select="FlowInstance" mode="overview" />
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="overview">
		
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="." />
			<xsl:with-param name="view" select="'OVERVIEW'" />
			<xsl:with-param name="bookmarked" select="../Bookmarked" />
		</xsl:call-template>
		
		<section class="child">
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
							
							<span class="errandno hide-mobile">
								<xsl:value-of select="$i18n.FlowInstanceID" />
								<xsl:text>:&#160;</xsl:text>
							</span>
							
							<span class="hide-mobile">									
								<xsl:value-of select="flowInstanceID" />
								
								<xsl:call-template name="printExternalID"/>
							</span>
							
							<br/>
							
							<xsl:if test="../ShowDescriptionColumn">
								
								<xsl:variable name="description" select="Attributes/Attribute[Name = 'description']/Value"/>
								
									<xsl:if test="$description">
									
									<span class="errandno hide-mobile">
										
										<xsl:value-of select="$i18n.Description" />
										
										<xsl:text>:&#160;</xsl:text>
									</span>
							
									<span class="hide-mobile">	
										<xsl:value-of select="$description"/>	
									</span>
											
									<br/>							
								
								</xsl:if>
								
							</xsl:if>							
							
							<span class="errandno hide-mobile">
								<xsl:value-of select="$i18n.CurrentStatus" />
								<xsl:text>:&#160;</xsl:text>
							</span>
							
							<span class="hide-mobile">
								<xsl:value-of select="Status/name" />
							</span>
						</div>
					</div>
  				</div>
  			</div>
  			<div class="section-inside header-full no-pad-top">
  				<div class="description">
  					
  					<xsl:variable name="submittedEvents" select="events/FlowInstanceEvent[eventType='SUBMITTED']" />
  					
  					<p class="only-mobile">
  						<strong class="overview">
  							<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text>
  						</strong>
  						
  						<xsl:value-of select="flowInstanceID" />
  						
  						<xsl:call-template name="printExternalID"/>
  					</p>
  					
					<xsl:if test="../ShowDescriptionColumn">
						
						<xsl:variable name="description" select="Attributes/Attribute[Name = 'description']/Value"/>
						
							<xsl:if test="$description">
						
							<p class="only-mobile">	
								<strong class="overview">
									
									<xsl:value-of select="$i18n.Description" />
									
									<xsl:text>:&#160;</xsl:text>
								</strong>
						
								<xsl:value-of select="$description"/>	
							</p>
									
						</xsl:if>
						
					</xsl:if>
  					
  					<xsl:if test="../Profile">
  					
	  					<p>
	  						<strong class="overview"><xsl:value-of select="$i18n.SiteProfile" /><xsl:text>:&#160;</xsl:text></strong>
	  						<xsl:value-of select="../Profile/name" />
	  					</p>
  					
  					</xsl:if>
  					
						<p>
							<strong class="overview"><xsl:value-of select="$i18n.FirstSubmitted" /><xsl:text>:&#160;</xsl:text></strong>
							<xsl:value-of select="firstSubmitted" /><xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
							
							<xsl:call-template name="PrintPostedBy">
								<xsl:with-param name="poster" select="$submittedEvents[position() = 1]/poster/user"/>
								<xsl:with-param name="flowInstanceAttributes" select="Attributes"/>
							</xsl:call-template>
							
							<!-- TODO add social security number -->
							
		 					<xsl:if test="not(poster)">
							
								<span class="overview">
									<xsl:value-of select="$i18n.PosterWasNotLoggedIn" />
								</span>
							</xsl:if>
							
						</p>
						
						<xsl:if test="count(owners/user) > 1">
							<p>
								<strong class="overview"><xsl:value-of select="$i18n.Owners" /><xsl:text>:&#160;</xsl:text></strong>
								<xsl:apply-templates select="owners/user" mode="owner" />
							</p>
						</xsl:if>

						<!-- Show poster if different from submitter -->
  					
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
	  						<xsl:value-of select="$i18n.by" /><xsl:text>&#160;</xsl:text>
	  						
	  						<xsl:call-template name="printUser">
	  							<xsl:with-param name="user" select="editor/user" />
	  						</xsl:call-template>
	  					</p>
  					</xsl:if>
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
  				</div>
  			</div>
  			<div class="aside-inside header-full">
  				<div class="section noborder">
  					<div class="inner">
	  					<!-- Extra fields from flow here -->
  					</div>
  				</div>
  			</div>
			
			<div id="tabs">
  				<ul class="tabs">
  				
  					<li class="active" data-tabid="#history">
  						<a data-icon-before="o" href="#history"><xsl:value-of select="$i18n.FlowInstanceEvents" /></a>
  					</li>  				
  				
  					<xsl:if test="poster">
	  					<li data-tabid="#messages">
	  						<a data-icon-before="m" href="#messages">
	  							<xsl:value-of select="$i18n.ExternalMessages" />
	  							<xsl:text>&#160;(</xsl:text>
	  							<xsl:value-of select="count(externalMessages/ExternalMessage)"/>
	  							<xsl:text>)</xsl:text>
	  							
	  							<!-- TODO count how many unread messages since last login -->
	  							<xsl:if test="false()">
	  								<span class="count">0</span>
	  							</xsl:if>
	  						</a>
	  					</li>
  					</xsl:if>
  					
  					<xsl:if test="not(Flow/hideInternalMessages = 'true')">
	  					<li class="notes" data-tabid="#notes">
	  						<a data-icon-before="i" href="#notes"><xsl:value-of select="$i18n.InternalMessages" /></a>
	  					</li>
  					</xsl:if>
  					
  					<xsl:apply-templates select="../TabHeaders/ExtensionLink" mode="tab-header"/>
  				</ul>
  				
  				<xsl:if test="poster">
	  				<div id="messages">
	  					
	  					<div id="new-message" class="tabs-content">
	  						
	  						<div class="heading-wrapper">
	  							<h2><xsl:value-of select="$i18n.NewMessage" /></h2>
	  							<a href="#" class="btn btn-light btn-right close_message"><xsl:value-of select="$i18n.Close" /><i data-icon-after="x"></i></a>
	  						</div>
	  						
	  						<form action="{/Document/requestinfo/uri}?externalmessage" method="post" enctype="multipart/form-data">
	  						
		  						<label class="required" for="message"><xsl:value-of select="$i18n.Message" /></label>
		  						<xsl:apply-templates select="../validationError[fieldName = 'externalmessage']" />
		  						<textarea id="message" name="externalmessage" class="full" data-requiredmessage="{$i18n.ValidationError.ExternalMessageRequired}"></textarea>
		  						<div class="heading-wrapper">
		  							<label><xsl:value-of select="$i18n.AttachFiles" /></label>
		  						</div>
		  						
		  						<script>
									imagePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
									deleteFile = '<xsl:value-of select="$i18n.DeleteFile" />';
								</script>				
								
								<xsl:apply-templates select="../validationError[messageKey = 'FileSizeLimitExceeded' or messageKey = 'UnableToParseRequest']" />
								
								<div class="full">
									
									<div class="upload clearboth">
										<span class="btn btn-upload btn-blue">
											<xsl:value-of select="$i18n.ChooseFiles" />
											<input id="external-message" type="file" name="attachments" multiple="multiple" size="55" class="qloader externalmessages bigmarginbottom" />
										</span>
										<span><xsl:value-of select="$i18n.MaximumFileSize" />: <xsl:value-of select="../FormatedMaxFileSize" /></span>
									</div>
									
									<ul id="external-message-qloader-filelist" class="files" />
									
								</div>
								
		  						<input type="submit" value="{$i18n.SubmitMessage}" name="externalmessage" class="btn btn-green btn-inline" />
		  						<a href="#" class="btn btn-light btn-inline close_message"><xsl:value-of select="$i18n.Cancel" /></a>
		  						
	  						</form>
	  						
	  					</div>
	  					
	  					<div class="tabs-content">
		  					
		  					<div class="heading-wrapper">
		  						<h2><xsl:value-of select="$i18n.ExternalMessages" /></h2>
		  						<a href="#" class="btn btn-blue btn-right open_message"><i data-icon-before="+"></i><xsl:value-of select="$i18n.NewMessage" /></a>
		  					</div>
		  					
		  					<xsl:choose>
		  						<xsl:when test="externalMessages/ExternalMessage">
		  							<ul class="messages">
		  								<xsl:apply-templates select="externalMessages/ExternalMessage" />
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
	  								<th class="icon" style="width: 32px;" />
	  								<th class="service active"><span><xsl:value-of select="$i18n.Action" /></span></th>
	  								<th class="details"><span><xsl:value-of select="$i18n.Details" /></span></th>
	  								<th class="date"><span><xsl:value-of select="$i18n.Date" /></span></th>
	  								<th class="status"><span><xsl:value-of select="$i18n.Status" /></span></th>
	  								<th class="person"><span><xsl:value-of select="$i18n.Person" /></span></th>
	  							</tr>
	  						</thead>
	  						<tbody>
	  							<xsl:choose>
	  								<xsl:when test="events/FlowInstanceEvent">
	  									<xsl:apply-templates select="events/FlowInstanceEvent" />
	  								</xsl:when>
	  								<xsl:otherwise>
	  									<tr><td /><td colspan="4"><xsl:value-of select="$i18n.NoEvents" /></td></tr>
	  								</xsl:otherwise>
	  							</xsl:choose>
	  						</tbody>
	  					</table>
	  					
	  				</div>
	  				
	  			</div>
	  			
	  			<xsl:if test="not(Flow/hideInternalMessages = 'true')">
		  			<div id="notes">
		  			
		  				<div id="new-note" class="tabs-content notes">
		  				
	  						<div class="heading-wrapper">
	  							<h2><xsl:value-of select="$i18n.NewInternalMessage" /></h2>
	  							<a href="#" class="btn btn-light btn-right close_message"><xsl:value-of select="$i18n.Close" /><i data-icon-after="x"></i></a>
	  						</div>
	  						
	  						<form action="{/Document/requestinfo/uri}?internalmessage" method="post" enctype="multipart/form-data">
	  						
		  						<label class="required" for="message"><xsl:value-of select="$i18n.InternalMessage" /></label>
		  						<xsl:apply-templates select="../validationError[fieldName = 'internalmessage']" />
		  						
		  						<textarea id="message" name="internalmessage" class="full mentionable" data-mentionableendpoint="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getmentionusers/{flowInstanceID}" data-requiredmessage="{$i18n.ValidationError.InternalMessageRequired}"></textarea>
		  						
		  						<div class="heading-wrapper">
		  							<label><xsl:value-of select="$i18n.AttachFiles" /></label>
		  						</div>
		  						
		  						<script>
									imagePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
									deleteFile = '<xsl:value-of select="$i18n.DeleteFile" />';
								</script>				
								
								<xsl:apply-templates select="../validationError[messageKey = 'FileSizeLimitExceeded' or messageKey = 'UnableToParseRequest']" />
								
								<div class="full">
									
									<div class="upload clearboth">
										<span class="btn btn-upload btn-blue">
											<xsl:value-of select="$i18n.ChooseFiles" />
											<input id="internal-message" type="file" name="attachments" multiple="multiple" size="55" class="qloader internalmessages bigmarginbottom" />
										</span>
										<span><xsl:value-of select="$i18n.MaximumFileSize" />: <xsl:value-of select="../FormatedMaxFileSize" /></span>
									</div>
									
									<ul id="internal-message-qloader-filelist" class="files" />
									
								</div>
								
		  						<input type="submit" value="{$i18n.SubmitInternalMessage}" name="internalmessage" class="btn btn-green btn-inline" />
		  						<a href="#" class="btn btn-light btn-inline close_message"><xsl:value-of select="$i18n.Cancel" /></a>
		  						
	  						</form>
	  						
	  					</div>
	  					
	  					<div class="tabs-content notes">
		  					
		  					<div class="heading-wrapper">
		  						<h2><xsl:value-of select="$i18n.InternalMessagesTitle" /></h2>
		  						<a href="#" class="btn btn-blue btn-right open_message"><i data-icon-before="+"></i><xsl:value-of select="$i18n.NewInternalMessage" /></a>
		  					</div>
		  					
		  					<xsl:choose>
		  						<xsl:when test="internalMessages/InternalMessage">
		  							<ul class="messages show-mentions">
		  								<xsl:apply-templates select="internalMessages/InternalMessage" />
		  							</ul>
		  						</xsl:when>
		  						<xsl:otherwise>
		  							<xsl:value-of select="$i18n.NoInternalMessages" />
		  						</xsl:otherwise>	  					
		  					</xsl:choose>
		  					
		  				</div>
		  				
		  			</div>
	  			</xsl:if>
	  			
	  			<xsl:for-each select="../TabContents/ViewFragment">
 						<xsl:value-of select="HTML" disable-output-escaping="yes"/>
 					</xsl:for-each>
	  			
  			</div>
			
			<div class="navigator-buttons centered">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/preview/{flowInstanceID}" class="btn btn-green xl next">
					<i data-icon-before="S" class="xl"></i>
					<xsl:value-of select="$i18n.ShowFlowInstance" />
				</a>
				<xsl:if test="Status/isAdminMutable = 'true'">
	  				<span class="or"><xsl:value-of select="$i18n.Or" /></span>     
	  				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowinstance/{Flow/flowID}/{flowInstanceID}" class="btn btn-light xl prev"><i data-icon-before="W" class="xl"></i><xsl:value-of select="$i18n.UpdateFlowInstance" /></a>
 				</xsl:if>
 			</div>
 			
		</section>
	
	</xsl:template>	
	
	<xsl:template match="FlowInstanceEvent">
		
		<xsl:variable name="odd" select="(position() mod 2) != 0" />
		
		<tr>
			<xsl:if test="$odd">
				<xsl:attribute name="class">odd</xsl:attribute>
			</xsl:if>
			
			<td class="icon">
				<xsl:if test="Attributes/Attribute[Name='pdf']/Value = 'true'">
					<xsl:choose>
						<xsl:when test="eventType = 'SIGNED'">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/pdf/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstanceSignPDF}">
								<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf_lock.png" />
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/pdf/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstancePDF}">
								<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf.png" />
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				
				<xsl:if test="Attributes/Attribute[Name='xml']/Value = 'true'">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/xml/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadFlowInstanceXML}">
						<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/xml.png" />
					</a>
				</xsl:if>
				
				<xsl:if test="Attributes/Attribute[Name='signingData']">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/signature/{../../flowInstanceID}/{eventID}" title="{$i18n.DownloadSignature}">
						<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/lock.png" />
					</a>
				</xsl:if>
			</td>
			
			<td data-title="{$i18n.Action}" class="service">
				<xsl:call-template name="getEventTypeText" />
			</td>
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
			<td data-title="{$i18n.Date}" class="date"><xsl:value-of select="added" /></td>
			<td data-title="{$i18n.Status}" class="status">
				<xsl:value-of select="status" />
				<xsl:if test="statusDescription">
					<xsl:call-template name="createHelpDialog">
						<xsl:with-param name="id" select="'status'" />
						<xsl:with-param name="text" select="statusDescription" />
						<xsl:with-param name="class" select="'floatright'" />
					</xsl:call-template>
				</xsl:if>
			</td>
			<td data-title="{$i18n.Person}" class="person">
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="poster/user" />
				</xsl:call-template>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="user" mode="manager">
		
		<i title="{email}" data-icon-before="p" class="sender"></i><xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
		
		<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
		
	</xsl:template>
	
	<xsl:template match="user" mode="owner">
		
		<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
		
		<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
		
	</xsl:template>
	
	<xsl:template match="ExternalMessage">
	
		<xsl:call-template name="createMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="attachments" select="attachments/ExternalMessageAttachment" />
			<xsl:with-param name="prefix" select="'messages'" />
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="InternalMessage">
	
		<xsl:call-template name="createMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="attachments" select="attachments/InternalMessageAttachment" />
			<xsl:with-param name="prefix" select="'notes'" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="createMessage">
		
		<xsl:param name="message" />
		<xsl:param name="attachments" />
		<xsl:param name="prefix" />
		
		<li id="{$prefix}-{$message/messageID}" class="official">
			
			<xsl:attribute name="class">
			
				<!-- TODO check if this message is unread -->
				<xsl:if test="false()">unread</xsl:if>
				
				<xsl:choose>
					<!-- External message -->
					<xsl:when test="postedByManager">
						<xsl:choose>
							<xsl:when test="postedByManager = 'false'"> official</xsl:when>
							<xsl:otherwise> me</xsl:otherwise>
						</xsl:choose>					
					</xsl:when>
					
					<!-- Internal message -->
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="../../../user/userID = poster/userID">me</xsl:when>
							<xsl:otherwise> official</xsl:otherwise>
						</xsl:choose>					
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:attribute>

			<div class="user">
				<figure><img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/profile-standard.png" /></figure>
			</div>
			
			<div class="message">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="$message/message" />
				</xsl:call-template>
				<span class="author">
					<i data-icon-before="m"/>
					<xsl:call-template name="printUser">
						<xsl:with-param name="user" select="$message/poster" />
					</xsl:call-template>
				 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="$message/added" /></span>
				 </span>
				 <xsl:if test="$attachments">
					<div class="files">
						<xsl:apply-templates select="$attachments" />
					</div>
				</xsl:if>
			</div>
			<div class="marker"></div>
			
		</li>
	
	</xsl:template>
	
	<xsl:template match="ExternalMessageAttachment">
		
		<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/externalattachment/{../../messageID}/{attachmentID}" class="btn btn-file"><i data-icon-before="d"></i><xsl:value-of select="filename" /><xsl:text>&#160;</xsl:text><span class="size">(<xsl:value-of select="FormatedSize" />)</span></a>
		
	</xsl:template>
	
	<xsl:template match="InternalMessageAttachment">
		
		<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/internalattachment/{../../messageID}/{attachmentID}" class="btn btn-file"><i data-icon-before="d"></i><xsl:value-of select="filename" /><xsl:text>&#160;</xsl:text><span class="size">(<xsl:value-of select="FormatedSize" />)</span></a>
		
	</xsl:template>
	
	<xsl:template match="ImmutableFlowInstanceManagerPreview">
		
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'PREVIEW'" />
		</xsl:call-template>
	
		<section class="modal warning child modal-marginbottom">
			<xsl:value-of select="$i18n.FlowInstancePreviewNotificationTitle.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="FlowInstance/Flow/name" />
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$i18n.FlowInstancePreviewNotificationTitle.Part2" />
			<xsl:text>&#160;</xsl:text>
			<strong><xsl:value-of select="FlowInstance/flowInstanceID" /></strong>
			<i class="icon close">x</i>
		</section>
	
		<section class="service child">
		
			<div class="section-full padtop">
  				<div class="heading-wrapper">
  					<figure>
	  					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
	  				</figure>
	  				<div class="heading">
  						<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /></h1>
					
						<xsl:variable name="submittedEvents" select="FlowInstance/events/FlowInstanceEvent[eventType='SUBMITTED']" />
						
						<span class="errandno">
							<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" />
							<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
							
							<xsl:value-of select="$i18n.PostedBy" /><xsl:text>:&#160;</xsl:text>
							<xsl:call-template name="PrintPostedBy">
								<xsl:with-param name="poster" select="$submittedEvents[position() = 1]/poster/user"/>
								<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="FlowInstance/firstSubmitted" />
							
							<xsl:if test="count($submittedEvents) > 1">
								<b class="pipe"><xsl:text>&#160;|&#160;</xsl:text></b>
								<xsl:value-of select="$i18n.LastSubmittedBy" /><xsl:text>:&#160;</xsl:text>
								<xsl:call-template name="printUser">
									<xsl:with-param name="user" select="$submittedEvents[position() = last()]/poster/user" />
								</xsl:call-template>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$submittedEvents[position() = last()]/added" />
							</xsl:if>
							
						</span>
					
					</div>
  				</div>
  			</div>

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
	
	<xsl:template match="UpdateInstanceStatus">
		
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'STATUS'" />
		</xsl:call-template>
	
		<section class="child">
			<div class="section-full header-full">
  				<div class="heading-wrapper full">
  					<figure>
	  					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
	  				</figure>
	  				<div class="heading">
  						<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /><xsl:text>&#160;</xsl:text><b>(<xsl:value-of select="FlowInstance/Status/name" />)</b></h1>
						<span class="errandno"><xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" /></span>
					</div>
  				</div>
  			</div>
  			<div class="divider errands"></div>
  			
  			<xsl:apply-templates select="validationError"/>
  			
  			<form method="post" action="{/Document/requestinfo/uri}">
  			
	  			<div class="section-full">
	  				<h2><xsl:value-of select="$i18n.CurrentStatus"/><xsl:text>&#160;</xsl:text><b><xsl:value-of select="FlowInstance/Status/name" /></b></h2>
	  				<article>
	 						<div class="heading-wrapper">
	 							<h2>Ny status:</h2>
	 						</div>
	 						<div class="clearfix"></div>
	 						
	 						<xsl:apply-templates select="FlowInstance/Flow/Statuses/Status[contentType != 'NEW' and contentType != 'WAITING_FOR_MULTISIGN' and contentType != 'WAITING_FOR_PAYMENT']" mode="radiobutton">
	 							<xsl:with-param name="selectedID">
	 								<xsl:value-of select="FlowInstance/Status/statusID"/>
	 							</xsl:with-param>
	 						</xsl:apply-templates>
	 						
	 					</article>
	  			</div>
	  			<div class="divider"/>
	  			<div class="section-full">
	  				<article class="buttons">
	 						<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline"/>
	 						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{FlowInstance/flowInstanceID}" class="btn btn-light btn-inline"><xsl:value-of select="$i18n.Cancel"/></a>
	 				</article>
	  			</div>
	  			
  			</form>
 		</section>	
	
	</xsl:template>		
	
	<xsl:template match="Status" mode="radiobutton">
	
		<xsl:param name="selectedID"/>
	
		<div class="bigmarginleft">
			<input type="radio" value="{statusID}" id="status_{statusID}" name="statusID">
				
				<xsl:if test="$selectedID = statusID">
					<xsl:attribute name="checked"/>
				</xsl:if>
				
			</input>
				
			<label class="radio" for="status_{statusID}">
				<xsl:value-of select="name"/>
			</label>
		</div>
	
	</xsl:template>		
	
	<xsl:template match="UpdateInstanceManagers">
		
		<xsl:call-template name="showManagerFlowInstanceControlPanel">
			<xsl:with-param name="flowInstance" select="FlowInstance" />
			<xsl:with-param name="view" select="'MANAGER'" />
		</xsl:call-template>

		<xsl:apply-templates select="validationError" />
	
		<section class="child">
			<div class="section-full header-full">
  				<div class="heading-wrapper full">
  					<figure>
	  					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
	  				</figure>
	  				<div class="heading">
  						<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name" /><xsl:text>&#160;</xsl:text><b>(<xsl:value-of select="FlowInstance/Status/name" />)</b></h1>
						<span class="errandno"><xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" /></span>
					</div>
  				</div>
  			</div>
  			<div class="divider"></div>
  			
  			<script type="text/javascript">
  				i18nChooseManager = '<xsl:value-of select="$i18n.ChooseManager" />';
  			</script>
  			
  			<form method="post" action="{/Document/requestinfo/uri}">
  			
  				<div class="section-full">
  			
					<ul class="manager-list list-table">
	  					
	  					<xsl:apply-templates select="FlowInstance/managers/user" mode="flowinstance-managers" />
	  					
	  					<li id="manager_template" style="display: none">
							<xsl:call-template name="createHiddenField">
								<xsl:with-param name="name" select="'userID'" />
								<xsl:with-param name="disabled" select="'disabled'" />
							</xsl:call-template>
							<div class="wrap">
								<figure>
									<img class="picture" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/profile-standard.png" alt="" />
								</figure>
								<span class="text"></span><a class="delete" data-icon-after="t"><xsl:value-of select="$i18n.DeleteManager" /></a>
							</div>
						</li>
						
	  				</ul>
	
					<div class="select-box left with-search addmanagers">
						<span class="text"><xsl:value-of select="$i18n.ChooseManager" /></span>
						<span class="arrow">_</span>
						<div class="search">
							<input type="text" placeholder="{$i18n.SearchManager}" />
						</div>
						<div class="options with-search">
							<ul>
								<xsl:apply-templates select="AvailableManagers/user" mode="manager-list" />
							</ul>
						</div>
					</div>
					<a class="btn btn-green btn-inline" href="#" data-icon-before="+"><xsl:value-of select="$i18n.AddManager" /></a>

				</div>

	  			<div class="divider"/>
	  			<div class="section-full">
	  				<article class="buttons">
 						<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline"/>
	 						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{FlowInstance/flowInstanceID}" class="btn btn-light btn-inline"><xsl:value-of select="$i18n.Cancel"/></a>
	 				</article>
	  			</div>
	  			
  			</form>
 		</section>	
	
	</xsl:template>		
	
	<xsl:template match="user" mode="flowinstance-managers">
		
		<li id="manager_user_{userID}">
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name" select="'userID'"></xsl:with-param>
				<xsl:with-param name="value" select="userID"></xsl:with-param>
			</xsl:call-template>
			<div class="wrap">
				<figure>
					<img class="picture" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/profile-standard.png" alt="" />
				</figure>
				<span class="text"><xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" /></span><a class="delete" data-icon-after="t"><xsl:value-of select="$i18n.DeleteManager" /></a>
			</div>
		</li>
		
	</xsl:template>
	
	<xsl:template match="user" mode="manager-list">
		
		<li id="user_{userID}">
			<a href="#">
				<span class="text"><xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" /></span>
			</a>
		</li>
	
	</xsl:template>
	
	<xsl:template match="lastFlowAction">
		
		<xsl:if test=". = 'SAVE' or . = 'SAVE_AND_PREVIEW'">
			
			<section class="modal success">
  				<span data-icon-before="c"><xsl:value-of select="$i18n.FlowInstanceSavedByManager" /></span>
  				<i class="icon close">x</i>
  			</section>
			
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowInstancePreviewError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstancePreviewError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='FlowInstanceManagerClosedError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstanceManagerClosedError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='StatusNotFoundValidationError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.StatusNotFoundValidationError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidStatusValidationError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.InvalidStatusValidationError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='OneOrMoreSelectedManagerUsersNotFoundError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.OneOrMoreSelectedManagerUsersNotFoundError"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='UnauthorizedManagerUserError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message">
			
				<xsl:value-of select="$i18n.UnauthorizedManagerUserError.part1"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="user/firstname" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="user/lastname" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$i18n.UnauthorizedManagerUserError.part2"/>	
					
			</xsl:with-param>
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
					<xsl:value-of select="$i18n.ValidationError.UnableToParseRequest" />
				</strong>
			</span>
		</div>
		
	</xsl:template>						
	
	<xsl:template match="validationError[fieldName='externalmessage']">
		
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageRequired" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageToShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageToLong" />
						</xsl:when>
					</xsl:choose>
				</strong>
			</span>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[fieldName='internalmessage']">
		
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageRequired" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageToShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageToLong" />
						</xsl:when>
					</xsl:choose>
				</strong>
			</span>
		</div>
		
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
	
	<xsl:template match="ExtensionLink">
	
		<div class="extensionlink">
			
			<a href="{url}">

				<xsl:value-of select="name"/>
	
				<xsl:if test="icon">
					<xsl:text> </xsl:text>
					<img src="{icon}"/>
				</xsl:if>
			
			</a>
		</div>
	
	</xsl:template>
	
	<xsl:template match="ExtensionLink" mode="tab-header">
		
		<li>
			<a href="{url}">

				<xsl:if test="icon">
					<xsl:text> </xsl:text>
					<img src="{icon}"/>
				</xsl:if>
				
				<xsl:value-of select="name"/>
			
			</a>
		</li>
	
	</xsl:template>
	
</xsl:stylesheet>