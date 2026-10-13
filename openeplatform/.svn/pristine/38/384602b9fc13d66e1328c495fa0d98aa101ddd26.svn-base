<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="PersonDataInformerModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Personuppgiftsinställningar</xsl:variable>
	<xsl:variable name="java.browserExtensionViewTitle">Behandling av personuppgifter</xsl:variable>
	
	<xsl:variable name="i18n.UpdateSettingsTitle">Personuppgiftsinställningar för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.NoSettings">Inga personuppgifter hanteras.</xsl:variable>
	<xsl:variable name="i18n.UsesPersonData">Personuppgifter hanteras.</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">Ändra inställningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inställningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings.Confirm">Är du säker på att du vill ta bort personuppgiftsinställningarna för den här e-tjänsten?</xsl:variable>
	
	<xsl:variable name="i18n.IsPersonDataUsed">Hanteras personuppgifter i e-tjänsten?</xsl:variable>
	
	<xsl:variable name="i18n.Accountable">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.SavedPersonData">Personuppgifter som behandlas</xsl:variable>
	<xsl:variable name="i18n.Reason">Ändamålet med behandlingen</xsl:variable>
	<xsl:variable name="i18n.Reasons">Rättslig grund för behandlingen</xsl:variable>
	<xsl:variable name="i18n.YearsSaved">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Infinite">För evigt</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Custom">Egen beskrivning</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.years">år</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Years">År</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.months">månader</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Months">Månader</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite">Gallras</xsl:variable>
	<xsl:variable name="i18n.YearsSaved.Finite.Years">Antal år uppgifterna sparas</xsl:variable>
	<xsl:variable name="i18n.ExtraInformation">Övriga upplysningar</xsl:variable>
	<xsl:variable name="i18n.Responsible">Personuppgiftsansvarig</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>
	<xsl:variable name="i18n.validation.NoStorageCounter" select="'Felaktigt antal lagringsobjekt'"/>
	<xsl:variable name="i18n.validation.NoStorageSettings">Du måste ange minst en lagringstid</xsl:variable>
	
	<xsl:variable name="i18n.DataFilter">Visa</xsl:variable>
	<xsl:variable name="i18n.FlowFilter">Sök</xsl:variable>
	<xsl:variable name="i18n.Filter.ShowAll">Alla</xsl:variable>
	
	<xsl:variable name="i18n.ListFlows.title">Följande tjänster hanterar personuppgifter</xsl:variable>
<!-- 	<xsl:variable name="i18n.ListFlows.description"></xsl:variable> -->
	<xsl:variable name="i18n.Column.FlowName">Tjänst</xsl:variable>
	<xsl:variable name="i18n.Column.FlowType">Kategori</xsl:variable>
	<xsl:variable name="i18n.Column.FlowCategory">Underkategori</xsl:variable>
	<xsl:variable name="i18n.Column.PersonData">Personuppgifter</xsl:variable>
	<xsl:variable name="i18n.Column.PersonResponsible">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.noFlowsFound">Inga tjänster som hanterar personuppgifter hittades.</xsl:variable>
	
	<xsl:variable name="i18n.Export">Exportera</xsl:variable>
	
	<xsl:variable name="i18n.ComplaintDescription">Beskrivning för att lämna klagomål på personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.OverrideTextDescription">Ange annan beskrivning än standard</xsl:variable>
	<xsl:variable name="i18n.StorageDescription">Beskrivning av lagringsobjekt</xsl:variable>
	<xsl:variable name="i18n.StorageType">Lagringstid</xsl:variable>
	<xsl:variable name="i18n.StoragePeriod">Antal</xsl:variable>
	<xsl:variable name="i18n.ResponsibleContact">Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.ExtraInformationStorage">Övrigt om lagringstid</xsl:variable>
	<xsl:variable name="i18n.ContactOwnerTitle">Kontaktuppgifter - Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Email">E-post</xsl:variable>
	
	<xsl:variable name="i18n.AddStorageSetting">Lägg till lagringsobjekt</xsl:variable>
	<xsl:variable name="i18n.RemoveStorageSettingConfirm">Vill du ta bort lagringsobjektet</xsl:variable>
	
	<xsl:variable name="i18n.ComplaintTitle">Lämna klagomål på personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.DataRecipient">Mottagare av personuppgifter</xsl:variable>
	<xsl:variable name="i18n.PersonDataInformerQuery">Bekräftelse vid personuppgiftsfråga</xsl:variable>
	<xsl:variable name="i18n.OverrideConfirmationText">Ange annan bekräftelsetext än standard</xsl:variable>
	
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
	<xsl:variable name="i18n.StandardTextFor">Standardtext för </xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	
</xsl:stylesheet>
