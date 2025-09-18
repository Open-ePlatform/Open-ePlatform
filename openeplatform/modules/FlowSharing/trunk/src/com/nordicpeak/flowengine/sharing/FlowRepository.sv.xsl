<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowRepositoryTemplates.xsl"/>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.username" select="'Användarnamn'"/>
	<xsl:variable name="i18n.password" select="'Lösenord'"/>
	<xsl:variable name="i18n.uploadAccess">Uppladdningsrättigheter</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet'"/>	
	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet'"/>
	<xsl:variable name="i18n.sourceToUpdateNotFound" select="'Organisation hittades inte'"/>
	<xsl:variable name="i18n.sourceToRemoveNotFound" select="'Organisation hittades inte'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett okänt fel har uppstått'"/>
	<xsl:variable name="i18n.usernameAlreadyTaken">Användarnamnet används redan av en annan organisation</xsl:variable>
	
	<xsl:variable name="i18n.passwordConfirmation">Bekräfta lösenord</xsl:variable>
	<xsl:variable name="i18n.passwordConfirmationMissMatch">Lösenorden stämmer inte överens</xsl:variable>
	
	<xsl:variable name="i18n.changePassword">Ändra lösenord</xsl:variable>
	<xsl:variable name="i18n.noSourcesFound">Inga organisationer hittades</xsl:variable>
	<xsl:variable name="i18n.addSource">Lägg till organisation</xsl:variable>
	<xsl:variable name="i18n.requestedSourceNotFound">Den begärda organisationen hittades inte</xsl:variable>
	<xsl:variable name="i18n.unableToAddSources">Det gick inte att lägga till organisationen</xsl:variable>
	
	<xsl:variable name="i18n.viewSource">Visa</xsl:variable>
	<xsl:variable name="i18n.deleteSourceConfirm">Är du säker på att du vill ta bort</xsl:variable>
	<xsl:variable name="i18n.removeSource">Ta bort</xsl:variable>
	<xsl:variable name="i18n.updateSource">Uppdatera</xsl:variable>
	<xsl:variable name="i18n.saveChanges">Spara</xsl:variable>
	<xsl:variable name="i18n.ListSources.Title">organisationer</xsl:variable>
</xsl:stylesheet>
