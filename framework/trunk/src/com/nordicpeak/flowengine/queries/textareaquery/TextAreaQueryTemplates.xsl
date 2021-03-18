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
	
		<div id="{$queryID}" data-keepalive-url="{/Document/requestinfo/contextpath}{/Document/fullAlias}/keepalive">
			<xsl:attribute name="class">
				<xsl:text>query textareaquery</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/showLetterCount = 'true' and TextAreaQueryInstance/TextAreaQuery/maxLength"> showlettercount</xsl:if>
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
				
				<xsl:if test="TextAreaQueryInstance/TextAreaQuery/showLetterCount = 'true' and TextAreaQueryInstance/TextAreaQuery/maxLength">
					<div class="floatleft marginbottom lettercounter" data-maxlength="{TextAreaQueryInstance/TextAreaQuery/maxLength}" />
				</xsl:if>
				
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="name">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="TextAreaQueryInstance/TextAreaQuery/queryID"/>
						<xsl:value-of select="'_value'"/>
					</xsl:with-param>
					
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
					<xsl:with-param name="disabled" select="Locked"/>
					<xsl:with-param name="aria-label" select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
				</xsl:call-template>
				
			</article>
			
			<script type="text/javascript">
				$(document).ready(function(){
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
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.TooLongFieldContent.part1"/>
				<xsl:value-of select="currentLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part2"/>
				<xsl:value-of select="maxLength"/>
				<xsl:value-of select="$i18n.TooLongFieldContent.part3"/>
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredField']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.RequiredField"/>
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