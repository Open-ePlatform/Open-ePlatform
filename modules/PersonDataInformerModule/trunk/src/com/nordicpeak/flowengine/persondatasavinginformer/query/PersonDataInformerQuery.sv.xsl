<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="PersonDataInformerQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.RequiredQuery">Den h�r fr�gan �r obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett ok�nt valideringsfel har uppst�tt!</xsl:variable>
	
	<xsl:variable name="i18n.Error.InvalidFormat">Inneh�llet i det h�r f�ltet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.Error.TooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	
	<xsl:variable name="i18n.Accountable">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SavedPersonData">Personuppgifter som behandlas</xsl:variable>
	<xsl:variable name="i18n.Reason">�ndam�let med behandlingen</xsl:variable>
	<xsl:variable name="i18n.Reasons">R�ttslig grund f�r behandlingen</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">�vriga upplysningar</xsl:variable>
	<xsl:variable name="i18n.ExtraInformationStorage">�vrigt om lagringstid</xsl:variable>
	
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">F�r evigt</xsl:variable>
	
	
	<xsl:variable name="i18n.Accept">Jag har tagit del av ovanst�ende information och godk�nner behandlingen av personuppgifter</xsl:variable>
		
	<xsl:variable name="i18n.YearsSaved.Years">�r</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Months">m�nader</xsl:variable>
	
	<xsl:variable name="i18n.DataRecipient">Mottagare av personuppgifter</xsl:variable>
</xsl:stylesheet>
