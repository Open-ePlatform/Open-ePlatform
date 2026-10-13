<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/radiobuttonquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/radiobuttonquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>

	<xsl:template match="ShowQueryValues">
	
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="RadioButtonQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<article>
				
				<div class="heading-wrapper">
				
					<xsl:if test="not(RadioButtonQueryInstance/RadioButtonQuery/hideTitle = 'true')">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="RadioButtonQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="RadioButtonQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="RadioButtonQueryInstance/RadioButtonQuery/queryID" />
						<xsl:with-param name="queryName" select="RadioButtonQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="RadioButtonQueryInstance/RadioButtonQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:choose>
					<xsl:when test="RadioButtonQueryInstance/RadioButtonAlternative">
						<xsl:value-of select="RadioButtonQueryInstance/RadioButtonAlternative/name"/>
						
						<xsl:if test="RadioButtonQueryInstance/RadioButtonAlternative/price > 0">
							<xsl:text>&#160;(</xsl:text>
							<xsl:value-of select="RadioButtonQueryInstance/RadioButtonAlternative/price"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.Currency"/>
							<xsl:text>)</xsl:text>
						</xsl:if>
						
					</xsl:when>
					<xsl:when test="RadioButtonQueryInstance/freeTextAlternativeValue">
						<xsl:value-of select="RadioButtonQueryInstance/freeTextAlternativeValue"/>
					</xsl:when>
				</xsl:choose>
				
			</article>
		
		</div>		
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', RadioButtonQueryInstance/RadioButtonQuery/queryID)" />
		<xsl:variable name="locked" select="(RadioButtonQueryInstance/RadioButtonQuery/lockForManagerUpdate = 'true' and RequestMetadata/manager = 'true') or (RadioButtonQueryInstance/RadioButtonQuery/lockForOwnerUpdate = 'true' and RequestMetadata/manager = 'false' and RequestMetadata/flowInstanceIsSubmitted = 'true') or RadioButtonQueryInstance/locked = 'true'" />
		
		<xsl:variable name="validationError" select="ValidationErrors/validationError"/>
		
		<div class="query radiobuttonquery" id="{$queryID}">
		
			<xsl:attribute name="class">
				<xsl:text>query radiobuttonquery</xsl:text>
				<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/freeTextAlternative">
					<xsl:text> hasFreeTextAlternative</xsl:text>
				</xsl:if>
				<xsl:if test="EnableAjaxPosting"><xsl:text> enableAjaxPosting</xsl:text></xsl:if>
				<xsl:if test="RadioButtonQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
			
			<xsl:if test="$validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
						<xsl:choose>
							<xsl:when test="$validationError[not(messageKey) and not(validationErrorType = 'InvalidFormat')]">
								<xsl:apply-templates select="$validationError[not(messageKey) and not(validationErrorType = 'InvalidFormat')]"/>
							</xsl:when>
							<xsl:otherwise>
								<span/>								
							</xsl:otherwise>
						</xsl:choose>
					
						<div class="marker"/>
					</div>
				</div>
			</xsl:if>
			
			<article>
			
				<xsl:if test="$validationError">
					<xsl:attribute name="class">input-error error floatnone</xsl:attribute>
				</xsl:if>
				
				<fieldset>
					
					<legend class="heading-wrapper">
						<xsl:if test="not(RadioButtonQueryInstance/RadioButtonQuery/hideTitle = 'true')">
							<h2>
								<xsl:attribute name="class">
									<xsl:if test="RadioButtonQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
										<xsl:text>required</xsl:text>
									</xsl:if>
								</xsl:attribute>
								<xsl:value-of select="RadioButtonQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
							</h2>
						</xsl:if>
					</legend>
							
					<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/helpText">		
						<xsl:apply-templates select="RadioButtonQueryInstance/RadioButtonQuery/helpText" />
					</xsl:if>
					
					<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/description">
						<span class="italic">
							<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
							<xsl:value-of select="RadioButtonQueryInstance/RadioButtonQuery/description" disable-output-escaping="yes" />
						</span>
					</xsl:if>
					
					<div class="clearfix"/>
					
					<xsl:if test="RadioButtonQueryInstance/lockedMessage">
						<p class="full">
							<xsl:value-of select="RadioButtonQueryInstance/lockedMessage"/>
						</p>
					</xsl:if>
					
					<xsl:apply-templates select="$validationError[messageKey and not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong')]">
						<xsl:with-param name="queryID" select="$queryID"/>
					</xsl:apply-templates>
					
					<div id="radiobutton-info-{$queryID}" class="marginbottom">
						<xsl:value-of select="$i18n.ChooseOneAlternative"/>
					</div>
					
					<xsl:apply-templates select="RadioButtonQueryInstance/RadioButtonQuery/Alternatives/RadioButtonAlternative" mode="form">
						<xsl:with-param name="locked" select="$locked" />
						<xsl:with-param name="validationError" select="$validationError"/>
					</xsl:apply-templates>
			
					<xsl:if test="RadioButtonQueryInstance/RadioButtonQuery/freeTextAlternative">
					
						<xsl:variable name="freeTextAlternativeName">
							<xsl:value-of select="'q'"/>
							<xsl:value-of select="RadioButtonQueryInstance/RadioButtonQuery/queryID"/>
							<xsl:value-of select="'_alternative'"/>
						</xsl:variable>
					
						<div class="alternative floatleft clearboth">
					
							<xsl:call-template name="createRadio">
								<xsl:with-param name="id" select="concat($freeTextAlternativeName, '_freeTextAlternative')" />
								<xsl:with-param name="name" select="$freeTextAlternativeName" />
								<xsl:with-param name="class" select="'freeTextAlternative'" />
								<xsl:with-param name="value" select="'freeTextAlternative'" />
								<xsl:with-param name="checked" select="RadioButtonQueryInstance/freeTextAlternativeValue" />
								<xsl:with-param name="disabled" select="$locked"/>
							</xsl:call-template>
							
							<xsl:text>&#x20;</xsl:text>
								
							<label for="{concat($freeTextAlternativeName, '_freeTextAlternative')}" id="{$freeTextAlternativeName}_label" class="radio">
								<xsl:if test="$locked">
									<xsl:attribute name="class">radio disabled</xsl:attribute>
								</xsl:if>
								
								<xsl:value-of select="RadioButtonQueryInstance/RadioButtonQuery/freeTextAlternative" />
							</label>
					
						</div>
						
						<xsl:choose>
							<xsl:when test="$locked">
								
								<xsl:if test="RadioButtonQueryInstance/freeTextAlternativeValue">
									<div class="freeTextAlternativeValue">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="concat($freeTextAlternativeName,'Value')" />
											<xsl:with-param name="value" select="RadioButtonQueryInstance/freeTextAlternativeValue" />
											<xsl:with-param name="disabled" select="'true'" />
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="aria-describedby">
												<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
													<xsl:value-of select="$queryID"/>
													<xsl:text>_radioalternative-error</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
									</div>
									
									<div class="clearfix"/>
									
									<xsl:apply-templates select="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
										<xsl:with-param name="queryID" select="$queryID"/>
									</xsl:apply-templates>
								</xsl:if>
								
							</xsl:when>
							<xsl:otherwise>
								
								<div class="freeTextAlternativeValue hidden">
									<xsl:attribute name="class">
										<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
											<xsl:text>freeTextAlternativeValue</xsl:text>
										</xsl:if>
									</xsl:attribute>
								
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="concat($freeTextAlternativeName,'Value')" />
										<xsl:with-param name="name" select="concat($freeTextAlternativeName,'Value')" />
										<xsl:with-param name="value" select="RadioButtonQueryInstance/freeTextAlternativeValue" />
										<xsl:with-param name="class">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:text>input-error</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-labelledby" select="concat($freeTextAlternativeName, '_label')" />
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-required" select="'true'"/>
										<xsl:with-param name="aria-describedby">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:value-of select="$queryID"/>
												<xsl:text>_radioalternative-error</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</div>
								
								<div class="clearfix"/>
									
								<xsl:apply-templates select="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
									<xsl:with-param name="queryID" select="$queryID"/>
								</xsl:apply-templates>
								
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</fieldset>
			</article>
		
		</div>
		
		<script type="text/javascript">$(function(){initRadioButtonQuery('<xsl:value-of select="RadioButtonQueryInstance/RadioButtonQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="RadioButtonAlternative" mode="form">
		<xsl:param name="locked" />
		<xsl:param name="validationError" select="null"/>
		
		<xsl:variable name="validationErrorID">
			<xsl:text>query_</xsl:text>
			<xsl:value-of select="../../queryID"/>
			<xsl:text>_radioalternative-error</xsl:text>
		</xsl:variable>
	
		<div class="alternative">
		
			<xsl:attribute name="class">
				<xsl:text>alternative</xsl:text>
				<xsl:choose>
					<xsl:when test="../../columns = 'TWO'"> floatleft fifty</xsl:when>
					<xsl:when test="../../columns = 'THREE'"> floatleft thirty</xsl:when>
				</xsl:choose>
			</xsl:attribute>

			<xsl:variable name="radioID">
				<xsl:value-of select="'q'"/>
				<xsl:value-of select="../../queryID"/>
				<xsl:value-of select="'_alternative'"/>
				<xsl:value-of select="alternativeID" />
			</xsl:variable>
		
			<xsl:variable name="alternativeID" select="alternativeID"/>
		
			<xsl:call-template name="createRadio">
				<xsl:with-param name="id" select="$radioID" />
				<xsl:with-param name="name">
					<xsl:value-of select="'q'"/>
					<xsl:value-of select="../../queryID"/>
					<xsl:value-of select="'_alternative'"/>
				</xsl:with-param>
				<xsl:with-param name="value" select="alternativeID"/>
				<xsl:with-param name="elementName" select="'alternativeID'" />
				<xsl:with-param name="element" select="../../../RadioButtonAlternative[alternativeID = $alternativeID]" />
				<xsl:with-param name="requestparameters" select="../../../../requestparameters"/>
				<xsl:with-param name="disabled" select="$locked"/>
				<xsl:with-param name="class">
					<xsl:if test="$validationError[not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat')]">
						<xsl:text>input-error</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-required">
					<xsl:if test="position() = 1 and ../../../QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">true</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-describedby">
					<xsl:if test="position() = 1">
						<xsl:choose>
							<xsl:when test="$validationError[not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat')]">
						<xsl:value-of select="$validationErrorID"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>radiobutton-info-query_</xsl:text>
								<xsl:value-of select="../../queryID"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-invalid">
					<xsl:if test="position() = 1 and $validationError[not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat')]">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
			</xsl:call-template>
			
			<label for="{$radioID}" class="radio">
				<xsl:if test="$locked">
					<xsl:attribute name="class">radio disabled</xsl:attribute>
				</xsl:if>
			
				<xsl:value-of select="name" />
				
				<xsl:if test="price > 0">
					<xsl:text>&#160;(</xsl:text>
					<xsl:value-of select="price"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.Currency"/>
					<xsl:text>)</xsl:text>
				</xsl:if>
			
			</label>
				
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueToLong']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_radioalternative-error" class="validationerror commonerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.FreeTextAlternativeToLong"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueRequired']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_radioalternative-error" class="validationerror commonerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.FreeTextAlternativeValueRequired"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<xsl:param name="queryID" select="null" />
		
		<div id="{$queryID}_radioalternative-error" class="validationerror commonerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:choose>
				<xsl:when test="invalidFormatMessage">
					<xsl:value-of select="invalidFormatMessage"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.InvalidFormat"/>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredQuery']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_radioalternative-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.RequiredQuery"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.ValidationError.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>
	
</xsl:stylesheet>