<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="Document">
		
		<html xmlns="http://www.w3.org/1999/xhtml">
			
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
				
				<title>
					<xsl:value-of select="$i18n.Signatures" />
					<xsl:text> - </xsl:text>
					<xsl:value-of select="Signatures/ActivityGroup/name" />
					<xsl:text> - </xsl:text>
					<xsl:value-of select="$i18n.FlowInstanceID" />
					<xsl:text> #</xsl:text>
					<xsl:value-of select="Signatures/FlowInstance/flowInstanceID" />
				</title>
				
				<link rel="stylesheet" type="text/css" href="classpath://com/nordicpeak/flowengine/flowapprovalmodule/staticcontent/css/pdf.css" />
				
			</head>
			
			<body>
			
				<xsl:apply-templates select="Signatures" />
				
				<div id="pagenumber-container">
					<xsl:text>Sida </xsl:text>
					<span id="pagenumber"/>
					<xsl:text> av </xsl:text>
					<span id="pagecount"/>
				</div>
				
			</body>
		</html>
		
	</xsl:template>
	
	<xsl:template match="Signatures">
		
		<div class="signatures">
			<h2>
				<xsl:value-of select="ActivityGroup/name" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$i18n.Signatures" />
			</h2>
			
			<div>
				<strong>
					<xsl:value-of select="$i18n.Flow" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:value-of select="FlowInstance/Flow/name" />
			</div>
			
			<div>
				<strong>
					<xsl:value-of select="$i18n.FlowInstanceID" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:value-of select="FlowInstance/flowInstanceID" />
			</div>
			
			<div>
				<strong>
					<xsl:value-of select="$i18n.ActivityRound.added" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:value-of select="ActivityRound/added" />
			</div>
			
			<div>
				<strong>
					<xsl:value-of select="$i18n.ActivityRound.completed" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:value-of select="ActivityRound/completed" />
			</div>
			
			<p>
				<xsl:value-of select="$i18n.Signatures.description" />
			</p>
			
			<div class="bigmargintop">
				<xsl:apply-templates select="ActivityRound/ActivityProgresses/ActivityProgress" mode="signature" />
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="ActivityProgress" mode="signature">
		
		<div class="signature">
		
			<h3 class="nopadding">
				<xsl:value-of select="Activity/name" />
			</h3>
			
			<div>
				<strong>
					<xsl:value-of select="$java.Signing.ActivityProgress.State" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:choose>
					<xsl:when test="denied = 'true'">
						
						<xsl:choose>
							<xsl:when test="../../../ActivityGroup/deniedText">
								<xsl:value-of select="../../../ActivityGroup/deniedText" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.ActivityProgress.denied" />
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:when>
					<xsl:otherwise>
						
						<xsl:choose>
							<xsl:when test="../../../ActivityGroup/useApproveDeny = 'true'">
							
								<xsl:choose>
									<xsl:when test="../../../ActivityGroup/approvedText">
										<xsl:value-of select="../../../ActivityGroup/approvedText" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.ActivityProgress.approved" />
									</xsl:otherwise>
								</xsl:choose>
								
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.ActivityProgress.complete" />
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:otherwise>
				</xsl:choose>
			</div>
			
			<xsl:if test="ShortDescription">
				<div>
					<strong>
						<xsl:value-of select="$i18n.ActivityProgress.shortDescription" />
						<xsl:text>:&#160;</xsl:text>
					</strong>
					
					<xsl:value-of select="ShortDescription" />
				</div>
			</xsl:if>
			
			<xsl:if test="Activity/description">
				<div class="bigmarginbottom">
					<strong>
						<xsl:value-of select="$i18n.Activity.description" />
					</strong>
					
					<p style="margin-top: 0;">
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="Activity/description"/>
						</xsl:call-template>
					</p>
				</div>
			</xsl:if>
			
			<xsl:if test="Activity/showFlowInstance = 'true'">
				<div class="service">
					<strong>
						<xsl:value-of select="$java.Signing.flowInstanceWasVisible" />
					</strong>
				</div>
			</xsl:if>
			
			<div>
				<strong>
					<xsl:value-of select="$i18n.ActivityProgress.comment" />
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="comment"/>
				</xsl:call-template>
			</div>
			
			<div>
				<strong>
					<xsl:choose>
						<xsl:when test="signedDate">
							<xsl:value-of select="$java.Signing.user" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.ActivityProgress.CompletingUser" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>:&#160;</xsl:text>
				</strong>
				
				<xsl:value-of select="CompletingUser/firstname" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="CompletingUser/lastname" />
				
				<xsl:if test="signedDate">
					<xsl:text> (</xsl:text>
					<xsl:value-of select="CompletingUserCitizenID" />
					<xsl:text>)</xsl:text>
				</xsl:if>
			</div>
			
			<xsl:choose>
				<xsl:when test="signedDate">
					
					<div>
						<strong><xsl:value-of select="$i18n.Signature.date"/>:&#160;</strong>
						<xsl:value-of select="signedDate"/>
					</div>
					
					<div>
						<strong><xsl:value-of select="$i18n.Signature.checksum"/>:&#160;</strong>
						<xsl:value-of select="signedChecksum"/>
					</div>
					
				</xsl:when>
				<xsl:otherwise>
					
					<div>
						<strong><xsl:value-of select="$i18n.ActivityProgress.completed"/>:&#160;</strong>
						<xsl:value-of select="completed"/>
					</div>
					
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>