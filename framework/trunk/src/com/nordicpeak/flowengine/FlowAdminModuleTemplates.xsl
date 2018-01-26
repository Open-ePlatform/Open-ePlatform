<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js			
	</xsl:variable>
	
	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.ui.touch.min.js
		/js/jquery.blockui.js
		/js/jquery.expander.min.js
		/js/flowengine.helpdialog.js
		/js/flowengine.js
		/js/flowengine.step-navigator.js
		/js/flowadminmodule.js
		/js/jquery.tablesorter.min.js
		/js/jquery.ui.datepicker-sv.js
		/js/flowengine.tablesorter.js
		/js/flowengine.tablefilter.js
		/js/UserGroupList.js
		/js/colorpicker/jquery.minicolors.min.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css
		/css/UserGroupList.css
		/js/colorpicker/jquery.minicolors.css
	</xsl:variable>

	<xsl:template match="Document">
		
		<div class="contentitem errands-wrapper flowadmin">
			
			<xsl:if test="FlowInstanceManagerForm or FlowInstanceManagerPreview or FlowInstanceManagerSubmitted or ShowFlowOverview">
				<xsl:attribute name="class">contentitem</xsl:attribute>
				<xsl:attribute name="id">FlowBrowser</xsl:attribute>
			</xsl:if>
			
			<xsl:apply-templates select="ListFlows" />
			<xsl:apply-templates select="ShowFlow" />
			<xsl:apply-templates select="AddFlow" />
			<xsl:apply-templates select="UpdateFlow" />
			<xsl:apply-templates select="UpdateFlowIcon" />
			<xsl:apply-templates select="UpdateNotifications"/>		
			<xsl:apply-templates select="AddQueryDescriptor" />
			<xsl:apply-templates select="AddEvaluatorDescriptor" />
			<xsl:apply-templates select="AddStep" />
			<xsl:apply-templates select="UpdateStep" />
			<xsl:apply-templates select="AddStatus" />
			<xsl:apply-templates select="UpdateStatus" />
			<xsl:apply-templates select="SortFlow" />
			<xsl:apply-templates select="SortStatuses" />
			<xsl:apply-templates select="ListStandardStatuses" />
			<xsl:apply-templates select="AddStandardStatus" />
			<xsl:apply-templates select="UpdateStandardStatus" />
			<xsl:apply-templates select="FlowInstanceManagerForm"/>
			<xsl:apply-templates select="FlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerSubmitted"/>
			<xsl:apply-templates select="ListFlowTypes"/>
			<xsl:apply-templates select="ShowFlowType"/>
			<xsl:apply-templates select="AddFlowType"/>
			<xsl:apply-templates select="UpdateFlowType"/>
			<xsl:apply-templates select="AddCategory"/>
			<xsl:apply-templates select="UpdateCategory"/>
			<xsl:apply-templates select="UpdateFlowFamily"/>
			<xsl:apply-templates select="SigningForm"/>
			<xsl:apply-templates select="SelectImportTargetType"/>
			<xsl:apply-templates select="ImportFlow"/>
			<xsl:apply-templates select="ImportQueries"/>
			<xsl:apply-templates select="ShowFlowOverview"/>
			<xsl:apply-templates select="ShowFlowFamilyEvents"/>
			<xsl:apply-templates select="ChangeFlowType"/>
			<xsl:apply-templates select="AddFlowForm" />
			<xsl:apply-templates select="UpdateFlowForm" />	
		</div>
		
	</xsl:template>

	<xsl:template match="ListFlows">
	
		<div class="floatleft marginbottom">
		
			<h1><xsl:value-of select="$i18n.Flowslist.title" /></h1>
			
			<xsl:apply-templates select="validationError"/>
			
			<p>
				<xsl:value-of select="$i18n.Flowlist.description" />
			</p>
		
		</div>
		
		<div class="floatright marginbottom">
			<label for="name" class="marginright margintop"><xsl:value-of select="$i18n.statusFilter" />:</label>
			<select id="flow-status-filter" class="bigmarginright" style="width: 180px">
				<option value="all"><xsl:value-of select="$i18n.All" /></option>
				<option value="published"><xsl:value-of select="$i18n.Published" /></option>
				<option value="unpublished"><xsl:value-of select="$i18n.Unpublished" /></option>
			</select>
			<label for="flow-filter-input" class="bigmarginleft marginright margintop"><xsl:value-of select="$i18n.Filter" />:</label>
			<input type="text" size="20" id="flow-filter-input" class="flow-filter-input" data-tableid="flowlist" />
		</div>
		
		<table id="flowlist" class="clearboth full coloredtable oep-table" cellspacing="0">
			<thead class="sortable">	
				<tr>
					<th width="25" class="no-sort"></th>
					<th class="default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.flowName" /></span></th>
					<th><span data-icon-after="_"><xsl:value-of select="$i18n.flowType" /></span></th>

					<xsl:if test="/Document/UseCategories">
						<th><span data-icon-after="_"><xsl:value-of select="$i18n.flowCategory" /></span></th>
					</xsl:if>
					
					<th><span data-icon-after="_"><xsl:value-of select="$i18n.versions" /></span></th>
					<th width="100"><span data-icon-after="_"><xsl:value-of select="$i18n.SubmittedInstances" /></span></th>
					<th width="115"><span data-icon-after="_"><xsl:value-of select="$i18n.NotSubmittedInstances" /></span></th>
					<xsl:call-template name="ExtraFlowListColumnsHeader"></xsl:call-template>					
					<th width="16" class="no-sort"/>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="not(Flow)">
						<tr>
							<td class="icon"></td>
							<td colspan="8">
								<xsl:value-of select="$i18n.noFlowsFound" />
							</td>
						</tr>					
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="Flow[latestVersion = 'true']" mode="list"/>
						
					</xsl:otherwise>
				</xsl:choose>			
			</tbody>
		</table>		
		
		<xsl:if test="AddAccess">
			<br/>
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflow" title="{$i18n.addFlow}">
					<xsl:value-of select="$i18n.addFlow"/>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
				</a>			
			</div>
			<br/>
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importflow" title="{$i18n.importFlow}">
					<xsl:value-of select="$i18n.importFlow"/>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
				</a>			
			</div>			
		</xsl:if>
		
		<xsl:if test="AdminAccess">
			<br/>
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/standardstatuses" title="{$i18n.administrateStandardStatuses}">
					<xsl:value-of select="$i18n.administrateStandardStatuses"/>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>			
			</div>
			<br/>
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtypes" title="{$i18n.administrateFlowTypes}">
					<xsl:value-of select="$i18n.administrateFlowTypes"/>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_edit.png" alt=""/>
				</a>			
			</div>
		</xsl:if>		
		
		<xsl:variable name="extensionLinks" select="ExtensionLink[slot='bottom-right']"/>
					
		<xsl:if test="$extensionLinks">
		
			<div class="clearboth floatright marginright bottom-right-slot">
				<xsl:apply-templates select="$extensionLinks"/>
			</div>
		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="ExtensionLink">
	
		<div class="extensionlink floatright clearboth">
			
			<a href="{url}">

				<xsl:value-of select="name"/>
	
				<xsl:if test="icon">
					<xsl:text> </xsl:text>
					<img src="{icon}"/>
				</xsl:if>
			
			</a>
		</div>
	
	</xsl:template>
	
	<xsl:template match="Flow" mode="list">
		
		<tr>
			<xsl:attribute name="class">
				<xsl:if test="HasPublishedVersion">published</xsl:if>
			</xsl:attribute>
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" width="25" alt="" />
			</td>
			<td data-title="{$i18n.flowName}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}">
				
					<xsl:if test="HasExternalVersions">
						<xsl:attribute name="data-icon-after">e</xsl:attribute>
					</xsl:if>
				
					<xsl:value-of select="name" />
				</a>
				
			</td>
			<td data-title="{$i18n.flowType}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtype/{FlowType/flowTypeID}"><xsl:value-of select="FlowType/name" /></a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td data-title="{$i18n.flowCategory}">
					<xsl:choose>
						<xsl:when test="Category"><a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="Category/name" /></a></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<td data-title="{$i18n.versions}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="VersionCount" /></a>
			</td>			
			<td data-title="{$i18n.SubmittedInstances}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="SubmittedInstanceCount" /></a>
			</td>
			<td data-title="{$i18n.NotSubmittedInstances}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="InstanceCount - SubmittedInstanceCount" /></a>
			</td>
			
			<xsl:call-template name="ExtraFlowListColumns" />
			
			<td>
				<xsl:choose>
					<xsl:when test="InstanceCount > 0">

						<a href="#" onclick="alert('{$i18n.deleteFlowFamilyDisabledHasInstances}'); return false;" title="{$i18n.deleteFlowFamilyDisabledHasInstances}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:when test="HasPublishedVersion">

						<a href="#" onclick="alert('{$i18n.deleteFlowFamilyDisabledIsPublished}'); return false;" title="{$i18n.deleteFlowFamilyDisabledIsPublished}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:when>										
					<xsl:otherwise>

						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowfamily/{FlowFamily/flowFamilyID}" onclick="return confirm('{$i18n.deleteFlowFamilyConfirm}: {name}?');" title="{$i18n.deleteFlowFamily.title}: {name}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
						</a>

					</xsl:otherwise>					
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ExtensionLink" mode="top-right">
		<xsl:param name="flowID"/>
	
		<a class="extensionlink" href="{url}/{$flowID}" title="{name}">
			<img class="alignbottom" src="{icon}" alt=""/>
		</a>
	
	</xsl:template>
	
	<xsl:template match="ShowFlow">
	
		<xsl:variable name="disableStructureManipulation" select="Flow/flowInstanceCount > 0 or (Flow/published = 'true' and Flow/enabled = 'true')"/>
		
		<xsl:variable name="isInternal">
			<xsl:if test="not(Flow/externalLink)">true</xsl:if>
		</xsl:variable>
		
		<h1>
			<xsl:value-of select="Flow/name"/>
			<xsl:text>&#x20;(</xsl:text>
			<xsl:value-of select="$i18n.flowVersion"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Flow/version"/>
			<xsl:text>)</xsl:text>
		</h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<a name="baseinfo"/>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.baseInfo"/></legend>
	
			<div class="floatleft full bigmarginbottom adminicons">

					<div class="floatright">
					
						<xsl:if test="/Document/user/admin = 'true'">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/recacheflow/{Flow/flowID}" title="{$i18n.ReCacheFlow}">
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/reload.png" alt="" />
							</a>
						</xsl:if>
					
						<xsl:variable name="extensionLinks" select="ExtensionLink[slot='top-right']"/>
					
						<xsl:if test="$extensionLinks">
							<xsl:apply-templates select="$extensionLinks" mode="top-right">
								<xsl:with-param name="flowID" select="Flow/flowID"/>
							</xsl:apply-templates>
						</xsl:if>
					
						<xsl:if test="$isInternal = 'true'">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/generatexsd/{Flow/flowID}" title="{$i18n.downloadxsd.title}">
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/xsd.png" alt="" />
							</a>
						</xsl:if>
					
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/changeflowtype/{Flow/flowID}" title="{$i18n.ChangeFlowType.linkTitle}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_edit.png" alt="" />
						</a>
						
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateicon/{Flow/flowID}" title="{$i18n.updateFlowIcon.link.title}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/photo_edit.png" alt="" />
						</a>
					
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflow/{Flow/flowID}" title="{$i18n.updateFlowBaseInfo.title}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
						</a>
					
						<xsl:choose>
							<xsl:when test="$disableStructureManipulation = false()">
							
								<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflow/{Flow/flowID}" onclick="return confirm('{$i18n.deleteFlowConfirm}: {Flow/name}?');" title="{$i18n.deleteFlow.title}: {Flow/name}">
									<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
								</a>				
							
							</xsl:when>
							<xsl:otherwise>
							
								<xsl:choose>
									<xsl:when test="Flow/flowInstanceCount > 0">
				
										<a href="#" onclick="alert('{$i18n.deleteFlowDisabledHasInstances}'); return false;" title="{$i18n.deleteFlowDisabledHasInstances}">
											<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
										</a>
				
									</xsl:when>
									<xsl:when test="Flow/published = 'true'">
				
										<a href="#" onclick="alert('{$i18n.deleteFlowDisabledIsPublished}'); return false;" title="{$i18n.deleteFlowDisabledIsPublished}">
											<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
										</a>
				
									</xsl:when>						
								</xsl:choose>
							
							</xsl:otherwise>
						</xsl:choose>
					
					</div>
				
				<label class="floatleft">
					<xsl:value-of select="$i18n.name" />
				</label>
				
				<div class="floatleft clearboth">
					<xsl:value-of select="Flow/name" />				
				</div>
			</div>			
			
			<xsl:if test="Flow/externalLink">
				
				<div class="floatleft full bigmarginbottom">
				
					<label class="floatleft clearboth">
						<xsl:value-of select="$i18n.externalLink" />
					</label>
					
					<div class="floatleft clearboth">
						<a href="{Flow/externalLink}" target="_blank" title="{$i18n.OpenExternalFlow}" data-icon-after="e"><xsl:value-of select="Flow/externalLink" /></a>
					</div>				
				</div>
				
			</xsl:if>
			
			<div class="floatleft min-width-thirtytree bigmarginbottom">
				
				<label class="floatleft">
					<xsl:value-of select="$i18n.icon" />
				</label>
				
				<div class="floatleft clearboth">
					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{Flow/flowID}?{Flow/IconLastModified}" alt="" />							
				</div>
			</div>			
			
			<div class="floatleft min-width-thirtytree bigmarginbottom">
				
				<label class="floatleft full">
					<xsl:value-of select="$i18n.status" />
				</label>
				
				<div class="floatleft full">
						<xsl:choose>
						<xsl:when test="Flow/enabled = 'false'">
							<xsl:value-of select="$i18n.disabled" />
						</xsl:when>
						<xsl:when test="Flow/published = 'true'">
							<xsl:value-of select="$i18n.published" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.notPublished" />
						</xsl:otherwise>					
					</xsl:choose>			
				</div>
			</div>				
			
			<xsl:if test="Flow/publishDate">	
				<div class="floatleft min-width-thirtytree">
				
					<div class="floatleft fifty">
						
						<label class="floatleft">
							<xsl:value-of select="$i18n.publishDate" />
						</label>
						
						<div class="floatleft clearboth">
							<xsl:value-of select="Flow/publishDate" />
						</div>
					</div>
					
					<xsl:if test="Flow/unPublishDate">
						<div class="floatleft fifty">
							
							<label class="floatleft">
								<xsl:value-of select="$i18n.unPublishDate" />
							</label>
							
							<div class="floatleft clearboth">
								<xsl:value-of select="Flow/unPublishDate" />
							</div>
						</div>
					</xsl:if>
				</div>
			</xsl:if>
			
			<div class="full clearboth"/>
			
			<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'enabled'" />
						<xsl:with-param name="id" select="'enabled'" />
						<xsl:with-param name="element" select="Flow" />
						<xsl:with-param name="disabled" select="'true'" />
					</xsl:call-template>
					
					<label for="enabled">
						<xsl:value-of select="$i18n.enabled" />
					</label>
				</div>
			</div>
			
			<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'skipOverview'" />
						<xsl:with-param name="id" select="'skipOverview'" />
						<xsl:with-param name="element" select="Flow" />
						<xsl:with-param name="disabled" select="'true'" />
					</xsl:call-template>
					
					<label for="skipOverview">
						<xsl:value-of select="$i18n.skipOverview" />
					</label>
				</div>
			</div>
			
			<xsl:if test="$isInternal = 'true'">
			
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'usePreview'" />
							<xsl:with-param name="id" select="'usePreview'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="usePreview">
							<xsl:value-of select="$i18n.PreviewEnabled" />
						</label>
					</div>
				</div>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'paymentSupportEnabled'" />
							<xsl:with-param name="id" select="'paymentSupportEnabled'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="paymentSupportEnabled">
							<xsl:value-of select="$i18n.PaymentEnabled" />
						</label>
					</div>
				</div>				
				
			</xsl:if>
			
			<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
			
				<div class="floatleft">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'requireAuthentication'" />
						<xsl:with-param name="id" select="'requireAuthentication'" />
						<xsl:with-param name="element" select="Flow" />
						<xsl:with-param name="disabled" select="'true'" />
					</xsl:call-template>
					
					<label for="usePreview">
						<xsl:value-of select="$i18n.requirersAuthentication" />
					</label>
				</div>
			</div>
				
			<xsl:if test="$isInternal = 'true'">
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'requireSigning'" />
							<xsl:with-param name="id" select="'requireSigning'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="usePreview">
							<xsl:value-of select="$i18n.requiresSigning" />
						</label>
					</div>
				</div>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideManagerDetails'" />
							<xsl:with-param name="id" select="'hideManagerDetails'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="hideManagerDetails">
							<xsl:value-of select="$i18n.hideManagerDetails" />
						</label>
					</div>
				</div>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideFromOverview'" />
							<xsl:with-param name="id" select="'hideFromOverview'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="hideManagerDetails">
							<xsl:value-of select="$i18n.hideFromOverview" />
						</label>
					</div>
				</div>
				
				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideInternalMessages'" />
							<xsl:with-param name="id" select="'hideInternalMessages'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="hideInternalMessages">
							<xsl:value-of select="$i18n.hideInternalMessages" />
						</label>
					</div>
				</div>
				
				<xsl:if test="SubmitSurveyEnabled">
					<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
					
						<div class="floatleft">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="name" select="'showSubmitSurvey'" />
								<xsl:with-param name="id" select="'showSubmitSurvey'" />
								<xsl:with-param name="element" select="Flow" />
								<xsl:with-param name="disabled" select="'true'" />
							</xsl:call-template>
							
							<label for="showSubmitSurvey">
								<xsl:value-of select="$i18n.showSubmitSurvey" />
							</label>
						</div>
					</div>
				</xsl:if>

				<div class="floatleft min-width-thirtytree bigmarginbottom margintop">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'hideSubmitStepText'" />
							<xsl:with-param name="id" select="'hideSubmitStepText'" />
							<xsl:with-param name="element" select="Flow" />
							<xsl:with-param name="disabled" select="'true'" />
						</xsl:call-template>
						
						<label for="hideSubmitStepText">
							<xsl:value-of select="$i18n.hideSubmitStepText" />
						</label>
					</div>
				</div>

			
			</xsl:if>
			
			<div class="floatleft full bigmarginbottom">
				
				<label class="floatleft full">
					<xsl:value-of select="$i18n.shortDescription" />
				</label>
				
				<div class="floatleft full border">
					<div class="padding">
						<xsl:value-of select="Flow/shortDescription" disable-output-escaping="yes"/>
					</div>						
				</div>
			</div>		
			
			<xsl:if test="Flow/longDescription">
			
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.longDescription" />
					</label>
					
					<div class="floatleft full border">
						<div class="padding">
							<xsl:value-of select="Flow/longDescription" disable-output-escaping="yes"/>	
						</div>
					</div>
				</div>
			
			</xsl:if>
			
			<xsl:if test="$isInternal = 'true'">
			
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.submittedMessage" />
					</label>
					
					<div class="floatleft full border">
						<div class="padding">
							<xsl:value-of select="Flow/submittedMessage" disable-output-escaping="yes"/>
						</div>
					</div>
				</div>
			
			</xsl:if>

			<xsl:if test="Flow/Tags">
			
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.tags" />
					</label>
					
					<div class="floatleft full border">
						<div class="padding">
							<ul>
								<xsl:for-each select="Flow/Tags/tag">
									<li>
										<xsl:value-of select="."/>
									</li>
								</xsl:for-each>
							</ul>
						</div>
					</div>
				</div>
						
			</xsl:if>

			<xsl:if test="Flow/Checks">
			
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.checks" />
					</label>
					
					<div class="floatleft full border">
						<div class="padding">
							<ul>
								<xsl:for-each select="Flow/Checks/check">
									<li>
										<xsl:value-of select="."/>
									</li>
								</xsl:for-each>
							</ul>
						</div>
					</div>
				</div>
						
			</xsl:if>
			
			<xsl:if test="Flow/FlowFamily/Aliases">
			
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full">
						<xsl:value-of select="$i18n.aliases" />
					</label>
					
					<div class="floatleft full border">
						<div class="padding">
							<ul>
								<xsl:for-each select="Flow/FlowFamily/Aliases/Alias">
									<li>
										
										<xsl:variable name="url">
											<xsl:value-of select="'http://'"/>
											<xsl:value-of select="/Document/requestinfo/servername"/>
											<xsl:value-of select="/Document/requestinfo/contextpath"/>
											<xsl:value-of select="'/'"/>
											<xsl:value-of select="."/>
										</xsl:variable>
										
										<a href="{$url}">
											<xsl:value-of select="$url"/>
										</a>
									</li>
								</xsl:for-each>
							</ul>
						</div>
					</div>
				</div>
						
			</xsl:if>
			
			<xsl:if test="Flow/FlowFamily/contactName">
				
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full"><xsl:value-of select="$i18n.contact.title" /></label>
					
					<div class="floatleft full border">
					
						<div class="padding">
					
							<div class="floatleft">
						
								<label class="floatleft full nomargin">
									<xsl:value-of select="$i18n.contact.name" />
								</label>
								
								<div class="floatleft full">
									<xsl:value-of select="Flow/FlowFamily/contactName" />
								</div>
							
							</div>
							
							<xsl:if test="Flow/FlowFamily/contactEmail">
								<div class="floatleft">
							
									<label class="floatleft full nomargin">
										<xsl:value-of select="$i18n.contact.email" />
									</label>
									
									<div class="floatleft full">
										<xsl:value-of select="Flow/FlowFamily/contactEmail" />
									</div>
								
								</div>
							</xsl:if>
							
							<xsl:if test="Flow/FlowFamily/contactPhone">
								<div class="floatleft">
							
									<label class="floatleft full nomargin">
										<xsl:value-of select="$i18n.contact.phone" />
									</label>
									
									<div class="floatleft full">
										<xsl:value-of select="Flow/FlowFamily/contactPhone" />
									</div>
								
								</div>
							</xsl:if>
					
						</div>
					
					</div>
					
				</div>
				
			</xsl:if>
			
			<xsl:if test="Flow/FlowFamily/ownerName">
				
				<div class="floatleft full bigmarginbottom">
					
					<label class="floatleft full"><xsl:value-of select="$i18n.owner.title" /></label>
					
					<div class="floatleft full border">
					
						<div class="padding">
					
							<div class="floatleft">
						
								<label class="floatleft full nomargin">
									<xsl:value-of select="$i18n.owner.name" />
								</label>
								
								<div class="floatleft full">
									<xsl:value-of select="Flow/FlowFamily/ownerName" />
								</div>
							
							</div>
							
							<xsl:if test="Flow/FlowFamily/ownerEmail">
								<div class="floatleft">
							
									<label class="floatleft full nomargin">
										<xsl:value-of select="$i18n.owner.email" />
									</label>
									
									<div class="floatleft full">
										<xsl:value-of select="Flow/FlowFamily/ownerEmail" />
									</div>
								
								</div>
							</xsl:if>
							
						</div>
					
					</div>
					
				</div>
				
			</xsl:if>
			
		</fieldset>
		
		<xsl:if test="$isInternal = 'true'">
		
			<a name="steps"/>
		
			<fieldset>
				<legend><xsl:value-of select="$i18n.stepsAndQueries"/></legend>
				
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
						<ol>
							<xsl:apply-templates select="Flow/Steps/Step" mode="list">
								<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
							</xsl:apply-templates>
						</ol>
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
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
						</a>
					</div>
					
					<xsl:if test="Flow/Steps/Step">
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addquery/{Flow/flowID}" title="{$i18n.addQuery}">
								<xsl:value-of select="$i18n.addQuery"/>
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
							</a>
						</div>
						
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/importqueries/{Flow/flowID}" title="{$i18n.ImportQueries}">
								<xsl:value-of select="$i18n.ImportQueries"/>
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
							</a>
						</div>						
						
						<div class="floatright marginright clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortflow/{Flow/flowID}" title="{$i18n.sortStepsAndQueries}">
								<xsl:value-of select="$i18n.sortStepsAndQueries"/>
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/move.png" alt="" />
							</a>
						</div>
					</xsl:if>
			
				</xsl:if>
				
				<xsl:if test="Flow/Steps/Step">
					<br/>
					
					<div class="floatright marginright clearboth">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{Flow/flowID}" title="{$i18n.testFlow}" target="_blank">
							<xsl:value-of select="$i18n.testFlow"/>
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/play.png" alt="" />
						</a>
					</div>
				</xsl:if>
				
			</fieldset>
			
		</xsl:if>
			
		<a name="pdfform"/>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.FlowForm"/></legend>
			
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
					
						<xsl:value-of select="$i18n.MayNotAddFlowFormIfOverviewSkipIsSet"/>
					
					</xsl:when>
					<xsl:otherwise>
					
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addflowform/{Flow/flowID}" title="{$i18n.addFlowForm.link.title}">
							<xsl:value-of select="$i18n.addFlowForm.link.title"/>
							<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
						</a>
						
					</xsl:otherwise>
				</xsl:choose>
				
			</div>
			
			<xsl:if test="Flow/FlowForms/FlowForm">
			
				<div class="floatright marginright clearboth">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{Flow/flowID}" title="{$i18n.testFlow}" target="_blank">
						<xsl:value-of select="$i18n.testFlow"/>
						<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/play.png" alt="" />
					</a>
			</div>
			
			</xsl:if>
			
		</fieldset>
		
		<xsl:if test="$isInternal = 'true'">
		
			<a name="statuses"/>
		
			<fieldset>
				<legend><xsl:value-of select="$i18n.statuses"/></legend>
				
				<xsl:choose>
					<xsl:when test="Flow/Statuses/Status">
					
						<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
							<thead>	
								<tr>
									<th><xsl:value-of select="$i18n.name" /></th>
									<th width="100"><xsl:value-of select="$i18n.SubmittedInstances" /></th>
									<th width="115"><xsl:value-of select="$i18n.NotSubmittedInstances" /></th>
									<th width="32" />
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
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
					</a>
				</div>
				
				<xsl:if test="Flow/Statuses/Status">
					<div class="floatright marginright clearboth">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sortstatuses/{Flow/flowID}" title="{$i18n.sortStatuses}">
							<xsl:value-of select="$i18n.sortStatuses"/>
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/move.png" alt="" />
						</a>
					</div>				
				</xsl:if>				
			</fieldset>		
		
			<a name="managers"/>
		
			<fieldset>
				<legend><xsl:value-of select="$i18n.Managers"/></legend>
				
				<xsl:choose>
					<xsl:when test="AllowedGroups or AllowedUsers">
					
						<p class="nomargin"><xsl:value-of select="$i18n.ManagersDescription"/></p>
						
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
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
					</a>
				</div>				
				
			</fieldset>
	
			<xsl:if test="Notifications">
			
				<a name="notifications"/>
			
				<fieldset>
					<legend><xsl:value-of select="$i18n.Notifications"/></legend>
					
					<xsl:value-of select="Notifications/HTML" disable-output-escaping="yes"/>
					
					<div class="floatright marginright">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatenotifications/{Flow/flowID}" title="{$i18n.UpdateNotificationSettings}">
							<xsl:value-of select="$i18n.UpdateNotificationSettings"/>
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
						</a>
					</div>
					
				</fieldset>				
			
			</xsl:if>
	
			<xsl:if test="ShowFlowSurveysHTML">
				
				<fieldset>
					
					<legend><xsl:value-of select="$i18n.FlowSurveysTitle"/></legend>
					
					<xsl:value-of select="ShowFlowSurveysHTML" disable-output-escaping="yes"/>
					
				</fieldset>	
				
			</xsl:if>		
	
		</xsl:if>

		<xsl:apply-templates select="ExtensionProvider" />
	
		<a name="versions"/>	
	
		<fieldset>
			<legend><xsl:value-of select="$i18n.versions"/></legend>
			
			<p class="nomargin"><xsl:value-of select="$i18n.versions.description"/></p>
	
			<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/copyflow">
			
				<xsl:choose>
					<xsl:when test="FlowVersions">
					
						<table id="flowversionlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
							<thead>	
								<tr>
									<th width="10"></th>
									<th width="25"></th>
									<th><xsl:value-of select="$i18n.name" /></th>
									
									<xsl:if test="/Document/UseCategories">
										<th><xsl:value-of select="$i18n.flowCategory" /></th>
									</xsl:if>
									
									<th><xsl:value-of select="$i18n.version.title" /></th>
									
									<xsl:if test="$isInternal = 'true'">
										<th width="25"><xsl:value-of select="$i18n.steps" /></th>
										<th width="25"><xsl:value-of select="$i18n.queries" /></th>
										<th width="100"><xsl:value-of select="$i18n.SubmittedInstances" /></th>
										<th width="115"><xsl:value-of select="$i18n.NotSubmittedInstances" /></th>
									</xsl:if>
									
									<th width="75"><xsl:value-of select="$i18n.status" /></th>
									<th width="32" >
										<xsl:if test="PublishAccess">
											<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/unpublishflowfamily/{Flow/FlowFamily/flowFamilyID}/{Flow/flowID}" onclick="return confirm('{$i18n.UnpublishFlowFamilyConfirm}: {name}?');" title="{$i18n.UnpublishFlowFamily}">
												<img class="alignmiddle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/disabled.png" alt="" />
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
														
		</fieldset>
		
		<a name="events"/>	
	
		<fieldset>
			<legend><xsl:value-of select="$i18n.Events.Title"/></legend>
			
			<p class="nomargin"><xsl:value-of select="$i18n.Events.Description"/></p>
	
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
				
				<div class="floatright marginright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflowfamilyevents/{Flow/FlowFamily/flowFamilyID}/{Flow/flowID}" title="{$i18n.Events.ShowAll}">
						<xsl:value-of select="$i18n.Events.ShowAll"/>
					</a>
				</div>				
				
		</fieldset>
		
	</xsl:template>	
	
	<xsl:template match="ExtensionProvider">
		
		<fieldset>
					
			<legend><xsl:value-of select="Title"/></legend>
			
			<xsl:value-of select="HTML" disable-output-escaping="yes"/>
			
		</fieldset>
		
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
						<img class="alignmiddle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf.png" alt="" />
						<xsl:value-of select="$i18n.DownloadFlowForm" />
						<xsl:value-of select="'&#160;'" />
						<xsl:text>(</xsl:text>
						<xsl:value-of select="$i18n.link" />
						<xsl:text>)</xsl:text>
						<xsl:value-of select="$name"/>
					</a>

				</xsl:when>
				<xsl:otherwise>

					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getflowform/{../../flowID}/{flowFormID}?raw=t" target="_blank">
						<img class="alignmiddle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf.png" alt="" />
						<xsl:value-of select="$i18n.DownloadFlowForm" />
						
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
					<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
				
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowform/{flowFormID}" title="{$i18n.deleteFlowForm.link.title}: {$name}" onclick="return confirm('{$i18n.deleteFlowForm.confirm}: {$name}?');" >
				
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
					
					<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/{$img}" alt="" />
				</a>
			
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="Flow" mode="list-versions">
		
		<xsl:param name="isInternal" />
			
		<tr>
			<xsl:if test="published = 'false' or enabled = 'false'">
				<xsl:attribute name="class">disabled</xsl:attribute>
			</xsl:if>
			<td>
				<input type="radio" name="flowID" value="{flowID}" onclick="$('#add_new_version').show();$('#create_copy').show();"/>
			</td>			
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" width="25" alt="" />
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
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showflow/{flowID}"><xsl:value-of select="version" /></a>
			</td>	
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/xml.png" alt="" />
				</a>
			
				<xsl:choose>
					<xsl:when test="flowInstanceCount > 0">

						<a href="#" onclick="alert('{$i18n.deleteFlowDisabledHasInstances}'); return false;" title="{$i18n.deleteFlowDisabledHasInstances}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:when test="published = 'true' and enabled = 'true'">

						<a href="#" onclick="alert('{$i18n.deleteFlowDisabledIsPublished}'); return false;" title="{$i18n.deleteFlowDisabledIsPublished}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:when>										
					<xsl:otherwise>
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflow/{flowID}" onclick="return confirm('{$i18n.deleteFlowConfirm}: {name}?');" title="{$i18n.deleteFlow.title}: {name}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
						</a>

					</xsl:otherwise>					
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="Step" mode="list">
	
		<xsl:param name="disableStructureManipulation"/>
	
		<li>			
			<span class="bold"><xsl:value-of select="name"/></span>
		
			<span class="bigmarginleft">
				<xsl:if test="$disableStructureManipulation = false()">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestep/{stepID}" title="{$i18n.updateStep.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
					</a>

					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestep/{stepID}" onclick="return confirm('{$i18n.deleteStep.confirm.part1} {name} {$i18n.deleteStep.confirm.part2}?');" title="{$i18n.deleteStep.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
					</a>
				</xsl:if>
			</span>		
		
			<xsl:if test="QueryDescriptors/QueryDescriptor">
				<ol>
					<xsl:apply-templates select="QueryDescriptors/QueryDescriptor" mode="list">
						<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
					</xsl:apply-templates>				
				</ol>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="QueryDescriptor" mode="list">
	
		<xsl:param name="disableStructureManipulation"/>
	
		<li>	
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
			
			<span class="bigmarginleft">
			
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/exportquery/{queryID}" title="{$i18n.ExportQuery.title}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/download.png" alt="" />
				</a>			
			
				<xsl:if test="$disableStructureManipulation = false()">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addevaluator/{queryID}" title="{$i18n.addEvaluator.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/cog_add.png" alt="" />
					</a>				
				
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatequery/{queryID}" title="{$i18n.updateQuery.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
					</a>
					
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletequery/{queryID}" onclick="return confirm('{$i18n.deleteQuery.confirm}: {name}?');" title="{$i18n.deleteQuery.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
					</a>
				</xsl:if>			
			</span>
			
			<xsl:if test="EvaluatorDescriptors/EvaluatorDescriptor">
				<ul>
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
			<xsl:value-of select="name"/>
			
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
			
			<span class="bigmarginleft">
				<xsl:if test="$disableStructureManipulation = false()">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateevaluator/{evaluatorID}" title="{$i18n.updateEvaluator.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
					</a>
					
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteevaluator/{evaluatorID}" onclick="return confirm('{$i18n.deleteEvaluator.confirm}: {name}?');" title="{$i18n.deleteEvaluator.title}: {name}">
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
					</a>
				</xsl:if>			
			</span>
			
			<xsl:if test="EvaluatorDescriptors/EvaluatorDescriptor">
				<ol>
					<xsl:apply-templates select="EvaluatorDescriptors/EvaluatorDescriptor" mode="list">
						<xsl:with-param name="disableStructureManipulation" select="$disableStructureManipulation"/>
					</xsl:apply-templates>				
				</ol>
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
		
				<xsl:choose>
					<xsl:when test="flowInstanceCount > 0">

						<a href="#" onclick="alert('{$i18n.deleteStatusDisabledHasInstances}'); return false;" title="{$i18n.deleteStatusDisabledHasInstances}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:when>
					<xsl:otherwise>
					
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestatus/{statusID}" onclick="return confirm('{$i18n.deleteStatus.confirm}: {name}?');" title="{$i18n.deleteStatus.link.title}: {name}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
						</a>					
					
					</xsl:otherwise>					
				</xsl:choose>
			</td>			
		</tr>
	
	</xsl:template>
	
	<xsl:template match="FlowFamilyEvent" mode="list">
		
		<tr>
			<td>
				<xsl:value-of select="message" />
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

		<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflow/{Flow/flowID}">
		
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
		
			<div class="floatleft full bigmarginbottom external">
				
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
		</xsl:if>
		
		<xsl:if test="LacksPublishAccess">
			<div class="floatleft full">
				
				<label class="floatleft full">
					<xsl:value-of select="$i18n.LacksPublishAccess" />
				</label>
			</div>
		</xsl:if>
		
		<div class="floatleft full bigmarginbottom">
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
						<xsl:with-param name="name" select="'usePreview'" />
						<xsl:with-param name="id" select="'usePreview'" />
						<xsl:with-param name="element" select="Flow" />
					</xsl:call-template>
					
					<label for="usePreview">
						<xsl:value-of select="$i18n.usePreview" />
					</label>
				</div>
			</div>
		
			<div class="floatleft full bigmarginbottom margintop internal">
			
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
		
		</xsl:if>
			
		<div class="floatleft full bigmarginbottom margintop">
		
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
			
		<xsl:if test="$isInternal = 'true'">
			
			<div class="floatleft full bigmarginbottom margintop internal">
			
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
			
			<div class="floatleft full bigmarginbottom margintop internal">
			
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
			
			<div class="floatleft full bigmarginbottom margintop internal">
			
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
			
			<xsl:choose>
				<xsl:when test="SubmitSurveyEnabled">
					<div class="floatleft full bigmarginbottom margintop internal">
				
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
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'showSubmitSurvey'" />
						<xsl:with-param name="name" select="'showSubmitSurvey'" />
						<xsl:with-param name="element" select="Flow" /> 
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			
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
			
		</xsl:if>
			
		<xsl:if test="$isInternal = 'true'">	
			
			<!-- Disable if we are NOT in add mode -->
			<xsl:if test="/Document/AddFlow">
				<div class="floatleft full bigmarginbottom margintop internal">
				
					<div class="floatleft">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'addstandardstatuses'" />
							<xsl:with-param name="id" select="'addstandardstatuses'" />
							<xsl:with-param name="checked" select="'true'" />
						</xsl:call-template>
						
						<label for="addstandardstatuses">
							<xsl:value-of select="$i18n.addStandardStatuses" />
						</label>				
					</div>
				</div>			
			</xsl:if>		
		
		</xsl:if>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="shortDescription" class="floatleft full">
				<xsl:value-of select="$i18n.shortDescription" />
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
			
			<label for="longDescription" class="floatleft full">
				<xsl:value-of select="$i18n.longDescription" />
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
				
				<label for="submittedMessage" class="floatleft full">
					<xsl:value-of select="$i18n.submittedMessage" />
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
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="tags" class="floatleft full">
				<xsl:value-of select="$i18n.tags" />
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
		
		<div class="floatleft full bigmarginbottom">
			
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
				$(document).ready(function() {
					
					var checkbox = $("#useLoginHelpLink");
					
					var showHideLoginHelpLink = function() {
						
						var checked = checkbox.prop("checked");
						
						$("#loginhelplink-container").toggle(checked).find("input").attr("disabled", !checked);
					};
					
					checkbox.change(showHideLoginHelpLink);
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
				<xsl:value-of select="$i18n.popularity.boost" />
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
				<xsl:value-of select="$i18n.startButtonText" />
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
	
	<xsl:template match="AddQueryDescriptor">
	
		<h1><xsl:value-of select="$i18n.AddQueryDescriptor.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
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

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="statusForm"/>
			
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

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="statusForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStatus.submit}" />
			</div>
		
		</form>
	
	</xsl:template>	
	
	<xsl:template name="statusForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="Status" />          
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
					<xsl:with-param name="element" select="Status" />          
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
					<xsl:with-param name="element" select="Status" />          
				</xsl:call-template>
			</div>
		</div>
		
		<h2><xsl:value-of select="$i18n.permissions"/></h2>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isUserMutable'" />
					<xsl:with-param name="id" select="'isUserMutable'" />
					<xsl:with-param name="element" select="Status" />  					     
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
					<xsl:with-param name="element" select="Status" />  					     
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
					<xsl:with-param name="element" select="Status" />  					     
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
					<xsl:with-param name="element" select="Status" />  					     
				</xsl:call-template>
				
				<label for="isAdminDeletable">
					<xsl:value-of select="$i18n.isAdminDeletable" />
				</label>				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'requireSigning'" />
					<xsl:with-param name="id" select="'requireSigning'" />
					<xsl:with-param name="element" select="Status" />  					     
				</xsl:call-template>
				
				<label for="requireSigning">
					<xsl:value-of select="$i18n.Status.requireSigning" />
				</label>				
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'useAccessCheck'" />
					<xsl:with-param name="id" select="'useAccessCheck'" />
					<xsl:with-param name="element" select="Status" />  					     
				</xsl:call-template>
				
				<label for="useAccessCheck">
					<xsl:value-of select="$i18n.Status.useAccessCheck" />
				</label>				
			</div>
		</div>
		
		<div id="allowedManagers">
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
						<xsl:text>/managerusers/</xsl:text>
						<xsl:value-of select="Flow/flowID"/>
					</xsl:with-param>
					<xsl:with-param name="name" select="'user'"/>
					<xsl:with-param name="users" select="ManagerUsers" />
				</xsl:call-template>
			</div>
		</div>
	
		<h2><xsl:value-of select="$i18n.statusContentType.title"/></h2>
		
		<p><xsl:value-of select="$i18n.statusContentType.description"/></p>
		
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'new'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
					<xsl:with-param name="element" select="Status" />  
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
		
			<xsl:apply-templates select="FlowActions/FlowAction" mode="statusForm"/>
		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="FlowAction" mode="statusForm">
	
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
					<xsl:with-param name="element" select="../../Status/DefaultStatusMappings/DefaultStatusMapping" />
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
				
				<label for="name" class="floatleft full">
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
				<img class="vertical-align-middle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/move.png" title="{$i18n.MoveStep}" alt="" />
				<xsl:value-of select="name" />
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
				<img class="vertical-align-middle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/move.png" title="{$i18n.MoveQuery}" alt="" />
				<xsl:value-of select="name" />
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
	
		<div id="status_{statusID}" class="floatleft hover border ninety marginbottom lightbackground cursor-move border-radius">
			<div class="padding">
				<img class="vertical-align-middle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/move.png" title="{$i18n.MoveStatus}" alt="" />
				<xsl:value-of select="name" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="concat('sortorder_', statusID)" />
					<xsl:with-param name="class" select="'sortorder'" />
					<xsl:with-param name="value" select="sortIndex" />
				</xsl:call-template>
			</div>
		</div>	
	
	</xsl:template>	
	
	<xsl:template match="ListStandardStatuses">
	
		<h1><xsl:value-of select="$i18n.ListStandardStatuses.title" /></h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<p>
			<xsl:value-of select="$i18n.ListStandardStatuses.description" />
		</p>
		
			<xsl:choose>
				<xsl:when test="StandardStatuses/StandardStatus">
				
					<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
						<thead>	
							<tr>
								<th><xsl:value-of select="$i18n.name" /></th>
								<th width="32" />
							</tr>
						</thead>
						<tbody>
						
							<xsl:apply-templates select="StandardStatuses/StandardStatus" mode="list"/>
									
						</tbody>
					</table>				
				
				</xsl:when>
				<xsl:otherwise>
					<p><xsl:value-of select="$i18n.noStandardStatusesFound"/></p>
				</xsl:otherwise>
			</xsl:choose>
			
			<br/>
			
			<div class="floatright marginright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addstandardstatus/{Flow/flowID}" title="{$i18n.addStandardStatus}">
					<xsl:value-of select="$i18n.addStandardStatus"/>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
				</a>
			</div>	
		
	</xsl:template>	
	
	<xsl:template match="StandardStatus" mode="list">
	
		<tr>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardstatus/{statusID}" title="{$i18n.updateStandardStatus.link.title}: {name}">
					<xsl:value-of select="name"/>
				</a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardstatus/{statusID}" title="{$i18n.updateStandardStatus.link.title}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
		
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletestandardstatus/{statusID}" onclick="return confirm('{$i18n.deleteStandardStatus.confirm}: {name}?');" title="{$i18n.deleteStandardStatus.link.title}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
				</a>							
			</td>			
		</tr>
	
	</xsl:template>		
	
	<xsl:template match="AddStandardStatus">
	
		<h1><xsl:value-of select="$i18n.AddStandardStatus.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="standardStatusForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddStatus.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateStandardStatus">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateStandardStatus.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="StandardStatus/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="standardStatusForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateStatus.submit}" />
			</div>
		
		</form>
	
	</xsl:template>	
	
	<xsl:template name="standardStatusForm">
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="StandardStatus" />          
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
					<xsl:with-param name="element" select="Status" />          
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
					<xsl:with-param name="element" select="Status" />          
				</xsl:call-template>
			</div>
		</div>
		
		<h2><xsl:value-of select="$i18n.permissions"/></h2>
		
		<div class="floatleft full bigmarginbottom margintop">
		
			<div class="floatleft">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'isUserMutable'" />
					<xsl:with-param name="id" select="'isUserMutable'" />
					<xsl:with-param name="element" select="StandardStatus" />  					     
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
					<xsl:with-param name="element" select="StandardStatus" />  					     
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
					<xsl:with-param name="element" select="StandardStatus" />  					     
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
					<xsl:with-param name="element" select="StandardStatus" />  					     
				</xsl:call-template>
				
				<label for="isAdminDeletable">
					<xsl:value-of select="$i18n.isAdminDeletable" />
				</label>				
			</div>
		</div>
	
		<h2><xsl:value-of select="$i18n.statusContentType.title"/></h2>
		
		<p><xsl:value-of select="$i18n.statusContentType.description"/></p>
		
		<div class="floatleft full bigmarginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'new'"/>
					<xsl:with-param name="name" select="'contentType'"/>
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
					<xsl:with-param name="element" select="StandardStatus" />  
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
		
			<xsl:apply-templates select="FlowActions/FlowAction" mode="standardStatusForm"/>
		
		</xsl:if>
	
	</xsl:template>	
	
	<xsl:template match="FlowAction" mode="standardStatusForm">
	
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
					<xsl:with-param name="element" select="../../StandardStatus/DefaultStandardStatusMappings/DefaultStandardStatusMapping" />
					<xsl:with-param name="requestparameters" select="../../requestparameters"/>				     
				</xsl:call-template>
				
				<label for="{$id}">
					<xsl:value-of select="name" />
				</label>				
			</div>
		</div>		
	
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
					<th width="32" />
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
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
						<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
					</a>
					
					<xsl:choose>
						<xsl:when test="flowFamilyCount > 0">
	
							<a href="#" onclick="alert('{$i18n.deleteFlowTypeDisabledHasFlows}'); return false;" title="{$i18n.deleteFlowTypeDisabledHasFlows}">
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
							</a>
	
						</xsl:when>									
						<xsl:otherwise>
	
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowtype/{flowTypeID}" onclick="return confirm('{$i18n.deleteFlowType}: {name}?');" title="{$i18n.deleteFlowType}: {name}">
								<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
				
				<xsl:choose>
					<xsl:when test="flowFamilyCount > 0">
	
						<a href="#" onclick="alert('{$i18n.deleteFlowTypeDisabledHasFlows}'); return false;" title="{$i18n.deleteFlowTypeDisabledHasFlows}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>
	
					</xsl:when>									
					<xsl:otherwise>
	
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteflowtype/{FlowType/flowTypeID}" onclick="return confirm('{$i18n.deleteFlowType}: {FlowType/name}?');" title="{$i18n.deleteFlowType}: {FlowType/name}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
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
				<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/lock.png" alt="" />
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
				<img class="alignmiddle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/lock.png" alt="" />
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_add.png" alt="" />
				</a>			
			</div>
		</xsl:if>

	</xsl:template>
	
	<xsl:template match="user" mode="list">
		
		<div class="floatleft full marginbottom border">

			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/user.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/user_disabled.png" alt="" />
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
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/group.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/group_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
		</div>	
		
	</xsl:template>
	
	<xsl:template match="QueryTypeDescriptor" mode="list">
		
		<div class="floatleft full marginbottom border">

			<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/form.png" alt="" />
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
		</div>	
		
	</xsl:template>		
	
	<xsl:template match="Category" mode="list">
		
		<div class="floatleft full marginbottom border">

			<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder.png" alt="" />
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="name"/>
			
			<div class="floatright marginright">

				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatecategory/{categoryID}" title="{$i18n.updateCategory}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>

				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletecategory/{categoryID}" onclick="return confirm('{$i18n.deleteCategory}: {name}?');" title="{$i18n.deleteCategory}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
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
				<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/form.png" alt="" />
				
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
						<xsl:text>/users</xsl:text>
					</xsl:with-param>
					<xsl:with-param name="name" select="'user'"/>
					<xsl:with-param name="users" select="ManagerUsers" />
				</xsl:call-template>
			</div>
		
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateManagers.submit}" />
			</div>
		
		</form>		
		
		<!--
		
		This code is kept in case client side validation is to be implemented again
		 		
		<xsl:for-each select="FlowInstanceManagerUsers/user">
			
			<xsl:variable name="userID" select="userID" />
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('manager_user_', $userID)" />
				<xsl:with-param name="name" select="'manager_user'" />
				<xsl:with-param name="value" select="userID" />
			</xsl:call-template>
			
			<xsl:if test="groups/group">
				<xsl:for-each select="groups/group">
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="concat('group_manager_user_', $userID)" />
						<xsl:with-param name="name" select="'group_manager_user'" />
						<xsl:with-param name="value" select="groupID" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
			
		</xsl:for-each> 
		
		-->
		
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

	<xsl:template name="ExtraFlowListColumnsHeader" />

	<xsl:template name="ExtraFlowListColumns" />

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
			<xsl:value-of select="$i18n.InvalidFlowFormFileFormat"/>
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
			<xsl:value-of select="$i18n.OneOrMoreSelectedManagerUsersNotFoundError"/>
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
	
	<xsl:template match="validationError[messageKey='UnauthorizedManagerUserError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnauthorizedManagerUserError.Part1" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/firstname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="user/lastname" />
			<xsl:text>&#160;</xsl:text>
			
			<xsl:if test="user/groups">
			
				<xsl:text>(</xsl:text>
				<xsl:value-of select="$i18n.UnauthorizedManagerUserError.MemberOfGroups" />
				<xsl:text>&#160;</xsl:text>
				
				<xsl:for-each select="user/groups/group">
				
					<xsl:if test="position() != 1">
						<xsl:text>,&#160;</xsl:text>
					</xsl:if>
				
					<xsl:value-of select="name"/>
					
				</xsl:for-each>
				
				<xsl:text>)&#160;</xsl:text>
				
			</xsl:if>
			
			<xsl:value-of select="$i18n.UnauthorizedManagerUserError.Part2" />!
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
					<xsl:when test="fieldName = 'group'">
						<xsl:value-of select="$i18n.allowedGroups"/>
					</xsl:when>
					<xsl:when test="fieldName = 'user'">
						<xsl:value-of select="$i18n.allowedUsers"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalLink'">
						<xsl:value-of select="$i18n.externalLink"/>
					</xsl:when>
					<xsl:when test="fieldName = 'externalURL'">
						<xsl:value-of select="$i18n.FlowForm.externalURL"/>
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
					<xsl:when test="fieldName = 'alias'">
						<xsl:value-of select="$i18n.aliases"/>
					</xsl:when>
					<xsl:when test="fieldName = 'popularityBoost'">
						<xsl:value-of select="$i18n.popularity.boost"/>
					</xsl:when>
					<xsl:when test="fieldName = 'iconColor'">
						<xsl:value-of select="$i18n.FlowType.iconColor"/>
					</xsl:when>
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