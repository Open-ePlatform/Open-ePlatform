<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowBrowserModule.sv.xsl"/>
	
	<xsl:include href="FlowInstanceBrowserModuleTemplates.xsl"/>
	
	<xsl:include href="FlowOverview.sv.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="java.searchHints">T.ex. bygglov hus, dagisplats, lämna synpunkt</xsl:variable>
	
	<xsl:variable name="i18n.ShowFlow">Ansök</xsl:variable>
	<xsl:variable name="i18n.UncategorizedFlows">Övriga e-tjänster</xsl:variable>
	<xsl:variable name="i18n.ShowLongDescription">Visa mer information</xsl:variable>
	<xsl:variable name="i18n.HideLongDescription">Dölj information</xsl:variable>
	<xsl:variable name="i18n.FlowDisabled">Ej tillgänglig på grund av underhållsarbete</xsl:variable>
	
	<xsl:variable name="i18n.SearchTitle">Sök e-tjänst</xsl:variable>
	<xsl:variable name="i18n.RecommendedSearches">Rekommenderade sökningar</xsl:variable>
	<xsl:variable name="i18n.SearchDone">Sökningen är klar</xsl:variable>
	<xsl:variable name="i18n.close">stäng</xsl:variable>
	<xsl:variable name="i18n.Hits.Part1">gav</xsl:variable>
	<xsl:variable name="i18n.Hits.Part2">träffar</xsl:variable>
	
	<xsl:variable name="i18n.FlowTypeFilter">Filtrera</xsl:variable>
	
	<xsl:variable name="i18n.SortFlowTypes">Sortera per kategori</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowTypesFound">Inga e-tjänster hittades.</xsl:variable>
	
</xsl:stylesheet>
