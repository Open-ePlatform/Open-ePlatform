<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/persondataquery.js
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
	
		<div class="query persondatainformerquery">
		
			<article>
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="PersonDataInformerQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="PersonDataInformerQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="PersonDataInformerQueryInstance/PersonDataInformerQuery/queryID" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="PersonDataInformerQueryInstance/PersonDataInformerQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="PersonDataInformerQueryInstance/PersonDataInformerQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<xsl:apply-templates select="PersonDataInformerQueryInstance/PersonDataInformerQuery/FlowFamilyInformerSetting"/>
				
				<xsl:if test="PersonDataInformerQueryInstance/FlowFamily/ownerName">
					<p>
						<strong>
							<xsl:value-of select="$i18n.Accountable"/>
						</strong>
						<br/>
						<xsl:value-of select="PersonDataInformerQueryInstance/FlowFamily/ownerName" />
					</p>
				</xsl:if>
				
				<xsl:if test="PersonDataInformerQueryInstance/accepted = 'true'">
				
					<p>
						<strong><xsl:value-of select="$i18n.Accept" /></strong>
					</p>
				
				</xsl:if>
				
			</article>
		
		</div>	
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="queryID" select="concat('query_', PersonDataInformerQueryInstance/PersonDataInformerQuery/queryID)" />
	
		<div class="query persondatainformerquery" id="{$queryID}">
	
			<xsl:if test="EnableAjaxPosting">
				<xsl:attribute name="class">query enableAjaxPosting</xsl:attribute>
			</xsl:if>
			
			<a name="{$queryID}" />
	
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
					
							<xsl:apply-templates select="ValidationErrors/validationError"/>
						
						<div class="marker"/>
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
							<xsl:if test="PersonDataInformerQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="PersonDataInformerQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="PersonDataInformerQueryInstance/PersonDataInformerQuery/helpText">		
						<xsl:apply-templates select="PersonDataInformerQueryInstance/PersonDataInformerQuery/helpText" />
					</xsl:if>

				</div>
				
				<xsl:if test="PersonDataInformerQueryInstance/PersonDataInformerQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="PersonDataInformerQueryInstance/PersonDataInformerQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<xsl:apply-templates select="PersonDataInformerQueryInstance/PersonDataInformerQuery/FlowFamilyInformerSetting"/>
				
				<xsl:if test="PersonDataInformerQueryInstance/FlowFamily/ownerName">
					<p>
						<strong>
							<xsl:value-of select="$i18n.Accountable"/>
						</strong>
						<br/>
						<xsl:value-of select="PersonDataInformerQueryInstance/FlowFamily/ownerName" />
					</p>
				</xsl:if>
				
				<div class="alternative description-text" >
					<xsl:variable name="fieldName">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="PersonDataInformerQueryInstance/PersonDataInformerQuery/queryID"/>
						<xsl:value-of select="'_accept'"/>
					</xsl:variable>
				
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="$fieldName"/>
						<xsl:with-param name="name" select="$fieldName"/>
						<xsl:with-param name="class" select="'vertical-align-bottom'" />
						<xsl:with-param name="checked" select="PersonDataInformerQueryInstance/accepted"/>
						<xsl:with-param name="disabled" select="ValidationErrors/validationError[messageKey = 'AlreadyMember']/messageKey"/>
					</xsl:call-template>
					
					<label for="{$fieldName}" class="checkbox">
						
						<xsl:if test="ValidationErrors/validationError[messageKey = 'AlreadyMember']">
							<xsl:attribute name="class">checkbox disabled</xsl:attribute>
						</xsl:if>
					
						<xsl:value-of select="$i18n.Accept" />
					</label>
				</div>
			
			</article>
	
		</div>
		
		<script type="text/javascript">$(document).ready(function(){initPersonDataInformerQuery('<xsl:value-of select="PersonDataInformerQueryInstance/PersonDataInformerQuery/queryID" />');});</script>
		
	</xsl:template>
	
	<xsl:template match="FlowFamilyInformerSetting">
	
		<strong>
			<xsl:value-of select="$i18n.SavedPersonData"/>
		</strong>
		
		<ul class="description-text">
			<xsl:apply-templates select="DataAlternatives/InformerDataAlternative" mode="show"/>
		</ul>
		
		<xsl:if test="reason">
			<strong>
				<xsl:value-of select="$i18n.Reason"/>
			</strong>
			
			<br/>
			
			<span class="description-text">
				<xsl:value-of select="reason" disable-output-escaping="yes"/>
			</span>
		</xsl:if>
		
		<strong>
			<xsl:value-of select="$i18n.Reasons"/>
		</strong>
		
		<ul class="description-text">
			<xsl:apply-templates select="ReasonAlternatives/InformerReasonAlternative" mode="show"/>
		</ul>
		
		<p>
			<strong>
				<xsl:value-of select="$i18n.YearsSaved"/>
			</strong>
			
			<ul class="description-text">
				<xsl:apply-templates select="StorageSettings" mode="list"/>
			</ul>
		</p>
		
		<xsl:if test="extraInformationStorage">
			<strong>
				<xsl:value-of select="$i18n.ExtraInformationStorage"/>
			</strong>
			
			<br/>
			
			<span class="description-text">
				<xsl:value-of select="extraInformationStorage" disable-output-escaping="yes"/>
			</span>
		</xsl:if>
		
		<xsl:if test="dataRecipient">
			<p>
				<strong><xsl:value-of select="$i18n.DataRecipient"/></strong>
				
				<br/>
				
				<span class="description-text">
					<xsl:value-of select="dataRecipient" disable-output-escaping="yes"/>
				</span>
			</p>
		</xsl:if>
		
		<xsl:if test="extraInformation">
			<p>
				<strong>
					<xsl:value-of select="$i18n.ExtraInformation"/>
				</strong>
				
				<br/>
				
				<span class="description-text">
					<xsl:value-of select="extraInformation" disable-output-escaping="yes"/>
				</span>
			</p>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="StorageSetting" mode="list">
	
		<li>
			<xsl:choose>
				<xsl:when test="storageType = 'INFINITY'">
					<xsl:value-of select="$i18n.YearsSaved.Infinite"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="period"/>
					
					<xsl:text> </xsl:text>
					
					<xsl:choose>
						<xsl:when test="storageType = 'YEAR'">
							<xsl:value-of select="$i18n.YearsSaved.Years"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.YearsSaved.Months"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="description">
				<xsl:text> - </xsl:text>
				
				<xsl:value-of select="description"/>
			</xsl:if>
		</li>
		
	</xsl:template>
	
	<xsl:template match="InformerDataAlternative" mode="show">

		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>
	
	<xsl:template match="InformerReasonAlternative" mode="show">

		<li>
			<xsl:value-of select="name" />
		</li>
	
	</xsl:template>		
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<i data-icon-after="!">
			<xsl:attribute name="title">
				<xsl:choose>
					<xsl:when test="invalidFormatMessage">
						<xsl:value-of select="invalidFormatMessage"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="$i18n.Error.InvalidFormat"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</i>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'RequiredQuery']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Error.RequiredQuery"/>
			</strong>
		</span>
		
	</xsl:template>
	
	
	<xsl:template match="validationError">
	
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<span>
				<strong data-icon-before="!">
			
					<xsl:choose>
						<xsl:when test="validationErrorType='InvalidFormat'">
							<xsl:value-of select="$i18n.Error.InvalidFormat" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.Error.TooLong" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.Error.UnknownValidationError" />
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:text>&#x20;</xsl:text>
					
					<xsl:choose>
						<xsl:when test="displayName">
							<xsl:value-of select="displayName"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="fieldName"/>
						</xsl:otherwise>
					</xsl:choose>
				</strong>
			</span>
		</xsl:if>
				
		<xsl:if test="messageKey">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.Error.UnknownValidationError" />
				</strong>
			</span>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>