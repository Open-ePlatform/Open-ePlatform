<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/xsl/CommonErrors.sv.xsl"/>
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="RadioButtonQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.InvalidFormat">Inneh�llet i det h�r f�ltet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.ChooseOneAlternative">V�lj ett alternativ.</xsl:variable>
	<xsl:variable name="i18n.RequiredQuery">Du m�ste v�lja ett av alternativen.</xsl:variable>

	
</xsl:stylesheet>
