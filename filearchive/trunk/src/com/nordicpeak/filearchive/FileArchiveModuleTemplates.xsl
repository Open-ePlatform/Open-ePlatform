<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/usergrouplist/UserGroupList.sv.xsl"/>
	
	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery-ui.js
		/tablesorter/js/jquery.tablesorter.min.js
	</xsl:variable>	

	<xsl:variable name="scripts">
		/js/jquery.qloader.js
		/js/qloader-init.js
		/js/UserGroupList.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/tablesorter.css
		/css/filearchive.css
		/css/UserGroupList.css
	</xsl:variable>

	<xsl:template match="Document">
	
		<div id="FileArchiveModule">
			<xsl:apply-templates select="ListFileCollections"/>
			<xsl:apply-templates select="AddFileCollection"/>
			<xsl:apply-templates select="UpdateFileCollection"/>
			<xsl:apply-templates select="ShowFileCollection"/>
			<xsl:apply-templates select="SecureCollection"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ShowFileCollection">
	
		<script src="{$scriptPath}/showfilecollection.js"></script>
	
		<div class="contentitem">
			<h1>
				<xsl:choose>
					<xsl:when test="FileCollection/name">
						<xsl:value-of select="FileCollection/name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.unNamedFileCollection" />
					</xsl:otherwise>
				</xsl:choose>
			</h1>
			
			<xsl:if test="ShowFileCollectionURL">
				<div class="form-group mt-3">
					<label for="link">
						<xsl:choose>
							<xsl:when test="FileCollection/secureCollection = 'true'">
								<xsl:value-of select="$i18n.SecureFileCollectionURL"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.fileCollectionURL"/>
							</xsl:otherwise>
						</xsl:choose>
					</label>
					
					<div class="d-flex">
						<input type="text" id="link" readonly="readonly" value="{/Document/requestinfo/url}" class="fifty form-control bigmarginright"/>
						
						<button id="copylink" type="button" data-copied="{$i18n.LinkCopied}" class="btn btn-secondary"><xsl:value-of select="$i18n.CopyLink"/></button>
					</div>
				</div>
				
				<xsl:value-of select="FileCollectionText" disable-output-escaping="yes"/>
			</xsl:if>
		</div>
		
		<xsl:if test="FileCollection/encrypted = 'true' and not(FileCollectionPasswordSet)">
			<div class="contentitem">
				<h2><xsl:value-of select="$i18n.EncryptedFileCollection"/></h2>
				
				<form action="{/Document/requestinfo/url}/" method="POST">
					<div class="mt-3">
						<xsl:value-of select="$i18n.EncryptedFileCollectionText"/>
					</div>
					
					<xsl:if test="validationError">
						<div class="alert alert-danger mt-3" role="alert">
							<xsl:apply-templates select="validationError" />
						</div>
					</xsl:if>
					
					<div class="form-group mt-3">
						<label for="password">
							<xsl:value-of select="$i18n.Password" />
						</label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'password'" />
							<xsl:with-param name="id" select="'password'" />
							<xsl:with-param name="class" select="'form-control'" />
						</xsl:call-template>
					</div>
					
					<div class="text-align-right">
						<input type="submit" class="btn btn-success" value="{$i18n.ShowFiles}" />
					</div>
				</form>
			</div>
		</xsl:if>
		
		<xsl:if test="FileCollection/encrypted = 'false' or FileCollectionPasswordSet">	
			<xsl:if test="FileCollection/allowsCollaboration = 'true'">
				<div class="contentitem">
					<h2><xsl:value-of select="$i18n.UploadFiles" /></h2>
			
					<div class="alert alert-info mt-3" role="alert">
						<xsl:value-of select="$i18n.CollaborationCollectionText"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$i18n.uploadLimit1"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="/Document/MaxUploadSize"/>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$i18n.uploadLimit2"/>
					</div>
					
					<form id="upload-file-form" enctype="multipart/form-data">
						<div class="form-group">
							<label for="add-files"><xsl:value-of select="$i18n.SelectFile"/></label>
							
							<input type="file" class="form-control-file" name="file" id="add-files"/>
						</div>
						
						<div class="alert alert-danger" role="alert" id="too-big-file" style="display: none;">
							<i class="icons icon-warning" aria-hidden="true"/>
							
							<span><xsl:value-of select="$i18n.SelectedFileTooBig"/></span>
						</div>
						
						<div class="text-align-right">
							<progress id="upload-progress" value="0" max="100" style="display: none;"></progress><br/>
							
							<button type="submit" class="btn btn-success" id="upload-file" data-maxsize="{/Document/MaxUploadSize * 1024 * 1024}" data-uploading="{$i18n.Uploading}" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/upload/{FileCollection/collectionID}/{FileCollection/alias}">
								<img src="{$imagePath}/spinner.gif" class="marginright" style="display: none;"/>
								
								<span>
									<xsl:value-of select="$i18n.UploadFile"/>
								</span>
							</button>
						</div>
					</form>
				</div>
			</xsl:if>
			
			<div class="contentitem">
				<div class="d-flex align-items-center mb-2">
					<h2><xsl:value-of select="$i18n.files"/></h2>
					
					<xsl:if test="count(FileCollection/files/File) > 1">
						<div class="ml-auto">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/downloadc/{FileCollection/collectionID}/{FileCollection/alias}">
								<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/zip.png"/>
								<xsl:text>&#x20;</xsl:text>
								<xsl:value-of select="$i18n.downloadAllFilesAsZip"/>
							</a>
						</div>
					</xsl:if>
				</div>
				
				<xsl:choose>
					<xsl:when test="FileCollection/files">
						<xsl:if test="RemainingDays">
							<div class="alert alert-info mb-3" role="alert">
								<xsl:value-of select="$i18n.FilesWillBeRemoved.part1"/>
								
								<xsl:value-of select="RemainingDays"/>
								
								<xsl:value-of select="$i18n.FilesWillBeRemoved.part2"/>
							</div>
						</xsl:if>
					
						<xsl:apply-templates select="FileCollection/files/File" mode="show"/>
					</xsl:when>
					<xsl:otherwise>
						<div><xsl:value-of select="$i18n.noFilesInCollection" /></div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="File" mode="show">
	
		<div class="d-flex align-items-center mb-2">
			<img class="mr-1" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/floppy.png" aria-hidden="true"/>
		
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/download/{fileID}/{alias}" class="mr-1">
				<xsl:value-of select="name" />
			</a>
			
			<small>
				<xsl:text> (</xsl:text>
				<xsl:value-of select="FormatedSize" />
				<xsl:text>)</xsl:text>
			</small>
		</div>
	
	</xsl:template>
	
	<xsl:template match="ListFileCollections">
	
		<script src="{$scriptPath}/filecollection.js?v=7"></script>
	
		<div class="contentitem">
			<h1><xsl:value-of select="$i18n.fileCollections"/></h1>
					
			<xsl:apply-templates select="validationError"/>
			
			<xsl:if test="ListFileCollectionsInfo or /Document/KeepFilesDays">
				<div class="alert alert-info mt-3" role="alert">
					<xsl:if test="ListFileCollectionsInfo">
						<xsl:value-of select="ListFileCollectionsInfo" disable-output-escaping="yes"/>
					</xsl:if>
					
					<xsl:if test="/Document/KeepFilesDays">
						<p><xsl:value-of select="$i18n.ListFileCollectionsInfo.part4.1"/><xsl:value-of select="/Document/KeepFilesDays"/><xsl:value-of select="$i18n.ListFileCollectionsInfo.part4.2"/></p>
					</xsl:if>
				</div>
			</xsl:if>
			
			<xsl:choose>
				<xsl:when test="FileCollections">
					<table id="collections-table" class="table table-striped table-sm full tablesorter">
						<thead class="sortable">
							<tr>
								<th class="pr-3 sorter-false"></th>
															
								<th class="pointer">
									<xsl:value-of select="$i18n.name" />
								</th>						
								
								<th class="pointer">
									<xsl:value-of select="$i18n.posted" />
								</th>
								
								<th class="pointer">
									<xsl:value-of select="$i18n.poster" />
								</th>
								
								<th class="pointer">
									<xsl:value-of select="$i18n.fileCount" />
								</th>
																
								<th class="pointer nowrap sorter-false"></th>
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="FileCollections/FileCollection" mode="list"/>
						</tbody>
					</table>
				</xsl:when>
				<xsl:otherwise>
					<p>
						<xsl:value-of select="$i18n.noFileCollectionsFound"/>
					</p>
				</xsl:otherwise>
			</xsl:choose>			
			
			<xsl:if test="AddAccess">
				<div class="text-align-right mt-3">
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add">
						<xsl:value-of select="$i18n.addFileCollection"/>
						
						<img class="marginleft alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_papers_add.png"/>
					</a>
				</div>		
			</xsl:if>
		</div>
				
	</xsl:template>
	
	<xsl:template match="FileCollection" mode="list">
	
		<xsl:variable name="name">
			<xsl:choose>
				<xsl:when test="name">
					<xsl:value-of select="name" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.unNamedFileCollection" />
				</xsl:otherwise>
			</xsl:choose>		
		</xsl:variable>
	
		<tr>
			<td>
				<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_papers.png" aria-hidden="true" />
			</td>
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{collectionID}/{alias}">
					<xsl:value-of select="$name" />
					
					<xsl:if test="encrypted = 'true'">
						<xsl:text> (</xsl:text>
						<xsl:value-of select="$i18n.Encrypted"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
				</a>
			</td>
			
			<td class="nowrap">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{collectionID}/{alias}">
					<xsl:value-of select="posted" />
				</a>
			</td>
			
			<td class="nowrap">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{collectionID}/{alias}">
					<xsl:value-of select="poster/user/firstname" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="poster/user/lastname" />
				</a>
			</td>
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{collectionID}/{alias}">
					<xsl:value-of select="count(files/File)" />
				</a>
			</td>
			
			<td class="text-align-right nowrap">
				<xsl:if test="IsOwner or ../../AdminAccess">
					<xsl:if test="encrypted = 'false'">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{collectionID}" title="{$i18n.editFileCollection}: {$name}">
							<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_papers_edit.png"/>
						</a>
					</xsl:if>
					
					<a class="marginleft delete-collection" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{collectionID}" data-confirm="{$i18n.deleteFileCollection}: {$name}?" title="{$i18n.deleteFileCollection}: {$name}">
						<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/folder_papers_delete.png"/>
					</a>
				</xsl:if>		
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="SecureCollection">
		
		<div class="contentitem">
			<h1><xsl:value-of select="$i18n.SecureCollectionInfo"/></h1>
			
			<xsl:if test="ShowFileCollectionURL">
				<script src="{$scriptPath}/showfilecollection.js"></script>
			
				<p>
					<xsl:value-of select="$i18n.SecureFileCollectionURL"/>
					
					<br/>
					
					<input type="text" id="link" readonly="readonly" value="{/Document/requestinfo/url}" class="forty bigmarginright"/>
					
					<button id="copylink" type="button" data-copied="{$i18n.LinkCopied}"><xsl:value-of select="$i18n.CopyLink"/></button>
				</p>
				
				<xsl:value-of select="FileCollectionText" disable-output-escaping="yes"/>
				
				<br/>
			</xsl:if>
			
			<xsl:choose>
				<xsl:when test="LoggedIn">
					<p><xsl:value-of select="$i18n.SecureCollectionLoggedIn"/></p>
				</xsl:when>
				<xsl:otherwise>
					<p>
						<xsl:value-of select="$i18n.SecureCollectionInfoText.part1"/>
						<xsl:if test="/Document/KeepFilesDays">
							<xsl:value-of select="$i18n.SecureCollectionInfoText.part2.1"/>
							<xsl:value-of select="/Document/KeepFilesDays"/>
							<xsl:value-of select="$i18n.SecureCollectionInfoText.part2.2"/>
						</xsl:if>
						<xsl:value-of select="$i18n.SecureCollectionInfoText.part3"/>
					</p>
					
					<div class="text-align-right">
						<!-- TODO this needs to be redone so no filter is needed and the normal call to the LoginHandler is done serverside instead -->
						<button type="button" class="btn btn-success" onclick="location.href='?triggerlogin'"><xsl:value-of select="$i18n.Continue"/></button>
					</div>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddFileCollection">

		<div class="contentitem">
			<h1>
				<xsl:value-of select="$i18n.AddFileCollection.header" />
			</h1>
			
			<div class="alert alert-info mt-3" role="alert">
				<p><xsl:value-of select="$i18n.AddFileCollectionText"/></p>
			</div>
			
			<xsl:if test="validationException/validationError">
				<div class="alert alert-danger mt-3" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>
		</div>
	
		<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add" id="collection-form" enctype="multipart/form-data">
			<xsl:call-template name="manipulateFileCollection" />
			
			<xsl:if test="AllowsFileEncryption">
				<div class="contentitem">
					<h2><xsl:value-of select="$i18n.EncryptFileCollection"/></h2>
					
					<div class="form-check mt-3">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'encrypted'"/>
							<xsl:with-param name="id" select="'encrypted'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="encrypted">
							<xsl:value-of select="$i18n.EncryptFileCollectionText"/>
						</label>
					</div>
					
					<div id="encryption-wrapper" class="hidden">
						<xsl:if test="requestparameters/parameter/name = 'encrypted'">
							<xsl:attribute name="class"></xsl:attribute>
						</xsl:if>
						
						<xsl:if test="EncryptionText">
							<div class="alert alert-info mt-3" role="alert">
								<xsl:value-of select="EncryptionText" disable-output-escaping="yes"/>
							</div>
						</xsl:if>
						
						<div class="form-group mt-3">
							<label for="encryptionPassword">
								<xsl:value-of select="$i18n.EncryptionPassword" />
								<xsl:value-of select="$i18n.EncryptionMinLenght.Part1" />
								<xsl:value-of select="EncryptionPasswordLength" />
								<xsl:value-of select="$i18n.EncryptionMinLenght.Part2" />
							</label>
							
							<div class="d-flex">
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'encryptionPassword'" />
									<xsl:with-param name="id" select="'encryptionPassword'" />
									<xsl:with-param name="element" select="FileCollection" />
									<xsl:with-param name="class" select="'fifty form-control'" />
									<xsl:with-param name="maxlength" select="EncryptionPasswordLength"/>
									<xsl:with-param name="width" select="''"/>
								</xsl:call-template>
								
								<button id="generate-password" type="button" class="bigmarginleft btn btn-primary" data-length="{EncryptionPasswordLength}"><xsl:value-of select="$i18n.Generate"/></button>
								<button id="copy-password" type="button" data-copied="{$i18n.PasswordCopied}" class="bigmarginleft btn btn-secondary"><xsl:value-of select="$i18n.Copy"/></button>
							</div>
						</div>
					</div>
				</div>
			</xsl:if>
			
			<xsl:if test="AllowsCollaboration">
				<div id="allowsCollaborationItem" class="contentitem">
					<h2><xsl:value-of select="$i18n.AllowCollaborationItem" /></h2>
					
					<div class="alert alert-info mt-3">
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="allowSecureCollabMessage"/>						
						</xsl:call-template>
					</div>
					
					<div class="form-check mt-4">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'allowsCollaboration'"/>
							<xsl:with-param name="id" select="'allowsCollaboration'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="allowsCollaboration">
							<xsl:value-of select="$i18n.AllowsCollaboration"/>
						</label>
					</div>
				</div>
			</xsl:if>
			
			<xsl:if test="AllowsUserKeepDays">
				<div class="contentitem">
					<h2><xsl:value-of select="$i18n.AutomaticDelete"/></h2>
					
					<p>
						<xsl:value-of select="$i18n.UserKeepDaysInfo.part.1"/>
						<xsl:value-of select="DefaultKeepDays"/>
						<xsl:value-of select="$i18n.UserKeepDaysInfo.part.2"/>
						
					</p>
					
					<div class="form-check mt-3">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'setUserKeepDays'"/>
							<xsl:with-param name="id" select="'setUserKeepDays'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="setUserKeepDays">
							<xsl:value-of select="$i18n.SetUserKeepDays"/>
						</label>
					</div>
					
					<div id="userkeepdays-wrapper" class="hidden">
						<xsl:if test="requestparameters/parameter/name = 'setUserKeepDays'">
							<xsl:attribute name="class"></xsl:attribute>
						</xsl:if>
						
						<xsl:if test="UserKeepDaysText">
							<div class="alert alert-warning mt-3" role="alert">
								<xsl:value-of select="UserKeepDaysText" disable-output-escaping="yes"/>
							</div>
						</xsl:if>
						
						<div class="form-group mt-3">
							<label for="collectionKeepDays">
								<xsl:value-of select="$i18n.UserKeepDays" />
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="name" select="'collectionKeepDays'" />
								<xsl:with-param name="id" select="'collectionKeepDays'" />
								<xsl:with-param name="class" select="'form-control'" />
								<xsl:with-param name="type" select="'number'"/>
								<xsl:with-param name="min" select="'1'"/>
								<xsl:with-param name="max" select="UserKeepDaysMax"/>
								<xsl:with-param name="value" select="DefaultKeepDays"/>
								<xsl:with-param name="aria-describedby" select="'userKeepDaysHelp'"/>
								<xsl:with-param name="width" select="''"/>
							</xsl:call-template>
							
							<div class="text-muted small" id="userKeepDaysHelp">
								<xsl:value-of select="$i18n.MaximumNumberOfKeepDays"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="UserKeepDaysMax"/>
							</div>
						</div>
					</div>
				</div>
			</xsl:if>
			
			<div class="contentitem">
				<h2><xsl:value-of select="$i18n.AddFileCollectionAndGetLink"/></h2>
				
				<div class="mt-3"><xsl:value-of select="$i18n.AddFileCollectionFinishText"/></div>
			</div>

			<div class="d-flex">
				<button type="submit" class="btn btn-success ml-auto" data-uploading="{$i18n.Uploading}">
					<img src="{$imagePath}/spinner.gif" class="marginright" style="display: none;"/>
					
					<span>
						<xsl:value-of select="$i18n.addFileCollection.button"/>
					</span>
				</button>
			</div>
		</form>

	</xsl:template>

	<xsl:template match="UpdateFileCollection">

		<div class="contentitem">
			<h1>
				<xsl:value-of select="$i18n.UpdateFileCollection.header" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="FileCollection/name" />
			</h1>
			
			<xsl:if test="validationException/validationError">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>
		</div>
		
		<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{FileCollection/collectionID}" id="collection-form" enctype="multipart/form-data">
			<xsl:call-template name="manipulateFileCollection" />
			
			<xsl:if test="AllowsCollaboration">
				<div id="allowsCollaborationItem" class="contentitem">
					<h2><xsl:value-of select="$i18n.AllowCollaborationItem" /></h2>
					
					<div class="alert alert-info mt-3">
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="allowSecureCollabMessage"/>						
						</xsl:call-template>
					</div>
					
					<div class="form-check mt-4">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="name" select="'allowsCollaboration'"/>
							<xsl:with-param name="id" select="'allowsCollaboration'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="allowsCollaboration">
							<xsl:value-of select="$i18n.AllowsCollaboration"/>
						</label>
					</div>
				</div>
			</xsl:if>

			<div class="d-flex">
				<button type="submit" class="btn btn-success ml-auto" data-uploading="{$i18n.Uploading}">
					<img src="{$imagePath}/spinner.gif" class="marginright" style="display: none;"/>
					<span>
						<xsl:value-of select="$i18n.updateFileCollection.button"/>
					</span>
				</button>
			</div>
		</form>

	</xsl:template>

	<xsl:template name="manipulateFileCollection">

		<script src="{$scriptPath}/filecollection.js?v=7"></script>

		<div class="contentitem">
			<h2><xsl:value-of select="$i18n.NameCollection"/></h2>
			
			<div class="form-group mt-3">
				<label for="name">
					<xsl:value-of select="$i18n.name" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="$i18n.notRequiredField" />
				</label>
				
				<xsl:call-template name="createTextField">
					<xsl:with-param name="name" select="'name'" />
					<xsl:with-param name="id" select="'name'" />
					<xsl:with-param name="element" select="FileCollection" />
					<xsl:with-param name="class" select="'form-control'"/>
				</xsl:call-template>
			</div>
		</div>

		<div class="contentitem">
			<h2><xsl:value-of select="$i18n.UploadFiles" /></h2>
	
			<div class="alert alert-info mt-3" role="alert">
				<xsl:value-of select="$i18n.uploadLimit1"/>
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="/Document/MaxUploadSize"/>
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.uploadLimit2"/>
			</div>
	
			<xsl:apply-templates select="FileCollection/files/File" mode="manipulate"/>
		
			<div class="form-group mb-3">
				<label for="qloader"><xsl:value-of select="$i18n.SelectFile"/></label>
				
				<input type="file" class="form-control-file" multiple="multiple" name="file" id="qloader" data-imagepath="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics"/>
			</div>
		</div>
		
		<xsl:if test="AccessMode = 'ACCESS_INTERFACE' or AllowSecureCollections = 'true'">
			<div class="contentitem">
				<h2><xsl:value-of select="$i18n.AccessHeader"/></h2>

				<div class="form-check mt-3 mb-2">
					<xsl:call-template name="createRadio">
						<xsl:with-param name="name" select="'securityMode'"/>
						<xsl:with-param name="value" select="'PUBLIC'"/>
						<xsl:with-param name="id" select="'publicCollection'"/>
						<xsl:with-param name="element" select="FileCollection"/>
						<xsl:with-param name="class" select="'form-check-input'"/>
					</xsl:call-template>
					
					<label class="form-check-label" for="publicCollection">
						<xsl:value-of select="$i18n.PublicCollection"/>
					</label>
				</div>
				
				<xsl:if test="AllowSecureCollections = 'true'">
					<div class="form-check mb-2">
						<xsl:call-template name="createRadio">
							<xsl:with-param name="name" select="'securityMode'"/>
							<xsl:with-param name="value" select="'IDENTIFICATION'"/>
							<xsl:with-param name="id" select="'secureCollection'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="secureCollection">
							<xsl:value-of select="$i18n.SecureCollection"/>
						</label>
					</div>
				
					<div id="persons-wrapper" class="hidden">
						<h3><xsl:value-of select="$i18n.Persons"/></h3>
						
						<table class="table table-sm table-striped" id="person-table">
							<thead>
								<tr>
									<th width="40"></th>
									<th><xsl:value-of select="$i18n.CitizenIdentifier"/></th>
									<th width="140"></th>
								</tr>
							</thead>
							<tbody>
								<xsl:choose>
									<xsl:when test="requestparameters">
										<xsl:for-each select="requestparameters/parameter[name='citizenID']/value">
											<xsl:call-template name="person-row">
												<xsl:with-param name="citizenID" select="."/>
											</xsl:call-template>
										</xsl:for-each>
									</xsl:when>
									<xsl:when test="FileCollection/citizenIDs/citizenID">
										<xsl:for-each select="FileCollection/citizenIDs/citizenID">
											<xsl:call-template name="person-row">
												<xsl:with-param name="citizenID" select="."/>
											</xsl:call-template>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<tr id="no-persons-yet">
											<td colspan="3"><xsl:value-of select="$i18n.NoPersonsYet"/></td>
										</tr>
									</xsl:otherwise>
								</xsl:choose>
							</tbody>
							<tfoot>
								<tr>
									<td></td>
									<td>
										<input class="sixty form-control" type="text" id="citizenidentifier" placeholder="{$i18n.CitizenIdentifierFormatted}"/>
										<small class="text-muted form-text display-block"><xsl:value-of select="$i18n.CitizenIdentifierHelp"/></small>
									</td>
									<td class="text-align-center"><button id="add-person" class="btn btn-sm btn-success" type="button" data-url="{/Document/requestinfo/contextpath}/{/Document/module/alias}/validateperson" data-unknown="{$i18n.UnknownError}" data-duplicateid="{$i18n.DuplicateID}" data-invalidid="{$i18n.InvalidID}"><xsl:value-of select="$i18n.AddPerson"/></button></td>
								</tr>
							</tfoot>
						</table>
						
						<div class="hidden" id="person-error">
							
						</div>
						
						<br/>
						
						<script type="text/template" id="person-template">
							<tr>
								<td>
									<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/user.png"/>
									<input type="hidden" name="citizenID" value="$citizenID"/>
								</td>
								
								<td>$citizenID</td>
								
								<td class="text-align-right">
									<a href="#" class="delete-person">
										<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png"/>
									</a>
								</td>
							</tr>
						</script>
					</div>
				</xsl:if>
				
				<xsl:if test="AccessMode = 'ACCESS_INTERFACE'">
					<div class="form-check mb-2">
						<xsl:call-template name="createRadio">
							<xsl:with-param name="name" select="'securityMode'"/>
							<xsl:with-param name="value" select="'ACCESS'"/>
							<xsl:with-param name="id" select="'accessCollection'"/>
							<xsl:with-param name="element" select="FileCollection"/>
							<xsl:with-param name="class" select="'form-check-input'"/>
						</xsl:call-template>
						
						<label class="form-check-label" for="accessCollection">
							<xsl:choose>
								<xsl:when test="HideGroupAccess">
									<xsl:value-of select="$i18n.ImportedUsers"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.UsersAndGroups"/>
								</xsl:otherwise>
							</xsl:choose>
						</label>
					</div>
					
					<div id="user-group-wrapper" class="hidden">
						<xsl:choose>
							<xsl:when test="HideGroupAccess">
								<p><xsl:value-of select="$i18n.accessConfigDescription.hiddenGroupAccess"/></p>
						
								<xsl:call-template name="users"/>
							</xsl:when>
							<xsl:otherwise>
								<p><xsl:value-of select="$i18n.accessConfigDescription"/></p>
						
								<xsl:call-template name="groups"/>
								<xsl:call-template name="users"/>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</xsl:if>
			</div>
		</xsl:if>

	</xsl:template>
	
	<xsl:template name="groups">
	
		<div class="bold"><xsl:value-of select="$i18n.Groups"/></div>
		
		<div class="mb-3">
			<xsl:call-template name="GroupList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/groups</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'group'"/>
				<xsl:with-param name="groups" select="groups" />
				<xsl:with-param name="document" select="/Document" />
				<xsl:with-param name="useExternalIcons" select="true()"/>
				<xsl:with-param name="searchfieldclass" select="'form-control'"/>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<xsl:template name="users">
	
		<div class="bold"><xsl:value-of select="$i18n.Users"/></div>
		
		<div class="mb-3">
			<xsl:call-template name="UserList">
				<xsl:with-param name="connectorURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/users</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="name" select="'user'"/>
				<xsl:with-param name="users" select="users" />
				<xsl:with-param name="document" select="/Document" />
				<xsl:with-param name="showUsername" select="true()" />
				<xsl:with-param name="useExternalIcons" select="true()"/>
				<xsl:with-param name="searchfieldclass" select="'form-control'"/>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<xsl:template name="person-row">
		
		<xsl:param name="citizenID"/>
	
		<tr>
			<td>
				<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/user.png"/>
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="'citizenID'"/>
					<xsl:with-param name="value" select="$citizenID"/>
				</xsl:call-template>
			</td>
			
			<td><xsl:value-of select="$citizenID"/></td>
			
			<td class="text-align-right">
				<a href="#" class="delete-person">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png"/>
				</a>
			</td>
		</tr>
		
	</xsl:template>

	<xsl:template match="File" mode="manipulate">

		<div class="d-flex align-items-center mb-2">
			<img src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/floppy.png" class="mr-1"/>
		
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/download/{fileID}/{alias}" class="mr-1">
				<xsl:value-of select="name" />
			</a>
			
			<small>
				<xsl:text>(</xsl:text>
				<xsl:value-of select="FormatedSize" />
				<xsl:text>)</xsl:text>
			</small>
			
			<div class="ml-auto">
				<div class="form-check">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="name" select="'deletefile'" />
						<xsl:with-param name="id" select="concat('deletefile-', fileID)" />
						<xsl:with-param name="value" select="fileID" />
						<xsl:with-param name="class" select="'form-check-input'"/>
					</xsl:call-template>
					
					<label class="form-check-label" for="deletefile-{fileID}">
						<xsl:value-of select="$i18n.deleteFileText"/>
					</label>
				</div>
			</div>
		</div>
		
	</xsl:template>

	<xsl:template match="validationError">
	
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<div>
				<i class="icons icon-warning" aria-hidden="true"/>
				
				<span>
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validation.requiredField" />
						</xsl:when>
						<xsl:when test="validationErrorType = 'InvalidFormat'">
							<xsl:value-of select="$i18n.validation.invalidFormat" />
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooShort'">
							<xsl:value-of select="$i18n.validation.tooShort" />
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validation.tooLong" />
						</xsl:when>														
						<xsl:otherwise>
							<xsl:value-of select="$i18n.validation.unknownError" />
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:text>&#x20;</xsl:text>
					
					<xsl:choose>
						<xsl:when test="fieldName = 'name'">
							<xsl:value-of select="$i18n.name"/>
						</xsl:when>
						<xsl:when test="fieldName = 'deletetionDate'">
							<xsl:value-of select="$i18n.deletetionDate"/>
						</xsl:when>
						<xsl:when test="fieldName = 'citizenID'">
							<xsl:value-of select="$i18n.CitizenIdentifier"/>
						</xsl:when>
						<xsl:when test="fieldName = 'encryptionPassword'">
							<xsl:value-of select="$i18n.EncryptionPassword"/>
						</xsl:when>
						<xsl:when test="fieldName = 'password'">
							<xsl:value-of select="$i18n.Password"/>
						</xsl:when>
						<xsl:when test="fieldName = 'collectionKeepDays'">
							<xsl:value-of select="$i18n.UserKeepDays"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="fieldName"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</div>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<div>
				<i class="icons icon-warning" aria-hidden="true"/>
				
				<span>
					<xsl:choose>
						<xsl:when test="messageKey = 'RequestedFileCollectionNotFound'">
							<xsl:value-of select="$i18n.requestedFileCollectionNotFound"/><xsl:text>!</xsl:text>
						</xsl:when>				
						<xsl:when test="messageKey = 'UpdateFailedFileCollectionNotFound'">
							<xsl:value-of select="$i18n.updateFailedFileCollectionNotFound"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey = 'DeleteFailedFileCollectionNotFound'">
							<xsl:value-of select="$i18n.deleteFailedFileCollectionNotFound"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey = 'FileSizeLimitExceeded'">
							<xsl:value-of select="$i18n.fileSizeLimitExceeded"/><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey = 'InvalidSecurityMode'">
							<xsl:value-of select="$i18n.InvalidSecurityMode"/>
						</xsl:when>
						<xsl:when test="messageKey = 'InvalidPassword'">
							<xsl:value-of select="$i18n.InvalidPassword"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.unknownFault"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</div>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>