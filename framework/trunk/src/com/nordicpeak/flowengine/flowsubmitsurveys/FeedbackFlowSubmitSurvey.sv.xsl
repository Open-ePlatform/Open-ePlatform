<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FeedbackFlowSubmitSurveyTemplates.xsl"/>
	
	<xsl:variable name="java.chartDataTitle">Hur nöjd är du med e-tjänsten?</xsl:variable>
	
	<xsl:variable name="i18n.FeedbackSurveyTitle">Hur nöjd är du med e-tjänsten?</xsl:variable>
	<xsl:variable name="i18n.VeryDissatisfied">Mycket missnöjd</xsl:variable>
	<xsl:variable name="i18n.Dissatisfied">Missnöjd</xsl:variable>
	<xsl:variable name="i18n.Neither">Varken eller</xsl:variable>
	<xsl:variable name="i18n.Satisfied">Nöjd</xsl:variable>
	<xsl:variable name="i18n.VerySatisfied">Mycket nöjd</xsl:variable>
	<xsl:variable name="i18n.Unkown">Okänt</xsl:variable>
	<xsl:variable name="i18n.LeaveComment">Lämna en kommentar</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.Send">Skicka betyg</xsl:variable>
	<xsl:variable name="i18n.CommentPlaceHolder">Lämna gärna en kommentar till ditt betyg</xsl:variable>
	<xsl:variable name="i18n.FeedbackSurveySuccess">Ditt betyg är registrerat, tack för din medverkan</xsl:variable>
	<xsl:variable name="i18n.NoAnswer">Du måste välja ett betyg för att kunna skicka in</xsl:variable>
	<xsl:variable name="i18n.ShowComments">Visa betyg med kommentarer</xsl:variable>
	<xsl:variable name="i18n.HideComments">Dölj betyg med kommentarer</xsl:variable>
	<xsl:variable name="i18n.Answer">Betyg</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowFeedbackSurveys">Den här versionen av e-tjänsten har ännu inga betyg</xsl:variable>

	<xsl:variable name="i18n.validationError.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>

</xsl:stylesheet>
