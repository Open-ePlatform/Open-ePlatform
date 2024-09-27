<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
	
		<xsl:call-template name="showMapQueryPDFPreview">
			<xsl:with-param name="query" select="MultiGeometryMapQuery" />
			<xsl:with-param name="queryInstance" select="MultiGeometryMapQueryInstance" />
		</xsl:call-template>
	
	</xsl:template>
	
</xsl:stylesheet>