<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/singlepolygonmapquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/basemapquery/staticcontent/css/basemapquery.css
	</xsl:variable>

	<xsl:template match="Document">	
	
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>

		<xsl:call-template name="setMapClientInstanceLanguages" />

		<script type="text/javascript">
			singlePolygonMapQueryLanguage = {
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
	
		<xsl:variable name="shortQueryID" select="concat('q', SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID)" />
		
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<article>
				
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID" />
					</xsl:call-template>
				</div>

				<xsl:if test="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
		
				<xsl:choose>
					<xsl:when test="/Document/previewMode = 'WEB_MAP'">
		
						<script type="text/javascript">mapClientScriptLocationFallback = '<xsl:value-of select="/Document/mapScriptURL" />';</script>
				
						<div id="{$shortQueryID}_search" class="mapquery-searchwrapper" />
						<div id="{$shortQueryID}_objectconfigtool" class="mapquery-objectconfigwrapper" />
				
						<div id="{$shortQueryID}" class="mapquery" />
				
						<div class="mapquery-pudinfo-wrapper">
							<div id="{$shortQueryID}_pudinfo" class="hidden">
								<xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span />
							</div>
						</div>
						
						<input id="{$shortQueryID}_propertyUnitDesignation" type="hidden" name="{$shortQueryID}_propertyUnitDesignation" value="{SinglePolygonMapQueryInstance/propertyUnitDesignation}" />
						<input id="{$shortQueryID}_polygon" type="hidden" name="{$shortQueryID}_polygon" value="{SinglePolygonMapQueryInstance/polygon}" />
						<input id="{$shortQueryID}_polygonConfig" type="hidden" name="{$shortQueryID}_polygonConfig" value="{SinglePolygonMapQueryInstance/polygonConfig}" />
						<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{SinglePolygonMapQueryInstance/extent}" />
						<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{SinglePolygonMapQueryInstance/epsg}" />
						<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{SinglePolygonMapQueryInstance/visibleBaseLayer}" />
						
						<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
					</xsl:when>
					<xsl:otherwise>
						
						<img src="{queryRequestURL}?mapimage=true" class="full bigmargintop" alt="{SinglePolygonMapQueryInstance/propertyUnitDesignation}" />
					
						<div class="mapquery-pudinfo-wrapper">
							<xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span><xsl:value-of select="SinglePolygonMapQueryInstance/propertyUnitDesignation" /></span>
						</div>
					
					</xsl:otherwise>
				</xsl:choose>
				
			</article>
			
			<xsl:if test="/Document/previewMode = 'WEB_MAP'">	
			
				<script type="text/javascript">
					
					singlePolygonMapQueryMinScales['<xsl:value-of select="$shortQueryID" />'] = '<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/minimumScale" />';
						
					$(function(){
						initSinglePolygonMapQuery('<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmSearchURL" />', '<xsl:value-of select="/Document/lmUser" />', true);
					});
					
				</script>
				
			</xsl:if>
		
		</div>
		
	</xsl:template>	
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
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
							<xsl:if test="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="SinglePolygonMapQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/helpText">		
						<xsl:apply-templates select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/helpText" />
					</xsl:if>
				
				</div>

				<xsl:if test="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
		
				<xsl:if test="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/startInstruction">
					<div id="{$shortQueryID}_startinstruction" class="mapquery-startinstruction">
						<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/startInstruction" disable-output-escaping="yes" />
						<a class="btn btn-blue"><xsl:value-of select="$i18n.StartInstructionButton" /></a>
					</div>
				</xsl:if>
		
				<script type="text/javascript">mapClientScriptLocationFallback = '<xsl:value-of select="/Document/mapScriptURL" />';</script>
		
				<div class="mapquery-searchwrapper">
		
					<div id="{$shortQueryID}_search" class="searchpud mapquery-searchtool">
						<h3><xsl:value-of select="$i18n.SearchToolDescription" /></h3>
					</div>
			
					<div id="{$shortQueryID}_searchcoordinate" class="searchcoordinate mapquery-searchtool">
						<h3><xsl:value-of select="$i18n.SearchCoordinateToolDescription" /><span class="epsg" /></h3>
					</div>
		
				</div>
		
				<div id="{$shortQueryID}" class="mapquery">
					
					<div id="{$shortQueryID}_objectconfigtool" class="mapquery-objectconfigwrapper" />
					
				</div>
		
				<div class="mapquery-pudinfo-wrapper">
					<div id="{$shortQueryID}_pudinfo" class="hidden">
						<xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text><span />
					</div>
					<div id="{$shortQueryID}_coordinatesinfo" class="hidden">
						<xsl:value-of select="$i18n.Coordinates" /><xsl:text>:&#160;</xsl:text><span />
					</div>
				</div>
				
				<input id="{$shortQueryID}_propertyUnitDesignation" type="hidden" name="{$shortQueryID}_propertyUnitDesignation" value="{SinglePolygonMapQueryInstance/propertyUnitDesignation}" />
				<input id="{$shortQueryID}_polygon" type="hidden" name="{$shortQueryID}_polygon" value="{SinglePolygonMapQueryInstance/polygon}" />
				<input id="{$shortQueryID}_extent" type="hidden" name="{$shortQueryID}_extent" value="{SinglePolygonMapQueryInstance/extent}" />
				<input id="{$shortQueryID}_epsg" type="hidden" name="{$shortQueryID}_epsg" value="{SinglePolygonMapQueryInstance/epsg}" />
				<input id="{$shortQueryID}_baseLayer" type="hidden" name="{$shortQueryID}_baseLayer" value="{SinglePolygonMapQueryInstance/visibleBaseLayer}" />
				
				<xsl:if test="/Document/requirePolygonConfig">
					<input id="{$shortQueryID}_polygonConfig" type="hidden" name="{$shortQueryID}_polygonConfig" value="{SinglePolygonMapQueryInstance/polygonConfig}" />
				</xsl:if>
				
				<script src="{/Document/mapScriptURL}" type="text/javascript" />
				
			</article>
		
			<script type="text/javascript">
				
				singlePolygonMapQueryMinScales['<xsl:value-of select="$shortQueryID" />'] = '<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/minimumScale" />';

				$(function(){
					initSinglePolygonMapQuery('<xsl:value-of select="SinglePolygonMapQueryInstance/SinglePolygonMapQuery/queryID" />', '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="/Document/fullAlias" />', '<xsl:value-of select="/Document/startExtent" />', '<xsl:value-of select="/Document/lmSearchURL" />', '<xsl:value-of select="/Document/lmUser" />', false);
				});
				
			</script>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='PolygonNotValid']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.PolygonNotValid" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='CentroidNotMatchingPUD']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.CentroidNotMatchingPUD" />
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