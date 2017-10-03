<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCitizenID = 'HIDDEN')">
					<div class="floatleft full bigmarginbottom">
						<strong><xsl:value-of select="$i18n.CitizenID" /></strong><br/>
						<xsl:value-of select="ContactDetailQueryInstance/citizenID" />
					</div>	
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldName = 'HIDDEN')">
					<div class="floatleft full bigmarginbottom">
						<strong><xsl:value-of select="$i18n.FirstnameAndLastname" /></strong><br/>
						<xsl:value-of select="ContactDetailQueryInstance/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="ContactDetailQueryInstance/lastname" />
					</div>
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'HIDDEN')">
					
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCareOf = 'HIDDEN')">
					
						<div class="floatleft full bigmarginbottom">
							<strong><xsl:value-of select="$i18n.CareOf" /></strong><br/>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/careOf" />
							</xsl:call-template>
						</div>	
					
					</xsl:if>
					
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Address" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/address" />
						</xsl:call-template>
					</div>					
					
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.ZipCodeAndPostalAddress" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/zipCode" />
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/postalAddress" />
						</xsl:call-template>
					</div>
						
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldPhone = 'HIDDEN')">
						
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Phone" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/phone" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN')">
			
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.Email" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/email" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN')">
			
					<div class="floatleft fifty bigmarginbottom">
						<strong><xsl:value-of select="$i18n.MobilePhone" /></strong><br/>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="ContactDetailQueryInstance/mobilePhone" />
						</xsl:call-template>
					</div>
				
				</xsl:if>
				
				<xsl:if test="ContactDetailQueryInstance/email or ContactDetailQueryInstance/contactBySMS = 'true'">
				
					<div class="floatleft full">
						<strong><xsl:value-of select="$i18n.ChooseContactChannels" /></strong><br/>
						
						<xsl:if test="ContactDetailQueryInstance/email">
							<xsl:value-of select="$i18n.ContactByEmail" /><br/>
						</xsl:if>
						
						<xsl:if test="ContactDetailQueryInstance/contactBySMS = 'true'">
							<xsl:value-of select="$i18n.ContactBySMS" /><br/>
						</xsl:if>
					</div>
				
				</xsl:if>
				
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