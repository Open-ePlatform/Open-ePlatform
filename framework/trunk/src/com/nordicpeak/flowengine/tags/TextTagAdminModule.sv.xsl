<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="TextTagAdminModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.TextTagDescription">
		Taggar anv�nds f�r att kunna �teranv�nda texter i e-tj�nsteplattformen. L�s mer om hur du anv�nder taggar under "Hj�lp" nedan.
		<br/>
		<strong><font color="red">OBSERVERA:</font></strong> vid �ndring i befintlig tagg sl�r det �ndrade tagg-inneh�llet igenom direkt i alla ber�rda e-tj�nster liksom alla tidigare anv�nda versioner d�r taggen har anv�nts. Om taggens v�rde �ven anv�nts och visats i ett �rende s� kommer v�rdet att sl� igenom �ven i �rendet som finns p� anv�ndarens Mina sidor. Det enda st�lle som taggens nya v�rde INTE p�verkar �r de pdf:er som finns under �rendehistoriken.
	</xsl:variable>
	
	<xsl:variable name="i18n.Name">Taggens namn</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.NoTextTags">Det finns inga taggar</xsl:variable>
	<xsl:variable name="i18n.AddTextTag">L�gg till tagg</xsl:variable>
	<xsl:variable name="i18n.Update">�ndra</xsl:variable>
	<xsl:variable name="i18n.DeleteTextTagConfirm">�r du s�ker p� att du vill ta bort taggen</xsl:variable>
	<xsl:variable name="i18n.Add">L�gg till</xsl:variable>
	<xsl:variable name="i18n.CancelConfirm">�r du s�ker p� att du vill avbryta utan att spara</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.UpdateTextTag">�ndra tagg</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.TextTagType">Formul�rtyp</xsl:variable>
	<xsl:variable name="i18n.TextField">Textf�lt</xsl:variable>
	<xsl:variable name="i18n.Editor">Editor</xsl:variable>
	<xsl:variable name="i18n.DefaultValue">Standardv�rde</xsl:variable>
	<xsl:variable name="i18n.name">taggens namn</xsl:variable>
	<xsl:variable name="i18n.description">beskrivning</xsl:variable>
	<xsl:variable name="i18n.textTagType">formul�rtyp</xsl:variable>
	<xsl:variable name="i18n.defaultValue">standardv�rde</xsl:variable>
	<xsl:variable name="i18n.TextTagNameExists">Det finns redan en tagg med det angivna namnet</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedTextTagNotFound">Den beg�rda taggen hittades inte</xsl:variable>
	
	<xsl:variable name="i18n.TextTagTitle.Part1">Det finns</xsl:variable>
	<xsl:variable name="i18n.TextTagTitle.Part2">tagg</xsl:variable>
	<xsl:variable name="i18n.TextTagTitle.Part2.Plural">taggar</xsl:variable>

	<xsl:variable name="i18n.Help">Hj�lp</xsl:variable>
	<xsl:variable name="i18n.TextTagAdminHelp">Inkludera taggar i texter genom att skriva: ${taggens namn}.</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett ok�nt fel har uppst�tt'"/>
	
	<xsl:variable name="i18n.Filter">S�k</xsl:variable>
	<xsl:variable name="i18n.Preview">Visa</xsl:variable>
	<xsl:variable name="i18n.ShareTextTags">Dela taggar</xsl:variable>
	
	<xsl:variable name="i18n.SelectTargets.Title">Dela taggar</xsl:variable>
	<xsl:variable name="i18n.SelectTargets.Description">V�lj till vilka milj�er som du vill dela de valda taggarna.</xsl:variable>
	<xsl:variable name="i18n.SelectTargets.Submit">Dela</xsl:variable>
	
	<xsl:variable name="i18n.SelectTargets.Settings">Inst�llningar</xsl:variable>
	<xsl:variable name="i18n.SelectTargets.Overwrite">Skriv �ver v�rdet om taggarna finns sen tidigare</xsl:variable>
</xsl:stylesheet>
