<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
		
	<xsl:template match="FlowInstanceManagerPreview" name="BaseFlowInstanceManagerPreview">
		
		<xsl:variable name="concurrentModificationLock" select="ManagerResponses/ManagerResponse[concurrentModificationLock = 'true']"/>
		
		<xsl:if test="$concurrentModificationLock">
			<section class="modal error">
  				<span data-icon-before="!">
  					<a href="{/Document/requestinfo/uri}?close-reopen=1" onclick="return confirm('{$i18n.FlowInstanceConcurrentlyModifiedConfirm}')" title="{$i18n.FlowInstanceConcurrentlyModifiedLinkTitle}">
  						<xsl:value-of select="$i18n.FlowInstanceConcurrentlyModified" />
  					</a>
  				</span>
  			</section>
		</xsl:if>
		
		<xsl:if test="not(validationError)">
			<xsl:apply-templates select="lastFlowAction" />
		</xsl:if>
	
		<xsl:apply-templates select="validationError"/>
	
		<section class="service">
			
			<form method="GET" action="{/Document/requestinfo/uri}">
			
				<input id="submitmode" type="hidden" value="true" />
				
				<xsl:call-template name="createFlowInstanceManagerPreviewHeader">
					<xsl:with-param name="showSaveButton" select="'false'"/>
					<!-- 					
					<xsl:with-param name="showSaveButton">
						<xsl:choose>
							<xsl:when test="$concurrentModificationLock">false</xsl:when>
							<xsl:otherwise>true</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					 -->
				</xsl:call-template>
			
				<!-- <div class="section-full" /> -->
				
				<div class="service-navigator-wrap">
					<div>
						<a data-icon-after="&lt;" href="#" class="js-prev" aria-label="{$i18n.Previous}">
		  					<span><xsl:value-of select="$i18n.Previous" /></span>
		  				</a>
						<ul class="service-navigator primary">
							<xsl:apply-templates select="FlowInstance/Flow/Steps"/>
						</ul>
						<a data-icon-after="&gt;" href="#" class="js-next" aria-label="{$i18n.Next}">
		  					<span><xsl:value-of select="$i18n.Next" /></span>
		  				</a>
					</div>
				</div>
				
				<div class="section-full preview">
					
					<div class="queries">
						<xsl:apply-templates select="ManagerResponses/ManagerResponse"/>
					</div>
					
					<div class="navigator-buttons">
						<a href="#" class="btn btn-light xl prev arrow-mobile" onclick="redirectFromPreview(event)" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="FlowInstance/Flow/Steps/Step[position() = last()]/name" /></span></a>
						
						<xsl:if test="not($concurrentModificationLock)">
						
							<xsl:choose>
								<xsl:when test="FlowInstance/Flow/requireSigning = 'true' and not(FlowInstance/Flow/skipPosterSigning = 'true') and PaymentRequired">
									<xsl:call-template name="createFlowInstanceManagerPreviewSigningPaymentButton" />
								</xsl:when>							
								<xsl:when test="FlowInstance/Flow/requireSigning = 'true' and not(FlowInstance/Flow/skipPosterSigning = 'true')">
									<xsl:call-template name="createFlowInstanceManagerPreviewSigningButton" />
								</xsl:when>
								<xsl:when test="PaymentRequired">
									<xsl:call-template name="createFlowInstanceManagerPaymentButton" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="createFlowInstanceManagerPreviewSubmitButton" />
								</xsl:otherwise>
							</xsl:choose>
						
						</xsl:if>
					</div>
					
				</div>
			
				<ul id="futureNavigator" class="service-navigator hide-desktop clearfix">
  				</ul>
			
				<div id="submitLoadingMessage" style="display: none">
					<h1><xsl:value-of select="$i18n.SubmitLoading" />..</h1>
				</div>
			
			</form>
			
		</section>
		
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPaymentButton">
	
		<a href="#" class="btn btn-green xl next" onclick="redirectFromPreview(event, 'save-submit=true')"><xsl:value-of select="$i18n.Payment" /></a>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewSigningPaymentButton">
	
		<a href="#" class="btn btn-green xl next" onclick="redirectFromPreview(event, 'save-submit=true')"><xsl:value-of select="$i18n.Sign" /></a>
	
	</xsl:template>
	
	<xsl:template match="SigningForm">
		
		<section class="service">
			
			<form method="GET" action="{/Document/requestinfo/uri}">
			
				<input id="submitmode" type="hidden" value="true" />
				
				<xsl:call-template name="createFlowInstanceManagerPreviewHeader">
				
					<xsl:with-param name="showSaveButton" select="'false'"/>
				
				</xsl:call-template>
			
				<!-- <div class="section-full" /> -->
				
				<div class="service-navigator-wrap">
					<div>
						<a data-icon-after="&lt;" href="#" class="js-prev" aria-label="{$i18n.Previous}">
		  					<span><xsl:value-of select="$i18n.Previous" /></span>
		  				</a>
						<ul class="service-navigator primary">
							<xsl:apply-templates select="FlowInstance/Flow/Steps"/>
						</ul>
						<a data-icon-after="&gt;" href="#" class="js-next" aria-label="{$i18n.Next}">
		  					<span><xsl:value-of select="$i18n.Next" /></span>
		  				</a>
					</div>
				</div>
				
				<div class="section-full push">
					
					<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.signAndSubmit" /></h2>
					
					<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
					
					<xsl:choose>
						<xsl:when test="FlowInstance/Flow/usePreview = 'true'">
							
							<div class="navigator-buttons">
								<a href="#" class="btn btn-light xl prev arrow-mobile" onclick="redirectFromPreview(event,'preview=1')" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="$i18n.preview" /></span></a>
							</div>								
							
						</xsl:when>
						<xsl:otherwise>
							
							<div class="navigator-buttons">
								<a href="#" class="btn btn-light xl prev arrow-mobile" onclick="redirectFromPreview(event)" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="FlowInstance/Flow/Steps/Step[position() = last()]/name" /></span></a>
							</div>							
							
						</xsl:otherwise>
					</xsl:choose>					
										
				</div>
			
				<ul id="futureNavigator" class="service-navigator hide-desktop clearfix">
  				</ul>
			
			</form>
			
		</section>
		
	</xsl:template>	

	<xsl:template match="MultiSigningStatusForm">
		
		<xsl:if test="SubmitSurveyHTML">
			<div class="info-box success">
				<span><strong><xsl:value-of select="$i18n.SubmitSurveyTitle" /></strong><i class="icon close">x</i></span>
				<div class="marker"></div>
			</div>
			<div class="info-box-message success">
				<xsl:value-of select="SubmitSurveyHTML" disable-output-escaping="yes" />
			</div>
		</xsl:if>
			
	
		<section class="service">
			
			<form method="GET" action="{/Document/requestinfo/uri}">
			
				<input id="submitmode" type="hidden" value="true" />
				
				<xsl:call-template name="createFlowInstanceManagerPreviewHeader">
					<xsl:with-param name="showSaveButton" select="'false'"/>
					<xsl:with-param name="showFlowInstanceID" select="'false'"/>
				</xsl:call-template>
			
				<div class="section-full push">
					
					<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.MultiSignStatus" /></h2>
					
					<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
					
				</div>
			
			</form>
			
		</section>
	
	</xsl:template>

	<xsl:template match="StandalonePaymentForm">
	
		<section class="service">
			
			<form id="paymentForm" method="POST" action="{/Document/requestinfo/uri}">
			
				<xsl:call-template name="createFlowInstanceManagerPreviewHeader">
					<xsl:with-param name="showSaveButton" select="'false'"/>
					<xsl:with-param name="showFlowInstanceID" select="'false'"/>
				</xsl:call-template>
				
<!-- 				<div class="service-navigator-wrap"> -->
<!-- 					<div> -->
<!-- 						<ul class="service-navigator primary"> -->
<!-- 							<xsl:apply-templates select="FlowInstance/Flow/Steps"/> -->
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 				</div> -->
				
				<div class="section-full push">
					
					<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.payHeader" /></h2>
					
					<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
					
					<div class="navigator-buttons clearboth">
						<a href="#" class="btn btn-light xl next" onclick="$('#paymentForm').submit()" data-icon-after=">"><span><xsl:value-of select="$i18n.payAndSubmit" /></span></a>
					</div>
					
				</div>
			
			</form>
			
		</section>
	
	</xsl:template>
	
	<xsl:template match="InlinePaymentForm">
		
		<section class="service">
			
			<form id="paymentForm" method="GET" action="{/Document/requestinfo/uri}">
			
				<input id="submitmode" type="hidden" value="true" />
				
				<xsl:call-template name="createFlowInstanceManagerPreviewHeader">
					<xsl:with-param name="showSaveButton" select="'false'"/>
					<xsl:with-param name="showFlowInstanceID" select="'false'"/>
				</xsl:call-template>
			
				<div class="service-navigator-wrap">
					<div>
						<a data-icon-after="&lt;" href="#" class="js-prev" aria-label="{$i18n.Previous}">
		  					<span><xsl:value-of select="$i18n.Previous" /></span>
		  				</a>
						<ul class="service-navigator primary">
							<xsl:apply-templates select="FlowInstance/Flow/Steps"/>
						</ul>
						<a data-icon-after="&gt;" href="#" class="js-next" aria-label="{$i18n.Next}">
		  					<span><xsl:value-of select="$i18n.Next" /></span>
		  				</a>
					</div>
				</div>
				
				<div class="section-full push">
					
					<h2 class="h1 hide-tablet"><xsl:value-of select="$i18n.payHeader" /></h2>
					
					<xsl:value-of select="ViewFragment/HTML" disable-output-escaping="yes"/>
					
					<div class="navigator-buttons clearboth">
					
						<xsl:choose>
							<xsl:when test="FlowInstance/Flow/usePreview = 'true'">
								
								<a href="#" class="btn btn-light xl prev arrow-mobile" onclick="redirectFromPreview(event,'preview=1')" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="$i18n.preview" /></span></a>
								
							</xsl:when>
							<xsl:otherwise>
								
								<a href="#" class="btn btn-light xl prev arrow-mobile" onclick="redirectFromPreview(event)" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="FlowInstance/Flow/Steps/Step[position() = last()]/name" /></span></a>
								
							</xsl:otherwise>
						</xsl:choose>					
						
						<a href="#" class="btn btn-green xl next" onclick="submitStep('save-submit', event)" data-icon-after=">"><span><xsl:value-of select="$i18n.payAndSubmit" /></span></a>					
					
					</div>	
				</div>
			
				<ul id="futureNavigator" class="service-navigator hide-desktop clearfix">
  				</ul>
			
			</form>
			
		</section>
		
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewHeader">
	
		<xsl:param name="showSaveButton" select="'true'"/>
		<xsl:param name="showFlowInstanceID" select="'true'"/>
	
		<div class="section-inside step">
			<div class="heading-wrapper">
				<div class="inner">
					<figure>
 						<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
	 				</figure>
	 				<div class="heading">
						<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name"/></h1>
						<xsl:if test="$showFlowInstanceID = 'true' and FlowInstance/flowInstanceID and FlowInstance/firstSubmitted">
							<span class="errandno"><xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" /></span>
						</xsl:if>
					</div>
				</div>
			</div>
		</div>
		
		<xsl:if test="loggedIn and $showSaveButton = 'true'">
			<div class="aside-inside step hide-mobile hide-tablet">
				<div class="section yellow">
					<div class="inner">
						<xsl:value-of select="$i18n.SaveBoxDescription" />
						<a class="btn btn-green btn-inline xl" id="save_errand" href="#" onclick="submitStep('save-preview', event)"><xsl:value-of select="$i18n.Save" /></a>
					</div>
				</div>
			</div>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewSubmitButton">
	
		<a href="#" class="btn btn-green xl next" onclick="redirectFromPreview(event, 'save-submit=true')"><xsl:value-of select="$i18n.submit" /></a>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerPreviewSigningButton">
	
		<a href="#" class="btn btn-green xl next" onclick="redirectFromPreview(event, 'save-submit=true')"><xsl:value-of select="$i18n.signAndSubmit.Goto" /></a>
	
	</xsl:template>
	
	<xsl:template match="ManagerResponse">

		<xsl:param name="mode" select="'preview'" />	
		<xsl:variable name="stepID" select="currentStepID"/>
	
		<div class="section-full {$mode}">
			
			<h2 data-icon-before="c" class="h1">
				<xsl:value-of select="currentStepIndex + 1"/>
				<xsl:text>. </xsl:text>
				<xsl:value-of select="../../FlowInstance/Flow/Steps/Step[stepID = $stepID]/name"/>
			</h2>
			
			<xsl:choose>
				<xsl:when test="QueryResponses">
					<xsl:apply-templates select="QueryResponses/QueryResponse"/>
				</xsl:when>
				<xsl:otherwise>
					<p><xsl:value-of select="$i18n.noAnsweredQueriesInThisStep"/></p>
				</xsl:otherwise>
			</xsl:choose>
			<!--
			<xsl:if test="$mode = 'receipt' and position() = last()">
				<div class="navigator-buttons">
					<a href="#" class="btn btn-right btn-light xl hide-mobile" onclick="event.preventDefault(); window.print();"><xsl:value-of select="$i18n.Print" /></a>
				</div>
			</xsl:if>
			 -->
		</div>
	
		<xsl:if test="position() != last()">
			<div class="divider preview" />
		</xsl:if>
	
	</xsl:template>
			
	<xsl:template match="FlowInstanceManagerSubmitted">

		<section class="service">
			
			<div class="section-full padtop receipt">
  				
				<div>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="AlternateSubmitMessageHeader or AlternateSubmitMessageText">
								info-box success alternatesubmittext
							</xsl:when>
							<xsl:otherwise>
								info-box success
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				
					<span data-icon-before="c">
						<xsl:choose>
							<xsl:when test="AlternateSubmitMessageHeader">
								<strong><xsl:value-of select="AlternateSubmitMessageHeader" /></strong>
							</xsl:when>
							<xsl:otherwise>
								<strong><xsl:value-of select="$i18n.FlowInstanceManagerSubmitted" /></strong>
							</xsl:otherwise>				
						</xsl:choose>
						<i class="icon close">x</i>
					</span>
					<div class="marker"></div>
				</div>
				
				<div>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="AlternateSubmitMessageHeader or AlternateSubmitMessageText">
								info-box-message success alternatesubmittext
							</xsl:when>
							<xsl:otherwise>
								info-box-message success
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					
					<xsl:choose>
						<xsl:when test="AlternateSubmitMessageText">
							<xsl:value-of select="AlternateSubmitMessageText" disable-output-escaping="yes"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="FlowInstance/Flow/submittedMessage" disable-output-escaping="yes"/>
						</xsl:otherwise>
					</xsl:choose>
						
				</div>
	  				
  				<div class="heading-wrapper">
  				
  					<xsl:variable name="submittedEvents" select="FlowInstance/events/FlowInstanceEvent[eventType='SUBMITTED']" />
  				
	  				<div class="errand-header">
  						<h1>
  							<xsl:value-of select="$i18n.Receipt" />
  						</h1>
  						<a id="show-receipt-btn" href="#" class="btn btn-right btn-light"><xsl:value-of select="$i18n.ShowReceipt" /></a>
  						<a id="hide-receipt-btn" href="#" class="btn btn-right btn-light hidden"><xsl:value-of select="$i18n.HideReceipt" /></a>
  						<xsl:choose>
							<xsl:when test="PDFLink">
								<a href="{PDFLink}" class="btn btn-right btn-light marginright"><xsl:value-of select="$i18n.DownloadPDF" /></a>	
							</xsl:when>
							<xsl:otherwise>
								<a href="#" class="btn btn-right btn-light marginright hide-mobile" onclick="event.preventDefault(); window.print();"><xsl:value-of select="$i18n.Print" /></a>
							</xsl:otherwise>
						</xsl:choose>
					</div>
					<div class="errand-info">
						<h2 class="h1">
							<figure>
			  					<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
			  				</figure>
							<xsl:value-of select="FlowInstance/Flow/name" />
						</h2>
						<p>
							<xsl:if test="not(FlowInstance/Flow/hideFlowInstanceIDFromUser = 'true')">
								<xsl:value-of select="$i18n.FlowInstanceID" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FlowInstance/flowInstanceID" />
							</xsl:if>
						</p>
						<p>
							<xsl:value-of select="$i18n.PostedBy" /><xsl:text>:&#160;</xsl:text>
							<xsl:call-template name="PrintPostedBy">
								<xsl:with-param name="poster" select="$submittedEvents[position() = last()]/poster/user"/>
								<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
							</xsl:call-template>
							<br/>
							<xsl:value-of select="$submittedEvents[position() = last()]/added" />
						</p>
					</div>
  				</div>
  			</div>
  			
  			<div class="divider"></div>
  			
  			<div class="queries receipt">
				
				<xsl:apply-templates select="ManagerResponses/ManagerResponse">
					<xsl:with-param name="mode" select="'receipt'" />
				</xsl:apply-templates>
				
				<div class="divider"></div>
				
			</div>
			
			<xsl:if test="SubmitSurveyHTML">
				<div class="survey-wrapper section-full">				
					<xsl:value-of select="SubmitSurveyHTML" disable-output-escaping="yes" />
				</div>
			</xsl:if>
			
		</section>
	
	</xsl:template>
	
	<xsl:template name="FlowInstanceManagerFormFollower">
		<xsl:param name="currentStepID"/>
		<xsl:param name="totalStepCount"/>
	
		<div class="panel-wrapper follow" style="display: none">
	 		<div class="inner">
	 			<div class="current-step" data-step="1">
	 				<xsl:value-of select="$i18n.StepDescription.Part1" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="ManagerResponse/currentStepIndex + 1" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.StepDescription.Part2" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$totalStepCount" />
					<xsl:text>:&#160;</xsl:text>
					<xsl:value-of select="FlowInstance/Flow/Steps/Step[stepID = $currentStepID]/name" />
	 			</div>
 				<xsl:if test="not(FlowInstance/Flow/hideSaveButton = 'true')">
		 			<div class="buttons">
		 				<a href="#" data-action="save_errand" class="btn btn-green" onclick="submitStep('save', event)"><xsl:value-of select="$i18n.save" /><span class="hide-mobile"><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.saveBtnSuffix" /></span></a>
		 			</div>
 				</xsl:if>
	 		</div>
	 	</div>
	
	</xsl:template>
			
	<xsl:template match="FlowInstanceManagerForm" name="BaseFlowInstanceManagerForm">
	
		<xsl:variable name="currentStepID" select="ManagerResponse/currentStepID" />
		<xsl:variable name="stepCount" select="count(FlowInstance/Flow/Steps/Step)" />
		<xsl:variable name="usePreviewStepCount">
			<xsl:choose>
				<xsl:when test="FlowInstance/Flow/usePreview = 'true'">1</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="hideSubmitStepTextStepCount">
			<xsl:choose>
				<xsl:when test="FlowInstance/Flow/hideSubmitStepText = 'true'">0</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="totalStepCount">
			<xsl:value-of select="$stepCount + $usePreviewStepCount + $hideSubmitStepTextStepCount" />
		</xsl:variable>
	
		<xsl:if test="loggedIn">
	
		 	<xsl:call-template name="FlowInstanceManagerFormFollower">
		 		<xsl:with-param name="currentStepID" select="$currentStepID"/>
		 		<xsl:with-param name="totalStepCount" select="$totalStepCount"/>
		 	</xsl:call-template>
		 	
		 	<xsl:if test="not(FlowInstance/Flow/hideSaveButton = 'true')">
		 		<xsl:call-template name="createMobileSavePanel" />
		 	</xsl:if>
	 		
	 	</xsl:if>
	
		<xsl:if test="ManagerResponse/concurrentModificationLock = 'true'">
			<section class="modal error">
  				<span data-icon-before="!">
  					<a href="{/Document/requestinfo/uri}?close-reopen=1" onclick="return confirm('{$i18n.FlowInstanceConcurrentlyModifiedConfirm}')" title="{$i18n.FlowInstanceConcurrentlyModifiedLinkTitle}">
  						<xsl:value-of select="$i18n.FlowInstanceConcurrentlyModified" />
  					</a>
  				</span>
  			</section>
		</xsl:if>
	
		<xsl:if test="ManagerResponse/ValidationErrors != 'true' and not(validationError)">
			<xsl:apply-templates select="lastFlowAction" />
		</xsl:if>
	
		<xsl:apply-templates select="validationError"/>

		<section class="service has-navigator">
			
			<form method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
			
				<input id="submitmode" type="hidden" value="true" />
			
				<xsl:call-template name="createFlowInstanceManagerFormHeader">
				
					<xsl:with-param name="showSaveButton">
						<xsl:choose>
							<xsl:when test="ManagerResponse/concurrentModificationLock = 'true'">false</xsl:when>
							<xsl:otherwise>true</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>				
				
				</xsl:call-template>
				
				<!-- <div class="section-full" /> -->
				
				<div class="service-navigator-wrap">
					<div>
						<a data-icon-after="&lt;" href="#" class="js-prev" aria-label="{$i18n.Previous}">
		  					<span><xsl:value-of select="$i18n.Previous" /></span>
		  				</a>
						<ul class="service-navigator primary">
							<xsl:apply-templates select="FlowInstance/Flow/Steps"/>
						</ul>
						<a data-icon-after="&gt;" href="#" class="js-next" aria-label="{$i18n.Next}">
		  					<span><xsl:value-of select="$i18n.Next" /></span>
		  				</a>
					</div>
				</div>
				
				<div class="section-full push">
					
					<div class="queries">
					
						<xsl:apply-templates select="ManagerResponse/QueryResponses/QueryResponse"/>
						
						<xsl:if test="not(ManagerResponse/QueryResponses/QueryResponse/HTML)">
							<p><xsl:value-of select="$i18n.NoQueriesInCurrentStep" /></p>
						</xsl:if>
					
					</div>
					
					<xsl:variable name="currentStepIndex" select="ManagerResponse/currentStepIndex" />
					
					<div class="navigator-buttons">
						<xsl:if test="ManagerResponse/currentStepIndex > 0">
								<a href="#" class="btn btn-light xl prev arrow-mobile force-submit" onclick="submitStep('back', event)" data-icon-before="&#60;"><span class="only-mobile"><xsl:value-of select="$i18n.previousStep" /></span><span class="hide-mobile"><xsl:value-of select="FlowInstance/Flow/Steps/Step[position() = $currentStepIndex]/name" /></span></a>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="$currentStepID = FlowInstance/Flow/Steps/Step[last()]/stepID">
								
								<xsl:choose>
									<xsl:when test="FlowInstance/Flow/usePreview = 'true'">
										<a href="#" class="btn btn-green xl next arrow-mobile force-submit" onclick="submitStep('preview', event)" data-icon-after=">"><span class="only-mobile"><xsl:value-of select="$i18n.nextStep" /></span><span class="hide-mobile"><xsl:value-of select="$i18n.preview" /></span></a>
									</xsl:when>
									<xsl:otherwise>
									
										<xsl:if test="ManagerResponse/concurrentModificationLock = 'false'">
										
											<xsl:choose>
												<xsl:when test="FlowInstance/Flow/requireSigning = 'true' and not(FlowInstance/Flow/skipPosterSigning = 'true')">
													<xsl:call-template name="createFlowInstanceManagerFormSigningButton" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="createFlowInstanceManagerFormSubmitButton" />
												</xsl:otherwise>
											</xsl:choose>
										
										</xsl:if>
										
									</xsl:otherwise>
								</xsl:choose>
								
							</xsl:when>
							<xsl:otherwise>
									<xsl:value-of select="currentStepIndex" />
									<a href="#" class="btn btn-green xl next arrow-mobile force-submit" onclick="submitStep('forward', event)" data-icon-after=">"><span class="only-mobile"><xsl:value-of select="$i18n.nextStep" /></span><span class="hide-mobile"><xsl:value-of select="FlowInstance/Flow/Steps/Step[position() = ($currentStepIndex + 2)]/name" /></span></a>
							</xsl:otherwise>
						</xsl:choose>
					</div>

					<div id="ajaxLoadingMessage" style="display: none">
						<h1><xsl:value-of select="$i18n.AjaxLoading" />..</h1><p class="tiny"><a href="javascript:void(0)" onclick="cancelAjaxPost()"><xsl:value-of select="$i18n.AjaxCancel" />.</a></p>
					</div>
				
					<div id="submitLoadingMessage" style="display: none">
						<h1><xsl:value-of select="$i18n.SubmitLoading" />..</h1>
					</div>
				
					<div id="ajaxErrorMessage" style="display: none">
						<h1><xsl:value-of select="$i18n.UnExpectedAjaxError" /></h1>
						<p><xsl:value-of select="$i18n.UnExpectedAjaxErrorDescription" /></p>
						<input id="AjaxRetryButton" type="button" value="{$i18n.AjaxRetry}" onclick="retryAjaxPost()" class="marginright btn btn-blue" /> 
						<input id="AjaxReloadButton" type="button" value="{$i18n.AjaxReload}" onclick="reloadCurrentStep()" class="btn btn-blue" />
					</div>
					
				</div>
			
				<ul id="futureNavigator" class="service-navigator hide-desktop clearfix">
  				</ul>
			
			</form>
			
		</section>
		
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerFormHeader">
		
		<xsl:param name="showSaveButton" select="'true'"/>
		
		<div class="section-inside step">
			<div class="heading-wrapper">
				<div class="inner">
					<figure>
 						<img src="{/Document/requestinfo/currentURI}/{/Document/module/alias}/icon/{FlowInstance/Flow/flowID}?{FlowInstance/Flow/IconLastModified}" alt="" />
	 				</figure>
	 				<div class="heading">
						<h1 class="xl"><xsl:value-of select="FlowInstance/Flow/name"/></h1>
					</div>
				</div>
			</div>
		</div>
		
		<xsl:if test="loggedIn and $showSaveButton = 'true' and not(FlowInstance/Flow/hideSaveButton = 'true')">
			<div class="aside-inside step hide-mobile hide-tablet">
				<div class="section yellow">
					<div class="inner">
						<xsl:value-of select="$i18n.SaveBoxDescription" />
						<a class="btn btn-green btn-inline xl force-submit" id="save_errand" href="#" onclick="submitStep('save', event)"><xsl:value-of select="$i18n.Save" /></a>
					</div>
				</div>
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerFormSubmitButton">
	
		<a href="#" class="btn btn-green xl next force-submit" onclick="submitStep('save-submit', event)"><xsl:value-of select="$i18n.submit" /></a>
	
	</xsl:template>
	
	<xsl:template name="createFlowInstanceManagerFormSigningButton">
	
		<a href="#" class="btn btn-green xl next force-submit" onclick="submitStep('save-submit', event)"><xsl:value-of select="$i18n.signAndSubmit.Goto" /></a>
	
	</xsl:template>
	
	<xsl:template name="createMobileSavePanel">
		
		<div class="panel-wrapper yellow hide-desktop">
	 		<div class="inner">
	 			<div class="current-step">
	 				<xsl:value-of select="$i18n.ShortSaveBoxDescription" />
	 			</div>
	 			<div class="buttons">
	 				<a class="btn btn-green force-submit" data-action="save_errand" href="#" onclick="submitStep('save', event)"><xsl:value-of select="$i18n.save" /></a>
	 			</div>
	 		</div>
	 	</div>
		
	</xsl:template>
	
	<xsl:template match="Steps">
	
		<xsl:variable name="stepCount" select="count(Step)" />
	
		<xsl:apply-templates select="Step" />
		
		<xsl:variable name="previewOffset">
			<xsl:choose>
				<xsl:when test="../usePreview = 'true'">
					<xsl:value-of select="1"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="0"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:if test="../usePreview = 'true'">
			<li>
				<xsl:if test="/Document/FlowInstanceManagerPreview">
					<xsl:attribute name="class">active</xsl:attribute>
					<xsl:attribute name="aria-current">step</xsl:attribute>
				</xsl:if>
				
				<span data-step="{$stepCount + 1}">
					<xsl:value-of select="$i18n.preview"/>
				</span>
			</li>
		</xsl:if>

		<xsl:choose>
			<xsl:when test="../paymentSupportEnabled = 'true' and ../requireSigning = 'true' and not(../skipPosterSigning = 'true')">
			
				<xsl:call-template name="createSubmitStep">
					<xsl:with-param name="step" select="$stepCount + 1 + $previewOffset" />
					<xsl:with-param name="submitText" select="$i18n.Sign" />
				</xsl:call-template>
			
				<li>
					<xsl:if test="/Document/InlinePaymentForm or StandalonePaymentForm">
						<xsl:attribute name="class">active</xsl:attribute>
						<xsl:attribute name="aria-current">step</xsl:attribute>
					</xsl:if>
					
					<span data-step="{$stepCount + 2 + $previewOffset}">
						<xsl:value-of select="$i18n.payAndSubmit"/>
					</span>
				</li>
			
			</xsl:when>
			<xsl:when test="../paymentSupportEnabled = 'true'">
				
				<li>
					<xsl:if test="/Document/InlinePaymentForm or StandalonePaymentForm">
						<xsl:attribute name="class">active</xsl:attribute>
						<xsl:attribute name="aria-current">step</xsl:attribute>
					</xsl:if>
					
					<span data-step="{$stepCount + 1 + $previewOffset}">
						<xsl:value-of select="$i18n.payAndSubmit"/>
					</span>
				</li>
			
			</xsl:when>
			<xsl:when test="../requireSigning = 'true' and not(../skipPosterSigning = 'true')">
				
				<xsl:call-template name="createSubmitStep">
					<xsl:with-param name="step" select="$stepCount + 1 + $previewOffset" />
					<xsl:with-param name="submitText" select="$i18n.signAndSubmit" />
				</xsl:call-template>
			
			</xsl:when>
			<xsl:otherwise>
			
				<xsl:if test="../hideSubmitStepText != 'true'">
					<xsl:call-template name="createSubmitStep">
						<xsl:with-param name="step" select="$stepCount + 1 + $previewOffset" />
						<xsl:with-param name="submitText" select="$i18n.submit" />
					</xsl:call-template>
				</xsl:if>
			
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="createSubmitStep">
		
		<xsl:param name="step" />
		<xsl:param name="submitText" />
		
		<li>
			<xsl:if test="/Document/SigningForm">
				<xsl:attribute name="class">active</xsl:attribute>
				<xsl:attribute name="aria-current">step</xsl:attribute>
			</xsl:if>
				
			<span data-step="{$step}"><xsl:value-of select="$submitText" /></span>
		</li>
		
	</xsl:template>
	
	<xsl:template match="Step">
		
		<xsl:variable name="currentStepID" select="../../../../ManagerResponse/currentStepID" />
		<xsl:variable name="currentStepIndex" select="//ManagerResponse[position() = last()]/currentStepIndex" />
	
		<li>
			
			<xsl:choose>
				<xsl:when test="$currentStepID = stepID"><xsl:attribute name="class">active</xsl:attribute><xsl:attribute name="aria-current">step</xsl:attribute></xsl:when>
				<xsl:when test="$currentStepIndex >= sortIndex">
					<xsl:attribute name="class">
						<xsl:text>completed</xsl:text>
					</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<span data-step="{position()}">
				<xsl:value-of select="name"/>
			</span>
		</li>
	
	</xsl:template>	
	
	<xsl:template match="QueryResponse">
	
		<xsl:choose>
			<xsl:when test="HTML">
				<xsl:value-of select="HTML" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:otherwise>
				<div id="query_{QueryDescriptor/queryID}" class="hidden" />
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='PreviewNotEnabledForCurrentFlow']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.PreviewNotEnabledForCurrentFlow"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='PreviewOnlyAvailableWhenFlowFullyPopulated']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.PreviewOnlyAvailableWhenFlowFullyPopulated"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='SubmitOnlyAvailableWhenFlowFullyPopulated']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.SubmitOnlyAvailableWhenFlowFullyPopulated"></xsl:with-param>
		</xsl:call-template>
			
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='UnableToPopulateQueryInstance']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnableToPopulateQueryInstance"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='UnableToSaveQueryInstance']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnableToSaveQueryInstance"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="validationError[messageKey='UnableToResetQueryInstance']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnableToResetQueryInstance"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>						
	
	<xsl:template match="validationError[messageKey='FileUploadException']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message">
				<xsl:value-of select="$i18n.FileUploadException.part1"/>
				<xsl:value-of select="maxRequestSize"/>
				<xsl:value-of select="$i18n.FileUploadException.part2"/>
			</xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>		
	
	<xsl:template match="validationError[messageKey='SigningProviderNotFoundError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message">
				<xsl:value-of select="$i18n.SigningProviderNotFoundError"/>
			</xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='PaymentProviderNotFoundError']">
	
		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message">
				<xsl:value-of select="$i18n.PaymentProviderNotFoundError"/>
			</xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError">

		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnknownValidationError"></xsl:with-param>
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
	
	<xsl:template match="lastFlowAction">
		
		<xsl:if test=". = 'SAVE' or . = 'SAVE_AND_PREVIEW'">

			<section class="modal success">
				<div>
					<span data-icon-before="c"><xsl:value-of select="$i18n.FlowInstanceSaved" /></span>
				</div>
				
				<xsl:if test="../SavedInstanceCullingMessage">
					<div>
						<span><xsl:value-of select="../SavedInstanceCullingMessage" disable-output-escaping="yes" /></span>
					</div>
				</xsl:if>
				
				<i class="icon close">x</i>
			</section>
			
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="getEventTypeText">
		
		<xsl:choose>
			<xsl:when test="eventType = 'SUBMITTED'">
				<xsl:value-of select="$i18n.SubmittedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'SIGNED'">
				<xsl:value-of select="$i18n.SignedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'SIGNING_SKIPPED'">
				<xsl:value-of select="$i18n.SigningSkippedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'PAYED'">
				<xsl:value-of select="$i18n.PayedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'UPDATED'">
				<xsl:value-of select="$i18n.UpdatedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'STATUS_UPDATED'">
				<xsl:value-of select="$i18n.StatusUpdatedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'MANAGERS_UPDATED'">
				<xsl:value-of select="$i18n.ManagersUpdatedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'OWNERS_UPDATED'">
				<xsl:value-of select="$i18n.OwnersUpdatedEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'CUSTOMER_NOTIFICATION_SENT'">
				<xsl:value-of select="$i18n.CustomerNotificationEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'CUSTOMER_MESSAGE_SENT'">
				<xsl:value-of select="$i18n.CustomerMessageSentEvent" />
			</xsl:when>
			<xsl:when test="eventType = 'MANAGER_MESSAGE_SENT'">
				<xsl:value-of select="$i18n.ManagerMessageSentEvent" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.OtherEvent" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="PrintPostedBy">
		<xsl:param name="poster"/>
		<xsl:param name="flowInstanceAttributes" select="null"/>
		<xsl:param name="fallbackAttributes" select="null"/>
		
		<xsl:choose>
		
			<xsl:when test="$flowInstanceAttributes and $flowInstanceAttributes/Attribute[Name = 'organizationName']">
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'organizationName']/Value" />
				
				<xsl:choose>
					
					<xsl:when test="$poster">
						<xsl:text>&#160;(</xsl:text>
						<xsl:call-template name="printUser">
							<xsl:with-param name="user" select="$poster" />
						</xsl:call-template>
						<xsl:text>)</xsl:text>
					</xsl:when>
					
					<xsl:when test="$flowInstanceAttributes/Attribute[Name = 'firstname']">
						<xsl:text>&#160;(</xsl:text>
						<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'firstname']/Value" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'lastname']/Value" />
						<xsl:text>)</xsl:text>
					</xsl:when>
					
				</xsl:choose>
				
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="PrintPostedByNoOrg">
					<xsl:with-param name="poster" select="$poster"/>
					<xsl:with-param name="flowInstanceAttributes" select="$flowInstanceAttributes"/>
					<xsl:with-param name="fallbackAttributes" select="$fallbackAttributes"/>
				</xsl:call-template>
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="PrintPostedByNoOrg">
		<xsl:param name="poster"/>
		<xsl:param name="flowInstanceAttributes" select="null"/>
		<xsl:param name="fallbackAttributes" select="null"/>
		
		<xsl:choose>
		
			<xsl:when test="$poster">
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="$poster" />
				</xsl:call-template>
			</xsl:when>
			
			<xsl:when test="$flowInstanceAttributes and $flowInstanceAttributes/Attribute[Name = 'firstname']">
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'firstname']/Value" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'lastname']/Value" />
			</xsl:when>
			
			<xsl:when test="$fallbackAttributes and $fallbackAttributes/Attribute[Name = 'firstname']">
				<xsl:value-of select="$fallbackAttributes/Attribute[Name = 'firstname']/Value" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$fallbackAttributes/Attribute[Name = 'lastname']/Value" />
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="$i18n.AnonymousUser" />
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="PrintAdminPostedBy">
		<xsl:param name="poster"/>
		
		<xsl:choose>
		
			<xsl:when test="$poster">
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="$poster" />
				</xsl:call-template>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="$i18n.RemovedUser" />
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="printUser">
		<xsl:param name="user" />
		<xsl:param name="hideUsername" select="''" />
		
		<xsl:value-of select="$user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$user/lastname" />
		
		<xsl:if test="$hideUsername = '' and $user/username">
			<xsl:text>&#160;(</xsl:text>
			<xsl:value-of select="$user/username" />
			<xsl:text>)</xsl:text>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>