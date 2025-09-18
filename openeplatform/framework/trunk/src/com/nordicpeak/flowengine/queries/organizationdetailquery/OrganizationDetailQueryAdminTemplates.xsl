<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common" exclude-result-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/organiationdetailqueryadmin.css
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/organizationdetailqueryadmin.js
	</xsl:variable>
		

	<xsl:template match="Document">	
		
		<div id="OrganizationDetailQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateOrganizationDetailQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateOrganizationDetailQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="OrganizationDetailQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateOrganizationDetailQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="OrganizationDetailQuery" />
			</xsl:call-template>
			
			<div class="clearboth">
				<label><xsl:value-of select="$i18n.ContactChannelSettings" /></label>	
			</div>
			
			<xsl:variable name="fieldOptions">
				<option>
					<name><xsl:value-of select="$i18n.Field.Hidden"/></name>
					<value>HIDDEN</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.Field.Visible"/></name>
					<value>VISIBLE</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.Field.Required"/></name>
					<value>REQUIRED</value>
				</option>
			</xsl:variable>

			<label class="nomargin"><xsl:value-of select="$i18n.Fields" /></label>

			<div class="bigmarginbottom">
				<label for="fieldAddress" class="nomargin">
					<span class="organization-field-label">
						<xsl:value-of select="$i18n.Address" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldAddress'"/>
							<xsl:with-param name="name" select="'fieldAddress'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="OrganizationDetailQuery/fieldAddress" />
						</xsl:call-template>
			    </div>
				</label>
			</div>

			<div class="bigmarginbottom">
				<label for="fieldEmail" class="nomargin">
					<span class="organization-field-label">
						<xsl:value-of select="$i18n.Email" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldEmail'"/>
							<xsl:with-param name="name" select="'fieldEmail'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="OrganizationDetailQuery/fieldEmail" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="bigmarginbottom">
				<label for="fieldMobilePhone" class="nomargin">
					<span class="organization-field-label">
						<xsl:value-of select="$i18n.MobilePhone" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldMobilePhone'"/>
							<xsl:with-param name="name" select="'fieldMobilePhone'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="OrganizationDetailQuery/fieldMobilePhone" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="bigmarginbottom">
				<label for="fieldPhone" class="nomargin">
					<span class="organization-field-label">
						<xsl:value-of select="$i18n.Phone" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldPhone'"/>
							<xsl:with-param name="name" select="'fieldPhone'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="OrganizationDetailQuery/fieldPhone" />
						</xsl:call-template>
			    </div>
				</label>
			</div>

			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'hideNotificationChannelSettings'" />
					<xsl:with-param name="name" select="'hideNotificationChannelSettings'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				<label for="hideNotificationChannelSettings" class="nomargin">
					<xsl:value-of select="$i18n.HideNotificationChannelSettings" />
				</label>
			</div>
			
			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowSMS'" />
					<xsl:with-param name="name" select="'allowSMS'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				<label for="allowSMS" class="nomargin"><xsl:value-of select="$i18n.AllowSMS" /></label>
			</div>
			
			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'forceSMS'" />
					<xsl:with-param name="name" select="'forceSMS'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				<label for="forceSMS" class="nomargin"><xsl:value-of select="$i18n.ForceSMS" /></label>
			</div>
			
			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireEmailOrMobile'" />
					<xsl:with-param name="name" select="'requireEmailOrMobile'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				<label for="requireEmailOrMobile" class="nomargin"><xsl:value-of select="$i18n.RequireEmailOrMobile" /></label>
			</div>
			
			<div class="checkbox-setting">

				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireContactInfoVerification'" />
					<xsl:with-param name="name" select="'requireContactInfoVerification'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				
				<label for="requireContactInfoVerification" class="nomargin">
					<xsl:value-of select="$i18n.RequiredContactWayVerification" />
				</label>
			</div>
			
			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'validateZipCode'" />
					<xsl:with-param name="name" select="'validateZipCode'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" />
				</xsl:call-template>
				<label for="validateZipCode" class="nomargin">
					<xsl:value-of select="$i18n.ValidateZipCode" />
				</label>
			</div>

			<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			
			<div class="checkbox-setting">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'setAsAttribute'" />
					<xsl:with-param name="name" select="'setAsAttribute'" />
					<xsl:with-param name="element" select="OrganizationDetailQuery" /> 
					<xsl:with-param name="class" select="'vertical-align-middle'" />
				</xsl:call-template>
					
				<label for="setAsAttribute" class="nomargin">
					<xsl:value-of select="$i18n.setAsAttribute" />
				</label>
			</div>
			
			<div id="attribute-container">
				<div class="marginbottom">
					<label for="attributeName"><xsl:value-of select="$i18n.attributeName" /></label>
					<div>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'attributeName'"/>
							<xsl:with-param name="name" select="'attributeName'"/>
							<xsl:with-param name="title" select="$i18n.attributeName"/>
							<xsl:with-param name="element" select="OrganizationDetailQuery" />
						</xsl:call-template>
					</div>
				</div>
				
				<p class="floatleft clearboth"><xsl:value-of select="$i18n.attributeDescription" /></p>
				<div class="floatleft clearboth bigmarginbottom">
					<p class="tiny"><xsl:value-of select="$i18n.GeneralContactAttributeInfo" /></p>
				</div>
				
				<table class="border">
					<thead>
						<th><xsl:value-of select="$i18n.attributes.name" /></th>
						<th><xsl:value-of select="$i18n.attributes.value" /></th>
					</thead>
					<tbody>
						<tr>
							<td>prefix.organizationName</td>
							<td><xsl:value-of select="$java.exportOrganizationName" /></td>
						</tr>
						<tr>
							<td>prefix.organizationNumber</td>
							<td><xsl:value-of select="$java.exportOrganizationNumber" /></td>
						</tr>
						<tr>
							<td>prefix.citizenIdentifier</td>
							<td><xsl:value-of select="$java.exportContactCitizenIdentifier" /></td>
						</tr>
						<tr>
							<td>prefix.firstname</td>
							<td><xsl:value-of select="$java.exportContactFirstName" /></td>
						</tr>
						<tr>
							<td>prefix.lastname</td>
							<td><xsl:value-of select="$java.exportContactLastName" /></td>
						</tr>
						<tr>
							<td>prefix.address</td>
							<td><xsl:value-of select="$java.exportAddress" /></td>
						</tr>
						<tr>
							<td>prefix.zipCode</td>
							<td><xsl:value-of select="$java.exportZipCode" /></td>
						</tr>
						<tr>
							<td>prefix.postalAddress</td>
							<td><xsl:value-of select="$java.exportPostalAddress" /></td>
						</tr>
						<tr>
							<td>prefix.email</td>
							<td><xsl:value-of select="$java.exportEmail" /></td>
						</tr>
						<tr>
							<td>prefix.phone</td>
							<td><xsl:value-of select="$java.exportPhone" /></td>
						</tr>
						<tr>
							<td>prefix.mobilePhone</td>
							<td><xsl:value-of select="$java.exportMobilePhone" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="text-align-right margintop">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedOrganizationDetailQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.OrganizationDetailQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'maxLength'">
				<xsl:value-of select="$i18n.maxLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'attributeName'">
				<xsl:value-of select="$i18n.attributeName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>