<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="Document">		
		
		<xsl:apply-templates select="OperatingMessage" />
		
	</xsl:template>
	
	<xsl:template match="OperatingMessage">
	
		<xsl:variable name="messageWithClickableLinks">
			<xsl:call-template name="replaceLineBreaksAndLinks">
				<xsl:with-param name="string" select="message"/>
				<xsl:with-param name="target" select="'_blank'"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="messageType = 'INFO'">
				<section id="OperatingMessageBackgroundModule" class="modal info">
				<xsl:copy-of select="$messageWithClickableLinks" />
				</section>
			</xsl:when>
 			<xsl:otherwise> <!--  messageType = 'WARNING' -->
				<section id="OperatingMessageBackgroundModule" class="modal warning">
					<i style="font-size: 16px; margin-right: 4px; color: rgb(199, 52, 52);" class="icon">!</i>
					<xsl:copy-of select="$messageWithClickableLinks" />
				</section>
			</xsl:otherwise>

			
		</xsl:choose>
	
	</xsl:template>
	
</xsl:stylesheet>