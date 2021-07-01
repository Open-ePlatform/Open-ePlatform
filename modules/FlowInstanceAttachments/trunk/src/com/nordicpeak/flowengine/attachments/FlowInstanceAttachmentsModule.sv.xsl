<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/BaseFlowModule.sv.xsl"/>
	
	<xsl:include href="FlowInstanceAttachmentsModuleTemplates.xsl"/>
	
	<xsl:variable name="java.tabTitle">Handlingar</xsl:variable>
	<xsl:variable name="java.adminExtensionViewTitle">Handlingar</xsl:variable>
	<xsl:variable name="java.attachmentsUpdatedMessage">Uppdaterade handlingar</xsl:variable>
	
	<xsl:variable name="java.newAttachmentsUserSMS">Du har fått nya handlingar kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.newAttachmentsUserEmailSubject">Nya handlingar kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.newAttachmentsUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har fått nya handlingar kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="i18n.Description">Hit bifogas handlingar till ärendet av handläggarna.</xsl:variable>
	
	<xsl:variable name="i18n.adminExtensionViewTitle">Inställningar för handlingar av e-tjänst</xsl:variable>
	<xsl:variable name="i18n.Settings.NotificationTitle">Aktivera notifikationer</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings.description">Inställningar nedan styr om tillägget "Handlingar" ska visas samt om notifikationer ska skickas.</xsl:variable>
	<xsl:variable name="i18n.Settings.Active">Visa Handlingar</xsl:variable>
	<xsl:variable name="i18n.Settings.Active.description">Markera för att aktivera tillägget.</xsl:variable>
	
	<xsl:variable name="i18n.Settings.sendEmail">Skicka e-postnotifiering när handlingar läggs till</xsl:variable>
	<xsl:variable name="i18n.Settings.sendSms">Skicka smsnotifiering när handlingar läggs till</xsl:variable>
	<xsl:variable name="i18n.Enabled">Inställningar för handlingar är aktiverat</xsl:variable>
	<xsl:variable name="i18n.NotEnabled">Inställningar för handlingar är inaktiverat</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">Ändra inställningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inställningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings.Confirm">Är du säker på att du vill ta bort alla inställningar för den här e-tjänsten?</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.validationError.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att tolka informationen från din webbläsare.</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.NewAttachment">Nya handlingar</xsl:variable>
	<xsl:variable name="i18n.Close">Stäng</xsl:variable>
	<xsl:variable name="i18n.Attachment">Handling</xsl:variable>
	<xsl:variable name="i18n.AttachFiles">Lägg till handlingar</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteFile.confirm">Är du säker på att du vill ta bort</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.SubmitAttachment">Lägg till handlingar</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.Attachments">Handlingar</xsl:variable>
	<xsl:variable name="i18n.NoAttachments">Inga handlingar hittades.</xsl:variable>
	
</xsl:stylesheet>
