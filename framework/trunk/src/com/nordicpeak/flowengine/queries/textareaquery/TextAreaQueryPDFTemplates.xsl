<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<xsl:if test="TextAreaQueryInstance/TextAreaQuery/hideTitle = 'false'">
				<h2>
					<xsl:value-of select="TextAreaQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
				</h2>
			</xsl:if>
			
			<xsl:if test="Description and TextAreaQueryInstance/TextAreaQuery/hideDescriptionInPDF = 'false'">

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
			
			<xsl:call-template name="replaceLineBreaksWithParagraph">
				<xsl:with-param name="string" select="TextAreaQueryInstance/value"/>
			</xsl:call-template>	
					
		</div>
		
	</xsl:template>		

</xsl:stylesheet>