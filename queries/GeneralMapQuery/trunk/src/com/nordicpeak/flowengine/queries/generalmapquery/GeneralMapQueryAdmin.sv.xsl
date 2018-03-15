<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="GeneralMapQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Generell kartfråga</xsl:variable>
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil från fråga:</xsl:variable>
	<xsl:variable name="java.pdfAttachmentFilename">Kartbild 1 till $scale</xsl:variable>
	<xsl:variable name="java.pdfAttachmentFilenameWithoutScale">Kartbild</xsl:variable>
	
	<xsl:variable name="i18n.minimumScale">minsta skala för att rita på kartan</xsl:variable>
	
	<xsl:variable name="i18n.GeneralMapQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.StartInstruction">Startmeddelande</xsl:variable>
	<xsl:variable name="i18n.StartInstructionDescription">Meddelande som visas i dialogruta innan användaren börjat interagera med kartan</xsl:variable>
	
	<xsl:variable name="i18n.MapConfiguration">Kartkonfiguration</xsl:variable>
	<xsl:variable name="i18n.CommonSettings">Allmänt</xsl:variable>
	
	<xsl:variable name="i18n.Tools">Verktyg</xsl:variable>
	<xsl:variable name="i18n.ToolsDescription">Välj vilka verktyg som skall inkluderas i kartan</xsl:variable>
	<xsl:variable name="i18n.IncorrectDrawingMessage">Meddelande vid felaktigt zoomläge</xsl:variable>
	<xsl:variable name="i18n.Prints">Utskrifter</xsl:variable>
	<xsl:variable name="i18n.PrintsDescription">Välj skala på de olika kartbilderna som skall genereras och bifogas med ärendet. Välj även vilken av utskrifterna som skall visas i förhandsgranskning och PDF.</xsl:variable>

	<xsl:variable name="i18n.MinimalDrawingScale">Minsta skala för att kunna rita på kartan (ej obligatoriskt)</xsl:variable>
	
	<xsl:variable name="i18n.CustomTooltip">Ange annat namn på verktyget</xsl:variable>
	<xsl:variable name="i18n.OnlyOneGeometry">Tillåt användaren att endast rita en geometri med det här verktyget</xsl:variable>
	<xsl:variable name="i18n.UseInPreview.Part1">Använd utskriften</xsl:variable>
	<xsl:variable name="i18n.UseInPreview.Part2">i förhandsgranskning och PDF</xsl:variable>
	
	<xsl:variable name="i18n.AllowOnlyOneGeometry">Tillåt användaren att endast rita en geometri med alla verktyg</xsl:variable>
	<xsl:variable name="i18n.ChooseScale">Välj skala</xsl:variable>
	
	<xsl:variable name="i18n.requiredQueryMessage">meddelande vid obligatorisk fråga</xsl:variable>
	<xsl:variable name="i18n.RequiredQueryMessage">Meddelande vid obligatorisk fråga</xsl:variable>
	<xsl:variable name="i18n.RequiredQueryMessageDescription">Meddelande som visas när frågan är obligatorisk och inkomplett</xsl:variable>
	
	<xsl:variable name="i18n.SearchServices">Söktjänster</xsl:variable>
	<xsl:variable name="i18n.SearchServicesDescription">Välj vilka söktjänster som skall finnas tillgängliga i kartan</xsl:variable>
	<xsl:variable name="i18n.PUDSearch">Fastighetssök</xsl:variable>
	<xsl:variable name="i18n.AddressSearch">Adresssök</xsl:variable>
	<xsl:variable name="i18n.PlaceSearch">Ortsök</xsl:variable>
	<xsl:variable name="i18n.CoordinateSearch">Koordinatsök</xsl:variable>
	
	<xsl:variable name="i18n.MapConfigurationDescription">Välj vilken kartkonfiguration som du vill utgå ifrån för den här kartfrågan</xsl:variable>
	<xsl:variable name="i18n.ChooseMapConfiguration">Välj kartkonfiguration</xsl:variable>
	
	<xsl:variable name="i18n.NoMapConfigurationChoosen">Du måste välja en kartkonfiguration</xsl:variable>
	<xsl:variable name="i18n.MapConfigurationNotFound">Den valda kartkonfigurationen hittades inte, försök igen</xsl:variable>
	
	<xsl:variable name="i18n.NoMapConfigurationsFound">Inga kartkonfigurationer hittades, kontakta systemansvarig</xsl:variable>
	<xsl:variable name="i18n.NoMapPrintsFound">Inga utskriftkonfigurationer hittades, kontakta systemansvarig</xsl:variable>
	
	<xsl:variable name="i18n.incorrectDrawingMessage">meddelande vid felaktigt zoomläge</xsl:variable>
	<xsl:variable name="i18n.ForceQueryPopulation">Bifoga alltid kartan med ansökan (även om inget ritats i kartan)</xsl:variable>
	
	<xsl:variable name="i18n.CurrentZoomLevel">Aktuellt zoomläge</xsl:variable>
	
</xsl:stylesheet>
