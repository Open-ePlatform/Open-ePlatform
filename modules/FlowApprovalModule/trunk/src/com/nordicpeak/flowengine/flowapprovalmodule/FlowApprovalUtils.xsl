<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="replaceLineBreakAndHref">
		<xsl:param name="string"/>
		<xsl:variable name="positionHref" select="string-length(substring-before($string,'&lt;a href='))"/>
		<xsl:variable name="positionNewLine" select="string-length(substring-before($string,'&#13;'))"/>
		<xsl:choose>
			<xsl:when test="($positionHref&lt;$positionNewLine and contains($string,'&lt;a href=')) ">
				<a href="">
					<xsl:attribute name="href">
						<xsl:value-of select="substring-before(substring-after($string,'&lt;a href='), '&gt;')"/>
					</xsl:attribute>
					<xsl:value-of select="substring-before(substring-after($string,'&gt;'),'&lt;/a&gt;')"/>
				</a>
				<xsl:call-template name="replaceLineBreakAndHref">
					<xsl:with-param name="string" select="substring-after($string,'&lt;/a&gt;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="contains($string,'&#13;')">
				<xsl:value-of select="substring-before($string,'&#13;')"/>
				<br/>
				<xsl:call-template name="replaceLineBreakAndHref">
					<xsl:with-param name="string" select="substring-after($string,'&#13;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="contains($string,'&lt;a href=')">
						<a href="">
							<xsl:attribute name="href">
								<xsl:value-of select="substring-before(substring-after($string,'&lt;a href='), '&gt;')"/>
							</xsl:attribute>
							<xsl:value-of select="substring-before(substring-after($string,'&gt;'),'&lt;/a&gt;')"/>
						</a>
						<xsl:call-template name="replaceLineBreakAndHref">
							<xsl:with-param name="string" select="substring-after($string,'&lt;/a&gt;')"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$string"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>