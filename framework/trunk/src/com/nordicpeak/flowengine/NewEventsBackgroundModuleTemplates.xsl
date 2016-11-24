<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/neweventsmodule.js
	</xsl:variable>

	<xsl:template match="Document">		
		
		<xsl:variable name="eventCount" select="count(FlowInstance)" />
		<xsl:variable name="nrOfEvents" select="nrOfEvents" />
		
		<ul class="right">
			<li id="newEventsMenu">
				<div class="marker"></div>
				<a href="#" class="submenu-trigger">
					<i class="xl">n</i>
					<xsl:if test="$eventCount > 0">
						<span class="count"><xsl:value-of select="$eventCount" /></span>
					</xsl:if>
				</a>
				<div class="submenu notification-menu" style="display: none;">
					<div class="heading-wrapper">
						<h3><xsl:value-of select="$i18n.Events" /></h3>
					</div>
					<xsl:choose>
						<xsl:when test="$eventCount > 0">
							<xsl:apply-templates select="FlowInstance[$nrOfEvents >= position()]" />
							<a href="{contextpath}{userFlowInstanceModuleAlias}" class="bordered-link"><xsl:value-of select="$i18n.ShowAllEvents" /><xsl:text>&#160;</xsl:text>(<xsl:value-of select="$eventCount" />)</a>
						</xsl:when>
						<xsl:otherwise>
							<article class="no-events"><xsl:value-of select="$i18n.NoNewEvents"></xsl:value-of></article>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</li>
		</ul>
		
	</xsl:template>
	
	<xsl:template match="FlowInstance">
		
		<article class="unread">
			<a href="{/Document/contextpath}{/Document/userFlowInstanceModuleAlias}/overview/{Flow/flowID}/{flowInstanceID}">
				<span class="bullet"><i></i></span>
				<div class="inner">
					<h3><xsl:value-of select="$i18n.FlowInstance" /><xsl:text>&#160;</xsl:text><xsl:value-of select="flowInstanceID" /></h3>
					<xsl:for-each select="events/FlowInstanceEvent">
						<div>
							<xsl:if test="position() != last()">
								<xsl:attribute name="class">paddingbottom</xsl:attribute>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="statusDescription"><xsl:value-of select="statusDescription" /></xsl:when>
								<xsl:otherwise><xsl:value-of select="status" /></xsl:otherwise>
							</xsl:choose>
							<span class="author">
							
								<xsl:choose>
									<xsl:when test="../../Flow/hideManagerDetails = 'true'">
									
										<xsl:value-of select="$i18n.PostedByManager"/>
									
									</xsl:when>
									<xsl:otherwise>
			
										<xsl:value-of select="poster/user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="poster/user/lastname" />
									
									</xsl:otherwise>
								</xsl:choose>							
							
								<xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="added" />
							</span>
						</div>
					</xsl:for-each>
				</div>
			</a>
		</article>
	
	</xsl:template>
	
</xsl:stylesheet>