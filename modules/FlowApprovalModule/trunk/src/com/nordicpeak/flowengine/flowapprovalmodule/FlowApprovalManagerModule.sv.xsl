<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:import href="FlowApprovalCommon.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="FlowApprovalManagerModuleTemplates.xsl"/>
	
	<xsl:variable name="java.tabTitle">Aktiviteter</xsl:variable>
	
	<xsl:variable name="i18n.Activity.reminders">P�minnelser</xsl:variable>
	<xsl:variable name="i18n.ViewComment">Visa kommentar</xsl:variable>
	<xsl:variable name="i18n.Open">�ppna</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.SendReminder">P�minnelse</xsl:variable>
	<xsl:variable name="i18n.SendReminderLong">Vill du skicka en p�minnelse till ansvariga</xsl:variable>
	<xsl:variable name="i18n.DownloadSignature">H�mta signatur</xsl:variable>
	<xsl:variable name="i18n.DownloadSignatures">H�mta bekr�ftelse med signaturer</xsl:variable>
	
	<xsl:variable name="i18n.ManualReminder">Manuell p�minnelse skickad av </xsl:variable>
	<xsl:variable name="i18n.AutomaticReminder">Automatisk p�minnelse skickad.</xsl:variable>
	<xsl:variable name="i18n.Total">Totalt</xsl:variable>
	<xsl:variable name="i18n.RemindersSent">p�minnelser skickade (max 10 visas).</xsl:variable>
	<xsl:variable name="i18n.AssignOwner">Byt ansvarig</xsl:variable>
	<xsl:variable name="i18n.AssignOwner.title">Byt ansvariga f�r aktivitet: </xsl:variable>
	<xsl:variable name="i18n.AssignOwner.description">V�lj nya ansvariga anv�ndare eller l�mna tomt f�r att �terg� till ordinarie ansvariga.</xsl:variable>
	<xsl:variable name="i18n.AssignOwner.submit">Byt ansvariga</xsl:variable>
	
</xsl:stylesheet>
