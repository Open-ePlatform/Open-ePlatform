<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:include href="FlowTestCommon.sv.xsl"/>
	
	<xsl:include href="FlowAdminModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="java.flowNameCopySuffix"> (kopia)</xsl:variable>
	<xsl:variable name="java.fileMissing">Filen saknas</xsl:variable>
	
	<xsl:variable name="java.eventCopyFlowMessage">Kopierade version</xsl:variable>
	<xsl:variable name="java.eventUpdateIconMessage">Updaterade ikon till</xsl:variable>
	<xsl:variable name="java.eventUpdateNotificationsMessage">Uppdaterade notifikations inställningar</xsl:variable>
	<xsl:variable name="java.eventSortFlowMessage">Sorterade frågor och steg</xsl:variable>
	<xsl:variable name="java.eventImportFlowMessage">Importerade version</xsl:variable>
	<xsl:variable name="java.eventImportQueriesMessage">Importerade frågor</xsl:variable>
	<xsl:variable name="java.eventFlowFamilyUpdatedMessage">Uppdaterade handläggare</xsl:variable>
	<xsl:variable name="java.eventFlowAddedMessage">Skapade version</xsl:variable>
	<xsl:variable name="java.eventFlowUpdatedMessage">Uppdaterade grundinformation</xsl:variable>
	<xsl:variable name="java.eventFlowDeletedMessage">Tog bort version</xsl:variable>
	<xsl:variable name="java.eventStepAddedMessage">Skapade steg</xsl:variable>
	<xsl:variable name="java.eventStepUpdatedMessage">Uppdaterade steg</xsl:variable>
	<xsl:variable name="java.eventStepDeletedMessage">Tog bort steg</xsl:variable>
	<xsl:variable name="java.eventQueryAddedMessage">Skapade fråga</xsl:variable>
	<xsl:variable name="java.eventQueryUpdatedMessage">Uppdaterade fråga</xsl:variable>
	<xsl:variable name="java.eventQueryDeletedMessage">Tog bort fråga</xsl:variable>
	<xsl:variable name="java.eventEvaluatorAddedMessage">Skapade regel</xsl:variable>
	<xsl:variable name="java.eventEvaluatorUpdatedMessage">Uppdaterade regel</xsl:variable>
	<xsl:variable name="java.eventEvaluatorDeletedMessage">Tog bort regel</xsl:variable>
	<xsl:variable name="java.eventStatusAddedMessage">Skapade status</xsl:variable>
	<xsl:variable name="java.eventStatusUpdatedMessage">Uppdaterade status</xsl:variable>
	<xsl:variable name="java.eventStatusDeletedMessage">Tog bort status</xsl:variable>
	<xsl:variable name="java.eventChangeFlowType">Ändrade kategori till</xsl:variable>
	<xsl:variable name="java.eventStatusSortMessage">Sorterade statusar</xsl:variable>
	<xsl:variable name="java.eventFlowFormAddedMessage">Skapade blankett</xsl:variable>
	<xsl:variable name="java.eventFlowFormUpdatedMessage">Uppdaterade blankett</xsl:variable>
	<xsl:variable name="java.eventFlowFormDeletedMessage">Tog bort blankett</xsl:variable>
	<xsl:variable name="java.eventFlowInstanceManagerExpired">Handläggare automatiskt borttagen:</xsl:variable>
	<xsl:variable name="java.eventFunctionConfigured">Ändrande konfigurationen av funktionen</xsl:variable>
	<xsl:variable name="java.eventUpdateAutoManagerAssignment">Ändrade inställningar för automatisk tilldelning av handläggare</xsl:variable>
	
	<xsl:variable name="java.bundleListFlows">Lista e-tjänster</xsl:variable>
	<xsl:variable name="java.bundleAddFlow">Lägg till e-tjänst</xsl:variable>
	<xsl:variable name="java.bundleImportFlow">Importera e-tjänst</xsl:variable>
	<xsl:variable name="java.bundleStandardStatuses">Adm. standardstatusar</xsl:variable>
	<xsl:variable name="java.bundleFlowtypes">Adm. kategorier</xsl:variable>
	
	<xsl:variable name="java.defaultLoginHelpLinkName">Så här skaffar du e-legitimation</xsl:variable>
	<xsl:variable name="java.defaultFlowStartLoginQuestionText">
		&lt;p&gt;Om du loggar in så kan vi förifylla frågor med uppgifter vi hämtar från andra system t.ex. skatteverket.&lt;/p&gt;
		&lt;p&gt;Du kan även få återkoppling om ärendets status och notifiering om handläggaren behöver mer information.&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="i18n.flowName">E-tjänst</xsl:variable>
	
	<xsl:variable name="i18n.Flowslist.title">E-tjänster</xsl:variable>
	<xsl:variable name="i18n.Flowlist.description">Nedan visas samtliga e-tjänster i systemet som du har behörighet att administrera.</xsl:variable>
	<xsl:variable name="i18n.typeOfFlow">Typ av e-tjänst</xsl:variable>
	<xsl:variable name="i18n.internal">E-tjänst i denna plattform</xsl:variable>
	<xsl:variable name="i18n.external">Länk till e-tjänst i annan plattform</xsl:variable>
	<xsl:variable name="i18n.externalLink">Länk till e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.OpenExternalFlow">Öppna e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.flowType">Kategori</xsl:variable>
	<xsl:variable name="i18n.flowCategory">Underkategori</xsl:variable>
	<xsl:variable name="i18n.steps">Steg</xsl:variable>
	<xsl:variable name="i18n.queries">Frågor</xsl:variable>
	<xsl:variable name="i18n.SubmittedInstances">Insk. ärenden</xsl:variable>
	<xsl:variable name="i18n.NotSubmittedInstances">Ej insk. ärenden</xsl:variable>
	<xsl:variable name="i18n.status">Status</xsl:variable>
	<xsl:variable name="i18n.noFlowsFound">Inga e-tjänster hittades.</xsl:variable>
	<xsl:variable name="i18n.disabled">Inaktiverad</xsl:variable>
	<xsl:variable name="i18n.published">Publicerad</xsl:variable>
	<xsl:variable name="i18n.notPublished">Ej publicerad</xsl:variable>
	<xsl:variable name="i18n.deleteFlowDisabledIsPublished">Den här e-tjänsten kan inte tas bort eftersom den är publicerad.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowDisabledHasInstances">Den här e-tjänsten kan inte tas bort eftersom det finns en eller flera ärenden kopplade till den.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowConfirm">Ta bort e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.deleteFlow.title">Ta bort e-tjänsten</xsl:variable>
	
	<xsl:variable name="i18n.addFlow">Lägg till e-tjänst</xsl:variable>

	<xsl:variable name="i18n.AddFlow.title">Lägg till e-tjänst</xsl:variable>
	<xsl:variable name="i18n.AddFlow.submit">Lägg till</xsl:variable>
	
	<xsl:variable name="i18n.UpdateFlow.title">Uppdatera e-tjänsten: </xsl:variable>
	<xsl:variable name="i18n.UpdateFlow.submit">Spara ändringar</xsl:variable>

	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.shortDescription">Kort beskrivning</xsl:variable>
	<xsl:variable name="i18n.longDescription">Längre beskrivning</xsl:variable>
	<xsl:variable name="i18n.submittedMessage">Meddelande vid inlämnat ärende</xsl:variable>
	<xsl:variable name="i18n.publishDate">Publiceringsdatum</xsl:variable>
	<xsl:variable name="i18n.unPublishDate">Avpubliceringsdatum</xsl:variable>
	<xsl:variable name="i18n.usePreview">Aktivera förhandsgranskning</xsl:variable>

	<xsl:variable name="i18n.FlowFamily.SharedSettings">Följande inställningar gäller för samtliga versioner av e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp">Visa länk i "Krav för e-tjänsten" (exempelvis länk till instruktion för att skaffa e-legitimation)</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp.Name">Länknamn</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.LoginHelp.URL">Adress</xsl:variable>
	<xsl:variable name="i18n.contact.title">Kontaktuppgifter - Frågor om e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.contact.name">Namn</xsl:variable>
	<xsl:variable name="i18n.contact.email">E-post</xsl:variable>
	<xsl:variable name="i18n.contact.phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.owner.title">Kontaktuppgifter - Personuppgiftsansvarig</xsl:variable>
	<xsl:variable name="i18n.owner.name">Namn</xsl:variable>
	<xsl:variable name="i18n.owner.email">E-post</xsl:variable>

	<xsl:variable name="i18n.SelectedFlowTypeNotFound">Den valda typen hittades inte!</xsl:variable>
	<xsl:variable name="i18n.FlowTypeAccessDenied">Du har inte behörighet till den valda typen!</xsl:variable>

	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet:'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet:'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet:'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet:'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet:'"/>
	<xsl:variable name="i18n.validation.unknownFault" select="'Ett okänt valideringsfel har uppstått.'"/>
	<xsl:variable name="i18n.enableFlow">Aktivera e-tjänsten</xsl:variable>
	
	<xsl:variable name="i18n.baseInfo">Grundinformation</xsl:variable>
	<xsl:variable name="i18n.enabled">Aktiverad</xsl:variable>
	<xsl:variable name="i18n.icon">Ikon</xsl:variable>
	<xsl:variable name="i18n.FlowForm">Blankett</xsl:variable>
	<xsl:variable name="i18n.stepsAndQueries">Frågor och steg</xsl:variable>
	<xsl:variable name="i18n.statuses">Statusar</xsl:variable>
	<xsl:variable name="i18n.flowContainsNoSteps">Inga steg hittades.</xsl:variable>
	<xsl:variable name="i18n.flowHasNoStatuses">Inga statusar hittades.</xsl:variable>
	
	<xsl:variable name="i18n.updateFlowBaseInfo.title">Uppdatera e-tjänstens grundinformation</xsl:variable>
		
	<xsl:variable name="i18n.stepAndQueryManipulationDisabledHasInstances">Det går inte att redigera frågorna och stegen för denna e-tjänst eftersom det finns en eller flera ärenden kopplad till den.</xsl:variable>
	<xsl:variable name="i18n.stepAndQueryManipulationDisabledIsPublished">Det går inte att redigera frågorna och stegen för denna e-tjänst eftersom den är publicerad.</xsl:variable>
	<xsl:variable name="i18n.updateStep.title">Uppdatera steget</xsl:variable>
	<xsl:variable name="i18n.deleteStep.confirm.part1">Ta bort steget</xsl:variable>
	<xsl:variable name="i18n.deleteStep.confirm.part2">och eventuella frågor kopplade till steget?</xsl:variable>
	<xsl:variable name="i18n.deleteStep.title">Ta bort steget</xsl:variable>
	
	<xsl:variable name="i18n.updateQuery.title">Uppdatera frågan</xsl:variable>
	<xsl:variable name="i18n.deleteQuery.confirm">Ta bort frågan</xsl:variable>
	<xsl:variable name="i18n.deleteQuery.title">Ta bort frågan</xsl:variable>
	<xsl:variable name="i18n.addStep">Lägg till steg</xsl:variable>
	<xsl:variable name="i18n.addQuery">Lägg till fråga</xsl:variable>
	<xsl:variable name="i18n.sortStepsAndQueries">Sortera frågor och steg</xsl:variable>
	
	<xsl:variable name="i18n.AddQueryDescriptor.title">Lägg till fråga</xsl:variable>
	<xsl:variable name="i18n.step">Steg</xsl:variable>
	<xsl:variable name="i18n.queryType">Frågetyp</xsl:variable>
	<xsl:variable name="i18n.AddQueryDescriptor.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.SelectedStepNotFound">Det valda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.SelectedQueryTypeNotFound">Den valda frågetypen hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.AddStep.title">Lägg till steg</xsl:variable>
	<xsl:variable name="i18n.AddStep.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateStep.title">Uppdatera steget: </xsl:variable>
	<xsl:variable name="i18n.UpdateStep.submit">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.AddStatus.title">Lägg till status</xsl:variable>
	<xsl:variable name="i18n.AddStatus.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateStatus.title">Uppdatera status: </xsl:variable>
	<xsl:variable name="i18n.UpdateStatus.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.isUserMutable">Tillåt användare att ändra ärenden med denna status</xsl:variable>
	<xsl:variable name="i18n.isUserDeletable">Tillåt användare att ta bort ärenden med denna status</xsl:variable>
	<xsl:variable name="i18n.isAdminMutable">Tillåt handläggare att ändra ärenden med denna status</xsl:variable>
	<xsl:variable name="i18n.isAdminDeletable">Tillåt handläggare att ta bort ärenden med denna status</xsl:variable>
	<xsl:variable name="i18n.defaultStatusMappings.title">Statusmappningar</xsl:variable>
	<xsl:variable name="i18n.defaultStatusMappings.description">Använd denna status vid följande händelser.</xsl:variable>
	<xsl:variable name="i18n.managingTime">Handläggningstid</xsl:variable>
	<xsl:variable name="i18n.required">obligatorisk</xsl:variable>
	<xsl:variable name="i18n.managingTime.description">Antalet dagar som ärenden får befinna sig i denna status innan de för handläggaren blir markerad som akuta.</xsl:variable>
	<xsl:variable name="i18n.Status.requireSigning">Kräv signering från handläggare vid byte till denna status</xsl:variable>
	<xsl:variable name="i18n.Status.useAccessCheck">Begränsa åtkomst till denna status</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerUsersNotFoundError">En eller flera av de valda användarna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.OneOrMoreSelectedManagerGroupsNotFoundError">En eller flera av de valda grupperna hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.1">Användaren</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedUserNotManager.2">är inte handläggare för den här e-tjänsten!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.1">Gruppen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnauthorizedGroupNotManager.2">är inte begränsad handläggare för den här e-tjänsten!</xsl:variable>
	
	<xsl:variable name="i18n.deleteStatusDisabledHasInstances">Den här statusen kan inte tas bort eftersom det finns en eller flera ärenden kopplade till den.</xsl:variable>
	<xsl:variable name="i18n.updateStatus.link.title">Uppdatera statusen</xsl:variable>
	<xsl:variable name="i18n.deleteStatus.link.title">Ta bort statusen</xsl:variable>
	<xsl:variable name="i18n.deleteStatus.confirm">Ta bort statusen</xsl:variable>
	<xsl:variable name="i18n.addStatus">Lägg till status</xsl:variable>
	
	<xsl:variable name="i18n.statusContentType.title">Innehåll</xsl:variable>
	<xsl:variable name="i18n.statusContentType.description">Välj vilken typ av ärenden som den här statusen kommer att innehålla.</xsl:variable>
	<xsl:variable name="i18n.contentType.NEW">Sparade men ej inskickade ärenden</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_MULTISIGN">Väntar på flerpartssignering</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_PAYMENT">Väntar på betalning</xsl:variable>
	<xsl:variable name="i18n.contentType.SUBMITTED">Inskickade ärenden</xsl:variable>
	<xsl:variable name="i18n.contentType.IN_PROGRESS">Ärenden under behandling</xsl:variable>
	<xsl:variable name="i18n.contentType.WAITING_FOR_COMPLETION">Väntar på komplettering</xsl:variable>
	<xsl:variable name="i18n.contentType.ARCHIVED">Avslutade ärenden</xsl:variable>

	<xsl:variable name="i18n.contentType">Innehåll</xsl:variable>
	<xsl:variable name="i18n.permissions">Behörigheter</xsl:variable>
	
	<xsl:variable name="i18n.updateFlowIcon.link.title">Uppdatera ikon</xsl:variable>
	
	<xsl:variable name="i18n.UpdateFlowIcon.title">Uppdatera ikon för e-tjänsten:</xsl:variable>
	<xsl:variable name="i18n.currentIcon">Aktuell ikon</xsl:variable>
	<xsl:variable name="i18n.defaultIcon">(standardikon)</xsl:variable>
	<xsl:variable name="i18n.restoreDefaultIcon">Återställ standardikon</xsl:variable>
	<xsl:variable name="i18n.uploadNewIcon">Ladda upp ny ikon (png, jpg, gif eller bmp format)</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowIcon.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att tolka informationen från din webbläsare.</xsl:variable>
	<xsl:variable name="i18n.UnableToParseIcon">Den gick inte att tolka ikonen.</xsl:variable>
	<xsl:variable name="i18n.InvalidIconFileFormat">Felaktig filformat endast ikoner i png, jpg, gif eller bmp format är tillåtna.</xsl:variable>
	
	<xsl:variable name="i18n.addFlowForm.link.title">Lägg till blankett</xsl:variable>
	<xsl:variable name="i18n.updateFlowForm.link.title">Uppdatera blankett</xsl:variable>
	<xsl:variable name="i18n.deleteFlowForm.link.title">Ta bort blankett</xsl:variable>
	<xsl:variable name="i18n.deleteFlowForm.confirm">Ta bort blankett</xsl:variable>
	<xsl:variable name="i18n.AddFlowForm.title">Uppdatera blankett för e-tjänsten:</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.title">Uppdatera blankett för e-tjänsten:</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.description">Välj antigen en PDF fil att ladda upp eller länka till en extern PDF</xsl:variable>
	<xsl:variable name="i18n.uploadNewFlowForm">Ladda upp ny blankett</xsl:variable>
	<xsl:variable name="i18n.FlowForm.name">Namn (kan lämnas tomt)</xsl:variable>
	<xsl:variable name="i18n.FlowForm.externalURL">Länk till extern PDF</xsl:variable>
	<xsl:variable name="i18n.AddFlowForm.submit">Lägg till blankett</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowForm.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.UnableToStoreFile">Det gick inte att spara blanketten.</xsl:variable>
	<xsl:variable name="i18n.InvalidFlowFormFileFormat.part1">Felaktig filformat endast blanketter i</xsl:variable>
	<xsl:variable name="i18n.InvalidFlowFormFileFormat.part2">format är tillåtna.</xsl:variable>
	<xsl:variable name="i18n.NoAttachedFile">Du har inte bifogat någon fil.</xsl:variable>
	
	<xsl:variable name="i18n.defaultQueryState">Standardläge</xsl:variable>
	<xsl:variable name="i18n.defaultQueryState.title">Standardläge</xsl:variable>
	<xsl:variable name="i18n.defaultQueryState.description">Välj vilket standardläge som frågan skall ha.</xsl:variable>
	<xsl:variable name="i18n.queryState.VISIBLE">Valfri</xsl:variable>
	<xsl:variable name="i18n.queryState.VISIBLE_REQUIRED">Obligatorisk</xsl:variable>
	<xsl:variable name="i18n.queryState.HIDDEN">Dold</xsl:variable>

	<xsl:variable name="i18n.SortFlow.title">Sortera frågor och steg</xsl:variable>
	<xsl:variable name="i18n.SortFlow.description">Observera att en fråga som har regler inte kan placeras efter de frågor som reglerna påverkar.</xsl:variable>
	<xsl:variable name="i18n.SortFlow.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.MoveStep">Flytta steg</xsl:variable>
	<xsl:variable name="i18n.MoveQuery">Flytta fråga</xsl:variable>
	
	<xsl:variable name="i18n.NoStepSortindex">Det gick inte att hitta sorteringsindex för alla steg.</xsl:variable>
	<xsl:variable name="i18n.NoQueryDescriptorSortindex">Det gick inte att hitta sorteringsindex för alla frågor.</xsl:variable>
	<xsl:variable name="i18n.InvalidQuerySortIndex">En eller flera frågor har felaktigt sorteringsindex. Frågor med regler får inte ligga efter de frågor som de påverkar. De frågor som påverkas av regler får inte ligga före frågan med regeln.</xsl:variable>
	
	<xsl:variable name="i18n.UnableToFindStepsForAllQueries">Det gick inte att koppla alla frågor till steg.</xsl:variable>
	<xsl:variable name="i18n.updateEvaluator.title">Uppdatera regel</xsl:variable>
	<xsl:variable name="i18n.deleteEvaluator.confirm">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.deleteEvaluator.title">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.addEvaluator.title">Lägg till regel kopplad till frågan</xsl:variable>
	<xsl:variable name="i18n.AddEvaluatorDescriptor.title">Lägg till regel kopplad till frågan</xsl:variable>
	<xsl:variable name="i18n.evaluatorType">Regeltyp</xsl:variable>
	<xsl:variable name="i18n.AddEvaluatorDescriptor.submit">Lägg till regel</xsl:variable>
	
	<xsl:variable name="i18n.SelectedEvaluatorTypeNotFound">Den valda regeltypen hittades inte</xsl:variable>
	<xsl:variable name="i18n.evaluatorTypeID">Regeltyp</xsl:variable>
	<xsl:variable name="i18n.flowVersion">version</xsl:variable>
	<xsl:variable name="i18n.versions">Versioner</xsl:variable>
	<xsl:variable name="i18n.version.title">Version</xsl:variable>
	<xsl:variable name="i18n.flowHasNoOtherVersions">Det finns inga andra versioner av denna e-tjänst.</xsl:variable>
	
	<xsl:variable name="i18n.addNewVersion">Lägg till en ny version</xsl:variable>
	<xsl:variable name="i18n.createNewFlow">Skapa en ny e-tjänst</xsl:variable>
	
	<xsl:variable name="i18n.deleteFlowFamilyDisabledHasInstances">Det går inte att ta bort den här e-tjänsten för en eller flera av dess versioner har ärenden kopplade till sig.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamilyDisabledIsPublished">Det går inte att ta bort den här e-tjänsten för en eller flera av dess versioner är publicerade.</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamilyConfirm">Är du säker på att du vill ta bort samtliga versioner av e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.deleteFlowFamily.title">Ta bort samtliga versioner av e-tjänsten</xsl:variable>
	
	<xsl:variable name="i18n.versions.description">Tabellen nedan visar samtliga versioner av denna e-tjänst. Markera en e-tjänst i listan för att skapa en ny version eller en helt ny e-tjänst baserat på den valda versionen.</xsl:variable>
	<xsl:variable name="i18n.FlowNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowFormNotFound">Den begärda blanketten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.unknownQueryType">Okänd frågetyp</xsl:variable>
	<xsl:variable name="i18n.unknownEvaluatorType">Okänd regeltyp</xsl:variable>
	<xsl:variable name="i18n.administrateStandardStatuses">Adm. standardstatusar</xsl:variable>
	<xsl:variable name="i18n.ListStandardStatuses.title">Standardstatusar</xsl:variable>
	<xsl:variable name="i18n.ListStandardStatuses.description">Nedan visas samtliga standardstatusar i systemet.</xsl:variable>
	<xsl:variable name="i18n.noStandardStatusesFound">Inga standardstatusar hittades.</xsl:variable>
	<xsl:variable name="i18n.addStandardStatus">Lägg till standardstatus</xsl:variable>
	<xsl:variable name="i18n.AddStandardStatus.title">Lägg till standardstatus</xsl:variable>
	<xsl:variable name="i18n.UpdateStandardStatus.title">Uppdatera standardstatus</xsl:variable>
	<xsl:variable name="i18n.updateStandardStatus.link.title">Uppdatera standardstatus</xsl:variable>
	<xsl:variable name="i18n.deleteStandardStatus.confirm">Ta bort standardstatus</xsl:variable>
	<xsl:variable name="i18n.deleteStandardStatus.link.title">Ta bort standardstatus</xsl:variable>
	<xsl:variable name="i18n.addStandardStatuses">Lägg till standardstatusar</xsl:variable>
	<xsl:variable name="i18n.RequestedFlowFamilyNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyCannotBeDeleted">Den begärda e-tjänsten kan inte tas bort då en eller flera versioner av den är publicerade eller har ärenden knuta till sig.</xsl:variable>
	<xsl:variable name="i18n.testFlow">Testa e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.downloadxsd.title">Ladda ner XSD schema</xsl:variable>
	
	<xsl:variable name="i18n.tags">Extra sökord (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.checks.title">Krav för e-tjänsten (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.checks">Krav för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.administrateFlowTypes">Adm. kategorier</xsl:variable>
	<xsl:variable name="i18n.ListFlowTypes.title">Kategorier</xsl:variable>
	<xsl:variable name="i18n.ListFlowTypes.description">Nedan visas en lista på de kategorier som du har behörighet att komma åt.</xsl:variable>
	<xsl:variable name="i18n.categories">Underkategorier</xsl:variable>
	<xsl:variable name="i18n.noFlowTypesFound">Inga kategorier hittades</xsl:variable>
	<xsl:variable name="i18n.addFlowType">Lägg till kategori</xsl:variable>
	<xsl:variable name="i18n.flowFamilies">E-tjänster</xsl:variable>
	<xsl:variable name="i18n.deleteFlowTypeDisabledHasFlows">Den här kategorin går inte att ta bort för den har en eller flera e-tjänster kopplade till sig!</xsl:variable>
	<xsl:variable name="i18n.deleteFlowType">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.showFlowType">Visa kategori</xsl:variable>
	<xsl:variable name="i18n.updateFlowType">Uppdatera kategori</xsl:variable>
	
	<xsl:variable name="i18n.allowedGroups">Grupper:</xsl:variable>
	<xsl:variable name="i18n.allowedUsers">Användare:</xsl:variable>

	<xsl:variable name="i18n.allowedQueryTypes">Frågetyper</xsl:variable>
	<xsl:variable name="i18n.noCategory">Ingen underkategori</xsl:variable>
	<xsl:variable name="i18n.noCategories">Det finns inga underkategorier för den här kategorin</xsl:variable>
	<xsl:variable name="i18n.updateCategory">Uppdatera underkategorin</xsl:variable>
	<xsl:variable name="i18n.deleteCategory">Ta bort underkategorin</xsl:variable>
	<xsl:variable name="i18n.addCategory">Lägg till underkategori</xsl:variable>
	<xsl:variable name="i18n.noAllowedQueryTypes">Inga frågetyper tillåts för denna kategori.</xsl:variable>
	<xsl:variable name="i18n.AddFlowType.title">Lägg till kategori</xsl:variable>
	<xsl:variable name="i18n.AddFlowType.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowType.title">Uppdatera kategori</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowType.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AddCategory.title">Lägg till underkategori</xsl:variable>
	<xsl:variable name="i18n.AddCategory.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateCategory.title">Uppdatera underkategorin</xsl:variable>
	<xsl:variable name="i18n.UpdateCategory.submit">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.Managers">Handläggare</xsl:variable>
	<xsl:variable name="i18n.ManagersDescription">Följande grupper och användare får handlägga ärenden för denna e-tjänst.</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowFamilyManagers">Välj handläggare</xsl:variable>
	<xsl:variable name="i18n.UpdateAutoManagerAssignment">Automatisk tilldelning av handläggare</xsl:variable>
	<xsl:variable name="i18n.NoManagers">Inga handläggare har åtkomst till ärenden för den här e-tjänsten.</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.title">Uppdatera handläggare för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.openModal">Avancerade inställningar</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.validFromDate">Giltig från och med</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.validToDate">Giltig till och med</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.restrictedUser">Begränsad (Kan endast se ärenden hen blivit tilldelade)</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.restrictedGroup">Begränsad (Kan endast se ärenden gruppen blivit tilldelad)</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.Title">Inställningar för</xsl:variable>
	<xsl:variable name="i18n.UpdateManagers.Modal.Close">Stäng</xsl:variable>
	<xsl:variable name="i18n.Manager.validFromDate">Giltig från och med</xsl:variable>
	<xsl:variable name="i18n.Manager.validToDate">Giltig till och med</xsl:variable>
	<xsl:variable name="i18n.Manager.validFromToDate">till och med</xsl:variable>
	<xsl:variable name="i18n.Manager.restricted">Begränsad</xsl:variable>
	
	<xsl:variable name="i18n.AddFlowCategoryNotFound">Den begärda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.AddCategoryFailedFlowTypeNotFound">Den begärda kategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedCategoryNotFound">Den begärda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedCategoryNotFound">Den begärda underkategorin hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedFlowTypeNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedFlowTypeNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.ShowFailedFlowTypeNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStepNotFound">Det begärda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStepNotFound">Det begärda steget hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedQueryDescriptorNotFound">Den begärda frågan hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedQueryDescriptorNotFound">Den begärda frågan hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedEvaluatorDescriptorNotFound">Den begärda regeln hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedEvaluatorDescriptorNotFound">Den begärda regeln hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStatusNotFound">Den begärda statusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStatusNotFound">Den begärda statusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.UpdateFailedStandardStatusNotFound">Den begärda standardstatusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.DeleteFailedStandardStatusNotFound">Den begärda standardstatusen hittades inte.</xsl:variable>
	<xsl:variable name="i18n.FlowTypeQueryTypeAccessDenied">Den här typen av e-tjänster har inte behörighet att använda den valda frågetypen.</xsl:variable>
	<xsl:variable name="i18n.requireAuthentication">Kräv inloggning</xsl:variable>
	<xsl:variable name="i18n.requirersAuthentication">Kräver inloggning</xsl:variable>
	<xsl:variable name="i18n.Flow.showLoginQuestion">Fråga om inloggning</xsl:variable>
	<xsl:variable name="i18n.requireSigning">Kräv signering</xsl:variable>
	<xsl:variable name="i18n.Flow.allowForeignIDs">Tillåt användare inloggade med eIDAS</xsl:variable>
	<xsl:variable name="i18n.Flow.useSequentialSigning">Sekventiell signering</xsl:variable>
	<xsl:variable name="i18n.Flow.skipPosterSigning">Kräv inte signering av första parten</xsl:variable>
	<xsl:variable name="i18n.Flow.skipPosterSigning.description">(bör endast användas vid flerpartsignering)</xsl:variable>
	<xsl:variable name="i18n.requiresSigning">Kräver signering</xsl:variable>
	<xsl:variable name="i18n.MissingDefaultStatusMapping">E-tjänsten går inte att publicera då dess statusar inte innehåller samtliga obligatoriska statusmappningar. Klicka ur "Aktivera" e-tjänsten" och spara gå sedan tillbaka till e-tjänstöversikten för att kontrollera statusarna.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.Part1">Användaren</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.Part2">handlägger aktiva ärenden för den här e-tjänsten och får därför inte plockas bort</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerUserError.MemberOfGroups">medlem i</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerGroupError.Part1">Gruppen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InUseManagerGroupError.Part2">handlägger aktiva ärenden för den här e-tjänsten och får därför inte plockas bort</xsl:variable>
	
	<xsl:variable name="i18n.exportFlow.title">Exportera e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.EvaluatorExportException.Part1">Ett fel uppstod när regelen</xsl:variable>
	<xsl:variable name="i18n.EvaluatorExportException.Part2">skulle exporteras, kontakta administratören för mer information.</xsl:variable>
	<xsl:variable name="i18n.QueryExportException.Part1">Ett fel uppstod när frågan</xsl:variable>
	<xsl:variable name="i18n.QueryExportException.Part2">skulle exporteras, kontakta administratören för mer information.</xsl:variable>
	<xsl:variable name="i18n.FlowImportFlowFamlilyNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.SelectImportTargetType.title">Välj kategori</xsl:variable>
	<xsl:variable name="i18n.SelectImportTargetType.description">Välj vilken kategori av e-tjänst du vill importera.</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewVersion.title">Importera ny version av e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewVersion.description">Använd formuläret nedan för att importera en ny version. Filen du väljer behöver vara av typen oeflow.</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewFamily.title">Importera ny e-tjänst i kategorin</xsl:variable>
	<xsl:variable name="i18n.ImportFlow.NewFamily.description">Använd formuläret nedan för att importera en ny e-tjänst. Filen du väljer behöver vara av typen oeflow.</xsl:variable>
	<xsl:variable name="i18n.selectFlowFile">Välj fil</xsl:variable>	
	
	<xsl:variable name="i18n.ImportFlow.submit">Importera</xsl:variable>

	<xsl:variable name="i18n.importFlow">Importera e-tjänst</xsl:variable>
	<xsl:variable name="i18n.importNewFlowVersion">Importera en ny version</xsl:variable>
	
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part1">Frågan</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part2">är av en typ som inte tillåts i e-tjänster av kategori</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotAllowedInFlowTypeValidationError.Part3">.</xsl:variable>
	
	<xsl:variable name="i18n.EvaluatorTypeNotFound.Part1">Regeltypen för regeln</xsl:variable>
	<xsl:variable name="i18n.EvaluatorTypeNotFound.Part2">hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.QueryTypeNotFound.Part1">Frågetypen för frågan</xsl:variable>
	<xsl:variable name="i18n.QueryTypeNotFound.Part2">hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.EvaluatorImportException.Part1">Ett fel uppstod när regeln</xsl:variable>
	<xsl:variable name="i18n.EvaluatorImportException.Part2">skulle importeras, kontakta administratören för mer information.</xsl:variable>
	<xsl:variable name="i18n.QueryImportException.Part1">Ett fel uppstod när frågan</xsl:variable>
	<xsl:variable name="i18n.QueryImportException.Part2">skulle importeras, kontakta administratören för mer information.</xsl:variable>
	
	<xsl:variable name="i18n.InvalidFileExtension.Part1">Filen</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.Part2">är av en felaktig filtyp.</xsl:variable>
	<xsl:variable name="i18n.InvalidFileExtension.Part3">Följande filtyper är tillåtna:</xsl:variable>
	
	<xsl:variable name="i18n.UnableToParseFile.part1">Den gick inte att tolka innehållet i filen </xsl:variable>
	<xsl:variable name="i18n.UnableToParseFile.part2">.</xsl:variable>
	
	<xsl:variable name="i18n.showSubmitSurvey">Visa användarundersökning</xsl:variable>
	<xsl:variable name="i18n.hideSubmitStepText">Dölj steget "Skicka in" i stegöverblicken</xsl:variable>
	
	<xsl:variable name="i18n.FlowSurveysTitle">Användarundersökningar</xsl:variable>
	
	<xsl:variable name="i18n.UpdateNotificationSettings">Ändra inställningar</xsl:variable>
	<xsl:variable name="i18n.Notifications">Notifieringar</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.title">Notifieringsinställningar för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.reset.confirm">Är du helt säker på att du vill återställa standardvärden för notifieringar för denna e-tjänst?</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.reset">Återställ standardvärden</xsl:variable>
	<xsl:variable name="i18n.UpdateNotifications.submit">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.StatisticsSettings">Statistik inställningar</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.None">Generera ingen statistik</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.Internal">Generera statistik men visa den endast för interna användare</xsl:variable>
	<xsl:variable name="i18n.StatisticsMode.Public">Generera statistik och visa den publikt</xsl:variable>

	<xsl:variable name="i18n.skipOverview">Hoppa över översiktssidan</xsl:variable>
	
	<xsl:variable name="i18n.MissingDefaultStatusMappingForMultiSigning">E-tjänsten går inte att publicera då den saknar statusmappning för flerpartssignering. Klicka ur "Aktivera" e-tjänsten" och spara gå sedan tillbaka till e-tjänstöversikten för att kontrollera statusarna.</xsl:variable>
	<xsl:variable name="i18n.MissingDefaultStatusMappingForPayment">E-tjänsten går inte att publicera då den saknar statusmappning för betalning. Klicka ur "Aktivera" e-tjänsten" och spara gå sedan tillbaka till e-tjänstöversikten för att kontrollera statusarna.</xsl:variable>
	
	<xsl:variable name="i18n.hasNoFlowForm">Den här e-tjänsten har ingen PDF blankett kopplad till sig.</xsl:variable>
	<xsl:variable name="i18n.MayNotRemoveFlowFormIfNoSteps">Du får inte ta bort blanketten om e-tjänsten är aktiverad och saknar steg</xsl:variable>
	<xsl:variable name="i18n.MayNotAddFlowFormIfOverviewSkipIsSet">Du kan inte lägga till en blankett om e-tjänsten är inställd på att hoppa över översiktssidan</xsl:variable>
	<xsl:variable name="i18n.MayNotSetOverviewIfFlowFormIsSet">Du kan inte hoppa över översiktssidan om det finns en blankett kopplad till e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.MayNotSetOverviewIfFlowFormIsSet.description">ej möjligt om det finns en blankett kopplad till e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.FlowHasNoContent">Du kan inte aktivera e-tjänsten då den inte har några steg eller har någon blankett</xsl:variable>
	<xsl:variable name="i18n.FlowHasNoStepsAndOverviewSkipIsSet">Du kan inte aktivera e-tjänsten med alterntivet 'hoppa över översiktssidan' satt då e-tjänsten inte innehåller några steg.</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUse">Kortnamnet</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUse2">används redan av e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.FlowFamilyAliasAlreadyInUseBySystem">används redan av en annan del av systemet.</xsl:variable>
	<xsl:variable name="i18n.aliases.title">Kortnamn för e-tjänsten (ett per rad ex. bygga eller skola, giltiga tecken är A-Z a-z 0-9 _- )</xsl:variable>
	<xsl:variable name="i18n.aliases">Kortnamn för e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.LacksPublishAccess">Du saknar publiceringsrättigheter och kan därför inte ändra publiceringsdatum eller aktivera e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.Filter">Sök</xsl:variable>
	<xsl:variable name="i18n.link">länk</xsl:variable>

	<xsl:variable name="i18n.ExportQuery.title">Exportera frågan</xsl:variable>
	<xsl:variable name="i18n.ExportFailedQueryDescriptorNotFound">Den begärda frågan hittades inte.</xsl:variable>
	
	<xsl:variable name="i18n.ImportQueries.title">Importera frågor i e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.ImportQueries.description">Använd formuläret nedan för att importera en eller flera frågor i det valda steget. Filerna du väljer måsta vara av typen oequery.</xsl:variable>
	<xsl:variable name="i18n.SelectQueryFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.ImportQueries.submit">Importera</xsl:variable>
	<xsl:variable name="i18n.ImportQueries">Importera frågor</xsl:variable>
	
	<xsl:variable name="i18n.Events.Title">Ändringslogg</xsl:variable>
	<xsl:variable name="i18n.Events.Full.Title">Fullständig ändringslogg för</xsl:variable>
	<xsl:variable name="i18n.Events.Description">Tabellen nedan visar de senaste ändringarna för samtliga versioner av denna e-tjänst.</xsl:variable>
	<xsl:variable name="i18n.Events.Full.Description">Tabellen nedan visar alla ändringarna för samtliga versioner av denna e-tjänst.</xsl:variable>
	<xsl:variable name="i18n.Events.message">Händelse</xsl:variable>
	<xsl:variable name="i18n.Event.message">Notering</xsl:variable>
	<xsl:variable name="i18n.Events.poster">Användare</xsl:variable>
	<xsl:variable name="i18n.Events.added">Tidpunkt</xsl:variable>
	<xsl:variable name="i18n.Events.FlowFamilyHasNoEvents">Inga ändringar hittades.</xsl:variable>
	<xsl:variable name="i18n.Events.ShowAll">Visa alla ändringar</xsl:variable>
	<xsl:variable name="i18n.Events.Add">Lägg till notering</xsl:variable>
	<xsl:variable name="i18n.Events.Add.title">Lägg till notering i ändringslogg för</xsl:variable>
	<xsl:variable name="i18n.Events.Add.submit">Lägg till</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part1">Du bifogade totalt </xsl:variable>
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part2"> filer och den max tillåtna storleken är </xsl:variable>
	<xsl:variable name="i18n.RequestSizeLimitExceeded.part3">!</xsl:variable>

	<xsl:variable name="i18n.FlowType.AdminAccess.Title">Administration</xsl:variable>
	<xsl:variable name="i18n.FlowType.AdminAccess.Description">Ange vilka användare och grupper som ska ha behörighet att bygga och administrera e-tjänster i den här kategorin.</xsl:variable>
	
	<xsl:variable name="i18n.onlyModuleAdminAccess">Endast globala administatörer har behörighet att bygga och administrera e-tjänster i den här kategorin.</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.UserAccess.Title">Åtkomst till e-tjänster</xsl:variable>
	<xsl:variable name="i18n.FlowType.UserAccess.Description">Om du vill begränsa åtkomsten till e-tjänsterna inom denna kategori så kan du kryssa i rutan och sedan ange vilka användare och grupper som ska få komma åt e-tjänsterna.</xsl:variable>
	<xsl:variable name="i18n.enableAccessFiltering">Aktivera begränsad åtkomst</xsl:variable>
	
	<xsl:variable name="i18n.noUserAccess">Inga användare eller grupper har åtkomst till e-tjänster i denna kategori.</xsl:variable>
	
	<xsl:variable name="i18n.noAccessFilter">Samtliga användare har åtkomst till e-tjänsterna i denna kategori.</xsl:variable>

	<xsl:variable name="i18n.hideManagerDetails">Dölj uppgifter om handläggare</xsl:variable>
	
	<xsl:variable name="i18n.popularity.boost">Extra antal ärenden vid beräkning av popularitet</xsl:variable>
	
	<xsl:variable name="i18n.startButtonText">Anpassad text på startknapp (max 24 tecken)</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.Icon">Ikon</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.UploadIcon">Tillåtna filtyper: jpg, png, gif, bmp</xsl:variable>
	<xsl:variable name="i18n.FlowType.DeleteIcon">Ta bort</xsl:variable>
	<xsl:variable name="i18n.FlowType.IconColor">Bakgrundsfärg</xsl:variable>
	<xsl:variable name="i18n.FlowType.iconColor">bakgrundsfärg</xsl:variable>
	<xsl:variable name="i18n.FlowType.UseIconOnAllFlows">Använd kategorins ikon på samtliga e-tjänster</xsl:variable>
	
	<xsl:variable name="i18n.All">Alla</xsl:variable>
	<xsl:variable name="i18n.Published">Publicerade</xsl:variable>
	<xsl:variable name="i18n.Unpublished">Ej publicerade</xsl:variable>
	<xsl:variable name="i18n.statusFilter">Visa</xsl:variable>
	
	<xsl:variable name="i18n.hideFromOverview">Dölj e-tjänsten på e-tjänsteportalens förstasida</xsl:variable>
	<xsl:variable name="i18n.hideInternalMessages">Stäng av interna noteringar</xsl:variable>
	<xsl:variable name="i18n.hideExternalMessages">Stäng av meddelandefunktionen</xsl:variable>
	<xsl:variable name="i18n.hideExternalMessageAttachments">Stäng av filuppladdningen i meddelandefunktionen</xsl:variable>
	<xsl:variable name="i18n.Flow.hideFromUser">Dölj inskickade ärenden från mina sidor</xsl:variable>
	
	<xsl:variable name="i18n.ChangeFlowType.linkTitle">Ändra kategori på e-tjänsten</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.title">Ändra kategori på e-tjänst</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.description">Här kan du ändra kategori på e-tjänsten. Tänk på att alla versioner påverkas.</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.ChooseNewFlowType">Välj ny kategori</xsl:variable>
	<xsl:variable name="i18n.ChangeFlowType.submit">Ändra kategori</xsl:variable>
	
	<xsl:variable name="i18n.sortStatuses">Sortera statusar</xsl:variable>
	<xsl:variable name="i18n.SortFlowStatuses.title">Sortera statusar</xsl:variable>
	<xsl:variable name="i18n.MoveStatus">Flytta status</xsl:variable>
	<xsl:variable name="i18n.PaymentSupportEnabled">Aktivera betalstöd</xsl:variable>
	
	<xsl:variable name="i18n.PreviewEnabled">Förhandsgranskning</xsl:variable>
	<xsl:variable name="i18n.PaymentEnabled">Betalstöd</xsl:variable>
	
	<xsl:variable name="i18n.FlowType.allowAnonymousAccess">Visa för ej inloggade användare</xsl:variable>
	
	<xsl:variable name="i18n.NoManagersSet">E-tjänsten går inte att publicera då den saknar handläggare.</xsl:variable>
	<xsl:variable name="i18n.UnpublishFlowFamily">Avpublicera alla versioner</xsl:variable>
	<xsl:variable name="i18n.UnpublishFlowFamilyConfirm">Är du säker på att du vill avpublicera alla versioner av e-tjänsten?</xsl:variable>
	
	<xsl:variable name="i18n.ReCacheFlow">Cacha om e-tjänst</xsl:variable>
	<xsl:variable name="i18n.FlowForm.externalIcon">Visa ikon för extern länk</xsl:variable>
	
	<xsl:variable name="i18n.FlowFamilyID">E-tjänstens familje ID</xsl:variable>
	<xsl:variable name="i18n.FlowID">Versionens ID</xsl:variable>
	<xsl:variable name="i18n.FlowFamily.UsesAutoManagerAssignment">E-tjänsten använder automatisk tilldelning av handläggare.</xsl:variable>
	
	<xsl:variable name="i18n.AutoManagerAssignment.title">Automatisk tilldelning av handläggare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.description">Här väljer du vilka handläggare som ska tilldelas ärenden automatiskt och när det ska ske.</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.submit">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules">Tilldelade handläggare om attribut har specificerat värde</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Add">Lägg till regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.AttributeName">Attribut</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.AttributeValues">Värden</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Inverted">Inverterad</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Users">Användare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Groups">Grupper</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rules.Row">Rad</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Update">Ändra regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Save">Spara</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Delete">Ta bort regel</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.DeleteConfirm">Är du säker på att du vill ta bort regeln</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.AttributeName">Attributnamn</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.AttributeValues">Värden (ett per rad)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Rule.Invert">Invertera (Om inget av värderna på regeln matchar attributets värde så aktiveras regeln)</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Users">Användare</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Groups">Grupper med begränsad behörighet</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.Always">Handläggare som alltid tilldelas</xsl:variable>
	<xsl:variable name="i18n.AutoManagerAssignment.NoMatch">Tilldelade handläggare om ingen regel matchar</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.FullManagerOrFallbackManagerRequired">För att e-tjänsten ska få använda automatiskt tilldelning av handläggare så måste den antingen ha minst 1 fullständig handläggare, alltid tilldela en handläggare eller tilldela handläggare när ingen regel matchar.</xsl:variable>
	
	<xsl:variable name="i18n.DescriptionColumnSettings.Title">Inställningar för ärendebeskrivningar</xsl:variable>
	<xsl:variable name="i18n.DescriptionColumnSettings.Description">För att göra det lättare att skilja på ärenden så finns möjligheten att ge dem en beskrivning under mina sidor och i handläggargränssnittet. Som standard hämtas beskrivningen från description attributet om inget annat anges. Via fälten nedan kan en valfri sträng anges med både fast text och $attribute{} taggar för beskrivningen i de respektive gränssnitten.</xsl:variable>
	<xsl:variable name="i18n.userDescriptionTemplate">Beskrivning på mina sidor</xsl:variable>
	<xsl:variable name="i18n.managerDescriptionTemplate">Beskrivning i handläggargränssnittet</xsl:variable>
	
	<xsl:variable name="i18n.ExtensionErrors">Ett validerings fel har uppstått i ett tillägg, kolla längre ner efter vad som är fel.</xsl:variable>
	
</xsl:stylesheet>
