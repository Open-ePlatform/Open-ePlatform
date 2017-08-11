<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>	

	<xsl:variable name="scripts">
		/common/js/queryadmin.js
		/common/js/featherlight.min.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/common/css/featherlight.min.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="RadioButtonQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateRadioButtonQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateRadioButtonQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="RadioButtonQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateRadioButtonQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="RadioButtonQuery" />
			</xsl:call-template>
			
			<xsl:call-template name="createAlternativesForm">
				<xsl:with-param name="alternatives" select="RadioButtonQuery/Alternatives/RadioButtonAlternative" />
				<xsl:with-param name="freeTextAlternative" select="RadioButtonQuery/freeTextAlternative" />
				<xsl:with-param name="editExtraValues" select="'true'" />
			</xsl:call-template>
			
			<xsl:variable name="options">
				<option>
					<name><xsl:value-of select="$i18n.Columns.One"/></name>
					<value>ONE</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.Columns.Two"/></name>
					<value>TWO</value>
				</option>
				<option>
					<name><xsl:value-of select="$i18n.Columns.Three"/></name>
					<value>THREE</value>
				</option>
			</xsl:variable>
				
			<div class="floatleft full bigmarginbottom">
				<label for="columnType" class="floatleft clearboth"><xsl:value-of select="$i18n.Columns" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'columns'"/>
						<xsl:with-param name="name" select="'columns'"/>
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="valueElementName" select="'value'" />
						<xsl:with-param name="element" select="exsl:node-set($options)/option" />
						<xsl:with-param name="selectedValue" select="RadioButtonQuery/columns" />
					</xsl:call-template>
		    </div>
			</div>
				
			<div class="floatleft full bigmarginbottom">
				<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'setAsAttribute'" />
						<xsl:with-param name="name" select="'setAsAttribute'" />
						<xsl:with-param name="element" select="RadioButtonQuery" /> 
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
						<xsl:with-param name="element" select="RadioButtonQuery" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
			    </div>
			</div>				
				
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>
	
	<xsl:template match="AlternativeModalExtra">
	
		<div class="floatleft full bigmarginbottom">
		
			<label for="alternativeprice" class="floatleft clearboth">
				<xsl:value-of select="$i18n.AlternativePrice" />
			</label>
			
			<p class="tiny floatleft full">
		  	<xsl:value-of select="$i18n.AlternativePriceDescription" />
		  </p>
			
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'alternativeprice'"/>
					<xsl:with-param name="name" select="'alternativeprice'"/>
				</xsl:call-template>
		  </div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="AlternativeExtraValues">
		<xsl:param name="alternativeID" />
		<xsl:param name="disabled" />
		<xsl:param name="context" />
	
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat('alternativeprice_', $alternativeID)" />
			<xsl:with-param name="name" select="concat('alternativeprice_', $alternativeID)" />
			<xsl:with-param name="value" select="$context/price" />
			<xsl:with-param name="requestparameters" select="//requestparameters" />
			<xsl:with-param name="disabled" select="$disabled"/>
		</xsl:call-template>
	
	</xsl:template>

	<xsl:template match="RadioButtonAlternative">
		
		<xsl:call-template name="createAlternative">
			<xsl:with-param name="alternativeID" select="alternativeID" />
			<xsl:with-param name="sortOrder" select="sortIndex" />
			<xsl:with-param name="value" select="name" />
			<xsl:with-param name="xmlValue" select="xmlValue" />
			<xsl:with-param name="attributeValue" select="attributeValue" />
			<xsl:with-param name="editExtraValues" select="'true'" />
		</xsl:call-template>
			
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedRadioButtonQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.RadioButtonQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">
	
		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'attributeName'">
				<xsl:value-of select="$i18n.attributeName" />
			</xsl:when>
			<xsl:when test="$fieldName = 'columns'">
				<xsl:value-of select="$i18n.Columns" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>