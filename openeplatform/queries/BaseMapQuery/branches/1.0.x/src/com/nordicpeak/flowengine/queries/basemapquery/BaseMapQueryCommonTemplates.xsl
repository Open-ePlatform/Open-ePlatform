<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template name="setMapClientInstanceLanguages">
		
		<script type="text/javascript">
			mapClientLanguages = {
				'DIMENSION_AND_ANGLE_SETTINGS' : '<xsl:value-of select="$i18n.DimensionAndAngleSettings" />'
			};
		</script>
		
	</xsl:template>

	<xsl:template name="createMapQueryCommonFieldsForm">
	
		<xsl:param name="element" />
	
		<div class="floatleft full bigmarginbottom">
			<label for="startInstruction" class="floatleft clearboth"><xsl:value-of select="$i18n.StartInstruction" /></label>
			<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.StartInstructionDescription" /></div>
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'startInstruction'" />
					<xsl:with-param name="name" select="'startInstruction'" />
					<xsl:with-param name="title" select="$i18n.StartInstruction"/>
					<xsl:with-param name="class" select="'ckeditor'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
			</div>
		</div>
	
	</xsl:template>

	<xsl:template name="createScaleDropDown">
		
		<xsl:param name="id" />
		<xsl:param name="name" />
		<xsl:param name="selectedValue" select="null" />
		<xsl:param name="requestparameters" select="requestparameters" />
		
		<select name="{$name}">
			<option value="500">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'500'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 500
			</option>
			<option value="1000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'1000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 1000
			</option>
			<option value="2000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'2000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 2000
			</option>
			<option value="4000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'4000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 4000
			</option>
			<option value="8000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'8000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 8000
			</option>
			<option value="16000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'16000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 16000
			</option>
			<option value="48000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'48000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 48000
			</option>
			<option value="144000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'144000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 144000
			</option>
			<option value="512000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'512000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 512000
			</option>
			<option value="1024000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'1024000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 1024000
			</option>
		</select>
		
	</xsl:template>
	
	<xsl:template name="checkIfSelected">
		
		<xsl:param name="name" />
		<xsl:param name="value" />
		<xsl:param name="selectedValue" />
		<xsl:param name="requestparameters" select="requestparameters" />
		
		<xsl:choose>
			<xsl:when test="$requestparameters">
				<xsl:if test="$requestparameters/parameter[name=$name]/value = $value">
					<xsl:attribute name="selected"/>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$value = $selectedValue">
					<xsl:attribute name="selected" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToValidatePUD']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToValidatePUD" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='PUDNotValid']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.PUDNotValid" />
			</strong>
		</span>
		
	</xsl:template>
		
	<xsl:template match="validationError[messageKey='InCompleteMapQuerySubmit']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.InCompleteMapQuerySubmit" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToGeneratePNG']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToGeneratePNG" />
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[fieldName='startInstruction']">

		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.startInstruction" />
			</strong>
		</span>
	
	</xsl:template>
		
</xsl:stylesheet>