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
						<xsl:apply-templates select="ValidationErrors/validationError"/>
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
				
				<fieldset>
					
					<xsl:variable name="dropDownName">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="DropDownQueryInstance/DropDownQuery/queryID"/>
						<xsl:value-of select="'_alternative'"/>
					</xsl:variable>
				
					<div class="split">

						<xsl:variable name="selectedAlternativeID" select="DropDownQueryInstance/DropDownAlternative/alternativeID" />
					
						<select name="{$dropDownName}" style="width: 100%;">
							
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
			
					</div>
			
					<div class="freeTextAlternativeValue hidden">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="concat($dropDownName,'Value')" />
							<xsl:with-param name="name" select="concat($dropDownName,'Value')" />
							<xsl:with-param name="value" select="DropDownQueryInstance/freeTextAlternativeValue" />
							<xsl:with-param name="disabled" select="disabled" />
						</xsl:call-template>
					</div>
				
				</fieldset>
				
			</article>
	
		</div>
		
		<script type="text/javascript">$(document).ready(function(){initDropDownQuery('<xsl:value-of select="DropDownQueryInstance/DropDownQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredQuery']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.RequiredQuery"/>
			</strong>
		</span>
		
	</xsl:template>		
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>	
	
</xsl:stylesheet>