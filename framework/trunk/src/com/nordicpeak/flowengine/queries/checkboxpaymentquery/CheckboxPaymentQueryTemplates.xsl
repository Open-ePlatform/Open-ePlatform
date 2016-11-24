<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/checkboxpaymentquery.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
	
		<div class="query">
		
			<article>
		
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="CheckboxPaymentQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="CheckboxPaymentQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<!-- <xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/queryID" />
					</xsl:call-template> -->
				</div>
				
				<xsl:if test="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:apply-templates select="CheckboxPaymentQueryInstance/Alternatives/CheckboxPaymentAlternative" mode="show"/>
	
				<xsl:if test="CheckboxPaymentQueryInstance/freeTextAlternativeValue">
					<div class="alternative"><xsl:value-of select="CheckboxPaymentQueryInstance/freeTextAlternativeValue"/></div>
				</xsl:if>
				
			</article>
				
		</div>	
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', CheckboxPaymentQueryInstance/CheckboxPaymentQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
	
			<xsl:if test="EnableAjaxPosting">
				<xsl:attribute name="class">query enableAjaxPosting</xsl:attribute>
			</xsl:if>
	
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
							<xsl:if test="CheckboxPaymentQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="CheckboxPaymentQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/helpText">		
						<xsl:apply-templates select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/helpText" />
					</xsl:if>
					
				</div>
				
				<xsl:if test="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>				
				
				<xsl:apply-templates select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/Alternatives/CheckboxPaymentAlternative" mode="form"/>
	
				<xsl:if test="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/freeTextAlternative">
				
					<xsl:variable name="freeTextAlternativeName">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/queryID"/>
						<xsl:value-of select="'_freeTextAlternative'"/>
					</xsl:variable>
				
					<div class="alternative">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="$freeTextAlternativeName" />
							<xsl:with-param name="name" select="$freeTextAlternativeName" />
							<xsl:with-param name="class" select="'vertical-align-bottom freeTextAlternative'" />
							<xsl:with-param name="checked">
								<xsl:if test="CheckboxPaymentQueryInstance/freeTextAlternativeValue">true</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<label for="{$freeTextAlternativeName}" class="checkbox"><xsl:value-of select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/freeTextAlternative" /></label>
					</div>
				
					<div class="freeTextAlternativeValue hidden">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="concat($freeTextAlternativeName,'Value')" />
							<xsl:with-param name="name" select="concat($freeTextAlternativeName,'Value')" />
							<xsl:with-param name="disabled" select="disabled" />
							<xsl:with-param name="value" select="CheckboxPaymentQueryInstance/freeTextAlternativeValue" />
						</xsl:call-template>
					</div>
					
				</xsl:if>
				
			</article>
	
		</div>
		
		<script type="text/javascript">$(document).ready(function(){initCheckBoxQuery('<xsl:value-of select="CheckboxPaymentQueryInstance/CheckboxPaymentQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="CheckboxPaymentAlternative" mode="form">

		<div class="alternative">
	
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
				<xsl:with-param name="class" select="'vertical-align-bottom'" />
				<xsl:with-param name="value" select="alternativeID" />
				<xsl:with-param name="elementName" select="'alternativeID'" />
				<xsl:with-param name="element" select="../../../Alternatives/CheckboxPaymentAlternative[alternativeID = $alternativeID]" />
				<xsl:with-param name="requestparameters" select="../../../../requestparameters"/>
			</xsl:call-template>
			<label for="{$checkboxID}" class="checkbox">
				<xsl:value-of select="name" />
				<xsl:if test="amount">
					<xsl:text>&#160;(</xsl:text><xsl:value-of select="amount" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.AmountUnit" /><xsl:text>)</xsl:text>
				</xsl:if>
			</label>
	
		</div>
	
	</xsl:template>
	
	<xsl:template match="CheckboxPaymentAlternative" mode="show">

		<div class="alternative">
	
			<xsl:value-of select="name" />		
	
			<xsl:if test="amount">
				<xsl:text>&#160;(</xsl:text><xsl:value-of select="amount" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.AmountUnit" /><xsl:text>)</xsl:text>
			</xsl:if>
	
		</div>
	
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey = 'TooManyAlternativesSelected']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.TooManyAlternativesSelected.part1"/>
				<xsl:value-of select="maxChecked"/>
				<xsl:value-of select="$i18n.TooManyAlternativesSelected.part2"/>
				<xsl:value-of select="checked"/>
				<xsl:value-of select="$i18n.TooManyAlternativesSelected.part3"/>
			</strong>
		</span>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey = 'TooFewAlternativesSelected']">
		
		<span>
			<strong data-icon-before="!">
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part1"/>
			<xsl:value-of select="minChecked"/>
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part2"/>
			<xsl:value-of select="checked"/>
			<xsl:value-of select="$i18n.TooFewAlternativesSelected.part3"/>
			</strong>
		</span>
		
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