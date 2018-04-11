<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="StandardFlowNotificationHandlerTemplates.xsl"/>
	
	<xsl:variable name="java.FlowInstance">�rende</xsl:variable>
	
	<xsl:variable name="java.statusChangedUserSMS">Statusen f�r �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har �ndrats till $status.name</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedUserSMS">Du har f�tt ett nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserSMS">�rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedUserSMS">Handl�ggningen av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har slutf�rts.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedNotLoggedInUserSMS">�rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedNotLoggedInUserSMS">Handl�ggningen av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har slutf�rts.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserSMS">Hej $signingParty.firstname, du har ombets att signera �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname. Anv�nd f�ljande l�nk f�r att visa �rendet: $flowInstanceSign.url</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserSMS">Hej $signingParty.firstname, $contact.firstname $contact.lastname har valt att avbryta signeringen av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) som du tidigare blev ombedd att signera. Det kan komma en ny signeringsf�rfr�gan om �rendet skickas in p� nytt.</xsl:variable>		
	
	<xsl:variable name="java.statusChangedUserEmailSubject">Ny status f�r �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.statusChangedUserEmailMessage">
		
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Statusen p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har �ndrats till $status.name.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.externalMessageReceivedUserEmailSubject">Nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt ett nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailSubject">�rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;H�r kommer en bekr�ftelse p� att �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedNotLoggedInUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;H�r kommer en bekr�ftelse p� att �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailPDFAttachedText">Din ans�kan �r bifogad i PDF-format.</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedUserEmailPDFSizeLimitExceededText">Din ans�kan var f�r stor och kunde inte bifogas i detta e-postmeddelande.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceArchivedUserEmailSubject">Handl�ggningen av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har slutf�rts.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Handl�ggningen av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har slutf�rts.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedNotLoggedInUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Handl�ggningen av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har slutf�rts.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserEmailSubject">Signering av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserEmailMessage">
	
		&lt;p&gt;Hej $signingParty.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har ombets att signera �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstanceSign.url"&gt;$flowInstanceSign.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserEmailSubject">Signering av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) avbruten</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserEmailMessage">
	
		&lt;p&gt;Hej $signingParty.firstname,&lt;/p&gt;
		
		&lt;p&gt;$contact.firstname $contact.lastname har valt att avbryta signeringen av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) som du tidigare blev ombedd att signera.&lt;/p&gt;
		
		&lt;p&gt;Det kan komma en ny signeringsf�rfr�gan om �rendet skickas in p� nytt.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.externalMessageReceivedManagerEmailSubject">Nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt ett nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceAssignedManagerEmailSubject">Tilldelad �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceAssignedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har tilldelats �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.statusChangedManagerEmailSubject">Statusen f�r �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har �ndrats till $status.name</xsl:variable>
	<xsl:variable name="java.statusChangedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Statusen p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som du �r tilldelad har �ndrats till $status.name.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedManagerEmailSubject">Nytt �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedManagerEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt �rende, $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in av $contact.firstname $contact.lastname&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedGlobalEmailSubject">Nytt �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt �rende, $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in av $contact.firstname $contact.lastname&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>	
	
	<xsl:variable name="java.externalMessageReceivedGlobalEmailSubject">Nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt meddelande har skickats in kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>	
	
	<xsl:variable name="java.managerMentionedEmailSubject">$poster.firstname $poster.lastname har n�mnt dig i en kommentar</xsl:variable>
	<xsl:variable name="java.managerMentionedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;

		&lt;p&gt;$poster.firstname $poster.lastname har n�mnt dig i en kommentar i interna meddelanden p� �rendet $flow.name med �rendenummer $flowInstance.flowInstanceID.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.managerExpiredGlobalEmailSubject">Handl�ggarbeh�righeten f�r $manager.firstname $manager.lastname kopplat till e-tj�nsten $flow.name har upph�rt</xsl:variable>
	<xsl:variable name="java.managerExpiredGlobalEmailMessage">
	
		&lt;p&gt;Handl�ggarbeh�righeten f�r $manager.firstname $manager.lastname kopplat till e-tj�nsten $flow.name har upph�rt.&lt;/p&gt;

		&lt;p&gt;Klicka p� l�nkarna nedan f�r att visa �rendena som ber�rs (OBS detta f�ruts�tter att du har r�tt att handl�gga �renden f�r e-tj�nsten $flow.name)&lt;/p&gt;
		&lt;p&gt;$flowInstances&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.UserNotifications">Notifikationer till medborgare</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserSMS">SMS vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserSMS">SMS vid nytt meddelande fr�n handl�ggare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserSMS">SMS vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserSMS">SMS vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserEmail">E-post vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserEmail">E-post vid nytt meddelande fr�n handl�ggare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserEmail">E-post vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserEmail">E-post vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.ManagerNotifications">Notifikationer till handl�ggare</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedManagerEmail">E-post till tilldelade handl�ggare vid nytt meddelande fr�n medborgare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceAssignedManagerEmail">E-post vid tilldelning av �rende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedManagerEmail">E-post till tilldelade handl�ggare vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedManagerEmail">E-post till samtliga beh�riga handl�ggare n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.NoNotificationsEnabled">Inga notifikationer �r aktiverade f�r denna e-tj�nst</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailSubject">Rubrik p� e-postmeddelande vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailMessage">Inneh�ll i e-postmeddelande vid inskickat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedNotLoggedInUserEmailMessage">Inneh�ll i e-postmeddelande vid inskickat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailSubject">Rubrik p� e-postmeddelande vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailMessage">Inneh�ll i e-postmeddelande vid avslutat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedNotLoggedInUserEmailMessage">Inneh�ll i e-postmeddelande vid avslutat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>

	<xsl:variable name="i18n.ToggleTexts">[Visa/d�lj texter]</xsl:variable>
	
	<xsl:variable name="i18n.UserTagsTable.description">F�ljande taggar kan anv�ndas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.FlowNameTag">E-tj�nsten namn</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceIDTag">�rende nummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceURLTag">Adress till �rendet (kan ej anv�ndas i meddelande till ej inloggade anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.StatusTag">�rende status</xsl:variable>
	<xsl:variable name="i18n.PosterFirstnameTag">Kontaktpersonens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.PosterLastnameTag">Kontaktpersonens efternamn</xsl:variable>
	<xsl:variable name="i18n.FLowInstanceAttachedPDFTextTag">Text som beskriver bifogad PDF-ans�kan</xsl:variable>
	
	<xsl:variable name="i18n.GlobalNotifications">Generella notifikationer</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedGlobalEmail">E-post n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAddresses">E-postadresser f�r notifiering om nya �renden (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDF">Bifoga �rendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachXML">Bifoga �rendet i XML-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately">Bifoga bilagor separat</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailMessage">Inneh�ll p� e-postmeddelande till myndighetsbrevl�da vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailSubject">Rubrik p� e-postmeddelande till myndighetsbrevl�da vid inskickat �rende</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailAttachPDF">Bifoga �rendet i PDF-format</xsl:variable>
	
	<xsl:variable name="i18n.SendExternalMessageReceivedGlobalEmail">E-post vid nytt meddelande fr�n medborgare</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedGlobalEmailAddresses">E-postadresser f�r notifiering om nya meddelanden fr�n medborgare (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.SendManagerExpiredGlobalEmail">E-post vid upph�rd handl�ggarbeh�righet</xsl:variable>
	<xsl:variable name="i18n.ManagerExpiredGlobalEmailAddresses">E-postadresser f�r notifiering om upph�rd handl�ggarbeh�righet (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.StatusChangedUserEmailSubject">Rubrik p� e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedUserEmailMessage">Inneh�ll i e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailSubject">Rubrik p� e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailMessage">Inneh�ll i e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerSubject">Rubrik p� e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerMessage">Inneh�ll i e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.StatusDescriptionTag">Beskrivning f�r �rendets status</xsl:variable>
</xsl:stylesheet>
