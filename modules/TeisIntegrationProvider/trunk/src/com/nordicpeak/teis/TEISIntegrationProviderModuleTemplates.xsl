<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-ui.js
		/jquery/jquery-migrate.js
	</xsl:variable>

<!-- 	<xsl:variable name="scripts">
		/js/targetgroupadmin.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/targetgroupadmin.css
	</xsl:variable> -->

	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="ShowView">
				<xsl:apply-templates select="ShowView" />
			</xsl:when>
			<xsl:otherwise>
				<div id="TEISIntegrationProviderModule" class="contentitem errands-wrapper border-box">
					
					<xsl:apply-templates select="ListTargetGroups" />
					<xsl:apply-templates select="AddTargetGroup" />
					<xsl:apply-templates select="UpdateTargetGroup" />
					<xsl:apply-templates select="UpdateFlowTargetGroups" />
					
				</div>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

	<xsl:template match="ListTargetGroups">
	
		<h1><xsl:value-of select="$i18n.ListTargetGroupsTitle" /></h1>
		
		<xsl:apply-templates select="validationError"/>

		<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
			<thead>	
				<tr>
					<th><xsl:value-of select="$i18n.Name" /></th>
					<th><xsl:value-of select="$i18n.Flow" /></th>
					<th width="32" />
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="not(TargetGroups/TargetGroup)">
						<tr>
							<td></td>
							<td colspan="4">
								<xsl:value-of select="$i18n.NoTargetGroupsFound" />
							</td>
						</tr>					
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:apply-templates select="TargetGroups/TargetGroup" mode="list"/>
						
					</xsl:otherwise>
				</xsl:choose>			
			</tbody>
		</table>		
		
		<br/>
		<div class="floatright marginright">
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addtargetgroup" title="{$i18n.AddTargetGroup}">
				<xsl:value-of select="$i18n.AddTargetGroup"/>
				<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" alt="" />
			</a>			
		</div>
	
	</xsl:template>
	
	<xsl:template match="TargetGroup" mode="list">
		
		<tr>
			<td data-title="{$i18n.Name}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatetargetgroup/{targetGroupID}" title="{$i18n.UpdateTargetGroup}: {name}"><xsl:value-of select="name"/></a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatetargetgroup/{targetGroupID}" title="{$i18n.UpdateTargetGroup}: {name}"><xsl:value-of select="count(flowFamilyIDs/flowFamilyID)" /></a>
			</td>
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatetargetgroup/{targetGroupID}" title="{$i18n.UpdateTargetGroup}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
				</a>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletetargetgroup/{targetGroupID}" onclick="return confirm('{$i18n.DeleteTargetGroupConfirm}: {name}?');" title="{$i18n.DeleteTargetGroup}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
				</a>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="AddTargetGroup">
	
		<h1><xsl:value-of select="$i18n.AddTargetGroup" /></h1>
	
		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
				
			<xsl:call-template name="targetGroupForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.AddTargetGroup}" />
			</div>
	
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateTargetGroup">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateTargetGroup"/>
			<xsl:text>:&#x20;</xsl:text>
			<xsl:value-of select="TargetGroup/name"/>
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="targetGroupForm"/>
			
			<div class="floatright">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
	
	</xsl:template>

	<xsl:template name="targetGroupForm">
	
		<script type="text/javascript">
			tragetGroupAdminModuleAlias = '<xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />';
			flowAdminModuleAlias = '<xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="FlowAdminModuleAlias" />';
		</script>
	
		<xsl:variable name="targetGroupID" select="TargetGroup/targetGroupID" />
	
		<div class="floatleft full bigmarginbottom">
			
			<label for="name" class="floatleft full">
				<xsl:value-of select="$i18n.Name" />
			</label>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="element" select="TargetGroup" />          
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			
			<label class="floatleft full">
				<xsl:value-of select="$i18n.Flows" />
			</label>
			
			<div class="floatleft full">
				
				<ul class="margintop flow-list" id="flow-list">
		
					<xsl:choose>
						<xsl:when test="requestparameters">
							<xsl:apply-templates select="requestparameters/parameter[ name = 'flowFamilyID']/value" mode="flow">
								<xsl:with-param name="requestparameters" select="requestparameters" />
								<xsl:with-param name="flowAdminModuleAlias" select="FlowAdminModuleAlias" />
							</xsl:apply-templates>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="Flow" mode="list" />
						</xsl:otherwise>
					</xsl:choose>
		
					<li id="flow-template" class="hidden">
						
						<input type="hidden" name="flowFamilyID" disabled="disabled"/>
						<input type="hidden" name="flowID-" disabled="disabled"/>
						<input type="hidden" name="flowName-" disabled="disabled" />
		
						<span class="text"/>
		
						<div class="floatright">
							<a class="delete" href="#" title="{$i18n.RemoveFlow}: ">
								<img class="vertical-align-middle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="{$i18n.RemoveFlow}" />
							</a>
						</div>
					</li>
					
				</ul>
			
				<div class="ui-widget">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'flow-search'" />
						<xsl:with-param name="class" select="'full border-box'" />
						<xsl:with-param name="width" select="'100%'" />
						<xsl:with-param name="placeholder" select="$i18n.SearchFlowPlaceholder" />
					</xsl:call-template>
				</div>
			
			</div>
		</div>		
		
	</xsl:template>	

	<xsl:template match="Flow" mode="list">
		
		<li>
						
			<input type="hidden" name="flowFamilyID" value="{FlowFamily/flowFamilyID}" />
			<input type="hidden" name="flowID-{FlowFamily/flowFamilyID}" value="{flowID}" />
			<input type="hidden" name="flowName-{FlowFamily/flowFamilyID}" value="{name}" />

			<img src="{/Document/requestinfo/contextpath}{../FlowAdminModuleAlias}/icon/{flowID}" width="26" />

			<span class="text"><xsl:value-of select="name" /></span>

			<div class="floatright">
				<a class="delete" href="#" title="{$i18n.RemoveFlow}: ">
					<img class="vertical-align-middle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="{$i18n.RemoveFlow}" />
				</a>
			</div>
		</li>
		
	</xsl:template>

	<xsl:template match="value" mode="flow">
	
		<xsl:param name="flowAdminModuleAlias" />
		<xsl:param name="requestparameters" />
	
		<xsl:variable name="flowFamilyID" select="."/>
	
		<xsl:variable name="name" select="$requestparameters/parameter[name=concat('flowName-', $flowFamilyID)]/value" />
		<xsl:variable name="flowID" select="$requestparameters/parameter[name=concat('flowID-', $flowFamilyID)]/value" />
		
		<xsl:if test="$flowID != ''">
	
			<li>
				
				<input type="hidden" name="flowFamilyID" value="{$flowFamilyID}"/>
				<input type="hidden" name="flowID-{$flowFamilyID}" value="{$flowID}" />
				<input type="hidden" name="flowName-{$flowFamilyID}" value="{$name}"/>
				
				<img src="{/Document/requestinfo/contextpath}{$flowAdminModuleAlias}/icon/{$flowID}" width="26" />
				
				<span class="text"><xsl:value-of select="$name" /></span>
				
				<div class="floatright">
					<a class="delete" href="#" title="{$i18n.RemoveFlow}: ">
						<img class="vertical-align-middle" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="{$i18n.RemoveFlow}" />
					</a>
				</div>
				
			</li>
		
		</xsl:if>
		
	</xsl:template>
	
	
	<xsl:template match="ShowView">
		
		<xsl:variable name="flowFamilyID" select="Flow/FlowFamily/flowFamilyID" />
		<xsl:text>&#x20;</xsl:text>
		<xsl:variable name="targetGroups" select="TargetGroup[flowFamilyIDs[flowFamilyID = $flowFamilyID]]" />
		
		<a name="teis-integration" />
		
		<xsl:choose>
			<xsl:when test="$targetGroups">
				<xsl:apply-templates select="TargetGroup[flowFamilyIDs[flowFamilyID = $flowFamilyID]]" mode="show" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.NoFlowTargetGroups" />
			</xsl:otherwise>
		</xsl:choose>
		
		<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updateflowtargetgroups/{Flow/flowID}" class="floatright">
			<xsl:value-of select="$i18n.ChooseTargetGroups" />
			<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png" alt="" />
		</a>
		
	</xsl:template>
	
	<xsl:template match="UpdateFlowTargetGroups">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateTargetGroupsTitle"/>
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="Flow/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form method="post" action="{/Document/requestinfo/uri}">
			
			<div class="floatleft full bigmarginbottom">
			
				<label class="floatleft full marginbottom">
					<xsl:value-of select="$i18n.TargetGroups" />
				</label>
				
				<div class="floatleft full">
					<xsl:apply-templates select="TargetGroup" mode="form">
						<xsl:with-param name="flowFamilyID" select="Flow/FlowFamily/flowFamilyID" />
					</xsl:apply-templates>
				</div>
				
			</div>
		
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.Save}" />
			</div>
		
		</form>
	
	</xsl:template>

	<xsl:template match="TargetGroup" mode="show">
		
		<xsl:value-of select="name" />
		
		<xsl:if test="position() != last()">
			<xsl:text>,&#x20;</xsl:text>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="TargetGroup" mode="form">
		
		<xsl:param name="flowFamilyID" />
		
		<div class="floatleft padding border-box full border marginbottom">
			
			<div class="floatleft">
				
				<!-- <img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/form.png" alt="" /> -->
				
				<xsl:value-of select="name" />
						
			</div>
			
			<div class="floatright marginright">
				
				<xsl:variable name="targetGroupID" select="targetGroupID"/>
			
				<input type="checkbox" name="targetGroup" value="{$targetGroupID}">
					
					<xsl:choose>
						<xsl:when test="../../requestparameters">
							<xsl:if test="../../requestparameters/parameter[name='targetGroup'][value=$targetGroupID]">
								<xsl:attribute name="checked"/>
							</xsl:if>
						</xsl:when>						
						<xsl:when test="flowFamilyIDs[flowFamilyID = $flowFamilyID]">
							<xsl:attribute name="checked"/>
						</xsl:when>						
					</xsl:choose>
					
				</input>
				
			</div>
						
		</div>
		
	</xsl:template>

	<xsl:template match="validationError">
	
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validation.requiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.validation.tooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.validation.tooLong" />
					</xsl:when>														
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validation.unknownError" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.validation.unknownFault" />
			</p>
		</xsl:if>
		
	</xsl:template>

</xsl:stylesheet>