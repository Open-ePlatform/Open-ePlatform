<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="SigningStatus"/>
		<xsl:apply-templates select="SignFragment"/>
		<xsl:apply-templates select="SignFlowInstance"/>
		<xsl:apply-templates select="Message"/>
		
	</xsl:template>
		
	<xsl:template match="SigningStatus">
		
		<p>
			<xsl:value-of select="$i18n.SigningStatus.Description"/>
		</p>
		
		<table class="full">
			<tr>
				<th>
					<xsl:value-of select="$i18n.Firstname"/>
				</th>
				<th>
					<xsl:value-of select="$i18n.Lastname"/>
				</th>				
				<th>
					<xsl:value-of select="$i18n.SocialSecurityNumber"/>
				</th>
				<th>
					<xsl:value-of select="$i18n.Email"/>
				</th>
				<th>
					<xsl:value-of select="$i18n.Mobilephone"/>
				</th>				
				<th>
					<xsl:value-of select="$i18n.Signature"/>
				</th>
			</tr>
			
			<xsl:apply-templates select="SigningParty"/>
			
		</table>
		
		<p>
			<xsl:value-of select="$i18n.SigningLinkMessage"/>
		</p>
		
		<p>
			<a href="{SigningLink}">
				<xsl:value-of select="SigningLink"/>
			</a>
		</p>
		
	</xsl:template>	
	
	<xsl:template match="SignFragment">
	
		<div class="contentitem">	
	
			<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
	
		</div>
	
	</xsl:template>
	
	<xsl:template match="SigningParty">

		<tr>
			<td>
				<xsl:value-of select="firstname"/>
			</td>
			<td>
				<xsl:value-of select="lastname"/>
			</td>			
			<td>
				<xsl:value-of select="socialSecurityNumber"/>
			</td>
			<td>
				<xsl:value-of select="email"/>
			</td>
			<td>
				<xsl:value-of select="mobilePhone"/>
			</td>			
			<td>
				
				<xsl:choose>
					<xsl:when test="Signature">
						<xsl:value-of select="Signature/added"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.No"/>
					</xsl:otherwise>				
				</xsl:choose>
				
			</td>
		</tr>

	</xsl:template>
	
	<xsl:template match="SignFlowInstance">
	
		<div class="contentitem">
			
			<h1>
				<xsl:value-of select="$i18n.SignFlowInstance"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="FlowInstance/Flow/name"/>
				<xsl:text>&#160;#</xsl:text>
				<xsl:value-of select="FlowInstance/flowInstanceID"/>
			</h1>
			
			<xsl:value-of select="Message" disable-output-escaping="yes"/>
			
			<p>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/pdf/{FlowInstance/flowInstanceID}">
					<img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pdf.png" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.DownloadFlowInstancePDF"/>
				</a>
			</p>
			
			<xsl:if test="not(Signature)">
				<a href="{/Document/requestinfo/uri}?sign=true" class="btn btn-green xl next arrow-mobile">
					
					<span class="only-mobile">
						<xsl:value-of select="$i18n.SignFlowInstanceButton"/>
					</span>
					
					<span class="hide-mobile">
						<xsl:value-of select="$i18n.SignFlowInstanceButton"/>
					</span>
				</a>
			</xsl:if>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="Message[@messageKey='FlowDisabled']">
	
		<div class="contentitem">
		
			<h1><xsl:value-of select="$i18n.FlowDisabled.title"/></h1>
			
			<p><xsl:value-of select="$i18n.FlowDisabled.message"/></p>
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="Message[@messageKey='SigningPartyNotFound']">
	
		<div class="contentitem">
		
			<h1><xsl:value-of select="$i18n.SigningPartyNotFound.title"/></h1>
			
			<p><xsl:value-of select="$i18n.SigningPartyNotFound.message"/></p>
		
		</div>	
	
	</xsl:template>
	
	<xsl:template match="Message[@messageKey='WrongStatusContentType']">
	
		<div class="contentitem">
		
			<h1><xsl:value-of select="$i18n.WrongStatusContentType.title"/></h1>
			
			<p><xsl:value-of select="$i18n.WrongStatusContentType.message"/></p>
		
		</div>	
	
	</xsl:template>	
	
</xsl:stylesheet>