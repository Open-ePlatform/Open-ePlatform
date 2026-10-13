<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/textareaquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/textareaquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		
		<div>
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
					
					<xsl:if test="TextAreaQueryInstance/TextAreaQuery/hideTitle = 'false'">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="TextAreaQueryInstance/TextAreaQuery/queryID" />
						<xsl:with-param name="queryName" select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/description">
					<span class="italic">
						<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:call-template name="replaceLineBreaksWithParagraph">
					<xsl:with-param name="string" select="TextAreaQueryInstance/value"/>
				</xsl:call-template>
				
			</article>
				
		</div>
		
	</xsl:template>	
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', TextAreaQueryInstance/TextAreaQuery/queryID)" />
		
		<xsl:variable name="validationError" select="ValidationErrors/validationError"/>
	
		<div id="{$queryID}" data-keepalive-url="{/Document/requestinfo/contextpath}{/Document/fullAlias}/keepalive">
			<xsl:attribute name="class">
				<xsl:text>query textareaquery</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/showLetterCount = 'true' and TextAreaQueryInstance/TextAreaQuery/maxLength"> showlettercount</xsl:if>
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
					<xsl:attribute name="class">error input-error floatnone</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<xsl:if test="TextAreaQueryInstance/TextAreaQuery/hideTitle = 'false'">
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:if test="TextAreaQueryInstance/TextAreaQuery/helpText">		
						<xsl:apply-templates select="TextAreaQueryInstance/TextAreaQuery/helpText" />
					</xsl:if>
				
				</div>

				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<xsl:if test="TextAreaQueryInstance/lockedMessage">
					<p class="full">
						<xsl:value-of select="TextAreaQueryInstance/lockedMessage"/>
					</p>
				</xsl:if>
				
				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/showLetterCount = 'true' and TextAreaQueryInstance/TextAreaQuery/maxLength">
					<div class="floatleft marginbottom lettercounter" data-maxlength="{TextAreaQueryInstance/TextAreaQuery/maxLength}" />
				</xsl:if>
				
				<xsl:variable name="textAreaName">
					<xsl:value-of select="'q'"/>
					<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/queryID"/>
					<xsl:value-of select="'_value'"/>
				</xsl:variable>
				
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="$textAreaName"/>
					<xsl:with-param name="name" select="$textAreaName"/>
					
					<xsl:with-param name="rows">
						<xsl:choose>
							<xsl:when test="TextAreaQueryInstance/TextAreaQuery/rows">
								<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/rows"/>
							</xsl:when>
							<xsl:otherwise>5</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					
					<xsl:with-param name="title" select="TextAreaQueryInstance/TextAreaQuery/name"/>
					<xsl:with-param name="value" select="TextAreaQueryInstance/value"/>
					<xsl:with-param name="width" select="'98%'"/>
					<xsl:with-param name="disabled" select="Locked or TextAreaQueryInstance/locked = 'true'"/>
					<xsl:with-param name="aria-label" select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					<xsl:with-param name="class">
						<xsl:if test="$validationError[messageKey or validationErrorType = 'InvalidFormat']">
							<xsl:text>input-error</xsl:text>
						</xsl:if>
					</xsl:with-param>
					<xsl:with-param name="aria-describedby">
						<xsl:if test="$validationError[messageKey or validationErrorType = 'InvalidFormat']">
							<xsl:value-of select="$textAreaName"/>
							<xsl:text>-error</xsl:text>
						</xsl:if>
					</xsl:with-param>
					<xsl:with-param name="aria-invalid">
						<xsl:if test="$validationError[messageKey or validationErrorType = 'InvalidFormat']">
							<xsl:text>true</xsl:text>
						</xsl:if>
					</xsl:with-param>
				</xsl:call-template>
				
				<div class="clearfix"/>
				
				<xsl:apply-templates select="$validationError">
					<xsl:with-param name="fieldName" select="$textAreaName"/>
				</xsl:apply-templates>
				
			</article>
			
			<script type="text/javascript">
				$(function(){
					initTextAreaQuery(
						'<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/queryID" />',
						<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/keepalive" />,
						<xsl:value-of select="KeepalivePollFrequency" />
					);
				});
			</script>
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'TooLongFieldContent']">
		
		<xsl:param name="fieldName" select="null"/>
		
		<div id="{$fieldName}-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.TooLongFieldContent.part1"/>
			<xsl:value-of select="currentLength"/>
			<xsl:value-of select="$i18n.TooLongFieldContent.part2"/>
			<xsl:value-of select="maxLength"/>
			<xsl:value-of select="$i18n.TooLongFieldContent.part3"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredField']">
	
		<xsl:param name="fieldName" select="null"/>
		
		<div id="{$fieldName}-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			
			<xsl:value-of select="$i18n.RequiredField"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<xsl:param name="fieldName" select="null" />
		
		<div id="{$fieldName}-error" class="validationerror">
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