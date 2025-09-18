<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="MessageTemplates.xsl"/>
	
	<xsl:variable name="i18n.NewMessageWarningNoContactWay">Mottagarna har varken e-post eller telefon satt och kommer därför inte få någon notifikation om ditt meddelande.</xsl:variable>
	
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.Message.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.PostedByManager">Handläggare</xsl:variable>
	<xsl:variable name="i18n.ReplyMessage">Svara</xsl:variable>
	<xsl:variable name="i18n.Added">Skapad</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort notering</xsl:variable>
	<xsl:variable name="i18n.Deleted">Borttagen</xsl:variable>
	<xsl:variable name="i18n.DeleteConfirmation">Vill du verkligen ta bort noteringen?</xsl:variable>
	<xsl:variable name="i18n.Message.Deleted">Noteringen är borttagen</xsl:variable>
	
	<xsl:variable name="i18n.ReadReceipts">Kvittenser</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Requested">Kvittens begärd</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Disable">Återkalla begäran om kvittens</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Disable.Confirm">Begäran om kvittens kommer att återkallas, vill du fortsätta?</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Read">Meddelande läst</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Downloaded">Nerladdad</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo">Genom att klicka på "Läs meddelande" får vi veta att du har öppnat meddelandet. Vill du fortsätta?</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo.Accept">Läs meddelande</xsl:variable>
	<xsl:variable name="i18n.ReadReceiptInfo.Accept.Confirm">Vi får veta att du har öppnat meddelandet, vill du fortsätta?</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.User">Person</xsl:variable>
	<xsl:variable name="i18n.ReadReceipt.Table.Description">Beskrivning</xsl:variable>

	<xsl:variable name="i18n.AttachFiles">Bifoga filer</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort fil</xsl:variable>
	<xsl:variable name="i18n.VerifyAttachedFiles">Bekräfta bifogade filer</xsl:variable>

	<xsl:variable name="i18n.MessageTemplates.choose">Meddelandemallar</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.UnableToParseRequest">Ett okänt fel uppstod vid filuppladdningen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageRequired">Du måste skriva ett meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToLong">Du har skrivit ett för långt meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToShort">Du har skrivit ett för kort meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageRequired">Du måste skriva en notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToLong">Du har skrivit en för lång notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToShort">Du har skrivit en för kort notering</xsl:variable>
	
</xsl:stylesheet>
