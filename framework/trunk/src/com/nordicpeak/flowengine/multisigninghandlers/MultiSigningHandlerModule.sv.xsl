<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="MultiSigningHandlerModuleTemplates.xsl"/>

	<xsl:variable name="java.signMessage">$poster har bett dig signera �rendet nedan.</xsl:variable>	
	<xsl:variable name="java.signedMessage">Du skrev under detta �rende den $signed.</xsl:variable>
	
	<xsl:variable name="i18n.SocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.Signature">Signerat</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.SigningStatus.Description">Ditt �rende v�ntar p� att en eller flera personer ska signera det. I tabellen nedan ser du aktuell status g�llande signeringen av detta �rende.</xsl:variable>
	
	<xsl:variable name="i18n.SignFlowInstance">Signering av �rende</xsl:variable>

	<xsl:variable name="i18n.DownloadFlowInstancePDF">H�mta �rendet i PDF format.</xsl:variable>
	<xsl:variable name="i18n.SigningLinkMessage">Ett SMS och/eller e-post meddelande har skickats till samtliga personer i listan ovan. Om de inte mottagit detta meddelande eller du vill p�minna dem s� kan du be dem bes�ka adressen nedan f�r att signera detta �rende.</xsl:variable>
	<xsl:variable name="i18n.SignFlowInstanceButton">Signera �rende</xsl:variable>
	<xsl:variable name="i18n.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Mobilephone">Mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.FlowDisabled.title">E-tj�nsten ej tillg�nglig</xsl:variable>
	<xsl:variable name="i18n.FlowDisabled.message">Den h�r tj�nsten �r st�ngd f�r underh�ll.</xsl:variable>
	
	<xsl:variable name="i18n.SigningPartyNotFound.title">Signering ej m�jlig</xsl:variable>
	<xsl:variable name="i18n.SigningPartyNotFound.message">Du kan inte signera detta �rende. Detta kan bero p� att du �r inloggad som fel person. Prova att logga ut och logga in igen.</xsl:variable>
	
	<xsl:variable name="i18n.WrongStatusContentType.title">Signering ej m�jlig</xsl:variable>
	<xsl:variable name="i18n.WrongStatusContentType.message">Detta �rende g�r inte l�ngre att signera. Detta kan bero p� att �rendet bytt status eller �ndrats. Kontakta den som bett dig signera �rendet f�r mer information.</xsl:variable>
	
	<xsl:variable name="i18n.CancelSigning">Avbryt flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.CancelSigning.Confirm">�r du s�ker p� att du vill avbryta flerpartssigneringen?</xsl:variable>
	<xsl:variable name="i18n.MayNotCancelSubmittedFlowInstance.title">Du kan ej avbyta flerpartssigneringen</xsl:variable>
	<xsl:variable name="i18n.MayNotCancelSubmittedFlowInstance.message">Detta �rende har redan skickats in och kr�ver d�rf�r att handl�ggaren �r den som avbryter flerpartsigneringen. Kontakta handl�ggaren f�r att avbryta flerpartssigneringen.</xsl:variable>
	
	
	<xsl:variable name="i18n.Help">Hj�lp</xsl:variable>
	<xsl:variable name="i18n.FlowName">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.Status">Status</xsl:variable>
	<xsl:variable name="i18n.Updated">Senast sparat</xsl:variable>
	<xsl:variable name="i18n.SiteProfile">Kommun</xsl:variable>
	
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesTitle">�renden som v�ntar p� din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2">�rende</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2.Plural">�renden</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part3">som v�ntar p� din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingForYourSignature">V�ntar p� din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">�renden som v�ntar p� din signatur</h2>
De �renden som n�gon annan har p�b�rjat med dig som signerings part. Signera genom att klicka p� "V�ntar p� signering". 
	</xsl:variable>
</xsl:stylesheet>
