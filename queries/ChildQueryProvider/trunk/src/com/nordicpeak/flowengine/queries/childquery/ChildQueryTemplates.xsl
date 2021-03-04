<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/childquery.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/childquery.css
	</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="ChildQueryQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article class="childquery show-mode">
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="ChildQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="ChildQueryInstance/ChildQuery/queryID" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="ChildQueryInstance/ChildQuery/description">
					<span class="italic">
						<xsl:value-of select="ChildQueryInstance/ChildQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<fieldset>
				
					<xsl:if test="ChildQueryInstance/citizenIdentifier">
					
						<div>
							<h3><xsl:value-of select="$i18n.ChosenChild" /></h3>
					
							<div class="marginleft">
								<xsl:value-of select="ChildQueryInstance/firstname" />
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="ChildQueryInstance/lastname" />
								
								<p class="tiny">
								
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
									
								</p>
							</div>
						</div>
						
					</xsl:if>
					
				</fieldset>
				
			</article>
			
			<xsl:if test="ChildQueryInstance/citizenIdentifier">
			
				<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true' or ChildQueryInstance/ChildQuery/alwaysShowOtherGuardians = 'true'">
				
					<article class="childquery show-mode">
					
						<div>
							<h3><xsl:value-of select="$i18n.Guardians"/></h3>
							
							<table class="full">
								<thead>
									<tr>
										<th>
											<xsl:value-of select="$i18n.Column.Firstname"/>
										</th>
										<th>
											<xsl:value-of select="$i18n.Column.Lastname"/>
										</th>
										
										<xsl:if test="ChildQueryInstance/ChildQuery/hideSSNForOtherGuardians = 'false'">
											<th>
												<xsl:value-of select="$i18n.Column.SocialSecurityNumber"/>
											</th>
										</xsl:if>
										
										<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true'">
											<th>
												<xsl:value-of select="$i18n.Column.Email"/>
											</th>
											<th>
												<xsl:value-of select="$i18n.Column.Phone"/>
											</th>
										</xsl:if>
										
										<xsl:if test="ChildQueryInstance/ChildQuery/showGuardianAddress = 'true'">
											<th>
												<xsl:value-of select="$i18n.Column.Address"/>
											</th>
											<th>
												<xsl:value-of select="$i18n.Column.Zipcode"/>
											</th>
											<th>
												<xsl:value-of select="$i18n.Column.PostalAddress"/>
											</th>
										</xsl:if>
									</tr>
								</thead>
								
								<tbody>
									<xsl:apply-templates select="ChildQueryInstance/Guardians/Guardian" mode="show"/>
								</tbody>
								
							</table>
							
							<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true' and count(ChildQueryInstance/Guardians/Guardian) > 1">
								<p class="tiny">
									<xsl:value-of select="$i18n.OtherGuardiansNotificationInfo"/>
								</p>
							</xsl:if>
					
						</div>
				
					</article>
				
				</xsl:if>
			
			</xsl:if>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="Guardian" mode="show">
	
		<tr>
			<td data-title="{$i18n.Column.Firstname}">
				<xsl:choose>
					<xsl:when test="firstname"><xsl:value-of select="firstname"/></xsl:when>
					<xsl:otherwise>-</xsl:otherwise>
				</xsl:choose>
			</td>
			<td data-title="{$i18n.Column.Lastname}">
				<xsl:choose>
					<xsl:when test="lastname"><xsl:value-of select="lastname"/></xsl:when>
					<xsl:otherwise>-</xsl:otherwise>
				</xsl:choose>
			</td>
			
			<xsl:if test="../../ChildQuery/hideSSNForOtherGuardians = 'false'">
				<td data-title="{$i18n.Column.SocialSecurityNumber}">
					<xsl:choose>
						<xsl:when test="citizenIdentifier"><xsl:value-of select="citizenIdentifier"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<xsl:if test="../../ChildQuery/useMultipartSigning = 'true'">
				<td data-title="{$i18n.Column.Email}">
					<xsl:choose>
						<xsl:when test="email"><xsl:value-of select="email"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
				<td data-title="{$i18n.Column.Phone}">
					<xsl:choose>
						<xsl:when test="phone"><xsl:value-of select="phone"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			
			<xsl:if test="../../ChildQuery/showGuardianAddress = 'true'">
				<td data-title="{$i18n.Column.Address}">
					<xsl:choose>
						<xsl:when test="address"><xsl:value-of select="address"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
				<td data-title="{$i18n.Column.Zipcode}">
					<xsl:choose>
						<xsl:when test="zipcode"><xsl:value-of select="zipcode"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
				<td data-title="{$i18n.Column.PostalAddress}">
					<xsl:choose>
						<xsl:when test="postalAddress"><xsl:value-of select="postalAddress"/></xsl:when>
						<xsl:otherwise>-</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
		</tr>
	
	</xsl:template>
		
	<xsl:template match="ShowQueryForm">
	
		<xsl:variable name="shortQueryID" select="concat('q', ChildQueryInstance/ChildQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', ChildQueryInstance/ChildQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
			
			<xsl:variable name="validationErrors" select="ValidationErrors/validationError[messageKey and messageKey != 'EmailOrPhoneRequired' and messageKey != 'EmailVerificationMismatch'  and messageKey != 'PhoneVerificationMismatch']"/>
		
			<xsl:if test="$validationErrors">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
					
						<xsl:apply-templates select="$validationErrors"/>
						
						<div class="marker"/>
					</div>
				</div>
			</xsl:if>
			
			<article class="childquery">
			
				<xsl:if test="$validationErrors">
					<xsl:attribute name="class">childquery error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="ChildQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="ChildQueryInstance/ChildQuery/helpText">
						<xsl:apply-templates select="ChildQueryInstance/ChildQuery/helpText" />
					</xsl:if>
				
				</div>

				<span class="italic">
					<xsl:if test="ChildQueryInstance/ChildQuery/description">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="ChildQueryInstance/ChildQuery/description" disable-output-escaping="yes" />
					</xsl:if>
						
					<xsl:if test="ChildQueryInstance/hasChildrenUnderSecrecy = 'true'">
						<p class="tiny">
							<xsl:value-of select="$i18n.SecretChildrenInfo"/>
						</p>
					</xsl:if>
					
					<xsl:if test="ChildQueryInstance/ageFilteredChildren = 'true' or (ChildQueryInstance/ChildQuery/minAge and ChildQueryInstance/Children/Child[current()/ChildQueryInstance/ChildQuery/minAge > Age]) or (ChildQueryInstance/ChildQuery/maxAge and ChildQueryInstance/Children/Child[Age > current()/ChildQueryInstance/ChildQuery/maxAge])">
						<p class="tiny">
							<xsl:value-of select="$i18n.AgeChildrenInfo"/>
						</p>
					</xsl:if>
				</span>
				
				<fieldset>
				
					<xsl:choose>
						<xsl:when test="ChildQueryInstance/Children/Child">	<!-- Children from provider -->
							
							<div>
								<xsl:apply-templates select="ChildQueryInstance/Children/Child"/>
							</div>
							
							<xsl:if test="ChildQueryInstance/ChildQuery/filteredChildrenDescription and ChildQueryInstance/filteredChildrenText">
								<p class="clearboth">
									<xsl:call-template name="replaceLineBreak">
										<xsl:with-param name="string" select="ChildQueryInstance/filteredChildrenText" />
									</xsl:call-template>
								</p>
							</xsl:if>
							
						</xsl:when>
						<xsl:when test="ChildQueryInstance/Children">	<!-- Empty list from provider -->
							
							<xsl:value-of select="$i18n.Error.NoChildren"/>
							
						</xsl:when>
						<xsl:when test="ChildQueryInstance/citizenIdentifier">	<!-- Editing saved instance as admin -->
							
							<div>
								<xsl:call-template name="SavedChild"/>
							</div>
							
						</xsl:when>
						<xsl:otherwise>	<!-- No list from provider -->
						
							<span class="italic html-description">
								<p class="error">
									<xsl:choose>
										<xsl:when test="ChildQueryInstance/FetchChildrenException/IncompleteFilterAPIDataException and ChildQueryInstance/ChildQuery/emptyFilterDescription">
											<xsl:call-template name="replaceLineBreak">
												<xsl:with-param name="string" select="ChildQueryInstance/ChildQuery/emptyFilterDescription" />
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="ChildQueryInstance/FetchChildrenException/CommunicationException">
											<xsl:choose>
												<xsl:when test="ChildQueryInstance/ChildQuery/communicationErrorDescription">
													<xsl:value-of select="ChildQueryInstance/ChildQuery/communicationErrorDescription"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$i18n.Error.Provider.CommunicationError"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:when test="ChildQueryInstance/FetchChildrenException/IncompleteDataException or ChildQueryInstance/FetchChildrenException/IncompleteFilterAPIDataException"><xsl:value-of select="$i18n.Error.Provider.IncompleteData"/></xsl:when>
										<xsl:when test="ChildQueryInstance/FetchChildrenException/InvalidCitizenIdentifierException"><xsl:value-of select="$i18n.Error.Provider.InvalidCitizenIdentifier"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$i18n.Error.Provider.Unknown"/></xsl:otherwise>
									</xsl:choose>
								</p>
							</span>
							
						</xsl:otherwise>
					</xsl:choose>
					
				</fieldset>
				
			</article>
				
			<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true' or ChildQueryInstance/ChildQuery/alwaysShowOtherGuardians = 'true'">
				
				<xsl:variable name="guardianValidationErrors" select="ValidationErrors/validationError[fieldName or messageKey = 'EmailOrPhoneRequired']"/>
				
				<xsl:if test="$guardianValidationErrors">
					<div id="{$queryID}-validationerrors" class="validationerrors">
						<div class="info-box error">
						
							<xsl:choose>
								<xsl:when test="ValidationErrors/validationError[messageKey = 'EmailOrPhoneRequired']">
									<xsl:apply-templates select="ValidationErrors/validationError[messageKey = 'EmailOrPhoneRequired']"/>
								</xsl:when>
								<xsl:otherwise>
									<span/>
								</xsl:otherwise>
							</xsl:choose>
							
							<div class="marker"/>
						</div>
					</div>
				</xsl:if>
				
				<article class="childquery otherguardians">
	
					<xsl:if test="$guardianValidationErrors">
						<xsl:attribute name="class">childquery otherguardians error</xsl:attribute>
					</xsl:if>
	
						<h3 class="marginbottom"><xsl:value-of select="$i18n.OtherGuardians"/></h3>
						
						<xsl:if test="ChildQueryInstance/ChildQuery/otherGuardiansDescription">
							<span class="italic">
								<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
								<xsl:value-of select="ChildQueryInstance/ChildQuery/otherGuardiansDescription" disable-output-escaping="yes" />
							</span>
						</xsl:if>
						
						<xsl:choose>
							<xsl:when test="ChildQueryInstance/Children">
							
								<!-- TODO Do the filtering in java instead and don't append unnecessary information to the XML document  -->
								<xsl:apply-templates select="ChildQueryInstance/Children/Child/Guardians/Guardian[not(citizenIdentifier=../../preceding-sibling::Child/Guardians/Guardian/citizenIdentifier) and not(citizenIdentifier = /Document/user/SocialSecurityNumber)]">
									<xsl:with-param name="useMultipartSigning" select="ChildQueryInstance/ChildQuery/useMultipartSigning"/>
								</xsl:apply-templates>
	
							</xsl:when>
							<xsl:when test="ChildQueryInstance/citizenIdentifier">
							
								<xsl:apply-templates select="ChildQueryInstance/Guardians/Guardian[not(citizenIdentifier = /Document/user/SocialSecurityNumber)]" mode="manager"/>
						
							</xsl:when>
						</xsl:choose>
						
				</article>
				
			</xsl:if>
			
			<script type="text/javascript">
				initChildQuery('<xsl:value-of select="ChildQueryInstance/ChildQuery/queryID" />');
			</script>
				
		</div>
		
	</xsl:template>
	
	<xsl:template match="Child">
	
		<xsl:variable name="disabled">
			<xsl:choose>
				<xsl:when test="../../ChildQuery/minAge and ../../ChildQuery/minAge > Age">true</xsl:when>
				<xsl:when test="../../ChildQuery/maxAge and Age > ../../ChildQuery/maxAge">true</xsl:when>
			</xsl:choose>
		</xsl:variable>
	
		<div class="alternative">
		
			<xsl:variable name="radioID">
				<xsl:value-of select="'q'"/>
				<xsl:value-of select="../../ChildQuery/queryID"/>
				<xsl:value-of select="'_child_'"/>
				<xsl:value-of select="citizenIdentifier"/>
			</xsl:variable>
		
			<xsl:call-template name="createRadio">
				<xsl:with-param name="id" select="$radioID" />
				<xsl:with-param name="name">
					<xsl:value-of select="'q'"/>
					<xsl:value-of select="../../ChildQuery/queryID"/>
					<xsl:value-of select="'_child'"/>
				</xsl:with-param>
				<xsl:with-param name="value" select="citizenIdentifier"/>
				<xsl:with-param name="checked" select="citizenIdentifier = ../../citizenIdentifier"/>
				<xsl:with-param name="requestparameters" select="../../../requestparameters"/>
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			
			<label for="{$radioID}" class="radio">
			
				<xsl:if test="$disabled = 'true'">
					<xsl:attribute name="class">radio disabled</xsl:attribute>
				</xsl:if>
			
				<span>
					<xsl:value-of select="firstname" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="lastname" />
				</span>
				
				<p class="tiny">
				
					<xsl:value-of select="$i18n.Column.SocialSecurityNumber" />
					<xsl:text>:&#160;</xsl:text>
					<xsl:value-of select="citizenIdentifier" />
					
					<xsl:if test="../../ChildQuery/showAddress = 'true'">
					
						<br/>
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
					
					<xsl:apply-templates select="../../../ChildQueryFilterEndpoint">
						<xsl:with-param name="attributes" select="Attributes" />
						<xsl:with-param name="selectedAttributes" select="../../ChildQuery/SelectedChildAttributes" />
					</xsl:apply-templates>
					
				</p>
			</label>
			
			<div class="guardians" style="display: none;">
				<xsl:for-each select="Guardians/Guardian[not(citizenIdentifier = /Document/user/SocialSecurityNumber)]">
					<div>
						<xsl:choose>
							<xsl:when test="citizenIdentifier">
								<xsl:value-of select="citizenIdentifier"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="generate-id(.)"/>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</xsl:for-each>
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="SavedChild">
	
		<div class="alternative">

			<xsl:variable name="radioID">
				<xsl:value-of select="'q'"/>
				<xsl:value-of select="ChildQueryInstance/ChildQuery/queryID"/>
				<xsl:value-of select="'_child_'"/>
				<xsl:value-of select="ChildQueryInstance/citizenIdentifier"/>
			</xsl:variable>
		
			<xsl:call-template name="createRadio">
				<xsl:with-param name="id" select="$radioID" />
				<xsl:with-param name="name">
					<xsl:value-of select="'q'"/>
					<xsl:value-of select="ChildQueryInstance/ChildQuery/queryID"/>
					<xsl:value-of select="'_child'"/>
				</xsl:with-param>
				<xsl:with-param name="value" select="ChildQueryInstance/citizenIdentifier"/>
				<xsl:with-param name="checked" select="true()"/>
				<xsl:with-param name="requestparameters" select="requestparameters"/>
			</xsl:call-template>
			
			<label for="{$radioID}" class="radio">
				<span>
					<xsl:value-of select="ChildQueryInstance/firstname" />
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="ChildQueryInstance/lastname" />
				</span>
				
				<p class="tiny">
				
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
					
				</p>
			</label>
			
			<div class="guardians" style="display: none;">
				<xsl:for-each select="ChildQueryInstance/Guardians/Guardian[not(citizenIdentifier = /Document/user/SocialSecurityNumber)]">
					<div><xsl:value-of select="citizenIdentifier"/></div>
				</xsl:for-each>
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="Guardian">
		<xsl:param name="useMultipartSigning" />
	
		<xsl:variable name="citizenIdentifier" select="citizenIdentifier"/>
		
		<xsl:variable name="jsCitizenID">
			<xsl:choose>
				<xsl:when test="citizenIdentifier">
					<xsl:value-of select="citizenIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="generate-id(.)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	
		<div class="guardian clearboth floatleft" data-citizenid="{$jsCitizenID}">
	
			<xsl:choose>
				<xsl:when test="$useMultipartSigning = 'true' and not(citizenIdentifier)">
					
					<span><xsl:value-of select="$i18n.Error.Provider.IncompleteData"/></span>
					
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:variable name="fieldID">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="../../../../ChildQuery/queryID"/>
						<xsl:value-of select="'_guardian_'"/>
						<xsl:value-of select="citizenIdentifier"/>
					</xsl:variable>
				
						<div>
							<strong>
								<xsl:value-of select="firstname" />
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="lastname" />
							</strong>
							
							<xsl:variable name="showAddress" select="../../../../ChildQuery/showGuardianAddress"></xsl:variable>
							<xsl:variable name="hideSSN" select="../../../../ChildQuery/hideSSNForOtherGuardians"></xsl:variable>
							
							<xsl:if test="$showAddress = 'true' or $hideSSN = 'false'">
								<p class="tiny">
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
								</p>
							</xsl:if>
						</div>
						
						<xsl:if test="../../../../ChildQuery/useMultipartSigning = 'true'">
						
							<fieldset>
								<xsl:variable name="emailID">
									<xsl:value-of select="$fieldID"/>
									<xsl:value-of select="'_email'"/>
								</xsl:variable>
								
								<xsl:variable name="classEmail">
									<xsl:if test="../../../../../ValidationErrors/validationError[fieldName = $emailID]">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<div class="split {$classEmail}">
								
									<label>
										<xsl:if test="../../../../ChildQuery/requireGuardianEmail = 'true'">
											<xsl:attribute name="class">
												<xsl:text>required</xsl:text>
											</xsl:attribute>
										</xsl:if>
										
										<xsl:value-of select="$i18n.Column.Email"/>
									</label>
									
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="$emailID"/>
										<xsl:with-param name="title" select="$i18n.Column.Email"/>
										<xsl:with-param name="value">
											<xsl:choose>
												<xsl:when test="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]">
													<xsl:value-of select="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]/email"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="email"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:with-param>
										<xsl:with-param name="requestparameters" select="../../../../../requestparameters"/>
										<xsl:with-param name="class" select="$classEmail"/>
										<xsl:with-param name="size" select="'50'"/>
									</xsl:call-template>
									
									<xsl:apply-templates select="../../../../../ValidationErrors/validationError[fieldName = $emailID]"/>
								
								</div>
								
								<xsl:if test="../../../../ChildQuery/requireGuardianContactInfoVerification = 'true'">
								
									<xsl:variable name="emailID2">
										<xsl:value-of select="$fieldID"/>
										<xsl:value-of select="'_email2'"/>
									</xsl:variable>
									
									<xsl:variable name="classEmail2">
										<xsl:text>disablepaste odd</xsl:text>
										<xsl:if test="../../../../../ValidationErrors/validationError[fieldName = $emailID2]">
											<xsl:text> invalid input-error</xsl:text>
										</xsl:if>
									</xsl:variable>
									
									<div class="split {$classEmail2}">
									
										<label>
											<xsl:if test="../../../../ChildQuery/requireGuardianEmail = 'true'">
												<xsl:attribute name="class">
													<xsl:text>required</xsl:text>
												</xsl:attribute>
											</xsl:if>
											
											<xsl:value-of select="$i18n.Column.Email"/>
											<xsl:text>&#160;</xsl:text>
											<xsl:value-of select="$i18n.Confirmation"/>
										</label>
										
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="$emailID2"/>
											<xsl:with-param name="title" select="$i18n.Column.Email"/>
											<xsl:with-param name="value">
												<xsl:choose>
													<xsl:when test="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]">
														<xsl:value-of select="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]/email"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="email"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>
											<xsl:with-param name="requestparameters" select="../../../../../requestparameters"/>
											<xsl:with-param name="class" select="$classEmail2"/>
											<xsl:with-param name="size" select="'50'"/>
										</xsl:call-template>
										
										<xsl:apply-templates select="../../../../../ValidationErrors/validationError[fieldName = $emailID2]"/>
									
									</div>
								
								</xsl:if>
								
								<xsl:variable name="phoneID">
									<xsl:value-of select="$fieldID"/>
											<xsl:value-of select="'_phone'"/>
								</xsl:variable>
								
								<xsl:variable name="classPhone">
									<xsl:if test="../../../../../ValidationErrors/validationError[fieldName = $phoneID]">
										<xsl:text>invalid input-error</xsl:text>
									</xsl:if>
								</xsl:variable>
								
								<div class="split {$classPhone}">
								
									<label>
										<xsl:if test="../../../../ChildQuery/requireGuardianPhone = 'true'">
											<xsl:attribute name="class">
												<xsl:text>required</xsl:text>
											</xsl:attribute>
										</xsl:if>
										
										<xsl:value-of select="$i18n.Column.Phone"/>
									</label>
							
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="$phoneID"/>
										<xsl:with-param name="title" select="$i18n.Column.Phone"/>
										<xsl:with-param name="value">
											<xsl:choose>
												<xsl:when test="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]">
													<xsl:value-of select="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]/phone"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="phone"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:with-param>
										<xsl:with-param name="requestparameters" select="../../../../../requestparameters"/>
										<xsl:with-param name="class" select="$classPhone"/>
										<xsl:with-param name="size" select="'50'"/>
									</xsl:call-template>
									
									<xsl:apply-templates select="../../../../../ValidationErrors/validationError[fieldName = $phoneID]"/>
									
								</div>
								
								<xsl:if test="../../../../ChildQuery/requireGuardianContactInfoVerification = 'true'">
																		
									<xsl:variable name="phoneID2">
										<xsl:value-of select="$fieldID"/>
										<xsl:value-of select="'_phone2'"/>
									</xsl:variable>
									
									<xsl:variable name="classPhone2">
										<xsl:text>disablepaste</xsl:text>
										<xsl:if test="../../../../../ValidationErrors/validationError[fieldName = $phoneID2]">
											<xsl:text> invalid input-error</xsl:text>
										</xsl:if>
									</xsl:variable>
									
									<div class="split odd {$classPhone2}">
									
										<label>
											<xsl:if test="../../../../ChildQuery/requireGuardianPhone = 'true'">
												<xsl:attribute name="class">
													<xsl:text>required</xsl:text>
												</xsl:attribute>
											</xsl:if>
											
											<xsl:value-of select="$i18n.Column.Phone"/>
											<xsl:text>&#160;</xsl:text>
											<xsl:value-of select="$i18n.Confirmation"/>
										</label>
								
										<xsl:call-template name="createTextField">
											<xsl:with-param name="name" select="$phoneID2"/>
											<xsl:with-param name="title" select="$i18n.Column.Phone"/>
											<xsl:with-param name="value">
												<xsl:choose>
													<xsl:when test="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]">
														<xsl:value-of select="../../../../Guardians/Guardian[citizenIdentifier = $citizenIdentifier]/phone"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="phone"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>
											<xsl:with-param name="requestparameters" select="../../../../../requestparameters"/>
											<xsl:with-param name="class" select="$classPhone2"/>
											<xsl:with-param name="size" select="'50'"/>
										</xsl:call-template>
										
										<xsl:apply-templates select="../../../../../ValidationErrors/validationError[fieldName = $phoneID2]"/>
									
									</div>
									
								</xsl:if>
						
							</fieldset>
						</xsl:if>
							
				</xsl:otherwise>	
			</xsl:choose>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="Guardian" mode="manager">
	
		<div class="guardian clearboth floatleft marginleft" data-citizenid="{citizenIdentifier}">
	
			<xsl:choose>
				<xsl:when test="not(citizenIdentifier)">
					<span><xsl:value-of select="$i18n.Error.Provider.IncompleteData"/></span>
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:variable name="fieldID">
						<xsl:value-of select="'q'"/>
						<xsl:value-of select="../../ChildQuery/queryID"/>
						<xsl:value-of select="'_guardian_'"/>
						<xsl:value-of select="citizenIdentifier"/>
					</xsl:variable>
				
						<div class="floatleft">
							<strong>
								<xsl:value-of select="firstname" />
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="lastname" />
							</strong>
							
							<xsl:variable name="showAddress" select="../../ChildQuery/showGuardianAddress"></xsl:variable>
							<xsl:variable name="hideSSN" select="../../ChildQuery/hideSSNForOtherGuardians"></xsl:variable>
							
							<xsl:if test="$showAddress = 'true' or $hideSSN = 'false'">
								<p class="tiny">
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
									
								</p>
							</xsl:if>
						</div>
						
						<xsl:if test="../../ChildQuery/useMultipartSigning = 'true'">
						
							<xsl:variable name="emailID">
								<xsl:value-of select="$fieldID"/>
								<xsl:value-of select="'_email'"/>
							</xsl:variable>
							
							<xsl:variable name="classEmail">
								<xsl:if test="../../../ValidationErrors/validationError[fieldName = $emailID]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<div class="clearboth split {$classEmail}">
							
								<label>
									<xsl:if test="../../ChildQuery/requireGuardianEmail = 'true'">
										<xsl:attribute name="class">
											<xsl:text>required</xsl:text>
										</xsl:attribute>
									</xsl:if>
									
									<xsl:value-of select="$i18n.Column.Email"/>
								</label>
						
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="$emailID"/>
									<xsl:with-param name="title" select="$i18n.Column.Email"/>
									<xsl:with-param name="value" select="email"/>
									<xsl:with-param name="requestparameters" select="../../../requestparameters"/>
									<xsl:with-param name="class" select="$classEmail"/>
									<xsl:with-param name="size" select="'50'"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="../../../ValidationErrors/validationError[fieldName = $emailID]"/>
							
							</div>
							
							<xsl:variable name="phoneID">
								<xsl:value-of select="$fieldID"/>
										<xsl:value-of select="'_phone'"/>
							</xsl:variable>
							
							<xsl:variable name="classPhone">
								<xsl:if test="../../../ValidationErrors/validationError[fieldName = $phoneID]">
									<xsl:text>invalid input-error</xsl:text>
								</xsl:if>
							</xsl:variable>
							
							<div class="split odd {$classPhone}">
							
								<label>
									<xsl:if test="../../ChildQuery/requireGuardianPhone = 'true'">
										<xsl:attribute name="class">
											<xsl:text>required</xsl:text>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$i18n.Column.Phone"/>
								</label>
						
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="$phoneID"/>
									<xsl:with-param name="title" select="$i18n.Column.Phone"/>
									<xsl:with-param name="value" select="phone"/>
									<xsl:with-param name="requestparameters" select="../../../requestparameters"/>
									<xsl:with-param name="class" select="$classPhone"/>
									<xsl:with-param name="size" select="'50'"/>
								</xsl:call-template>
								
								<xsl:apply-templates select="../../../ValidationErrors/validationError[fieldName = $phoneID]"/>
							
							</div>
						
						</xsl:if>
							
				</xsl:otherwise>	
			</xsl:choose>
			
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
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<i data-icon-after="!" title="{$i18n.Error.RequiredField}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<i data-icon-after="!">
			<xsl:attribute name="title">
				<xsl:value-of select="$i18n.Error.TooLongFieldContent.part1"/>
				<xsl:value-of select="currentLength"/>
				<xsl:value-of select="$i18n.Error.TooLongFieldContent.part2"/>
				<xsl:value-of select="maxLength"/>
				<xsl:value-of select="$i18n.Error.TooLongFieldContent.part3"/>
			</xsl:attribute>
		</i>
		
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
	
	<xsl:template match="validationError[messageKey = 'EmailVerificationMismatch']">
		
		<i data-icon-after="!" title="{$i18n.Error.EmailVerificationMismatch}"></i>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'PhoneVerificationMismatch']">
		
		<i data-icon-after="!" title="{$i18n.Error.PhoneVerificationMismatch}"></i>
		
	</xsl:template>
		
	<xsl:template match="validationError[messageKey = 'Provider.IncompleteData']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Error.Provider.IncompleteData"/>
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'EmailOrPhoneRequired']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Error.EmailOrPhoneRequired"/>
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'Required']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Error.Required"/>
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'InvalidFormat']">
		
		<span>
			<strong data-icon-before="!">
				<xsl:value-of select="$i18n.Error.InvalidFormatMain"/>
			</strong>
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError">
	
		<xsl:choose>
			<xsl:when test="fieldName != ''">
				
				<i data-icon-after="!" title="{$i18n.Error.UnknownValidationError}"></i>
				
			</xsl:when>
			<xsl:otherwise>
				
				<span>
					<strong data-icon-before="!">
						<xsl:value-of select="$i18n.Error.UnknownValidationError"/>
					</strong>
				</span>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
		
</xsl:stylesheet>