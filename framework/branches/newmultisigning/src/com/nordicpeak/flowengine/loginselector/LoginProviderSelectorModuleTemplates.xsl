<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="ProviderConfiguration">
	
		<div class="loginprovider bigmargintop">
			
			<xsl:value-of select="description" disable-output-escaping="yes"/>
			
			<form method="GET" action="{/Document/requestinfo/contextpath}{../../FullAlias}/login">
			
				<xsl:if test="../../Redirect">
					<input type="hidden" name="redirect" value="{../../Redirect}"/>
				</xsl:if>
			
				<input type="hidden" name="provider" value="{providerID}"/>
			
				<input type="submit" value="{buttonText}" class="btn btn-blue"/>
			
			</form>
			
		</div>
	
	</xsl:template>

</xsl:stylesheet>