<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="RemovedGroupsBackgroundModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.RemovedGroups.Message">På grund av den valda inloggningsmetoden har dina behörigheter begränsats</xsl:variable>
	
	<xsl:variable name="i18n.RemovedGroups.Message.Link">(läs mer)</xsl:variable>
	
	<xsl:variable name="i18n.RemovedGroups.Modal.Title">Begränsade behörigheter</xsl:variable>
	
	<xsl:variable name="i18n.RemovedGroups.Modal.Description">Tillitsnivån på den valda inloggningsmetoden är lägre än vad som krävs för följande behörighetsgrupper och du har för denna inloggning därför tappat de behörigheter som är kopplade till dessa grupper.</xsl:variable>
	
	<xsl:variable name="i18n.RemovedGroups.Modal.Footer">Kontakta systemadministratören för mer information.</xsl:variable>
	
	<xsl:variable name="i18n.Close">Stäng</xsl:variable>
</xsl:stylesheet>
