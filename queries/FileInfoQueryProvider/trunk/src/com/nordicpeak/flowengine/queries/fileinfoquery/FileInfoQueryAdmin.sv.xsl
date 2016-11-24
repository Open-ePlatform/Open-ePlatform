<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileInfoQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Informationsfr�ga med filer</xsl:variable>
	
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil fr�n fr�ga:</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">�ndra fr�ga</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.HideTitle">D�lj rubrik</xsl:variable>
	<xsl:variable name="i18n.HideBackground">D�lj bakgrund</xsl:variable>
	<xsl:variable name="i18n.DontSetPopulated">D�lj fr�gan i f�rhandsgranskning och PDF</xsl:variable>
	
	<xsl:variable name="i18n.DescriptionRequiredWhenTitleHidden">En beskrivning beh�ver fyllas i d� rubriken i satt som dold.</xsl:variable>
	<xsl:variable name="i18n.FileInfoQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>

	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part1">Ett fel uppstod n�r filen </xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part2"> skulle sparas.</xsl:variable>
	<xsl:variable name="i18n.ConfirmDeleteFile">�r du s�ker p� att du vill ta bort filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	
</xsl:stylesheet>
