<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryCommon.sv.xsl"/>
	<xsl:include href="ChildQueryTemplates.xsl"/>
	
	<xsl:variable name="i18n.Error.Required">Den h�r fr�gan �r obligatorisk!</xsl:variable>
	<xsl:variable name="i18n.Error.NoChildren">Du �r inte v�rdnadshavare f�r n�got barn och kan d�rf�r inte v�lja ett barn.</xsl:variable>
	<xsl:variable name="i18n.Error.UnableToGetChildren">Det g�r inte att komma �t personuppgiftstj�nsten f�r att h�mta barn, v�nligen f�rs�k igen senare.</xsl:variable>
	<xsl:variable name="i18n.Error.UnknownValidationError">Ett ok�nt valideringsfel har uppst�tt!</xsl:variable>
	<xsl:variable name="i18n.Error.RequiredField">Det h�r f�ltet �r obligatoriskt!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormat">Inneh�llet i det h�r f�ltet har ett ogiltigt format!</xsl:variable>
	<xsl:variable name="i18n.Error.InvalidFormatMain">Du har valt ett ogiltig barn, f�rs�k igen!</xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part1">Inneh�llet i det h�r f�ltet �r </xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part2"> tecken vilket �verskrider maxgr�nsen p� </xsl:variable>
	<xsl:variable name="i18n.Error.TooLongFieldContent.part3"> tecken!</xsl:variable>
	<xsl:variable name="i18n.Error.SecretGuardian">Din ans�kan kan inte slutf�ras. V�nligen kontakta kommunen f�r mer hj�lp.</xsl:variable>
	<xsl:variable name="i18n.Error.EmailOrPhoneRequired">Du beh�ver fylla i e-post eller mobiltelefon f�r den andra v�rdnadshavaren.</xsl:variable>
	<xsl:variable name="i18n.Error.EmailVerificationMismatch">De angivna e-postaddresserna �verenst�mmer inte</xsl:variable>
	<xsl:variable name="i18n.Error.PhoneVerificationMismatch">De angivna mobiltelefonnumren �verenst�mmer inte</xsl:variable>
	
	<xsl:variable name="i18n.ChosenChild">Barn</xsl:variable>
	<xsl:variable name="i18n.Guardians">V�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.OtherGuardians">Andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.SecretChildrenInfo">Barn under sekretess visas inte.</xsl:variable>
	<xsl:variable name="i18n.OtherGuardiansNotificationInfo">B�da v�rdnadshavares signaturer kr�vs. Den andra v�rdnadshavaren kommer att f� ett meddelande med uppmaning om att signera �rendet.</xsl:variable>
	
	<xsl:variable name="i18n.Column.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.Column.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Column.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Column.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Column.SocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.Column.Address">Adress</xsl:variable>
	<xsl:variable name="i18n.Column.Zipcode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.Column.PostalAddress">Postort</xsl:variable>
	<xsl:variable name="i18n.Confirmation">bekr�ftelse</xsl:variable>
	
</xsl:stylesheet>
