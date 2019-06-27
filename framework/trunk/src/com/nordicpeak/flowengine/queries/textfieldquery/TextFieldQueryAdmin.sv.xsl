<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="TextFieldQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Textf�ltsfr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Textf�ltsfr�ga anv�nds n�r anv�ndaren skall ange information i textf�lt. En fr�ga kan inneh�lla flera textf�lt och skapas under fr�gans inst�llningar.
	</xsl:variable>
	
	<xsl:variable name="java.fieldLayoutNewLine">En kolumn</xsl:variable>
	<xsl:variable name="java.fieldLayoutFloat">Tv� kolumner</xsl:variable>
	
	<xsl:variable name="i18n.BaseInfo">Grundinformation</xsl:variable>
	<xsl:variable name="i18n.Layout">Layout</xsl:variable>
	<xsl:variable name="i18n.MergeQuery">Gruppera fr�gan</xsl:variable>
	
	<xsl:variable name="i18n.TextFieldQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	<xsl:variable name="i18n.TextFields">Textf�lt</xsl:variable>
	<xsl:variable name="i18n.AddTextField">L�gg till textf�lt</xsl:variable>
	<xsl:variable name="i18n.UpdateTextField">Uppdatera textf�lt</xsl:variable>
	<xsl:variable name="i18n.UpdateBaseInformation">Uppdatera basinformation</xsl:variable>
	<xsl:variable name="i18n.Add">L�gg till</xsl:variable>
	<xsl:variable name="i18n.Done">Klar</xsl:variable>
	<xsl:variable name="i18n.SortTextFields.Title">Sortera textf�lt</xsl:variable>
	<xsl:variable name="i18n.Label">Namn</xsl:variable>
	<xsl:variable name="i18n.label">namn</xsl:variable>
	
	<xsl:variable name="i18n.width">f�ltets bredd</xsl:variable>
	<xsl:variable name="i18n.Required">Obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.MaxLength">Till�ten l�ngd p� textinneh�ll</xsl:variable>
	<xsl:variable name="i18n.maxLength">till�ten l�ngd p� textinneh�ll</xsl:variable>
	<xsl:variable name="i18n.Field.MinLength">Kr�vd l�ngd p� textinneh�ll</xsl:variable>
	<xsl:variable name="i18n.Field.minLength">kr�vd l�ngd p� textinneh�ll</xsl:variable>
	<xsl:variable name="i18n.FormatValidator">Validator</xsl:variable>
	<xsl:variable name="i18n.InvalidFormatMessage">Valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.invalidFormatMessage">valideringsmeddelande</xsl:variable>
	<xsl:variable name="i18n.SortTextFields">Sortera textf�lt f�r fr�ga</xsl:variable>
	<xsl:variable name="i18n.PlaceHolderText">Placeholder text</xsl:variable>
	<xsl:variable name="i18n.lockOnOwnershipTransfer">L�s fr�ga vid �verl�telse</xsl:variable>
	
	<xsl:variable name="i18n.DeleteTextField.Confirm">Ta bort textf�ltet</xsl:variable>
	<xsl:variable name="i18n.DeleteTextField">Ta bort textf�ltet</xsl:variable>	
	
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltets v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.Disabled">L�s f�ltet</xsl:variable>
	<xsl:variable name="i18n.DefaultValue">Standardv�rde</xsl:variable>
	<xsl:variable name="i18n.HideTitle">D�lj rubrik</xsl:variable>
	
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	<xsl:variable name="i18n.Endpoint">API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Title">Koppling mot API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Select">V�lj API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Select.empty">Ingen API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Selected">Denna fr�ga �r kopplad mot API-k�llan</xsl:variable>
	<xsl:variable name="i18n.Endpoint.NonSelected">Denna fr�ga �r inte kopplad mot n�gon API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Fields">Koppling av f�lt</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Field.Select">V�lj f�lt</xsl:variable>
	<xsl:variable name="i18n.Endpoint.Validation.NoEndpointFieldsSelected">Du m�ste koppla minst ett f�lt</xsl:variable>

</xsl:stylesheet>
