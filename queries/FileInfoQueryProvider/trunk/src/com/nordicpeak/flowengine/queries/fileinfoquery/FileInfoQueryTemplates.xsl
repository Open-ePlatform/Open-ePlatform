<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="scripts">
		/js/fileinfoquery.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/fileinfoquery.css
	</xsl:variable>
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideTitle = 'true'"> notitle</xsl:if>
			</xsl:attribute>
			
			<article class="infoquery show-mode">
				
				<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideBackground = 'true'">
					<xsl:attribute name="class">infoquery show-mode nobackground</xsl:attribute>
				</xsl:if>
				
				<div class="heading-wrapper">
				
					<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideTitle = 'false'">
						<h2>
							<xsl:value-of select="FileInfoQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
				</div>
				
				<xsl:if test="FileInfoQueryInstance/FileInfoQuery/description">
					<span class="italic">
						<xsl:value-of select="FileInfoQueryInstance/FileInfoQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<ul class="files preview">
					<xsl:apply-templates select="FileInfoQueryInstance/FileInfoQuery/Files/FileDescriptor" mode="show" />
				</ul>
							
			</article>
				
		</div>
		
	</xsl:template>	
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', FileInfoQueryInstance/FileInfoQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', FileInfoQueryInstance/FileInfoQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query fileinfoquery</xsl:text>
				<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideTitle = 'true'"> notitle</xsl:if>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
		
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<xsl:if test="ValidationErrors/validationError[messageKey]">
						<div class="info-box error">
							<xsl:apply-templates select="ValidationErrors/validationError[messageKey]"/>
							<div class="marker"></div>
						</div>
					</xsl:if>
				</div>
			</xsl:if>
	
			<article>
			
				<xsl:attribute name="class">
					<xsl:text>infoquery fileinfoquery</xsl:text>
				
					<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideBackground = 'true'">
						<xsl:text> nobackground</xsl:text>
					</xsl:if>				
					
					<xsl:if test="ValidationErrors/validationError[messageKey]">
						<xsl:text> error</xsl:text>
					</xsl:if>
					
				</xsl:attribute>
			
				<div class="heading-wrapper">
					
					<xsl:if test="FileInfoQueryInstance/FileInfoQuery/hideTitle = 'false'">
						<h2>
							<xsl:value-of select="FileInfoQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</xsl:if>
					
					<xsl:if test="FileInfoQueryInstance/FileInfoQuery/helpText">		
						<xsl:apply-templates select="FileInfoQueryInstance/FileInfoQuery/helpText" />
					</xsl:if>
				</div>
				
				<xsl:if test="FileInfoQueryInstance/FileInfoQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="FileInfoQueryInstance/FileInfoQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
				
				<div class="full">
				
					<ul class="files">
						<xsl:apply-templates select="FileInfoQueryInstance/FileInfoQuery/Files/FileDescriptor"/>
					</ul>
					
				</div>
				
				<script type="text/javascript">
					initFileInfoQuery('<xsl:value-of select="FileInfoQueryInstance/FileInfoQuery/queryID" />');
				</script>
				
			</article>			
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="FileDescriptor">
	
		<li class="finished">
			<div class="file">
				<span class="name">
					<a href="{/Document/ShowQueryForm/queryRequestURL}?file={fileID}" target="blank">
						<img src="{/Document/requestinfo/contextpath}{/Document/fullAlias}/fileicon/{name}" class="vertical-align-middle marginright" />
						<xsl:value-of select="name" />
					</a>
				</span>
				
				<span class="italic"><xsl:text>(</xsl:text><xsl:value-of select="FormatedSize" /><xsl:text>)</xsl:text></span>
			
			</div>
			<div class="progressbar">
				<div style="width: 100%;" class="innerbar"></div>
			</div>
		</li>
		
	</xsl:template>
	
	<xsl:template match="FileDescriptor" mode="show">
	
		<li class="finished">
		
			<a href="{/Document/ShowQueryValues/queryRequestURL}?file={fileID}" class="btn btn-file">
				<img src="{/Document/requestinfo/contextpath}{/Document/fullAlias}/fileicon/{name}" class="vertical-align-middle" /><xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="name"/><xsl:text>&#x20;</xsl:text><span class="size"><xsl:text>(</xsl:text><xsl:value-of select="FormatedSize" /><xsl:text>)</xsl:text></span>
			</a>
			
		</li>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<i data-icon-after="!" title="{$i18n.InvalidFormat}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
		
	</xsl:template>	
		
</xsl:stylesheet>