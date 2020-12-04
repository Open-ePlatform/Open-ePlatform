<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="globalscripts">
		/js/confirmpost.js
	</xsl:variable>

	<xsl:template match="validationError[messageKey='FlowNoLongerAvailable']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowNoLongerAvailable"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowInstanceNoLongerAvailable']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstanceNoLongerAvailable"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='FlowDisabled']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowDisabled"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowNoLongerPublished']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowNoLongerPublished"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RequestedFlowNotFound']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.RequestedFlowNotFound"></xsl:with-param>
		</xsl:call-template>

	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='RequestedFlowInstanceNotFound']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.RequestedFlowInstanceNotFound"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ErrorGettingFlowInstanceManager']">
		
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.ErrorGettingFlowInstanceManager"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='InvalidLinkRequested']">
			
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.InvalidLinkRequested"></xsl:with-param>
		</xsl:call-template>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowInstanceErrorDataSaved']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstanceErrorDataSaved"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='FlowInstanceErrorDataNotSaved']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstanceErrorDataNotSaved"></xsl:with-param>
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowInstanceManagerClosed']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowInstanceManagerClosed"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='UnableToPopulateQueryInstance']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnableToPopulateQueryInstance"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowNotAvailiableInRequestedFormat']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.FlowNotAvailiableInRequestedFormat"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="printValidationError">
		
		<xsl:param name="message" />
		
		<section class="modal error">
			<span data-icon-before="!">
				<xsl:value-of select="$message" />
			</span>
			<i class="icon close">x</i>
		</section>
		
	</xsl:template>
	
	<xsl:template name="showFlowInstanceControlPanel">
		
		<xsl:param name="flowInstance" />
		<xsl:param name="view" />
		
		<div class="errand-menu buttons-in-desktop" data-menu="errand">
		  	<a class="btn btn-dark" data-toggle-menu="errand" href="#">
		  		<span data-icon-after="_">
		  			<xsl:choose>
		  				<xsl:when test="$view = 'OVERVIEW'">
		  					<xsl:attribute name="data-icon-before">L</xsl:attribute>
		  					<xsl:value-of select="$i18n.overview"/>
		  				</xsl:when>
		  				<xsl:when test="$view = 'PREVIEW'">
		  					<xsl:attribute name="data-icon-before">ó</xsl:attribute>
		  					<xsl:value-of select="$i18n.showInstance"/>
		  				</xsl:when>
		  				<xsl:when test="$view = 'FLOWINSTANCE'">
		  					<xsl:attribute name="data-icon-before">w</xsl:attribute>
		  					<xsl:value-of select="$i18n.updateInstance"/>
		  				</xsl:when>
		  			</xsl:choose>
		  		</span>
		  	</a>
		  	<ul>
		  		<li>
		  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{$flowInstance/Flow/flowID}/{$flowInstance/flowInstanceID}" class="btn btn-light">
						<xsl:if test="$view = 'OVERVIEW'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
						<span data-icon-before="L"><xsl:value-of select="$i18n.overview"/></span>
					</a>
		  		</li>
		  		<li>
		  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/preview/{$flowInstance/flowInstanceID}" class="btn btn-light">
						<xsl:if test="$view = 'PREVIEW'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
						<span data-icon-before="ó"><xsl:value-of select="$i18n.showInstance"/></span>
					</a>
		  		</li>
		  		<xsl:if test="$flowInstance/Status/isUserMutable = 'true'">
			  		<li>
			  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowinstance/{$flowInstance/Flow/flowID}/{$flowInstance/flowInstanceID}" class="btn btn-light">
							<xsl:if test="$view = 'FLOWINSTANCE'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
							<span data-icon-before="w"><xsl:value-of select="$i18n.updateInstance"/></span>
						</a>
			  		</li>
		  		</xsl:if>
		  		<xsl:if test="$flowInstance/Status/isUserDeletable = 'true'">
			  		<li>
			  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{$flowInstance/flowInstanceID}" onclick="return confirmPost(this.href, '{$i18n.DeleteFlowInstanceConfirm}: {$flowInstance/Flow/name}?');" class="btn btn-red">
							<span data-icon-before="x"><xsl:value-of select="$i18n.cancelInstance"/></span>
						</a>
			  		</li>
		  		</xsl:if>
		  	</ul>
		</div>
		
	</xsl:template>
	
	<xsl:template name="showManagerFlowInstanceControlPanel">
		
		<xsl:param name="flowInstance" />
		<xsl:param name="view" />
		<xsl:param name="bookmarked" select="Bookmarked" />
		<xsl:param name="hideUpdateManagers" select="null" />
		<xsl:param name="deleteAccessOverride" select="null" />
		
		<div class="panel-wrapper official">
		
			<div class="errand-menu buttons-in-desktop" data-menu="errand">
			  	<a class="btn btn-dark" data-toggle-menu="errand" href="#">
			  		<span data-icon-after="_">
			  			<xsl:choose>
			  				<xsl:when test="$view = 'OVERVIEW'">
			  					<xsl:attribute name="data-icon-before">L</xsl:attribute>
			  					<xsl:value-of select="$i18n.overview"/>
			  				</xsl:when>
			  				<xsl:when test="$view = 'PREVIEW'">
			  					<xsl:attribute name="data-icon-before">s</xsl:attribute>
			  					<xsl:value-of select="$i18n.showInstance"/>
			  				</xsl:when>
			  				<xsl:when test="$view = 'FLOWINSTANCE'">
			  					<xsl:attribute name="data-icon-before">w</xsl:attribute>
			  					<xsl:value-of select="$i18n.updateInstance"/>
			  				</xsl:when>
			  				<xsl:when test="$view = 'STATUS'">
			  					<xsl:attribute name="data-icon-before">!</xsl:attribute>
			  					<xsl:value-of select="$i18n.updateStatus"/>
			  				</xsl:when>
			  				<xsl:when test="$view = 'MANAGER'">
			  					<xsl:attribute name="data-icon-before">u</xsl:attribute>
			  					<xsl:value-of select="$i18n.updateManagers"/>
			  				</xsl:when>
			  			</xsl:choose>
			  		</span>
			  	</a>
			  	<ul>
			  		<li>
			  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/overview/{$flowInstance/flowInstanceID}" class="btn btn-light">
							<xsl:if test="$view = 'OVERVIEW'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
							<span data-icon-before="L"><xsl:value-of select="$i18n.overview"/></span>
						</a>
			  		</li>
			  		<li>
			  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/preview/{$flowInstance/flowInstanceID}" class="btn btn-light">
							<xsl:if test="$view = 'PREVIEW'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
							<span data-icon-before="s"><xsl:value-of select="$i18n.showInstance"/></span>
						</a>
			  		</li>
			  		<xsl:if test="$flowInstance/Status/isAdminMutable = 'true'">
				  		<li>
				  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowinstance/{$flowInstance/Flow/flowID}/{$flowInstance/flowInstanceID}" class="btn btn-light">
								<xsl:if test="$view = 'FLOWINSTANCE'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
								<span data-icon-before="w"><xsl:value-of select="$i18n.updateInstance"/></span>
							</a>
				  		</li>
			  		</xsl:if>
			  		
			  		<li>
			  			<a href="#" class="btn btn-light" onclick="return toggleBookmark(event, this, '{/Document/requestinfo/currentURI}/{/Document/module/alias}/bookmark/{$flowInstance/flowInstanceID}');">
							<xsl:if test="$bookmarked"><xsl:attribute name="class">btn green</xsl:attribute></xsl:if>
							<span data-icon-before="*"><xsl:value-of select="$i18n.bookmarkInstance"/></span>
						</a>
			  		</li>
			  		
			  		<li>
			  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/status/{$flowInstance/flowInstanceID}" class="btn btn-light">
							<xsl:if test="$view = 'STATUS'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
							<span data-icon-before="!"><xsl:value-of select="$i18n.updateStatus"/></span>
						</a>
			  		</li>
			  		
			  		<xsl:if test="not($hideUpdateManagers)">
				  		<li>
				  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/managers/{$flowInstance/flowInstanceID}" class="btn btn-light">
								<xsl:if test="$view = 'MANAGER'"><xsl:attribute name="class">btn btn-light active</xsl:attribute></xsl:if>
								<span data-icon-before="u"><xsl:value-of select="$i18n.updateManagers"/></span>
							</a>
				  		</li>
			  		</xsl:if>
			  		
			  		<xsl:if test="$flowInstance/Status/isAdminDeletable = 'true' or $deleteAccessOverride">
				  		<li>
				  			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{$flowInstance/flowInstanceID}" onclick="return confirmPost(this.href, '{$i18n.DeleteFlowInstanceConfirm}: {$flowInstance/Flow/name}?');" class="btn btn-light">
								<span data-icon-before="x"><xsl:value-of select="$i18n.deleteInstance"/></span>
							</a>
				  		</li>
			  		</xsl:if>

			  	</ul>
			</div>
		
		</div>
		
	</xsl:template>
		
</xsl:stylesheet>