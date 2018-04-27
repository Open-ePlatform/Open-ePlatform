<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<xsl:if test="not(TextFieldQueryInstance/TextFieldQuery/hideTitle = 'true')">
				<h2>
					<xsl:value-of select="TextFieldQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
				</h2>
			</xsl:if>
			
			<xsl:if test="Description">
				
				<div class="query-description">
					<xsl:choose>
						<xsl:when test="isHTMLDescription = 'true'">
							<xsl:value-of select="Description" disable-output-escaping="yes"/>
						</xsl:when>
						<xsl:otherwise>
							<p>
								<xsl:value-of select="Description" disable-output-escaping="yes"/>
							</p>
						</xsl:otherwise>
					</xsl:choose>
				</div>
								
			</xsl:if>
			
			
			<div class="full display-table bigmarginbottom">
				<xsl:apply-templates select="TextFieldQueryInstance/TextFieldQuery/Fields/TextField" mode="show"/>
			</div>
		</div>
		
	</xsl:template>

	<xsl:template match="TextField" mode="show">

		<xsl:variable name="textFieldID" select="textFieldID"/>
		<xsl:variable name="value" select="../../../Values/TextFieldValue[TextField/textFieldID = $textFieldID]/value"/>

		<div>
			
			<xsl:choose>
				<xsl:when test="../../layout = 'FLOAT'">
					<xsl:attribute name="class">floatleft fifty bigmarginbottom</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class">floatleft full bigmarginbottom</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>				
			
			<strong><xsl:value-of select="label"/></strong>
			<br/>
			<xsl:choose>
				<xsl:when test="$value">
					<xsl:value-of select="$value"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>-</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	
	</xsl:template>	
	
</xsl:stylesheet>