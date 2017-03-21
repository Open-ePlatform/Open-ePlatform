<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="SigningStatus"/>
		<xsl:apply-templates select="SignFragment"/>
		<xsl:apply-templates select="SignFlowInstance"/>
		<xsl:apply-templates select="Message"/>
		<xsl:apply-templates select="ListFlowInstancesExtension"/>
		
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
	
	<xsl:template match="ListFlowInstancesExtension">
	
		<xsl:variable name="flowInstanceCount" select="count(WaitingMultiSignFlowInstances/FlowInstance)" />

		<div class="errands-wrapper draft">
			<div class="heading-wrapper">
				<h2><xsl:value-of select="$i18n.WaitingMultiSignFlowInstancesTitle" /></h2>
				<h3 class="clearboth"><xsl:value-of select="$i18n.WaitingMultiSignFlowInstances.Part1" />
				<xsl:text>&#160;</xsl:text>
				<strong>
					<xsl:value-of select="$flowInstanceCount" />
					<xsl:text>&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="$flowInstanceCount > 1">
							<xsl:value-of select="$i18n.WaitingMultiSignFlowInstances.Part2.Plural" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.WaitingMultiSignFlowInstances.Part2" />
						</xsl:otherwise>
					</xsl:choose>
				</strong>
				<xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.WaitingMultiSignFlowInstances.Part3" /></h3>
				<xsl:call-template name="createHelpDialog">
					<xsl:with-param name="id" select="'waiting-multisign'" />
					<xsl:with-param name="text" select="$i18n.WaitingMultiSignFlowInstancesHelp" />
				</xsl:call-template>
			</div>
			<table class="oep-table errand-table">
				<thead>
					<tr>
						<th class="icon"></th>
						<th class="service"><span><xsl:value-of select="$i18n.FlowName" /></span></th>
						
						<xsl:if test="SiteProfiles">
							<th class="status"><span><xsl:value-of select="$i18n.SiteProfile" /></span></th>
						</xsl:if>								
						
						<th class="status"><span><xsl:value-of select="$i18n.Status" /></span></th>
						<th class="date"><span><xsl:value-of select="$i18n.Updated" /></span></th>
						<th class="link"></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="WaitingMultiSignFlowInstances/FlowInstance" mode="waiting-multisign" />
				</tbody>
			</table>
		</div>
	
		<div class="divider errands"></div>
	
	</xsl:template>
	
	<xsl:template match="FlowInstance" mode="waiting-multisign">
	
		<tr>
			<td class="icon"><i data-icon-before="w"></i></td>
			<td data-title="{$i18n.FlowName}" class="service"><xsl:value-of select="Flow/name" /></td>
			
			<xsl:call-template name="printSiteProfile"/>
			
			<td data-title="{$i18n.Status}" class="status"><xsl:value-of select="Status/name" /></td>
			<td data-title="{$i18n.Updated}" class="date">
				<xsl:choose>
					<xsl:when test="updated"><xsl:value-of select="updated" /></xsl:when>
					<xsl:otherwise><xsl:value-of select="added" /></xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="link">
				<a class="btn btn-green vertical-align-middle" href="{MultiSignURL}"><xsl:value-of select="$i18n.WaitingForYourSignature" /></a>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template name="createHelpDialog">
		
		<xsl:param name="id" />
		<xsl:param name="text" />
		<xsl:param name="class" select="''" />
		
		<div class="help {$class}">
			<a class="open-help" href="#" data-icon-after="?" data-help-box="helpdialog_{$id}"><span><xsl:value-of select="$i18n.Help" /></span></a>
			<div class="help-box" data-help-box="helpdialog_{$id}">
				<div>
		  			<div> 
		  				<a class="close" href="#" data-icon-after="x"></a>
		  				<xsl:copy-of select="$text" />
		  			</div> 
				</div>
			</div>
		</div>
		
		<div class="help-backdrop" data-help-box="helpdialog_{$id}" />
		
	</xsl:template>
	
	<xsl:template name="printSiteProfile">
	
		<xsl:if test="../../SiteProfiles">
			
			<td data-title="{$i18n.SiteProfile}" class="siteProfile">
				
				<xsl:choose>
					<xsl:when test="Attributes/Attribute/Name = 'siteProfileName'">
					
						<xsl:value-of select="Attributes/Attribute[Name = 'siteProfileName']/Value"/>
					
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:variable name="profileID" select="profileID"/>
						
						<xsl:value-of select="../../SiteProfiles/Profile[profileID = $profileID]/name" />							
					
					</xsl:otherwise>
				</xsl:choose>
			</td>
					
		</xsl:if>	
	
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