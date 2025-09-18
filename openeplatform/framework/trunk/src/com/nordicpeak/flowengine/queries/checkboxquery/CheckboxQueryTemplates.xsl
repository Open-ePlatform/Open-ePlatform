<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/checkboxquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/checkboxquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
	
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="CheckboxQueryInstance/CheckboxQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<article>
				
				<div class="heading-wrapper">
				
					<xsl:if test="not(CheckboxQueryInstance/CheckboxQuery/hideTitle = 'true')">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="CheckboxQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="CheckboxQueryInstance/CheckboxQuery/queryID" />
						<xsl:with-param name="queryName" select="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="CheckboxQueryInstance/CheckboxQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:apply-templates select="CheckboxQueryInstance/Alternatives/CheckboxAlternative" mode="show"/>
	
				<xsl:if test="CheckboxQueryInstance/freeTextAlternativeValue">
					<div class="alternative"><xsl:value-of select="CheckboxQueryInstance/freeTextAlternativeValue"/></div>
				</xsl:if>
				
			</article>
		
		</div>
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', CheckboxQueryInstance/CheckboxQuery/queryID)" />
		<xsl:variable name="validationError" select="ValidationErrors/validationError"/>
	
		<div class="query checkboxquery" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query checkboxquery</xsl:text>
				<xsl:if test="CheckboxQueryInstance/CheckboxQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
	
			<xsl:if test="CheckboxQueryInstance/CheckboxQuery/maxChecked and not(Locked)">
				<xsl:attribute name="data-maxchecked">
					<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/maxChecked"/>
				</xsl:attribute>
			</xsl:if>
	
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
						<xsl:if test="not(CheckboxQueryInstance/CheckboxQuery/hideTitle = 'true')">
							<h2>
								<xsl:attribute name="class">
									<xsl:if test="CheckboxQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
										<xsl:text>required</xsl:text>
									</xsl:if>
								</xsl:attribute>
								<xsl:value-of select="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
							</h2>
						</xsl:if>
					</legend>
						
					<xsl:if test="CheckboxQueryInstance/CheckboxQuery/helpText">		
						<xsl:apply-templates select="CheckboxQueryInstance/CheckboxQuery/helpText" />
					</xsl:if>
						
					<xsl:if test="CheckboxQueryInstance/CheckboxQuery/description">
						<span class="italic">
							<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
							<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/description" disable-output-escaping="yes" />
						</span>
					</xsl:if>
					
					<div class="clearfix"/>
					
					<xsl:if test="CheckboxQueryInstance/lockedMessage">
						<p class="full">
							<xsl:value-of select="CheckboxQueryInstance/lockedMessage"/>
						</p>
					</xsl:if>
						
						
					<xsl:apply-templates select="$validationError[messageKey and not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong')]">
						<xsl:with-param name="queryID" select="$queryID"/>
					</xsl:apply-templates>
					
					<xsl:if test="CheckboxQueryInstance/CheckboxQuery/minChecked or CheckboxQueryInstance/CheckboxQuery/maxChecked">
						<div id="checkbox-info-{$queryID}" class="marginbottom">
	
							<xsl:if test="not(CheckboxQueryInstance/CheckboxQuery/description)">
								<xsl:attribute name="style">clear: left</xsl:attribute>
							</xsl:if>
	
							<xsl:choose>
								<xsl:when test="CheckboxQueryInstance/CheckboxQuery/minChecked and CheckboxQueryInstance/CheckboxQuery/maxChecked">
								
										<xsl:value-of select="$i18n.MinMaxAlternatives.part1"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/minChecked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$i18n.MinMaxAlternatives.part2"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/maxChecked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$i18n.MinMaxAlternatives.part3"/>
										
								</xsl:when>
								<xsl:when test="CheckboxQueryInstance/CheckboxQuery/minChecked">
								
										<xsl:value-of select="$i18n.MinAlternatives.part1"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/minChecked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$i18n.MinAlternatives.part2"/>
										
								</xsl:when>
								<xsl:when test="CheckboxQueryInstance/CheckboxQuery/maxChecked">
								
										<xsl:value-of select="$i18n.MaxAlternatives.part1"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/maxChecked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$i18n.MaxAlternatives.part2"/>
										
								</xsl:when>
							</xsl:choose>
							
						</div>
					</xsl:if>
					
					<xsl:if test="CheckboxQueryInstance/CheckboxQuery/showCheckAllBoxes = 'true'"> 
	
						<div class="floatleft full bigmarginbottom">
							<div class="floatleft full">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="concat($queryID, 'checkAllBoxes')" />
									<xsl:with-param name="name" select="'checkAllBoxes'" />
								</xsl:call-template>
								
								<label for="{concat($queryID, 'checkAllBoxes')}" class="checkbox">
									<xsl:value-of select="$i18n.CheckAllBoxes" />
								</label>
							</div>
						</div>
					
					</xsl:if>
					
					<xsl:apply-templates select="CheckboxQueryInstance/CheckboxQuery/Alternatives/CheckboxAlternative" mode="form">
						<xsl:with-param name="validationError" select="$validationError"/>
					</xsl:apply-templates>
		
					<xsl:if test="CheckboxQueryInstance/CheckboxQuery/freeTextAlternative">
					
						<xsl:variable name="freeTextAlternativeName">
							<xsl:value-of select="'q'"/>
							<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/queryID"/>
							<xsl:value-of select="'_freeTextAlternative'"/>
						</xsl:variable>
					
						<div class="alternative floatleft clearboth">
						
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="$freeTextAlternativeName" />
								<xsl:with-param name="name" select="$freeTextAlternativeName" />
								<xsl:with-param name="class" >
									<xsl:choose>
										<xsl:when test="Locked">
											<xsl:text>vertical-align-bottom</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>vertical-align-bottom freeTextAlternative</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="checked">
									<xsl:if test="CheckboxQueryInstance/freeTextAlternativeValue">true</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="disabled" select="Locked or CheckboxQueryInstance/locked = 'true'"/>
							</xsl:call-template>
							
							<label for="{$freeTextAlternativeName}" id="{$freeTextAlternativeName}_label" class="checkbox">
								<xsl:if test="Locked or CheckboxQueryInstance/locked = 'true'">
									<xsl:attribute name="class">checkbox disabled</xsl:attribute>
								</xsl:if>
								
								<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/freeTextAlternative" />
							</label>
							
						</div>
						
						<xsl:choose>
							<xsl:when test="Locked">
								
								<xsl:if test="CheckboxQueryInstance/freeTextAlternativeValue">
									<div class="freeTextAlternativeValue">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="concat($freeTextAlternativeName,'Value')" />
											<xsl:with-param name="disabled" select="Locked or CheckboxQueryInstance/locked = 'true'" />
											<xsl:with-param name="value" select="CheckboxQueryInstance/freeTextAlternativeValue" />
											<xsl:with-param name="class">
												<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
													<xsl:text>input-error</xsl:text>
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="aria-describedby">
												<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
													<xsl:value-of select="$queryID"/>
													<xsl:text>_checkboxalternative-error</xsl:text>
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
										
										<xsl:if test="CheckboxQueryInstance/locked = 'true'">
											<xsl:text> forceDisabled</xsl:text>
										</xsl:if>
									</xsl:attribute>
								
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="concat($freeTextAlternativeName,'Value')" />
										<xsl:with-param name="name" select="concat($freeTextAlternativeName,'Value')" />
										<xsl:with-param name="value" select="CheckboxQueryInstance/freeTextAlternativeValue" />
										<xsl:with-param name="disabled" select="CheckboxQueryInstance/locked = 'true'" />
										<xsl:with-param name="aria-labelledby" select="concat($freeTextAlternativeName, '_label')" />
										<xsl:with-param name="class">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:text>input-error</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-required" select="'true'"/>
										<xsl:with-param name="aria-describedby">
											<xsl:if test="$validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']">
												<xsl:value-of select="$queryID"/>
												<xsl:text>_checkboxalternative-error</xsl:text>
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
		
		<script type="text/javascript">$(function(){initCheckBoxQuery('<xsl:value-of select="CheckboxQueryInstance/CheckboxQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="CheckboxAlternative" mode="form">
	
		<xsl:param name="validationError" select="null"/>
		
		<xsl:variable name="validationErrorID">
			<xsl:text>query_</xsl:text>
			<xsl:value-of select="../../queryID"/>
			<xsl:text>_checkboxalternative-error</xsl:text>
		</xsl:variable>

		<div class="alternative">
		
			<xsl:attribute name="class">
				<xsl:text>alternative</xsl:text>
				<xsl:choose>
					<xsl:when test="../../columns = 'TWO'"> floatleft fifty</xsl:when>
					<xsl:when test="../../columns = 'THREE'"> floatleft thirty</xsl:when>
				</xsl:choose>
			</xsl:attribute>
	
			<xsl:variable name="alternativeID" select="alternativeID"/>
		
			<xsl:variable name="checkboxID">
				<xsl:value-of select="'q'"/>
				<xsl:value-of select="../../queryID"/>
				<xsl:value-of select="'_alternative'"/>
				<xsl:value-of select="alternativeID"/>
			</xsl:variable>
		
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$checkboxID" />
				<xsl:with-param name="name" select="$checkboxID" />
				<xsl:with-param name="class">
					<xsl:text>vertical-align-bottom</xsl:text>
					
					<xsl:if test="$validationError[not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat')]">
						<xsl:text> input-error</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="value" select="alternativeID" />
				<xsl:with-param name="elementName" select="'alternativeID'" />
				<xsl:with-param name="element" select="../../../Alternatives/CheckboxAlternative[alternativeID = $alternativeID]" />
				<xsl:with-param name="requestparameters" select="../../../../requestparameters"/>
				<xsl:with-param name="disabled" select="../../../../Locked or ../../../../CheckboxQueryInstance/locked = 'true'"/>
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
								<xsl:text>checkbox-info-query_</xsl:text>
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
			
			<label for="{$checkboxID}" class="checkbox">
				<xsl:if test="../../../../Locked or ../../../../CheckboxQueryInstance/locked = 'true'">
					<xsl:attribute name="class">checkbox disabled</xsl:attribute>
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
	
	<xsl:template match="CheckboxAlternative" mode="show">

		<div class="alternative">
	
			<xsl:value-of select="name" />
			
			<xsl:if test="price > 0">
				<xsl:text>&#160;(</xsl:text>
				<xsl:value-of select="price"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$i18n.Currency"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
	
		</div>
	
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey = 'TooManyAlternativesSelected']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.TooManyAlternativesSelected.part1"/>
			<xsl:value-of select="maxChecked"/>
			<xsl:value-of select="$i18n.TooManyAlternativesSelected.part2"/>
			<xsl:value-of select="checked"/>
			<xsl:value-of select="$i18n.TooManyAlternativesSelected.part3"/>
		</div>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey = 'TooFewAlternativesSelected']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part1"/>
			<xsl:value-of select="minChecked"/>
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part2"/>
			<xsl:value-of select="checked"/>
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part3"/>
		</div>
		
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueToLong']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror commonerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.FreeTextAlternativeToLong"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueRequired']">
	
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror commonerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.FreeTextAlternativeValueRequired"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<xsl:param name="queryID" />
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror commonerror">
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
		
		<div id="{$queryID}_checkboxalternative-error" class="validationerror">
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