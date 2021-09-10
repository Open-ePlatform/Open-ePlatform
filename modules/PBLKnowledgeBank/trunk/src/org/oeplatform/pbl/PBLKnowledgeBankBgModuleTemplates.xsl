<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>	
	
	<xsl:variable name="scripts">
		/js/pbl.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/pbl.css
	</xsl:variable>
	
	<xsl:template match="Document">
				
		<script type="text/javascript">
			pblProxyModuleURI = '<xsl:value-of select="pblProxyModuleURI" />';
			pblLanguage = {
				'GETTING_WORD_DESCRIPTION': '<xsl:value-of select="$i18n.GettingWordDescription" />',
				'NO_WORD_DESCRIPTION_FOUND': '<xsl:value-of select="$i18n.NoWordDescriptionFound" />'
			};
		</script>
	 
	</xsl:template>
	
</xsl:stylesheet>