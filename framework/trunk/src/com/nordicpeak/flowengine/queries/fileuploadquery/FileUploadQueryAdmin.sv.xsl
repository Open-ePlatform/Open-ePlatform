<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="FileUploadQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Filuppladdningsfr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Filuppladdningsfr�ga anv�nds n�r anv�ndaren skall ladda upp en eller flera filer i e-tj�nsten.
	</xsl:variable>
	
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil fr�n fr�ga:</xsl:variable>
	
	<xsl:variable name="i18n.FileUploadQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.AllowedFileExtensions">Till�tna filtyper (ange en filtyp per rad)</xsl:variable>
	<xsl:variable name="i18n.MaxFileCount">Antal filer som f�r bifogas</xsl:variable>
	<xsl:variable name="i18n.MaxFileSize">Maximal filstorlek som f�r bifogas i MB</xsl:variable>
	<xsl:variable name="i18n.MaxQuerySize">Maximal storlek f�r samtliga filer som f�r bifogas i den h�r fr�gan i MB</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part1">som standard</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part2">MB</xsl:variable>
	<xsl:variable name="i18n.maxFileCount">antal filer som f�r bifogas</xsl:variable>
	<xsl:variable name="i18n.maxFileSize">maximal filstorlek som f�r bifogas i MB</xsl:variable>
	<xsl:variable name="i18n.MaxFileNameLength">Maximalt antal tecken i filnamnet, inklusive fil�ndelse</xsl:variable>
	<xsl:variable name="i18n.maxFileNameLength">maximalt antal tecken i filnamnet, maximalt v�rde �r 255</xsl:variable>
	<xsl:variable name="i18n.inlinePDFAttachments">Inkludera bifogade PDF filer som sidor i �rendets PDF ist�llet f�r som separata bilagor</xsl:variable>
	<xsl:variable name="i18n.numberInlineAttachments">L�gg till sidfot i bifogade PDF filer</xsl:variable>
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">L�s fr�ga vid �verl�telse</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara bifogade filers filnamn som en kommaseparerad lista i ett attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.HideTitle">D�lj rubrik</xsl:variable>
	<xsl:variable name="i18n.hideDescriptionInPDF">D�lj beskrivning i PDF</xsl:variable>

	<xsl:variable name="i18n.attachmentNamePrefixMode">filnamnprefix p� bifogade filer i PDF och notifikationer</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode">Filnamnprefix p� bifogade filer i PDF och notifikationer</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.QUERY_NAME">Fr�gans namn</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.NO_PREFIX">Inget prefix</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.CUSTOM">Eget prefix</xsl:variable>
	
	<xsl:variable name="i18n.attachmentNameCustomPrefix">filnamnsprefix</xsl:variable>
	<xsl:variable name="i18n.AttachmentNameCustomPrefix">Filnamnsprefix</xsl:variable>

	<xsl:variable name="i18n.SelectFilesButtonText">Anpassad text p� uppladdningsknapp (max 30 tecken)</xsl:variable>
	
	<xsl:variable name="i18n.MaxFileSizeBiggerThanQuerySize">Maximal filstorlek f�r inte vara st�rre �n maximal storlek p� fr�gan</xsl:variable>
	
	<xsl:variable name="i18n.excludeFileContentFromXML">Exkludera filernas inneh�ll vid export till XML</xsl:variable>
	
</xsl:stylesheet>
