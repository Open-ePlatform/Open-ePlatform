<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/userorganizationsmodule.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
	</xsl:variable>

	<xsl:template match="Document">	
			
			<xsl:apply-templates select="ListOrganizations"/>
			<xsl:apply-templates select="AddOrganization"/>
			<xsl:apply-templates select="UpdateOrganization"/>
			
	</xsl:template>
	
	<xsl:template match="ListOrganizations">
		
		<xsl:apply-templates select="validationError"/>
		
		<section class="settings">
			
			<div class="heading-wrapper">
				<h1>
					<xsl:value-of select="/Document/module/name" />
				</h1>
			</div>
			
			<div class="errands-wrapper draft">
				<xsl:if test="Organizations/Organization">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.ListOrganizationDescription.Part1" /><xsl:text>&#160;</xsl:text><strong><xsl:value-of select="count(Organizations/Organization)" /></strong><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.ListOrganizationDescription.Part2" />
						</h3>
					</div>
				
				</xsl:if>
				
					<table class="oep-table">
						<thead class="sortable">
							<tr>
								<th class="icon"></th>
								<th class="organization">
									<span><xsl:value-of select="$i18n.Organization" /></span>
								</th>
								<th class="organizationNumer">
									<span><xsl:value-of select="$i18n.OrganizationNumber" /></span>
								</th>
								<th class="link" />
							</tr>
						</thead>
						<tbody>
							
							<xsl:choose>
								<xsl:when test="Organizations/Organization">
									<xsl:apply-templates select="Organizations/Organization" mode="list" />
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td class="icon" />
										<td colspan="3"><xsl:value-of select="$i18n.NoOrganizations" /></td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
							
						</tbody>
					</table>	
					
				</div>
			
				<article class="buttons">
			
				<a class="btn btn-green btn-right" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add"><xsl:value-of select="$i18n.AddOrganization" /></a>
			
				</article>
			
		</section>
		
	</xsl:template>
	
	<xsl:template match="Organization" mode="list">
	
		<tr>
			<td class="icon" />
			<td data-title="{$i18n.Organization}" class="organization"><xsl:value-of select="name" /></td>
			<td data-title="{$i18n.OrganizationNumber}" class="organizationNumber"><xsl:value-of select="organizationNumber" /></td>
			<td class="link">
				<a class="btn btn-green vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{organizationID}"><xsl:value-of select="$i18n.Update" /></a>
				<a class="btn btn-red vertical-align-middle" style="margin-left: 2px" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{organizationID}" onclick="return confirm('{$i18n.DeleteOrganizationConfirm}: {name}?');"><xsl:value-of select="$i18n.Delete" /></a>
			</td>
		</tr>
		
	
	</xsl:template>
	
	<xsl:template match="AddOrganization">

		<xsl:apply-templates select="validationException/validationError"/>

		<section class="settings">
			<div class="heading-wrapper">
				<h1>
					<xsl:value-of select="$i18n.AddOrganization" />
				</h1>
			</div>
			
			<form method="POST" action="{/document/requestinfo/uri}" name="userform">
				
				<xsl:call-template name="organizationForm" />
			
				<article class="buttons">
					<input type="submit" value="{$i18n.Add}" class="btn btn-green btn-inline" />
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel" /></a>
				</article>
			
			</form>
			
		</section>
		
	</xsl:template>	
	
	<xsl:template match="UpdateOrganization">

		<xsl:apply-templates select="validationException/validationError"/>

		<section class="settings">
			<div class="heading-wrapper">
				<h1>
					<xsl:value-of select="$i18n.UpdateOrganization" />
				</h1>
			</div>
			
			<form method="POST" action="{/document/requestinfo/uri}" name="userform">
				
				<xsl:call-template name="organizationForm">
					<xsl:with-param name="organization" select="Organization" />
				</xsl:call-template>
			
				<article class="buttons">
					<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline" />
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel" /></a>
				</article>
			
			</form>
			
		</section>
		
	</xsl:template>
	
	<xsl:template name="organizationForm">
		
		<xsl:param name="organization" select="null" />

		<article class="clearfix">
			
			<div class="heading-wrapper">
				<h2>
					<xsl:value-of select="$i18n.ContactDetails" />
				</h2>
			</div>
			<fieldset>
				<div class="split">
					<label for="name" class="required">
						<xsl:value-of select="$i18n.Name" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'name'" />
						<xsl:with-param name="id" select="'name'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				<div class="split odd">
					<label for="organizationNumber" class="required">
						<xsl:value-of select="$i18n.OrganizationNumberWithFormat" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'organizationNumber'" />
						<xsl:with-param name="id" select="'organizationNumber'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
			</fieldset>
			<fieldset>
				<div class="split">
					<label for="address">
						<xsl:value-of select="$i18n.Address" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'address'" />
						<xsl:with-param name="id" select="'address'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				<div class="split">
					<div class="left">
						<label for="zipCode">
							<xsl:value-of select="$i18n.ZipCode" />
						</label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'zipCode'" />
							<xsl:with-param name="id" select="'zipCode'" />
							<xsl:with-param name="size" select="40" />
							<xsl:with-param name="element" select="$organization" />
						</xsl:call-template>

					</div>
					<div class="right">
						<label for="postalAddress">
							<xsl:value-of select="$i18n.PostalAddress" />
						</label>

						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'postalAddress'" />
							<xsl:with-param name="id" select="'postalAddress'" />
							<xsl:with-param name="size" select="40" />
							<xsl:with-param name="element" select="$organization" />
						</xsl:call-template>
					</div>
				</div>
			</fieldset>
			<fieldset>
				
				<h3><xsl:value-of select="$i18n.ContactPerson" /></h3>
			
				<div class="split">
					<label for="firstname">
						<xsl:value-of select="$i18n.Firstname" />
					</label>

					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'firstname'" />
						<xsl:with-param name="id" select="'firstname'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				<div class="split odd">
					<label for="lastname">
						<xsl:value-of select="$i18n.Lastname" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'lastname'" />
						<xsl:with-param name="id" select="'lastname'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
			</fieldset>
			<fieldset>
				
				<div class="split">
					<label for="phone">
						<xsl:value-of select="$i18n.Phone" />
					</label>

					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'phone'" />
						<xsl:with-param name="id" select="'phone'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				
				<div class="split odd">
					<label for="email">
						<xsl:value-of select="$i18n.Email" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'email'" />
						<xsl:with-param name="id" select="'email'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				
			</fieldset>					
			
			<fieldset>
				
				<div class="split">
					<label for="mobilePhone">
						<xsl:value-of select="$i18n.MobilePhone" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'mobilePhone'" />
						<xsl:with-param name="id" select="'mobilePhone'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$organization" />
					</xsl:call-template>
				</div>
				
			</fieldset>
			
			<fieldset>
						
				<div>
							
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'contactByEmail'" />
						<xsl:with-param name="name" select="'contactByEmail'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="disabled" select="'disabled'" />
						<xsl:with-param name="checked">
							<xsl:if test="$organization/email != ''">true</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
					<label for="contactByEmail" class="checkbox disabled"><xsl:value-of select="$i18n.AllowContactByEmail" /></label><br/>
					
				</div>
				
				<div>
					
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'contactBySMS'" />
						<xsl:with-param name="name" select="'contactBySMS'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="disabled" select="'disabled'" />
						<xsl:with-param name="element" select="$organization"/>
					</xsl:call-template>
					<label for="contactBySMS" class="checkbox"><xsl:value-of select="$i18n.AllowContactBySMS" /></label><br/>
					
				</div>
				
			</fieldset>
			
		</article>

		<div class="divider"></div>

	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			
			<section class="modal error">
				<span data-icon-before="!">
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
							<xsl:value-of select="$i18n.Name"/>
						</xsl:when>
						<xsl:when test="fieldName = 'email'">
							<xsl:value-of select="$i18n.Email"/>
						</xsl:when>
						<xsl:when test="fieldName = 'address'">
							<xsl:value-of select="$i18n.Address"/>
						</xsl:when>
						<xsl:when test="fieldName = 'zipCode'">
							<xsl:value-of select="$i18n.ZipCode"/>
						</xsl:when>
						<xsl:when test="fieldName = 'postalAddress'">
							<xsl:value-of select="$i18n.PostalAddress"/>
						</xsl:when>
						<xsl:when test="fieldName = 'firstname'">
							<xsl:value-of select="$i18n.Firstname"/>
						</xsl:when>
						<xsl:when test="fieldName = 'lastname'">
							<xsl:value-of select="$i18n.Lastname"/>
						</xsl:when>
						<xsl:when test="fieldName = 'phone'">
							<xsl:value-of select="$i18n.Phone"/>
						</xsl:when>
						<xsl:when test="fieldName = 'mobilePhone'">
							<xsl:value-of select="$i18n.MobilePhone"/>
						</xsl:when>
						<xsl:when test="fieldName = 'organizationNumber'">
							<xsl:value-of select="$i18n.OrganizationNumber"/>
						</xsl:when>
						<xsl:when test="fieldName = 'email'">
							<xsl:value-of select="$i18n.Email"/>
						</xsl:when>
																																												
						<xsl:otherwise>
							<xsl:value-of select="fieldName"/>
						</xsl:otherwise>
					</xsl:choose>
					</span>
					<i class="icon close">x</i>
			</section>
			
		</xsl:if>
		
		<xsl:if test="messageKey">
			<section class="modal error">
				<span data-icon-before="!">
					<xsl:choose>
						<xsl:when test="messageKey='NameExists'">
							<xsl:value-of select="$i18n.NameExists"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey='OrganizationNumberExists'">
							<xsl:value-of select="$i18n.OrganizationNumberExists"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey='UpdateFailedOrganizationNotFound'">
							<xsl:value-of select="$i18n.UpdateFailedOrganizationNotFound"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.unknownFault"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</section>
		</xsl:if>
	</xsl:template>				
</xsl:stylesheet>