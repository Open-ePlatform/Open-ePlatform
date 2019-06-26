<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
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
		/css/contactdetailqueryadmin.css
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/contactdetailqueryadmin.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="ContactDetailQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateContactDetailQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateContactDetailQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="ContactDetailQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateContactDetailQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="ContactDetailQuery" />
			</xsl:call-template>
			
			<div class="floatleft clearboth">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.ContactChannelSettings" /></label>
			</div>
			
			<xsl:variable name="fieldUpdateOptions">
				<option>
					<name><xsl:value-of select="$i18n.FieldUpdate.Always"/></name>
					<value>ALWAYS</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.FieldUpdate.Ask"/></name>
					<value>ASK</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.FieldUpdate.Never"/></name>
					<value>NEVER</value>
				</option>
			</xsl:variable>
				
			<div class="floatleft full bigmarginbottom">
				<label for="fieldUpdate" class="floatleft clearboth">
					<xsl:value-of select="$i18n.ProfileUpdate" />
				</label>
					
				<div class="floatleft full margintop">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'fieldUpdate'"/>
						<xsl:with-param name="name" select="'fieldUpdate'"/>
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="valueElementName" select="'value'" />
						<xsl:with-param name="element" select="exsl:node-set($fieldUpdateOptions)/option" />
						<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldUpdate" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'managerUpdateAccess'" />
					<xsl:with-param name="name" select="'managerUpdateAccess'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="managerUpdateAccess">
					<xsl:value-of select="$i18n.ManagerUpdateAccess" />
				</label>
			</div>
			
			<div class="floatleft clearboth">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.Fields" /></label>
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
				
			<div class="floatleft full bigmarginbottom">
				<label for="fieldCitizenID" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.CitizenID" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldCitizenID'"/>
							<xsl:with-param name="name" select="'fieldCitizenID'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldCitizenID" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldName" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.FirstAndLastname" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldName'"/>
							<xsl:with-param name="name" select="'fieldName'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldName" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldAddress" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.Address" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldAddress'"/>
							<xsl:with-param name="name" select="'fieldAddress'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldAddress" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldCareOf" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.CareOf" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldCareOf'"/>
							<xsl:with-param name="name" select="'fieldCareOf'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldCareOf" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldEmail" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.Email" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldEmail'"/>
							<xsl:with-param name="name" select="'fieldEmail'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldEmail" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldMobilePhone" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.MobilePhone" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldMobilePhone'"/>
							<xsl:with-param name="name" select="'fieldMobilePhone'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldMobilePhone" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fieldPhone" class="floatleft clearboth">
					<span class="contact-field-label">
						<xsl:value-of select="$i18n.Phone" />
					</span>
					
					<div class="bigmarginleft display-inline">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'fieldPhone'"/>
							<xsl:with-param name="name" select="'fieldPhone'"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($fieldOptions)/option" />
							<xsl:with-param name="selectedValue" select="ContactDetailQuery/fieldPhone" />
						</xsl:call-template>
			    </div>
				</label>
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'hideNotificationChannelSettings'" />
					<xsl:with-param name="name" select="'hideNotificationChannelSettings'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="hideNotificationChannelSettings">
					<xsl:value-of select="$i18n.HideNotificationChannelSettings" />
				</label>
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'allowSMS'" />
					<xsl:with-param name="name" select="'allowSMS'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="allowSMS"><xsl:value-of select="$i18n.AllowSMS" /></label>
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'requireAtLeastOneContactWay'" />
					<xsl:with-param name="name" select="'requireAtLeastOneContactWay'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="requireAtLeastOneContactWay"><xsl:value-of select="$i18n.RequireAtLeastOneContactWay" /></label>
			</div>
			
			<div class="floatleft full marginbottom">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'useOfficalAddress'" />
					<xsl:with-param name="name" select="'useOfficalAddress'" />
					<xsl:with-param name="value" select="'true'" />
					<xsl:with-param name="element" select="ContactDetailQuery" />
				</xsl:call-template>
				<label for="useOfficalAddress"><xsl:value-of select="$i18n.OfficalAddress" /></label>
			</div>
			
			<div class="floatleft full bigmargintop bigmarginbottom">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="ContactDetailQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="setAsAttribute">
						<xsl:value-of select="$i18n.setAsAttribute" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="attributeName" class="floatleft clearboth"><xsl:value-of select="$i18n.attributeName" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attributeName'"/>
						<xsl:with-param name="name" select="'attributeName'"/>
						<xsl:with-param name="title" select="$i18n.attributeName"/>
						<xsl:with-param name="element" select="ContactDetailQuery" />
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
						<td>prefix.citizenIdentifier</td>
						<td><xsl:value-of select="$java.exportCitizenID" /></td>
					</tr>
				</tbody>
			</table>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedContactDetailQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.ContactDetailQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'maxLength'">
				<xsl:value-of select="$i18n.maxLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldCitizenID'">
				<xsl:value-of select="$i18n.CitizenID" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldName'">
				<xsl:value-of select="i18n.FirstAndLastname" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldAddress'">
				<xsl:value-of select="$i18n.Address" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldMobilePhone'">
				<xsl:value-of select="$i18n.MobilePhone" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldPhone'">
				<xsl:value-of select="$i18n.Phone" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldEmail'">
				<xsl:value-of select="$i18n.Email" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldCareOf'">
				<xsl:value-of select="$i18n.CareOf" />
			</xsl:when>
			<xsl:when test="$fieldName = 'fieldUpdate'">
				<xsl:value-of select="$i18n.ProfileUpdate" />
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