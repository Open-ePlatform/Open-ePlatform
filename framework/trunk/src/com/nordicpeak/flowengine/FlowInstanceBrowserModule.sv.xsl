<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowBrowserModule.sv.xsl"/>
	
	<xsl:include href="FlowInstanceBrowserModuleTemplates.xsl"/>
	
	<xsl:include href="FlowOverview.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="java.searchHints">T.ex. bygglov hus, dagisplats, l�mna synpunkt</xsl:variable>
	<xsl:variable name="java.foreignIDBlockedMessage">
		&lt;section class="modal warning"&gt;
			&lt;i class="icon" style="font-size: 16px; margin-right: 4px; color: rgb(199, 52, 52);"&gt;!&lt;/i&gt;
			eIDAS inloggning st�ds inte av den h�r e-tj�nsten
		&lt;/section&gt;
		&lt;p&gt;
			Den h�r e-tj�nsten fungerar inte med eIDAS inloggning, prova en annan inloggningsmetod.
		&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="i18n.ShowFlow">Ans�k</xsl:variable>
	<xsl:variable name="i18n.UncategorizedFlows">�vriga e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.ShowLongDescription">Visa mer information</xsl:variable>
	<xsl:variable name="i18n.HideLongDescription">D�lj information</xsl:variable>
	
	<xsl:variable name="i18n.SearchTitle">S�k e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.RecommendedSearches">Rekommenderade s�kningar</xsl:variable>
	<xsl:variable name="i18n.SearchDone">S�kningen �r klar</xsl:variable>
	<xsl:variable name="i18n.close">st�ng</xsl:variable>
	<xsl:variable name="i18n.Hits.Part1">gav</xsl:variable>
	<xsl:variable name="i18n.Hits.Part2">tr�ffar</xsl:variable>
	
	<xsl:variable name="i18n.FlowTypeFilter">Filtrera</xsl:variable>
	
	<xsl:variable name="i18n.SortFlowTypes">Sortera per kategori</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowTypesFound">Inga e-tj�nster hittades.</xsl:variable>
	
</xsl:stylesheet>
