<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/js/confirmpost.js
	</xsl:variable>

	<xsl:template match="Document">	

		<div class="contentitem">
			<xsl:apply-templates select="ListSources"/>
			<xsl:apply-templates select="ShowSource"/>
			<xsl:apply-templates select="AddSource"/>
			<xsl:apply-templates select="UpdateSource"/>
		</div>

	</xsl:template>
	
	<xsl:template match="ListSources">
	
		<h1>
			<xsl:value-of select="../module/name"/>
			<xsl:text>: </xsl:text>
			<xsl:value-of select="$i18n.ListSources.Title"/>
		</h1>
		
		<xsl:choose>
			<xsl:when test="Sources">
				<br/>
				<xsl:apply-templates select="Sources/Source" mode="list"/>
				<br/>
			</xsl:when>
			<xsl:otherwise>
				<p><xsl:value-of select="$i18n.noSourcesFound"/></p>
			</xsl:otherwise>
		</xsl:choose>	
	
		<div class="floatright marginright">
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addsource" title="{$i18n.addSource}">
				<xsl:value-of select="$i18n.addSource"/>
				<img class="alignbottom marginleft" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png"/>
			</a>
		</div>
	
	</xsl:template>
	
	<xsl:template match="Source" mode="list">
		
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/showsource/{sourceID}" title="{$i18n.viewSource}: {name}">
					
					<xsl:value-of select="name"/>
						
					<xsl:text>&#x20;</xsl:text>
					
					<xsl:text>(</xsl:text>
						<xsl:value-of select="username"/>
					<xsl:text>)</xsl:text>
				</a>
			</div>

			<xsl:call-template name="adminLinks"/>
			
		</div>
	</xsl:template>
	
	<xsl:template match="Source" mode="adminLinks" name="adminLinks">
		<div class="floatright marginright">
					
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/updatesource/{sourceID}" title="{$i18n.updateSource}: {name}">
				<img class="alignbottom marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/pen.png"/>
			</a>

			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/deletesource/{sourceID}" onclick="return confirmPost(this.href, '{$i18n.deleteSourceConfirm}: {name}?')" title="{$i18n.removeSource}: {name}">
				<img class="alignbottom marginright" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png"/>
			</a>
				
		</div>
	</xsl:template>
		
	<xsl:template match="ShowSource">
	
		<xsl:apply-templates select="Source" mode="adminLinks"/>
		
		<h1><xsl:value-of select="Source/name"/></h1>
				
		<table>
			<tr>
				<td><xsl:value-of select="$i18n.name" />:</td>
				<td>
					<xsl:value-of select="Source/name"/>
				</td>
			</tr>
			<tr>
				<td><xsl:value-of select="$i18n.username" />:</td>
				<td>
					<xsl:value-of select="Source/username"/>
				</td>
			</tr>
			<tr>
				<td><xsl:value-of select="$i18n.uploadAccess" />:</td>
				<td>
					<xsl:choose>
						<xsl:when test="Source/uploadAccess = 'true'">
							<xsl:value-of select="$i18n.Yes"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.No"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
		
	</xsl:template>
						
	<xsl:template match="AddSource">
	
		<h1><xsl:value-of select="$i18n.addSource"/></h1>
		
		<xsl:apply-templates select="validationError"/>
		
		<form method="POST" action="{/document/requestinfo/uri}">

			<xsl:call-template name="EditSource">
				<xsl:with-param name="mode" select="'add'"/>
			</xsl:call-template>
			
			<div align="right">
				<input type="submit" value="{$i18n.addSource}"/>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateSource">
	
		<h1>
			<xsl:value-of select="$i18n.updateSource" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="Source/name"/>
		</h1>
		
		<xsl:apply-templates select="validationError"/>
				
		<form method="POST" action="{/document/requestinfo/uri}" name="sourceform">

			<xsl:call-template name="EditSource">
				<xsl:with-param name="mode" select="'update'"/>
			</xsl:call-template>
			
			<div align="right">
				<input type="submit" value="{$i18n.saveChanges}"/>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template name="EditSource">
	
		<xsl:param name="mode"/>
	
		<xsl:apply-templates select="validationException/validationError"/>
		
		<table>
		
			<tr>
				<td><xsl:value-of select="$i18n.name"/>:</td>
				<td>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'name'"/>
						<xsl:with-param name="size" select="20"/>
						<xsl:with-param name="element" select="Source"/>
					</xsl:call-template>
				</td>
			</tr>
			
			<tr>
				<td><xsl:value-of select="$i18n.username"/>:</td>
				<td>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'username'"/>
						<xsl:with-param name="size" select="20"/>
						<xsl:with-param name="element" select="Source"/>
					</xsl:call-template>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'uploadAccess'" />
						<xsl:with-param name="name" select="'uploadAccess'" />
						<xsl:with-param name="value" select="'true'" />
						<xsl:with-param name="element" select="Source" />
					</xsl:call-template>
					
					<label for="uploadAccess">
						<xsl:value-of select="$i18n.uploadAccess" />
					</label>
				</td>
			</tr>
			
			<xsl:if test="$mode = 'update'">
				<tr>
					<td colspan="2">
						<input id="changepassword" type="checkbox" onclick="document.sourceform.password.disabled=!this.checked;document.sourceform.passwordconfirmation.disabled=!this.checked"/>
						<label for="changepassword">
							<xsl:value-of select="$i18n.changePassword" />
						</label>
					</td>
				</tr>
			</xsl:if>
	
			<tr>
				<td><xsl:value-of select="$i18n.password"/>:</td>
				<td>
					<input type="password" name="password" size="20">
						
						<xsl:if test="$mode = 'update'">
							
							<xsl:attribute name="disabled">true</xsl:attribute>
							
						</xsl:if>
										
					</input>
				</td>
			</tr>
			<tr>
				<td><xsl:value-of select="$i18n.passwordConfirmation"/>:</td>
				<td>
					<input type="password" name="passwordconfirmation" size="20">
					
						<xsl:if test="$mode = 'update'">
							
							<xsl:attribute name="disabled">true</xsl:attribute>
							
						</xsl:if>
					
					</input>
				</td>
			</tr>
			
		</table>
		
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
					<xsl:when test="displayName">
						<xsl:value-of select="displayName"/>
					</xsl:when>				
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'username'">
						<xsl:value-of select="$i18n.username"/>
					</xsl:when>
					<xsl:when test="fieldName = 'password'">
						<xsl:value-of select="$i18n.password"/>
					</xsl:when>																
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>			
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>		
					<xsl:when test="messageKey='RequestedSourceNotFound'">
						<xsl:value-of select="$i18n.requestedSourceNotFound"/><xsl:text>!</xsl:text>
					</xsl:when>				
					<xsl:when test="messageKey='UpdateFailedSourceNotFound'">
						<xsl:value-of select="$i18n.sourceToUpdateNotFound"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='DeleteFailedSourceNotFound'">
						<xsl:value-of select="$i18n.sourceToRemoveNotFound"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='UsernameAlreadyTaken'">
						<xsl:value-of select="$i18n.usernameAlreadyTaken"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='PasswordConfirmationMissMatch'">
						<xsl:value-of select="$i18n.passwordConfirmationMissMatch"/><xsl:text>!</xsl:text>
					</xsl:when>
					<xsl:when test="messageKey='UnableToAddSources'">
						<xsl:value-of select="$i18n.unableToAddSources"/><xsl:text>!</xsl:text>
					</xsl:when>																												
					<xsl:otherwise>
						<xsl:value-of select="$i18n.unknownFault"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
	</xsl:template>				
</xsl:stylesheet>