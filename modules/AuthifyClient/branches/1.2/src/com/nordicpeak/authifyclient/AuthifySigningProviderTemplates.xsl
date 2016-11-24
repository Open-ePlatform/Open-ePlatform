<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="links">
		/css/authifyclientprovider.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<div id="AuthifySigningProvider">
			
			<xsl:apply-templates select="SignForm" />
			
		</div>
			
	</xsl:template>
	
	<xsl:template match="SignForm">
		
		<xsl:apply-templates select="validationError" />
		
		<article>
			
			<xsl:if test="validationError">
				<xsl:attribute name="class">error</xsl:attribute>
			</xsl:if>
			
			<h2><xsl:value-of select="$i18n.SigningHeader" /></h2>
			<ul>
				<li><a href="{signingURL}&amp;idp=bankid_sign" title="{$i18n.BankID}"><xsl:value-of select="$i18n.BankID" /></a></li>
				<li><a href="{signingURL}&amp;idp=wpki_sign" title="{$i18n.MobileBankID}"><xsl:value-of select="$i18n.MobileBankID" /></a></li>
				<li><a href="{signingURL}&amp;idp=nordea_sign" title="{$i18n.Nordea}"><xsl:value-of select="$i18n.Nordea" /></a></li>
				<li><a href="{signingURL}&amp;idp=telia_sign" title="{$i18n.Telia}"><xsl:value-of select="$i18n.Telia" /></a></li>
			</ul>
			
		</article>

	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SigningFailed']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.SigningFailed" />
		</xsl:call-template>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='IncompleteSigning']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.SigningFailed" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='SSNNotMatching']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.SSNNotMatching" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError">

		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnknownValidationError"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="printValidationError">
		
		<xsl:param name="message" />
		
		<div class="info-box first error">
			<span>
				<strong data-icon-before="!"><xsl:value-of select="$i18n.SigningFailedTitle" />.</strong>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$message" />
			</span>
			<div class="marker" />
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>