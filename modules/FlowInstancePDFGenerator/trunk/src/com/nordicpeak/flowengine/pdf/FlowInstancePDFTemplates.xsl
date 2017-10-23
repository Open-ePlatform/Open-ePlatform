<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:template name="head">
	
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
			
			<xsl:call-template name="title"/>
			
			<xsl:apply-templates select="staticStylesheets"/>>
			
			<xsl:apply-templates select="StyleSheets/StyleSheet"/>
			
			<bookmarks>
				<xsl:apply-templates select="ManagerResponses/PDFManagerResponse" mode="bookmark"/>
				
				<xsl:if test="SignEvents">
					<bookmark name="{$i18n.Signed.Title}" href="#signed"/>
				</xsl:if>
			</bookmarks>
		</head>
		
	</xsl:template>
	
	<xsl:template name="title">
		<title>Ärendenummer #<xsl:value-of select="FlowInstance/flowInstanceID"/></title>
	</xsl:template>
	
	<xsl:template match="staticStylesheets" >
	
		<link rel="stylesheet" type="text/css" href="classpath://com/nordicpeak/flowengine/pdf/staticcontent/css/flowinstance.css" />
	
	</xsl:template>
	
	<xsl:template name="header">
	
		<div class="header">
					
			<div class="logo">
			
				<xsl:choose>
					<xsl:when test="Logotype">
						<img src="{Logotype}"/>
					</xsl:when>
					<xsl:otherwise>
						<img src="classpath://com/nordicpeak/flowengine/pdf/staticcontent/pics/logo.png"/>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			
			<div class="header-text">
				<h1>
					<xsl:value-of select="FlowInstance/Flow/name"/>
				</h1>
				
				<p class="clearboth">
					<xsl:choose>
						<xsl:when test="Signing"><xsl:value-of select="$i18n.Signing"/></xsl:when>
						<xsl:otherwise>
						
							<xsl:text>Ärendenummer: #</xsl:text>
							<xsl:value-of select="FlowInstance/flowInstanceID"/>
							
							<xsl:text> | Inskickat av: </xsl:text>
							<xsl:call-template name="PrintPostedBy" />
							
							<xsl:if test="SignEvents">
								<xsl:text> (signerad)</xsl:text>
							</xsl:if>
							
							<xsl:text> | Datum: </xsl:text>
							<xsl:value-of select="SubmitDate"/>
							
							<xsl:if test="EditedByManager">
								<br/>
								<xsl:call-template name="PrintEditedBy" />
							</xsl:if>
							
						</xsl:otherwise>
					</xsl:choose>
				</p>
			</div>
			
		</div>
	 
	</xsl:template>
	
	<xsl:template name="footer">
	
		<div id="footer">
			
			<xsl:if test="not(Signing)">
			
				<xsl:text>Ärendenummer: #</xsl:text>
				<xsl:value-of select="FlowInstance/flowInstanceID"/>
			
				<xsl:text> | Inskickat av: </xsl:text>
				<xsl:call-template name="PrintPostedBy" />
				
				<xsl:if test="SignEvents">
					<xsl:text> (signerad)</xsl:text>
				</xsl:if>
				
				<xsl:text> | Datum: </xsl:text>
				<xsl:value-of select="SubmitDate"/>
				
			</xsl:if>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="footerPageNumber">
	
		<div id="pagenumber-container">
			<xsl:text>Sida </xsl:text>
			<span id="pagenumber"/>
			<xsl:text> av </xsl:text>
			<span id="pagecount"/>
		</div>
	
	</xsl:template>
	
	<xsl:template name="preContent" />
	
	<xsl:template name="content">
	
		<div class="content">
		
			<xsl:apply-templates select="ManagerResponses/PDFManagerResponse" mode="xhtml"/>

		</div>
	
	</xsl:template>
	
	<xsl:template match="Document">
		
		<html xmlns="http://www.w3.org/1999/xhtml">
			
			<xsl:call-template name="head"/>
			
			<body>
			
				<xsl:call-template name="header"/>
				<xsl:call-template name="footer"/>
				<xsl:call-template name="footerPageNumber"/>
				<xsl:call-template name="preContent"/>
				<xsl:call-template name="content"/>
			
				<xsl:apply-templates select="SignEvents"/>
				
			</body>
		</html>
		
	</xsl:template>
	
	<xsl:template match="PDFManagerResponse" mode="bookmark">
	
		<xsl:variable name="stepID" select="currentStepID"/>
	
		<bookmark name="{currentStepIndex + 1}. {../../FlowInstance/Flow/Steps/Step[stepID = $stepID]/name}" href="#step{currentStepIndex + 1}">
	
			<xsl:apply-templates select="QueryResponses/PDFQueryResponse" mode="bookmark"/>
	
		</bookmark>
		
	</xsl:template>	
	
	<xsl:template match="PDFQueryResponse" mode="bookmark">
	
		<bookmark name="{QueryDescriptor/name}" href="#query{QueryDescriptor/queryID}"/>
	
	</xsl:template>
	
	<xsl:template match="PDFManagerResponse" mode="xhtml">
	
		<xsl:variable name="stepID" select="currentStepID"/>
	
		<div class="step">
			
			 <a name="step{currentStepIndex + 1}"/>
		
			<h1>
				<img src="classpath://com/nordicpeak/flowengine/pdf/staticcontent/pics/check.png"/>
				<xsl:value-of select="currentStepIndex + 1"/>
				<xsl:text>. </xsl:text>
				<xsl:value-of select="../../FlowInstance/Flow/Steps/Step[stepID = $stepID]/name"/>
			</h1>
			
			<xsl:apply-templates select="QueryResponses/PDFQueryResponse" mode="xhtml"/>
		</div>
	
	</xsl:template>	
	
	<xsl:template match="PDFQueryResponse" mode="xhtml">
	
		<div>
			<xsl:if test="following-sibling::*[1]/QueryDescriptor/mergeWithPreviousQuery = 'true'">
				<xsl:attribute name="class">
					<xsl:text> mergedquery</xsl:text>
				</xsl:attribute>
			</xsl:if>
		
			<xsl:value-of select="XHTML" disable-output-escaping="yes"/>
		</div>
		
	</xsl:template>	
	
	<xsl:template match="StyleSheet">
	
		<link rel="stylesheet" type="text/css" href="{.}" />
	
	</xsl:template>
	
	<xsl:template name="showCitizenIDInPrintPostedBy">
		<xsl:text>false</xsl:text>
	</xsl:template>
	
	<xsl:template name="showCitizenIDInSignEvents">
		<xsl:text>true</xsl:text>
	</xsl:template>
	
	<xsl:template name="PrintPostedBy">
		<xsl:param name="citizenIDspacer" select="', '"/>
		
		<xsl:choose>
			<xsl:when test="/Document/PostedBy">
				<xsl:value-of select="/Document/PostedBy" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.AnonymousUser" />
			</xsl:otherwise>
		</xsl:choose>

		<xsl:variable name="showCitizenID">
			<xsl:call-template name="showCitizenIDInPrintPostedBy"/>
		</xsl:variable>
		
		<xsl:if test="$showCitizenID = 'true' and /Document/PostedByCitizenID">
			<xsl:value-of select="$citizenIDspacer"/>
			<xsl:value-of select="/Document/PostedByCitizenID" />
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="PrintEditedBy">
		<xsl:param name="dateSpacer" select="' | '"/>
		
		<xsl:if test="/Document/EditedByManager">

			<xsl:value-of select="$i18n.EditedByManager" />
			
			<xsl:if test="/Document/Manager">
			
				<xsl:text>: </xsl:text>
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="/Document/Manager/user"/>
				</xsl:call-template>
			
			</xsl:if>
			
			<xsl:value-of select="$dateSpacer"/>
			<xsl:value-of select="$i18n.EditDate" />
			<xsl:text>: </xsl:text>
			<xsl:value-of select="/Document/EditDate"/>
		
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template name="printUser">
		<xsl:param name="user" />
		
		<xsl:value-of select="$user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$user/lastname" />
		
	</xsl:template>
	
	<xsl:template match="SignEvents">
	
		<div class="signed query">
		
			<a name="signed" />
			
			<h2>
				<xsl:value-of select="$i18n.Signed.Title"/>
			</h2>
			
			<p><xsl:value-of select="$i18n.Signed.Description"/></p>
			
			<xsl:apply-templates select="FlowInstanceEvent" mode="signed" />
		
		</div>
	
	</xsl:template>
	
	<xsl:template match="FlowInstanceEvent" mode="signed">
	
		<div class="bigmarginbottom">
		
			<div>
				<strong><xsl:value-of select="$i18n.Signed.Name"/>:&#160;</strong>
				
				<xsl:choose>
					<xsl:when test="poster/user">
						<xsl:value-of select="poster/user/firstname"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="poster/user/lastname"/>
					</xsl:when>
					<xsl:when test="Attributes/Attribute[Name = 'name']">
						<xsl:value-of select="Attributes/Attribute[Name = 'name']/Value"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="Attributes/Attribute[Name = 'firstname']/Value"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="Attributes/Attribute[Name = 'lastname']/Value"/>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			
			<xsl:variable name="showCitizenID">
				<xsl:call-template name="showCitizenIDInSignEvents"/>
			</xsl:variable>
			
			<xsl:if test="$showCitizenID = 'true'">
				<div>
					<strong><xsl:value-of select="$i18n.Signed.CitizenIdentifier"/>:&#160;</strong>
					<xsl:value-of select="Attributes/Attribute[Name = 'citizenIdentifier']/Value"/>
				</div>
			</xsl:if>
			
			<div>
				<strong><xsl:value-of select="$i18n.Signed.Date"/>:&#160;</strong>
				<xsl:value-of select="added"/>
			</div>
			
			<div>
				<strong><xsl:value-of select="$i18n.Signed.Checksum"/>:&#160;</strong>
				<xsl:value-of select="Attributes/Attribute[Name = 'signingChecksum']/Value"/>
			</div>
			
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>