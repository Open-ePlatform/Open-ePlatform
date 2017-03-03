<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template name="showMapQueryPDFPreview">
		
		<xsl:param name="query" />
		<xsl:param name="queryInstance" />
	
		<div class="query">
	
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
		
			<div class="bigmarginbottom">
				
				<div>
					<strong><xsl:value-of select="$i18n.PropertyUnitDesignation" /><xsl:text>:&#160;</xsl:text></strong>
					<xsl:value-of select="$queryInstance/propertyUnitDesignation" />
				</div>
				
				<xsl:call-template name="showSpecificMapInformation" />
				
			</div>
		
		</div>
	
	</xsl:template>
	
	<xsl:template name="showSpecificMapInformation" />

</xsl:stylesheet>