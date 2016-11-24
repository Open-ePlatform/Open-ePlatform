<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="scripts">
		/js/userfavouritemenumodule.js
	</xsl:variable>

	<xsl:template match="Document">		
		
		<xsl:variable name="flowFamilyOverviewURI"><xsl:value-of select="/Document/contextpath" /><xsl:value-of select="flowBrowserAlias" />/overview</xsl:variable>
		
		<script type="text/javascript">
			flowFamilyOverviewURI = '<xsl:value-of select="$flowFamilyOverviewURI" />';
		</script>
		
		<li id="UserFavouriteMenuModule" class="dd">
			<div class="marker"></div>
			<a><span><xsl:value-of select="$i18n.UserFavourites" /><xsl:text>&#160;</xsl:text><span class="icon">_</span></span></a>
			<div class="submenu">
				<ul>
					<xsl:apply-templates select="UserFavourite">
						<xsl:with-param name="flowFamilyOverviewURI" select="$flowFamilyOverviewURI" />
					</xsl:apply-templates>
					<li class="always-keep no-favourites">
						<xsl:if test="UserFavourite">
							<xsl:attribute name="style">display:none</xsl:attribute>
						</xsl:if>
						<span class="text"><xsl:value-of select="$i18n.NoFavourites" /></span>
					</li>
					<li class="always-keep bordered-link">
						<a href="{/Document/contextpath}{editFavouritesAlias}"><xsl:value-of select="$i18n.UpdateUserFavourites" /></a>
					</li>
				</ul>
			</div>
		</li>
		
	</xsl:template>
	
	<xsl:template match="UserFavourite">
		
		<xsl:param name="flowFamilyOverviewURI" />
		
		<li id="flowFamily_{FlowFamily/flowFamilyID}">
			
			<xsl:if test="flowEnabled = 'false'"><xsl:attribute name="class">disabled</xsl:attribute></xsl:if>
			
			<a href="{$flowFamilyOverviewURI}/{FlowFamily/flowFamilyID}">
				<span class="text" data-icon-before=">"><xsl:value-of select="flowName" /></span>
			</a>					
			
		</li>
	
	</xsl:template>
	
</xsl:stylesheet>