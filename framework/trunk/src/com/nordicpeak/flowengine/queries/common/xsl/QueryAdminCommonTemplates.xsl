<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="commonImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/common/pics</xsl:variable>

	<xsl:template name="createCommonFieldsForm">
	
		<xsl:param name="element" />
		<xsl:param name="flowTypeID" select="$element/../flowTypeID" />
	
		<div class="floatleft full bigmarginbottom">
			<label for="name" class="floatleft clearboth"><xsl:value-of select="$i18n.Name" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'name'"/>
					<xsl:with-param name="name" select="'name'"/>
					<xsl:with-param name="title" select="$i18n.Name"/>
					<xsl:with-param name="element" select="$element/QueryDescriptor" />
				</xsl:call-template>
		    </div>
		</div>
	
		<xsl:call-template name="appendCreateCommonFieldsFormConterAfterName"/>	
	
		<div class="floatleft full bigmarginbottom">
			<label for="description" class="floatleft clearboth"><xsl:value-of select="$i18n.Description" /></label>
			<div class="floatleft full">
				<xsl:choose>
					<xsl:when test="/Document/useCKEditorForDescription = 'true'">
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'" />
							<xsl:with-param name="name" select="'description'" />
							<xsl:with-param name="title" select="$i18n.Description"/>
							<xsl:with-param name="class" select="'common-ckeditor'" />
							<xsl:with-param name="element" select="$element" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="title" select="$i18n.Description"/>
							<xsl:with-param name="element" select="$element" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				
		    </div>
		</div>
		
		<xsl:call-template name="appendCreateCommonFieldsFormConterAfterDescription"/>
		
		<div class="floatleft full bigmarginbottom">
			<label for="helpText" class="floatleft clearboth"><xsl:value-of select="$i18n.HelpText" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextArea">
					<xsl:with-param name="id" select="'helpText'" />
					<xsl:with-param name="name" select="'helpText'" />
					<xsl:with-param name="class" select="'common-ckeditor'" />
					<xsl:with-param name="element" select="$element" />
				</xsl:call-template>
		    </div>
		</div>
		
		<xsl:call-template name="initializeCommonCKEditor">
			<xsl:with-param name="element" select="$element" />
			<xsl:with-param name="flowTypeID" select="$flowTypeID"/>
		</xsl:call-template>		
		
		<label class="floatleft"><xsl:value-of select="$i18n.DefaultQueryState.title"/></label>
			
		<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.DefaultQueryState.description"/></div>
		
		<div class="floatleft full marginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'visible'"/>
					<xsl:with-param name="name" select="'defaultQueryState'"/>
					<xsl:with-param name="value" select="'VISIBLE'"/>
					<xsl:with-param name="element" select="$element/QueryDescriptor" />       
				</xsl:call-template>
				
				<label for="visible">
					<xsl:value-of select="$i18n.QueryState.VISIBLE" />
				</label>					
			</div>
		</div>	
	
		<div class="floatleft full marginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'visible_required'"/>
					<xsl:with-param name="name" select="'defaultQueryState'"/>
					<xsl:with-param name="value" select="'VISIBLE_REQUIRED'"/>   
					<xsl:with-param name="element" select="$element/QueryDescriptor" />     
				</xsl:call-template>
				
				<label for="visible_required">
					<xsl:value-of select="$i18n.QueryState.VISIBLE_REQUIRED" />
				</label>					
			</div>
		</div>	
	
		<div class="floatleft full marginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'hidden'"/>
					<xsl:with-param name="name" select="'defaultQueryState'"/>
					<xsl:with-param name="value" select="'HIDDEN'"/> 
					<xsl:with-param name="element" select="$element/QueryDescriptor" />
				</xsl:call-template>
				
				<label for="hidden">
					<xsl:value-of select="$i18n.QueryState.HIDDEN" />
				</label>					
			</div>
		</div>

		<label class="floatleft"><xsl:value-of select="$i18n.mergeWithPreviousQuery.title"/></label>
			
		<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.mergeWithPreviousQuery.description"/></div>	
	
		<div class="floatleft full marginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'mergeWithPreviousQuery'"/>
					<xsl:with-param name="name" select="'mergeWithPreviousQuery'"/>
					<xsl:with-param name="value" select="'true'"/> 
					<xsl:with-param name="element" select="$element/QueryDescriptor" />
				</xsl:call-template>
				
				<label for="mergeWithPreviousQuery">
					<xsl:value-of select="$i18n.mergeWithPreviousQuery" />
				</label>					
			</div>
		</div>

	
		<label class="floatleft"><xsl:value-of select="$i18n.exportQuery.title"/></label>
			
		<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.exportQuery.description"/></div>	
	
		<div class="floatleft full marginbottom">
						
			<div class="floatleft full">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="'export'"/>
					<xsl:with-param name="name" select="'exported'"/>
					<xsl:with-param name="value" select="'true'"/> 
					<xsl:with-param name="element" select="$element/QueryDescriptor" />
				</xsl:call-template>
				
				<label for="export">
					<xsl:value-of select="$i18n.exportQuery" />
				</label>					
			</div>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label for="xsdElementName" class="floatleft clearboth"><xsl:value-of select="$i18n.xsdElementName" /></label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'xsdElementName'"/>
					<xsl:with-param name="name" select="'xsdElementName'"/>
					<xsl:with-param name="title" select="$i18n.xsdElementName"/>
					<xsl:with-param name="element" select="$element/QueryDescriptor" />
				</xsl:call-template>
		    </div>
		</div>
	
	</xsl:template>

	<xsl:template name="appendCreateCommonFieldsFormConterAfterName"/>
	
	<xsl:template name="appendCreateCommonFieldsFormConterAfterDescription"/>

	<xsl:template name="createCommonShowFields">
	
		<xsl:param name="element" />
	
		<div class="floatleft bigmarginbottom">
			<label class="floatleft clearboth"><xsl:value-of select="$i18n.Name" /></label>
			<div class="floatleft full">
				<xsl:value-of select="$element/QueryDescriptor/name" />
		    </div>
		</div>
	
		<xsl:if test="$element/description">
			<div class="clearboth floatleft bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.Description" /></label>
				<div class="floatleft full">
					<xsl:value-of select="$element/description" disable-output-escaping="yes"/>
			    </div>
			</div>
		</xsl:if>
		
		<xsl:if test="$element/helpText">
			<div class="clearboth floatleft bigmarginbottom">
				<label class="floatleft clearboth"><xsl:value-of select="$i18n.HelpText" /></label>
				<div class="floatleft full">
					<xsl:value-of select="$element/helpText" disable-output-escaping="yes" />
			    </div>
			</div>
		</xsl:if>
	
		<div class="clearboth floatleft bigmarginbottom">
			<label class="floatleft clearboth"><xsl:value-of select="$i18n.DefaultQueryState.title" /></label>
			<div class="floatleft full">
				<xsl:choose>
					<xsl:when test="$element/QueryDescriptor/defaultQueryState = 'VISIBLE'">
						<xsl:value-of select="$i18n.QueryState.VISIBLE" />
					</xsl:when>
					<xsl:when test="$element/QueryDescriptor/defaultQueryState = 'VISIBLE_REQUIRED'">
						<xsl:value-of select="$i18n.QueryState.VISIBLE_REQUIRED" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.QueryState.HIDDEN" />
					</xsl:otherwise>
				</xsl:choose>
		    </div>
		</div>
	
	</xsl:template>

	<xsl:template name="createAlternativesForm">
		
		<xsl:param name="alternatives" />
		<xsl:param name="freeTextAlternative" />
		<xsl:param name="allowFreeTextAlternative" select="'true'"/>
		<xsl:param name="requestparameters" select="requestparameters" />
		<xsl:param name="editExtraValues" select="'false'"/>
		
		<script type="text/javascript">
			Alternativesi18n = {
				deleteConfirm: "<xsl:value-of select="$i18n.DeleteAlternativeConfirm"/>",
				title: "<xsl:value-of select="$i18n.AlternativeExtraTitle"/>",
			};
		</script>
		
		<div class="alternatives-container full floatleft bigmarginbottom">
		
			<xsl:call-template name="createAlternativesFormHeader"/>
		
			<div class="alternatives full floatleft marginleft sortable">
				
				<xsl:choose>
					<xsl:when test="$requestparameters">
						<xsl:apply-templates select="$requestparameters/parameter[starts-with(name,'alternative_')]" mode="alternative" >
							<xsl:with-param name="editExtraValues" select="$editExtraValues"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$alternatives" >
							<xsl:with-param name="editExtraValues" select="$editExtraValues"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
			<div class="alternative-template" style="display: none;">
				<xsl:call-template name="createAlternative">
					<xsl:with-param name="alternativeID" select="''" />
					<xsl:with-param name="sortOrder" select="'sortIndex'" />
					<xsl:with-param name="value" select="''" />
					<xsl:with-param name="xmlValue" select="''" />
					<xsl:with-param name="attributeValue" select="''" />
					<xsl:with-param name="disabled" select="'true'" />
					<xsl:with-param name="editExtraValues" select="$editExtraValues"/>
				</xsl:call-template>
			</div>
			
			<div style="display: none;">
			
				<div class="alternatives-modal contentitem">
					<div class="modal-content">
					
						<div class="modal-header bigmarginbottom">
							<h1/>
						</div>
						
						<div class="modal-body">
						
							<div class="floatleft full bigmarginbottom">
							
								<label for="alternative-xml-value" class="floatleft clearboth">
									<xsl:value-of select="$i18n.AlternativeXMLValue" />
								</label>
								
								<p class="tiny floatleft full">
							  	<xsl:value-of select="$i18n.AlternativeXMLValueDescription" />
							  </p>
								
								<div class="floatleft full">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'alternative-xml-value'"/>
										<xsl:with-param name="name" select="'alternative-xml-value'"/>
									</xsl:call-template>
							  </div>
							</div>
							
							<div class="floatleft full bigmarginbottom">
							
								<label for="alternative-attribute-value" class="floatleft clearboth">
									<xsl:value-of select="$i18n.AlternativeAttributeValue" />
								</label>
								
								<p class="tiny floatleft full">
							  	<xsl:value-of select="$i18n.AlternativeAttributeValueDescription" />
							  </p>
								
								<div class="floatleft full">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'alternative-attribute-value'"/>
										<xsl:with-param name="name" select="'alternative-attribute-value'"/>
									</xsl:call-template>
							  </div>
							</div>
							
							<xsl:variable name="helper"><AlternativeModalExtra/></xsl:variable>
							<xsl:apply-templates select="exsl:node-set($helper)/AlternativeModalExtra"/>
							
							<input class="close bigmargintop floatright" type="button" value="{$i18n.CloseAlternativeExtra}" />
							
						</div>
						
					</div>
				</div>
			
			</div>
			
			<div class="floatright margintop" style="margin-right: 21px">
				<a href="#" onclick="addAlternative(this, event)" title="{$i18n.AddAlternative}">
					<xsl:value-of select="$i18n.AddAlternative"/>
				</a>
				<a href="#" onclick="addAlternative(this, event)" title="{$i18n.AddAlternative}">
					<img class="vertical-align-bottom marginleft" src="{$commonImagePath}/add.png"/>
				</a>
			</div>
		
			<xsl:if test="$allowFreeTextAlternative = 'true'">
				<label for="useFreeTextAlternative" class="floatleft clearboth"><xsl:value-of select="$i18n.FreeTextAlternative" /></label>
				<div class="clearboth floatleft marginbottom"><xsl:value-of select="$i18n.FreeTextAlternativeDescription" /></div>
				<div class="floatleft full marginbottom">
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="createCheckbox">
						<xsl:with-param name="id" select="'useFreeTextAlternative'" />
						<xsl:with-param name="name" select="'useFreeTextAlternative'" />
						<xsl:with-param name="title" select="$i18n.FreeTextAlternative" />
						<xsl:with-param name="checked">
							<xsl:if test="$freeTextAlternative">true</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'freeTextAlternative'"/>
						<xsl:with-param name="name" select="'freeTextAlternative'"/>
						<xsl:with-param name="title" select="$i18n.FreeTextAlternative"/>
						<xsl:with-param name="value" select="name"/>
						<xsl:with-param name="width" select="'89%'"/>
						<xsl:with-param name="value" select="$freeTextAlternative"/>
						<xsl:with-param name="disabled" select="'disabled'"/>
					</xsl:call-template>
				</div>
			</xsl:if>
		
		</div>
		
	</xsl:template>
	
	<xsl:template name="createAlternativesFormHeader">
		
		<div class="floatright bigmargintop" style="margin-right: 42px">
			
				<xsl:variable name="options">
					<option>
						<name><xsl:value-of select="$i18n.AutoSortAlternatives.Select"/></name>
						<value></value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.AutoSortAlternatives.Descending"/></name>
						<value>d</value>
					</option>
					<option>
						<name><xsl:value-of select="$i18n.AutoSortAlternatives.Ascending"/></name>
						<value>a</value>
					</option>
				</xsl:variable>
			
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="class" select="'sorting'"/>
					<xsl:with-param name="name" select="'sorting'"/>
					<xsl:with-param name="labelElementName" select="'name'" />
					<xsl:with-param name="valueElementName" select="'value'" />
					<xsl:with-param name="element" select="exsl:node-set($options)/option" />
				</xsl:call-template>

			</div>
		
			<label class="floatleft"><xsl:value-of select="$i18n.Alternative" /></label>
			
	</xsl:template>
	
	<xsl:template match="AlternativeModalExtra">
	
	</xsl:template>
	
	<xsl:template name="createAlternative">
		
		<xsl:param name="alternativeID" />
		<xsl:param name="sortOrder" />
		<xsl:param name="value" />
		<xsl:param name="xmlValue" />
		<xsl:param name="attributeValue" />
		<xsl:param name="disabled" select="null" />
		<xsl:param name="editExtraValues" select="'false'"/>
		
		<div class="alternative full floatleft margintop marginbottom">
			<img class="vertical-align-middle marginright cursor-move" src="{$commonImagePath}/move.png" title="{$i18n.MoveAlternative}"/>
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('alternativeID_', $alternativeID)"/>
				<xsl:with-param name="name" select="'alternativeID'"/>
				<xsl:with-param name="value" select="$alternativeID"/>
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('sortorder_', $alternativeID)" />
				<xsl:with-param name="name" select="concat('sortorder_', $alternativeID)" />
				<xsl:with-param name="value" select="$sortOrder" />
				<xsl:with-param name="class" select="'sortorder'" />
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('alternative-xml-value_', $alternativeID)" />
				<xsl:with-param name="name" select="concat('alternative-xml-value_', $alternativeID)" />
				<xsl:with-param name="value" select="$xmlValue" />
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="id" select="concat('alternative-attribute-value_', $alternativeID)" />
				<xsl:with-param name="name" select="concat('alternative-attribute-value_', $alternativeID)" />
				<xsl:with-param name="value" select="$attributeValue" />
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="concat('alternative_', $alternativeID)"/>
				<xsl:with-param name="name" select="concat('alternative_', $alternativeID)"/>
				<xsl:with-param name="title" select="name"/>
				<xsl:with-param name="value" select="$value"/>
				<xsl:with-param name="width" select="'calc(100% - 68px)'"/>
				<xsl:with-param name="class" select="'name'" />
				<xsl:with-param name="requestparameters" select="//requestparameters" />
				<xsl:with-param name="disabled" select="$disabled"/>
			</xsl:call-template>
			
			<xsl:variable name="helper"><AlternativeExtraValues/></xsl:variable>
			<xsl:apply-templates select="exsl:node-set($helper)/AlternativeExtraValues">
				<xsl:with-param name="alternativeID" select="$alternativeID"/>
				<xsl:with-param name="disabled" select="$disabled"/>
				<xsl:with-param name="context" select="."/>
			</xsl:apply-templates>
			
			<xsl:if test="$editExtraValues = 'true'">
			
				<a href="#" onclick="openAlternativeModal(this, event)" title="{$i18n.EditAlternativeExtra}">
					<img class="vertical-align-middle marginleft" src="{$commonImagePath}/pen.png"/>
				</a>
			
			</xsl:if>
			
			<a href="#" onclick="deleteAlternative(this, event)" title="{$i18n.DeleteAlternative}">
				<img class="vertical-align-middle marginleft" src="{$commonImagePath}/delete.png"/>
			</a>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="AlternativeExtraValues">
		<xsl:param name="alternativeID" />
		<xsl:param name="disabled" />
		<xsl:param name="context" />
	
	</xsl:template>

	<xsl:template match="parameter" mode="alternative">
		<xsl:param name="editExtraValues" select="'false'"/>
		
		<xsl:variable name="id" select="substring(name,13,47)" />
		
		<xsl:call-template name="createAlternative">
			<xsl:with-param name="alternativeID" select="$id" />
			<xsl:with-param name="sortOrder" select="../parameter[name = concat('sortorder_',$id)]/value" />
			<xsl:with-param name="value" select="value" />
			<xsl:with-param name="xmlValue" select="../parameter[name = concat('alternative-xml-value_',$id)]/value" />
			<xsl:with-param name="attributeValue" select="../parameter[name = concat('alternative-attribute-value_',$id)]/value" />
			<xsl:with-param name="editExtraValues" select="$editExtraValues"/>
		</xsl:call-template>
			
	</xsl:template>

	<xsl:template match="validationError[messageKey = 'TooFewAlternatives']">
		
		<p class="error">
			<xsl:value-of select="$i18n.TooFewAlternatives" />
		</p>
		
	</xsl:template>

	<xsl:template match="validationError[validationErrorType = 'TooLong']">
		
		<p class="error">
			<xsl:value-of select="$i18n.TooLongFieldContent"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="fieldName" mode="common" />!
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'InvalidFormat']">
		
		<p class="error">
			<xsl:value-of select="$i18n.InvalidFormat"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="fieldName" mode="common" />!
		</p>
		
	</xsl:template>
	
	<xsl:template match="validationError[validationErrorType = 'RequiredField']">
		
		<xsl:choose>
			<xsl:when test="starts-with(fieldName, 'alternative_')">
				
				<xsl:variable name="id" select="substring(fieldName,13)" />
		
				<p class="error">
					<xsl:value-of select="$i18n.AlternativeRequired" /><xsl:text>&#160;</xsl:text><xsl:value-of select="../../requestparameters/parameter[name = concat('sortorder_', $id)]/value + 1" />!
				</p>
				
			</xsl:when>
			<xsl:otherwise>
				
				<p class="error">
					<xsl:value-of select="$i18n.RequiredField"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:apply-templates select="fieldName" mode="common" />!
				</p>
				
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

	<xsl:template match="validationError">
		
		<p class="error">
			<xsl:value-of select="$i18n.UnknownValidationError"/>
		</p>
		
	</xsl:template>

	<xsl:template match="fieldName" mode="common">
	
		<xsl:variable name="fieldName" select="." />
	
		<xsl:choose>
			<xsl:when test="$fieldName = 'description'">
				<xsl:value-of select="$i18n.description" />
			</xsl:when>
			<xsl:when test="$fieldName = 'helpText'">
				<xsl:value-of select="$i18n.helpText" />
			</xsl:when>
			<xsl:when test="$fieldName = 'name'">
				<xsl:value-of select="$i18n.name" />
			</xsl:when>
			<xsl:when test="$fieldName = 'xsdElementName'">
				<xsl:value-of select="$i18n.xsdElementName" />
			</xsl:when>			
			<xsl:when test="$fieldName = 'defaultQueryState'">
				<xsl:value-of select="$i18n.defaultQueryState" />
			</xsl:when>
			<xsl:when test="$fieldName = 'freeTextAlternative'">
				<xsl:value-of select="$i18n.freeTextAlternative" />
			</xsl:when>
			<xsl:when test="starts-with($fieldName, 'alternative_')">
				<xsl:value-of select="$i18n.validationError.alternative" />
				<xsl:text>&#160;</xsl:text>
				<xsl:variable name="id" select="substring($fieldName,13)" />
				<xsl:value-of select="../../../requestparameters/parameter[name = concat('sortorder_', $id)]/value + 1" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName"/>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="initializeCommonCKEditor">
		
		<xsl:param name="element" />
		<xsl:param name="flowTypeID" select="$element/../flowTypeID" />
		
		<script type="text/javascript">
			
			var fckSettings = new Object();
			var basePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/ckeditor/';
			fckSettings.editorContainerClass = 'common-ckeditor';
			fckSettings.editorHeight = 150;
			
			<xsl:if test="/Document/ckConnectorModuleAlias">
				fckSettings.filebrowserBrowseUri = basePath + 'filemanager/index.html?Connector=<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="/Document/ckConnectorModuleAlias" />/connector/<xsl:value-of select="$flowTypeID" />';
		       	fckSettings.filebrowserImageBrowseUri = basePath + 'filemanager/index.html?Connector=<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="/Document/ckConnectorModuleAlias" />/connector/<xsl:value-of select="$flowTypeID" />';
			</xsl:if>
	
			<xsl:if test="/Document/cssPath">
				fckSettings.contentsCss = '<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:value-of select="/Document/cssPath"/>';
			</xsl:if>
			
			fckSettings.customConfig = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/common/ckeditor/config.js';
		
		</script>
	
	</xsl:template>

</xsl:stylesheet>