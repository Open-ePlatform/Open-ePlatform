<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exslt="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<!-- Programatical additional globalscripts for UpdateSettings -->
	<xsl:variable name="updateglobalscripts">
		/jquery/jquery.js
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
	
	<xsl:variable name="storageOptionsVar">
		<option>
			<name><xsl:value-of select="$i18n.YearsSaved.Infinite"/></name>
			<value>INFINITY</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.YearsSaved.Years"/></name>
			<value>YEAR</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.YearsSaved.Months"/></name>
			<value>MONTH</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.YearsSaved.Custom"/></name>
			<value>CUSTOM</value>
		</option>
	</xsl:variable>
	
	<xsl:variable name="storageOptions" select="exslt:node-set($storageOptionsVar)/option" />

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
		
		<xsl:if test="FlowFamilyInformerSetting/reason != '' or DefaultReasonDescription != ''">
			<strong>
				<xsl:value-of select="$i18n.Reason"/>
			</strong>
			
			<br/>
		
			<xsl:choose>
				<xsl:when test="FlowFamilyInformerSetting/reason">
					<xsl:value-of select="FlowFamilyInformerSetting/reason" disable-output-escaping="yes"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="DefaultReasonDescription" disable-output-escaping="yes"/>
				</xsl:otherwise>
			</xsl:choose>
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
			<ul>
				<xsl:apply-templates select="FlowFamilyInformerSetting/StorageSettings" mode="list"/>
			</ul>
		</p>
		
		<xsl:if test="DefaultStorageDescription != '' or FlowFamilyInformerSetting/extraInformationStorage != ''">
			<strong>
				<xsl:value-of select="$i18n.ExtraInformationStorage"/>
			</strong>
			
			<br/>
			
			<xsl:choose>
				<xsl:when test="FlowFamilyInformerSetting/extraInformationStorage">
					<xsl:value-of select="FlowFamilyInformerSetting/extraInformationStorage" disable-output-escaping="yes"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="DefaultStorageDescription" disable-output-escaping="yes"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		
		<xsl:if test="FlowFamilyInformerSetting/dataRecipient != ''">
			<p>
				<strong><xsl:value-of select="$i18n.DataRecipient"/></strong>
				
				<br/>
				
				<xsl:value-of select="FlowFamilyInformerSetting/dataRecipient" disable-output-escaping="yes"/>
			</p>
		</xsl:if>
		
		<xsl:if test="DefaultExtraInformationDescription != '' or FlowFamilyInformerSetting/extraInformation != ''">
			<p>
				<strong><xsl:value-of select="$i18n.ExtraInformation"/></strong>
				
				<br/>
				
				<xsl:choose>
					<xsl:when test="FlowFamilyInformerSetting/extraInformation">
						<xsl:value-of select="FlowFamilyInformerSetting/extraInformation" disable-output-escaping="yes"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="DefaultExtraInformationDescription" disable-output-escaping="yes"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="DefaultComplaintDescription != '' or FlowFamilyInformerSetting/complaintDescription != ''">
			<strong><xsl:value-of select="$i18n.ComplaintTitle"/></strong>
			
			<br/>
			
			<xsl:choose>
				<xsl:when test="FlowFamilyInformerSetting/complaintDescription">
					<xsl:value-of select="FlowFamilyInformerSetting/complaintDescription" disable-output-escaping="yes"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="DefaultComplaintDescription" disable-output-escaping="yes"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
				
	</xsl:template>
	
	<xsl:template match="StorageSetting" mode="list">
	
		<li>
			<xsl:choose>
				<xsl:when test="storageType = 'INFINITY'">
					<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
				</xsl:when>
				<xsl:when test="storageType = 'CUSTOM'">
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="period"/>
					
					<xsl:text> </xsl:text>
					
					<xsl:choose>
						<xsl:when test="storageType = 'YEAR'">
							<xsl:value-of select="$i18n.YearsSaved.years"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.YearsSaved.months"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="description">
				<xsl:if test="not(storageType = 'CUSTOM')">
					<xsl:text> - </xsl:text>
				</xsl:if>
				
				<xsl:value-of select="description"/>
			</xsl:if>
		</li>
		
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
					<xsl:apply-templates select="FlowFamilyInformerSetting/DataAlternatives/InformerDataAlternative" mode="show"/>
				</ul>
			</td>
			<td data-title="{$i18n.Reasons}">
				<ul>
					<xsl:apply-templates select="FlowFamilyInformerSetting/ReasonAlternatives/InformerReasonAlternative" mode="show"/>
				</ul>
			</td>
			<td data-title="{$i18n.YearsSaved}">
				<ul>
					<xsl:apply-templates select="FlowFamilyInformerSetting/StorageSettings/StorageSetting" mode="list"/>
				</ul>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShowSettings">
	
		<a name="informersettings" />
		
		<xsl:choose>
			<xsl:when test="FlowFamilyInformerSetting">
				<xsl:value-of select="$i18n.UsesPersonData" />				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.NoSettings" />
			</xsl:otherwise>
		</xsl:choose>
		
		<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateflowsettings" class="floatright">
			<xsl:value-of select="$i18n.UpdateSettings" />
			<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/pen.png" alt="" />
		</a>
		
		<xsl:if test="FlowFamilyInformerSetting">
			<a href="{/Document/requestinfo/contextpath}{extensionRequestURL}/deleteflowsettings" class="floatright clearboth" onclick="return confirm('{$i18n.DeleteSettings.Confirm}');">
				<xsl:value-of select="$i18n.DeleteSettings" />
				<img class="marginleft vertical-align-bottom" src="{/Document/StaticContentURL}/pics/delete.png" alt="" />
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
	
			<form method="post" action="{/Document/requestinfo/contextpath}{extensionRequestURL}/updateflowsettings">
			
				<div class="floatleft full bigmarginbottom">

					<div class="floatleft full">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'usesPersonData'" />
							<xsl:with-param name="name" select="'usesPersonData'" />
							<xsl:with-param name="value" select="'true'" />
							<xsl:with-param name="checked">
								<xsl:if test="FlowFamilyInformerSetting">
									<xsl:text>true</xsl:text>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						
						<label for="usesPersonData">
							<xsl:value-of select="$i18n.IsPersonDataUsed" />
						</label>
					</div>
	
				</div>
				
				<script type="text/javascript">
					$(function() {
					
						var checkbox = $("#usesPersonData");
						var settingsDiv = $("#settings");
						
						settingsDiv.toggle(checkbox[0].checked);
						
						checkbox.click(function(){
							settingsDiv.toggle(this.checked);
						});
					});
				</script>
				
				<div id="settings">
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.SavedPersonData" />
							<span class="required">*</span>
						</legend>
						
						<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="form"/>
							
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend for="reason">
							<xsl:value-of select="$i18n.Reason" />
						</legend>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideReasonDescription'" />
								<xsl:with-param name="name" select="'overrideReasonDescription'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/reason != ''" />
							</xsl:call-template>
							
							<label for="overrideReasonDescription">
								<xsl:value-of select="$i18n.OverrideTextDescription" />
							</label>
						</div>
						
						<div id="reasonDescriptionContainer" class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'reason'"/>
								<xsl:with-param name="name" select="'reason'"/>
								<xsl:with-param name="class" select="'ckeditor'" />
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
								<xsl:with-param name="value" select="DefaultReasonDescription" />
							</xsl:call-template>
						</div>
						
						<script type="text/javascript">
							$(function() {
								var checkbox = $("#overrideReasonDescription");
								var div = $("#reasonDescriptionContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						</script>
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.Reasons" />
							<span class="required">*</span>
						</legend>

						<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="form"/>
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.YearsSaved" />
							<span class="required">*</span>
						</legend>
						
						<div class="floatleft full" id="storage-settings-wrapper">
							<script type="text/javascript">
					
								$(function() {
									var $storageSettings = $("#storage-settings-wrapper");
									
									var toggleDescriptions = (function() {
										var $visibleSettings = $("#storage-settings-wrapper .storage-setting").slice(1);
										var settingCount = $visibleSettings.length;
										
										if (settingCount === 1) {
											$visibleSettings.find(".storage-description").hide().find("input").prop("disabled, true");
										}
										else {
											$visibleSettings.find(".storage-description").show().find("input").prop("disabled", false);
										}
										
										$visibleSettings.filter(function(){
											var input = $(this).find(".storagetype-selector");
											return input.val() == "CUSTOM";
										}).find(".storage-description").show().find("input").prop("disabled", false);
									});
									
									toggleDescriptions();
									
									$storageSettings.on("change", ".storagetype-selector", function() {
										var $select = $(this);
										
										$select.parent().siblings(".storagetype-period").toggle($select.val() == "YEAR" || $select.val() == "MONTH");
										
										var $visibleSettings = $("#storage-settings-wrapper .storage-setting").slice(1);
										var settingCount = $visibleSettings.length;
										
										if (settingCount === 1) {
											var show = $select.val() == "CUSTOM";
											$select.parent().siblings(".storage-description").toggle(show).find("input").prop("disabled, !show");
										}
										
									}).on("click", ".remove-storage-setting", function(e) {
										e.preventDefault();
										
										if (confirm('<xsl:value-of select="$i18n.RemoveStorageSettingConfirm" />?')) {
											$(this).closest(".storage-setting").remove();
											toggleDescriptions();
										}
									});
									
									$(".storagetype-selector").change();
									
									var $storageCounter = $("#storageCounter");
									
									$("#addStorageSetting").click(function(e) {
										e.preventDefault();
										
										var $clone = $("#storage-settings-template").clone();
										
										var counter = Number($storageCounter.val()) + 1;
										
										$storageCounter.val(counter);
										
										$clone.find("input, select").prop("disabled", false).each(function() {
											var $this = $(this);
											
											$this.attr("name", $this.attr("name") + "-" + counter);
										});
										
										$storageSettings.append($clone.html());
										
										toggleDescriptions();
									});
								});
							
							</script>
							
							<div class="hidden" id="storage-settings-template">
								<div class="clearfix storage-setting">
									<div class="floatleft fifteen">
										<label class="floatleft full">
											<xsl:value-of select="$i18n.StorageType"/>
										</label>
										
										<xsl:call-template name="createDropdown">
											<xsl:with-param name="name" select="'storageType'"/>
											<xsl:with-param name="element" select="$storageOptions"/>
											<xsl:with-param name="disabled" select="true()"/>
											<xsl:with-param name="valueElementName" select="'value'" />
											<xsl:with-param name="labelElementName" select="'name'" />
											<xsl:with-param name="class" select="'floatleft bigmargintop full storagetype-selector'"/>
										</xsl:call-template>
									</div>
									
									<div class="floatleft bigmarginleft ten storagetype-period">
										<label class="floatleft full">
											<xsl:value-of select="$i18n.StoragePeriod"/>
										</label>
										
										<input disabled="disabled" type="text" name="storagePeriod" class="floatleft full"/>
									</div>
									
									<div class="floatleft forty bigmarginleft storage-description">
										<label class="floatleft full">
											<xsl:value-of select="$i18n.StorageDescription"/>
										</label>
										
										<input type="text" disabled="disabled" name="storageDescription" class="floatleft full"/>
									</div>
									
									<div class="floatleft bigmarginleft ten" style="padding-top: 48px">
										<a href="#" class="remove-storage-setting icon">
											<i data-icon-before="x"></i>
										</a>
									</div>
								</div>
							</div>
							
							<xsl:choose>
								<xsl:when test="requestparameters">
									<input type="hidden" value="{requestparameters/parameter[name='storageCounter']/value}" id="storageCounter" name="storageCounter"/>
									
									<xsl:call-template name="LoopStorageSettingTemplate">
										<xsl:with-param name="loopFrom" select="'1'"/>
										<xsl:with-param name="loopTo" select="requestparameters/parameter[name='storageCounter']/value"/>
										<xsl:with-param name="requestparameters" select="requestparameters"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="FlowFamilyInformerSetting/StorageSettings/StorageSetting">
									<input type="hidden" value="{count(FlowFamilyInformerSetting/StorageSettings/StorageSetting)}" id="storageCounter" name="storageCounter"/>
									
									<xsl:apply-templates select="FlowFamilyInformerSetting/StorageSettings/StorageSetting"/>
								</xsl:when>
								<xsl:otherwise>
									<input type="hidden" value="1" id="storageCounter" name="storageCounter"/>
									
									<xsl:call-template name="StorageSettingTemplate">
										<xsl:with-param name="counter" select="'1'"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</div>
						
						<div class="floatleft full text-align-right">
							<button class="btn btn-green display-inline-block" id="addStorageSetting"><xsl:value-of select="$i18n.AddStorageSetting"/></button>
						</div>
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.ExtraInformationStorage" />
						</legend>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideExtraInformationStorage'" />
								<xsl:with-param name="name" select="'overrideExtraInformationStorage'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/extraInformationStorage != ''" />
							</xsl:call-template>
							
							<label for="overrideExtraInformationStorage">
								<xsl:value-of select="$i18n.OverrideTextDescription" />
							</label>
						</div>
						
						<div id="overrideExtraInformationStorageContainer" class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'extraInformationStorage'"/>
								<xsl:with-param name="name" select="'extraInformationStorage'"/>
								<xsl:with-param name="class" select="'ckeditor'" />
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
								<xsl:with-param name="value" select="DefaultStorageDescription" />
							</xsl:call-template>
						</div>
						
						<script type="text/javascript">
				
							$(function() {
								var checkbox = $("#overrideExtraInformationStorage");
								var div = $("#overrideExtraInformationStorageContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						
						</script>
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.DataRecipient" />
						</legend>
						
						<div class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'dataRecipient'"/>
								<xsl:with-param name="name" select="'dataRecipient'"/>
								<xsl:with-param name="rows" select="5"/>
								<xsl:with-param name="class" select="'ckeditor'" />
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />      
							</xsl:call-template>
						</div>
					</fieldset>
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.ExtraInformation" />
						</legend>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideExtraInformation'" />
								<xsl:with-param name="name" select="'overrideExtraInformation'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/extraInformation != ''" />
							</xsl:call-template>
							
							<label for="overrideExtraInformation">
								<xsl:value-of select="$i18n.OverrideTextDescription" />
							</label>
						</div>
						
						<div id="extraInformationDescriptionContainer" class="floatleft full">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'extraInformation'"/>
								<xsl:with-param name="name" select="'extraInformation'"/>
								<xsl:with-param name="class" select="'ckeditor'" />
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
								<xsl:with-param name="value" select="DefaultExtraInformationDescription" />
							</xsl:call-template>
						</div>
						
						<script type="text/javascript">
				
							$(function() {
								var checkbox = $("#overrideExtraInformation");
								var div = $("#extraInformationDescriptionContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						
						</script>
					</fieldset>
					
					<fieldset class="floatleft full">
					
						<legend>
							<xsl:value-of select="$i18n.ComplaintDescription" />
						</legend>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideComplaintDescription'" />
								<xsl:with-param name="name" select="'overrideComplaintDescription'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/complaintDescription != ''" />
							</xsl:call-template>
							
							<label for="overrideComplaintDescription">
								<xsl:value-of select="$i18n.OverrideTextDescription" />
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
				
							$(function() {
								var checkbox = $("#overrideComplaintDescription");
								var div = $("#complaintDescriptionContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						
						</script>
						
					</fieldset>
					
					<fieldset class="floatleft full">
					
						<legend>
							<xsl:value-of select="$i18n.PersonDataInformerQuery" />
						</legend>
						
						<div class="floatleft full marginleft marginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'overrideConfirmationText'" />
								<xsl:with-param name="name" select="'overrideConfirmationText'" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="FlowFamilyInformerSetting/confirmationText != ''" />
							</xsl:call-template>
							
							<label for="overrideConfirmationText">
								<xsl:value-of select="$i18n.OverrideConfirmationText" />
							</label>
						</div>
						
						<div id="confirmationTextContainer" class="floatleft full">
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'confirmationText'"/>
								<xsl:with-param name="name" select="'confirmationText'"/>
								<xsl:with-param name="element" select="FlowFamilyInformerSetting" />
								<xsl:with-param name="value" select="DefaultConfirmationText" />
							</xsl:call-template>
						</div>
						
						<script type="text/javascript">
				
							$(function() {
								var checkbox = $("#overrideConfirmationText");
								var div = $("#confirmationTextContainer");
								
								div.toggle(checkbox[0].checked);
								
								checkbox.click(function(){
									var checked = checkbox[0].checked;
									div.toggle(checked).find('textarea').prop('disabled', !checked);
								});
							});
						
						</script>
						
					</fieldset>
					
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
					
					<fieldset class="floatleft full">
						<legend>
							<xsl:value-of select="$i18n.ContactOwnerTitle"/>
						</legend>
						
						<div class="floatleft forty">
							<label for="ownerName" class="floatleft full"><xsl:value-of select="$i18n.Name"/></label>
							
							<div class="floatleft full">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'ownerName'"/>
									<xsl:with-param name="name" select="'ownerName'"/>
									<xsl:with-param name="value" select="OwnerName" />
									<xsl:with-param name="size" select="'10'"/>
								</xsl:call-template>
							</div>
						</div>
						
						<div class="floatleft forty bigmarginleft">
							<label for="ownerEmail" class="floatleft full"><xsl:value-of select="$i18n.Email"/></label>
							
							<div class="floatleft full">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'ownerEmail'"/>
									<xsl:with-param name="name" select="'ownerEmail'"/>
									<xsl:with-param name="value" select="OwnerEmail" />
									<xsl:with-param name="size" select="'10'"/>
								</xsl:call-template>
							</div>
						</div>
					</fieldset>
				</div>
				
				<div class="floatright clearboth bigmargintop">
					<input type="submit" value="{$i18n.SaveChanges}" />
				</div>
			
			</form>
		
		</div>

	</xsl:template>
	
	<xsl:template name="LoopStorageSettingTemplate">
		
		<xsl:param name="loopFrom"/>
		<xsl:param name="loopTo"/>
		<xsl:param name="requestparameters"/>
		
		<xsl:variable name="typeName">
			<xsl:text>storageType-</xsl:text>
			<xsl:value-of select="$loopFrom"/>
		</xsl:variable>
		
		<xsl:if test="$requestparameters/parameter[name=$typeName]">
			<xsl:call-template name="StorageSettingTemplate">
				<xsl:with-param name="counter" select="$loopFrom"/>
				<xsl:with-param name="requestparameters" select="$requestparameters"/>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="$loopTo > $loopFrom">
			<xsl:call-template name="LoopStorageSettingTemplate">
				<xsl:with-param name="loopFrom" select="$loopFrom + 1"/>
				<xsl:with-param name="requestparameters" select="$requestparameters"/>
				<xsl:with-param name="loopTo" select="$loopTo"/>
			</xsl:call-template>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="StorageSetting">
		
		<xsl:call-template name="StorageSettingTemplate">
			<xsl:with-param name="counter" select="position()"/>
			<xsl:with-param name="element" select="."/>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="StorageSettingTemplate">
	
		<xsl:param name="counter"/>
		<xsl:param name="element" select="null"/>
		<xsl:param name="requestparameters" select="null"/>
		
		<xsl:variable name="descriptionName">
			<xsl:text>storageDescription-</xsl:text>
			<xsl:value-of select="$counter"/>
		</xsl:variable>
		
		<xsl:variable name="typeName">
			<xsl:text>storageType-</xsl:text>
			<xsl:value-of select="$counter"/>
		</xsl:variable>
		
		<xsl:variable name="periodName">
			<xsl:text>storagePeriod-</xsl:text>
			<xsl:value-of select="$counter"/>
		</xsl:variable>
		
		<div class="clearfix storage-setting">
			<div class="floatleft fifteen">
				<label for="{$typeName}" class="floatleft full">
					<xsl:value-of select="$i18n.StorageType"/>
				</label>
				
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="name" select="$typeName"/>
					<xsl:with-param name="element" select="$storageOptions"/>
					<xsl:with-param name="valueElementName" select="'value'" />
					<xsl:with-param name="labelElementName" select="'name'" />
					<xsl:with-param name="class" select="'floatleft bigmargintop full storagetype-selector'"/>
					<xsl:with-param name="selectedValue">
						<xsl:choose>
							<xsl:when test="$requestparameters">
								<xsl:value-of select="$requestparameters/parameter[name=$typeName]/value"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$element/storageType"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</div>
			
			<div class="floatleft bigmarginleft ten storagetype-period">
				<xsl:if test="($requestparameters/parameter[name=$typeName]/value != 'YEAR' and $requestparameters/parameter[name=$typeName]/value != 'MONTH') or (not($requestparameters) and ($element/storageType != 'YEAR' or $element/storageType != 'MONTH'))">
					<xsl:attribute name="class">
						<xsl:text>floatleft bigmarginleft ten storagetype-period hidden</xsl:text>
					</xsl:attribute>
				</xsl:if>
				
				<label for="{$periodName}" class="floatleft full">
					<xsl:value-of select="$i18n.StoragePeriod"/>
				</label>
				
				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="$periodName"/>
					<xsl:with-param name="id" select="$periodName"/>
					<xsl:with-param name="class" select="'floatleft full'"/>
					<xsl:with-param name="value">
						<xsl:choose>
							<xsl:when test="$requestparameters">
								<xsl:value-of select="$requestparameters/parameter[name=$periodName]/value"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$element/period"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="width" select="null"/>
				</xsl:call-template>
			</div>
			
			<div class="floatleft bigmarginleft forty storage-description">
				<label class="floatleft full">
					<xsl:value-of select="$i18n.StorageDescription"/>
				</label>
				
				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="$descriptionName"/>
					<xsl:with-param name="id" select="$descriptionName"/>
					<xsl:with-param name="class" select="'floatleft full'"/>
					<xsl:with-param name="value">
						<xsl:choose>
							<xsl:when test="$requestparameters">
								<xsl:value-of select="$requestparameters/parameter[name=$descriptionName]/value"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$element/description"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="width" select="null"/>
				</xsl:call-template>
			</div>
			
			<xsl:if test="$counter > 1">
				<div class="floatleft bigmarginleft ten" style="padding-top: 48px">
					<a href="#" class="remove-storage-setting icon">
						<i data-icon-before="x"></i>
					</a>
				</div>
			</xsl:if>
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
				<xsl:with-param name="checked">
					<xsl:if test="not(../../FlowFamilyInformerSetting) and autoSelect = 'true'">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
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
					<xsl:when test="fieldName = 'ownerName'">
						<xsl:value-of select="$i18n.Name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'ownerEmail'">
						<xsl:value-of select="$i18n.Email"/>
					</xsl:when>
					<xsl:when test="fieldName = 'complaintDescription'">
						<xsl:value-of select="$i18n.ComplaintDescription"/>
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'storagePeriod-')">
						<xsl:value-of select="$i18n.StorageType" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="substring(fieldName, 15)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.StoragePeriod" />
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'storageDescription-')">
						<xsl:value-of select="$i18n.StorageType" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.row" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="substring(fieldName, 20)" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="$i18n.StorageDescription" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='NoStorageCounter'">
						<xsl:value-of select="$i18n.validation.NoStorageCounter"/>
					</xsl:when>
					<xsl:when test="messageKey = 'NoStorageSettings'">
						<xsl:value-of select="$i18n.validation.NoStorageSettings"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="$i18n.validation.unknownFault" />
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
	</xsl:template>	
	
</xsl:stylesheet>