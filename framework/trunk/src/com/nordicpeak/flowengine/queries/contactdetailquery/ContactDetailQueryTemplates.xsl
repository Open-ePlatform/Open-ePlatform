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
				
				<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/showSocialSecurityNumberField = 'true'">

					<fieldset>

						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.SocialSecurityNumber" /></strong>
							<xsl:value-of select="ContactDetailQueryInstance/socialSecurityNumber" />						
						</div>				
				
					</fieldset>
				
				</xsl:if>				
				
				<fieldset>
					
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.FirstnameAndLastname" /></strong>
						<xsl:value-of select="ContactDetailQueryInstance/firstname" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="ContactDetailQueryInstance/lastname" />
					</div>
					
					<div class="split odd"></div>
					
				</fieldset>
				
				<xsl:if test="ContactDetailQueryInstance/address">
					
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
					
					<xsl:if test="ContactDetailQueryInstance/phone">
						
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Phone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/phone" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
					<xsl:if test="ContactDetailQueryInstance/email">
			
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.Email" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/email" />
							</xsl:call-template>
						</div>
					
					</xsl:if>
					
					<xsl:if test="ContactDetailQueryInstance/mobilePhone">
						<div class="split">
							<strong class="block"><xsl:value-of select="$i18n.MobilePhone" /></strong>
							<xsl:call-template name="printValue">
								<xsl:with-param name="value" select="ContactDetailQueryInstance/mobilePhone" />
							</xsl:call-template>
						</div>
					</xsl:if>
					
				</fieldset>
				
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
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="ContactDetailQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
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
					
					<xsl:if test="ValidationErrors/validationError[messageKey = 'UnableToUpdateUser']">
						<div class="info-box error">
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'UnableToUpdateUser']"/>
							<div class="marker"></div>
						</div>
					</xsl:if>
					
					<xsl:if test="not(ValidationErrors/validationError[messageKey = 'NoContactChannelChoosen'] or ValidationErrors/validationError[messageKey = 'UnableToUpdateUser'])">
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
				
				<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/showSocialSecurityNumberField = 'true'">

					<fieldset>

						<div class="split">
	
							<xsl:variable name="fieldName" select="concat($shortQueryID, '_socialSecurityNumber')" />
						
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:attribute name="class">
									<xsl:text>split invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>
						
							<label for="{$fieldName}" class="required"><xsl:value-of select="$i18n.SocialSecurityNumberWithFormat" /></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName" />
								<xsl:with-param name="name" select="$fieldName" />
								<xsl:with-param name="title" select="$i18n.SocialSecurityNumberWithFormat"/>
								<xsl:with-param name="size" select="50"/>
								<xsl:with-param name="disabled">
									<xsl:if test="/Document/user">disabled</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="value">
									<xsl:choose>
										<xsl:when test="/Document/user"><xsl:value-of select="/Document/user/SocialSecurityNumber" /></xsl:when>
										<xsl:otherwise><xsl:value-of select="ContactDetailQueryInstance/socialSecurityNumber" /></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							
							<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
						</div>				
				
					</fieldset>
				
				</xsl:if>				
				
				<fieldset>
					
					<div class="split">
					
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_firstname')" />
					
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>
					
						<label for="{$fieldName}" class="required"><xsl:value-of select="$i18n.Firstname" /></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Firstname"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="disabled">
								<xsl:if test="/Document/user">disabled</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="value">
								<xsl:choose>
									<xsl:when test="/Document/user"><xsl:value-of select="/Document/user/firstname" /></xsl:when>
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
					
						<label for="{$fieldName}" class="required"><xsl:value-of select="$i18n.Lastname" /></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Lastname"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="disabled">
								<xsl:if test="/Document/user">disabled</xsl:if>
							</xsl:with-param>
							<xsl:with-param name="value">
								<xsl:choose>
									<xsl:when test="/Document/user"><xsl:value-of select="/Document/user/lastname" /></xsl:when>
									<xsl:otherwise><xsl:value-of select="ContactDetailQueryInstance/lastname" /></xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
						</xsl:call-template>
						
						<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
					</div>
					
				</fieldset>
				
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
							<xsl:if test="$requireAddress"><xsl:attribute name="class">required</xsl:attribute></xsl:if>
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
							
							<label for="{$fieldName}" class="floatleft">
								<xsl:if test="$requireAddress"><xsl:attribute name="class">floatleft required</xsl:attribute></xsl:if>
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
						
							<label for="{$fieldName}" class="floatleft">
								<xsl:if test="$requireAddress"><xsl:attribute name="class">floatleft required</xsl:attribute></xsl:if>
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
				
				<fieldset>
					
					<div class="split">
					
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_phone')" />
					
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>
					
						<label for="concat($shortQueryID, '_phone')" class="floatleft full">
							<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/requirePhone = 'true'"><xsl:attribute name="class">floatleft full required</xsl:attribute></xsl:if>
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
					
					<div class="split">
						
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_email')" />
					
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName] or ValidationErrors/validationError[messageKey = 'EmailAlreadyTaken']">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>	
					
						<label for="{$fieldName}" class="floatleft full">
							<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/requireEmail = 'true'"><xsl:attribute name="class">floatleft full required</xsl:attribute></xsl:if>
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
					
					<div class="split">
						
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_mobilephone')" />
					
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>
						
						<label for="{$fieldName}" class="floatleft full">
							<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/requireMobilePhone = 'true'"><xsl:attribute name="class">floatleft full required</xsl:attribute></xsl:if>
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
					
				</fieldset>
				
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
								<xsl:if test="ContactDetailQueryInstance/email != ''">true</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<label for="{concat($shortQueryID, '_contactByEmail')}" class="checkbox disabled"><xsl:value-of select="$i18n.AllowContactByEmail" /></label><br/>
						
					</div>
					
					<div>
						
						<xsl:if test="ContactDetailQueryInstance/ContactDetailQuery/allowSMS = 'true'">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="concat($shortQueryID, '_contactBySMS')" />
								<xsl:with-param name="name" select="concat($shortQueryID, '_contactBySMS')" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="disabled" select="'disabled'" />
								<xsl:with-param name="checked" select="ContactDetailQueryInstance/contactBySMS"/>
							</xsl:call-template>
							<label for="{concat($shortQueryID, '_contactBySMS')}" class="checkbox"><xsl:value-of select="$i18n.AllowContactBySMS" /></label><br/>
						</xsl:if>
						
					</div>
					
				</fieldset>
				
				<xsl:if test="ContactDetailQueryInstance/isMutableUserWithAccess = 'true' and ContactDetailQueryInstance/ContactDetailQuery/disableProfileUpdate = 'false'">
				
					<br/>
				
					<fieldset>
							
						<div class="split">
						
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="concat($shortQueryID, '_persistUserProfile')" />
								<xsl:with-param name="name" select="concat($shortQueryID, '_persistUserProfile')" />
								<xsl:with-param name="value" select="'true'" />
								<xsl:with-param name="checked" select="ContactDetailQueryInstance/persistUserProfile" />
							</xsl:call-template>
							
							<label for="{concat($shortQueryID, '_persistUserProfile')}" class="checkbox"><xsl:value-of select="$i18n.UpdateMyUserProfile" /></label>
							
						</div>
						
					</fieldset>
				
				</xsl:if>
				
			</article>
		
		</div>
		
		<script type="text/javascript">$(document).ready(function(){initContactDetailQuery('<xsl:value-of select="ContactDetailQueryInstance/ContactDetailQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NoContactChannelChoosen']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.NoContactChannelChoosen" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'UnableToUpdateUser']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToUpdateUser" />
			</strong>
		</span>
	
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<xsl:variable name="message">
			<xsl:value-of select="$i18n.TooLongFieldContent.part1"/>
			<xsl:value-of select="currentLength"/>
			<xsl:value-of select="$i18n.TooLongFieldContent.part2"/>
			<xsl:value-of select="maxLength"/>
			<xsl:value-of select="$i18n.TooLongFieldContent.part3"/>
		</xsl:variable>
		
		<i data-icon-after="!" title="{$message}"></i>
		
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