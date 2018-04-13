<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" indent="yes" method="xml" omit-xml-declaration="yes"/>

	<xsl:include href="PersonDataInformerQueryPDFTemplates.xsl"/>
	
	<xsl:variable name="i18n.Accountable">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SavedPersonData">Personuppgifter som behandlas</xsl:variable>
	<xsl:variable name="i18n.Reason">�ndam�let med behandlingen</xsl:variable>
	<xsl:variable name="i18n.Reasons">Laglig grund f�r behandlingen</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">�vriga upplysningar</xsl:variable>
	
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">F�r evigt</xsl:variable>
	
	
	<xsl:variable name="i18n.Accept">Jag har tagit del av ovanst�ende information och godk�nner behandlingen av personuppgifter</xsl:variable>
	
	<xsl:variable name="i18n.ExtraInformationStorage">�vrigt om lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Years">�r</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Months">m�nader</xsl:variable>
	<xsl:variable name="i18n.DataRecipient">Mottagare av personuppgifter</xsl:variable>
</xsl:stylesheet>
