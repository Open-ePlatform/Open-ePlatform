<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="classpath://com/nordicpeak/flowengine/queries/common/xsl/QueryAdminCommon.sv.xsl"/>

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="ChildQueryAdminTemplates.xsl"/>
	
	<xsl:variable name="java.queryTypeName">Val av barn</xsl:variable>
	<xsl:variable name="java.childSelectedAlternativeName">Barn valt</xsl:variable>
	<xsl:variable name="java.singleGuardianAlternativeName">Ensam v�rdnad</xsl:variable>
	<xsl:variable name="java.multiGuardianAlternativeName">Gemensam  v�rdnad</xsl:variable>
	
	<xsl:variable name="java.exportChildCitizenName">Barnets namn</xsl:variable>
	<xsl:variable name="java.exportChildCitizenIdentifier">Barnets personnummer</xsl:variable>
	<xsl:variable name="java.exportChildAdress">Barnets address</xsl:variable>
	<xsl:variable name="java.exportChildPostalAdress">Barnets postort</xsl:variable>
	<xsl:variable name="java.exportChildZipCode">Barnets postnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianName">Andra v�rdnadshavarens namn</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianEmail">Andra v�rdnadshavarens e-post</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPhone">Andra v�rdnadshavarens telefon</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianCitizenIdentifier">Andra v�rdnadshavarens personnummer</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianAdress">Andra v�rdnadshavarens address</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianPostalAdress">Andra v�rdnadshavarens postort</xsl:variable>
	<xsl:variable name="java.exportOtherGuardianZipCode">Andra v�rdnadshavarens postnummer</xsl:variable>
	
	<xsl:variable name="i18n.UpdateQuery">�ndra fr�ga</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.ChildQueryNotFound">Den beg�rda fr�gan hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.OtherGuardiansDescription">Beskrivning f�r andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.UseMultipartSigning">Anv�nd flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.AlwaysShowOtherGuardians">Visa alltid andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.ShowAddress">Visa folkbokf�ringsaddress f�r barn</xsl:variable>
	<xsl:variable name="i18n.ShowGuardianAddress">Visa folkbokf�ringsaddress f�r v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWays">Kr�vda kontaktv�gar f�r andra v�rdnadshavare</xsl:variable>
	<xsl:variable name="i18n.RequiredContactWayVerification">Kr�v verifiering av kontaktv�gar</xsl:variable>
	<xsl:variable name="i18n.MinChildAge">L�gsta �lder p� barn</xsl:variable>
	<xsl:variable name="i18n.MaxChildAge">H�gsta �lder p� barn</xsl:variable>
	<xsl:variable name="i18n.MinAgeLargerThanMaxAge">Minsta �lder kan inte vara st�rre �n h�gsta �lder</xsl:variable>
	<xsl:variable name="i18n.SetMultipartsAsOwners">S�tt andra v�rdnadshavaren som meds�kande f�r �rendet</xsl:variable>
	
	<xsl:variable name="i18n.ContactWays.Either">Antingen e-post eller mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Phone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ContactWays.Both">B�de e-post och mobiltelefon</xsl:variable>
</xsl:stylesheet>
