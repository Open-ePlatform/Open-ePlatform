<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

 	<xsl:template name="FlowFamilyList">
	
		<xsl:param name="name" select="null" />
		<xsl:param name="requestparameters" select="requestparameters" />
		<xsl:param name="connectorURL" />
		<xsl:param name="placeholder" select="$i18n.flowfamilylist.SearchFlow" />
		<xsl:param name="document" select="/Document" />
		<xsl:param name="searchfieldclass" select="''" />
		<xsl:param name="useExternalIcons" select="null" />
		<xsl:param name="flowfamilylist" select="flowfamilylist" />
		
		
		<ul class="list-style-type-none margintop flowfamily-list" id="{$name}-list">
			<input type="hidden" name="prefix" disabled="disabled" value="{$name}"/>
			<input type="hidden" name="connectorURL" disabled="disabled" value="{$connectorURL}"/>
			
					<xsl:apply-templates select="$requestparameters/parameter[name=$name]/value" mode="flowfamily">
						<xsl:with-param name="requestparameters" select="$requestparameters" />
						<xsl:with-param name="prefix" select="$name" />
						<xsl:with-param name="document" select="$document" />
						<xsl:with-param name="useExternalIcons" select="$useExternalIcons"/>
					</xsl:apply-templates>
			
			
				<xsl:for-each select="$flowfamilylist/FlowFamily">
				<xsl:variable name="id" select="FlowFamilyID"></xsl:variable>
				
					<li id="{$name}_{$id}" class="flowfamily-list-entry">
						<input type="hidden" name="{$name}ID" value="{$id}"/>
						
						
						<xsl:call-template name="flowfamily-list-extension-defaults">
							 <xsl:with-param name="listname" select="$name"/>
						</xsl:call-template>
						
						
						
						<xsl:text>&#x20;</xsl:text>
						
						<span class="text"/>
						
						
						<xsl:value-of select="FlowFamilyName"/>
						
		
						<xsl:choose>
							<xsl:when test="$useExternalIcons">
								<a class="delete floatright" href="#" title="{$i18n.flowfamilylist.DeleteFlow}:">
									<i class="icons icon-delete"></i>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="floatright delete" href="#" title="{$i18n.flowfamilylist.DeleteFlow}:">
									<img class="vertical-align-middle" src="{$document/requestinfo/contextpath}/static/f/{$document/module/sectionID}/{$document/module/moduleID}/pics/delete.png" alt="{$i18n.flowfamilylist.DeleteFlow}" />
								</a>
							</xsl:otherwise>
						</xsl:choose>
						
						
						
						<xsl:call-template name="flowfamily-list-extension-buttons">
							<xsl:with-param name="listname" select="$name"/>
						</xsl:call-template>
					</li>
			
			</xsl:for-each>
			
			
			<li id="{$name}-template" class="hidden">
				<input type="hidden" name="{$name}ID-template" disabled="disabled"/>
				
				<xsl:call-template name="flowfamily-list-extension-defaults">
					 <xsl:with-param name="listname" select="$name"/>
				</xsl:call-template>
				
				
				
				<xsl:text>&#x20;</xsl:text>
				
				<span class="text"/>
				
				
				<xsl:value-of select="FlowFamilyName"/>
				

				<xsl:choose>
					<xsl:when test="$useExternalIcons">
						<a class="delete floatright" href="#" title="{$i18n.flowfamilylist.DeleteFlow}:">
							<i class="icons icon-delete"></i>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a class="floatright delete" href="#" title="{$i18n.flowfamilylist.DeleteFlow}:">
							<img class="vertical-align-middle" src="{$document/requestinfo/contextpath}/static/f/{$document/module/sectionID}/{$document/module/moduleID}/pics/delete.png" alt="{$i18n.flowfamilylist.DeleteFlow}" />
						</a>
					</xsl:otherwise>
				</xsl:choose>
				
				
				
				<xsl:call-template name="flowfamily-list-extension-buttons">
					<xsl:with-param name="listname" select="$name"/>
				</xsl:call-template>
			</li>
		</ul>
		
		<xsl:call-template name="searchField">
			<xsl:with-param name="connectorURL" select="$connectorURL" />
			<xsl:with-param name="prefix" select="$name" />
			<xsl:with-param name="placeholder" select="$placeholder" />
			<xsl:with-param name="class" select="$searchfieldclass" />
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template name="flowfamily-list-extension-buttons">
		<xsl:param name="listname"/>
		
	</xsl:template>
	
	<xsl:template name="flowfamily-list-extension-defaults">
		<xsl:param name="listname"/>
	
	</xsl:template>
	
	<xsl:template name="flowfamily-list-extension-default">
		<xsl:param name="listname"/>
		<xsl:param name="name" />
		<xsl:param name="value" />

		<input type="hidden" name="{$listname}-{$name}" value="{$value}" disabled="disabled" />

	</xsl:template>
	
	<xsl:template name="flowfamily-list-extension">
		<xsl:param name="listname"/>
		<xsl:param name="requestparameters" />
		
		<xsl:param name="name"/>
		<xsl:param name="value"/>
		
		<xsl:variable name="extensionName">
			<xsl:value-of select="$listname"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="$name"/>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$requestparameters">
			
				<input type="hidden" name="{$extensionName}" value="{$requestparameters/parameter[name = $extensionName]/value}" />
			
			</xsl:when>
			<xsl:otherwise>
			
				<input type="hidden" name="{$extensionName}" value="{$value}" />
			
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template match="flowfamily" mode="flowfamily-list-extension">
		<xsl:param name="listname"/>
		<xsl:param name="requestparameters" />
		
	</xsl:template>
	
	<xsl:template name="searchField">
	
		<xsl:param name="connectorURL"/>
		<xsl:param name="prefix"/>
		<xsl:param name="placeholder" select="''"/>
		<xsl:param name="class" select="''"/>

		<xsl:if test="$connectorURL">
			<div class="ui-widget">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id">
						<xsl:value-of select="$prefix" />
						<xsl:value-of select="'-search'" />
					</xsl:with-param>
					<xsl:with-param name="class">
						<xsl:text>full border-box </xsl:text>
						<xsl:value-of select="$class"/>
					</xsl:with-param>
					<xsl:with-param name="width" select="''" />
					<xsl:with-param name="placeholder" select="$placeholder" />
				</xsl:call-template>
			</div>
		</xsl:if>

	</xsl:template>
	
</xsl:stylesheet>