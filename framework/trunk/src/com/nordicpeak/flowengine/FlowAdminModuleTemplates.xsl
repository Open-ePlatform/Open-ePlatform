<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common" exclude-result-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>
	
	<xsl:variable name="imgPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
		/jquery/jquery-ui.js?v=1
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
		/featherlight/js/featherlight.min.js
		/js/confirmpost.js
		/tablesorter/js/jquery.tablesorter.min.js
	</xsl:variable>
	
	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
		/featherlight/css/featherlight.min.css?v=1
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.ui.touch.min.js
		/js/jquery.blockui.js
		/js/jquery.expander.min.js
		/js/flowengine.helpdialog.js
		/js/flowengine.js?v=2
		/js/flowengine.step-navigator.js
		/js/flowadminmodule.js?v=23
		/js/jquery.ui.datepicker-sv.js
		/js/flowengine.tablesorter.js
		/js/flowengine.tablefilter.js
		/js/UserGroupList.js
		/js/colorpicker/jquery.minicolors.min.js
		/js/jquery.cookie.js
		/js/datatables.min.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css?v=9
		/css/UserGroupList.css?v=1
		/js/colorpicker/jquery.minicolors.css
		/css/datatables.css
		/css/flowadmin.css
	</xsl:variable>

	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="ViewFragmentExtension">
			
				<xsl:apply-templates select="ViewFragmentExtension" />
				
			</xsl:when>
			<xsl:when test="ShowFlowMenu">
				
				<div class="contentitem">
					<xsl:apply-templates select="ShowFlowMenu" />
				</div>
				
			</xsl:when>
			<xsl:when test="FlowUserAdminExtension">
			
				<xsl:apply-templates select="FlowUserAdminExtension" />
			
			</xsl:when>			
			<xsl:otherwise>
				
				<div class="contentitem errands-wrapper flowadmin">
					
					<xsl:choose>
						<xsl:when test="FlowInstanceManagerForm or FlowInstanceManagerPreview or FlowInstanceManagerSubmitted or ShowFlowOverview">
							<xsl:attribute name="class">contentitem</xsl:attribute>
							<xsl:attribute name="id">FlowBrowser</xsl:attribute>
						</xsl:when>
						<xsl:when test="ShowFlow"><xsl:attribute name="class">contentitem errands-wrapper flowadmin showflow</xsl:attribute></xsl:when>
					</xsl:choose>
					
					<xsl:apply-templates select="ListFlows" />
					<xsl:apply-templates select="ShowFlow" />
					<xsl:apply-templates select="AddFlow" />
					<xsl:apply-templates select="UpdateFlow" />
					<xsl:apply-templates select="SortFlow" />
					
					<xsl:apply-templates select="UpdateFlowFamily"/>
					<xsl:apply-templates select="ChangeFlowType"/>
					<xsl:apply-templates select="UpdateFlowIcon" />
					<xsl:apply-templates select="UpdateNotifications"/>
							
					<xsl:apply-templates select="AddStep" />
					<xsl:apply-templates select="UpdateStep" />
					<xsl:apply-templates select="AddQueryDescriptor" />
					<xsl:apply-templates select="AddEvaluatorDescriptor" />
					<xsl:apply-templates select="SortEvaluators" />
			
					<xsl:apply-templates select="AddStatus" />
					<xsl:apply-templates select="UpdateStatus" />
					<xsl:apply-templates select="SortStatuses" />
					<xsl:apply-templates select="ReplaceFlowStatuses" />
					
					<xsl:apply-templates select="ListStandardStatusGroups" />
					<xsl:apply-templates select="AddStandardStatusGroup" />
					<xsl:apply-templates select="UpdateStandardStatusGroup" />
					<xsl:apply-templates select="ShowStandardStatusGroup" />
					<xsl:apply-templates select="AddStandardStatus" />
					<xsl:apply-templates select="UpdateStandardStatus" />
					<xsl:apply-templates select="SortStandardStatuses" />
					
					<xsl:apply-templates select="FlowInstanceManagerForm"/>
					<xsl:apply-templates select="FlowInstanceManagerPreview"/>
					<xsl:apply-templates select="FlowInstanceManagerSubmitted"/>
					<xsl:apply-templates select="FlowInstanceManagerAllStepsForm"/>
					
					<xsl:apply-templates select="ListFlowTypes"/>
					<xsl:apply-templates select="ShowFlowType"/>
					<xsl:apply-templates select="AddFlowType"/>
					<xsl:apply-templates select="UpdateFlowType"/>
					
					<xsl:apply-templates select="AddCategory"/>
					<xsl:apply-templates select="UpdateCategory"/>
					
					<xsl:apply-templates select="SelectImportTargetType"/>
					<xsl:apply-templates select="ImportFlow"/>
					<xsl:apply-templates select="ImportQueries"/>
					
					<xsl:apply-templates select="AddFlowForm" />
					<xsl:apply-templates select="UpdateFlowForm" />
					
					<xsl:apply-templates select="ShowFlowFamilyEvents"/>
					<xsl:apply-templates select="AddFlowFamilyEvent" />
					
					<xsl:apply-templates select="SigningForm"/>
					<xsl:apply-templates select="ShowFlowOverview"/>
					<xsl:apply-templates select="AutoManagerAssignment" />
					
					<xsl:apply-templates select="AddMessageTemplate" />
					<xsl:apply-templates select="UpdateMessageTemplate" />

					<xsl:apply-templates select="UpdateManagementInfo" />
					
				</div>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

	<xsl:template match="ListFlows">
	
		<div class="floatleft marginbottom eighty">
		
			<h1><xsl:value-of select="$i18n.Flowslist.title" /></h1>
			
			<xsl:apply-templates select="validationError"/>
			
			
		
		</div>
		
		<xsl:call-template name="CreateShortcutMenu"/>
		
		<div class="floatleft marginbottom">
		
			<p>
				<xsl:value-of select="$i18n.Flowlist.description" />
			</p>
		
		</div>
		
		<div id="reordering" class=" dd twenty floatright">
			<a class="floatright btn btn-light" href="#" onclick="openTableSettingsModal(this, event)" title="{$i18n.EditTableSettings}" >
	 			<span data-icon-before="W">
	 				<xsl:value-of select="$i18n.TableSettings"/>
	 			</span>
	 		</a>
		</div>
		
		<div class="hidden flow-list-filters floatleft marginbottom">
			<select id="flow-status-filter" class="bigmarginright" style="width: 180px">
				<option value="all"><xsl:value-of select="$i18n.All" /></option>
				<option value="published"><xsl:value-of select="$i18n.Published" /></option>
				<option value="unpublished"><xsl:value-of select="$i18n.Unpublished" /></option>
			</select>
			<xsl:call-template name="AdditionalListFilters" />
		</div>
		
		<div id="flowlist-form">
		<table id="flowlist" class="flow-list-table clearboth full stripe display" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowdata">
			<thead>	
				<tr>
					<th width="10"></th>
					<th width="10"></th>
					<th width="10"></th>
					<th><xsl:value-of select="$i18n.flowName" /></th>
					<th width="150"><xsl:value-of select="$i18n.flowType" /></th>
					<th width="50"><xsl:value-of select="$i18n.flowCategory" /></th>
					<th width="50"><xsl:value-of select="$i18n.versions" /></th>
					<th width="50"><xsl:value-of select="$i18n.SubmittedInstances" /></th>
					<th width="50"><xsl:value-of select="$i18n.NotSubmittedInstances" /></th>
					<th width="50"><xsl:value-of select="$i18n.flowFamilyLastReviewed" /></th>
					<th width="50"><xsl:value-of select="$i18n.flowFamilyID" /></th>
					<th width="50"><xsl:value-of select="$i18n.organization" /></th>
					<xsl:call-template name="ExtraFlowListColumnsHeader" />
					<th width="1" />
				</tr>
			</thead>
		</table>
		</div>
		
		<div style="display: none;">
			
					<div id="table-settings-modal" class="table-settings-modal contentitem">
						<div class="modal-content">
						
							<div class="modal-header bigmarginbottom">
								<h1>
									<xsl:value-of select="$i18n.TableSettings.Title"/>
								</h1>
							</div>
							
							<div class="modal-body">
							
								<p class="tiny full">
							  	<xsl:value-of select="$i18n.TableSettings.Description" />
							  </p>
								  
								<div id="table-settings-modal-div" class="full bigmarginbottom sortable">
								
									<xsl:call-template name="createTableSettingRow">
										<xsl:with-param name="columnID">flowName</xsl:with-param>
										<xsl:with-param name="name" select="$i18n.flowName" />
									</xsl:call-template>
									
									<xsl:call-template name="createTableSettingRow">
										<xsl:with-param name="columnID">flowType</xsl:with-param>
										<xsl:with-param name="name" select="$i18n.flowType" />
									</xsl:call-template>
								
									<xsl:if test="/Document/UseCategories">
										<xsl:call-template name="createTableSettingRow">
											<xsl:with-param name="columnID">flowCategory</xsl:with-param>
											<xsl:with-param name="name" select="$i18n.flowCategory" />
										</xsl:call-template>
									</xsl:if>
									
									<xsl:call-template name="createTableSettingRow">
										<xsl:with-param name="columnID">versions</xsl:with-param>
										<xsl:with-param name="name" select="$i18n.versions" />
									</xsl:call-template>
									
									<xsl:if test="not(/Document/HideSubmittedInstances)">
										<xsl:call-template name="createTableSettingRow">
											<xsl:with-param name="columnID">submittedInstances</xsl:with-param>
											<xsl:with-param name="name" select="$i18n.SubmittedInstances" />
										</xsl:call-template>
									</xsl:if>
									
									<xsl:if test="not(/Document/HideNotSubmittedInstances)">
										<xsl:call-template name="createTableSettingRow">
											<xsl:with-param name="columnID">notSubmittedInstances</xsl:with-param>
											<xsl:with-param name="name" select="$i18n.NotSubmittedInstances" />
										</xsl:call-template>
									</xsl:if>
									
									<xsl:if test="not(/Document/HideFlowFamilyLastReviewed)">
										<xsl:call-template name="createTableSettingRow">
											<xsl:with-param name="columnID">flowFamilyLastReviewed</xsl:with-param>
											<xsl:with-param name="name" select="$i18n.flowFamilyLastReviewed" />
										</xsl:call-template>
									</xsl:if>
									
									<xsl:call-template name="createTableSettingRow">
										<xsl:with-param name="columnID">flowFamilyID</xsl:with-param>
										<xsl:with-param name="name" select="$i18n.flowFamilyID" />
									</xsl:call-template>
									
									<xsl:if test="not(/Document/HideOrganization)">
										<xsl:call-template name="createTableSettingRow">
											<xsl:with-param name="columnID">organization</xsl:with-param>
											<xsl:with-param name="name" select="$i18n.organization" />
										</xsl:call-template>
									</xsl:if>
									
								</div>
													
								<input class="save bigmargintop floatright btn btn-blue" type="button" value="{$i18n.TableSettings.Save}" />
								<input class="reset bigmargintop bigmarginright floatright btn btn-blue" type="button" value="{$i18n.TableSettings.Reset}" />
								
							</div>
							
						</div>
					</div>
				
				</div>

		<script type="text/javascript">
		
			FlowAdmin = {
				saveSettingsURL: "<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>/saveusersettings",
	 			showFlowURL: "<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>/showflow/",
				showFlowTypeURL: "<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>/flowtype/",
				deleteFlowFamilyURL: "<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>/deleteflowfamily/",
				iconURL: "<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>/icon/",
				imgPath: "<xsl:value-of select="$imgPath"/>",
				useCategories: <xsl:choose><xsl:when test="/Document/UseCategories">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				hideSubmittedInstances: <xsl:choose><xsl:when test="/Document/HideSubmittedInstances">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				hideNotSubmittedInstances: <xsl:choose><xsl:when test="/Document/HideNotSubmittedInstances">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				hideFlowFamilyLastReviewed: <xsl:choose><xsl:when test="/Document/HideFlowFamilyLastReviewed">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				hideOrganization: <xsl:choose><xsl:when test="/Document/HideOrganization">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				hideDeleteButton: <xsl:choose><xsl:when test="/Document/HideDeleteButton">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose>,
				i18n: {
		 			"decimal":        "<xsl:value-of select="$i18n.DataTable.decimal"/>",
					"emptyTable":     "<xsl:value-of select="$i18n.DataTable.emptyTable"/>",
					"info":           "<xsl:value-of select="$i18n.DataTable.info"/>",
					"infoEmpty":      "<xsl:value-of select="$i18n.DataTable.infoEmpty"/>",
					"infoFiltered":   "<xsl:value-of select="$i18n.DataTable.infoFiltered"/>",
					"infoPostFix":    "<xsl:value-of select="$i18n.DataTable.infoPostFix"/>",
					"thousands":      "<xsl:value-of select="$i18n.DataTable.thousands"/>",
					"lengthMenu":     "<xsl:value-of select="$i18n.DataTable.lengthMenu"/>",
					"loadingRecords": "<xsl:value-of select="$i18n.DataTable.loadingRecords"/>",
					"processing":     "<xsl:value-of select="$i18n.DataTable.processing"/>",
					"search":         "<xsl:value-of select="$i18n.DataTable.search"/>",
					"zeroRecords":    "<xsl:value-of select="$i18n.DataTable.zeroRecords"/>",
					"fetchError":     "<xsl:value-of select="$i18n.DataTable.fetchError"/>",
					paginate: {
						"first":      "<xsl:value-of select="$i18n.DataTable.paginate.first"/>",
						"last":       "<xsl:value-of select="$i18n.DataTable.paginate.last"/>",
						"next":       "<xsl:value-of select="$i18n.DataTable.paginate.next"/>",
						"previous":   "<xsl:value-of select="$i18n.DataTable.paginate.previous"/>"
					},
					aria: {
						"sortAscending":  "<xsl:value-of select="$i18n.DataTable.aria.sortAscending"/>",
						"sortDescending": "<xsl:value-of select="$i18n.DataTable.aria.sortDescending"/>"
					},
					select: {
						rows: {
							many: "<xsl:value-of select="$i18n.DataTable.selection.many"/>",
							none: "<xsl:value-of select="$i18n.DataTable.selection.none"/>",
							one: "<xsl:value-of select="$i18n.DataTable.selection.one"/>"
						}
					},
					deleteFlowFamilyDisabledHasInstances: "<xsl:value-of select="$i18n.deleteFlowFamilyDisabledHasInstances"/>",
					deleteFlowFamilyDisabledIsPublished: "<xsl:value-of select="$i18n.deleteFlowFamilyDisabledIsPublished"/>",
					deleteFlowFamilyTitle: "<xsl:value-of select="$i18n.deleteFlowFamily.title"/>"
				},
			};
			
			<xsl:if test="FlowAdminUserColumnSetting">
						try {
							userColumnOrder = [<xsl:for-each select="FlowAdminUserColumnSetting">"<xsl:value-of select="column"/>"<xsl:if test="position() != last()" >, </xsl:if></xsl:for-each>];
							userColumnVisible = {<xsl:for-each select="FlowAdminUserColumnSetting"> <xsl:value-of select="column"/>&#58;"<xsl:value-of select="visible"/>"<xsl:if test="position() != last()" >, </xsl:if></xsl:for-each>};	
						} catch(error){
						
							if (console != undefined) {
								console.warn("Invalid user settings, using default instead. " + error);
							}
						}
			</xsl:if>
			
		</script>

	</xsl:template>
	
	<xsl:template name="createTableSettingRow">
		
		<xsl:param name="columnID" />
		<xsl:param name="name" />
		
		<div class="setting full padding margintop marginbottom lightbackground">
			<img class="bigmarginright cursor-move" src="{$imgPath}/move.png" title="{$i18n.TableSettings.MoveColumnOrder}" style="vertical-align: sub" />
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('columnID_', $columnID)"/>
				<xsl:with-param name="name" select="'columnID'"/>
				<xsl:with-param name="value" select="$columnID"/>
				<xsl:with-param name="class" select="'columnid'" />
			</xsl:call-template>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('sortorder_', $columnID)" />
				<xsl:with-param name="name" select="concat('sortorder_', $columnID)" />
				<xsl:with-param name="value" select="''" />
				<xsl:with-param name="class" select="'sortorder'" />
			</xsl:call-template>
			
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="concat('enabled_', $columnID)"/>
				<xsl:with-param name="name" select="concat('enabled_', $columnID)"/>
				<xsl:with-param name="checked" select="'false'"/>
				<xsl:with-param name="class" select="'enable bigmarginright'" />
			</xsl:call-template>
			
			<label class="nomargin" for="{concat('enabled_', $columnID)}">
				<xsl:value-of select="$name"/>
			</label>
			
		</div>
		
	</xsl:template>
	
	<xsl:template name="AdditionalListFilters" />
	
	<xsl:template name="CreateShortcutMenu">
	
		<xsl:variable name="extensionLinks" select="ExtensionLink[slot='bottom-right']"/>
		
		<xsl:if test="AddAccess or AdminAccess or $extensionLinks">
		
			<div id="shortcut-menu" class="flowadmin-menu dd twenty floatright">
				<a>
					<span>
						<xsl:value-of select="$i18n.Shortcuts"/>
					</span>
					<div class="menu-arrow"><i>_</i></div>
				</a>
				<div class="submenu">
					<ul>
					
						<xsl:if test="AddAccess">
						
							<xsl:call-template name="CreateShortcutMenuLink">
								<xsl:with-param name="url" select="concat(/Document/requestinfo/currentURI, '/', /Document/module/alias, '/addflow')" />
								<xsl:with-param name="name" select="$i18n.addFlow" />
							</xsl:call-template>
							
							<xsl:call-template name="CreateShortcutMenuLink">
								<xsl:with-param name="url" select="concat(/Document/requestinfo/currentURI, '/', /Document/module/alias, '/importflow')" />
								<xsl:with-param name="name" select="$i18n.importFlow" />
							</xsl:call-template>
							
						</xsl:if>
					
						<xsl:if test="AdminAccess">
						
							<xsl:call-template name="CreateShortcutMenuLink">
								<xsl:with-param name="url" select="concat(/Document/requestinfo/currentURI, '/', /Document/module/alias, '/standardstatuses')" />
								<xsl:with-param name="name" select="$i18n.administrateStandardStatuses" />
							</xsl:call-template>
							
							<xsl:call-template name="CreateShortcutMenuLink">
								<xsl:with-param name="url" select="concat(/Document/requestinfo/currentURI, '/', /Document/module/alias, '/flowtypes')" />
								<xsl:with-param name="name" select="$i18n.administrateFlowTypes" />
							</xsl:call-template>
							
						</xsl:if>
					
						<xsl:apply-templates select="$extensionLinks"/>
						
					</ul>
				</div>
			</div>
			
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="CreateShortcutMenuLink">
		<xsl:param name="url" select="url" />
		<xsl:param name="name" select="name" />
	
		<li>

			<a href="{$url}" title="{$name}">
				
				<span class="icon arrow"><i data-icon-before=">"></i></span>
				<span><xsl:value-of select="$name"/></span>
			
			</a>

		</li>
			
	</xsl:template>
	
	<xsl:template match="ExtensionLink">
	
		<xsl:call-template name="CreateShortcutMenuLink" />
	
	</xsl:template>
	
	<xsl:template match="ExtensionLink" mode="top-right">
		<xsl:param name="flowID"/>
	
		<a class="extensionlink marginleft" href="{url}/{$flowID}" title="{name}">
			<img class="alignbottom" src="{icon}" alt=""/>
		</a>
	
	</xsl:template>
	
	<xsl:template match="ShowFlowMenu">
		
		<section id="flowMenu" class="nomargin">
		
			<ul class="list-table">
				
				<li><a href="#baseinfo" title="{$i18n.baseInfo}"><xsl:value-of select="$i18n.baseInfo" /></a></li>
				<xsl:if test="IsInternal">
					<li><a href="#steps" title="{$i18n.stepsAndQueries}"><xsl:value-of select="$i18n.stepsAndQueries" /></a></li>
					<li><a href="{PreviewQueriesURI}" title="{$i18n.PreviewQueries}" target="_blank"><xsl:value-of select="$i18n.PreviewQueries"/></a></li>
					<li><a href="{TestFlowURI}" title="{$i18n.testFlow}" target="_blank"><xsl:value-of select="$i18n.testFlow"/></a></li>
				</xsl:if>
				
				<li><a href="#pdfform" title="{$i18n.FlowForm}"><xsl:value-of select="$i18n.FlowForm" /></a></li>
				
				<xsl:if test="IsInternal">
					<li><a href="#statuses" title="{$i18n.statuses}"><xsl:value-of select="$i18n.statuses" /></a></li>
					<li><a href="#managers" title="{$i18n.Managers}"><xsl:value-of select="$i18n.Managers" /></a></li>
				</xsl:if>
				
				<xsl:if test="ShowNotifications">
					<li><a href="#notifications" title="{$i18n.Notifications}"><xsl:value-of select="$i18n.Notifications" /></a></li>
				</xsl:if>
				
				<xsl:if test="ShowFlowSurveys">
					<li><a href="#flowsurveys" title="{$i18n.FlowSurveysTitle}"><xsl:value-of select="$i18n.FlowSurveysTitle" /></a></li>
				</xsl:if>

				<li><a href="#messagetemplates" title="{$i18n.MessageTemplates.title}"><xsl:value-of select="$i18n.MessageTemplates.title" /></a></li>

				<li><a href="#versions" title="{$i18n.versions}"><xsl:value-of select="$i18n.versions" /></a></li>
				
				<li><a href="#extensions" title="{$i18n.ExtensionProviders}"><xsl:value-of select="$i18n.ExtensionProviders" /></a></li>
				
				<xsl:apply-templates select="ExtensionProvider" mode="menu" />
				
				<li><a href="#events" title="{$i18n.Events.Title}"><xsl:value-of select="$i18n.Events.Title" /></a></li>
				
			</ul>
		
		</section>
		
	</xsl:template>
	
	<xsl:template match="ExtensionProvider" mode="menu">
		
		<li><a href="#extension-{ID}"><xsl:value-of select="Title" /></a></li>
		
	</xsl:template>
	
	<xsl:template match="ShowFlow">
	
		<xsl:variable name="disableStructureManipulation" select="Flow/flowInstanceCount > 0 or (Flow/published = 'true' and Flow/enabled = 'true')"/>
		
		<xsl:variable name="isInternal">
			<xsl:if test="not(Flow/externalLink)">true</xsl:if>
		</xsl:variable>
		
		<xsl:apply-templates select="validationError"/>
		
		<xsl:variable name="updateBaseInfoLink">
			<xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/updateflow/<xsl:value-of select="Flow/flowID" />
		</xsl:variable>
		
		<a name="baseinfo"/>
		
		<div class="showflow-wrapper">
			
			<h2 class="title">
				<xsl:value-of select="$i18n.baseInfo"/>
				
				<div class="floatright adminicons">
						
					<xsl:if test="/Document/user/admin = 'true'">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/recacheflow/{Flow/flowID}" title="{$i18n.ReCacheFlow}">
							<img src="{$imgPath}/reload.png" alt="" />
						</a>
					</xsl:if>
				
					<xsl:variable name="extensionLinks" select="ExtensionLink[slot='top-right']"/>
				
					<xsl:if test="$extensionLinks">
						<xsl:apply-templates select="$extensionLinks" mode="top-right">
							<xsl:with-param name="flowID" select="Flow/flowID"/>
						</xsl:apply-templates>
					</xsl:if>
				
					<xsl:if test="$isInternal = 'true'">
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/generatexsd/{Flow/flowID}" title="{$i18n.downloadxsd.title}">
							<img src="{$imgPath}/xsd.png" alt="" />
						</a>
					</xsl:if>
				
					<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/changeflowtype/{Flow/flowID}" title="{$i18n.ChangeFlowType.linkTitle}">
						<img src="{$imgPath}/folder_edit.png" alt="" />
					</a>
					
					<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateicon/{Flow/flowID}" title="{$i18n.updateFlowIcon.link.title}">
						<img src="{$imgPath}/photo_edit.png" alt="" />
					</a>
				
					<a class="marginleft" href="{$updateBaseInfoLink}" title="{$i18n.updateFlowBaseInfo.title}">
						<img src="{$imgPath}/pen.png" alt="" />
					</a>
				
					<xsl:choose>
						<xsl:when test="$disableStructureManipulation = false()">
						
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflow/{Flow/flowID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteFlow.title}: {Flow/name}">
								<img src="{$imgPath}/delete.png" alt="" />
							</a>				
						
						</xsl:when>
						<xsl:otherwise>
						
							<xsl:choose>
								<xsl:when test="Flow/flowInstanceCount > 0">
			
									<a class="marginleft" href="#" onclick="alert('{$i18n.deleteFlowDisabledHasInstances}'); return false;" title="{$i18n.deleteFlowDisabledHasInstances}">
										<img src="{$imgPath}/delete_gray.png" alt="" />
									</a>
			
								</xsl:when>
								<xsl:when test="Flow/published = 'true'">
			
									<a class="marginleft" href="#" onclick="alert('{$i18n.deleteFlowDisabledIsPublished}'); return false;" title="{$i18n.deleteFlowDisabledIsPublished}">
										<img src="{$imgPath}/delete_gray.png" alt="" />
									</a>
			
								</xsl:when>
							</xsl:choose>
						
						</xsl:otherwise>
					</xsl:choose>
				
				</div>
				
			</h2>
	
			<div class="showflow-content">
	
				<div class="floatleft full bigmarginbottom">
	
					<h3 class="floatleft clearboth">
						<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{Flow/flowID}?{Flow/IconLastModified}" alt="" class="bigmarginright" width="25px" />
						<xsl:value-of select="Flow/name" />
						<xsl:text>&#x20;(</xsl:text>
						<xsl:value-of select="$i18n.flowVersion"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="Flow/version"/>
						<xsl:text>)</xsl:text>
					</h3>
					
				</div>
				
				<xsl:if test="Flow/externalLink">
					
					<div class="floatleft full bigmarginbottom">
					
						<label class="floatleft clearboth">
							<xsl:value-of select="$i18n.externalLink" />
						</label>
						
						<div class="floatleft clearboth  word-break-all">
							<a href="{Flow/externalLink}" target="_blank" title="{$i18n.OpenExternalFlow}" data-icon-after="e"><xsl:value-of select="Flow/externalLink" /></a>
						</div>
					</div>
					
				</xsl:if>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.status" />
					</label>
					
					<div class="floatleft full">
							<xsl:choose>
							<xsl:when test="Flow/enabled = 'false'">
								<img src="{$imgPath}/disabled.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" />
								<xsl:value-of select="$i18n.disabled" />
							</xsl:when>
							<xsl:when test="Flow/published = 'true'">
								<img src="{$imgPath}/play.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" />
								<xsl:value-of select="$i18n.published" />
								<xsl:text> (</xsl:text>
								<xsl:value-of select="Flow/publishDate" />
								<xsl:if test="Flow/unPublishDate">
									<xsl:text> - </xsl:text>
									<xsl:value-of select="Flow/unPublishDate" />
								</xsl:if>
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$imgPath}/stop.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" />
								<xsl:value-of select="$i18n.notPublished" />
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</div>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom">
					<div class="floatleft full">
						<label><xsl:value-of select="$i18n.FlowFamilyID" /></label>
						<div><xsl:value-of select="Flow/FlowFamily/flowFamilyID" /></div>
					</div>
				</div>
				
				<div class="floatright min-width-thirtytree">
					<div class="floatleft fifty">
						<label><xsl:value-of select="$i18n.FlowID" /></label>
						<div><xsl:value-of select="Flow/flowID" /></div>
					</div>
				</div>
				
				<div class="full clearboth"/>
				
				<div class="floatleft min-width-thirtytree">
					<div class="floatleft fifty">
						<label><xsl:value-of select="$i18n.flowType" /></label>
						<div><xsl:value-of select="Flow/FlowType/name" /></div>
					</div>
				</div>
				
				<div class="floaleft min-width-thirtytree">
					<div class="floatleft fifty">
						<label><xsl:value-of select="$i18n.aliases.url" /></label>
						<div>
							<xsl:choose>
								<xsl:when test="Flow/FlowFamily/Aliases/Alias">
									<xsl:for-each select="Flow/FlowFamily/Aliases/Alias">
										<xsl:variable name="url">
											<xsl:value-of select="'https://'"/>
											<xsl:value-of select="/Document/requestinfo/servername"/>
											<xsl:value-of select="/Document/requestinfo/contextpath"/>
											<xsl:value-of select="'/'"/>
											<xsl:value-of select="."/>
										</xsl:variable>
										<a href="{$url}">
											<xsl:value-of select="$url"/>
										</a>
										<xsl:if test="position() != last()"><br/></xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>-</xsl:otherwise>
							</xsl:choose>
						</div>
					</div>
				</div>
				
				<div class="full clearboth"/>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
					<div class="floatleft">
						<label>
							<xsl:choose>
								<xsl:when test="Flow/requireAuthentication = 'true'"><img src="{$imgPath}/lock.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" /><xsl:value-of select="$i18n.requirersAuthentication" /></xsl:when>
								<xsl:otherwise><img src="{$imgPath}/lock_open.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" /><xsl:value-of select="$i18n.requirersNoAuthentication" /></xsl:otherwise>
							</xsl:choose>
						</label>
					</div>
				</div>
					
				<xsl:if test="$isInternal = 'true'">
				
					<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
						<div class="floatleft">
							<label>
								<xsl:choose>
									<xsl:when test="Flow/requireSigning = 'true'"><img src="{$imgPath}/page_edit.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" /><xsl:value-of select="$i18n.requiresSigning" /></xsl:when>
									<xsl:otherwise><img src="{$imgPath}/page_edit.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" /><xsl:value-of select="$i18n.requiresNoSigning" /></xsl:otherwise>
								</xsl:choose>
							</label>
						</div>
					</div>
				
				</xsl:if>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
					
					<div class="floatleft">
						<label for="hideFromOverview">
							<xsl:choose>
								<xsl:when test="Flow/hideFromOverview = 'true'"><img src="{$imgPath}/invisible.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" width="16px" /><xsl:value-of select="$i18n.hiddenFromOverview" /></xsl:when>
								<xsl:otherwise><img src="{$imgPath}/visible.png" alt="" class="marginright vertical-align-bottom flow-setting-icon" /><xsl:value-of select="$i18n.shownOnOverview" /></xsl:otherwise>
							</xsl:choose>
						</label>
					</div>
				</div>
				
				<div class="showflow-moreinfo-wrapper">
				
					<div class="showflow-moreinfo-content hidden" data-rel="baseinfo">
				
						<div class="textsetting">
							
							<label class="floatleft full bigmarginbottom">
								<xsl:value-of select="$i18n.shortDescription" />
								<a class="floatright" href="{$updateBaseInfoLink}#shortdescription" title="{$i18n.updateFlowBaseInfo.title}">
									<img src="{$imgPath}/pen.png" alt="" />
								</a>
							</label>
							
							<div class="floatleft full">
								<div class="padding border whitebackground">
									<xsl:value-of select="Flow/shortDescription" disable-output-escaping="yes"/>
								</div>
							</div>
						</div>
						
						<xsl:if test="Flow/longDescription">
						
							<div class="textsetting">
								
								<label class="floatleft full bigmarginbottom">
									<xsl:value-of select="$i18n.longDescription" />
									<a class="floatright" href="{$updateBaseInfoLink}#longdescription" title="{$i18n.updateFlowBaseInfo.title}">
										<img src="{$imgPath}/pen.png" alt="" />
									</a>
								</label>
								
								<div class="floatleft full">
									<div class="padding border whitebackground">
										<xsl:value-of select="Flow/longDescription" disable-output-escaping="yes"/>
									</div>
								</div>
							</div>
						
						</xsl:if>
						
						<xsl:if test="$isInternal = 'true'">
						
							<div class="textsetting">
								
								<label class="floatleft full bigmarginbottom">
									<xsl:value-of select="$i18n.submittedMessage" />
									<a class="floatright" href="{$updateBaseInfoLink}#submittedmessage" title="{$i18n.updateFlowBaseInfo.title}">
										<img src="{$imgPath}/pen.png" alt="" />
									</a>
								</label>
								
								<div class="floatleft full">
									<div class="padding border whitebackground">
										<xsl:value-of select="Flow/submittedMessage" disable-output-escaping="yes"/>
									</div>
								</div>
							</div>
						
						</xsl:if>
			
						<xsl:if test="Flow/Tags">
						
							<div class="textsetting fifty">
								
								<label class="floatleft full bigmarginbottom">
									<xsl:value-of select="$i18n.tags" />
									<a class="floatright" href="{$updateBaseInfoLink}#tags" title="{$i18n.updateFlowBaseInfo.title}">
										<img src="{$imgPath}/pen.png" alt="" />
									</a>
								</label>
								
								<div class="floatleft full">
									<div class="padding border whitebackground">
										<xsl:for-each select="Flow/Tags/tag">
											<xsl:value-of select="."/>
											<xsl:if test="position() != last()"><xsl:text>, </xsl:text></xsl:if>
										</xsl:for-each>
									</div>
								</div>
							</div>
							
						</xsl:if>
			
						<xsl:if test="Flow/Checks">
						
							<div class="textsetting fifty floatright">
								
								<label class="floatleft full bigmarginbottom">
									<xsl:value-of select="$i18n.checks" />
									<a class="floatright" href="{$updateBaseInfoLink}#checks" title="{$i18n.updateFlowBaseInfo.title}">
										<img src="{$imgPath}/pen.png" alt="" />
									</a>
								</label>
								
								<div class="floatleft full">
									<div class="padding border whitebackground">
										<xsl:for-each select="Flow/Checks/check">
											<span data-icon-before="c"><xsl:value-of select="."/></span>
											<xsl:if test="position() != last()"><br /></xsl:if>
										</xsl:for-each>
									</div>
								</div>
							</div>
							
						</xsl:if>
						
					</div>
				
				</div>
			
			</div>
			
			<div class="showflow-moreinfo-footer"><a href="#" class="show-more" data-rel="baseinfo"><xsl:value-of select="$i18n.ShowMore" /></a><a href="#" class="show-less hidden" data-rel="baseinfo"><xsl:value-of select="$i18n.ShowLess" /></a></div>
			
		</div>
		
		<div class="showflow-wrapper">
		
			<a name="managementinfo"/>
		
			<h2 class="title">
				<xsl:value-of select="$i18n.managementInfo"/>
								
				<div class="floatright adminicons">
						
					<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatemanagementinfo/{Flow/flowID}" title="{$i18n.updateManagementInfo}">
						<img src="{$imgPath}/pen.png" alt="" />
					</a>
					
				</div>
			</h2>
			
			<xsl:variable name="managementInfo" select="Flow/FlowFamily/ManagementInfo" />
			
			<div class="showflow-content">
			
				<xsl:choose>
					<xsl:when test="$managementInfo">
					
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.processOwner" /></label>
								<div><xsl:value-of select="$managementInfo/processOwner" /></div>
							</div>
						</div>
						
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.flowResponsible" /></label>
								<div><xsl:value-of select="$managementInfo/flowResponsible" /></div>
							</div>
						</div>
						
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.informationResponsible" /></label>
								<div><xsl:value-of select="$managementInfo/informationResponsible" /></div>
							</div>
						</div>
						
						<div class="full clearboth"/>
						
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.status" /></label>
								<div><xsl:value-of select="$managementInfo/status" /></div>
							</div>
						</div>
						
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.organization" /></label>
								<div><xsl:value-of select="$managementInfo/organization" /></div>
							</div>
						</div>
						
						<div class="floatleft min-width-thirtytree">
							<div class="floatleft fifty">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.lastReviewed" /></label>
								<div><xsl:value-of select="$managementInfo/lastReviewed" /></div>
							</div>
						</div>
	
						<div class="full clearboth"/>
						
						<div class="floatleft full">
							<div class="floatleft">
								<label class="nowrap"><xsl:value-of select="$i18n.managementInfo.aboutFlow" /></label>
								<div>
									<xsl:call-template name="replaceLineBreak">
										<xsl:with-param name="string" select="$managementInfo/aboutFlow"/>
									</xsl:call-template>
								</div>
							</div>
						</div>
					
					</xsl:when>
					<xsl:otherwise>
					
						<p><xsl:value-of select="$i18n.noManagementInfo"/></p>
					
					</xsl:otherwise>
				</xsl:choose>
			
				
			</div>
			
		</div>
			
		<xsl:if test="$isInternal = 'true'">
		
			<div class="showflow-wrapper">
			
				<a name="steps"/>
			
				<h2 class="title">
					<xsl:value-of select="$i18n.stepsAndQueries"/>
					<div class="floatright">
						<a class="comments-btn show-all-comments" href="#" title="{$i18n.ShowAllComments}">
							<xsl:value-of select="$i18n.ShowAllComments"/><img class="marginleft" src="{$imgPath}/visible.png" alt="" />
						</a>
						<a class="comments-btn hide-all-comments hidden" href="#" title="{$i18n.HideAllComments}">
							<xsl:value-of select="$i18n.HideAllComments"/><img class="marginleft" src="{$imgPath}/invisible.png" alt="" />
						</a>
					</div>
				</h2>
			
				<div class="showflow-content">
				
					<xsl:if test="$disableStructureManipulation = true()">
						<xsl:choose>
							<xsl:when test="Flow/flowInstanceCount > 0">
		
								<p><xsl:value-of select="$i18n.stepAndQueryManipulationDisabledHasInstances"/></p>
		
							</xsl:when>
							<xsl:when test="Flow/published = 'true'">
		
								<p><xsl:value-of select="$i18n.stepAndQueryManipulationDisabledIsPublished"/></p>
		
							</xsl:when>
						</xsl:choose>
					</xsl:if>
					
					<xsl:choose>
						<xsl:when test="Flow/Steps/Step">
							<ul class="steps" data-flowid="{flowID}">
								<xsl:apply-templates select="Flow/Steps/Step" mode="list">
									<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
								</xsl:apply-templates>
							</ul>
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="$i18n.flowContainsNoSteps"/></p>
						</xsl:otherwise>
					</xsl:choose>
					
					
					<xsl:if test="$disableStructureManipulation = false()">
						
						<br/>
						
						<div class="floatright marginright">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addstep/{Flow/flowID}" title="{$i18n.addStep}">
								<xsl:value-of select="$i18n.addStep"/>
								<img class="marginleft" src="{$imgPath}/add.png" alt="" />
							</a>
						</div>
						
						<xsl:if test="Flow/Steps/Step">
							<div class="floatright marginright clearboth">
								<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addquery/{Flow/flowID}" title="{$i18n.addQuery}">
									<xsl:value-of select="$i18n.addQuery"/>
									<img class="marginleft" src="{$imgPath}/form_add.png" alt="" />
								</a>
							</div>
							
							<div class="floatright marginright clearboth">
								<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importqueries/{Flow/flowID}" title="{$i18n.ImportQueries}">
									<xsl:value-of select="$i18n.ImportQueries"/>
									<img class="marginleft" src="{$imgPath}/form_import.png" alt="" />
								</a>
							</div>
							
							<div class="floatright marginright clearboth">
								<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortflow/{Flow/flowID}" title="{$i18n.sortStepsAndQueries}">
									<xsl:value-of select="$i18n.sortStepsAndQueries"/>
									<img class="marginleft" src="{$imgPath}/move.png" alt="" />
								</a>
							</div>
						</xsl:if>
				
					</xsl:if>
					
					<xsl:if test="Flow/Steps/Step">
						<br/>
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/testflowallsteps/{Flow/flowID}" title="{$i18n.PreviewQueries}" target="_blank">
								<xsl:value-of select="$i18n.PreviewQueries"/>
								<img class="marginleft" src="{$imgPath}/magnifying_glass.png" alt="" />
							</a>
						</div>
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{Flow/flowID}" title="{$i18n.testFlow}" target="_blank">
								<xsl:value-of select="$i18n.testFlow"/>
								<img class="marginleft" src="{$imgPath}/play.png" alt="" />
							</a>
						</div>
					</xsl:if>
				
				</div>
				
			</div>
			
		</xsl:if>
			
		<div class="showflow-wrapper">
	
			<a name="pdfform"/>
			
			<h2 class="title">
				<xsl:value-of select="$i18n.FlowForm"/>
			</h2>
			
			<div class="showflow-content">
			
				<xsl:choose>
					<xsl:when test="Flow/FlowForms/FlowForm">
					
						<div>
							<xsl:apply-templates select="Flow/FlowForms/FlowForm" mode="list"/>
						</div>
					
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:value-of select="$i18n.hasNoFlowForm"/>
						
					</xsl:otherwise>
				</xsl:choose>
				
				<div class="clearboth floatright marginright">
					
					<xsl:choose>
						<xsl:when test="Flow/skipOverview = 'true' and not(AllowSkipOverviewForFlowForms)">
						
<!-- 						TODO: Rmove AllowSkipOverviewForFlowForms 2020 when support for this function ends -->
<!-- 						TODO: When $i18n.MayNotAddFlowFormIfOverviewSkipIsSet is shown  $i18n.hasNoFlowForm doesn't have to shown. Fix when AllowSkipOverviewForFlowForms has been removed. -->
							<xsl:value-of select="$i18n.MayNotAddFlowFormIfOverviewSkipIsSet"/>
						
						</xsl:when>
						<xsl:otherwise>
						
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflowform/{Flow/flowID}" title="{$i18n.addFlowForm.link.title}">
								<xsl:value-of select="$i18n.addFlowForm.link.title"/>
								<img class="marginleft" src="{$imgPath}/add.png" alt="" />
							</a>
							
						</xsl:otherwise>
					</xsl:choose>
					
				</div>
				
				<xsl:if test="Flow/FlowForms/FlowForm">
					
					<div class="floatright marginright clearboth">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{Flow/flowID}" title="{$i18n.testFlow}" target="_blank">
							<xsl:value-of select="$i18n.testFlow"/>
							<img class="marginleft" src="{$imgPath}/play.png" alt="" />
						</a>
					</div>
					
				</xsl:if>
			
			</div>
			
		</div>
		
		<xsl:if test="$isInternal = 'true'">
		
			<div class="showflow-wrapper">
			
				<a name="statuses"/>
			
				<h2 class="title">
					<xsl:value-of select="$i18n.statuses"/>
				</h2>
			
				<div class="showflow-content">
				
					<xsl:choose>
						<xsl:when test="Flow/Statuses/Status">
						
							<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
								<thead>	
									<tr>
										<th><xsl:value-of select="$i18n.name" /></th>
										<th width="100"><xsl:value-of select="$i18n.SubmittedInstances" /></th>
										<th width="115"><xsl:value-of select="$i18n.NotSubmittedInstances" /></th>
										<th width="37" />
									</tr>
								</thead>
								<tbody>
								
									<xsl:apply-templates select="Flow/Statuses/Status" mode="list"/>
											
								</tbody>
							</table>
						
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="$i18n.flowHasNoStatuses"/></p>
						</xsl:otherwise>
					</xsl:choose>
					
					<div class="floatright marginright">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addstatus/{Flow/flowID}" title="{$i18n.addStatus}">
							<xsl:value-of select="$i18n.addStatus"/>
							<img class="marginleft" src="{$imgPath}/add.png" alt="" />
						</a>
					</div>
					
					<xsl:if test="Flow/Statuses/Status">
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortstatuses/{Flow/flowID}" title="{$i18n.sortStatuses}">
								<xsl:value-of select="$i18n.sortStatuses"/>
								<img class="marginleft" src="{$imgPath}/move.png" alt="" />
							</a>
						</div>
					</xsl:if>
					
					<xsl:if test="not(Flow/Statuses/Status/flowInstanceCount > 0)">
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/replaceflowstatuses/{Flow/flowID}" title="{$i18n.ReplaceFlowStatusesWithStandard}">
								<xsl:value-of select="$i18n.ReplaceFlowStatusesWithStandard"/>
								<img class="marginleft" src="{$imgPath}/move.png" alt="" />
							</a>
						</div>
					</xsl:if>
					
				</div>
				
			</div>
		
			<div class="showflow-wrapper">
			
				<a name="managers"/>
			
				<h2 class="title">
					<xsl:value-of select="$i18n.Managers"/>
				</h2>
			
				<div class="showflow-content">
				
					<xsl:choose>
						<xsl:when test="ManagerGroups or ManagerUsers">
						
							<p class="nomargin">
								<xsl:value-of select="$i18n.ManagersDescription"/>
								
								<xsl:if test="UsesAutoManagerAssignment">
									<xsl:text> </xsl:text>
									<xsl:value-of select="$i18n.FlowFamily.UsesAutoManagerAssignment"/>
								</xsl:if>
							</p>
							
							<xsl:if test="ManagerGroups">
								<span class="floatleft bold">
									<xsl:value-of select="$i18n.allowedGroups"/>
								</span>
								
								<xsl:apply-templates select="ManagerGroups/FlowFamilyManagerGroup[group]" mode="list"/>
							</xsl:if>
						
							<xsl:if test="ManagerUsers">
								<span class="floatleft bold">
									<xsl:value-of select="$i18n.allowedUsers"/>
								</span>
								
								<xsl:apply-templates select="ManagerUsers/FlowFamilyManager[user]" mode="list"/>
							</xsl:if>
						
						</xsl:when>
						<xsl:otherwise>
							
							<span class="floatleft">
								<xsl:value-of select="$i18n.NoManagers"/>
							</span>
							
						</xsl:otherwise>
					</xsl:choose>
					
					<div class="floatright marginright">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatemanagers/{Flow/FlowFamily/flowFamilyID}/{Flow/flowID}" title="{$i18n.UpdateFlowFamilyManagers}">
							<xsl:value-of select="$i18n.UpdateFlowFamilyManagers"/>
							<img class="marginleft" src="{$imgPath}/pen.png" alt="" />
						</a>
					</div>
					
					<div class="floatright marginright clearboth">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateautomanagerassignment/{Flow/FlowFamily/flowFamilyID}/{Flow/flowID}" title="{$i18n.UpdateAutoManagerAssignment}">
							<xsl:value-of select="$i18n.UpdateAutoManagerAssignment"/>
							<img class="marginleft" src="{$imgPath}/pen.png" alt="" />
						</a>
					</div>
				
				</div>
				
			</div>
	
			<xsl:if test="Notifications">
			
				<div class="showflow-wrapper">

					<a name="notifications"/>
			
					<h2 class="title">
						<xsl:value-of select="$i18n.Notifications"/>
					</h2>
			
					<div class="showflow-content">
					
						<xsl:value-of select="Notifications/HTML" disable-output-escaping="yes"/>
						
						<div class="floatright marginright">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatenotifications/{Flow/flowID}" title="{$i18n.UpdateNotificationSettings}">
								<xsl:value-of select="$i18n.UpdateNotificationSettings"/>
								<img class="marginleft" src="{$imgPath}/pen.png" alt="" />
							</a>
						</div>
					
					</div>
					
				</div>
			
			</xsl:if>
	
			<xsl:if test="ShowFlowSurveysHTML">
				
				<div class="showflow-wrapper">
			
					<a name="flowsurveys"/>
			
					<h2 class="title">
						<xsl:value-of select="$i18n.FlowSurveysTitle"/>
					</h2>
					
					<div class="showflow-content">
					
						<xsl:value-of select="ShowFlowSurveysHTML" disable-output-escaping="yes"/>
					
					</div>
					
				</div>
				
			</xsl:if>
	
			<div class="showflow-wrapper">
				
				<a name="messagetemplates"/>
			
				<h2 class="title">
					<xsl:value-of select="$i18n.MessageTemplates.title"/>
				</h2>
				
				<div class="showflow-content">
				
					<xsl:choose>
						<xsl:when test="Flow/FlowFamily/MessageTemplates">
							
							<table class="full coloredtable sortabletable oep-table" cellspacing="0">
								<thead>
									<tr>
										<th><xsl:value-of select="$i18n.MessageTemplate.name" /></th>
										<th><xsl:value-of select="$i18n.MessageTemplate.type" /></th>
										<th width="37" />
									</tr>
								</thead>
								<tbody>
									
									<xsl:apply-templates select="Flow/FlowFamily/MessageTemplates/MessageTemplate" mode="list"/>
									
								</tbody>
							</table>
							
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.MessageTemplates.noMessageTemplates" />
						</xsl:otherwise>
					</xsl:choose>
					
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addmessagetemplate/{Flow/flowID}" class="floatright">
						<xsl:value-of select="$i18n.MessageTemplates.add" />
						<img class="marginleft" src="{$imgPath}/add.png" alt="" />
					</a>
				
				</div>
				
			</div>
	
		</xsl:if>

		<div class="showflow-wrapper">
			
			<a name="versions"/>
			
			<h2 class="title">
				<xsl:value-of select="$i18n.versions"/>
			</h2>
			
			<div class="showflow-content">
			
				<p class="nomargin"><xsl:value-of select="$i18n.versions.description"/></p>
		
				<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/copyflow">
				
					<xsl:choose>
						<xsl:when test="FlowVersions">
						
							<table id="flowversionlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
								<thead>	
									<tr>
										<th width="10"></th>
										<th width="16">#</th>
										<th><xsl:value-of select="$i18n.name" /></th>
										
										<xsl:if test="/Document/UseCategories">
											<th><xsl:value-of select="$i18n.flowCategory" /></th>
										</xsl:if>
										
										<xsl:if test="$isInternal = 'true'">
											<th width="25"><xsl:value-of select="$i18n.steps" /></th>
											<th width="25"><xsl:value-of select="$i18n.queries" /></th>
											<th width="100"><xsl:value-of select="$i18n.SubmittedInstances" /></th>
											<th width="115"><xsl:value-of select="$i18n.NotSubmittedInstances" /></th>
										</xsl:if>
										
										<th width="75"><xsl:value-of select="$i18n.status" /></th>
										<th width="37" >
											<xsl:if test="PublishAccess">
												<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/unpublishflowfamily/{Flow/FlowFamily/flowFamilyID}/{Flow/flowID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.UnpublishFlowFamily}">
													<img class="alignmiddle marginright" src="{$imgPath}/disabled.png" alt="" />
												</a>
											</xsl:if>
										
										</th>
									</tr>
								</thead>
								<tbody>
									<xsl:apply-templates select="FlowVersions/Flow" mode="list-versions">
										<xsl:with-param name="isInternal" select="$isInternal" />	
									</xsl:apply-templates>
								</tbody>
							</table>
		
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="$i18n.flowHasNoOtherVersions"/></p>
						</xsl:otherwise>
					</xsl:choose>
					
					<br/>
					
					<div class="floatright marginright">
						<input type="button" value="{$i18n.importNewFlowVersion}" title="{$i18n.importNewFlowVersion}" onclick="window.location = '{/Document/requestinfo/currentURI}/{/Document/module/alias}/importversion/{Flow/flowID}'"/>
					</div>
					
					<div class="floatright margintop marginright hidden clearboth" id="add_new_version">
						<input type="submit" value="{$i18n.addNewVersion}"/>
					</div>
					
					<div class="floatright marginright margintop clearboth hidden" id="create_copy">
						<input type="submit" name="new_family" value="{$i18n.createNewFlow}"/>
					</div>
				
				</form>
			
			</div>
			
			<xsl:if test="count(FlowVersions/Flow) > 5">
				<div class="showflow-moreinfo-footer"><a href="#" class="show-more" data-rel="versions"><xsl:value-of select="$i18n.ShowOldVersions" /></a></div>
			</xsl:if>
			
		</div>
		
		<div class="extensionproviders-wrapper">
		
			<a name="extensions" />
		
			<h2>
				<xsl:value-of select="$i18n.ExtensionProviders" />
				<xsl:text> (</xsl:text>
				<xsl:value-of select="count(ExtensionProvider[Enabled = 'true'])" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$i18n.ExtensionProviders.by" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="count(ExtensionProvider)" />
				<xsl:text>&#160;<xsl:value-of select="$i18n.ExtensionProviders.activated" /></xsl:text>
				<xsl:text>)</xsl:text>
				<div class="floatright">
					<a id="show-inactive-providers" href="#"><xsl:value-of select="$i18n.ShowInactiveExtensionProviders" /></a>
					<a id="hide-inactive-providers" class="hidden" href="#"><xsl:value-of select="$i18n.HideInactiveExtensionProviders" /></a>
				</div>
			</h2>
		
		</div>
		
		<xsl:apply-templates select="ExtensionProvider" />
		
		<div class="showflow-wrapper">
			
			<a name="events"/>
		
			<h2 class="title">
				<xsl:value-of select="$i18n.Events.Title"/>
			</h2>
			
			<div class="showflow-content">
			
				<p class="nomargin"><xsl:value-of select="$i18n.Events.Description"/></p>
	
				<xsl:choose>
					<xsl:when test="FlowFamilyEvents/FlowFamilyEvent">
					
						<table class="full coloredtable sortabletable oep-table flowfamilyeventstable" cellspacing="0">
							<thead>	
								<tr>
									<th><xsl:value-of select="$i18n.Events.message" /></th>
									<th class="nowrap"><xsl:value-of select="$i18n.version.title" /></th>
									<th><xsl:value-of select="$i18n.Events.poster" /></th>
									<th><xsl:value-of select="$i18n.Events.added" /></th>
								</tr>
							</thead>
							<tbody>
							
								<xsl:apply-templates select="FlowFamilyEvents/FlowFamilyEvent" mode="list"/>
								
							</tbody>
						</table>
	
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$i18n.Events.FlowFamilyHasNoEvents"/></p>
					</xsl:otherwise>
				</xsl:choose>
				
				<div class="floatright marginright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflowfamilyevent/{Flow/flowID}" title="{$i18n.Events.Add}">
						<xsl:value-of select="$i18n.Events.Add"/>
						<img class="marginleft" src="{$imgPath}/add.png" alt="" />
					</a>
				</div>
					
				<div class="floatright marginright clearboth">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflowfamilyevents/{Flow/flowID}" title="{$i18n.Events.ShowAll}">
						<xsl:value-of select="$i18n.Events.ShowAll"/>
					</a>
				</div>
			
			</div>
				
		</div>
		
	</xsl:template>
	
	<xsl:template match="ExtensionProvider">
		
		<div class="showflow-wrapper extension">
			
			<xsl:if test="Enabled != 'true'"><xsl:attribute name="class">showflow-wrapper extension inactive hidden</xsl:attribute></xsl:if>
			
			<a name="extension-{ID}" />
			
			<h2 class="title">
				<xsl:value-of select="Title"/>
				<xsl:choose>
					<xsl:when test="Enabled = 'true'"><img src="{$imgPath}/check.png" alt="" class="floatright" title="{$i18n.ExtensionEnabled}" /></xsl:when>
					<xsl:otherwise><img src="{$imgPath}/disabled.png" alt="" class="floatright" title="{$i18n.ExtensionDisabled}" /></xsl:otherwise>
				</xsl:choose>
			</h2>
			
			<div class="showflow-content">
			
				<xsl:value-of select="HTML" disable-output-escaping="yes"/>
			
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowFamilyManager" mode="list">
		
		<div class="floatleft full marginbottom border">

			<xsl:choose>
				<xsl:when test="user/enabled='true'">
					<img class="alignmiddle" src="{$imgPath}/user.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignmiddle" src="{$imgPath}/user_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="user/firstname"/>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="user/lastname"/>
			
			<xsl:if test="user/username">
				<xsl:text>&#x20;(</xsl:text>
					<xsl:value-of select="user/username"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			
			<xsl:if test="restricted = 'true'">
			
				<span class="bigmarginleft">
					<xsl:value-of select="$i18n.Manager.restricted"/>
				</span>
			
				<xsl:if test="allowUpdatingManagers = 'true'">
					
					<span class="bigmarginleft">
						<xsl:value-of select="$i18n.Manager.allowUpdatingManagers"/>
					</span>
					
				</xsl:if>
				
				<xsl:if test="notifyGroupMembersPersonally = 'true'">
					
					<span class="bigmarginleft">
						<xsl:value-of select="$i18n.Manager.notifyGroupMembersPersonally"/>
					</span>
					
				</xsl:if>
			
			</xsl:if>
			
			<xsl:choose>
				<xsl:when test="validFromDate and not(Active = 'true') and validToDate">
				
					<span class="bigmarginleft">
						<xsl:value-of select="$i18n.Manager.validFromDate"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="validFromDate"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$i18n.Manager.validFromToDate"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="validToDate"/>
					</span>
				
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:if test="validFromDate and not(Active = 'true')">
					
						<span class="bigmarginleft">
							<xsl:value-of select="$i18n.Manager.validFromDate"/>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="validFromDate"/>
						</span>
						
					</xsl:if>
					
					<xsl:if test="validToDate">
					
						<span class="bigmarginleft">
							<xsl:value-of select="$i18n.Manager.validToDate"/>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="validToDate"/>
						</span>
					
					</xsl:if>
				
				</xsl:otherwise>
			</xsl:choose>
			
			
		</div>	
		
	</xsl:template>
	
	<xsl:template match="FlowFamilyManagerGroup" mode="list">
		
		<div class="floatleft full marginbottom border">

			<xsl:choose>
				<xsl:when test="group/enabled='true'">
					<img class="alignmiddle" src="{$imgPath}/group.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignmiddle" src="{$imgPath}/group_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="group/name"/>
			
			<xsl:if test="restricted = 'true'">
			
				<span class="bigmarginleft">
					<xsl:value-of select="$i18n.Manager.restricted"/>
				</span>
			
				<xsl:if test="allowUpdatingManagers = 'true'">
					
					<span class="bigmarginleft">
						<xsl:value-of select="$i18n.Manager.allowUpdatingManagers"/>
					</span>
					
				</xsl:if>
				
				<xsl:if test="notifyGroupMembersPersonally = 'true'">
					
					<span class="bigmarginleft">
						<xsl:value-of select="$i18n.Manager.notifyGroupMembersPersonally"/>
					</span>
					
				</xsl:if>
			
			</xsl:if>
			
		</div>	
		
	</xsl:template>
	
	<xsl:template match="FlowForm" mode="list">
	
		<div class="full">
		
			<xsl:variable name="name">
				<xsl:choose>
					<xsl:when test="name">
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="name"/>
					</xsl:when>
					<xsl:when test="count(../FlowForm) = 1"></xsl:when>
					<xsl:otherwise>
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="position()"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
		
			<xsl:choose>
				<xsl:when test="externalURL">

					<a href="{externalURL}" target="_blank">
						<img class="alignmiddle marginright" src="{$imgPath}/file.png" alt="" />
						<xsl:choose>
							<xsl:when test="downloadFormButtonText">
								<xsl:value-of select="downloadFormButtonText"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.DownloadFlowForm" />
							</xsl:otherwise>
						</xsl:choose>
						<xsl:value-of select="'&#160;'" />
						<xsl:text>(</xsl:text>
						<xsl:value-of select="$i18n.link" />
						<xsl:text>)</xsl:text>
						<xsl:value-of select="$name"/>
					</a>

				</xsl:when>
				<xsl:otherwise>

					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getflowform/{../../flowID}/{flowFormID}" target="_blank">
						<img class="alignmiddle marginright" alt="" >
							<xsl:attribute name="src">
								<xsl:value-of select="$imgPath" />
								<xsl:choose>
									<xsl:when test="fileExtension = 'pdf'">/pdf.png</xsl:when>
									<xsl:when test="fileExtension = 'xls' or fileExtension = 'xlsx'">/xls.png</xsl:when>
									<xsl:otherwise>/file.png</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</img>
						<xsl:choose>
							<xsl:when test="downloadFormButtonText">
								<xsl:value-of select="downloadFormButtonText"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.DownloadFlowForm" />
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:if test="formattedSize">
							<xsl:value-of select="' ('" />
							<xsl:value-of select="formattedSize" />
							<xsl:value-of select="')'" />
						</xsl:if>
						
						<xsl:value-of select="$name"/>
					</a>

				</xsl:otherwise>
			</xsl:choose>
			
			<div class="floatright marginright">
		
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflowform/{flowFormID}" title="{$i18n.updateFlowForm.link.title}">
					<img class="marginleft" src="{$imgPath}/pen.png" alt="" />
				</a>
				
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowform/{flowFormID}" title="{$i18n.deleteFlowForm.link.title}: {$name}" onclick="return confirmHyperlinkPost(this)" >
				
					<xsl:if test="../../MayNotRemoveFlowFormIfNoSteps">
						<xsl:attribute name="onclick">alert('<xsl:value-of select="$i18n.MayNotRemoveFlowFormIfNoSteps"/>'); return false;</xsl:attribute>
						<xsl:attribute name="href">
							<xsl:value-of select="'#'"/>
						</xsl:attribute>
					</xsl:if>
					
					<xsl:variable name="img">
						<xsl:choose>
							<xsl:when test="../../MayNotRemoveFlowFormIfNoSteps">
								
								<xsl:value-of select="'delete_gray.png'"/>
								
							</xsl:when>
							<xsl:otherwise>
								
								<xsl:value-of select="'delete.png'"/>
								
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					
					<img class="marginleft" src="{$imgPath}/{$img}" alt="" />
				</a>
			
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="MessageTemplate" mode="list">
	
		<tr>
			<td>
				<xsl:value-of select="name"/>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="type = 'EXTERNAL'">
						<xsl:value-of select="$i18n.MessageTemplate.type.external"/>
					</xsl:when>
					<xsl:when test="type = 'INTERNAL'">
						<xsl:value-of select="$i18n.MessageTemplate.type.internal"/>
					</xsl:when>
					<xsl:when test="type = 'ALL'">
						<xsl:value-of select="$i18n.MessageTemplate.type.all"/>
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatemessagetemplate/{../../../flowID}/{templateID}" title="{$i18n.MessageTemplates.update}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
				<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletemessagetemplate/{../../../flowID}/{templateID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.MessageTemplates.delete}: {name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="Flow" mode="list-versions">
		
		<xsl:param name="isInternal" />
			
		<tr data-rel="versions">
			<xsl:attribute name="class">
				<xsl:if test="published = 'false' or enabled = 'false'">disabled </xsl:if>
				<xsl:if test="position() > 5">hidden</xsl:if>
			</xsl:attribute>
			<td style="padding-right: 0px;">
				<input type="radio" name="flowID" value="{flowID}" onclick="$('#add_new_version').show();$('#create_copy').show();"/>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="version" /></a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="name" /></a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td>
					<xsl:if test="Category">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="Category/name" /></a>
					</xsl:if>
				</td>
			</xsl:if>
			
			<xsl:if test="$isInternal = 'true'">
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="count(Steps/Step)" /></a>
				</td>
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="count(Steps/Step/QueryDescriptors/QueryDescriptor)" /></a>
				</td>
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="flowSubmittedInstanceCount" /></a>
				</td>
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="flowInstanceCount - flowSubmittedInstanceCount" /></a>
				</td>
			</xsl:if>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}">
				
					<xsl:choose>
						<xsl:when test="enabled = 'false'">
							<xsl:value-of select="$i18n.disabled" />
						</xsl:when>
						<xsl:when test="published = 'true'">
							<xsl:value-of select="$i18n.published" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.notPublished" />
						</xsl:otherwise>
					</xsl:choose>
					
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/exportflow/{flowID}" title="{$i18n.exportFlow.title}: {name}">
					<img src="{$imgPath}/xml.png" alt="" />
				</a>
			
				<xsl:choose>
					<xsl:when test="flowInstanceCount > 0">

						<a class="marginleft" href="#" onclick="alert('{$i18n.deleteFlowDisabledHasInstances}'); return false;" title="{$i18n.deleteFlowDisabledHasInstances}">
							<img src="{$imgPath}/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:when test="published = 'true' and enabled = 'true'">

						<a class="marginleft" href="#" onclick="alert('{$i18n.deleteFlowDisabledIsPublished}'); return false;" title="{$i18n.deleteFlowDisabledIsPublished}">
							<img src="{$imgPath}/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:otherwise>
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflow/{flowID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteFlow.title}: {name}">
							<img src="{$imgPath}/delete.png" alt="" />
						</a>

					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="Step" mode="list">
	
		<xsl:param name="disableStructureManipulation"/>
	
		<li class="step" data-cookiesuffix="{../../flowID}-{stepID}">
			
			<div class="title">
				<span class="index"><xsl:value-of select="position()" /></span>
				<span class="name">
					<xsl:value-of select="name"/>
					<xsl:text> (</xsl:text>
					
					<xsl:variable name="queryCount" select="count(QueryDescriptors/QueryDescriptor)"/>
					
					<xsl:value-of select="$queryCount"/>
					
					<xsl:text> </xsl:text>
					
					<xsl:choose>
						<xsl:when test="$queryCount = 1">
							<xsl:value-of select="$i18n.Step.OneQuery"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.Step.MultipleQueries"/>
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:text>)</xsl:text>
				</span>
				<span class="tools">
					<xsl:if test="$disableStructureManipulation = false()">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importqueries/{../../flowID}?step={stepID}" title="{$i18n.ImportQueriesInStep.title}: {name}">
							<img src="{$imgPath}/form_import.png" alt="" />
						</a>
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addquery/{../../flowID}?step={stepID}" title="{$i18n.AddQueryInStep.title}: {name}">
							<img src="{$imgPath}/form_add.png" alt="" />
						</a>
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestep/{stepID}" title="{$i18n.updateStep.title}: {name}">
							<img src="{$imgPath}/pen.png" alt="" />
						</a>
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestep/{stepID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteStep.title}: {name}">
							<img src="{$imgPath}/delete.png" alt="" />
						</a>
					</xsl:if>
				</span>		
			</div>
		
			<xsl:if test="QueryDescriptors/QueryDescriptor">
				<ul class="querydescriptors">
					<xsl:apply-templates select="QueryDescriptors/QueryDescriptor" mode="list">
						<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
					</xsl:apply-templates>				
				</ul>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="QueryDescriptor" mode="list">
	
		<xsl:param name="disableStructureManipulation"/>
	
		<li class="querydescriptor" data-queryid="{queryID}">	
			<div class="title">
				<span class="index"><xsl:value-of select="position()" /><xsl:text>.</xsl:text></span>
				
				<div>
					<span class="name">
						<xsl:value-of select="name"/>
						<span class="tiny">
							<xsl:text>&#x20;(</xsl:text>
							
							<xsl:variable name="queryTypeID" select="queryTypeID"/>
							
							<xsl:variable name="queryType" select="../../../../../QueryTypes/QueryTypeDescriptor[queryTypeID=$queryTypeID]/name"/>
							
							<xsl:choose>
								<xsl:when test="$queryType">
									<xsl:value-of select="$queryType"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.unknownQueryType"/>
								</xsl:otherwise>
							</xsl:choose>
							
							<xsl:text>)</xsl:text>
						</span>
					</span>
					
					<xsl:if test="comment">
						<span class="comment">
							<xsl:value-of select="comment" />
						</span>
					</xsl:if>
				</div>
				
				<span class="tools">
					<xsl:choose>
						<xsl:when test="$disableStructureManipulation = false()">
							
							<xsl:if test="count(EvaluatorDescriptors/EvaluatorDescriptor) > 1">
								<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortevaluators/{queryID}" title="{$i18n.SortEvaluators.title}: {name}">
									<img src="{$imgPath}/cog_move.png" alt="" />
								</a>
							</xsl:if>
							
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addevaluator/{queryID}" title="{$i18n.addEvaluator.title}: {name}">
								<img src="{$imgPath}/cog_add.png" alt="" />
							</a>				
	
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/exportquery/{queryID}" title="{$i18n.ExportQuery.title}: {name}">
								<img src="{$imgPath}/download.png" alt="" />
							</a>
	
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/testflowallsteps/{../../../../flowID}#query_{queryID}" title="{$i18n.PreviewQuery.title}: {name}" target="_blank">
								<img src="{$imgPath}/magnifying_glass.png" alt="" />
							</a>
	
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatequery/{queryID}" title="{$i18n.updateQuery.title}: {name}">
								<img src="{$imgPath}/pen.png" alt="" />
							</a>
							
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletequery/{queryID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteQuery.title}: {name}">
								<img src="{$imgPath}/delete.png" alt="" />
							</a>
							
						</xsl:when>
						<xsl:otherwise>
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/testflowallsteps/{../../../../flowID}#query_{queryID}" title="{$i18n.PreviewQuery.title}: {name}" target="_blank">
								<img src="{$imgPath}/magnifying_glass.png" alt="" />
							</a>
							<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/exportquery/{queryID}" title="{$i18n.ExportQuery.title}: {name}">
								<img src="{$imgPath}/download.png" alt="" />
							</a>
						</xsl:otherwise>
					</xsl:choose>

				</span>
			</div>
			
			<xsl:if test="EvaluatorDescriptors/EvaluatorDescriptor">
				<ul class="evaluators">
					<xsl:apply-templates select="EvaluatorDescriptors/EvaluatorDescriptor" mode="list">
						<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
					</xsl:apply-templates>				
				</ul>
			</xsl:if>					
		</li>
	
	</xsl:template>
	
	<xsl:template match="EvaluatorDescriptor" mode="list">
	
		<xsl:param name="disableStructureManipulation"/>
	
		<li>	
			
			<div class="title">
			
				<span class="icon">
					<xsl:choose>
						<xsl:when test="enabled = 'true'"><img src="{$imgPath}/cog.png" alt="" /></xsl:when>
						<xsl:otherwise><img src="{$imgPath}/cog_disabled.png" alt="" /></xsl:otherwise>
					</xsl:choose>
				</span>
				
				<div>
					<span class="marginright">
						<xsl:value-of select="name"/>
					</span>
					
					<span class="tiny">
					
						<xsl:text>&#x20;(</xsl:text>
					
						<xsl:variable name="evaluatorTypeID" select="evaluatorTypeID"/>
						
						<xsl:variable name="evaluatorType" select="../../../../../../../EvaluatorTypes/EvaluatorTypeDescriptor[evaluatorTypeID=$evaluatorTypeID]/name"/>
						
						<xsl:choose>
							<xsl:when test="$evaluatorType">
								<xsl:value-of select="$evaluatorType"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.unknownEvaluatorType"/>
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:text>)</xsl:text>
						
					</span>
					
					<xsl:if test="comment">
						<span class="comment">
							<xsl:value-of select="comment" />
						</span>
					</xsl:if>
				</div>
				
				<span class="tools">
					<xsl:if test="$disableStructureManipulation = false()">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateevaluator/{evaluatorID}" title="{$i18n.updateEvaluator.title}: {name}">
							<img src="{$imgPath}/pen.png" alt="" />
						</a>
						
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteevaluator/{evaluatorID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteEvaluator.title}: {name}">
							<img src="{$imgPath}/delete.png" alt="" />
						</a>
					</xsl:if>
				</span>
			</div>
			
			<xsl:if test="EvaluatorDescriptors/EvaluatorDescriptor">
				<ul class="evaluators">
					<xsl:apply-templates select="EvaluatorDescriptors/EvaluatorDescriptor" mode="list">
						<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
					</xsl:apply-templates>
				</ul>
			</xsl:if>
		</li>
	
	</xsl:template>
		
	<xsl:template match="Status" mode="list">
	
		<tr>
			<td>
				<xsl:value-of select="name"/>
			</td>
			<td>
				<xsl:value-of select="flowSubmittedInstanceCount"/>
			</td>
			<td>
				<xsl:value-of select="flowInstanceCount - flowSubmittedInstanceCount"/>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestatus/{statusID}" title="{$i18n.updateStatus.link.title}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
		
				<xsl:choose>
					<xsl:when test="flowInstanceCount > 0">

						<a class="marginleft" href="#" onclick="alert('{$i18n.deleteStatusDisabledHasInstances}'); return false;" title="{$i18n.deleteStatusDisabledHasInstances}">
							<img src="{$imgPath}/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:otherwise>
					
						<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestatus/{statusID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.deleteStatus.link.title}: {name}">
							<img src="{$imgPath}/delete.png" alt="" />
						</a>
					
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="FlowFamilyEvent" mode="list">
		
		<tr>
			<td>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="message"/>
				</xsl:call-template>
			</td>
			<td>
				<xsl:value-of select="flowVersion" />
			</td>
			<td>
				<xsl:call-template name="PrintAdminPostedBy">
					<xsl:with-param name="poster" select="poster"/>
				</xsl:call-template>
			</td>
			<td>
				<xsl:value-of select="added" />
			</td>
		</tr>
	
	</xsl:template>
		
	<xsl:template match="AddFlow">
	
		<h1><xsl:value-of select="$i18n.AddFlow.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form id="flowForm" method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflow">
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="typeOfFlow" class="floatleft full">
					<xsl:value-of select="$i18n.typeOfFlow" />
				</label>
				
				<div class="floatleft full">
					<select id="typeOfFlow" name="typeOfFlow">
						<option value="INTERNAL">
							<xsl:if test="requestparameters/parameter[name='typeOfFlow']/value = 'INTERNAL'">
								<xsl:attribute name="selected"/>
							</xsl:if>
							<xsl:value-of select="$i18n.internal" />
						</option>
						<option value="EXTERNAL">
							<xsl:if test="requestparameters/parameter[name='typeOfFlow']/value = 'EXTERNAL'">
								<xsl:attribute name="selected"/>
							</xsl:if>
							<xsl:value-of select="$i18n.external" />
						</option>
					</select>
				</div>
				
			</div>
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="flowtype" class="floatleft full">
					<xsl:value-of select="$i18n.flowType" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'flowtype'"/>
						<xsl:with-param name="name" select="'flowTypeID'"/>
						<xsl:with-param name="valueElementName" select="'flowTypeID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="FlowTypes/FlowType" />
						<xsl:with-param name="selectedValue" select="Flow/FlowType/flowTypeID"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<xsl:if test="/Document/UseCategories">
				<div class="floatleft full bigmarginbottom">
					
					<label for="flowCategory" class="floatleft full">
						<xsl:value-of select="$i18n.flowCategory" />
					</label>
					
					<xsl:apply-templates select="FlowTypes/FlowType" mode="categories" />
					
				</div>
			</xsl:if>
				
			<xsl:call-template name="flowForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddFlow.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="FlowType" mode="categories">
		
		<xsl:param name="selectedValue" select="null" />
		<xsl:param name="requestparameters" select="../../requestparameters" />
		
		
		<div id="flowTypeCategories_{flowTypeID}" class="flowTypeCategories">
			<xsl:choose>
				<xsl:when test="Categories/Category">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="concat('categories_',../flowTypeID)"/>
						<xsl:with-param name="name" select="'categoryID'"/>
						<xsl:with-param name="valueElementName" select="'categoryID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="Categories/Category" />
						<xsl:with-param name="addEmptyOption" select="$i18n.noCategory" />
						<xsl:with-param name="requestparameters" select="$requestparameters" />
						<xsl:with-param name="selectedValue" select="$selectedValue" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.noCategories" />
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="UpdateFlow">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateFlow.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
			<xsl:text>&#x20;(</xsl:text>
			<xsl:value-of select="$i18n.flowVersion"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/version"/>
			<xsl:text>)</xsl:text>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="flowForm" method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflow/{Flow/flowID}">
		
			<xsl:if test="Flow/externalLink">
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="'typeOfFlow'" />
					<xsl:with-param name="value" select="'EXTERNAL'" />
				</xsl:call-template>
			</xsl:if>
		
			<xsl:if test="/Document/UseCategories">
				<div class="floatleft full bigmarginbottom">
					
					<label for="flowtype" class="floatleft full">
						<xsl:value-of select="$i18n.flowCategory" />
					</label>
					
					<xsl:apply-templates select="Flow/FlowType" mode="categories">
						<xsl:with-param name="selectedValue" select="Flow/Category/categoryID" />
					</xsl:apply-templates>
					
				</div>
			</xsl:if>
		
			<xsl:call-template name="flowForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateFlow.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="flowForm">
		
		<xsl:variable name="isInternal">
			<xsl:if test="not(Flow/externalLink)">true</xsl:if>
		</xsl:variable>
		
		<script>
			$(function() {
				$( "#publishDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.publishDate"/>',
					changeMonth: true,
            		changeYear: true,
            		showWeek: true,
				});
				
				$( "#unPublishDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.unPublishDate"/>',
					changeMonth: true,
            		changeYear: true,
            		showWeek: true,
				});
			});	
		</script>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
				<span class="required">*</span>
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="Flow" />
				</xsl:call-template>
			</div>
		</div>
		
		<xsl:if test="not(/Document/UpdateFlow and $isInternal = 'true')">
		
			<fieldset class="external">
				<legend><xsl:value-of select="$i18n.Flow.link" /></legend>
		
				<div class="floatleft full bigmarginbottom">
					
					<xsl:if test="/Document/AddFlow">
						<xsl:attribute name="class">floatleft full bigmarginbottom external hidden</xsl:attribute>
					</xsl:if>
					
					<label for="name" class="floatleft full">
						<xsl:value-of select="$i18n.externalLink" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'externalLink'"/>
							<xsl:with-param name="name" select="'externalLink'"/>
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
					</div>
				</div>
			
			</fieldset>
		</xsl:if>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.Flow.activationAndPublishing" /></legend>
		
			<xsl:if test="LacksPublishAccess">
				<div class="floatleft full">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.LacksPublishAccess" />
					</label>
				</div>
			</xsl:if>
			
			<div class="floatleft full">
				<xsl:if test="LacksPublishAccess">
					<xsl:attribute name="class">floatleft full bigmarginbottom opacity-fifty</xsl:attribute>
				</xsl:if>
			
				<div class="floatleft">
					
					<label for="publishDate" class="floatleft">
						<xsl:value-of select="$i18n.publishDate" />
					</label>
					
					<div class="floatleft clearboth">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'publishDate'" />
							<xsl:with-param name="id" select="'publishDate'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="size" select="10" />
							<xsl:with-param name="disabled" select="LacksPublishAccess" />
							<xsl:with-param name="class" select="'marginright'" />
						</xsl:call-template>
					</div>
				</div>
				
				<div class="bigmarginleft floatleft">
					
					<label for="unPublishDate" class="floatleft">
						<xsl:value-of select="$i18n.unPublishDate" />
					</label>
					
					<div class="floatleft clearboth">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'unPublishDate'" />
							<xsl:with-param name="id" select="'unPublishDate'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="size" select="10" />
							<xsl:with-param name="disabled" select="LacksPublishAccess" />
							<xsl:with-param name="class" select="'marginright'" />
						</xsl:call-template>
					</div>
				</div>
			
			</div>
			
			<div class="floatleft full bigmarginbottom margintop">
				
				<xsl:if test="LacksPublishAccess">
					<xsl:attribute name="class">floatleft full bigmarginbottom margintop opacity-fifty</xsl:attribute>
				</xsl:if>
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'enabled'" />
						<xsl:with-param name="id" select="'enabled'" />
						<xsl:with-param name="element" select="Flow" /> 
						
						<!-- Disable if we are in add mode -->
						<xsl:with-param name="disabled" select="LacksPublishAccess or (/Document/AddFlow and $isInternal = 'true')" />
					</xsl:call-template>
					
					<label for="enabled">
						<xsl:value-of select="$i18n.enableFlow" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom margintop">
				
				<xsl:if test="LacksPublishAccess">
					<xsl:attribute name="class">floatleft full bigmarginbottom margintop opacity-fifty</xsl:attribute>
				</xsl:if>
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'lockSubmitForUnpublishedSavedFlow'" />
						<xsl:with-param name="id" select="'lockSubmitForUnpublishedSavedFlow'" />
						<xsl:with-param name="element" select="Flow" />
						
					</xsl:call-template>
					
					<label for="lockSubmitForUnpublishedSavedFlow">
						<xsl:value-of select="$i18n.disableUnpublishedSavedFlows" />
					</label>
				</div>
			</div>
		
		</fieldset>
		
		<xsl:if test="$isInternal = 'true'">
		
			<fieldset class="internal">
			
				<legend><xsl:value-of select="$i18n.Flow.preview" /></legend>	
		
				<div class="floatleft full bigmarginbottom">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'usePreview'" />
							<xsl:with-param name="id" select="'usePreview'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="usePreview">
							<xsl:value-of select="$i18n.usePreview" />
						</label>
					</div>
				</div>
			
			</fieldset>
		
		</xsl:if>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.Flow.authenticationSettings" /></legend>
		
			<div class="floatleft full bigmarginbottom">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'requireAuthentication'" />
						<xsl:with-param name="id" select="'requireAuthentication'" />
						<xsl:with-param name="element" select="Flow" />
					</xsl:call-template>
					
					<label for="requireAuthentication">
						<xsl:value-of select="$i18n.requireAuthentication" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'showLoginQuestion'" />
						<xsl:with-param name="name" select="'showLoginQuestion'" />
						<xsl:with-param name="element" select="Flow" />
					</xsl:call-template>
					
					<label for="showLoginQuestion">
						<xsl:value-of select="$i18n.Flow.showLoginQuestion" />
					</label>
				</div>
			
				<div class="floatleft full margintop">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'loginQuestionText'" />
						<xsl:with-param name="name" select="'loginQuestionText'" />
						<xsl:with-param name="class" select="'flow-ckeditor'" />
						<xsl:with-param name="value" >
							<xsl:choose>
								<xsl:when test="Flow/loginQuestionText">
									<xsl:value-of select="Flow/loginQuestionText" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$java.defaultFlowStartLoginQuestionText" disable-output-escaping="yes"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
		    </div>
		    
		    <script type="text/javascript">
					$(function() {
						
						var authCheckbox = $("#requireAuthentication");
						
						var showLoginQuestionCheckbox = function() {
							
							var checked = authCheckbox.prop("checked");
							
							$("#showLoginQuestion").parent().parent().toggle(!checked);
							
							if (checked) {
								$("#showLoginQuestion").prop("checked", false).trigger("change");
							}
						};
						
						authCheckbox.on("change", showLoginQuestionCheckbox);
						showLoginQuestionCheckbox();
						
						
						var checkbox = $("#showLoginQuestion");
						
						var showLoginQuestionText = function() {
							
							var checked = checkbox.prop("checked");
							
							$("#loginQuestionText").parent().toggle(checked);
						};
						
						checkbox.on("change", showLoginQuestionText);
						showLoginQuestionText();
					});
				</script>
			</div>
		
			<xsl:if test="ForeignIDsBlocked">
				<div class="floatleft full bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'allowForeignIDs'" />
							<xsl:with-param name="id" select="'allowForeignIDs'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="allowForeignIDs">
							<xsl:value-of select="$i18n.Flow.allowForeignIDs" />
						</label>
					</div>
				</div>
			</xsl:if>
		
		</fieldset>
			
		<xsl:if test="$isInternal = 'true'">
			
			<fieldset class="internal">
				<legend><xsl:value-of select="$i18n.Flow.signing" /></legend>
			
				<div class="floatleft full bigmarginbottom">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'requireSigning'" />
							<xsl:with-param name="id" select="'requireSigning'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="requireSigning">
							<xsl:value-of select="$i18n.requireSigning" />
						</label>
					</div>
				</div>
				
				<xsl:if test="SupportsSequentialSigning">
					<div class="floatleft full bigmarginbottom margintop">
					
						<div class="floatleft">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="'useSequentialSigning'" />
								<xsl:with-param name="id" select="'useSequentialSigning'" />
								<xsl:with-param name="element" select="Flow" />
							</xsl:call-template>
							
							<label for="useSequentialSigning">
								<xsl:value-of select="$i18n.Flow.useSequentialSigning" />
							</label>
						</div>
					</div>
					
					<div class="floatleft full bigmarginbottom margintop">
					
						<div class="floatleft">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="'skipPosterSigning'" />
								<xsl:with-param name="id" select="'skipPosterSigning'" />
								<xsl:with-param name="element" select="Flow" />
							</xsl:call-template>
							
							<label for="skipPosterSigning">
								<xsl:value-of select="$i18n.Flow.skipPosterSigning" />
								<xsl:text> </xsl:text>
								<xsl:value-of select="$i18n.Flow.skipPosterSigning.description" />
							</label>
						</div>
					</div>
					
					<div class="floatleft full bigmarginbottom margintop">
					
						<div class="floatleft">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="'allowPosterMultipartSigning'" />
								<xsl:with-param name="id" select="'allowPosterMultipartSigning'" />
								<xsl:with-param name="element" select="Flow" />
							</xsl:call-template>
							
							<label for="allowPosterMultipartSigning">
								<xsl:value-of select="$i18n.Flow.allowPosterMultipartSigning" />
							</label>
						</div>
					</div>
				</xsl:if>
				
				<div class="floatleft full bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'appendSigningSignatureToPDF'" />
							<xsl:with-param name="id" select="'appendSigningSignatureToPDF'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="appendSigningSignatureToPDF">
							<xsl:value-of select="$i18n.Flow.appendSigningSignatureToPDF" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'showPreviousSignaturesToSigners'" />
							<xsl:with-param name="id" select="'showPreviousSignaturesToSigners'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="showPreviousSignaturesToSigners">
							<xsl:value-of select="$i18n.Flow.showPreviousSignaturesToSigners" />
						</label>
					</div>
				</div>
			
			</fieldset>
			
			<fieldset class="internal">
				<legend><xsl:value-of select="$i18n.Flow.paymentSettings" /></legend>		
			
				<div class="floatleft full bigmarginbottom">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'paymentSupportEnabled'" />
							<xsl:with-param name="id" select="'paymentSupportEnabled'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="paymentSupportEnabled">
							<xsl:value-of select="$i18n.PaymentSupportEnabled" />
						</label>
					</div>
				</div>
				
			</fieldset>
			
			<xsl:choose>
				<xsl:when test="SubmitSurveyEnabled">
				
					<fieldset class="internal">
						<legend><xsl:value-of select="$i18n.Flow.surveys" /></legend>
				
						<div class="floatleft full bigmarginbottom">
					
							<div class="floatleft">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="name" select="'showSubmitSurvey'" />
									<xsl:with-param name="id" select="'showSubmitSurvey'" />
									<xsl:with-param name="element" select="Flow" />       
								</xsl:call-template>
								
								<label for="showSubmitSurvey">
									<xsl:value-of select="$i18n.showSubmitSurvey" />
								</label>
							</div>
						</div>
					</fieldset>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'showSubmitSurvey'" />
						<xsl:with-param name="name" select="'showSubmitSurvey'" />
						<xsl:with-param name="element" select="Flow" /> 
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>			
			
			<fieldset class="internal">
				<legend><xsl:value-of select="$i18n.Flow.messagesAndNotes" /></legend>
			
				<div class="floatleft full bigmarginbottom">
					
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideExternalMessages'" />
							<xsl:with-param name="id" select="'hideExternalMessages'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideExternalMessages">
							<xsl:value-of select="$i18n.hideExternalMessages" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop">
					
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideExternalMessageAttachments'" />
							<xsl:with-param name="id" select="'hideExternalMessageAttachments'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideExternalMessageAttachments">
							<xsl:value-of select="$i18n.hideExternalMessageAttachments" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom bigmarginleft">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'showExternalMessageAttachmentsForManager'" />
							<xsl:with-param name="id" select="'showExternalMessageAttachmentsForManager'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="showExternalMessageAttachmentsForManager">
							<xsl:value-of select="$i18n.showExternalMessageAttachmentsForManager" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop">
					
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'readReceiptsEnabled'" />
							<xsl:with-param name="id" select="'readReceiptsEnabled'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="readReceiptsEnabled">
							<xsl:value-of select="$i18n.readReceiptsEnabled" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop">
					
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'readReceiptsEnabledByDefault'" />
							<xsl:with-param name="id" select="'readReceiptsEnabledByDefault'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="readReceiptsEnabledByDefault">
							<xsl:value-of select="$i18n.readReceiptsEnabledByDefault" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideInternalMessages'" />
							<xsl:with-param name="id" select="'hideInternalMessages'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideInternalMessages">
							<xsl:value-of select="$i18n.hideInternalMessages" />
						</label>
					</div>
				</div>
			
			</fieldset>
			
			<fieldset class="internal">
				<legend><xsl:value-of select="$i18n.Flow.hideFlowInstances" /></legend>
				
				<div class="floatleft full bigmarginbottom">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideFromUser'" />
							<xsl:with-param name="id" select="'hideFromUser'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideFromUser">
							<xsl:value-of select="$i18n.Flow.hideFromUser" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideFromManager'" />
							<xsl:with-param name="id" select="'hideFromManager'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideFromManager">
							<xsl:value-of select="$i18n.Flow.hideFromManager" />
						</label>
					</div>
				</div>
				
			</fieldset>
			
		</xsl:if>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.Flow.hideDetailsAndFunctions" /></legend>
			
			<div class="floatleft full bigmarginbottom">
			
				<xsl:variable name="disableSkip" select="Flow/FlowForms/FlowForm and not(AllowSkipOverviewForFlowForms)"/>
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'skipOverview'" />
						<xsl:with-param name="id" select="'skipOverview'" />
						<xsl:with-param name="element" select="Flow" />
						<xsl:with-param name="disabled" select="$disableSkip" />
					</xsl:call-template>
					
					<label for="skipOverview">
						<xsl:value-of select="$i18n.skipOverview" />
						
						<xsl:if test="$disableSkip">
							<xsl:value-of select="' ('" />
							<xsl:value-of select="$i18n.MayNotSetOverviewIfFlowFormIsSet.description" />
							<xsl:value-of select="')'" />
						</xsl:if>
					</label>
				</div>
			</div>				
			
			<xsl:if test="$isInternal = 'true'">
			
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideFlowInstanceIDFromUser'" />
							<xsl:with-param name="id" select="'hideFlowInstanceIDFromUser'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideFlowInstanceIDFromUser">
							<xsl:value-of select="$i18n.Flow.hideFlowInstanceIDFromUser" />
						</label>
					</div>
				</div>				
				
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideManagerDetails'" />
							<xsl:with-param name="id" select="'hideManagerDetails'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideManagerDetails">
							<xsl:value-of select="$i18n.hideManagerDetails" />
						</label>
					</div>
				</div>
			
			</xsl:if>
				
			<div class="floatleft full bigmarginbottom margintop">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'hideFromOverview'" />
						<xsl:with-param name="id" select="'hideFromOverview'" />
						<xsl:with-param name="element" select="Flow" />
					</xsl:call-template>
					
					<label for="hideFromOverview">
						<xsl:value-of select="$i18n.hideFromOverview" />
					</label>
				</div>
			</div>				
			
			<xsl:if test="$isInternal = 'true'">
				
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideSubmitStepText'" />
							<xsl:with-param name="id" select="'hideSubmitStepText'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideSubmitStepText">
							<xsl:value-of select="$i18n.hideSubmitStepText" />
						</label>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideSaveButton'" />
							<xsl:with-param name="id" select="'hideSaveButton'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="hideSaveButton">
							<xsl:value-of select="$i18n.hideSaveButton" />
						</label>
					</div>
				</div>
			
			</xsl:if>
			
		</fieldset>
		
		<xsl:if test="$isInternal = 'true'">
		
			<fieldset>
				<legend><xsl:value-of select="$i18n.Flow.otherSettings" /></legend>
				
				<div class="floatleft full bigmarginbottom">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'alwaysStartFromFirstStep'" />
							<xsl:with-param name="id" select="'alwaysStartFromFirstStep'" />
							<xsl:with-param name="element" select="Flow" />
						</xsl:call-template>
						
						<label for="alwaysStartFromFirstStep">
							<xsl:value-of select="$i18n.Flow.alwaysStartFromFirstStep" />
						</label>
					</div>
				</div>				
	
			</fieldset>
		
		</xsl:if>
				
		<xsl:if test="$isInternal = 'true'">
			
			<!-- Disable if we are NOT in add mode -->
			<xsl:if test="/Document/AddFlow">
			
				<fieldset class="internal">
					<legend><xsl:value-of select="$i18n.Flow.statuses" /></legend>
			
					<div class="floatleft full">
					
						<div class="floatleft">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="'addstandardstatuses'" />
								<xsl:with-param name="id" select="'addstandardstatuses'" />
								<xsl:with-param name="checked" select="'true'" />
							</xsl:call-template>
							
							<label for="addstandardstatuses">
								<xsl:value-of select="$i18n.AddStandardStatuses" />
							</label>
						</div>
					</div>
					
					<div class="floatleft full bigmarginbottom">
						
						<label for="statusGroupID" class="floatleft full">
							<xsl:value-of select="$i18n.AddStandardStatuses.StandardStatusGroup" />
							<span class="required">*</span>
						</label>
						
						<div class="floatleft full">
							<xsl:call-template name="createDropdown">
								<xsl:with-param name="id" select="'statusGroupID'"/>
								<xsl:with-param name="name" select="'statusGroupID'"/>
								<xsl:with-param name="valueElementName" select="'statusGroupID'" />
								<xsl:with-param name="labelElementName" select="'name'" />
								<xsl:with-param name="element" select="StandardStatusGroups/StandardStatusGroup" />
								<xsl:with-param name="addEmptyOption" select="$i18n.AddStandardStatuses.StandardStatusGroup.choose" />
							</xsl:call-template>
						</div>
						
					</div>
				</fieldset>
			</xsl:if>
		
		</xsl:if>
		
		<div class="floatleft full bigmarginbottom">
			
			<a name="shortdescription" />
			
			<label for="shortDescription" class="floatleft full">
				<xsl:value-of select="$i18n.shortDescription" />
				<span class="required">*</span>
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name" select="'shortDescription'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="Flow" />
				</xsl:call-template>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<a name="longdescription" />
			
			<label for="longDescription" class="floatleft full">
				<xsl:value-of select="$i18n.longDescription" />
				<span class="required">*</span>
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name" select="'longDescription'"/>
					<xsl:with-param name="class" select="'flow-ckeditor'"/>
					<xsl:with-param name="element" select="Flow" />
				</xsl:call-template>
				
			</div>
		</div>
		
		<xsl:if test="$isInternal = 'true'">
			
			<div class="floatleft full bigmarginbottom internal">
				
				<a name="submittedmessade" />
				
				<label for="submittedMessage" class="floatleft full">
					<xsl:value-of select="$i18n.submittedMessage" />
					<span class="required">*</span>
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="name" select="'submittedMessage'"/>
						<xsl:with-param name="class" select="'flow-ckeditor'"/>
						<xsl:with-param name="element" select="Flow" />		
					</xsl:call-template>
					
				</div>
			</div>
		
		</xsl:if>
			
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
		
		<div class="floatleft fifty bigmarginbottom paddingright border-box">
			
			<a name="tags" />
			
			<label for="tags" class="floatleft full">
				<xsl:value-of select="$i18n.tags.title" />
			</label>
			
			<div class="floatleft full">
				
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name" select="'tags'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="element" select="Flow/Tags/tag" />
					<xsl:with-param name="separateListValues" select="'true'"/>
				</xsl:call-template>
				
			</div>
		</div>
		
		<div class="paddingleft floatleft fifty bigmarginbottom border-box">
			
			<a name="checks" />
			
			<label for="checks" class="floatleft full">
				<xsl:value-of select="$i18n.checks.title" />
			</label>
			
			<div class="floatleft full">
				
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name" select="'checks'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="element" select="Flow/Checks/check" />
					<xsl:with-param name="separateListValues" select="'true'"/>
				</xsl:call-template>
				
			</div>
			
		</div>
		
		<h2 class="floatleft bigmargintop">
			<xsl:value-of select="$i18n.DescriptionColumnSettings.Title"/>
		</h2>
		
		<div class="floatleft full">
			<p>
				<xsl:value-of select="$i18n.DescriptionColumnSettings.Description"/>
			</p>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="userDescriptionTemplate" class="floatleft full">
				<xsl:value-of select="$i18n.userDescriptionTemplate" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'userDescriptionTemplate'"/>
					<xsl:with-param name="name" select="'userDescriptionTemplate'"/>
					<xsl:with-param name="element" select="Flow" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="managerDescriptionTemplate" class="floatleft full">
				<xsl:value-of select="$i18n.managerDescriptionTemplate" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'managerDescriptionTemplate'"/>
					<xsl:with-param name="name" select="'managerDescriptionTemplate'"/>
					<xsl:with-param name="element" select="Flow" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes" />
			</label>
			
			<div class="floatleft full">
				<p>
					<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.Description"/>
				</p>
			</div>
			
			<div class="overview-attributes-container full floatleft bigmarginbottom">
			
				<div class="full floatleft marginleft">
					<span class="display-inline-block paddingright" style="padding-left: 23px; width: calc(50% - 24px);">
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.name"/>
					</span>
					<span class="display-inline-block">
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.value"/>
					</span>
				</div>
			
				<div class="overview-attributes full floatleft marginleft sortable">
					<xsl:choose>
						<xsl:when test="requestparameters">
							<xsl:apply-templates select="requestparameters/parameter[name = 'overviewAttributeID']" mode="overviewAttribute" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="OverviewAttributes/FlowOverviewAttribute" >
							</xsl:apply-templates>
						</xsl:otherwise>
					</xsl:choose>
				</div>
				
				<div class="overview-attribute-template" style="display: none;" data-maxtext="{$i18n.FlowFamily.OverviewAttributes.Add.MaxReached}">
					<xsl:call-template name="createOverviewAttribute">
						<xsl:with-param name="id" select="''" />
						<xsl:with-param name="name" select="''" />
						<xsl:with-param name="value" select="''" />
						<xsl:with-param name="sortIndex" select="''" />
						<xsl:with-param name="disabled" select="'true'" />
					</xsl:call-template>
				</div>
				
				<div class="floatright margintop">
					<a href="#" onclick="addOverviewAttribute(this, event)" title="{$i18n.FlowFamily.OverviewAttributes.Add}">
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.Add"/>
					</a>
					<a href="#" onclick="addOverviewAttribute(this, event)" title="{$i18n.FlowFamily.OverviewAttributes.Add}">
						<img class="vertical-align-bottom marginleft" src="{$imgPath}/add.png"/>
					</a>
				</div>
			
			</div>
			
		</div>
		
		<h2 class="floatleft bigmargintop">
			<xsl:value-of select="$i18n.FlowFamily.SharedSettings"/>
		</h2>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'useLoginHelpLink'" />
					<xsl:with-param name="name" select="'useLoginHelpLink'" />
					<xsl:with-param name="element" select="Flow/FlowFamily" />    
				</xsl:call-template>
				
				<label for="useLoginHelpLink">
					<xsl:value-of select="$i18n.FlowFamily.LoginHelp" />
				</label>
			</div>
			
			<div id="loginhelplink-container" class="floatleft full margintop">
			
				<div class="floatleft forty">
				
					<label for="loginHelpLinkName" class="floatleft full">
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp.Name" />
					</label>
					
					<div class="floatleft full">
		
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'loginHelpLinkName'" />
							<xsl:with-param name="id" select="'loginHelpLinkName'" />
							<xsl:with-param name="element" select="Flow/FlowFamily" />
							<xsl:with-param name="size" select="10" />
						</xsl:call-template>
						
					</div>
				
				</div>
				
				<div class="floatleft forty bigmarginleft">
				
					<label for="loginHelpLinkURL" class="floatleft full">
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp.URL" />
					</label>
					
					<div class="floatleft full">
		
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'loginHelpLinkURL'" />
							<xsl:with-param name="id" select="'loginHelpLinkURL'" />
							<xsl:with-param name="element" select="Flow/FlowFamily" />
							<xsl:with-param name="size" select="10" />
						</xsl:call-template>
						
					</div>
				
				</div>
				
			</div>
			
			<script type="text/javascript">
				$(function() {
					
					var checkbox = $("#useLoginHelpLink");
					
					var showHideLoginHelpLink = function() {
						
						var checked = checkbox.prop("checked");
						
						$("#loginhelplink-container").toggle(checked).find("input").attr("disabled", !checked);
					};
					
					checkbox.on("change", showHideLoginHelpLink);
					showHideLoginHelpLink();
				});
			</script>
		</div>
		
		<div class="floatleft full bigmarginbottom">
				
			<label for="statisticsMode" class="floatleft full">
				<xsl:value-of select="$i18n.StatisticsSettings" />
			</label>
			
			<xsl:variable name="options">
				<option>
					<name><xsl:value-of select="$i18n.StatisticsMode.Internal"/></name>
					<value>INTERNAL</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.StatisticsMode.Public"/></name>
					<value>PUBLIC</value>
				</option>
			</xsl:variable>
		
			<xsl:call-template name="createDropdown">
				<xsl:with-param name="id" select="'statisticsMode'"/>
				<xsl:with-param name="name" select="'statisticsMode'"/>
				<xsl:with-param name="class" select="'forty'"/>
				<xsl:with-param name="element" select="exsl:node-set($options)/option"/>
				<xsl:with-param name="labelElementName" select="'name'" />
				<xsl:with-param name="valueElementName" select="'value'" />
				<xsl:with-param name="addEmptyOption" select="$i18n.StatisticsMode.None" />
				<xsl:with-param name="selectedValue">
					
					<xsl:choose>
						<xsl:when test="name() = 'AddFlow'">
							<xsl:value-of select="DefaultStatisticsMode"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="Flow/FlowFamily/statisticsMode"/>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:with-param>
			</xsl:call-template>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="tags" class="floatleft full">
				<xsl:value-of select="$i18n.aliases.title" />
			</label>
			
			<div class="floatleft full">
				
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name" select="'alias'"/>
					<xsl:with-param name="rows" select="5"/>
					<xsl:with-param name="element" select="Flow/FlowFamily/Aliases/Alias" />
					<xsl:with-param name="separateListValues" select="'true'"/>
				</xsl:call-template>
				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="tags" class="floatleft full">
				<xsl:value-of select="$i18n.FlowFamily.popularity.boost" />
			</label>
			
			<div class="floatleft full">
				
				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="'popularityBoost'" />
					<xsl:with-param name="id" select="'popularityBoost'" />
					<xsl:with-param name="element" select="Flow/FlowFamily" />
					<xsl:with-param name="size" select="10" />
				</xsl:call-template>
				
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			
			<label for="tags" class="floatleft full">
				<xsl:value-of select="$i18n.FlowFamily.startButtonText" />
			</label>
			
			<div class="floatleft full">
				
				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="'startButtonText'" />
					<xsl:with-param name="id" select="'startButtonText'" />
					<xsl:with-param name="element" select="Flow/FlowFamily" />
					<xsl:with-param name="size" select="30" />
				</xsl:call-template>
				
			</div>
		</div>

		
		<div class="floatleft full bigmarginbottom margintop">
			
			<h3><xsl:value-of select="$i18n.contact.title" /></h3>
			
			<div class="floatleft forty">
				
				<label for="contactName" class="floatleft full">
					<xsl:value-of select="$i18n.contact.name" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'contactName'" />
						<xsl:with-param name="id" select="'contactName'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>
			
			<div class="floatleft forty bigmarginleft">
				
				<label for="contactEmail" class="floatleft full">
					<xsl:value-of select="$i18n.contact.email" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'contactEmail'" />
						<xsl:with-param name="id" select="'contactEmail'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>
			
			<div class="floatleft forty">
			
				<label for="contactPhone" class="floatleft full">
					<xsl:value-of select="$i18n.contact.phone" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'contactPhone'" />
						<xsl:with-param name="id" select="'contactPhone'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>

			<div class="floatleft forty bigmarginleft">
			
				<label for="contactPhone" class="floatleft full">
					<xsl:value-of select="$i18n.contact.webaddress" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'contactWebAddress'" />
						<xsl:with-param name="id" select="'contactWebAddress'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>
			
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
			
			<h3><xsl:value-of select="$i18n.owner.title" /></h3>
			
			<div class="floatleft forty">
			
				<label for="ownerName" class="floatleft full">
					<xsl:value-of select="$i18n.owner.name" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'ownerName'" />
						<xsl:with-param name="id" select="'ownerName'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>
			
			<div class="floatleft forty bigmarginleft">
			
				<label for="ownerEmail" class="floatleft full">
					<xsl:value-of select="$i18n.owner.email" />
				</label>
				
				<div class="floatleft full">
	
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'ownerEmail'" />
						<xsl:with-param name="id" select="'ownerEmail'" />
						<xsl:with-param name="element" select="Flow/FlowFamily" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
					
				</div>
			
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template name="createOverviewAttribute">
		
		<xsl:param name="id" />
		<xsl:param name="name" />
		<xsl:param name="value" />
		<xsl:param name="sortIndex" />
		<xsl:param name="disabled" select="null" />
		
		<div class="overview-attribute full floatleft margintop marginbottom">
			<img class="vertical-align-middle marginright cursor-move" src="{$imgPath}/move.png" title="{$i18n.FlowFamily.OverviewAttributes.Move}"/>
			
			<!-- TODO Use of //requestparameters below is very slow and needs to be replaced -->
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('overviewAttributeID_', $id)"/>
				<xsl:with-param name="name" select="'overviewAttributeID'"/>
				<xsl:with-param name="value" select="$id"/>
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('overviewAttributeSortIndex_', $id)" />
				<xsl:with-param name="name" select="concat('overviewAttributeSortIndex_', $id)" />
				<xsl:with-param name="value" select="$sortIndex" />
				<xsl:with-param name="class" select="'sortorder'" />
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="concat('overviewAttributeName_', $id)"/>
				<xsl:with-param name="name" select="concat('overviewAttributeName_', $id)"/>
				<xsl:with-param name="value" select="$name"/>
				<xsl:with-param name="class" select="'marginright'"/>
				<xsl:with-param name="width" select="'calc(37% - 24px)'"/>
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="concat('overviewAttributeValue_', $id)"/>
				<xsl:with-param name="name" select="concat('overviewAttributeValue_', $id)"/>
				<xsl:with-param name="value" select="$value"/>
				<xsl:with-param name="width" select="'calc(63% - 24px)'"/>
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			
			<a href="#" onclick="deleteOverviewAttribute(this, event)" title="{$i18n.FlowFamily.OverviewAttributes.Delete}" data-confirm="{$i18n.FlowFamily.OverviewAttributes.DeleteConfirm}">
				<img class="vertical-align-middle marginleft" src="{$imgPath}/delete.png"/>
			</a>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowOverviewAttribute">
	
		<xsl:call-template name="createOverviewAttribute">
			<xsl:with-param name="id" select="generate-id(.)" />
			<xsl:with-param name="name" select="name" />
			<xsl:with-param name="value" select="value" />
			<xsl:with-param name="sortIndex" select="sortIndex" />
		</xsl:call-template>
			
	</xsl:template>
	
	<xsl:template match="parameter" mode="overviewAttribute">
	
		<xsl:for-each select="value">
			<xsl:variable name="id" select="." />
			
			<xsl:call-template name="createOverviewAttribute">
				<xsl:with-param name="id" select="$id" />
				<xsl:with-param name="name" select="../../parameter[name = concat('overviewAttributeName_', $id)]/value" />
				<xsl:with-param name="value" select="../../parameter[name = concat('overviewAttributeValue_', $id)]/value" />
				<xsl:with-param name="sortIndex" select="../../parameter[name = concat('overviewAttributeSortIndex_', $id)]/value" />
			</xsl:call-template>
		</xsl:for-each>
		
	</xsl:template>
	
	<xsl:template match="AddQueryDescriptor">
	
		<h1><xsl:value-of select="$i18n.AddQueryDescriptor.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<div class="thirty floatleft">
		
				<div class="floatleft full bigmarginbottom">
					
					<label for="flowtype" class="floatleft full">
						<xsl:value-of select="$i18n.step" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="name" select="'stepID'"/>
							<xsl:with-param name="valueElementName" select="'stepID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="element" select="Steps/Step" />
							<xsl:with-param name="selectedValue" select="SelectedStep" />
						</xsl:call-template>
					</div>
				</div>	
			
				<div class="floatleft full bigmarginbottom">
					
					<label for="flowtype" class="floatleft full">
						<xsl:value-of select="$i18n.queryType" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="name" select="'queryTypeID'"/>
							<xsl:with-param name="valueElementName" select="'queryTypeID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="element" select="QueryTypes/QueryTypeDescriptor" />
						</xsl:call-template>
						<xsl:for-each select="QueryTypes/QueryTypeDescriptor">
							<xsl:if test="description != ''">
								<xsl:call-template name="createHiddenField">
									<xsl:with-param name="id" select="concat('queryTypeDescription_', queryTypeID)" />
									<xsl:with-param name="name" select="concat('queryTypeDescription_', queryTypeID)" />
									<xsl:with-param name="disabled" select="'true'" />
									<xsl:with-param name="value" select="description" />
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
					</div>
				</div>
				
			</div>
				
			<div class="floatleft seventy bigmarginbottom">
				
				<label for="flowtype" class="floatleft full">
					<xsl:value-of select="$i18n.queryTypeDescription" />
				</label>
				
				<div id="queryTypeDescription">
				
				</div>
				
			</div>	
			
			<div class="clearboth" />
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="name" class="floatleft full">
					<xsl:value-of select="$i18n.name" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'"/>
						<xsl:with-param name="name" select="'name'"/>     
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
			
				<label for="comment" class="floatleft full">
					<xsl:value-of select="$i18n.Comment" /><xsl:text> (</xsl:text><xsl:value-of select="$i18n.commentVisibility" /><xsl:text>)</xsl:text>
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'comment'"/>
						<xsl:with-param name="name" select="'comment'"/>
						<xsl:with-param name="rows" select="1" />
					</xsl:call-template>
				</div>
			</div>
			
			<h2><xsl:value-of select="$i18n.defaultQueryState.title"/></h2>
			
			<p><xsl:value-of select="$i18n.defaultQueryState.description"/></p>
			
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'visible'"/>
						<xsl:with-param name="name" select="'defaultQueryState'"/>
						<xsl:with-param name="value" select="'VISIBLE'"/>
					</xsl:call-template>
					
					<label for="visible">
						<xsl:value-of select="$i18n.queryState.VISIBLE" />
					</label>
				</div>
			</div>	
		
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'visible_required'"/>
						<xsl:with-param name="name" select="'defaultQueryState'"/>
						<xsl:with-param name="value" select="'VISIBLE_REQUIRED'"/>
					</xsl:call-template>
					
					<label for="visible_required">
						<xsl:value-of select="$i18n.queryState.VISIBLE_REQUIRED" />
					</label>
				</div>
			</div>	
		
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'hidden'"/>
						<xsl:with-param name="name" select="'defaultQueryState'"/>
						<xsl:with-param name="value" select="'HIDDEN'"/>
					</xsl:call-template>
					
					<label for="hidden">
						<xsl:value-of select="$i18n.queryState.HIDDEN" />
					</label>
				</div>
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddQueryDescriptor.submit}" />
			</div>
	
		</form>
	
	</xsl:template>	
	
	<xsl:template match="AddEvaluatorDescriptor">
	
		<h1>
			<xsl:value-of select="$i18n.AddEvaluatorDescriptor.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="QueryDescriptor/name"/>
		</h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<div class="floatleft full bigmarginbottom">
				
				<label for="flowtype" class="floatleft full">
					<xsl:value-of select="$i18n.evaluatorType" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="name" select="'evaluatorTypeID'"/>
						<xsl:with-param name="valueElementName" select="'evaluatorTypeID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="EvaluatorTypes/EvaluatorTypeDescriptor" />
						<xsl:with-param name="class" select="'width-auto'"/>      
					</xsl:call-template>
				</div>
			</div>		
					
			<div class="floatleft full bigmarginbottom">
				
				<label for="name" class="floatleft full">
					<xsl:value-of select="$i18n.name" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'"/>
						<xsl:with-param name="name" select="'name'"/>     
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
			
				<label for="comment" class="floatleft full">
					<xsl:value-of select="$i18n.Comment" /><xsl:text> (</xsl:text><xsl:value-of select="$i18n.commentVisibility" /><xsl:text>)</xsl:text>
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'comment'"/>
						<xsl:with-param name="name" select="'comment'"/>
						<xsl:with-param name="rows" select="1" />       
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddEvaluatorDescriptor.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="AddStep">
	
		<h1><xsl:value-of select="$i18n.AddStep.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="stepForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddStep.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateStep">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateStep.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Step/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="stepForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStep.submit}" />
			</div>
		
		</form>
	
	</xsl:template>	
	
	<xsl:template name="stepForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="Step" />          
				</xsl:call-template>
			</div>
		</div>	
	
	</xsl:template>
	
	<xsl:template match="AddStatus">
	
		<h1><xsl:value-of select="$i18n.AddStatus.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form id="statusform" method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="statusForm">
				<xsl:with-param name="element" select="Status" />
				<xsl:with-param name="statusMappings" select="Status/DefaultStatusMappings/DefaultStatusMapping" />
				<xsl:with-param name="hideExternalMessageSettings" select="Status/Flow/hideExternalMessages = 'true'" />
			</xsl:call-template>
			
			<xsl:call-template name="statusFragmentExtension"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddStatus.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateStatus">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateStatus.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Status/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="statusform" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="statusForm">
				<xsl:with-param name="element" select="Status" />
				<xsl:with-param name="statusMappings" select="Status/DefaultStatusMappings/DefaultStatusMapping" />
				<xsl:with-param name="hideExternalMessageSettings" select="Status/Flow/hideExternalMessages = 'true'" />
			</xsl:call-template>

			<xsl:call-template name="statusFragmentExtension"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStatus.submit}" />
			</div>
		
		</form>
	
	</xsl:template>	
	
	<xsl:template name="statusFragmentExtension">
	
		<xsl:if test="ViewFragmentExtension/ViewFragment">
						
			<h2 class="floatleft full bigmargintop"><xsl:value-of select="$i18n.UpdateStatus.Advanced" /></h2>

			<xsl:apply-templates select="ViewFragmentExtension" />
		
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="statusForm">
		<xsl:param name="element" />
		<xsl:param name="statusMappings" />
		<xsl:param name="hideExternalMessageSettings" select="false()" />
		<xsl:param name="hideFlowSpecificSettings" select="false()" />
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="$element" />          
				</xsl:call-template>
			</div>
		</div>	
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="description" class="floatleft full">
				<xsl:value-of select="$i18n.description" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'description'"/>
					<xsl:with-param name="name" select="'description'"/>
					<xsl:with-param name="element" select="$element" />          
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="managingTime" class="floatleft full">
				<xsl:value-of select="$i18n.managingTime" />
			</label>
			
			<p><xsl:value-of select="$i18n.managingTime.description"/></p>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'managingTime'"/>
					<xsl:with-param name="name" select="'managingTime'"/>
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
			</div>
		</div>

		<div>

			<xsl:if test="$hideExternalMessageSettings">
				<xsl:attribute name="class">hidden</xsl:attribute>
			</xsl:if>

			<h2><xsl:value-of select="$i18n.externalMessages"/></h2>

			<div class="floatleft full bigmarginbottom">
				
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'newExternalMessagesDisallowed'" />
						<xsl:with-param name="id" select="'newExternalMessagesDisallowed'" />
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
					
					<label for="newExternalMessagesDisallowed">
						<xsl:value-of select="$i18n.newExternalMessagesDisallowed" />
					</label>
				</div>
				
				<div class="floatleft full">
					
					<label for="newExternalMessagesAllowedDays" class="floatleft full">
						<xsl:value-of select="$i18n.newExternalMessagesAllowedDays" />
					</label>
		
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'newExternalMessagesAllowedDays'"/>
						<xsl:with-param name="name" select="'newExternalMessagesAllowedDays'"/>
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
				</div>
	
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'addExternalMessage'" />
						<xsl:with-param name="id" select="'addExternalMessage'" />
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
					
					<label for="addExternalMessage">
						<xsl:value-of select="$i18n.addExternalMessage" />
					</label>
				</div>
				
				<xsl:if test="not($hideFlowSpecificSettings)">
				
					<div class="floatleft full bigmarginbottom hidden">
					
						<label for="defaultExternalMessageTemplate" class="floatleft full">
							<xsl:value-of select="$i18n.defaultMessageTemplate" />
						</label>
			
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="name" select="'defaultExternalMessageTemplate'" />
							<xsl:with-param name="id" select="'defaultExternalMessageTemplate'" />
							<xsl:with-param name="element" select="MessageTemplates/MessageTemplate[type='EXTERNAL' or type='ALL']" />
							<xsl:with-param name="valueElementName" select="'templateID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="selectedValue" select="$element/defaultExternalMessageTemplateID" />
							<xsl:with-param name="addEmptyOption" select="$i18n.defaultMessageTemplate.None" />
						</xsl:call-template>
					</div>
					
				</xsl:if>
	
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'addInternalMessage'" />
						<xsl:with-param name="id" select="'addInternalMessage'" />
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
					
					<label for="addInternalMessage">
						<xsl:value-of select="$i18n.addInternalMessage" />
					</label>
				</div>
				
				<xsl:if test="not($hideFlowSpecificSettings)">
				
					<div class="floatleft full bigmarginbottom hidden">
					
						<label for="defaultInternalMessageTemplate" class="floatleft full">
							<xsl:value-of select="$i18n.defaultMessageTemplate" />
						</label>
			
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="name" select="'defaultInternalMessageTemplate'" />
							<xsl:with-param name="id" select="'defaultInternalMessageTemplate'" />
							<xsl:with-param name="element" select="MessageTemplates/MessageTemplate[type='INTERNAL' or type='ALL']" />
							<xsl:with-param name="valueElementName" select="'templateID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="selectedValue" select="$element/defaultInternalMessageTemplateID" />
							<xsl:with-param name="addEmptyOption" select="$i18n.defaultMessageTemplate.None" />
						</xsl:call-template>
					</div>
					
				</xsl:if>
				
			</div>
		</div>

		<h2><xsl:value-of select="$i18n.permissions"/></h2>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isUserMutable'" />
					<xsl:with-param name="id" select="'isUserMutable'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="isUserMutable">
					<xsl:value-of select="$i18n.isUserMutable" />
				</label>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isUserDeletable'" />
					<xsl:with-param name="id" select="'isUserDeletable'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="isUserDeletable">
					<xsl:value-of select="$i18n.isUserDeletable" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isAdminMutable'" />
					<xsl:with-param name="id" select="'isAdminMutable'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="isAdminMutable">
					<xsl:value-of select="$i18n.isAdminMutable" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isAdminDeletable'" />
					<xsl:with-param name="id" select="'isAdminDeletable'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="isAdminDeletable">
					<xsl:value-of select="$i18n.isAdminDeletable" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
			
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isRestrictedAdminDeletable'" />
					<xsl:with-param name="id" select="'isRestrictedAdminDeletable'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="isRestrictedAdminDeletable">
					<xsl:value-of select="$i18n.isRestrictedAdminDeletable" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'requireSigning'" />
					<xsl:with-param name="id" select="'requireSigning'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="requireSigning">
					<xsl:value-of select="$i18n.Status.requireSigning" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useAccessCheckByUser'" />
					<xsl:with-param name="id" select="'useAccessCheckByUser'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="useAccessCheckByUser">
					<xsl:value-of select="$i18n.Status.useAccessCheckByUser" />
				</label>
			</div>
		</div>
		
		<xsl:if test="not($hideFlowSpecificSettings)">
		
			<div id="allowedManagers" class="floatleft full hidden">
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.allowedGroups" />
					</label>
					
					<xsl:call-template name="GroupList">
						<xsl:with-param name="connectorURL">
							<xsl:value-of select="/Document/requestinfo/currentURI"/>
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/Document/module/alias"/>
							<xsl:text>/groups/</xsl:text>
							<xsl:value-of select="Flow/flowID"/>
						</xsl:with-param>
						<xsl:with-param name="name" select="'group'"/>
						<xsl:with-param name="groups" select="ManagerGroups" />
					</xsl:call-template>
					
				</div>
				
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.allowedUsers" />
					</label>
					
					<xsl:call-template name="UserList">
						<xsl:with-param name="connectorURL">
							<xsl:value-of select="/Document/requestinfo/currentURI"/>
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/Document/module/alias"/>
							<xsl:text>/users/</xsl:text>
							<xsl:value-of select="Flow/flowID"/>
						</xsl:with-param>
						<xsl:with-param name="name" select="'user'"/>
						<xsl:with-param name="users" select="ManagerUsers" />
					</xsl:call-template>
				</div>
			</div>
			
		</xsl:if>

		<div class="floatleft full margintop">
		
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useAccessCheckByStatus'" />
					<xsl:with-param name="id" select="'useAccessCheckByStatus'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
				
				<label for="useAccessCheckByStatus">
					<xsl:value-of select="$i18n.Status.useAccessCheckByStatus" />
				</label>
			</div>
			
			<div class="floatleft full tiny"><xsl:value-of select="$i18n.Status.useAccessCheckByStatusInfo" /></div>
			
		</div>
		
		<xsl:if test="not($hideFlowSpecificSettings)">
		
			<div id="acceptedStatuses" class="floatleft full hidden">
				
				<div class="floatleft full bigmarginbottom">
					
					<div id="accepted-statuses-list" class="floatleft full margintop marginbottom">
				
						<xsl:choose>
							<xsl:when test="requestparameters">
							
								<xsl:apply-templates select="requestparameters/parameter[name='acceptedStatusID']/value" mode="acceptedStatusList" />
								
							</xsl:when>
							<xsl:otherwise>
								
								<xsl:apply-templates select="Status/AcceptedStatusIDs/acceptedStatusID" />
									
							</xsl:otherwise>
						</xsl:choose>
							
						<div id="accepted-statuses-template" style="display: none;">
							<div class="floatleft hover border full marginbottom border-radius lightbackground">
								
								<div class="marginleft margintop floatleft">
									<div class="marginleft">
										<h3>
											<xsl:value-of select="acceptedStatusName" />
										</h3>
									</div>
								</div>
								
								<input name="acceptedStatusName" hidden="true" />
								<input name="acceptedStatusID" hidden="true"/>
								
								<div class="padding floatright">
									<div class="floatright marginright">
										<a href="#" onclick="if(confirm('{$i18n.DeleteAcceptedStatus.Confirm}: {acceptedStatusName}?'))$(this).closest('div.border-radius').remove(); return false;" title="{$i18n.DeleteAcceptedStatus}: {acceptedStatusName}">
											<img src="{$imgPath}/delete.png"/>
										</a>
									</div>
								</div>
								
							</div>
						</div>
							
							<input name="connectorURL" disabled="true" hidden="true" value="{/Document/requestinfo/currentURI}/{/Document/module/alias}/searchstatuses/{Flow/flowID}"/>
							
						</div>
						
						<div class="floatleft full ui-widget">
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'accepted-statuses-search'" />
								<xsl:with-param name="placeholder" select="$i18n.AcceptedStatuses.SearchPlaceholder" />
							</xsl:call-template>
						</div>
					
				</div>
				
			</div>
				
		</xsl:if>
	
		<div class="floatleft full bigmargintop"><h2 class="clearboth"><xsl:value-of select="$i18n.statusContentType.title"/></h2></div>
		
		<p><xsl:value-of select="$i18n.statusContentType.description"/></p>
		
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'new'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'NEW'"/>
				</xsl:call-template>
				
				<label for="new">
					<xsl:value-of select="$i18n.contentType.NEW" />
				</label>
			</div>
		</div>	
	
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'waiting_for_multisign'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />  
					<xsl:with-param name="value" select="'WAITING_FOR_MULTISIGN'"/>
				</xsl:call-template>
				
				<label for="waiting_for_multisign">
					<xsl:value-of select="$i18n.contentType.WAITING_FOR_MULTISIGN" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'waiting_for_payment'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'WAITING_FOR_PAYMENT'"/>
				</xsl:call-template>
				
				<label for="waiting_for_payment">
					<xsl:value-of select="$i18n.contentType.WAITING_FOR_PAYMENT" />
				</label>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'submitted'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'SUBMITTED'"/>
				</xsl:call-template>
				
				<label for="submitted">
					<xsl:value-of select="$i18n.contentType.SUBMITTED" />
				</label>
			</div>
		</div>	
	
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'in_progress'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'IN_PROGRESS'"/>
				</xsl:call-template>
				
				<label for="in_progress">
					<xsl:value-of select="$i18n.contentType.IN_PROGRESS" />
				</label>
			</div>
		</div>	
		
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'waiting_for_completion'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'WAITING_FOR_COMPLETION'"/>
				</xsl:call-template>
				
				<label for="waiting_for_completion">
					<xsl:value-of select="$i18n.contentType.WAITING_FOR_COMPLETION" />
				</label>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'archived'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="value" select="'ARCHIVED'"/>
				</xsl:call-template>
				
				<label for="archived">
					<xsl:value-of select="$i18n.contentType.ARCHIVED" />
				</label>
			</div>
		</div>	
	
		<xsl:if test="FlowActions">
		
			<h2><xsl:value-of select="$i18n.defaultStatusMappings.title"/></h2>
		
			<p><xsl:value-of select="$i18n.defaultStatusMappings.description"/></p>
		
			<xsl:apply-templates select="FlowActions/FlowAction" mode="statusForm">
				<xsl:with-param name="statusMappings" select="$statusMappings" />
			</xsl:apply-templates>
		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="FlowAction" mode="statusForm">
		<xsl:param name="statusMappings" />
	
		<div class="floatleft full bigmarginbottom margintop">
		
			<xsl:variable name="id">
				<xsl:value-of select="'action_'"/>
				<xsl:value-of select="position()"/>
			</xsl:variable>
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'actionID'" />
					<xsl:with-param name="value" select="actionID"/>
					<xsl:with-param name="id" select="$id"/>
					<xsl:with-param name="element" select="$statusMappings" />
					<xsl:with-param name="requestparameters" select="../../requestparameters"/>				     
				</xsl:call-template>
				
				<label for="{$id}">
					<xsl:value-of select="name" />
					<xsl:if test="required = 'true'">
						<xsl:text>&#160;(</xsl:text><xsl:value-of select="$i18n.required" /><xsl:text>)</xsl:text>
					</xsl:if>
				</label>
			</div>
		</div>
	
	</xsl:template>

	<xsl:template match="acceptedStatusID">
		
		<xsl:variable name="acceptedStatusID" select="." />
		<xsl:variable name="acceptedStatusName" select="../../../Statuses/Status[statusID = $acceptedStatusID]/name" />
		
		<div class="floatleft hover border full marginbottom border-radius lightbackground">
			
			<div class="marginleft margintop floatleft">
				<div class="marginleft">
					<h3>
						<xsl:value-of select="$acceptedStatusName" />
					</h3>
				</div>
			</div>
			
			<input name="acceptedStatusName-{$acceptedStatusID}" hidden="true" value="{$acceptedStatusName}"/>
			<input name="acceptedStatusID" hidden="true" value="{$acceptedStatusID}"/>
			
			<div class="padding floatright">
				<div class="floatright marginright">
					<a href="#" onclick="if(confirm('{$i18n.DeleteAcceptedStatus.Confirm}: {$acceptedStatusName}?'))$(this).closest('div.border-radius').remove(); return false;" title="{$i18n.DeleteAcceptedStatus}: {$acceptedStatusName}">
						<img src="{$imgPath}/delete.png"/>
					</a>
				</div>
			</div>
			
		</div>
		
	</xsl:template>

	<xsl:template match="value" mode="acceptedStatusList">

		<xsl:variable name="acceptedStatusID" select="."/>
		<xsl:variable name="acceptedStatusName" select="../../../Statuses/Status[statusID = $acceptedStatusID]/name" />

		
		<xsl:variable name="nameParam" select="concat('name-', $acceptedStatusID)"/>
		
		<div class="floatleft hover border full marginbottom border-radius lightbackground">
			
			<div class="marginleft margintop floatleft">
				<div class="marginleft">
					<h3>
						<xsl:value-of select="$acceptedStatusName" />
					</h3>
				</div>
			</div>
			
			<input name="acceptedStatusName-{$acceptedStatusID}" hidden="true" value="{$acceptedStatusName}"/>
			<input name="acceptedStatusID" hidden="true" value="{$acceptedStatusID}"/>
			
			<div class="padding floatright">
				<div class="floatright marginright">
					<a href="#" onclick="if(confirm('{$i18n.DeleteAcceptedStatus.Confirm}: {$acceptedStatusName}?'))$(this).closest('div.border-radius').remove(); return false;" title="{$i18n.DeleteAcceptedStatus}: {$acceptedStatusName}">
						<img src="{$imgPath}/delete.png"/>
					</a>
				</div>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="UpdateFlowIcon">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateFlowIcon.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>

		<xsl:apply-templates select="validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="icon" class="floatleft">
					<xsl:value-of select="$i18n.currentIcon" />
					
					<xsl:if test="not(Flow/iconFileName)">
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$i18n.defaultIcon" />
					</xsl:if>
				</label>
				
				<div class="floatleft clearboth">
					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{Flow/flowID}?{Flow/IconLastModified}" id="icon" alt="" />
				</div>
			</div>
			
			<xsl:if test="Flow/iconFileName">
				<div class="floatleft full bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'clearicon'" />
							<xsl:with-param name="value" select="'true'"/>
							<xsl:with-param name="id" select="'clearicon'" />
							<xsl:with-param name="onclick" select="'updateFileIconField(this.checked)'"/>
						</xsl:call-template>
						
						<label for="clearicon">
							<xsl:value-of select="$i18n.restoreDefaultIcon" />
						</label>
					</div>
				</div>
			</xsl:if>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="icon" class="floatleft full">
					<xsl:value-of select="$i18n.uploadNewIcon" />
				</label>
				
				<div class="floatleft full">
					<input type="file" name="icon" id="iconfile"/>
				</div>
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateFlowIcon.submit}" />
			</div>
		
		</form>
	
		<script>
			function updateFileIconField(checked){
						
				$('#iconfile').attr('disabled',checked);
			}
		</script>
	
	</xsl:template>
	
	<xsl:template match="AddFlowForm">
	
		<h1>
			<xsl:value-of select="$i18n.AddFlowForm.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<p><xsl:value-of select="$i18n.UpdateFlowForm.description"/></p>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
		
			<xsl:call-template name="editFlowForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddFlowForm.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateFlowForm">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateFlowForm.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<p><xsl:value-of select="$i18n.UpdateFlowForm.description"/></p>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
		
			<xsl:call-template name="editFlowForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateFlowForm.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="editFlowForm">
	
		<div class="floatleft full bigmarginbottom">
		
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.FlowForm.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="FlowForm" />
				</xsl:call-template>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.uploadNewFlowForm" />
			</label>
			
			<div class="floatleft full">
				<input type="file" name="pdf"/>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<label for="externalURL" class="floatleft full">
				<xsl:value-of select="$i18n.FlowForm.externalURL" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'externalURL'"/>
					<xsl:with-param name="name" select="'externalURL'"/>
					<xsl:with-param name="element" select="FlowForm" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="'showExternalLinkIcon'"/>
				<xsl:with-param name="name" select="'showExternalLinkIcon'"/>
				<xsl:with-param name="element" select="FlowForm" />
			</xsl:call-template>	
		
			<xsl:text> </xsl:text>
		
			<label for="showExternalLinkIcon">
				<xsl:value-of select="$i18n.FlowForm.externalIcon" />
			</label>
			
		</div>		

		<div class="floatleft full bigmarginbottom">
		
			<label for="downloadFormButtonText" class="floatleft full">
				<xsl:value-of select="$i18n.FlowForm.downloadFormButtonText" />
			</label>
			<div class="tiny">
				<xsl:value-of select="$i18n.FlowForm.downloadFormButtonText.Info" />
			</div>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'downloadFormButtonText'"/>
					<xsl:with-param name="name" select="'downloadFormButtonText'"/>
					<xsl:with-param name="width" select="'300px'" />
					<xsl:with-param name="element" select="FlowForm" />
				</xsl:call-template>
			</div>
		</div>

	
	</xsl:template>
	
	<xsl:template match="UpdateNotifications">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateNotifications.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
			
			<div class="floatright">
			
				<input type="submit" name="reset" value="{$i18n.UpdateNotifications.reset}" onclick="return confirm('{$i18n.UpdateNotifications.reset.confirm}');" class="marginright"/>
			
				<input type="submit" value="{$i18n.UpdateNotifications.submit}" />
			</div>
		
		</form>
		
	</xsl:template>	
	
	<xsl:template match="SortFlow">
	
		<h1>
			<xsl:value-of select="$i18n.SortFlow.title" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="Flow/name" />
		</h1>
		
		<p>
			<xsl:value-of select="$i18n.SortFlow.description" />
		</p>
		
		<xsl:if test="validationError">
			
			<script>
				validationError = true;
			</script>
			
			<xsl:apply-templates select="validationError" />
			
		</xsl:if>
		
		<form id="sortingForm" name="sortingForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full sortable">
							
				<xsl:apply-templates select="Flow/Steps/Step" mode="sort" />
							
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SortFlow.submit}" />
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="Step" mode="sort">
	
		<div id="step_{stepID}" class="floatleft hover border ninety marginbottom lightbackground cursor-move border-radius">
			<div class="padding bold">
				<img class="vertical-align-middle marginright" src="{$imgPath}/move.png" title="{$i18n.MoveStep}" alt="" />
				<xsl:value-of select="position()" /><xsl:text>. </xsl:text><xsl:value-of select="name" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="concat('step', stepID)" />
					<xsl:with-param name="class" select="'sortorder'" />
					<xsl:with-param name="value" select="sortIndex" />
					<xsl:with-param name="requestparameters" select="../../../requestparameters" />
				</xsl:call-template>
			</div>
		</div>	
	
		<xsl:apply-templates select="QueryDescriptors/QueryDescriptor" mode="sort" />
	
	</xsl:template>
	
	<xsl:template match="QueryDescriptor" mode="sort">
	
		<div id="query_{queryID}" class="query bigmarginleft floatleft hover border ninety marginbottom lightbackground cursor-move border-radius">
			
			<div class="padding">
				<img class="vertical-align-middle marginright" src="{$imgPath}/move.png" title="{$i18n.MoveQuery}" alt="" />
				<xsl:value-of select="position()" /><xsl:text>. </xsl:text><xsl:value-of select="name" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="concat('query', queryID)" />
					<xsl:with-param name="class" select="'sortorder'" />
					<xsl:with-param name="value" select="sortIndex" />
					<xsl:with-param name="requestparameters" select="../../../../../requestparameters" />
				</xsl:call-template>
			</div>
			
			<xsl:apply-templates select="EvaluatorDescriptors/EvaluatorDescriptor/TargetQueryIDs/queryID" mode="targetIDs" />
			
		</div>	
		
	</xsl:template>	
	
	<xsl:template match="queryID" mode="targetIDs">
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="name" select="concat('targetQueryIDs_', ../../../../queryID)" />
			<xsl:with-param name="class" select="'targetQueryIDs'" />
			<xsl:with-param name="value" select="." />
			<xsl:with-param name="disabled" select="'true'" />
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="SortStatuses">
	
		<h1>
			<xsl:value-of select="$i18n.SortFlowStatuses.title" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="Flow/name" />
		</h1>
		
		<form id="statusSortingForm" name="statusSortingForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full sortable">
				
				<xsl:apply-templates select="Flow/Statuses/Status" mode="sort" />
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SortFlow.submit}" />
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="Status" mode="sort">
	
		<xsl:call-template name="sortStatuses" />
	
	</xsl:template>
	
	<xsl:template match="StandardStatus" mode="sort">
	
		<xsl:call-template name="sortStatuses" />
	
	</xsl:template>
	
	<xsl:template name="sortStatuses">
	
		<div id="status_{statusID}" class="floatleft hover border ninety marginbottom lightbackground cursor-move border-radius">
			<div class="padding">
				<img class="vertical-align-middle marginright" src="{$imgPath}/move.png" title="{$i18n.MoveStatus}" alt="" />
				<xsl:value-of select="name" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="concat('sortorder_', statusID)" />
					<xsl:with-param name="class" select="'sortorder'" />
					<xsl:with-param name="value" select="sortIndex" />
				</xsl:call-template>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="SortEvaluators">
	
		<h1>
			<xsl:value-of select="$i18n.SortEvaluators.title" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="QueryDescriptor/name" />
		</h1>
		
		<form id="evaluatorSortingForm" name="evaluatorSortingForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full sortable">
				
				<xsl:apply-templates select="QueryDescriptor/EvaluatorDescriptors/EvaluatorDescriptor" mode="sort" />
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SortFlow.submit}" />
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="EvaluatorDescriptor" mode="sort">
	
		<div id="evaluator_{evaluatorID}" class="floatleft hover border ninety marginbottom lightbackground cursor-move border-radius">
			<div class="padding">
				<img class="vertical-align-middle marginright" src="{$imgPath}/move.png" title="{$i18n.MoveEvaluator}" alt="" />
				<xsl:value-of select="name" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="concat('sortorder_', evaluatorID)" />
					<xsl:with-param name="class" select="'sortorder'" />
					<xsl:with-param name="value" select="sortIndex" />
				</xsl:call-template>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="ListStandardStatusGroups">
	
		<h1><xsl:value-of select="$i18n.ListStandardStatusGroups.title" /></h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<p>
			<xsl:value-of select="$i18n.ListStandardStatusGroups.description" />
		</p>
		
			<xsl:choose>
				<xsl:when test="StandardStatusGroups/StandardStatusGroup">
				
					<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>
							<tr>
								<th><xsl:value-of select="$i18n.name" /></th>
								<th width="58" />
							</tr>
						</thead>
						<tbody>
							
							<xsl:apply-templates select="StandardStatusGroups/StandardStatusGroup" mode="list"/>
							
						</tbody>
					</table>
				
				</xsl:when>
				<xsl:otherwise>
					<p><xsl:value-of select="$i18n.ListStandardStatusGroups.noStandardStatusGroupsFound"/></p>
				</xsl:otherwise>
			</xsl:choose>
			
			<br/>
			
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addstandardstatusgroup" title="{$i18n.AddStandardStatusGroup}">
					<xsl:value-of select="$i18n.AddStandardStatusGroup"/>
					<img class="marginleft" src="{$imgPath}/add.png" alt="" />
				</a>
			</div>
			
	</xsl:template>
	
	<xsl:template match="StandardStatusGroup" mode="list">
		
		<tr>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showstandardstatusgroup/{statusGroupID}" title="{$i18n.UpdateStandardStatusGroup}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardstatusgroup/{statusGroupID}" title="{$i18n.UpdateStandardStatusGroup}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
				
				<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/copystandardstatusgroup/{statusGroupID}" title="{$i18n.CopyStandardStatusGroup}: {name}">
					<img src="{$imgPath}/page_copy.png" alt="" />
				</a>
				
				<xsl:if test="statusGroupID != 1">
					<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestandardstatusgroup/{statusGroupID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.DeleteStandardStatusGroup}: {name}">
						<img src="{$imgPath}/delete.png" alt="" />
					</a>
				</xsl:if>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="AddStandardStatusGroup">
	
		<h1><xsl:value-of select="$i18n.AddStandardStatusGroup"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="standardStatusGroupForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddStandardStatusGroup}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateStandardStatusGroup">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateStandardStatusGroup"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="StandardStatus/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="standardStatusGroupForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStandardStatusGroup}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="standardStatusGroupForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="StandardStatusGroup" />
				</xsl:call-template>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ShowStandardStatusGroup">
	
		<h1>
			<xsl:value-of select="$i18n.ShowStandardStatusGroup.title" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="StandardStatusGroup/name" />
		</h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<p>
			<xsl:value-of select="$i18n.ShowStandardStatusGroup.description" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="StandardStatusGroup/name" />
			<xsl:text>.</xsl:text>
		</p>
		
			<xsl:choose>
				<xsl:when test="StandardStatusGroup/StandardStatuses/StandardStatus">
				
					<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>
							<tr>
								<th><xsl:value-of select="$i18n.name" /></th>
								<th width="37" />
							</tr>
						</thead>
						<tbody>
							
							<xsl:apply-templates select="StandardStatusGroup/StandardStatuses/StandardStatus" mode="list"/>
							
						</tbody>
					</table>
				
				</xsl:when>
				<xsl:otherwise>
					<p><xsl:value-of select="$i18n.ShowStandardStatusGroup.noStandardStatusesFound"/></p>
				</xsl:otherwise>
			</xsl:choose>
			
			<br/>
			
			<div class="floatleft">
				<a class="btn btn-light btn-inline" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/standardstatuses" title="{$i18n.ShowStandardStatusGroup.back}">
					<xsl:value-of select="$i18n.ShowStandardStatusGroup.back"/>
				</a>
			</div>
			
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addstandardstatus/{StandardStatusGroup/statusGroupID}" title="{$i18n.AddStandardStatus}">
					<xsl:value-of select="$i18n.AddStandardStatus"/>
					<img class="marginleft" src="{$imgPath}/add.png" alt="" />
				</a>
			</div>
			
			<xsl:if test="StandardStatusGroup/StandardStatuses/StandardStatus">
				<div class="floatright marginright clearright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortstandardstatuses/{StandardStatusGroup/statusGroupID}" title="{$i18n.SortStandardStatuses}">
						<xsl:value-of select="$i18n.SortStandardStatuses"/>
						<img class="marginleft" src="{$imgPath}/move.png" alt="" />
					</a>
				</div>
			</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="StandardStatus" mode="list">
	
		<tr>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardstatus/{statusID}" title="{$i18n.UpdateStandardStatus}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardstatus/{statusID}" title="{$i18n.UpdateStandardStatus}: {name}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
		
				<a class="marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestandardstatus/{statusID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.DeleteStandardStatus}: {name}">
					<img src="{$imgPath}/delete.png" alt="" />
				</a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="AddStandardStatus">
	
		<h1><xsl:value-of select="$i18n.AddStandardStatus"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form id="statusform" method="post" action="{/Document/requestinfo/uri}">
			
			<xsl:call-template name="statusForm">
				<xsl:with-param name="element" select="StandardStatus" />
				<xsl:with-param name="statusMappings" select="StandardStatus/DefaultStandardStatusMappings/DefaultStandardStatusMapping" />
				<xsl:with-param name="hideFlowSpecificSettings" select="true()" />
			</xsl:call-template>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddStandardStatus}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateStandardStatus">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateStandardStatus"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="StandardStatus/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="statusform" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="statusForm">
				<xsl:with-param name="element" select="StandardStatus" />
				<xsl:with-param name="statusMappings" select="StandardStatus/DefaultStandardStatusMappings/DefaultStandardStatusMapping" />
				<xsl:with-param name="hideFlowSpecificSettings" select="true()" />
			</xsl:call-template>
						
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStandardStatus}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="SortStandardStatuses">
	
		<h1>
			<xsl:value-of select="$i18n.SortStandardStatuses" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="StandardStatusGroup/name" />
		</h1>
		
		<form id="statusSortingForm" name="statusSortingForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full sortable">
				
				<xsl:apply-templates select="StandardStatusGroup/StandardStatuses/StandardStatus" mode="sort" />
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SortFlow.submit}" />
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="ListFlowTypes">
	
		<h1><xsl:value-of select="$i18n.ListFlowTypes.title" /></h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<p>
			<xsl:value-of select="$i18n.ListFlowTypes.description" />
		</p>
		
		<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
			<thead>	
				<tr>
					<xsl:if test="FlowTypes/FlowType[iconFileName]">
						<th class="icon" />
					</xsl:if>
					<th><xsl:value-of select="$i18n.name" /></th>
					
					<xsl:if test="/Document/UseCategories">
						<th><xsl:value-of select="$i18n.categories" /></th>
					</xsl:if>
					
					<th><xsl:value-of select="$i18n.flowFamilies" /></th>
					<th width="37" />
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="not(FlowTypes)">
						<tr>
							<td></td>
							<td colspan="4">
								<xsl:value-of select="$i18n.noFlowTypesFound" />
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="FlowTypes/FlowType" mode="list"/>
						
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		
		<xsl:if test="AdminAccess">
			<br/>
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflowtype" title="{$i18n.addFlowType}">
					<xsl:value-of select="$i18n.addFlowType"/>
					<img class="marginleft" src="{$imgPath}/add.png" alt="" />
				</a>
			</div>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="FlowType" mode="list">
	
		<tr>
			<xsl:if test="../FlowType[iconFileName]">
				<td class="icon">
					<xsl:if test="iconFileName">
						<img alt="{iconFileName}" src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtypeicon/{flowTypeID}" width="25" />
					</xsl:if>
				</td>
			</xsl:if>
			<td data-title="{$i18n.name}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtype/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td data-title="{$i18n.categories}">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtype/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
						<xsl:value-of select="count(Categories/Category)"/>
					</a>
				</td>
			</xsl:if>
			
			<td data-title="{$i18n.flowFamilies}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtype/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="flowFamilyCount"/>
				</a>
			</td>
			<td>
				<xsl:if test="../../AdminAccess">
					
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflowtype/{flowTypeID}" title="{$i18n.updateFlowType}: {name}">
						<img src="{$imgPath}/pen.png" alt="" />
					</a>
					
					<xsl:choose>
						<xsl:when test="flowFamilyCount > 0">
	
							<a href="#" onclick="alert('{$i18n.deleteFlowTypeDisabledHasFlows}'); return false;" title="{$i18n.deleteFlowTypeDisabledHasFlows}">
								<img class="marginleft" src="{$imgPath}/delete_gray.png" alt="" />
							</a>
	
						</xsl:when>
						<xsl:otherwise>
	
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowtype/{flowTypeID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteFlowType}: {name}">
								<img class="marginleft" src="{$imgPath}/delete.png" alt="" />
							</a>
	
						</xsl:otherwise>
					</xsl:choose>
					
				</xsl:if>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShowFlowType">
	
		<xsl:if test="AdminAccess">

			<div class="floatright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflowtype/{FlowType/flowTypeID}" title="{$i18n.updateFlowType}: {FlowType/name}">
					<img class="alignbottom" src="{$imgPath}/pen.png" alt="" />
				</a>
				
				<xsl:choose>
					<xsl:when test="flowFamilyCount > 0">
	
						<a href="#" onclick="alert('{$i18n.deleteFlowTypeDisabledHasFlows}'); return false;" title="{$i18n.deleteFlowTypeDisabledHasFlows}">
							<img class="alignbottom" src="{$imgPath}/delete_gray.png" alt="" />
						</a>
	
					</xsl:when>
					<xsl:otherwise>
	
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowtype/{FlowType/flowTypeID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteFlowType}: {FlowType/name}">
							<img class="alignbottom" src="{$imgPath}/delete.png" alt="" />
						</a>
	
					</xsl:otherwise>
				</xsl:choose>
			</div>
	
		</xsl:if>
	
		<h1>
			<xsl:if test="FlowType/iconFileName">
				<img class="flowtype-icon" width="30" alt="{iconFileName}" src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtypeicon/{FlowType/flowTypeID}" />
				<xsl:text>&#160;&#160;</xsl:text>
			</xsl:if>
			<xsl:value-of select="FlowType/name"/>
		</h1>	
	
		<fieldset>
			<legend>
				<img class="alignmiddle" src="{$imgPath}/lock.png" alt="" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.FlowType.AdminAccess.Title"/>
			</legend>
			
			<xsl:if test="AllowedAdminGroups">
				<span class="floatleft bold">
					<xsl:value-of select="$i18n.allowedGroups"/>
				</span>
				
				<xsl:apply-templates select="AllowedAdminGroups/group" mode="list"/>
			</xsl:if>
			
			<xsl:if test="AllowedAdminUsers">
				<span class="floatleft bold">
					<xsl:value-of select="$i18n.allowedUsers"/>
				</span>
				
				<xsl:apply-templates select="AllowedAdminUsers/user" mode="list"/>
			</xsl:if>
			
			<xsl:if test="not(AllowedAdminGroups) and not(AllowedAdminUsers)">
			
				<span class="floatleft">
					<xsl:value-of select="$i18n.onlyModuleAdminAccess"/>
				</span>
			
			</xsl:if>
		</fieldset>
		
		<fieldset>
			<legend>
				<img class="alignmiddle" src="{$imgPath}/lock.png" alt="" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.FlowType.UserAccess.Title"/>
			</legend>
			
			<xsl:choose>
				<xsl:when test="FlowType/useAccessFilter = 'true'">
				
				<xsl:if test="AllowedGroups">
					<span class="floatleft bold">
						<xsl:value-of select="$i18n.allowedGroups"/>
					</span>
					
					<xsl:apply-templates select="AllowedGroups/group" mode="list"/>
				</xsl:if>
				
				<xsl:if test="AllowedUsers">
					<span class="floatleft bold">
						<xsl:value-of select="$i18n.allowedUsers"/>
					</span>
					
					<xsl:apply-templates select="AllowedUsers/user" mode="list"/>
				</xsl:if>
				
				<xsl:if test="not(AllowedGroups) and not(AllowedUsers)">
				
					<span class="floatleft">
						<xsl:value-of select="$i18n.noUserAccess"/>
					</span>
				</xsl:if>
				
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:value-of select="$i18n.noAccessFilter"/>
				
				</xsl:otherwise>
			</xsl:choose>
			
		</fieldset>
		
		<fieldset>
			<legend>
				<img class="alignmiddle" src="{$imgPath}/lock.png" alt="" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.FlowType.FlowPublishedNotificationUsers.Title"/>
			</legend>
			
			<xsl:choose>
				<xsl:when test="FlowPublishedNotificationUsers">
				
					<span class="floatleft bold">
						<xsl:value-of select="$i18n.allowedUsers"/>
					</span>
					
					<xsl:apply-templates select="FlowPublishedNotificationUsers/user" mode="list"/>
				
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:value-of select="$i18n.noFlowPublishedNotificationUsers"/>
				
				</xsl:otherwise>
			</xsl:choose>
			
		</fieldset>
		
		<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes" />

		<fieldset>
			<legend>
				<xsl:value-of select="$i18n.allowedQueryTypes"/>
			</legend>
			
			<xsl:choose>
				<xsl:when test="QueryTypeDescriptors">
				
					<xsl:apply-templates select="QueryTypeDescriptors/QueryTypeDescriptor" mode="list"/>
				
				</xsl:when>
				<xsl:otherwise>
				
					<span class="floatleft">
						<xsl:value-of select="$i18n.noAllowedQueryTypes"/>
					</span>
				
				</xsl:otherwise>
			</xsl:choose>
		</fieldset>

		<xsl:if test="/Document/UseCategories">
			<fieldset>
				<legend>
					<xsl:value-of select="$i18n.categories"/>
				</legend>
				
				<xsl:choose>
					<xsl:when test="FlowType/Categories">
					
						<xsl:apply-templates select="FlowType/Categories/Category" mode="list"/>
					
					</xsl:when>
					<xsl:otherwise>
					
						<span class="floatleft">
							<xsl:value-of select="$i18n.noCategories"/>
						</span>
					
					</xsl:otherwise>
				</xsl:choose>
			</fieldset>
	
			<br/>
			
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addcategory/{FlowType/flowTypeID}" title="{$i18n.addCategory}">
					<xsl:value-of select="$i18n.addCategory"/>
					<img class="alignbottom" src="{$imgPath}/folder_add.png" alt="" />
				</a>
			</div>
		</xsl:if>

	</xsl:template>
	
	<xsl:template match="user" mode="list">
		
		<div class="floatleft full marginbottom border">

			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="alignbottom" src="{$imgPath}/user.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignbottom" src="{$imgPath}/user_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
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
		
		<div class="floatleft full marginbottom border">

			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="alignbottom" src="{$imgPath}/group.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignbottom" src="{$imgPath}/group_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
		</div>	
		
	</xsl:template>
	
	<xsl:template match="QueryTypeDescriptor" mode="list">
		
		<div class="floatleft full marginbottom border">

			<img class="alignbottom" src="{$imgPath}/form.png" alt="" />
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="Category" mode="list">
		
		<div class="floatleft full marginbottom border">

			<img class="alignbottom" src="{$imgPath}/folder.png" alt="" />
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
			
			<div class="floatright marginright">

				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatecategory/{categoryID}" title="{$i18n.updateCategory}: {name}">
					<img class="alignbottom" src="{$imgPath}/pen.png" alt="" />
				</a>

				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletecategory/{categoryID}" onclick="return confirmHyperlinkPost(this)" title="{$i18n.deleteCategory}: {name}">
					<img class="alignbottom" src="{$imgPath}/delete.png" alt="" />
				</a>

			</div>	
		</div>	
		
	</xsl:template>	
	
	<xsl:template match="AddFlowType">
	
		<h1><xsl:value-of select="$i18n.AddFlowType.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
				
			<xsl:call-template name="flowTypeForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddFlowType.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateFlowType">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateFlowType.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="FlowType/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
		
			<xsl:call-template name="flowTypeForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateFlowType.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="flowTypeForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="FlowType" />
				</xsl:call-template>
			</div>
		</div>
		
		<xsl:if test="useFlowTypeIconUpload">
		
			<div class="floatleft full bigmarginbottom">
			
				<label for="icon" class="floatleft full">
					<xsl:value-of select="$i18n.FlowType.Icon" />
				</label>
			
				<div class="upload flowtype-icon-upload clearboth">
					
					<span>
						<input type="file" name="icon" size="55" class="bigmarginbottom" />
					</span>
					
					<span>
						<xsl:value-of select="$i18n.FlowType.UploadIcon" />
					</span>
					
					<xsl:if test="FlowType/iconFileName">
						<div class="flowtype-icon-preview">
							<img alt="{FlowType/iconFileName}" src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtypeicon/{FlowType/flowTypeID}" width="95px" />
							<div class="full margintop">
								<a href="#" class="btn btn-upload btn-blue"><xsl:value-of select="$i18n.FlowType.DeleteIcon" /></a>
								<xsl:call-template name="createHiddenField">
									<xsl:with-param name="name" select="'deleteicon'" />
									<xsl:with-param name="value" select="'false'" />
									<xsl:with-param name="disabled" select="'disabled'" />
								</xsl:call-template>
							</div>
						</div>
					</xsl:if>
					
				</div>
			
			</div>

			<div class="floatleft full bigmarginbottom">
				
					<label for="flowTypeColor" class="floatleft full"><xsl:value-of select="$i18n.FlowType.IconColor" />:</label>
					<div class="floatleft full">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'iconColor'"/>
							<xsl:with-param name="name" select="'iconColor'"/>
							<xsl:with-param name="class" select="'color-input'"/>
							<xsl:with-param name="element" select="FlowType" />
						</xsl:call-template>
					</div>
							
			</div>
		
			<div class="floatleft full bigmarginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useIconOnAllFlows'" />
					<xsl:with-param name="id" select="'useIconOnAllFlows'" />
					<xsl:with-param name="element" select="FlowType" />
				</xsl:call-template>
				
				<label for="useIconOnAllFlows">
					<xsl:value-of select="$i18n.FlowType.UseIconOnAllFlows" />
				</label>
			</div>
		
		</xsl:if>
		
		<fieldset class="floatleft full bigmarginbottom">
		
			<legend><xsl:value-of select="$i18n.FlowType.AdminAccess.Title" /></legend>
		
			<p class="margin">
				<xsl:value-of select="$i18n.FlowType.AdminAccess.Description" />
			</p>
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.allowedGroups" />
			</label>
			
			<xsl:call-template name="GroupList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/groups</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'admingroup'"/>
				<xsl:with-param name="groups" select="AllowedAdminGroups" />
			</xsl:call-template>
		
			<label class="floatleft full">
				<xsl:value-of select="$i18n.allowedUsers" />
			</label>
			
			<xsl:call-template name="UserList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/users</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'adminuser'"/>
				<xsl:with-param name="users" select="AllowedAdminUsers" />
			</xsl:call-template>
		
		</fieldset>
		
		<fieldset class="floatleft full bigmarginbottom">
		
			<legend><xsl:value-of select="$i18n.FlowType.UserAccess.Title" /></legend>
		
			<p class="margin">
				<xsl:value-of select="$i18n.FlowType.UserAccess.Description" />
			</p>
		
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useAccessFilter'" />
					<xsl:with-param name="id" select="'useAccessFilter'" />
					<xsl:with-param name="element" select="FlowType" />
				</xsl:call-template>
				
				<label for="useAccessFilter">
					<xsl:value-of select="$i18n.enableAccessFiltering" />
				</label>
			</div>
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.allowedGroups" />
			</label>
							
			<xsl:call-template name="GroupList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/allgroups</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'group'"/>
				<xsl:with-param name="groups" select="AllowedGroups" />
			</xsl:call-template>
		
			<label class="floatleft full">
				<xsl:value-of select="$i18n.allowedUsers" />
			</label>
			
			<xsl:call-template name="UserList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/allusers</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'user'"/>
				<xsl:with-param name="users" select="AllowedUsers" />
			</xsl:call-template>		
		
			<div class="floatleft full margintop">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'allowAnonymousAccess'" />
					<xsl:with-param name="id" select="'allowAnonymousAccess'" />
					<xsl:with-param name="element" select="FlowType" />
				</xsl:call-template>
				
				<label for="useAccessFilter">
					<xsl:value-of select="$i18n.FlowType.allowAnonymousAccess" />
				</label>
			</div>
		
		</fieldset>
		
		<fieldset class="floatleft full bigmarginbottom">
		
			<legend><xsl:value-of select="$i18n.FlowType.FlowPublishedNotificationUsers.Title" /></legend>
		
			<p class="margin">
				<xsl:value-of select="$i18n.FlowType.FlowPublishedNotificationUsers.Description" />
			</p>
			
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'onlyNotifyOnNewFlowPublications'" />
					<xsl:with-param name="id" select="'onlyNotifyOnNewFlowPublications'" />
					<xsl:with-param name="element" select="FlowType" />
				</xsl:call-template>
				
				<label for="onlyNotifyOnNewFlowPublications">
					<xsl:value-of select="$i18n.onlyNotifyOnNewFlowPublications" />
				</label>
			</div>
		
			<label class="floatleft full">
				<xsl:value-of select="$i18n.allowedUsers" />
			</label>
			
			<xsl:call-template name="UserList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/allusers</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'flowpublishednotificationuser'"/>
				<xsl:with-param name="users" select="FlowPublishedNotificationUsers" />
			</xsl:call-template>		
		
		</fieldset>
		
		<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes" />
		
		<div class="floatleft full bigmarginbottom">

			<div class="floatright marginright margintop">
				<input type="checkbox" id="checkall"/>
			</div>
			
			<label class="floatleft">
				<xsl:value-of select="$i18n.allowedQueryTypes" />
			</label>
			
			<div class="floatleft full">
				<xsl:apply-templates select="QueryTypeDescriptors/QueryTypeDescriptor" mode="scrolllist"/>
			</div>
		</div>
		
	</xsl:template>		
	
	<xsl:template match="QueryTypeDescriptor" mode="scrolllist">
		<div class="floatleft full border marginbottom">
			<div class="floatleft">
				<img class="alignbottom" src="{$imgPath}/form.png" alt="" />
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:value-of select="name"/>
						
			</div>
			<div class="floatright marginright">
				
				<xsl:variable name="queryTypeID" select="queryTypeID"/>
			
				<input type="checkbox" name="queryType" value="{queryTypeID}">
					<xsl:choose>
						<xsl:when test="../../requestparameters">
							<xsl:if test="../../requestparameters/parameter[name='queryType'][value=$queryTypeID]">
								<xsl:attribute name="checked"/>
							</xsl:if>
						</xsl:when>						
						<xsl:when test="../../FlowType">
							<xsl:if test="../../FlowType/allowedQueryTypes[queryTypeID=$queryTypeID]">
								<xsl:attribute name="checked"/>
							</xsl:if>								
						</xsl:when>						
					</xsl:choose>
				</input>
			</div>				
		</div>
	</xsl:template>	

	<xsl:template match="AddCategory">
	
		<h1><xsl:value-of select="$i18n.AddCategory.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="categoryForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddCategory.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateCategory">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateCategory.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Category/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="categoryForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateCategory.submit}" />
			</div>
		
		</form>
	
	</xsl:template>	
	
	<xsl:template name="categoryForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="Category" />          
				</xsl:call-template>
			</div>
		</div>
				
	</xsl:template>	

	<xsl:template match="UpdateFlowFamily">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateManagers.title"/>
			<xsl:text>:&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form method="post" action="{/Document/requestinfo/uri}">
			
			<div class="floatleft full bigmarginbottom">
				
				<label class="floatleft full">
					<xsl:value-of select="$i18n.allowedGroups" />
				</label>
								
				<xsl:call-template name="GroupList">
					<xsl:with-param name="connectorURL">
						<xsl:value-of select="/Document/requestinfo/currentURI"/>
						<xsl:text>/</xsl:text>
						<xsl:value-of select="/Document/module/alias"/>
						<xsl:text>/groups</xsl:text>
					</xsl:with-param>
					<xsl:with-param name="name" select="'manager-group'"/>
					<xsl:with-param name="groups" select="ManagerGroups/FlowFamilyManagerGroup[group]" />
				</xsl:call-template>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label class="floatleft full">
					<xsl:value-of select="$i18n.allowedUsers" />
				</label>
				
				<xsl:call-template name="UserList">
					<xsl:with-param name="connectorURL">
						<xsl:value-of select="/Document/requestinfo/currentURI"/>
						<xsl:text>/</xsl:text>
						<xsl:value-of select="/Document/module/alias"/>
						<xsl:text>/users</xsl:text>
					</xsl:with-param>
					<xsl:with-param name="name" select="'manager'"/>
					<xsl:with-param name="users" select="ManagerUsers/FlowFamilyManager[user]" />
				</xsl:call-template>
				
			</div>
					
			<script type="text/javascript">
				$(function() {
					
					$("#manager-list").children("li.manager-list-entry").each(function(){
						updateManagerShowHideRowExtra($(this));
					});
					
					$("#manager-group-list").children("li.manager-group-list-entry").each(function(){
						updateManagerGroupShowHideRowExtra($(this));
					});
					
					<xsl:if test="ShowManagerModalOnAdd">
					// Overrides UserGroupList.js
					var origFunction = addUserGroupEntry;
					
					addUserGroupEntry = function(item, list, prefix, template, showUserURL) {
						
						var newRow = origFunction.apply(null, arguments);
						
						if (newRow != null) {
							if (prefix == "manager" || prefix == "manager-group") {
								newRow.find("a.open-manager-modal").trigger("click");
							}
						}
					};
					</xsl:if>
					
				});
			</script>
		
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateManagers.submit}" />
			</div>
		
		</form>
		
		<div id="updateManagerModal" style="display: none;" >
			
			<div class="manager-modal contentitem" data-calendaricon="{$imgPath}/calendar_grid.png">
				<div class="modal-content">
				
					<div class="modal-header bigmarginbottom">
						<h1 data-title="{$i18n.UpdateManagers.Modal.Title}" />
					</div>
					
					<div class="modal-body">
					
						<div class="floatleft full bigmarginbottom">
						
							<label for="validFromDate" class="floatleft clearboth">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.validFromDate" />
							</label>
							
							<div class="floatleft full">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'validFromDate'"/>
									<xsl:with-param name="name" select="'validFromDate'"/>
									<xsl:with-param name="type" select="'date'"/>
								</xsl:call-template>
						  </div>
						</div>
						
						<div class="floatleft full bigmarginbottom">
						
							<label for="validToDate" class="floatleft clearboth">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.validToDate" />
							</label>
							
							<div class="floatleft full">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'validToDate'"/>
									<xsl:with-param name="name" select="'validToDate'"/>
									<xsl:with-param name="type" select="'date'"/>
								</xsl:call-template>
						  </div>
						</div>
						
						<div class="floatleft full bigmarginbottom">
							<input type="checkbox" id="restricted1" name="restricted" value="true" />
							<label class="marginleft" for="restricted1">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.restrictedUser" />
							</label>
						</div>
						
						<div class="floatleft full bigmarginbottom">
							<input type="checkbox" id="allowUpdatingManagers1" name="allowUpdatingManagers" value="true" />
							<label class="marginleft" for="allowUpdatingManagers1">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.allowUpdatingManagers" />
							</label>
						</div>
						
						<input class="close bigmargintop clearboth floatright" type="button" value="{$i18n.UpdateManagers.Modal.Close}" />
						
					</div>
					
				</div>
			</div>
		
		</div>
		
		<div id="updateManagerGroupModal" style="display: none;" >
			
			<div class="manager-modal contentitem" data-calendaricon="{$imgPath}/calendar_grid.png">
				<div class="modal-content">
				
					<div class="modal-header bigmarginbottom">
						<h1 data-title="{$i18n.UpdateManagers.Modal.Title}" />
					</div>
					
					<div class="modal-body">
					
						<div class="floatleft full bigmarginbottom">
							<input type="checkbox" id="restricted2" name="restricted" value="true" />
							<label class="marginleft" for="restricted2">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.restrictedGroup" />
							</label>
						</div>
						
						<div class="floatleft full bigmarginbottom">
							<input type="checkbox" id="allowUpdatingManagers2" name="allowUpdatingManagers" value="true" />
							<label class="marginleft" for="allowUpdatingManagers2">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.allowUpdatingManagers" />
							</label>
								<div class="floatleft full bigmarginbottom">
							<input type="checkbox" id="notifyGroupMembersPersonally" name="notifyGroupMembersPersonally" value="true"/>
							<label class="marginleft" for="notifyGroupMembersPersonally">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.notifyGroupMembersPersonally" />
							</label>
							<div class="floatleft full bigmarginbottom bigpaddingleft bigmarginleft">
								<p class="tiny"><xsl:value-of select="$i18n.UpdateManagers.Modal.notifyGroupMembersPersonally.disclamer" /></p>
							</div>
						</div>
						</div>
						
					
								
						<div class="floatleft full bigmarginbottom">
						
							<label for="notificationEmailAddresses" class="floatleft full">
								<xsl:value-of select="$i18n.UpdateManagers.Modal.notificationEmailAddresses" />
							</label>
						
							<div class="tiny"><xsl:value-of select="$i18n.UpdateManagers.Modal.notificationEmailAddresses.info" /></div>
							
							<div class="floatleft margintop full">
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'notificationEmailAddresses'"/>
									<xsl:with-param name="name" select="'notificationEmailAddresses'"/>
									<xsl:with-param name="rows" select="5"/>
									<xsl:with-param name="separateListValues" select="'true'"/>
								</xsl:call-template>
							</div>
						</div>
						
						<input class="close bigmargintop clearboth floatright" type="button" value="{$i18n.UpdateManagers.Modal.Close}" />
						
					</div>
					
				</div>
			</div>
		
		</div>
		
	</xsl:template>
	
	<xsl:template name="userlist-extension-buttons">
		<xsl:param name="listname"/>
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager'">
				
				<span class="bigmarginleft restricted" style="display: none;">
					<xsl:value-of select="$i18n.Manager.restricted" />
				</span>
				
				<span class="bigmarginleft allowUpdatingManagers" style="display: none;">
					<xsl:value-of select="$i18n.Manager.allowUpdatingManagers" />
				</span>
				
				<span class="bigmarginleft validfrom" style="display: none;">
					<xsl:value-of select="$i18n.Manager.validFromDate"/>
					<xsl:text>&#x20;</xsl:text>
					<span/>
				</span>
				
				<span class="bigmarginleft validto" style="display: none;">
					<xsl:value-of select="$i18n.Manager.validToDate"/>
					<xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="validToDate"/>
					<span/>
				</span>
				
				<a class="floatright marginright open-manager-modal" href="#" onclick="openUpdateManagerModal(this, event)" title="{$i18n.UpdateManagers.openModal}">
					<img class="vertical-align-middle" src="{$imgPath}/pen.png" alt="{$i18n.UpdateManagers.openModal}" />
				</a>
			
			</xsl:when>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="grouplist-extension-buttons">
		<xsl:param name="listname"/>
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager-group'">
			
				<span class="bigmarginleft restricted" style="display: none;">
					<xsl:value-of select="$i18n.Manager.restricted" />
				</span>
				
				<span class="bigmarginleft allowUpdatingManagers" style="display: none;">
					<xsl:value-of select="$i18n.Manager.allowUpdatingManagers" />
				</span>
				
				<span class="bigmarginleft notifyGroupMembersPersonally" style="display: none;">
					<xsl:value-of select="$i18n.Manager.notifyGroupMembersPersonally" />
				</span>
			
				<a class="floatright marginright open-manager-modal" href="#" onclick="openUpdateManagerGroupModal(this, event)" title="{$i18n.UpdateManagers.openModal}">
					<img class="vertical-align-middle" src="{$imgPath}/pen.png" alt="{$i18n.UpdateManagers.openModal}" />
				</a>
			
			</xsl:when>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="userlist-extension-defaults">
		<xsl:param name="listname"/>
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager'">
			
				<xsl:call-template name="userlist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'validFromDate'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'validToDate'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'restricted'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'allowUpdatingManagers'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
			
			</xsl:when>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template match="user" mode="userlist-extension">
		<xsl:param name="listname"/>
		<xsl:param name="requestparameters" />
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager'">
			
				<xsl:call-template name="userlist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'validFromDate'" />
					<xsl:with-param name="value" select="../validFromDate" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'validToDate'" />
					<xsl:with-param name="value" select="../validToDate" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'restricted'" />
					<xsl:with-param name="value" select="../restricted" />
				</xsl:call-template>
				
				<xsl:call-template name="userlist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'allowUpdatingManagers'" />
					<xsl:with-param name="value" select="../allowUpdatingManagers" />
				</xsl:call-template>
			
			</xsl:when>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="grouplist-extension-defaults">
		<xsl:param name="listname"/>
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager-group'">
			
				<xsl:call-template name="grouplist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'restricted'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="grouplist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'allowUpdatingManagers'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="grouplist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'notifyGroupMembersPersonally'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
				<xsl:call-template name="grouplist-extension-default">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="name" select="'notificationEmailAddresses'" />
					<xsl:with-param name="value" select="''" />
				</xsl:call-template>
				
			</xsl:when>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template match="group" mode="grouplist-extension">
		<xsl:param name="listname"/>
		<xsl:param name="requestparameters" />
		
		<xsl:choose>
			<xsl:when test="$listname = 'manager-group'">
			
				<xsl:call-template name="grouplist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'restricted'" />
					<xsl:with-param name="value" select="../restricted" />
				</xsl:call-template>
				
				<xsl:call-template name="grouplist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'allowUpdatingManagers'" />
					<xsl:with-param name="value" select="../allowUpdatingManagers" />
				</xsl:call-template>
				
				<xsl:call-template name="grouplist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'notifyGroupMembersPersonally'" />
					<xsl:with-param name="value" select="../notifyGroupMembersPersonally" />
				</xsl:call-template>
				
				<xsl:variable name="notificationEmailAddressesString">
					<xsl:for-each select="../notificationEmailAddresses/address">
					    <xsl:value-of select="." />
					    <xsl:if test="position() != last()"><xsl:text>&#xa;</xsl:text></xsl:if>
					</xsl:for-each>
				</xsl:variable>
				
				<xsl:call-template name="grouplist-extension">
					<xsl:with-param name="listname" select="$listname" />
					<xsl:with-param name="requestparameters" select="$requestparameters" />
					<xsl:with-param name="name" select="'notificationEmailAddresses'" />
					<xsl:with-param name="value" select="$notificationEmailAddressesString" />
				</xsl:call-template>
			
			</xsl:when>
		</xsl:choose>
		
	</xsl:template>

	<xsl:template match="SelectImportTargetType">
	
		<h1><xsl:value-of select="$i18n.SelectImportTargetType.title" /></h1>
				
		<p>
			<xsl:value-of select="$i18n.SelectImportTargetType.description" />
		</p>
		
		<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
			<thead>
				<tr>
					<th><xsl:value-of select="$i18n.name" /></th>
					
					<xsl:if test="/Document/UseCategories">
						<th><xsl:value-of select="$i18n.categories" /></th>
					</xsl:if>
					
					<th><xsl:value-of select="$i18n.flowFamilies" /></th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="not(FlowTypes)">
						<tr>
							<td></td>
							<td colspan="3">
								<xsl:value-of select="$i18n.noFlowTypesFound" />
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="FlowTypes/FlowType" mode="list-import-target"/>
						
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
			
	</xsl:template>

	<xsl:template match="FlowType" mode="list-import-target">
	
	<xsl:variable name="repositoryIndex" select="../../Repository/RepositoryIndex"/>
	<xsl:variable name="sharedFlowID" select="../../SharedFlow/SharedFlowID"/>
	<xsl:variable name="providerID" select="../../ProviderID"/>
	
	<xsl:choose>
	<xsl:when test="$sharedFlowID">
    
		<tr>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}/{$repositoryIndex}/{$sharedFlowID}/{$providerID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}/{RepositoryIndex}/{sharedFlowID}/{$providerID}" title="{$i18n.showFlowType}: {name}">
						<xsl:value-of select="count(Categories/Category)"/>
					</a>
				</td>
			</xsl:if>
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}/{RepositoryIndex}/{sharedFlowID}/{$providerID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="flowFamilyCount"/>
				</a>
			</td>
		</tr>
	</xsl:when>
	<xsl:otherwise>
		<tr>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
						<xsl:value-of select="count(Categories/Category)"/>
					</a>
				</td>
			</xsl:if>
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow/{flowTypeID}" title="{$i18n.showFlowType}: {name}">
					<xsl:value-of select="flowFamilyCount"/>
				</a>
			</td>
		</tr>
	</xsl:otherwise>	
	</xsl:choose>
	
	</xsl:template>

	<xsl:template match="ImportFlow">
	
		<xsl:choose>
			<xsl:when test="Flow">
			
				<h1>
					<xsl:value-of select="$i18n.ImportFlow.NewVersion.title" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="Flow/name"/>
				</h1>
						
				<p>
					<xsl:value-of select="$i18n.ImportFlow.NewVersion.description" />
				</p>			
			
			</xsl:when>
			<xsl:otherwise>
			
				<h1>
					<xsl:value-of select="$i18n.ImportFlow.NewFamily.title" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="FlowType/name"/>
				</h1>
						
				<p>
					<xsl:value-of select="$i18n.ImportFlow.NewFamily.description" />
				</p>				
			
			</xsl:otherwise>
		</xsl:choose>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">

			<xsl:if test="/Document/UseCategories">
				<div class="floatleft full bigmarginbottom">
					
					<label for="flowtype" class="floatleft full">
						<xsl:value-of select="$i18n.flowCategory" />
					</label>
					
					<xsl:apply-templates select="FlowType" mode="categories">
						<xsl:with-param name="selectedValue" select="Flow/Category/categoryID" />
						<xsl:with-param name="requestparameters" select="requestparameters"/>
					</xsl:apply-templates>
					
				</div>
			</xsl:if>
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="flow" class="floatleft full">
					<xsl:value-of select="$i18n.selectFlowFile" />
				</label>
				
				<div class="floatleft full">
					<input type="file" name="flow" id="flow"/>
				</div>
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.ImportFlow.submit}" />
			</div>
		
		</form>
		
	
	</xsl:template>

	<xsl:template match="ImportQueries">
	
		<h1>
			<xsl:value-of select="$i18n.ImportQueries.title" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
				
		<p>
			<xsl:value-of select="$i18n.ImportQueries.description" />
		</p>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="flowtype" class="floatleft full">
					<xsl:value-of select="$i18n.step" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="name" select="'stepID'"/>
						<xsl:with-param name="valueElementName" select="'stepID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="Flow/Steps/Step" />      
						<xsl:with-param name="selectedValue" select="SelectedStep" /> 
					</xsl:call-template>
				</div>
			</div>		
						
			<div class="floatleft full bigmarginbottom">
				
				<label for="flow" class="floatleft full">
					<xsl:value-of select="$i18n.SelectQueryFiles" />
				</label>
				
				<div class="floatleft full">
					<input type="file" name="queries" id="queries" multiple="true"/>
				</div>
			</div>			
			
			<div class="floatright">
				<input type="submit" value="{$i18n.ImportQueries.submit}" />
			</div>
		
		</form>
		
	</xsl:template>
	
	<xsl:template match="ShowFlowFamilyEvents">
	
		<h1>
			<xsl:value-of select="$i18n.Events.Full.Title"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
	
		<p class="nomargin">
			<xsl:value-of select="$i18n.Events.Full.Description"/>
		</p>

		<xsl:choose>
			<xsl:when test="FlowFamilyEvents/FlowFamilyEvent">
			
				<table class="full coloredtable sortabletable oep-table flowfamilyeventstable" cellspacing="0">
					<thead>
						<tr>
							<th><xsl:value-of select="$i18n.Events.message" /></th>
							<th><xsl:value-of select="$i18n.version.title" /></th>
							<th><xsl:value-of select="$i18n.Events.poster" /></th>
							<th><xsl:value-of select="$i18n.Events.added" /></th>
						</tr>
					</thead>
					<tbody>
					
						<xsl:apply-templates select="FlowFamilyEvents/FlowFamilyEvent" mode="list"/>
						
					</tbody>
				</table>

			</xsl:when>
			<xsl:otherwise>
			
				<p><xsl:value-of select="$i18n.Events.FlowFamilyHasNoEvents"/></p>
				
			</xsl:otherwise>
		</xsl:choose>
			
	</xsl:template>
	
	<xsl:template match="AddFlowFamilyEvent">
		
		<h1>
			<xsl:value-of select="$i18n.Events.Add.title"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="Flow/name"></xsl:value-of>
			<xsl:text>&#x20;(</xsl:text>
			<xsl:value-of select="$i18n.flowVersion"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/version"/>
			<xsl:text>)</xsl:text>
		</h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError" />
		
		<form method="post" action="{/Document/requestinfo/uri}">
				
			<div class="floatleft full bigmarginbottom">
				
				<label for="name" class="floatleft full">
					<xsl:value-of select="$i18n.Events.message" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'event-message'"/>
						<xsl:with-param name="name" select="'event-message'"/>
					</xsl:call-template>
				</div>
			</div>	
			
			<div class="floatright">
				<input type="submit" value="{$i18n.Events.Add.submit}" />
			</div>
			
		</form>
	
	</xsl:template>
	
	<xsl:template match="ChangeFlowType">
	
		<h1>
			<xsl:value-of select="$i18n.ChangeFlowType.title"/>
			<xsl:text>:&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<p><xsl:value-of select="$i18n.ChangeFlowType.description"/></p>

		<xsl:apply-templates select="validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="flowTypeID" class="floatleft full">
					<xsl:value-of select="$i18n.ChangeFlowType.ChooseNewFlowType" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'flowTypeID'"/>
						<xsl:with-param name="name" select="'flowTypeID'"/>
						<xsl:with-param name="valueElementName" select="'flowTypeID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="AllowedFlowTypes/FlowType" />
						<xsl:with-param name="selectedValue" select="Flow/FlowType/flowTypeID"/>       
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.ChangeFlowType.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="AutoManagerAssignment">
	
		<h1>
			<xsl:value-of select="$i18n.AutoManagerAssignment.title"/>
			<xsl:text>:&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<p><xsl:value-of select="$i18n.AutoManagerAssignment.description"/></p>

		<xsl:apply-templates select="ValidationErrors/validationError" />
		
		<br/>
		
		<form class="automanagerassignment" method="post" action="{/Document/requestinfo/uri}">

			<h2 class="bigmargintop bigmarginbottom">
				<xsl:value-of select="$i18n.AutoManagerAssignment.On.Submit" />
			</h2>

			<fieldset id="auto-manager-rules" class="clearboth">
				<legend><xsl:value-of select="$i18n.AutoManagerAssignment.Rules"/></legend>
				
				<table class="full oep-table word-break-all">
					<thead>
						<tr>
							<th class="twenty"><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.AttributeName" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Inverted" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.AttributeValues" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Users" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Groups" /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						
						<xsl:apply-templates select="FlowFamily/AutoManagerAssignmentRules/AutoManagerAssignmentRule" />
						
						<tr id="auto-manager-rule-template" class="auto-manager-rule new_row" style="display: none;">
				
							<td class="auto-manager-attribute" />
							<td class="auto-manager-invert">
								<span>
									<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Inverted" />
								</span>
							</td>
							<td class="auto-manager-values" />
							<td class="auto-manager-users">
								<span />
								<div style="display: none;" />
							</td>
							<td class="auto-manager-groups">
								<span />
								<div style="display: none;" />
							</td>
							
							<td>
								<input type="hidden" disabled="true" name="auto-manager-rule" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-attribute" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-values" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-invert" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-includeUnsetAttribute" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-users" value="" />
								<input type="hidden" disabled="true" name="auto-manager-rule-groups" value="" />
							
								<a class="marginright open-auto-manager-modal" href="#" onclick="openAutoManagerAssignmentRuleModal(this, event)" title="{$i18n.AutoManagerAssignment.Rule.Update}">
									<img src="{$imgPath}/pen.png" alt="" />
								</a>
								<a class="" href="#" onclick="removeAutoManagerAssignmentRule(this, event, '{$i18n.AutoManagerAssignment.Rule.DeleteConfirm}?')" title="{$i18n.AutoManagerAssignment.Rule.Delete}">
									<img src="{$imgPath}/delete.png"/>
								</a>
							</td>
							
						</tr>
					</tbody>
				</table>
				
				<a class="floatright marginright" href="#" onclick="addAutoManagerAssignmentRule(this, event)" title="{$i18n.AutoManagerAssignment.Rules.Add}">
					<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Add"/>
					<img class="marginleft vertical-align-middle" src="{$imgPath}/add.png" alt="" />
				</a>
				
			</fieldset>
			
			<div id="updateAutoManagerModal" style="display: none;" >
				
				<div class="auto-manager-modal contentitem">
					<div class="modal-content">
					
						<div id="updateAutoManagerModalHeader" class="modal-header bigmarginbottom">
							<h1>
								<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.Update" />
							</h1>
							<h1 class="hidden">
								<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Add" />
							</h1>
						</div>
						
						<div class="modal-body">
						
							<div class="floatleft full bigmarginbottom">
							
								<label for="attribute" class="floatleft full">
									<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeName" />
								</label>
								
								<div class="floatleft full">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'attribute'"/>
										<xsl:with-param name="name" select="'attribute'"/>
									</xsl:call-template>
								</div>
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'invert'" />
										<xsl:with-param name="id" select="'invert'" />
									</xsl:call-template>
									
									<label class="marginleft" for="invert">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.Invert" />
									</label>
								</div>
							</div>

							<div id="includeUnsetAttributeContainer" class="floatleft full hidden">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'includeUnsetAttribute'" />
										<xsl:with-param name="id" select="'includeUnsetAttribute'" />
									</xsl:call-template>
									
									<label class="marginleft" for="includeUnsetAttribute">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.IncludeUnsetAttribute" />
									</label>
								</div>
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<label for="values" class="floatleft">
									<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeValues" />
								</label>
								
								<div class="floatleft full">
									<xsl:call-template name="createTextArea">
										<xsl:with-param name="id" select="'values'"/>
										<xsl:with-param name="name" select="'values'"/>
									</xsl:call-template>
								</div>
							</div>
							
							<div class="floatleft full usergrouplist-split marginbottom">
				
								<div class="floatleft bigmarginright">
									
									<label class="">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Users" />
									</label>
									
									<xsl:call-template name="UserList">
										<xsl:with-param name="connectorURL">
											<xsl:value-of select="/Document/requestinfo/currentURI"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="/Document/module/alias"/>
											<xsl:text>/managerusers/</xsl:text>
											<xsl:value-of select="Flow/flowID"/>
										</xsl:with-param>
										<xsl:with-param name="name" select="'template-user'"/>
									</xsl:call-template>
								
								</div>
								
								<div class="floatleft">
								
									<label class="">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Groups" />
									</label>
													
									<xsl:call-template name="GroupList">
										<xsl:with-param name="connectorURL">
											<xsl:value-of select="/Document/requestinfo/currentURI"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="/Document/module/alias"/>
											<xsl:text>/managergroups/</xsl:text>
											<xsl:value-of select="Flow/flowID"/>
										</xsl:with-param>
										<xsl:with-param name="name" select="'template-group'"/>
									</xsl:call-template>
									
								</div>
								
							</div>
							
							<input class="close bigmargintop floatright" type="button" value="{$i18n.AutoManagerAssignment.Rule.Save}" />
							
						</div>
						
					</div>
				</div>
			
			</div>
			
			<fieldset class="clearboth">
				<legend><xsl:value-of select="$i18n.AutoManagerAssignment.NoMatch"/></legend>
			
				<div class="floatleft full usergrouplist-split marginbottom">
					
					<div class="floatleft bigmarginright">
					
						<label class="">
							<xsl:value-of select="$i18n.AutoManagerAssignment.Users" />
						</label>
						
						<xsl:call-template name="UserList">
							<xsl:with-param name="connectorURL">
								<xsl:value-of select="/Document/requestinfo/currentURI"/>
								<xsl:text>/</xsl:text>
								<xsl:value-of select="/Document/module/alias"/>
								<xsl:text>/managerusers/</xsl:text>
								<xsl:value-of select="Flow/flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name" select="'auto-manager-nomatch-user'"/>
							<xsl:with-param name="users" select="AutoManagerAssignmentNoMatchUsers" />
						</xsl:call-template>
					
					</div>
					
					<div class="floatleft">
					
						<label class="">
							<xsl:value-of select="$i18n.AutoManagerAssignment.Groups" />
						</label>
										
						<xsl:call-template name="GroupList">
							<xsl:with-param name="connectorURL">
								<xsl:value-of select="/Document/requestinfo/currentURI"/>
								<xsl:text>/</xsl:text>
								<xsl:value-of select="/Document/module/alias"/>
								<xsl:text>/managergroups/</xsl:text>
								<xsl:value-of select="Flow/flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name" select="'auto-manager-nomatch-group'"/>
							<xsl:with-param name="groups" select="AutoManagerAssignmentNoMatchGroups" />
						</xsl:call-template>
						
					</div>
					
				</div>
			
			</fieldset>
			
			<fieldset class="bigmarginbottom">
				<legend><xsl:value-of select="$i18n.AutoManagerAssignment.Always"/></legend>
			
				<div class="floatleft full usergrouplist-split marginbottom">
					
					<div class="floatleft bigmarginright">
					
						<label class="">
							<xsl:value-of select="$i18n.AutoManagerAssignment.Users" />
						</label>
						
						<xsl:call-template name="UserList">
							<xsl:with-param name="connectorURL">
								<xsl:value-of select="/Document/requestinfo/currentURI"/>
								<xsl:text>/</xsl:text>
								<xsl:value-of select="/Document/module/alias"/>
								<xsl:text>/managerusers/</xsl:text>
								<xsl:value-of select="Flow/flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name" select="'auto-manager-always-user'"/>
							<xsl:with-param name="users" select="AutoManagerAssignmentAlwaysUsers" />
						</xsl:call-template>
					
					</div>
					
					<div class="floatleft">
					
						<label class="">
							<xsl:value-of select="$i18n.AutoManagerAssignment.Groups" />
						</label>
										
						<xsl:call-template name="GroupList">
							<xsl:with-param name="connectorURL">
								<xsl:value-of select="/Document/requestinfo/currentURI"/>
								<xsl:text>/</xsl:text>
								<xsl:value-of select="/Document/module/alias"/>
								<xsl:text>/managergroups/</xsl:text>
								<xsl:value-of select="Flow/flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name" select="'auto-manager-always-group'"/>
							<xsl:with-param name="groups" select="AutoManagerAssignmentAlwaysGroups" />
						</xsl:call-template>
						
					</div>
					
				</div>
			</fieldset>
			
			<br/>
			
			<h2 class="bigmargintop bigmarginbottom">
				<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
			</h2>
			
			<fieldset id="auto-manager-status-rules" class="clearboth">
				<legend><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRules"/></legend>
				
				<table class="full oep-table word-break-all">
					<thead>
						<tr>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRules.StatusName" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRules.AddManagers" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Users" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Groups" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRules.RemovePreviousManagers" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.UseStatusAttributeColumnTitle" /></th>
							<th><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.SendNotificationColumnTitle" /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						
						<xsl:apply-templates select="FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule" />
						
						<tr id="auto-manager-status-rule-template" class="auto-manager-status-rule new_row" style="display: none;">
				
							<td class="auto-manager-status-name" />
							<td class="auto-manager-add-managers">
								<span>
									<xsl:value-of select="$i18n.Yes" />
								</span>
							</td>
							<td class="auto-manager-users">
								<span />
								<div style="display: none;" />
							</td>
							<td class="auto-manager-groups">
								<span />
								<div style="display: none;" />
							</td>
							<td class="auto-manager-remove-previous-managers">
								<span>
									<xsl:value-of select="$i18n.Yes" />
								</span>
							</td>
							<td class="auto-manager-useStatusAttribute">
								<span>
									<xsl:value-of select="$i18n.Yes" />
								</span>
							</td>
							<td class="auto-manager-send-notification">
								<span>
									<xsl:value-of select="$i18n.Yes" />
								</span>
							</td>
							
							<td>
								<input type="hidden" disabled="true" name="auto-manager-status-rule" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-statusName" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-addManagers" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-users" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-groups" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-removePreviousManagers" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-sendNotification" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-emailRecipients" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-useStatusAttribute" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-statusAttributeName" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-statusAttributeInvert" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-includeUnsetStatusAttribute" value="" />
								<input type="hidden" disabled="true" name="auto-manager-status-rule-statusAttributeValues" value="" />
							
								<a class="marginright open-auto-manager-status-modal" href="#" onclick="openAutoManagerAssignmentStatusRuleModal(this, event)" title="{$i18n.AutoManagerAssignment.Rule.Update}">
									<img src="{$imgPath}/pen.png" alt="" />
								</a>
								<a class="" href="#" onclick="removeAutoManagerAssignmentStatusRule(this, event, '{$i18n.AutoManagerAssignment.Rule.DeleteConfirm}?')" title="{$i18n.AutoManagerAssignment.Rule.Delete}">
									<img src="{$imgPath}/delete.png"/>
								</a>
							</td>
							
						</tr>
					</tbody>
				</table>
				
				<a class="floatright marginright" href="#" onclick="addAutoManagerAssignmentStatusRule(this, event)" title="{$i18n.AutoManagerAssignment.Rules.Add}">
					<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Add"/>
					<img class="marginleft vertical-align-middle" src="{$imgPath}/add.png" alt="" />
				</a>
				
			</fieldset>
			
			<div id="updateAutoManagerStatusModal" style="display: none;" >
				
				<div class="auto-manager-status-modal contentitem">
					<div class="modal-content">
					
						<div id="updateAutoManagerStatusModalHeader" class="modal-header bigmarginbottom">
							<h1>
								<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.Update" />
							</h1>
							<h1 class="hidden">
								<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Add" />
							</h1>
						</div>
						
						<div class="modal-body">
						
							<div class="floatleft full bigmarginbottom">
							
								<label for="statusName" class="floatleft full">
									<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.StatusName" />
								</label>
								
								<div class="floatleft full" data-connector-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/statuses/{Flow/flowID}">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'statusName'"/>
										<xsl:with-param name="name" select="'statusName'"/>
									</xsl:call-template>
								</div>
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'addManagers'" />
										<xsl:with-param name="id" select="'addManagers'" />
									</xsl:call-template>
									
									<label class="marginleft" for="addManagers">
										<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.AddManagers" />
									</label>
								</div>
							</div>
							
							<div id="updateAutoManagerStatusManagerContainer" class="floatleft full usergrouplist-split marginbottom hidden">
				
								<div class="floatleft bigmarginright">
									
									<label class="">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Users" />
									</label>
									
									<xsl:call-template name="UserList">
										<xsl:with-param name="connectorURL">
											<xsl:value-of select="/Document/requestinfo/currentURI"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="/Document/module/alias"/>
											<xsl:text>/managerusers/</xsl:text>
											<xsl:value-of select="Flow/flowID"/>
										</xsl:with-param>
										<xsl:with-param name="name" select="'status-template-user'"/>
									</xsl:call-template>
								
								</div>
								
								<div class="floatleft">
								
									<label class="">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Groups" />
									</label>
													
									<xsl:call-template name="GroupList">
										<xsl:with-param name="connectorURL">
											<xsl:value-of select="/Document/requestinfo/currentURI"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="/Document/module/alias"/>
											<xsl:text>/managergroups/</xsl:text>
											<xsl:value-of select="Flow/flowID"/>
										</xsl:with-param>
										<xsl:with-param name="name" select="'status-template-group'"/>
									</xsl:call-template>
									
								</div>
								
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'removePreviousManagers'" />
										<xsl:with-param name="id" select="'removePreviousManagers'" />
									</xsl:call-template>
									
									<label class="marginleft" for="removePreviousManagers">
										<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.RemovePreviousManagers" />
									</label>
								</div>
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'sendNotification'" />
										<xsl:with-param name="id" select="'sendNotification'" />
									</xsl:call-template>
									
									<label class="marginleft" for="sendNotification">
										<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.SendNotification" />
									</label>
								</div>
							</div>
							
							<div id="updateAutoManagerStatusNotificationContainer" class="floatleft full bigmarginbottom hidden">
							
								<label for="emailRecipients" class="floatleft full">
									<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.EmailRecipients" />
								</label>
								
								<div class="floatleft full">
									<xsl:call-template name="createTextArea">
										<xsl:with-param name="id" select="'emailRecipients'"/>
										<xsl:with-param name="name" select="'emailRecipients'"/>
										<xsl:with-param name="rows" select="5"/>
										<xsl:with-param name="separateListValues" select="'true'"/>
									</xsl:call-template>
								</div>
							</div>
							
							<div class="floatleft full">
							
								<div class="floatleft">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="name" select="'useStatusAttribute'" />
										<xsl:with-param name="id" select="'useStatusAttribute'" />
									</xsl:call-template>
									
									<label class="marginleft" for="useStatusAttribute">
										<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.UseStatusAttribute" />
									</label>
								</div>
								
							</div>

							<div id="statusAttributeContainer" class="hidden">
															
								<div class="floatleft full bigmarginbottom">
									<p class="tiny"><xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.UseStatusAttributeInfo" /></p>
								</div>
								
								<div class="floatleft full marginbottom">
								
									<label for="statusAttributeName" class="floatleft full">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeName" />
									</label>
									
									<div class="floatleft full">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="id" select="'statusAttributeName'"/>
											<xsl:with-param name="name" select="'statusAttributeName'"/>
										</xsl:call-template>
									</div>
								</div>
								
								<div class="floatleft full">
								
									<div class="floatleft">
										<xsl:call-template name="createCheckbox">
											<xsl:with-param name="name" select="'statusAttributeInvert'" />
											<xsl:with-param name="id" select="'statusAttributeInvert'" />
										</xsl:call-template>
										
										<label class="marginleft" for="statusAttributeInvert">
											<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.Invert" />
										</label>
									</div>
								</div>

								<div id="includeUnsetStatusAttributeContainer" class="floatleft full hidden">
								
									<div class="floatleft">
										<xsl:call-template name="createCheckbox">
											<xsl:with-param name="name" select="'includeUnsetStatusAttribute'" />
											<xsl:with-param name="id" select="'includeUnsetStatusAttribute'" />
										</xsl:call-template>
										
										<label class="marginleft" for="includeUnsetStatusAttribute">
											<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.IncludeUnsetStatusAttribute" />
										</label>
									</div>
								</div>
								
								<div class="floatleft full bigmarginbottom">
								
									<label for="values" class="floatleft">
										<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeValues" />
									</label>
									
									<div class="floatleft full">
										<xsl:call-template name="createTextArea">
											<xsl:with-param name="id" select="'statusAttributeValues'"/>
											<xsl:with-param name="name" select="'statusAttributeValues'"/>
										</xsl:call-template>
									</div>
								</div>
							</div>
							
							<input class="close bigmargintop floatright" type="button" value="{$i18n.AutoManagerAssignment.Rule.Save}" />
							
						</div>
						
					</div>
				</div>
			
			</div>			
			
			<div class="floatright bigmargintop">
				<input type="submit" value="{$i18n.AutoManagerAssignment.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="AutoManagerAssignmentRule">
		<xsl:param name="requestparameters" select="../../../requestparameters"/>
	
		<tr class="auto-manager-rule">
		
			<xsl:variable name="ruleID">
				<xsl:choose>
					<xsl:when test="ruleID">
						<xsl:value-of select="ruleID"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generatedRuleID"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="attribute">
				<xsl:choose>
					<xsl:when test="$requestparameters">
						<xsl:value-of select="$requestparameters/parameter[name = concat('auto-manager-rule-attribute-', $ruleID)]/value" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="attributeName" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<td class="auto-manager-attribute">
				<xsl:value-of select="$attribute" />
			</td>
			<td class="auto-manager-invert">
				<span>
					<xsl:if test="not(invert = 'true')">
						<xsl:attribute name="style">display: none;</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Inverted" />
				</span>
			</td>
			<td class="auto-manager-values">
				<xsl:choose>
						<xsl:when test="$requestparameters">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="$requestparameters/parameter[name = concat('auto-manager-rule-values-', $ruleID)]/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="', '"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
						
							<xsl:for-each select="Values/*">
								
								<xsl:if test="position() > 1">
									<xsl:text>, </xsl:text>
								</xsl:if>
								
								<xsl:value-of select="."/>
								
							</xsl:for-each>
							
						</xsl:otherwise>
					</xsl:choose>
			</td>
			<td class="auto-manager-users">
				<span>
					<xsl:value-of select="count(UserIDs/*)" />
				</span>
				<div style="display: none;">
					<xsl:apply-templates select="Users/user" mode="userlist-fromXML">
						<xsl:with-param name="prefix" select="'template-user'" />
						<xsl:with-param name="showEmail" select="false()" />
						<xsl:with-param name="showUsername" select="true()" />
						<xsl:with-param name="document" select="/Document" />
						<xsl:with-param name="showUserURL" select="null" />
						<xsl:with-param name="useExternalIcons" select="null"/>
					</xsl:apply-templates>
				</div>
			</td>
			<td class="auto-manager-groups">
				<span>
					<xsl:value-of select="count(GroupIDs/*)" />
				</span>
				<div style="display: none;">
					<xsl:apply-templates select="Groups/group" mode="grouplist-fromXML">
						<xsl:with-param name="prefix" select="'template-group'" />
						<xsl:with-param name="document" select="/Document" />
						<xsl:with-param name="useExternalIcons" select="null"/>
					</xsl:apply-templates>
				</div>
			</td>
			
			<td>
				<xsl:variable name="userIDs">
					<xsl:for-each select="UserIDs/*">
						
						<xsl:if test="position() > 1">
							<xsl:text>,</xsl:text>
						</xsl:if>
						
						<xsl:value-of select="."/>
						
					</xsl:for-each>
				</xsl:variable>
				
				<xsl:variable name="groupIDs">
					<xsl:for-each select="GroupIDs/*">
					
						<xsl:if test="position() > 1">
							<xsl:text>,</xsl:text>
						</xsl:if>
						
						<xsl:value-of select="."/>
					
					</xsl:for-each>
				</xsl:variable>
				
				<xsl:variable name="values">
					<xsl:choose>
						<xsl:when test="$requestparameters">
							
							<xsl:value-of select="$requestparameters/parameter[name = concat('auto-manager-rule-values-', $ruleID)]/value"/>
							
						</xsl:when>
						<xsl:otherwise>
							
							<xsl:for-each select="Values/*">
								
								<xsl:if test="position() > 1">
									<xsl:text>&#13;</xsl:text>
								</xsl:if>
								
								<xsl:value-of select="."/>
								
							</xsl:for-each>
							
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<input type="hidden" name="auto-manager-rule" value="{$ruleID}" />
				<input type="hidden" name="auto-manager-rule-attribute-{$ruleID}" value="{$attribute}" />
				<input type="hidden" name="auto-manager-rule-values-{$ruleID}" value="{$values}" />
				<input type="hidden" name="auto-manager-rule-invert-{$ruleID}" value="{invert}" />
				<input type="hidden" name="auto-manager-rule-includeUnsetAttribute-{$ruleID}" value="{includeUnsetAttribute}" />
				<input type="hidden" name="auto-manager-rule-users-{$ruleID}" value="{$userIDs}" />
				<input type="hidden" name="auto-manager-rule-groups-{$ruleID}" value="{$groupIDs}" />
			
				<a class="marginright open-auto-manager-modal" href="#" onclick="openAutoManagerAssignmentRuleModal(this, event)" title="{$i18n.AutoManagerAssignment.Rule.Update}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
				<a class="" href="#" onclick="removeAutoManagerAssignmentRule(this, event, '{$i18n.AutoManagerAssignment.Rule.DeleteConfirm}?')" title="{$i18n.AutoManagerAssignment.Rule.Delete}">
					<img src="{$imgPath}/delete.png"/>
				</a>
			</td>
			
		</tr>
	
	</xsl:template>
	
	<xsl:template match="AutoManagerAssignmentStatusRule">
		<xsl:param name="requestparameters" select="../../../requestparameters"/>
		
		<tr class="auto-manager-status-rule">
		
			<xsl:variable name="ruleID">
				<xsl:choose>
					<xsl:when test="ruleID">
						<xsl:value-of select="ruleID"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generatedRuleID"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
		
			<xsl:variable name="statusName">
				<xsl:choose>
					<xsl:when test="$requestparameters">
						<xsl:value-of select="$requestparameters/parameter[name = concat('auto-manager-status-rule-statusName-', $ruleID)]/value" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="statusName" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<td class="auto-manager-status-name">
				<xsl:value-of select="$statusName" />
			</td>
			<td class="auto-manager-add-managers">
				<span>
					<xsl:if test="not(addManagers = 'true')">
						<xsl:attribute name="style">display: none;</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i18n.Yes" />
				</span>
			</td>
			<td class="auto-manager-users">
				<span>
					<xsl:value-of select="count(UserIDs/*)" />
				</span>
				<div style="display: none;">
					<xsl:apply-templates select="Users/user" mode="userlist-fromXML">
						<xsl:with-param name="prefix" select="'status-template-user'" />
						<xsl:with-param name="showEmail" select="false()" />
						<xsl:with-param name="showUsername" select="true()" />
						<xsl:with-param name="document" select="/Document" />
						<xsl:with-param name="showUserURL" select="null" />
						<xsl:with-param name="useExternalIcons" select="null"/>
					</xsl:apply-templates>
				</div>
			</td>
			<td class="auto-manager-groups">
				<span>
					<xsl:value-of select="count(GroupIDs/*)" />
				</span>
				<div style="display: none;">
					<xsl:apply-templates select="Groups/group" mode="grouplist-fromXML">
						<xsl:with-param name="prefix" select="'status-template-group'" />
						<xsl:with-param name="document" select="/Document" />
						<xsl:with-param name="useExternalIcons" select="null"/>
					</xsl:apply-templates>
				</div>
			</td>
			<td class="auto-manager-remove-previous-managers">
				<span>
					<xsl:if test="not(removePreviousManagers = 'true')">
						<xsl:attribute name="style">display: none;</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i18n.Yes" />
				</span>
			</td>
			<td class="auto-manager-useStatusAttribute">
				<span>
					<xsl:if test="not(useStatusAttribute = 'true')">
						<xsl:attribute name="style">display: none;</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i18n.Yes" />
				</span>
			</td>
			<td class="auto-manager-send-notification">
				<span>
					<xsl:if test="not(sendNotification = 'true')">
						<xsl:attribute name="style">display: none;</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i18n.Yes" />
				</span>
			</td>
			<td>
				<xsl:variable name="userIDs">
					<xsl:for-each select="UserIDs/*">
						
						<xsl:if test="position() > 1">
							<xsl:text>,</xsl:text>
						</xsl:if>
						
						<xsl:value-of select="."/>
						
					</xsl:for-each>
				</xsl:variable>
				
				<xsl:variable name="groupIDs">
					<xsl:for-each select="GroupIDs/*">
					
						<xsl:if test="position() > 1">
							<xsl:text>,</xsl:text>
						</xsl:if>
						
						<xsl:value-of select="."/>
					
					</xsl:for-each>
				</xsl:variable>
				
				<xsl:variable name="emailRecipients">
					<xsl:for-each select="EmailRecipients/value">
						
						<xsl:value-of select="."/>
						<xsl:text>&#13;</xsl:text>
					
					</xsl:for-each>
				</xsl:variable>

				<xsl:variable name="statusAttributeValues">
					<xsl:for-each  select="StatusAttributeValues/value">
						
						<xsl:value-of select="."/>
						<xsl:text>&#13;</xsl:text>
					
					</xsl:for-each>
				</xsl:variable>
				
				<input type="hidden" name="auto-manager-status-rule" value="{$ruleID}" />
				<input type="hidden" name="auto-manager-status-rule-statusName-{$ruleID}" value="{$statusName}" />
				<input type="hidden" name="auto-manager-status-rule-addManagers-{$ruleID}" value="{addManagers}" />
				<input type="hidden" name="auto-manager-status-rule-users-{$ruleID}" value="{$userIDs}" />
				<input type="hidden" name="auto-manager-status-rule-groups-{$ruleID}" value="{$groupIDs}" />
				<input type="hidden" name="auto-manager-status-rule-removePreviousManagers-{$ruleID}" value="{removePreviousManagers}" />
				<input type="hidden" name="auto-manager-status-rule-sendNotification-{$ruleID}" value="{sendNotification}" />
				<input type="hidden" name="auto-manager-status-rule-emailRecipients-{$ruleID}" value="{$emailRecipients}" />
				<input type="hidden" name="auto-manager-status-rule-useStatusAttribute-{$ruleID}" value="{useStatusAttribute}" />
				<input type="hidden" name="auto-manager-status-rule-statusAttributeName-{$ruleID}" value="{statusAttributeName}" />
				<input type="hidden" name="auto-manager-status-rule-statusAttributeInvert-{$ruleID}" value="{statusAttributeInvert}" />
				<input type="hidden" name="auto-manager-status-rule-includeUnsetStatusAttribute-{$ruleID}" value="{includeUnsetStatusAttribute}" />
				<input type="hidden" name="auto-manager-status-rule-statusAttributeValues-{$ruleID}" value="{$statusAttributeValues}" />
			
				<a class="marginright open-auto-manager-status-modal" href="#" onclick="openAutoManagerAssignmentStatusRuleModal(this, event)" title="{$i18n.AutoManagerAssignment.Rule.Update}">
					<img src="{$imgPath}/pen.png" alt="" />
				</a>
				<a class="" href="#" onclick="removeAutoManagerAssignmentStatusRule(this, event, '{$i18n.AutoManagerAssignment.Rule.DeleteConfirm}?')" title="{$i18n.AutoManagerAssignment.Rule.Delete}">
					<img src="{$imgPath}/delete.png"/>
				</a>
			</td>
			
		</tr>
	
	</xsl:template>
	
	<xsl:template name="ExtraFlowListColumnsHeader" />

	<xsl:template match="validationError[messageKey='UnableToParseFile']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToParseFile.part1"/>
			<xsl:value-of select="filename"/>
			<xsl:value-of select="$i18n.UnableToParseFile.part2"/>
		</p>
			
	</xsl:template>

	<xsl:template match="validationError[messageKey='InvalidFileExtension']">
	
		<p class="error">
			<xsl:value-of select="$i18n.InvalidFileExtension.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="filename" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.InvalidFileExtension.Part2" />

			<xsl:if test="AllowedExtensions">
			
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$i18n.InvalidFileExtension.Part3" />
				<xsl:text>&#160;</xsl:text>
				
				<xsl:apply-templates select="AllowedExtensions/Extension" mode="AllowedExtensions"/>
			</xsl:if>			
		</p>
	</xsl:template>
	
	<xsl:template match="Extension" mode="AllowedExtensions">
	
		<xsl:value-of select="."/>
		
		<xsl:if test="position() != last()">
			<xsl:text>, </xsl:text>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="ViewFragmentExtension">
		
		<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
		
	</xsl:template>
	
	<xsl:template match="AddMessageTemplate">
		
		<h1>
			<xsl:value-of select="$i18n.MessageTemplates.add"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form method="post" action="{/Document/requestinfo/uri}">
			
			<xsl:call-template name="messageTemplateForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.MessageTemplates.submit}" />
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateMessageTemplate">
		
		<h1>
			<xsl:value-of select="$i18n.MessageTemplates.update"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="MessageTemplate/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form method="post" action="{/Document/requestinfo/uri}">
			
			<xsl:call-template name="messageTemplateForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.MessageTemplates.submit}" />
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template name="messageTemplateForm">
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="messageTemplateName" class="floatleft nomargin">
				<xsl:value-of select="$i18n.MessageTemplate.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'messageTemplateName'"/>
					<xsl:with-param name="name" select="'messageTemplateName'"/>
					<xsl:with-param name="value" select="MessageTemplate/name"/>
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="messageTemplateMessage" class="floatleft full">
				<xsl:value-of select="$i18n.MessageTemplate.message" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'messageTemplateMessage'"/>
					<xsl:with-param name="name" select="'messageTemplateMessage'"/>
					<xsl:with-param name="value" select="MessageTemplate/message"/>
				</xsl:call-template>
			</div>
		</div>
		
		<div>
			
			<label for="messageTemplateType" class="floatleft full">
				<xsl:value-of select="$i18n.MessageTemplate.type" />
			</label>
		
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'messageTemplateTypeExternal'"/>
						<xsl:with-param name="name" select="'messageTemplateType'"/>
						<xsl:with-param name="value" select="'EXTERNAL'"/>
						<xsl:with-param name="checked" select="MessageTemplate/type = 'EXTERNAL'" />
					</xsl:call-template>
					
					<label for="messageTemplateTypeExternal">
						<xsl:value-of select="$i18n.MessageTemplate.type.external" />
					</label>
				</div>
			</div>	
		
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'messageTemplateTypeInternal'"/>
						<xsl:with-param name="name" select="'messageTemplateType'"/>
						<xsl:with-param name="value" select="'INTERNAL'"/>
						<xsl:with-param name="checked" select="MessageTemplate/type = 'INTERNAL'" />
					</xsl:call-template>
					
					<label for="messageTemplateTypeInternal">
						<xsl:value-of select="$i18n.MessageTemplate.type.internal" />
					</label>
				</div>
			</div>	
		
			<div class="floatleft full bigmarginbottom">
							
				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'messageTemplateTypeAll'"/>
						<xsl:with-param name="name" select="'messageTemplateType'"/>
						<xsl:with-param name="value" select="'ALL'"/>
						<xsl:with-param name="checked" select="MessageTemplate/type = 'ALL'" />
					</xsl:call-template>
					
					<label for="messageTemplateTypeAll">
						<xsl:value-of select="$i18n.MessageTemplate.type.all" />
					</label>
				</div>
			</div>	
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateManagementInfo">
		
		<xsl:variable name="managementInfo" select="Flow/FlowFamily/ManagementInfo" />
		
		<h1>
			<xsl:value-of select="$i18n.updateManagementInfo"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateManagementInfo" class="bigmargintop" method="post" action="{/Document/requestinfo/uri}">
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="processOwner" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.processOwner" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'processOwner'"/>
						<xsl:with-param name="name" select="'processOwner'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="flowResponsible" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.flowResponsible" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'flowResponsible'"/>
						<xsl:with-param name="name" select="'flowResponsible'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="informationResponsible" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.informationResponsible" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'informationResponsible'"/>
						<xsl:with-param name="name" select="'informationResponsible'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="status" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.status" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'status'"/>
						<xsl:with-param name="name" select="'status'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="organization" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.organization" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'organization'"/>
						<xsl:with-param name="name" select="'organization'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="lastReviewed" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.lastReviewed" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'lastReviewed'"/>
						<xsl:with-param name="name" select="'lastReviewed'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
						<xsl:with-param name="type" select="'date'"/>
						<xsl:with-param name="title" select="$i18n.managementInfo.lastReviewed.Details"/>
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="aboutFlow" class="floatleft nomargin">
					<xsl:value-of select="$i18n.managementInfo.aboutFlow" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'aboutFlow'"/>
						<xsl:with-param name="name" select="'aboutFlow'"/>
						<xsl:with-param name="element" select="$managementInfo"/>
						<xsl:with-param name="rows" select="5"/>
					</xsl:call-template>
				</div>
				
			</div>

			<div class="bigmargintop floatright">
				<input type="submit" value="{$i18n.updateManagementInfo.submit}" />
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerAllStepsForm">
	
		<xsl:if test="ManagerResponse/concurrentModificationLock = 'true'">
			<section class="modal error">
					<span data-icon-before="!">
						<a href="{/Document/requestinfo/uri}?close-reopen=1" onclick="return confirm('{$i18n.FlowInstanceConcurrentlyModifiedConfirm}')" title="{$i18n.FlowInstanceConcurrentlyModifiedLinkTitle}">
							<xsl:value-of select="$i18n.FlowInstanceConcurrentlyModified" />
						</a>
					</span>
				</section>
		</xsl:if>
	
		<xsl:if test="ManagerResponse/ValidationErrors != 'true' and not(validationError)">
			<xsl:apply-templates select="lastFlowAction" />
		</xsl:if>
	
		<xsl:apply-templates select="validationError"/>

		<section class="service has-navigator testing">
			
			<xsl:call-template name="createFlowInstanceManagerFormHeader">
				<xsl:with-param name="showSaveButton">false</xsl:with-param>
			</xsl:call-template>
			
			<xsl:for-each select="ManagerResponses/ManagerResponse">
				
				<xsl:variable name="currentStepID" select="currentStepID" />
				<xsl:variable name="currentStepIndex" select="currentStepIndex" />
				
				<div class="service-navigator-wrap">
					<div>
						<ul class="service-navigator primary full">
							<xsl:for-each select="../../FlowInstance/Flow/Steps/Step">
							
								<li>
									<xsl:choose>
										<xsl:when test="$currentStepID = stepID"><xsl:attribute name="class">active</xsl:attribute></xsl:when>
										<xsl:when test="$currentStepIndex >= sortIndex">
											<xsl:attribute name="class">
												<xsl:text>completed</xsl:text>
											</xsl:attribute>
										</xsl:when>
									</xsl:choose>
									
									<span data-step="{position()}">
										<xsl:value-of select="name"/>
									</span>
								</li>
								
							</xsl:for-each>
						</ul>
					</div>
				</div>
				
				<div class="section-full">
					
					<div class="queries">
						
						<xsl:apply-templates select="QueryResponses/QueryResponse"/>
						
						<xsl:if test="not(QueryResponses/QueryResponse/HTML)">
							<p><xsl:value-of select="$i18n.NoQueriesInCurrentStep" /></p>
						</xsl:if>
						
					</div>
				
				</div>
				
			</xsl:for-each>
			
			<div id="ajaxLoadingMessage" style="display: none">
				<h1><xsl:value-of select="$i18n.AjaxLoading" />..</h1><p class="tiny"><a href="javascript:void(0)" onclick="cancelAjaxPost()"><xsl:value-of select="$i18n.AjaxCancel" />.</a></p>
			</div>
			
			<div id="submitLoadingMessage" style="display: none">
				<h1><xsl:value-of select="$i18n.SubmitLoading" />..</h1>
			</div>
			
			<div id="ajaxErrorMessage" style="display: none">
				<h1><xsl:value-of select="$i18n.UnExpectedAjaxError" /></h1>
				<p><xsl:value-of select="$i18n.UnExpectedAjaxErrorDescription" /></p>
				<input id="AjaxRetryButton" type="button" value="{$i18n.AjaxRetry}" onclick="retryAjaxPost()" class="marginright btn btn-blue" /> 
				<input id="AjaxReloadButton" type="button" value="{$i18n.AjaxReload}" onclick="reloadCurrentStep()" class="btn btn-blue" />
			</div>
			
		</section>
		
	</xsl:template>
	
	<xsl:template match="FlowUserAdminExtension">
	
		<xsl:if test="Flows">
		
			<div class="full floatleft bigmargintop">
	
				<h2><xsl:value-of select="$i18n.Flowslist.title" /></h2>
				
				<div>
				
					<xsl:for-each select="Flows/Flow">
				
						<div class="floatleft full border marginbottom">
						
							<div class="floatleft">
								<img class="alignbottom marginright" src="{../../FlowAdminURL}/icon/{flowID}?{IconLastModified}" width="20px"  />
								<a href="{../../FlowAdminURL}/showflow/{flowID}" title="{name}"><xsl:value-of select="name" /></a>
								<xsl:if test="access = 'RESTRICTED'">
									<xsl:text> (</xsl:text>
									<span><xsl:value-of select="$i18n.Manager.restricted" /></span>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</div>
						
						</div>
					
					</xsl:for-each>
				
				</div>
			</div>
					
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="ReplaceFlowStatuses">
	
		<h1>
			<xsl:value-of select="$i18n.ReplaceFlowStatusesWithStandard.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>

		<xsl:apply-templates select="ValidationErrors/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full bigmarginbottom">
				
				<label for="statusGroupID" class="floatleft full required">
					<xsl:value-of select="$i18n.AddStandardStatuses.StandardStatusGroup" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'statusGroupID'"/>
						<xsl:with-param name="name" select="'statusGroupID'"/>
						<xsl:with-param name="valueElementName" select="'statusGroupID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="StandardStatusGroups/StandardStatusGroup" />
						<xsl:with-param name="addEmptyOption" select="$i18n.AddStandardStatuses.StandardStatusGroup.choose" />
					</xsl:call-template>
				</div>
				
			</div>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateFlowIcon.submit}" />
			</div>
		
		</form>
	
	</xsl:template>

	<xsl:template match="validationError[messageKey='NoManagersSet']">
	
		<p class="error">
			<xsl:value-of select="$i18n.NoManagersSet" />
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='EvaluatorImportException']">
	
		<p class="error">
			<xsl:value-of select="$i18n.EvaluatorImportException.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="EvaluatorDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.EvaluatorImportException.Part2" />
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='QueryImportException']">
	
		<p class="error">
			<xsl:value-of select="$i18n.QueryImportException.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="QueryDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.QueryImportException.Part2" />
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='QueryTypeNotAllowedInFlowTypeValidationError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="QueryDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part2" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="FlowType/name" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part3" />			
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='EvaluatorTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.EvaluatorTypeNotFound.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="EvaluatorDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.EvaluatorTypeNotFound.Part2" />
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='QueryTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.QueryTypeNotFound.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="QueryDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.QueryTypeNotFound.Part2" />
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='EvaluatorExportException']">
	
		<p class="error">
			<xsl:value-of select="$i18n.EvaluatorExportException.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="EvaluatorDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.EvaluatorExportException.Part2" />
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='QueryExportException']">
	
		<p class="error">
			<xsl:value-of select="$i18n.QueryExportException.Part1" />
			<xsl:text>&#160;"</xsl:text>
			<xsl:value-of select="QueryDescriptor/name" />
			<xsl:text>"&#160;</xsl:text>
			<xsl:value-of select="$i18n.QueryExportException.Part2" />
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='MissingDefaultStatusMapping']">
	
		<p class="error">
			<xsl:value-of select="$i18n.MissingDefaultStatusMapping"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='MissingDefaultStatusMappingForMultiSigning']">
	
		<p class="error">
			<xsl:value-of select="$i18n.MissingDefaultStatusMappingForMultiSigning"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='MissingDefaultStatusMappingForPayment']">
	
		<p class="error">
			<xsl:value-of select="$i18n.MissingDefaultStatusMappingForPayment"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='SelectedFlowTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.SelectedFlowTypeNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowTypeAccessDenied']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowTypeAccessDenied"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SelectedStepNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.SelectedStepNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SelectedQueryTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.SelectedQueryTypeNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToParseRequest']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToParseRequest"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToParseIcon']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToParseIcon"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidIconFileFormat']">
	
		<p class="error">
			<xsl:value-of select="$i18n.InvalidIconFileFormat"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToStoreFile']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToStoreFile"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidFlowFormFileFormat']">
	
		<p class="error">
			<xsl:value-of select="$i18n.InvalidFlowFormFileFormat.part1"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="displayName"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$i18n.InvalidFlowFormFileFormat.part2"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='NoAttachedFile']">
	
		<p class="error">
			<xsl:value-of select="$i18n.NoAttachedFile"/>
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
	
	<xsl:template match="validationError[messageKey='RequestSizeLimitExceeded']">
	
		<p class="error">
			<xsl:value-of select="$i18n.RequestSizeLimitExceeded.part1"/>
			<xsl:value-of select="actualSize"/>
			<xsl:value-of select="$i18n.RequestSizeLimitExceeded.part2"/>
			<xsl:value-of select="maxAllowedSize"/>
			<xsl:value-of select="$i18n.RequestSizeLimitExceeded.part3"/>
		</p>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='NoStepSortindex']">
	
		<p class="error">
			<xsl:value-of select="$i18n.NoStepSortindex"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='NoQueryDescriptorSortindex']">
	
		<p class="error">
			<xsl:value-of select="$i18n.NoQueryDescriptorSortindex"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToFindStepsForAllQueries']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToFindStepsForAllQueries"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SelectedEvaluatorTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.SelectedEvaluatorTypeNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ShowFailedFlowNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UpdateFailedFlowNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='DeleteFailedFlowNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='DeleteFailedFlowFormNotFound' or messageKey='UpdateFailedFlowFormNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowFormNotFound" />
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidQuerySortIndex']">
	
		<p class="error">
			<xsl:value-of select="$i18n.InvalidQuerySortIndex"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RequestedFlowFamilyNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.RequestedFlowFamilyNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowFamilyCannotBeDeleted']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowFamilyCannotBeDeleted"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='AddCategoryFailedFlowTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.AddCategoryFailedFlowTypeNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedCategoryNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedCategoryNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedCategoryNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedCategoryNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedFlowTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedFlowTypeNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedFlowTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedFlowTypeNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='ShowFailedFlowTypeNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ShowFailedFlowTypeNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedStepNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedStepNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedStepNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedStepNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedQueryDescriptorNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedQueryDescriptorNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedQueryDescriptorNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedQueryDescriptorNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='ExportFailedQueryDescriptorNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ExportFailedQueryDescriptorNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedEvaluatorDescriptorNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedEvaluatorDescriptorNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedEvaluatorDescriptorNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedEvaluatorDescriptorNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedStatusNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedStatusNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedStatusNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedStatusNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='UpdateFailedStandardStatusNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UpdateFailedStandardStatusNotFound"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DeleteFailedStandardStatusNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.DeleteFailedStandardStatusNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowTypeQueryTypeAccessDenied']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowTypeQueryTypeAccessDenied"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='CategoryNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.AddFlowCategoryNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowImportFlowFamlilyNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowImportFlowFamlilyNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='MayNotRemoveFlowFormIfNoSteps']">
		
		<p class="error">
			<xsl:value-of select="$i18n.MayNotRemoveFlowFormIfNoSteps"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='MayNotAddFlowFormIfOverviewSkipIsSet']">
		
		<p class="error">
			<xsl:value-of select="$i18n.MayNotAddFlowFormIfOverviewSkipIsSet"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='MayNotSetOverviewIfFlowFormIsSet']">
		
		<p class="error">
			<xsl:value-of select="$i18n.MayNotSetOverviewIfFlowFormIsSet"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowHasNoContent']">
		
		<p class="error">
			<xsl:value-of select="$i18n.FlowHasNoContent"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowNotAvailiableInRequestedFormat']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowNotAvailiableInRequestedFormat"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowHasNoStepsAndOverviewSkipIsSet']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowHasNoStepsAndOverviewSkipIsSet"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='OneOrMoreSelectedManagerUsersNotFoundError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.OneOrMoreSelectedManagerUsersNotFoundError"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='OneOrMoreSelectedManagerGroupsNotFoundError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.OneOrMoreSelectedManagerGroupsNotFoundError"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnauthorizedUserNotManager']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.UnauthorizedUserNotManager.1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/firstname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/lastname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.ValidationError.UnauthorizedUserNotManager.2" />!
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnauthorizedGroupNotManager']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.UnauthorizedGroupNotManager.1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="group/name" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.ValidationError.UnauthorizedGroupNotManager.2" />!
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ManagerUserInUseError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.InUseManagerUserError.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/firstname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/lastname" />
			<xsl:text>&#160;</xsl:text>
			
			<xsl:if test="user/groups">
			
				<xsl:text>(</xsl:text>
				<xsl:value-of select="$i18n.ValidationError.InUseManagerUserError.MemberOfGroups" />
				<xsl:text>&#160;</xsl:text>
				
				<xsl:for-each select="user/groups/group">
				
					<xsl:if test="position() != 1">
						<xsl:text>,&#160;</xsl:text>
					</xsl:if>
				
					<xsl:value-of select="name"/>
					
				</xsl:for-each>
				
				<xsl:text>)&#160;</xsl:text>
				
			</xsl:if>
			
			<xsl:value-of select="$i18n.ValidationError.InUseManagerUserError.Part2" />!
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ManagerGroupInUseError']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.InUseManagerGroupError.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="group/name" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.ValidationError.InUseManagerGroupError.Part2" />!
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FullManagerOrFallbackManagerRequired']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FullManagerOrFallbackManagerRequired" />
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowFamilyAliasCollision']">
			
		<p class="error">
			<xsl:value-of select="$i18n.FlowFamilyAliasAlreadyInUse"/>
			<xsl:value-of select="'&#160;'"/>
			<xsl:value-of select="alias"/>
			<xsl:value-of select="'&#160;'"/>
			<xsl:value-of select="$i18n.FlowFamilyAliasAlreadyInUse2"/>
			<xsl:value-of select="'&#160;'"/>
			<xsl:value-of select="collidingFlowFamilyName"/>
			<xsl:value-of select="'.'"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowFamilyAliasAlreadyInUseBySystem']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FlowFamilyAliasAlreadyInUse"/>
			<xsl:value-of select="'&#160;'"/>
			<xsl:value-of select="fieldName"/>
			<xsl:value-of select="'&#160;'"/>
			<xsl:value-of select="$i18n.FlowFamilyAliasAlreadyInUseBySystem"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ExtensionErrors']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ExtensionErrors"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='DeleteFailedMessageTemplateNotFound' or messageKey='UpdateFailedMessageTemplateNotFound']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.MessageTemplateNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='DuplicateStatusRule']">
		<p class="error">
					
			<xsl:variable name="id" select="substring(fieldName, 37)" />
			
			<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="$i18n.ValidationError.DuplicateStatusRule"/>
						
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='NoActionsSelected']">
		<p class="error">
			
			<xsl:variable name="id" select="substring(fieldName, 26)" />
			
			<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="$i18n.ValidationError.NoActionsSelected"/>
			
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='DuplicateOverviewAttributeNames']">
		<p class="error">
			<xsl:value-of select="$i18n.DuplicateOverviewAttributeNames.part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="displayName" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.DuplicateOverviewAttributeNames.part2" />
		</p>
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
					<xsl:when test="fieldName = 'flowTypeID'">
						<xsl:value-of select="$i18n.flowType"/>
					</xsl:when>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'comment'">
						<xsl:value-of select="$i18n.comment"/>
					</xsl:when>
					<xsl:when test="fieldName = 'shortDescription'">
						<xsl:value-of select="$i18n.shortDescription"/>
					</xsl:when>
					<xsl:when test="fieldName = 'longDescription'">
						<xsl:value-of select="$i18n.longDescription"/>
					</xsl:when>
					<xsl:when test="fieldName = 'submittedMessage'">
						<xsl:value-of select="$i18n.submittedMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'publishDate'">
						<xsl:value-of select="$i18n.publishDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'unPublishDate'">
						<xsl:value-of select="$i18n.unPublishDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'longDescription'">
						<xsl:value-of select="$i18n.longDescription"/>
					</xsl:when>
					<xsl:when test="fieldName = 'stepID'">
						<xsl:value-of select="$i18n.step"/>
					</xsl:when>
					<xsl:when test="fieldName = 'queryTypeID'">
						<xsl:value-of select="$i18n.queryType"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contentType'">
						<xsl:value-of select="$i18n.contentType"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contentType'">
						<xsl:value-of select="$i18n.contentType"/>
					</xsl:when>
					<xsl:when test="fieldName = 'defaultExternalMessageTemplate'">
						<xsl:value-of select="$i18n.defaultExternalMessageTemplate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'defaultInternalMessageTemplate'">
						<xsl:value-of select="$i18n.defaultInternalMessageTemplate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'defaultQueryState'">
						<xsl:value-of select="$i18n.defaultQueryState"/>
					</xsl:when>
					<xsl:when test="fieldName = 'evaluatorTypeID'">
						<xsl:value-of select="$i18n.evaluatorTypeID"/>
					</xsl:when>
					<xsl:when test="fieldName = 'tags'">
						<xsl:value-of select="$i18n.tags"/>
					</xsl:when>
					<xsl:when test="fieldName = 'checks'">
						<xsl:value-of select="$i18n.checks"/>
					</xsl:when>	
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$i18n.description"/>
					</xsl:when>
					<xsl:when test="fieldName = 'managingTime'">
						<xsl:value-of select="$i18n.managingTime"/>
					</xsl:when>
					<xsl:when test="fieldName = 'newExternalMessagesAllowedDays'">
						<xsl:value-of select="$i18n.newExternalMessagesAllowedDays"/>
					</xsl:when>
					<xsl:when test="fieldName = 'group'">
						<xsl:value-of select="$i18n.allowedGroups"/>
					</xsl:when>
					<xsl:when test="fieldName = 'user' or fieldName = 'manager'">
						<xsl:value-of select="$i18n.allowedUsers"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalLink'">
						<xsl:value-of select="$i18n.externalLink"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalURL'">
						<xsl:value-of select="$i18n.FlowForm.externalURL"/>
					</xsl:when>
					<xsl:when test="fieldName = 'downloadFormButtonText'">
						<xsl:value-of select="$i18n.FlowForm.downloadFormButtonText"/>
					</xsl:when>
					<xsl:when test="fieldName = 'userDescriptionTemplate'">
						<xsl:value-of select="$i18n.userDescriptionTemplate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'managerDescriptionTemplate'">
						<xsl:value-of select="$i18n.managerDescriptionTemplate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'iconColor'">
						<xsl:value-of select="$i18n.FlowType.iconColor"/>
					</xsl:when>
					<xsl:when test="fieldName = 'loginQuestionText'">
						<xsl:value-of select="$i18n.Flow.showLoginQuestion"/>
					</xsl:when>
					<xsl:when test="fieldName = 'statusGroupID'">
						<xsl:value-of select="$i18n.AddStandardStatuses.StandardStatusGroup"/>
					</xsl:when>
					
					<xsl:when test="fieldName = 'event-message'">
						<xsl:value-of select="$i18n.Events.message"/>
					</xsl:when>
					
					<!-- FlowFamily -->
					<xsl:when test="fieldName = 'loginHelpLinkName'">
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp"/>
						<xsl:text>: </xsl:text>
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp.Name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'loginHelpLinkURL'">
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp"/>
						<xsl:text>: </xsl:text>
						<xsl:value-of select="$i18n.FlowFamily.LoginHelp.URL"/>
					</xsl:when>
					<xsl:when test="fieldName = 'alias'">
						<xsl:value-of select="$i18n.aliases"/>
					</xsl:when>
					<xsl:when test="fieldName = 'popularityBoost'">
						<xsl:value-of select="$i18n.FlowFamily.popularity.boost"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startButtonText'">
						<xsl:value-of select="$i18n.FlowFamily.startButtonText"/>
					</xsl:when>
					<xsl:when test="fieldName = 'ownerName'">
						<xsl:value-of select="$i18n.owner.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.owner.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'ownerEmail'">
						<xsl:value-of select="$i18n.owner.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.owner.email"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contactName'">
						<xsl:value-of select="$i18n.contact.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.contact.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contactEmail'">
						<xsl:value-of select="$i18n.contact.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.contact.email"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contactPhone'">
						<xsl:value-of select="$i18n.contact.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.contact.phone"/>
					</xsl:when>
					<xsl:when test="fieldName = 'contactWebAddress'">
						<xsl:value-of select="$i18n.contact.title"/><xsl:text>,&#160;</xsl:text><xsl:value-of select="$i18n.contact.webaddress"/>
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'overviewAttributeName_')">
						<xsl:variable name="id" select="substring(fieldName, 23)" />
						<xsl:variable name="sortIndex" select="../../requestparameters/parameter[name = concat('overviewAttributeSortIndex_', $id)]/value" />
						
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + $sortIndex" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.name" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'overviewAttributeValue_')">
						<xsl:variable name="id" select="substring(fieldName, 24)" />
						<xsl:variable name="sortIndex" select="../../requestparameters/parameter[name = concat('overviewAttributeSortIndex_', $id)]/value" />
						
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + $sortIndex" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.FlowFamily.OverviewAttributes.value" />
						
					</xsl:when>
					
					<!-- Managers -->
					<xsl:when test="starts-with(fieldName, 'manager-validFromDate')">
						<xsl:variable name="id" select="substring(fieldName, 22)" />
						
						<xsl:value-of select="../../requestparameters/parameter[name = concat('manager-name', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.UpdateManagers.Modal.validFromDate" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'manager-validToDate')">
						<xsl:variable name="id" select="substring(fieldName, 20)" />
						
						<xsl:value-of select="../../requestparameters/parameter[name = concat('manager-name', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.UpdateManagers.Modal.validToDate" />
						
					</xsl:when>
					
					<!-- Manager groups -->
					<xsl:when test="starts-with(fieldName, 'manager-group-notificationEmailAddresses')">
						<xsl:variable name="id" select="substring(fieldName, 41)" />

						<xsl:value-of select="../../requestparameters/parameter[name = concat('manager-group-name', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.UpdateManagers.Modal.notificationEmailAddresses"/>
					</xsl:when>
					
					<!-- Auto manager assignment -->
					<xsl:when test="starts-with(fieldName, 'auto-manager-rule-attribute-')">
						<xsl:variable name="id" select="substring(fieldName, 29)" />
						
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.Submit" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentRules/AutoManagerAssignmentRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeName" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-rule-values-')">
						<xsl:variable name="id" select="substring(fieldName, 26)" />
						
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.Submit" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentRules/AutoManagerAssignmentRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>,&#160;</xsl:text>
						<xsl:value-of select="../../requestparameters/parameter[name = concat('auto-manager-rule-attribute-', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rule.AttributeValues" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-rule-users-')">
						<xsl:variable name="id" select="substring(fieldName, 25)" />
							
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.Submit" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentRules/AutoManagerAssignmentRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>,&#160;</xsl:text>
						<xsl:value-of select="../../requestparameters/parameter[name = concat('auto-manager-rule-attribute-', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Users" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-rule-groups-')">
						<xsl:variable name="id" select="substring(fieldName, 26)" />
						
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.Submit" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentRules/AutoManagerAssignmentRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>,&#160;</xsl:text>
						<xsl:value-of select="../../requestparameters/parameter[name = concat('auto-manager-rule-attribute-', $id)]/value" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Groups" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-status-rule-statusName-')">
						<xsl:variable name="id" select="substring(fieldName, 37)" />
			
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRules.StatusName" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-status-rule-emailRecipients-')">
						<xsl:variable name="id" select="substring(fieldName, 42)" />
			
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.EmailRecipients" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-status-rule-statusAttributeName-')">
						<xsl:variable name="id" select="substring(fieldName, 46)" />
						
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.StatusAttributeName" />
						
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'auto-manager-status-rule-statusAttributeValues-')">
						<xsl:variable name="id" select="substring(fieldName, 48)" />
						
						<xsl:value-of select="$i18n.AutoManagerAssignment.On.StatusChange" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.Rules.Row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="1 + count(../../FlowFamily/AutoManagerAssignmentStatusRules/AutoManagerAssignmentStatusRule[generatedRuleID = $id]/preceding-sibling::*)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.AutoManagerAssignment.StatusRule.AttributeValues" />
						
					</xsl:when>
					
					<!-- Message Templates -->
					<xsl:when test="fieldName = 'messageTemplateName'">
						<xsl:value-of select="$i18n.MessageTemplate.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'messageTemplateMessage'">
						<xsl:value-of select="$i18n.MessageTemplate.message"/>
					</xsl:when>
					<xsl:when test="fieldName = 'messageTemplateType'">
						<xsl:value-of select="$i18n.MessageTemplate.type"/>
					</xsl:when>
					
					<!-- ManagementInfo -->
					<xsl:when test="fieldName = 'processOwner'">
						<xsl:value-of select="$i18n.managementInfo.processOwner"/>
					</xsl:when>
					<xsl:when test="fieldName = 'flowResponsible'">
						<xsl:value-of select="$i18n.managementInfo.flowResponsible"/>
					</xsl:when>
					<xsl:when test="fieldName = 'informationResponsible'">
						<xsl:value-of select="$i18n.managementInfo.informationResponsible"/>
					</xsl:when>
					<xsl:when test="fieldName = 'status'">
						<xsl:value-of select="$i18n.managementInfo.status"/>
					</xsl:when>
					<xsl:when test="fieldName = 'organization'">
						<xsl:value-of select="$i18n.managementInfo.organization"/>
					</xsl:when>
					<xsl:when test="fieldName = 'lastReviewed'">
						<xsl:value-of select="$i18n.managementInfo.lastReviewed"/>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="$i18n.managementInfo.lastReviewed.Details"/>
						<xsl:text>)</xsl:text>
					</xsl:when>
					<xsl:when test="fieldName = 'aboutFlow'">
						<xsl:value-of select="$i18n.managementInfo.aboutFlow"/>
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