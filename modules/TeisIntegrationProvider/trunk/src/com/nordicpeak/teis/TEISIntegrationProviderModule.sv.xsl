<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="TargetGroupAdminModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="i18n.TargetGroups">Målgrupper</xsl:variable>
	<xsl:variable name="i18n.NoFlowTargetGroups">Den här e-tjänsten är ännu inte kopplad till någon målgrupp</xsl:variable>
	<xsl:variable name="i18n.TargetGroupDescription">E-tjänstens målgrupper</xsl:variable>
	<xsl:variable name="i18n.ChooseTargetGroups">Välj målgrupper</xsl:variable>
	<xsl:variable name="i18n.UpdateTargetGroupsTitle">Uppdatera målgrupper för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.Save">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.ListTargetGroupsTitle">Målgrupper</xsl:variable>
	<xsl:variable name="i18n.NoTargetGroupsFound">Det finns ännu inga målgrupper</xsl:variable>
	<xsl:variable name="i18n.AddTargetGroup">Lägg till målgrupp</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.name">namn</xsl:variable>
	<xsl:variable name="i18n.Flow">Antal e-tjänster</xsl:variable>
	<xsl:variable name="i18n.UpdateTargetGroup">Ändra målgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteTargetGroupConfirm">Är du säker på att du vill ta bort målgruppen</xsl:variable>
	<xsl:variable name="i18n.DeleteTargetGroup">Ta bort målgrupp</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.Flows">E-tjänster</xsl:variable>
	
	<xsl:variable name="i18n.SearchFlowPlaceholder">Lägg till e-tjänst</xsl:variable>
	<xsl:variable name="i18n.RemoveFlow">Ta bort e-tjänsten från den här målgruppen</xsl:variable>

	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>

</xsl:stylesheet>
