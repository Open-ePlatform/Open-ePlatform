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
						<xsl:with-param name="queryName" select="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name" />
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
							
							<xsl:if test="ChildQueryInstance/ChildQuery/useMultipartSigning = 'true' and skipMultipartSigningIfSameAddress = 'true' and count(ChildQueryInstance/Guardians/Guardian[sameAddressAsPoster != 'true']) > 1">
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
					
						<xsl:choose>
							<xsl:when test="$validationErrors[not(fieldName) and messageKey != 'Required' and messageKey != 'InvalidFormat']">
								<xsl:apply-templates select="ValidationErrors/validationError[not(fieldName) and messageKey != 'Required' and messageKey != 'InvalidFormat' and messageKey != 'EmailOrPhoneRequired']"/>
							</xsl:when>
							<xsl:otherwise>
								<span/>
							</xsl:otherwise>
						</xsl:choose>

						<div class="marker"/>
					</div>
				</div>
			</xsl:if>
			
			<article class="childquery">
			
				<xsl:if test="$validationErrors">
					<xsl:attribute name="class">childquery input-error error floatnone</xsl:attribute>
				</xsl:if>
						
				<fieldset>
					<legend class="heading-wrapper">					
						<h2>
							<xsl:attribute name="class">
								<xsl:if test="ChildQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
									<xsl:text>required</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="ChildQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
						</h2>
					</legend>
					
					<xsl:if test="ChildQueryInstance/ChildQuery/helpText">
						<xsl:apply-templates select="ChildQueryInstance/ChildQuery/helpText" />
					</xsl:if>

					<span class="italic" id="childquery_info_{$queryID}">
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
					
						<xsl:if test="ChildQueryInstance/lockedMessage">
							<p class="full">
								<xsl:value-of select="ChildQueryInstance/lockedMessage"/>
							</p>
						</xsl:if>
						
					</span>
					
					<div class="clearfix"/>
					
					<xsl:apply-templates select="$validationErrors[messageKey and messageKey != 'Provider.IncompleteData']">
						<xsl:with-param name="queryID" select="$queryID"/>
					</xsl:apply-templates>				
					
					<xsl:choose>
						<xsl:when test="ChildQueryInstance/Children/Child">	<!-- Children from provider -->
						
							<xsl:variable name="disabled">
								<xsl:if test="../../locked = 'true' or selectionStatus = 'NOT_SELECTABLE'"><xsl:value-of select="true()"></xsl:value-of></xsl:if>
							</xsl:variable>
						
							<xsl:variable name="isFirstSelectable" select="not($disabled = 'true') and selectionStatus = 'FIRST_SELECTABLE'" />
					
							<div>
								<xsl:apply-templates select="ChildQueryInstance/Children/Child">
									<xsl:with-param name="firstSelectable" select="$isFirstSelectable"/>
									<xsl:with-param name="validationErrors" select="$validationErrors"/>
								</xsl:apply-templates>
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
						<xsl:when test="ChildQueryInstance/citizenIdentifier">	<!-- Editing saved instance as manager -->
							
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

		<xsl:param name="validationErrors" select="null" />
	
		<xsl:variable name="disabled">
			<xsl:if test="../../locked = 'true' or selectionStatus = 'NOT_SELECTABLE'"><xsl:value-of select="true()"></xsl:value-of></xsl:if>
		</xsl:variable>		
		
		<xsl:variable name="isFirstSelectable" select="not($disabled = 'true') and selectionStatus = 'FIRST_SELECTABLE'" />

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
				<xsl:with-param name="class">			
					<xsl:if test="$validationErrors">
						<xsl:text>input-error</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="value" select="citizenIdentifier"/>
				<xsl:with-param name="checked" select="citizenIdentifier = ../../citizenIdentifier"/>
				<xsl:with-param name="requestparameters" select="../../../requestparameters"/>
				<xsl:with-param name="disabled" select="$disabled"/>
				<xsl:with-param name="aria-required">
					<xsl:if test="$isFirstSelectable">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-invalid">
					<xsl:if test="$validationErrors and $isFirstSelectable">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
				<xsl:with-param name="aria-describedby">
					<xsl:if test="$isFirstSelectable">
						<xsl:if test="$validationErrors">
							<xsl:value-of select="'query_'"/>
							<xsl:value-of select="../../ChildQuery/queryID"/>
							<xsl:value-of select="'_childquery-error'"/>
							<xsl:text> </xsl:text>
						</xsl:if>
						<xsl:text>childquery_info_query_</xsl:text>
						<xsl:value-of select="../../ChildQuery/queryID"/>
					</xsl:if>
				</xsl:with-param>
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
					<xsl:if test="../../../../ChildQuery/skipMultipartSigningIfSameAddress != 'true' or sameAddressAsPoster != 'true'">
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
					</xsl:if>
				</xsl:for-each>
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template name="SavedChild">
	
		<xsl:variable name="isLocked" select="ChildQueryInstance/locked = 'true'"/>
	
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
				<xsl:with-param name="disabled" select="$isLocked"/>
				<xsl:with-param name="checked" select="true()"/>
				<xsl:with-param name="requestparameters" select="requestparameters"/>
			</xsl:call-template>
			
			<label for="{$radioID}" class="radio">
				<xsl:attribute name="class">
					<xsl:text>radio</xsl:text>
					
					<xsl:if test="$isLocked"> disabled</xsl:if>
				</xsl:attribute>
			
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
					<xsl:if test="../../ChildQuery/skipMultipartSigningIfSameAddress != 'true' or sameAddressAsPoster != 'true'">
						<div><xsl:value-of select="citizenIdentifier"/></div>
					</xsl:if>
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
								
								<xsl:variable name="fieldName" select="$emailID" />
								
								<xsl:variable name="validationError" select="../../../../../ValidationErrors/validationError[fieldName = $fieldName]"/>
								
								<xsl:variable name="classEmail">
									<xsl:if test="$validationError">
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
										<xsl:with-param name="autocomplete" select="'email'"/>
										<xsl:with-param name="aria-describedby">
											<xsl:if test="$validationError">
												<xsl:value-of select="$fieldName"/>
												<xsl:text>-error </xsl:text>
											</xsl:if>
									
											<xsl:text>emailInfoTextChildQuery</xsl:text>
										</xsl:with-param>
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$validationError">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
									
									<div class="clearfix"/>
							
									<div id="emailInfoTextChildQuery" class="input-info-text">
										<xsl:value-of select="$i18n.Example.EmailFormat"/>
									</div>
								
									<xsl:apply-templates select="$validationError"/>

								</div>
								
								<xsl:if test="../../../../ChildQuery/requireGuardianContactInfoVerification = 'true'">
								
									<xsl:variable name="emailID2">
										<xsl:value-of select="$fieldID"/>
										<xsl:value-of select="'_email2'"/>
									</xsl:variable>
									
									<xsl:variable name="fieldName" select="$emailID2" />
								
									<xsl:variable name="validationError" select="../../../../../ValidationErrors/validationError[fieldName = $fieldName]"/>
									
									<xsl:variable name="classEmail2">
										<xsl:text>disablepaste odd</xsl:text>
										<xsl:if test="$validationError">
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
											<xsl:with-param name="autocomplete" select="'email'"/>
											<xsl:with-param name="aria-describedby">
												<xsl:if test="$validationError">
													<xsl:value-of select="$fieldName"/>
													<xsl:text>-error </xsl:text>
												</xsl:if>
										
												<xsl:text>confirmEmailInfoTextChildQuery</xsl:text>
											</xsl:with-param>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<div class="clearfix"/>
							
										<div id="confirmEmailInfoTextChildQuery" class="input-info-text">
											<xsl:value-of select="$i18n.Example.EmailFormat"/>
										</div>
									
										<xsl:apply-templates select="$validationError"/>
									
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
								
									<xsl:variable name="fieldName" select="$phoneID" />
									
									<xsl:variable name="validationError" select="../../../../../ValidationErrors/validationError[fieldName = $fieldName]"/>
								
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
										<xsl:with-param name="autocomplete" select="'tel'"/>
										<xsl:with-param name="aria-describedby">
											<xsl:if test="$validationError">
												<xsl:value-of select="$fieldName"/>
												<xsl:text>-error </xsl:text>
											</xsl:if>
									
											<xsl:text>phoneInfoTextChildQuery</xsl:text>
										</xsl:with-param>
										<xsl:with-param name="aria-invalid">
											<xsl:if test="$validationError">
												<xsl:text>true</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
									
									<div class="clearfix"/>
									
									<div id="phoneInfoTextChildQuery" class="input-info-text">
										<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
									</div>
										
									<xsl:apply-templates select="../../../../../ValidationErrors/validationError[fieldName = $phoneID]"/>
									
								</div>
								
								<xsl:if test="../../../../ChildQuery/requireGuardianContactInfoVerification = 'true'">
																		
									<xsl:variable name="phoneID2">
										<xsl:value-of select="$fieldID"/>
										<xsl:value-of select="'_phone2'"/>
									</xsl:variable>
									
									<xsl:variable name="fieldName" select="$phoneID2" />
								
									<xsl:variable name="validationError" select="../../../../../ValidationErrors/validationError[fieldName = $fieldName]"/>
									
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
											<xsl:with-param name="autocomplete" select="'tel'"/>
											<xsl:with-param name="aria-describedby">
												<xsl:if test="$validationError">
													<xsl:value-of select="$fieldName"/>
													<xsl:text>-error </xsl:text>
												</xsl:if>
										
												<xsl:text>confirmPhoneInfoTextChildQuery</xsl:text>
											</xsl:with-param>
											<xsl:with-param name="aria-invalid">
												<xsl:if test="$validationError">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<div id="confirmPhoneInfoTextChildQuery" class="input-info-text">
											<xsl:value-of select="$i18n.Example.MobilePhoneFormat"/>
										</div>
										
										<xsl:apply-templates select="$validationError"/>
									
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

		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.RequiredField"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'TooLong']">

		<xsl:variable name="message">
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part1"/>
			<xsl:value-of select="currentLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part2"/>
			<xsl:value-of select="maxLength"/>
			<xsl:value-of select="$i18n.ValidationError.TooLongFieldContent.part3"/>
		</xsl:variable>
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$message"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'EmailVerificationMismatch']">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.EmailVerificationMismatch"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'PhoneVerificationMismatch']">
		
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
			 <xsl:value-of select="$i18n.ValidationError.MobilePhoneVerificationMismatch"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
	
		<div id="{fieldName}-error" class="error">
			<i data-icon-after="!" aria-hidden="true"/>
				
			<xsl:choose>
				<xsl:when test="contains(fieldName, '_email') or contains(fieldName, '_email2')">
					<xsl:value-of select="$i18n.ValidationError.InvalidEmail"/>
				</xsl:when>
				<xsl:when test="contains(fieldName, '_phone') or contains(fieldName, '_phone2')">
					<xsl:value-of select="$i18n.ValidationError.InvalidMobilePhoneNumber"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$i18n.ValidationError.InvalidFormat"/>
				</xsl:otherwise>
			</xsl:choose>
			
		</div>
		
	</xsl:template>
		
	<xsl:template match="validationError[messageKey = 'Provider.IncompleteData']">
		
		<span class="d-flex justify-content-center align-items-center">
			<i data-icon-before="!" aria-hidden="true"/>
			<strong>
				<xsl:value-of select="$i18n.Error.Provider.IncompleteData"/>
			</strong>							
		</span>		
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'EmailOrPhoneRequired']">
		
		<span class="d-flex justify-content-center align-items-center">
			<i data-icon-before="!" aria-hidden="true"/>
			<strong>
				<xsl:value-of select="$i18n.Error.EmailOrPhoneRequired"/>
			</strong>							
		</span>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'Required']">

		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_childquery-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.ValidationError.RequiredQuery"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'InvalidFormat']">
			
		<xsl:param name="queryID" select="null"/>
		
		<div id="{$queryID}_childquery-error" class="validationerror">
			<i data-icon-after="!" aria-hidden="true"/>
			<xsl:value-of select="$i18n.Error.InvalidFormatMain"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:param name="queryID" select="null"/>

		<xsl:choose>
			<xsl:when test="fieldName != ''">
			
				<div id="{$queryID}_childquery-error" class="validationerror">
					<i data-icon-after="!" aria-hidden="true"/>
					
					<xsl:value-of select="$i18n.ValidationError.UnknownValidationError"/>
				</div>

			</xsl:when>
			<xsl:otherwise>
			
				<span class="d-flex justify-content-center align-items-center">
					<i data-icon-before="!" aria-hidden="true"/>
					<strong>
						<xsl:value-of select="i18n.ValidationError.UnknownValidationError"/>
					</strong>							
				</span>	
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
		
</xsl:stylesheet>