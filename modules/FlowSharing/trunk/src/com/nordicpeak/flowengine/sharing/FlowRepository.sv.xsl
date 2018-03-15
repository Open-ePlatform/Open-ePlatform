<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FlowRepositoryTemplates.xsl"/>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.username" select="'Anv�ndarnamn'"/>
	<xsl:variable name="i18n.password" select="'L�senord'"/>
	<xsl:variable name="i18n.uploadAccess">Uppladdningsr�ttigheter</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet'"/>	
	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet'"/>
	<xsl:variable name="i18n.sourceToUpdateNotFound" select="'Organisation hittades inte'"/>
	<xsl:variable name="i18n.sourceToRemoveNotFound" select="'Organisation hittades inte'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett ok�nt fel har uppst�tt'"/>
	<xsl:variable name="i18n.usernameAlreadyTaken">Anv�ndarnamnet anv�nds redan av en annan organisation</xsl:variable>
	
	<xsl:variable name="i18n.passwordConfirmation">Bekr�fta l�senord</xsl:variable>
	<xsl:variable name="i18n.passwordConfirmationMissMatch">L�senorden st�mmer inte �verens</xsl:variable>
	
	<xsl:variable name="i18n.changePassword">�ndra l�senord</xsl:variable>
	<xsl:variable name="i18n.noSourcesFound">Inga organisationer hittades</xsl:variable>
	<xsl:variable name="i18n.addSource">L�gg till organisation</xsl:variable>
	<xsl:variable name="i18n.requestedSourceNotFound">Den beg�rda organisationen hittades inte</xsl:variable>
	<xsl:variable name="i18n.unableToAddSources">Det gick inte att l�gga till organisationen</xsl:variable>
	
	<xsl:variable name="i18n.viewSource">Visa</xsl:variable>
	<xsl:variable name="i18n.deleteSourceConfirm">�r du s�ker p� att du vill ta bort</xsl:variable>
	<xsl:variable name="i18n.removeSource">Ta bort</xsl:variable>
	<xsl:variable name="i18n.updateSource">Uppdatera</xsl:variable>
	<xsl:variable name="i18n.saveChanges">Spara</xsl:variable>
	<xsl:variable name="i18n.ListSources.Title">organisationer</xsl:variable>
</xsl:stylesheet>
