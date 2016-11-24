<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="AuthifySigningProviderTemplates.xsl"/>
	
	<xsl:variable name="java.signingMessage">Du signerar nu $flow.name med ärendenummer $flowInstance.flowInstanceID. Ärendet har följande unika nyckel: $hash</xsl:variable>
	
	<xsl:variable name="i18n.SigningHeader">Välj någon av följande e-legitimationer att signera med</xsl:variable>
	<xsl:variable name="i18n.BankID">BankID</xsl:variable>
	<xsl:variable name="i18n.MobileBankID">Mobilt BankID</xsl:variable>
	<xsl:variable name="i18n.Nordea">Nordea</xsl:variable>
	<xsl:variable name="i18n.Telia">Telia</xsl:variable>
	
	<xsl:variable name="i18n.SigningFailedTitle">Signeringen misslyckades</xsl:variable>
	<xsl:variable name="i18n.SigningFailed">Ett fel inträffade vid signeringen, försök igen!</xsl:variable>
	<xsl:variable name="i18n.SSNNotMatching">Den e-legitimation du loggade in med är inte samma som du signerade med, försök igen!</xsl:variable>
	<xsl:variable name="i18n.UnknownValidationError">Ett okänt fel inträffade vid signeringen</xsl:variable>
	
</xsl:stylesheet>
