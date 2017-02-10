<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowBrowserModule.sv.xsl"/>
	
	<xsl:include href="UserFlowInstanceModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="i18n.FlowName">E-tjänst</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceID">Ärendenummer</xsl:variable>
	<xsl:variable name="i18n.Status">Status</xsl:variable>
	<xsl:variable name="i18n.Updated">Senast sparat</xsl:variable>
	<xsl:variable name="i18n.LastEvent">Senaste händelse</xsl:variable>
	<xsl:variable name="i18n.Date">Datum</xsl:variable>
	
	<xsl:variable name="i18n.FirstSubmitted">Inskickat</xsl:variable>
	<xsl:variable name="i18n.LastSubmitted">Senast kompletterat</xsl:variable>
	<xsl:variable name="i18n.LastChanged">Senast ändrat</xsl:variable>
	<xsl:variable name="i18n.Managers">Handläggare</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.by">av</xsl:variable>
	<xsl:variable name="i18n.NoManager">Ingen handläggare tilldelad</xsl:variable>
	<xsl:variable name="i18n.Choose">Till ärende</xsl:variable>
	<xsl:variable name="i18n.NotEnabled">Ej tillgänglig</xsl:variable>
	
	<xsl:variable name="i18n.Continue">Fortsätt</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	
	<xsl:variable name="i18n.Details">Detaljer</xsl:variable>
	<xsl:variable name="i18n.ExternalMessages">Meddelanden</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceEvents">Ärendehistorik</xsl:variable>
	<xsl:variable name="i18n.NoExternalMessages">Inga meddelanden</xsl:variable>
	<xsl:variable name="i18n.NewMessage">Nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.Action">Händelse</xsl:variable>
	<xsl:variable name="i18n.Person">Person</xsl:variable>
	<xsl:variable name="i18n.NoEvents">Ingen ärendehistorik</xsl:variable>
	
	<xsl:variable name="i18n.Message">Meddelande</xsl:variable>
	<xsl:variable name="i18n.AttachFiles">Bifoga filer</xsl:variable>
	<xsl:variable name="i18n.Close">Stäng</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.ChooseFiles">Välj filer</xsl:variable>
	<xsl:variable name="i18n.MaximumFileSize">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.SubmitMessage">Skicka meddelande</xsl:variable>
	
	<xsl:variable name="i18n.DeleteFile">Ta bort fil</xsl:variable>

	<xsl:variable name="i18n.ShowFlowInstance">Visa ärende</xsl:variable>
	<xsl:variable name="i18n.Or">Eller</xsl:variable>
	<xsl:variable name="i18n.UpdateFlowInstance">Ändra ärende</xsl:variable>
	
	<xsl:variable name="i18n.Help">Hjälp</xsl:variable>
	
	
	
	<xsl:variable name="i18n.MyMessages">Mina meddelanden</xsl:variable>
	<xsl:variable name="i18n.ToFlowInstance">Till ärende</xsl:variable>
	
	<xsl:variable name="i18n.SavedFlowInstancesTitle">Ej inskickade ärenden</xsl:variable>
	<xsl:variable name="i18n.SavedFlowInstances.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.SavedFlowInstances.Part2">ärende</xsl:variable>
	<xsl:variable name="i18n.SavedFlowInstances.Part2.Plural">ärenden</xsl:variable>
	<xsl:variable name="i18n.SavedFlowInstances.Part3">som inte har skickats in</xsl:variable>
	<xsl:variable name="i18n.SavedFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Sparade ärenden</h2>
De ärenden som du har påbörjat kan du fortsätta arbeta med genom att klicka på "Fortsätt". Om du vill avbryta ärendet klicka på den röda knappen i anslutning till ärendet.
	</xsl:variable>
	
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesTitle">Ärenden som väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part1">Du har</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2">ärende</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part2.Plural">ärenden</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstances.Part3">som väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingMultiSignFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Ärenden som väntar på din signatur</h2>
De ärenden som någon annan har påbörjat med dig som signerings part. Signera genom att klicka på "Väntar på signering". 
	</xsl:variable>
	
	<xsl:variable name="i18n.SubmittedFlowInstances">Pågående ärenden</xsl:variable>
	<xsl:variable name="i18n.NoSubmittedFlowInstances">Du har inga pågående ärenden</xsl:variable>
	<xsl:variable name="i18n.SubmittedFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Pågående ärenden</h2>
Pågående ärenden är de ärenden som du har skickat in. Klicka på "Till ärende" för att komma till ditt ärende.
	</xsl:variable>
	
	<xsl:variable name="i18n.ArchivedFlowInstances">Avslutade ärenden</xsl:variable>
	<xsl:variable name="i18n.NoArchivedFlowInstances">Du har inga avslutade ärenden</xsl:variable>
	<xsl:variable name="i18n.ArchivedFlowInstancesHelp">
<h2 class="h1 full" data-icon-before="?">Avslutade ärenden</h2>
Så länge e-tjänsten för dina avslutade ärenden finns tillgängliga kan du alltid titta på ditt ärende genom att klicka på "Till ärende"
</xsl:variable>
	
	<xsl:variable name="i18n.DeleteFlowInstanceConfirm">Är du säker på att du vill ta bort ärendet</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstancePreviewError">Ett fel uppstod vid visning av ärendet.</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded.part1">Filen </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part2"> har en storlek på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part3"> vilket överskrider den maximalt tillåtna filstorleken på </xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded.part4">.</xsl:variable>
	
	<xsl:variable name="i18n.validationError.UnableToParseRequest">Ett okänt fel uppstod vid filuppladdningen</xsl:variable>
	<xsl:variable name="i18n.validationError.MessageRequired">Du behöver skriva ett meddelande</xsl:variable>
	<xsl:variable name="i18n.validationError.MessageToLong">Du har skrivit ett för långt meddelande</xsl:variable>
	<xsl:variable name="i18n.validationError.MessageToShort">Du har skrivit ett för kort meddelande</xsl:variable>
	
	<xsl:variable name="i18n.DownloadFlowInstancePDF">Hämta kvittens i PDF-format</xsl:variable>
	<xsl:variable name="i18n.DownloadFlowInstanceXML">Hämta ärendet i XML-format</xsl:variable>
	<xsl:variable name="i18n.DownloadFlowInstanceSignPDF">Hämta signerings underlag i PDF-format</xsl:variable>
	<xsl:variable name="i18n.DownloadSignature">Hämta signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingForYourSignature">Väntar på din signatur</xsl:variable>
	<xsl:variable name="i18n.WaitingForOthersSignatures">Väntar på signatur från annan part</xsl:variable>
	<xsl:variable name="i18n.WaitingForPayment">Väntar på betalning</xsl:variable>
	<xsl:variable name="i18n.PostedByManager">Handläggare</xsl:variable>
	<xsl:variable name="i18n.Owners">Sökande</xsl:variable>
	<xsl:variable name="i18n.SiteProfile">Kommun</xsl:variable>
	<xsl:variable name="i18n.CurrentStatus">Aktuell status:</xsl:variable>
</xsl:stylesheet>
