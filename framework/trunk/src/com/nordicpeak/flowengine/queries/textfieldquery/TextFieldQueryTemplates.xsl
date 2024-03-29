<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
		/jquery/jquery-ui.js?v=1
		/timepicker/js/jquery.timepicker.js?v=1
	</xsl:variable>
	
	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
		/timepicker/css/jquery.timepicker.css
	</xsl:variable>

	<xsl:variable name="links">
		/css/spectrum.css
		/css/textfieldquery.css
	</xsl:variable>

	<xsl:variable name="scripts">
		/common/js/query.js
		/js/jquery.ui.datepicker-sv.js
		/js/spectrum.js
		/js/textfieldquery.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="TextFieldQueryInstance/TextFieldQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
				
					<xsl:if test="not(TextFieldQueryInstance/TextFieldQuery/hideTitle = 'true')">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="TextFieldQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="TextFieldQueryInstance/TextFieldQuery/queryID" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="TextFieldQueryInstance/TextFieldQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="TextFieldQueryInstance/TextFieldQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<fieldset>
					<xsl:apply-templates select="TextFieldQueryInstance/TextFieldQuery/Fields/TextField" mode="show"/>
				</fieldset>
				
			</article>
			
		</div>
		
	</xsl:template>		
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', TextFieldQueryInstance/TextFieldQuery/queryID)" />
		<xsl:variable name="locked" select="TextFieldQueryInstance/TextFieldQuery/lockForManagerUpdate = 'true' and RequestMetadata/manager = 'true'" />
	
		<div class="query textfieldquery" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query textfieldquery</xsl:text>
				<xsl:if test="TextFieldQueryInstance/TextFieldQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<xsl:if test="EnableAjaxPosting">
				<xsl:call-template name="loadAjaxValidationErrors"/>
			</xsl:if>
			
			<xsl:if test="ValidationErrors/validationError">
			
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
					
						<xsl:variable name="validationErrors" select="ValidationErrors/validationError[messageKey]"/>
						
						<xsl:choose>
							<xsl:when test="$validationErrors">
								
								<xsl:apply-templates select="$validationErrors" />
								
							</xsl:when>
							<xsl:otherwise>
								
								<span />
								
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
					
					<xsl:if test="not(TextFieldQueryInstance/TextFieldQuery/hideTitle = 'true')">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="TextFieldQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:if test="TextFieldQueryInstance/TextFieldQuery/helpText">
						<xsl:apply-templates select="TextFieldQueryInstance/TextFieldQuery/helpText" />
					</xsl:if>
					
				</div>
				
				<xsl:if test="TextFieldQueryInstance/TextFieldQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="TextFieldQueryInstance/TextFieldQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<fieldset>
					<xsl:apply-templates select="TextFieldQueryInstance/TextFieldQuery/Fields/TextField">
						<xsl:with-param name="locked" select="$locked" />
					</xsl:apply-templates>
				</fieldset>
		
			</article>
			
		</div>
		
		<script type="text/javascript">
			TextFieldQueryi18n = {
				colorChoose: "<xsl:value-of select="$i18n.js.ColorChoose"/>",
				colorCancel: "<xsl:value-of select="$i18n.js.ColorCancel"/>",
			}
					
			$(function(){initTextFieldQuery('<xsl:value-of select="TextFieldQueryInstance/TextFieldQuery/queryID" />');});
		</script>
		
	</xsl:template>
	
	<xsl:template match="TextField">
		<xsl:param name="locked" />

		<xsl:variable name="textFieldID" select="textFieldID"/>

		<xsl:variable name="odd" select="(position() mod 2) = 0" />

		<xsl:variable name="class">
			<xsl:if test="../../../../ValidationErrors/validationError[fieldName = $textFieldID]">
				<xsl:text>invalid input-error</xsl:text>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="formatValidator = 'se.unlogic.standardutils.validation.StringColorHEXValidator'">
					<xsl:text> color color-hex</xsl:text>
				</xsl:when>
				<xsl:when test="formatValidator = 'se.unlogic.standardutils.validation.StringColorRGBValidator'">
					<xsl:text> color color-rgb</xsl:text>
				</xsl:when>
				<xsl:when test="formatValidator = 'se.unlogic.standardutils.validation.StringColorHSLValidator'">
					<xsl:text> color color-hsl</xsl:text>
				</xsl:when>
				<xsl:when test="formatValidator = 'se.unlogic.standardutils.validation.StringColorHSVValidator'">
					<xsl:text> color color-hsv</xsl:text>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>

		<div class="split break {$class}">
			
			<xsl:choose>
				<xsl:when test="../../layout = 'FLOAT'">
					<xsl:attribute name="class">
						<xsl:text>split </xsl:text>
						<xsl:if test="$odd">odd </xsl:if>
						<xsl:value-of select="$class" />
					</xsl:attribute>
				</xsl:when>
				<xsl:when test="../../layout = 'NEW_LINE_FULL_WIDTH'">
					<xsl:attribute name="class">
						<xsl:text>split break full </xsl:text>
						<xsl:value-of select="$class" />
					</xsl:attribute>
				</xsl:when>
			</xsl:choose>

			<xsl:variable name="id">
				<xsl:value-of select="'q'"/>
				<xsl:value-of select="../../queryID"/>
				<xsl:value-of select="'_field'"/>
				<xsl:value-of select="textFieldID"/>
			</xsl:variable>

			<label>
				<xsl:if test="required = 'true'">
					<xsl:attribute name="class">required</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="for"><xsl:value-of select="$id" /></xsl:attribute>
				<xsl:value-of select="label"/>
			</label>

			<xsl:variable name="size">
				<xsl:choose>
					<xsl:when test="../../layout = 'NEW_LINE_FULL_WIDTH'">
						<!-- No size, defaults to 100% width -->
					</xsl:when>
					<xsl:when test="width">
						<xsl:value-of select="width"/>
					</xsl:when>
					<xsl:otherwise>
						<!-- Default size, maybe a future module setting? -->
						<xsl:text>50</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="$id" />
				<xsl:with-param name="name" select="$id" />
				<xsl:with-param name="title" select="label"/>
				<xsl:with-param name="value" select="../../../Values/TextFieldValue[TextField/textFieldID = $textFieldID]/value"/>
				<xsl:with-param name="requestparameters" select="../../../../requestparameters"/>
				<xsl:with-param name="size" select="$size"/>
				<xsl:with-param name="class" select="$class"/>
				<xsl:with-param name="placeholder"><xsl:if test="maskFieldContent = 'false'"><xsl:value-of select="placeholderText"/></xsl:if></xsl:with-param>
				<xsl:with-param name="type">
					<xsl:choose>
						<xsl:when test="maskFieldContent = 'true'">
							<xsl:text>password</xsl:text>
						</xsl:when>					
						<xsl:when test="formatValidator = 'se.unlogic.standardutils.populators.DatePopulator'">
							<xsl:text>date</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>text</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="disabled">
					<xsl:if test="disabled = 'true' or ../../../../Locked or ../../../../ValidationErrors/validationError[messageKey = 'APIRequestException'] or $locked">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-describedby">
					<xsl:value-of select="$id" />
					<xsl:value-of select="'_error'" />
				</xsl:with-param>
				<xsl:with-param name="aria-required"><xsl:if test="required = 'true'">true</xsl:if></xsl:with-param>
				<xsl:with-param name="autocomplete"><xsl:if test="maskFieldContent = 'true'">new-password</xsl:if></xsl:with-param>
			</xsl:call-template>
			
			<xsl:apply-templates select="../../../../ValidationErrors/validationError[fieldName = $textFieldID]">
				<xsl:with-param name="id"><xsl:value-of select="$id" /><xsl:value-of select="'_error'" /></xsl:with-param>
			</xsl:apply-templates>
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="TextField" mode="show">

		<xsl:variable name="odd" select="(position() mod 2) = 0" />

		<div>
			<xsl:if test="../../layout = 'FLOAT'">
				<xsl:attribute name="class">
					<xsl:text>split</xsl:text>
					<xsl:if test="$odd"> odd</xsl:if>
				</xsl:attribute>
			</xsl:if>
			<strong class="block">
				<xsl:value-of select="label"/>
				<xsl:if test="required = 'true'">
					<xsl:text> *</xsl:text>
				</xsl:if>
			</strong>
			
			<xsl:variable name="textFieldID" select="textFieldID"/>
			<xsl:variable name="value" select="../../../Values/TextFieldValue[TextField/textFieldID = $textFieldID]/value"/>
			
			<xsl:choose>
				<xsl:when test="$value">
				
					<xsl:choose>
						<xsl:when test="maskFieldContent = 'true'">
							<xsl:text>********</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$value"/>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>-</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'APIRequestException']">
		
		<xsl:param name="id" />
		
		<span>
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
			<xsl:value-of select="$i18n.APIRequestException" />
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<xsl:param name="id" />
		
		<i data-icon-after="!" title="{$i18n.RequiredField}" tabindex="0">
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooShort']">
		
		<xsl:param name="id" />
		
		<i data-icon-after="!" tabindex="0">
			<xsl:attribute name="title">
				<xsl:value-of select="$i18n.TooShortFieldContent.part1"/>
				<xsl:value-of select="currentLength"/>
				<xsl:value-of select="$i18n.TooShortFieldContent.part2"/>
				<xsl:value-of select="minLength"/>
				<xsl:value-of select="$i18n.TooShortFieldContent.part3"/>
			</xsl:attribute>
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<xsl:param name="id" />
		
		<i data-icon-after="!" tabindex="0">
			<xsl:attribute name="title">
				<xsl:value-of select="$i18n.TooLongFieldContent.part1"/>
				<xsl:value-of select="currentLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part2"/>
				<xsl:value-of select="maxLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part3"/>
			</xsl:attribute>
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<xsl:param name="id" />
		
		<i data-icon-after="!" tabindex="0">
			<xsl:attribute name="title">
				<xsl:choose>
					<xsl:when test="invalidFormatMessage">
						<xsl:value-of select="invalidFormatMessage"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="$i18n.InvalidFormat"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
		</i>
		
	</xsl:template>
		
	<xsl:template match="validationError">
		
		<xsl:param name="id" />
		
		<i data-icon-after="!" title="{$i18n.UnknownValidationError}" tabindex="0">
			<xsl:if test="$id"><xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute></xsl:if>
		</i>
		
	</xsl:template>
	
</xsl:stylesheet>