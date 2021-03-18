<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/contactdetails.js
	</xsl:variable>

	<xsl:template match="Document">	
			
			<xsl:apply-templates select="UpdateUser"/>
			<xsl:apply-templates select="UserNotFound"/>
			
	</xsl:template>
	
	<xsl:template match="UserNotFound">
	
		<p><xsl:value-of select="$i18n.UserNotFound"/></p>
	
	</xsl:template>
	
	<xsl:template match="UpdateUser">

		<div id="UserProfileModule" class="contentitem">

			<xsl:if test="UserUpdated">
				<span class="user-updated-message hidden"><xsl:value-of select="$i18n.UserUpdatedMessage"/></span>
			</xsl:if>
	
			<section class="settings">
				<div class="heading-wrapper">
					<h1>
						<xsl:value-of select="/Document/module/name" />
					</h1>
				</div>
							
				<form method="POST" action="{/document/requestinfo/uri}" name="userform">
					<article class="clearfix">
						
						<xsl:if test="validationException/validationError">
							<div class="bigmarginbottom">
								<xsl:apply-templates select="validationException/validationError"/>
							</div>
						</xsl:if>
					
						<div class="heading-wrapper">
							<h2>
								<xsl:value-of select="$i18n.ContactDetails" />
							</h2>
						</div>
						<fieldset>
							<div class="split">
								<div class="left">
									<label for="firstname" class="required">
										<xsl:value-of select="$i18n.firstname" />
									</label>
									<input type="text" disabled="" value="{user/firstname}" id="firstname" name="firstname" />
								</div>
								<div class="right">
									<label for="lastname" class="required">
										<xsl:value-of select="$i18n.lastname" />
									</label>
									<input type="text" disabled="" value="{user/lastname}" id="lastname" name="lastname" />
								</div>
							</div>
							<div class="split odd">
								<label for="citizenIdentifier" class="required">
									<xsl:value-of select="$i18n.citizenIdentifier" />
								</label>
								<input type="text" disabled="" value="{user/Attributes/Attribute[Name = 'citizenIdentifier']/Value}" id="citizenIdentifier" name="citizenIdentifier" />
							</div>
						</fieldset>
						<fieldset>
							<div class="split">
								<xsl:variable name="class">
									<xsl:if test="validationException/validationError[fieldName = 'attribute-address']">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<label for="attribute-address">
									<xsl:value-of select="$i18n.address" />
								</label>
		
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'attribute-address'" />
									<xsl:with-param name="id" select="'attribute-address'" />
									<xsl:with-param name="size" select="40" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'address']/Value" />
									<xsl:with-param name="class" select="$class"/>
								</xsl:call-template>
							</div>
							<div class="split">
								<div class="left">
									<xsl:variable name="class">
										<xsl:if test="validationException/validationError[fieldName = 'attribute-zipCode']">
											<xsl:text>invalid input-error</xsl:text>
										</xsl:if>
									</xsl:variable>
								
									<label for="attribute-zipCode">
										<xsl:value-of select="$i18n.zipCode" />
									</label>
		
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'attribute-zipCode'" />
										<xsl:with-param name="id" select="'attribute-zipCode'" />
										<xsl:with-param name="size" select="40" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'zipCode']/Value" />
										<xsl:with-param name="class" select="$class"/>
									</xsl:call-template>
		
								</div>
								<div class="right">
									<xsl:variable name="class">
										<xsl:if test="validationException/validationError[fieldName = 'attribute-postalAddress']">
											<xsl:text>invalid input-error</xsl:text>
										</xsl:if>
									</xsl:variable>
								
									<label for="attribute-postalAddress">
										<xsl:value-of select="$i18n.postalAddress" />
									</label>
		
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'attribute-postalAddress'" />
										<xsl:with-param name="id" select="'attribute-postalAddress'" />
										<xsl:with-param name="size" select="40" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'postalAddress']/Value" />
										<xsl:with-param name="class" select="$class"/>
									</xsl:call-template>
								</div>
							</div>
						</fieldset>
						<fieldset>
							<div class="split">
								<xsl:variable name="fieldName" select="'attribute-phone'" />
								
								<xsl:variable name="class">
									<xsl:if test="validationException/validationError[fieldName = $fieldName]">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<label for="attribute-phone">
									<xsl:value-of select="$i18n.phone" />
								</label>
		
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="$fieldName" />
									<xsl:with-param name="id" select="$fieldName" />
									<xsl:with-param name="size" select="40" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'phone']/Value" />
									<xsl:with-param name="class" select="$class"/>
								</xsl:call-template>
							</div>
							<div class="split odd">
								<xsl:variable name="class">
									<xsl:if test="validationException/validationError[fieldName = 'email']">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
							
								<label for="email">
									<xsl:value-of select="$i18n.email" />
								</label>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'email'" />
									<xsl:with-param name="id" select="'email'" />
									<xsl:with-param name="size" select="40" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="$class"/>
								</xsl:call-template>
							</div>
						</fieldset>
						<fieldset>
							<div class="split">
								<xsl:variable name="class">
									<xsl:if test="validationException/validationError[fieldName = 'attribute-mobilePhone']">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<label for="attribute-mobilePhone">
									<xsl:value-of select="$i18n.mobilePhone" />
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'attribute-mobilePhone'" />
									<xsl:with-param name="id" select="'attribute-mobilePhone'" />
									<xsl:with-param name="size" select="40" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'mobilePhone']/Value" />
									<xsl:with-param name="class" select="$class"/>
								</xsl:call-template>
							</div>
						</fieldset>					
						
					</article>
					<article>
						
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'attribute-contactBySMS'" />
							<xsl:with-param name="id" select="'attribute-contactBySMS'" />
							<xsl:with-param name="checked" select="user/Attributes/Attribute[Name = 'contactBySMS']/Value" />
						</xsl:call-template>
						
						<label class="checkbox" for="attribute-contactBySMS"><xsl:value-of select="$i18n.SMS" /></label>
						
					</article>
					<div class="divider"></div>
					
					<article class="buttons">
						<input type="submit" value="{$i18n.saveChanges}" class="btn btn-green btn-inline" />
						<xsl:if test="/Document/cancelRedirectURI">
							<a href="{/Document/cancelRedirectURI}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');">Avbryt</a>
						</xsl:if>
					</article>
				</form>
			</section>	
		</div>
	</xsl:template>	
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validation.requiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validation.tooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.validation.tooLong" />
					</xsl:when>														
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validation.unknownError" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="displayName">
						<xsl:value-of select="displayName"/>
					</xsl:when>
					<xsl:when test="fieldName = 'firstname'">
						<xsl:value-of select="$i18n.firstname"/>
					</xsl:when>
					<xsl:when test="fieldName = 'lastname'">
						<xsl:value-of select="$i18n.lastname"/>
					</xsl:when>	
					<xsl:when test="fieldName = 'username'">
						<xsl:value-of select="$i18n.username"/>
					</xsl:when>
					<xsl:when test="fieldName = 'password'">
						<xsl:value-of select="$i18n.password"/>
					</xsl:when>																
					<xsl:when test="fieldName = 'email'">
						<xsl:value-of select="$i18n.email"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attribute-address'">
						<xsl:value-of select="$i18n.address"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attribute-zipCode'">
						<xsl:value-of select="$i18n.zipCode"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attribute-postalAddress'">
						<xsl:value-of select="$i18n.postalAddress"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attribute-phone'">
						<xsl:value-of select="$i18n.phone"/>
					</xsl:when>
					<xsl:when test="fieldName = 'attribute-mobilePhone'">
						<xsl:value-of select="$i18n.mobilePhone"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='UsernameAlreadyTaken'">
						<xsl:value-of select="$i18n.usernameAlreadyTaken"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='EmailAlreadyTaken'">
						<xsl:value-of select="$i18n.emailAlreadyTaken"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='PasswordConfirmationMissMatch'">
						<xsl:value-of select="$i18n.passwordConfirmationMissMatch"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.unknownFault"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>