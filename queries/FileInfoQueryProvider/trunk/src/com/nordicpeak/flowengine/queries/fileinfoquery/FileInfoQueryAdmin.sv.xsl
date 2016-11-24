<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileInfoQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Informationsfråga med filer</xsl:variable>
	
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil från fråga:</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.HideTitle">Dölj rubrik</xsl:variable>
	<xsl:variable name="i18n.HideBackground">Dölj bakgrund</xsl:variable>
	<xsl:variable name="i18n.DontSetPopulated">Dölj frågan i förhandsgranskning och PDF</xsl:variable>
	
	<xsl:variable name="i18n.DescriptionRequiredWhenTitleHidden">En beskrivning behöver fyllas i då rubriken i satt som dold.</xsl:variable>
	<xsl:variable name="i18n.FileInfoQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>

	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part1">Ett fel uppstod när filen </xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part2"> skulle sparas.</xsl:variable>
	<xsl:variable name="i18n.ConfirmDeleteFile">Är du säker på att du vill ta bort filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	
</xsl:stylesheet>
