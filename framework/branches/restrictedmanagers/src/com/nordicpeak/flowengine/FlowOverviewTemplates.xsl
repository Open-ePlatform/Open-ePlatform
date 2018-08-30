<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Flow" mode="overview">
		
		<xsl:param name="internalFlowMethodAlias" select="'flow'"/>
		<xsl:param name="externalFlowMethodAlias" select="'external'"/>
		
		<xsl:variable name="flowID" select="flowID" />
		<xsl:variable name="flowFamilyID" select="FlowFamily/flowFamilyID" />
		<xsl:variable name="operatingMessage" select="../OperatingMessage" />
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
						
							<xsl:choose>
								<xsl:when test="$operatingMessage/messageType = 'INFO'">
									<section class="modal info floatleft clearboth border-box full" style=""><xsl:value-of select="$operatingMessage/message" /></section>
								</xsl:when>
								<xsl:otherwise>
									<section class="modal warning floatleft clearboth border-box full" style=""><i class="icon" style="font-size: 16px; margin-right: 4px; color: rgb(199, 52, 52);">!</i><xsl:value-of select="$operatingMessage/message" /></section>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						
	  				</div>
	  				<div class="description">
	  					<a class="btn btn-light btn-inline btn-readmore">LÄS MER</a>
	  					<xsl:choose>
	  						<xsl:when test="longDescription"><xsl:value-of select="longDescription" disable-output-escaping="yes" /></xsl:when>
	  						<xsl:otherwise><xsl:value-of select="shortDescription" disable-output-escaping="yes" /></xsl:otherwise>
	  					</xsl:choose>
	  					
	  					<xsl:call-template name="appendFlowContactAndOwner"/>
	  					
	  					<xsl:apply-templates select="../ExtensionViews/ExtensionView[slot = 'left']" mode="flowOverview-left"/>
	  				</div>
	  				
 				</div>
 				
 			<xsl:variable name="isDisabled" select="$operatingMessage and $operatingMessage/disableFlows = 'true'" />
 			
  			<div class="aside-inside start-flow-panel">
  			
  				<div class="section noborder">
  					<xsl:if test="Checks/check or FlowFamily/useLoginHelpLink = 'true'">
	  					<xsl:attribute name="class">section yellow</xsl:attribute>
	  					<h2 class="bordered"><xsl:value-of select="$i18n.ChecklistTitle" /></h2>
	  					<ul class="checklist">
	  						<xsl:apply-templates select="Checks/check" mode="overview" />
	  						
	  						<xsl:if test="FlowFamily/useLoginHelpLink = 'true'">
	  						
	  							<li class="link">
	  								<a href="{FlowFamily/loginHelpLinkURL}" target="_blank">
	  									<xsl:value-of select="FlowFamily/loginHelpLinkName" />
	  								</a>
	  							</li>
	  						
	  						</xsl:if>
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
	  									
	  										<a class="btn btn-green xl full" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{$internalFlowMethodAlias}/{flowID}">
	  											
	  											<xsl:if test="showLoginQuestion = 'true' and not(/Document/user)">
	  												<xsl:attribute name="onclick">askForLoginOrContinue(this, event);</xsl:attribute>
	  											</xsl:if>
	  											
	  											<xsl:value-of select="$startButtonText" />
	  										</a>
	  										
	  									</xsl:otherwise>
	  								</xsl:choose>
	  								
	  							</xsl:when>
	  							<xsl:otherwise>
	  							
	  								<a class="btn btn-green xl full" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{$externalFlowMethodAlias}/{flowID}">
	  									
	  									<xsl:if test="showLoginQuestion = 'true' and not(/Document/user)">
 												<xsl:attribute name="onclick">askForLoginOrContinue(this, event);</xsl:attribute>
 											</xsl:if>
	  									
	  									<xsl:if test="externalFlowMethodAlias = ''">
	  										<xsl:attribute name="href"><xsl:value-of select="externalLink"/></xsl:attribute>
	  									</xsl:if>
	  									
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
	  					
	  					<xsl:if test="showLoginQuestion = 'true' and not(/Document/user)">
	  					
								<div class="help-box" data-help-box="askLogin">
									<div>
						  			<div> 
						  				<a class="close" href="#" data-icon-after="x"></a> 
						  				
						  				<h1 class="nopadding bigmarginbottom">
						  					<xsl:value-of select="$i18n.StartFlow.LoginQuestion.title" />
						  				</h1>
						  				
						  				<xsl:value-of select="loginQuestionText" disable-output-escaping="yes" />
						  				
						  				<a class="btn btn-green xl full login">
  											<xsl:value-of select="$i18n.StartFlow.LoginQuestion.Login" />
  										</a>
  										
						  				<a class="btn btn-green xl full bigmargintop continue">
  											<xsl:value-of select="$i18n.StartFlow.LoginQuestion.NoLogin" />
  										</a>
						  			</div>
									</div>
								</div>
								
								<div class="help-backdrop" data-help-box="askLogin" />
								
	  					</xsl:if>
	  					
  					</xsl:if>
  					
  				</div>
  				
  				<xsl:if test="FlowForms/FlowForm">
					<xsl:call-template name="FlowFormButton">
						<xsl:with-param name="isDisabled" select="$isDisabled"/>
						<xsl:with-param name="operatingMessage" select="$operatingMessage"/>
					</xsl:call-template>
				</xsl:if>
  					
  			</div>
  			
  			<xsl:if test="$isInternal = 'true' and Steps">
  			
	  			<div class="section-full no-pad-tablet">
	  				<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.StepDescriptionTitle" />:</h2>
	  				
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
			  					
								<xsl:variable name="previewOffset">
									<xsl:choose>
										<xsl:when test="usePreview = 'true'">
											<xsl:value-of select="1"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="0"/>
										</xsl:otherwise>			
									</xsl:choose>
								</xsl:variable>				  					
			  								  					
								<xsl:if test="usePreview = 'true'">
		  							<li>
				  						<span data-step="{$stepCount + 1}"><xsl:value-of select="$i18n.preview" /></span>
				  					</li>
	  							</xsl:if>
			  					
			  					
			  					<xsl:choose>
			  						<xsl:when test="paymentSupportEnabled = 'true' and requireSigning = 'true'">
			  						
										<li>
					  						<span data-step="{$stepCount + 1 + $previewOffset}"><xsl:value-of select="$i18n.Sign" /></span>
					  					</li>			  							
			  						
				  						<li>												
											<span data-step="{$stepCount + 2 + $previewOffset}">
												<xsl:value-of select="$i18n.payAndSubmit"/>
											</span>
										</li>
			  						
			  						</xsl:when>
			  						<xsl:when test="paymentSupportEnabled = 'true'">
			  						
				  						<li>												
											<span data-step="{$stepCount + 1 + $previewOffset}">
												<xsl:value-of select="$i18n.payAndSubmit"/>
											</span>
										</li>
			  						
			  						</xsl:when>
			  						<xsl:when test="requireSigning = 'true'">
			  									  						
										<li>
					  						<span data-step="{$stepCount + 1 + $previewOffset}"><xsl:value-of select="$i18n.signAndSubmit" /></span>
					  					</li>			  						
			  						
			  						</xsl:when>
			  						<xsl:otherwise>
			  						
										<xsl:if test="hideSubmitStepText != 'true'">
					  						<li>
						  						<span data-step="{$stepCount + 1 + $previewOffset}"><xsl:value-of select="$i18n.submit" /></span>
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
	
	<xsl:template match="FlowType">
		
		<xsl:param name="flowTypeID" select="flowTypeID" />
		<xsl:param name="flows" select="../Flow[FlowType/flowTypeID = $flowTypeID]" />
		<xsl:param name="useCategoryFilter" select="../useCategoryFilter" />
		
		<section id="flowtype_{flowTypeID}" class="accordion" data-filter="flowtype{flowTypeID}">
			
			<xsl:if test="position() = 1">
				<xsl:attribute name="class">accordion first</xsl:attribute>
			</xsl:if>
			
			<div class="heading-wrapper">
				
				<xsl:if test="iconFileName">
					<figure>
						<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flowtypeicon/{flowTypeID}" alt="{iconFileName}" />
					</figure>
				</xsl:if>
				
				<h2 class="h1">
					<xsl:if test="iconFileName"><xsl:attribute name="class">has-icon h1</xsl:attribute></xsl:if>
					<xsl:value-of select="name" />
					<a class="btn btn-light accordion-toggler count"></a>
				</h2>
				
				<xsl:if test="$flows and $useCategoryFilter">
					
					<div class="select-wrapper">
						<div class="select-box category-select" id="select_{flowTypeID}">
							<span class="text"><xsl:value-of select="$i18n.MostPopular" /></span>
							<span class="arrow">_</span>
							<div class="options">
								<ul>
									<xsl:apply-templates select="Categories/Category">
										<xsl:with-param name="flows" select="$flows" />
									</xsl:apply-templates>
									<xsl:variable name="uncategorizedFlows" select="$flows[not(Category)]" />
									<xsl:if test="$uncategorizedFlows">
										<li id="uncategorized">
											<a href="#">
												<span class="text"><xsl:value-of select="$i18n.Uncategorized" /></span>
												<span class="count"><xsl:value-of select="count($uncategorizedFlows)" /></span>
											</a>
										</li>
									</xsl:if>
									<li id="popular" class="selected">
										<a href="#">
											<span class="text"><xsl:value-of select="$i18n.MostPopular" /></span>
											<span class="count"><i>*</i></span>
										</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				
				</xsl:if>
				
			</div>
			
			<xsl:choose>
				<xsl:when test="$useCategoryFilter">
					
					<ul class="previews">
						<xsl:choose>
							<xsl:when test="not($flows)">
								<li><div class="inner"><h2 class="no-flows"><xsl:value-of select="$i18n.NoFlowsFound"/></h2></div></li>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$flows" />
							</xsl:otherwise>
						</xsl:choose>
					</ul>
					
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:variable name="popularFlows" select="$flows[popular]" />
					
					<div class="popularflows-wrapper">
				
						<div class="hr-divider popularflows-divider">
							<span class="label"><xsl:value-of select="$i18n.MostUsedFLows" /></span>
						</div>
				
						<ul class="previews popularflows-list">
						</ul>
					
					</div>
				
					<div class="allflows-wrapper hidden">
				
						<div class="hr-divider allflows-divider">
							<span class="label"><xsl:value-of select="$i18n.AllFlows" /></span>
						</div>
					
						<ul class="previews allflows-list">
							<xsl:choose>
								<xsl:when test="not($flows)">
									<li><div class="inner"><h2 class="no-flows"><xsl:value-of select="$i18n.NoFlowsFound"/></h2></div></li>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="$flows" />
								</xsl:otherwise>
							</xsl:choose>
						</ul>
				
					</div>
				
					<a class="footer-button show-all-link"><xsl:value-of select="$i18n.ShowAll" /></a>
					
				</xsl:otherwise>
			</xsl:choose>
			
		</section>
		
	</xsl:template>	
	
	<xsl:template name="appendFlowContactAndOwner">

		<xsl:variable name="ownerExtensionView" select="../ExtensionViews/ExtensionView[slot = 'left-owner']"/>
	
		<xsl:if test="(FlowFamily/contactName or FlowFamily/ownerName) and not($ownerExtensionView)">
		
			<div class="about-flow">
			
				<xsl:if test="FlowFamily/contactName">
				
					<div class="inner">
						<h2 class="h1"><xsl:value-of select="$i18n.Questions" /></h2>
						<xsl:value-of select="FlowFamily/contactName" />
						
						<xsl:if test="FlowFamily/contactEmail">
							<br/><a href="mailto:{FlowFamily/contactEmail}" title="{$i18n.SendMailTo}: {FlowFamily/contactEmail}"><xsl:value-of select="FlowFamily/contactEmail" /></a>
						</xsl:if>
						
						<xsl:if test="FlowFamily/contactPhone">
							<br /><a href="tel:{FlowFamily/contactPhone}" title="{$i18n.CallNumber}: {FlowFamily/contactPhone}"><xsl:value-of select="FlowFamily/contactPhone" /></a>
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
		
		<xsl:if test="$ownerExtensionView">
		
			<div class="about-flow-extension full border border-radius-small noexpander">

				<h2 class="h1 pointer hover border-radius-small lightbackground" onclick="$(this).next().slideToggle(200); $(this).toggleClass('open').find('span').toggle(); return false;">
					<span class="bigmarginleft floatright" data-icon-before="-" style="display: none;" />
					<span class="bigmarginleft floatright" data-icon-before="+" />
					<xsl:value-of select="$ownerExtensionView/name" />
				</h2>

				<div class="bigpaddingleft bigpaddingright bigmarginleft bigmarginright" style="display: none">
				
					<xsl:value-of select="$ownerExtensionView/ViewFragment/HTML" disable-output-escaping="yes"/>
					
					<xsl:if test="FlowFamily/ownerName">
						<p>
							<strong><xsl:value-of select="$i18n.Responsible"/></strong>
							<br/>
							<xsl:value-of select="FlowFamily/ownerName" />
								
							<xsl:if test="FlowFamily/ownerEmail">
								<br /><a href="mailto:{FlowFamily/ownerEmail}" title="{$i18n.SendMailTo}: {FlowFamily/ownerEmail}"><xsl:value-of select="FlowFamily/ownerEmail" /></a>
							</xsl:if>
						</p>
					</xsl:if>
				
				</div>
			
			</div>
				
			<xsl:if test="FlowFamily/contactName">
		
				<div class="about-flow-extension full border border-radius-small noexpander">
		
					<h2 class="h1 pointer hover border-radius-small lightbackground" onclick="$(this).next().slideToggle(200); $(this).toggleClass('open').find('span').toggle(); return false;">
						<span class="bigmarginleft floatright" data-icon-before="-" style="display: none;" />
						<span class="bigmarginleft floatright" data-icon-before="+" />
						<xsl:value-of select="$i18n.Questions" />
					</h2>
		
					<div class="bigpaddingleft bigpaddingright bigmarginleft bigmarginright" style="display: none">
					
						<p>
							<xsl:value-of select="FlowFamily/contactName" />
							
							<xsl:if test="FlowFamily/contactEmail">
								<br/><a href="mailto:{FlowFamily/contactEmail}" title="{$i18n.SendMailTo}: {FlowFamily/contactEmail}"><xsl:value-of select="FlowFamily/contactEmail" /></a>
							</xsl:if>
							
							<xsl:if test="FlowFamily/contactPhone">
								<br /><a href="tel:{FlowFamily/contactPhone}" title="{$i18n.CallNumber}: {FlowFamily/contactPhone}"><xsl:value-of select="FlowFamily/contactPhone" /></a>
							</xsl:if>
						</p>
					
					</div>
				
				</div>
				
			</xsl:if>
				
		</xsl:if>
	
	</xsl:template>	
		
	<xsl:template name="FlowFormButton">
		<xsl:param name="flow" select="."/>
		<xsl:param name="isDisabled"/>
		<xsl:param name="operatingMessage"/>
	
		<div class="section no-border">
			<div class="btn-wrapper no-border">
				<xsl:if test="not($flow/Checks/check) and $flow/FlowFamily/useLoginHelpLink != 'true'"><xsl:attribute name="class">btn-wrapper no-border no-padding</xsl:attribute></xsl:if>
				<xsl:choose>
					<xsl:when test="$isDisabled">
					
						<a class="btn btn-blue xl disabled full" href="javascript:void(0)" title="{$operatingMessage/message}"><xsl:value-of select="$i18n.DownloadFlowForm" /></a>
					
					</xsl:when>
					<xsl:when test="count($flow/FlowForms/FlowForm) = 1">
					
						<a class="btn btn-blue xl full" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getflowform/{$flow/flowID}/{$flow/FlowForms/FlowForm[1]/flowFormID}" target="_blank">
							
							<xsl:if test="$flow/FlowForms/FlowForm/showExternalLinkIcon = 'true'">
								<xsl:attribute name="data-icon-after">e</xsl:attribute>
							</xsl:if>
							
							<xsl:if test="$flow/FlowForms/FlowForm/name">
								<xsl:attribute name="title"><xsl:value-of select="$flow/FlowForms/FlowForm/name" /></xsl:attribute>
							</xsl:if>
							
							<xsl:if test="../setNoFollowOnFlowForms = 'true'">
								<xsl:attribute name="rel">nofollow</xsl:attribute>
							</xsl:if>
							
							<xsl:value-of select="$i18n.DownloadFlowForm" />
						</a>
						
					</xsl:when>
					<xsl:otherwise>
					
						<a id="flowforms-list-button" class="btn btn-blue xl full" href="#" onclick="$('#flowforms-list').slideToggle(200); $(this).toggleClass('open').find('span').toggle(); return false;">
							<xsl:value-of select="$i18n.DownloadFlowForms" />
							<span class="bigmarginleft" data-icon-before="^" style="display: none;" />
							<span class="bigmarginleft" data-icon-before="_" />
						</a>
						
						<div id="flowforms-list" class="bigpadding border" style="display: none">
							<xsl:apply-templates select="$flow/FlowForms/FlowForm" mode="link"/>
						</div>
						
					</xsl:otherwise>
				</xsl:choose>
				
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="FlowForm" mode="link">
	
		<a class="display-block text-align-left padding" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getflowform/{../../flowID}/{flowFormID}" target="_blank">

			<xsl:if test="name">
				<xsl:attribute name="title"><xsl:value-of select="name" /></xsl:attribute>
			</xsl:if>
		
			<xsl:if test="../../../setNoFollowOnFlowForms = 'true'">
				<xsl:attribute name="rel">nofollow</xsl:attribute>
			</xsl:if>

			<img class="alignmiddle marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/file.png" alt="" />
		
			<xsl:choose>
				<xsl:when test="name">
					<xsl:value-of select="name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.DownloadFlowForm" />
					<xsl:text>:&#160;</xsl:text>
					<xsl:value-of select="position()"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="showExternalLinkIcon = 'true'">
				<span class="bigmarginleft" data-icon-before="e" />
			</xsl:if>
		</a>
	
	</xsl:template>
	
	<xsl:template match="ExtensionView" mode="flowOverview-left">
		
		<div class="about-flow-extension full border border-radius-small noexpander">
	
			<h2 class="h1 pointer hover border-radius-small lightbackground" onclick="$(this).next().slideToggle(200); $(this).toggleClass('open').find('span').toggle(); return false;">
				<xsl:value-of select="name" />
				<span class="bigmarginleft floatright" data-icon-before="-" style="display: none;" />
				<span class="bigmarginleft floatright" data-icon-before="+" />
			</h2>
			
			<div class="bigpaddingleft bigpaddingright" style="display: none">
				<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
			</div>
		
		</div>
			
	</xsl:template>
	
</xsl:stylesheet>