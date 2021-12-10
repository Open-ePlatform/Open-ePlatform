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
	<xsl:variable name="java.eventActivityGroupUpdated">�ndrade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDeleted">Tog bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCopied">Kopierade aktivitetsgrupp</xsl:variable>
	<xsl:variable name="java.eventActivityGroupsSorted">Sorterat aktivitetsgrupper</xsl:variable>
	<xsl:variable name="java.eventActivityAdded">Skapade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityUpdated">�ndrade aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityDeleted">Tog bort aktivitet</xsl:variable>
	<xsl:variable name="java.eventActivityCopied">Kopierade aktivitet</xsl:variable>
	
	<xsl:variable name="java.eventActivityGroupStarted">P�b�rjade aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCancelled">Avbr�t aktivitetsgrupper:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompleted">Avklarad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupCompletedMissingStatus">Automatiskt statusbyte misslyckat status "$status" saknas.</xsl:variable>
	<xsl:variable name="java.eventActivityGroupApproved">Godk�nd aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupDenied">Nekad aktivitetsgrupp:</xsl:variable>
	<xsl:variable name="java.eventActivityGroupSkipped">Hoppade �ver aktivitetsgrupp:</xsl:variable>
	
	<xsl:variable name="java.eventActivityOwnerChanged">�ndrat ansvarig f�r aktivitet $activity fr�n $from till $to</xsl:variable>
	<xsl:variable name="java.eventActivityOwnerDefault">ordinarie</xsl:variable>
	
	<xsl:variable name="java.reminderEmailPrefix">P�minnelse: </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailSubject">Nya aktiviteter f�r $activityGroup.name i �rende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupStartedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Det finns nya aktiviteter f�r $activityGroup.name i �rende $flowInstance.flowInstanceID $flow.name.&lt;/p&gt;
		
		&lt;p&gt;$activities&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa dina aktiviteter:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$myActivitiesURL"&gt;$myActivitiesURL&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	<xsl:variable name="java.activityGroupCompletedEmailSubject">Avklarad aktivitetsgrupp $activityGroup.name f�r �rende $flowInstance.flowInstanceID </xsl:variable>
	<xsl:variable name="java.activityGroupCompletedEmailMessage">
	
		&lt;p&gt;Aktivitetsgruppen $activityGroup.name f�r �rende $flowInstance.flowInstanceID $flow.name �r avklarad.&lt;/p&gt;
		
		&lt;p&gt;Avklarade aktiviteter:<br/>$activities&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.Validation.StatusNotFound.1">Status </xsl:variable>
	<xsl:variable name="i18n.Validation.StatusNotFound.2"> finns ej f�r aktuell e-tj�nst.</xsl:variable>
	
	<xsl:variable name="i18n.Validation.ResponsibleUserNotFound">Ok�nd ansvarig anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleGroupNotFound">Ok�nd ansvarig grupp</xsl:variable>
	<xsl:variable name="i18n.Validation.AssignableGroupNotFound">Ok�nd tilldelad grupp</xsl:variable>
	<xsl:variable name="i18n.Validation.AssignableUserNotFound">Ok�nd tilldelad anv�ndare</xsl:variable>
	
	<xsl:variable name="i18n.Validation.UnableToParseFile">Importfilen kunde inte l�sas in.</xsl:variable>
	<xsl:variable name="i18n.Validation.UnableToParseRequest">Ett ok�nt fel har intr�ffat.</xsl:variable>
	<xsl:variable name="i18n.Validation.FileSizeLimitExceeded">Importfilen �r st�rre �n till�ten storlek.</xsl:variable>
	<xsl:variable name="i18n.Validation.InvalidFileExtension">Felaktigt typ av fil f�r import-fil.</xsl:variable>
	<xsl:variable name="i18n.Validation.NoAttachedFile">En import-fil m�ste v�ljas.</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityNotFound">Den beg�rda aktiviteten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupNotFound">Den beg�rda aktivitetsgruppen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.Validation.AssignableRequired">Du m�ste v�lja minst en valbar grupp eller anv�ndare om du har markerat Till�t handl�ggare att byta ansvarig anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleRequired">Aktiviteten m�ste ha n�gon ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.ResponsibleFallbackRequired">Aktiviteten m�ste ha n�gon reserv eller vanlig ansvarig</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleCompletionStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika m�lstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.MultipleDenyStatusesForSameStartStatus">Det finns aktivitetsgrupper som aktiveras vid samma status men har olika nekadstatusar!</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.1">Aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.2">anv�nder statusen</xsl:variable>
	<xsl:variable name="i18n.Validation.ActivityGroupInvalidStatus.3">som inte finns i den h�r versionen av e-tj�nsten!</xsl:variable>
	<xsl:variable name="i18n.Validation.InvalidStatus">Angiven status finns inte i den h�r versionen av e-tj�nsten:</xsl:variable>
	<xsl:variable name="i18n.Validation.ResposibleAttributeNamesRequired">D� ansvarig s�ks upp via attribut s� m�ste minst ett attributnamn anges</xsl:variable>
	
	<xsl:variable name="i18n.BackToFlow">Bak�t</xsl:variable>
	<xsl:variable name="i18n.BackToActivityGroup">Bak�t</xsl:variable>
	<xsl:variable name="i18n.Move">Flytta</xsl:variable>
	<xsl:variable name="i18n.Copy">Kopiera</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	
	<xsl:variable name="i18n.ShowActivityGroup">Visa aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.AddActivityGroup">L�gg till ny aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup">�ndra aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateActivityGroup.Notifications">Notifikationer</xsl:variable>
	<xsl:variable name="i18n.SortActivityGroups">Sortera aktivitetsgrupper</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup">Ta bort aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteActivityGroup.Confirm">�r du s�ker p� att du vill ta bort aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.ExportActivityGroup">Exportera aktivitetsgrupp</xsl:variable>	
	
	<xsl:variable name="i18n.ShowActivity">Visa aktivitet</xsl:variable>
	<xsl:variable name="i18n.AddActivity">L�gg till ny aktivitet</xsl:variable>
	<xsl:variable name="i18n.UpdateActivity">�ndra aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity">Ta bort aktivitet</xsl:variable>
	<xsl:variable name="i18n.DeleteActivity.Confirm">�r du s�ker p� att du vill ta bort aktviteten</xsl:variable>
	
	<xsl:variable name="i18n.Activity.AttributeFilter">Aktivering vid attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeFilterDescription">H�r kan du st�lla in om aktiviteten bara ska anv�ndas n�r �rendet har ett attribut med ett visst v�rde.</xsl:variable>
	<xsl:variable name="i18n.Activity.useResponsibleUserAttributeName">S�k upp ansvarig anv�ndare via attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNames">Attributnamn f�r anv�ndarnamn f�r ansvarig anv�ndare (en per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.ResponsibleUserAttributeNamesDescription">Om inga anv�ndare hittas med anv�ndarnamnen fr�n attributen nedan s� kommer nedan valda reservansvariga att f� aktiviteten ist�llet.</xsl:variable>
	<xsl:variable name="i18n.Activity.useAttributeFilter">Anv�nd attributfilter f�r att aktivera aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.Activity.invert">Invertera (Om inget av v�rdena matchar attributets v�rde eller om attributet inte �r satt s� anv�nds aktiviteten)</xsl:variable>
	<xsl:variable name="i18n.Activity.AttributeValues">V�rden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddress">Funktionsbrevl�da</xsl:variable>
	<xsl:variable name="i18n.Activity.globalEmailAddressHelp">F�r notifikation om startad aktivitet ut�ver ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.onlyUseGlobalNotifications">Skicka notifikationer endast till funktionsbrevl�dan</xsl:variable>
	<xsl:variable name="i18n.Activity.StartedNotificationDisabled">OBS Aktivitetsgruppen �r inte inst�lld p� att skicka p�b�rjatnotifikationer men p�minnelser, automatiska och manuella, kan fortfarande anv�ndas.</xsl:variable>
	<xsl:variable name="i18n.Activity.shortDescriptionHelp">Kort beskrivning av aktiviteten i listan med aktiviteter. Via f�ltet nedan kan en valfri str�ng anges med b�de fast text och $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.Activity.descriptionHelp">Information om vad ansvarig ska utf�ra f�r denna aktivitet. Du f�r anv�nda $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.Activity.showFlowInstance">Visa f�rhandsgranskning av hela �rendet</xsl:variable>
	<xsl:variable name="i18n.Activity.showPDFDownloadActivation">Till�t aktivitets�gare att ladda ner hela �rendets PDF</xsl:variable>
	<xsl:variable name="i18n.Activity.requireSigning">Kr�v signering vid klarmarkera / godk�nn / neka</xsl:variable>
	<xsl:variable name="i18n.Activity.requireComment">Kr�v att kommentar anges</xsl:variable>
	<xsl:variable name="i18n.Activity.allowManagersToAssignOwner">Till�t handl�ggare att byta ansvarig anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Activity.assignableUsers">Valbara anv�ndare vid byte av ansvarig</xsl:variable>
	<xsl:variable name="i18n.Activity.assignableGroups">Valbara grupper vid byte av ansvarig</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.useApproveDeny">Anv�nd godk�nn / neka ist�llet f�r klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.useCustomApprovedText">Anv�nd annat namn f�r klarmarkerad / godk�nd / nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.approvedText">Namn f�r klarmarkerad/godk�nd</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.deniedText">Namn f�r nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.allowSkip">Hoppa �ver / byt till m�lstatus �ven om ingen aktivitet startas (endast om ingen grupp alls startas/�r ig�ng)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.allowRestarts">Till�t omstart av aktiviteter</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.restartInfo">Om �rendets status �ndras tillbaka till aktiveringsstatus kommer aktiviteter att startas om oavstett hur inst�llning f�r omstart �r satta h�r</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.onlyRestartIfActivityChanges">Till�t omstart endast om det blir skillnad p� startade aktiviteter (pga attribut)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupStartedEmail">Skicka e-postnotifiering vid p�b�rjad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailSubject">Rubrik f�r notifiering till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupStartedEmailMessage">Meddelandetext f�r notifieringar till aktivitetsansvariga</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.sendActivityGroupCompletedEmail">Skicka e-postnotifiering vid avklarad aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAttachPDF">Bifoga PDF-sammanst�llning f�r aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAttachFlowInstancePDF">Bifoga �rendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailSubject">Rubrik f�r notifiering</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailMessage">Meddelandetext f�r notifiering</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityGroupCompletedEmailAddresses">E-postadresser f�r notifiering om avklarad aktivitetsgrupp (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.appendCommentsToExternalMessages">Kopiera aktivitetskommentarer till meddelanden p� �rendet</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus2">Aktivitetsgruppen p�b�rjas n�r �rendet hamnar i denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus2">Om alla aktiviteter i gruppen �r klarmarkerade/godk�nda s� f�r �rendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus2">Om n�gon aktivitet i gruppen inte blir godk�nd s� f�r �rendet denna status.</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.reminderAfterXDays">Skicka automatisk p�minnelse om aktivitet inte �r klarmarkerad inom x dagar</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.suppressChangeStatusManagerNotifications">F�rhindra notifikationer till handl�ggare vid byte av status</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.importUsers">Importera inte anv�ndare</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.importUsers.description">Markera f�r att inte importera anv�ndare</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.importGroups">Importera inte grupper</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.importGroups.description">Markera f�r att inte importera grupper</xsl:variable>
	
	<xsl:variable name="i18n.ImportActivityGroup.title">Importera aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ImportActivityGroup.description">Anv�nd formul�ret nedan f�r att importera en aktivitetsgrupp. Filen du v�ljer beh�ver vara av typen oeactgroup.</xsl:variable>
	<xsl:variable name="i18n.selectActivityGroupFile">V�lj fil</xsl:variable>
	<xsl:variable name="i18n.ImportActivityGroup.submit">Importera</xsl:variable>
	<xsl:variable name="i18n.ImportActivityGroup">Importera aktivitetsgrupp</xsl:variable>
	
	<xsl:variable name="i18n.TagsTable.Description.Email">F�ljande taggar kan anv�ndas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.TagDescription">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.Tag.ActivityGroup.name">Aktivitetsgruppnamn</xsl:variable>
	<xsl:variable name="i18n.Tag.Activities">Aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.MyActivities">L�nk till mina aktiviteter</xsl:variable>
	<xsl:variable name="i18n.Tag.FlowInstance.flowInstanceID">�rendenummer</xsl:variable>
	<xsl:variable name="i18n.Tag.Flow.name">E-tj�nstnamn</xsl:variable>
	
	<xsl:variable name="i18n.ResponsibleUser.fallback">Reserv</xsl:variable>
	
	<xsl:variable name="i18n.ToggleTexts">[Visa/d�lj texter]</xsl:variable>
	<xsl:variable name="i18n.row">rad</xsl:variable>
	
	<xsl:variable name="i18n.ActivityGroup.hideFlowinstanceIDInPDF">D�lj �rendenummer i aktivitetgruppens PDF</xsl:variable>
	
</xsl:stylesheet>
