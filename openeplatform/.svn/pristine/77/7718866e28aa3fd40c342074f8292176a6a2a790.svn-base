<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/dropdownquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/dropdownquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
	
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="DropDownQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
							<xsl:if test="DropDownQueryInstance/DropDownQuery/description"> 
								<xsl:text> hasDescription</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="DropDownQueryInstance/DropDownQuery/queryID" />
						<xsl:with-param name="queryName" select="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
				</div>
				
				<xsl:if test="DropDownQueryInstance/DropDownQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="DropDownQueryInstance/DropDownQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:choose>
						<xsl:when test="DropDownQueryInstance/DropDownAlternative">
							<xsl:value-of select="DropDownQueryInstance/DropDownAlternative/name"/>
							
							<xsl:if test="DropDownQueryInstance/DropDownAlternative/price > 0">
								<xsl:text>&#160;(</xsl:text>
								<xsl:value-of select="DropDownQueryInstance/DropDownAlternative/price"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.Currency"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							
						</xsl:when>
						<xsl:when test="DropDownQueryInstance/freeTextAlternativeValue">
							<xsl:value-of select="DropDownQueryInstance/freeTextAlternativeValue"/>
						</xsl:when>
					</xsl:choose>
				
			</article>
		
		</div>
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', DropDownQueryInstance/DropDownQuery/queryID)" />
		<xsl:variable name="locked" select="locked or DropDownQueryInstance/locked = 'true'"/>
	
		<div class="query dropdownquery" id="{$queryID}">
	
			<xsl:attribute name="class">
				<xsl:text>query dropdownquery</xsl:text>
				<xsl:if test="DropDownQueryInstance/DropDownQuery/freeTextAlternative"><xsl:text> hasFreeTextAlternative</xsl:text></xsl:if>
				<xsl:if test="EnableAjaxPosting"><xsl:text> enableAjaxPosting</xsl:text></xsl:if>
				<xsl:if test="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
	
			<a name="{$queryID}" />
	
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
						<xsl:choose>
							<xsl:when test="ValidationErrors/validationError[messageKey and not(fieldName) and not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong')]">
								<xsl:apply-templates select="ValidationErrors/validationError[messageKey and not(fieldName) and not(messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong')]"/>
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
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="DropDownQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="DropDownQueryInstance/DropDownQuery/helpText">		
						<xsl:apply-templates select="DropDownQueryInstance/DropDownQuery/helpText" />
					</xsl:if>
					
				</div>
					
				<xsl:if test="DropDownQueryInstance/DropDownQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="DropDownQueryInstance/DropDownQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<xsl:if test="DropDownQueryInstance/lockedMessage">
					<p class="full">
						<xsl:value-of select="DropDownQueryInstance/lockedMessage"/>
					</p>
				</xsl:if>
				
				<fieldset>
					
					<xsl:variable name="dropDownName">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="DropDownQueryInstance/DropDownQuery/queryID"/>
						<xsl:value-of select="'_dropdownalternative'"/>
					</xsl:variable>
					
					<xsl:variable name="validationError" select="ValidationErrors/validationError[fieldName = $dropDownName]"/>
				
					<div class="split">

						<xsl:variable name="selectedAlternativeID" select="DropDownQueryInstance/DropDownAlternative/alternativeID" />
						
						<xsl:attribute name="class">
							<xsl:text>split</xsl:text>
							
							<xsl:if test="$validationError">
								<xsl:text> invalid input-error</xsl:text>
							</xsl:if>
						</xsl:attribute>
					
						<select id="{$dropDownName}" name="{$dropDownName}" style="width: 100%;" aria-label="{DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name}">
							<xsl:if test="$locked">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
							
							<xsl:if test="$validationError">
								<xsl:attribute name="aria-invalid">true</xsl:attribute>
								<xsl:attribute name="aria-describedby">
									<xsl:value-of select="$dropDownName"/>
									<xsl:text>-error</xsl:text>
								</xsl:attribute>
								<xsl:attribute name="class">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:attribute>
							</xsl:if>
							
							<option value=""><xsl:value-of select="DropDownQueryInstance/DropDownQuery/shortDescription"/></option>
							
							<xsl:for-each select="DropDownQueryInstance/DropDownQuery/Alternatives/DropDownAlternative">
								<option value="{alternativeID}">
									<xsl:choose>
										<xsl:when test="requestparameters">
											<xsl:if test="requestparameters/parameter[name=$dropDownName]/value = alternativeID">
												<xsl:attribute name="selected"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$selectedAlternativeID = alternativeID">
												<xsl:attribute name="selected" />
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
									
									<xsl:value-of select="name" />
									
									<xsl:if test="price > 0">
										<xsl:text>&#160;(</xsl:text>
										<xsl:value-of select="price"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$i18n.Currency"/>
										<xsl:text>)</xsl:text>
									</xsl:if>
									
								</option>
							</xsl:for-each>
	
							<xsl:if test="DropDownQueryInstance/DropDownQuery/freeTextAlternative">
							
								<option value="freeTextAlternative">
									
									<xsl:choose>
										<xsl:when test="requestparameters">
											<xsl:if test="requestparameters/parameter[name=$dropDownName]/value = 'freeTextAlternative'">
												<xsl:attribute name="selected"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="DropDownQueryInstance/freeTextAlternativeValue">
												<xsl:attribute name="selected" />
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>	
									
									<xsl:value-of select="DropDownQueryInstance/DropDownQuery/freeTextAlternative" />
									
								</option>
							
							</xsl:if>
							
						</select>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$validationError"/>
						
					</div>
					
					<xsl:if test="DropDownQueryInstance/DropDownQuery/freeTextAlternative">
						<xsl:variable name="freeTextValidationError" select="ValidationErrors/validationError[messageKey = 'FreeTextAlternativeValueRequired' or messageKey = 'FreeTextAlternativeValueToLong' or validationErrorType = 'InvalidFormat']"/>
					
						<xsl:choose>
							<xsl:when test="$locked">
								
								<xsl:if test="DropDownQueryInstance/freeTextAlternativeValue">
									<div class="freeTextAlternativeValue">
										<xsl:attribute name="class">
											<xsl:text>freeTextAlternativeValue</xsl:text>
											
											<xsl:if test="$freeTextValidationError">
												<xsl:text> invalid</xsl:text>
											</xsl:if>
										</xsl:attribute>
									
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="concat($dropDownName,'Value')" />
											<xsl:with-param name="disabled" select="$locked" />
											<xsl:with-param name="value" select="DropDownQueryInstance/freeTextAlternativeValue" />
											<xsl:with-param name="class">
												<xsl:if test="$freeTextValidationError">
													<xsl:text>invalid input-error</xsl:text>
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$freeTextValidationError">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="aria-describedby">
												<xsl:if test="$freeTextValidationError">
													<xsl:value-of select="concat($dropDownName, 'Value')"/>
													<xsl:text>-error</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
									</div>
								</xsl:if>
								
							</xsl:when>
							<xsl:otherwise>
								
								<div class="freeTextAlternativeValue hidden">
									<xsl:attribute name="class">
										<xsl:text>freeTextAlternativeValue</xsl:text>
										
										<xsl:choose>
											<xsl:when test="$freeTextValidationError">
												<xsl:text> invalid</xsl:text>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text> hidden</xsl:text>
											</xsl:otherwise>
										</xsl:choose>
										
										<xsl:if test="$freeTextValidationError">
										</xsl:if>
									</xsl:attribute>
								
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="concat($dropDownName,'Value')" />
										<xsl:with-param name="name" select="concat($dropDownName,'Value')" />
										<xsl:with-param name="disabled" select="disabled" />
										<xsl:with-param name="value" select="DropDownQueryInstance/freeTextAlternativeValue" />
										<xsl:with-param name="class">
											<xsl:if test="$freeTextValidationError">
												<xsl:text>invalid input-error</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$freeTextValidationError">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="aria-describedby">
											<xsl:if test="$freeTextValidationError">
												<xsl:value-of select="$dropDownName"/>
												<xsl:text>-error</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</div>
								
							</xsl:otherwise>
						</xsl:choose>
						
						<div class="clearfix"/>
						
						<xsl:apply-templates select="$freeTextValidationError">
							<xsl:with-param name="fieldName" select="$dropDownName"/>
						</xsl:apply-templates>
					</xsl:if>
				
				</fieldset>
				
			</article>
	
		</div>
		
		<script type="text/javascript">$(function(){initDropDownQuery('<xsl:value-of select="DropDownQueryInstance/DropDownQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredQuery']">
	
		<div id="{fieldName}-error" class="validationerror error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.RequiredQuery"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueRequired']">
	
		<xsl:param name="fieldName" select="null"/>
	
		<div id="{$fieldName}-error" class="validationerror error input-error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.FreeTextAlternativeValueRequired"/>
		</div>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey = 'FreeTextAlternativeValueToLong']">
	
		<xsl:param name="fieldName" select="null"/>
	
		<div id="{$fieldName}-error" class="validationerror error input-error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.FreeTextAlternativeToLong"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<xsl:param name="fieldName" select="null" />
		
		<div id="{$fieldName}-error" class="validationerror error input-error">
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
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>	
	
</xsl:stylesheet>