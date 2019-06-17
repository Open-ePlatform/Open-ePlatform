<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/BaseFlowModule.sv.xsl"/>
	
	<xsl:include href="FlowInstanceAttachmentsModuleTemplates.xsl"/>
	
	<xsl:variable name="java.tabTitle">Handlingar</xsl:variable>
	<xsl:variable name="java.attachmentsUpdatedMessage">Uppdaterade handlingar</xsl:variable>
	
	<xsl:variable name="java.newAttachmentsUserSMS">Du har f�tt nya handlingar kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.newAttachmentsUserEmailSubject">Nya handlingar kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.newAttachmentsUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt nya handlingar kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="i18n.Description">Hit bifogas handlingar till �rendet av handl�ggarna.</xsl:variable>
	
	<xsl:variable name="i18n.validationError.RequiredField">Du m�ste fylla i f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett ok�nt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att tolka informationen fr�n din webbl�sare.</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.NewAttachment">Nya handlingar</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
	<xsl:variable name="i18n.Attachment">Handling</xsl:variable>
	<xsl:variable name="i18n.AttachFiles">L�gg till handlingar</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteFile.confirm">�r du s�ker p� att du vill ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.SubmitAttachment">L�gg till handlingar</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.Attachments">Handlingar</xsl:variable>
	<xsl:variable name="i18n.NoAttachments">Inga handlingar hittades.</xsl:variable>
	
</xsl:stylesheet>
