<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/flowengine.js
		/js/contactdetails.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/userprofilemodule.css
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
	
			<section class="setting">
				
				<div class="errands-wrapper">
					
					<div class="heading-wrapper">
						<h1>
							<xsl:value-of select="/Document/module/name" />
						</h1>
					</div>
								
					<form method="POST" action="{/document/requestinfo/uri}" name="userform">
						<div class="clearfix">
						
						<xsl:if test="ValidationErrors/validationError">
							<div id="validationerrors" class="validationerrors">
							
								<div class="info-box error">
								
									<xsl:choose>
										<xsl:when test="ValidationException/validationError[not(fieldName)]">
											<xsl:apply-templates select="ValidationException/validationError[not(fieldName)]"/>
										</xsl:when>
										<xsl:otherwise>
											<span/>
										</xsl:otherwise>
									</xsl:choose>
							
									<div class="marker"></div>
								</div>
							
							</div>
						</xsl:if>
			
							<xsl:if test="ValidationException/validationError">
								<xsl:attribute name="class">error</xsl:attribute>
							</xsl:if>		
													
							<div class="heading-wrapper">
								<h2>
									<xsl:value-of select="$i18n.ContactDetails" />
								</h2>
							</div>
							<div class="flex-wrap">
								<div class="split-fields  align-items-flex-end">
									<div class="contact-detail-field">
										<label for="firstname" class="required">
											<xsl:value-of select="$i18n.firstname" />
										</label>
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="'firstname'" />
											<xsl:with-param name="id" select="'firstname'" />
											<xsl:with-param name="disabled" select="'true'"/>
											<xsl:with-param name="size" select="40" />
											<xsl:with-param name="value" select="user/firstname" />
											<xsl:with-param name="autocomplete" select="'given-name'" />
										</xsl:call-template>
										
									</div>
									<div class="contact-detail-field">
										<label for="lastname" class="required">
											<xsl:value-of select="$i18n.lastname" />
										</label>

										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="'lastname'" />
											<xsl:with-param name="id" select="'lastname'" />
											<xsl:with-param name="disabled" select="'true'"/>
											<xsl:with-param name="size" select="40" />
											<xsl:with-param name="value" select="user/lastname" />
											<xsl:with-param name="autocomplete" select="'family-name'" />
										</xsl:call-template>
									</div>
								
									<div class="contact-detail-field">
										<label for="citizenIdentifier" class="required">
											<xsl:value-of select="$i18n.CitizenIDWithFormat" />
										</label>

										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="'citizenIdentifier'" />
											<xsl:with-param name="id" select="'citizenIdentifier'" />
											<xsl:with-param name="disabled" select="'true'"/>
											<xsl:with-param name="size" select="40" />
											<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'citizenIdentifier']/Value" />
										</xsl:call-template>
									</div>							
								</div>
							<div class="flex-wrap">
								<div class="contact-detail-field">									
									
									<xsl:variable name="fieldName" select="'attribute-address'" />
									
									<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
											
									<xsl:variable name="class">
										<xsl:if test="validationException/validationError[fieldName = 'attribute-address']">
											<xsl:text>invalid input-error</xsl:text>
										</xsl:if>
									</xsl:variable>
									
									<xsl:if test="$validationError">
										<xsl:attribute name="class">
											<xsl:value-of select="$class"/>
											<xsl:text> split</xsl:text>
										</xsl:attribute>
									</xsl:if>				
									
									<label for="attribute-address">
										<xsl:value-of select="$i18n.address" />
									</label>
			
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="$fieldName" />
										<xsl:with-param name="id" select="$fieldName" />
										<xsl:with-param name="size" select="40" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'address']/Value" />
										<xsl:with-param name="class" select="$class"/>
										<xsl:with-param name="aria-describedby">								
											<xsl:if test="$validationError">
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
								
									<xsl:apply-templates select="$validationError"/>
								</div>
								
								<div class="split-fields">
									<div class="contact-detail-field">
									
										<xsl:variable name="fieldName" select="'attribute-zipCode'" />
											
											<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
											
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
											</xsl:if>																	
										
											<label for="attribute-zipCode">
												<xsl:value-of select="$i18n.zipCode" />
											</label>
				
											<xsl:call-template name="createTextField">
												<xsl:with-param name="name" select="$fieldName" />
												<xsl:with-param name="id" select="$fieldName" />
												<xsl:with-param name="size" select="40" />
												<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'zipCode']/Value" />
												<xsl:with-param name="class" select="$class"/>
												<xsl:with-param name="aria-describedby">								
													<xsl:if test="$validationError">
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
										
											<xsl:apply-templates select="$validationError"/>
	
										</div>
										
										<div class="contact-detail-field">
										
											<xsl:variable name="fieldName" select="'attribute-postalAddress'" />
											
											<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
											
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
											</xsl:if>								
										
											<label for="attribute-postalAddress">
												<xsl:value-of select="$i18n.postalAddress" />
											</label>
				
											<xsl:call-template name="createTextField">
												<xsl:with-param name="name" select="$fieldName" />
												<xsl:with-param name="id" select="$fieldName" />
												<xsl:with-param name="size" select="40" />
												<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'postalAddress']/Value" />
												<xsl:with-param name="class" select="$class"/>
												<xsl:with-param name="aria-describedby">								
													<xsl:if test="$validationError">
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
										
											<xsl:apply-templates select="$validationError"/>
	
										</div>
									</div>
								</div>
								
								<div class="split-fields">
									<div class="contact-detail-field">
										<xsl:variable name="fieldName" select="'attribute-phone'" />
										
										<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
										
										<xsl:variable name="class">
											<xsl:if test="$validationError">
												<xsl:text>invalid input-error</xsl:text>
											</xsl:if>
										</xsl:variable>
										
										
										<xsl:attribute name="class">
											<xsl:if test="$validationError">
												<xsl:value-of select="$class"/>
												<xsl:text> split</xsl:text>
											</xsl:if>
										</xsl:attribute>
										
										
										<label for="attribute-phone">
											<xsl:value-of select="$i18n.phone" />
										</label>
				
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="$fieldName" />
											<xsl:with-param name="id" select="$fieldName" />
											<xsl:with-param name="size" select="40" />
											<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'phone']/Value" />
											<xsl:with-param name="class" select="$class"/>
											<xsl:with-param name="aria-describedby">																			
												<xsl:if test="$validationError">
													<xsl:value-of select="$fieldName"/>
													<xsl:text>-error </xsl:text>
												</xsl:if>
												
												<xsl:value-of select="'phoneInfoText'"/>
											</xsl:with-param>
											<xsl:with-param name="autocomplete" select="'tel-national'"/>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<div id="'phoneInfoText'" class="input-info-text">
											<xsl:value-of select="$i18n.Example.PhoneFormat"/>
										</div>
										
										<xsl:apply-templates select="$validationError"/>
									
									</div>
									<div class="contact-detail-field">
									
										<xsl:variable name="fieldName" select="'email'" />
									
										<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
	
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
										</xsl:if>
									
										<label for="email" class="required">
											<xsl:value-of select="$i18n.email" />
										</label>
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="$fieldName" />
											<xsl:with-param name="id" select="$fieldName" />
											<xsl:with-param name="size" select="40" />
											<xsl:with-param name="element" select="user" />
											<xsl:with-param name="class" select="$class"/>
											<xsl:with-param name="autocomplete" select="'email'"/>
											<xsl:with-param name="aria-describedby">								
												<xsl:if test="$validationError">
													<xsl:value-of select="$fieldName"/>
													<xsl:text>-error </xsl:text>
												</xsl:if>											
												<xsl:value-of select="'emailInfoText'"/>
											</xsl:with-param>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<div id="emailInfoText" class="input-info-text">
											<xsl:value-of select="$i18n.Example.EmailFormat"/>
										</div>
										
										<xsl:apply-templates select="$validationError"/>
									
									</div>
								</div>
								<div class="contact-detail-field">
								
									<xsl:variable name="fieldName" select="'attribute-mobilePhone'" />
									
									<xsl:variable name="validationError" select="validationException/validationError[fieldName = $fieldName]"/>
								
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
									</xsl:if>
									
									<label for="attribute-mobilePhone">
										<xsl:value-of select="$i18n.mobilePhone" />
									</label>
									
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'attribute-mobilePhone'" />
										<xsl:with-param name="id" select="'attribute-mobilePhone'" />
										<xsl:with-param name="size" select="40" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name = 'mobilePhone']/Value" />
										<xsl:with-param name="class" select="$class"/>
										<xsl:with-param name="aria-describedby">								
											<xsl:if test="$validationError">
												<xsl:value-of select="$fieldName"/>
												<xsl:text>-error </xsl:text>
											</xsl:if>											
											<xsl:value-of select="'mobileInfoText'"/>
										</xsl:with-param>
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$validationError">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
									
									<div id="mobileInfoText" class="input-info-text">
										<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
									</div>
									
									<xsl:apply-templates select="$validationError"/>
								
								</div>
							</div>					
							
						</div>
						
						<xsl:if test="AttrbuteDescriptors/AttributeDescriptor[Name = 'contactBySMS']">
							
							<div class="no-sections checkbox-setting">
								
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="name" select="'attribute-contactBySMS'" />
									<xsl:with-param name="id" select="'attribute-contactBySMS'" />
									<xsl:with-param name="checked" select="user/Attributes/Attribute[Name = 'contactBySMS']/Value" />
								</xsl:call-template>
								
								<label class="nomargin" for="attribute-contactBySMS"><xsl:value-of select="$i18n.SMS" /></label>
								
							</div>
							
						</xsl:if>
						<div class="divider"></div>
						
						<div class="buttons">
							<input type="submit" value="{$i18n.saveChanges}" class="btn btn-green btn-inline" />
							<xsl:if test="/Document/cancelRedirectURI">
								<a href="{/Document/cancelRedirectURI}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');">Avbryt</a>
							</xsl:if>
						</div>
					</form>
				
				</div>
				
			</section>	
		</div>
	</xsl:template>	
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.ValidationError.RequiredField" />
		</div>
		
	</xsl:template>		
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part1" />
			<xsl:value-of select="currentLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part2" />
			<xsl:value-of select="maxLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part3" />
		</div>
		
	</xsl:template>	
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
				
			<xsl:choose>
				<xsl:when test="contains(fieldName, 'attribute-zipCode')">
					<xsl:value-of select="$i18n.ValidationError.InvalidZipCode"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, 'attribute-phone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidPhoneNumber"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, 'email')">
					<xsl:value-of select="$i18n.ValidationError.InvalidEmail"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, 'attribute-mobilePhone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidMobilePhoneNumber"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, 'attribute-phone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidPhoneNumber"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.ValidationError.InvalidFormat"/>
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.ValidationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validation.tooShort" />
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