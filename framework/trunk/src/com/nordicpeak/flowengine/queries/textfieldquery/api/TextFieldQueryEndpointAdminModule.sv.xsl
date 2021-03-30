<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="TextFieldQueryEndpointAdminModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validation.requiredField">Du måste fylla i fältet:</xsl:variable>
	<xsl:variable name="i18n.validation.invalidFormat">Felaktigt format på fältet:</xsl:variable>
	<xsl:variable name="i18n.validation.tooShort">För kort innehåll i fältet:</xsl:variable>
	<xsl:variable name="i18n.validation.tooLong">För långt innehåll i fältet:</xsl:variable>
	<xsl:variable name="i18n.validation.unknownError">Okänt fel på fältet:</xsl:variable>
	<xsl:variable name="i18n.validation.unknownFault">Ett okänt valideringsfel har uppstått.</xsl:variable>
	<xsl:variable name="i18n.validation.EndpointNotFound">Den begärda API-källan hittades inte.</xsl:variable>
	
	<!-- Internationalization -->
	
	<xsl:variable name="i18n.ListEndpoints">API-källor för textfältsfrågor</xsl:variable>
	<xsl:variable name="i18n.NoEndpoints">Det finns inga API-källor upplagda</xsl:variable>
	
	<xsl:variable name="i18n.ShowEndpoint">API-källa</xsl:variable>
	<xsl:variable name="i18n.Add">Ny API-källa</xsl:variable>
	<xsl:variable name="i18n.AddEndpoint">Ny API-källa</xsl:variable>
	<xsl:variable name="i18n.Update">Ändra</xsl:variable>
	<xsl:variable name="i18n.UpdateEndpoint">Ändra API-källa</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.Back">Bakåt</xsl:variable>
	
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteConfirm">Vill du verkligen ta bort</xsl:variable>
	<xsl:variable name="i18n.DeleteDisabled">Går inte att ta bort då den används av textfältsfrågor</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Address">Adress (URL)</xsl:variable>
	<xsl:variable name="i18n.Username">Användarnamn</xsl:variable>
	<xsl:variable name="i18n.Password">Lösenord</xsl:variable>
	<xsl:variable name="i18n.Encoding">Teckenkodning</xsl:variable>
	<xsl:variable name="i18n.Query.fields">Fält (en per rad)</xsl:variable>
	<xsl:variable name="i18n.EndpointUsage">Användning i e-tjänster</xsl:variable>
	
	<xsl:variable name="i18n.Flow">E-tjänst</xsl:variable>
	<xsl:variable name="i18n.Version">Version</xsl:variable>
	<xsl:variable name="i18n.StepAndQuestion">Steg och fråga</xsl:variable>
	<xsl:variable name="i18n.NoQueries">API-källan används inte i några e-tjänster</xsl:variable>
	
	<xsl:variable name="i18n.TagsTable.description.part1">Exempel på en attributfiltrerad url som utnyttjar attribut/taggar:</xsl:variable>
	<xsl:variable name="i18n.DynamicURLExample">https://api.organisationen.se/anrop?fastparam=fastvärde&amp;dynamiskparam1=$attribute{mitt_attributnamn}&amp;dynamiskparam2=$user.attribute{citizenIdentifier}&amp;dynamiskparam3=$user.email</xsl:variable>
	<xsl:variable name="i18n.TagsTable.description.part2">Följande taggar kan användas, $user alltid avser den sökande medan $currentUser den inloggade användaren för tillfället ex. handläggare eller den sökande:</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.UserFirstnameTag">Användarens förnamn</xsl:variable>
	<xsl:variable name="i18n.UserLastnameTag">Användarens efternamn</xsl:variable>
	<xsl:variable name="i18n.UserEmailTag">Användarens e-postadress</xsl:variable>
	<xsl:variable name="i18n.UserUsernameTag">Användarens användarnamn (endast medarbetarkonto)</xsl:variable>
	<xsl:variable name="i18n.UserUserIDTag">Användarens användar-ID</xsl:variable>
	<xsl:variable name="i18n.UserCitizenIdentifierTag">Användarens personnummer</xsl:variable>
	<xsl:variable name="i18n.Tags.Attribute">Värde på attribut från ärende</xsl:variable>
	
</xsl:stylesheet>
