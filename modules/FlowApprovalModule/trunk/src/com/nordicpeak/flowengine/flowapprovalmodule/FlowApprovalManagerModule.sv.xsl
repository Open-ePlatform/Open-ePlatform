<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowApprovalManagerModuleTemplates.xsl"/>
	
	<xsl:variable name="java.tabTitle">Aktiviteter</xsl:variable>
	
	<xsl:variable name="i18n.Activity.reminders">Påminnelser</xsl:variable>
	<xsl:variable name="i18n.ViewComment">Visa kommentar</xsl:variable>
	<xsl:variable name="i18n.Open">Öppna</xsl:variable>
	<xsl:variable name="i18n.Close">Stäng</xsl:variable>
	<xsl:variable name="i18n.SendReminder">Påminnelse</xsl:variable>
	<xsl:variable name="i18n.SendReminderLong">Skicka påminnelse till ansvariga</xsl:variable>
	<xsl:variable name="i18n.DownloadSignature">Hämta signatur</xsl:variable>
	<xsl:variable name="i18n.DownloadSignatures">Hämta bekräftelse med signaturer</xsl:variable>
	
	<xsl:variable name="i18n.ManualReminder">Manuell påminnelse skickad av </xsl:variable>
	<xsl:variable name="i18n.AutomaticReminder">Automatisk påminnelse skickad.</xsl:variable>
	<xsl:variable name="i18n.Total">Totalt</xsl:variable>
	<xsl:variable name="i18n.RemindersSent">påminnelser skickade (max 10 visas).</xsl:variable>
	
</xsl:stylesheet>
