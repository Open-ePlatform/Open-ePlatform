<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">
	
		<section>
 			<h2 class="bordered"><xsl:value-of select="module/name"/></h2>
 			<ul class="list-table">
 				<xsl:apply-templates select="Flows/Flow"/>
 			</ul>
 		</section>
	
	</xsl:template>
	
	<xsl:template match="Flow">
	
		<li>
			<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">odd</xsl:if>
				<xsl:if test="enabled = 'false'"> disabled</xsl:if>
			</xsl:attribute>
		
			<xsl:choose>
				<xsl:when test="enabled = 'true'">
					
					<a href="{../../browserModuleAlias}/overview/{FlowFamily/flowFamilyID}">
						<span class="text">
							<xsl:value-of select="name"/>
						</span>
						
						<xsl:if test="not(../../loggedIn) and requireAuthentication = 'true'">
							<i data-icon-after="u" title="{$i18n.requiresAuthentication}"></i>				
						</xsl:if>
					</a>
					
				</xsl:when>
				<xsl:otherwise>
					
					<span class="text">
						<xsl:value-of select="name"/>
					</span>
				
					<xsl:if test="not(../../loggedIn) and requireAuthentication = 'true'">
						<i data-icon-after="u" title="{$i18n.requiresAuthentication}"></i>				
					</xsl:if>
				
				</xsl:otherwise>
			</xsl:choose>
		
		</li>		
	
	</xsl:template>					
	
</xsl:stylesheet>