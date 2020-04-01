<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="Document">
		
		<html xmlns="http://www.w3.org/1999/xhtml">
			
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
				
				<title>
					<xsl:value-of select="$i18n.Signatures" />
					<xsl:text> - </xsl:text>
					<xsl:value-of select="Signatures/ActivityGroup/name" />
					<xsl:text> - </xsl:text>
					<xsl:value-of select="$i18n.FlowInstanceID" />
					<xsl:text> #</xsl:text>
					<xsl:value-of select="Signatures/FlowInstance/flowInstanceID" />
				</title>
				
				<link rel="stylesheet" type="text/css" href="classpath://com/nordicpeak/flowengine/flowapprovalmodule/staticcontent/css/pdf.css" />
				
			</head>
			
			<body>
			
				<xsl:apply-templates select="Signatures" />
				
				<div id="pagenumber-container">
					<xsl:text>Sida </xsl:text>
					<span id="pagenumber"/>
					<xsl:text> av </xsl:text>
					<span id="pagecount"/>
				</div>
				
			</body>
		</html>
		
	</xsl:template>
	
	<xsl:template match="Signatures">
		
		<div class="signatures">
			<h2>
				<xsl:value-of select="ActivityGroup/name" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$i18n.Signatures" />
			</h2>
			
			<p>
				<xsl:value-of select="$i18n.Signatures.description.1" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="ActivityGroup/name" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$i18n.Signatures.description.2" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="FlowInstance/Flow/name" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$i18n.FlowInstanceID" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="FlowInstance/flowInstanceID" />
				<xsl:text>.</xsl:text>
			</p>
			
			<xsl:apply-templates select="ActivityRound/ActivityProgresses/ActivityProgress" mode="signature" />
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityProgress" mode="signature">
	
		<div class="bigmarginbottom">
			
			<div>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="signingData" />
				</xsl:call-template>
			</div>
			
			<div>
				<strong><xsl:value-of select="$i18n.Signature.date"/>:&#160;</strong>
				<xsl:value-of select="signedDate"/>
			</div>
			
			<div>
				<strong><xsl:value-of select="$i18n.Signature.checksum"/>:&#160;</strong>
				<xsl:value-of select="signedChecksum"/>
			</div>
			
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>