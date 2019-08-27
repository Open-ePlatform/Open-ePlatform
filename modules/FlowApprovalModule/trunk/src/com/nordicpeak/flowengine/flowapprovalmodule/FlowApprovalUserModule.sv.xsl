<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowApprovalUserModuleTemplates.xsl"/>
	
	<xsl:variable name="java.userMenuTabTitle">Mina aktiviteter</xsl:variable>
	
	<xsl:variable name="i18n.PendingActivities">Ej slutförda aktiviteter</xsl:variable>
	<xsl:variable name="i18n.CompletedActivities">Slutförda aktiviteter</xsl:variable>
	<xsl:variable name="i18n.ShowPending">Visa ej slutförda</xsl:variable>
	<xsl:variable name="i18n.ShowCompleted">Visa slutförda</xsl:variable>
	<xsl:variable name="i18n.Back">Bakåt</xsl:variable>
	
	<xsl:variable name="i18n.Help">Hjälp</xsl:variable>
	<xsl:variable name="i18n.PendingActivities.help">
		<h2 class="h1 full" data-icon-before="?">Ej slutförda aktiviteter</h2>
		Här visas ej slutförda aktiviteter du har.
	</xsl:variable>
	<xsl:variable name="i18n.CompletedActivities.help">
		<h2 class="h1 full" data-icon-before="?">Slutförda aktiviteter</h2>
		Här visas slutförda aktiviteter du har.
	</xsl:variable>
	
	<xsl:variable name="i18n.ActivityProgress.complete">Klarmarkerad</xsl:variable>
	<xsl:variable name="i18n.ActivityProgress.userDescription">Information från ärendet</xsl:variable>
	
	<xsl:variable name="i18n.Flow.name">E-tjänst</xsl:variable>
	<xsl:variable name="i18n.FlowInstance.flowInstanceID">Ärendenummer</xsl:variable>
	
</xsl:stylesheet>
