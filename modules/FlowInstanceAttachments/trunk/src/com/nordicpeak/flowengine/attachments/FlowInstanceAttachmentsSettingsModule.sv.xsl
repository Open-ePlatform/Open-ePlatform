<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="FlowInstanceAttachmentsSettingsModuleTemplates.xsl"/>
	
	<xsl:variable name="java.adminExtensionViewTitle">Handlingar</xsl:variable>
	<xsl:variable name="i18n.adminExtensionViewTitle">Inst�llningar f�r handlingar av e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.Settings.NotificationTitle">Aktivera notifikationer</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings.description">Inst�llningar nedan styr om till�gget "Handlingar" ska visas samt om notifikationer ska skickas.</xsl:variable>
	<xsl:variable name="i18n.Settings.Active">Visa Handlingar</xsl:variable>
	<xsl:variable name="i18n.Settings.Active.description">Markera f�r att aktivera till�gget.</xsl:variable>
	
	<xsl:variable name="i18n.Settings.sendEmail">Skicka e-postnotifiering n�r handlingar l�ggs till</xsl:variable>
	<xsl:variable name="i18n.Settings.sendSms">Skicka smsnotifiering n�r handlingar l�ggs till</xsl:variable>
	<xsl:variable name="i18n.Enabled">Inst�llningar f�r handlingar �r aktiverat</xsl:variable>
	<xsl:variable name="i18n.NotEnabled">Inst�llningar f�r handlingar �r inaktiverat</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inst�llningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings.Confirm">�r du s�ker p� att du vill ta bort alla inst�llningar f�r den h�r e-tj�nsten?</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
</xsl:stylesheet>