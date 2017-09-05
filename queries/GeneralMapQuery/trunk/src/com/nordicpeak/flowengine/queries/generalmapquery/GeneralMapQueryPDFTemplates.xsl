<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
	
		<xsl:variable name="queryInstance" select="GeneralMapQueryInstance" />
	
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="$queryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
	
			<a name="query{$queryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
				
			<h2>
				<xsl:value-of select="$queryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
			</h2>
		
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
		
			<div class="marginbottom">
				<img src="query://{$queryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}/map/{$queryInstance/queryInstanceID}" width="100%" />
			</div>
		
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>