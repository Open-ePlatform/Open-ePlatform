<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.CitizenID">Personnummer</xsl:variable>
	<xsl:variable name="i18n.CitizenIDWithFormat">Personnummer (ÅÅÅÅMMDDXXXX)</xsl:variable>
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.FirstnameAndLastname">För- och efternamn</xsl:variable>
	<xsl:variable name="i18n.CareOf">C/O</xsl:variable>
	<xsl:variable name="i18n.Address">Adress</xsl:variable>
	<xsl:variable name="i18n.ZipCode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.PostalAddress">Ort</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	
	<xsl:variable name="i18n.ChooseContactChannels">Notifieringar</xsl:variable>
	
	<xsl:variable name="i18n.AllowContactBySMS">Jag vill bli notifierad via SMS</xsl:variable>
	<xsl:variable name="i18n.AllowContactByEmail">Jag vill bli notifierad via e-post</xsl:variable>
	<xsl:variable name="i18n.ContactBySMS">SMS</xsl:variable>
	<xsl:variable name="i18n.ContactByEmail">E-post</xsl:variable>
	
	<xsl:variable name="i18n.UpdateMyUserProfile">Uppdatera kontaktuppgifterna under mina uppgifter</xsl:variable>
	<xsl:variable name="i18n.UpdatingMyUserProfile">Dina kontaktuppgifter kommer att sparas under mina uppgifter.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Det här fältet är obligatoriskt!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLongFieldContent.part1">Innehållet i det här fältet är </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLongFieldContent.part2"> tecken vilket överskrider maxgränsen på </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLongFieldContent.part3"> tecken!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnableToUpdateUser">Det gick inte att uppdatera dina kontaktuppgifter, försök igen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.NoContactChannelChoosen">Du behöver välja minst en form av notifiering!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RequiresCitizenIDOnUser">Ditt användarkonto saknar personnummer, vänligen kontakta administratören!</xsl:variable>
	
	<xsl:variable name="i18n.ZipCodeAndPostalAddress">Postnummer och ort</xsl:variable>
</xsl:stylesheet>
