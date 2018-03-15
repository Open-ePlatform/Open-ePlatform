<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="UserOrganizationsModuleTemplates.xsl"/>
	
	<xsl:variable name="java.userMenuTabTitle">Mina organisationer</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett ok�nt fel har uppst�tt'"/>
	
	<xsl:variable name="i18n.ListOrganizationDescription.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.ListOrganizationDescription.Part2">registrerade organisationer</xsl:variable>
	
	<xsl:variable name="i18n.Organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.OrganizationNumber">Organisationsnummer</xsl:variable>
	<xsl:variable name="i18n.OrganizationNumberWithFormat">Organisationsnummer (��MMDD-XXXX)</xsl:variable>
	<xsl:variable name="i18n.AddOrganization">L�gg till organisation</xsl:variable>
	<xsl:variable name="i18n.Add">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateOrganization">�ndra organisation</xsl:variable>
	<xsl:variable name="i18n.Update">�ndra</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteOrganizationConfirm">�r du s�ker p� att du vill ta bort organisationen</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.ContactDetails">Kontaktuppgifter</xsl:variable>
	<xsl:variable name="i18n.Name">Organisationens namn</xsl:variable>
	<xsl:variable name="i18n.Address">Adress</xsl:variable>
	<xsl:variable name="i18n.ZipCode">Postnummer</xsl:variable>
	<xsl:variable name="i18n.PostalAddress">Ort</xsl:variable>
	<xsl:variable name="i18n.ContactPerson">Kontaktperson</xsl:variable>
	<xsl:variable name="i18n.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	
	
	<xsl:variable name="i18n.AllowContactBySMS">Jag vill bli notifierad via SMS</xsl:variable>
	<xsl:variable name="i18n.AllowContactByEmail">Jag vill bli notifierad via e-post</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.NoOrganizations">Du har inga registrerade organisationer</xsl:variable>
	
	<xsl:variable name="i18n.CancelConfirm">�r du s�ker p� att du vill avbryta utan att spara</xsl:variable>
	
	<xsl:variable name="i18n.NameExists">Du har redan registrerat en organisation med det angivna namnet</xsl:variable>
	<xsl:variable name="i18n.OrganizationNumberExists">Du har redan registrerat en organisation med det angivna organisationsnumret</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedOrganizationNotFound">Den beg�rda organisation hittades inte</xsl:variable>
	
</xsl:stylesheet>
