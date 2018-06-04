<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="ShowFlowOverview">
		
		<xsl:apply-templates select="Flow" mode="overview">
			<xsl:with-param name="internalFlowMethodAlias" select="'testflow'"></xsl:with-param>
			<xsl:with-param name="externalFlowMethodAlias" select="''"/>
		</xsl:apply-templates>
		
	</xsl:template>
		
</xsl:stylesheet>