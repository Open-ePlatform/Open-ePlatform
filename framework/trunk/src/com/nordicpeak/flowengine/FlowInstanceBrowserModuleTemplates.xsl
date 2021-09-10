<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.blockui.js
		/js/flowengine.helpdialog.js
		/js/flowengine.js
		/js/flowengine.step-navigator.js
		/js/jquery.expander.min.js
		/js/flowinstancebrowser.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="FlowBrowser" class="contentitem">
			<xsl:apply-templates select="ShowFlowOverview"/>
			<xsl:apply-templates select="ShowFlowTypes"/>
			<xsl:apply-templates select="FlowInstanceManagerForm"/>
			<xsl:apply-templates select="FlowInstanceManagerPreview"/>
			<xsl:apply-templates select="FlowInstanceManagerSubmitted"/>
			<xsl:apply-templates select="SigningForm"/>	
			<xsl:apply-templates select="MultiSigningStatusForm"/>
			<xsl:apply-templates select="StandalonePaymentForm"/>
			<xsl:apply-templates select="InlinePaymentForm"/>
			<xsl:apply-templates select="ForeignIDBlocked"/>
		</div>
		
	</xsl:template>

	<xsl:template match="FlowInstanceManagerForm">
	
		<xsl:if test="FlowInstance/Status/contentType != 'NEW'">
			<xsl:call-template name="showFlowInstanceControlPanel">
				<xsl:with-param name="flowInstance" select="FlowInstance" />
				<xsl:with-param name="view" select="'FLOWINSTANCE'" />
			</xsl:call-template>
		</xsl:if>	
	
		<xsl:apply-imports/>
	
	</xsl:template>
	
	<xsl:template match="FlowInstanceManagerPreview">
	
		<xsl:if test="FlowInstance/Status/contentType != 'NEW'">
			<xsl:call-template name="showFlowInstanceControlPanel">
				<xsl:with-param name="flowInstance" select="FlowInstance" />
				<xsl:with-param name="view" select="'FLOWINSTANCE'" />
			</xsl:call-template>
		</xsl:if>	
	
		<xsl:apply-imports/>
	
	</xsl:template>
				
	<xsl:template match="ShowFlowOverview">
		
		<script type="text/javascript">
			userFavouriteModuleURI = '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="userFavouriteModuleAlias" />';
		</script>
		
		<xsl:apply-templates select="Flow" mode="overview" />
		
	</xsl:template>
	
	<xsl:template match="ShowFlowTypes">
		
		<script type="text/javascript">
			searchFlowURI = '<xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/search';
			userFavouriteModuleURI = '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="userFavouriteModuleAlias" />';
		</script>
		
		<xsl:if test="validationError">
			<xsl:apply-templates select="validationError" />
		</xsl:if>
		
		<section class="no-shadow-btm">
	  				
			<div class="search-wrapper">
				<h2 class="h1"><xsl:value-of select="$i18n.SearchTitle" /></h2>
				<div class="search">
					<div class="input-wrapper">
						<input type="text" name="search" class="noborder" id="search">
							<xsl:if test="searchHints">
								<xsl:attribute name="placeholder"><xsl:value-of select="searchHints" /></xsl:attribute>
							</xsl:if>
						</input>
						<!-- <div class="symbol"><i class="xl">r</i></div> -->
						<input type="button" value="s" class="btn btn-search" onclick="searchFlow()" />
					</div>
				</div>
			</div>
			<xsl:if test="recommendedTags">
				<div class="tags-wrapper">
					<div class="tags">
						<h2 class="h1"><xsl:value-of select="$i18n.RecommendedSearches" /></h2>
						<ul>
							<xsl:apply-templates select="recommendedTags/Tag" />
						</ul>
					</div>
				</div>
			</xsl:if>
			
		</section>
		
		<section class="search-results">
			<div class="info">
				<!-- <span class="message"><i>c</i><xsl:value-of select="$i18n.SearchDone" />.</span> -->
				<span class="close"><a href="#"><xsl:value-of select="$i18n.close" /> <i>x</i></a></span>
			</div>
			<h2 class="h1 search-results-title"><span class="title" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Hits.Part1" /><xsl:text>&#160;</xsl:text><span class="hits" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Hits.Part2" /></h2>
			<ul class="previews" />
		</section>
		
		<xsl:choose>
			<xsl:when test="FlowType">
			
				<section>
					<div class="filter-wrapper">
						<div class="hr-divider popularflows-divider">
							<span class="label"><xsl:value-of select="$i18n.SortFlowTypes" /></span>
						</div>
						<a class="btn btn-dark xl filter-btn" data-icon-after="_"><xsl:value-of select="$i18n.FlowTypeFilter" /></a>
						<div class="filters">
							<xsl:apply-templates select="FlowType" mode="filter" />
						</div>
					</div>
				</section>			
			
				<xsl:apply-templates select="FlowType" />
			</xsl:when>
			<xsl:otherwise>
				<section>
					<div class="heading-wrapper">
						<h1><xsl:value-of select="$i18n.NoFlowTypesFound"/></h1>
					</div>					
				</section>
			</xsl:otherwise>
		</xsl:choose>
				
	</xsl:template>
	
	<xsl:template match="Tag">
	
		<li><a href="#"><xsl:value-of select="." /></a></li>
		
	</xsl:template>
	
	<xsl:template match="FlowType" mode="filter">
		
		<xsl:variable name="flowTypeID" select="flowTypeID" />
		
		<a class="btn btn-xs btn-light btn-inline" href="#flowtype{flowTypeID}"><xsl:value-of select="name" /><xsl:text>&#160;(</xsl:text><xsl:value-of select="count(../Flow[FlowType/flowTypeID = $flowTypeID])" /><xsl:text>)</xsl:text></a>
		
	</xsl:template>
	
	<xsl:template match="Category">
		
		<xsl:param name="flows" />
		
		<xsl:variable name="categoryID" select="categoryID" />
		<xsl:variable name="categoryFlows" select="$flows[Category/categoryID = $categoryID]" />
		
		<xsl:if test="$categoryFlows">
			
			<li id="category_{$categoryID}">
				<a href="#">
					<span class="text"><xsl:value-of select="name" /></span>
					<span class="count"><xsl:value-of select="count($flows[Category/categoryID = $categoryID])" /></span>
				</a>
			</li>

		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="Flow">
		
		<xsl:variable name="flowID" select="flowID" />
		
		<li id="flow_{flowID}">
			
			<xsl:attribute name="class">
				
				<xsl:choose>
					<xsl:when test="Category">
						<xsl:text> category_</xsl:text><xsl:value-of select="Category/categoryID" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text> uncategorized</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="enabled = 'false'"> disabled</xsl:if>
	 			<xsl:if test="popular = 'true'"> popular</xsl:if>
			</xsl:attribute>
			
			<xsl:variable name="flowFamilyID" select="FlowFamily/flowFamilyID" />
			
			<xsl:choose>
				<xsl:when test="enabled = 'true'">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{FlowFamily/flowFamilyID}">
					
						<xsl:if test="../openExternalFlowsInNewWindow = 'true' and skipOverview = 'true' and externalLink">
						
							<xsl:attribute name="target">_blank</xsl:attribute>
						
						</xsl:if>
					
						<div class="inner">
							<figure><img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" alt="" /></figure>
							<div>
								<h2>
									<xsl:value-of select="name" />
									<xsl:choose>
										<xsl:when test="/Document/ShowFlowTypes/loggedIn">
											<xsl:if test="/Document/ShowFlowTypes/userFavouriteModuleAlias">
												<i id="flowFamily_{FlowFamily/flowFamilyID}" data-icon-after="*" class="favourite">
													<xsl:if test="not(/Document/ShowFlowTypes/UserFavourite[FlowFamily/flowFamilyID = $flowFamilyID])">
														<xsl:attribute name="class">favourite gray</xsl:attribute>
													</xsl:if>
												</i>
											</xsl:if>
										</xsl:when>
										<xsl:when test="requireAuthentication = 'true'">
											<i data-icon-before="u" title="{$i18n.AuthenticationRequired}" class="marginleft"></i>
										</xsl:when>
									</xsl:choose>
								</h2>
								<span class="description"><xsl:value-of select="shortDescription" disable-output-escaping="yes" /></span>
							</div>
						</div>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<div class="inner">
						<figure><img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}?{IconLastModified}" width="65" alt="" /></figure>
						<div>
							<h2><xsl:value-of select="name" /><b class="hidden">(<xsl:value-of select="$i18n.FlowDisabled" />)</b></h2>
							<span class="description">
								<b>(<xsl:value-of select="$i18n.FlowDisabled" />)</b><br/>
								<xsl:value-of select="shortDescription" disable-output-escaping="yes" />
							</span>
						</div>
					</div>
				</xsl:otherwise>
			</xsl:choose>
			
		</li>
		
	</xsl:template>

	<xsl:template match="ForeignIDBlocked">
	
		<section class="child">
			<div class="section-inside step full">
				<div class="heading-wrapper">
					<div class="inner inner-less-padding">
						<figure>
							<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{Flow/flowID}" alt="" />
						</figure>
	
						<div class="heading">
							<div>
								<h1 class="xl">
									<xsl:value-of select="Flow/name" />
								</h1>
							</div>
						</div>
					</div>
	
				</div>
			</div>
			<div class="section-full header-full no-pad-top">
				<div class="description">
					<xsl:value-of select="Message" disable-output-escaping="yes" />
				</div>
			</div>
		</section>
	
	</xsl:template>
	
</xsl:stylesheet>