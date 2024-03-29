<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="FileUploadQueryTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Filuppladdningsfr�ga</xsl:variable>
	
	<xsl:variable name="i18n.MaxFileCountReached.part1">Du f�r maximalt bifoga </xsl:variable>
	<xsl:variable name="i18n.MaxFileCountReached.part2"> fil(er).</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.part2"> �r av en otill�ten filtyp och har d�rf�r inte sparats.</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	<xsl:variable name="i18n.FileSizeZero.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeZero.part2"> �r tom och har d�rf�r inte sparats.</xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part1">Ett fel uppstod n�r filen </xsl:variable>
	<xsl:variable name="i18n.UnableToSaveFile.part2"> skulle sparas.</xsl:variable>
	<xsl:variable name="i18n.RequiredField">Den h�r fr�gan �r obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.UnknownValidationError">Ett ok�nt valideringsfel har uppst�tt!</xsl:variable>
	<xsl:variable name="i18n.ConfirmDeleteFile">�r du s�ker p� att du vill ta bort filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.MaximumFileCount">Maximalt antal filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileNameLength">Maximalt antal tecken i filnamnet</xsl:variable>
	<xsl:variable name="i18n.AllowedFilextentions">Till�tna filtyper</xsl:variable>
	<xsl:variable name="i18n.FileNameLengthLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileNameLengthLimitExceeded.part2"> har ett filnamn med </xsl:variable>
	<xsl:variable name="i18n.FileNameLengthLimitExceeded.part3"> tecken vilket �verskrider det maximalt till�tna antalet tecken som �r </xsl:variable>
	<xsl:variable name="i18n.FileNameLengthLimitExceeded.part4">.</xsl:variable>
	<xsl:variable name="i18n.MaximumQuerySize">Total maximal storlek f�r filer i den h�r fr�gan</xsl:variable>
	<xsl:variable name="i18n.QuerySizeLimitExceeded.part1">Maximal storlek f�r den h�r fr�gan har �verskridits. Du f�r maximalt ladda upp </xsl:variable>
	<xsl:variable name="i18n.QuerySizeLimitExceeded.part2"> i den h�r fr�gan.</xsl:variable>

</xsl:stylesheet>
