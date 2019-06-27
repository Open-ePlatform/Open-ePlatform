<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="QueryAdminCommonTemplates.xsl"/>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.name">namn</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.comment">kommentar</xsl:variable>
	<xsl:variable name="i18n.commentVisibility">visas endast för e-tjänstbyggaren</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.description">beskrivning</xsl:variable>
	<xsl:variable name="i18n.HelpText">Hjälptext</xsl:variable>
	<xsl:variable name="i18n.helpText">Hjälptext</xsl:variable>
	<xsl:variable name="i18n.MoveAlternative">Flytta alternativ</xsl:variable>
	<xsl:variable name="i18n.DeleteAlternative">Ta bort alternativet</xsl:variable>
	<xsl:variable name="i18n.DeleteAlternativeConfirm">Är du säker på att du vill ta bort alternativet</xsl:variable>
	<xsl:variable name="i18n.AddAlternative">Lägg till alternativ</xsl:variable>
	<xsl:variable name="i18n.Alternative">Alternativ</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.defaultQueryState">standardläge</xsl:variable>
	<xsl:variable name="i18n.DefaultQueryState.title">Standardläge</xsl:variable>
	
	<xsl:variable name="i18n.DefaultQueryState.description">Välj vilket standardläge som frågan skall ha.</xsl:variable>
	<xsl:variable name="i18n.QueryState.VISIBLE">Valfri</xsl:variable>
	<xsl:variable name="i18n.QueryState.VISIBLE_REQUIRED">Obligatorisk</xsl:variable>
	<xsl:variable name="i18n.QueryState.HIDDEN">Dold</xsl:variable>
	
	<xsl:variable name="i18n.FreeTextAlternative">Fritextalternativ</xsl:variable>
	<xsl:variable name="i18n.FreeTextAlternativeDescription">Välj om frågan skall ha ett fritextalternativ</xsl:variable>
	<xsl:variable name="i18n.freeTextAlternative">fritextalternativ</xsl:variable>
	
	<xsl:variable name="i18n.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.AlternativeRequired">Du måste fylla i ett namn för alternativ</xsl:variable>
	<xsl:variable name="i18n.TooFewAlternatives">Du måste skapa minst 2 alternativ för frågan!</xsl:variable>
	<xsl:variable name="i18n.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	<xsl:variable name="i18n.TooLongFieldContent">För långt innehåll i fältet</xsl:variable>
	<xsl:variable name="i18n.InvalidFormat">Ogiltigt format på innehållet i fältet</xsl:variable>
	
	<xsl:variable name="i18n.exportQuery.title">Exportera fråga</xsl:variable>
	<xsl:variable name="i18n.exportQuery.description">Välj om frågan skall inkluderas i den XML som skickas till andra system.</xsl:variable>
	<xsl:variable name="i18n.exportQuery">Exportera frågan</xsl:variable>
	<xsl:variable name="i18n.xsdElementName">XML-elementnamn</xsl:variable>
	
	<xsl:variable name="i18n.EditAlternativeExtra">Avancerade inställningar för alternativet</xsl:variable>
	<xsl:variable name="i18n.AlternativeExtraTitle">Avancerade inställningar för alternativet:</xsl:variable>
	<xsl:variable name="i18n.CloseAlternativeExtra">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AutoSortAlternatives.Select">Sortera..</xsl:variable>
	<xsl:variable name="i18n.AutoSortAlternatives.Descending">Sortera A-Ö</xsl:variable>
	<xsl:variable name="i18n.AutoSortAlternatives.Ascending">Sortera Ö-A</xsl:variable>
	<xsl:variable name="i18n.AlternativeXMLValue">Värde i XML</xsl:variable>
	<xsl:variable name="i18n.AlternativeXMLValueDescription">Lämna tomt för att använda alternativets namn som värde.</xsl:variable>
	<xsl:variable name="i18n.AlternativeAttributeValue">Värde för attribut</xsl:variable>
	<xsl:variable name="i18n.AlternativeAttributeValueDescription">Lämna tomt för att använda alternativets namn som värde.</xsl:variable>
	<xsl:variable name="i18n.AlternativePrice">Kostnad</xsl:variable>
	<xsl:variable name="i18n.AlternativePriceDescription">Alternativets kostnad om betalning är aktiverat för e-tjänsten</xsl:variable>
	
	<xsl:variable name="i18n.validationError.alternative">alternativ</xsl:variable>
	
	<xsl:variable name="i18n.mergeWithPreviousQuery.title">Gruppera frågan med föregående</xsl:variable>
	<xsl:variable name="i18n.mergeWithPreviousQuery.description">Visuellt gruppera frågan med föregående fråga</xsl:variable>
	<xsl:variable name="i18n.mergeWithPreviousQuery">Gruppera fråga</xsl:variable>
	
</xsl:stylesheet>
