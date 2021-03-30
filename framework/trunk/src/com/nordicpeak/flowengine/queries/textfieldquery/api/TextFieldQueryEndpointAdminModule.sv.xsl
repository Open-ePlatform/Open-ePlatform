<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="TextFieldQueryEndpointAdminModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validation.requiredField">Du m�ste fylla i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.validation.invalidFormat">Felaktigt format p� f�ltet:</xsl:variable>
	<xsl:variable name="i18n.validation.tooShort">F�r kort inneh�ll i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.validation.tooLong">F�r l�ngt inneh�ll i f�ltet:</xsl:variable>
	<xsl:variable name="i18n.validation.unknownError">Ok�nt fel p� f�ltet:</xsl:variable>
	<xsl:variable name="i18n.validation.unknownFault">Ett ok�nt valideringsfel har uppst�tt.</xsl:variable>
	<xsl:variable name="i18n.validation.EndpointNotFound">Den beg�rda API-k�llan hittades inte.</xsl:variable>
	
	<!-- Internationalization -->
	
	<xsl:variable name="i18n.ListEndpoints">API-k�llor f�r textf�ltsfr�gor</xsl:variable>
	<xsl:variable name="i18n.NoEndpoints">Det finns inga API-k�llor upplagda</xsl:variable>
	
	<xsl:variable name="i18n.ShowEndpoint">API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Add">Ny API-k�lla</xsl:variable>
	<xsl:variable name="i18n.AddEndpoint">Ny API-k�lla</xsl:variable>
	<xsl:variable name="i18n.Update">�ndra</xsl:variable>
	<xsl:variable name="i18n.UpdateEndpoint">�ndra API-k�lla</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.Back">Bak�t</xsl:variable>
	
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteConfirm">Vill du verkligen ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteDisabled">G�r inte att ta bort d� den anv�nds av textf�ltsfr�gor</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Address">Adress (URL)</xsl:variable>
	<xsl:variable name="i18n.Username">Anv�ndarnamn</xsl:variable>
	<xsl:variable name="i18n.Password">L�senord</xsl:variable>
	<xsl:variable name="i18n.Encoding">Teckenkodning</xsl:variable>
	<xsl:variable name="i18n.Query.fields">F�lt (en per rad)</xsl:variable>
	<xsl:variable name="i18n.EndpointUsage">Anv�ndning i e-tj�nster</xsl:variable>
	
	<xsl:variable name="i18n.Flow">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.Version">Version</xsl:variable>
	<xsl:variable name="i18n.StepAndQuestion">Steg och fr�ga</xsl:variable>
	<xsl:variable name="i18n.NoQueries">API-k�llan anv�nds inte i n�gra e-tj�nster</xsl:variable>
	
	<xsl:variable name="i18n.TagsTable.description.part1">Exempel p� en attributfiltrerad url som utnyttjar attribut/taggar:</xsl:variable>
	<xsl:variable name="i18n.DynamicURLExample">https://api.organisationen.se/anrop?fastparam=fastv�rde&amp;dynamiskparam1=$attribute{mitt_attributnamn}&amp;dynamiskparam2=$user.attribute{citizenIdentifier}&amp;dynamiskparam3=$user.email</xsl:variable>
	<xsl:variable name="i18n.TagsTable.description.part2">F�ljande taggar kan anv�ndas, $user alltid avser den s�kande medan $currentUser den inloggade anv�ndaren f�r tillf�llet ex. handl�ggare eller den s�kande:</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.UserFirstnameTag">Anv�ndarens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.UserLastnameTag">Anv�ndarens efternamn</xsl:variable>
	<xsl:variable name="i18n.UserEmailTag">Anv�ndarens e-postadress</xsl:variable>
	<xsl:variable name="i18n.UserUsernameTag">Anv�ndarens anv�ndarnamn (endast medarbetarkonto)</xsl:variable>
	<xsl:variable name="i18n.UserUserIDTag">Anv�ndarens anv�ndar-ID</xsl:variable>
	<xsl:variable name="i18n.UserCitizenIdentifierTag">Anv�ndarens personnummer</xsl:variable>
	<xsl:variable name="i18n.Tags.Attribute">V�rde p� attribut fr�n �rende</xsl:variable>
	
</xsl:stylesheet>
