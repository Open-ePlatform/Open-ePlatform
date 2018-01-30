<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/flowengine.tablesorter.js
		/js/flowengine.helpdialog.js
		/js/flowengine.tablefilter.js
		/js/texttagadminmodule.js
		/js/texttagadmin.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/flowengine.css
	</xsl:variable>

	<xsl:template match="Document">	
			
		<div id="TextTagAdminModule" class="contentitem errands-wrapper">
			
			<xsl:apply-templates select="ListTextTags"/>
			<xsl:apply-templates select="AddTextTag"/>
			<xsl:apply-templates select="UpdateTextTag"/>
				
		</div>
			
	</xsl:template>
	
	<xsl:template match="ListTextTags">
		
		<xsl:apply-templates select="validationError"/>

		<form method="POST" action="{/Document/requestinfo/currentURI}/{/Document/module/alias}/share">

			<section class="settings">
				
				<div class="heading-wrapper">
				
					<div class="floatright bigmarginright" style="margin-top: 15px; margin-right: 35px;">
						<label for="tag-filter-input" class="bigmarginright" style="margin-top: 15px;"><xsl:value-of select="$i18n.Filter" />:</label>
						<input type="text" size="20" name="tag-filter-input" class="filter-input" data-tableid="taglist" />
					</div>			
				
					<h1>
						<xsl:value-of select="/Document/module/name" />
					</h1>
					
					<p class="description"><xsl:value-of select="$i18n.TextTagDescription" /></p>
					
				</div>
				
				<xsl:variable name="textTagCount" select="count(TextTags/TextTag)" />
				
				<div class="errands-wrapper draft">
					
					<div class="heading-wrapper">
						<h3>
							<xsl:value-of select="$i18n.TextTagTitle.Part1" />
							<xsl:text>&#160;</xsl:text>
							
							<strong>
								<xsl:value-of select="$textTagCount" />
								<xsl:text>&#160;</xsl:text>
							</strong>
							
							<xsl:choose>
								<xsl:when test="$textTagCount > 1">
									<xsl:value-of select="$i18n.TextTagTitle.Part2.Plural" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$i18n.TextTagTitle.Part2" />
								</xsl:otherwise>
							</xsl:choose>
						</h3>
						
						<div class="help">
							<a class="open-help" href="#" data-icon-after="?" data-help-box="helpdialog"><span><xsl:value-of select="$i18n.Help" /></span></a>
							<div class="help-box" data-help-box="helpdialog">
								<div>
						  			<div> 
						  				<a class="close" href="#" data-icon-after="x"></a>
						  				<xsl:value-of select="$i18n.TextTagAdminHelp" />
						  			</div> 
								</div>
							</div>
						</div>
			
						<div class="help-backdrop" data-help-box="helpdialog" />
						
					</div>
					<table class="full oep-table" cellspacing="0" id="taglist">
						<thead class="sortable">
							<tr>
								<th class="icon no-sort"></th>
								<th class="id default-sort">
									<span><xsl:value-of select="$i18n.Name" /></span>
								</th>
								<th class="description">
									<span><xsl:value-of select="$i18n.Description" /></span>
								</th>
								<th class="link no-sort" />
							</tr>
						</thead>
						<tbody>
							
							<xsl:choose>
								<xsl:when test="TextTags/TextTag">
									<xsl:apply-templates select="TextTags/TextTag" mode="list" />
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td />
										<td colspan="3"><xsl:value-of select="$i18n.NoTextTags" /></td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
							
						</tbody>
					</table>	
						
					<xsl:apply-templates select="TextTags/TextTag" mode="preview" />					
						
				</div>
				
				<article class="buttons">
			
					<a class="btn btn-green btn-right" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add"><xsl:value-of select="$i18n.AddTextTag" /></a>
					
					<xsl:if test="TagSharingTargets and TextTags">
					
						<a class="btn btn-blue btn-right disabled open-help" id="share-button" data-help-box="tag-sharing" style="margin-right: 2px" href="#" onclick="checkState(event);"><xsl:value-of select="$i18n.ShareTextTags" /></a>
					
					</xsl:if>
			
				</article>
				
			</section>
	
			<div class="help-box" data-help-box="tag-sharing">
				<div>
		  			<div class="no-sections"> 
		  				<a class="close" href="#" data-icon-after="x"></a>
		  				
		  				<h1><xsl:value-of select="$i18n.SelectTargets.Title" /></h1>
		  				
		  				<p><xsl:value-of select="$i18n.SelectTargets.Description" /></p>
		  				
	 					<xsl:apply-templates select="TagSharingTargets/TagSharingTarget"/>
	  					
	  					<h2 class="bigmargintop"><xsl:value-of select="$i18n.SelectTargets.Settings" /></h2>
	  					
						<input type="checkbox" name="overwrite" id="overwrite" value="true" style="display: inline-block;"/>
	
						<xsl:text> </xsl:text>
						
						<label for="overwrite" style="display: inline-block !important;">
							<xsl:value-of select="$i18n.SelectTargets.Overwrite"/>
						</label>
						
						<br/>
	  						
	  					<input type="submit" id="share-submit-button" disabled="true" class="floatright btn btn-green bigmargintop" value="{$i18n.SelectTargets.Submit}"/>
		  				
		  			</div> 
				</div>
			</div>
		
		</form>

	</xsl:template>
	
	<xsl:template match="TextTag" mode="list">
	
		<tr>
			<td>
				<xsl:if test="../../TagSharingTargets">
					<input type="checkbox" class="tag" name="textTagID" value="{textTagID}" style="display: inherit;"/>
				</xsl:if>
			</td>
			<td data-title="{$i18n.Name}" class="name"><xsl:value-of select="name" /></td>
			<td data-title="{$i18n.Description}" class="description"><xsl:value-of select="description" /></td>
			<td class="link">
				<a class="open-help btn btn-blue vertical-align-middle" data-help-box="tag-preview-{textTagID}" href="#"><xsl:value-of select="$i18n.Preview" /></a>
				<a class="btn btn-green vertical-align-middle" style="margin-left: 2px" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{name}"><xsl:value-of select="$i18n.Update" /></a>
				<a class="btn btn-red vertical-align-middle" style="margin-left: 2px" data-icon-before="x" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{name}" onclick="return confirm('{$i18n.DeleteTextTagConfirm}: {name}?');"></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="TextTag" mode="preview">
	
		<div class="help-box" data-help-box="tag-preview-{textTagID}">
			<div>
	  			<div> 
	  				<a class="close" href="#" data-icon-after="x"></a>
	  				<xsl:value-of select="defaultValue" disable-output-escaping="yes"/>
	  			</div> 
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="TagSharingTarget">
	
			<input type="checkbox" name="target" id="target{position()}" value="{name}" style="display: inline-block;"/>
			
			<xsl:text> </xsl:text>
			
			<label for="target{position()}" style="display: inline-block !important;">
				<xsl:value-of select="name"/>
			</label>
			
			<br/>
	
	</xsl:template>
	
	<xsl:template match="AddTextTag">

		<xsl:apply-templates select="validationException/validationError"/>

		<section class="settings">
			<div class="heading-wrapper">
				<h1>
					<xsl:value-of select="$i18n.AddTextTag" />
				</h1>
			</div>
			
			<form method="POST" action="{/document/requestinfo/uri}" name="textTagForm">
				
				<xsl:call-template name="textTagForm" />
			
				<article class="buttons">
					<input type="submit" value="{$i18n.Add}" class="btn btn-green btn-inline" />
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel" /></a>
				</article>
			
			</form>
			
		</section>
		
	</xsl:template>	
	
	<xsl:template match="UpdateTextTag">

		<xsl:apply-templates select="validationException/validationError"/>

		<section class="settings">
			<div class="heading-wrapper">
				<h1>
					<xsl:value-of select="$i18n.UpdateTextTag" />
				</h1>
			</div>
			
			<form method="POST" action="{/document/requestinfo/uri}" name="textTagForm">
				
				<xsl:call-template name="textTagForm">
					<xsl:with-param name="textTag" select="TextTag" />
				</xsl:call-template>
			
				<article class="buttons">
					<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline" />
					<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel" /></a>
				</article>
			
			</form>
			
		</section>
		
	</xsl:template>
	
	<xsl:template name="textTagForm">
		
		<xsl:param name="textTag" select="null" />

		<article class="clearfix">
			
			<fieldset>
				<div class="split">
					<label for="name" class="required">
						<xsl:value-of select="$i18n.Name" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'" />
						<xsl:with-param name="name" select="'name'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$textTag" />
					</xsl:call-template>
				</div>
				<div class="split odd">
					<label for="description">
						<xsl:value-of select="$i18n.Description" />
					</label>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'description'" />
						<xsl:with-param name="name" select="'description'" />
						<xsl:with-param name="size" select="40" />
						<xsl:with-param name="element" select="$textTag" />
					</xsl:call-template>
				</div>
			</fieldset>
			<fieldset>
				<div class="split">
					<label for="type" class="required">
						<xsl:value-of select="$i18n.TextTagType" />
					</label>
					<select id="type" name="type">
						<option value="TEXTFIELD">
							<xsl:choose>
								<xsl:when test="requestparameters">
									<xsl:if test="requestparameters/parameter[name='type']/value = 'TEXTFIELD'"><xsl:attribute name="selected"/></xsl:if>
								</xsl:when>
								<xsl:when test="$textTag/type = 'TEXTFIELD'">
									<xsl:attribute name="selected"/>
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="$i18n.TextField" />
						</option>
						<option value="EDITOR">
							<xsl:choose>
								<xsl:when test="requestparameters">
									<xsl:if test="requestparameters/parameter[name='type']/value = 'EDITOR'"><xsl:attribute name="selected"/></xsl:if>
								</xsl:when>
								<xsl:when test="$textTag/type = 'EDITOR'">
									<xsl:attribute name="selected"/>
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="$i18n.Editor" />
						</option>
					</select>
				</div>
			</fieldset>
			<fieldset>
					
				<label for="defaultValue">
					<xsl:value-of select="$i18n.DefaultValue" />
				</label>
					
				<div id="editor-wrapper" class="hidden">
					
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'defaultValue'"/>
						<xsl:with-param name="name" select="'defaultValue'"/>
						<xsl:with-param name="class" select="'defaultValue-ckeditor'"/>
						<xsl:with-param name="element" select="$textTag" />
					</xsl:call-template>
					
					<xsl:call-template name="initializeFCKEditor">
						<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
						<xsl:with-param name="customConfig">config.js</xsl:with-param>
						<xsl:with-param name="editorContainerClass">defaultValue-ckeditor</xsl:with-param>
						<xsl:with-param name="editorHeight">150</xsl:with-param>
						<xsl:with-param name="contentsCss">
							<xsl:if test="/Document/cssPath">
								<xsl:value-of select="/Document/cssPath"/>
							</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
				
				</div>
				
				<div id="textfield-wrapper" class="hidden">
					
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'defaultValue'" />
						<xsl:with-param name="name" select="'defaultValue'" />
						<xsl:with-param name="element" select="$textTag" />
					</xsl:call-template>
					
				</div>
							
			</fieldset>
			
		</article>

		<div class="divider"></div>

	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			
			<section class="modal error">
				<span data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.validation.requiredField" />
						</xsl:when>
						<xsl:when test="validationErrorType='InvalidFormat'">
							<xsl:value-of select="$i18n.validation.invalidFormat" />
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
						<xsl:when test="fieldName = 'name'">
							<xsl:value-of select="$i18n.name"/>
						</xsl:when>
						<xsl:when test="fieldName = 'description'">
							<xsl:value-of select="$i18n.description"/>
						</xsl:when>
						<xsl:when test="fieldName = 'type'">
							<xsl:value-of select="$i18n.textTagType"/>
						</xsl:when>
						<xsl:when test="fieldName = 'defaultValue'">
							<xsl:value-of select="$i18n.defaultValue"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="fieldName"/>
						</xsl:otherwise>
					</xsl:choose>
					</span>
					<i class="icon close">x</i>
			</section>
			
		</xsl:if>
		
		<xsl:if test="messageKey">
			<section class="modal error">
				<span data-icon-before="!">
					<xsl:choose>
						<xsl:when test="messageKey='TextTagNameExists'">
							<xsl:value-of select="$i18n.TextTagNameExists" /><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:when test="messageKey='UpdateFailedTextTagNotFound'">
							<xsl:value-of select="$i18n.UpdateFailedTextTagNotFound" /><xsl:text>!</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.unknownFault"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</section>
		</xsl:if>
	</xsl:template>				
</xsl:stylesheet>