<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:template match="Document">
		<div class="contentitem">
		
			<div id="AuthifyClientModule">
				
				<xsl:apply-templates select="ChooseIDP" />
				<xsl:apply-templates select="Sign" />
				<xsl:apply-templates select="Signed" />
				
			</div>
			
		</div>
	</xsl:template>
	
	<xsl:template match="ChooseIDP">
		
		<div>
			
			<h1>Signering</h1>
			<h3>Välj någon av följande e-legitimationer att signera med:</h3>
			<ul>
				<li><a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}?idp=bankid_sign">BankID</a></li>
				<li><a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}?idp=nordea_sign">Nordea</a></li>
				<li><a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}?idp=telia_sign">Telia</a></li>
			</ul>
		</div>
						
	</xsl:template>
	
	<xsl:template match="Signed">
	
		<div>
			<h1>Signering lyckades</h1>
			
			<h3>Information om signeringen:</h3>
			
			<xsl:value-of select="AuthifySession/signingInfo"  />
			
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>