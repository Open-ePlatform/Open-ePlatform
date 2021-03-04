<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		
	</xsl:template>
		
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
			</h2>
			
			<xsl:if test="Description">
				
				<div class="query-description">
					<xsl:choose>
						<xsl:when test="isHTMLDescription = 'true'">
							<xsl:value-of select="Description" disable-output-escaping="yes"/>
						</xsl:when>
						<xsl:otherwise>
							<p>
								<xsl:value-of select="Description" disable-output-escaping="yes"/>
							</p>
						</xsl:otherwise>
					</xsl:choose>
				</div>
								
			</xsl:if>
			
			<div class="full display-table bigmarginbottom">
			
				<xsl:if test="ChildQueryInstance/citizenIdentifier">
						
					<div class="bigmarginbottom">
						<strong><xsl:value-of select="$i18n.ChosenChild" /></strong>
						<br/>
						
						<xsl:value-of select="ChildQueryInstance/firstname" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="ChildQueryInstance/lastname" />
						<br/>
						
						<xsl:value-of select="$i18n.Column.SocialSecurityNumber" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="ChildQueryInstance/citizenIdentifier" />
						
						<xsl:if test="ChildQueryInstance/ChildQuery/showAddress = 'true'">
							
							<br/>
							<xsl:value-of select="$i18n.Column.Address" />
							<xsl:text>:&#160;</xsl:text>
							
							<xsl:if test="ChildQueryInstance/address">
								<xsl:value-of select="ChildQueryInstance/address" />
								<br/>
							</xsl:if>
							
							<xsl:if test="ChildQueryInstance/zipcode or ChildQueryInstance/postalAddress">
								<xsl:if test="ChildQueryInstance/zipcode">
									<xsl:value-of select="ChildQueryInstance/zipcode" />
									<xsl:text>&#160;</xsl:text>
								</xsl:if>
								
								<xsl:value-of select="ChildQueryInstance/postalAddress" />
							</xsl:if>
							
						</xsl:if>
						
						<xsl:apply-templates select="ChildQueryFilterEndpoint">
							<xsl:with-param name="attributes" select="ChildQueryInstance/ChildAttributes" />
							<xsl:with-param name="selectedAttributes" select="ChildQueryInstance/ChildQuery/SelectedChildAttributes" />
						</xsl:apply-templates>
						
					</div>
					
				</xsl:if>
				
			</div>
			
		</div>
		
		<xsl:if test="ChildQueryInstance/citizenIdentifier">
		
			<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true' or ChildQueryInstance/ChildQuery/alwaysShowOtherGuardians = 'true'">
					
					<div class="query">
						<strong class="marginbottom">
							<xsl:value-of select="$i18n.Guardians"/>
						</strong>
						
						<xsl:apply-templates select="ChildQueryInstance/Guardians/Guardian" mode="show"/>
					</div>
					
				</xsl:if>
		
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="Guardian" mode="show">
	
		<div class="bigmarginbottom">
		
			<xsl:value-of select="firstname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="lastname" />
			
			<xsl:variable name="showAddress" select="../../ChildQuery/showGuardianAddress"/>
			<xsl:variable name="hideSSN" select="../../ChildQuery/hideSSNForOtherGuardians"/>
			
			<xsl:if test="$showAddress = 'true' or $hideSSN = 'false'">
				<div class="bigmarginleft">
					<xsl:if test="$hideSSN = 'false'">
						<xsl:value-of select="$i18n.Column.SocialSecurityNumber" />
						<xsl:text>:&#160;</xsl:text>
						<xsl:value-of select="citizenIdentifier" />
					</xsl:if>
					
					<xsl:if test="$hideSSN = 'false' and $showAddress = 'true'">
						<br/>
					</xsl:if>
					
					<xsl:if test="$showAddress = 'true'">
						<xsl:value-of select="$i18n.Column.Address" />
						<xsl:text>:&#160;</xsl:text>
						
						<xsl:if test="address">
							<xsl:value-of select="address" />
							<br/>
						</xsl:if>
						
						<xsl:if test="zipcode or postalAddress">
							<xsl:if test="zipcode">
								<xsl:value-of select="zipcode" />
								<xsl:text>&#160;</xsl:text>
							</xsl:if>
							
							<xsl:value-of select="postalAddress" />
						</xsl:if>
					</xsl:if>
				</div>
			</xsl:if>
		</div>
	
	</xsl:template>
	
	<xsl:template match="ChildQueryFilterEndpoint">
		<xsl:param name="attributes" />
		<xsl:param name="selectedAttributes" />
	
		<xsl:variable name="endpointFields" select="Fields"/>
	
		<xsl:for-each select="$selectedAttributes/SelectedAttribute">
			
			<xsl:if test="$endpointFields/value[. = current()/name]">
				
				<xsl:variable name="value" select="$attributes/Attribute[name = current()/name]/value"/>
				
				<xsl:if test="displayMode = 'ALWAYS' or (displayMode = 'IF_VALUE' and $value != '')">
				
					<br/>
					<xsl:value-of select="name" />
					<xsl:text>:&#160;</xsl:text>
					<xsl:value-of select="$value" />
				
				</xsl:if>
				
			</xsl:if>
		
		</xsl:for-each>
	
	</xsl:template>

</xsl:stylesheet>