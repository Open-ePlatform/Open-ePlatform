<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="scripts">
		/js/userfavouritemodule.js
	</xsl:variable>

	<xsl:template match="Document">		
		
		<xsl:variable name="flowFamilyOverviewURI"><xsl:value-of select="/Document/contextpath" /><xsl:value-of select="flowBrowserAlias" />/overview</xsl:variable>
		<xsl:variable name="userFavouriteModuleURI"><xsl:value-of select="/Document/contextpath" /><xsl:value-of select="userFavouriteModuleAlias" /></xsl:variable>
		
		<script type="text/javascript">
			flowFamilyOverviewURI = '<xsl:value-of select="$flowFamilyOverviewURI" />';
			userFavouriteModuleURI = '<xsl:value-of select="$userFavouriteModuleURI" />';
			userFavouriteModuleMode = '<xsl:value-of select="mode" />';
		</script>
		
		<section id="UserFavouriteBackgroundModule">
			
			<h2 class="bordered"><xsl:value-of select="module/name"/></h2>
			<ul class="list-table">
				<xsl:apply-templates select="UserFavourite">
					<xsl:with-param name="flowFamilyOverviewURI" select="$flowFamilyOverviewURI" />
				</xsl:apply-templates>
				<li class="always-keep no-favourites">
					<xsl:if test="UserFavourite">
						<xsl:attribute name="style">display:none</xsl:attribute>
					</xsl:if>
					<span class="text"><xsl:value-of select="$i18n.NoFavourites" /></span>
				</li>
			</ul>
			
		</section>
		
	</xsl:template>
	
	<xsl:template match="UserFavourite">
		
		<xsl:param name="flowFamilyOverviewURI" />
		
		<xsl:variable name="odd" select="(position() mod 2) = 0" />
		
		<li id="flowFamily_{FlowFamily/flowFamilyID}">
			
			<xsl:attribute name="class">
				<xsl:if test="$odd">odd</xsl:if>
				<xsl:if test="flowEnabled = 'false'"> disabled</xsl:if>
			</xsl:attribute>
			
			<a href="{$flowFamilyOverviewURI}/{FlowFamily/flowFamilyID}">
				
				<span class="text"><xsl:value-of select="flowName" /></span>
				<i>
					<xsl:choose>
						<xsl:when test="/Document/mode = 'SHOW'">
							<xsl:attribute name="data-icon-after">*</xsl:attribute>
							<xsl:attribute name="class">favourite</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="data-icon-after">t</xsl:attribute>
							<xsl:attribute name="class">delete_favourite</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
				</i>
			</a>
			
		</li>
	
	</xsl:template>
	
</xsl:stylesheet>