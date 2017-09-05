<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
			
			<div class="full display-table bigmarginbottom">
				
				<xsl:if test="not(ManualMultiSignQueryInstance/ManualMultiSignQuery/hideCitizenIdetifierInPDF = 'true')">
					<div class="floatleft full bigmarginbottom">
						<strong><xsl:value-of select="$i18n.SocialSecurityNumber" /></strong><br/>
						<xsl:value-of select="ManualMultiSignQueryInstance/socialSecurityNumber" />
					</div>				
				</xsl:if>
				
				<div class="floatleft fifty bigmarginbottom">
					<strong><xsl:value-of select="$i18n.Firstname" /></strong><br/>
					<xsl:value-of select="ManualMultiSignQueryInstance/firstname" />
				</div>
				
				<div class="floatleft fifty bigmarginbottom">
					<strong><xsl:value-of select="$i18n.Lastname" /></strong><br/>
					<xsl:value-of select="ManualMultiSignQueryInstance/lastname" />
				</div>
				
				<xsl:if test="ManualMultiSignQueryInstance/email">

					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Email" /></strong><br/>
						<xsl:value-of select="ManualMultiSignQueryInstance/email" />
					</div>				
				
				</xsl:if>
				
				<xsl:if test="ManualMultiSignQueryInstance/mobilePhone">

					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.MobilePhone" /></strong><br/>
						<xsl:value-of select="ManualMultiSignQueryInstance/mobilePhone" />
					</div>				
				
				</xsl:if>				
				
			</div>
					
		</div>
		
	</xsl:template>		

</xsl:stylesheet>