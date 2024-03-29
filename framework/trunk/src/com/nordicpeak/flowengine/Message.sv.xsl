<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="MessageTemplates.xsl"/>
	
	<xsl:variable name="i18n.NewMessageWarningNoContactWay">Mottagarna har varken e-post eller telefon satt och kommer d�rf�r inte f� n�gon notifikation om ditt meddelande.</xsl:variable>
	
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.Message.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.PostedByManager">Handl�ggare</xsl:variable>
	<xsl:variable name="i18n.ReplyMessage">Svara</xsl:variable>
	
	<xsl:variable name="i18n.ReadReceipts">Kvittenser</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Requested">Kvittens beg�rd</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Disable">�terkalla beg�ran om kvittens</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Disable.Confirm">Beg�ran om kvittens kommer att �terkallas, vill du forts�tta?</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Read">Meddelande l�st</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Downloaded">Nerladdad</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo">Kvittens kommer att lagras, klicka p� "L�s meddelande" f�r att acceptera och ta del av meddelandet.</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo.Accept">L�s meddelande</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo.Accept.Confirm">Kvittens kommer att lagras, vill du forts�tta?</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.User">Person</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.Description">Beskrivning</xsl:variable>

	<xsl:variable name="i18n.AttachFiles">Bifoga filer</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort fil</xsl:variable>
	<xsl:variable name="i18n.VerifyAttachedFiles">Bekr�fta bifogade filer</xsl:variable>

	<xsl:variable name="i18n.MessageTemplates.choose">Meddelandemallar</xsl:variable>
	
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
