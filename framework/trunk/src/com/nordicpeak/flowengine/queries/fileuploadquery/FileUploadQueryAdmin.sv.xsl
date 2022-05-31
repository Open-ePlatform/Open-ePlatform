<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="FileUploadQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Filuppladdningsfråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Filuppladdningsfråga används när användaren skall ladda upp en eller flera filer i e-tjänsten.
	</xsl:variable>
	
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil från fråga:</xsl:variable>
	
	<xsl:variable name="i18n.FileUploadQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.AllowedFileExtensions">Tillåtna filtyper (ange en filtyp per rad)</xsl:variable>
	<xsl:variable name="i18n.MaxFileCount">Antal filer som får bifogas</xsl:variable>
	<xsl:variable name="i18n.MaxFileSize">Maximal filstorlek som får bifogas i MB</xsl:variable>
	<xsl:variable name="i18n.MaxQuerySize">Maximal storlek för samtliga filer som får bifogas i den här frågan i MB</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part1">som standard</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part2">MB</xsl:variable>
	<xsl:variable name="i18n.maxFileCount">antal filer som får bifogas</xsl:variable>
	<xsl:variable name="i18n.maxFileSize">maximal filstorlek som får bifogas i MB</xsl:variable>
	<xsl:variable name="i18n.MaxFileNameLength">Maximalt antal tecken i filnamnet, inklusive filändelse</xsl:variable>
	<xsl:variable name="i18n.maxFileNameLength">maximalt antal tecken i filnamnet, maximalt värde är 255</xsl:variable>
	<xsl:variable name="i18n.inlinePDFAttachments">Inkludera bifogade PDF filer som sidor i ärendets PDF istället för som separata bilagor</xsl:variable>
	<xsl:variable name="i18n.numberInlineAttachments">Lägg till sidfot i bifogade PDF filer</xsl:variable>
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">Lås fråga vid överlåtelse</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara bifogade filers filnamn som en kommaseparerad lista i ett attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.HideTitle">Dölj rubrik</xsl:variable>
	<xsl:variable name="i18n.hideDescriptionInPDF">Dölj beskrivning i PDF</xsl:variable>

	<xsl:variable name="i18n.attachmentNamePrefixMode">filnamnprefix på bifogade filer i PDF och notifikationer</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode">Filnamnprefix på bifogade filer i PDF och notifikationer</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.QUERY_NAME">Frågans namn</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.NO_PREFIX">Inget prefix</xsl:variable>
	<xsl:variable name="i18n.AttachmentNamePrefixMode.CUSTOM">Eget prefix</xsl:variable>
	
	<xsl:variable name="i18n.attachmentNameCustomPrefix">filnamnsprefix</xsl:variable>
	<xsl:variable name="i18n.AttachmentNameCustomPrefix">Filnamnsprefix</xsl:variable>

	<xsl:variable name="i18n.SelectFilesButtonText">Anpassad text på uppladdningsknapp (max 30 tecken)</xsl:variable>
	
	<xsl:variable name="i18n.MaxFileSizeBiggerThanQuerySize">Maximal filstorlek får inte vara större än maximal storlek på frågan</xsl:variable>
	
	<xsl:variable name="i18n.excludeFileContentFromXML">Exkludera filernas innehåll vid export till XML</xsl:variable>
	
</xsl:stylesheet>
