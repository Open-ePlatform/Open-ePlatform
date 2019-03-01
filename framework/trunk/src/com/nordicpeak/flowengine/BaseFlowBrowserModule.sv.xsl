<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:import href="BaseFlowModule.sv.xsl"/>
	
	<xsl:import href="BaseFlowBrowserModuleTemplates.xsl"/>
	
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:variable name="i18n.FlowDisabled">Den här e-tjänsten är inte tillgänglig för tillfället</xsl:variable>
	<xsl:variable name="i18n.FlowNotAvailiableInRequestedFormat">E-tjänsten är inte tillgänglig i det begärda formatet</xsl:variable>
	<xsl:variable name="i18n.FlowNoLongerAvailable">E-tjänsten är inte längre tillgänglig.</xsl:variable>
	<xsl:variable name="i18n.FlowNoLongerPublished">Den här versionen av e-tjänsten är inte längre publicerad i systemet. Sök upp e-tjänsten här nedan och starta den på nytt utifrån den senaste versionen.</xsl:variable>
	
	<xsl:variable name="i18n.RequestedFlowNotFound">Den begärda e-tjänsten hittades inte.</xsl:variable>
	<xsl:variable name="i18n.RequestedFlowInstanceNotFound">Det begärda ärendet hittades inte.</xsl:variable>
	<xsl:variable name="i18n.InvalidLinkRequested">Den begärda e-tjänsten hittades inte.</xsl:variable>		
	
	<xsl:variable name="i18n.FlowInstanceNoLongerAvailable">Den här e-tjänsten är inte tillgänglig lägre.</xsl:variable>
	<xsl:variable name="i18n.ErrorGettingFlowInstanceManager">Ett fel uppstod när ärendet skulle öppnas.</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceErrorDataSaved">Ett fel uppstod i e-tjänsten, ditt ärende har sparats.</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceErrorDataNotSaved">Ett fel uppstod i e-tjänsten.</xsl:variable>
	<xsl:variable name="i18n.FlowInstanceManagerClosed">Den här kopian på ärendet har stängts.</xsl:variable>
	
	<xsl:variable name="i18n.CancelFlowInstanceConfirm">Är du säker på att du vill avbryta ärendet</xsl:variable>
	<xsl:variable name="i18n.DeleteFlowInstanceConfirm">Är du säker på att du vill ta bort ärendet</xsl:variable>
	
	<xsl:variable name="i18n.overview">Ärendeöversikt</xsl:variable>
	<xsl:variable name="i18n.showInstance">Visa ärende</xsl:variable>
	<xsl:variable name="i18n.updateInstance">Ändra ärende</xsl:variable>
	<xsl:variable name="i18n.cancelInstance">Avbryt ärende</xsl:variable>
	
	<xsl:variable name="i18n.updateStatus">Ändra status</xsl:variable>
	<xsl:variable name="i18n.updateManagers">Ändra handläggare</xsl:variable>
	<xsl:variable name="i18n.deleteInstance">Ta bort ärende</xsl:variable>
	<xsl:variable name="i18n.bookmarkInstance">Flagga ärende</xsl:variable>
</xsl:stylesheet>
