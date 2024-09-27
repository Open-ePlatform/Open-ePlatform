<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
		/ckeditor/init.js
		/js/confirmpost.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/persondatainformeradmin.css
	</xsl:variable>
	
	<xsl:template match="Document">
	
		<div id="PersonDataInformerAdmin">
			<xsl:choose>
				<xsl:when test="ListDataReasonAlternatives">
					<xsl:apply-templates select="ListDataReasonAlternatives" />
				</xsl:when>
				<xsl:otherwise>
					<div class="contentitem errands-wrapper">
						<xsl:apply-templates select="UpdateInformerStandardText"/>
						<xsl:apply-templates select="AddInformerDataAlternative"/>
						<xsl:apply-templates select="UpdateInformerDataAlternative"/>
						<xsl:apply-templates select="AddInformerReasonAlternative"/>
						<xsl:apply-templates select="UpdateInformerReasonAlternative"/>
					</div>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ListDataReasonAlternatives">
	
		<div class="contentitem errands-wrapper">
			<div class="heading-wrapper bigpaddingbottom clearfix">
				<h2><xsl:value-of select="$i18n.StandardTexts"/></h2>
			</div>
			
			<table class="oep-table full">
				<thead>
					<tr>
						<th></th>
						<th><xsl:value-of select="$i18n.Name"/></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="InformerStandardTexts/InformerStandardText">
							<xsl:apply-templates select="InformerStandardTexts/InformerStandardText" mode="table"/>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="3"><xsl:value-of select="$i18n.NoStandardTexts"/></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</div>
	
		<div class="contentitem errands-wrapper">
			<div class="heading-wrapper bigpaddingbottom clearfix">
				<a class="btn btn-blue floatright margintop bigmarginright icon" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/adddataalternative">
					<i data-icon-before="+"/>
					
					<xsl:value-of select="$i18n.Add" />
				</a>
			
				<h2><xsl:value-of select="$i18n.DataAlternatives"/></h2>
			</div>
			
			<table class="oep-table full">
				<thead>
					<tr>
						<th></th>
						<th><xsl:value-of select="$i18n.Name"/></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="DataAlternatives">
							<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="table"/>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="3"><xsl:value-of select="$i18n.NoDataAlternatives"/></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</div>
		
		<div class="contentitem errands-wrapper">
			<div class="heading-wrapper bigpaddingbottom clearfix">
				<a class="btn btn-blue floatright margintop bigmarginright icon" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addreasonalternative">
					<i data-icon-before="+"/>
					
					<xsl:value-of select="$i18n.Add" />
				</a>
				
				<h2><xsl:value-of select="$i18n.ReasonAlternatives"/></h2>
			</div>
		
			<table class="oep-table full">
				<thead>
					<tr>
						<th></th>
						<th><xsl:value-of select="$i18n.Name"/></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="ReasonAlternatives">
							<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="table"/>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="3"><xsl:value-of select="$i18n.NoReasonAlternatives"/></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</div>
	
	</xsl:template>
	
	<xsl:template match="InformerStandardText" mode="table">
	
		<tr>
			<td class="icon">
				<i data-icon-before="o"/>
			</td>
			
			<td>
				<xsl:call-template name="textNames">
					<xsl:with-param name="name" select="name"/>
				</xsl:call-template>
			</td>
			
			<td class="link">
				<a class="btn btn-green vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatestandardtext/{textID}"><xsl:value-of select="$i18n.Update" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template name="textNames">
	
		<xsl:param name="name"/>
		
		<xsl:choose>
			<xsl:when test="$name = 'defaultComplaintDescription'">
				<xsl:value-of select="$i18n.DefaultComplaintName"/>
			</xsl:when>
			<xsl:when test="$name = 'defaultReasonDescription'">
				<xsl:value-of select="$i18n.DefaultReasonName"/>
			</xsl:when>
			<xsl:when test="$name = 'defaultStorageDescription'">
				<xsl:value-of select="$i18n.DefaultStorageName"/>
			</xsl:when>
			<xsl:when test="$name = 'defaultExtraInformationDescription'">
				<xsl:value-of select="$i18n.DefaultExtraInformationName"/>
			</xsl:when>
			<xsl:when test="$name = 'defaultConfirmationText'">
				<xsl:value-of select="$i18n.DefaultConfirmationText"/>
			</xsl:when>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="table">
	
		<tr>
			<td class="icon">
				<i data-icon-before="u"/>
			</td>
			
			<td>
				<xsl:value-of select="name"/>
			</td>
			
			<td class="link">
				<a class="btn btn-green vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatedataalternative/{alternativeID}"><xsl:value-of select="$i18n.Update" /></a>
				<a class="btn btn-red vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletedataalternative/{alternativeID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteDataAlternativeConfirm}: {name}"><xsl:value-of select="$i18n.Delete" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="table">
	
		<tr>
			<td class="icon">
				<i data-icon-before="c"/>
			</td>
			
			<td>
				<xsl:value-of select="name"/>
			</td>
			
			<td class="link">
				<a class="btn btn-green vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatereasonalternative/{alternativeID}"><xsl:value-of select="$i18n.Update" /></a>
				<a class="btn btn-red vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletereasonalternative/{alternativeID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteReasonAlternativeConfirm}: {name}"><xsl:value-of select="$i18n.Delete" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="UpdateInformerStandardText">
		
		<h1>
			<xsl:value-of select="$i18n.UpdateInformerStandardText"/>
			<xsl:text> - </xsl:text>
			<xsl:call-template name="textNames">
				<xsl:with-param name="name" select="InformerStandardText/name"/>
			</xsl:call-template>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<form action="{/Document/requestinfo/url}" method="post">
			<xsl:call-template name="InformerStandardTextForm"/>
			
			<div class="text-align-right">
				<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline"/>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel"/></a>
			</div>
		</form>
		
	</xsl:template>
	
	
	<xsl:template name="InformerStandardTextForm">
		
		<div class="full bigmarginbottom">
			<label for="name" class="full"><xsl:value-of select="$i18n.StandardText"/></label>
			
			<xsl:choose>
				<xsl:when test="InformerStandardText/type = 'EDITOR'">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'value'"/>
						<xsl:with-param name="name" select="'value'"/>
						<xsl:with-param name="class" select="'ckeditor'" />
						<xsl:with-param name="element" select="InformerStandardText"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="InformerStandardText/type = 'TEXTFIELD'">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'value'"/>
						<xsl:with-param name="name" select="'value'"/>
						<xsl:with-param name="maxlength" select="'1024'"/>
						<xsl:with-param name="element" select="InformerStandardText"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</div>
		
		<xsl:if test="InformerStandardText/type = 'EDITOR'">
			<xsl:call-template name="initializeFCKEditor">
				<xsl:with-param name="basePath"><xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/</xsl:with-param>
				<xsl:with-param name="customConfig">config.js</xsl:with-param>
				<xsl:with-param name="editorContainerClass">ckeditor</xsl:with-param>
				<xsl:with-param name="editorHeight">150</xsl:with-param>
				
				<xsl:with-param name="contentsCss">
					<xsl:if test="cssPath">
						<xsl:value-of select="cssPath"/>
					</xsl:if>
				</xsl:with-param>
				
				<xsl:with-param name="filebrowserBrowseUri">
					
					<xsl:choose>
						<xsl:when test="ckConnectorModuleAlias">
							<xsl:text>filemanager/index.html?Connector=</xsl:text>
							<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="ckConnectorModuleAlias" />
							<xsl:text>/connector</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="''"/>
						</xsl:otherwise>				
					</xsl:choose>
	
				</xsl:with-param>
				
				<xsl:with-param name="filebrowserImageBrowseUri">
				
					<xsl:choose>
						<xsl:when test="ckConnectorModuleAlias">
							<xsl:text>filemanager/index.html?Connector=</xsl:text>
							<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="ckConnectorModuleAlias" />
							<xsl:text>/connector</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="''"/>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:with-param>
				
			</xsl:call-template>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="AddInformerDataAlternative">
		
		<h1><xsl:value-of select="$i18n.AddInformerDataAlternative"/></h1>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<form action="{/Document/requestinfo/url}" method="post">
			<xsl:call-template name="InformerDataAlternativeForm"/>
			
			<div class="text-align-right">
				<input type="submit" value="{$i18n.Add}" class="btn btn-green btn-inline"/>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel"/></a>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateInformerDataAlternative">
		
		<h1>
			<xsl:value-of select="$i18n.UpdateInformerDataAlternative"/>
			<xsl:text> - </xsl:text>
			<xsl:value-of select="InformerDataAlternative/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<form action="{/Document/requestinfo/url}" method="post">
			<xsl:call-template name="InformerDataAlternativeForm"/>
			
			<div class="text-align-right">
				<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline"/>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel"/></a>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template name="InformerDataAlternativeForm">
		
		<div class="full bigmarginbottom">
			<label for="name" class="full"><xsl:value-of select="$i18n.Name"/></label>
			
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="'name'"/>
				<xsl:with-param name="name" select="'name'"/>
				<xsl:with-param name="size" select="'255'"/>
				<xsl:with-param name="element" select="InformerDataAlternative"/>
			</xsl:call-template>
		</div>
		
		<div class="full bigmarginbottom">
			
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="'autoSelect'"/>
				<xsl:with-param name="name" select="'autoSelect'"/>
				<xsl:with-param name="element" select="InformerDataAlternative"/>
			</xsl:call-template>
			
			<label for="autoSelect"><xsl:value-of select="$i18n.AutoSelect"/></label>
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddInformerReasonAlternative">
	
		<h1><xsl:value-of select="$i18n.AddInformerReasonAlternative"/></h1>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<form action="{/Document/requestinfo/url}" method="post">
			<xsl:call-template name="InformerReasonAlternativeForm"/>
			
			<div class="text-align-right">
				<input type="submit" value="{$i18n.Add}" class="btn btn-green btn-inline"/>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel"/></a>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateInformerReasonAlternative">
		
		<h1>
			<xsl:value-of select="$i18n.UpdateInformerReasonAlternative"/>
			<xsl:text> - </xsl:text>
			<xsl:value-of select="InformerReasonAlternative/name"/>
		</h1>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<form action="{/Document/requestinfo/url}" method="post">
			<xsl:call-template name="InformerReasonAlternativeForm"/>
			
			<div class="text-align-right">
				<input type="submit" value="{$i18n.SaveChanges}" class="btn btn-green btn-inline"/>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}" class="btn btn-light btn-inline" onclick="return confirm('{$i18n.CancelConfirm}?');"><xsl:value-of select="$i18n.Cancel"/></a>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template name="InformerReasonAlternativeForm">
	
		<div class="full bigmarginbottom">
			<label for="name"><xsl:value-of select="$i18n.Name"/></label>
			
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="'name'"/>
				<xsl:with-param name="name" select="'name'"/>
				<xsl:with-param name="size" select="'255'"/>
				<xsl:with-param name="element" select="InformerReasonAlternative"/>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
	
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
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
						<xsl:value-of select="$i18n.Name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'value'">
						<xsl:value-of select="$i18n.StandardText"/>
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