<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<a name="query{DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="DropDownQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
			</h2>
			
			<xsl:if test="Description">
				
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
				
			</xsl:if>
			
			<p>
				<xsl:choose>
					<xsl:when test="DropDownQueryInstance/DropDownAlternative">
						<xsl:value-of select="DropDownQueryInstance/DropDownAlternative/name"/>
					</xsl:when>
					<xsl:when test="DropDownQueryInstance/freeTextAlternativeValue">
						<xsl:value-of select="DropDownQueryInstance/freeTextAlternativeValue"/>
					</xsl:when>
				</xsl:choose>
			</p>	
					
		</div>
		
	</xsl:template>		

</xsl:stylesheet>