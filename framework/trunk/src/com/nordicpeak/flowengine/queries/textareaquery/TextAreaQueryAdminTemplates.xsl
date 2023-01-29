<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/jquery-migrate.js?v=1
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>	

	<xsl:template match="Document">	
		
		<div id="TextAreaQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateTextAreaQuery"/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateTextAreaQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="TextAreaQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateTextAreaQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
		
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="TextAreaQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<label for="maxLength" class="floatleft clearboth"><xsl:value-of select="$i18n.MaxLength" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'maxLength'"/>
						<xsl:with-param name="name" select="'maxLength'"/>
						<xsl:with-param name="title" select="$i18n.MaxLength"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="element" select="TextAreaQuery" />
					</xsl:call-template>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="maxLength" class="floatleft clearboth"><xsl:value-of select="$i18n.Rows" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'rows'"/>
						<xsl:with-param name="name" select="'rows'"/>
						<xsl:with-param name="title" select="$i18n.Rows"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="element" select="TextAreaQuery" />
					</xsl:call-template>
			    </div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft bigmarginbottom full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'searchable'" />
						<xsl:with-param name="name" select="'searchable'" />
						<xsl:with-param name="element" select="TextAreaQuery" /> 
						<xsl:with-param name="class" select="'vertical-align-middle'" />
					</xsl:call-template>
						
					<label for="searchable">
						<xsl:value-of select="$i18n.Searchable" />
					</label>
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
						<xsl:with-param name="element" select="TextAreaQuery" /> 
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
						<xsl:with-param name="element" select="TextAreaQuery" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideTitle'" />
						<xsl:with-param name="name" select="'hideTitle'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="TextAreaQuery" />
					</xsl:call-template>
					
					<label for="hideTitle">
						<xsl:value-of select="$i18n.HideTitle" />
					</label>
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideDescriptionInPDF'" />
						<xsl:with-param name="name" select="'hideDescriptionInPDF'" />
						<xsl:with-param name="element" select="TextAreaQuery" /> 
					</xsl:call-template>
						
					<label for="hideDescriptionInPDF">
						<xsl:value-of select="$i18n.hideDescriptionInPDF" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'showLetterCount'" />
						<xsl:with-param name="name" select="'showLetterCount'" />
						<xsl:with-param name="element" select="TextAreaQuery" /> 
					</xsl:call-template>
						
					<label for="showLetterCount">
						<xsl:value-of select="$i18n.showLetterCount" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="name" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="element" select="TextAreaQuery" /> 
					</xsl:call-template>
						
					<label for="lockOnOwnershipTransfer">
						<xsl:value-of select="$i18n.lockOnOwnershipTransfer" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'keepalive'" />
						<xsl:with-param name="name" select="'keepalive'" />
						<xsl:with-param name="element" select="TextAreaQuery" /> 
					</xsl:call-template>
						
					<label for="keepalive">
						<xsl:value-of select="$i18n.keepalive" />
					</label>
				</div>
			</div>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'MaxLengthToBig']">
		
		<p class="error">
			<xsl:value-of select="$i18n.MaxLengthToBig" />
		</p>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'RowCountTooHigh']">
		
		<p class="error">
			<xsl:value-of select="$i18n.RowCountTooHigh" />
		</p>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedTextAreaQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.TextAreaQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'maxLength'">
				<xsl:value-of select="$i18n.maxLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'rows'">
				<xsl:value-of select="$i18n.Rows" />
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