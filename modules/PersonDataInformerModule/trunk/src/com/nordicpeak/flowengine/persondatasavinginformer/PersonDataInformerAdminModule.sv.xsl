<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="PersonDataInformerAdminModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validation.requiredField" select="'Du beh�ver fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.DataAlternatives">Personuppgifter</xsl:variable>
	<xsl:variable name="i18n.ReasonAlternatives">Lagliga grunder</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.NoDataAlternatives">Det finns inga personuppgifter �nnu</xsl:variable>
	<xsl:variable name="i18n.NoReasonAlternatives">Det finns inga lagliga grunder �nnu</xsl:variable>
	
	<xsl:variable name="i18n.Update">�ndra</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteDataAlternativeConfirm">�r du s�ker p� att du vill ta bort personuppgiften</xsl:variable>
	<xsl:variable name="i18n.DeleteReasonAlternativeConfirm">�r du s�ker p� att du vill ta bort laglig grund</xsl:variable>
	<xsl:variable name="i18n.Add">L�gg till</xsl:variable>
	<xsl:variable name="i18n.AddInformerDataAlternative">L�gg till personuppgift</xsl:variable>
	<xsl:variable name="i18n.CancelConfirm">�r du s�ker p� att du vill avbryta utan att spara</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerDataAlternative">Uppdatera personuppgift</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AddInformerReasonAlternative">L�gg till laglig grund</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerReasonAlternative">Uppdatera laglig grund</xsl:variable>
	<xsl:variable name="i18n.NoStandardTexts">Det finns inga standardtexter �nnu</xsl:variable>
	<xsl:variable name="i18n.StandardTexts">Standardtexter</xsl:variable>
	<xsl:variable name="i18n.DefaultComplaintName">Beskrivning f�r l�mna klagom�l p� personuppgiftshantering</xsl:variable>
	<xsl:variable name="i18n.DefaultReasonName">�ndam�let med behandlingen</xsl:variable>
	<xsl:variable name="i18n.DefaultStorageName">�vrigt om lagringstid</xsl:variable>
	<xsl:variable name="i18n.UpdateInformerStandardText">Uppdatera standardtext</xsl:variable>
	<xsl:variable name="i18n.StandardText">Standardtext</xsl:variable>
	<xsl:variable name="i18n.AutoSelect">F�rvald</xsl:variable>
</xsl:stylesheet>
