<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowApprovalAdminModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Aktivitetsgrupper</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupAdded">Skapade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupUpdated">�ndrade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupsSorted">Sorterat aktivitetsgrupper</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDeleted">Tog bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityAdded">Skapade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityUpdated">�ndrade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityDeleted">Tog bort aktivitet</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupStarted">P�b�rjade aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompleted">Avklarad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupApproved">Godk�nd aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDenied">Nekad aktivitetsgrupp:</xsl:variable>
	
	<xsl:variable name="java.reminderEmailPrefix">P�minnelse: </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailSubject">Nya aktiviteter f�r $activityGroup.name i �rende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Det finns nya aktiviteter f�r $activityGroup.name  i �rende $flowInstance.flowInstanceID $flow.name.&lt;/p&gt;
		
		&lt;p&gt;$activities&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa dina aktiviteter:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$myActivitiesURL"&gt;$myActivitiesURL&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.Validation.TooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.Validation.UnknownValidationErrorType" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.Validation.UnknownMessageKey" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	
	<xsl:variable name="i18n.Validation.ActivityNotFound">Den beg�rda aktiviteten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupNotFound">Den beg�rda aktivitetsgruppen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleRequired">Aktiviteten m�ste ha n�gon ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleFallbackRequired">Aktiviteten m�ste ha n�gon reserv eller vanlig ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleCompletionStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika m�lstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleDenyStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika nekadstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.1">Aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.2">anv�nder statusen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.3">som inte finns i e-tj�nsten!</xsl:variable>
	<xsl:variable name="i18n.Validation.InvalidStatus">Angiven status finns inte i den h�r versionen av e-tj�nsten:</xsl:variable>
	
	<xsl:variable name="i18n.BackToFlow">Bak�t</xsl:variable>
	<xsl:variable name="i18n.BackToActivityGroup">Bak�t</xsl:variable>
	<xsl:variable name="i18n.Move">Flytta</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivityGroup">Visa aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.AddActivityGroup">L�gg till ny aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup">�ndra aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.SortActivityGroups">Sortera aktivitetsgrupper</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup">Ta bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup.Confirm">�r du s�ker p� att du vill ta bort aktivitetsgruppen</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivity">Visa aktivitet</xsl:variable>
	<xsl:variable name="i18n.AddActivity">L�gg till ny aktivitet</xsl:variable>
	<xsl:variable name="i18n.UpdateActivity">�ndra aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity">Ta bort aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity.Confirm">�r du s�ker p� att du vill ta bort aktviteten</xsl:variable>
	
	<xsl:variable name="i18n.Activity.AttributeFilter">Aktivering vid attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeFilterDescription">H�r kan du st�lla in om aktiviteten bara ska anv�ndas n�r �rendet har ett attribut med ett visst v�rde.</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeName">Attributnamn (l�mna tom f�r att alltid anv�nda aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeName">Attributnamn f�r anv�ndarnamn f�r ansvarig anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNameDescription">Om ingen anv�ndare hittas med anv�ndarnamnet i attributet s� kommer anv�ndare valda ovan som reserv att f� aktiviteten ist�llet.</xsl:variable>
	<xsl:variable name="i18n.Activity.invert">Invertera (Om inget av v�rderna matchar attributets v�rde eller om attributet inte �r satt s� anv�nds aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeValues">V�rden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddress">Funktionsbrevl�da</xsl:variable>
	<xsl:variable name="i18n.Activity.descriptionHelp">Information om vad ansvarig ska utf�ra f�r denna aktivitet. Du f�r anv�nda $attribute{} taggar.</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.userDescriptionHelp">Kort beskrivning av �rendet f�r listan av aktiviteter. Via f�ltet nedan kan en valfri str�ng anges med b�de fast text och $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.useApproveDeny">Anv�nd godk�nn / neka ist�llet f�r klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupStartedEmail">Skicka epost notifiering vid p�b�rjad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailSubject">Rubrik f�r notifiering till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailMessage">Meddelandetext f�r notifieringar till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.appendCommentsToExternalMessages">Kopiera aktivitetskommentarer till meddelanden p� �rendet</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus2">Aktivitetsgruppen p�b�rjas n�r �rendet hamnar i denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus2">Om alla aktiviteter i gruppen �r klarmarkerade/godk�nda s� f�r �rendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus2">Om n�gon aktivitet i gruppen inte blir godk�nd s� f�r �rendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.reminderAfterXDays">Skicka automatisk p�minnelse om aktivitet inte �r klarmarkerad inom x dagar</xsl:variable>
	
	<xsl:variable name="i18n.TagsTable.Description.Email">F�ljande taggar kan anv�ndas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.TagDescription">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.Tag.ActivityGroup.name">Aktivitetsgruppnamn</xsl:variable>
	<xsl:variable name="i18n.Tag.Activities">Aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.MyActivities">L�nk till mina aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.Manager.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Tag.Manager.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Tag.FlowInstance.flowInstanceID">�rendenummer</xsl:variable>
	<xsl:variable name="i18n.Tag.Flow.name">E-tj�nstnamn</xsl:variable>
	
	<xsl:variable name="i18n.ResponsibleUser.fallback">Reserv</xsl:variable>
	<xsl:variable name="i18n.ResponsibleUser.ToggleFallback">V�xla reserv ansvarig</xsl:variable>
	
	<xsl:variable name="i18n.ToggleTexts">[Visa/d�lj texter]</xsl:variable>
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
</xsl:stylesheet>
