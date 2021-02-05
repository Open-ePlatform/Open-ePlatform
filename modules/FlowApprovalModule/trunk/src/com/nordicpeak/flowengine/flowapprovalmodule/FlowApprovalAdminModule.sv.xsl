<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowApprovalAdminModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Aktivitetsgrupper</xsl:variable>
	<xsl:variable name="java.copySuffix"> (kopia)</xsl:variable>
	<xsl:variable name="java.signaturesFilename">signaturer</xsl:variable>
	<xsl:variable name="java.pdfSignatureAttachment">Signatur</xsl:variable>
	<xsl:variable name="java.pdfSigningDataAttachment">Signeringsunderlag</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupAdded">Skapade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupUpdated">Ändrade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDeleted">Tog bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCopied">Kopierade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupsSorted">Sorterat aktivitetsgrupper</xsl:variable>
	<xsl:variable name="java.eventActivityAdded">Skapade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityUpdated">Ändrade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityDeleted">Tog bort aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityCopied">Kopierade aktivitet</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupStarted">Påbörjade aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCancelled">Avbröt aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompleted">Avklarad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompletedMissingStatus">Automatiskt statusbyte misslyckat status "$status" saknas.</xsl:variable>
	<xsl:variable name="java.eventActivityGroupApproved">Godkänd aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDenied">Nekad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupSkipped">Hoppade över aktivitetsgrupp:</xsl:variable>
	
	<xsl:variable name="java.reminderEmailPrefix">Påminnelse: </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailSubject">Nya aktiviteter för $activityGroup.name i ärende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Det finns nya aktiviteter för $activityGroup.name i ärende $flowInstance.flowInstanceID $flow.name.&lt;/p&gt;
		
		&lt;p&gt;$activities&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa dina aktiviteter:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$myActivitiesURL"&gt;$myActivitiesURL&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	<xsl:variable name="java.activityGroupCompletedEmailSubject">Avklarad aktivitetsgrupp $activityGroup.name för ärende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupCompletedEmailMessage">
	
		&lt;p&gt;Aktivitetsgruppen $activityGroup.name för ärende $flowInstance.flowInstanceID $flow.name är avklarad.&lt;/p&gt;
		
		&lt;p&gt;Avklarade aktiviteter:<br/>$activities&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.Validation.ActivityNotFound">Den begärda aktiviteten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupNotFound">Den begärda aktivitetsgruppen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleRequired">Aktiviteten måste ha någon ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleFallbackRequired">Aktiviteten måste ha någon reserv eller vanlig ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleCompletionStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika målstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleDenyStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika nekadstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.1">Aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.2">använder statusen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.3">som inte finns i den här versionen av e-tjänsten!</xsl:variable>
	<xsl:variable name="i18n.Validation.InvalidStatus">Angiven status finns inte i den här versionen av e-tjänsten:</xsl:variable>
	
	<xsl:variable name="i18n.BackToFlow">Bakåt</xsl:variable>
	<xsl:variable name="i18n.BackToActivityGroup">Bakåt</xsl:variable>
	<xsl:variable name="i18n.Move">Flytta</xsl:variable>
	<xsl:variable name="i18n.Copy">Kopiera</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivityGroup">Visa aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.AddActivityGroup">Lägg till ny aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup">Ändra aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup.Notifications">Notifikationer</xsl:variable>
	<xsl:variable name="i18n.SortActivityGroups">Sortera aktivitetsgrupper</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup">Ta bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup.Confirm">Är du säker på att du vill ta bort aktivitetsgruppen</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivity">Visa aktivitet</xsl:variable>
	<xsl:variable name="i18n.AddActivity">Lägg till ny aktivitet</xsl:variable>
	<xsl:variable name="i18n.UpdateActivity">Ändra aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity">Ta bort aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity.Confirm">Är du säker på att du vill ta bort aktviteten</xsl:variable>
	
	<xsl:variable name="i18n.Activity.AttributeFilter">Aktivering vid attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeFilterDescription">Här kan du ställa in om aktiviteten bara ska användas när ärendet har ett attribut med ett visst värde.</xsl:variable>
	<xsl:variable name="i18n.Activity.useResponsibleUserAttributeName">Sök upp ansvarig användare via attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNames">Attributnamn för användarnamn för ansvarig användare (en per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNamesDescription">Om inga användare hittas med användarnamnen från attributen nedan så kommer nedan valda reservansvariga att få aktiviteten istället.</xsl:variable>
	<xsl:variable name="i18n.Activity.useAttributeFilter">Använd attributfilter för att aktivera aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.Activity.invert">Invertera (Om inget av värderna matchar attributets värde eller om attributet inte är satt så används aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeValues">Värden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddress">Funktionsbrevlåda</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddressHelp">Får notifikation om startad aktivitet utöver ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.onlyUseGlobalNotifications">Skicka notifikationer endast till funktionsbrevlådan</xsl:variable>
	<xsl:variable name="i18n.Activity.StartedNotificationDisabled">OBS Aktivitetsgruppen är inte inställd på att skicka påbörjatnotifikationer men påminnelser, automatiska och manuella, kan fortfarande användas.</xsl:variable>
	<xsl:variable name="i18n.Activity.shortDescriptionHelp">Kort beskrivning av aktiviteten i listan med aktiviteter. Via fältet nedan kan en valfri sträng anges med både fast text och $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.Activity.descriptionHelp">Information om vad ansvarig ska utföra för denna aktivitet. Du får använda $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.Activity.showFlowInstance">Visa förhandsgranskning av hela ärendet</xsl:variable>
	<xsl:variable name="i18n.Activity.requireSigning">Kräv signering vid klarmarkera / godkänn / neka</xsl:variable>
	<xsl:variable name="i18n.Activity.requireComment">Kräv att kommentar anges</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.useApproveDeny">Använd godkänn / neka istället för klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.useCustomApprovedText">Använd annat namn för klarmarkerad / godkänd / nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.approvedText">Namn för klarmarkerad/godkänd</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.deniedText">Namn för nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.allowSkip">Hoppa över / byt till målstatus även om ingen aktivitet startas (endast om ingen grupp alls startas/är igång)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.allowRestarts">Tillåt omstart av aktiviteter</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.onlyRestartIfActivityChanges">Tillåt omstart endast om det blir skillnad på startade aktiviteter (pga attribut)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupStartedEmail">Skicka e-postnotifiering vid påbörjad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailSubject">Rubrik för notifiering till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailMessage">Meddelandetext för notifieringar till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupCompletedEmail">Skicka e-postnotifiering vid avklarad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAttachPDF">Bifoga PDF-sammanställning för aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAttachFlowInstancePDF">Bifoga ärendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailSubject">Rubrik för notifiering</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailMessage">Meddelandetext för notifiering</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAddresses">E-postadresser för notifiering om avklarad aktivitetsgrupp (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.appendCommentsToExternalMessages">Kopiera aktivitetskommentarer till meddelanden på ärendet</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus2">Aktivitetsgruppen påbörjas när ärendet hamnar i denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus2">Om alla aktiviteter i gruppen är klarmarkerade/godkända så får ärendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus2">Om någon aktivitet i gruppen inte blir godkänd så får ärendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.reminderAfterXDays">Skicka automatisk påminnelse om aktivitet inte är klarmarkerad inom x dagar</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.suppressChangeStatusManagerNotifications">Förhindra notifikationer till handläggare vid byte av status</xsl:variable>
	
	<xsl:variable name="i18n.TagsTable.Description.Email">Följande taggar kan användas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.TagDescription">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.Tag.ActivityGroup.name">Aktivitetsgruppnamn</xsl:variable>
	<xsl:variable name="i18n.Tag.Activities">Aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.MyActivities">Länk till mina aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.Manager.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Tag.Manager.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Tag.FlowInstance.flowInstanceID">Ärendenummer</xsl:variable>
	<xsl:variable name="i18n.Tag.Flow.name">E-tjänstnamn</xsl:variable>
	
	<xsl:variable name="i18n.ResponsibleUser.fallback">Reserv</xsl:variable>
	
	<xsl:variable name="i18n.ToggleTexts">[Visa/dölj texter]</xsl:variable>
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
</xsl:stylesheet>
