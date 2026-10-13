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
			<option value="200">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'200'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 200
			</option>
			<option value="400">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'400'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 400
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
			<option value="5000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'5000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 5000
			</option>
			<option value="10000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'10000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 10000
			</option>
			<option value="15000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'15000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 15000
			</option>
			<option value="25000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'25000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 25000
			</option>
			<option value="50000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'50000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 50000
			</option>
			<option value="100000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'100000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 100000
			</option>
			<option value="250000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'250000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 250000
			</option>
			<option value="500000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'500000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 500000
			</option>
			<option value="1000000">
				<xsl:call-template name="checkIfSelected">
					<xsl:with-param name="name" select="$name" />
					<xsl:with-param name="value" select="'1000000'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
				1: 1000000
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