<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:variable name="java.Signing.user">Signerad av</xsl:variable>
	<xsl:variable name="java.Signing.ActivityProgress.State">Status</xsl:variable>
	<xsl:variable name="java.Signing.flowInstanceWasVisible">F�rhandsgranskning av �rendet var synligt</xsl:variable>
	
	<xsl:variable name="i18n.FlowDisabled">Den h�r e-tj�nsten �r inte tillg�nglig f�r tillf�llet</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.Validation.TooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.Validation.UnknownValidationErrorType" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.Validation.UnknownMessageKey" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>

	<xsl:variable name="i18n.ActivityGroup">Aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.name">Namn f�r aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus">Aktiveringstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus">M�lstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus">Nekadstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityCount">Antal aktiviteter</xsl:variable>
	
	<xsl:variable name="i18n.ActivityRound.added">P�b�rjad</xsl:variable>
	<xsl:variable name="i18n.ActivityRound.completed">Slutf�rd</xsl:variable>
	<xsl:variable name="i18n.ActivityRound.cancelled">Avbruten</xsl:variable>
	
	<xsl:variable name="i18n.Activity">Aktivitet</xsl:variable>
	<xsl:variable name="i18n.Activity.name">Namn f�r aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.shortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.Activity.description">Information om aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.responsible">Ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleUsers">Ansvariga anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleUsersFallback">Ansvariga reservanv�ndare</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleGroups">Ansvariga grupper</xsl:variable>
	<xsl:variable name="i18n.Activity.noResponsibles">Inga ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.attributeName">Attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.inverted">Inverterad</xsl:variable>
	<xsl:variable name="i18n.Activity.attributeValues">V�rden</xsl:variable>
	
	<xsl:variable name="i18n.ActivityProgress">Aktivitet</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.added">P�b�rjad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.notStarted">Ej aktiverad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.complete">�r du s�ker p� att du vill klarmarkera denna aktivitet?</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.approve">�r du s�ker p� att du vill godk�nna denna aktivitet?</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.denie">�r du s�ker p� att du vill neka denna aktivitet?</xsl:variable>
	
	<xsl:variable name="i18n.ActivityProgress.result">Utfall</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.completed">Slutf�rd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.completebutton">Klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.complete">Klarmarkerad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.approvebutton">Godk�nn</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.approved">Godk�nd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.deniebutton">Neka</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.denied">Nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.CompletingUser">F�rdigst�lld av</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.signedDate">Signeringsdatum</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.shortDescription">Beskrivning</xsl:variable>
	
	<xsl:variable name="i18n.NoActivityGroups">Inga aktivitetsgrupper finns.</xsl:variable>
	<xsl:variable name="i18n.NoActivities">Inga aktiviteter finns.</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.ShowActivity">Till aktivitet</xsl:variable>
	
</xsl:stylesheet>
