<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/childqueryadmin.js?v=1
	</xsl:variable>

	<xsl:template match="Document">

		<div id="ChildQueryProvider" class="contentitem">

			<xsl:apply-templates select="validationError" />
			<xsl:apply-templates select="UpdateChildQuery" />
			<xsl:apply-templates select="TestChildren" />

		</div>

	</xsl:template>

	<xsl:template match="UpdateChildQuery">

		<h1>
			<xsl:value-of select="$i18n.UpdateQuery" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="ChildQuery/QueryDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="updateChildQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="ChildQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'showAddress'" />
					<xsl:with-param name="name" select="'showAddress'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="showAddress">
					<xsl:value-of select="$i18n.ShowAddress" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label>
					<xsl:value-of select="$i18n.MinChildAge" />
				</label>
		
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'minAge'" />
					<xsl:with-param name="name" select="'minAge'" />
					<xsl:with-param name="title" select="$i18n.MinChildAge"/>
					<xsl:with-param name="element" select="ChildQuery"/>
				</xsl:call-template>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				
				<label>
					<xsl:value-of select="$i18n.MaxChildAge" />
				</label>
		
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'maxAge'" />
					<xsl:with-param name="name" select="'maxAge'" />
					<xsl:with-param name="title" select="$i18n.MaxChildAge"/>
					<xsl:with-param name="element" select="ChildQuery"/>
				</xsl:call-template>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'useMultipartSigning'" />
					<xsl:with-param name="name" select="'useMultipartSigning'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="useMultipartSigning">
					<xsl:value-of select="$i18n.UseMultipartSigning" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'alwaysShowOtherGuardians'" />
					<xsl:with-param name="name" select="'alwaysShowOtherGuardians'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="alwaysShowOtherGuardians">
					<xsl:value-of select="$i18n.AlwaysShowOtherGuardians" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'hideSSNForOtherGuardians'" />
					<xsl:with-param name="name" select="'hideSSNForOtherGuardians'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="hideSSNForOtherGuardians">
					<xsl:value-of select="$i18n.HideSSNForOtherGuardians" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
			
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'setMultipartsAsOwners'" />
					<xsl:with-param name="name" select="'setMultipartsAsOwners'" />
					<xsl:with-param name="element" select="ChildQuery" /> 
				</xsl:call-template>
					
				<label for="setMultipartsAsOwners">
					<xsl:value-of select="$i18n.SetMultipartsAsOwners" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireGuardianContactInfoVerification'" />
					<xsl:with-param name="name" select="'requireGuardianContactInfoVerification'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="requireGuardianContactInfoVerification">
					<xsl:value-of select="$i18n.RequiredContactWayVerification" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
			
				<label for="contactWays" class="full marginbottom">
					<xsl:value-of select="$i18n.RequiredContactWays" />
				</label>
			
				<xsl:variable name="options">
					<option>
						<name><xsl:value-of select="$i18n.ContactWays.Either"/></name>
						<value>either</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.ContactWays.Email"/></name>
						<value>email</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.ContactWays.Phone"/></name>
						<value>phone</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.ContactWays.Both"/></name>
						<value>both</value>
					</option>
				</xsl:variable>
				
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="id" select="'contactWays'" />
					<xsl:with-param name="name" select="'contactWays'" />
					<xsl:with-param name="valueElementName" select="'value'" />
					<xsl:with-param name="labelElementName" select="'name'" />
					<xsl:with-param name="element" select="exsl:node-set($options)/option" />
					<xsl:with-param name="selectedValue">
						<xsl:choose>
							<xsl:when test="ChildQuery/requireGuardianEmail = 'true' and ChildQuery/requireGuardianPhone = 'true'">both</xsl:when>
							<xsl:when test="ChildQuery/requireGuardianEmail = 'true'">email</xsl:when>
							<xsl:when test="ChildQuery/requireGuardianPhone = 'true'">phone</xsl:when>
							<xsl:otherwise>either</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'showGuardianAddress'" />
					<xsl:with-param name="name" select="'showGuardianAddress'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ChildQuery" />
				</xsl:call-template>
				
				<label for="showGuardianAddress">
					<xsl:value-of select="$i18n.ShowGuardianAddress" />
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="otherGuardiansDescription" class="floatleft clearboth">
					<xsl:value-of select="$i18n.OtherGuardiansDescription" />
				</label>
				<div class="floatleft full">
					<xsl:choose>
						<xsl:when test="/Document/useCKEditorForDescription = 'true'">
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="id" select="'otherGuardiansDescription'" />
								<xsl:with-param name="name" select="'otherGuardiansDescription'" />
								<xsl:with-param name="title" select="$i18n.OtherGuardiansDescription"/>
								<xsl:with-param name="class" select="'common-ckeditor'" />
								<xsl:with-param name="element" select="ChildQuery" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'otherGuardiansDescription'"/>
								<xsl:with-param name="name" select="'otherGuardiansDescription'"/>
								<xsl:with-param name="title" select="$i18n.OtherGuardiansDescription"/>
								<xsl:with-param name="element" select="ChildQuery" />
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					
				</div>
			</div>
			
			<xsl:if test="FilterApiEndpoints">
			
				<div class="floatleft hugemarginright">
					<div>
						<label for="filterEndpointID" class="floatleft clearboth">
							<xsl:value-of select="$i18n.FilterEndpoint.title" />
						</label>
					</div>
					<div>
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'filterEndpointID'" />
							<xsl:with-param name="name" select="'filterEndpointID'" />
							<xsl:with-param name="title" select="$i18n.FilterEndpoint.title"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'endpointID'" />
							<xsl:with-param name="element" select="FilterApiEndpoints/ChildQueryFilterEndpoint" />
							<xsl:with-param name="class" select="'marginright'" />
							<xsl:with-param name="addEmptyOption" select="$i18n.FilterEndpoint.EmptyOption" />
							<xsl:with-param name="selectedValue" select="ChildQuery/ChildQueryFilterEndpoint/endpointID" />
						</xsl:call-template>
					</div>
				</div>
				
				<label id="endpoint-attributes" class="floatleft full">
					<xsl:value-of select="$i18n.FilterEndpoint.Attributes" />
				</label>
				
				<xsl:variable name="attributeOptions">
					<option>
						<name><xsl:value-of select="$i18n.FilterEndpoint.Attribute.DisplayMode.Always"/></name>
						<value>ALWAYS</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.FilterEndpoint.Attribute.DisplayMode.IfValue"/></name>
						<value>IF_VALUE</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.FilterEndpoint.Attribute.DisplayMode.Never"/></name>
						<value>NEVER</value>
					</option>
				</xsl:variable>
				
				<xsl:for-each select="FilterApiEndpoints/ChildQueryFilterEndpoint">
					<div id="endpoint-{endpointID}" class="filterEndpoint floatleft full" style="display: none;">
						
						<xsl:variable name="endpointID" select="endpointID" />
						
						<table>
							
							<xsl:for-each select="Fields/value">
								
								<tr>
									<xsl:variable name="name" select="concat('attribute-', $endpointID, '-', .)" />
									
									<td>
										<label for="{$name}" class="nomargin bigpaddingright">
											<xsl:value-of select="." />
										</label>
									</td>
									
									<td>
										<xsl:call-template name="createDropdown">
											<xsl:with-param name="id" select="$name" />
											<xsl:with-param name="name" select="$name" />
											<xsl:with-param name="title" select="$i18n.FilterEndpoint.Attribute.DisplayMode"/>
											<xsl:with-param name="labelElementName" select="'name'" />
											<xsl:with-param name="valueElementName" select="'value'" />
											<xsl:with-param name="element" select="exsl:node-set($attributeOptions)/option" />
											<xsl:with-param name="selectedValue" select="../../../../ChildQuery/SelectedChildAttributes/SelectedAttribute[name = current()]/displayMode" />
											<xsl:with-param name="requestparameters" select="../../../../requestparameters" />
										</xsl:call-template>
									</td>
									
								</tr>
								
							</xsl:for-each>
							
						</table>
						
					</div>
				</xsl:for-each>
				
				<div id="emptyFilterDescriptionContainer" class="floatleft full bigmarginbottom">
					<label for="emptyFilterDescription" class="floatleft clearboth"><xsl:value-of select="$i18n.EmptyFilterDescription" /></label>
					<div class="floatleft full">
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'emptyFilterDescription'" />
							<xsl:with-param name="name" select="'emptyFilterDescription'" />
							<xsl:with-param name="element" select="ChildQuery" />
							<xsl:with-param name="rows">4</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
				
			</xsl:if>
			
			<div class="floatleft full bigmargintop">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="ChildQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="setAsAttribute">
						<xsl:value-of select="$i18n.Query.setAsAttribute" />
					</label>
				</div>
			</div>
			
			<div id="attributeContainer">
			
				<div class="floatleft full bigmarginbottom">
					<label for="attributeName" class="floatleft clearboth"><xsl:value-of select="$i18n.Query.attributeName" /></label>
					
					<div class="floatleft full">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'attributeName'"/>
							<xsl:with-param name="name" select="'attributeName'"/>
							<xsl:with-param name="title" select="$i18n.Query.attributeName"/>
							<xsl:with-param name="element" select="ChildQuery" />
							<xsl:with-param name="maxlength" select="'255'"/>
						</xsl:call-template>
					</div>
				</div>
	
				<p class="floatleft clearboth"><xsl:value-of select="$i18n.attributeDescription" /></p>
				
				<table class="floatleft clearboth border">
					<thead>
						<th><xsl:value-of select="$i18n.attributes.name" /></th>
						<th><xsl:value-of select="$i18n.attributes.value" /></th>
					</thead>
					<tbody>
						<tr>
							<td>prefix.childFirstname</td>
							<td><xsl:value-of select="$java.exportChildCitizenFirstName" /></td>
						</tr>
						<tr>
							<td>prefix.childLastname</td>
							<td><xsl:value-of select="$java.exportChildCitizenLastName" /></td>
						</tr>
						<tr>
							<td>prefix.childCitizenIdentifier</td>
							<td><xsl:value-of select="$java.exportChildCitizenIdentifier" /></td>
						</tr>
					</tbody>
				</table>
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>
	
	<xsl:template match="TestChildren">
	
		<h1>
			<xsl:value-of select="$i18n.TestChildren.title" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form name="testchildren" method="post" action="{/Document/requestinfo/uri}">

			<p class="floatleft full">
				<xsl:value-of select="$i18n.TestChildren.description.part1" />
			</p>
			
			<p class="floatleft full">
				<xsl:value-of select="$i18n.TestChildren.description.part2" />
			</p>

			<div class="floatleft full bigmarginbottom">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'enable'" />
					<xsl:with-param name="name" select="'enable'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="checked" select="Enabled = 'true'" />
				</xsl:call-template>
				
				<label for="enable">
					<xsl:value-of select="$i18n.TestChildren.enabled" />
				</label>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>
	
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedChildQueryNotFound']">

		<p class="error">
			<xsl:value-of select="$i18n.ChildQueryNotFound" />
		</p>

	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'MinAgeLargerThanMaxAge']">

		<p class="error">
			<xsl:value-of select="$i18n.MinAgeLargerThanMaxAge" />
		</p>

	</xsl:template>
	
	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />

		<xsl:choose>
			<xsl:when test="$fieldName = 'minAge'">
				<xsl:value-of select="$i18n.MinChildAge" />
			</xsl:when>
			<xsl:when test="$fieldName = 'maxAge'">
				<xsl:value-of select="$i18n.MaxChildAge" />
			</xsl:when>
			<xsl:when test="$fieldName = 'filterEndpointID'">
				<xsl:value-of select="$i18n.FilterEndpoint.title" />
			</xsl:when>
			<xsl:when test="$fieldName = 'emptyFilterDescription'">
				<xsl:value-of select="$i18n.EmptyFilterDescription" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

</xsl:stylesheet>