<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="CheckboxQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kryssrutefr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Kryssrutefr�ga anv�nds n�r anv�ndaren skall kunna v�lja mellan olika alternativ. Anv�ndaren kan v�lja ett eller flera alternativ beroende p� inst�llningarna.
	</xsl:variable>
	
	<xsl:variable name="java.countText">Antal</xsl:variable>
	<xsl:variable name="java.alternativesText">Alternativ</xsl:variable>
	
	<xsl:variable name="i18n.CheckboxQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.Searchable">G�r alternativen s�kbara</xsl:variable>
	<xsl:variable name="i18n.MinChecked">Minst antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.MaxChecked">Max antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.minChecked">minst antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.maxChecked">max antal valda alternativ</xsl:variable>
	<xsl:variable name="i18n.ShowCheckAllBoxes">Visa kryssruta som markerar alla alternativ</xsl:variable>
	<xsl:variable name="i18n.MinCheckedBiggerThanMaxChecked">Minst antal valda alternativ f�r inte vara st�rre �n max!</xsl:variable>
	<xsl:variable name="i18n.MaxCheckedToBig">Max antal valda alternativ f�r inte �verstiga antalet alternativ!</xsl:variable>
	<xsl:variable name="i18n.MinCheckedToBig">Minst antal valda alternativ f�r inte �verstiga antalet alternativ!</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">L�s fr�ga vid �verl�telse</xsl:variable>
	
	<xsl:variable name="i18n.TooFewAlternatives1Min">Du m�ste skapa minst 1 alternativ f�r fr�gan!</xsl:variable>
	
	<xsl:variable name="i18n.Columns">Kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.One">En kolumn</xsl:variable>
	<xsl:variable name="i18n.Columns.Two">Tv� kolumner</xsl:variable>
	<xsl:variable name="i18n.Columns.Three">Tre kolumner</xsl:variable>
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>	
	
	<xsl:variable name="i18n.HideTitle">D�lj rubrik</xsl:variable>
	<xsl:variable name="i18n.hideDescriptionInPDF">D�lj beskrivning i PDF</xsl:variable>
	
</xsl:stylesheet>
