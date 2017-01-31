<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/flowsharing.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowsharing.css
		/css/flowengine.css
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="FlowBrowser" class="contentitem errands-wrapper">
			<xsl:apply-templates select="ListRepositories" />
			<xsl:apply-templates select="ListFlowVersions" />
			<xsl:apply-templates select="ExportFlow" />
			<xsl:apply-templates select="DownloadError" />
			<xsl:apply-templates select="ShareFlow" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="ListRepositories">
	
		<h1><xsl:value-of select="$i18n.ListRepositories.Title" /></h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError"/>
		
		<xsl:for-each select="Repositories/Repository[Missing]">
			<xsl:call-template name="ErrorConnectingToRepository"/>
		</xsl:for-each>
		
		<p>
			<xsl:value-of select="$i18n.ListRepositories.Description" />
		</p>
		
		<xsl:choose>
			<xsl:when test="not(Repositories/Repository)">
				<span><strong><xsl:value-of select="$i18n.ListRepositories.NoRepositoriesFound" /></strong></span>
			</xsl:when>
			<xsl:otherwise>
			
				<xsl:apply-templates select="Repositories/Repository[not(Missing)]"/>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="Repository">
	
		<div class="bigmarginbottom">
		
			<h2><xsl:value-of select="RepositoryConfiguration/name"/></h2>
			
			<div class="bigmarginleft">
				<xsl:value-of select="RepositoryConfiguration/description" disable-output-escaping="yes"/>
			</div>
			
			<table id="flowlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
				<thead class="sortable">	
					<tr>
						<th class="default-sort"><xsl:value-of select="$i18n.Column.FlowName" /></th>
						<th><xsl:value-of select="$i18n.Column.Source" /></th>
						<th><xsl:value-of select="$i18n.Column.Versions" /></th>
						<th><xsl:value-of select="$i18n.Column.LastModified" /></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="not(SharedFlows/SharedFlow)">
							<tr>
								<td colspan="4">
									<strong>
										<xsl:value-of select="$i18n.ListRepositories.NoFlowsFound" />
									</strong>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							
							<xsl:apply-templates select="SharedFlows/SharedFlow" mode="list"/>
							
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
			
			<br/>
		</div>
	
	</xsl:template>
	
	<xsl:template match="SharedFlow" mode="list">
		
		<tr>
			<td data-title="{$i18n.Column.FlowName}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/{../../RepositoryIndex}/{Source/sourceID}/{flowFamilyID}"><xsl:value-of select="name" /></a>
			</td>
			<td data-title="{$i18n.Column.Source}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/{../../RepositoryIndex}/{Source/sourceID}/{flowFamilyID}"><xsl:value-of select="Source/name" /></a>
			</td>
			<td data-title="{$i18n.Column.Versions}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/{../../RepositoryIndex}/{Source/sourceID}/{flowFamilyID}"><xsl:value-of select="familySize" /></a>
			</td>
			<td data-title="{$i18n.Column.LastModified}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/{../../RepositoryIndex}/{Source/sourceID}/{flowFamilyID}"><xsl:value-of select="added" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ListFlowVersions">
	
		<h1>
			<xsl:value-of select="$i18n.ListFlowVersions.Title" />
			<xsl:text>: </xsl:text>
			<xsl:value-of select="SharedFlows/SharedFlow[position() = last()]/name"/>
		</h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError"/>

		<p><xsl:value-of select="$i18n.ListFlowVersions.Description"/></p>
		
		<table id="flowversionlist" class="full coloredtable sortabletable oep-table" cellspacing="0">
			<thead class="sortable">	
				<tr>
					<th><xsl:value-of select="$i18n.Column.FlowName" /></th>
					<th><xsl:value-of select="$i18n.Column.Source" /></th>
					<th class="default-sort"><xsl:value-of select="$i18n.Column.Version" /></th>
					<th><xsl:value-of select="$i18n.Column.LastModified" /></th>
					<th><xsl:value-of select="$i18n.Column.Comment" /></th>
					<th width="40" class="no-sort"></th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="SharedFlows/SharedFlow" mode="list-versions"/>
			</tbody>
		</table>
		
		<br/>
		
		<div class="floatleft marginleft bigmargintop">
			<a class="btn btn-blue btn-inline" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" title="{$i18n.Back}">
				<xsl:value-of select="$i18n.Back" />
			</a>
		</div>
	
	</xsl:template>
	
	<xsl:template match="SharedFlow" mode="list-versions">
		
		<tr>
			<td data-title="{$i18n.Column.FlowName}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/import/{../../RepositoryIndex}/{sharedFlowID}"><xsl:value-of select="name" /></a>
			</td>
			<td data-title="{$i18n.Column.Source}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/import/{../../RepositoryIndex}/{sharedFlowID}"><xsl:value-of select="Source/name" /></a>
			</td>
			<td data-title="{$i18n.Column.Version}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/import/{../../RepositoryIndex}/{sharedFlowID}"><xsl:value-of select="version" /></a>
			</td>
			<td data-title="{$i18n.Column.LastModified}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/import/{../../RepositoryIndex}/{sharedFlowID}"><xsl:value-of select="added" /></a>
			</td>
			<td data-title="{$i18n.Column.Comment}">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/import/{../../RepositoryIndex}/{sharedFlowID}"><xsl:value-of select="comment" /></a>
			</td>
			<td>
				<a class="floatleft marginright" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/download/{../../RepositoryIndex}/{sharedFlowID}" title="{$i18n.Download.Title} {version}: {name}">
					<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/download.png" alt="" />
				</a>
				
				<xsl:choose>
					<xsl:when test="../../DeleteAccess">
					
						<xsl:variable name="name">
							<xsl:call-template name="escapeForJavascript">
								<xsl:with-param name="text" select="name"/>
							</xsl:call-template>
						</xsl:variable>

						<a class="floatleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{../../RepositoryIndex}/{sharedFlowID}" onclick="return confirm('{$i18n.DeleteFlowConfirm} {version}: {$name}?');" title="{$i18n.DeleteFlow.title} {version}: {name}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" alt="" />
						</a>

					</xsl:when>										
					<xsl:otherwise>

						<a class="floatleft" href="#" onclick="alert('{$i18n.DeleteFlowDisabledNoAccess}'); return false;" title="{$i18n.DeleteFlowDisabledNoAccess}">
							<img class="alignbottom" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete_gray.png" alt="" />
						</a>

					</xsl:otherwise>					
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShareFlow">
	
		<h1>
			<xsl:value-of select="$i18n.ShareFlow.Title" />
			<xsl:text>:&#160;</xsl:text>
			<xsl:value-of select="Flow/name" />
		</h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError"/>
		
		<xsl:for-each select="MissingRepository">
			<xsl:call-template name="ErrorConnectingToRepository"/>
		</xsl:for-each>
		
		<p>
			<xsl:value-of select="$i18n.ShareFlow.Description" />
		</p>
		
		<xsl:choose>
			<xsl:when test="not(Repositories/Repository)">
			
				<span><strong><xsl:value-of select="$i18n.ShareFlow.NoRepositoriesWithAccessFound" /></strong></span>
				
			</xsl:when>
			<xsl:otherwise>
			
			<form method="post" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/share/{Flow/flowID}">
			
				<div class="floatleft full bigmarginbottom">
					
					<label for="repositoryIndex" class="floatleft full">
						<xsl:value-of select="$i18n.ShareFlow.SelectRepository" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'shareflow-repositoryindex'"/>
							<xsl:with-param name="name" select="'repositoryIndex'"/>
							<xsl:with-param name="element" select="Repositories/Repository"/>
							<xsl:with-param name="labelElementName" select="'Name'"/>
							<xsl:with-param name="valueElementName" select="'RepositoryIndex'"/>
						</xsl:call-template>
					</div>
				</div>
				
				<div id="descriptions" class="floatleft full">
				
					<xsl:for-each select="Repositories/Repository">
						<div data-repoindex="{RepositoryIndex}">
							<xsl:value-of select="UploadDescription" disable-output-escaping="yes"/>
						</div>
					</xsl:for-each>
					
				</div>
				
				<div class="floatleft full bigmarginbottom">
					
					<label for="comment" class="floatleft full">
						<xsl:value-of select="$i18n.ShareFlow.Comment" />
					</label>
					
					<div class="floatleft full">
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'comment'"/>
							<xsl:with-param name="name" select="'comment'"/>
							<xsl:with-param name="maxlength" select="'65535'"/>
							<xsl:with-param name="rows" select="'4'"/>
						</xsl:call-template>
					</div>
				</div>
				
				<div class="floatright marginright bigmargintop">
					<input type="submit" value="{$i18n.Submit}" title="{$i18n.Submit}"/>
				</div>
			
			</form>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="ErrorConnectingToRepository">
	
		<p class="error">
			<xsl:value-of select="$i18n.ErrorConnectingToRepository.1" />
			<xsl:text>&#160;</xsl:text>
			
			<xsl:choose>
				<xsl:when test="RepositoryConfiguration/name">
					<xsl:value-of select="RepositoryConfiguration/name" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="RepositoryConfiguration/url" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:value-of select="$i18n.ErrorConnectingToRepository.2" />
		</p>
	
	</xsl:template>
	
	<xsl:template match="DownloadError">
	
		<h1><xsl:value-of select="$i18n.DownloadError.Title" /></h1>
		
		<xsl:apply-templates select="ValidationErrors/validationError"/>
		
		<p>
<!-- 			<xsl:value-of select="$i18n.DownloadError.Description" /> -->
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowFamilyNotFound']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FlowFamilyNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='FlowNotFound']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FlowNotFound"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='AccessDenied']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.AccessDenied"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnknownRemoteError']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.UnknownRemoteError"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='RepositoryCommunicationFailed']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.RepositoryCommunicationFailed"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='ErrorExportingFlow']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.ErrorExportingFlow"/>
		</p>
	</xsl:template>

	<xsl:template match="validationError[messageKey='FlowAlreadyExists']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FlowAlreadyExists"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.validation.requiredField" />
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
					<xsl:when test="fieldName = 'fromDate'">
<!-- 						<xsl:value-of select="$i18n.fromDate"/> -->
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:value-of select="$i18n.validation.unknownFault" />
			</p>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>