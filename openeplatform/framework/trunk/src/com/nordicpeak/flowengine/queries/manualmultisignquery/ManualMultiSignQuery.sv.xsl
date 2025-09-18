<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/xsl/CommonErrors.sv.xsl"/>
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="ManualMultiSignQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.email">e-postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.mobilePhone">mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.mobilePhoneNumber">mobiltelefonnummer</xsl:variable>
	<xsl:variable name="i18n.SocialSecurityNumber">Personnummer (����MMDDXXXX)</xsl:variable>
	<xsl:variable name="i18n.Confirmation">Bekr�fta</xsl:variable>

	<xsl:variable name="i18n.UserUsingPostersCitizenIdentifier">Du f�r inte ange det personnummer som tillh�r anv�ndaren som skickat in �rendet</xsl:variable>
	
</xsl:stylesheet>
