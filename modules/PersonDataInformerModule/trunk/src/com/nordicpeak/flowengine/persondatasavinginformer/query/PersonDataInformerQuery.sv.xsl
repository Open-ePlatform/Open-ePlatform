<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="PersonDataInformerQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.RequiredQuery">Den här frågan är obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	
	<xsl:variable name="i18n.Error.InvalidFormat">Innehållet i det här fältet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.Error.TooLong" select="'För långt innehåll i fältet:'"/>	
	
	<xsl:variable name="i18n.Accountable">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SavedPersonData">Personuppgifter som behandlas</xsl:variable>
	<xsl:variable name="i18n.Reason">Ändamålet med behandlingen</xsl:variable>
	<xsl:variable name="i18n.Reasons">Rättslig grund för behandlingen</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">Övriga upplysningar</xsl:variable>
	<xsl:variable name="i18n.ExtraInformationStorage">Övrigt om lagringstid</xsl:variable>
	
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">För evigt</xsl:variable>
	
	
	<xsl:variable name="i18n.Accept">Jag har tagit del av ovanstående information och godkänner behandlingen av personuppgifter</xsl:variable>
		
	<xsl:variable name="i18n.YearsSaved.Years">år</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Months">månader</xsl:variable>
	
	<xsl:variable name="i18n.DataRecipient">Mottagare av personuppgifter</xsl:variable>
</xsl:stylesheet>
