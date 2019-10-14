<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowBrowserModule.sv.xsl"/>
	<xsl:import href="classpath://com/nordicpeak/flowengine/Message.sv.xsl"/>
	
	<xsl:include href="FlowInstanceAdminModuleTemplates.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>

	<xsl:variable name="java.noManagersSelected">Ingen handl�ggare vald.</xsl:variable>
	<xsl:variable name="java.notificationNewManager">Tilldelad �rende</xsl:variable>
	<xsl:variable name="java.notificationExternalMessage">Meddelande fr�n medborgare</xsl:variable>
	<xsl:variable name="java.notificationCompletion">Medborgare har kompletterat �rendet</xsl:variable>
	<xsl:variable name="java.mentionedInFlowInstance">Du har blivit omn�md i ett meddelande</xsl:variable>
	<xsl:variable name="java.managerSignedDetailsText">Signerad av handl�ggare</xsl:variable>
	<xsl:variable name="java.signStatusDescription">Byte av status p� �rende $flowInstanceID till $statusName</xsl:variable>
	<xsl:variable name="java.signStatusSigningEventDescription">Signerad av handl�ggare f�r statusbyte</xsl:variable>

	<xsl:variable name="i18n.Help">Hj�lp</xsl:variable>
	
	<xsl:variable name="i18n.Emergency">akut</xsl:variable>
	<xsl:variable name="i18n.Owned">mina</xsl:variable>
	<xsl:variable name="i18n.Flagged">flaggade</xsl:variable>
	<xsl:variable name="i18n.Active">aktiva</xsl:variable>
	<xsl:variable name="i18n.UnAssigned">obehandlade</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	
	<xsl:variable name="i18n.EmergencyTab">�renden du beh�ver agera p�</xsl:variable>
	<xsl:variable name="i18n.OwnedTab">Mina</xsl:variable>
	<xsl:variable name="i18n.FlaggedTab">Flaggade</xsl:variable>
	<xsl:variable name="i18n.ActiveTab">Aktiva</xsl:variable>
	<xsl:variable name="i18n.UnAssignedTab">Obehandlade</xsl:variable>
	
	<xsl:variable name="i18n.Flow">E-tj�nst</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceID">�rendenummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceID.short">�rendenr.</xsl:variable>
	<xsl:variable name="i18n.Status">Status</xsl:variable>
	<xsl:variable name="i18n.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.Priority">Prioritet</xsl:variable>
	
	<xsl:variable name="i18n.CurrentStatus">Aktuell status</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.SaveAndClose">Spara och st�ng</xsl:variable>
	
	<xsl:variable name="i18n.PrioritizedInstancesDescription.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.PrioritizedInstancesDescription.Part2">�renden som du beh�ver agera p�, klicka p� knappen "V�lj" f�r att forts�tta.</xsl:variable>
	<xsl:variable name="i18n.PrioritizedInstancesHelp">
<h2 class="h1 full" data-icon-before="?">�renden du beh�ver agera p�</h2>
<p>H�r hittar du �renden som legat f�r l�nge i sin aktuella status.</p>
	</xsl:variable>
	
	<xsl:variable name="i18n.UserAssignedInstancesDescription.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.UserAssignedInstancesDescription.Part2"> �renden, klicka p� knappen "V�lj" f�r att forts�tta.</xsl:variable>
	<xsl:variable name="i18n.UserAssignedInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Mina �renden</h2>
<p>H�r hittar du dina p�g�ende �renden.</p>
	</xsl:variable>
	
	<xsl:variable name="i18n.BookmarkedInstancesDescription.Part1">Det finns</xsl:variable>
	<xsl:variable name="i18n.BookmarkedInstancesDescription.Part2">�renden som du beh�ver agera p�, klicka p� knappen "V�lj" f�r att forts�tta.</xsl:variable>
	<xsl:variable name="i18n.BookmarkedInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Flaggade �renden</h2>
<p>H�r hittar du de �renden som du valt att flagga</p>
	</xsl:variable>
	
	<xsl:variable name="i18n.ActiveInstancesDescription.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.ActiveInstancesDescription.Part2">aktiva �renden, klicka p� knappen "V�lj" f�r att forts�tta.</xsl:variable>
	<xsl:variable name="i18n.ActiveInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Aktiva �renden</h2>
<p>H�r hittar du �renden som du har beh�righets att se men som handl�ggs av andra personer.</p>
	</xsl:variable>
	
	<xsl:variable name="i18n.UnassignedInstancesDescription.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.UnassignedInstancesDescription.Part2">obehandlade �renden, klicka p� knappen "V�lj" f�r att forts�tta.</xsl:variable>
	<xsl:variable name="i18n.UnassignedInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Obehandlade �renden</h2>
<p>H�r hittar du �renden som inte tilldelats n�gon handl�ggare.</p>
	</xsl:variable>
	
	<xsl:variable name="i18n.NoFlowInstances">Inga �renden</xsl:variable>
	<xsl:variable name="i18n.Choose">V�lj</xsl:variable>
	
	<xsl:variable name="i18n.High">H�g</xsl:variable>
	<xsl:variable name="i18n.Medium">Medium</xsl:variable>
	
	<xsl:variable name="i18n.SearchFlowInstance">S�k �rende</xsl:variable>
	<xsl:variable name="i18n.SearchFlowInstanceDescription">S�k bland �renden i systemet. Endast �renden du har beh�righet till visas.</xsl:variable>
	<xsl:variable name="i18n.SearchFormTitle">S�k p� �rendenummer, personnummer, organisationsnummer</xsl:variable>
		
	
	<xsl:variable name="i18n.FirstSubmitted">Inskickat</xsl:variable>
	<xsl:variable name="i18n.LastSubmitted">Senast kompletterat</xsl:variable>
	<xsl:variable name="i18n.LastChanged">Senast �ndrat</xsl:variable>
	<xsl:variable name="i18n.Managers">Handl�ggare</xsl:variable>
	<xsl:variable name="i18n.Groups">Handl�ggargrupper</xsl:variable>
	<xsl:variable name="i18n.LastSubmittedBy">Senast kompletterat av</xsl:variable>

	<xsl:variable name="i18n.Search.Groups">S�k handl�ggargrupper</xsl:variable>
	<xsl:variable name="i18n.Search.Managers">S�k handl�ggare</xsl:variable>
	
	<xsl:variable name="i18n.by">av</xsl:variable>
	<xsl:variable name="i18n.NoManager">Ingen handl�ggare tilldelad</xsl:variable>
	
	<xsl:variable name="i18n.Details">Detaljer</xsl:variable>
	<xsl:variable name="i18n.ExternalMessages">Meddelanden</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceEvents">�rendehistorik</xsl:variable>
	<xsl:variable name="i18n.NoExternalMessages">Inga meddelanden</xsl:variable>
	<xsl:variable name="i18n.NewMessage">Nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.NewMessageWarningNoContactWay">Mottagarna har varken e-post eller telefon satt och kommer d�rf�r inte f� n�gon notifikation om ditt meddelande.</xsl:variable>
	<xsl:variable name="i18n.Action">H�ndelse</xsl:variable>
	<xsl:variable name="i18n.Person">Person</xsl:variable>
	<xsl:variable name="i18n.NoEvents">Ingen �rendehistorik</xsl:variable>
	
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.AttachFiles">Bifoga filer</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
	<xsl:variable name="i18n.close">st�ng</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.SubmitMessage">Skicka meddelande</xsl:variable>
	
	<xsl:variable name="i18n.DeleteFile">Ta bort fil</xsl:variable>

	<xsl:variable name="i18n.ShowFlowInstance">Visa �rende</xsl:variable>
	<xsl:variable name="i18n.Or">Eller</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowInstance">�ndra �rende</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstancePreviewNotificationTitle.Part1">Du granskar nu</xsl:variable>
	<xsl:variable name="i18n.FlowInstancePreviewNotificationTitle.Part2">�rende</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceFormNotificationTitle.Part1">Du �ndrar nu</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceFormNotificationTitle.Part2">�rende</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceSavedByManager">�ndringarna i �rendet sparades!</xsl:variable>
	
	<xsl:variable name="i18n.Hits.Part1">gav</xsl:variable>
	<xsl:variable name="i18n.Hits.Part2">tr�ffar</xsl:variable>
	<xsl:variable name="i18n.SearchDone">S�kningen �r klar</xsl:variable>
	
	<xsl:variable name="i18n.InternalMessages">Interna noteringar</xsl:variable>
	<xsl:variable name="i18n.SubmitInternalMessage">Spara notering</xsl:variable>
	<xsl:variable name="i18n.InternalMessage">Notering</xsl:variable>
	<xsl:variable name="i18n.NewInternalMessage">Ny notering</xsl:variable>
	<xsl:variable name="i18n.InternalMessagesTitle">Noteringar</xsl:variable>
	<xsl:variable name="i18n.NoInternalMessages">Inga noteringar</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstancePreviewError">Ett fel uppstod vid visning av �rendet.</xsl:variable>
		
	<xsl:variable name="i18n.FlowInstanceManagerClosedError">Den kopia av �rendet som du hade �ppen har st�ngts.</xsl:variable>
	<xsl:variable name="i18n.StatusNotFoundValidationError">Den valda statusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.InvalidStatusValidationError">Du har valt en ogiltig status.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerUsersNotFoundError">En eller flera av de valda anv�ndarna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerGroupsNotFoundError">En eller flera av de valda grupperna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.part1">Anv�ndaren</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.part2">har inte beh�righet att handl�gga detta �rende.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.part1">Gruppen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.part2">har inte beh�righet att handl�gga detta �rende.</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.UnableToParseRequest">Ett ok�nt fel uppstod vid filuppladdningen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageRequired">Du m�ste skriva ett meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToLong">Du har skrivit ett f�r l�ngt meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.ExternalMessageToShort">Du har skrivit ett f�r kort meddelande</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageRequired">Du m�ste skriva en notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToLong">Du har skrivit en f�r l�ng notering</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InternalMessageToShort">Du har skrivit en f�r kort notering</xsl:variable>
	
	<xsl:variable name="i18n.DownloadFlowInstancePDF">H�mta kvittens i PDF-format</xsl:variable>
	<xsl:variable name="i18n.DownloadFlowInstanceXML">H�mta �rendet i XML-format</xsl:variable>
	<xsl:variable name="i18n.DownloadFlowInstanceSignPDF">H�mta signerings underlag i PDF-format</xsl:variable>	
	<xsl:variable name="i18n.DownloadSignature">H�mta signatur</xsl:variable>
	 
	<xsl:variable name="i18n.SiteProfile">Kommun</xsl:variable>
	<xsl:variable name="i18n.Summary">Sammanfattning av �ppna �renden</xsl:variable>

	<xsl:variable name="i18n.PosterWasNotLoggedIn">Anv�ndaren som skickade in denna ans�kan var inte inloggad och ser inte detta �rende p� Mina sidor</xsl:variable>
	<xsl:variable name="i18n.PosterAnonymized">Anv�ndaren som skickade in denna ans�kan ser inte detta �rende p� Mina sidor</xsl:variable>
	<xsl:variable name="i18n.Owners">S�kande</xsl:variable>
</xsl:stylesheet>
