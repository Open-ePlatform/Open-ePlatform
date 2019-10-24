<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowApprovalAdminModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Aktivitetsgrupper</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupAdded">Skapade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupUpdated">Ändrade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupsSorted">Sorterat aktivitetsgrupper</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDeleted">Tog bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityAdded">Skapade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityUpdated">Ändrade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityDeleted">Tog bort aktivitet</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupStarted">Påbörjade aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompleted">Avklarad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupApproved">Godkänd aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDenied">Nekad aktivitetsgrupp:</xsl:variable>
	
	<xsl:variable name="java.reminderEmailPrefix">Påminnelse: </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailSubject">Nya aktiviteter för $activityGroup.name i ärende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Det finns nya aktiviteter för $activityGroup.name  i ärende $flowInstance.flowInstanceID $flow.name.&lt;/p&gt;
		
		&lt;p&gt;$activities&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa dina aktiviteter:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$myActivitiesURL"&gt;$myActivitiesURL&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.Validation.TooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.Validation.UnknownValidationErrorType" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.Validation.UnknownMessageKey" select="'Ett okänt valideringsfel har uppstått.'"/>
	
	<xsl:variable name="i18n.Validation.ActivityNotFound">Den begärda aktiviteten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupNotFound">Den begärda aktivitetsgruppen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleRequired">Aktiviteten måste ha någon ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleFallbackRequired">Aktiviteten måste ha någon reserv eller vanlig ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleCompletionStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika målstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleDenyStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika nekadstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.1">Aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.2">använder statusen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.3">som inte finns i e-tjänsten!</xsl:variable>
	<xsl:variable name="i18n.Validation.InvalidStatus">Angiven status finns inte i den här versionen av e-tjänsten:</xsl:variable>
	
	<xsl:variable name="i18n.BackToFlow">Bakåt</xsl:variable>
	<xsl:variable name="i18n.BackToActivityGroup">Bakåt</xsl:variable>
	<xsl:variable name="i18n.Move">Flytta</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivityGroup">Visa aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.AddActivityGroup">Lägg till ny aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup">Ändra aktivitetsgrupp</xsl:variable>
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
	<xsl:variable name="i18n.Activity.AttributeName">Attributnamn (lämna tom för att alltid använda aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeName">Attributnamn för användarnamn för ansvarig användare</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNameDescription">Om ingen användare hittas med användarnamnet i attributet så kommer användare valda ovan som reserv att få aktiviteten istället.</xsl:variable>
	<xsl:variable name="i18n.Activity.invert">Invertera (Om inget av värderna matchar attributets värde eller om attributet inte är satt så används aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeValues">Värden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddress">Funktionsbrevlåda</xsl:variable>
	<xsl:variable name="i18n.Activity.descriptionHelp">Information om vad ansvarig ska utföra för denna aktivitet. Du får använda $attribute{} taggar.</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.userDescriptionHelp">Kort beskrivning av ärendet för listan av aktiviteter. Via fältet nedan kan en valfri sträng anges med både fast text och $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.useApproveDeny">Använd godkänn / neka istället för klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupStartedEmail">Skicka epost notifiering vid påbörjad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailSubject">Rubrik för notifiering till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailMessage">Meddelandetext för notifieringar till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.appendCommentsToExternalMessages">Kopiera aktivitetskommentarer till meddelanden på ärendet</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus2">Aktivitetsgruppen påbörjas när ärendet hamnar i denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus2">Om alla aktiviteter i gruppen är klarmarkerade/godkända så får ärendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus2">Om någon aktivitet i gruppen inte blir godkänd så får ärendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.reminderAfterXDays">Skicka automatisk påminnelse om aktivitet inte är klarmarkerad inom x dagar</xsl:variable>
	
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
	<xsl:variable name="i18n.ResponsibleUser.ToggleFallback">Växla reserv ansvarig</xsl:variable>
	
	<xsl:variable name="i18n.ToggleTexts">[Visa/dölj texter]</xsl:variable>
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
</xsl:stylesheet>
