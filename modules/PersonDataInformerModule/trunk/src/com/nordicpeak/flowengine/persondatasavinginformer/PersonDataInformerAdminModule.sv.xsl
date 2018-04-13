<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="PersonDataInformerAdminModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validation.requiredField" select="'Du behöver fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.DataAlternatives">Personuppgifter</xsl:variable>
	<xsl:variable name="i18n.ReasonAlternatives">Lagliga grunder</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.NoDataAlternatives">Det finns inga personuppgifter ännu</xsl:variable>
	<xsl:variable name="i18n.NoReasonAlternatives">Det finns inga lagliga grunder ännu</xsl:variable>
	
	<xsl:variable name="i18n.Update">Ändra</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteDataAlternativeConfirm">Är du säker på att du vill ta bort personuppgiften</xsl:variable>
	<xsl:variable name="i18n.DeleteReasonAlternativeConfirm">Är du säker på att du vill ta bort laglig grund</xsl:variable>
	<xsl:variable name="i18n.Add">Lägg till</xsl:variable>
	<xsl:variable name="i18n.AddInformerDataAlternative">Lägg till personuppgift</xsl:variable>
	<xsl:variable name="i18n.CancelConfirm">Är du säker på att du vill avbryta utan att spara</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerDataAlternative">Uppdatera personuppgift</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AddInformerReasonAlternative">Lägg till laglig grund</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerReasonAlternative">Uppdatera laglig grund</xsl:variable>
	<xsl:variable name="i18n.NoStandardTexts">Det finns inga standardtexter ännu</xsl:variable>
	<xsl:variable name="i18n.StandardTexts">Standardtexter</xsl:variable>
	<xsl:variable name="i18n.DefaultComplaintName">Beskrivning för lämna klagomål på personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.DefaultReasonName">Ändamålet med behandlingen</xsl:variable>
	<xsl:variable name="i18n.DefaultStorageName">Övrigt om lagringstid</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerStandardText">Uppdatera standardtext</xsl:variable>
	<xsl:variable name="i18n.StandardText">Standardtext</xsl:variable>
	<xsl:variable name="i18n.AutoSelect">Förvald</xsl:variable>
</xsl:stylesheet>
