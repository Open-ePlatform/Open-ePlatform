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
	
	<xsl:variable name="java.flowInstanceMultiSignInitiatedUserSMS">Hej $signingParty.firstname, du har ombetts att signera �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname. Anv�nd f�ljande l�nk f�r att visa �rendet: $flowInstanceSign.url</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledUserSMS">Hej $signingParty.firstname, $contact.firstname $contact.lastname har valt att avbryta signeringen av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) som du tidigare blev ombedd att signera. Det kan komma en ny signeringsf�rfr�gan om �rendet skickas in p� nytt.</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledOwnerSMS">Hej $contact.firstname, $signingParty.firstname $signingParty.lastname har valt att avbryta signeringen av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID).</xsl:variable>
	
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
			&lt;a href="$flowInstance.messagesUrl"&gt;$flowInstance.messagesUrl&lt;/a&gt;
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
		
		&lt;p&gt;Du har ombetts att signera �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som skickats in av $contact.firstname $contact.lastname.&lt;/p&gt;
		
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
	
	<xsl:variable name="java.flowInstanceMultiSignCanceledOwnerEmailSubject">Signering av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) avbruten</xsl:variable>
	<xsl:variable name="java.flowInstanceMultiSignCanceledOwnerEmailMessage">
	
		&lt;p&gt;Hej $contact.firstname,&lt;/p&gt;
		
		&lt;p&gt;$signingParty.firstname $signingParty.lastname har valt att avbryta signeringen av �rende $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
		
	</xsl:variable>
	
	<xsl:variable name="java.externalMessageReceivedManagerEmailSubject">Nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt ett nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.messagesUrl"&gt;$flowInstance.messagesUrl&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.internalMessageAddedManagerEmailSubject">Ny intern notering p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.internalMessageAddedManagerEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;Det har lagts till en ny notering p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.notesUrl"&gt;$flowInstance.notesUrl&lt;/a&gt;
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

	<xsl:variable name="java.managerCompletionSubmittedEmailSubject">En komplettering av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har skickats in</xsl:variable>
	<xsl:variable name="java.managerCompletionSubmittedEmailMessage">
	
		&lt;p&gt;Hej $manager.firstname,&lt;/p&gt;
		
		&lt;p&gt;En komplettering av �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) som du �r tilldelad har skickats in.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>

	<xsl:variable name="java.statusChangedManagerGroupEmailSubject">Statusen f�r �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har �ndrats till $status.name</xsl:variable>
	<xsl:variable name="java.statusChangedManagerGroupEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Statusen p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har �ndrats till $status.name.&lt;/p&gt;
		
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
	
	<xsl:variable name="java.flowInstanceExpiredManagerEmailSubject">F�rfallet �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceExpiredManagerEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Handl�ggningstiden f�r �rende $flow.name (�rende nr. $flowInstance.flowInstanceID) i statusen $status.name har passerat. �rendet finns under fliken F�rfallna.&lt;/p&gt;
		
		&lt;p&gt;Denna notifikation skickas endast en g�ng.&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
		
	<xsl:variable name="java.externalMessageReceivedGroupEmailSubject">Nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.externalMessageReceivedGroupEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt ett nytt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.messagesUrl"&gt;$flowInstance.messagesUrl&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.internalMessageAddedGroupEmailSubject">Nytt internt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.internalMessageAddedGroupEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Det har lagts till en ny notering p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.notesUrl"&gt;$flowInstance.notesUrl&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceAssignedGroupEmailSubject">Tilldelad �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceAssignedGroupEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Du har f�tt tillg�ng till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
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
	
	<xsl:variable name="java.flowInstanceAssignedGlobalEmailSubject">Nya handl�ggare p� �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceAssignedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;�rendet $flow.name (�rende nr. $flowInstance.flowInstanceID) har tilldelats nya handl�ggare&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.url"&gt;$flowInstance.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceArchivedGlobalEmailSubject">Avslutat �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceArchivedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett �rende har avslutats, $flow.name (�rende nr. $flowInstance.flowInstanceID). �rendet �r inskickat av $contact.firstname $contact.lastname&lt;/p&gt;
		
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
			&lt;a href="$flowInstance.messagesUrl"&gt;$flowInstance.messagesUrl&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>	
	
	<xsl:variable name="java.internalMessageReceivedGlobalEmailSubject">Nytt internt meddelande kopplat till �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.internalMessageReceivedGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Det har lagts till en ny notering p� �rendet $flow.name (�rende nr. $flowInstance.flowInstanceID).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att visa �rendet:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flowInstance.notesUrl"&gt;$flowInstance.notesUrl&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
	
	<xsl:variable name="java.flowInstanceExpiredGlobalEmailSubject">F�rfallet �rende $flow.name (�rende nr. $flowInstance.flowInstanceID)</xsl:variable>
	<xsl:variable name="java.flowInstanceExpiredGlobalEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;Ett �rende har f�rfallit, $flow.name (�rende nr. $flowInstance.flowInstanceID). �rendet �r inskickat av $contact.firstname $contact.lastname&lt;/p&gt;
		
		&lt;p&gt;Denna notifikation skickas endast en g�ng.&lt;/p&gt;
		
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
	
	<xsl:variable name="java.flowPublishedUserEmailSubject">Publicerad e-tj�nst $flow.name (version $flow.version)</xsl:variable>
	<xsl:variable name="java.flowPublishedUserEmailMessage">
	
		&lt;p&gt;Hej,&lt;/p&gt;
		
		&lt;p&gt;En e-tj�nst har publicerats, $flow.name (version $flow.version).&lt;/p&gt;
		
		&lt;p&gt;Klicka p� l�nken nedan f�r att komma till e-tj�nsten:&lt;/p&gt;
		
		&lt;p&gt;
			&lt;a href="$flow.url"&gt;$flow.url&lt;/a&gt;
		&lt;/p&gt;
	
	</xsl:variable>
		
	<xsl:variable name="i18n.UserNotifications">Notifikationer till s�kande</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserSMS">SMS vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserSMS">SMS vid nytt meddelande fr�n handl�ggare</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserSMS">SMS vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserSMS">SMS vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedUserEmail">E-post vid byte av status</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedUserEmail">E-post vid nytt meddelande fr�n handl�ggare</xsl:variable>

	<xsl:variable name="i18n.SendFlowInstanceSubmittedUserEmail">E-post vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceArchivedUserEmail">E-post vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.ManagerNotifications">Notifikationer till handl�ggare</xsl:variable>
	<xsl:variable name="i18n.SendExternalMessageReceivedManagerEmail">E-post till tilldelade handl�ggare vid nytt meddelande fr�n s�kande</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceAssignedManagerEmail">E-post vid tilldelning av �rende</xsl:variable>
	<xsl:variable name="i18n.SendStatusChangedManagerEmail">E-post till tilldelade handl�ggare vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedManagerEmailSubject">Rubrik p� e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedManagerEmailMessage">Inneh�ll i e-postmeddelande vid byte av status</xsl:variable>
	
	<xsl:variable name="i18n.SendStatusChangedManagerGroupEmail">E-post till tilldelade handl�ggargrupper vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedManagerGroupEmailSubject">Rubrik p� e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedManagerGroupEmailMessage">Inneh�ll i e-postmeddelande vid byte av status</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceSubmittedManagerEmail">E-post till samtliga beh�riga handl�ggare n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedManagerEmailSubject">Rubrik p� e-postmeddelande till samtliga beh�riga handl�ggare n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.SendFlowInstanceSubmittedManagerEmailMessage">Inneh�ll i e-postmeddelande till samtliga beh�riga handl�ggare n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.NoNotificationsEnabled">Inga notifikationer �r aktiverade f�r denna e-tj�nst</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceExpiredManagerEmail">E-post till tilldelade handl�ggare n�r �renden f�rfaller</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceExpiredManagerEmailSubject">Rubrik p� e-postmeddelande till tilldelade handl�ggare n�r  �renden f�rfaller</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceExpiredManagerEmailMessage">Inneh�ll i e-postmeddelande till tilldelade handl�ggare n�r �renden f�rfaller</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserSMS">Inneh�ll i SMS vid inskickat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedNotLoggedInUserSMS">Inneh�ll i SMS vid inskickat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceArchivedUserSMS">Inneh�ll i SMS vid avslutat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedNotLoggedInUserSMS">Inneh�ll i SMS vid avslutat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailSubject">Rubrik p� e-postmeddelande vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailMessage">Inneh�ll i e-postmeddelande vid inskickat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedNotLoggedInUserEmailMessage">Inneh�ll i e-postmeddelande vid inskickat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailSubject">Rubrik p� e-postmeddelande vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedUserEmailMessage">Inneh�ll i e-postmeddelande vid avslutat �rende (inloggad anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedNotLoggedInUserEmailMessage">Inneh�ll i e-postmeddelande vid avslutat �rende (ej inloggad anv�ndare)</xsl:variable>
	
	<xsl:variable name="i18n.GroupNotifications">Notifikationer till handl�ggargrupper</xsl:variable>
	
	<xsl:variable name="i18n.SendExternalMessageReceivedGroupEmail">E-post till tilldelade handl�ggargrupper vid nytt meddelande fr�n s�kande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedGroupEmailSubject">Rubrik p� e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedGroupEmailMessage">Inneh�ll i e-postmeddelande vid nytt meddelande</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceAssignedGroupEmail">E-post vid tilldelning av �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedGroupEmailSubject">Rubrik p� e-postmeddelande vid tilldelning av �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedGroupEmailMessage">Inneh�ll i e-postmeddelande vid tilldelning av �rende</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>

	<xsl:variable name="i18n.ToggleTexts">[Visa/d�lj texter]</xsl:variable>
	
	<xsl:variable name="i18n.UserTagsTable.smsDescription">F�ljande taggar kan anv�ndas i meddelande</xsl:variable>
	<xsl:variable name="i18n.UserTagsTable.emailDescription">F�ljande taggar kan anv�ndas i rubrik och meddelande</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	
	<xsl:variable name="i18n.FlowNameTag">E-tj�nsten namn</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceIDTag">�rende nummer</xsl:variable>
	<xsl:variable name="i18n.Tags.FlowInstance.ExternalID">Externt �rendenummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceURLTag">Adress till �rendet (kan ej anv�ndas i meddelande till ej inloggade anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceMessagesURLTag">Adress till meddelande-fliken i �rendet (kan ej anv�ndas i meddelande till ej inloggade anv�ndare)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceNotesURLTag">Adress till interna noteringar-fliken i �rendet</xsl:variable>
	<xsl:variable name="i18n.StatusTag">�rende status</xsl:variable>
	<xsl:variable name="i18n.Tags.Manager.Firstname">Handl�ggarens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.Tags.Manager.Lastname">Handl�ggarens efternamn</xsl:variable>
	<xsl:variable name="i18n.PosterFirstnameTag">Kontaktpersonens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.PosterLastnameTag">Kontaktpersonens efternamn</xsl:variable>
	<xsl:variable name="i18n.StatusDescriptionTag">Beskrivning f�r �rendets status</xsl:variable>
	<xsl:variable name="i18n.FLowInstanceAttachedPDFTextTag">Text som beskriver bifogad PDF-ans�kan</xsl:variable>
	<xsl:variable name="i18n.Tags.SigningParty.firstname">Signerarens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.Tags.SigningParty.lastname">Signerarens efternamn</xsl:variable>
	<xsl:variable name="i18n.Tags.SigningURL">Signeringsl�nk</xsl:variable>
	
	<xsl:variable name="i18n.MultisigningNotifications">Notifikationer vid flerpartssignering</xsl:variable>
	
	<xsl:variable name="i18n.sendFlowInstanceMultiSignInitiatedUserSMS">SMS till signeringspart n�r de ombeds signera ett �rende med flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignInitiatedUserSMS">Inneh�ll i SMS till signeringspart n�r de ombeds signera ett �rende med flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.sendFlowInstanceMultiSignInitiatedUserEmail">E-post till signeringspart n�r de ombeds signera ett �rende med flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignInitiatedUserEmailSubject">Rubrik p� e-postmeddelande n�r signeringspart ombeds signera ett �rende med flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignInitiatedUserEmailMessage">Inneh�ll i e-postmeddelande n�r signeringspart ombeds signera ett �rende med flerpartssignering</xsl:variable>
	
	<xsl:variable name="i18n.sendFlowInstanceMultiSignCanceledUserSMS">SMS till signeringspart n�r ett �rende med flerpartssignering som de blivit ombedda att signera avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledUserSMS">Inneh�ll i SMS till signeringspart n�r ett �rende med flerpartssignering som de blivit ombedda att signera avbryts</xsl:variable>
	<xsl:variable name="i18n.sendFlowInstanceMultiSignCanceledUserEmail">E-post till signeringspart n�r ett �rende med flerpartssignering som de blivit ombedda att signera avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledUserEmailSubject">Rubrik p� e-postmeddelande till signeringspart n�r ett �rende med flerpartssignering som de blivit ombedda att signera avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledUserEmailMessage">Inneh�ll i e-postmeddelande till signeringspart n�r ett �rende med flerpartssignering som de blivit ombedda att signera avbryts</xsl:variable>
	
	<xsl:variable name="i18n.sendFlowInstanceMultiSignCanceledOwnerSMS">SMS till �rende �garna n�r ett �rende med flerpartssignering avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledOwnerSMS">Inneh�ll i SMS till �rende �garna n�r ett �rende med flerpartssignering avbryts</xsl:variable>
	<xsl:variable name="i18n.sendFlowInstanceMultiSignCanceledOwnerEmail">E-post till �rende �garna n�r ett �rende med flerpartssignering avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledOwnerEmailSubject">Rubrik p� e-postmeddelande till �rende �garna n�r ett �rende med flerpartssignering avbryts</xsl:variable>
	<xsl:variable name="i18n.flowInstanceMultiSignCanceledOwnerEmailMessage">Inneh�ll i e-postmeddelande till �rende �garna n�r ett �rende med flerpartssignering avbryts</xsl:variable>
	
	<xsl:variable name="i18n.GlobalNotifications">Generella notifikationer</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceSubmittedGlobalEmail">E-post n�r nya �renden skickas in</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailMessage">Inneh�ll p� e-postmeddelande till myndighetsbrevl�da vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailSubject">Rubrik p� e-postmeddelande till myndighetsbrevl�da vid inskickat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAddresses">E-postadresser f�r notifiering om nya �renden (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDF">Bifoga �rendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachXML">Bifoga �rendet i XML-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately">Bifoga bilagor separat</xsl:variable>

	<xsl:variable name="i18n.SendFlowInstanceAssignedGlobalEmail">E-post n�r �renden tilldelats handl�ggare</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedGlobalEmailMessage">Inneh�ll p� e-postmeddelande till myndighetsbrevl�da vid tilldelning av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedGlobalEmailSubject">Rubrik p� e-postmeddelande till myndighetsbrevl�da vid  tilldelning av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedGlobalEmailAddresses">E-postadresser f�r notifiering om tilldelning av handl�ggare (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceArchivedGlobalEmail">E-post n�r �renden avslutas</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedGlobalEmailAddresses">E-postadresser f�r notifiering om avslutade �renden (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedGlobalEmailMessage">Inneh�ll p� e-postmeddelande till myndighetsbrevl�da vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedGlobalEmailSubject">Rubrik p� e-postmeddelande till myndighetsbrevl�da vid avslutat �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedGlobalEmailAttachPDF">Bifoga �rendet i PDF-format</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceArchivedGlobalEmailAttachXML">Bifoga �rendet i XML-format</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSubmittedUserEmailAttachPDF">Bifoga �rendet i PDF-format</xsl:variable>
	
	<xsl:variable name="i18n.SendExternalMessageReceivedGlobalEmail">E-post vid nytt meddelande fr�n s�kande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedGlobalEmailAddresses">E-postadresser f�r notifiering om nya meddelanden fr�n s�kande (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.SendInternalMessageReceivedGlobalEmail">E-post vid ny intern notering</xsl:variable>
	<xsl:variable name="i18n.InternalMessageReceivedGlobalEmailAddresses">E-postadresser f�r notifiering om nya interna noteringar (en adress per rad)</xsl:variable>
	
	<xsl:variable name="i18n.SendManagerExpiredGlobalEmail">E-post vid upph�rd handl�ggarbeh�righet</xsl:variable>
	<xsl:variable name="i18n.ManagerExpiredGlobalEmailAddresses">E-postadresser f�r notifiering om upph�rd handl�ggarbeh�righet (en adress per rad)</xsl:variable>

	<xsl:variable name="i18n.SendFlowInstanceExpiredGlobalEmail">E-post n�r �renden f�rfaller</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceExpiredGlobalEmailAddresses">E-postadresser f�r notifiering om f�rfallna �renden (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceExpiredGlobalEmailMessage">Inneh�ll p� e-postmeddelande till myndighetsbrevl�da vid f�rfallet �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceExpiredGlobalEmailSubject">Rubrik p� e-postmeddelande till myndighetsbrevl�da vid f�rfallet �rende</xsl:variable>
	
	<xsl:variable name="i18n.StatusChangedUserEmailSubject">Rubrik p� e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.StatusChangedUserEmailMessage">Inneh�ll i e-postmeddelande vid byte av status</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailSubject">Rubrik p� e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedUserEmailMessage">Inneh�ll i e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerSubject">Rubrik p� e-postmeddelande vid nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageReceivedManagerMessage">Inneh�ll i e-postmeddelande vid nytt meddelande</xsl:variable>
	
	<xsl:variable name="i18n.SendInternalMessageAddedManagerEmail">E-post till tilldelade handl�ggare vid ny intern notering</xsl:variable>
	<xsl:variable name="i18n.internalMessageAddedManagerEmailSubject">Rubrik p� e-postmeddelande vid ny intern notering</xsl:variable>
	<xsl:variable name="i18n.internalMessageAddedManagerEmailMessage">Inneh�ll i e-postmeddelande vid ny intern notering</xsl:variable>
	
	<xsl:variable name="i18n.SendInternalMessageAddedGroupEmail">E-post till tilldelade handl�ggargrupper vid ny intern notering</xsl:variable>
	<xsl:variable name="i18n.internalMessageAddedGroupEmailSubject">Rubrik p� e-postmeddelande vid ny intern notering</xsl:variable>
	<xsl:variable name="i18n.internalMessageAddedGroupEmailMessage">Inneh�ll i e-postmeddelande vid ny intern notering</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceAssignedManagerEmailSubject">Rubrik p� e-postmeddelande vid tilldelning av �rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceAssignedManagerEmailMessage">Inneh�ll i e-postmeddelande vid tilldelning av �rende</xsl:variable>
	
	<xsl:variable name="i18n.SendFlowInstanceCompletionManagerEmail">E-post till tilldelade handl�ggare vid inskickad komplettering</xsl:variable>
	<xsl:variable name="i18n.ManagerCompletionSubmittedEmailSubject">Rubrik p� e-postmeddelande vid inskickad komplettering</xsl:variable>
	<xsl:variable name="i18n.ManagerCompletionSubmittedEmailMessage">Inneh�ll i e-postmeddelande vid inskickad komplettering</xsl:variable>
	
</xsl:stylesheet>
