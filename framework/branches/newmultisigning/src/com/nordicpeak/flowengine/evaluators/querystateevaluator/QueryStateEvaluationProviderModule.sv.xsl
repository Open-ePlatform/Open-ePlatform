<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
		
	<xsl:include href="QueryStateEvaluationProviderModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="java.evaluatorTypeName">Ändra standardläge</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.UpdateEvaluatorDescriptor.title">Uppdatera regel</xsl:variable>
	<xsl:variable name="i18n.UpdateQueryStateEvaluator.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.selectionMode.title">Aktiveringsläge</xsl:variable>
	<xsl:variable name="i18n.selectionMode.description">Välj när denna regel ska aktiveras.</xsl:variable>
	<xsl:variable name="i18n.SelectionMode.ANY">När användaren väljer något av nedanstående markerade alternativ.</xsl:variable>
	<xsl:variable name="i18n.SelectionMode.ALL">När användaren väljer samtliga av nedanstående markerade alternativ.</xsl:variable>
	<xsl:variable name="i18n.alternatives.title">Alternativ</xsl:variable>
	<xsl:variable name="i18n.alternatives.description">Välj vilka alternativ som denna regel skall kopplas mot.</xsl:variable>
	<xsl:variable name="i18n.QueryHasNoAlternatives">Frågan som denna regel är kopplad till har inga alternativ.</xsl:variable>
	<xsl:variable name="i18n.TargetQueryState.title">Frågeläge</xsl:variable>
	<xsl:variable name="i18n.TargetQueryState.description">Välj vilket frågeläge nedanstående markerade frågor ska få när denna regel aktiveras.</xsl:variable>
	<xsl:variable name="i18n.QueryState.VISIBLE">Valfri</xsl:variable>
	<xsl:variable name="i18n.QueryState.VISIBLE_REQUIRED">Obligatorisk</xsl:variable>
	<xsl:variable name="i18n.QueryState.HIDDEN">Dold</xsl:variable>
	<xsl:variable name="i18n.selectionMode">Aktiveringsläge</xsl:variable>
	<xsl:variable name="i18n.queryState">Frågeläge</xsl:variable>
	<xsl:variable name="i18n.TargetQueries.title">Frågor</xsl:variable>
	<xsl:variable name="i18n.TargetQueries.description">Välj vilka frågor som ska påverkas av denna regel.</xsl:variable>
		
	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>

	<xsl:variable name="i18n.enabled">Aktivera regeln</xsl:variable>
	<xsl:variable name="i18n.doNotResetQueryState">Återställ INTE frågornas standardläge automatiskt (användbart när flera regler påverkar samma frågor).</xsl:variable>
</xsl:stylesheet>
