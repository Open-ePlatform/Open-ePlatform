<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="PersonDataInformerModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Personuppgiftsinst�llningar</xsl:variable>
	<xsl:variable name="java.browserExtensionViewTitle">E-tj�nsten kr�ver behandling av dina personuppgifter</xsl:variable>
	
	<xsl:variable name="i18n.UpdateSettingsTitle">Personuppgiftsinst�llningar f�r e-tj�nsten</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.NoSettings">Inga personuppgifter hanteras.</xsl:variable>
	<xsl:variable name="i18n.UsesPersonData">Personuppgifter hanteras.</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inst�llningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings.Confirm">�r du s�ker p� att du vill ta bort personuppgiftsinst�llningarna f�r den h�r e-tj�nsten?</xsl:variable>
	
	<xsl:variable name="i18n.IsPersonDataUsed">Hanteras personuppgifter i e-tj�nsten?</xsl:variable>
	
	<xsl:variable name="i18n.Accountable">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SavedPersonData">Personuppgifter som behandlas</xsl:variable>
	<xsl:variable name="i18n.Reason">�ndam�let med behandlingen</xsl:variable>
	<xsl:variable name="i18n.Reasons">Laglig grund f�r behandlingen</xsl:variable>
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">F�r evigt</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite">Gallras</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite.Years">Antal �r de sparas</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">�vriga upplysningar</xsl:variable>
	
	<xsl:variable name="i18n.years">�r</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du beh�ver fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	
	<xsl:variable name="i18n.DataFilter">Visa</xsl:variable>
	<xsl:variable name="i18n.FlowFilter">S�k</xsl:variable>
	<xsl:variable name="i18n.Filter.ShowAll">Alla</xsl:variable>
	
	<xsl:variable name="i18n.ListFlows.title">F�ljande e-tj�nster hanterar personuppgifter</xsl:variable>
<!-- 	<xsl:variable name="i18n.ListFlows.description"></xsl:variable> -->
	<xsl:variable name="i18n.Column.FlowName">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.Column.FlowType">Kategori</xsl:variable>
	<xsl:variable name="i18n.Column.FlowCategory">Underkategori</xsl:variable>
	<xsl:variable name="i18n.Column.PersonData">Personuppgifter</xsl:variable>
	<xsl:variable name="i18n.noFlowsFound">Hittade inga e-tj�nster som hanterar personuppgifter</xsl:variable>
	
	<xsl:variable name="i18n.Export">Exportera</xsl:variable>
	
</xsl:stylesheet>
