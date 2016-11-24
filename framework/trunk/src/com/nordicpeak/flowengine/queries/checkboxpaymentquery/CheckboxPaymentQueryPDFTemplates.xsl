<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<a name="query{CheckboxPaymentQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="CheckboxPaymentQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
				<xsl:apply-templates select="CheckboxPaymentQueryInstance/Alternatives/CheckboxPaymentAlternative" mode="show"/>
		
				<xsl:if test="CheckboxPaymentQueryInstance/freeTextAlternativeValue">
					<br/><xsl:value-of select="CheckboxPaymentQueryInstance/freeTextAlternativeValue"/>
				</xsl:if>
			</p>	
			
		</div>
		
	</xsl:template>		

	<xsl:template match="CheckboxPaymentAlternative" mode="show">

		<xsl:value-of select="name" />		

		<xsl:if test="amount">
			<xsl:text>&#160;(</xsl:text><xsl:value-of select="amount" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.AmountUnit" /><xsl:text>)</xsl:text>
		</xsl:if>

		<xsl:if test="position() != last()"><br/></xsl:if>
	
	</xsl:template>

</xsl:stylesheet>