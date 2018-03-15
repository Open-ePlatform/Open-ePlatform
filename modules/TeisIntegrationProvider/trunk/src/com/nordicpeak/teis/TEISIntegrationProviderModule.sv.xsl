<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="TargetGroupAdminModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="i18n.TargetGroups">M�lgrupper</xsl:variable>
	<xsl:variable name="i18n.NoFlowTargetGroups">Den h�r e-tj�nsten �r �nnu inte kopplad till n�gon m�lgrupp</xsl:variable>
	<xsl:variable name="i18n.TargetGroupDescription">E-tj�nstens m�lgrupper</xsl:variable>
	<xsl:variable name="i18n.ChooseTargetGroups">V�lj m�lgrupper</xsl:variable>
	<xsl:variable name="i18n.UpdateTargetGroupsTitle">Uppdatera m�lgrupper f�r e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.Save">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.ListTargetGroupsTitle">M�lgrupper</xsl:variable>
	<xsl:variable name="i18n.NoTargetGroupsFound">Det finns �nnu inga m�lgrupper</xsl:variable>
	<xsl:variable name="i18n.AddTargetGroup">L�gg till m�lgrupp</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.name">namn</xsl:variable>
	<xsl:variable name="i18n.Flow">Antal e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.UpdateTargetGroup">�ndra m�lgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteTargetGroupConfirm">�r du s�ker p� att du vill ta bort m�lgruppen</xsl:variable>
	<xsl:variable name="i18n.DeleteTargetGroup">Ta bort m�lgrupp</xsl:variable>
	
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.Flows">E-tj�nster</xsl:variable>
	
	<xsl:variable name="i18n.SearchFlowPlaceholder">L�gg till e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.RemoveFlow">Ta bort e-tj�nsten fr�n den h�r m�lgruppen</xsl:variable>

	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>

</xsl:stylesheet>
