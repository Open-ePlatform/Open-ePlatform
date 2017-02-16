<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowCatalogModuleTemplates.xsl"/>
	
	<xsl:variable name="java.shareFlowTitle">Dela e-tjänsten</xsl:variable>
	
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.requiredField" select="'Du behöver fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	

	<xsl:variable name="i18n.ValidationError.FlowFamilyNotFound">Den angivna e-tjänstfamiljen hittades inte</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowNotFound">Den angivna e-tjänsten hittades inte</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownRemoteError">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.ValidationError.AccessDenied">Åtkomst nekad</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RepositoryCommunicationFailed">Ett fel uppstod i kommunikationen med katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ErrorExportingFlow">Ett okänt fel har uppstått vid exportering av e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowAlreadyExists">Den här versionen av e-tjänsten finns redan i den valda katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FileSizeLimitExceeded">E-tjänsten är för stor för att tas emot av den valda katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FileUploadException">Ett fel uppstod när e-tjänsten skulle skickas till katalogen</xsl:variable>
	
	<xsl:variable name="i18n.ErrorConnectingToRepository.1">Det gick inte att ansluta till</xsl:variable>
	<xsl:variable name="i18n.ErrorConnectingToRepository.2">, försök igen senare.</xsl:variable>
	
	<xsl:variable name="i18n.Column.Source">Organisation</xsl:variable>
	<xsl:variable name="i18n.Column.FlowName">E-tjänstens namn</xsl:variable>
	<xsl:variable name="i18n.Column.Version">Version</xsl:variable>
	<xsl:variable name="i18n.Column.Versions">Versioner</xsl:variable>
	<xsl:variable name="i18n.Column.LastModified">Senast ändrad</xsl:variable>
	<xsl:variable name="i18n.Column.Comment">Kommentar</xsl:variable>

	<xsl:variable name="i18n.ListRepositories.Title">E-tjänstkatalog</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.Description">Här listas alla kataloger. Klicka på en rad för att visa mer information om e-tjänsten.</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.NoRepositoriesFound">Inga kataloger hittades.</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.NoFlowsFound">Inga e-tjänster funna.</xsl:variable>
	
	<xsl:variable name="i18n.ListFlowVersions.Title">E-tjänst</xsl:variable>
	<xsl:variable name="i18n.ListFlowVersions.Description">Här listas alla versioner av e-tjänsten som finns i katalogen</xsl:variable>
	
	<xsl:variable name="i18n.ShareFlow.Title">Dela e-tjänst</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.Description">Välj vilken katalog du vill ladda upp e-tjänsten till.</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.SelectRepository">Katalog</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.NoRepositoriesWithAccessFound">Inga kataloger med uppladdningsrättigheter hittades.</xsl:variable>
	
	<xsl:variable name="i18n.Download.Title">Ladda ner version</xsl:variable>
	<xsl:variable name="i18n.DownloadError.Title">Ett fel uppstod vid nedladdning</xsl:variable>
	
	<xsl:variable name="i18n.Back">Bakåt</xsl:variable>
	<xsl:variable name="i18n.Submit">Skicka in</xsl:variable>

	<xsl:variable name="i18n.DeleteFlowConfirm">Är du säker på att du vill ta bort version</xsl:variable>
	<xsl:variable name="i18n.DeleteFlow.title">Ta bort version</xsl:variable>
	<xsl:variable name="i18n.DeleteFlowDisabledNoAccess">Du saknar rättigheter för att ta bort versionen</xsl:variable>
	
</xsl:stylesheet>
