<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FeedbackFlowSubmitSurveyTemplates.xsl"/>
	
	<xsl:variable name="java.chartDataTitle">Hur nöjd är du med e-tjänsten?</xsl:variable>
	<xsl:variable name="java.extensionViewTitle">Användarundersökning</xsl:variable>
	<xsl:variable name="java.flowEventDescription">Användarundersökning</xsl:variable>
	
	<xsl:variable name="java.feedbackFlowSurveyGlobalEmailSubject">Kommentarer från användarundersökningar för e-tjänst $flow.name</xsl:variable>
	<xsl:variable name="java.feedbackFlowSurveyGlobalEmailMessage">
	
		&lt;p&gt;Hej!&lt;/p&gt;
		
		&lt;p&gt;Det finns nya kommentarer för e-tjänst $flow.name i dessa version(er): $flow.version.&lt;/p&gt;
		&lt;p&gt;$flow.url&lt;/p&gt;
		
		
	</xsl:variable>
	
	
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
	<xsl:variable name="i18n.NoCommentFound">Ingen kommentar angiven.</xsl:variable>
	<xsl:variable name="i18n.CommentDeleted">Kommentar borttagen </xsl:variable>
	<xsl:variable name="i18n.CommentDeletedBy"> av </xsl:variable>
	<xsl:variable name="i18n.Answer">Betyg</xsl:variable>
	<xsl:variable name="i18n.DeleteComment.Confirm">Är du säker att du vill ta bort vald kommentar</xsl:variable>
	<xsl:variable name="i18n.DeleteComment.Inactive">Finns ingen kommentar att ta bort.</xsl:variable>
	<xsl:variable name="i18n.DeleteComment">Ta bort vald kommentar</xsl:variable>
	
	<xsl:variable name="i18n.SettingsTitle">Inställningar</xsl:variable>
	<xsl:variable name="i18n.UpdateSettingsTitle">Uppdatera inställningar</xsl:variable>
	<xsl:variable name="i18n.SendNotification">Skicka notifikation vid kommentar</xsl:variable>
	
	<xsl:variable name="i18n.SendNotification.Description">Notis skickas varje måndag klockan 8 och innehåller namnet på e-tjänsten och vilka versioner av den e-tjänsten som nya kommentarer skickats in till. Notisen innehåller även en länk till den senaste versionen av e-tjänsten.</xsl:variable>
	
	<xsl:variable name="i18n.NotificationEmails">Ange mottagande e-postadresser. En per rad.</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">Ändra inställningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inställningar</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara inställningar</xsl:variable>
	<xsl:variable name="i18n.SendsNotifications">Skicka notifikationer vid kommentar</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowFeedbackSurveys">Den här versionen av e-tjänsten har ännu inga betyg</xsl:variable>

	<xsl:variable name="i18n.validationError.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>

</xsl:stylesheet>
