<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/organizationdetailquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/organizationdetailquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="OrganizationDetailQueryInstance/OrganizationDetailQuery/queryID" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/description">
					<span class="italic">
						<xsl:value-of select="OrganizationDetailQueryInstance/OrganizationDetailQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<fieldset>
					
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.Name" /></strong>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/name" />
						</xsl:call-template>
					</div>
					
					<div class="split odd">
						<strong class="block"><xsl:value-of select="$i18n.OrganizationNumber" /></strong>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/organizationNumber" />
						</xsl:call-template>
					</div>
					
				</fieldset>
				
				<xsl:if test="OrganizationDetailQueryInstance/address">
					
					<fieldset>
					
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Address" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/address" />
							</xsl:call-template>
						</div>
						
						<div class="split odd">
							<strong class="block"><xsl:value-of select="$i18n.ZipCodeAndPostalAddress" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/zipCode" />
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/postalAddress" />
							</xsl:call-template>
						</div>
						
					</fieldset>
					
				</xsl:if>
				
				<fieldset>
					
					<h3 class="marginbottom"><xsl:value-of select="$i18n.ContactPerson" /></h3>
					
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.FirstnameAndLastname" /></strong>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/firstname" />
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/lastname" />
						</xsl:call-template>
					</div>
					
					<xsl:if test="OrganizationDetailQueryInstance/email">
			
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Email" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/email" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
					<xsl:if test="OrganizationDetailQueryInstance/phone">
						
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Phone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/phone" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
					<xsl:if test="OrganizationDetailQueryInstance/mobilePhone">
					
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.MobilePhone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
				</fieldset>
				
				<fieldset>
					
					<div>
						<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
						
						<xsl:if test="OrganizationDetailQueryInstance/email">
							<xsl:value-of select="$i18n.ContactByEmail" /><br/>
						</xsl:if>
						
						<xsl:if test="OrganizationDetailQueryInstance/contactBySMS = 'true'">
							<xsl:value-of select="$i18n.ContactBySMS" /><br/>
						</xsl:if>
					</div>
					
				</fieldset>
				
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
	
		<xsl:variable name="shortQueryID" select="concat('q', OrganizationDetailQueryInstance/OrganizationDetailQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', OrganizationDetailQueryInstance/OrganizationDetailQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
		
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
				
					<xsl:if test="ValidationErrors/validationError[messageKey = 'NoContactChannelChoosen']">
						<div class="info-box error">
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'NoContactChannelChoosen']"/>
							<div class="marker"></div>
						</div>
					</xsl:if>
					
					<xsl:if test="ValidationErrors/validationError[messageKey = 'UnableToPersistOrganization']">
						<div class="info-box error">
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'UnableToPersistOrganization']"/>
							<div class="marker"></div>
						</div>
					</xsl:if>
					
					<xsl:if test="not(ValidationErrors/validationError[messageKey = 'NoContactChannelChoosen'] or ValidationErrors/validationError[messageKey = 'UnableToPersistOrganization'])">
						<div class="info-box error">
							<span/>
							<div class="marker"></div>
						</div>
					</xsl:if>
					
				</div>
			</xsl:if>
			
			<article>
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/helpText">
						<xsl:apply-templates select="OrganizationDetailQueryInstance/OrganizationDetailQuery/helpText" />
					</xsl:if>
				
				</div>
				
				<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="OrganizationDetailQueryInstance/OrganizationDetailQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<xsl:variable name="userOrganizations" select="/Document/UserOrganizations/Organization" />
				
				<xsl:if test="$userOrganizations">
					
					<fieldset>
						
						<div>
					
							<label class="marginbottom full"><xsl:value-of select="$i18n.OrganizationDescription" /></label>
							
							<xsl:call-template name="createDropdown">
								<xsl:with-param name="id" select="concat($shortQueryID, '_organization')"/>
								<xsl:with-param name="name" select="concat($shortQueryID, '_organization')"/>
								<xsl:with-param name="valueElementName" select="'organizationID'" />
								<xsl:with-param name="labelElementName" select="'name'" />
								<xsl:with-param name="element" select="$userOrganizations" />
								<xsl:with-param name="selectedValue" select="OrganizationDetailQueryInstance/organizationID" />
								<xsl:with-param name="addEmptyOption" select="$i18n.ChooseOrganization" />
								<xsl:with-param name="class" select="'organization'" />
							</xsl:call-template>
						
							<a id="{$shortQueryID}_newOrganization" class="btn btn-green btn-left btn-newOrganization" href="#"><xsl:value-of select="$i18n.NewOrganization" /></a>

							<xsl:apply-templates select="$userOrganizations" mode="hidden-values" />
					
						</div>
					
					</fieldset>
					
				</xsl:if>
				
				<div class="form-wrapper">
				
					<xsl:if test="$userOrganizations and not(OrganizationDetailQueryInstance/organizationID) and not(ValidationErrors/validationError)">
						<xsl:attribute name="class">form-wrapper hidden</xsl:attribute>
					</xsl:if>
				
					<fieldset class="clearboth">
						
						<div class="split">
						
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_name')" />
							
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName] or ValidationErrors/validationError[messageKey = 'NameExists']">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}"><xsl:value-of select="$i18n.Name" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Name"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/name" />
								<xsl:with-param name="class" select="$class"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'NameExists']"/>
							
						</div>
						
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_organizationNumber')" />
							
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName] or ValidationErrors/validationError[messageKey = 'OrganizationNumberExists']">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}"><xsl:value-of select="$i18n.OrganizationNumberWithFormat" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.OrganizationNumber"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/organizationNumber" />
								<xsl:with-param name="class" select="$class"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'OrganizationNumberExists']"/>
							
						</div>
						
					</fieldset>
						
					<fieldset>
					
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_address')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}"><xsl:value-of select="$i18n.Address" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Address"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/address"/>
								<xsl:with-param name="class" select="$class"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
						<div class="split">
							
							<div class="left">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_zipcode')" />
							
								<xsl:variable name="class">
									<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<label for="{$fieldName}" class="floatleft"><xsl:value-of select="$i18n.ZipCode" /></label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.ZipCode"/>
									<xsl:with-param name="size" select="15"/>
									<xsl:with-param name="class" select="concat('floatleft clearboth ', $class)"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/zipCode"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
							</div>
							
							<div class="right">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_postaladdress')" />
							
								<xsl:variable name="class">
									<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
							
								<label for="{$fieldName}" class="floatleft"><xsl:value-of select="$i18n.PostalAddress" /></label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="title" select="$i18n.PostalAddress"/>
									<xsl:with-param name="size" select="28"/>
									<xsl:with-param name="class" select="concat('floatleft clearboth ', $class)"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/postalAddress"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
								
							</div>
							
						</div>
						
					</fieldset>
						
					
					<fieldset>
						
						<h3 class="marginbottom"><xsl:value-of select="$i18n.ContactPerson" /></h3>
						
						<div class="split">
								
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_firstname')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
						
							<label for="{$fieldName}" class="floatleft full"><xsl:value-of select="$i18n.Firstname" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Firstname"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/user">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/user"><xsl:value-of select="/Document/user/firstname" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="OrganizationDetailQueryInstance/firstname" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
						<div class="split">
								
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_lastname')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
						
							<label for="{$fieldName}" class="floatleft full"><xsl:value-of select="$i18n.Lastname" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Lastname"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/user">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/user"><xsl:value-of select="/Document/user/lastname" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="OrganizationDetailQueryInstance/lastname" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
				
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_email')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
						
							<label for="{$fieldName}" class="floatleft full"><xsl:value-of select="$i18n.Email" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Email"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/email"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
							
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_phone')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}" class="floatleft full"><xsl:value-of select="$i18n.Phone" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Phone"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/phone"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_mobilephone')" />
						
							<xsl:variable name="class">
								<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}" class="floatleft full"><xsl:value-of select="$i18n.MobilePhone" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.MobilePhone"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone"/>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
						</div>
						
					</fieldset>
	
					<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/hideNotificationChannelSettings = 'false'">
					
						<fieldset>
						
							<div>
								<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
							</div>
							
							<div>
								
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="concat($shortQueryID, '_contactByEmail')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_contactByEmail')" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="disabled" select="'disabled'" />
									<xsl:with-param name="checked">
										<xsl:if test="OrganizationDetailQueryInstance/email != ''">true</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
								<label for="{concat($shortQueryID, '_contactByEmail')}" class="checkbox disabled"><xsl:value-of select="$i18n.AllowContactByEmail" /></label><br/>
								
							</div>
							
							<div>
								
								<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/allowSMS = 'true'">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="id" select="concat($shortQueryID, '_contactBySMS')" />
										<xsl:with-param name="name" select="concat($shortQueryID, '_contactBySMS')" />
										<xsl:with-param name="value" select="'true'" />
										<xsl:with-param name="disabled" select="'disabled'" />
										<xsl:with-param name="checked" select="OrganizationDetailQueryInstance/contactBySMS"/>
									</xsl:call-template>
									<label for="{concat($shortQueryID, '_contactBySMS')}" class="checkbox"><xsl:value-of select="$i18n.AllowContactBySMS" /></label><br/>
								</xsl:if>
								
							</div>
							
						</fieldset>
					
					</xsl:if>	
					
				
					<xsl:if test="OrganizationDetailQueryInstance/isUserOrganizationsMutableWithAccess = 'true'">
						<fieldset>
							
							<div class="split">
							
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="concat($shortQueryID, '_persistOrganization')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_persistOrganization')" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="checked" select="OrganizationDetailQueryInstance/persistOrganization" />
								</xsl:call-template>
								
								<label for="{concat($shortQueryID, '_persistOrganization')}" class="checkbox"><xsl:value-of select="$i18n.AddToMyOrganizations" /></label>
								
							</div>
							
						</fieldset>
					</xsl:if>
				
				</div>
				
			</article>
		
		</div>
		
		<script type="text/javascript">
			var organizationDetailQueryi18n = {
				"AddToMyOrganizations": '<xsl:value-of select="$i18n.AddToMyOrganizations" />',
				"UpdateToMyOrganizations": '<xsl:value-of select="$i18n.UpdateToMyOrganizations" />'
			};
		</script>
		
		<script type="text/javascript">$(document).ready(function(){initOrganizationDetailQuery('<xsl:value-of select="OrganizationDetailQueryInstance/OrganizationDetailQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="Organization" mode="hidden-values">
		
		<xsl:variable name="id" select="concat('organization', organizationID)" />
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_name')" />
			<xsl:with-param name="value" select="name" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_organizationNumber')" />
			<xsl:with-param name="value" select="organizationNumber" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_address')" />
			<xsl:with-param name="value" select="address" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_zipcode')" />
			<xsl:with-param name="value" select="zipCode" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_postaladdress')" />
			<xsl:with-param name="value" select="postalAddress" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_firstname')" />
			<xsl:with-param name="value" select="firstname" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_lastname')" />
			<xsl:with-param name="value" select="lastname" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_mobilephone')" />
			<xsl:with-param name="value" select="mobilePhone" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_email')" />
			<xsl:with-param name="value" select="email" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_phone')" />
			<xsl:with-param name="value" select="phone" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_contactByLetter')" />
			<xsl:with-param name="value" select="contactByLetter" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_contactBySMS')" />
			<xsl:with-param name="value" select="contactBySMS" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_contactByEmail')" />
			<xsl:with-param name="value" select="contactByEmail" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat($id, '_contactByPhone')" />
			<xsl:with-param name="value" select="contactByPhone" />
			<xsl:with-param name="disabled" select="true()" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NoContactChannelChoosen']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.NoContactChannelChoosen" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'UnableToPersistOrganization']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToPersistOrganization" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<i data-icon-after="!">
			<xsl:attribute name="title">
				<xsl:value-of select="$i18n.TooLongFieldContent.part1"/>
				<xsl:value-of select="currentLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part2"/>
				<xsl:value-of select="maxLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part3"/>
			</xsl:attribute>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NameExists']">
	
		<i data-icon-after="!" title="{$i18n.NameExists}"></i>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'OrganizationNumberExists']">
	
		<i data-icon-after="!" title="{$i18n.OrganizationNumberExists}"></i>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<i data-icon-after="!" title="{$i18n.InvalidFormat}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>
		
</xsl:stylesheet>