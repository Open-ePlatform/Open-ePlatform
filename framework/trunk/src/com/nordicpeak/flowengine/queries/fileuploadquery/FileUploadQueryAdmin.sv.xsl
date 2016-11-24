<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="FileUploadQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Filuppladdningsfråga</xsl:variable>
	
	<xsl:variable name="java.pdfAttachmentDescriptionPrefix">En fil från fråga:</xsl:variable>
	
	<xsl:variable name="i18n.FileUploadQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.AllowedFileExtensions">Tillåtna filtyper (ange en filtyp per rad)</xsl:variable>
	<xsl:variable name="i18n.MaxFileCount">Antal filer som får bifogas</xsl:variable>
	<xsl:variable name="i18n.MaxFileSize">Största datamängd som får bifogas i MB</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part1">som standard</xsl:variable>
	<xsl:variable name="i18n.MaxAllowedFileSize.Part2">MB</xsl:variable>
	<xsl:variable name="i18n.maxFileCount">antal filer som får bifogas</xsl:variable>
	<xsl:variable name="i18n.maxFileSize">största datamängd som får bifogas i MB</xsl:variable>
	
	
</xsl:stylesheet>
