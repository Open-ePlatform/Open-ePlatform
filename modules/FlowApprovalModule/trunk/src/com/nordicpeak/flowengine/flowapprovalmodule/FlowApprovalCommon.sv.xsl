<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

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
	<xsl:variable name="i18n.ActivityProgress.completed">Slutf�rd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.approved">Godk�nd</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.denied">Nekad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.CompletingUser">Person</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.signedDate">Signeringsdatum</xsl:variable>
	
	<xsl:variable name="i18n.NoActivityGroups">Inga aktivitetsgrupper finns.</xsl:variable>
	<xsl:variable name="i18n.NoActivities">Inga aktiviteter finns.</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.ShowActivity">Till aktivitet</xsl:variable>
	
</xsl:stylesheet>
