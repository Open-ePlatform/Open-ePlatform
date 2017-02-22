<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="scripts">
		/js/userflowinstancemenumodule.js
	</xsl:variable>

	<xsl:template match="Document">

		<div id="MyPagesMenu" class="errand-menu buttons-in-desktop errand-page">

		  	<a href="{contextpath}{section/fullAlias}" class="btn btn-dark">
		  		<span data-icon-after="_" data-icon-before="L"><xsl:value-of select="section/name" /></span>
		  	</a>
	
		  	<ul>
		  		<xsl:apply-templates select="menuitem" />
		  	</ul>
	  	</div>

	</xsl:template>

	<xsl:template match="menuitem">

		<li>
			<xsl:choose>
				<xsl:when test="url">
					
					<a class="btn btn-light">
						<xsl:attribute name="href">
							<xsl:choose>
								<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
									<xsl:value-of select="/Document/contextpath"/>
									<xsl:value-of select="url"/>
								</xsl:when>
								
								<xsl:when test="urlType='FULL'">
									<xsl:value-of select="url"/>
								</xsl:when>
							</xsl:choose>
						</xsl:attribute>
						<span class="text" title="{description}">
							<xsl:attribute name="data-icon-before">
								<xsl:choose>
									<xsl:when test="name = 'Mina ärenden'">&#58894;</xsl:when>
									<xsl:when test="name = 'Mina uppgifter'">u</xsl:when>
									<xsl:when test="name = 'Mina engagemang' or name = 'Mina företag'">b</xsl:when>
									<xsl:otherwise>&gt;</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
							<xsl:value-of select="name" />
						</span>
					</a>
					
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:attribute name="class">no-url</xsl:attribute>
						
					<xsl:value-of select="name"/>
					
					<xsl:if test="itemType='BLANK'">
						<xsl:text>&#160;</xsl:text>
					</xsl:if>
								
				</xsl:otherwise>
			</xsl:choose>
		</li>

	</xsl:template>

</xsl:stylesheet>