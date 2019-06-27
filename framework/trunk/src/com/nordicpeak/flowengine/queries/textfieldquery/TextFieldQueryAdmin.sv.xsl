<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="TextFieldQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Textfältsfråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Textfältsfråga används när användaren skall ange information i textfält. En fråga kan innehålla flera textfält och skapas under frågans inställningar.
	</xsl:variable>
	
	<xsl:variable name="java.fieldLayoutNewLine">En kolumn</xsl:variable>
	<xsl:variable name="java.fieldLayoutFloat">Två kolumner</xsl:variable>
	
	<xsl:variable name="i18n.BaseInfo">Grundinformation</xsl:variable>
	<xsl:variable name="i18n.Layout">Layout</xsl:variable>
	<xsl:variable name="i18n.MergeQuery">Gruppera frågan</xsl:variable>
	
	<xsl:variable name="i18n.TextFieldQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.TextFields">Textfält</xsl:variable>
	<xsl:variable name="i18n.AddTextField">Lägg till textfält</xsl:variable>
	<xsl:variable name="i18n.UpdateTextField">Uppdatera textfält</xsl:variable>
	<xsl:variable name="i18n.UpdateBaseInformation">Uppdatera basinformation</xsl:variable>
	<xsl:variable name="i18n.Add">Lägg till</xsl:variable>
	<xsl:variable name="i18n.Done">Klar</xsl:variable>
	<xsl:variable name="i18n.SortTextFields.Title">Sortera textfält</xsl:variable>
	<xsl:variable name="i18n.Label">Namn</xsl:variable>
	<xsl:variable name="i18n.label">namn</xsl:variable>
	
	<xsl:variable name="i18n.width">fältets bredd</xsl:variable>
	<xsl:variable name="i18n.Required">Obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.MaxLength">Tillåten längd på textinnehåll</xsl:variable>
	<xsl:variable name="i18n.maxLength">tillåten längd på textinnehåll</xsl:variable>
	<xsl:variable name="i18n.Field.MinLength">Krävd längd på textinnehåll</xsl:variable>
	<xsl:variable name="i18n.Field.minLength">krävd längd på textinnehåll</xsl:variable>
	<xsl:variable name="i18n.FormatValidator">Validator</xsl:variable>
	<xsl:variable name="i18n.InvalidFormatMessage">Valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.invalidFormatMessage">valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.SortTextFields">Sortera textfält för fråga</xsl:variable>
	<xsl:variable name="i18n.PlaceHolderText">Placeholder text</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">Lås fråga vid överlåtelse</xsl:variable>
	
	<xsl:variable name="i18n.DeleteTextField.Confirm">Ta bort textfältet</xsl:variable>
	<xsl:variable name="i18n.DeleteTextField">Ta bort textfältet</xsl:variable>	
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara fältets värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.Disabled">Lås fältet</xsl:variable>
	<xsl:variable name="i18n.DefaultValue">Standardvärde</xsl:variable>
	<xsl:variable name="i18n.HideTitle">Dölj rubrik</xsl:variable>
	
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	<xsl:variable name="i18n.Endpoint">API-källa</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Title">Koppling mot API-källa</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Select">Välj API-källa</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Select.empty">Ingen API-källa</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Selected">Denna fråga är kopplad mot API-källan</xsl:variable>
	<xsl:variable name="i18n.Endpoint.NonSelected">Denna fråga är inte kopplad mot någon API-källa</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Fields">Koppling av fält</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Field.Select">Välj fält</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Validation.NoEndpointFieldsSelected">Du måste koppla minst ett fält</xsl:variable>

</xsl:stylesheet>
