<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>
		
	<xsl:template match="Document">
		
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
				<title>Ärendenummer #<xsl:value-of select="FlowInstance/flowInstanceID"/></title>
				<link rel="stylesheet" type="text/css" href="classpath://com/nordicpeak/flowengine/pdf/staticcontent/css/flowinstance.css" />
				
				<xsl:apply-templates select="StyleSheets/StyleSheet"/>
				
				<bookmarks>
					<xsl:apply-templates select="ManagerResponses/PDFManagerResponse" mode="bookmark"/>
				</bookmarks>
			</head>
			<body>
			
				<div id="footer">
					<xsl:text>Ärendenummer: #</xsl:text>
					<xsl:value-of select="FlowInstance/flowInstanceID"/>
					
					<xsl:text> | Inskickat av: </xsl:text>
					<xsl:call-template name="PrintPostedBy">
						<xsl:with-param name="poster" select="FlowInstance/poster/user"/>
						<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
					</xsl:call-template>
					
					<xsl:if test="Signed = 'true'">
						<xsl:text> (signerad)</xsl:text>
					</xsl:if>
					
					<xsl:text> | Datum: </xsl:text>
					<xsl:value-of select="SubmitDate"/>
				</div>
			    
				<div id="pagenumber-container">
				    Sida <span id="pagenumber"></span> av <span id="pagecount"></span>
				</div>
							
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
						<p>
							<xsl:text>Ärendenummer: #</xsl:text>
							<xsl:value-of select="FlowInstance/flowInstanceID"/>
							
							<xsl:text> | Inskickat av: </xsl:text>
							<xsl:call-template name="PrintPostedBy">
	 							<xsl:with-param name="poster" select="FlowInstance/poster/user"/>
	 							<xsl:with-param name="flowInstanceAttributes" select="FlowInstance/Attributes"/>
	 						</xsl:call-template>
							
							<xsl:text> | Datum: </xsl:text>
							<xsl:value-of select="SubmitDate"/>
						</p>
					</div>
					
				</div>	
				<div class="content">
		
					<xsl:apply-templates select="ManagerResponses/PDFManagerResponse" mode="xhtml"/>
		
				</div>
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
	
		<xsl:value-of select="XHTML" disable-output-escaping="yes"/>
	
	</xsl:template>	
	
	<xsl:template match="StyleSheet">
	
		<link rel="stylesheet" type="text/css" href="{.}" />
	
	</xsl:template>
	
	<xsl:template name="PrintPostedBy">
		<xsl:param name="poster"/>
		<xsl:param name="flowInstanceAttributes"/>
		
		<xsl:choose>
		
			<xsl:when test="$flowInstanceAttributes/Attribute[Name = 'organizationName']">
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'organizationName']/Value" />
				
				<xsl:choose>
					<xsl:when test="$poster">
						<xsl:text>&#160;(</xsl:text>
						<xsl:call-template name="printUser">
							<xsl:with-param name="user" select="$poster" />
						</xsl:call-template>
						<xsl:text>)</xsl:text>
					</xsl:when>
					
					<xsl:when test="$flowInstanceAttributes/Attribute[Name = 'firstname']">
						<xsl:text>&#160;(</xsl:text>
						<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'firstname']/Value" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'lastname']/Value" />
						<xsl:text>)</xsl:text>
					</xsl:when>	
				</xsl:choose>
				
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:call-template name="PrintPostedByNoOrg">
					<xsl:with-param name="poster" select="$poster"/>
					<xsl:with-param name="flowInstanceAttributes" select="$flowInstanceAttributes"/>
				</xsl:call-template>
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="PrintPostedByNoOrg">
		<xsl:param name="poster"/>
		<xsl:param name="flowInstanceAttributes"/>
		
		<xsl:choose>
		
			<xsl:when test="$poster">
				<xsl:call-template name="printUser">
					<xsl:with-param name="user" select="$poster" />
				</xsl:call-template>
			</xsl:when>
				
			<xsl:when test="$flowInstanceAttributes/Attribute[Name = 'firstname']">
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'firstname']/Value" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$flowInstanceAttributes/Attribute[Name = 'lastname']/Value" />
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="$i18n.AnonymousUser" />
			</xsl:otherwise>
			
		</xsl:choose>
	
	</xsl:template>
	
	<xsl:template name="printUser">
		<xsl:param name="user" />
		
		<xsl:value-of select="$user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$user/lastname" />
		
	</xsl:template>
	
</xsl:stylesheet>