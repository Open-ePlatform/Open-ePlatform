<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<a name="query{PersonDataInformerQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="PersonDataInformerQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
			</h2>
			
			<xsl:if test="Description">
				
				<div class="query-description">
					<xsl:choose>
						<xsl:when test="isHTMLDescription = 'true'">
							<xsl:value-of select="Description" disable-output-escaping="yes"/>
						</xsl:when>
						<xsl:otherwise>
							<p>
								<xsl:value-of select="Description" disable-output-escaping="yes"/>
							</p>
						</xsl:otherwise>
					</xsl:choose>
				</div>
								
			</xsl:if>
			
			<div class="margintop">
				<xsl:apply-templates select="PersonDataInformerQueryInstance/PersonDataInformerQuery/FlowFamilyInformerSetting"/>
			</div>
			
			<xsl:if test="PersonDataInformerQueryInstance/FlowFamily/ownerName">
				<p>
					<xsl:value-of select="$i18n.Accountable"/>
					<br/>
					<xsl:value-of select="PersonDataInformerQueryInstance/FlowFamily/ownerName" />
				</p>
			</xsl:if>
			
			<p>
				<xsl:if test="PersonDataInformerQueryInstance/accepted = 'true'">
					<strong><xsl:value-of select="$i18n.Accept" /></strong>
				</xsl:if>
			</p>
			
		</div>
		
	</xsl:template>

	<xsl:template match="CheckboxAlternative" mode="show">

		<xsl:value-of select="name" />		

		<xsl:if test="position() != last()"><br/></xsl:if>
	
	</xsl:template>
	
	<xsl:template match="FlowFamilyInformerSetting">
	
		<xsl:value-of select="$i18n.SavedPersonData"/>
		<xsl:text>:</xsl:text>
		<ul>
			<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="show"/>
		</ul>
		
		<xsl:if test="reason">
			<p>
				<xsl:value-of select="$i18n.Reason"/>
				<br/>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="reason"/>
				</xsl:call-template>
			</p>
		</xsl:if>
		
		<xsl:value-of select="$i18n.Reasons"/>
		<ul>
			<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="show"/>
		</ul>
		
		<p>
			<xsl:value-of select="$i18n.YearsSaved"/>
			<xsl:text>:  </xsl:text>
			
			<xsl:variable name="years" select="yearsSaved"/>
		
			<xsl:choose>
				<xsl:when test="$years">
					<xsl:value-of select="$years"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$i18n.years"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
				</xsl:otherwise>
			</xsl:choose>
		</p>
		
		<xsl:if test="extraInformation">
			<p>
				<xsl:value-of select="$i18n.ExtraInformation"/>
				<br/>
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="extraInformation"/>
				</xsl:call-template>
			</p>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="show">

		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="show">
	
		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>		

</xsl:stylesheet>