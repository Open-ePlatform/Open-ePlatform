<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="StandardFlowNotificationHandlerTemplates.xsl"/>
	
	<xsl:variable name="java.FlowInstance">Ärende</xsl:variable>
	
	<xsl:variable name="java.statusChangedUserSMS">Statusen för ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har ändrats till $status.name</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedUserSMS">Du har fått ett nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserSMS">Ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedUserSMS">Handläggningen av ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har slutförts.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedNotLoggedInUserSMS">Ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedNotLoggedInUserSMS">Handläggningen av ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har slutförts.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserSMS">Hej $signingParty.firstname, du har ombets att signera ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname. Använd följande länk för att visa ärendet: $flowInstanceSign.url</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserSMS">Hej $signingParty.firstname, $contact.firstname $contact.lastname har valt att avbryta signeringen av ärende $flow.name (ärende nr. $flowInstance.flowInstanceID) som du tidigare blev ombedd att signera. Det kan komma en ny signeringsförfrågan om ärendet skickas in på nytt.</xsl:variable>		
	
	<xsl:variable name="java.statusChangedUserEmailSubject">Ny status för ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.statusChangedUserEmailMessage">
		
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Statusen på ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har ändrats till $status.name.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.externalMessageReceivedUserEmailSubject">Nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har fått ett nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailSubject">Ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Här kommer en bekräftelse på att ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedNotLoggedInUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Här kommer en bekräftelse på att ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedUserEmailPDFAttachedText">Din ansökan är bifogad i PDF-format.</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedUserEmailPDFSizeLimitExceededText">Din ansökan var för stor och kunde inte bifogas i detta e-postmeddelande.</xsl:variable>
	
	<xsl:variable name="java.flowInstanceArchivedUserEmailSubject">Handläggningen av ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har slutförts.</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Handläggningen av ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har slutförts.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedNotLoggedInUserEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;Handläggningen av ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har slutförts.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserEmailSubject">Signering av ärende $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserEmailMessage">
	
		&lt;p&gt;Hej $signingParty.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har ombets att signera ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstanceSign.url"&gt;$flowInstanceSign.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserEmailSubject">Signering av ärende $flow.name (ärende nr. $flowInstance.flowInstanceID) avbruten</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserEmailMessage">
	
		&lt;p&gt;Hej $signingParty.firstname,&lt;/p&gt;
		
		&lt;p&gt;$contact.firstname $contact.lastname har valt att avbryta signeringen av ärende $flow.name (ärende nr. $flowInstance.flowInstanceID) som du tidigare blev ombedd att signera.&lt;/p&gt;
		
		&lt;p&gt;Det kan komma en ny signeringsförfrågan om ärendet skickas in på nytt.&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.externalMessageReceivedManagerEmailSubject">Nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har fått ett nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceAssignedManagerEmailSubject">Tilldelad ärende $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceAssignedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har tilldelats ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.statusChangedManagerEmailSubject">Statusen för ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) har ändrats till $status.name</xsl:variable>
	<xsl:variable name="java.statusChangedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Statusen på ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID) som du är tilldelad har ändrats till $status.name.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedManagerEmailSubject">Nytt ärende $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedManagerEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt ärende, $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in av $contact.firstname $contact.lastname&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceSubmittedGlobalEmailSubject">Nytt ärende $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceSubmittedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt ärende, $flow.name (ärende nr. $flowInstance.flowInstanceID) har skickats in av $contact.firstname $contact.lastname&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>	
	
	<xsl:variable name="java.externalMessageReceivedGlobalEmailSubject">Nytt meddelande kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett nytt meddelande har skickats in kopplat till ärendet $flow.name (ärende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>	
	
	<xsl:variable name="java.managerMentionedEmailSubject">$poster.firstname $poster.lastname har nämnt dig i en kommentar</xsl:variable>
	<xsl:variable name="java.managerMentionedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;

		&lt;p&gt;$poster.firstname $poster.lastname har nämnt dig i en kommentar i interna meddelanden på ärendet $flow.name med ärendenummer $flowInstance.flowInstanceID.&lt;/p&gt;
		
		&lt;p&gt;Klicka på länken nedan för att visa ärendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.managerExpiredGlobalEmailSubject">Handläggarbehörigheten för $manager.firstname $manager.lastname kopplat till e-tjänsten $flow.name har upphört</xsl:variable>
	<xsl:variable name="java.managerExpiredGlobalEmailMessage">
	
		&lt;p&gt;Handläggarbehörigheten för $manager.firstname $manager.lastname kopplat till e-tjänsten $flow.name har upphört.&lt;/p&gt;

		&lt;p&gt;Klicka på länkarna nedan för att visa ärendena som berörs (OBS detta förutsätter att du har rätt att handlägga ärenden för e-tjänsten $flow.name)&lt;/p&gt;
		&lt;p&gt;$flowInstances&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="i18n.UserNotifications">Notifikationer till medborgare</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserSMS">SMS vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserSMS">SMS vid nytt meddelande från handläggare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserSMS">SMS vid inskickat ärende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserSMS">SMS vid avslutat ärende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserEmail">E-post vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserEmail">E-post vid nytt meddelande från handläggare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserEmail">E-post vid inskickat ärende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserEmail">E-post vid avslutat ärende</xsl:variable>
	<xsl:variable name="i18n.ManagerNotifications">Notifikationer till handläggare</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedManagerEmail">E-post till tilldelade handläggare vid nytt meddelande från medborgare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceAssignedManagerEmail">E-post vid tilldelning av ärende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedManagerEmail">E-post till tilldelade handläggare vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedManagerEmail">E-post till samtliga behöriga handläggare när nya ärenden skickas in</xsl:variable>
	<xsl:variable name="i18n.NoNotificationsEnabled">Inga notifikationer är aktiverade för denna e-tjänst</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailSubject">Rubrik på e-postmeddelande vid inskickat ärende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailMessage">Innehåll i e-postmeddelande vid inskickat ärende (inloggad användare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedNotLoggedInUserEmailMessage">Innehåll i e-postmeddelande vid inskickat ärende (ej inloggad användare)</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailSubject">Rubrik på e-postmeddelande vid avslutat ärende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailMessage">Innehåll i e-postmeddelande vid avslutat ärende (inloggad användare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedNotLoggedInUserEmailMessage">Innehåll i e-postmeddelande vid avslutat ärende (ej inloggad användare)</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>

	<xsl:variable name="i18n.ToggleTexts">[Visa/dölj texter]</xsl:variable>
	
	<xsl:variable name="i18n.UserTagsTable.description">Följande taggar kan användas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.FlowNameTag">E-tjänsten namn</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceIDTag">Ärende nummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceURLTag">Adress till ärendet (kan ej användas i meddelande till ej inloggade användare)</xsl:variable>
	<xsl:variable name="i18n.StatusTag">Ärende status</xsl:variable>
	<xsl:variable name="i18n.PosterFirstnameTag">Kontaktpersonens förnamn</xsl:variable>
	<xsl:variable name="i18n.PosterLastnameTag">Kontaktpersonens efternamn</xsl:variable>
	<xsl:variable name="i18n.FLowInstanceAttachedPDFTextTag">Text som beskriver bifogad PDF-ansökan</xsl:variable>
	
	<xsl:variable name="i18n.GlobalNotifications">Generella notifikationer</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedGlobalEmail">E-post när nya ärenden skickas in</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAddresses">E-postadresser för notifiering om nya ärenden (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDF">Bifoga ärendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachXML">Bifoga ärendet i XML-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately">Bifoga bilagor separat</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailMessage">Innehåll på e-postmeddelande till myndighetsbrevlåda vid inskickat ärende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailSubject">Rubrik på e-postmeddelande till myndighetsbrevlåda vid inskickat ärende</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailAttachPDF">Bifoga ärendet i PDF-format</xsl:variable>
	
	<xsl:variable name="i18n.SendExternalMessageReceivedGlobalEmail">E-post vid nytt meddelande från medborgare</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedGlobalEmailAddresses">E-postadresser för notifiering om nya meddelanden från medborgare (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.SendManagerExpiredGlobalEmail">E-post vid upphörd handläggarbehörighet</xsl:variable>
	<xsl:variable name="i18n.ManagerExpiredGlobalEmailAddresses">E-postadresser för notifiering om upphörd handläggarbehörighet (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.StatusChangedUserEmailSubject">Rubrik på e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedUserEmailMessage">Innehåll i e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailSubject">Rubrik på e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailMessage">Innehåll i e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerSubject">Rubrik på e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerMessage">Innehåll i e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.StatusDescriptionTag">Beskrivning för ärendets status</xsl:variable>
</xsl:stylesheet>
