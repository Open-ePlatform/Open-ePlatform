<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ManualMultiSignQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Flerpartssigneringsfråga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Frågetypen Flerpartssigneringsfråga används när ärendet skall signeras av flera parter. Användaren får i e-tjänsten ange förnamn, efternamn, personnummer, e-post och/eller telefonnummer på den person
som skall signera ärendet. Om flera personer skall signera ärendet skapas flera frågor av denna typ. För att flerpartssignering skall aktiveras måste &quot;Kräv signering&quot; vara aktiverad på e-tjänsten.
	</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">Förnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportSocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.hideCitizenIdetifierInPDF">Dölj personnummer i PDF</xsl:variable>
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">Sätt part som sökande för ärendet</xsl:variable>
	
	<xsl:variable name="i18n.ManualMultiSignQueryNotFound">Den begärda frågan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.setAsAttribute">Spara fältens värde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">Följande attribut kommer att sparas med det ovan valda prefixet. Användningsexempel om du valt prefixet 'test': $attribute{test.firstname}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">Värde</xsl:variable>
	
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.SocialSecurityNumber">Personnummer (ÅÅÅÅMMDDXXXX)</xsl:variable>
	
</xsl:stylesheet>
