<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="PersonDataInformerQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Personuppgiftshantering</xsl:variable>
	<xsl:variable name="java.queryDescription">	
Fr�getypen Personuppgiftshantering anv�nds f�r att i e-tj�nsten informera om e-tj�nstens personuppgiftshantering (informationen h�mtas fr�n e-tj�nstens
personuppgiftsinst�llning). Fr�gan visar �ven en kryssruta f�r att ge anv�ndaren chansen att visa att den tagit del av informationen. S�tts fr�gan som obligatorisk
m�ste anv�ndaren kryssa i rutan f�r att komma vidare till n�sta steg/skicka in �rendet.
	</xsl:variable>	
	<xsl:variable name="java.alternativeName">Personuppgiftshantering accepterat</xsl:variable>
	
	<xsl:variable name="i18n.PersonDataInformerQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
</xsl:stylesheet>
