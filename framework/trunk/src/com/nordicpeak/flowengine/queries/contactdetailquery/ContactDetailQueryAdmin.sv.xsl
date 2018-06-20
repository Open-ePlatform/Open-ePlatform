<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ContactDetailQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Kontaktuppgiftsfr�ga (privatperson)</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">F�rnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportCareOf">C/O</xsl:variable>
	<xsl:variable name="java.exportAddress">Adress</xsl:variable>
	<xsl:variable name="java.exportZipCode">Postnummer</xsl:variable>
	<xsl:variable name="java.exportPostalAddress">Ort</xsl:variable>
	<xsl:variable name="java.exportPhone">Telefon</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportCitizenID">Personnummer</xsl:variable>
	
	<xsl:variable name="i18n.maxLength">till�ten l�ngd p� textinneh�ll</xsl:variable>
	
	<xsl:variable name="i18n.ContactDetailQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.AllowSMS">Till�t notifieringar via SMS</xsl:variable>
	
	<xsl:variable name="i18n.ContactChannelSettings">Inst�llningar</xsl:variable>
	
	<xsl:variable name="i18n.RequireAddress">Kr�v postadress</xsl:variable>
	
	<xsl:variable name="i18n.ShowCitizenIDField">Visa f�lt f�r personnummer</xsl:variable>
	<xsl:variable name="i18n.RequireEmail">Kr�v e-post</xsl:variable>
	<xsl:variable name="i18n.RequirePhone">Kr�v telefon</xsl:variable>
	<xsl:variable name="i18n.RequireMobilePhone">Kr�v mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.OfficalAddress">L�s postadressf�lten till folkbokf�ringsadressen om denna finns tillg�nglig</xsl:variable>
	<xsl:variable name="i18n.DisableProfileUpdate">D�lj kryssruta f�r uppdatering av kontaktuppgifter</xsl:variable>
	<xsl:variable name="i18n.RequireAtLeastOneContactWay">Kr�v antingen e-post eller mobiltelefon</xsl:variable>
	
	<xsl:variable name="i18n.Fields">F�lt</xsl:variable>
	<xsl:variable name="i18n.Field.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.Field.Visible">Valfritt</xsl:variable>
	<xsl:variable name="i18n.Field.Required">Obligatoriskt</xsl:variable>
	
	<xsl:variable name="i18n.CitizenID">Personnummer</xsl:variable>
	<xsl:variable name="i18n.FirstAndLastname">Namn</xsl:variable>
	<xsl:variable name="i18n.Address">Postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.CareOf">C/O</xsl:variable>
	
	<xsl:variable name="i18n.ProfileUpdate">Uppdatera kontaktuppgifter under mina uppgifter</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Always">Alltid</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Ask">Valfritt</xsl:variable>
	<xsl:variable name="i18n.FieldUpdate.Never">Aldrig</xsl:variable>
	<xsl:variable name="i18n.ManagerUpdateAccess">Handl�ggares �ndringar i kontaktuppgiftsfr�gan uppdaterar anv�ndarens kontaktinformation i mina sidor</xsl:variable>
	
</xsl:stylesheet>
