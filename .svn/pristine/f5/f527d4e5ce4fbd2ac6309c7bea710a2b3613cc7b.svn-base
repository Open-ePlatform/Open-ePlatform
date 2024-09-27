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
		
		<div class="query organizationdetailquery">
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
						<xsl:with-param name="queryName" select="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/description">
					<span class="italic">
						<xsl:value-of select="OrganizationDetailQueryInstance/OrganizationDetailQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
					
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

				<xsl:if test="OrganizationDetailQueryInstance/address">
				
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
					
				</xsl:if>
					
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
				
				<xsl:if test="OrganizationDetailQueryInstance/mobilePhone">
				
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.MobilePhone" /></strong>
						<xsl:call-template name="printValue">
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone" />
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
					
					
				<div>
					<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
					
					<xsl:if test="OrganizationDetailQueryInstance/email">
						<xsl:value-of select="$i18n.ContactByEmail" /><br/>
					</xsl:if>
					
					<xsl:if test="OrganizationDetailQueryInstance/contactBySMS = 'true'">
						<xsl:value-of select="$i18n.ContactBySMS" /><br/>
					</xsl:if>
				</div>
				
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
				<xsl:text>query organizationdetailquery</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
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
			
				<xsl:variable name="isRequired"><xsl:if test="OrganizationDetailQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">true</xsl:if></xsl:variable>
				<xsl:variable name="requiresAddress"><xsl:if test="$isRequired = 'true' and OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldAddress = 'REQUIRED'">true</xsl:if></xsl:variable>
				<xsl:variable name="requiresEmail"><xsl:if test="$isRequired = 'true' and OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldEmail= 'REQUIRED'">true</xsl:if></xsl:variable>
				<xsl:variable name="requiresMobilePhone">
					<xsl:if test="$isRequired = 'true' and OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldMobilePhone = 'REQUIRED' and OrganizationDetailQueryInstance/OrganizationDetailQuery/requireEmailOrMobile != 'true'">true</xsl:if>
				</xsl:variable>
				<xsl:variable name="requiresPhone"><xsl:if test="$isRequired = 'true' and OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldPhone = 'REQUIRED'">true</xsl:if></xsl:variable>
				<xsl:variable name="contactWaysHidden"><xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldMobilePhone = 'HIDDEN' and OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldEmail = 'HIDDEN'">true</xsl:if></xsl:variable>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="$isRequired = 'true'">
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
					
					<div>
						<xsl:variable name="selectID" select="concat($shortQueryID, '_organization')" />
						
						<p><xsl:value-of select="$i18n.OrganizationDescription" /></p>
						
						<label for="{$selectID}"><xsl:value-of select="$i18n.ChooseOrganizationDescription" /></label>
						
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="$selectID"/>
							<xsl:with-param name="name" select="$selectID"/>
							<xsl:with-param name="valueElementName" select="'organizationID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="element" select="$userOrganizations" />
							<xsl:with-param name="selectedValue" select="OrganizationDetailQueryInstance/organizationID" />
							<xsl:with-param name="addEmptyOption" select="$i18n.ChooseOrganization" />
							<xsl:with-param name="class" select="'organization'" />
						</xsl:call-template>
					
						<a id="{$shortQueryID}_newOrganization" role="button" class="btn btn-green btn-left btn-newOrganization" href="#"><xsl:value-of select="$i18n.NewOrganization" /></a>

						<xsl:apply-templates select="$userOrganizations" mode="hidden-values" />
				
					</div>
					
				</xsl:if>
				
				<div class="form-wrapper">

					<xsl:if test="$userOrganizations and not(OrganizationDetailQueryInstance/organizationNumber) and not(ValidationErrors/validationError)">
						<xsl:attribute name="class">form-wrapper hidden</xsl:attribute>
					</xsl:if>

					<div class="clearboth" />
						
					<div class="split">
					
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_name')" />
						
						<xsl:variable name="fieldValidationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						<xsl:variable name="nameExistsValidationError" select="ValidationErrors/validationError[messageKey = 'NameExists']"/>
						
						<xsl:variable name="class">
							<xsl:if test="$fieldValidationError or $nameExistsValidationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
						
						<label for="{$fieldName}">
							<xsl:if test="$isRequired = 'true'">
								<xsl:attribute name="class">required</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$i18n.Name" />
						</label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Name"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/name" />
							<xsl:with-param name="class" select="$class"/>
							<xsl:with-param name="aria-required"><xsl:if test="$isRequired = 'true'">true</xsl:if></xsl:with-param>
							<xsl:with-param name="autocomplete" select="'organization'"/>
							<xsl:with-param name="aria-describedby">
									<xsl:if test="$fieldValidationError or $nameExistsValidationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error</xsl:text>
									</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="aria-invalid">
								<xsl:if test="$fieldValidationError or $nameExistsValidationError">
									<xsl:text>true</xsl:text>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$fieldValidationError"/>
						<xsl:apply-templates select="$nameExistsValidationError">
							<xsl:with-param name="fieldName" select="$fieldName"/>
						</xsl:apply-templates>
						
					</div>
					
					<div class="split">
						
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_organizationNumber')" />
						
						<xsl:variable name="fieldValidationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						<xsl:variable name="orgNumberExistsValidationError" select="ValidationErrors/validationError[messageKey = 'OrganizationNumberExists']"/>
						
						<xsl:variable name="class">
							<xsl:if test="$fieldValidationError or $orgNumberExistsValidationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
						
						<label for="{$fieldName}">
							<xsl:if test="$isRequired = 'true'">
								<xsl:attribute name="class">required</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$i18n.OrganizationNumberWithFormat" />
						</label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.OrganizationNumber"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="value" select="OrganizationDetailQueryInstance/organizationNumber" />
							<xsl:with-param name="class" select="$class"/>
							<xsl:with-param name="aria-required"><xsl:if test="$isRequired = 'true'">true</xsl:if></xsl:with-param>
							<xsl:with-param name="aria-describedby">
									<xsl:if test="$fieldValidationError or $orgNumberExistsValidationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error</xsl:text>
									</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="aria-invalid">
								<xsl:if test="$fieldValidationError or $orgNumberExistsValidationError">
									<xsl:text>true</xsl:text>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$fieldValidationError"/>
						<xsl:apply-templates select="$orgNumberExistsValidationError">
							<xsl:with-param name="fieldName" select="$fieldName"/>
						</xsl:apply-templates>
						
					</div>
					
					<xsl:if test="not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldAddress = 'HIDDEN')">
			
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_address')" />
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
							<xsl:variable name="class">
								<xsl:if test="$validationError">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<label for="{$fieldName}">
								<xsl:if test="$requiresAddress = 'true'">
									<xsl:attribute name="class">required</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$i18n.Address" />
							</label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Address"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/address"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="aria-required"><xsl:if test="$requiresAddress = 'true'">true</xsl:if></xsl:with-param>
								<xsl:with-param name="autocomplete" select="'street-address'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="$validationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error</xsl:text>
									</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="aria-invalid">
									<xsl:if test="$validationError">
										<xsl:text>true</xsl:text>
									</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
							
							<div class="clearfix"/>
							
							<xsl:apply-templates select="$validationError"/>
							
						</div>
						
						<div class="split">
							
							<div class="left">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_zipcode')" />
								
								<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
								<xsl:variable name="class">
									<xsl:if test="$validationError">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<xsl:variable name="zipCodeInfoText" select="'zipCodeInfoText'"/>
								
								<label for="{$fieldName}" class="floatleft">
									<xsl:if test="$requiresAddress = 'true'">
										<xsl:attribute name="class">floatleft required</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.ZipCode" />
								</label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.ZipCode"/>
									<xsl:with-param name="size" select="15"/>
									<xsl:with-param name="class" select="concat('floatleft clearboth ', $class)"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/zipCode"/>
									<xsl:with-param name="aria-required"><xsl:if test="$requiresAddress = 'true'">true</xsl:if></xsl:with-param>
									<xsl:with-param name="autocomplete" select="'postal-code'"/>
									<xsl:with-param name="aria-describedby">										
										<xsl:value-of select="$zipCodeInfoText"/>
										<xsl:if test="$validationError">
											<xsl:text> </xsl:text>
											<xsl:value-of select="$fieldName"/>
											<xsl:text>-error </xsl:text>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="aria-invalid">
										<xsl:if test="$validationError">
											<xsl:text>true</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
								
								<div class="clearfix"/>
								
								<div id="{$zipCodeInfoText}" class="input-info-text">
									<xsl:value-of select="$i18n.Example.ZipCodeFormat"/>
								</div>
								
								<xsl:apply-templates select="$validationError"/>
							
							</div>
							
							<div class="right">
							
								<xsl:variable name="fieldName" select="concat($shortQueryID, '_postaladdress')" />
							
								<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
								<xsl:variable name="class">
									<xsl:if test="$validationError">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<label for="{$fieldName}" class="floatleft">
									<xsl:if test="$requiresAddress = 'true'">
										<xsl:attribute name="class">floatleft required</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.PostalAddress" />
								</label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="name" select="concat($shortQueryID, '_postaladdress')" />
									<xsl:with-param name="title" select="$i18n.PostalAddress"/>
									<xsl:with-param name="size" select="28"/>
									<xsl:with-param name="class" select="concat('floatleft clearboth ', $class)"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/postalAddress"/>
									<xsl:with-param name="aria-required"><xsl:if test="$requiresAddress = 'true'">true</xsl:if></xsl:with-param>
									<xsl:with-param name="autocomplete" select="'address-level1'"/>
									<xsl:with-param name="aria-describedby">
										<xsl:if test="$validationError">
											<xsl:value-of select="$fieldName"/>
											<xsl:text>-error</xsl:text>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="aria-invalid">
										<xsl:if test="$validationError">
											<xsl:text>true</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
								
								<div class="clearfix"/>
								
								<xsl:apply-templates select="$validationError"/>
								
							</div>
							
						</div>

					</xsl:if>
					
					<div class="clearfix"/>
					
					<h3 class="marginbottom"><xsl:value-of select="$i18n.ContactPerson" /></h3>
					
					<div class="split">
							
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_firstname')" />
						
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
					
						<xsl:variable name="class">
							<xsl:if test="$validationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
					
						<label for="{$fieldName}" class="floatleft full">
							<xsl:if test="$requiresAddress = 'true'">
								<xsl:attribute name="class">floatleft full required</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$i18n.Firstname" />
						</label>
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
							<xsl:with-param name="aria-required"><xsl:if test="$isRequired = 'true'">true</xsl:if></xsl:with-param>
							<xsl:with-param name="autocomplete" select="'given-name'"/>
							<xsl:with-param name="aria-describedby">
								<xsl:if test="$validationError">
									<xsl:value-of select="$fieldName"/>
									<xsl:text>-error</xsl:text>
								</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="aria-invalid">
								<xsl:if test="$validationError">
									<xsl:text>true</xsl:text>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$validationError"/>
						
					</div>
					
					<div class="split">
							
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_lastname')" />
						
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
					
						<xsl:variable name="class">
							<xsl:if test="$validationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
					
						<label for="{$fieldName}" class="floatleft full">
							<xsl:if test="$requiresAddress = 'true'">
								<xsl:attribute name="class">floatleft full required</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$i18n.Lastname" />
						</label>
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
							<xsl:with-param name="aria-required"><xsl:if test="$isRequired = 'true'">true</xsl:if></xsl:with-param>
							<xsl:with-param name="autocomplete" select="'family-name'"/>
							<xsl:with-param name="aria-describedby">
								<xsl:if test="$validationError">
									<xsl:value-of select="$fieldName"/>
									<xsl:text>-error</xsl:text>
								</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="aria-invalid">
								<xsl:if test="$validationError">
									<xsl:text>true</xsl:text>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$validationError"/>
						
					</div>
					
					<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/requireEmailOrMobile = 'true'
						and not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldMobilePhone = 'REQUIRED')
						and not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldEmail = 'REQUIRED')">
						<p id="requireEmailOrMobileInfo" class="floatleft full"><xsl:value-of select="$i18n.InfoRequireEmailOrMobile"/></p>
					</xsl:if>
					
					<xsl:if test="not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldEmail = 'HIDDEN')">
			
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_email')" />
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
							<xsl:variable name="class">
								<xsl:if test="$validationError">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<xsl:variable name="emailInfoText" select="'emailInfoText'"/>
						
							<label for="{$fieldName}" class="floatleft full">
								<xsl:if test="$requiresEmail = 'true'">
									<xsl:attribute name="class">floatleft full required</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$i18n.Email" />
							</label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Email"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/email"/>
								<xsl:with-param name="autocomplete" select="'email'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/requireEmailOrMobile = 'true'">
										<xsl:text>requireEmailOrMobileInfo </xsl:text>
									</xsl:if>
									
									<xsl:value-of select="$emailInfoText"/>
								
									<xsl:if test="$validationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error</xsl:text>
									</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="aria-invalid">
									<xsl:if test="$validationError">
										<xsl:text>true</xsl:text>
									</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
							
							<div class="clearfix"/>
							
							<div id="{$emailInfoText}" class="input-info-text">
								<xsl:value-of select="$i18n.Example.EmailFormat"/>
							</div>
							
							<xsl:apply-templates select="$validationError"/>
							
						</div>
						
						<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/requireContactInfoVerification = 'true'">
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_confirmEmail')" />
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
							<xsl:variable name="classConfirmEmail">
								<xsl:text>odd</xsl:text>
								<xsl:if test="$validationError">
									<xsl:text> invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<xsl:variable name="confirmEmailInfoText" select="'emailInfoText'"/>
						
							<div class="split {$classConfirmEmail}">	
								<label for="{$fieldName}" class="floatleft full">
									<xsl:if test="$requiresEmail = 'true'">
										<xsl:attribute name="class">floatleft full required</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.Confirmation"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$i18n.email"/>
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.Email"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="class" select="$classConfirmEmail"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/email"/>
									<xsl:with-param name="aria-describedby">
										<xsl:if test="$validationError">
											<xsl:value-of select="$fieldName"/>
											<xsl:text>-error</xsl:text>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="aria-invalid">
										<xsl:if test="$validationError">
											<xsl:text>true</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							
							<div class="clearfix"/>
							
							<div id="{$confirmEmailInfoText}" class="input-info-text">
								<xsl:value-of select="$i18n.Example.EmailFormat"/>
							</div>
							
							<xsl:apply-templates select="$validationError"/>
							
						</div>
						
						</xsl:if>
					</xsl:if>
					
					<xsl:if test="not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldMobilePhone = 'HIDDEN')">
						
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_mobilephone')" />
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
							<xsl:variable name="class">
								<xsl:if test="$validationError">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<xsl:variable name="mobileInfoText" select="'mobileInfoText'"/>
							
							<label for="{$fieldName}" class="floatleft full">
								<xsl:if test="$requiresMobilePhone = 'true'">
									<xsl:attribute name="class">floatleft full required</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$i18n.MobilePhone" />
							</label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.MobilePhone"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone"/>
								<xsl:with-param name="autocomplete" select="'tel-national'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/requireEmailOrMobile = 'true'">
										<xsl:text>requireEmailOrMobileInfo </xsl:text>
									</xsl:if>
									
									<xsl:value-of select="$mobileInfoText"/>
									
									<xsl:if test="$validationError">
										<xsl:text> </xsl:text>
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error</xsl:text>
									</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="aria-invalid">
									<xsl:if test="$validationError">
										<xsl:text>true</xsl:text>
									</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
							
							<div class="clearfix"/>
							
							<div id="{$mobileInfoText}" class="input-info-text">
								<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
							</div>
							
							<xsl:apply-templates select="$validationError"/>
							
						</div>
													
						<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/requireContactInfoVerification = 'true'">

							<xsl:variable name="fieldName" select="concat($shortQueryID, '_confirmMobilephone')" />
								
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
							
							<xsl:variable name="classConfirmMobilePhone">
								<xsl:text>odd</xsl:text>
								<xsl:if test="$validationError">
									<xsl:text> invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<xsl:variable name="confirmMobileInfoText" select="'mobileInfoText'"/>
							
							<div class="split {$classConfirmMobilePhone}">
								<label for="{$fieldName}">
									<xsl:if test="$requiresMobilePhone = 'true'">
										<xsl:attribute name="class">required</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.Confirmation"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$i18n.mobilePhone"/>
								</label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="title" select="$i18n.MobilePhone"/>
									<xsl:with-param name="size" select="50"/>
									<xsl:with-param name="class" select="$classConfirmMobilePhone"/>
									<xsl:with-param name="value" select="OrganizationDetailQueryInstance/mobilePhone"/>
									<xsl:with-param name="aria-describedby">										
										<xsl:if test="$validationError">
											<xsl:text> </xsl:text>
											<xsl:value-of select="$fieldName"/>
											<xsl:text>-error</xsl:text>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="aria-invalid">
										<xsl:if test="$validationError">
											<xsl:text>true</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
								
								<div class="clearfix"/>
								
								<div id="{$confirmMobileInfoText}" class="input-info-text">
									<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
								</div>
								
								<xsl:apply-templates select="$validationError"/>
							</div>
						</xsl:if>
						
					</xsl:if>
					
					<xsl:if test="not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldPhone = 'HIDDEN')">
					
						<div class="split">
							
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_phone')" />
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
							<xsl:variable name="class">
								<xsl:if test="$validationError">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<xsl:variable name="phoneInfoText" select="'phoneInfoText'"/>
							
							<label for="{$fieldName}" class="floatleft full">
								<xsl:if test="$requiresPhone = 'true'">
									<xsl:attribute name="class">floatleft full required</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$i18n.Phone" />
							</label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.Phone"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="class" select="$class"/>
								<xsl:with-param name="value" select="OrganizationDetailQueryInstance/phone"/>
								<xsl:with-param name="autocomplete" select="'tel-national'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="$validationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error </xsl:text>
									</xsl:if>
									
									<xsl:value-of select="$phoneInfoText"/>
								</xsl:with-param>
								<xsl:with-param name="aria-invalid">
									<xsl:if test="$validationError">
										<xsl:text>true</xsl:text>
									</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
							
							<div class="clearfix"/>
							
							<div id="{$phoneInfoText}" class="input-info-text">
								<xsl:value-of select="$i18n.Example.PhoneFormat"/>
							</div>
							
							<xsl:apply-templates select="$validationError"/>
							
						</div>
					</xsl:if>
										
					<xsl:if test="$contactWaysHidden != 'true' and OrganizationDetailQueryInstance/OrganizationDetailQuery/hideNotificationChannelSettings = 'false'">
									
						<xsl:variable name="fieldsetID" select="concat($shortQueryID, '_notification-channel-settings')" />
						
						<fieldset class="notification-channel-settings" id="{$fieldsetID}">
							
							<legend for="{$fieldsetID}">
								<strong class="block"><xsl:value-of select="$i18n.ChooseContactChannels" /></strong>
							</legend>
							
							<xsl:variable name="validationError" select="ValidationErrors/validationError[messageKey = 'SMSNotificationNotChosen']"/>
							
							<xsl:variable name="class">
								<xsl:if test="$validationError">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
						
							<xsl:if test="$validationError">
								<xsl:attribute name="class">
									<xsl:value-of select="$class"/>
									<xsl:text> split</xsl:text>
								</xsl:attribute>
								<xsl:attribute name="aria-describedby">
									<xsl:value-of select="$fieldsetID"/>
									<xsl:text>-error </xsl:text>
								</xsl:attribute>
							</xsl:if>	
							
							<xsl:apply-templates select="$validationError">
								<xsl:with-param name="fieldName" select="$fieldsetID"/>
							</xsl:apply-templates>
							
							<xsl:if test="not(OrganizationDetailQueryInstance/OrganizationDetailQuery/fieldEmail = 'HIDDEN')">
									
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
									
							</xsl:if>
							
								<xsl:if test="OrganizationDetailQueryInstance/OrganizationDetailQuery/allowSMS = 'true'">
									<xsl:call-template name="createCheckbox">
										<xsl:with-param name="id" select="concat($shortQueryID, '_contactBySMS')" />
										<xsl:with-param name="name" select="concat($shortQueryID, '_contactBySMS')" />
										<xsl:with-param name="value" select="'true'" />
										<xsl:with-param name="checked" select="OrganizationDetailQueryInstance/contactBySMS"/>
									</xsl:call-template>
									<label for="{concat($shortQueryID, '_contactBySMS')}" class="checkbox"><xsl:value-of select="$i18n.AllowContactBySMS" /></label><br/>
								</xsl:if>
								
						</fieldset>
					
					</xsl:if>	
					
				
					<xsl:if test="OrganizationDetailQueryInstance/isUserOrganizationsMutableWithAccess = 'true'">
						
						<div class="clearfix"/>
						
						<div>
						
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="concat($shortQueryID, '_persistOrganization')" />
								<xsl:with-param name="name" select="concat($shortQueryID, '_persistOrganization')" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="OrganizationDetailQueryInstance/persistOrganization" />
							</xsl:call-template>
							
							<label for="{concat($shortQueryID, '_persistOrganization')}" class="checkbox"><xsl:value-of select="$i18n.AddToMyOrganizations" /></label>
							
						</div>

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
		
		<script type="text/javascript">$(function(){initOrganizationDetailQuery('<xsl:value-of select="OrganizationDetailQueryInstance/OrganizationDetailQuery/queryID" />');});</script>
		
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
		
		<span class="d-flex justify-content-center align-items-center">
			<i data-icon-before="!" aria-hidden="true"/>
			<strong>
				<xsl:value-of select="$i18n.ValidationError.NoneOfOptionalRequiredFieldsEntered.part1" />
				<a href="#{./displayName}email"><xsl:value-of select="$i18n.email" /></a>
				<xsl:value-of select="$i18n.ValidationError.NoneOfOptionalRequiredFieldsEntered.part2" />
				<a href="#{./displayName}mobilephone"><xsl:value-of select="$i18n.mobilePhone" /></a>
			</strong>							
		</span>
	
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'SMSNotificationNotChosen']">
			
		<div id="{displayName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
				<xsl:value-of select="$i18n.ValidationError.NoContactChannelChosen" />
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'UnableToPersistOrganization']">
	
		<span class="d-flex justify-content-center align-items-center">
			<i data-icon-before="!" aria-hidden="true"/>
			<strong>
				<xsl:value-of select="$i18n.UnableToPersistOrganization" />
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
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$message"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NameExists']">
	
		<xsl:param name="fieldName" select="null"/>
		
		<div id="{$fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.NameExists"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'OrganizationNumberExists']">
	
		<xsl:param name="fieldName" select="null"/>
		
		<div id="{$fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
		 	<xsl:value-of select="$i18n.OrganizationNumberExists"/>
		 </div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredField']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.RequiredField"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.RequiredField"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'EmailVerificationMismatch']">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.EmailVerificationMismatch"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'MobilePhoneVerificationMismatch']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.MobilePhoneVerificationMismatch"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:choose>
				<xsl:when test="contains(fieldName, '_organizationNumber')">
					<xsl:value-of select="$i18n.ValidationError.InvalidOrgNumber"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, '_email') or contains(fieldName, '_confirmEmail')">
					<xsl:value-of select="$i18n.ValidationError.InvalidEmail"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, '_mobilephone') or contains(fieldName, '_confirmMobilephone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidMobilePhoneNumber"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, '_phone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidPhoneNumber"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.ValidationError.InvalidFormat"/>
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<span class="d-flex justify-content-center align-items-center">
			<i data-icon-before="!" aria-hidden="true"/>
			<strong>
				<xsl:value-of select="$i18n.ValidationError.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>
		
</xsl:stylesheet>