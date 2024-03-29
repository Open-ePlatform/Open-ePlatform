<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:include href="FlowTestCommon.sv.xsl"/>
	
	<xsl:include href="FlowAdminModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="java.flowNameCopySuffix"> (kopia)</xsl:variable>
	<xsl:variable name="java.fileMissing">Filen saknas</xsl:variable>
	
	<xsl:variable name="java.eventCopyFlowMessage">Kopierade version</xsl:variable>
	<xsl:variable name="java.eventUpdateIconMessage">Uppdaterade ikon till</xsl:variable>
	<xsl:variable name="java.eventUpdateNotificationsMessage">Uppdaterade notifikationsinst�llningar</xsl:variable>
	<xsl:variable name="java.eventUpdateManagementInfoMessage">Uppdaterade f�rvaltningsinformationen</xsl:variable>
	<xsl:variable name="java.eventSortFlowMessage">Sorterade fr�gor och steg</xsl:variable>
	<xsl:variable name="java.eventImportFlowMessage">Importerade version</xsl:variable>
	<xsl:variable name="java.eventImportQueriesMessage">Importerade fr�gor</xsl:variable>
	<xsl:variable name="java.eventFlowFamilyUpdatedMessage">Uppdaterade handl�ggare</xsl:variable>
	<xsl:variable name="java.eventFlowAddedMessage">Skapade version</xsl:variable>
	<xsl:variable name="java.eventFlowUpdatedMessage">Uppdaterade grundinformation</xsl:variable>
	<xsl:variable name="java.eventFlowDeletedMessage">Tog bort version</xsl:variable>
	<xsl:variable name="java.eventStepAddedMessage">Skapade steg</xsl:variable>
	<xsl:variable name="java.eventStepUpdatedMessage">Uppdaterade steg</xsl:variable>
	<xsl:variable name="java.eventStepDeletedMessage">Tog bort steg</xsl:variable>
	<xsl:variable name="java.eventQueryAddedMessage">Skapade fr�ga</xsl:variable>
	<xsl:variable name="java.eventQueryUpdatedMessage">Uppdaterade fr�ga</xsl:variable>
	<xsl:variable name="java.eventQueryDeletedMessage">Tog bort fr�ga</xsl:variable>
	<xsl:variable name="java.eventEvaluatorAddedMessage">Skapade regel</xsl:variable>
	<xsl:variable name="java.eventEvaluatorUpdatedMessage">Uppdaterade regel</xsl:variable>
	<xsl:variable name="java.eventEvaluatorDeletedMessage">Tog bort regel</xsl:variable>
	<xsl:variable name="java.eventStatusAddedMessage">Skapade status</xsl:variable>
	<xsl:variable name="java.eventStatusUpdatedMessage">Uppdaterade status</xsl:variable>
	<xsl:variable name="java.eventStatusDeletedMessage">Tog bort status</xsl:variable>
	<xsl:variable name="java.eventStatusesReplacedMessage">Ersatte statusar med</xsl:variable>
	<xsl:variable name="java.eventChangeFlowType">�ndrade kategori till</xsl:variable>
	<xsl:variable name="java.eventStatusSortMessage">Sorterade statusar</xsl:variable>
	<xsl:variable name="java.eventEvaluatorSortMessage">Sorterade regler i fr�ga</xsl:variable>
	<xsl:variable name="java.eventFlowFormAddedMessage">Skapade blankett</xsl:variable>
	<xsl:variable name="java.eventFlowFormUpdatedMessage">Uppdaterade blankett</xsl:variable>
	<xsl:variable name="java.eventFlowFormDeletedMessage">Tog bort blankett</xsl:variable>
	<xsl:variable name="java.eventFlowInstanceManagerExpired">Handl�ggare automatiskt borttagen:</xsl:variable>
	<xsl:variable name="java.eventFunctionConfigured">�ndrande konfigurationen av funktionen</xsl:variable>
	<xsl:variable name="java.eventUpdateAutoManagerAssignment">�ndrade inst�llningar f�r automatisk tilldelning av handl�ggare</xsl:variable>
	<xsl:variable name="java.eventMessageTemplatesAddedMessage">Skapade meddelandemall</xsl:variable>
	<xsl:variable name="java.eventMessageTemplatesUpdatedMessage">Uppdaterade meddelandemall</xsl:variable>
	<xsl:variable name="java.eventMessageTemplatesDeletedMessage">Tog bort meddelandemall</xsl:variable>
	
	<xsl:variable name="java.bundleListFlows">Lista e-tj�nster</xsl:variable>
	<xsl:variable name="java.bundleAddFlow">L�gg till e-tj�nst</xsl:variable>
	<xsl:variable name="java.bundleImportFlow">Importera e-tj�nst</xsl:variable>
	<xsl:variable name="java.bundleStandardStatuses">Adm. statusgrupper</xsl:variable>
	<xsl:variable name="java.bundleFlowtypes">Adm. kategorier</xsl:variable>
	
	<xsl:variable name="java.defaultLoginHelpLinkName">S� h�r skaffar du e-legitimation</xsl:variable>
	<xsl:variable name="java.defaultFlowStartLoginQuestionText">
		&lt;p&gt;Om du loggar in s� kan vi f�rifylla fr�gor med uppgifter vi h�mtar fr�n andra system t.ex. skatteverket.&lt;/p&gt;
		&lt;p&gt;Du kan �ven f� �terkoppling om �rendets status och notifiering om handl�ggaren beh�ver mer information.&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="java.hiddenQueryText">(dold)</xsl:variable>
	
	<xsl:variable name="i18n.flowName">E-tj�nst</xsl:variable>
	
	<xsl:variable name="i18n.Flowslist.title">E-tj�nster</xsl:variable>
	<xsl:variable name="i18n.Flowlist.description">Nedan visas samtliga e-tj�nster i systemet som du har beh�righet att administrera.</xsl:variable>
	<xsl:variable name="i18n.typeOfFlow">Typ av e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.internal">E-tj�nst i denna plattform</xsl:variable>
	<xsl:variable name="i18n.external">L�nk till e-tj�nst i annan plattform</xsl:variable>
	<xsl:variable name="i18n.externalLink">L�nk till e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.OpenExternalFlow">�ppna e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.flowType">Kategori</xsl:variable>
	<xsl:variable name="i18n.flowCategory">Underkategori</xsl:variable>
	<xsl:variable name="i18n.flowFamilyID">FamiljeID</xsl:variable>
	<xsl:variable name="i18n.organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.steps">Steg</xsl:variable>
	<xsl:variable name="i18n.queries">Fr�gor</xsl:variable>
	<xsl:variable name="i18n.SubmittedInstances">Insk. �renden</xsl:variable>
	<xsl:variable name="i18n.NotSubmittedInstances">Ej insk. �renden</xsl:variable>
	<xsl:variable name="i18n.flowFamilyLastReviewed">Granskad</xsl:variable>
	<xsl:variable name="i18n.status">Status</xsl:variable>
	<xsl:variable name="i18n.noFlowsFound">Inga e-tj�nster hittades.</xsl:variable>
	<xsl:variable name="i18n.disabled">Inaktiverad</xsl:variable>
	<xsl:variable name="i18n.published">Publicerad</xsl:variable>
	<xsl:variable name="i18n.notPublished">Ej publicerad</xsl:variable>
	<xsl:variable name="i18n.deleteFlowDisabledIsPublished">Den h�r e-tj�nsten kan inte tas bort eftersom den �r publicerad.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowDisabledHasInstances">Den h�r e-tj�nsten kan inte tas bort eftersom det finns ett eller flera �renden kopplade till den.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowConfirm">Ta bort e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.deleteFlow.title">Ta bort e-tj�nsten</xsl:variable>
	
	<xsl:variable name="i18n.addFlow">L�gg till e-tj�nst</xsl:variable>

	<xsl:variable name="i18n.AddFlow.title">L�gg till e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.AddFlow.submit">L�gg till</xsl:variable>
	
	<xsl:variable name="i18n.UpdateFlow.title">Uppdatera e-tj�nsten: </xsl:variable>
	<xsl:variable name="i18n.UpdateFlow.submit">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.TableSettings">Inst�llningar</xsl:variable>
	<xsl:variable name="i18n.EditTableSettings">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.TableSettings.Title">Inst�llningar</xsl:variable>
	<xsl:variable name="i18n.TableSettings.Description">Markera de kolumner du vill ha aktiva i e-tj�nst�versikten och spara. Dra i raderna f�r att �ndra ordning.</xsl:variable>
	
	<xsl:variable name="i18n.TableSettings.Save">Spara</xsl:variable>
	<xsl:variable name="i18n.TableSettings.Reset">�terst�ll standard</xsl:variable>
	<xsl:variable name="i18n.TableSettings.MoveColumnOrder">�ndra ordning</xsl:variable>

	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.shortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.longDescription">L�ngre beskrivning</xsl:variable>
	<xsl:variable name="i18n.submittedMessage">Meddelande vid inl�mnat �rende</xsl:variable>
	<xsl:variable name="i18n.publishDate">Publiceringsdatum</xsl:variable>
	<xsl:variable name="i18n.unPublishDate">Avpubliceringsdatum</xsl:variable>
	<xsl:variable name="i18n.usePreview">Aktivera f�rhandsgranskning</xsl:variable>

	<xsl:variable name="i18n.FlowFamily.SharedSettings">F�ljande inst�llningar g�ller f�r samtliga versioner av e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp">Visa l�nk p� �versiktssidan (exempelvis l�nk till instruktion f�r att skaffa e-legitimation)</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp.Name">L�nknamn</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp.URL">Adress (komplett URL)</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.popularity.boost">Extra antal �renden vid ber�kning av popularitet</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.startButtonText">Anpassad text p� startknapp (max 24 tecken)</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes">Extra attribut i �rende�versikten</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.Description">F�r att underl�tta handl�ggning s� finns m�jligheten att visa upp till 6st rader med attributv�rden direkt i �rende�versikten. Via f�lten nedan kan en valfri str�ng anges med b�de fast text och $attribute{} taggar.</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.name">Namn</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.value">Attribut</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.Add">L�gg till attributrad</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.Add.MaxReached">Du f�r h�gst ha 6st rader</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.Move">Flytta attribut</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.Delete">Ta bort attributet fr�n �rende�versikten</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.OverviewAttributes.DeleteConfirm">�r du s�ker p� att du inte l�ngre vill visa attributet p� �rende�versikten?</xsl:variable>
	<xsl:variable name="i18n.DuplicateOverviewAttributeNames.part1">Namnet</xsl:variable>
	<xsl:variable name="i18n.DuplicateOverviewAttributeNames.part2">f�r bara f�rekomma en g�ng i listan av extra attribut i �rende�versikten.</xsl:variable>
	
	<xsl:variable name="i18n.contact.title">Kontaktuppgifter - Fr�gor om e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.contact.name">Namn</xsl:variable>
	<xsl:variable name="i18n.contact.email">E-post</xsl:variable>
	<xsl:variable name="i18n.contact.phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.contact.webaddress">Webbsida (komplett URL)</xsl:variable>
	<xsl:variable name="i18n.owner.title">Kontaktuppgifter - Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.owner.name">Namn</xsl:variable>
	<xsl:variable name="i18n.owner.email">E-post</xsl:variable>

	<xsl:variable name="i18n.SelectedFlowTypeNotFound">Den valda typen hittades inte!</xsl:variable>
	<xsl:variable name="i18n.FlowTypeAccessDenied">Du har inte beh�righet till den valda typen!</xsl:variable>

	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett ok�nt valideringsfel har uppst�tt.'"/>
	<xsl:variable name="i18n.enableFlow">Aktivera e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.disableUnpublishedSavedFlows">St�ng av m�jligheten att skicka in sparade �renden n�r versionen inte �r publicerad</xsl:variable>
	
	<xsl:variable name="i18n.baseInfo">Grundinformation</xsl:variable>
	<xsl:variable name="i18n.enabled">Aktiverad</xsl:variable>
	<xsl:variable name="i18n.icon">Ikon</xsl:variable>
	<xsl:variable name="i18n.FlowForm">Blankett</xsl:variable>
	<xsl:variable name="i18n.stepsAndQueries">Fr�gor och steg</xsl:variable>
	<xsl:variable name="i18n.statuses">Statusar</xsl:variable>
	<xsl:variable name="i18n.flowContainsNoSteps">Inga steg hittades.</xsl:variable>
	<xsl:variable name="i18n.flowHasNoStatuses">Inga statusar hittades.</xsl:variable>
	
	<xsl:variable name="i18n.updateFlowBaseInfo.title">Uppdatera e-tj�nstens grundinformation</xsl:variable>

	<xsl:variable name="i18n.updateManagementInfo">Uppdatera f�rvaltningsinformation f�r e-tj�nsten:</xsl:variable>
	<xsl:variable name="i18n.noManagementInfo">Det finns ingen f�rvaltningsinformation f�r denna e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.managementInfo">F�rvaltningsinformation</xsl:variable>
	<xsl:variable name="i18n.managementInfo.processOwner">Process�gare</xsl:variable>
	<xsl:variable name="i18n.managementInfo.flowResponsible">E-tj�nstansvarig</xsl:variable>
	<xsl:variable name="i18n.managementInfo.informationResponsible">Fakta- och informationsansvarig</xsl:variable>
	<xsl:variable name="i18n.managementInfo.status">Status</xsl:variable>
	<xsl:variable name="i18n.managementInfo.organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.managementInfo.lastReviewed">Senast granskad</xsl:variable>
	<xsl:variable name="i18n.managementInfo.lastReviewed.Details">F�r som senast vara dagens datum</xsl:variable>
	<xsl:variable name="i18n.managementInfo.aboutFlow">Om e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.updateManagementInfo.submit">Spara f�rvaltningsinformation</xsl:variable>
		
	<xsl:variable name="i18n.stepAndQueryManipulationDisabledHasInstances">Det g�r inte att redigera fr�gorna och stegen f�r denna e-tj�nst eftersom det finns ett eller flera �renden kopplad till den.</xsl:variable>
	<xsl:variable name="i18n.stepAndQueryManipulationDisabledIsPublished">Det g�r inte att redigera fr�gorna och stegen f�r denna e-tj�nst eftersom den �r publicerad.</xsl:variable>
	<xsl:variable name="i18n.updateStep.title">Uppdatera steget</xsl:variable>
	<xsl:variable name="i18n.deleteStep.confirm.part1">Ta bort steget</xsl:variable>
	<xsl:variable name="i18n.deleteStep.confirm.part2">och eventuella fr�gor kopplade till steget?</xsl:variable>
	<xsl:variable name="i18n.deleteStep.title">Ta bort steget</xsl:variable>
	
	<xsl:variable name="i18n.updateQuery.title">Uppdatera fr�gan</xsl:variable>
	<xsl:variable name="i18n.deleteQuery.confirm">Ta bort fr�gan</xsl:variable>
	<xsl:variable name="i18n.deleteQuery.title">Ta bort fr�gan</xsl:variable>
	<xsl:variable name="i18n.addStep">L�gg till steg</xsl:variable>
	<xsl:variable name="i18n.addQuery">L�gg till fr�ga</xsl:variable>
	<xsl:variable name="i18n.sortStepsAndQueries">Sortera fr�gor och steg</xsl:variable>
	
	<xsl:variable name="i18n.AddQueryDescriptor.title">L�gg till fr�ga</xsl:variable>
	<xsl:variable name="i18n.step">Steg</xsl:variable>
	<xsl:variable name="i18n.queryType">Fr�getyp</xsl:variable>
	<xsl:variable name="i18n.queryTypeDescription">Beskrivning f�r vald fr�getyp</xsl:variable>
	<xsl:variable name="i18n.AddQueryDescriptor.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.SelectedStepNotFound">Det valda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.SelectedQueryTypeNotFound">Den valda fr�getypen hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.AddStep.title">L�gg till steg</xsl:variable>
	<xsl:variable name="i18n.AddStep.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateStep.title">Uppdatera steget: </xsl:variable>
	<xsl:variable name="i18n.UpdateStep.submit">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.AddStatus.title">L�gg till status</xsl:variable>
	<xsl:variable name="i18n.AddStatus.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateStatus.title">Uppdatera status: </xsl:variable>
	<xsl:variable name="i18n.UpdateStatus.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.UpdateStatus.Advanced">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.isUserMutable">Till�t anv�ndare att �ndra �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.isUserDeletable">Till�t anv�ndare att ta bort �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.isAdminMutable">Till�t handl�ggare att �ndra �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.isAdminDeletable">Till�t handl�ggare att ta bort �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.isRestrictedAdminDeletable">Till�t begr�nsade handl�ggare att ta bort �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.defaultStatusMappings.title">Statusmappningar</xsl:variable>
	<xsl:variable name="i18n.defaultStatusMappings.description">Anv�nd denna status vid f�ljande h�ndelser.</xsl:variable>
	<xsl:variable name="i18n.managingTime">Handl�ggningstid</xsl:variable>
	<xsl:variable name="i18n.required">obligatorisk</xsl:variable>
	<xsl:variable name="i18n.managingTime.description">Antalet vardagar (l�rdagar och s�ndagar r�knas ej) som �renden f�r befinna sig i denna status innan de f�r handl�ggaren blir markerade som f�rfallna.</xsl:variable>
	<xsl:variable name="i18n.newExternalMessagesDisallowed">Till�t ej nya meddelanden p� �renden med denna status</xsl:variable>
	<xsl:variable name="i18n.newExternalMessagesAllowedDays">Antal dagar som nya meddelanden �r till�tna efter byte till denna status</xsl:variable>		
	<xsl:variable name="i18n.addExternalMessage">Kr�v att handl�ggaren l�gger till ett meddelande vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.addInternalMessage">Kr�v att handl�ggaren l�gger till en intern notering vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.defaultMessageTemplate">F�rvald meddelandemall</xsl:variable>
	<xsl:variable name="i18n.defaultExternalMessageTemplate">F�rvald meddelandemall (meddelande)</xsl:variable>
	<xsl:variable name="i18n.defaultInternalMessageTemplate">F�rvald meddelandemall (intern notering)</xsl:variable>
	<xsl:variable name="i18n.defaultMessageTemplate.None">Ingen f�rvald meddelandemall</xsl:variable>
	<xsl:variable name="i18n.Status.requireSigning">Kr�v signering fr�n handl�ggare vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.Status.useAccessCheckByUser">Begr�nsa �tkomst till denna status baserat p� anv�ndare och grupper</xsl:variable>
	<xsl:variable name="i18n.Status.useAccessCheckByStatus">Begr�nsa �tkomst till denna status baserat p� �rendets status</xsl:variable>
	<xsl:variable name="i18n.Status.useAccessCheckByStatusInfo">Ange vilken eller vilka statusar som �rendet m�ste ha f�r att denna status ska kunna s�ttas p� �rendet av handl�ggaren.</xsl:variable>
	<xsl:variable name="i18n.DeleteAcceptedStatus">Ta bort statusen som godk�nd status</xsl:variable>
	<xsl:variable name="i18n.DeleteAcceptedStatus.Confirm">Vill du ta bort denna status</xsl:variable>
	<xsl:variable name="i18n.AcceptedStatuses.SearchPlaceholder">S�k p� statusnamn</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerUsersNotFoundError">En eller flera av de valda anv�ndarna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerGroupsNotFoundError">En eller flera av de valda grupperna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.1">Anv�ndaren</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.2">�r inte handl�ggare f�r den h�r e-tj�nsten!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.1">Gruppen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.2">�r inte begr�nsad handl�ggare f�r den h�r e-tj�nsten!</xsl:variable>
	
	<xsl:variable name="i18n.deleteStatusDisabledHasInstances">Den h�r statusen kan inte tas bort eftersom det finns ett eller flera �renden kopplade till den.</xsl:variable>
	<xsl:variable name="i18n.updateStatus.link.title">Uppdatera statusen</xsl:variable>
	<xsl:variable name="i18n.deleteStatus.link.title">Ta bort statusen</xsl:variable>
	<xsl:variable name="i18n.deleteStatus.confirm">Ta bort statusen</xsl:variable>
	<xsl:variable name="i18n.addStatus">L�gg till status</xsl:variable>
	
	<xsl:variable name="i18n.statusContentType.title">Inneh�llstyp</xsl:variable>
	<xsl:variable name="i18n.statusContentType.description">V�lj vilken typ av �renden som den h�r statusen kommer att inneh�lla.</xsl:variable>
	<xsl:variable name="i18n.contentType.NEW">Sparade men ej inskickade �renden</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_MULTISIGN">V�ntar p� flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_PAYMENT">V�ntar p� betalning</xsl:variable>
	<xsl:variable name="i18n.contentType.SUBMITTED">Inskickade �renden</xsl:variable>
	<xsl:variable name="i18n.contentType.IN_PROGRESS">�renden under behandling</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_COMPLETION">V�ntar p� komplettering</xsl:variable>
	<xsl:variable name="i18n.contentType.ARCHIVED">Avslutade �renden</xsl:variable>

	<xsl:variable name="i18n.externalMessages">Meddelanden</xsl:variable>
	<xsl:variable name="i18n.permissions">Beh�righeter</xsl:variable>
	<xsl:variable name="i18n.contentType">Inneh�llstyp</xsl:variable>
	
	<xsl:variable name="i18n.updateFlowIcon.link.title">Uppdatera ikon</xsl:variable>
	
	<xsl:variable name="i18n.UpdateFlowIcon.title">Uppdatera ikon f�r e-tj�nsten:</xsl:variable>
	<xsl:variable name="i18n.currentIcon">Aktuell ikon</xsl:variable>
	<xsl:variable name="i18n.defaultIcon">(standardikon)</xsl:variable>
	<xsl:variable name="i18n.restoreDefaultIcon">�terst�ll standardikon</xsl:variable>
	<xsl:variable name="i18n.uploadNewIcon">Ladda upp ny ikon (png, jpg, gif eller bmp format)</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowIcon.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att tolka informationen fr�n din webbl�sare.</xsl:variable>
	<xsl:variable name="i18n.UnableToParseIcon">Den gick inte att tolka ikonen.</xsl:variable>
	<xsl:variable name="i18n.InvalidIconFileFormat">Felaktig filformat endast ikoner i png, jpg, gif eller bmp format �r till�tna.</xsl:variable>
	
	<xsl:variable name="i18n.addFlowForm.link.title">L�gg till blankett</xsl:variable>
	<xsl:variable name="i18n.updateFlowForm.link.title">Uppdatera blankett</xsl:variable>
	<xsl:variable name="i18n.deleteFlowForm.link.title">Ta bort blankett</xsl:variable>
	<xsl:variable name="i18n.deleteFlowForm.confirm">Ta bort blankett</xsl:variable>
	<xsl:variable name="i18n.AddFlowForm.title">Uppdatera blankett f�r e-tj�nsten:</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.title">Uppdatera blankett f�r e-tj�nsten:</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.description">V�lj antigen en PDF fil att ladda upp eller l�nka till en extern PDF</xsl:variable>
	<xsl:variable name="i18n.uploadNewFlowForm">Ladda upp ny blankett</xsl:variable>
	<xsl:variable name="i18n.FlowForm.name">Namn (kan l�mnas tomt)</xsl:variable>
	<xsl:variable name="i18n.FlowForm.externalURL">L�nk till extern PDF</xsl:variable>
	<xsl:variable name="i18n.FlowForm.downloadFormButtonText">Anpassad text p� knappen f�r att ladda ner blanketten (max 24 tecken)</xsl:variable>
	<xsl:variable name="i18n.FlowForm.downloadFormButtonText.Info">Om ingen anpassad text anges f�r knappen standardtexten. Om flera blanketter laddas upp och ska ha en anpassad text s� beh�ver samma text l�ggas p� alla blanketterna.</xsl:variable>
	<xsl:variable name="i18n.AddFlowForm.submit">L�gg till blankett</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.UnableToStoreFile">Det gick inte att spara blanketten.</xsl:variable>
	<xsl:variable name="i18n.InvalidFlowFormFileFormat.part1">Felaktig filformat endast blanketter i</xsl:variable>
	<xsl:variable name="i18n.InvalidFlowFormFileFormat.part2">format �r till�tna.</xsl:variable>
	<xsl:variable name="i18n.NoAttachedFile">Du har inte bifogat n�gon fil.</xsl:variable>
	
	<xsl:variable name="i18n.defaultQueryState">Standardl�ge</xsl:variable>
	<xsl:variable name="i18n.defaultQueryState.title">Standardl�ge</xsl:variable>
	<xsl:variable name="i18n.defaultQueryState.description">V�lj vilket standardl�ge som fr�gan skall ha.</xsl:variable>
	<xsl:variable name="i18n.queryState.VISIBLE">Valfri</xsl:variable>
	<xsl:variable name="i18n.queryState.VISIBLE_REQUIRED">Obligatorisk</xsl:variable>
	<xsl:variable name="i18n.queryState.HIDDEN">Dold</xsl:variable>

	<xsl:variable name="i18n.SortFlow.title">Sortera fr�gor och steg</xsl:variable>
	<xsl:variable name="i18n.SortFlow.description">Observera att en fr�ga som har regler inte kan placeras efter de fr�gor som reglerna p�verkar.</xsl:variable>
	<xsl:variable name="i18n.SortFlow.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.MoveStep">Flytta steg</xsl:variable>
	<xsl:variable name="i18n.MoveQuery">Flytta fr�ga</xsl:variable>
	
	<xsl:variable name="i18n.NoStepSortindex">Det gick inte att hitta sorteringsindex f�r alla steg.</xsl:variable>
	<xsl:variable name="i18n.NoQueryDescriptorSortindex">Det gick inte att hitta sorteringsindex f�r alla fr�gor.</xsl:variable>
	<xsl:variable name="i18n.InvalidQuerySortIndex">En eller flera fr�gor har felaktigt sorteringsindex. Fr�gor med regler f�r inte ligga efter de fr�gor som de p�verkar. De fr�gor som p�verkas av regler f�r inte ligga f�re fr�gan med regeln.</xsl:variable>
	
	<xsl:variable name="i18n.UnableToFindStepsForAllQueries">Det gick inte att koppla alla fr�gor till steg.</xsl:variable>
	<xsl:variable name="i18n.updateEvaluator.title">Uppdatera regel</xsl:variable>
	<xsl:variable name="i18n.deleteEvaluator.confirm">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.deleteEvaluator.title">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.addEvaluator.title">L�gg till regel kopplad till fr�gan</xsl:variable>
	<xsl:variable name="i18n.AddEvaluatorDescriptor.title">L�gg till regel kopplad till fr�gan</xsl:variable>
	<xsl:variable name="i18n.evaluatorType">Regeltyp</xsl:variable>
	<xsl:variable name="i18n.AddEvaluatorDescriptor.submit">L�gg till regel</xsl:variable>
	
	<xsl:variable name="i18n.SelectedEvaluatorTypeNotFound">Den valda regeltypen hittades inte</xsl:variable>
	<xsl:variable name="i18n.evaluatorTypeID">Regeltyp</xsl:variable>
	<xsl:variable name="i18n.flowVersion">version</xsl:variable>
	<xsl:variable name="i18n.versions">Versioner</xsl:variable>
	<xsl:variable name="i18n.version.title">Version</xsl:variable>
	<xsl:variable name="i18n.flowHasNoOtherVersions">Det finns inga andra versioner av denna e-tj�nst.</xsl:variable>
	
	<xsl:variable name="i18n.addNewVersion">L�gg till en ny version</xsl:variable>
	<xsl:variable name="i18n.createNewFlow">Skapa en ny e-tj�nst</xsl:variable>
	
	<xsl:variable name="i18n.deleteFlowFamilyDisabledHasInstances">Det g�r inte att ta bort den h�r e-tj�nsten f�r en eller flera av dess versioner har �renden kopplade till sig.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamilyDisabledIsPublished">Det g�r inte att ta bort den h�r e-tj�nsten f�r en eller flera av dess versioner �r publicerade.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamilyConfirm">�r du s�ker p� att du vill ta bort samtliga versioner av e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamily.title">Ta bort samtliga versioner av e-tj�nsten</xsl:variable>
	
	<xsl:variable name="i18n.versions.description">Tabellen nedan visar samtliga versioner av denna e-tj�nst. Markera en e-tj�nst i listan f�r att skapa en ny version eller en helt ny e-tj�nst baserat p� den valda versionen.</xsl:variable>
	<xsl:variable name="i18n.FlowNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowFormNotFound">Den beg�rda blanketten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.unknownQueryType">Ok�nd fr�getyp</xsl:variable>
	<xsl:variable name="i18n.unknownEvaluatorType">Ok�nd regeltyp</xsl:variable>
	
	<xsl:variable name="i18n.administrateStandardStatuses">Adm. statusgrupper</xsl:variable>
	<xsl:variable name="i18n.ListStandardStatusGroups.title">Statusgrupper</xsl:variable>
	<xsl:variable name="i18n.ListStandardStatusGroups.description">Nedan visas samtliga statusgrupper i systemet.</xsl:variable>
	<xsl:variable name="i18n.ListStandardStatusGroups.noStandardStatusGroupsFound">Inga statusgrupper hittades.</xsl:variable>
	<xsl:variable name="i18n.ShowStandardStatusGroup.title">Statusgrupp:</xsl:variable>
	<xsl:variable name="i18n.ShowStandardStatusGroup.description">Nedan visas standardstatusar tillh�rande statusgruppen</xsl:variable>
	<xsl:variable name="i18n.ShowStandardStatusGroup.noStandardStatusesFound">Inga standardstatusar hittades.</xsl:variable>
	<xsl:variable name="i18n.ShowStandardStatusGroup.back">Bak�t</xsl:variable>
	<xsl:variable name="i18n.AddStandardStatus">L�gg till standardstatus</xsl:variable>
	<xsl:variable name="i18n.AddStandardStatusGroup">L�gg till statusgrupp</xsl:variable>
	<xsl:variable name="i18n.UpdateStandardStatus">Uppdatera standardstatus</xsl:variable>
	<xsl:variable name="i18n.UpdateStandardStatusGroup">Uppdatera statusgrupp</xsl:variable>
	<xsl:variable name="i18n.CopyStandardStatusGroup">Kopiera statusgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteStandardStatus">Ta bort standardstatus</xsl:variable>
	<xsl:variable name="i18n.DeleteStandardStatus.confirm">Ta bort standardstatus</xsl:variable>
	<xsl:variable name="i18n.DeleteStandardStatusGroup">Ta bort statusgrupp</xsl:variable>
	<xsl:variable name="i18n.DeleteStandardStatusGroup.confirm">Ta bort statusgrupp</xsl:variable>
	<xsl:variable name="i18n.SortStandardStatuses">Sortera standardstatusar i</xsl:variable>
	
	<xsl:variable name="i18n.AddStandardStatuses">L�gg till standardstatusar</xsl:variable>
	<xsl:variable name="i18n.AddStandardStatuses.StandardStatusGroup">Statusgrupp</xsl:variable>
	<xsl:variable name="i18n.AddStandardStatuses.StandardStatusGroup.choose">V�lj statusgrupp</xsl:variable>
	<xsl:variable name="i18n.RequestedFlowFamilyNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyCannotBeDeleted">Den beg�rda e-tj�nsten kan inte tas bort d� en eller flera versioner av den �r publicerade eller har �renden knuta till sig.</xsl:variable>
	<xsl:variable name="i18n.testFlow">Testa e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.downloadxsd.title">Ladda ner XSD schema</xsl:variable>
	
	<xsl:variable name="i18n.tags.title">Extra s�kord (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.tags">Extra s�kord</xsl:variable>
	<xsl:variable name="i18n.checks.title">Krav f�r e-tj�nsten (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.checks">Krav f�r e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.administrateFlowTypes">Adm. kategorier</xsl:variable>
	<xsl:variable name="i18n.ListFlowTypes.title">Kategorier</xsl:variable>
	<xsl:variable name="i18n.ListFlowTypes.description">Nedan visas en lista p� de kategorier som du har beh�righet att komma �t.</xsl:variable>
	<xsl:variable name="i18n.categories">Underkategorier</xsl:variable>
	<xsl:variable name="i18n.noFlowTypesFound">Inga kategorier hittades</xsl:variable>
	<xsl:variable name="i18n.addFlowType">L�gg till kategori</xsl:variable>
	<xsl:variable name="i18n.flowFamilies">E-tj�nster</xsl:variable>
	<xsl:variable name="i18n.deleteFlowTypeDisabledHasFlows">Den h�r kategorin g�r inte att ta bort f�r den har en eller flera e-tj�nster kopplade till sig!</xsl:variable>
	<xsl:variable name="i18n.deleteFlowType">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.showFlowType">Visa kategori</xsl:variable>
	<xsl:variable name="i18n.updateFlowType">Uppdatera kategori</xsl:variable>
	
	<xsl:variable name="i18n.allowedGroups">Grupper:</xsl:variable>
	<xsl:variable name="i18n.allowedUsers">Anv�ndare:</xsl:variable>

	<xsl:variable name="i18n.allowedQueryTypes">Fr�getyper</xsl:variable>
	<xsl:variable name="i18n.noCategory">Ingen underkategori</xsl:variable>
	<xsl:variable name="i18n.noCategories">Det finns inga underkategorier f�r den h�r kategorin</xsl:variable>
	<xsl:variable name="i18n.updateCategory">Uppdatera underkategorin</xsl:variable>
	<xsl:variable name="i18n.deleteCategory">Ta bort underkategorin</xsl:variable>
	<xsl:variable name="i18n.addCategory">L�gg till underkategori</xsl:variable>
	<xsl:variable name="i18n.noAllowedQueryTypes">Inga fr�getyper till�ts f�r denna kategori.</xsl:variable>
	<xsl:variable name="i18n.AddFlowType.title">L�gg till kategori</xsl:variable>
	<xsl:variable name="i18n.AddFlowType.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowType.title">Uppdatera kategori</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowType.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AddCategory.title">L�gg till underkategori</xsl:variable>
	<xsl:variable name="i18n.AddCategory.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateCategory.title">Uppdatera underkategorin</xsl:variable>
	<xsl:variable name="i18n.UpdateCategory.submit">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.Managers">Handl�ggare</xsl:variable>
	<xsl:variable name="i18n.ManagersDescription">F�ljande grupper och anv�ndare f�r handl�gga �renden f�r denna e-tj�nst.</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowFamilyManagers">V�lj handl�ggare</xsl:variable>
	<xsl:variable name="i18n.UpdateAutoManagerAssignment">Automatisk tilldelning av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.NoManagers">Inga handl�ggare har �tkomst till �renden f�r den h�r e-tj�nsten.</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.title">Uppdatera handl�ggare f�r e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.openModal">Avancerade inst�llningar</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.validFromDate">Giltig fr�n och med</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.validToDate">Giltig till och med</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.restrictedUser">Begr�nsad (kan endast se tilldelade �renden)</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.restrictedGroup">Begr�nsad (kan endast se tilldelade �renden)</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.allowUpdatingManagers">Till�t byte av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.notifyGroupMembersPersonally">Notifiera gruppens medlemmar personligen.</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.notifyGroupMembersPersonally.disclamer">OBS, max 100 gruppmedlemmar kommer f� notifieringen.</xsl:variable>
	
	
	<xsl:variable name="i18n.UpdateManagers.Modal.notificationEmailAddresses">E-postadresser f�r notifiering till funktionsbrevl�da (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.notificationEmailAddresses.info">F�r att notifieringar ska skickas till nedanst�ende adresser m�ste notifieringen 'E-post vid tilldelning av �rende' under 'Notifikationer till handl�ggargrupper' vara aktiverad.</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.Title">Inst�llningar f�r</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.Close">St�ng</xsl:variable>
	<xsl:variable name="i18n.Manager.validFromDate">Giltig fr�n och med</xsl:variable>
	<xsl:variable name="i18n.Manager.validToDate">Giltig till och med</xsl:variable>
	<xsl:variable name="i18n.Manager.validFromToDate">till och med</xsl:variable>
	<xsl:variable name="i18n.Manager.restricted">Begr�nsad</xsl:variable>
	<xsl:variable name="i18n.Manager.allowUpdatingManagers">F�r byta handl�ggare</xsl:variable>
	<xsl:variable name="i18n.Manager.notifyGroupMembersPersonally">Notifierar gruppens medlemmar</xsl:variable>
	
	
	<xsl:variable name="i18n.AddFlowCategoryNotFound">Den beg�rda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.AddCategoryFailedFlowTypeNotFound">Den beg�rda kategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedCategoryNotFound">Den beg�rda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedCategoryNotFound">Den beg�rda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedFlowTypeNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedFlowTypeNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ShowFailedFlowTypeNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStepNotFound">Det beg�rda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStepNotFound">Det beg�rda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedQueryDescriptorNotFound">Den beg�rda fr�gan hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedQueryDescriptorNotFound">Den beg�rda fr�gan hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedEvaluatorDescriptorNotFound">Den beg�rda regeln hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedEvaluatorDescriptorNotFound">Den beg�rda regeln hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStatusNotFound">Den beg�rda statusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStatusNotFound">Den beg�rda statusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStandardStatusNotFound">Den beg�rda standardstatusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStandardStatusNotFound">Den beg�rda standardstatusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowTypeQueryTypeAccessDenied">Den h�r typen av e-tj�nster har inte beh�righet att anv�nda den valda fr�getypen.</xsl:variable>
	<xsl:variable name="i18n.requireAuthentication">Kr�v inloggning</xsl:variable>
	<xsl:variable name="i18n.requirersAuthentication">Kr�ver inloggning</xsl:variable>
	<xsl:variable name="i18n.requirersNoAuthentication">Kr�ver inte inloggning</xsl:variable>
	<xsl:variable name="i18n.Flow.showLoginQuestion">Fr�ga om inloggning</xsl:variable>
	<xsl:variable name="i18n.requireSigning">Kr�v signering</xsl:variable>
	<xsl:variable name="i18n.Flow.allowForeignIDs">Till�t anv�ndare inloggade med eIDAS</xsl:variable>
	<xsl:variable name="i18n.Flow.useSequentialSigning">Sekventiell signering</xsl:variable>
	<xsl:variable name="i18n.Flow.skipPosterSigning">Hoppa �ver signering f�r f�rsta parten</xsl:variable>
	<xsl:variable name="i18n.Flow.skipPosterSigning.description">(b�r endast anv�ndas vid flerpartssignering)</xsl:variable>
	<xsl:variable name="i18n.Flow.allowPosterMultipartSigning">Till�t f�rsta parten att flerpartsignera �rendet</xsl:variable>
	<xsl:variable name="i18n.Flow.appendSigningSignatureToPDF">Bifoga signeringsunderlag i PDF</xsl:variable>
	<xsl:variable name="i18n.Flow.showPreviousSignaturesToSigners">Vid signering visa tidigare parter som signerat</xsl:variable>
	<xsl:variable name="i18n.requiresSigning">Kr�ver signering</xsl:variable>
	<xsl:variable name="i18n.requiresNoSigning">Kr�ver inte signering</xsl:variable>
	<xsl:variable name="i18n.MissingDefaultStatusMapping">E-tj�nsten g�r inte att publicera d� dess statusar inte inneh�ller samtliga obligatoriska statusmappningar. Klicka ur "Aktivera" e-tj�nsten" och spara g� sedan tillbaka till e-tj�nst�versikten f�r att kontrollera statusarna.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.Part1">Anv�ndaren</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.Part2">handl�gger aktiva �renden f�r den h�r e-tj�nsten och f�r d�rf�r inte plockas bort</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.MemberOfGroups">medlem i</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerGroupError.Part1">Gruppen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerGroupError.Part2">handl�gger aktiva �renden f�r den h�r e-tj�nsten och f�r d�rf�r inte plockas bort</xsl:variable>
	
	<xsl:variable name="i18n.exportFlow.title">Exportera e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.EvaluatorExportException.Part1">Ett fel uppstod n�r regelen</xsl:variable>
	<xsl:variable name="i18n.EvaluatorExportException.Part2">skulle exporteras, kontakta administrat�ren f�r mer information.</xsl:variable>
	<xsl:variable name="i18n.QueryExportException.Part1">Ett fel uppstod n�r fr�gan</xsl:variable>
	<xsl:variable name="i18n.QueryExportException.Part2">skulle exporteras, kontakta administrat�ren f�r mer information.</xsl:variable>
	<xsl:variable name="i18n.FlowImportFlowFamlilyNotFound">Den beg�rda e-tj�nsten hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.SelectImportTargetType.title">V�lj kategori</xsl:variable>
	<xsl:variable name="i18n.SelectImportTargetType.description">V�lj vilken kategori av e-tj�nst du vill importera.</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewVersion.title">Importera ny version av e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewVersion.description">Anv�nd formul�ret nedan f�r att importera en ny version. Filen du v�ljer beh�ver vara av typen oeflow.</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewFamily.title">Importera ny e-tj�nst i kategorin</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewFamily.description">Anv�nd formul�ret nedan f�r att importera en ny e-tj�nst. Filen du v�ljer beh�ver vara av typen oeflow.</xsl:variable>
	<xsl:variable name="i18n.selectFlowFile">V�lj fil</xsl:variable>	
	
	<xsl:variable name="i18n.ImportFlow.submit">Importera</xsl:variable>

	<xsl:variable name="i18n.importFlow">Importera e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.importNewFlowVersion">Importera en ny version</xsl:variable>
	
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part1">Fr�gan</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part2">�r av en typ som inte till�ts i e-tj�nster av kategori</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part3">.</xsl:variable>
	
	<xsl:variable name="i18n.EvaluatorTypeNotFound.Part1">Regeltypen f�r regeln</xsl:variable>
	<xsl:variable name="i18n.EvaluatorTypeNotFound.Part2">hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.QueryTypeNotFound.Part1">Fr�getypen f�r fr�gan</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotFound.Part2">hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.EvaluatorImportException.Part1">Ett fel uppstod n�r regeln</xsl:variable>
	<xsl:variable name="i18n.EvaluatorImportException.Part2">skulle importeras, kontakta administrat�ren f�r mer information.</xsl:variable>
	<xsl:variable name="i18n.QueryImportException.Part1">Ett fel uppstod n�r fr�gan</xsl:variable>
	<xsl:variable name="i18n.QueryImportException.Part2">skulle importeras, kontakta administrat�ren f�r mer information.</xsl:variable>
	
	<xsl:variable name="i18n.InvalidFileExtension.Part1">Filen</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.Part2">�r av en felaktig filtyp.</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.Part3">F�ljande filtyper �r till�tna:</xsl:variable>
	
	<xsl:variable name="i18n.UnableToParseFile.part1">Den gick inte att tolka inneh�llet i filen </xsl:variable>
	<xsl:variable name="i18n.UnableToParseFile.part2">.</xsl:variable>
	
	<xsl:variable name="i18n.showSubmitSurvey">Visa anv�ndarunders�kning</xsl:variable>
	<xsl:variable name="i18n.hideSubmitStepText">D�lj steget "Skicka in" i steg�verblicken</xsl:variable>

	<xsl:variable name="i18n.hideSaveButton">D�lj sparaknappen</xsl:variable>
	
	<xsl:variable name="i18n.FlowSurveysTitle">Anv�ndarunders�kningar</xsl:variable>
	
	<xsl:variable name="i18n.UpdateNotificationSettings">�ndra inst�llningar</xsl:variable>
	<xsl:variable name="i18n.Notifications">Notifieringar</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.title">Notifieringsinst�llningar f�r e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.reset.confirm">�r du helt s�ker p� att du vill �terst�lla standardv�rden f�r notifieringar f�r denna e-tj�nst?</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.reset">�terst�ll standardv�rden</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.submit">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.StatisticsSettings">Statistikinst�llningar</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.None">Generera ingen statistik</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.Internal">Generera statistik men visa den endast f�r interna anv�ndare</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.Public">Generera statistik och visa den publikt</xsl:variable>

	<xsl:variable name="i18n.skipOverview">Hoppa �ver �versiktssidan</xsl:variable>
	
	<xsl:variable name="i18n.MissingDefaultStatusMappingForMultiSigning">E-tj�nsten g�r inte att publicera d� den saknar statusmappning f�r flerpartssignering. Klicka ur "Aktivera" e-tj�nsten" och spara g� sedan tillbaka till e-tj�nst�versikten f�r att kontrollera statusarna.</xsl:variable>
	<xsl:variable name="i18n.MissingDefaultStatusMappingForPayment">E-tj�nsten g�r inte att publicera d� den saknar statusmappning f�r betalning. Klicka ur "Aktivera" e-tj�nsten" och spara g� sedan tillbaka till e-tj�nst�versikten f�r att kontrollera statusarna.</xsl:variable>
	
	<xsl:variable name="i18n.hasNoFlowForm">Den h�r e-tj�nsten har ingen PDF blankett kopplad till sig.</xsl:variable>
	<xsl:variable name="i18n.MayNotRemoveFlowFormIfNoSteps">Du f�r inte ta bort blanketten om e-tj�nsten �r aktiverad och saknar steg</xsl:variable>
	<xsl:variable name="i18n.MayNotAddFlowFormIfOverviewSkipIsSet">Du kan inte l�gga till en blankett om e-tj�nsten �r inst�lld p� att hoppa �ver �versiktssidan</xsl:variable>
	<xsl:variable name="i18n.MayNotSetOverviewIfFlowFormIsSet">Du kan inte hoppa �ver �versiktssidan om det finns en blankett kopplad till e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.MayNotSetOverviewIfFlowFormIsSet.description">ej m�jligt om det finns en blankett kopplad till e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.FlowHasNoContent">Du kan inte aktivera e-tj�nsten d� den inte har n�gra steg eller har n�gon blankett</xsl:variable>
	<xsl:variable name="i18n.FlowHasNoStepsAndOverviewSkipIsSet">Du kan inte aktivera e-tj�nsten med alterntivet 'hoppa �ver �versiktssidan' satt d� e-tj�nsten inte inneh�ller n�gra steg.</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUse">Kortnamnet</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUse2">anv�nds redan av e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUseBySystem">anv�nds redan av en annan del av systemet.</xsl:variable>
	<xsl:variable name="i18n.aliases.title">Kortnamn f�r e-tj�nsten (ett per rad ex. bygga eller skola, giltiga tecken �r A-Z a-z 0-9 _- )</xsl:variable>
	<xsl:variable name="i18n.aliases">Kortnamn f�r e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.aliases.url">Kortadress</xsl:variable>
	<xsl:variable name="i18n.LacksPublishAccess">Du saknar publiceringsr�ttigheter och kan d�rf�r inte �ndra publiceringsdatum eller aktivera e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.Filter">S�k</xsl:variable>
	<xsl:variable name="i18n.link">l�nk</xsl:variable>

	<xsl:variable name="i18n.ExportQuery.title">Exportera fr�gan</xsl:variable>
	<xsl:variable name="i18n.ExportFailedQueryDescriptorNotFound">Den beg�rda fr�gan hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.ImportQueries.title">Importera fr�gor i e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.ImportQueries.description">Anv�nd formul�ret nedan f�r att importera en eller flera fr�gor i det valda steget. Filerna du v�ljer m�sta vara av typen oequery.</xsl:variable>
	<xsl:variable name="i18n.SelectQueryFiles">V�lj filer</xsl:variable>
	<xsl:variable name="i18n.ImportQueries.submit">Importera</xsl:variable>
	<xsl:variable name="i18n.ImportQueries">Importera fr�gor</xsl:variable>
	
	<xsl:variable name="i18n.Events.Title">�ndringslogg</xsl:variable>
	<xsl:variable name="i18n.Events.Full.Title">Fullst�ndig �ndringslogg f�r</xsl:variable>
	<xsl:variable name="i18n.Events.Description">Tabellen nedan visar de senaste �ndringarna f�r samtliga versioner av denna e-tj�nst.</xsl:variable>
	<xsl:variable name="i18n.Events.Full.Description">Tabellen nedan visar alla �ndringarna f�r samtliga versioner av denna e-tj�nst.</xsl:variable>
	<xsl:variable name="i18n.Events.message">H�ndelse</xsl:variable>
	<xsl:variable name="i18n.Events.poster">Anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Events.added">Tidpunkt</xsl:variable>
	<xsl:variable name="i18n.Events.FlowFamilyHasNoEvents">Inga �ndringar hittades.</xsl:variable>
	<xsl:variable name="i18n.Events.ShowAll">Visa alla �ndringar</xsl:variable>
	<xsl:variable name="i18n.Events.Add">L�gg till h�ndelse</xsl:variable>
	<xsl:variable name="i18n.Events.Add.title">L�gg till h�ndelse i �ndringslogg f�r</xsl:variable>
	<xsl:variable name="i18n.Events.Add.submit">L�gg till</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket �verskrider den maximalt till�tna filstorleken p� </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part1">Du bifogade totalt </xsl:variable>
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part2"> filer och den max till�tna storleken �r </xsl:variable>
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part3">!</xsl:variable>

	<xsl:variable name="i18n.FlowType.AdminAccess.Title">Administration</xsl:variable>
	<xsl:variable name="i18n.FlowType.AdminAccess.Description">Ange vilka anv�ndare och grupper som ska ha beh�righet att bygga och administrera e-tj�nster i den h�r kategorin.</xsl:variable>
	
	<xsl:variable name="i18n.onlyModuleAdminAccess">Endast globala administat�rer har beh�righet att bygga och administrera e-tj�nster i den h�r kategorin.</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.UserAccess.Title">�tkomst till e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.FlowType.UserAccess.Description">Om du vill begr�nsa �tkomsten till e-tj�nsterna inom denna kategori s� kan du kryssa i rutan och sedan ange vilka anv�ndare och grupper som ska f� komma �t e-tj�nsterna.</xsl:variable>
	<xsl:variable name="i18n.enableAccessFiltering">Aktivera begr�nsad �tkomst</xsl:variable>

	<xsl:variable name="i18n.FlowType.FlowPublishedNotificationUsers.Title">Notifieringar</xsl:variable>
	<xsl:variable name="i18n.FlowType.FlowPublishedNotificationUsers.Description">F�ljande anv�ndare kommer att notifieras automatiskt n�r e-tj�nster inom denna kategori publiceras.</xsl:variable>
	
	<xsl:variable name="i18n.noUserAccess">Inga anv�ndare eller grupper har �tkomst till e-tj�nster i denna kategori.</xsl:variable>
	
	<xsl:variable name="i18n.noAccessFilter">Samtliga anv�ndare har �tkomst till e-tj�nsterna i denna kategori.</xsl:variable>

	<xsl:variable name="i18n.noFlowPublishedNotificationUsers">Inga anv�ndare notifieras n�r e-tj�nster inom denna kategori publiceras.</xsl:variable>

	<xsl:variable name="i18n.onlyNotifyOnNewFlowPublications">Notifiera endast vid ny publicering (ny e-tj�nst eller efter avpublicering).</xsl:variable>

	<xsl:variable name="i18n.hideManagerDetails">D�lj uppgifter om handl�ggare</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.Icon">Ikon</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.UploadIcon">Till�tna filtyper: jpg, png, gif, bmp</xsl:variable>
	<xsl:variable name="i18n.FlowType.DeleteIcon">Ta bort</xsl:variable>
	<xsl:variable name="i18n.FlowType.IconColor">Bakgrundsf�rg</xsl:variable>
	<xsl:variable name="i18n.FlowType.iconColor">bakgrundsf�rg</xsl:variable>
	<xsl:variable name="i18n.FlowType.UseIconOnAllFlows">Anv�nd kategorins ikon p� samtliga e-tj�nster</xsl:variable>
	
	<xsl:variable name="i18n.All">Visa alla e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.Published">Publicerade</xsl:variable>
	<xsl:variable name="i18n.Unpublished">Ej publicerade</xsl:variable>
	<xsl:variable name="i18n.statusFilter">Visa</xsl:variable>
	
	<xsl:variable name="i18n.hiddenFromOverview">Dold p� f�rstasidan</xsl:variable>
	<xsl:variable name="i18n.shownOnOverview">Visas p� f�rstasidan</xsl:variable>
	<xsl:variable name="i18n.hideFromOverview">D�lj e-tj�nsten p� e-tj�nsteportalens f�rstasida</xsl:variable>
	<xsl:variable name="i18n.hideInternalMessages">St�ng av interna noteringar</xsl:variable>
	<xsl:variable name="i18n.hideExternalMessages">St�ng av meddelandefunktionen</xsl:variable>
	<xsl:variable name="i18n.hideExternalMessageAttachments">St�ng av filuppladdningen i meddelandefunktionen</xsl:variable>
	<xsl:variable name="i18n.showExternalMessageAttachmentsForManager">Till�t enbart handl�ggare att ladda upp filer</xsl:variable>
	<xsl:variable name="i18n.readReceiptsEnabled">Aktivera beg�ran om kvittens i meddelandefunktionen</xsl:variable>
	<xsl:variable name="i18n.readReceiptsEnabledByDefault">Aktivera f�rval av Beg�r kvittens f�r detta meddelande</xsl:variable>
	<xsl:variable name="i18n.Flow.hideFromUser">D�lj inskickade �renden fr�n mina sidor</xsl:variable>
	<xsl:variable name="i18n.Flow.hideFlowInstanceIDFromUser">D�lj �rendenummer fr�n mina sidor och kvittens</xsl:variable>
	
	<xsl:variable name="i18n.Flow.otherSettings">�vrigt</xsl:variable>
	<xsl:variable name="i18n.Flow.alwaysStartFromFirstStep">�ppna alltid sparade �renden fr�n f�rsta steget</xsl:variable>
	
	<xsl:variable name="i18n.ChangeFlowType.linkTitle">�ndra kategori p� e-tj�nsten</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.title">�ndra kategori p� e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.description">H�r kan du �ndra kategori p� e-tj�nsten. T�nk p� att alla versioner p�verkas.</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.ChooseNewFlowType">V�lj ny kategori</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.submit">�ndra kategori</xsl:variable>
	
	<xsl:variable name="i18n.sortStatuses">Sortera statusar</xsl:variable>
	<xsl:variable name="i18n.SortFlowStatuses.title">Sortera statusar</xsl:variable>
	<xsl:variable name="i18n.MoveStatus">Flytta status</xsl:variable>
	
	<xsl:variable name="i18n.ReplaceFlowStatusesWithStandard">Ers�tt statusar med statusgrupp</xsl:variable>
	<xsl:variable name="i18n.ReplaceFlowStatusesWithStandard.title">Ers�tt statusar med statusgrupp i e-tj�nst</xsl:variable>
	
	<xsl:variable name="i18n.MoveEvaluator">Flytta regel</xsl:variable>
	<xsl:variable name="i18n.PaymentSupportEnabled">Aktivera betalst�d</xsl:variable>
	
	<xsl:variable name="i18n.PreviewEnabled">F�rhandsgranskning</xsl:variable>
	<xsl:variable name="i18n.PaymentEnabled">Betalst�d</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.allowAnonymousAccess">Visa f�r ej inloggade anv�ndare</xsl:variable>
	
	<xsl:variable name="i18n.NoManagersSet">E-tj�nsten g�r inte att publicera d� den saknar handl�ggare.</xsl:variable>
	<xsl:variable name="i18n.UnpublishFlowFamily">Avpublicera alla versioner</xsl:variable>
	<xsl:variable name="i18n.UnpublishFlowFamilyConfirm">�r du s�ker p� att du vill avpublicera alla versioner av e-tj�nsten</xsl:variable>
	
	<xsl:variable name="i18n.ReCacheFlow">Cacha om e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.FlowForm.externalIcon">Visa ikon f�r extern l�nk</xsl:variable>
	
	<xsl:variable name="i18n.FlowFamilyID">E-tj�nstens familje ID</xsl:variable>
	<xsl:variable name="i18n.FlowID">Versionens ID</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.UsesAutoManagerAssignment">E-tj�nsten anv�nder automatisk tilldelning av handl�ggare.</xsl:variable>
	
	<xsl:variable name="i18n.AutoManagerAssignment.title">Automatisk tilldelning av handl�ggare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.description">H�r v�ljer du vilka handl�ggare som ska tilldelas �renden automatiskt och n�r det ska ske.</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.submit">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.On.Submit">Regler vid nytt �rende</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules">Tilldelning av handl�ggare baserat p� attributv�rden</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Add">L�gg till regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.AttributeName">Attribut</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.AttributeValues">V�rden</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Inverted">Inverterad</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Users">Anv�ndare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Groups">Grupper</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Row">rad</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Update">�ndra regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Save">Spara</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Delete">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.DeleteConfirm">�r du s�ker p� att du vill ta bort regeln</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.AttributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.AttributeValues">V�rden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Invert">Invertera (Om inget av v�rderna p� regeln matchar attributets v�rde s� aktiveras regeln)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.IncludeUnsetAttribute">Aktivera regeln �ven d� attributet �r tomt i �rendet (d� fr�gan ej �r besvarad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Users">Anv�ndare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Groups">Grupper med begr�nsad beh�righet</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Always">Handl�ggare som alltid tilldelas</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.NoMatch">Tilldelning av handl�ggare om ingen regel matchar</xsl:variable>
	
	<xsl:variable name="i18n.AutoManagerAssignment.On.StatusChange">Regler vid statusbyte</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRules">Tilldelning av handl�ggare vid statusbyte</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRules.StatusName">Status</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRules.AddManagers">L�gg till handl�ggare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRules.RemovePreviousManagers">Ta bort befintliga</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.UseStatusAttributeColumnTitle">Attributaktivering</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.StatusName">Status</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.AddManagers">L�gg till handl�ggare vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.RemovePreviousManagers">Ta bort befintliga handl�ggare vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.SendNotification">E-postnotifiering n�r �renden tilldelats handl�ggare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.SendNotificationColumnTitle">E-postnotifiering</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.EmailRecipients">E-postadresser f�r notifiering (en adress per rad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.StatusAttributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.AttributeValues">V�rden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.IncludeUnsetStatusAttribute">Aktivera regeln �ven d� attributet �r tomt i �rendet (d� fr�gan ej �r besvarad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.UseStatusAttribute">Anv�nd attribut f�r regelaktivering</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.StatusRule.UseStatusAttributeInfo">Regeln aktiveras vid byte till ovan angiven status, om namngivet attribut �r satt till n�got av nedanst�ende v�rden (eller inversen)</xsl:variable>
	<xsl:variable name="i18n.ValidationError.DuplicateStatusRule">Det finns redan en regel f�r denna status d�r attributaktivering ej �r p�slaget. D� kan ingen ytterligare regel f�r den statusen skapas.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.NoActionsSelected">Du m�ste v�lja att antingen l�gga till eller ta bort handl�ggare, eller b�de och.</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	
	<xsl:variable name="i18n.MessageTemplates.title">Meddelandemallar</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.add">L�gg till meddelandemall</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.update">�ndra meddelandemallar</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.submit">Spara meddelandemall</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.deleteConfirm">�r du s�ker p� att du vill ta bort meddelandemallen?</xsl:variable>
	<xsl:variable name="i18n.MessageTemplates.noMessageTemplates">Inga meddelandemallar finns.</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.name">Namn</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.type">Ange vilken typ av meddelanden som den h�r mallen g�ller f�r</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.type.external">Meddelanden</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.type.internal">Interna noteringar</xsl:variable>
	<xsl:variable name="i18n.MessageTemplate.type.all">Meddelanden och interna noteringar</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.FullManagerOrFallbackManagerRequired">F�r att e-tj�nsten ska f� anv�nda automatiskt tilldelning av handl�ggare s� m�ste den antingen ha minst 1 fullst�ndig handl�ggare, alltid tilldela en handl�ggare eller tilldela handl�ggare n�r ingen regel matchar.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.MessageTemplateNotFound">Den beg�rda meddelandemallen hittades inte!</xsl:variable>
	
	<xsl:variable name="i18n.DescriptionColumnSettings.Title">Inst�llningar f�r �rendebeskrivningar</xsl:variable>
	<xsl:variable name="i18n.DescriptionColumnSettings.Description">F�r att g�ra det l�ttare att skilja p� �renden s� finns m�jligheten att ge dem en beskrivning under mina sidor och i handl�ggargr�nssnittet. Som standard h�mtas beskrivningen fr�n descriptionattributet om inget annat anges. Via f�lten nedan kan en valfri str�ng anges med b�de fast text och $attribute{} taggar f�r beskrivningen i de respektive gr�nssnitten. �ndringar sl�r inte igenom p� befintliga �renden.</xsl:variable>
	<xsl:variable name="i18n.userDescriptionTemplate">Beskrivning p� mina sidor</xsl:variable>
	<xsl:variable name="i18n.managerDescriptionTemplate">Beskrivning i handl�ggargr�nssnittet</xsl:variable>
	
	<xsl:variable name="i18n.ExtensionErrors">Ett validerings fel har uppst�tt i ett till�gg, kolla l�ngre ner efter vad som �r fel.</xsl:variable>
	
	<xsl:variable name="i18n.ShowMore">Visa mer</xsl:variable>
	<xsl:variable name="i18n.ShowLess">Visa mindre</xsl:variable>
	<xsl:variable name="i18n.ShowOldVersions">Visa �ldre versioner</xsl:variable>
	<xsl:variable name="i18n.ExtensionProviders">Till�gg och extrafunktioner</xsl:variable>
	<xsl:variable name="i18n.ShowInactiveExtensionProviders">Visa inaktiva till�gg</xsl:variable>
	<xsl:variable name="i18n.HideInactiveExtensionProviders">D�lj inaktiva till�gg</xsl:variable>
	
	<xsl:variable name="i18n.ExtensionProviders.by">av</xsl:variable>
	<xsl:variable name="i18n.ExtensionProviders.activated">aktiverade</xsl:variable>

	<xsl:variable name="i18n.Row">Rad</xsl:variable>
	<xsl:variable name="i18n.AddQueryInStep.title">L�gg till fr�ga i steget</xsl:variable>
	<xsl:variable name="i18n.ImportQueriesInStep.title">Importera fr�gor i steget</xsl:variable>
	<xsl:variable name="i18n.SortEvaluators.title">Sortera regler kopplad till fr�gan</xsl:variable>
	<xsl:variable name="i18n.PreviewQuery.title">F�rhandsgranska fr�gan</xsl:variable>
	
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.commentVisibility">visas endast f�r e-tj�nstbyggaren</xsl:variable>
	<xsl:variable name="i18n.comment">kommentar</xsl:variable>
	<xsl:variable name="i18n.ShowAllComments">Visa kommentarer</xsl:variable>
	<xsl:variable name="i18n.HideAllComments">D�lj kommentarer</xsl:variable>
	
	<xsl:variable name="i18n.ExtensionEnabled">Till�gg aktiverat</xsl:variable>
	<xsl:variable name="i18n.ExtensionDisabled">Till�gg inaktiverat</xsl:variable>
	
	<xsl:variable name="i18n.PreviewQueries">F�rhandsgranska fr�gor</xsl:variable>
	
	<xsl:variable name="i18n.Step.OneQuery">fr�ga</xsl:variable>
	<xsl:variable name="i18n.Step.MultipleQueries">fr�gor</xsl:variable>
	
	<xsl:variable name="i18n.Flow.hideFlowInstances">D�lj �renden</xsl:variable>
	<xsl:variable name="i18n.Flow.hideFromManager">D�lj �renden i handl�ggargr�nssnittet (g�ller inte fliken alla, anv�nd med f�rsiktighet)</xsl:variable>
	
	<xsl:variable name="i18n.Flow.link">L�nkning</xsl:variable>
	<xsl:variable name="i18n.Flow.activationAndPublishing">Aktivering och publicering</xsl:variable>
	<xsl:variable name="i18n.Flow.preview">F�rhandsgranskning</xsl:variable>	
	<xsl:variable name="i18n.Flow.paymentSettings">Betalning</xsl:variable>
	<xsl:variable name="i18n.Flow.authenticationSettings">Inloggning</xsl:variable>
	<xsl:variable name="i18n.Flow.signing">Signering</xsl:variable>
	<xsl:variable name="i18n.Flow.surveys">Unders�kning</xsl:variable>
	<xsl:variable name="i18n.Flow.messagesAndNotes">Meddelanden och noteringar</xsl:variable>
	<xsl:variable name="i18n.Flow.hideDetailsAndFunctions">D�lj information och funktioner</xsl:variable>
	<xsl:variable name="i18n.Flow.statuses">Statusar/Handl�ggningsprocess</xsl:variable>

	<xsl:variable name="i18n.Shortcuts">Verktyg</xsl:variable>
	
	<xsl:variable name="i18n.DataTable.decimal">,</xsl:variable>
	<xsl:variable name="i18n.DataTable.emptyTable">Finns inga e-tj�nster att visa</xsl:variable>
	<xsl:variable name="i18n.DataTable.info">Visar _START_ till _END_ av _TOTAL_ e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoEmpty">Visar 0 till 0 av 0 e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoFiltered">(filtrerat fr�n totalt _MAX_ e-tj�nster)</xsl:variable>
	<xsl:variable name="i18n.DataTable.infoPostFix"/>
	<xsl:variable name="i18n.DataTable.thousands"/>
	<xsl:variable name="i18n.DataTable.lengthMenu">Visa _MENU_ e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DataTable.loadingRecords">H�mtar e-tj�nster...</xsl:variable>
	<xsl:variable name="i18n.DataTable.processing">Bearbetar...</xsl:variable>
	<xsl:variable name="i18n.DataTable.search">Filtrera i listan</xsl:variable>
	<xsl:variable name="i18n.DataTable.zeroRecords">Inga matchande e-tj�nster hittades</xsl:variable>
	<xsl:variable name="i18n.DataTable.fetchError">Ett fel uppstod vid h�mtning av e-tj�nster, v�nligen f�rs�k igen senare.</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.first">F�rsta</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.last">Sista</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.next">N�sta</xsl:variable>
	<xsl:variable name="i18n.DataTable.paginate.previous">F�reg�ende</xsl:variable>
	<xsl:variable name="i18n.DataTable.aria.sortAscending">: aktivera f�r att sortera kolumnen stigande</xsl:variable>
	<xsl:variable name="i18n.DataTable.aria.sortDescending">: aktivera f�r att sortera kolumnen fallande</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.none">Inga rader markerade</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.one">1 rad markerad</xsl:variable>
	<xsl:variable name="i18n.DataTable.selection.many">%d rader markerade</xsl:variable>	
	
</xsl:stylesheet>
