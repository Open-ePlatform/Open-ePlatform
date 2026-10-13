<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/js/focus-trapper.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/notificationsmodule.js
	</xsl:variable>

	<xsl:template match="Document">
	
		<ul class="right" aria-expanded="false" aria-controls="notificationsMenu" aria-label="{$i18n.Events}">
			<li id="notificationsMenu">
			
				<script>
					var notificationHandlerUrl = '<xsl:value-of select="NotificationHandlerURL"/>';
				</script>
			
				<div class="marker"></div>
				
				<a href="#" class="submenu-trigger">
					<i class="xl" aria-hidden="true">n</i>
					
					<xsl:if test="UnreadCount > 0">
						<span class="count"><xsl:value-of select="UnreadCount" /></span>
					</xsl:if>
				</a>
				
				<div class="submenu notification-menu" style="display: none;"/>
				
				<div id="notifications-loading" style="display: none;">
					<div class="heading-wrapper">
						<h3><xsl:value-of select="$i18n.Events" /></h3>
					</div>
					
					<article>
						<div class="inner">
							<img style="margin-right: 5px;" src="{/Document/requestinfo/contextpath}/static/b/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/loading.gif" alt="" />
							<xsl:value-of select="$i18n.LoadingNotifications"/>
						</div>
					</article>
				</div>
				
				<div id="notifications-loading-error" style="display: none;">
					<div class="heading-wrapper">
						<h3><xsl:value-of select="$i18n.Events" /></h3>
					</div>
					
					<article>
						<div class="inner">
							<xsl:value-of select="$i18n.LoadingNotifications.Error"/>
						</div>
					</article>
				</div>
				
				<div id="notifications-login-error" style="display: none;">
					<div class="heading-wrapper">
						<h3><xsl:value-of select="$i18n.Events" /></h3>
					</div>
					
					<article>
						<div class="inner">
							<xsl:value-of select="$i18n.LoadingNotifications.LoginError"/>
						</div>
					</article>
				</div>
				
			</li>
		</ul>
		
	</xsl:template>
	
</xsl:stylesheet>