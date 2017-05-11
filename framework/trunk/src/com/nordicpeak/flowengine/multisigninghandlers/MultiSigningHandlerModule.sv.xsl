<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="MultiSigningHandlerModuleTemplates.xsl"/>

	<xsl:variable name="java.signMessage">$poster har bett dig signera ärendet nedan.</xsl:variable>	
	<xsl:variable name="java.signedMessage">Du skrev under detta ärende den $signed.</xsl:variable>
	
	<xsl:variable name="i18n.SocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.Signature">Signerat</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.SigningStatus.Description">Ditt ärende väntar på att en eller flera personer ska signera det. I tabellen nedan ser du aktuell status gällande signeringen av detta ärende.</xsl:variable>
	
	<xsl:variable name="i18n.SignFlowInstance">Signering av ärende</xsl:variable>

	<xsl:variable name="i18n.DownloadFlowInstancePDF">Hämta ärendet i PDF format.</xsl:variable>
	<xsl:variable name="i18n.SigningLinkMessage">Ett SMS och/eller e-post meddelande har skickats till samtliga personer i listan ovan. Om de inte mottagit detta meddelande eller du vill påminna dem så kan du be dem besöka adressen nedan för att signera detta ärende.</xsl:variable>
	<xsl:variable name="i18n.SignFlowInstanceButton">Signera ärende</xsl:variable>
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Mobilephone">Mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.FlowDisabled.title">E-tjänsten ej tillgänglig</xsl:variable>
	<xsl:variable name="i18n.FlowDisabled.message">Den här tjänsten är stängd för underhåll.</xsl:variable>
	
	<xsl:variable name="i18n.SigningPartyNotFound.title">Signering ej möjlig</xsl:variable>
	<xsl:variable name="i18n.SigningPartyNotFound.message">Du kan inte signera detta ärende. Detta kan bero på att du är inloggad som fel person. Prova att logga ut och logga in igen.</xsl:variable>
	
	<xsl:variable name="i18n.WrongStatusContentType.title">Signering ej möjlig</xsl:variable>
	<xsl:variable name="i18n.WrongStatusContentType.message">Detta ärende går inte längre att signera. Detta kan bero på att ärendet bytt status eller ändrats. Kontakta den som bett dig signera ärendet för mer information.</xsl:variable>
	
	<xsl:variable name="i18n.CancelSigning">Avbryt flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.CancelSigning.Confirm">Är du säker på att du vill avbryta flerpartssigneringen?</xsl:variable>
	<xsl:variable name="i18n.MayNotCancelSubmittedFlowInstance.title">Du kan ej avbyta flerpartssigneringen</xsl:variable>
	<xsl:variable name="i18n.MayNotCancelSubmittedFlowInstance.message">Detta ärende har redan skickats in och kräver därför att handläggaren är den som avbryter flerpartsigneringen. Kontakta handläggaren för att avbryta flerpartssigneringen.</xsl:variable>
	
	
	<xsl:variable name="i18n.Help">Hjälp</xsl:variable>
	<xsl:variable name="i18n.FlowName">E-tjänst</xsl:variable>
	<xsl:variable name="i18n.Status">Status</xsl:variable>
	<xsl:variable name="i18n.Updated">Senast sparat</xsl:variable>
	<xsl:variable name="i18n.SiteProfile">Kommun</xsl:variable>
	
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesTitle">Ärenden som väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2">ärende</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2.Plural">ärenden</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part3">som väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingForYourSignature">Väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Ärenden som väntar på din signatur</h2>
De ärenden som någon annan har påbörjat med dig som signerings part. Signera genom att klicka på "Väntar på signering". 
	</xsl:variable>
</xsl:stylesheet>
