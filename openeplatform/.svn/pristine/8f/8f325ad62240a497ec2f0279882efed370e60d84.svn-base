<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
	
		<xsl:call-template name="showMapQueryPDFPreview">
			<xsl:with-param name="query" select="PUDMapQuery" />
			<xsl:with-param name="queryInstance" select="PUDMapQueryInstance" />
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template name="showSpecificMapInformation">
		
		<div>
			<strong><xsl:value-of select="$i18n.Coordinates" /><xsl:text>:&#160;</xsl:text></strong>
			<xsl:value-of select="PUDMapQueryInstance/xCoordinate" />
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="PUDMapQueryInstance/yCoordinate" />
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>