<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="StopQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Stoppfråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Stoppfråga används för att blockera användaren från att gå vidare i e-tjänsten. När frågan visas är det inte möjligt att gå vidare till nästa steg eller skicka in ärendet.
	</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.StopQueryNotFound">Den begärda frågan hittades inte</xsl:variable>
	<xsl:variable name="i18n.DisplayValidationError">Visa röd ram runt frågan</xsl:variable>
</xsl:stylesheet>
