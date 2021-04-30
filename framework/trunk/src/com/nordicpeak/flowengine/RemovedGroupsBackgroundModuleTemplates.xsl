<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/featherlight/js/featherlight.min.js
	</xsl:variable>

	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
		/featherlight/css/featherlight.min.css
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/removedgroups.js
	</xsl:variable>

	<xsl:template match="Document">		
		
		<xsl:apply-templates select="RemovedGroups" />
		
	</xsl:template>
	
	<xsl:template match="RemovedGroups">
	
		<section class="modal info">
		
			<xsl:value-of select="$i18n.RemovedGroups.Message"/>
			
			<xsl:text> </xsl:text>
			
			<a href="#" onclick="showRemovedGroupsDialog(); return false;" title="{$i18n.RemovedGroups.Message.Link}">
				<xsl:value-of select="$i18n.RemovedGroups.Message.Link"/>
			</a>

			
		</section>
		
		<div class="hidden">
			<div id="removedGroupsModal" class="contentitem" style="max-width: 500px">

				<h1><xsl:value-of select="$i18n.RemovedGroups.Modal.Title"/></h1>
			
				<p>
					<xsl:value-of select="$i18n.RemovedGroups.Modal.Description"/>
				
					<ul>
						<xsl:apply-templates select="group" />
					</ul>
					
					<xsl:value-of select="$i18n.RemovedGroups.Modal.Footer"/>
				</p>
				
				<p>
					<div style="text-align: center;">
						<input type="button" value="{$i18n.Close}" onClick="$.featherlight.current().close();"/>
					</div>							
				</p>
			</div>

		</div>
	
	</xsl:template>
	
	<xsl:template match="group">

		<li>
			<xsl:value-of select="name"/>
		</li>
	
	</xsl:template>
	
</xsl:stylesheet>