<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

		<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>	

	<xsl:template match="Document">	
		
		<div id="ManualMultiSignQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateManualMultiSignQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateManualMultiSignQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="ManualMultiSignQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateManualMultiSignQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="ManualMultiSignQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideCitizenIdetifierInPDF'" />
						<xsl:with-param name="name" select="'hideCitizenIdetifierInPDF'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
					</xsl:call-template>
						
					<label for="hideCitizenIdetifierInPDF">
						<xsl:value-of select="$i18n.hideCitizenIdetifierInPDF" />
					</label>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setMultipartsAsOwners'" />
						<xsl:with-param name="name" select="'setMultipartsAsOwners'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
					</xsl:call-template>
						
					<label for="setMultipartsAsOwners">
						<xsl:value-of select="$i18n.SetMultipartsAsOwners" />
					</label>
			    </div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'preventPostersCitizenIdentifier'" />
						<xsl:with-param name="name" select="'preventPostersCitizenIdentifier'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
					</xsl:call-template>
						
					<label for="preventPostersCitizenIdentifier">
						<xsl:value-of select="$i18n.PreventPostersCitizenIdentifier" />
					</label>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="ManualMultiSignQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="setAsAttribute">
						<xsl:value-of select="$i18n.setAsAttribute" />
					</label>
			    </div>
			</div>
			
			<div id="attribute-container" class="floatleft full bigmarginbottom">
				<label for="attributeName" class="floatleft clearboth"><xsl:value-of select="$i18n.attributeName" /></label>
				
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attributeName'"/>
						<xsl:with-param name="name" select="'attributeName'"/>
						<xsl:with-param name="title" select="$i18n.attributeName"/>
						<xsl:with-param name="element" select="ManualMultiSignQuery" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
			  </div>
			    
				<p class="floatleft clearboth"><xsl:value-of select="$i18n.attributeDescription" /></p>
				
				<table class="floatleft clearboth">
					<thead>
						<th><xsl:value-of select="$i18n.attributes.name" /></th>
						<th><xsl:value-of select="$i18n.attributes.value" /></th>
					</thead>
					<tbody>
						<tr>
							<td>prefix.firstname</td>
							<td><xsl:value-of select="$i18n.Firstname" /></td>
						</tr>
						<tr>
							<td>prefix.lastname</td>
							<td><xsl:value-of select="$i18n.Lastname" /></td>
						</tr>
						<tr>
							<td>prefix.email</td>
							<td><xsl:value-of select="$i18n.Email" /></td>
						</tr>
						<tr>
							<td>prefix.mobilePhone</td>
							<td><xsl:value-of select="$i18n.MobilePhone" /></td>
						</tr>
						<tr>
							<td>prefix.citizenIdentifier</td>
							<td><xsl:value-of select="$i18n.SocialSecurityNumber" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
			
			<script type="text/javascript">
				var showHideAttributeName = function(){
					var checked = $("#setAsAttribute").prop("checked");
					$("#attribute-container").toggle(checked).find("input").prop("disabled", !checked);
				};
			
				showHideAttributeName();
				
				$("#setAsAttribute").change(function(){showHideAttributeName()});
			</script>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedManualMultiSignQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.ManualMultiSignQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:value-of select="$fieldName" />
		
	</xsl:template>

</xsl:stylesheet>