<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:include href="FlowInstancePDFTemplates.xsl"/>
	
	<xsl:variable name="i18n.AnonymousUser">anonym användare</xsl:variable>
	<xsl:variable name="i18n.EditedByManager">Ändrad av handläggare</xsl:variable>
	<xsl:variable name="i18n.EditDate">Ändrings datum</xsl:variable>
	
	<xsl:variable name="i18n.Signing">Signeringsunderlag</xsl:variable>
	<xsl:variable name="i18n.Signed.Title">Signering</xsl:variable>
	<xsl:variable name="i18n.Signed.Description">Följande parter har signerat detta ärende.</xsl:variable>
	<xsl:variable name="i18n.Signed.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Signed.CitizenIdentifier">Person ID</xsl:variable>
	<xsl:variable name="i18n.Signed.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.Signed.Checksum">Signerad checksumma</xsl:variable>
	
	<xsl:variable name="i18n.Payed.Title">Betalning</xsl:variable>
	<xsl:variable name="i18n.Payed.Amount">Belopp</xsl:variable>
	<xsl:variable name="i18n.Payed.Date">Datum</xsl:variable>
	
	<xsl:variable name="i18n.UnsubmittedExport">Ej inskickat ärende</xsl:variable>
</xsl:stylesheet>