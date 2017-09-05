<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
			
			<ul class="list-style-type-none">
				<xsl:apply-templates select="FileUploadQueryInstance/Files/FileDescriptor" mode="show" />
			</ul>
			
			<p class="tiny"><xsl:value-of select="$i18n.AttachedFilesNoticed"/></p>
						
		</div>
		
	</xsl:template>		

	<xsl:template match="FileDescriptor" mode="show">

		<li>
			<img src="query://{../../QueryInstanceDescriptor/QueryDescriptor/queryID}/fileicon/.{FileExtension}" class="alignmiddle"/>
			
			<xsl:text>&#160;&#160;</xsl:text> 
			
			<strong>
				<xsl:value-of select="name"/>
			</strong>
			
			<xsl:text> (</xsl:text>
			
			<xsl:value-of select="FormatedSize"/>
			
			<xsl:text>)</xsl:text>
		</li>
	
	</xsl:template>	
	
</xsl:stylesheet>