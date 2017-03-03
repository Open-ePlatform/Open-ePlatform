<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<a name="query{TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
				
				<div class="floatleft fifty bigmarginbottom">
					<strong><xsl:value-of select="$i18n.Name" /></strong><br/>
					<xsl:call-template name="printValue">
						<xsl:with-param name="value" select="OrganizationDetailQueryInstance/name" />
					</xsl:call-template>
				</div>
				
				<div class="floatleft fifty bigmarginbottom">
					<strong><xsl:value-of select="$i18n.OrganizationNumber" /></strong><br/>
					<xsl:call-template name="printValue">
						<xsl:with-param name="value" select="OrganizationDetailQueryInstance/organizationNumber" />
					</xsl:call-template>
				</div>
				
				<xsl:if test="OrganizationDetailQueryInstance/address">
					
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Address" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/address" />
						</xsl:call-template>
					</div>
					
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.ZipCodeAndPostalAddress" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/zipCode" />
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/postalAddress" />
						</xsl:call-template>
					</div>
					
				</xsl:if>
				
				<div class="floatleft full">
					<h3><xsl:value-of select="$i18n.ContactPerson" /></h3>
				</div>
				
				<div class="floatleft fifty bigmarginbottom">
					<strong><xsl:value-of select="$i18n.FirstnameAndLastname" /></strong><br/>
					<xsl:call-template name="printValue">
						<xsl:with-param name="value" select="OrganizationDetailQueryInstance/firstname" />
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="printValue">
						<xsl:with-param name="value" select="OrganizationDetailQueryInstance/lastname" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="OrganizationDetailQueryInstance/email">
			
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Email" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/email" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<xsl:if test="OrganizationDetailQueryInstance/phone">
					
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Phone" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/phone" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<xsl:if test="OrganizationDetailQueryInstance/mobilePhone">
		
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.MobilePhone" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<div class="floatleft full">
					<strong><xsl:value-of select="$i18n.ChooseContactChannels" /></strong><br/>
					
					<xsl:if test="OrganizationDetailQueryInstance/email">
						<xsl:value-of select="$i18n.ContactByEmail" /><br/>
					</xsl:if>
					
					<xsl:if test="OrganizationDetailQueryInstance/contactBySMS = 'true'">
						<xsl:value-of select="$i18n.ContactBySMS" /><br/>
					</xsl:if>
				</div>
			</div>
					
		</div>
		
	</xsl:template>

	<xsl:template name="printValue">
		
		<xsl:param name="value" />
		
		<xsl:choose>
			<xsl:when test="$value">
				<xsl:value-of select="$value"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>-</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>