<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="FileUploadQueryTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Filuppladdningsfråga</xsl:variable>
	
	<xsl:variable name="i18n.MaxFileCountReached.part1">Du får maximalt bifoga </xsl:variable>
	<xsl:variable name="i18n.MaxFileCountReached.part2"> fil(er).</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.part2"> är av en otillåten filtyp och har därför inte sparats.</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part1">Ett fel uppstod när filen </xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part2"> skulle sparas.</xsl:variable>
	<xsl:variable name="i18n.RequiredField">Den här frågan är obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	<xsl:variable name="i18n.ConfirmDeleteFile">Är du säker på att du vill ta bort filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.MaximumFileCount">Maximalt antal filer</xsl:variable>
	<xsl:variable name="i18n.AllowedFilextentions">Tillåtna filtyper</xsl:variable>

</xsl:stylesheet>
