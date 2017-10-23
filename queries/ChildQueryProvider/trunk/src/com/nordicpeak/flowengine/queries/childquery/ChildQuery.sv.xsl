<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="ChildQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.Required">Den här frågan är obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.NoChildren">Du är inte vårdnadshavare för något barn och kan därför inte välja ett barn.</xsl:variable>
	
	<xsl:variable name="i18n.Error.Provider.CommunicationError">Det går inte att komma åt personuppgiftstjänsten, vänligen försök igen senare.</xsl:variable>
	<xsl:variable name="i18n.Error.Provider.IncompleteData">Det går inte att hämta komplett data från personuppgiftstjänsten. Vänligen kontakta administratören för mer information.</xsl:variable>
	<xsl:variable name="i18n.Error.Provider.InvalidCitizenIdentifier">Ditt personnummer accepterades inte av personuppgiftstjänsten. Vänligen kontakta administratören för mer information.</xsl:variable>
	<xsl:variable name="i18n.Error.Provider.Unknown">Ett okänt fel uppstod under bearbetning av information från personuppgiftstjänsten, vänligen försök igen senare.</xsl:variable>
	
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett okänt valideringsfel har uppstått!</xsl:variable>
	<xsl:variable name="i18n.Error.RequiredField">Det här fältet är obligatoriskt!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormat">Innehållet i det här fältet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormatMain">Du har valt ett ogiltig barn, försök igen!</xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part1">Innehållet i det här fältet är </xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part2"> tecken vilket överskrider maxgränsen på </xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part3"> tecken!</xsl:variable>
	<xsl:variable name="i18n.Error.SecretGuardian">Din ansökan kan inte slutföras. Vänligen kontakta kommunen för mer hjälp.</xsl:variable>
	<xsl:variable name="i18n.Error.EmailOrPhoneRequired">Du behöver fylla i e-post eller mobiltelefon för den andra vårdnadshavaren.</xsl:variable>
	<xsl:variable name="i18n.Error.EmailVerificationMismatch">De angivna e-postaddresserna överenstämmer inte</xsl:variable>
	<xsl:variable name="i18n.Error.PhoneVerificationMismatch">De angivna mobiltelefonnumren överenstämmer inte</xsl:variable>
	
	<xsl:variable name="i18n.ChosenChild">Barn</xsl:variable>
	<xsl:variable name="i18n.Guardians">Vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.OtherGuardians">Barnets andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.SecretChildrenInfo">Barn under sekretess visas inte. Kontakta verksamheten för andra ansökningsmöjligheter.</xsl:variable>
	<xsl:variable name="i18n.AgeChildrenInfo">Barn som inte upfyller ålderskravet går inte att välja.</xsl:variable>
	<xsl:variable name="i18n.OtherGuardiansNotificationInfo">Båda vårdnadshavares signaturer krävs. Den andra vårdnadshavaren kommer att få ett meddelande med uppmaning om att signera ärendet.</xsl:variable>
	
	<xsl:variable name="i18n.Column.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.Column.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Column.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Column.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Column.SocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.Column.Address">Adress</xsl:variable>
	<xsl:variable name="i18n.Column.Zipcode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.Column.PostalAddress">Postort</xsl:variable>
	<xsl:variable name="i18n.Confirmation">bekräftelse</xsl:variable>
	
</xsl:stylesheet>
