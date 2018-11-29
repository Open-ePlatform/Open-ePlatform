<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>

	<xsl:include href="OperatingMessageModuleTemplates.xsl"/>
	
	<xsl:variable name="java.newExternalOperatingMessageEmailSubject">Nytt externt driftmeddelande publicerat f�r $name</xsl:variable>
	<xsl:variable name="java.newExternalOperatingMessageEmailMessage">
		&lt;p&gt;Ett nytt externt driftmeddelande publicerat f�r $name har tagits emot med f�ljande meddelande:&lt;/p&gt;
		&lt;p&gt;$message&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="java.removedExternalOperatingMessageEmailSubject">Externt driftmeddelande f�r $name avpublicerat</xsl:variable>
	<xsl:variable name="java.removedExternalOperatingMessageEmailMessage">
		&lt;p&gt;Ett externt driftmeddelande f�r $name har avpublicerat bort med f�ljande meddelande:&lt;/p&gt;
		&lt;p&gt;$message&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="i18n.AddOperatingMessage">L�gg till driftmeddelande</xsl:variable>
	<xsl:variable name="i18n.UpdateOperatingMessage">�ndra driftmeddelande</xsl:variable>
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.Publish">Publiceras</xsl:variable>
	<xsl:variable name="i18n.UnPublish">Avpubliceras</xsl:variable>
	<xsl:variable name="i18n.StartDate">Datum fr�n</xsl:variable>
	<xsl:variable name="i18n.EndDate">Datum till</xsl:variable>
	<xsl:variable name="i18n.StartTime">Tid fr�n</xsl:variable>
	<xsl:variable name="i18n.EndTime">Tid till</xsl:variable>
	<xsl:variable name="i18n.MessageType">Meddelandetyp</xsl:variable>
	<xsl:variable name="i18n.FlowFamilies">Antal e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.Profiles">Antal profiler</xsl:variable>
	<xsl:variable name="i18n.DisableFlows">Inaktivera e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.NoOperatingMessagesFound">Inga driftmeddelanden hittades</xsl:variable>
	<xsl:variable name="i18n.SelectMessageType">V�lj meddelandetyp</xsl:variable>
	<xsl:variable name="i18n.MessageTypeWarning">Varning</xsl:variable>
	<xsl:variable name="i18n.MessageTypeInfo">Info</xsl:variable>
	<xsl:variable name="i18n.MessageTypeWarningText">Varningsmeddelande</xsl:variable>
	<xsl:variable name="i18n.MessageTypeInfoText">Informationsmeddelande</xsl:variable>
	<xsl:variable name="i18n.All">Alla</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.DeleteOperatingMessageConfirm">�r du s�ker p� att du vill ta bort driftmeddelandet</xsl:variable>
	<xsl:variable name="i18n.DeleteOperatingMessageTitle">Ta bort driftmeddelande</xsl:variable>
	
	<xsl:variable name="i18n.Submit">L�gg till</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format p� f�ltet'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'F�r kort inneh�ll i f�ltet'"/>
		
	<xsl:variable name="i18n.Validation.UnknownError" select="'Ok�nt fel p� f�ltet'"/>
	<xsl:variable name="i18n.UnknownFault" select="'Ett ok�nt fel intr�ffade'"/>
	
	<xsl:variable name="i18n.Global">Visa driftmeddelandet f�r alla e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.ChooseFlowFamilies">Visa driftmeddelandet f�r specifika e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.DisableFlowsLabel">Inaktivera e-tj�nsterna under perioden f�r driftmeddelandet</xsl:variable>
	
	<xsl:variable name="i18n.message">meddelande</xsl:variable>
	<xsl:variable name="i18n.startDate">datum fr�n</xsl:variable>
	<xsl:variable name="i18n.startTime">tid fr�n</xsl:variable>
	<xsl:variable name="i18n.endDate">datum till</xsl:variable>
	<xsl:variable name="i18n.endTime">tid till</xsl:variable>
	<xsl:variable name="i18n.disabledFlows">inaktivera e-tj�nsterna</xsl:variable>
	<xsl:variable name="i18n.Validation.EndTimeBeforeStartTime">Slutdatum kan inte ligga f�re startdatum</xsl:variable>
	<xsl:variable name="i18n.Validation.DaysBetweenToSmall">Sluttid kan inte ligga f�re starttid</xsl:variable>
	<xsl:variable name="i18n.Validation.NoFlowFamilyChosen">Du har inte valt n�gra e-tj�nster</xsl:variable>
	<xsl:variable name="i18n.Validation.NoMessageTypeChosen">Du har inte valt meddelandetyp</xsl:variable>
	
	<xsl:variable name="i18n.Validation.TooLong.Part1">Inneh�llet i f�ltet </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part2"> �r </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part3"> tecken, vilket �verskrider maxgr�nsen p� </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part4"> tecken</xsl:variable>
	<xsl:variable name="i18n.AllowManagingOfInstancesLabel">Till�t handl�ggning av �renden under driftstoppet</xsl:variable>
	<xsl:variable name="i18n.AllowUserHandlingOfSubmittedInstancesLabel">Till�t medborgare att hantera inskickade �renden under driftstoppet</xsl:variable>
	<xsl:variable name="i18n.AllProfiles">Visa driftmeddelandet f�r alla profiler</xsl:variable>
	<xsl:variable name="i18n.ChooseProfiles">Visa driftmeddelandet f�r specifika profiler</xsl:variable>
	
	<xsl:variable name="i18n.LocalOperatingMessages">Lokala meddelanden</xsl:variable>
	<xsl:variable name="i18n.ExternalOperatingMessages">Externa meddelanden</xsl:variable>
	<xsl:variable name="i18n.ExternalOperatingMessageSources">Externa k�llor</xsl:variable>
	<xsl:variable name="i18n.NoExternalOperatingMessageSourcesFound">Inga external k�llor �r inst�llda</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceName">Namn</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled">Status</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled.Yes">Aktiverad</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled.No">Inaktiverad</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageFrom">K�lla</xsl:variable>

	<xsl:variable name="i18n.UpdateNotificationSettings">�ndra notifierade anv�ndare</xsl:variable>	
	<xsl:variable name="i18n.Notifications">Notifiering vid externa driftmeddelanden</xsl:variable>
	<xsl:variable name="i18n.Notifications.description">F�ljande anv�ndare kommer att notifieras automatiskt n�r externa driftmeddelanden publiceras och avpubliceras.</xsl:variable>
	<xsl:variable name="i18n.NotificationSettings.Update.title">�ndra notifikationsinst�llningar</xsl:variable>
	<xsl:variable name="i18n.NotificationSettings.NotificationUsers">Anv�ndare som blir notifierade vid publiceras och avpubliceras externa driftmeddelanden</xsl:variable>
	
</xsl:stylesheet>
