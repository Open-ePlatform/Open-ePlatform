<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<xsl:if test="not(CheckboxQueryInstance/CheckboxQuery/hideTitle = 'true')">
				<h2>
					<xsl:value-of select="CheckboxQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
		
		<xsl:if test="price > 0">
			<xsl:text>&#160;(</xsl:text>
			<xsl:value-of select="price"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$i18n.Currency"/>
			<xsl:text>)</xsl:text>
		</xsl:if>

		<xsl:if test="position() != last()"><br/></xsl:if>
	
	</xsl:template>

</xsl:stylesheet>