<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<a name="query{CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
				<xsl:apply-templates select="CheckboxQueryInstance/Alternatives/CheckboxAlternative" mode="show"/>
		
				<xsl:if test="CheckboxQueryInstance/freeTextAlternativeValue">
					<br/><xsl:value-of select="CheckboxQueryInstance/freeTextAlternativeValue"/>
				</xsl:if>
			</p>	
			
		</div>
		
	</xsl:template>		

	<xsl:template match="CheckboxAlternative" mode="show">

		<xsl:value-of select="name" />		

		<xsl:if test="position() != last()"><br/></xsl:if>
	
	</xsl:template>

</xsl:stylesheet>