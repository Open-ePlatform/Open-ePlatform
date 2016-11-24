<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/d3.v3.min.js
		/js/c3.min.js
		/js/stats.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/c3.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div class="contentitem">
		
			<script>
				chartColor = '<xsl:value-of select="ChartColor"/>';
			</script>
		
			<xsl:apply-templates select="GlobalStatistics"/>
			<xsl:apply-templates select="FlowFamilyStatistics"/>
			<xsl:apply-templates select="FlowFamilyList"/>
		</div>
		
	</xsl:template>
		
	<xsl:template match="GlobalStatistics">
	
		<script>
			<xsl:if test="FlowInstanceCountWeeks">
				flowInstanceCountWeeks = <xsl:value-of select="FlowInstanceCountWeeks"/>;
				flowInstanceCountValues = <xsl:value-of select="FlowInstanceCountValues"/>;
			</xsl:if>
			
			<xsl:if test="FlowFamilyCountWeeks">
				flowFamilyCountWeeks = <xsl:value-of select="FlowFamilyCountWeeks"/>;
				flowFamilyCountValues = <xsl:value-of select="FlowFamilyCountValues"/>;
			</xsl:if>			
		</script>		

	
		<xsl:value-of select="Message" disable-output-escaping="yes"/>
		
		<h2>
			<xsl:value-of select="$i18n.FlowInstancesPerWeek"/>
			
			<xsl:if test="/Document/ExportSupportEnabled">
				<div class="floatright tiny margintop">
						<xsl:text>(</xsl:text>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/instancecount" title="{$i18n.DownloadChartDataInCSVFormat}">
							<xsl:value-of select="$i18n.DownloadChartData"/>
					</a>
						<xsl:text>)</xsl:text>
				</div>
			</xsl:if>
		</h2>
		
		<xsl:choose>
			<xsl:when test="FlowInstanceCountWeeks">
				<div class="border-box full">
					<div id="flowInstanceCountChart"/>	
				</div>			
			</xsl:when>
			<xsl:otherwise>
				<p>
					<xsl:value-of select="$i18n.NoStatisticsAvailable"/>
				</p>
			</xsl:otherwise>
		</xsl:choose>
		
		<br/><br/>
		
		<h2>
			<xsl:value-of select="$i18n.PublishedFlowFamiliesPerWeek"/>
			
			<xsl:if test="/Document/ExportSupportEnabled">
				<div class="floatright tiny margintop">
						<xsl:text>(</xsl:text>
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowcount" title="{$i18n.DownloadChartDataInCSVFormat}">
							<xsl:value-of select="$i18n.DownloadChartData"/>
					</a>
						<xsl:text>)</xsl:text>
				</div>
			</xsl:if>			
		</h2>		

		<xsl:choose>
			<xsl:when test="FlowFamilyCountWeeks">
				<div class="border-box full clearboth">
					<div id="flowFamilyCountChart"/>	
				</div>
			</xsl:when>
			<xsl:otherwise>
				<p>
					<xsl:value-of select="$i18n.NoStatisticsAvailable"/>
				</p>
			</xsl:otherwise>
		</xsl:choose>		
		
	</xsl:template>
	
	<xsl:template match="FlowFamilyStatistics">

		<script>
			<xsl:if test="FlowInstanceCountWeeks">
				flowInstanceCountWeeks = <xsl:value-of select="FlowInstanceCountWeeks"/>;
				flowInstanceCountValues = <xsl:value-of select="FlowInstanceCountValues"/>;
			</xsl:if>
			
			<xsl:if test="ExternalFlowRedirectCountWeeks">
				externalRedirectCountWeeks = <xsl:value-of select="ExternalFlowRedirectCountWeeks"/>;
				externalRedirectCountValues = <xsl:value-of select="ExternalFlowRedirectCountValues"/>;
			</xsl:if>
		
			<xsl:if test="Steps">
				steps = <xsl:value-of select="Steps"/>;
				
				<xsl:if test="StepAbortCount">
					stepAbortCount = <xsl:value-of select="StepAbortCount"/>;
				</xsl:if>
				
				<xsl:if test="StepAbortCount">
					stepUnsubmittedCount = <xsl:value-of select="StepUnsubmittedCount"/>;			
				</xsl:if>
			</xsl:if>
			
			<xsl:if test="count(Versions/FlowStatistics) > 1">
				moduleURL = '<xsl:value-of select="/Document/requestinfo/currentURI"/>/<xsl:value-of select="/Document/module/alias"/>';
				familyID = <xsl:value-of select="flowFamilyID"/>;
			</xsl:if>
			
			<xsl:if test="RatingWeeks">
				ratingWeeks = <xsl:value-of select="RatingWeeks"/>;
				ratingValues = <xsl:value-of select="RatingValues"/>;
			</xsl:if>
		</script>
	
		<xsl:value-of select="Message" disable-output-escaping="yes"/>
		
		<xsl:if test="FlowInstanceCountWeeks or not(ExternalFlowRedirectCountWeeks)">
		
			<h2>
				<xsl:value-of select="$i18n.FlowInstancesPerWeek"/>
				
				<xsl:if test="/Document/ExportSupportEnabled">
					<div class="floatright tiny margintop">
						<xsl:text>(</xsl:text>
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/familiyinstancecount/{flowFamilyID}" title="{$i18n.DownloadChartDataInCSVFormat}">
								<xsl:value-of select="$i18n.DownloadChartData"/>
						</a>
						<xsl:text>)</xsl:text>
					</div>
				</xsl:if>
			</h2>
			
			<xsl:choose>
				<xsl:when test="FlowInstanceCountWeeks">
					<div class="border-box full">
						<div id="flowInstanceCountChart"/>	
					</div>			
				</xsl:when>
				<xsl:otherwise>
					<p>
						<xsl:value-of select="$i18n.NoStatisticsAvailable"/>
					</p>
				</xsl:otherwise>
			</xsl:choose>	
		
		</xsl:if>
		
		<xsl:if test="ExternalFlowRedirectCountWeeks">
			
			<h2>
				<xsl:value-of select="$i18n.RedirectsPerWeek"/>
				
				<xsl:if test="/Document/ExportSupportEnabled">
					<div class="floatright tiny margintop">
						<xsl:text>(</xsl:text>
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/redirectcount/{flowFamilyID}" title="{$i18n.DownloadChartDataInCSVFormat}">
								<xsl:value-of select="$i18n.DownloadChartData"/>
						</a>
						<xsl:text>)</xsl:text>
					</div>
				</xsl:if>
			</h2>
			
			<div class="border-box full">
				<div id="redirectCountChart"/>	
			</div>
			
		</xsl:if>
		
		<xsl:if test="RatingWeeks">
		
			<br/><br/>
			
			<h2>
				<xsl:value-of select="$i18n.RatingPerWeek"/>
				
				<xsl:if test="/Document/ExportSupportEnabled">
					<div class="floatright tiny margintop">
						<xsl:text>(</xsl:text>
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/ratings/{flowFamilyID}" title="{$i18n.DownloadChartDataInCSVFormat}">
								<xsl:value-of select="$i18n.DownloadChartData"/>
						</a>
						<xsl:text>)</xsl:text>
					</div>
				</xsl:if>				
			</h2>
			
			<div id="ratingChart"/>				
			
		</xsl:if>
		
		<br/><br/>
		
		<xsl:if test="count(Versions/FlowStatistics) > 1">
			
			<div class="floatright bigmarginleft">
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="id" select="'abortVersion'"/>
					<xsl:with-param name="element" select="Versions/FlowStatistics"/>
					<xsl:with-param name="labelElementName" select="'version'"/>
					<xsl:with-param name="valueElementName" select="'flowID'"/>
					<xsl:with-param name="labelPrefix" select="$i18n.Version"/>
				</xsl:call-template>			
			</div>
			
		</xsl:if>		
		
		<h2>
			<xsl:value-of select="$i18n.FlowStepAbortCountChartLabel"/>
			
			<xsl:if test="/Document/ExportSupportEnabled">
				<div class="floatright tiny margintop">
				
					<input type="hidden" id="abortbaseurl" value="{/Document/requestinfo/currentURI}/{/Document/module/alias}/stepabortcount/{flowFamilyID}/"/>
				
					<xsl:text>(</xsl:text>
					<a id="abortlink" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/stepabortcount/{flowFamilyID}/{Versions/FlowStatistics/flowID}" title="{$i18n.DownloadChartDataInCSVFormat}">
							<xsl:value-of select="$i18n.DownloadChartData"/>
					</a>
					<xsl:text>)</xsl:text>
				</div>
			</xsl:if>		
		</h2>
		
		<xsl:choose>
			<xsl:when test="StepAbortCount">
				<div class="border-box full">
					<div id="flowStepAbortCountChart"/>	
				</div>		
			</xsl:when>
			<xsl:otherwise>
				<p>
					<xsl:value-of select="$i18n.NoStatisticsAvailable"/>
				</p>
			</xsl:otherwise>
		</xsl:choose>		
		
		<br/><br/>
		
		<xsl:if test="count(Versions/FlowStatistics) > 1">
			
			<div class="floatright bigmarginleft">
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="id" select="'unsubmittedVersion'"/>
					<xsl:with-param name="element" select="Versions/FlowStatistics"/>
					<xsl:with-param name="labelElementName" select="'version'"/>
					<xsl:with-param name="valueElementName" select="'flowID'"/>
					<xsl:with-param name="labelPrefix" select="$i18n.Version"/>
				</xsl:call-template>			
			</div>
			
		</xsl:if>
		
		<h2>
			<xsl:value-of select="$i18n.FlowStepUnsubmittedCountChartLabel"/>
			
			<xsl:if test="/Document/ExportSupportEnabled">
				<div class="floatright tiny margintop">
					
					<input type="hidden" id="unsubmitbaseurl" value="{/Document/requestinfo/currentURI}/{/Document/module/alias}/unsubmitcount/{flowFamilyID}/"/>
					
					<xsl:text>(</xsl:text>
					<a id="unsubmitlink" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/unsubmitcount/{flowFamilyID}/{Versions/FlowStatistics/flowID}" title="{$i18n.DownloadChartDataInCSVFormat}">
							<xsl:value-of select="$i18n.DownloadChartData"/>
					</a>
					<xsl:text>)</xsl:text>
				</div>
			</xsl:if>
		</h2>
		
		<xsl:choose>
			<xsl:when test="StepUnsubmittedCount">
				<div class="border-box full">
					<div id="flowStepUnsubmittedCountChart"/>	
				</div>		
			</xsl:when>
			<xsl:otherwise>
				<p>
					<xsl:value-of select="$i18n.NoStatisticsAvailable"/>
				</p>
			</xsl:otherwise>
		</xsl:choose>			
		
	</xsl:template>	

	<xsl:template match="FlowFamilyList">
	
		<section class="nomargin">
 			<h2 class="bordered"><xsl:value-of select="$i18n.FlowFamilies"/></h2>
 			<ul class="list-table">
 				<xsl:apply-templates select="FlowFamilyStatistics" mode="list"/>
 			</ul>
 		</section>		
	
	</xsl:template>

	<xsl:template match="FlowFamilyStatistics" mode="list">
	
		<li>
			<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">odd</xsl:if>
				<xsl:if test="enabled = 'false'"> disabled</xsl:if>
			</xsl:attribute>
		
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/{flowFamilyID}">
				<span class="text">
					<xsl:value-of select="name"/>
				</span>
			</a>
		
		</li>		
	
	</xsl:template>

</xsl:stylesheet>