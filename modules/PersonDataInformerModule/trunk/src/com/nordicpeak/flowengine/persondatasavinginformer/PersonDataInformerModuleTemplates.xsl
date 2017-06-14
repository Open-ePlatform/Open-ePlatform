<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<!-- Programatical additional globalscripts for UpdateSettings -->
	<xsl:variable name="updateglobalscripts">
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/persondatainformermodule.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/persondatainformer.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<xsl:apply-templates select="FlowOverviewExtension" />
		<xsl:apply-templates select="ListFlows" />
		<xsl:apply-templates select="ShowSettings" />
		<xsl:apply-templates select="UpdateSettings" />
			
	</xsl:template>
	
	<xsl:template match="FlowOverviewExtension">
	
		<p>
			<strong>
				<xsl:value-of select="$i18n.SavedPersonData"/>
			</strong>
		</p>
		<ul>
			<xsl:apply-templates select="FlowFamilyInformerSetting/DataAlternatives/InformerDataAlternative" mode="show"/>
		</ul>
		
		<xsl:if test="FlowFamilyInformerSetting/reason">
			<p>
				<strong>
					<xsl:value-of select="$i18n.Reason"/>
				</strong>
				<br/>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="FlowFamilyInformerSetting/reason"/>
				</xsl:call-template>
			</p>
		</xsl:if>
		
		<p>
			<strong>
				<xsl:value-of select="$i18n.Reasons"/>
			</strong>
		</p>
		<ul>
			<xsl:apply-templates select="FlowFamilyInformerSetting/ReasonAlternatives/InformerReasonAlternative" mode="show"/>
		</ul>
		
		<p>
			<strong>
				<xsl:value-of select="$i18n.YearsSaved"/>
			</strong>
			<br/>
			<xsl:variable name="years" select="FlowFamilyInformerSetting/yearsSaved"/>
		
			<xsl:choose>
				<xsl:when test="$years">
					<xsl:value-of select="$years"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$i18n.years"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
				</xsl:otherwise>
			</xsl:choose>
		</p>
		
		<xsl:if test="FlowFamilyInformerSetting/extraInformation">
			<p>
				<strong>
					<xsl:value-of select="$i18n.ExtraInformation"/>
				</strong>
				<br/>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="FlowFamilyInformerSetting/extraInformation"/>
				</xsl:call-template>
			</p>
		</xsl:if>
		
		<xsl:choose>
			<xsl:when test="FlowFamilyInformerSetting/complaintDescription">
				<xsl:value-of select="FlowFamilyInformerSetting/complaintDescription" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="DefaultComplaintDescription" disable-output-escaping="yes"/>
			</xsl:otherwise>
		</xsl:choose>
				
	</xsl:template>
	
	<xsl:template match="ListFlows">
	
		<div id="PersonDataInformer" class="contentitem errands-wrapper">
	
			<div class="floatleft marginbottom">
			
				<h1><xsl:value-of select="$i18n.ListFlows.title" /></h1>
				
<!-- 				<p> -->
<!-- 					<xsl:value-of select="$i18n.ListFlows.description" /> -->
<!-- 				</p> -->
			
			</div>
			
			<div class="floatright marginbottom">
				<label for="name" class="marginright margintop"><xsl:value-of select="$i18n.DataFilter" />:</label>
				
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="id" select="'person-data-filter'"/>
					<xsl:with-param name="class" select="'bigmarginright'"/>
					<xsl:with-param name="labelElementName" select="'name'" />
					<xsl:with-param name="valueElementName" select="'alternativeID'" />
					<xsl:with-param name="element" select="DataAlternatives/InformerDataAlternative" />
					<xsl:with-param name="addEmptyOption" select="$i18n.Filter.ShowAll" />
				</xsl:call-template>
				
				<label for="name" class="bigmarginleft marginright margintop"><xsl:value-of select="$i18n.FlowFilter" />:</label>
				<input type="text" size="20" name="flow-filter-input" class="flow-filter-input" data-tableid="flowlist" />
			</div>
			
			<form method="post" action="{/Document/ModuleURI}/export">
			
				<a class="clearboth floatright bigmargin btn btn-green" onclick="exportPersonData(event, this);">
					<xsl:value-of select="$i18n.Export"/>
				</a>
				
				<table id="flowlist" class="clearboth full coloredtable oep-table" cellspacing="0">
					<thead class="sortable">	
						<tr>
							<th width="25" class="no-sort"></th>
							<th class="default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.Column.FlowName" /></span></th>
							<th><span data-icon-after="_"><xsl:value-of select="$i18n.Column.FlowType" /></span></th>
		
							<xsl:if test="/Document/UseCategories">
								<th><span data-icon-after="_"><xsl:value-of select="$i18n.Column.FlowCategory" /></span></th>
							</xsl:if>
							
							<th><span data-icon-after="_"><xsl:value-of select="$i18n.Column.PersonData" /></span></th>
							<th><span data-icon-after="_"><xsl:value-of select="$i18n.Reasons" /></span></th>
							<th><span data-icon-after="_"><xsl:value-of select="$i18n.YearsSaved" /></span></th>
						</tr>
					</thead>
					<tbody>
						<xsl:choose>
							<xsl:when test="not(Flows/Flow)">
								<tr>
									<td class="icon"></td>
									<td colspan="5">
										<xsl:value-of select="$i18n.noFlowsFound" />
									</td>
								</tr>					
							</xsl:when>
							<xsl:otherwise>
								
								<xsl:apply-templates select="Flows/Flow" mode="list"/>
								
							</xsl:otherwise>
						</xsl:choose>			
					</tbody>
				</table>
		
			</form>
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="Flow" mode="list">
		
		<tr>
			<td class="icon">
				<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" width="25" alt="" />
			</td>
			<td data-title="{$i18n.Column.FlowName}">
				<a href="{/Document/requestinfo/contextpath}{../../FlowBrowserFullAlias}/overview/{FlowFamily/flowFamilyID}">
				
					<xsl:if test="HasExternalVersions">
						<xsl:attribute name="data-icon-after">e</xsl:attribute>
					</xsl:if>
				
					<xsl:value-of select="name" />
				</a>
				
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="'flowFamilyID'"  />
					<xsl:with-param name="value" select="FlowFamily/flowFamilyID"  />
				</xsl:call-template>
				
			</td>
			<td data-title="{$i18n.Column.FlowType}">
				<a href="{/Document/requestinfo/contextpath}{../../FlowAdminFullAlias}/flowtype/{FlowType/flowTypeID}"><xsl:value-of select="FlowType/name" /></a>
			</td>
			
			<xsl:if test="/Document/UseCategories">
				<td data-title="{$i18n.Column.FlowCategory}">
					<xsl:choose>
						<xsl:when test="Category"><xsl:value-of select="Category/name" /></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<td class="persondata" data-title="{$i18n.Column.PersonData}">
				<ul>
					<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/DataAlternatives/InformerDataAlternative" mode="show"/>
				</ul>
			</td>
			<td data-title="{$i18n.Reasons}">
				<ul>
					<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/ReasonAlternatives/InformerReasonAlternative" mode="show"/>
				</ul>
			</td>
			<td data-title="{$i18n.YearsSaved}">
				<xsl:variable name="years" select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/yearsSaved"/>
			
				<xsl:choose>
					<xsl:when test="$years">
						<xsl:value-of select="$years"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$i18n.years"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShowSettings">
	
		<a name="informersettings" />
		
		<xsl:choose>
			<xsl:when test="FlowFamilyInformerSetting and FlowFamilyInformerSetting/usesPersonData = 'true'">
				<xsl:value-of select="$i18n.UsesPersonData" />				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.NoSettings" />
			</xsl:otherwise>
		</xsl:choose>
		
		<a href="{/Document/ModuleURI}/updateflowsettings/{Flow/flowID}" class="floatright">
			<xsl:value-of select="$i18n.UpdateSettings" />
			<img class="alignbottom" src="{/Document/StaticContentURL}/pics/pen.png" alt="" />
		</a>
		
		<xsl:if test="FlowFamilyInformerSetting">
			<a href="{/Document/ModuleURI}/deleteflowsettings/{Flow/flowID}" class="floatright clearboth" onclick="return confirm('{$i18n.DeleteSettings.Confirm}');">
				<xsl:value-of select="$i18n.DeleteSettings" />
				<img class="alignbottom" src="{/Document/StaticContentURL}/pics/delete.png" alt="" />
			</a>		
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="UpdateSettings">
		
		<div id="PersonDataInformer" class="contentitem errands-wrapper border-box">
		
			<h1>
				<xsl:value-of select="$i18n.UpdateSettingsTitle"/>
				<xsl:text>:&#x20;</xsl:text>
				<xsl:value-of select="Flow/name"/>
			</h1>
	
			<xsl:apply-templates select="ValidationErrors/validationError" mode="admin"/>
	
			<form method="post" action="{/Document/ModuleURI}/updateflowsettings/{Flow/flowID}">
			
				<div class="floatleft full bigmarginbottom">

					<div class="floatleft full">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'usesPersonData'" />
							<xsl:with-param name="name" select="'usesPersonData'" />
							<xsl:with-param name="value" select="'true'" />
							<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
						</xsl:call-template>
						
						<label for="usesPersonData">
							<xsl:value-of select="$i18n.IsPersonDataUsed" />
						</label>
					</div>
	
				</div>
				
				<script type="text/javascript">
				
					$(document).ready(function() {
					
						var checkbox = $("#usesPersonData");
						var settingsDiv = $("#settings");
						
						settingsDiv.toggle(checkbox[0].checked);
						
						checkbox.click(function(){
							settingsDiv.toggle(this.checked);
						});
					});
				
				</script>
				
				<div id="settings">
				
					<div class="floatleft full">
						<label class="floatleft full">
							<xsl:value-of select="$i18n.SavedPersonData" />
							<span class="required">*</span>
						</label>
						
						<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="form"/>
							
					</div>
					
					<div class="floatleft full">
						<label for="reason" class="floatleft full">
							<xsl:value-of select="$i18n.Reason" />
						</label>
						
						<div class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'reason'"/>
								<xsl:with-param name="name" select="'reason'"/>
								<xsl:with-param name="rows" select="5"/>
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />      
							</xsl:call-template>
						</div>
					</div>
					
					<div class="floatleft full">
						<label class="floatleft full">
							<xsl:value-of select="$i18n.Reasons" />
							<span class="required">*</span>
						</label>

						<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="form"/>
												
					</div>
					
					<div class="floatleft full">
						<label for="extraInformation" class="floatleft full">
							<xsl:value-of select="$i18n.ExtraInformation" />
						</label>
						
						<div class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'extraInformation'"/>
								<xsl:with-param name="name" select="'extraInformation'"/>
								<xsl:with-param name="rows" select="5"/>
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />      
							</xsl:call-template>
						</div>
					</div>
					
					<div class="floatleft full">
					
						<label class="floatleft full">
							<xsl:value-of select="$i18n.YearsSaved" />
							<span class="required">*</span>
						</label>
						
						<div class="alternative floatleft full">
						
							<xsl:call-template name="createRadio">
								<xsl:with-param name="id" select="'yearsSavedInfinite'" />
								<xsl:with-param name="name" select="'yearsSavedType'"/>
								<xsl:with-param name="value" select="'INFINITE'"/>
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting and not(FlowFamilyInformerSetting/yearsSaved)" />
							</xsl:call-template>
							
							<label for="yearsSavedInfinite">
								<xsl:value-of select="$i18n.YearsSaved.Infinite" />
							</label>
						
						</div>
						
						<div class="alternative floatleft full">
							
							<xsl:call-template name="createRadio">
								<xsl:with-param name="id" select="'yearsSavedFinite'" />
								<xsl:with-param name="name" select="'yearsSavedType'"/>
								<xsl:with-param name="value" select="'FINITE'"/>
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/yearsSaved >= 0" />
							</xsl:call-template>
							
							<label for="yearsSavedFinite">
								<xsl:value-of select="$i18n.YearsSaved.Finite" />
							</label>
						
						</div>
						
						<script type="text/javascript">
				
							$(document).ready(function() {
							
								var radio = $("#yearsSavedFinite");
								var div = $("#yearsSavedContainer");
								
								div.toggle(radio[0].checked);
								
								$("input[name = 'yearsSavedType']").click(function(){
									var checked = radio[0].checked;
									div.toggle(checked).find('input').prop('disabled', !checked);
								});
							});
						
						</script>
						
						<div id="yearsSavedContainer" class="floatleft full">
							<label for="yearsSaved" class="floatleft full">
								<xsl:value-of select="$i18n.YearsSaved.Finite.Years" />
								<span class="required">*</span>
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'yearsSaved'"/>
								<xsl:with-param name="name" select="'yearsSaved'"/>
								<xsl:with-param name="element" select="FlowFamilyInformerSetting"/>
							</xsl:call-template>
						</div>
						
					</div>
					
					<div class="floatleft full">
					
						<label for="complaintDescription" class="floatleft full">
							<xsl:value-of select="$i18n.ComplaintDescription" />
						</label>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideComplaintDescription'" />
								<xsl:with-param name="name" select="'overrideComplaintDescription'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/complaintDescription != ''" />
							</xsl:call-template>
							
							<label for="overrideComplaintDescription">
								<xsl:value-of select="$i18n.OverrideComplaintDescription" />
							</label>
						</div>
						
						<div id="complaintDescriptionContainer" class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'complaintDescription'"/>
								<xsl:with-param name="name" select="'complaintDescription'"/>
								<xsl:with-param name="class" select="'ckeditor'" />
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
								<xsl:with-param name="value" select="DefaultComplaintDescription" />
							</xsl:call-template>
						</div>
						
						<script type="text/javascript">
				
							$(document).ready(function() {
							
								var checkbox = $("#overrideComplaintDescription");
								var div = $("#complaintDescriptionContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						
						</script>
						
					</div>
					
					<xsl:call-template name="initializeFCKEditor">
						<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
						<xsl:with-param name="customConfig">config.js</xsl:with-param>
						<xsl:with-param name="editorContainerClass">ckeditor</xsl:with-param>
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
				
				</div>
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>	
	
	<xsl:template match="InformerDataAlternative" mode="form">

		<div class="alternative floatleft fifty">
		
			<xsl:variable name="alternativeID" select="alternativeID"/>
		
			<xsl:variable name="checkboxID">
				<xsl:value-of select="'data_alternative_'"/>
				<xsl:value-of select="alternativeID"/>		
			</xsl:variable>
		
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$checkboxID" />
				<xsl:with-param name="name" select="$checkboxID" />
				<xsl:with-param name="value" select="alternativeID" />
				<xsl:with-param name="elementName" select="'alternativeID'" />
				<xsl:with-param name="element" select="../../FlowFamilyInformerSetting/DataAlternatives/InformerDataAlternative[alternativeID = $alternativeID]" />
				<xsl:with-param name="requestparameters" select="../../requestparameters"/>
			</xsl:call-template>
			
			<label for="{$checkboxID}">
				<xsl:value-of select="name" />
			</label>
	
		</div>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="form">

		<div class="alternative floatleft full">
		
			<xsl:variable name="alternativeID" select="alternativeID"/>
		
			<xsl:variable name="checkboxID">
				<xsl:value-of select="'reason_alternative_'"/>
				<xsl:value-of select="alternativeID"/>		
			</xsl:variable>
		
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$checkboxID" />
				<xsl:with-param name="name" select="$checkboxID" />
				<xsl:with-param name="value" select="alternativeID" />
				<xsl:with-param name="elementName" select="'alternativeID'" />
				<xsl:with-param name="element" select="../../FlowFamilyInformerSetting/ReasonAlternatives/InformerReasonAlternative[alternativeID = $alternativeID]" />
				<xsl:with-param name="requestparameters" select="../../requestparameters"/>
			</xsl:call-template>
			
			<label for="{$checkboxID}">
				<xsl:value-of select="name" />
			</label>
	
		</div>
	
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="show">
	
		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="show">

		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>		
	
	<xsl:template match="validationError" mode="admin">
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
					<xsl:when test="fieldName = 'reason'">
						<xsl:value-of select="$i18n.Reason"/>
					</xsl:when>
					<xsl:when test="fieldName = 'extraInformation'">
						<xsl:value-of select="$i18n.ExtraInformation"/>
					</xsl:when>
					<xsl:when test="fieldName = 'yearsSaved'">
						<xsl:value-of select="$i18n.YearsSaved"/>
					</xsl:when>
					<xsl:when test="fieldName = 'dataAlternatives'">
						<xsl:value-of select="$i18n.SavedPersonData"/>
					</xsl:when>
					<xsl:when test="fieldName = 'reasonAlternatives'">
						<xsl:value-of select="$i18n.Reasons"/>
					</xsl:when>
					<xsl:when test="fieldName = 'complaintDescription'">
						<xsl:value-of select="$i18n.ComplaintDescription"/>
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