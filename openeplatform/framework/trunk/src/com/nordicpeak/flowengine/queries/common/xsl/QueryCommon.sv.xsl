<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="QueryCommonTemplates.xsl"/>
	
	<xsl:variable name="i18n.Help">Hjälp</xsl:variable>
	<xsl:variable name="i18n.UpdateQuery">Ändra</xsl:variable>
	<xsl:variable name="i18n.UpdateQueryTitle">Ändra fråga</xsl:variable>
	
	<xsl:variable name="i18n.FreeTextAlternativeToLong">Innehållet i fritextalternativfältet överskrider gränsen på 255 tecken!</xsl:variable>
	<xsl:variable name="i18n.FreeTextAlternativeValueRequired">Du måste fylla i fältet för fritextalternativet!</xsl:variable>
	
	<xsl:variable name="i18n.Currency">SEK</xsl:variable>
	
	<xsl:variable name="i18n.AjaxValidationError.RequiredField">Det här fältet är obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.AjaxValidationError.InvalidFormat">Innehållet i det här fältet har felaktigt format</xsl:variable>
	<xsl:variable name="i18n.AjaxValidationError.TooLong">Innehållet i det här fältet är för långt</xsl:variable>
	<xsl:variable name="i18n.AjaxValidationError.TooShort">Innehållet i det här fältet är för kort</xsl:variable>
	<xsl:variable name="i18n.AjaxValidationError.Other">Ett okänt valideringsfel har uppstått</xsl:variable>
	
</xsl:stylesheet>