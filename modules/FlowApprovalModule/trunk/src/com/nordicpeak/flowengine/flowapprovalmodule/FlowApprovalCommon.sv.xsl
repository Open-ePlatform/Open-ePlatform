<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:variable name="java.Signing.user">Signerad av</xsl:variable>
	<xsl:variable name="java.Signing.ActivityProgress.State">Status</xsl:variable>
	<xsl:variable name="java.Signing.flowInstanceWasVisible">Förhandsgranskning av ärendet var synligt</xsl:variable>
	
	<xsl:variable name="i18n.FlowDisabled">Den här e-tjänsten är inte tillgänglig för tillfället</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.Validation.TooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.Validation.UnknownValidationErrorType" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.Validation.UnknownMessageKey" select="'Ett okänt valideringsfel har uppstått.'"/>

	<xsl:variable name="i18n.ActivityGroup">Aktivitetsgrupp</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.name">Namn för aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.startStatus">Aktiveringstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.completeStatus">Målstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.denyStatus">Nekadstatus</xsl:variable>
	<xsl:variable name="i18n.ActivityGroup.activityCount">Antal aktiviteter</xsl:variable>
	
	<xsl:variable name="i18n.ActivityRound.added">Påbörjad</xsl:variable>
	<xsl:variable name="i18n.ActivityRound.completed">Slutförd</xsl:variable>
	<xsl:variable name="i18n.ActivityRound.cancelled">Avbruten</xsl:variable>
	
	<xsl:variable name="i18n.Activity">Aktivitet</xsl:variable>
	<xsl:variable name="i18n.Activity.name">Namn för aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.shortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.Activity.description">Information om aktiviteten</xsl:variable>
	<xsl:variable name="i18n.Activity.responsible">Ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleUsers">Ansvariga användare</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleUsersFallback">Ansvariga reservanvändare</xsl:variable>
	<xsl:variable name="i18n.Activity.responsibleGroups">Ansvariga grupper</xsl:variable>
	<xsl:variable name="i18n.Activity.noResponsibles">Inga ansvariga</xsl:variable>
	<xsl:variable name="i18n.Activity.attributeName">Attribut</xsl:variable>
	<xsl:variable name="i18n.Activity.inverted">Inverterad</xsl:variable>
	<xsl:variable name="i18n.Activity.attributeValues">Värden</xsl:variable>
	
	<xsl:variable name="i18n.ActivityProgress">Aktivitet</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.added">Påbörjad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.notStarted">Ej aktiverad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.complete">Är du säker på att du vill klarmarkera denna aktivitet?</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.approve">Är du säker på att du vill godkänna denna aktivitet?</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.confirm.denie">Är du säker på att du vill neka denna aktivitet?</xsl:variable>
	
	<xsl:variable name="i18n.ActivityProgress.result">Utfall</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.completed">Slutförd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.completebutton">Klarmarkera</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.complete">Klarmarkerad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.approvebutton">Godkänn</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.approved">Godkänd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.deniebutton">Neka</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.denied">Nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.CompletingUser">Färdigställd av</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.signedDate">Signeringsdatum</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.shortDescription">Beskrivning</xsl:variable>
	
	<xsl:variable name="i18n.NoActivityGroups">Inga aktivitetsgrupper finns.</xsl:variable>
	<xsl:variable name="i18n.NoActivities">Inga aktiviteter finns.</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.ShowActivity">Till aktivitet</xsl:variable>
	
</xsl:stylesheet>
