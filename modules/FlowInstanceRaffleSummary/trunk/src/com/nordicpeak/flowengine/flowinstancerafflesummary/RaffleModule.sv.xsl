<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/flowinstancesummary/FlowInstanceSummaryModule.sv.xsl"/>
	
	<xsl:include href="RaffleModuleTemplates.xsl"/>
	
	<xsl:variable name="java.cellValueYes">Ja</xsl:variable>
	<xsl:variable name="java.cellValueNo">Nej</xsl:variable>
	<xsl:variable name="java.numberAssignedMessage">Lottning utförd. Du fick nummer $nummer</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TableColumnValidationError">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RequestedRoundNotFound">Hittar ej den angivna omgången</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RoundDoesNotSupportAdd">Omgången är inte inställd för att kunna lägga till nya ärenden</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceAlreadyRaffled.1">Du kan inte utföra lottning på omgången då ärende</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceAlreadyRaffled.2">redan har lottats.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceStatusExcluded">Du får inte göra saker med ärenden vars status är exkluderade</xsl:variable>
	
	<xsl:variable name="i18n.RoundsTitle">Omgångar</xsl:variable>
	<xsl:variable name="i18n.RoundsDescription">Nedan visas samtliga omgångar som du har behörighet att hantera. Klicka på en omgång för att gå vidare.</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceCount">Ärenden</xsl:variable>
	<xsl:variable name="i18n.NoRounds">Finns inget att visa</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.PeriodStart">Period start</xsl:variable>
	<xsl:variable name="i18n.PeriodEnd">Period slut</xsl:variable>
	<xsl:variable name="i18n.ShowRound">Öppna omgång</xsl:variable>
	
	<xsl:variable name="i18n.RoundDescription">Nedan visas de ärenden som inkommit kopplat till denna omgång av fonden. Klicka på ärendenumret för att visa ärendet.</xsl:variable>
	
	<xsl:variable name="i18n.Button.AddInstance">Lägg till ärende</xsl:variable>
	<xsl:variable name="i18n.Button.AddPayment">Lägg till betalning</xsl:variable>
	<xsl:variable name="i18n.Button.Raffle">Utför lottning</xsl:variable>
	
	<xsl:variable name="i18n.AddInstance.RoundNotConfiguredForAdd">Omgång ej konfigurerad för att lägga till ärenden.</xsl:variable>
	<xsl:variable name="i18n.AddInstance.FlowInstanceModuleMissing">Modul för att lägga till ärenden saknas.</xsl:variable>
	
	<xsl:variable name="i18n.AddRaffleRound">Lägg till omgång</xsl:variable>
	<xsl:variable name="i18n.UpdateRaffleRound">Uppdatera omgång</xsl:variable>
	<xsl:variable name="i18n.AddRound.title">Lägg till omgång</xsl:variable>
	<xsl:variable name="i18n.UpdateRound.title">Uppdatera omgång</xsl:variable>
	
	<xsl:variable name="i18n.startDate">Start datum</xsl:variable>
	<xsl:variable name="i18n.endDate">Slut datum</xsl:variable>
	<xsl:variable name="i18n.money">Budget i kronor (lämna tom för ingen gräns)</xsl:variable>
	<xsl:variable name="i18n.RaffleFlows">E-tjänst kopplingar</xsl:variable>
	<xsl:variable name="i18n.AddRound.submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateRound.submit">Spara</xsl:variable>
	<xsl:variable name="i18n.RaffleFlows.SearchPlaceholder">Lägg till e-tjänst</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RaffleFlowNotFound">E-tjänst koppling med ID</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RaffleFlowNotFound2">hittades ej</xsl:variable>
	<xsl:variable name="i18n.raffledStatus">Status efter lottning</xsl:variable>
	<xsl:variable name="i18n.excludedStatuses">Exkluderade statusar</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleRound.Confirm">Är du säker på att du vill ta bort omgång</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleRound">Ta bort omgång</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleFlow.Confirm">Är du säker på att du vill ta bort e-tjänst kopplingen till</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleFlow">Ta bort e-tjänst koppling</xsl:variable>

	<xsl:variable name="i18n.UseForAddFlow">Använd för manuell inläggning av ärenden</xsl:variable>
	
	<xsl:variable name="i18n.UserTagsTable.description">Följande taggar kan användas i notifikationer gällande lottning</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.FlowNameTag">E-tjänsten namn</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceIDTag">Ärende nummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceURLTag">Adress till ärendet</xsl:variable>
	<xsl:variable name="i18n.StatusTag">Ärende status</xsl:variable>
	<xsl:variable name="i18n.PosterFirstnameTag">Kontaktpersonens förnamn</xsl:variable>
	<xsl:variable name="i18n.PosterLastnameTag">Kontaktpersonens efternamn</xsl:variable>
	<xsl:variable name="i18n.NumberTag">Nummer</xsl:variable>

	<xsl:variable name="i18n.OverideStatusChangedNotificationOnDecision">Använd annan text i notifikationer vid lottning (annars används standardtexter för byte av status)</xsl:variable>
	<xsl:variable name="i18n.DecisionEmailMessage">Innehåll i e-postmeddelande vid lottning</xsl:variable>
	<xsl:variable name="i18n.DecisionSMSMessage">Innehåll i SMS vid lottning</xsl:variable>
	
	<xsl:variable name="i18n.Button.Back">Tillbaka till omgångar</xsl:variable>
</xsl:stylesheet>
