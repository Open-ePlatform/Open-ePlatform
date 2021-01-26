<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
		/js/confirmpost.js
	</xsl:variable>	

	<xsl:variable name="scripts">
		/common/js/queryadmin.js
		/js/textfieldqueryadmin.js?v=1
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="TextFieldQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="ShowTextFieldQuery"/>
			<xsl:apply-templates select="UpdateTextFieldQuery"/>
			<xsl:apply-templates select="AddTextField"/>
			<xsl:apply-templates select="UpdateTextField"/>
			<xsl:apply-templates select="SortTextFields"/>
			<xsl:apply-templates select="SelectTextFieldQueryEndpoint"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="ShowTextFieldQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="TextFieldQuery/QueryDescriptor/name" /></h1>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.BaseInfo"/></legend>
			
			<div class="floatright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatequery/{TextFieldQuery/queryID}" title="{$i18n.UpdateBaseInformation}">
					<img class="alignbottom" src="{$imagePath}/pen.png"/>
				</a>
			</div>
			
			<xsl:call-template name="createCommonShowFields">
				<xsl:with-param name="element" select="TextFieldQuery" />
			</xsl:call-template>

			<div class="floatleft full bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.MergeQuery" /></label>
				<div class="floatleft full">
					<xsl:choose>
						<xsl:when test="TextFieldQuery/QueryDescriptor/mergeWithPreviousQuery = 'true'"><xsl:value-of select="$i18n.Yes" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$i18n.No" /></xsl:otherwise>
					</xsl:choose>
				</div>
			</div>
			
			<xsl:variable name="layout" select="TextFieldQuery/layout" />
			
			<div class="floatleft full bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.Layout" /></label>
				<div class="floatleft full">
					<xsl:value-of select="FieldLayout[value = $layout]/name" />
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.HideTitle" /></label>
				<div class="floatleft full">
					<xsl:choose>
						<xsl:when test="TextFieldQuery/hideTitle = 'true'"><xsl:value-of select="$i18n.Yes" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$i18n.No" /></xsl:otherwise>
					</xsl:choose>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.lockOnOwnershipTransfer" /></label>
				<div class="floatleft full">
					<xsl:choose>
						<xsl:when test="TextFieldQuery/lockOnOwnershipTransfer = 'true'"><xsl:value-of select="$i18n.Yes" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$i18n.No" /></xsl:otherwise>
					</xsl:choose>
				</div>
			</div>
			
		</fieldset>
		
		<fieldset>
			<legend><xsl:value-of select="$i18n.TextFields"/></legend>
			
			<xsl:if test="TextFieldQuery/Fields/TextField">
				
				<div class="floatleft full">
				
					<xsl:apply-templates select="TextFieldQuery/Fields/TextField" mode="list" />
			
				</div>
			
			</xsl:if>
			
			<xsl:if test="TextFieldQuery/Fields/TextField">
			
				<div class="floatright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sorttextfields/{TextFieldQuery/queryID}" title="{$i18n.SortTextFields.Title}">
						<xsl:value-of select="$i18n.SortTextFields.Title"/>
						<img class="alignbottom marginleft" src="{$commonImagePath}/move.png"/>
					</a>
				</div>
				
			</xsl:if>
			
			<div class="clearboth floatright">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addtextfield/{TextFieldQuery/queryID}" title="{$i18n.AddTextField}">
					<xsl:value-of select="$i18n.AddTextField"/>
					<img class="alignbottom marginleft" src="{$commonImagePath}/add.png"/>
				</a>
			</div>
			
		</fieldset>
		
		<xsl:if test="Endpoints and TextFieldQuery/Fields/TextField">
			
			<fieldset>
				<legend><xsl:value-of select="$i18n.Endpoint.Title"/></legend>
				
				<xsl:choose>
					<xsl:when test="TextFieldQuery/Endpoint">
					
						<xsl:value-of select="$i18n.Endpoint.Selected"/>
						<xsl:text>: </xsl:text>
						<strong><xsl:value-of select="TextFieldQuery/Endpoint/name"/></strong>
					
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:value-of select="$i18n.Endpoint.NonSelected"/>
					
					</xsl:otherwise>
				</xsl:choose>
				
				<div class="clearboth floatright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/selectendpoint/{TextFieldQuery/queryID}" title="{$i18n.Endpoint.Select}">
						<xsl:value-of select="$i18n.Endpoint.Select"/>
						<img class="alignbottom marginleft" src="{$commonImagePath}/pen.png"/>
					</a>
				</div>
				
			</fieldset>
		
		</xsl:if>
		
		<div class="floatright margintop clearboth">
			<input type="button" value="{$i18n.Done}" onclick="window.location = '{showFlowURL}'" />
		</div>
		
	</xsl:template>
		
	<xsl:template match="TextField" mode="list">
				
		<div class="floatleft hover border full marginbottom border-radius lightbackground">
			
			<div class="padding floatleft thirty">
				<div class="marginleft"><b><xsl:value-of select="label" /></b></div>
			</div>
			<div class="padding floatleft twenty">
				<xsl:if test="required = 'true'">
					<b><xsl:value-of select="$i18n.Required" /></b>
				</xsl:if>
			</div>
			<div class="padding floatleft twenty">
				<xsl:if test="formatValidator">
				
					<xsl:variable name="formatValidator" select="formatValidator"/>
					
					<b><xsl:value-of select="../../../FormatValidator[className = $formatValidator]/name" /></b>
				</xsl:if>
			</div>
			<div class="padding floatright">
				<div class="floatright marginright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletetextfield/{textFieldID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteTextField}: {label}">
						<img src="{$imagePath}/delete.png"/>
					</a>
				</div>
				<div class="floatright">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatetextfield/{textFieldID}" title="{$i18n.UpdateTextField}: {label}">
						<img src="{$imagePath}/pen.png"/>
					</a>
				</div>
			</div>
			
		</div>
				
	</xsl:template>
	
	<xsl:template match="AddTextField">
	
		<h1><xsl:value-of select="$i18n.AddTextField" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
	
		<form id="addTextFieldForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
			
			<xsl:call-template name="showTextFieldForm" />
		
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.Add}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="UpdateTextField">
	
		<h1><xsl:value-of select="$i18n.UpdateTextField" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="TextField/label" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="udpateTextFieldForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="showTextFieldForm">
				<xsl:with-param name="element" select="TextField" />
			</xsl:call-template>
		
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template name="showTextFieldForm">
		
		<xsl:param name="element" select="null" />
		
		<div class="floatleft full bigmarginbottom">
			<label for="label" class="floatleft clearboth"><xsl:value-of select="$i18n.Label" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'label'"/>
					<xsl:with-param name="name" select="'label'"/>
					<xsl:with-param name="title" select="$i18n.Label"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="maxlength" select="'255'"/>
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'disabled'" />
					<xsl:with-param name="name" select="'disabled'" />
					<xsl:with-param name="element" select="$element" /> 
					<xsl:with-param name="class" select="'vertical-align-middle'" />
				</xsl:call-template>
					
				<label for="disabled">
					<xsl:value-of select="$i18n.Disabled" />
				</label>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom" id="contains-price">
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'containsPrice'" />
					<xsl:with-param name="name" select="'containsPrice'" />
					<xsl:with-param name="element" select="$element" /> 
					<xsl:with-param name="class" select="'vertical-align-middle'" />
				</xsl:call-template>
					
				<label for="containsPrice">
					<xsl:value-of select="$i18n.ContainsPrice" />
				</label>
			</div>
		</div>		
		
		<div class="floatleft full bigmarginbottom" id="default-value">
			<label for="defaultValue" class="floatleft clearboth"><xsl:value-of select="$i18n.DefaultValue" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'defaultValue'"/>
					<xsl:with-param name="name" select="'defaultValue'"/>
					<xsl:with-param name="title" select="$i18n.DefaultValue"/>
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="maxlength" select="'255'"/>
				</xsl:call-template>
			</div>
		</div>
		
		<div id="disable-hide-fields">
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'required'" />
						<xsl:with-param name="name" select="'required'" />
						<xsl:with-param name="element" select="$element" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="required">
						<xsl:value-of select="$i18n.Required" />
					</label>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'searchable'" />
						<xsl:with-param name="name" select="'searchable'" />
						<xsl:with-param name="element" select="$element" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="searchable">
						<xsl:value-of select="$i18n.Searchable" />
					</label>
				</div>
			</div>
			
			<!-- 
			<div class="floatleft full bigmarginbottom">
				<label for="width" class="floatleft clearboth"><xsl:value-of select="$i18n.Width" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'width'"/>
						<xsl:with-param name="name" select="'width'"/>
						<xsl:with-param name="title" select="$i18n.Width"/>
						<xsl:with-param name="size" select="'30'" />
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
				</div>
			</div>
			 -->
			
			<div class="floatleft full bigmarginbottom">
				<label for="formatValidator" class="floatleft clearboth"><xsl:value-of select="$i18n.FormatValidator" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'formatValidator'" />
						<xsl:with-param name="name" select="'formatValidator'" />
						<xsl:with-param name="valueElementName" select="'className'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="FormatValidator" />
						<xsl:with-param name="addEmptyOption" select="'&#160;'" />
						<xsl:with-param name="selectedValue" select="$element/formatValidator" />
					</xsl:call-template>
					<xsl:apply-templates select="FormatValidator" />
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="invalidFormatMessage" class="floatleft clearboth"><xsl:value-of select="$i18n.InvalidFormatMessage" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'invalidFormatMessage'"/>
						<xsl:with-param name="name" select="'invalidFormatMessage'"/>
						<xsl:with-param name="title" select="$i18n.InvalidFormatMessage"/>
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="placeholderText" class="floatleft clearboth"><xsl:value-of select="$i18n.PlaceHolderText" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'placeholderText'"/>
						<xsl:with-param name="name" select="'placeholderText'"/>
						<xsl:with-param name="title" select="$i18n.PlaceHolderText"/>
						<xsl:with-param name="element" select="$element" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="minContentLength" class="floatleft clearboth"><xsl:value-of select="$i18n.Field.MinLength" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'minContentLength'"/>
						<xsl:with-param name="name" select="'minContentLength'"/>
						<xsl:with-param name="title" select="$i18n.Field.MinLength"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="maxContentLength" class="floatleft clearboth"><xsl:value-of select="$i18n.MaxLength" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'maxContentLength'"/>
						<xsl:with-param name="name" select="'maxContentLength'"/>
						<xsl:with-param name="title" select="$i18n.MaxLength"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="element" select="$element" />
					</xsl:call-template>
				</div>
			</div>
		</div>
		
		<h2><xsl:value-of select="$i18n.AdvancedSettings" /></h2>
		
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'setAsAttribute'" />
					<xsl:with-param name="name" select="'setAsAttribute'" />
					<xsl:with-param name="element" select="$element" /> 
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
					<xsl:with-param name="element" select="$element" />
					<xsl:with-param name="maxlength" select="'255'"/>
				</xsl:call-template>
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label for="xsdElementName" class="floatleft clearboth"><xsl:value-of select="$i18n.xsdElementName" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'xsdElementName'"/>
					<xsl:with-param name="name" select="'xsdElementName'"/>
					<xsl:with-param name="title" select="$i18n.xsdElementName"/>
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="FormatValidator">
		
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat('validatorMessage-', formatValidatorID)" />
			<xsl:with-param name="name" select="concat('validatorMessage-', formatValidatorID)" />
			<xsl:with-param name="value" select="validationMessage" />
		</xsl:call-template>
			
		<xsl:call-template name="createHiddenField">
			<xsl:with-param name="id" select="concat('placeholder-', formatValidatorID)" />
			<xsl:with-param name="name" select="concat('placeholder-', formatValidatorID)" />
			<xsl:with-param name="value" select="placeholder" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="UpdateTextFieldQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateBaseInformation" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateTextFieldQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="TextFieldQuery" />
				<xsl:with-param name="flowTypeID" select="FlowType/flowTypeID" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<label for="layout" class="floatleft clearboth"><xsl:value-of select="$i18n.Layout" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'layout'"/>
						<xsl:with-param name="name" select="'layout'"/>
						<xsl:with-param name="valueElementName" select="'value'" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="element" select="FieldLayout" />
						<xsl:with-param name="selectedValue" select="TextFieldQuery/layout" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideTitle'" />
						<xsl:with-param name="name" select="'hideTitle'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="TextFieldQuery" />
					</xsl:call-template>
					
					<label for="hideTitle">
						<xsl:value-of select="$i18n.HideTitle" />
					</label>
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="name" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="element" select="TextFieldQuery" /> 
					</xsl:call-template>
					
					<label for="lockOnOwnershipTransfer">
						<xsl:value-of select="$i18n.lockOnOwnershipTransfer" />
					</label>
				</div>
			</div>
						
			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'lockForManagerUpdate'" />
						<xsl:with-param name="name" select="'lockForManagerUpdate'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="TextFieldQuery" />
					</xsl:call-template>
					
					<label for="lockForManagerUpdate">
						<xsl:value-of select="$i18n.lockForManagerUpdate" />
					</label>
				</div>

			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="SortTextFields">
		
		<h1><xsl:value-of select="$i18n.SortTextFields" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="TextFieldQuery/QueryDescriptor/name" /></h1>
		
		<form id="sortTextFieldsForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full sortable">
				
				<xsl:apply-templates select="TextFieldQuery/Fields/TextField" mode="sort" />
				
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>
		
	</xsl:template>
	
	<xsl:template match="TextField" mode="sort">
	
		<div id="textfield_{textFieldID}" class="floatleft hover border full marginbottom lightbackground cursor-move border-radius">
			<div class="padding">
				<img class="vertical-align-middle marginright" src="{$commonImagePath}/move.png" title="{$i18n.MoveAlternative}"/>
				<xsl:value-of select="label" />
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="id" select="concat('sortorder_', textFieldID)" />
					<xsl:with-param name="name" select="concat('sortorder_', textFieldID)" />
					<xsl:with-param name="value" select="sortIndex" />
					<xsl:with-param name="class" select="'sortorder'" />
				</xsl:call-template>
			</div>
		</div>
	
	</xsl:template>
	 
	<xsl:template match="SelectTextFieldQueryEndpoint">
	
		<xsl:apply-templates select="validationException/validationError" />
	
		<form id="selectTextFieldQueryEndpointForm" name="selectTextFieldQueryEndpointForm" method="post" action="{/Document/requestinfo/uri}">
	
			<div class="floatleft hugemarginright">
				<div>
					<label for="endpointID" class="floatleft clearboth">
						<xsl:value-of select="$i18n.Endpoint.Title" />
					</label>
				</div>
				<div>
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'endpointID'" />
						<xsl:with-param name="name" select="'endpointID'" />
						<xsl:with-param name="title" select="$i18n.Endpoint.Title"/>
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="valueElementName" select="'endpointID'" />
						<xsl:with-param name="element" select="Endpoints/Endpoint" />
						<xsl:with-param name="class" select="'marginright'" />
						<xsl:with-param name="selectedValue" select="TextFieldQuery/Endpoint/endpointID" />
						<xsl:with-param name="addEmptyOption" select="$i18n.Endpoint.Select.empty" />
					</xsl:call-template>
				</div>
			</div>
			
			<label id="endpoint-fields" class="floatleft full">
				<xsl:value-of select="$i18n.Endpoint.Fields" />
			</label>
			
			<xsl:for-each select="Endpoints/Endpoint">
			
				<div id="endpoint-{endpointID}" class="endpoint floatleft full" style="display: none;">
					
					<table>
					
						<xsl:variable name="endpoint" select="."/>
						
						<xsl:for-each select="../../TextFieldQuery/Fields/TextField">
						
							<tr>
								<xsl:variable name="name" select="concat('endpointField-', $endpoint/endpointID, '-', textFieldID)" />
								
								<td>
									<label for="{$name}" class="nomargin bigpaddingright">
										<xsl:value-of select="label" />
									</label>
								</td>
								
								<td>
									<xsl:call-template name="createDropdown">
										<xsl:with-param name="id" select="$name" />
										<xsl:with-param name="name" select="$name" />
										<xsl:with-param name="selectedValue" select="endpointField" />
										<xsl:with-param name="element" select="$endpoint/Fields/value" />
										<xsl:with-param name="simple" select="true()" />
										<xsl:with-param name="addEmptyOption" select="$i18n.Endpoint.Field.Select" />
										<xsl:with-param name="requestparameters" select="../../../requestparameters" />
									</xsl:call-template>
								</td>
								
							</tr>	
						
						</xsl:for-each>
						
					</table>
					
				</div>
			</xsl:for-each>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'UpdateFailedTextFieldQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.TextFieldQueryNotFound" />
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'NoEndpointFieldsSelected']">
		
		<p class="error">
			<xsl:value-of select="$i18n.Endpoint.Validation.NoEndpointFieldsSelected" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'minContentLength'">
				<xsl:value-of select="$i18n.Field.minLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'maxContentLength'">
				<xsl:value-of select="$i18n.maxLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'label'">
				<xsl:value-of select="$i18n.label" />
			</xsl:when>
			<xsl:when test="$fieldName = 'width'">
				<xsl:value-of select="$i18n.width" />
			</xsl:when>
			<xsl:when test="$fieldName = 'invalidFormatMessage'">
				<xsl:value-of select="$i18n.invalidFormatMessage" />
			</xsl:when>
			<xsl:when test="$fieldName = 'attributeName'">
				<xsl:value-of select="$i18n.attributeName" />
			</xsl:when>
			<xsl:when test="$fieldName = 'defaultValue'">
				<xsl:value-of select="$i18n.DefaultValue" />
			</xsl:when>
			<xsl:when test="$fieldName = 'endpointID'">
				<xsl:value-of select="$i18n.Endpoint" />
			</xsl:when>
			<xsl:when test="starts-with($fieldName, 'endpointField-')">
			
				<xsl:variable name="lastParts" select="substring($fieldName, 15)" />
				<xsl:variable name="endpointID" select="substring-before($lastParts, '-')" />
				<xsl:variable name="textFieldID" select="substring($lastParts, 2 + string-length($endpointID))" />
				
				<xsl:value-of select="../../../TextFieldQuery/Fields/TextField[textFieldID = $textFieldID]/label" />
				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>