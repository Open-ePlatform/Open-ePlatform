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
					<strong><xsl:value-of select="$i18n.Accountable"/></strong>
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
	
		<strong><xsl:value-of select="$i18n.SavedPersonData"/></strong>
		<ul>
			<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="show"/>
		</ul>
		
		<xsl:if test="reason != ''">
			<strong>
				<xsl:value-of select="$i18n.Reason"/>
			</strong>
			
			<br/>
			
			<xsl:value-of select="reason" disable-output-escaping="yes"/>
		</xsl:if>
		
		<strong><xsl:value-of select="$i18n.Reasons"/></strong>
		<ul>
			<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="show"/>
		</ul>
		
		<strong><xsl:value-of select="$i18n.YearsSaved"/></strong>
		<ul>
			<xsl:apply-templates select="StorageSettings" mode="list"/>
		</ul>
		
		<xsl:if test="extraInformationStorage != ''">
			<strong>
				<xsl:value-of select="$i18n.ExtraInformationStorage"/>
			</strong>
			
			<br/>
			
			<xsl:value-of select="extraInformationStorage" disable-output-escaping="yes"/>
		</xsl:if>
		
		<xsl:if test="dataRecipient != ''">
			<p>
				<strong><xsl:value-of select="$i18n.DataRecipient"/></strong>
				
				<br/>
				
				<xsl:value-of select="dataRecipient" disable-output-escaping="yes"/>
			</p>
		</xsl:if>
		
		<xsl:if test="extraInformation != ''">
			<p>
				<strong><xsl:value-of select="$i18n.ExtraInformation"/></strong>
				<br/>
				
				<xsl:value-of select="extraInformation" disable-output-escaping="yes"/>
			</p>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="StorageSetting" mode="list">
	
		<li>
			<xsl:choose>
				<xsl:when test="storageType = 'INFINITY'">
					<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="period"/>
					
					<xsl:text> </xsl:text>
					
					<xsl:choose>
						<xsl:when test="storageType = 'YEAR'">
							<xsl:value-of select="$i18n.YearsSaved.Years"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.YearsSaved.Months"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="description">
				<xsl:text> - </xsl:text>
				
				<xsl:value-of select="description"/>
			</xsl:if>
		</li>
		
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