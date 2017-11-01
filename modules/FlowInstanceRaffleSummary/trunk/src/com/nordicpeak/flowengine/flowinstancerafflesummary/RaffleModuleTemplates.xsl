<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>
	
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-ui.js
		/timepicker/js/jquery.timepicker.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>
	
	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
		/timepicker/css/jquery.timepicker.css
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/widget-saveSort.min.js
		/js/widget-stickyHeaders.min.js
		/js/widget-storage.min.js
		/js/jquery.ui.datepicker-sv.js
		/js/tableSummary.js
		/js/raffle.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
		/css/summary.css
		/css/raffle.css
		/css/tablesorter.css
	</xsl:variable>

	<xsl:template match="Document">
		<div class="contentitem">
			
			<xsl:apply-templates select="ListRounds"/>
			<xsl:apply-templates select="Summary"/>
			<xsl:apply-templates select="AddRaffleRound"/>
			<xsl:apply-templates select="UpdateRaffleRound"/>
			
		</div>
	</xsl:template>
	
	<xsl:template match="ListRounds">
	
		<xsl:apply-templates select="ValidationErrors/validationError" />
	
		<h1><xsl:value-of select="$i18n.RoundsTitle"/></h1>
		
		<p><xsl:value-of select="$i18n.RoundsDescription"/></p>
		
		<table class="coloredtable full oep-table">
			<thead class="sortable">
				<tr>
					<th>
						<span>
							<xsl:value-of select="$i18n.Name"/>
						</span>
					</th>
					<th width="150px">
						<span>
							<xsl:value-of select="$i18n.PeriodStart"/>
						</span>
					</th>
					<th width="150px">
						<span>
							<xsl:value-of select="$i18n.PeriodEnd"/>
						</span>
					</th>
					<th width="150px">
						<span>
							<xsl:value-of select="$i18n.FlowInstanceCount"/>
						</span>
					</th>
					<xsl:if test="RaffleAdmin">
						<th width="50px"/>
					</xsl:if>
				</tr>
			</thead>
			<tbody>
			
				<xsl:choose>
					<xsl:when test="Rounds/Round">
					
						<xsl:apply-templates select="Rounds/Round"/>
					
					</xsl:when>
					<xsl:otherwise>
					
						<tr>
							<td colspan="4">
								<xsl:if test="RaffleAdmin">
									<xsl:attribute name="colspan" >5</xsl:attribute>
								</xsl:if>
								
								<xsl:value-of select="$i18n.NoRounds"/>
							</td>
						</tr>
					
					</xsl:otherwise>
				</xsl:choose>
				
			</tbody>
		</table>
		
		<xsl:if test="RaffleAdmin">
			<div class="clearboth floatright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addround" title="{$i18n.AddRaffleRound}">
					<xsl:value-of select="$i18n.AddRaffleRound"/>
					<img class="alignbottom marginleft" src="{$imagePath}/add.png"/>
				</a>
			</div>
		</xsl:if>
			
	</xsl:template>
	
	<xsl:template match="Round">
	
		<tr>
			<td>
				<a href="{URL}" title="{$i18n.ShowRound}">
					<xsl:value-of select="Name"/>
				</a>
			</td>
			<td>
				<xsl:value-of select="Start"/>
			</td>
			<td>
				<xsl:value-of select="End"/>
			</td>
			<td>
				<xsl:value-of select="FlowInstanceCount"/>
			</td>
			
			<xsl:if test="../../RaffleAdmin">
				<td>
					<a class="floatleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateround/{ID}" title="{$i18n.UpdateRaffleRound}">
						<img class="alignbottom" src="{$imagePath}/pen.png"/>
					</a>
					<a class="floatleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteround/{ID}" onclick="return confirm('{$i18n.DeleteRaffleRound.Confirm}: {Name}?');" title="{$i18n.DeleteRaffleRound}: {Name}">
						<img src="{$imagePath}/delete.png"/>
					</a>
				</td>
			</xsl:if>
		
		</tr>
	
	</xsl:template>
	
	<xsl:template match="Summary">
	
		<xsl:if test="RaffleAdmin">
			<div class="floatright">
				<a class="floatleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateround/{Round/roundID}" title="{$i18n.UpdateRaffleRound}">
					<img class="alignbottom" src="{$imagePath}/pen.png"/>
				</a>
				<a class="floatleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deleteround/{Round/roundID}" onclick="return confirm('{$i18n.DeleteRaffleRound.Confirm}: {Round/name}?');" title="{$i18n.DeleteRaffleRound}: {Round/name}">
					<img src="{$imagePath}/delete.png"/>
				</a>
			</div>
		</xsl:if>
		
		<h1><xsl:value-of select="Round/name"/></h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError" />
		
		<p>
			<xsl:value-of select="$i18n.RoundDescription"/>
		</p>
		
		<form method="post" action="">
		
			<xsl:call-template name="InstanceTable"/>
			
			<div class="bigmargintop">
				
				<div class="floatright marginright">
					<xsl:choose>
						<xsl:when test="AddFlowInstance">
						
							<a class="btn btn-green btn-inline" title="" onclick="$(this).addClass('btn-light'); $(this).removeClass('btn-green');" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add/{Round/roundID}">
								<xsl:value-of select="$i18n.Button.AddInstance"/>
							</a>
						
						</xsl:when>
						<xsl:when test="AddFlowInstanceModule">
						
							<a class="btn btn-light btn-inline" title="" onclick="alert('{$i18n.AddInstance.RoundNotConfiguredForAdd}'); return false;" href="#">
								<xsl:value-of select="$i18n.Button.AddInstance"/>
							</a>
						
						</xsl:when>
						<xsl:otherwise>
						
							<a class="btn btn-light btn-inline" title="" onclick="alert('{$i18n.AddInstance.FlowInstanceModuleMissing}'); return false;" href="#">
								<xsl:value-of select="$i18n.Button.AddInstance"/>
							</a>
						
						</xsl:otherwise>
					</xsl:choose>
					
				</div>
				
				<div class="floatright marginright">
					<a class="btn btn-red btn-inline" title="" href="#" onclick="$(this).addClass('btn-light'); $(this).removeClass('btn-red'); var form = $(this).closest('form'); form.attr('action', '{/Document/requestinfo/currentURI}/{/Document/module/alias}/raffle/{Round/roundID}'); form.submit(); return false;">
						<xsl:value-of select="$i18n.Button.Raffle"/>
					</a>
				</div>
				
				<div class="floatright bigmarginright">
					<a class="btn btn-light btn-inline" title="" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/list">
						<xsl:value-of select="$i18n.Button.Back"/>
					</a>
				</div>
				
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="AddRaffleRound">
	
		<h1><xsl:value-of select="$i18n.AddRound.title"/></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form id="flowForm" method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addround">
		
			<xsl:call-template name="raffleRoundForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddRound.submit}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateRaffleRound">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateRound.title"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="Round/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateround/{Round/roundID}">
		
			<xsl:call-template name="raffleRoundForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.UpdateRound.submit}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="raffleRoundForm">
		
		<script>
			$(function() {
				$( "#startDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.startDate"/>',
					changeMonth: true,
            		changeYear: true,
            		showWeek: true,
				});
				
				$( "#endDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.endDate"/>',
					changeMonth: true,
            		changeYear: true,
            		showWeek: true,
				});
			});	
		</script>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.Name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="Round" />
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
		
			<div class="floatleft">
				
				<label for="startDate" class="floatleft">
					<xsl:value-of select="$i18n.startDate" />
				</label>
				
				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'startDate'" />
						<xsl:with-param name="id" select="'startDate'" />
						<xsl:with-param name="element" select="Round" />
						<xsl:with-param name="size" select="10" />
						
					</xsl:call-template>
				</div>
			</div>
			
			<div class="bigmarginleft floatleft">
				
				<label for="endDate" class="floatleft">
					<xsl:value-of select="$i18n.endDate" />
				</label>
				
				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'endDate'" />
						<xsl:with-param name="id" select="'endDate'" />
						<xsl:with-param name="element" select="Round" />
						<xsl:with-param name="size" select="10" />
					</xsl:call-template>
				</div>
			</div>
		
		</div>
		
		<div class="floatleft full bigmarginbottom">
				
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'overrideStatusChangedNotification'"/>
					<xsl:with-param name="name" select="'overrideStatusChangedNotification'"/>
					<xsl:with-param name="element" select="Round" />
				</xsl:call-template>
				
				<label for="overrideStatusChangedNotification" class="marginleft">
					<xsl:value-of select="$i18n.OverideStatusChangedNotificationOnDecision" />
				</label>
			</div>
		</div>
		
		<div id="statusChangedMessages">
			<div class="floatleft full bigmarginbottom">
				
				<label for="decisionEmailMessage" class="floatleft full">
					<xsl:value-of select="$i18n.DecisionEmailMessage" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'decisionEmailMessage'"/>
						<xsl:with-param name="name" select="'decisionEmailMessage'"/>
						<xsl:with-param name="class" select="'ckeditor'"/>
						<xsl:with-param name="element" select="Round" />
						<xsl:with-param name="value">
							<xsl:if test="not(Round)">
								<xsl:value-of select="defaultRaffleRoundDecisionEmailMessage"/>
							</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label for="decisionSMSMessage" class="floatleft full">
					<xsl:value-of select="$i18n.DecisionSMSMessage" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'decisionSMSMessage'"/>
						<xsl:with-param name="name" select="'decisionSMSMessage'"/>
						<xsl:with-param name="rows" select="2"/>
						<xsl:with-param name="element" select="Round" />
						<xsl:with-param name="value">
							<xsl:if test="not(Round)">
								<xsl:value-of select="defaultRaffleRoundDecisionSMSMessage"/>
							</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
				</div>
				
				<xsl:call-template name="addUserTagsTable"/>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label class="floatleft">
				<xsl:value-of select="$i18n.RaffleFlows"/>
			</label>
			
			<div class="floatleft full margintop marginbottom raffleflow-list">
			
				<xsl:choose>
					<xsl:when test="requestparameters">
					
						<xsl:apply-templates select="requestparameters/parameter[name='flowID']/value" mode="RaffleFlow">
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:apply-templates>
						
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="Round/RaffleFlows/RaffleFlow" mode="list" />
						
					</xsl:otherwise>
				</xsl:choose>
				
				<div id="raffleflow-template" style="display: none;">
					<xsl:call-template name="RaffleFlowTemplate"/>
				</div>
				
				<input name="connectorURL" disabled="true" hidden="true" value="{/Document/requestinfo/currentURI}/{/Document/module/alias}/flows"/>
				
			</div>
			
			<div class="floatleft full ui-widget">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id">
						<xsl:value-of select="'raffleflow-search'" />
					</xsl:with-param>
					<xsl:with-param name="class" select="'full border-box'" />
					<xsl:with-param name="width" select="''" />
					<xsl:with-param name="placeholder" select="$i18n.RaffleFlows.SearchPlaceholder" />
				</xsl:call-template>
			</div>
			
			<xsl:call-template name="initializeFCKEditor">
				<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
				<xsl:with-param name="customConfig">config.js</xsl:with-param>
				<xsl:with-param name="editorContainerClass">ckeditor</xsl:with-param>
				<xsl:with-param name="editorHeight">150</xsl:with-param>
				
				<xsl:with-param name="contentsCss">
					<xsl:if test="cssPath">
						<xsl:value-of select="cssPath"/>
					</xsl:if>
				</xsl:with-param>
				
			</xsl:call-template>
			
		</div>
			
	</xsl:template>
	
	<xsl:template match="RaffleFlow" mode="list" name="RaffleFlowTemplate">
				
		<div class="floatleft hover border full marginbottom border-radius lightbackground">
			
			<div class="marginleft margintop floatleft">
				<div class="marginleft">
					<h3>
						<xsl:value-of select="Flow/name" />
						<xsl:value-of select="' (v'" />
						<xsl:value-of select="Flow/version" />
						<xsl:value-of select="')'" />
					</h3>
				</div>
			</div>
			
			<input name="flowID" hidden="true" value="{flowID}"/>
			<input name="flowName-{flowID}" hidden="true" value="{Flow/name}"/>
			<input name="flowVersion-{flowID}" hidden="true" value="{Flow/version}"/>
			<input name="raffleFlowID-{flowID}" hidden="true" value="{raffleFlowID}"/>
			
			<div class="padding floatright">
				<div class="floatright marginright">
					<a href="#" onclick="if(confirm('{$i18n.DeleteRaffleFlow.Confirm}: {Flow/name}?'))$(this).closest('div.border-radius').remove(); return false;" title="{$i18n.DeleteRaffleFlow}: {Flow/name}">
						<img src="{$imagePath}/delete.png"/>
					</a>
				</div>
			</div>
			
			<div class="marginleft marginbottom floatleft clearboth">

				<div class="floatleft">
					<div class="marginleft floatleft" style="width: 270px">
							
						<label for="raffledStatusID-{flowID}" class="floatleft full">
							<xsl:value-of select="$i18n.raffledStatus" />
						</label>
						
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id">
								<xsl:value-of select="'raffledStatusID-'"/>
								<xsl:value-of select="flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name">
								<xsl:value-of select="'raffledStatusID-'"/>
								<xsl:value-of select="flowID"/>
							</xsl:with-param>
							<xsl:with-param name="valueElementName" select="'statusID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="element" select="Flow/Statuses/Status" />
							<xsl:with-param name="selectedValue" select="raffledStatusID"/>
						</xsl:call-template>
							
					</div>
					
					<div class="marginleft floatleft clearboth bigmargintop">
					
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id">
								<xsl:value-of select="'addFlowID-'"/>
								<xsl:value-of select="flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name" select="'addFlowID'"/>
							<xsl:with-param name="value" select="flowID" />
							<xsl:with-param name="checked" select="../../addFlowID = flowID" />
						</xsl:call-template>
						
						<label for="addFlowID-{flowID}">
							<xsl:value-of select="$i18n.UseForAddFlow" />
						</label>
					</div>
				</div>
				
				<div class="marginleft floatleft" style="width: 270px">
				
					<label for="excludedStatusIDs-{flowID}" class="floatleft full">
						<xsl:value-of select="$i18n.excludedStatuses" />
					</label>
					
					<xsl:call-template name="createMultipleDropdown">
						<xsl:with-param name="id">
							<xsl:value-of select="'excludedStatusIDs-'"/>
							<xsl:value-of select="flowID"/>
						</xsl:with-param>
						<xsl:with-param name="name">
							<xsl:value-of select="'excludedStatusIDs-'"/>
							<xsl:value-of select="flowID"/>
						</xsl:with-param>
						<xsl:with-param name="valueElementName" select="'statusID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="Flow/Statuses/Status" />
						<xsl:with-param name="selectedValues" select="ExcludedStatusIDs/value"/>
					</xsl:call-template>
					
				</div>
			</div>
			
		</div>
				
	</xsl:template>
	
	<xsl:template match="value" mode="RaffleFlow">
		<xsl:param name="requestparameters" select="requestparameters"/>
		
		<xsl:variable name="flowID" select="."/>
		<xsl:variable name="flowName" select="$requestparameters/parameter[name=concat('flowName-', $flowID)]/value"/>
		<xsl:variable name="flowVersion" select="$requestparameters/parameter[name=concat('flowVersion-', $flowID)]/value"/>
		
		<div class="floatleft hover border full marginbottom border-radius lightbackground">
			
			<div class="marginleft margintop floatleft">
				<div class="marginleft">
					<h3>
						<xsl:value-of select="$flowName" />
						<xsl:value-of select="' (v'" />
						<xsl:value-of select="$flowVersion" />
						<xsl:value-of select="')'" />
					</h3>
				</div>
			</div>
			
			<input name="flowID" hidden="true" value="{$flowID}"/>
			<input name="flowName-{$flowID}" hidden="true" value="{$flowName}"/>
			<input name="flowVersion-{$flowID}" hidden="true" value="{$flowVersion}"/>
			<input name="raffleFlowID-{$flowID}" hidden="true" value="{$requestparameters/parameter[name=concat('raffleFlowID-', $flowID)]/value}"/>
			
			<div class="padding floatright">
				<div class="floatright marginright">
					<a href="#" onclick="if(confirm('{$i18n.DeleteRaffleFlow.Confirm}: {$flowName}?'))$(this).closest('div.border-radius').remove(); return false;" title="{$i18n.DeleteRaffleFlow}: {$flowName}">
						<img src="{$imagePath}/delete.png"/>
					</a>
				</div>
			</div>
			
			<div class="marginleft marginbottom floatleft clearboth">

				<div class="floatleft">

					<div class="marginleft floatleft" style="width: 270px">
							
						<label for="raffledStatusID-{$flowID}" class="floatleft full">
							<xsl:value-of select="$i18n.raffledStatus" />
						</label>
						
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id">
								<xsl:value-of select="'raffledStatusID-'"/>
								<xsl:value-of select="$flowID"/>
							</xsl:with-param>
							<xsl:with-param name="name">
								<xsl:value-of select="'raffledStatusID-'"/>
								<xsl:value-of select="$flowID"/>
							</xsl:with-param>
							<xsl:with-param name="valueElementName" select="'statusID'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="element" select="../../../Flows/Flow[flowID = $flowID]/Statuses/Status" />
							<xsl:with-param name="requestparameters" select="$requestparameters"/>
						</xsl:call-template>
							
					</div>
					
					<div class="marginleft floatleft clearboth bigmargintop">
					
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'addFlowID'"/>
							<xsl:with-param name="value" select="$flowID" />
							<xsl:with-param name="requestparameters" select="$requestparameters"/>
						</xsl:call-template>
						
						<label>
							<xsl:value-of select="$i18n.UseForAddFlow" />
						</label>
						
					</div>
				
				</div>
				
				<div class="marginleft floatleft" style="width: 270px">
			
					<label for="excludedStatusIDs-{$flowID}" class="floatleft full">
						<xsl:value-of select="$i18n.excludedStatuses" />
					</label>
					
					<xsl:call-template name="createMultipleDropdown">
						<xsl:with-param name="id">
							<xsl:value-of select="'excludedStatusIDs-'"/>
							<xsl:value-of select="$flowID"/>
						</xsl:with-param>
						<xsl:with-param name="name">
							<xsl:value-of select="'excludedStatusIDs-'"/>
							<xsl:value-of select="$flowID"/>
						</xsl:with-param>
						<xsl:with-param name="valueElementName" select="'statusID'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="../../../Flows/Flow[flowID = $flowID]/Statuses/Status" />
						<xsl:with-param name="requestparameters" select="$requestparameters"/>
					</xsl:call-template>
					
				</div>
			</div>
			
		</div>
				
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'TableColumnValidationError']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.TableColumnValidationError" />
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequestedRoundNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.RequestedRoundNotFound" />
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RoundDoesNotSupportAdd']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.RoundDoesNotSupportAdd" />
		</p>
	
	</xsl:template>
	
	<xsl:template name="validationErrorFlowInstanceAlreadyRaffled">
	
		<xsl:value-of select="$i18n.ValidationError.FlowInstanceAlreadyRaffled.1" />
		<xsl:text> </xsl:text>
		<xsl:value-of select="FlowInstance/flowInstanceID" />
		<xsl:text> </xsl:text>
		<xsl:value-of select="$i18n.ValidationError.FlowInstanceAlreadyRaffled.2" />
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FlowInstanceAlreadyRaffled']">
	
		<p class="error">
			<xsl:call-template name="validationErrorFlowInstanceAlreadyRaffled"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FlowInstanceAlreadyRaffled']" mode="setting">
		
		<i data-icon-after="!">
			<xsl:attribute name="title">
				<xsl:call-template name="validationErrorFlowInstanceAlreadyRaffled"/>
			</xsl:attribute>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RaffleFlowNotFound']">
	
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.RaffleFlowNotFound" />
			<xsl:value-of select="'&#160;'" />
			<xsl:value-of select="raffleFlowID" />
			<xsl:value-of select="'&#160;'" />
			<xsl:value-of select="$i18n.ValidationError.RaffleFlowNotFound2" />
		</p>
	
	</xsl:template>
	
	<xsl:template name="validationErrorFlowInstanceStatusExcluded">
	
		<xsl:value-of select="$i18n.ValidationError.FlowInstanceStatusExcluded" />
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FlowInstanceStatusExcluded']">
	
		<p class="error">
			<xsl:call-template name="validationErrorFlowInstanceStatusExcluded"/>
		</p>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'FlowInstanceStatusExcluded']" mode="setting">
		
		<i data-icon-after="!">
			<xsl:attribute name="title">
				<xsl:call-template name="validationErrorFlowInstanceStatusExcluded"/>
			</xsl:attribute>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError">
	
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.ValidationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.ValidationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.ValidationError.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.ValidationError.TooLong" />
					</xsl:when>							
					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="displayName">
						<xsl:value-of select="displayName"/>
					</xsl:when>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.Name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'money'">
						<xsl:value-of select="$i18n.money"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startDate'">
						<xsl:value-of select="$i18n.startDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'endDate'">
						<xsl:value-of select="$i18n.endDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'decisionEmailMessage'">
						<xsl:value-of select="$i18n.DecisionEmailMessage"/>
					</xsl:when>
					<xsl:when test="fieldName = 'decisionSMSMessage'">
						<xsl:value-of select="$i18n.DecisionSMSMessage"/>
					</xsl:when>
					<xsl:when test="starts-with(fieldName, 'setting-')">
						<xsl:variable name="attributeNameWithSuffix" select="substring(fieldName, 9)"/>
						
						<xsl:variable name="attributeName">
							<xsl:call-template name="substring-before-last">
					      <xsl:with-param name="string1" select="$attributeNameWithSuffix" />
					      <xsl:with-param name="string2" select="'-'" />
					    </xsl:call-template>
						</xsl:variable>
						
						<xsl:value-of select="../../Columns/Column[attribute = $attributeName]/Title"/>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>!</xsl:text>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.ValidationError.UnknownFault" />
			</p>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="substring-before-last">
	  <xsl:param name="string1" select="''" />
	  <xsl:param name="string2" select="''" />
		
	  <xsl:if test="$string1 != '' and $string2 != ''">
	    <xsl:variable name="head" select="substring-before($string1, $string2)" />
	    <xsl:variable name="tail" select="substring-after($string1, $string2)" />
	    <xsl:value-of select="$head" />
	    <xsl:if test="contains($tail, $string2)">
	      <xsl:value-of select="$string2" />
	      <xsl:call-template name="substring-before-last">
	        <xsl:with-param name="string1" select="$tail" />
	        <xsl:with-param name="string2" select="$string2" />
	      </xsl:call-template>
	    </xsl:if>
	  </xsl:if>
	</xsl:template>
	
	<xsl:template name="addUserTagsTable">
	
		<div class="floatleft margintop full">

			<p><xsl:value-of select="$i18n.UserTagsTable.description"/></p>
		
			<table class="full border">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Tag"/>
					</th>
					<th>
						<xsl:value-of select="$i18n.Description"/>
					</th>
				</tr>
				<tr>
					<td>
						<xsl:text>$flow.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowNameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.flowInstanceID</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceIDTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$flowInstance.url</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.FlowInstanceURLTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$status.name</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.StatusTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.firstname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterFirstnameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$contact.lastname</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.PosterLastnameTag"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:text>$number</xsl:text>
					</td>
					<td>
						<xsl:value-of select="$i18n.NumberTag"/>
					</td>
				</tr>
			</table>
		
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>