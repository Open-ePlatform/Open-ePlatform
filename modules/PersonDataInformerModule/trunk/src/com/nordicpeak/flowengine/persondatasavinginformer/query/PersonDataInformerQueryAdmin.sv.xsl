<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="PersonDataInformerQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Personuppgiftshantering</xsl:variable>
	<xsl:variable name="java.queryDescription">	
Frågetypen Personuppgiftshantering används för att i e-tjänsten informera om e-tjänstens personuppgiftshantering (informationen hämtas från e-tjänstens
personuppgiftsinställning). Frågan visar även en kryssruta för att ge användaren chansen att visa att den tagit del av informationen. Sätts frågan som obligatorisk
måste användaren kryssa i rutan för att komma vidare till nästa steg/skicka in ärendet.
	</xsl:variable>	
	<xsl:variable name="java.alternativeName">Personuppgiftshantering accepterat</xsl:variable>
	
	<xsl:variable name="i18n.PersonDataInformerQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
</xsl:stylesheet>
