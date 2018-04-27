<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="StatisticsModuleTemplates.xsl"/>
	
	<xsl:variable name="java.flowFamilyCountChartLabel">Publicerade e-tj�nster</xsl:variable>
	<xsl:variable name="java.surveyRatingsChartLabel">Anv�ndarn�jdhet per vecka</xsl:variable>
	
	<xsl:variable name="java.csvWeek">Vecka</xsl:variable>
	<xsl:variable name="java.csvFlowInstanceCount">Antal �renden</xsl:variable>
	<xsl:variable name="java.csvFlowInstanceCountFile">antal �renden.csv</xsl:variable>
	<xsl:variable name="java.csvGlobalFlowCount">Antal e-tj�nster</xsl:variable>
	<xsl:variable name="java.csvGlobalFlowCountFile">antal e-tj�nster.csv</xsl:variable>
	<xsl:variable name="java.csvFamilyRating">Anv�ndarn�jdhet</xsl:variable>
	<xsl:variable name="java.csvFamilyRatingFile">anv�ndarn�jdhet.csv</xsl:variable>
	<xsl:variable name="java.csvStep">Steg</xsl:variable>
	<xsl:variable name="java.csvAbortCount">Avbrutna �renden</xsl:variable>
	<xsl:variable name="java.csvStepAbortCountFile">avbrutna �renden.csv</xsl:variable>
	<xsl:variable name="java.csvUnsubmittedCount">Antal ej inskickade �renden</xsl:variable>
	<xsl:variable name="java.csvStepUnsubmittedCountFile">antal ej inskickade �renden.csv</xsl:variable>
	<xsl:variable name="java.csvGlobaExternalFlowCount">Antal externa e-tj�nster</xsl:variable>
	<xsl:variable name="java.csvGlobaPDFFormFlowCount">Antal e-tj�nster med enbart blankett</xsl:variable>
	<xsl:variable name="java.csvDetailedGlobalFlowCountFile">detaljerad statistik e-tj�nster.csv</xsl:variable>
	<xsl:variable name="java.csvDetailedFlowInstanceCountFile">detaljerad statistik �renden.csv</xsl:variable>
	<xsl:variable name="java.csvFlowName">E-tj�nst</xsl:variable>
	<xsl:variable name="java.csvFlowType">Kategori</xsl:variable>
	<xsl:variable name="java.csvTotalCountWeek">Totalt</xsl:variable>
	<xsl:variable name="java.csvFemaleCountWeek">Kvinna</xsl:variable>
	<xsl:variable name="java.csvMaleCountWeek">Man</xsl:variable>
	<xsl:variable name="java.csvUnkownCountWeek">Ok�nt</xsl:variable>
	
	<xsl:variable name="i18n.RatingPerWeek">Anv�ndarn�jdhet per vecka</xsl:variable>
	
	<xsl:variable name="i18n.FlowInstancesPerWeek">Antal �renden per vecka</xsl:variable>
	<xsl:variable name="i18n.RedirectsPerWeek">Antal klick per vecka</xsl:variable>
	
	<xsl:variable name="i18n.FlowStepAbortCountChartLabel">Avbrutna �renden per steg</xsl:variable>
	<xsl:variable name="i18n.FlowStepUnsubmittedCountChartLabel">Antal sparade �renden per steg (ej inskickade �renden)</xsl:variable>
	<xsl:variable name="i18n.NoStatisticsAvailable">Det finns f�r n�rvarande ingen statstik tillg�nglig f�r detta v�rde.</xsl:variable>
	
	<xsl:variable name="i18n.FlowFamilies">V�lj e-tj�nst</xsl:variable>
	<xsl:variable name="i18n.Version">Version </xsl:variable>
	<xsl:variable name="i18n.PublishedFlowFamiliesPerWeek">Antal publicerade e-tj�nster per vecka</xsl:variable>
	<xsl:variable name="i18n.DownloadChartDataInCSVFormat">Ladda ner datat f�r detta diagram i CSV-format</xsl:variable>
	<xsl:variable name="i18n.DownloadDetailedChartDataInCSVFormat">Ladda ner detaljerat data f�r detta diagram i CSV-format</xsl:variable>
	
	<xsl:variable name="i18n.DownloadChartData">Ladda ner</xsl:variable>
	<xsl:variable name="i18n.DownloadDetailedChartData">Ladda ner detaljerad statistik</xsl:variable>
	
	<xsl:variable name="i18n.LoadingPleaseWait">Statistiken h�ller p� att genereras, prova att ladda om sidan om en liten stund.</xsl:variable>
	
</xsl:stylesheet>
