<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:include href="FlowInstancePDFTemplates.xsl"/>
	
	<xsl:variable name="java.signature">Signatur</xsl:variable>
	<xsl:variable name="java.signingPDF">Signeringsunderlag</xsl:variable>
	<xsl:variable name="java.attachmentPageNumber1">Bilaga </xsl:variable>
	<xsl:variable name="java.attachmentPageNumber2"> sida </xsl:variable>
	<xsl:variable name="java.attachmentPageNumber3"> av </xsl:variable>
	<xsl:variable name="java.attachmentFlowInstanceID">�rendenummer: #</xsl:variable>
	<xsl:variable name="java.attachmentDate">Datum: </xsl:variable>
	
	<xsl:variable name="i18n.AnonymousUser">Ej inloggad anv�ndare</xsl:variable>
	<xsl:variable name="i18n.EditedByManager">�ndrad av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.EditDate">�ndringsdatum</xsl:variable>
	
	<xsl:variable name="i18n.Signing">Signeringsunderlag</xsl:variable>
	<xsl:variable name="i18n.Signed">signerad</xsl:variable>
	<xsl:variable name="i18n.Signed.Title">Signering</xsl:variable>
	<xsl:variable name="i18n.Signed.Description">F�ljande parter har signerat detta �rende.</xsl:variable>
	<xsl:variable name="i18n.Signed.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Signed.CitizenIdentifier">Person ID</xsl:variable>
	<xsl:variable name="i18n.Signed.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.Signed.Checksum">Signerad checksumma</xsl:variable>
	
	<xsl:variable name="i18n.Payed.Title">Betalning</xsl:variable>
	<xsl:variable name="i18n.Payed.Amount">Belopp</xsl:variable>
	<xsl:variable name="i18n.Payed.Date">Datum</xsl:variable>
	
	<xsl:variable name="i18n.UnsubmittedExport">Ej inskickat �rende</xsl:variable>
</xsl:stylesheet>