<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />
	
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>
	
	<xsl:variable name="globalscripts">
		/js/confirmpost.js
	</xsl:variable>
	
	<xsl:template match="Document">

		<div id="TextFieldQueryEndpointAdminModule" class="contentitem errands-wrapper">

			<xsl:apply-templates select="ListTextFieldQueryEndpoints"/>
			<xsl:apply-templates select="ShowTextFieldQueryEndpoint"/>
			<xsl:apply-templates select="AddTextFieldQueryEndpoint"/>
			<xsl:apply-templates select="UpdateTextFieldQueryEndpoint"/>

		</div>

	</xsl:template>

	<xsl:template match="ListTextFieldQueryEndpoints">

		<div class="errands-wrapper">
			<div lass="heading-wrapper bigpaddingbottom clearfix">
				<a class="btn btn-blue floatright margintop bigmarginright icon" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add">
					<i data-icon-before="+"/>
					
					<xsl:value-of select="$i18n.Add" />
				</a>
				
				<h1>
					<xsl:value-of select="$i18n.ListEndpoints" />
				</h1>
			</div>
			
			<table class="oep-table full">
				<thead>
					<tr>
						<th width="15"></th>
						<th><xsl:value-of select="$i18n.Name"/></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="TextFieldQueryEndpoints/Endpoint">
							<xsl:apply-templates select="TextFieldQueryEndpoints/Endpoint" mode="table"/>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="3"><xsl:value-of select="$i18n.NoEndpoints"/></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</div>
		
	</xsl:template>
	
	<xsl:template match="Endpoint" mode="table">
	
		<tr>
			<td class="icon">
				<i data-icon-before="d"/>
			</td>
			
			<td>
				<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{endpointID}"><xsl:value-of select="name"/></a>
			</td>
			
			<td class="link">
				<a class="btn btn-green vertical-align-middle marginright" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{endpointID}">
					<xsl:value-of select="$i18n.Update" />
				</a>
				<xsl:choose>
					<xsl:when test="Queries">
						<a class="btn btn-light disabled vertical-align-middle" href="#" onclick="return false;" title="$i18n.DeleteDisabled">
							<xsl:value-of select="$i18n.Delete" />
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a class="btn btn-red vertical-align-middle" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{endpointID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteConfirm}: {name}">
							<xsl:value-of select="$i18n.Delete" />
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShowTextFieldQueryEndpoint">
	
		<h1 class="clearfix">
			<xsl:value-of select="$i18n.ShowEndpoint" />
			<xsl:text> - </xsl:text>
			<xsl:value-of select="Endpoint/name"/>
			
			<xsl:choose>
				<xsl:when test="Queries">
					<a class="floatright marginleft" href="#" onclick="return false;">
						<img src="{$imagePath}/delete_gray.png"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a class="floatright marginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{endpointID}" onclick="return confirmHyperlinkPost(this);" title="{$i18n.DeleteConfirm}: {name}">
						<img src="{$imagePath}/delete.png"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
			
			<a class="floatright" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{Endpoint/endpointID}">
				<img src="{$imagePath}/pen.png"/>
			</a>
		</h1>
		
		<div class="floatleft full bigmarginbottom">
			<label><xsl:value-of select="$i18n.Username"/></label><br/>
			
			<xsl:choose>
				<xsl:when test="Endpoint/username">
					<xsl:value-of select="Endpoint/username"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>-</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label><xsl:value-of select="$i18n.Password"/></label><br/>
			
			<xsl:choose>
				<xsl:when test="Endpoint/password">
					<xsl:value-of select="Endpoint/password"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>-</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label><xsl:value-of select="$i18n.Encoding"/></label><br/>
			<xsl:value-of select="Endpoint/encoding"/>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label><xsl:value-of select="$i18n.Address"/></label><br/>
			<xsl:value-of select="Endpoint/address"/>
		</div>
		
		<div class="floatleft full bigmarginbottom bigmargintop">
			<h2><xsl:value-of select="$i18n.EndpointUsage"/></h2>
			
			<table class="coloredtable sortabletable oep-table">
				<thead class="sortable">
					<tr>
						<th><xsl:value-of select="$i18n.Flow" /></th>
						<th><xsl:value-of select="$i18n.Version" /></th>
						<th><xsl:value-of select="$i18n.StepAndQuestion" /></th>
					</tr>
				</thead>
				<tbody>
					<xsl:choose>
						<xsl:when test="Queries">
							<xsl:apply-templates select="Queries/Flow"/>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="3"><xsl:value-of select="$i18n.NoQueries"/></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</div>
		
		<div class="clearboth"/>
		
		<a class="btn btn-blue floatright margintop" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}"><xsl:value-of select="$i18n.Back"/></a>
		
	</xsl:template>
	
	<xsl:template match="Flow">
		
		<tr>
			<td><xsl:value-of select="name"/></td>
			<td><xsl:value-of select="version"/></td>
			<td>
				<xsl:value-of select="Step/name"/>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="Step/Query/QueryDescriptor/name"/>
			</td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="AddTextFieldQueryEndpoint">
	
		<h1>
			<xsl:value-of select="$i18n.AddEndpoint" />
		</h1>
		
		<xsl:call-template name="EndpointForm" />
		
	</xsl:template>
	
	<xsl:template match="UpdateTextFieldQueryEndpoint">
		
		<h1>
			<xsl:value-of select="$i18n.UpdateEndpoint" />
			<xsl:text> - </xsl:text>
			<xsl:value-of select="Endpoint/name"/>
		</h1>
		
		<xsl:call-template name="EndpointForm" />
		
	</xsl:template>
	
	<xsl:template name="EndpointForm">

		<xsl:apply-templates select="validationException/validationError" />

		<form id="TextFieldQueryEndpointForm" name="textFieldQueryEndpointForm" method="post" action="{/Document/requestinfo/uri}">
		
			<div class="floatleft full bigmarginbottom">
				<label for="name" class="floatleft clearboth required"><xsl:value-of select="$i18n.Name" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'"/>
						<xsl:with-param name="name" select="'name'"/>
						<xsl:with-param name="title" select="$i18n.Name"/>
						<xsl:with-param name="element" select="Endpoint" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="address" class="floatleft clearboth required">
					<xsl:value-of select="$i18n.Address" />
				</label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'address'"/>
						<xsl:with-param name="name" select="'address'"/>
						<xsl:with-param name="title" select="$i18n.Address"/>
						<xsl:with-param name="element" select="Endpoint" />
						<xsl:with-param name="maxlength" select="'1024'"/>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft margintop full">
				<p><xsl:value-of select="$i18n.TagsTable.description.part1"/></p>
				<p><xsl:value-of select="$i18n.DynamicURLExample" /></p>
				<p><xsl:value-of select="$i18n.TagsTable.description.part2"/></p>
			
				<table class="full border">
					<tr>
						<th>
							<xsl:value-of select="$i18n.Tag"/>
						</th>
						<th>
							<xsl:value-of select="$i18n.Description"/>
						</th>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.firstname</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserFirstnameTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.lastname</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserLastnameTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.email</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserEmailTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.username</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserUsernameTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.userID</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserUserIDTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$user.attribute{citizenIdentifier}</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.UserCitizenIdentifierTag"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:text>$attribute{attributnamn}</xsl:text>
						</td>
						<td>
							<xsl:value-of select="$i18n.Tags.Attribute"/>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="fields" class="floatleft clearboth required"><xsl:value-of select="$i18n.Query.fields" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextArea">
						<xsl:with-param name="id" select="'fields'" />
						<xsl:with-param name="name" select="'fields'" />
						<xsl:with-param name="title" select="$i18n.Query.fields"/>
						<xsl:with-param name="separateListValues" select="'true'" />
						<xsl:with-param name="element" select="Endpoint" />
					<xsl:with-param name="element" select="Endpoint/Fields/value" />
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="username" class="floatleft clearboth"><xsl:value-of select="$i18n.Username" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'username'"/>
						<xsl:with-param name="name" select="'username'"/>
						<xsl:with-param name="title" select="$i18n.Username"/>
						<xsl:with-param name="element" select="Endpoint" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="password" class="floatleft clearboth"><xsl:value-of select="$i18n.Password" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'password'"/>
						<xsl:with-param name="name" select="'password'"/>
						<xsl:with-param name="title" select="$i18n.Password"/>
						<xsl:with-param name="element" select="Endpoint" />
						<xsl:with-param name="maxlength" select="'255'"/>
					</xsl:call-template>
				</div>
			</div>
			
			<div class="floatleft full bigmarginbottom">
				<label for="password" class="floatleft clearboth"><xsl:value-of select="$i18n.Encoding" /></label>
				
				<div class="floatleft full">
					<xsl:call-template name="createDropdown">
						<xsl:with-param name="id" select="'encoding'"/>
						<xsl:with-param name="name" select="'encoding'"/>
						<xsl:with-param name="selectedValue" select="Endpoint/encoding" />
						<xsl:with-param name="element" select="AllowedEncodings/Encoding"/>
						<xsl:with-param name="simple" select="true()"/>
					</xsl:call-template>
				</div>
			</div>

			<div class="floatright margintop clearboth">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

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
					<xsl:when test="fieldName = 'address'">
						<xsl:value-of select="$i18n.Address"/>
					</xsl:when>
					<xsl:when test="fieldName = 'username'">
						<xsl:value-of select="$i18n.Username"/>
					</xsl:when>
					<xsl:when test="fieldName = 'password'">
						<xsl:value-of select="$i18n.Password"/>
					</xsl:when>
					<xsl:when test="fieldName = 'encoding'">
						<xsl:value-of select="$i18n.Encoding"/>
					</xsl:when>
					<xsl:when test="fieldName = 'fields'">
						<xsl:value-of select="$i18n.Query.fields"/>
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
					<xsl:when test="messageKey = 'ShowFailedTextFieldQueryEndpointNotFound' or messageKey = 'UpdateFailedTextFieldQueryEndpointNotFound' or messageKey = 'DeleteFailedTextFieldQueryEndpointNotFound'">
						<xsl:value-of select="$i18n.validation.EndpointNotFound" />
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="$i18n.validation.unknownFault" />
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
	</xsl:template>	

</xsl:stylesheet>