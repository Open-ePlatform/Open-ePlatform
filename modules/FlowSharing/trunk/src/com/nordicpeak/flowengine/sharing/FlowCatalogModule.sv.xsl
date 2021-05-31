<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowCatalogModuleTemplates.xsl"/>
	
	<xsl:variable name="java.shareFlowTitle">Dela e-tj�nsten</xsl:variable>
	
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	

	<xsl:variable name="i18n.ValidationError.FlowFamilyNotFound">Den angivna e-tj�nstfamiljen hittades inte</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowNotFound">Den angivna e-tj�nsten hittades inte</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownRemoteError">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.ValidationError.AccessDenied">�tkomst nekad</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RepositoryCommunicationFailed">Ett fel uppstod i kommunikationen med katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ErrorExportingFlow">Ett ok�nt fel har uppst�tt vid exportering av e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowAlreadyExists">Den h�r versionen av e-tj�nsten finns redan i den valda katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FileSizeLimitExceeded">E-tj�nsten �r f�r stor f�r att tas emot av den valda katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FileUploadException">Ett fel uppstod n�r e-tj�nsten skulle skickas till katalogen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.DownloadFailedError">Ett fel uppstod vid nedladdning</xsl:variable>
	
	<xsl:variable name="i18n.ErrorConnectingToRepository.1">Det gick inte att ansluta till</xsl:variable>
	<xsl:variable name="i18n.ErrorConnectingToRepository.2">, f�rs�k igen senare.</xsl:variable>
	
	<xsl:variable name="i18n.Column.Source">Organisation</xsl:variable>
	<xsl:variable name="i18n.Column.FlowName">E-tj�nstens namn</xsl:variable>
	<xsl:variable name="i18n.Column.Version">Version</xsl:variable>
	<xsl:variable name="i18n.Column.Versions">Versioner</xsl:variable>
	<xsl:variable name="i18n.Column.LastModified">Senast �ndrad</xsl:variable>
	<xsl:variable name="i18n.Column.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.Column.Repository">Katalog</xsl:variable>

	<xsl:variable name="i18n.ListRepositories.Title">E-tj�nstkatalog</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.Description">H�r listas alla kataloger. Klicka p� en rad f�r att visa mer information om e-tj�nsten.</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.NoRepositoriesFound">Inga kataloger hittades.</xsl:variable>
	<xsl:variable name="i18n.ListRepositories.NoFlowsFound">Inga e-tj�nster funna.</xsl:variable>
	
	<xsl:variable name="i18n.ListFlowVersions.Title">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.ListFlowVersions.Description">H�r listas alla versioner av e-tj�nsten som finns i katalogen</xsl:variable>
	
	<xsl:variable name="i18n.ShareFlow.Title">Dela e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.Description">V�lj vilken katalog du vill ladda upp e-tj�nsten till.</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.SelectRepository">Katalog</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.ShareFlow.NoRepositoriesWithAccessFound">Inga kataloger med uppladdningsr�ttigheter hittades.</xsl:variable>
	
	<xsl:variable name="i18n.Download.Title">Ladda ner version</xsl:variable>
	<xsl:variable name="i18n.DownloadError.Title">Ett fel uppstod vid nedladdning</xsl:variable>
	
	<xsl:variable name="i18n.Back">Bak�t</xsl:variable>
	<xsl:variable name="i18n.Submit">Skicka in</xsl:variable>

	<xsl:variable name="i18n.DeleteFlowConfirm">�r du s�ker p� att du vill ta bort version</xsl:variable>
	<xsl:variable name="i18n.DeleteFlow.title">Ta bort version</xsl:variable>
	<xsl:variable name="i18n.DeleteFlowDisabledNoAccess">Du saknar r�ttigheter f�r att ta bort versionen</xsl:variable>
	
	<xsl:variable name="i18n.DataTable.decimal">,</xsl:variable>
	<xsl:variable name="i18n.DataTable.emptyTable">Finns inga e-tj�nster att visa</xsl:variable>
	<xsl:variable name="i18n.DataTable.info">Visar _START_ till _END_ av _TOTAL_ e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoEmpty">Visar 0 till 0 av 0 e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoFiltered">(filtrerat fr�n totalt _MAX_ e-tj�nster)</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoPostFix"/>
	<xsl:variable name="i18n.DataTable.thousands"/>
	<xsl:variable name="i18n.DataTable.lengthMenu">Visa _MENU_ e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.loadingRecords">H�mtar e-tj�nster...</xsl:variable>
	<xsl:variable name="i18n.DataTable.processing">Bearbetar...</xsl:variable>
	<xsl:variable name="i18n.DataTable.search">S�k:</xsl:variable>
	<xsl:variable name="i18n.DataTable.zeroRecords">Inga matchande e-tj�nster hittades</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.first">F�rsta</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.last">Sista</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.next">N�sta</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.previous">F�reg�ende</xsl:variable>
	<xsl:variable name="i18n.DataTable.aria.sortAscending">: aktivera f�r att sortera kolumnen stigande</xsl:variable>
	<xsl:variable name="i18n.DataTable.aria.sortDescending">: aktivera f�r att sortera kolumnen fallande</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.none">Inga rader markerade</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.one">1 rad markerad</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.many">%d rader markerade</xsl:variable>
	
	<xsl:variable name="i18n.FilterRepository">Kataloger</xsl:variable>
	<xsl:variable name="i18n.FilterRepository.None">Alla</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
	
</xsl:stylesheet>
