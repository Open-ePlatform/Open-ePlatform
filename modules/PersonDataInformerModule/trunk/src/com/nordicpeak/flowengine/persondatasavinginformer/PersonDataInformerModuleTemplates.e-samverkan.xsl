<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="scripts">
		/js/persondatainformermodule.e-samverkan.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/persondatainformer.css
		/css/persondatainformer.e-samverkan.css
	</xsl:variable>

	<xsl:template match="ListFlows">
	
		<div id="PersonDataInformer" class="">
		
			<div class="filters-wrapper">
				
				<div class="inner">
				
					<section class="contentitem">
						<h1 class="bigmargintop"><xsl:value-of select="$i18n.ListFlows.title" /></h1>
					</section>
				
					<section class="filter-list persondata">
						<h2>
							<xsl:value-of select="$i18n.Filter" />
							<a href="#" class="clear-filter-btn disabled">
								<i class="material-icons" />
							</a>
						</h2>
						<ul style="display: none">
							<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="filter" />
						</ul>
					</section>
					
					<a href="#" class="btn btn-red clear-all-filter-btn hidden"><xsl:value-of select="$i18n.ResetAllFilters" /><span /></a>
				
				</div>
				
			</div>
	
			<div class="flow-wrapper" style="visibility: visible;">
				<div class="views-wrapper grid-view">
					<div class="flowtype-list active-list">
					
						<form method="post" action="{/Document/ModuleURI}/export">
					
							<xsl:choose>
								<xsl:when test="Flows/Flow">
									
									<section id="flowtype_{flowTypeID}" data-flowtypeid="{flowTypeID}" class="flowtype">
							
										<div class="heading-wrapper">
										
											<a class="floatright btn btn-green" onclick="exportPersonData(event, this);">
												<xsl:value-of select="$i18n.Export"/>
											</a>
											
											<h2>
												<xsl:value-of select="$i18n.Flows" />
												<xsl:text>&#160;(</xsl:text>
												<span class="count"><xsl:value-of select="count(Flows/Flow)" /></span>
												<xsl:text>)</xsl:text>
												<a href="#" class="clear-filter-btn">
													<i class="material-icons" />
												</a>
											</h2>
											
											<ul class="flow-list">
												<xsl:apply-templates select="Flows/Flow" />
											</ul>
											
										</div>
										
									</section>
									
								</xsl:when>
								<xsl:otherwise>
								
									<section>
										<div class="heading-wrapper">
											<h2><xsl:value-of select="$i18n.noFlowsFound"/></h2>
										</div>					
									</section>
									
								</xsl:otherwise>
							</xsl:choose>
							
						</form>
				
					</div>
				</div>
			</div>
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="filter">
		
		<xsl:variable name="flowTypeCount" select="count(../../Settings/FlowFamilyInformerSetting/DataAlternatives/InformerDataAlternative[alternativeID = current()/alternativeID])" />
		
<!-- 		<xsl:if test="$flowTypeCount > 0"> -->
		
			<li>
<!-- 			class="btn btn-light btn-inline" -->
				<a  href="#persondata-{alternativeID}" data-flowtypeid="{alternativeID}">
					<span class="text">
						<xsl:value-of select="name" />
					</span>
					<span class="count">
						<xsl:value-of select="$flowTypeCount" />
					</span>
				</a>
			</li>
		
<!-- 		</xsl:if> -->
		
	</xsl:template>
	
	<xsl:template match="Flow">
		
		<xsl:variable name="flowID" select="flowID" />
		<xsl:variable name="flowFamilyID" select="FlowFamily/flowFamilyID" />
		
		<li id="flow_{flowID}" data-flowid="{flowID}" data-flowname="{name}">
			
			<xsl:attribute name="class">
				<xsl:text>show</xsl:text>
				<xsl:if test="enabled = 'false'"> disabled</xsl:if>
			</xsl:attribute>
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name" select="'flowFamilyID'"  />
				<xsl:with-param name="value" select="$flowFamilyID"  />
			</xsl:call-template>

			<xsl:variable name="targetBlank" select="../openExternalFlowsInNewWindow = 'true' and skipOverview = 'true' and externalLink" />
			<xsl:variable name="flowLink"><xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="../../FlowBrowserFullAlias" />/overview/<xsl:value-of select="$flowFamilyID" /></xsl:variable>
			
			<div class="flow">
				<figure>
					<img alt="" src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" />
				</figure>
				<div>
					<a title="{$i18n.ShowFlow}" href="{$flowLink}">
						<xsl:if test="$targetBlank">
							<xsl:attribute name="target">_blank</xsl:attribute>
						</xsl:if>
						<xsl:if test="enabled = 'false'">
							<xsl:attribute name="href">#</xsl:attribute>
						</xsl:if>
						<h2>
							<xsl:value-of select="name" />
						</h2>
					</a>
					<div class="icons only-desktop-tablet">
						<xsl:choose>
							<xsl:when test="externalLink">
								<i class="material-icons" title="{$i18n.IsExternal}">launch</i>
							</xsl:when>
							<xsl:when test="Steps/Step">
								<i class="material-icons" title="{$i18n.HasFlow}">computer</i>
							</xsl:when>
						</xsl:choose>
						<xsl:if test="hasPDF = 'true'">
							<i class="material-icons" title="{$i18n.HasPDF}">file_download</i>
						</xsl:if>
						<xsl:if test="requireAuthentication = 'true'">
							<i class="material-icons" title="{$i18n.AuthenticationRequired}">person</i>
						</xsl:if>
					</div>
					<div class="description" style="display: block;">
						<p>
							<strong>
								<xsl:value-of select="$i18n.Column.PersonData"/>
							</strong>
							<br/>
							<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/DataAlternatives/InformerDataAlternative" mode="show"/>
							<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/DataAlternatives/InformerDataAlternative" mode="hiddenInputs"/>
						</p>
						
						<p>
							<strong>
								<xsl:value-of select="$i18n.Reasons"/>
							</strong>
							<br/>
							<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/ReasonAlternatives/InformerReasonAlternative" mode="show"/>
							<xsl:apply-templates select="../../Settings/FlowFamilyInformerSetting[flowFamilyID = current()/FlowFamily/flowFamilyID]/ReasonAlternatives/InformerReasonAlternative" mode="hiddenInputs"/>
						</p>
						
						<p>
							<strong>
								<xsl:value-of select="$i18n.YearsSaved"/>
							</strong>
							<br/>
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
						</p>
						
					</div>
					
				</div>
				
			</div>
		</li>
		
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="hiddenInputs">

		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="name" select="'persondataid'"  />
			<xsl:with-param name="value" select="alternativeID"  />
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="hiddenInputs">

		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="name" select="'reasonid'"  />
			<xsl:with-param name="value" select="alternativeID"  />
		</xsl:call-template>
	
	</xsl:template>		
	
</xsl:stylesheet>