<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/fancytree/jquery.fancytree.min.js
		/js/fancytree/jquery.fancytree.min.js
		/js/fancytree/src/jquery.fancytree.persist.js
		/js/fancytree/src/jquery.fancytree.filter.js
		/js/multitreequery.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/fancytree/skin-win7/ui.fancytree.min.css
		/css/multitreequery.css?v=2
	</xsl:variable>

	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="Document">	
		
		<xsl:apply-templates select="ShowQueryValues"/>
		<xsl:apply-templates select="ShowQueryForm"/>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryValues">
		
		<div class="query">
			
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<article class="multitreequery show-mode">
				
				<div class="heading-wrapper">
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="MultiTreeQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:call-template name="createUpdateButton">
						<xsl:with-param name="queryID" select="MultiTreeQueryInstance/MultiTreeQuery/queryID" />
					</xsl:call-template>
					
				</div>

				<xsl:if test="MultiTreeQueryInstance/MultiTreeQuery/description">
					<span class="italic">
						<xsl:value-of select="MultiTreeQueryInstance/MultiTreeQuery/description" disable-output-escaping="yes" />
					</span>
				</xsl:if>
				
				<div class="clearboth"/>
				
				<fieldset>
				
					<xsl:if test="MultiTreeQueryInstance/selectedNodeKeys">
					
						<div>
							<h3><xsl:value-of select="$i18n.ChosenTrees" /></h3>
							
							<div class="marginleft">
							
								<xsl:call-template name="FancyTreeShow">
									<xsl:with-param name="QueryID" select="MultiTreeQueryInstance/MultiTreeQuery/queryID"/>
									<xsl:with-param name="TreeNodes" select="MultiTreeQueryInstance/StoredTreeNodes/TreeNode[not(parentNodeKey)]"/>
								</xsl:call-template>
								
							</div>
							
							<script type="text/javascript">multiTreeQueryCreateShowTree('<xsl:value-of select="MultiTreeQueryInstance/MultiTreeQuery/queryID" />');</script>
						</div>
						
					</xsl:if>
					
				</fieldset>
				
			</article>
				
		</div>
		
	</xsl:template>
	
	<xsl:template match="ShowQueryForm">
			
		<script type="text/javascript">
			i18n = {
				"NoChosenTrees": "<xsl:value-of select="$i18n.NoChosenTrees"/>",
			}
		</script>
	
		<xsl:variable name="shortQueryID" select="concat('q', MultiTreeQueryInstance/MultiTreeQuery/queryID)" />
	
		<xsl:variable name="queryID" select="concat('query_', MultiTreeQueryInstance/MultiTreeQuery/queryID)" />
	
		<div class="query" id="{$queryID}">
			
			<xsl:attribute name="class">
				<xsl:text>query</xsl:text>
				<xsl:if test="EnableAjaxPosting"> enableAjaxPosting</xsl:if>
				<xsl:if test="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="{$queryID}" />
		
			<xsl:if test="ValidationErrors/validationError">
				<div id="{$queryID}-validationerrors" class="validationerrors">
					<div class="info-box error">
					
						<xsl:choose>
							<xsl:when test="ValidationErrors/validationError[messageKey]">
								<xsl:apply-templates select="ValidationErrors/validationError[messageKey]"/>
							</xsl:when>
							<xsl:otherwise><span/></xsl:otherwise>
						</xsl:choose>
						
						<div class="marker"/>
					</div>
				</div>
			</xsl:if>
			
			<article class="multitreequery">
			
				<xsl:if test="ValidationErrors/validationError">
					<xsl:attribute name="class">multitreequery error</xsl:attribute>
				</xsl:if>
			
				<div class="heading-wrapper">
					
					<h2>
						<xsl:attribute name="class">
							<xsl:if test="MultiTreeQueryInstance/QueryInstanceDescriptor/queryState = 'VISIBLE_REQUIRED'">
								<xsl:text>required</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:value-of select="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
					</h2>
					
					<xsl:if test="MultiTreeQueryInstance/MultiTreeQuery/helpText">		
						<xsl:apply-templates select="MultiTreeQueryInstance/MultiTreeQuery/helpText" />
					</xsl:if>
				
				</div>

				<xsl:if test="MultiTreeQueryInstance/MultiTreeQuery/description">
					<span class="italic">
						<xsl:if test="/Document/useCKEditorForDescription = 'true'"><xsl:attribute name="class">italic html-description</xsl:attribute></xsl:if>
						<xsl:value-of select="MultiTreeQueryInstance/MultiTreeQuery/description" disable-output-escaping="yes" />
					</span>		
				</xsl:if>
				
				<fieldset>
				
					<xsl:choose>
						<xsl:when test="MultiTreeQueryInstance/FullTree">
						
							<div class="marginbottom">

								<xsl:call-template name="FancyTree">
									<xsl:with-param name="Query" select="MultiTreeQueryInstance/MultiTreeQuery"/>
									<xsl:with-param name="TreeNodes" select="MultiTreeQueryInstance/FullTree/TreeNode"/>
								</xsl:call-template>
								
							</div>
							
						</xsl:when>
						<xsl:when test="MultiTreeQueryInstance/selectedNodeKeys">
						
							<div class="marginbottom">

								<xsl:call-template name="FancyTree">
									<xsl:with-param name="Query" select="MultiTreeQueryInstance/MultiTreeQuery"/>
									<xsl:with-param name="TreeNodes" select="MultiTreeQueryInstance/StoredTreeNodes/TreeNode[not(parentNodeKey)]"/>
								</xsl:call-template>
								
							</div>
					
						</xsl:when>
						<xsl:otherwise>
						
							<xsl:value-of select="$i18n.Error.UnableToGetTrees"/>
							
						</xsl:otherwise>
					</xsl:choose>
					
				</fieldset>
				
				<xsl:variable name="selected">
					<xsl:choose>
						<xsl:when test="requestparameters">
						
							<xsl:variable name="inputName">
								<xsl:value-of select="'q'"/>
								<xsl:value-of select="MultiTreeQueryInstance/MultiTreeQuery/queryID"/>
								<xsl:value-of select="'_selectedkey'"/>
							</xsl:variable>
							
							<xsl:if test="requestparameters/parameter[name = $inputName]">
							
								<xsl:for-each select="requestparameters/parameter[name = $inputName]">
									<xsl:call-template name="escapeForJavascript">
										<xsl:with-param name="text" select="value"/>
									</xsl:call-template>
								</xsl:for-each>
							
							</xsl:if>
							
						</xsl:when>
						<xsl:when test="MultiTreeQueryInstance/selectedNodeKeys">
						
							<xsl:for-each select="MultiTreeQueryInstance/selectedNodeKeys/value">
								<xsl:call-template name="escapeForJavascript">
									<xsl:with-param name="text" select="."/>
								</xsl:call-template>
								<xsl:if test="position() != last()">
									<xsl:text>,</xsl:text>
								</xsl:if>
							</xsl:for-each>
							
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				
				<script type="text/javascript">initMultiTreeQuery('<xsl:value-of select="MultiTreeQueryInstance/MultiTreeQuery/queryID" />', '<xsl:value-of select="$selected"/>');</script>
			</article>
		
		</div>
		
	</xsl:template>
	
	<xsl:template name="FancyTreeShow">
		<xsl:param name="QueryID"/>
		<xsl:param name="TreeNodes"/>
		
		<div id="tree{$QueryID}"/>
		
		<script type="text/javascript">var MultiTreeQueryTree<xsl:value-of select="$QueryID"/> = [<xsl:apply-templates select="$TreeNodes"/>];</script>
	
	</xsl:template>
	
	<xsl:template name="FancyTree">
		<xsl:param name="Query"/>
		<xsl:param name="TreeNodes"/>

		<div>
		
			<h3><xsl:value-of select="$i18n.ChosenTrees" /></h3>
			
			<xsl:choose>
				<xsl:when test="$Query/previewMode = 'LIST'">
				
					<div class="border">
					
						<ul class="chosen-tree-list list-table">
							<li class="chosen-tree-template hidden">
								<span class="chosen-tree-name floatleft marginleft"></span>
								<a class="remove-chosen-tree delete floatright" href="javascript:void(0)"><img src="{$imagePath}/delete.png" alt="" /></a>
							</li>
							<li class="no-chosen-trees" >
								<span class="floatleft marginleft"><xsl:value-of select="$i18n.NoChosenTrees" /></span>
							</li>
						</ul>
					
					</div>
				
				</xsl:when>
				<xsl:otherwise>
				
					<div id="tree-preview{$Query/queryID}"/>
					
					<script type="text/javascript">var MultiTreeQueryPreviewTree<xsl:value-of select="$Query/queryID"/> = [<xsl:apply-templates select="$TreeNodes"/>];</script>
					
					<script type="text/javascript">multiTreeQueryCreatePreviewTree('<xsl:value-of select="$Query/queryID" />');</script>
						
				</xsl:otherwise>
			</xsl:choose>
				
		</div>
		
		<div class="floatright full marginbottom">
		
			<div class="floatright twenty">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id">
						<xsl:value-of select="'filterField'"/>
						<xsl:value-of select="$Query/queryID"/>
					</xsl:with-param>
					<xsl:with-param name="name">
						<xsl:value-of select="'filterField'"/>
						<xsl:value-of select="$Query/queryID"/>
					</xsl:with-param>
				</xsl:call-template>
			</div>
			
			<div class="floatright margintop">
				<label for="filterField{$Query/queryID}" class="floatright marginright">
					<xsl:value-of select="$i18n.Filter" />
				</label>
    		</div>
		
		</div>
		
		<div id="tree{$Query/queryID}" class="clearboth" data-nofolders="{$Query/onlyAllowSelectingLeafs}">
			<xsl:if test="$Query/onlyAllowSelectingLeafs = 'true'"><xsl:attribute name="class">leafselection clearboth</xsl:attribute></xsl:if>
		</div>
		
		<div>
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name">
					<xsl:value-of select="'q'"/>
					<xsl:value-of select="$Query/queryID"/>
					<xsl:value-of select="'_selectedkey'"/>
				</xsl:with-param>
				<xsl:with-param name="class" select="'selectedkey-template'" />
				<xsl:with-param name="disabled" select="'true'" />
			</xsl:call-template>
		</div>
		
		<script type="text/javascript">var MultiTreeQueryTree<xsl:value-of select="$Query/queryID"/> = [<xsl:apply-templates select="$TreeNodes"/>];</script>
	
	</xsl:template>
	
	<xsl:template match="TreeNode[parent::StoredTreeNodes]">
	
		<xsl:if test="not(position() = 1)">
			<xsl:text>,</xsl:text>
		</xsl:if>
	
		<xsl:variable name="key" select="key"/>
	
		<xsl:variable name="escapedKey">
			<xsl:call-template name="escapeForJavascript">
				<xsl:with-param name="text" select="key"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="escapedName">
			<xsl:call-template name="escapeForJavascript">
				<xsl:with-param name="text" select="name"/>
			</xsl:call-template>
		</xsl:variable>
	
		<xsl:text>{</xsl:text>
		
		<xsl:text>title:'</xsl:text>
		<xsl:value-of select="$escapedName"/>
		<xsl:text>'</xsl:text>
		
		<xsl:text>,key:'</xsl:text>
		<xsl:value-of select="$escapedKey"/>
		<xsl:text>'</xsl:text>
		
		<xsl:if test="icon">
			<xsl:text>,icon:'</xsl:text>
			<xsl:value-of select="icon"/>
			<xsl:text>'</xsl:text>
		</xsl:if>
		
		<xsl:if test="folder">
			<xsl:text>,folder:</xsl:text>
			<xsl:value-of select="folder"/>
		</xsl:if>
		
		<xsl:if test="../TreeNode[parentNodeKey = $key]">
			
			<xsl:text>,children:[</xsl:text>
			<xsl:apply-templates select="../TreeNode[parentNodeKey = $key]"/>
			<xsl:text>]</xsl:text>
			
		</xsl:if>
		
		<xsl:text>}</xsl:text>
	
	</xsl:template>
	
	<xsl:template match="TreeNode">
	
		<xsl:variable name="position">
			<xsl:number/>
		</xsl:variable>
	
		<xsl:if test="not($position = 1)">
			<xsl:text>,</xsl:text>
		</xsl:if>
		
		<xsl:variable name="escapedKey">
			<xsl:call-template name="escapeForJavascript">
				<xsl:with-param name="text" select="key"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="escapedName">
			<xsl:call-template name="escapeForJavascript">
				<xsl:with-param name="text" select="name"/>
			</xsl:call-template>
		</xsl:variable>
	
		<xsl:text>{</xsl:text>
		
		<xsl:text>title:'</xsl:text>
		<xsl:value-of select="$escapedName"/>
		<xsl:text>'</xsl:text>
		
		<xsl:text>,key:'</xsl:text>
		<xsl:value-of select="$escapedKey"/>
		<xsl:text>'</xsl:text>
		
		<xsl:if test="icon">
			<xsl:text>,icon:'</xsl:text>
			<xsl:value-of select="icon"/>
			<xsl:text>'</xsl:text>
		</xsl:if>
		
		<xsl:if test="folder">
			<xsl:text>,folder:</xsl:text>
			<xsl:value-of select="folder"/>
		</xsl:if>
		
		<xsl:if test="Children">
			
			<xsl:text>,children:[</xsl:text>
			<xsl:apply-templates select="Children/TreeNode"/>
			<xsl:text>]</xsl:text>
			
		</xsl:if>
		
		<xsl:text>}</xsl:text>
	
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
				<xsl:value-of select="$i18n.Error.InvalidFormat"/>
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