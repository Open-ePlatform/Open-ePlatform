<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="ShowFlowOverview">
		
		<xsl:apply-templates select="Flow" mode="overview" />
		
	</xsl:template>

	<xsl:template match="Flow" mode="overview">
		
		<xsl:variable name="flowID" select="flowID" />
		<xsl:variable name="flowFamilyID" select="FlowFamily/flowFamilyID" />
		<xsl:variable name="operatingMessage" select="../OperatingMessage" />

		<xsl:variable name="isInternal">
			<xsl:if test="not(externalLink)">true</xsl:if>
		</xsl:variable>
		
		<section class="no-pad-tablet">
		
				<div class="section-inside">
	  				<div class="heading-wrapper">
	  					<figure>
		  					<img alt="" src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{flowID}" />
		  				</figure>
		  				<div class="heading">
	  						<h1 id="flow_{$flowID}" class="xl"><xsl:value-of select="name" />
	  							<xsl:if test="../loggedIn and ../userFavouriteModuleAlias">
	  								<i id="flowFamily_{FlowFamily/flowFamilyID}" data-icon-after="*" class="xl favourite">
	  									<xsl:if test="not(../UserFavourite[FlowFamily/flowFamilyID = $flowFamilyID])">
											<xsl:attribute name="class">xl favourite gray</xsl:attribute>
										</xsl:if>
	  								</i>
	  							</xsl:if>
	  						</h1>
	  						<xsl:if test="not(../loggedIn) and requireAuthentication = 'true'">
	  							<span data-icon-before="u" class="marginleft"><xsl:value-of select="$i18n.AuthenticationRequired" /></span>
	  						</xsl:if>
						</div>
						<xsl:if test="$operatingMessage/global = 'false'">
							<section class="modal warning floatleft clearboth border-box full" style=""><i class="icon" style="font-size: 16px; margin-right: 4px; color: rgb(199, 52, 52);">!</i><xsl:value-of select="$operatingMessage/message" /></section>
						</xsl:if>
	  				</div>
	  				<div class="description">
	  					<a class="btn btn-light btn-inline btn-readmore">LÄS MER</a>
	  					<xsl:choose>
	  						<xsl:when test="longDescription"><xsl:value-of select="longDescription" disable-output-escaping="yes" /></xsl:when>
	  						<xsl:otherwise><xsl:value-of select="shortDescription" disable-output-escaping="yes" /></xsl:otherwise>
	  					</xsl:choose>
	  					<xsl:if test="FlowFamily/contactName or FlowFamily/ownerName">
							<div class="about-flow">
								<xsl:if test="FlowFamily/contactName">
									<div class="inner">
										<h2 class="h1"><xsl:value-of select="$i18n.Questions" /></h2>
										<xsl:value-of select="FlowFamily/contactName" />
										<xsl:if test="FlowFamily/contactEmail">
											<br/><a href="mailto:{FlowFamily/contactEmail}" title="{$i18n.SendMailTo}: {FlowFamily/contactEmail}"><xsl:value-of select="FlowFamily/contactEmail" /></a>
										</xsl:if>
										<xsl:if test="FlowFamily/contactPhone">
											<br /><xsl:value-of select="FlowFamily/contactPhone" />
										</xsl:if>
									</div>
								</xsl:if>
								<xsl:if test="FlowFamily/ownerName">
									<div class="inner">
										<h2 class="h1"><xsl:value-of select="$i18n.Responsible" /></h2>
										<xsl:value-of select="FlowFamily/ownerName" />
										<xsl:if test="FlowFamily/ownerEmail">
											<br /><a href="mailto:{FlowFamily/ownerEmail}" title="{$i18n.SendMailTo}: {FlowFamily/ownerEmail}"><xsl:value-of select="FlowFamily/ownerEmail" /></a>
										</xsl:if>
									</div>
								</xsl:if>
							</div>
						</xsl:if>
	  				</div>
	  				
 				</div>
 				
 			<xsl:variable name="isDisabled" select="$operatingMessage and $operatingMessage/disableFlows = 'true'" />
	 		
	 		<xsl:variable name="startButtonText">
				<xsl:choose>
					<xsl:when test="FlowFamily/startButtonText">
						<xsl:value-of select="FlowFamily/startButtonText" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.StartFlow" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
	 			
  			<div class="aside-inside">
  			
  				<div class="section noborder">
  					<xsl:if test="Checks/check">
	  					<xsl:attribute name="class">section yellow</xsl:attribute>
	  					<h2 class="bordered"><xsl:value-of select="$i18n.ChecklistTitle" /></h2>
	  					<ul class="checklist">
	  						<xsl:apply-templates select="Checks/check" mode="overview" />
	  					</ul>
  					</xsl:if>
  					
  					<xsl:if test="Steps or externalLink">
	  					<div class="btn-wrapper">
	  						<xsl:choose>
	  							<xsl:when test="$isInternal = 'true'">
	  							
	  								<xsl:choose>
	  									<xsl:when test="$isDisabled">
	  									
	  										<a class="btn btn-green xl disabled full" href="javascript:void(0)" title="{$operatingMessage/message}">
	  											<xsl:value-of select="$startButtonText" />
	  										</a>
	  										
	  									</xsl:when>
	  									<xsl:otherwise>
	  									
	  										<a class="btn btn-green xl full" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/testflow/{flowID}">
	  											<xsl:value-of select="$startButtonText" />
	  										</a>
	  										
	  									</xsl:otherwise>
	  								</xsl:choose>
	  								
	  							</xsl:when>
	  							<xsl:otherwise>
	  							
	  								<a class="btn btn-green xl full" href="{externalLink}">
	  									<xsl:if test="../openExternalFlowsInNewWindow = 'true'">
			  								<xsl:attribute name="data-icon-after">e</xsl:attribute>
												<xsl:attribute name="target">_blank</xsl:attribute>
			  							</xsl:if>
			  							
			  							<xsl:if test="$isDisabled">
			  								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
			  								<xsl:attribute name="class">btn btn-green xl disabled</xsl:attribute>
			  							</xsl:if>
			  							
	  									<xsl:value-of select="$startButtonText" />
	  								</a>
	  								
	  							</xsl:otherwise>
	  						</xsl:choose>
	  					</div>
  					</xsl:if>
  					
  				</div>
  				
  				<xsl:if test="hasPDF = 'true'">
					<div class="section no-border">
						<div class="btn-wrapper no-border">
							<xsl:if test="not(Checks/check)"><xsl:attribute name="class">btn-wrapper no-border no-padding</xsl:attribute></xsl:if>
							<xsl:choose>
								<xsl:when test="$isDisabled"><a class="btn btn-blue xl disabled full" href="javascript:void(0)" title="{$operatingMessage/message}"><xsl:value-of select="$i18n.DownloadPDFForm" /></a></xsl:when>
								<xsl:otherwise><a class="btn btn-blue xl full" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getflowformpdf/{flowID}" target="_blank"><xsl:value-of select="$i18n.DownloadPDFForm" /></a></xsl:otherwise>
							</xsl:choose>
						</div>
					</div>
				</xsl:if>
  				
  			</div>
  			
  			<xsl:if test="$isInternal = 'true' and Steps">
  			
	  			<div class="section-full no-pad-tablet">
	  				<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.StepDescriptionTitle" />:</h2>
	  				
	  				<xsl:variable name="submitText">
						<xsl:choose>
							<xsl:when test="requireSigning = 'true'"><xsl:value-of select="$i18n.signAndSubmit" /></xsl:when>
							<xsl:otherwise><xsl:value-of select="$i18n.submit" /></xsl:otherwise>
						</xsl:choose>			
					</xsl:variable>
	  				
	  				<div class="service-navigator-wrap summary">
	  					<div>
	  					
	  						<a data-icon-after="&lt;" href="#" class="js-prev disabled">
			  					<span><xsl:value-of select="$i18n.Previous" /></span>
			  				</a>
	  						
	  						<ul class="service-navigator primary navigated">
	  						
			  					<xsl:apply-templates select="Steps/Step" mode="overview">
			  						<xsl:with-param name="flowDisabled" select="$isDisabled" />
			  					</xsl:apply-templates>
			  					
			  					<xsl:variable name="stepCount" select="count(Steps/Step)" />
			  					
			  					<xsl:choose>
			  						<xsl:when test="usePreview = 'true'">
			  							<li>
					  						<span data-step="{$stepCount + 1}"><xsl:value-of select="$i18n.preview" /></span>
					  					</li>
					  					<xsl:if test="hideSubmitStepText != 'true'">
						  					<li>
						  						<span data-step="{$stepCount + 2}"><xsl:value-of select="$submitText" /></span>
						  					</li>
					  					</xsl:if>
			  						</xsl:when>
			  						<xsl:otherwise>
			  							<xsl:if test="hideSubmitStepText != 'true'">
			  								<li>
						  						<span data-step="{$stepCount + 1}"><xsl:value-of select="$submitText" /></span>
						  					</li>
						  				</xsl:if>
			  						</xsl:otherwise>
			  					</xsl:choose>
		  					
		  					</ul>
		  					
		  					<a data-icon-after="&gt;" href="#" class="js-next">
			  					<span><xsl:value-of select="$i18n.Next" /></span>
			  				</a>
		  					
	  					</div>
	  					
	  				</div>
	  			</div>
	  		
	  		</xsl:if>
	  		
		</section>
	
		<xsl:if test="../showRelatedFlows = 'true'">
	
			<xsl:variable name="flowTypeID" select="FlowType/flowTypeID" />
		
			<xsl:apply-templates select="FlowType">
				<xsl:with-param name="flows" select="../FlowTypeFlows/Flow[FlowType/flowTypeID = $flowTypeID]" />
			</xsl:apply-templates>
	
		</xsl:if>
	
	</xsl:template>
	
		<xsl:template match="check" mode="overview">
	
		<li><xsl:value-of select="." /></li>
	
	</xsl:template>
	
	<xsl:template match="Step" mode="overview">
		
		<xsl:param name="flowDisabled" />
		
		<li>
			<span data-step="{position()}"><xsl:value-of select="name" /></span>
		</li>
		
	</xsl:template>
	
</xsl:stylesheet>