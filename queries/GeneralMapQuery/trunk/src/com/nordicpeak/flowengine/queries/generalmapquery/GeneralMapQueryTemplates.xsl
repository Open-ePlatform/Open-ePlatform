<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/generalmapquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/generalmapquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
	
		<script type="text/javascript">
			mapClientLanguages = {
				'DIMENSION_AND_ANGLE_SETTINGS' : '<xsl:value-of select="$i18n.DimensionAndAngleSettings" />'
			};
		</script>

		<script type="text/javascript">
			generalMapQueryLanguage = {
				'ZOOMSCALE_BUTTON': '<xsl:value-of select="$i18n.ZoomScaleButton" />',
				'UNKOWN_ERROR_MESSAGE_TITLE' :  '<xsl:value-of select="$i18n.UnkownErrorMessageTitle" />',
				'UNKOWN_ERROR_MESSAGE' : '<xsl:value-of select="$i18n.UnkownErrorMessage" />'
			};
		</script>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
	
		<xsl:variable name="shortQueryID" select="concat('q', GeneralMapQueryInstance/GeneralMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', GeneralMapQueryInstance/GeneralMapQuery/queryID)" />
		
		<div class="query" id="{$queryID}">
		
			<xsl:variable name="webMapPreview" select="not(GeneralMapQueryInstance/mapPrints/GeneralMapQueryInstancePrint[useInPreview = 'true'])" />
		
			<article>
				
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="GeneralMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="GeneralMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="GeneralMapQueryInstance/GeneralMapQuery/queryID" />
					</xsl:call-template>
				
				</div>

				<xsl:if test="GeneralMapQueryInstance/GeneralMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
		
				<xsl:choose>
					<xsl:when test="$webMapPreview">
		
						<script type="text/javascript">mapClientScriptLocationFallback = '<xsl:value-of select="/Document/mapScriptURL" />';</script>
				
						<div id="{$shortQueryID}_search" class="mapquery-searchwrapper" />
						<div id="{$shortQueryID}_objectconfigtool" class="mapquery-objectconfigwrapper" />
				
						<div id="{$shortQueryID}" class="mapquery" />
				
						<xsl:for-each select="GeneralMapQueryInstance/geometries/Geometry">
							
							<input type="hidden" name="{$shortQueryID}_geometry"><xsl:attribute name="value"><xsl:value-of select="geometry" /><xsl:if test="config">#<xsl:value-of select="config" /></xsl:if></xsl:attribute></input>
							
						</xsl:for-each>
						
						<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{MultiGeometryMapQueryInstance/extent}" />
						<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{MultiGeometryMapQueryInstance/epsg}" />
						<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{MultiGeometryMapQueryInstance/visibleBaseLayer}" />
						
						<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
					</xsl:when>
					<xsl:otherwise>
						<img src="{queryRequestURL}?mapimage=true" class="full bigmargintop" alt="" />
					</xsl:otherwise>
				</xsl:choose>
				
			</article>
				
			<xsl:if test="$webMapPreview">	
				
				<script type="text/javascript">
					
					generalMapQueryMinScales['<xsl:value-of select="$shortQueryID" />'] = '<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/minimalDrawingScale" />';
						
					$(document).ready(function(){
						initGeneralMapQuery('<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmUser" />', true);
					});
					
				</script>
			
			</xsl:if>
		
		</div>
		
	</xsl:template>	
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', GeneralMapQueryInstance/GeneralMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', GeneralMapQueryInstance/GeneralMapQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
		
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
							<xsl:if test="GeneralMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="GeneralMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="GeneralMapQueryInstance/GeneralMapQuery/helpText">		
						<xsl:apply-templates select="GeneralMapQueryInstance/GeneralMapQuery/helpText" />
					</xsl:if>
				
				</div>

				<xsl:if test="GeneralMapQueryInstance/GeneralMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
		
				<xsl:if test="GeneralMapQueryInstance/GeneralMapQuery/startInstruction">
					<div id="{$shortQueryID}_startinstruction" class="mapquery-startinstruction">
						<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/startInstruction" disable-output-escaping="yes" />
						<a class="btn btn-blue"><xsl:value-of select="$i18n.StartInstructionButton" /></a>
					</div>
				</xsl:if>
		
				<script type="text/javascript">mapClientScriptLocationFallback = '<xsl:value-of select="/Document/mapScriptURL" />';</script>
		
				<div class="mapquery-searchwrapper">
		
					<div id="{$shortQueryID}_search" class="searchpud mapquery-searchtool">
						<h3><xsl:value-of select="$i18n.SearchToolDescription" /></h3>
					</div>
			
					<div id="{$shortQueryID}_searchcoordinate" class="searchcoordinate mapquery-searchtool">
						<h3><xsl:value-of select="$i18n.SearchCoordinateToolDescription" /></h3>
					</div>
		
				</div>
		
				<div id="{$shortQueryID}" class="mapquery">
					
					<div id="{$shortQueryID}_objectconfigtool" class="mapquery-objectconfigwrapper" />

				</div>
		
				<xsl:for-each select="GeneralMapQueryInstance/geometries/Geometry">
					
					<input type="hidden" name="{$shortQueryID}_geometry"><xsl:attribute name="value"><xsl:value-of select="geometry" /><xsl:if test="config">#<xsl:value-of select="config" /></xsl:if></xsl:attribute></input>
					
				</xsl:for-each>
				
				<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{GeneralMapQueryInstance/extent}" />
				<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{GeneralMapQueryInstance/epsg}" />
				<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{GeneralMapQueryInstance/visibleBaseLayer}" />
				
				<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
			</article>
		
			<script type="text/javascript">
				
				generalMapQueryMinScales['<xsl:value-of select="$shortQueryID" />'] = '<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/minimalDrawingScale" />';
				
				$(document).ready(function(){
					initGeneralMapQuery('<xsl:value-of select="GeneralMapQueryInstance/GeneralMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/searchURL" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmUser" />', false);
				});
				
			</script>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='GeometriesRequired']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="../../GeneralMapQueryInstance/GeneralMapQuery/requiredQueryMessage" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='GeometryRequired']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="../../GeneralMapQueryInstance/GeneralMapQuery/requiredQueryMessage" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='GeometryNotValid']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.GeometryNotValid" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RequiredQuery']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="../../GeneralMapQueryInstance/requiredQueryMessage" />
			</strong>
		</span>
		
	</xsl:template>
	
		<xsl:template match="validationError[messageKey='InCompleteMapQuerySubmit']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.InCompleteMapQuerySubmit" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToGeneratePNG']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToGeneratePNG" />
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