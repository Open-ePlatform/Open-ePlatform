<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/timepicker/js/jquery.timepicker.js
	</xsl:variable>

	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
		/timepicker/css/jquery.timepicker.css
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.ui.datepicker-sv.js
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/operatingmessagemodule.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
	</xsl:variable>

	<xsl:template match="Document">	
			
			<div id="OperatingMessageModule" class="contentitem errands-wrapper">
			
				<xsl:apply-templates select="ListOperatingMessages" />
				<xsl:apply-templates select="AddOperatingMessage" />
				<xsl:apply-templates select="UpdateOperatingMessage" />
			
			</div>
			
	</xsl:template>
	
	<xsl:template match="ListOperatingMessages">
	
		<h1><xsl:value-of select="/Document/module/name" /></h1>
	
		<table id="messageList" class="full coloredtable oep-table" cellspacing="0">
			<thead>	
				<tr>
					<th width="16" class="no-sort"></th>
					<th><span data-icon-after="_"><xsl:value-of select="$i18n.Message" /></span></th>
					<th width="120" class="default-sort"><span data-icon-after="_"><xsl:value-of select="$i18n.Publish" /></span></th>
					<th width="120"><span data-icon-after="_"><xsl:value-of select="$i18n.UnPublish" /></span></th>
					<th width="110"><span data-icon-after="_"><xsl:value-of select="$i18n.FlowFamilies" /></span></th>
					<th width="130"><span data-icon-after="_"><xsl:value-of select="$i18n.DisableFlows" /></span></th>
					<th width="32" class="no-sort" />
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="not(OperatingMessages/OperatingMessage)">
						<tr>
							<td class="icon"></td>
							<td colspan="8">
								<xsl:value-of select="$i18n.NoOperatingMessagesFound" />
							</td>
						</tr>					
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="OperatingMessages/OperatingMessage" mode="list" />
						
					</xsl:otherwise>
				</xsl:choose>			
			</tbody>
		</table>
		
		<div class="floatright">
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add" title="{$i18n.AddOperatingMessage}">
				<xsl:value-of select="$i18n.AddOperatingMessage"/>
				<img class="vertical-align-middle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
			</a>			
		</div>
	
	</xsl:template>

	<xsl:template match="OperatingMessage" mode="list">
		
		<tr>
			
			<td class="icon">
				<i data-icon-after="!"></i>
			</td>
			<td data-title="{$i18n.Message}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}"><xsl:value-of select="message" /></a>
			</td>
			<td data-title="{$i18n.Publish}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}"><xsl:value-of select="startDate" /><xsl:text>&#160;</xsl:text><xsl:value-of select="startTime" /></a>
			</td>
			<td data-title="{$i18n.UnPublish}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}"><xsl:value-of select="endDate" /><xsl:text>&#160;</xsl:text><xsl:value-of select="endTime" /></a>
			</td>
			<td data-title="{$i18n.FlowFamilies}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}">
					<xsl:choose>
						<xsl:when test="flowFamilyIDs/flowFamilyID">
							<xsl:value-of select="count(flowFamilyIDs/flowFamilyID)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.All" />
						</xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:if test="flowFamilyIDs/flowFamilyID">
					<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/info.png" class="marginleft vertical-align-middle pointer">
						<xsl:attribute name="title">
							<xsl:for-each select="flowFamilyIDs/flowFamilyID">
								<xsl:variable name="flowFamilyID" select="." />
								<xsl:value-of select="../../../../FlowFamily[flowFamilyID = $flowFamilyID]/name" />
								<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
							</xsl:for-each>
						</xsl:attribute>
					</img>
				</xsl:if>
			</td>			
			
			<td data-title="{$i18n.DisableFlows}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}">
					<xsl:choose>
						<xsl:when test="disableFlows = 'true'"><xsl:value-of select="$i18n.Yes" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$i18n.No" /></xsl:otherwise>
					</xsl:choose>
				</a>
			</td>										
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{messageID}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{messageID}" onclick="return confirm('{$i18n.DeleteOperatingMessageConfirm}: {name}?');" title="{$i18n.DeleteOperatingMessageTitle}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
				</a>
			</td>
		</tr>
		
	</xsl:template>

	<xsl:template match="AddOperatingMessage">
	
		<form id="addOperatingMessageForm" method="post" action="{/Document/requestinfo/uri}">
	
			<h1><xsl:value-of select="$i18n.AddOperatingMessage" /></h1>
	
			<xsl:apply-templates select="validationException/validationError" />
	
			<xsl:call-template name="operatingMessageForm" />
		
			<div class="floatright">
				<input type="submit" value="{$i18n.Submit}"/>
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateOperatingMessage">
		
		<form id="updateOperatingMessageForm" method="post" action="{/Document/requestinfo/uri}">
		
			<h1><xsl:value-of select="$i18n.UpdateOperatingMessage" /></h1>
			
			<xsl:apply-templates select="validationException/validationError" />
			
			<xsl:call-template name="operatingMessageForm">
				<xsl:with-param name="operatingMessage" select="OperatingMessage" />
			</xsl:call-template>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.SaveChanges}"/>
			</div>
		
		</form>
		
	</xsl:template>
	
	<xsl:template name="operatingMessageForm">
		
		<xsl:param name="operatingMessage" select="null" />
		
		<script type="text/javascript">
		
			$(function() {						
				$( "#startDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.StartDate"/>',
					changeMonth: true,
            		changeYear: true,
           			showWeek: true,
				});
				
				$( "#endDate" ).datepicker({
					showOn: "button",
					buttonImage: '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics/calendar_grid.png',
					buttonImageOnly: true,
					buttonText: '<xsl:value-of select="$i18n.EndDate"/>',
					changeMonth: true,
            		changeYear: true,
           			showWeek: true,
				});								    							  									
			});	
			
		</script>
		
		<div class="floatleft full bigmarginbottom">
			
			<label for="message" class="floatleft full">
				<xsl:value-of select="$i18n.Message" />
			</label>
			
			<div class="floatleft full">

				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="'message'"/>
					<xsl:with-param name="class" select="'full'"/>
					<xsl:with-param name="element" select="$operatingMessage" />		
				</xsl:call-template>									
										
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<div class="floatleft marginright">

				<label for="startDate" class="floatleft clearboth">
					<xsl:value-of select="$i18n.StartDate" />
				</label>

				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startDate'"/>
						<xsl:with-param name="name" select="'startDate'"/>
						<xsl:with-param name="element" select="$operatingMessage" />
						<xsl:with-param name="size" select="'15'" />
					</xsl:call-template>									
				</div>						
			
			</div>
						
			<div class="floatleft bigmarginright">

				<label for="startTime" class="floatleft clearboth">
					<xsl:value-of select="$i18n.StartTime" />
				</label>

				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startTime'"/>
						<xsl:with-param name="name" select="'startTime'"/>
						<xsl:with-param name="size" select="'10'"/>
						<xsl:with-param name="element" select="$operatingMessage" />
					</xsl:call-template>									
				</div>
								
			</div>
			
			<div class="floatleft bigmarginleft marginright">

				<label for="endDate" class="floatleft clearboth">
					<xsl:value-of select="$i18n.EndDate" />
				</label>

				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endDate'"/>
						<xsl:with-param name="name" select="'endDate'"/>
						<xsl:with-param name="size" select="'15'" />
						<xsl:with-param name="element" select="$operatingMessage" />
					</xsl:call-template>
				</div>									
										
			</div>
			
			<div class="floatleft">

				<label for="endTime" class="floatleft clearboth">
					<xsl:value-of select="$i18n.EndTime" />
				</label>
				
				<div class="floatleft clearboth">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endTime'"/>
						<xsl:with-param name="name" select="'endTime'"/>
						<xsl:with-param name="size" select="'10'"/>
						<xsl:with-param name="element" select="$operatingMessage" />
					</xsl:call-template>		
				</div>							
										
			</div>
			
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<div class="floatleft">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'disableFlows'" />
					<xsl:with-param name="name" select="'disableFlows'" />
					<xsl:with-param name="element" select="$operatingMessage" />
					<xsl:with-param name="value" select="'true'" />
				</xsl:call-template>									
						
				<label for="disableFlows">
					<xsl:value-of select="$i18n.DisableFlowsLabel" />
				</label>
										
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="allowManagingOfInstancesWrapper">
			
			<div class="floatleft">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowManagingOfInstances'" />
					<xsl:with-param name="name" select="'allowManagingOfInstances'" />
					<xsl:with-param name="element" select="$operatingMessage" />
					<xsl:with-param name="value" select="'true'" />
				</xsl:call-template>									
						
				<label for="allowManagingOfInstances">
					<xsl:value-of select="$i18n.AllowManagingOfInstancesLabel" />
				</label>
										
			</div>
		</div>		
		
		<div class="floatleft full">
				
			<div class="floatleft">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'global1'" />
					<xsl:with-param name="name" select="'global'" />
					<xsl:with-param name="element" select="$operatingMessage" />
					<xsl:with-param name="value" select="'true'" />
				</xsl:call-template>
				
				<label for="global1">
					<xsl:value-of select="$i18n.Global" />
				</label>
			</div>
			
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<div class="floatleft full bigmarginbottom">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'global2'" />
					<xsl:with-param name="name" select="'global'" />
					<xsl:with-param name="element" select="$operatingMessage" />
					<xsl:with-param name="value" select="'false'" />
				</xsl:call-template>
				
				<label for="global2">
					<xsl:value-of select="$i18n.ChooseFlowFamilies" />
				</label>
			</div>
			
			<div class="floatleft full">

				<div id="chooseFlowFamilies" class="scrolllist hidden">
					<xsl:apply-templates select="FlowFamily">
						<xsl:with-param name="operatingMessage" select="$operatingMessage" />
					</xsl:apply-templates>
				</div>								
										
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="FlowFamily">
		
		<xsl:param name="operatingMessage" />
		
		<div class="floatleft full border marginbottom padding border-box flowfamily" data-name="{name}">
			<div class="floatleft title">
				<xsl:value-of select="name"/>
			</div>
			<div class="floatright">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="concat('flowFamilyID_', flowFamilyID)" />
					<xsl:with-param name="name" select="'flowFamilyID'" />
					<xsl:with-param name="element" select="$operatingMessage/flowFamilyIDs" />
					<xsl:with-param name="value" select="flowFamilyID" />
					<xsl:with-param name="requestparameters" select="../requestparameters" />
				</xsl:call-template>
			</div>				
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.Validation.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.Validation.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.Validation.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.Validation.TooLong.Part1" />
					</xsl:when>														
					<xsl:otherwise>
						<xsl:value-of select="$i18n.Validation.UnknownError" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="displayName">
						<xsl:value-of select="displayName"/>
					</xsl:when>
					<xsl:when test="fieldName = 'message'">
						<xsl:value-of select="$i18n.message"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startDate'">
						<xsl:value-of select="$i18n.startDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startTime'">
						<xsl:value-of select="$i18n.startTime"/>
					</xsl:when>
					<xsl:when test="fieldName = 'endDate'">
						<xsl:value-of select="$i18n.endDate"/>
					</xsl:when>
					<xsl:when test="fieldName = 'endTime'">
						<xsl:value-of select="$i18n.endTime"/>
					</xsl:when>
					<xsl:when test="fieldName = 'disableFlows'">
						<xsl:value-of select="$i18n.disabledFlows"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:if test="validationErrorType='TooLong'">
					<xsl:value-of select="$i18n.Validation.TooLong.Part2" />
					<xsl:value-of select="currentLength"/>
					<xsl:value-of select="$i18n.Validation.TooLong.Part3"/>
					<xsl:value-of select="maxLength"/>
					<xsl:value-of select="$i18n.Validation.TooLong.Part4"/>
				</xsl:if>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='EndTimeBeforeStartTime'">
						<xsl:value-of select="$i18n.Validation.EndTimeBeforeStartTime"/>
					</xsl:when>
					<xsl:when test="messageKey='DaysBetweenToSmall'">
						<xsl:value-of select="$i18n.Validation.DaysBetweenToSmall"/>
					</xsl:when>
					<xsl:when test="messageKey='NoFlowFamilyChoosen'">
						<xsl:value-of select="$i18n.Validation.NoFlowFamilyChoosen"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.UnknownFault"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>				
</xsl:stylesheet>