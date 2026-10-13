<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

 	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/manualmultisignquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/manualmultisignquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="ManualMultiSignQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="ManualMultiSignQueryInstance/ManualMultiSignQuery/queryID" />
						<xsl:with-param name="queryName" select="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/description">
					<span class="italic">
						<xsl:value-of select="ManualMultiSignQueryInstance/ManualMultiSignQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<fieldset>
				
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.SocialSecurityNumber" /></strong>
						<xsl:value-of select="ManualMultiSignQueryInstance/socialSecurityNumber" />
					</div>				
				
				</fieldset>
				
				<fieldset>
					
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.Firstname" /></strong>
						<xsl:value-of select="ManualMultiSignQueryInstance/firstname" />
					</div>
					
					<div class="split odd">
						<strong class="block"><xsl:value-of select="$i18n.Lastname" /></strong>
						<xsl:value-of select="ManualMultiSignQueryInstance/lastname" />
					</div>					
					
					<div class="split">
						<strong class="block"><xsl:value-of select="$i18n.Email" /></strong>
						<xsl:value-of select="ManualMultiSignQueryInstance/email" />
					</div>
					
					<div class="split odd">
						<strong class="block"><xsl:value-of select="$i18n.MobilePhone" /></strong>
						<xsl:value-of select="ManualMultiSignQueryInstance/mobilePhone" />
					</div>								
					
				</fieldset>
				
				
			</article>
			
		</div>
		
	</xsl:template>	
	
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', ManualMultiSignQueryInstance/ManualMultiSignQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', ManualMultiSignQueryInstance/ManualMultiSignQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
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
							<xsl:if test="ManualMultiSignQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ManualMultiSignQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/helpText">		
						<xsl:apply-templates select="ManualMultiSignQueryInstance/ManualMultiSignQuery/helpText" />
					</xsl:if>
				
				</div>
				
				<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="ManualMultiSignQueryInstance/ManualMultiSignQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>				
				
				<fieldset>

					<div class="split">
						
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_socialSecurityNumber')" />
					
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>
					
						<label for="{$fieldName}" class="required">
							<xsl:value-of select="$i18n.SocialSecurityNumber" />						
						</label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.SocialSecurityNumber"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="aria-required" select="'true'"/>
							<xsl:with-param name="value">
								<xsl:value-of select="ManualMultiSignQueryInstance/socialSecurityNumber" />							
							</xsl:with-param>
						</xsl:call-template>
						
						<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
					</div>
				
				</fieldset>
				
				<fieldset>
					
					<div class="split">
					
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_firstname')" />
					
						
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>							
					
						<label for="{$fieldName}" class="required">
							<xsl:value-of select="$i18n.Firstname" />
						</label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Firstname"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="autocomplete" select="'given-name'"/>
							<xsl:with-param name="aria-required" select="'true'"/>
							<xsl:with-param name="value">
								<xsl:value-of select="ManualMultiSignQueryInstance/firstname" />							
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
					
						<label for="{$fieldName}" class="required">
							<xsl:value-of select="$i18n.Lastname" />					
						</label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Lastname"/>
							<xsl:with-param name="autocomplete" select="'family-name'"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="aria-required" select="'true'"/>
							<xsl:with-param name="value">
								<xsl:value-of select="ManualMultiSignQueryInstance/lastname" />							
							</xsl:with-param>
						</xsl:call-template>
						
						<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
					</div>					
					
					<div class="split">
						
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_email')" />
					
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
						<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
							<xsl:attribute name="class">
								<xsl:text>split invalid input-error</xsl:text>
							</xsl:attribute>
						</xsl:if>
						
						<xsl:variable name="emailInfoText" select="'emailInfoText'"/>
					
						<label for="{$fieldName}"><xsl:value-of select="$i18n.Email" /></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.Email"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="autocomplete" select="'email'"/>
							<xsl:with-param name="value"><xsl:value-of select="ManualMultiSignQueryInstance/email" /></xsl:with-param>
							<xsl:with-param name="aria-describedby">
								<xsl:if test="$validationError">
									<xsl:value-of select="$fieldName"/>
									<xsl:text>-error </xsl:text>
								</xsl:if>
										
								<xsl:value-of select="$emailInfoText"/>
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
						
						<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
					</div>
					
					<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/requireContactInfoVerification = 'true'">
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_confirmEmail')" />
						
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
						<xsl:variable name="class">
							<xsl:if test="$validationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
					
						<xsl:variable name="classConfirmEmail">
							<xsl:text>disablepaste odd</xsl:text>
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:text> invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
						
						<xsl:variable name="confirmEmailInfoText" select="'emailInfoText'"/>
						
						<div class="split {$classConfirmEmail}">		
								
							<label for="{$fieldName}">
								
								<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/fieldEmail = 'REQUIRED'">
									<xsl:attribute name="class">
										<xsl:text>required</xsl:text>
									</xsl:attribute>
								</xsl:if>
								
								<xsl:value-of select="$i18n.Confirmation"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.email"/>
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName"/>
								<xsl:with-param name="name" select="$fieldName"/>
								<xsl:with-param name="title" select="$i18n.Email"/>
								<xsl:with-param name="requestparameters" select="requestparameters"/>
								<xsl:with-param name="value" select="ManualMultiSignQueryInstance/email"/>
								<xsl:with-param name="class" select="$classConfirmEmail"/>
								<xsl:with-param name="size" select="'50'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="$validationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error </xsl:text>
									</xsl:if>
							
									<xsl:value-of select="$confirmEmailInfoText"/>
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
					
					<div>
						<xsl:variable name="requireContactInfoVerification" select="ManualMultiSignQueryInstance/ManualMultiSignQuery/requireContactInfoVerification" />
						
						<!-- Add class depending on weather there are several fields or not -->
						 <xsl:variable name="baseClass">
					     	<xsl:choose>
					        	<xsl:when test="$requireContactInfoVerification = 'true'">split</xsl:when>
					        	<xsl:otherwise>split odd</xsl:otherwise>
					      	</xsl:choose>
					    </xsl:variable>
    
						<xsl:attribute name="class">
					     	<xsl:value-of select="$baseClass" />
					    </xsl:attribute>
					    
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_mobilePhone')" />
						
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
														
						<xsl:if test="$validationError">
							<xsl:attribute name="class">
					     		<xsl:value-of select="$baseClass" />
					     		<xsl:text> invalid input-error</xsl:text>
					    	</xsl:attribute>
						</xsl:if>
						
						<xsl:variable name="mobileInfoText" select="'mobileInfoText'"/>
					
						<label for="{$fieldName}"><xsl:value-of select="$i18n.MobilePhone" /></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="$fieldName" />
							<xsl:with-param name="name" select="$fieldName" />
							<xsl:with-param name="title" select="$i18n.MobilePhone"/>
							<xsl:with-param name="size" select="50"/>
							<xsl:with-param name="value">
								<xsl:value-of select="ManualMultiSignQueryInstance/mobilePhone" />							
							</xsl:with-param>
							<xsl:with-param name="aria-describedby">								
								<xsl:if test="$validationError">
									<xsl:value-of select="$fieldName"/>
									<xsl:text>-error </xsl:text>
								</xsl:if>
								
								<xsl:value-of select="$mobileInfoText"/>
							</xsl:with-param>
							<xsl:with-param name="autocomplete" select="'tel-national'"/>
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
						
						<xsl:apply-templates select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
					</div>
					
					<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/requireContactInfoVerification = 'true'">
								
						<xsl:variable name="fieldName" select="concat($shortQueryID, '_confirmMobilePhone')" />
						
						<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $fieldName]"/>
						
						<xsl:variable name="class">
							<xsl:if test="$validationError">
								<xsl:text>invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
					
						<xsl:variable name="classConfirmMobilePhone">
							<xsl:text>disablepaste odd</xsl:text>
							<xsl:if test="ValidationErrors/validationError[fieldName = $fieldName]">
								<xsl:text> invalid input-error</xsl:text>
							</xsl:if>
						</xsl:variable>
						
						<xsl:variable name="confirmMobilePhoneInfoText" select="'mobilePhoneInfoText'"/>
						
						<div class="split {$classConfirmMobilePhone}">
						
							<label for="{$fieldName}">
								<xsl:if test="ManualMultiSignQueryInstance/ManualMultiSignQuery/fieldMobilePhone = 'REQUIRED'">
									<xsl:attribute name="class">
										<xsl:text>required</xsl:text>
									</xsl:attribute>
								</xsl:if>
								
								<xsl:value-of select="$i18n.Confirmation"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.mobilePhoneNumber"/>
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="$fieldName"/>
								<xsl:with-param name="name" select="$fieldName"/>
								<xsl:with-param name="title" select="$i18n.MobilePhone"/>
								<xsl:with-param name="requestparameters" select="requestparameters"/>
								<xsl:with-param name="class" select="$classConfirmMobilePhone"/>
								<xsl:with-param name="value" select="ManualMultiSignQueryInstance/mobilePhone"/>
								<xsl:with-param name="size" select="'50'"/>
								<xsl:with-param name="aria-describedby">
									<xsl:if test="$validationError">
										<xsl:value-of select="$fieldName"/>
										<xsl:text>-error </xsl:text>
									</xsl:if>
							
									<xsl:value-of select="$confirmMobilePhoneInfoText"/>
								</xsl:with-param>
								<xsl:with-param name="aria-invalid">
									<xsl:if test="$validationError">
										<xsl:text>true</xsl:text>
									</xsl:if>
								</xsl:with-param>
							</xsl:call-template>
							
							<div class="clearfix"/>
					
							<div id="{$confirmMobilePhoneInfoText}" class="input-info-text">
								<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
							</div>
							
							<xsl:apply-templates select="$validationError"/>
		
						</div>
					
					</xsl:if>
								
					
				</fieldset>
				
			</article>
		
		</div>
		
		<script type="text/javascript">$(function(){initManualMultiSignQuery('<xsl:value-of select="ManualMultiSignQueryInstance/ManualMultiSignQuery/queryID" />');});</script>
		
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
				<xsl:when test="contains(fieldName, '_email') or contains(fieldName, '_confirmEmail')">
					<xsl:value-of select="$i18n.ValidationError.InvalidEmail"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, '_mobilePhone') or contains(fieldName, '_confirmMobilePhone')">
					<xsl:value-of select="$i18n.ValidationError.InvalidPhoneNumber"/>
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
	
	<xsl:template match="validationError[messageKey = 'NoContactChannelSpecified']">
	
		<span>
			<strong data-icon-before="!" aria-hidden="true">
				<xsl:value-of select="$i18n.ValidationError.NoneOfOptionalRequiredFieldsEntered.part1" />
				<a href="#{./displayName}email"><xsl:value-of select="$i18n.email" /></a>
				<xsl:value-of select="$i18n.ValidationError.NoneOfOptionalRequiredFieldsEntered.part2" />
				<a href="#{./displayName}mobilePhone"><xsl:value-of select="$i18n.mobilePhone" /></a>								
			</strong>
		</span>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey = 'PosterUsingPostersCitizenIdentifier']">

		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.Info.PosterUsingPostersCitizenIdentifier"/>
		</div>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UserUsingPostersCitizenIdentifier']">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.UserUsingPostersCitizenIdentifier"/>
		</div>
		
	</xsl:template>

	
	<xsl:template match="validationError">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.UnknownValidationError"/>
		</div>
		
	</xsl:template>	
		
</xsl:stylesheet>