<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowApprovalUserModuleTemplates.xsl"/>
	
	<xsl:variable name="java.userMenuTabTitle">Mina aktiviteter</xsl:variable>
	<xsl:variable name="java.signingMessage">Markera aktivitet $activity.name (ID $activityProgress.ID) som $state f�r �rende $flowInstance.ID. Aktiviteten har f�ljande unika nyckel: $hash</xsl:variable>
	
	<xsl:variable name="i18n.PendingActivities">Ej slutf�rda aktiviteter</xsl:variable>
	<xsl:variable name="i18n.CompletedActivities">Slutf�rda aktiviteter</xsl:variable>
	<xsl:variable name="i18n.ShowPending">Visa ej slutf�rda</xsl:variable>
	<xsl:variable name="i18n.ShowCompleted">Visa slutf�rda</xsl:variable>
	<xsl:variable name="i18n.Back">Bak�t</xsl:variable>
	<xsl:variable name="i18n.DownloadPDF">H�mta PDF</xsl:variable>
	<xsl:variable name="i18n.DownloadPDF.title">H�mta senaste PDF-kvittensen</xsl:variable>
	
	
	<xsl:variable name="i18n.Help">Hj�lp</xsl:variable>
	<xsl:variable name="i18n.PendingActivities.help">
		<h2 class="h1 full" data-icon-before="?">Ej slutf�rda aktiviteter</h2>
		H�r visas ej slutf�rda aktiviteter du har.
	</xsl:variable>
	<xsl:variable name="i18n.CompletedActivities.help">
		<h2 class="h1 full" data-icon-before="?">Slutf�rda aktiviteter</h2>
		H�r visas slutf�rda aktiviteter du har.
	</xsl:variable>
	
	<xsl:variable name="i18n.Flow.name">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.FlowInstance.flowInstanceID">�rendenummer</xsl:variable>
	<xsl:variable name="i18n.noAnsweredQueriesInThisStep">Det finns inga besvarade fr�gor i detta steg.</xsl:variable>
	
	<xsl:variable name="i18n.SignActivity">Signera aktivitet</xsl:variable>
	<xsl:variable name="i18n.SignActivity.Back">Avbryt signering</xsl:variable>
	
	<xsl:variable name="i18n.MessageTemplates.choose">V�lj kommentarsmall</xsl:variable>
	
</xsl:stylesheet>
