<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">		
		
		<xsl:apply-templates select="OperatingMessage" />
		
	</xsl:template>
	
	<xsl:template match="OperatingMessage">
		
		<section id="OperatingMessageBackgroundModule" class="modal warning">
			<i style="font-size: 16px; margin-right: 4px; color: rgb(199, 52, 52);" class="icon">!</i>
			<xsl:value-of select="message" />
		</section>
	
	</xsl:template>
	
</xsl:stylesheet>