<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="imgPath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics</xsl:variable>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/featherlight.min.js
		/js/datatables.min.js
		/js/flowcatalog.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/flowengine.css
		/css/featherlight.min.css
		/css/datatables.css
		/css/flowcatalog.css
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
		
		<xsl:choose>
			<xsl:when test="not(Repositories/Repository)">
				<span><strong><xsl:value-of select="$i18n.ListRepositories.NoRepositoriesFound" /></strong></span>
			</xsl:when>
			<xsl:otherwise>
			
				<div id="table-description" class="floatleft">
					<p><xsl:value-of select="$i18n.ListRepositories.Description"/></p>
				</div>
				
				<div id="table-searchbuttons" class="floatright bigmarginleft">
					<label>
						<xsl:value-of select="$i18n.FilterRepository" />
						
						<xsl:variable name="options">
							<xsl:for-each select="Repositories/Repository">
								<option>
									<name><xsl:value-of select="RepositoryConfiguration/name"/></name>
									<value><xsl:value-of select="RepositoryIndex"/></value>
								</option>
							</xsl:for-each>
						</xsl:variable>
						
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'table-filterrepository'" />
							<xsl:with-param name="class" select="'marginleft'" />
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="element" select="exsl:node-set($options)/option" />
							<xsl:with-param name="addEmptyOption" select="$i18n.FilterRepository.None"/>
						</xsl:call-template>
						
					</label>
				</div>
				
				<table id="catalogtable" class="stripe display">
					<thead>	
						<tr>
							<th><xsl:value-of select="$i18n.Column.FlowName" /></th>
							<th><xsl:value-of select="$i18n.Column.Source" /></th>
							<th><xsl:value-of select="$i18n.Column.Repository" /></th>
							<th><xsl:value-of select="$i18n.Column.Versions" /></th>
							<th><xsl:value-of select="$i18n.Column.LastModified" /></th>
							<th/><!-- repositoryID -->
							<th/><!-- sourceID -->
							<th/><!-- flowfamilyID -->
						</tr>
					</thead>
					<tbody/>
				</table>
				
				<xsl:variable name="ajaxURL">
					<xsl:value-of select="/Document/requestinfo/currentURI"/>
					<xsl:text>/</xsl:text>
					<xsl:value-of select="/Document/module/alias"/>
					<xsl:text>/flows</xsl:text>
				</xsl:variable>
				
				<script type="text/javascript">
					$(document).ready(function() {
					
						var table = $('#catalogtable').DataTable({
							ajax: {
								url: '<xsl:value-of select="$ajaxURL"/>',
								dataSrc: 'rows',
								error: function(jqXHR, textStatus, errorThrown) {
									
									if (jqXHR.status == 401) { // Unauthenticated, 
										alert("Du har blivit utloggad, vänligen ladda om sidan eller logga in i en annan flik och försök igen.");
										
									} else if (jqXHR.status == 403) { // Forbidden
										alert("Du har inte åtkomst till den här funktionen, är du inloggad som rätt användare?");
									
									}  else if (jqXHR.status == 500) { // Internal Server Error
										alert("Ett server fel har uppstått, vänligen försök igen senare.");
									
									} else {
										alert("Ett okänt fel har uppstått, vänligen försök igen senare.");
									}
									
									if (console != undefined) {
										console.error("Ajax error: " + jqXHR.status + " " + textStatus + ", " + errorThrown);
									}
								}
							},
							deferRender: true,
							columns: [
								{ name: "name", type: "text",
									render: function(data, type, row) {
									
										if (type == "display") {
											return '<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/family/' + row[5] +  '/' + row[6] +  '/' + row[7] +  '">' + data + '</a>';
										}
										
										return data;
									}
								},
								{ name: "source", type: "text" },
								{ name: "repository", type: "text",
									render: function(data, type, row) {
									
										if (type == "display") {
											return data + '<a class="marginleft" onclick="showRepositoryDescription(' + row[5] + ', event)"><img src="{$imgPath}/info.png" /></a>';
										}
										
										return data;
									}
								},
								{ name: "versions", sClass: "text-align-right", type: "num-fmt" },
								{ name: "lastModified", sClass: "text-align-right", type: "date" },
								{ name: "repositoryID", visible: false },
								{ name: "sourceID", visible: false, searchable: false },
								{ name: "flowFamilyID", visible: false, searchable: false }
						  ],
						  order: [[ 4, 'desc' ]],
						  dom: 'frtipl',
						  responsive: true,
						  bAutoWidth: false,
							paging: true,
							pagingType: 'simple_numbers',
							pageLength: 15,
							lengthMenu: [ 15, 25, 35, 50, 100, 500 ],
							language: {
								"decimal":        "<xsl:value-of select="$i18n.DataTable.decimal"/>",
								"emptyTable":     "<xsl:value-of select="$i18n.DataTable.emptyTable"/>",
								"info":           "<xsl:value-of select="$i18n.DataTable.info"/>",
								"infoEmpty":      "<xsl:value-of select="$i18n.DataTable.infoEmpty"/>",
								"infoFiltered":   "<xsl:value-of select="$i18n.DataTable.infoFiltered"/>",
								"infoPostFix":    "<xsl:value-of select="$i18n.DataTable.infoPostFix"/>",
								"thousands":      " ",
								"lengthMenu":     "<xsl:value-of select="$i18n.DataTable.lengthMenu"/>",
								"loadingRecords": "<xsl:value-of select="$i18n.DataTable.loadingRecords"/>",
								"processing":     "<xsl:value-of select="$i18n.DataTable.processing"/>",
								"search":         "<xsl:value-of select="$i18n.DataTable.search"/>",
								"zeroRecords":    "<xsl:value-of select="$i18n.DataTable.zeroRecords"/>",
								"paginate": {
									"first":      "<xsl:value-of select="$i18n.DataTable.paginate.first"/>",
									"last":       "<xsl:value-of select="$i18n.DataTable.paginate.last"/>",
									"next":       "<xsl:value-of select="$i18n.DataTable.paginate.next"/>",
									"previous":   "<xsl:value-of select="$i18n.DataTable.paginate.previous"/>"
								},
								"aria": {
									"sortAscending":  "<xsl:value-of select="$i18n.DataTable.aria.sortAscending"/>",
									"sortDescending": "<xsl:value-of select="$i18n.DataTable.aria.sortDescending"/>"
								}
							},
							stateSave: true,
							stateSaveParams: function (settings, data) {
								data.order = undefined;
								data.search.search = "";
		    				
		    				data.custom = {};
		    				data.custom.filterRepository = $("#table-filterrepository").val();
		  				},
		  				stateLoadParams: function (settings, data) {
		  					
		  					if (data != undefined) {
									
									data.order = undefined;
									data.search.search = "";
									
									data.columns.forEach(function(column){
										column.visible = undefined;
									});
			    				
			    				if (data.custom != undefined) {
			    				
				    				if (data.custom.filterRepository != undefined) {
				    				
					    				var select = $("#table-filterrepository");
					    				select.val(data.custom.filterRepository);
				    				}
			    				}
		    				}
		  				},
		  				initComplete: function(settings, json){
			        
			        	filterOnRepositories(false);
			        	
			        	var api = this.api();
		  					api.draw();
		  					api.responsive.recalc();
			        }
						});
						
						$("#catalogtable_wrapper").prepend($("#table-description"));
						$("#catalogtable_filter").after($("#table-searchbuttons"));
						
						$("#catalogtable").on( 'click', 'tbody td a', function(event) {
							event.stopPropagation();
						});
						
						$("#catalogtable").on( 'click', 'tbody td', function(event) {
						
							var td = $(this);
							var tr = td.parent();
							
							if (tr.children("td").first().hasClass("dataTables_empty")) {
								return;
							}
							
							if (tr.closest("table").hasClass("collapsed") &amp;&amp; td.is(":first-child")) {
								return;
							}
							
							event.stopPropagation();
							
							var repositoryID = table.cell(tr, "repositoryID:name").data();
							var sourceID = table.cell(tr, "sourceID:name").data();
							var flowFamilyID = table.cell(tr, "flowFamilyID:name").data();
							
							window.location.href = "<xsl:value-of select="/Document/requestinfo/url"/>/family/" + repositoryID + "/" + sourceID + "/" + flowFamilyID;
						});
						
						var filterOnRepositories = function(render) {
							
							var select = $("#table-filterrepository");
							var repository = select.val();
							var column = table.column(["repositoryID:name"]);
							
							if (repository != "") {
							
								column.search(repository, false, false, false);
										
							} else {
								
								column.search("");
							}
							
							if (render) {
								table.draw();
								table.responsive.recalc();
							}
						};
						
						$("#table-filterrepository").on('change', filterOnRepositories);
					});
				</script>
				
				<div id="repository-modal-template" style="display: none;">
					
					<div class="repository-modal contentitem">
						<div class="modal-content">
						
							<div class="modal-header bigmarginbottom">
								<h1/>
							</div>
							
							<div class="modal-body">
							
								<p class="description floatleft full"/>
								
								<input class="close bigmargintop floatright" type="button" value="{$i18n.Close}" />
								
							</div>
							
						</div>
					</div>
				
				</div>
			
				<div style="display: none;">
					<xsl:apply-templates select="Repositories/Repository[not(Missing)]"/>
				</div>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="Repository">
	
		<div id="repository-{RepositoryIndex}">
			<h2 class="name"><xsl:value-of select="RepositoryConfiguration/name"/></h2>
			
			<div class="description">
				<xsl:value-of select="RepositoryConfiguration/description" disable-output-escaping="yes"/>
			</div>
		</div>
	
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

	<xsl:template match="validationError[messageKey='SharedFlowAlreadyExists']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FlowAlreadyExists"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[fieldName='flowXML' and validationErrorType='TooLong']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FileSizeLimitExceeded"/>
		</p>
	</xsl:template>
	
	<xsl:template match="validationError[fieldName='flowXML' and validationErrorType='RequiredField']">
		<p class="error">
			<xsl:value-of select="$i18n.ValidationError.FileUploadException"/>
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