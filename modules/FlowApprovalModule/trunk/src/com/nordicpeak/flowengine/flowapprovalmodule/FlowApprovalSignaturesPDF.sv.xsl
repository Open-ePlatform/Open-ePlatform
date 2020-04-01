<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:include href="FlowApprovalSignaturesPDFTemplate.xsl"/>
	
	<xsl:variable name="i18n.Signatures">signaturer</xsl:variable>
	<xsl:variable name="i18n.Signatures.description.1">F�ljande aktiviteter har signerats f�r aktivitetsgruppen</xsl:variable>
	<xsl:variable name="i18n.Signatures.description.2">tillh�rande</xsl:variable>
	
	<xsl:variable name="i18n.Signature.date">Datum</xsl:variable>
	<xsl:variable name="i18n.Signature.checksum">Signerad checksumma</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceID">�rendenummer</xsl:variable>
	<xsl:variable name="i18n.Flow">E-tj�nst</xsl:variable>
	
</xsl:stylesheet>
