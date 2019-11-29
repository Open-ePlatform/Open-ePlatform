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

	<xsl:variable name="prefixModes">
		<option>
			<name><xsl:value-of select="$i18n.AttachmentNamePrefixMode.QUERY_NAME" /></name>
			<value>QUERY_NAME</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.AttachmentNamePrefixMode.NO_PREFIX" /></name>
			<value>NO_PREFIX</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.AttachmentNamePrefixMode.CUSTOM" /></name>
			<value>CUSTOM</value>
		</option>
	</xsl:variable>

	<xsl:template match="Document">	
		
		<div id="FileUploadQueryProvider" class="contentitem">
		
			<xsl:apply-templates select="validationError"/>
			<xsl:apply-templates select="UpdateFileUploadQuery "/>
		
		</div>
		
	</xsl:template>
		
	<xsl:template match="UpdateFileUploadQuery">
	
		<h1><xsl:value-of select="$i18n.UpdateQuery" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="FileUploadQuery/QueryDescriptor/name" /></h1>
		
		<xsl:apply-templates select="validationException/validationError" />
		
		<form id="updateFileUploadQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}">
			
			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="FileUploadQuery" />
			</xsl:call-template>
			
			<div class="floatleft full bigmarginbottom">
				<label for="allowedFileExtensions" class="floatleft clearboth"><xsl:value-of select="$i18n.AllowedFileExtensions" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'allowedFileExtensions'"/>
						<xsl:with-param name="name" select="'allowedFileExtensions'"/>
						<xsl:with-param name="title" select="$i18n.AllowedFileExtensions"/>
						<xsl:with-param name="rows" select="'5'"/>
						<xsl:with-param name="value">
							<xsl:apply-templates select="FileUploadQuery/allowedFileExtensions/value" />
						</xsl:with-param>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="maxFileCount" class="floatleft clearboth"><xsl:value-of select="$i18n.MaxFileCount" /></label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'maxFileCount'"/>
						<xsl:with-param name="name" select="'maxFileCount'"/>
						<xsl:with-param name="title" select="$i18n.MaxFileCount"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="element" select="FileUploadQuery" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="maxFileSize" class="floatleft clearboth">
					<xsl:value-of select="$i18n.MaxFileSize" />
					<xsl:text>&#160;(</xsl:text>
					<xsl:value-of select="$i18n.MaxAllowedFileSize.Part1" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="MaxAllowedFileSize"  />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.MaxAllowedFileSize.Part2" />
					<xsl:text>)</xsl:text>
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'maxFileSize'"/>
						<xsl:with-param name="name" select="'maxFileSize'"/>
						<xsl:with-param name="title" select="$i18n.MaxFileSize"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="value" select="MaxFileSizeInMB" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="maxFileNameLength" class="floatleft clearboth">
					<xsl:value-of select="$i18n.MaxFileNameLength" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'maxFileNameLength'"/>
						<xsl:with-param name="name" select="'maxFileNameLength'"/>
						<xsl:with-param name="title" select="$i18n.MaxFileNameLength"/>
						<xsl:with-param name="size" select="'30'"/>
						<xsl:with-param name="value" select="MaxFileNameLength" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="name" select="'lockOnOwnershipTransfer'" />
						<xsl:with-param name="element" select="FileUploadQuery" /> 
					</xsl:call-template>
					
					<label for="lockOnOwnershipTransfer">
						<xsl:value-of select="$i18n.lockOnOwnershipTransfer" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full marginbottom">
				
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'inlinePDFAttachments'"/>
						<xsl:with-param name="name" select="'inlinePDFAttachments'"/>
						<xsl:with-param name="value" select="'true'"/> 
						<xsl:with-param name="element" select="FileUploadQuery" />
					</xsl:call-template>
					
					<label for="inlinePDFAttachments">
						<xsl:value-of select="$i18n.inlinePDFAttachments" />
					</label>
				</div>
			</div>
			
			<div class="floatleft full marginbottom">
				
				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'numberInlineAttachments'"/>
						<xsl:with-param name="name" select="'numberInlineAttachments'"/>
						<xsl:with-param name="value" select="'true'"/> 
						<xsl:with-param name="element" select="FileUploadQuery" />
					</xsl:call-template>
					
					<label for="numberInlineAttachments">
						<xsl:value-of select="$i18n.numberInlineAttachments" />
					</label>
				</div>
			</div>
			
			<script type="text/javascript">
				$(document).ready(function() {
					
					$("#inlinePDFAttachments").change(function() {
					
						var checked = $(this).prop("checked");
						$("#numberInlineAttachments").prop("disabled", !checked).parent().parent().toggle(checked);
						
					}).change();
				});
			</script>
			
			<div class="floatleft full bigmarginbottom">
				<label for="attachmentNamePrefixMode" class="floatleft clearboth">
					<xsl:value-of select="$i18n.AttachmentNamePrefixMode" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'attachmentNamePrefixMode'" />
						<xsl:with-param name="name" select="'attachmentNamePrefixMode'" />
						<xsl:with-param name="title" select="$i18n.AttachmentNamePrefixMode" />
						<xsl:with-param name="labelElementName" select="'name'" />
						<xsl:with-param name="valueElementName" select="'value'" />
						<xsl:with-param name="element" select="exsl:node-set($prefixModes)/option" />
						<xsl:with-param name="selectedValue" select="FileUploadQuery/attachmentNamePrefixMode" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="attachmentNameCustomPrefix" class="floatleft clearboth">
					<xsl:value-of select="$i18n.AttachmentNameCustomPrefix" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'attachmentNameCustomPrefix'" />
						<xsl:with-param name="name" select="'attachmentNameCustomPrefix'" />
						<xsl:with-param name="title" select="$i18n.MaxFileNameLength" />
						<xsl:with-param name="element" select="FileUploadQuery" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="selectFilesButtonText" class="floatleft clearboth">
					<xsl:value-of select="$i18n.SelectFilesButtonText" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'selectFilesButtonText'" />
						<xsl:with-param name="name" select="'selectFilesButtonText'" />
						<xsl:with-param name="title" select="$i18n.SelectFilesButtonText" />
						<xsl:with-param name="element" select="FileUploadQuery" />
					</xsl:call-template>
				</div>
			</div>
			
			<script type="text/javascript">
				(function(){
					var prefixMode = $("#attachmentNamePrefixMode");
					
					var showHideCustomPrefix = function() {
						var useCustom = prefixMode.val() == "CUSTOM";
						$("#attachmentNameCustomPrefix").prop("disabled", !useCustom).parent().parent().toggle(useCustom);
					};
					
					showHideCustomPrefix();
					prefixMode.change(showHideCustomPrefix);
				})();
			</script>
			
			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>
		
		</form>
		
	</xsl:template>

	<xsl:template match="value">
		
		<xsl:value-of select="." />
		
		<xsl:if test="position() != last()">
			<xsl:text>&#10;</xsl:text>
		</xsl:if>
		
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedFileUploadQueryNotFound']">
		
		<p class="error">
			<xsl:value-of select="$i18n.FileUploadQueryNotFound" />
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName">
	
		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'maxFileCount'">
				<xsl:value-of select="$i18n.maxFileCount" />
			</xsl:when>
			<xsl:when test="$fieldName = 'maxFileSize'">
				<xsl:value-of select="$i18n.maxFileSize" />
			</xsl:when>
			<xsl:when test="$fieldName = 'maxFileNameLength'">
				<xsl:value-of select="$i18n.maxFileNameLength" />
			</xsl:when>
			<xsl:when test="$fieldName = 'attachmentNamePrefixMode'">
				<xsl:value-of select="$i18n.attachmentNamePrefixMode" />
			</xsl:when>
			<xsl:when test="$fieldName = 'attachmentNameCustomPrefix'">
				<xsl:value-of select="$i18n.attachmentNameCustomPrefix" />
			</xsl:when>
			<xsl:when test="$fieldName = 'selectFilesButtonText'">
				<xsl:value-of select="$i18n.SelectFilesButtonText" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

</xsl:stylesheet>