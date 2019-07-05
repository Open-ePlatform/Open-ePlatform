<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="MessageTemplates.xsl"/>
	
	<xsl:variable name="i18n.NewMessageWarningNoContactWay">Mottagarna har varken e-post eller telefon satt och kommer d�rf�r inte f� n�gon notifikation om ditt meddelande.</xsl:variable>
	
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.Message.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.PostedByManager">Handl�ggare</xsl:variable>
	<xsl:variable name="i18n.ReplyMessage">Svara</xsl:variable>

	<xsl:variable name="i18n.AttachFiles">Bifoga filer</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort fil</xsl:variable>
	<xsl:variable name="i18n.VerifyAttachedFiles">Bekr�fta bifogade filer</xsl:variable>

	<xsl:variable name="i18n.ExternalMessageTemplates.choose">Meddelandemallar</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.UnableToParseRequest">Ett ok�nt fel uppstod vid filuppladdningen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageRequired">Du m�ste skriva ett meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToLong">Du har skrivit ett f�r l�ngt meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToShort">Du har skrivit ett f�r kort meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageRequired">Du m�ste skriva en notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToLong">Du har skrivit en f�r l�ng notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToShort">Du har skrivit en f�r kort notering</xsl:variable>
	
</xsl:stylesheet>
