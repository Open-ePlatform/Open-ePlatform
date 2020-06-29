<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/plugins/jquery.fileuploader.js
	</xsl:variable>	

	<xsl:variable name="scripts">
		/js/fileuploader-init.js
		/js/fileuploadquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/fileuploadquery.css?v=1
	</xsl:variable>
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="FileUploadQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="FileUploadQueryInstance/FileUploadQuery/queryID" />
					</xsl:call-template>
					
				</div>
				
				<xsl:if test="FileUploadQueryInstance/FileUploadQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<ul class="files preview">
					<xsl:apply-templates select="FileUploadQueryInstance/Files/FileDescriptor" mode="show" />
				</ul>
				
			</article>
			
		</div>
		
	</xsl:template>		
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', FileUploadQueryInstance/FileUploadQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
		
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
		
			<a name="{$queryID}" />
		
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
						<xsl:apply-templates select="ValidationErrors/validationError"/>
						<div class="marker"></div>
					</div>
				</div>
			</xsl:if>
	
			<article>
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="FileUploadQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="FileUploadQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="FileUploadQueryInstance/FileUploadQuery/helpText">
						<xsl:apply-templates select="FileUploadQueryInstance/FileUploadQuery/helpText" />
					</xsl:if>
					
				</div>
				
				<xsl:if test="FileUploadQueryInstance/FileUploadQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<script type="text/javascript">
					fileuploader.imagePath='<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
					fileuploader.deleteFile='<xsl:value-of select="$i18n.DeleteFile" />';
				</script>
				
				<div class="full">
					
					<xsl:if test="not(Locked)">
						<div class="upload clearboth">
							<span class="btn btn-upload btn-blue">
							
								<xsl:choose>
									<xsl:when test="FileUploadQueryInstance/FileUploadQuery/selectFilesButtonText">
										<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/selectFilesButtonText" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.ChooseFiles" />
									</xsl:otherwise>
								</xsl:choose>
								
								<xsl:variable name="allowedFileExtensionsRegex">
									<xsl:choose>
										<xsl:when test="FileUploadQueryInstance/FileUploadQuery/allowedFileExtensions">
										
											<xsl:text>^.*\.(</xsl:text>
											<xsl:for-each select="FileUploadQueryInstance/FileUploadQuery/allowedFileExtensions/value">
												<xsl:value-of select="."/>
												
												<xsl:if test="position() != last()">
													<xsl:text>|</xsl:text>
												</xsl:if>
											</xsl:for-each>
											<xsl:text>)$</xsl:text>
										
										</xsl:when>
										<xsl:otherwise>
										
											<xsl:text>^.*$</xsl:text>
											
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								
								<xsl:variable name="fileCountLimit">
									<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/maxFileCount"/>
								</xsl:variable>
								
								<input id="{$queryID}_fileuploader" type="file" name="{concat('q', FileUploadQueryInstance/FileUploadQuery/queryID,'_newfile')}" multiple="multiple" size="55" class="fileuploader bigmarginbottom hidden" data-allowedfilesregex="{$allowedFileExtensionsRegex}" data-filecountlimit="{$fileCountLimit}"/>
							</span>
							
							<span>
								<xsl:value-of select="$i18n.MaximumFileSize" /><xsl:text>:&#x20;</xsl:text>
								<xsl:choose>
									<xsl:when test="FileUploadQueryInstance/FileUploadQuery/FormatedMaxSize">
										 <xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/FormatedMaxSize" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="/Document/FormatedMaxAllowedFileSize" />
									</xsl:otherwise>
								</xsl:choose>
							</span>
							
							<xsl:if test="FileUploadQueryInstance/FileUploadQuery/maxFileCount">
								<span>
									<xsl:value-of select="$i18n.MaximumFileCount" />
									<xsl:text>: </xsl:text>
									<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/maxFileCount" />
								</span>
							</xsl:if>
	
							<xsl:if test="FileUploadQueryInstance/FileUploadQuery/maxFileNameLength">
								<span>
									<xsl:value-of select="$i18n.MaximumFileNameLength" />
									<xsl:text>: </xsl:text>
									<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/maxFileNameLength" />
								</span>
							</xsl:if>
							
							<xsl:if test="FileUploadQueryInstance/FileUploadQuery/allowedFileExtensions/value">
								<span>
									<xsl:value-of select="$i18n.AllowedFilextentions" />
									<xsl:text>: </xsl:text>
									<xsl:apply-templates select="FileUploadQueryInstance/FileUploadQuery/allowedFileExtensions/value" mode="file-extension"/>
								</span>
							</xsl:if>
							
						</div>
					</xsl:if>
					
					<ul class="files">
					
						<xsl:apply-templates select="FileUploadQueryInstance/Files/FileDescriptor"/>
						
					</ul>
					
				</div>
				
			</article>
		
		</div>
		
		<script type="text/javascript">
			$(document).ready(function(){
				initFileUploadQuery('<xsl:value-of select="FileUploadQueryInstance/FileUploadQuery/queryID" />');
			});
		</script>
		
	</xsl:template>
		
	<xsl:template match="value" mode="file-extension">
	
		<xsl:value-of select="."/>
		
		<xsl:if test="position() != last()">
			<xsl:text>, </xsl:text>
		</xsl:if>
	
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
					<a href="{/Document/ShowQueryForm/queryRequestURL}?file={temporaryFileID}" target="blank">
						<img src="{/Document/requestinfo/contextpath}{/Document/fullAlias}/fileicon/.{FileExtension}" class="vertical-align-middle marginright" />
						<xsl:value-of select="name" />
					</a>
				</span>
				
				<span class="italic"><xsl:text>(</xsl:text><xsl:value-of select="FormatedSize" /><xsl:text>)</xsl:text></span>
				
				<xsl:if test="not(../../../Locked)">
					<a data-icon-after="t" href="#" onclick="removeFile(event, 'q{../../FileUploadQuery/queryID}_file{temporaryFileID}', '{$i18n.ConfirmDeleteFile} {$file-name}?', this)" class="progress"><xsl:value-of select="$i18n.DeleteFile" /></a>
				</xsl:if>
			
			</div>
			<div class="progressbar">
				<div style="width: 100%;" class="innerbar"></div>
			</div>
		</li>
	
	</xsl:template>
		
	<xsl:template match="FileDescriptor" mode="show">
		
		<li class="finished">
		
			<xsl:variable name="fileID">
				<xsl:choose>
					<xsl:when test="temporaryFileID">
						<xsl:value-of select="temporaryFileID"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fileID"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
		
			<a href="{/Document/ShowQueryValues/queryRequestURL}?file={$fileID}" class="btn btn-file">
				<img src="{/Document/requestinfo/contextpath}{/Document/fullAlias}/fileicon/.{FileExtension}" class="vertical-align-middle" /><xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="name"/><xsl:text>&#x20;</xsl:text><span class="size"><xsl:text>(</xsl:text><xsl:value-of select="FormatedSize" /><xsl:text>)</xsl:text></span>
			</a>
			
		</li>
		
	</xsl:template>
		
	<xsl:template match="validationError[messageKey='MaxFileCountReached']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.MaxFileCountReached.part1"/>
				<xsl:value-of select="maxFileCount"/>
				<xsl:value-of select="$i18n.MaxFileCountReached.part2"/>
			</strong>
		</span>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='InvalidFileExtension']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.InvalidFileExtension.part1"/>
				<xsl:value-of select="filename"/>
				<xsl:value-of select="$i18n.InvalidFileExtension.part2"/>
			</strong>
		</span>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FileSizeLimitExceeded']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.FileSizeLimitExceeded.part1"/>
				<xsl:value-of select="filename"/>
				<xsl:value-of select="$i18n.FileSizeLimitExceeded.part2"/>
				<xsl:value-of select="size"/>
				<xsl:value-of select="$i18n.FileSizeLimitExceeded.part3"/>
				<xsl:value-of select="maxFileSize"/>
				<xsl:value-of select="$i18n.FileSizeLimitExceeded.part4"/>
			</strong>
		</span>
			
	</xsl:template>

	<xsl:template match="validationError[messageKey='FileNameLengthLimitExceeded']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.FileNameLengthLimitExceeded.part1"/>
				<xsl:value-of select="filename"/>
				<xsl:value-of select="$i18n.FileNameLengthLimitExceeded.part2"/>
				<xsl:value-of select="length"/>
				<xsl:value-of select="$i18n.FileNameLengthLimitExceeded.part3"/>
				<xsl:value-of select="maxFileNameLength"/>
				<xsl:value-of select="$i18n.FileNameLengthLimitExceeded.part4"/>
			</strong>
		</span>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToSaveFile']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnableToSaveFile.part1"/>
				<xsl:value-of select="filename"/>
				<xsl:value-of select="$i18n.UnableToSaveFile.part2"/>
			</strong>
		</span>
			
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RequiredField']">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.RequiredField"/>
			</strong>
		</span>
			
	</xsl:template>
	
	<xsl:template match="validationError">
	
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.UnknownValidationError"/>
			</strong>
		</span>
			
	</xsl:template>			
		
</xsl:stylesheet>