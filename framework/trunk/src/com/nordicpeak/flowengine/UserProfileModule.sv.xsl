<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="UserProfileModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.username" select="'Användarnamn'"/>
	<xsl:variable name="i18n.password" select="'Lösenord'"/>
	<xsl:variable name="i18n.firstname" select="'Förnamn'"/>
	<xsl:variable name="i18n.lastname" select="'Efternamn'"/>
	
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du behöver fylla i fältet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet'"/>
	
	<xsl:variable name="i18n.unknownFault" select="'Ett okänt fel har uppstått'"/>
	
	<xsl:variable name="i18n.usernameAlreadyTaken">Användarnamnet används redan av en annan användare</xsl:variable>
	<xsl:variable name="i18n.emailAlreadyTaken">E-post adressen används redan av en annan användare</xsl:variable>
	<xsl:variable name="i18n.passwordConfirmationMissMatch">Lösenorden stämmer inte överens</xsl:variable>
	
	<xsl:variable name="i18n.CancelConfirm">Är du säker på att du vill avbryta utan att spara</xsl:variable>
	<xsl:variable name="i18n.UserNotFound">Din användare hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UserUpdatedMessage">Ändringar sparade.</xsl:variable>
	
	<xsl:variable name="i18n.email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.saveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.ContactDetails">Kontaktuppgifter</xsl:variable>
	
	<xsl:variable name="i18n.citizenIdentifier">Personnummer</xsl:variable>
	<xsl:variable name="i18n.address">Adress</xsl:variable>
	<xsl:variable name="i18n.zipCode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.postalAddress">Ort</xsl:variable>
	<xsl:variable name="i18n.phone">Telefonnummer</xsl:variable>
	<xsl:variable name="i18n.mobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.SMS">Jag vill bli notifierad via SMS</xsl:variable>
</xsl:stylesheet>
