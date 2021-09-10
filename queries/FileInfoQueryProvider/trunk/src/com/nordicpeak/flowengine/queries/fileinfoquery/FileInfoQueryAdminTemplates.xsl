<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/jquery/plugins/jquery.fileuploader.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/fileuploader-init.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/fileinfoqueryadmin.css
	</xsl:variable>

	<xsl:template match="Document">

		<div id="FileInfoQueryProvider" class="contentitem">

			<xsl:apply-templates select="validationError" />
			<xsl:apply-templates select="UpdateFileInfoQuery" />

		</div>

	</xsl:template>

	<xsl:template match="UpdateFileInfoQuery">
	
		<h1>
			<xsl:value-of select="$i18n.UpdateQuery" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="FileInfoQuery/QueryDescriptor/name" />
		</h1>

		<xsl:apply-templates select="validationException/validationError" />

		<form id="updateFileInfoQueryForm" name="queryAdminForm" method="post" action="{/Document/requestinfo/uri}"  enctype="multipart/form-data">

			<xsl:call-template name="createCommonFieldsForm">
				<xsl:with-param name="element" select="FileInfoQuery" />
			</xsl:call-template>

			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideTitle'" />
						<xsl:with-param name="name" select="'hideTitle'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="FileInfoQuery" />
					</xsl:call-template>
					
					<label for="hideTitle">
						<xsl:value-of select="$i18n.HideTitle" />
					</label>
				</div>

			</div>

			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'hideBackground'" />
						<xsl:with-param name="name" select="'hideBackground'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="FileInfoQuery" />
					</xsl:call-template>
					
					<label for="hideBackground">
						<xsl:value-of select="$i18n.HideBackground" />
					</label>
				</div>

			</div>
			
			<div class="floatleft full bigmarginbottom">

				<div class="floatleft full">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'dontSetPopulated'" />
						<xsl:with-param name="name" select="'dontSetPopulated'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="FileInfoQuery" />
					</xsl:call-template>
					
					<label for="dontSetPopulated">
						<xsl:value-of select="$i18n.DontSetPopulated" />
					</label>
				</div>

			</div>
			
			<script type="text/javascript">
				fileuploader.imagePath='<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
				fileuploader.deleteFile='<xsl:value-of select="$i18n.DeleteFile" />';
			</script>
			
			<div class="full">
					
					<div class="upload clearboth">
						<span class="btn btn-upload btn-blue">
							<xsl:value-of select="$i18n.ChooseFiles" />
							
							<input id="fileuploader" type="file" name="fileuploader_newfile" multiple="multiple" size="55" class="fileuploader bigmarginbottom hidden"/>
						</span>
						
						<span>
							<xsl:value-of select="$i18n.MaximumFileSize" /><xsl:text>:&#x20;</xsl:text>
							<xsl:choose>
								<xsl:when test="FileInfoQuery/FormatedMaxSize">
									 <xsl:value-of select="FileInfoQuery/FormatedMaxSize" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="/Document/FormatedMaxAllowedFileSize" />
								</xsl:otherwise>
							</xsl:choose>
						</span>
						
					</div>
					
					<ul class="files">
					
						<xsl:apply-templates select="FileInfoQuery/Files/FileDescriptor"/>
						
					</ul>
					
				</div>
			
			<script type="text/javascript">
				$(function(){
					initFileUploader($("#fileuploader"));
				});
			</script>

			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>
	
	<xsl:template match="FileDescriptor">
	
		<!-- Javascript escaped filename -->
		<xsl:variable name="file-name">
			<xsl:call-template name="replace-string">
				<xsl:with-param name="text">
					<xsl:call-template name="replace-string">
						<xsl:with-param name="text" select="value" />
						<xsl:with-param name="from">
							<xsl:text>"</xsl:text>
						</xsl:with-param>
						<xsl:with-param name="to">
							<xsl:text>&quot;</xsl:text>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:with-param>
				<xsl:with-param name="from">
					<xsl:text>'</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="to">
					<xsl:text>\'</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		
		<li class="finished">
			<div class="file">
				<span class="name">
					<a href="{/Document/requestinfo/contextpath}{/Document/FullAlias}/file/{../../queryID}/{fileID}" target="blank">
						<img src="{/Document/requestinfo/contextpath}{/Document/FullAlias}/fileicon/{name}" class="vertical-align-middle marginright" />
						<xsl:value-of select="name" />
					</a>
				</span>
				
				<span class="italic"><xsl:text>(</xsl:text><xsl:value-of select="FormatedSize" /><xsl:text>)</xsl:text></span>
				
				<a data-icon-after="t" href="#" onclick="removeFile(event, 'fileuploader_delete_{fileID}', '{$i18n.ConfirmDeleteFile} {$file-name}?', this)" class="progress"><xsl:value-of select="$i18n.DeleteFile" /></a>
			
			</div>
			<div class="progressbar">
				<div style="width: 100%;" class="innerbar"></div>
			</div>
		</li>
	
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'UpdateFailedFileInfoQueryNotFound']">

		<p class="error">
			<xsl:value-of select="$i18n.FileInfoQueryNotFound" />
		</p>

	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'DescriptionRequiredWhenTitleHidden']">

		<p class="error">
			<xsl:value-of select="$i18n.DescriptionRequiredWhenTitleHidden" />
		</p>

	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FileSizeLimitExceeded']">
	
		<p class="error">
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part1"/>
			<xsl:value-of select="filename"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part2"/>
			<xsl:value-of select="size"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part3"/>
			<xsl:value-of select="maxFileSize"/>
			<xsl:value-of select="$i18n.FileSizeLimitExceeded.part4"/>
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToSaveFile']">
	
		<p class="error">
			<xsl:value-of select="$i18n.UnableToSaveFile.part1"/>
			<xsl:value-of select="filename"/>
			<xsl:value-of select="$i18n.UnableToSaveFile.part2"/>
		</p>
			
	</xsl:template>

	<xsl:template match="fieldName">

		<xsl:variable name="fieldName" select="." />

		<xsl:choose>
			<xsl:when test="$fieldName = ''">
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

</xsl:stylesheet>