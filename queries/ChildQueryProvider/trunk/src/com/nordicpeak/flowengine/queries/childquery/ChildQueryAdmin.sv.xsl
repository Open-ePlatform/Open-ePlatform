<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="ChildQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Val av barn</xsl:variable>
	<xsl:variable name="java.childSelectedAlternativeName">Barn valt</xsl:variable>
	<xsl:variable name="java.singleGuardianAlternativeName">Ensam vårdnad</xsl:variable>
	<xsl:variable name="java.multiGuardianAlternativeName">Gemensam  vårdnad</xsl:variable>
	
	<xsl:variable name="java.exportChildCitizenName">Barnets namn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenIdentifier">Barnets personnummer</xsl:variable>
	<xsl:variable name="java.exportChildAdress">Barnets address</xsl:variable>
	<xsl:variable name="java.exportChildPostalAdress">Barnets postort</xsl:variable>
	<xsl:variable name="java.exportChildZipCode">Barnets postnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianName">Andra vårdnadshavarens namn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianEmail">Andra vårdnadshavarens e-post</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPhone">Andra vårdnadshavarens telefon</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianCitizenIdentifier">Andra vårdnadshavarens personnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianAdress">Andra vårdnadshavarens address</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPostalAdress">Andra vårdnadshavarens postort</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianZipCode">Andra vårdnadshavarens postnummer</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">Ändra fråga</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.ChildQueryNotFound">Den begärda frågan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OtherGuardiansDescription">Beskrivning för andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.UseMultipartSigning">Använd flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.AlwaysShowOtherGuardians">Visa alltid andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.ShowAddress">Visa folkbokföringsaddress för barn</xsl:variable>
	<xsl:variable name="i18n.ShowGuardianAddress">Visa folkbokföringsaddress för vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWays">Krävda kontaktvägar för andra vårdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWayVerification">Kräv verifiering av kontaktvägar</xsl:variable>
	<xsl:variable name="i18n.MinChildAge">Lägsta ålder på barn</xsl:variable>
	<xsl:variable name="i18n.MaxChildAge">Högsta ålder på barn</xsl:variable>
	<xsl:variable name="i18n.MinAgeLargerThanMaxAge">Minsta ålder kan inte vara större än högsta ålder</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">Sätt andra vårdnadshavaren som medsökande för ärendet</xsl:variable>
	
	<xsl:variable name="i18n.ContactWays.Either">Antingen e-post eller mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Both">Både e-post och mobiltelefon</xsl:variable>
</xsl:stylesheet>
