<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/jquery/jquery-ui.js
	</xsl:variable>

	<xsl:variable name="globallinks">
		/css/openhierarchy-jquery-ui.css
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/UserGroupList.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/UserGroupList.css
	</xsl:variable>
	
	<xsl:template match="Document">
	
		<xsl:apply-templates select="Show" />
		<xsl:apply-templates select="Update" />
			
	</xsl:template>
	
	<xsl:template match="Show">
		<xsl:variable name="extensionImgPath"><xsl:value-of select="/Document/requestinfo/contextpath" /><xsl:value-of select="extensionRequestURL" />/static/pics</xsl:variable>
		
		<a name="apiaccess" />
		
		<xsl:choose>
			<xsl:when test="APIUsers">
			
				<div class="bold">
					<xsl:value-of select="$i18n.Settings.allowedUsers"/>
				</div>
				
				<xsl:apply-templates select="APIUsers/user" mode="list">
					<xsl:with-param name="imgPath" select="$extensionImgPath"/>
				</xsl:apply-templates>
				
			</xsl:when>
			<xsl:otherwise>
				
				<xsl:value-of select="$i18n.Settings.allowedUsers.empty" />
				
			</xsl:otherwise>
		</xsl:choose>
		
		<a class="floatright clearboth" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/update">
			<xsl:value-of select="$i18n.Update" />
			<img class="marginleft" src="{$extensionImgPath}/pen.png" alt="{$i18n.Update}" />
		</a>
		
	</xsl:template>
		
	<xsl:template match="user" mode="list">
		<xsl:param name="imgPath"/>
		
		<div class="marginbottom border">

			<xsl:choose>
				<xsl:when test="enabled='true'">
					<img class="alignbottom" src="{$imgPath}/user.png" alt="" />
				</xsl:when>
				<xsl:otherwise>
					<img class="alignbottom" src="{$imgPath}/user_disabled.png" alt="" />
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:text>&#x20;</xsl:text>
			
			<xsl:value-of select="firstname"/>
			<xsl:text>&#x20;</xsl:text>
			<xsl:value-of select="lastname"/>
			
			<xsl:if test="username">
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:text>(</xsl:text>
					<xsl:value-of select="username"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="Update">
		
		<div id="APIAccessModule">
			<div class="contentitem errands-wrapper">
			
				<h1>
					<xsl:value-of select="$i18n.Update.title"/>
					<xsl:value-of select="VersionName"/>
				</h1>
				
				<xsl:apply-templates select="ValidationErrors/validationError[not(starts-with(fieldName, 'days-'))]"/>
				
				<p>
					<xsl:value-of select="$i18n.Update.description"/>
				</p>
				
					
				<form id="autoculling" method="POST" action="{/Document/requestinfo/uri}">
						
					<div class="floatleft full bigmarginbottom">
						
						<label class="floatleft full">
							<xsl:value-of select="$i18n.Settings.allowedUsers" />
						</label>
						
						<xsl:call-template name="UserList">
							<xsl:with-param name="connectorURL">
								<xsl:value-of select="/Document/requestinfo/contextpath"/>
								<xsl:value-of select="extensionRequestURL"/>
								<xsl:text>/users</xsl:text>
							</xsl:with-param>
							<xsl:with-param name="name" select="'users'"/>
							<xsl:with-param name="users" select="APIUsers" />
						</xsl:call-template>
					</div>
						
					<div class="clearfix">
						<div class="floatright">
							<input class="btn btn-green" type="submit" value="{$i18n.Update.submit}" />
						</div>
						<div class="floatright marginright">
							<a class="btn btn-light" href="{/Document/requestinfo/contextpath}{extensionRequestURL}/toflow" title="{$i18n.Back}">
								<xsl:value-of select="$i18n.Back"/>
							</a>
						</div>
					</div>
				</form>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.Validation.requiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.Validation.invalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.Validation.tooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.Validation.tooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.Validation.unknownError" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'users'">
						<xsl:value-of select="$i18n.Settings.allowedUsers" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
<!-- 				<xsl:choose> -->
<!-- 					<xsl:when test="messageKey='DummyKey'"> -->
<!-- 					</xsl:when> -->
<!-- 					<xsl:otherwise> -->
							<xsl:value-of select="$i18n.Validation.unknownFault" />
<!-- 					</xsl:otherwise> -->
<!-- 				</xsl:choose> -->
			</p>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>