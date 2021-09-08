<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/pudmapquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/basemapquery/staticcontent/css/basemapquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>

		<xsl:call-template name="setMapClientInstanceLanguages" />

		<script type="text/javascript">
			pudMapQueryLanguage = {
				'RETRIEVING_PUD' : '<xsl:value-of select="$i18n.RetrievingPUD" />',
				'ZOOMSCALE_BUTTON': '<xsl:value-of select="$i18n.ZoomScaleButton" />',
				'UNKOWN_ERROR_MESSAGE_TITLE' :  '<xsl:value-of select="$i18n.UnkownErrorMessageTitle" />',
				'UNKOWN_ERROR_MESSAGE' : '<xsl:value-of select="$i18n.UnkownErrorMessage" />',
				'NO_PUD_FOUND_MESSAGE_TITLE' : '<xsl:value-of select="$i18n.NoPUDFoundMessageTitle" />',
				'NO_PUD_FOUND_MESSAGE' : '<xsl:value-of select="$i18n.NoPUDFoundMessage" />'
			};
		</script>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
	
		<xsl:variable name="shortQueryID" select="concat('q', PUDMapQueryInstance/PUDMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', PUDMapQueryInstance/PUDMapQuery/queryID)" />
		
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="PUDMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<article>
				
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="PUDMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="PUDMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="PUDMapQueryInstance/PUDMapQuery/queryID" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="PUDMapQueryInstance/PUDMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
		
				<xsl:choose>
					<xsl:when test="/Document/previewMode = 'WEB_MAP'">
		
						<div id="{$shortQueryID}_search" class="mapquery-searchwrapper mapquery-searchtool" />
				
						<div id="{$shortQueryID}" class="mapquery" />
				
						<div class="mapquery-pudinfo-wrapper">
							<div id="{$shortQueryID}_pudinfo" class="hidden">
								<xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span />
							</div>
							<div id="{$shortQueryID}_coordinatesinfo" class="hidden"><xsl:value-of select="$i18n.Coordinates" /><xsl:text>:&#160;</xsl:text><span /></div>
						</div>
						
						<input id="{$shortQueryID}_propertyUnitDesignation" type="hidden" name="{$shortQueryID}_propertyUnitDesignation" value="{PUDMapQueryInstance/propertyUnitDesignation}" />
						<input id="{$shortQueryID}_xCoordinate" type="hidden" name="{$shortQueryID}_xCoordinate" value="{PUDMapQueryInstance/xCoordinate}" />
						<input id="{$shortQueryID}_yCoordinate" type="hidden" name="{$shortQueryID}_yCoordinate" value="{PUDMapQueryInstance/yCoordinate}" />
						<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{PUDMapQueryInstance/extent}" />
						<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{PUDMapQueryInstance/epsg}" />
						<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{PUDMapQueryInstance/visibleBaseLayer}" />
						
						<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
					</xsl:when>
					<xsl:otherwise>
						
						<img src="{queryRequestURL}?mapimage=true" class="full bigmargintop" alt="{PUDMapQueryInstance/propertyUnitDesignation}" />
					
						<div class="mapquery-pudinfo-wrapper">
							<div><xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span><xsl:value-of select="PUDMapQueryInstance/propertyUnitDesignation" /></span></div>
							<div><xsl:value-of select="$i18n.Coordinates" /><xsl:text>:&#160;</xsl:text><span><xsl:value-of select="PUDMapQueryInstance/xCoordinate" /><xsl:text>,&#160;</xsl:text><xsl:value-of select="PUDMapQueryInstance/yCoordinate" /></span></div>
						</div>
					
					</xsl:otherwise>
				</xsl:choose>
				
			</article>
			
			<xsl:if test="/Document/previewMode = 'WEB_MAP'">
				
				<script type="text/javascript">
						
					$(function(){
						initPUDMapQuery('<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmSearchURL" />', '<xsl:value-of select="/Document/lmUser" />', true);
					});
					
				</script>
		
			</xsl:if>
		
		</div>
		
	</xsl:template>	
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', PUDMapQueryInstance/PUDMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', PUDMapQueryInstance/PUDMapQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="PUDMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<a name="{$queryID}" />
	
			<div id="{$shortQueryID}-validationerrors">
				<xsl:if test="ValidationErrors/validationError">
					<div id="{$queryID}-validationerrors" class="validationerrors">
						<div class="info-box error">
							<xsl:apply-templates select="ValidationErrors/validationError" />
							<div class="marker"></div>
						</div>
					</div>
				</xsl:if>
			</div>
	
			<article>
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="PUDMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="PUDMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="PUDMapQueryInstance/PUDMapQuery/helpText">		
						<xsl:apply-templates select="PUDMapQueryInstance/PUDMapQuery/helpText" />
					</xsl:if>

				</div>

				<xsl:if test="PUDMapQueryInstance/PUDMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
				
				<xsl:if test="PUDMapQueryInstance/PUDMapQuery/startInstruction">
					<div id="{$shortQueryID}_startinstruction" class="mapquery-startinstruction">
						<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/startInstruction" disable-output-escaping="yes" />
						<a class="btn btn-blue"><xsl:value-of select="$i18n.StartInstructionButton" /></a>
					</div>
				</xsl:if>
		
				<script type="text/javascript">mapClientScriptLocationFallback = '<xsl:value-of select="/Document/mapScriptURL" />';</script>
		
				<div class="mapquery-searchwrapper">
					<div id="{$shortQueryID}_search" class="searchpud mapquery-searchtool">
						<h3><xsl:value-of select="$i18n.SearchToolDescription" /></h3>
					</div>
				</div>
		
				<div id="{$shortQueryID}" class="mapquery" />
		
				<div class="mapquery-pudinfo-wrapper">
					<div id="{$shortQueryID}_pudinfo" class="hidden">
						<xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span />
					</div>
					<div id="{$shortQueryID}_coordinatesinfo" class="hidden"><xsl:value-of select="$i18n.Coordinates" /><xsl:text>:&#160;</xsl:text><span /></div>
				</div>
				
				<input id="{$shortQueryID}_propertyUnitDesignation" type="hidden" name="{$shortQueryID}_propertyUnitDesignation" value="{PUDMapQueryInstance/propertyUnitDesignation}" />
				<input id="{$shortQueryID}_xCoordinate" type="hidden" name="{$shortQueryID}_xCoordinate" value="{PUDMapQueryInstance/xCoordinate}" />
				<input id="{$shortQueryID}_yCoordinate" type="hidden" name="{$shortQueryID}_yCoordinate" value="{PUDMapQueryInstance/yCoordinate}" />
				<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{PUDMapQueryInstance/extent}" />
				<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{PUDMapQueryInstance/epsg}" />
				<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{PUDMapQueryInstance/visibleBaseLayer}" />
				
				<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
			</article>
		
			<script type="text/javascript">
				
				pudMapQueryProviderURI = '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />';
				pudMapQueryMinScales['<xsl:value-of select="$shortQueryID" />'] = '<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/minimumScale" />';
				
				$(function(){
					initPUDMapQuery('<xsl:value-of select="PUDMapQueryInstance/PUDMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmSearchURL" />', '<xsl:value-of select="/Document/lmUser" />', false);
				});
				
			</script>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='CoordinatesNotValid']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.CoordinatesNotValid" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ErrorLookingUpEstateID']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Validation.ErrorLookingUpEstateID" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RequiredQuery']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="/Document/requiredQueryMessage" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>
	
</xsl:stylesheet>