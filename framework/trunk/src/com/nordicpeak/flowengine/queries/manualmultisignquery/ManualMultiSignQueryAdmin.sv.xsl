<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>
	<xsl:include href="ManualMultiSignQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Flerpartssigneringsfr�ga</xsl:variable>
	<xsl:variable name="java.queryDescription">
Fr�getypen Flerpartssigneringsfr�ga anv�nds n�r �rendet skall signeras av flera parter. Anv�ndaren f�r i e-tj�nsten ange f�rnamn, efternamn, personnummer, e-post och/eller telefonnummer p� den person
som skall signera �rendet. Om flera personer skall signera �rendet skapas flera fr�gor av denna typ. F�r att flerpartssignering skall aktiveras m�ste &quot;Kr�v signering&quot; vara aktiverad p� e-tj�nsten.
	</xsl:variable>
	
	<xsl:variable name="java.exportFirstName">F�rnamn</xsl:variable>
	<xsl:variable name="java.exportLastName">Efternamn</xsl:variable>
	<xsl:variable name="java.exportEmail">E-post</xsl:variable>
	<xsl:variable name="java.exportMobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="java.exportSocialSecurityNumber">Personnummer</xsl:variable>
	<xsl:variable name="i18n.hideCitizenIdetifierInPDF">D�lj personnummer i PDF</xsl:variable>
	<xsl:variable name="i18n.AdvancedSettings">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">S�tt part som s�kande f�r �rendet</xsl:variable>
	
	<xsl:variable name="i18n.ManualMultiSignQueryNotFound">Den beg�rda fr�gan hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.setAsAttribute">Spara f�ltens v�rde som attribut</xsl:variable>
	<xsl:variable name="i18n.attributeName">Attributprefix</xsl:variable>
	<xsl:variable name="i18n.attributeDescription">F�ljande attribut kommer att sparas med det ovan valda prefixet. Anv�ndningsexempel om du valt prefixet 'test': $attribute{test.firstname}</xsl:variable>
	<xsl:variable name="i18n.attributes.name">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.attributes.value">V�rde</xsl:variable>
	
	<xsl:variable name="i18n.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.SocialSecurityNumber">Personnummer (����MMDDXXXX)</xsl:variable>
	
</xsl:stylesheet>
