<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>

	<xsl:include href="OperatingMessageModuleTemplates.xsl"/>
	
	<xsl:variable name="java.newExternalOperatingMessageEmailSubject">Nytt externt driftmeddelande publicerat för $name</xsl:variable>
	<xsl:variable name="java.newExternalOperatingMessageEmailMessage">
		&lt;p&gt;Ett nytt externt driftmeddelande publicerat för $name har tagits emot med följande meddelande:&lt;/p&gt;
		&lt;p&gt;$message&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="java.removedExternalOperatingMessageEmailSubject">Externt driftmeddelande för $name avpublicerat</xsl:variable>
	<xsl:variable name="java.removedExternalOperatingMessageEmailMessage">
		&lt;p&gt;Ett externt driftmeddelande för $name har avpublicerat bort med följande meddelande:&lt;/p&gt;
		&lt;p&gt;$message&lt;/p&gt;
	</xsl:variable>
	
	<xsl:variable name="i18n.AddOperatingMessage">Lägg till driftmeddelande</xsl:variable>
	<xsl:variable name="i18n.UpdateOperatingMessage">Ändra driftmeddelande</xsl:variable>
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.Publish">Publiceras</xsl:variable>
	<xsl:variable name="i18n.UnPublish">Avpubliceras</xsl:variable>
	<xsl:variable name="i18n.StartDate">Datum från</xsl:variable>
	<xsl:variable name="i18n.EndDate">Datum till</xsl:variable>
	<xsl:variable name="i18n.StartTime">Tid från</xsl:variable>
	<xsl:variable name="i18n.EndTime">Tid till</xsl:variable>
	<xsl:variable name="i18n.MessageType">Meddelandetyp</xsl:variable>
	<xsl:variable name="i18n.FlowFamilies">Antal e-tjänster</xsl:variable>
	<xsl:variable name="i18n.Profiles">Antal profiler</xsl:variable>
	<xsl:variable name="i18n.DisableFlows">Inaktivera e-tjänst</xsl:variable>
	<xsl:variable name="i18n.NoOperatingMessagesFound">Inga driftmeddelanden hittades</xsl:variable>
	<xsl:variable name="i18n.SelectMessageType">Välj meddelandetyp</xsl:variable>
	<xsl:variable name="i18n.MessageTypeWarning">Varning</xsl:variable>
	<xsl:variable name="i18n.MessageTypeInfo">Info</xsl:variable>
	<xsl:variable name="i18n.MessageTypeWarningText">Varningsmeddelande</xsl:variable>
	<xsl:variable name="i18n.MessageTypeInfoText">Informationsmeddelande</xsl:variable>
	<xsl:variable name="i18n.All">Alla</xsl:variable>
	<xsl:variable name="i18n.Yes">Ja</xsl:variable>
	<xsl:variable name="i18n.No">Nej</xsl:variable>
	<xsl:variable name="i18n.DeleteOperatingMessageConfirm">Är du säker på att du vill ta bort driftmeddelandet</xsl:variable>
	<xsl:variable name="i18n.DeleteOperatingMessageTitle">Ta bort driftmeddelande</xsl:variable>
	
	<xsl:variable name="i18n.Submit">Lägg till</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.Validation.RequiredField" select="'Du måste fylla i fältet'"/>
	<xsl:variable name="i18n.Validation.InvalidFormat" select="'Felaktigt format på fältet'"/>
	<xsl:variable name="i18n.Validation.TooShort" select="'För kort innehåll i fältet'"/>
		
	<xsl:variable name="i18n.Validation.UnknownError" select="'Okänt fel på fältet'"/>
	<xsl:variable name="i18n.UnknownFault" select="'Ett okänt fel inträffade'"/>
	
	<xsl:variable name="i18n.Global">Visa driftmeddelandet för alla e-tjänster</xsl:variable>
	<xsl:variable name="i18n.ChooseFlowFamilies">Visa driftmeddelandet för specifika e-tjänster</xsl:variable>
	<xsl:variable name="i18n.DisableFlowsLabel">Inaktivera e-tjänsterna under perioden för driftmeddelandet</xsl:variable>
	
	<xsl:variable name="i18n.message">meddelande</xsl:variable>
	<xsl:variable name="i18n.startDate">datum från</xsl:variable>
	<xsl:variable name="i18n.startTime">tid från</xsl:variable>
	<xsl:variable name="i18n.endDate">datum till</xsl:variable>
	<xsl:variable name="i18n.endTime">tid till</xsl:variable>
	<xsl:variable name="i18n.disabledFlows">inaktivera e-tjänsterna</xsl:variable>
	<xsl:variable name="i18n.Validation.EndTimeBeforeStartTime">Slutdatum kan inte ligga före startdatum</xsl:variable>
	<xsl:variable name="i18n.Validation.DaysBetweenToSmall">Sluttid kan inte ligga före starttid</xsl:variable>
	<xsl:variable name="i18n.Validation.NoFlowFamilyChosen">Du har inte valt några e-tjänster</xsl:variable>
	<xsl:variable name="i18n.Validation.NoMessageTypeChosen">Du har inte valt meddelandetyp</xsl:variable>
	
	<xsl:variable name="i18n.Validation.TooLong.Part1">Innehållet i fältet </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part2"> är </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part3"> tecken, vilket överskrider maxgränsen på </xsl:variable>
	<xsl:variable name="i18n.Validation.TooLong.Part4"> tecken</xsl:variable>
	<xsl:variable name="i18n.AllowManagingOfInstancesLabel">Tillåt handläggning av ärenden under driftstoppet</xsl:variable>
	<xsl:variable name="i18n.AllowUserHandlingOfSubmittedInstancesLabel">Tillåt medborgare att hantera inskickade ärenden under driftstoppet</xsl:variable>
	<xsl:variable name="i18n.AllProfiles">Visa driftmeddelandet för alla profiler</xsl:variable>
	<xsl:variable name="i18n.ChooseProfiles">Visa driftmeddelandet för specifika profiler</xsl:variable>
	
	<xsl:variable name="i18n.LocalOperatingMessages">Lokala meddelanden</xsl:variable>
	<xsl:variable name="i18n.ExternalOperatingMessages">Externa meddelanden</xsl:variable>
	<xsl:variable name="i18n.ExternalOperatingMessageSources">Externa källor</xsl:variable>
	<xsl:variable name="i18n.NoExternalOperatingMessageSourcesFound">Inga external källor är inställda</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceName">Namn</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled">Status</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled.Yes">Aktiverad</xsl:variable>
	<xsl:variable name="i18n.ExternalSourceEnabled.No">Inaktiverad</xsl:variable>
	<xsl:variable name="i18n.ExternalMessageFrom">Källa</xsl:variable>

	<xsl:variable name="i18n.UpdateNotificationSettings">Ändra notifierade användare</xsl:variable>	
	<xsl:variable name="i18n.Notifications">Notifiering vid externa driftmeddelanden</xsl:variable>
	<xsl:variable name="i18n.Notifications.description">Följande användare kommer att notifieras automatiskt när externa driftmeddelanden publiceras och avpubliceras.</xsl:variable>
	<xsl:variable name="i18n.NotificationSettings.Update.title">Ändra notifikationsinställningar</xsl:variable>
	<xsl:variable name="i18n.NotificationSettings.NotificationUsers">Användare som blir notifierade vid publiceras och avpubliceras externa driftmeddelanden</xsl:variable>
	
</xsl:stylesheet>
