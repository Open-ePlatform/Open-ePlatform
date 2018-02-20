<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/generalmapqueryadmin.js?v2
	</xsl:variable>

	<xsl:variable name="links">
		/css/generalmapqueryadmin.css
	</xsl:variable>

	<xsl:variable name="rawZoomScaleOptions">
		<option>
			<name>1: 500</name>
			<value>500</value>
		</option>
		<option>
			<name>1: 1000</name>
			<value>1000</value>
		</option>
		<option>
			<name>1: 2000</name>
			<value>2000</value>
		</option>
		<option>
			<name>1: 4000</name>
			<value>4000</value>
		</option>
		<option>
			<name>1: 8000</name>
			<value>8000</value>
		</option>
		<option>
			<name>1: 16000</name>
			<value>16000</value>
		</option>
		<option>
			<name>1: 48000</name>
			<value>48000</value>
		</option>
		<option>
			<name>1: 144000</name>
			<value>144000</value>
		</option>
		<option>
			<name>1: 512000</name>
			<value>512000</value>
		</option>
		<option>
			<name>1: 1024000</name>
			<value>1024000</value>
		</option>
	</xsl:variable>
	
	<xsl:variable name="zoomScaleOptions" select="exsl:node-set($rawZoomScaleOptions)/option" />

	<xsl:template match="Document">	
		
		<div id="GeneralMapQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateGeneralMapQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateGeneralMapQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="GeneralMapQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateGeneralMapQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="GeneralMapQuery" />
			</xsl:call-template>
			
			<h2 class="floatleft clearboth"><xsl:value-of select="$i18n.MapConfiguration" /></h2>
			
			<div class="lightbackground border bigpadding floatleft full border-box bigmarginbottom">
				<div class="floatleft clearboth full">
					<h2><xsl:value-of select="$i18n.CommonSettings" /></h2>
				</div>
				
				<div class="floatleft full bigmarginbottom">
					
					<div class="floatleft full">
					
						<div class="floatleft full bigmarginbottom">
							<label class="floatleft clearboth" for="mapConfigurationSelector"><xsl:value-of select="$i18n.MapConfiguration" /></label>
							<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.MapConfigurationDescription" /></div>
							<div class="floatleft full">
								<xsl:choose>
									<xsl:when test="MapConfiguration">
										<xsl:call-template name="createDropdown">
											<xsl:with-param name="id" select="'mapConfigurationSelector'"/>
											<xsl:with-param name="name" select="'mapConfigurationID'"/>
											<xsl:with-param name="element" select="MapConfiguration"/>
											<xsl:with-param name="labelElementName" select="'name'" />
											<xsl:with-param name="valueElementName" select="'mapConfigurationID'" />
											<xsl:with-param name="addEmptyOption" select="$i18n.ChooseMapConfiguration" />
											<xsl:with-param name="selectedValue" select="GeneralMapQuery/MapConfiguration/mapConfigurationID" />
										</xsl:call-template>
										<xsl:for-each select="MapConfiguration">
											<xsl:if test="printConfigurationID">
												<xsl:call-template name="createHiddenField">
													<xsl:with-param name="name" select="concat('mapconfig_', mapConfigurationID)" />
													<xsl:with-param name="value" select="printConfigurationID" />
												</xsl:call-template>
											</xsl:if>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										
										<p class="error"><xsl:value-of select="$i18n.NoMapConfigurationsFound" /></p>
										
									</xsl:otherwise>
								</xsl:choose>
								
							</div>
						</div>
					
					</div>
					
				</div>
				
				<div class="floatleft full bigmarginbottom hidden mapquery-wrapper">
				
					<div class="floatleft full">
						
						<div class="floatleft full bigmarginbottom">
							<label for="startInstruction" class="floatleft clearboth"><xsl:value-of select="$i18n.StartInstruction" /></label>
							<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.StartInstructionDescription" /></div>
							<div class="floatleft full">
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'startInstruction'" />
									<xsl:with-param name="name" select="'startInstruction'" />
									<xsl:with-param name="title" select="$i18n.StartInstruction"/>
									<xsl:with-param name="class" select="'common-ckeditor'" />
									<xsl:with-param name="element" select="GeneralMapQuery" />
								</xsl:call-template>
							</div>
						</div>
						
						<div class="floatleft full bigmarginbottom">
							<label class="floatleft clearboth" for="requiredQueryMessage"><xsl:value-of select="$i18n.RequiredQueryMessage" /></label>
							<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.RequiredQueryMessageDescription" /></div>
							<div class="floatleft full clearboth">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'requiredQueryMessage'" />
									<xsl:with-param name="name" select="'requiredQueryMessage'" />
									<xsl:with-param name="element" select="GeneralMapQuery" />
								</xsl:call-template>
							</div>
						</div>
						
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'forceQueryPopulation'"/>
								<xsl:with-param name="name" select="'forceQueryPopulation'"/>
								<xsl:with-param name="value" select="'true'"/>
								<xsl:with-param name="checked" select="GeneralMapQuery/forceQueryPopulation = 'true'" />
							</xsl:call-template>
							
							<label for="forceQueryPopulation"><xsl:value-of select="$i18n.ForceQueryPopulation" /></label>
						</div>
						
					</div>
					
				</div>
				
			</div>
			
			<div class="lightbackground border bigpadding floatleft full border-box bigmarginbottom hidden mapquery-wrapper">

				<h2 class="floatleft clearboth"><xsl:value-of select="$i18n.Tools" /></h2>
				
				<div class="floatleft full bigmarginbottom">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'allowOnlyOneGeometry'"/>
						<xsl:with-param name="name" select="'allowOnlyOneGeometry'"/>
						<xsl:with-param name="value" select="'true'"/>
						<xsl:with-param name="checked" select="GeneralMapQuery/allowOnlyOneGeometry = 'true'" />
					</xsl:call-template>
					
					<label for="allowOnlyOneGeometry"><xsl:value-of select="$i18n.AllowOnlyOneGeometry" /></label>
				</div>
				
				<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.ToolsDescription" /></div>
				
				<xsl:apply-templates select="MapConfiguration" mode="form">
					<xsl:with-param name="mapQueryElement" select="GeneralMapQuery" />
				</xsl:apply-templates>
				
				<div class="floatleft full bigmarginbottom">
					<label class="floatleft clearboth" for="minimumScale"><xsl:value-of select="$i18n.MinimalDrawingScale" /></label>
					<div class="floatleft full">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'minimalDrawingScale'"/>
							<xsl:with-param name="name" select="'minimalDrawingScale'"/>
							<xsl:with-param name="element" select="$zoomScaleOptions"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="addEmptyOption" select="$i18n.ChooseScale" />
							<xsl:with-param name="selectedValue" select="GeneralMapQuery/minimalDrawingScale" />
						</xsl:call-template>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom hidden">
					<label class="floatleft clearboth" for="minimumScale"><xsl:value-of select="$i18n.IncorrectDrawingMessage" /></label>
					<div class="floatleft full">
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'incorrectDrawingMessage'" />
							<xsl:with-param name="name" select="'incorrectDrawingMessage'" />
							<xsl:with-param name="title" select="$i18n.IncorrectDrawingMessage"/>
							<xsl:with-param name="rows" select="'3'"/>
							<xsl:with-param name="element" select="GeneralMapQuery" />
						</xsl:call-template>
					</div>
				</div>
				
			</div>

			<xsl:if test="SearchLMService">
			
				<div class="lightbackground border bigpadding floatleft full border-box bigmarginbottom hidden mapquery-wrapper">
	
					<h2 class="floatleft clearboth"><xsl:value-of select="$i18n.SearchServices" /></h2>
	
					<div class="clearboth floatleft"><xsl:value-of select="$i18n.SearchServicesDescription" /></div>
					
					<xsl:if test="SearchLMService/pudSearchEnabled">
					
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'pudSearchEnabled'"/>
								<xsl:with-param name="name" select="'pudSearchEnabled'"/>
								<xsl:with-param name="value" select="'true'"/>
								<xsl:with-param name="element" select="GeneralMapQuery" />
							</xsl:call-template>
							
							<label for="pudSearchEnabled"><xsl:value-of select="$i18n.PUDSearch" /></label>
						</div>
					
					</xsl:if>
					
					<xsl:if test="SearchLMService/addressSearchEnabled">
					
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'addressSearchEnabled'"/>
								<xsl:with-param name="name" select="'addressSearchEnabled'"/>
								<xsl:with-param name="value" select="'true'"/>
								<xsl:with-param name="element" select="GeneralMapQuery" />
							</xsl:call-template>
							
							<label for="addressSearchEnabled"><xsl:value-of select="$i18n.AddressSearch" /></label>
						</div>
					
					</xsl:if>
					
					<xsl:if test="SearchLMService/placeSearchEnabled">
					
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'placeSearchEnabled'"/>
								<xsl:with-param name="name" select="'placeSearchEnabled'"/>
								<xsl:with-param name="value" select="'true'"/>
								<xsl:with-param name="element" select="GeneralMapQuery" />
							</xsl:call-template>
							
							<label for="placeSearchEnabled"><xsl:value-of select="$i18n.PlaceSearch" /></label>
						</div>
					
					</xsl:if>
					
					<xsl:if test="SearchLMService/coordinateSearchEnabled">
					
						<div class="floatleft full">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'coordinateSearchEnabled'"/>
								<xsl:with-param name="name" select="'coordinateSearchEnabled'"/>
								<xsl:with-param name="value" select="'true'"/>
								<xsl:with-param name="element" select="GeneralMapQuery" />
							</xsl:call-template>
							
							<label for="coordinateSearchEnabled"><xsl:value-of select="$i18n.CoordinateSearch" /></label>
						</div>
					
					</xsl:if>
					
				</div>
			
			</xsl:if>
			
			<div class="lightbackground border bigpadding floatleft full border-box bigmarginbottom hidden mapquery-wrapper">

				<h2 class="floatleft clearboth"><xsl:value-of select="$i18n.Prints" /></h2>

				<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.PrintsDescription" /></div>
				
				<xsl:choose>
					<xsl:when test="MapPrint">
						<xsl:apply-templates select="MapPrint">
							<xsl:with-param name="mapQueryElement" select="GeneralMapQuery" />
						</xsl:apply-templates>						
					</xsl:when>
					<xsl:otherwise>
						<p class="clearboth error"><xsl:value-of select="$i18n.NoMapPrintsFound" /></p>
					</xsl:otherwise>
				</xsl:choose>
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}">
					<xsl:if test="not(MapConfiguration)">
						<xsl:attribute name="disabled">true</xsl:attribute>
					</xsl:if>
				</input>
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="MapConfiguration" mode="form">
	
		<xsl:param name="mapQueryElement" />
	
		<div id="tools_{mapConfigurationID}" class="tools floatleft full hidden">
			<xsl:apply-templates select="mapTools/MapTool">
				<xsl:with-param name="mapQueryElement" select="$mapQueryElement" />
			</xsl:apply-templates>
		</div>
		
		<xsl:apply-templates select="mapTools/MapTool" mode="form">
			<xsl:with-param name="mapQueryElement" select="$mapQueryElement" />
		</xsl:apply-templates>

	</xsl:template>

	<xsl:template match="MapTool">
		
		<xsl:param name="mapQueryElement" />
		
		<xsl:variable name="toolID" select="toolID" />
		<xsl:variable name="idSuffix" select="concat(concat(../../mapConfigurationID, '_'), $toolID)" />
		<xsl:variable name="id" select="concat('tool_', $idSuffix)" />
		
		<div class="tool">
		
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$id"/>
				<xsl:with-param name="name" select="$id"/>
				<xsl:with-param name="elementName" select="'toolID'"/>
				<xsl:with-param name="value" select="toolID"/>
				<xsl:with-param name="title" select="tooltip"/>
				<xsl:with-param name="element" select="$mapQueryElement/mapTools/GeneralMapQueryTool/MapTool[toolID = $toolID]" />
				<xsl:with-param name="requestparameters" select="../../../requestparameters" />
			</xsl:call-template>

			<label for="{$id}" class="checkbox" title="{tooltip}" style="background-image: url('{/Document/requestinfo/currentURI}/{/Document/module/alias}/toolicon/{toolID}')"><xsl:text>&#160;</xsl:text></label>

		</div>
		
	</xsl:template>

	<xsl:template match="MapTool" mode="form">
		
		<xsl:param name="mapQueryElement" />
		
		<xsl:variable name="toolID" select="toolID" />
		
		<xsl:variable name="generalMapQueryTool" select="$mapQueryElement/mapTools/GeneralMapQueryTool[MapTool/toolID = $toolID]" />
		
		<xsl:variable name="idSuffix" select="concat(concat(../../mapConfigurationID, '_'), $toolID)" />
		<xsl:variable name="id" select="concat('tool_', $idSuffix)" />
		
		<div id="{$id}_form" class="hidden">
			<div class="floatleft full">
				<label class="floatleft clearboth"><xsl:value-of select="tooltip" /></label>
				<div class="floatleft fifty clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat($id, '_tooltip')" />
						<xsl:with-param name="name" select="concat($id, '_tooltip')" />
						<xsl:with-param name="placeholder" select="$i18n.CustomTooltip"/>
						<xsl:with-param name="value" select="$generalMapQueryTool/tooltip" />
						<xsl:with-param name="requestparameters" select="../../../requestparameters" />
					</xsl:call-template>
				</div>
			</div>
			<xsl:if test="toolType = 'DRAWING'">
				<div class="floatleft full margintop">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="concat($id, '_onlyOneGeometry')"/>
						<xsl:with-param name="name" select="concat($id, '_onlyOneGeometry')"/>
						<xsl:with-param name="value" select="toolID"/>
						<xsl:with-param name="checked" select="$generalMapQueryTool/onlyOneGeometry = 'true'" />
						<xsl:with-param name="requestparameters" select="../../../requestparameters" />
					</xsl:call-template>
					
					<label for="{concat($id, '_onlyOneGeometry')}"><xsl:value-of select="$i18n.OnlyOneGeometry" /></label>
				</div>
			</xsl:if>
			
			<hr class="floatleft full" />
			
		</div>
		
	</xsl:template>

	<xsl:template match="MapPrint">
		
		<xsl:param name="mapQueryElement" />
		
		<xsl:variable name="mapPrintID" select="mapPrintID" />
		
		<xsl:variable name="id" select="concat('mapprint_', mapPrintID)" />
		
		<xsl:variable name="generalMapQueryPrint" select="$mapQueryElement/mapPrints/GeneralMapQueryPrint[MapPrint/mapPrintID = $mapPrintID]" />
		
		<div class="floatleft full mapprint" data-printconfigurationid="{PrintConfiguration/printConfigurationID}">
		
			<div class="floatleft bigmarginbottom">
				<label class="floatleft clearboth" for="minimumScale"><xsl:value-of select="name" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="concat($id, '_scale')"/>
						<xsl:with-param name="name" select="concat($id, '_scale')"/>
						<xsl:with-param name="element" select="PrintService/scales/Scale" />
						<xsl:with-param name="class" select="'floatleft bigmarginright'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="valueElementName" select="'scaleID'" />
						<xsl:with-param name="addEmptyOption" select="$i18n.CurrentZoomLevel" />
						<xsl:with-param name="selectedValue" select="PrintService/scales/Scale[scale = $generalMapQueryPrint/scale]/scaleID" />
						<xsl:with-param name="requestparameters" select="../requestparameters" />
					</xsl:call-template>
					
					<xsl:if test="OutputFormat/format = 'png'">
						<div class="floatleft bigmarginleft">
							<xsl:call-template name="createRadio">
								<xsl:with-param name="id" select="concat($id, '_useInPreview')"/>
								<xsl:with-param name="name" select="'useInPreview'"/>
								<xsl:with-param name="value" select="$mapPrintID" />
								<xsl:with-param name="checked" select="$generalMapQueryPrint/useInPreview = 'true' or count(../MapPrint[OutputFormat[format = 'png']]) = 1" />
								<xsl:with-param name="requestparameters" select="../requestparameters" />
							</xsl:call-template>
							<label for="{concat($id, '_useInPreview')}"><xsl:value-of select="$i18n.UseInPreview.Part1" /><xsl:text>&#160;&quot;</xsl:text><xsl:value-of select="name" /><xsl:text>&quot;&#160;</xsl:text><xsl:value-of select="$i18n.UseInPreview.Part2" /></label>
						</div>
					</xsl:if>
				</div>
			</div>
		
		</div>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedGeneralMapQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.GeneralMapQueryNotFound" />
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NoMapConfigurationChoosen']">
		
		<p class="error">
			<xsl:value-of select="$i18n.NoMapConfigurationChoosen" />
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'MapConfigurationNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.MapConfigurationNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'minimumScale'">
				<xsl:value-of select="$i18n.minimumScale" />
			</xsl:when>
			<xsl:when test="$fieldName = 'requiredQueryMessage'">
				<xsl:value-of select="$i18n.requiredQueryMessage" />
			</xsl:when>
			<xsl:when test="$fieldName = 'incorrectDrawingMessage'">
				<xsl:value-of select="$i18n.incorrectDrawingMessage" />
			</xsl:when>
			<xsl:when test="starts-with($fieldName, 'tool_') and ../displayName">
				<xsl:value-of select="../displayName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>