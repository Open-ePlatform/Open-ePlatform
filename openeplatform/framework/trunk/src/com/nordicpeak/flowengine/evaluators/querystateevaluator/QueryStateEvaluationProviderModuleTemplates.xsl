<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:template match="Document">

		<div class="contentitem">

			<xsl:apply-templates select="UpdateQueryStateEvaluator" />
		</div>

	</xsl:template>

	<xsl:template match="UpdateQueryStateEvaluator">

		<h1>
			<xsl:value-of select="$i18n.UpdateEvaluatorDescriptor.title" />
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="QueryStateEvaluator/EvaluatorDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="QueryStateEvaluator" />
			</xsl:call-template>		

			<label class="floatleft">
				<xsl:value-of select="$i18n.selectionMode.title" />
			</label>

			<div class="clearboth floatleft marginbottom">
				<xsl:value-of select="$i18n.selectionMode.description" />
			</div>

			<div class="floatleft full marginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'any'" />
						<xsl:with-param name="name" select="'selectionMode'" />
						<xsl:with-param name="value" select="'ANY'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>

					<label for="any">
						<xsl:value-of select="$i18n.SelectionMode.ANY" />
					</label>
				</div>
			</div>

			<div class="floatleft full marginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'all'" />
						<xsl:with-param name="name" select="'selectionMode'" />
						<xsl:with-param name="value" select="'ALL'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>

					<label for="all">
						<xsl:value-of select="$i18n.SelectionMode.ALL" />
					</label>
				</div>
			</div>

			<label class="floatleft">
				<xsl:value-of select="$i18n.alternatives.title" />
			</label>

			<div class="clearboth floatleft marginbottom">
				<xsl:value-of select="$i18n.alternatives.description" />
			</div>

			<xsl:choose>
				<xsl:when test="Alternatives/Alternative">
					<xsl:apply-templates select="Alternatives/Alternative" />
					
					<xsl:if test="Alternatives/FreeTextAlternative">
					
						<div class="floatleft full marginbottom">
				
							<div class="floatleft full">
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="'freeTextAlternative'"/>
									<xsl:with-param name="name" select="'freeTextAlternative'" />
									<xsl:with-param name="value" select="'true'" />
									<xsl:with-param name="element" select="QueryStateEvaluator" />
								</xsl:call-template>
				
								<label for="freeTextAlternative">
									<xsl:value-of select="Alternatives/FreeTextAlternative" />
								</label>
							</div>
						</div>
					
					</xsl:if>
					
				</xsl:when>
				<xsl:otherwise>
					<p class="floatleft full">
						<xsl:value-of select="$i18n.QueryHasNoAlternatives" />
					</p>
				</xsl:otherwise>
			</xsl:choose>

			<label class="floatleft">
				<xsl:value-of select="$i18n.TargetQueryState.title" />
			</label>

			<div class="clearboth floatleft marginbottom">
				<xsl:value-of select="$i18n.TargetQueryState.description" />
			</div>

			<div class="floatleft full marginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'visible'" />
						<xsl:with-param name="name" select="'queryState'" />
						<xsl:with-param name="value" select="'VISIBLE'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>

					<label for="visible">
						<xsl:value-of select="$i18n.QueryState.VISIBLE" />
					</label>
				</div>
			</div>

			<div class="floatleft full marginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'visible_required'" />
						<xsl:with-param name="name" select="'queryState'" />
						<xsl:with-param name="value" select="'VISIBLE_REQUIRED'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>

					<label for="visible_required">
						<xsl:value-of select="$i18n.QueryState.VISIBLE_REQUIRED" />
					</label>
				</div>
			</div>

			<div class="floatleft full marginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'hidden'" />
						<xsl:with-param name="name" select="'queryState'" />
						<xsl:with-param name="value" select="'HIDDEN'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>

					<label for="hidden">
						<xsl:value-of select="$i18n.QueryState.HIDDEN" />
					</label>
				</div>
			</div>

			<div class="floatleft full marginbottom">
	
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'doNotResetQueryState'" />
						<xsl:with-param name="name" select="'doNotResetQueryState'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="QueryStateEvaluator" />
					</xsl:call-template>
	
					<label for="doNotResetQueryState">
						<xsl:value-of select="$i18n.doNotResetQueryState" />
					</label>
				</div>
			</div>

			<label class="floatleft">
				<xsl:value-of select="$i18n.TargetQueries.title" />
			</label>

			<div class="clearboth floatleft marginbottom">
				<xsl:value-of select="$i18n.TargetQueries.description" />
			</div>

			<div class="clearboth floatleft marginbottom">
				<ol>
					<xsl:apply-templates select="Flow/Steps/Step" mode="list"/>
				</ol>
			</div>

			<div class="floatright clearboth">
				<input type="submit" value="{$i18n.UpdateQueryStateEvaluator.submit}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template match="Step" mode="list">
		
		<li>
			<span class="font-weight-bold"><xsl:value-of select="name"/></span>
				
			<xsl:if test="QueryDescriptors/QueryDescriptor">
				<ol>
					<xsl:apply-templates select="QueryDescriptors/QueryDescriptor" mode="list"/>
				</ol>
			</xsl:if>
		</li>
	
	</xsl:template>

	<xsl:template match="QueryDescriptor" mode="list">
		
		<li>
			<xsl:variable name="queryID" select="queryID"/>
			
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id">
					<xsl:value-of select="'query'"/>
					<xsl:value-of select="queryID"/>
				</xsl:with-param>
				<xsl:with-param name="name" select="'queryID'" />
				<xsl:with-param name="value" select="queryID" />
				<xsl:with-param name="disabled" select="../../../../../DisabledQueries[queryID=$queryID]/queryID"/>
				<xsl:with-param name="element" select="../../../../../QueryStateEvaluator/EvaluatorDescriptor/TargetQueryIDs" />
				<xsl:with-param name="requestparameters" select="../../../../../requestparameters" />
			</xsl:call-template>

			<label for="query{queryID}">
				<xsl:value-of select="name" />
			</label>
		</li>
	
	</xsl:template>

	<xsl:template match="Alternative">

		<div class="floatleft full marginbottom">

			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id">
						<xsl:value-of select="'alternative'"/>
						<xsl:value-of select="alternativeID"/>
					</xsl:with-param>
					<xsl:with-param name="name" select="'alternativeID'" />
					<xsl:with-param name="value" select="alternativeID" />
					<xsl:with-param name="element" select="../../QueryStateEvaluator/RequiredAlternativeIDs" />
				</xsl:call-template>

				<label for="alternative{alternativeID}">
					<xsl:value-of select="name" />
				</label>
			</div>
		</div>

	</xsl:template>

	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validation.requiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validation.tooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.validation.tooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validation.unknownError" />
					</xsl:otherwise>
				</xsl:choose>

				<xsl:text>&#x20;</xsl:text>

				<xsl:choose>
					<xsl:when test="fieldName = 'selectionMode'">
						<xsl:value-of select="$i18n.selectionMode" />
					</xsl:when>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name" />
					</xsl:when>
					<xsl:when test="fieldName = 'queryState'">
						<xsl:value-of select="$i18n.queryState" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName" />
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>

		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.validation.unknownFault" />
			</p>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>