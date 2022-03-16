<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FeedbackFlowSubmitSurveyTemplates.xsl"/>
	
	<xsl:variable name="java.chartDataTitle">Hur n�jd �r du med e-tj�nsten?</xsl:variable>
	<xsl:variable name="java.extensionViewTitle">Anv�ndarunders�kning</xsl:variable>
	<xsl:variable name="java.flowEventDescription">Anv�ndarunders�kning</xsl:variable>
	
	<xsl:variable name="java.feedbackFlowSurveyGlobalEmailSubject">Kommentarer fr�n anv�ndarunders�kningar f�r e-tj�nst $flow.name</xsl:variable>
	<xsl:variable name="java.feedbackFlowSurveyGlobalEmailMessage">
	
		&lt;p&gt;Hej!&lt;/p&gt;
		
		&lt;p&gt;Det finns nya kommentarer f�r e-tj�nst $flow.name i dessa version(er): $flow.version.&lt;/p&gt;
		&lt;p&gt;$flow.url&lt;/p&gt;
		
		
	</xsl:variable>
	
	
	<xsl:variable name="i18n.FeedbackSurveyTitle">Hur n�jd �r du med e-tj�nsten?</xsl:variable>
	<xsl:variable name="i18n.VeryDissatisfied">Mycket missn�jd</xsl:variable>
	<xsl:variable name="i18n.Dissatisfied">Missn�jd</xsl:variable>
	<xsl:variable name="i18n.Neither">Varken eller</xsl:variable>
	<xsl:variable name="i18n.Satisfied">N�jd</xsl:variable>
	<xsl:variable name="i18n.VerySatisfied">Mycket n�jd</xsl:variable>
	<xsl:variable name="i18n.Unkown">Ok�nt</xsl:variable>
	<xsl:variable name="i18n.LeaveComment">L�mna en kommentar</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.Send">Skicka betyg</xsl:variable>
	<xsl:variable name="i18n.CommentPlaceHolder">L�mna g�rna en kommentar till ditt betyg</xsl:variable>
	<xsl:variable name="i18n.FeedbackSurveySuccess">Ditt betyg �r registrerat, tack f�r din medverkan</xsl:variable>
	<xsl:variable name="i18n.NoAnswer">Du m�ste v�lja ett betyg f�r att kunna skicka in</xsl:variable>
	<xsl:variable name="i18n.ShowComments">Visa betyg med kommentarer</xsl:variable>
	<xsl:variable name="i18n.HideComments">D�lj betyg med kommentarer</xsl:variable>
	<xsl:variable name="i18n.NoCommentFound">Ingen kommentar angiven.</xsl:variable>
	<xsl:variable name="i18n.CommentDeleted">Kommentar borttagen </xsl:variable>
	<xsl:variable name="i18n.CommentDeletedBy"> av </xsl:variable>
	<xsl:variable name="i18n.Answer">Betyg</xsl:variable>
	<xsl:variable name="i18n.DeleteComment.Confirm">�r du s�ker att du vill ta bort vald kommentar</xsl:variable>
	<xsl:variable name="i18n.DeleteComment.Inactive">Finns ingen kommentar att ta bort.</xsl:variable>
	<xsl:variable name="i18n.DeleteComment">Ta bort vald kommentar</xsl:variable>
	
	<xsl:variable name="i18n.SettingsTitle">Inst�llningar</xsl:variable>
	<xsl:variable name="i18n.UpdateSettingsTitle">Uppdatera inst�llningar</xsl:variable>
	<xsl:variable name="i18n.SendNotification">Skicka notifikation vid kommentar</xsl:variable>
	
	<xsl:variable name="i18n.SendNotification.Description">Notis skickas varje m�ndag klockan 8 och inneh�ller namnet p� e-tj�nsten och vilka versioner av den e-tj�nsten som nya kommentarer skickats in till. Notisen inneh�ller �ven en l�nk till den senaste versionen av e-tj�nsten.</xsl:variable>
	
	<xsl:variable name="i18n.NotificationEmails">Ange mottagande e-postadresser. En per rad.</xsl:variable>
	<xsl:variable name="i18n.UpdateSettings">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.DeleteSettings">Ta bort inst�llningar</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara inst�llningar</xsl:variable>
	<xsl:variable name="i18n.SendsNotifications">Skicka notifikationer vid kommentar</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowFeedbackSurveys">Den h�r versionen av e-tj�nsten har �nnu inga betyg</xsl:variable>

	<xsl:variable name="i18n.validationError.RequiredField">Du m�ste fylla i f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett ok�nt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>

</xsl:stylesheet>
