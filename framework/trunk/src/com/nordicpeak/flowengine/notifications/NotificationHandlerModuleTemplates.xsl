<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="links">
		/css/notificationhandler.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<xsl:apply-templates select="InlineNotifications"/>
		<xsl:apply-templates select="ListNotifications"/>

	</xsl:template>
	
	<xsl:template match="ListNotifications">
	
		<div id="NotificationHandlerModule" class="contentitem">
	
				<h1>
					<xsl:value-of select="$i18n.AllNotifications" />
				</h1>
	
				<xsl:choose>
					<xsl:when test="FlowInstances/FlowInstance">
					
						<xsl:apply-templates select="FlowInstances/FlowInstance" />
						
					</xsl:when>
					<xsl:otherwise>
					
						<article class="no-events">
							<xsl:value-of select="$i18n.NoNotifications"/>
						</article>
						
					</xsl:otherwise>
				</xsl:choose>
							
		</div>
	
	</xsl:template>
	
	<xsl:template match="InlineNotifications">
	
		<div class="heading-wrapper">
			<h3><xsl:value-of select="$i18n.Events" /></h3>
		</div>
		
		<xsl:choose>
			<xsl:when test="FlowInstances/FlowInstance">
			
				<xsl:apply-templates select="FlowInstances/FlowInstance" />
				
			</xsl:when>
			<xsl:otherwise>
			
				<article class="no-events">
					<xsl:value-of select="$i18n.NoNewNotifications"/>
				</article>
				
			</xsl:otherwise>
		</xsl:choose>
		
		<a href="{/Document/requestinfo/contextpath}{/Document/fullAlias}" class="bordered-link">
			<xsl:value-of select="$i18n.ShowAllEvents" />
			
			<xsl:if test="UnreadCount > 0">
				<xsl:text>&#160;(</xsl:text>
				<xsl:value-of select="UnreadCount" />
				<xsl:text>)</xsl:text>
			</xsl:if>
		</a>
		
		<div class="unread-count" style="display: none">
			<xsl:value-of select="UnreadCount" />
		</div>
	
	</xsl:template>
	
	<xsl:template match="FlowInstance">
		
		<xsl:variable name="url" select="../../Notifications/Notification[flowInstanceID = current()/flowInstanceID]/NotificationExtra/showURL" />
		
		<article data-url="{$url}">
			<div class="inner">
			
				<a href="{$url}">
					<h3>
						<xsl:value-of select="$i18n.FlowInstance" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="flowInstanceID" />
					</h3>
				</a>
				
				<xsl:apply-templates select="../../Notifications/Notification[flowInstanceID = current()/flowInstanceID]">
					<xsl:with-param name="flowInstance" select="."/>
				</xsl:apply-templates>
				
			</div>
		</article>
	
	</xsl:template>
	
	<xsl:template match="Notification">
		<xsl:param name="flowInstance"/>
		
		<a class="notification" href="{NotificationExtra/url}">
			
			<xsl:if test="not(seen)">
				<xsl:attribute name="class">notification unread</xsl:attribute>
			</xsl:if>
			
			<span class="bullet"><i/></span>
			
			<xsl:value-of select="title" />
			
			<span class="author">

				<xsl:value-of select="NotificationExtra/poster/user/firstname" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="NotificationExtra/poster/user/lastname" />
					
				<xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="added" />
			</span>
			
		</a>
	
	</xsl:template>
	
</xsl:stylesheet>