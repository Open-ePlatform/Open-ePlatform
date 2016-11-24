<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" indent="yes" method="xml" omit-xml-declaration="yes"/>

	<xsl:include href="ChildQueryPDFTemplates.xsl"/>
	
	<xsl:variable name="i18n.ChosenChild">Barn</xsl:variable>
	<xsl:variable name="i18n.Guardians">Vårdnadshavare</xsl:variable>
	
	<xsl:variable name="i18n.Column.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.Column.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Column.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Column.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Column.SocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.Column.Address">Address</xsl:variable>
	<xsl:variable name="i18n.Column.Zipcode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.Column.PostalAddress">Postort</xsl:variable>
</xsl:stylesheet>
