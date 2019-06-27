<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="TextAreaQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Textareafr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Textareafr�ga anv�nds n�r anv�ndaren skall ange information i ett textf�lt och kunna skriva in flera rader.
	</xsl:variable>
	
	<xsl:variable name="i18n.MaxLength">Till�ten l�ngd p� textinneh�ll</xsl:variable>
	<xsl:variable name="i18n.maxLength">till�ten l�ngd p� textinneh�ll</xsl:variable>
	
	<xsl:variable name="i18n.TextAreaQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.MaxLengthToBig">Maxl�ngden f�r textinneh�llet kan vara h�gst 65535!</xsl:variable>
	
	<xsl:variable name="i18n.RowCountTooHigh">Max antal rader f�r h�gst vara 255!</xsl:variable>
	
	<xsl:variable name="i18n.Rows">Antal rader</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	
	<xsl:variable name="i18n.hideDescriptionInPDF">D�lj beskrivning i PDF</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">L�s fr�ga vid �verl�telse</xsl:variable>
	<xsl:variable name="i18n.showLetterCount">Visa r�knare f�r antal tecken som anv�nts av maximalt antal tecken</xsl:variable>
	<xsl:variable name="i18n.keepalive">H�ll anv�ndaren inloggad n�r anv�ndaren skriver i f�ltet</xsl:variable>
	
	<xsl:variable name="i18n.HideTitle">D�lj rubrik</xsl:variable>
	
</xsl:stylesheet>
