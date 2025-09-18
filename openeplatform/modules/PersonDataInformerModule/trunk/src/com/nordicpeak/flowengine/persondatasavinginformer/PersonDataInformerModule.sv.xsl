<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="PersonDataInformerModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Personuppgiftsinst�llningar</xsl:variable>
	<xsl:variable name="java.browserExtensionViewTitle">Behandling av personuppgifter</xsl:variable>
	
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
	<xsl:variable name="i18n.Reasons">R�ttslig grund f�r behandlingen</xsl:variable>
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">F�r evigt</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Custom">Egen beskrivning</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.years">�r</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Years">�r</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.months">m�nader</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Months">M�nader</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite">Gallras</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite.Years">Antal �r uppgifterna sparas</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">�vriga upplysningar</xsl:variable>
	<xsl:variable name="i18n.Responsible">Personuppgiftsansvarig</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	<xsl:variable name="i18n.validation.NoStorageCounter" select="'Felaktigt antal lagringsobjekt'"/>
	<xsl:variable name="i18n.validation.NoStorageSettings">Du m�ste ange minst en lagringstid</xsl:variable>
	
	<xsl:variable name="i18n.DataFilter">Visa</xsl:variable>
	<xsl:variable name="i18n.FlowFilter">S�k</xsl:variable>
	<xsl:variable name="i18n.Filter.ShowAll">Alla</xsl:variable>
	
	<xsl:variable name="i18n.ListFlows.title">F�ljande tj�nster hanterar personuppgifter</xsl:variable>
<!-- 	<xsl:variable name="i18n.ListFlows.description"></xsl:variable> -->
	<xsl:variable name="i18n.Column.FlowName">Tj�nst</xsl:variable>
	<xsl:variable name="i18n.Column.FlowType">Kategori</xsl:variable>
	<xsl:variable name="i18n.Column.FlowCategory">Underkategori</xsl:variable>
	<xsl:variable name="i18n.Column.PersonData">Personuppgifter</xsl:variable>
	<xsl:variable name="i18n.Column.PersonResponsible">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.noFlowsFound">Inga tj�nster som hanterar personuppgifter hittades.</xsl:variable>
	
	<xsl:variable name="i18n.Export">Exportera</xsl:variable>
	
	<xsl:variable name="i18n.ComplaintDescription">Beskrivning f�r att l�mna klagom�l p� personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.OverrideTextDescription">Ange annan beskrivning �n standard</xsl:variable>
	<xsl:variable name="i18n.StorageDescription">Beskrivning av lagringsobjekt</xsl:variable>
	<xsl:variable name="i18n.StorageType">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.StoragePeriod">Antal</xsl:variable>
	<xsl:variable name="i18n.ResponsibleContact">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.ExtraInformationStorage">�vrigt om lagringstid</xsl:variable>
	<xsl:variable name="i18n.ContactOwnerTitle">Kontaktuppgifter - Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Email">E-post</xsl:variable>
	
	<xsl:variable name="i18n.AddStorageSetting">L�gg till lagringsobjekt</xsl:variable>
	<xsl:variable name="i18n.RemoveStorageSettingConfirm">Vill du ta bort lagringsobjektet</xsl:variable>
	
	<xsl:variable name="i18n.ComplaintTitle">L�mna klagom�l p� personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.DataRecipient">Mottagare av personuppgifter</xsl:variable>
	<xsl:variable name="i18n.PersonDataInformerQuery">Bekr�ftelse vid personuppgiftsfr�ga</xsl:variable>
	<xsl:variable name="i18n.OverrideConfirmationText">Ange annan bekr�ftelsetext �n standard</xsl:variable>
	
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
	<xsl:variable name="i18n.StandardTextFor">Standardtext f�r </xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	
</xsl:stylesheet>
