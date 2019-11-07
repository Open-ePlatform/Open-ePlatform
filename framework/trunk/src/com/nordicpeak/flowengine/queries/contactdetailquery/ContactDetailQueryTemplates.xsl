<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/contactdetailquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/contactdetailquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="ContactDetailQueryInstance/ContactDetailQuery/queryID" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/description">
					<span class="italic">
						<xsl:value-of select="ContactDetailQueryInstance/ContactDetailQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCitizenID = 'HIDDEN')">

					<fieldset>

						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.CitizenID" /></strong>
							<xsl:value-of select="ContactDetailQueryInstance/citizenID" />						
						</div>				
				
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldName = 'HIDDEN')">
				
					<fieldset>
						
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.FirstnameAndLastname" /></strong>
							<xsl:value-of select="ContactDetailQueryInstance/firstname" />
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="ContactDetailQueryInstance/lastname" />
						</div>
						
						<div class="split odd"></div>
						
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'HIDDEN')">
				
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCareOf = 'HIDDEN')">
					
						<fieldset>
					
							<div class="split">
								<strong class="block"><xsl:value-of select="$i18n.CareOf" /></strong>
								<xsl:call-template name="printValue">
									<xsl:with-param name="value" select="ContactDetailQueryInstance/careOf" />
								</xsl:call-template>
							</div>
						
						</fieldset>
					
					</xsl:if>
					
					<fieldset>
					
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Address" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/address" />
							</xsl:call-template>
						</div>
						
						<div class="split odd">
							<strong class="block"><xsl:value-of select="$i18n.ZipCodeAndPostalAddress" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/zipCode" />
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/postalAddress" />
							</xsl:call-template>
						</div>
						
					</fieldset>
					
				</xsl:if>
				
				<fieldset>
					
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldPhone = 'HIDDEN')">
						
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Phone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/phone" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN')">
			
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Email" /></strong>
							<xsl:choose>
								<xsl:when test="ContactDetailQueryInstance/email">
									<a href="mailto:{ContactDetailQueryInstance/email}" ><xsl:value-of select="ContactDetailQueryInstance/email" /></a>
								</xsl:when>
								<xsl:otherwise>-</xsl:otherwise>
							</xsl:choose>
						</div>
					
					</xsl:if>
					
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN')">
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.MobilePhone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/mobilePhone" />
							</xsl:call-template>
						</div>
					</xsl:if>
					
				</fieldset>
				
				<xsl:if test="ContactDetailQueryInstance/email or ContactDetailQueryInstance/contactBySMS = 'true'">
				
					<fieldset>
						
						<div>
							<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
							
							<xsl:if test="ContactDetailQueryInstance/email">
								<xsl:value-of select="$i18n.ContactByEmail" /><br/>
							</xsl:if>
							
							<xsl:if test="ContactDetailQueryInstance/contactBySMS = 'true'">
								<xsl:value-of select="$i18n.ContactBySMS" /><br/>
							</xsl:if>
						</div>
						
	 				</fieldset>
 				
 				</xsl:if>
				
			</article>
			
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
	
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', ContactDetailQueryInstance/ContactDetailQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', ContactDetailQueryInstance/ContactDetailQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			
			<xsl:attribute name="class">
				<xsl:text>query contactdetailquery</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
		
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
				
					<div class="info-box error">
					
						<xsl:choose>
							<xsl:when test="ValidationErrors/validationError[not(fieldName)]">
								<xsl:apply-templates select="ValidationErrors/validationError[not(fieldName)]"/>
							</xsl:when>
							<xsl:otherwise>
								<span/>
							</xsl:otherwise>
						</xsl:choose>
				
						<div class="marker"></div>
					</div>
				
				</div>
			</xsl:if>
	
			<article>
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/helpText">		
						<xsl:apply-templates select="ContactDetailQueryInstance/ContactDetailQuery/helpText" />
					</xsl:if>
				
				</div>
				
				<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="ContactDetailQueryInstance/ContactDetailQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCitizenID = 'HIDDEN')">

					<fieldset>

						<div class="split">
	
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_citizenID')" />
						
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:attribute name="class">
									<xsl:text>split invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>
						
							<label for="{$fieldName}" >
								<xsl:attribute name="class">
									<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldCitizenID = 'REQUIRED'">required</xsl:if>
								</xsl:attribute>
							
								<xsl:value-of select="$i18n.CitizenIDWithFormat" />
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.CitizenIDWithFormat"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/Poster">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/Poster and not(RequestMetadata/manager = 'true')"><xsl:value-of select="/Document/Poster/citizenID" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="ContactDetailQueryInstance/citizenID" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
						</div>				
				
					</fieldset>
				
				</xsl:if>				
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldName = 'HIDDEN')">
				
					<fieldset>
						
						<div class="split">
						
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_firstname')" />
						
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:attribute name="class">
									<xsl:text>split invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>
						
							<label for="{$fieldName}" >
								<xsl:attribute name="class">
									<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldName = 'REQUIRED'">required</xsl:if>
								</xsl:attribute>
								
								<xsl:value-of select="$i18n.Firstname" />
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Firstname"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/Poster">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/Poster and not(RequestMetadata/manager = 'true')"><xsl:value-of select="/Document/Poster/firstname" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="ContactDetailQueryInstance/firstname" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
					
						<div class="split odd">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_lastname')" />
							
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:attribute name="class">
									<xsl:text>split odd invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>	
						
							<label for="{$fieldName}" >
								<xsl:attribute name="class">
									<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldName = 'REQUIRED'">required</xsl:if>
								</xsl:attribute>
								
								<xsl:value-of select="$i18n.Lastname" />
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Lastname"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/Poster">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/Poster and not(RequestMetadata/manager = 'true')"><xsl:value-of select="/Document/Poster/lastname" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="ContactDetailQueryInstance/lastname" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'HIDDEN')">
				
					<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldCareOf = 'HIDDEN')">

						<fieldset>
	
							<div class="split">
								
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_careof')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:attribute name="class">
										<xsl:text>split invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>	
								
								<label for="{$fieldName}">
									<xsl:attribute name="class">
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldCareOf = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.CareOf" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.CareOf"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="disabled">
										<xsl:if test="ContactDetailQueryInstance/officalAddress = 'true'">disabled</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/careOf"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
								
							</div>				
					
						</fieldset>
						
					</xsl:if>
					
					<fieldset>
					
						<xsl:variable name="requireAddress" select="ContactDetailQueryInstance/ContactDetailQuery/requireAddress = 'true'" />
					
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_address')" />
						
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:attribute name="class">
									<xsl:text>split invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>	
							
							<label for="{$fieldName}">
								<xsl:attribute name="class">
									<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'REQUIRED'">required</xsl:if>
								</xsl:attribute>
								
								<xsl:value-of select="$i18n.Address" />
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Address"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="disabled">
									<xsl:if test="ContactDetailQueryInstance/officalAddress = 'true'">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value" select="ContactDetailQueryInstance/address"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
					
						<div class="split">
							
							<div class="left">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_zipcode')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:attribute name="class">
										<xsl:text>left invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>	
								
								<label for="{$fieldName}">
									<xsl:attribute name="class">
										<xsl:text>floatleft </xsl:text>
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.ZipCode" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.ZipCode"/>
									<xsl:with-param name="size" select="15"/>
									<xsl:with-param name="class" select="'floatleft clearboth'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="ContactDetailQueryInstance/officalAddress = 'true'">disabled</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/zipCode"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
							</div>
						
							<div class="right">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_postaladdress')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:attribute name="class">
										<xsl:text>right invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>	
							
								<label for="{$fieldName}">
									<xsl:attribute name="class">
										<xsl:text>floatleft </xsl:text>
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldAddress = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.PostalAddress" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="title" select="$i18n.PostalAddress"/>
									<xsl:with-param name="size" select="28"/>
									<xsl:with-param name="class" select="'floatleft clearboth'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="ContactDetailQueryInstance/officalAddress = 'true'">disabled</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/postalAddress"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
								
							</div>
							
						</div>
						
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldPhone = 'HIDDEN') or not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN') or not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN')">
				
					<fieldset>
						
						<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldPhone = 'HIDDEN')">
						
							<div class="split">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_phone')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:attribute name="class">
										<xsl:text>split invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>
							
								<label for="{$shortQueryID}_phone">
									<xsl:attribute name="class">
										<xsl:text>floatleft full </xsl:text>
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldPhone = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.Phone" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.Phone"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/phone"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							</div>
						
						</xsl:if>
						
						<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN')">
						
							<div class="split">
								
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_email')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName] or ValidationErrors/validationError[messageKey = 'EmailAlreadyTaken']">
									<xsl:attribute name="class">
										<xsl:text>split invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>	
							
								<label for="{$fieldName}">
									<xsl:attribute name="class">
										<xsl:text>floatleft full </xsl:text>
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.Email" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.Email"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/email"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
								<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'EmailAlreadyTaken']" />
								
							</div>
						
						</xsl:if>
						
						<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN')">
						
							<div class="split">
								
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_mobilephone')" />
							
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:attribute name="class">
										<xsl:text>split invalid input-error</xsl:text>
									</xsl:attribute>
								</xsl:if>
								
								<label for="{$fieldName}">
									<xsl:attribute name="class">
										<xsl:text>floatleft full </xsl:text>
										<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'REQUIRED'">required</xsl:if>
									</xsl:attribute>
									
									<xsl:value-of select="$i18n.MobilePhone" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.MobilePhone"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="value" select="ContactDetailQueryInstance/mobilePhone"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
								
							</div>
						
						</xsl:if>
						
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/hideNotificationChannelSettings = 'false' and (not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN') or not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN'))">
				
					<fieldset class="notification-channel-settings">
					
						<div>
							<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
						</div>
					
						<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN')">
					
							<div>
							
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="concat($shortQueryID, '_contactByEmail')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_contactByEmail')" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="disabled" select="'disabled'" />
									<xsl:with-param name="checked">
										<xsl:if test="ContactDetailQueryInstance/email != ''">true</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
								<label for="{$shortQueryID}_contactByEmail" class="checkbox disabled"><xsl:value-of select="$i18n.AllowContactByEmail" /></label><br/>
								
							</div>
						
						</xsl:if>
						
						<xsl:if test="not(ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'HIDDEN') and ContactDetailQueryInstance/ContactDetailQuery/allowSMS = 'true'">
							
							<xsl:variable name="forced" select="ContactDetailQueryInstance/ContactDetailQuery/fieldMobilePhone = 'REQUIRED' and ContactDetailQueryInstance/ContactDetailQuery/fieldEmail = 'HIDDEN'"/>
							
							<div>
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="concat($shortQueryID, '_contactBySMS')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_contactBySMS')" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="disabled" select="'disabled'" />
									<xsl:with-param name="class" >
										<xsl:if test="$forced">forced</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="checked" >
										<xsl:choose>
											<xsl:when test="$forced">true</xsl:when>
											<xsl:when test="ContactDetailQueryInstance">
												<xsl:value-of select="ContactDetailQueryInstance/contactBySMS"/>
											</xsl:when>
											<xsl:otherwise>false</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
								
								<label for="{$shortQueryID}_contactBySMS" class="checkbox">
									<xsl:if test="$forced">
										<xsl:attribute name="class">checkbox disabled</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.AllowContactBySMS" />
								</label>
								
								<br/>
							</div>
							
						</xsl:if>
						
					</fieldset>
				
				</xsl:if>
				
				<xsl:if test="ContactDetailQueryInstance/isMutableUserWithAccess = 'true'">
					
					<xsl:choose>
						<xsl:when test="ContactDetailQueryInstance/ContactDetailQuery/fieldUpdate = 'ALWAYS'">
						
							<br/>
							
							<div>
								<xsl:value-of select="$i18n.UpdatingMyUserProfile"/>
							</div>
						
						</xsl:when>
						<xsl:when test="ContactDetailQueryInstance/ContactDetailQuery/fieldUpdate = 'ASK'">
						
							<br/>
						
							<fieldset>
									
								<div class="split">
								
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="id" select="concat($shortQueryID, '_persistUserProfile')" />
										<xsl:with-param name="name" select="concat($shortQueryID, '_persistUserProfile')" />
										<xsl:with-param name="value" select="'true'" />
										<xsl:with-param name="checked" select="ContactDetailQueryInstance/persistUserProfile" />
									</xsl:call-template>
									
									<label for="{$shortQueryID}_persistUserProfile" class="checkbox"><xsl:value-of select="$i18n.UpdateMyUserProfile" /></label>
									
								</div>
								
							</fieldset>
						
						</xsl:when>
				</xsl:choose>
				</xsl:if>
				
			</article>
		
		</div>
		
		<script type="text/javascript">$(document).ready(function(){initContactDetailQuery('<xsl:value-of select="ContactDetailQueryInstance/ContactDetailQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NoContactChannelChoosen']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.ValidationError.NoContactChannelChoosen" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'UnableToUpdateUser']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.ValidationError.UnableToUpdateUser" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiresCitizenIDOnUser']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.ValidationError.RequiresCitizenIDOnUser" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<xsl:variable name="message">
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part1"/>
			<xsl:value-of select="currentLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part2"/>
			<xsl:value-of select="maxLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part3"/>
		</xsl:variable>
		
		<i data-icon-after="!" title="{$message}"></i>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.ValidationError.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.ValidationError.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<i data-icon-after="!" title="{$i18n.ValidationError.InvalidFormat}"></i>
		
	</xsl:template>		
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.ValidationError.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>	
		
</xsl:stylesheet>