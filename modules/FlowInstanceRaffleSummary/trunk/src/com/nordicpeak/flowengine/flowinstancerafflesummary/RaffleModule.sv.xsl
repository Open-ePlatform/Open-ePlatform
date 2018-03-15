<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="classpath://com/nordicpeak/flowengine/flowinstancesummary/FlowInstanceSummaryModule.sv.xsl"/>
	
	<xsl:include href="RaffleModuleTemplates.xsl"/>
	
	<xsl:variable name="java.cellValueYes">Ja</xsl:variable>
	<xsl:variable name="java.cellValueNo">Nej</xsl:variable>
	<xsl:variable name="java.numberAssignedMessage">Lottning utf�rd. Du fick nummer $nummer</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du m�ste fylla i f�ltet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.ValidationError.TableColumnValidationError">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RequestedRoundNotFound">Hittar ej den angivna omg�ngen</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RoundDoesNotSupportAdd">Omg�ngen �r inte inst�lld f�r att kunna l�gga till nya �renden</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceAlreadyRaffled.1">Du kan inte utf�ra lottning p� omg�ngen d� �rende</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceAlreadyRaffled.2">redan har lottats.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.FlowInstanceStatusExcluded">Du f�r inte g�ra saker med �renden vars status �r exkluderade</xsl:variable>
	
	<xsl:variable name="i18n.RoundsTitle">Omg�ngar</xsl:variable>
	<xsl:variable name="i18n.RoundsDescription">Nedan visas samtliga omg�ngar som du har beh�righet att hantera. Klicka p� en omg�ng f�r att g� vidare.</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstanceCount">�renden</xsl:variable>
	<xsl:variable name="i18n.NoRounds">Finns inget att visa</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.PeriodStart">Period start</xsl:variable>
	<xsl:variable name="i18n.PeriodEnd">Period slut</xsl:variable>
	<xsl:variable name="i18n.ShowRound">�ppna omg�ng</xsl:variable>
	
	<xsl:variable name="i18n.RoundDescription">Nedan visas de �renden som inkommit kopplat till denna omg�ng av fonden. Klicka p� �rendenumret f�r att visa �rendet.</xsl:variable>
	
	<xsl:variable name="i18n.Button.AddInstance">L�gg till �rende</xsl:variable>
	<xsl:variable name="i18n.Button.AddPayment">L�gg till betalning</xsl:variable>
	<xsl:variable name="i18n.Button.Raffle">Utf�r lottning</xsl:variable>
	
	<xsl:variable name="i18n.AddInstance.RoundNotConfiguredForAdd">Omg�ng ej konfigurerad f�r att l�gga till �renden.</xsl:variable>
	<xsl:variable name="i18n.AddInstance.FlowInstanceModuleMissing">Modul f�r att l�gga till �renden saknas.</xsl:variable>
	
	<xsl:variable name="i18n.AddRaffleRound">L�gg till omg�ng</xsl:variable>
	<xsl:variable name="i18n.UpdateRaffleRound">Uppdatera omg�ng</xsl:variable>
	<xsl:variable name="i18n.AddRound.title">L�gg till omg�ng</xsl:variable>
	<xsl:variable name="i18n.UpdateRound.title">Uppdatera omg�ng</xsl:variable>
	
	<xsl:variable name="i18n.startDate">Start datum</xsl:variable>
	<xsl:variable name="i18n.endDate">Slut datum</xsl:variable>
	<xsl:variable name="i18n.money">Budget i kronor (l�mna tom f�r ingen gr�ns)</xsl:variable>
	<xsl:variable name="i18n.RaffleFlows">E-tj�nst kopplingar</xsl:variable>
	<xsl:variable name="i18n.AddRound.submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateRound.submit">Spara</xsl:variable>
	<xsl:variable name="i18n.RaffleFlows.SearchPlaceholder">L�gg till e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RaffleFlowNotFound">E-tj�nst koppling med ID</xsl:variable>
	<xsl:variable name="i18n.ValidationError.RaffleFlowNotFound2">hittades ej</xsl:variable>
	<xsl:variable name="i18n.raffledStatus">Status efter lottning</xsl:variable>
	<xsl:variable name="i18n.excludedStatuses">Exkluderade statusar</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleRound.Confirm">�r du s�ker p� att du vill ta bort omg�ng</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleRound">Ta bort omg�ng</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleFlow.Confirm">�r du s�ker p� att du vill ta bort e-tj�nst kopplingen till</xsl:variable>
	<xsl:variable name="i18n.DeleteRaffleFlow">Ta bort e-tj�nst koppling</xsl:variable>

	<xsl:variable name="i18n.UseForAddFlow">Anv�nd f�r manuell inl�ggning av �renden</xsl:variable>
	
	<xsl:variable name="i18n.UserTagsTable.description">F�ljande taggar kan anv�ndas i notifikationer g�llande lottning</xsl:variable>
	<xsl:variable name="i18n.Tag">Tagg</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.FlowNameTag">E-tj�nsten namn</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceIDTag">�rende nummer</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceURLTag">Adress till �rendet</xsl:variable>
	<xsl:variable name="i18n.StatusTag">�rende status</xsl:variable>
	<xsl:variable name="i18n.PosterFirstnameTag">Kontaktpersonens f�rnamn</xsl:variable>
	<xsl:variable name="i18n.PosterLastnameTag">Kontaktpersonens efternamn</xsl:variable>
	<xsl:variable name="i18n.NumberTag">Nummer</xsl:variable>

	<xsl:variable name="i18n.OverideStatusChangedNotificationOnDecision">Anv�nd annan text i notifikationer vid lottning (annars anv�nds standardtexter f�r byte av status)</xsl:variable>
	<xsl:variable name="i18n.DecisionEmailMessage">Inneh�ll i e-postmeddelande vid lottning</xsl:variable>
	<xsl:variable name="i18n.DecisionSMSMessage">Inneh�ll i SMS vid lottning</xsl:variable>
	
	<xsl:variable name="i18n.Button.Back">Tillbaka till omg�ngar</xsl:variable>
</xsl:stylesheet>
