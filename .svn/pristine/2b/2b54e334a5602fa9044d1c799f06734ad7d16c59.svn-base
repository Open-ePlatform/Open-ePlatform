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
				<xsl:if test="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/mergeWithPreviousQuery = 'true'"> mergewithpreviousquery</xsl:if>
			</xsl:attribute>
			
			<a name="query{MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/queryID}"/>
			
			<h2>
				<xsl:value-of select="MultiTreeQueryInstance/QueryInstanceDescriptor/QueryDescriptor/name"/>
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
			
				<xsl:if test="MultiTreeQueryInstance/selectedNodeKeys">
						
					<p>
					
						<xsl:choose>
							<xsl:when test="MultiTreeQueryInstance/MultiTreeQuery/previewMode = 'LIST'">
					
								<div class="border">
								
									<ul class="list-style-type-none">
									
										<xsl:choose>
											<xsl:when test="MultiTreeQueryInstance/StoredTreeNodes/TreeNode[nodeHierarchy]">
												
												<xsl:apply-templates select="MultiTreeQueryInstance/StoredTreeNodes/TreeNode[nodeHierarchy]" mode="list"/>
											
											</xsl:when>
											<xsl:otherwise>
											
												<li class="no-chosen-trees">
													<span class="floatleft marginleft"><xsl:value-of select="$i18n.NoChosenTrees" /></span>
												</li>
												
											</xsl:otherwise>
										</xsl:choose>
				
									</ul>
								
								</div>

							</xsl:when>
							<xsl:otherwise>
							
								<ul class="tree" style="list-style-type: none; margin: 0; padding: 0;">
									<xsl:apply-templates select="MultiTreeQueryInstance/StoredTreeNodes/TreeNode[not(parentNodeKey)]" />
								</ul>
							
							</xsl:otherwise>										
						</xsl:choose>
					
					</p>
					
				</xsl:if>
						
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="TreeNode[parent::StoredTreeNodes]">
	
		<xsl:variable name="key" select="key"/>
	
		<li style="margin: 0; padding: 0 12px; line-height: 20px;  background: url({/Document/StaticContent}/pics/lastnode.png) no-repeat;">
		
			<xsl:if test="not(parentNodeKey)">
				<xsl:attribute name="style">
					<xsl:text>margin: 0; padding: 0 0; line-height: 20px; </xsl:text>
				</xsl:attribute>
			</xsl:if>
		
			<xsl:value-of select="name"/>
			
			<xsl:if test="../TreeNode[parentNodeKey = $key]">
				<ul style="list-style-type: none; margin: 0; margin-left: 10px; padding: 0;">
					<xsl:apply-templates select="../TreeNode[parentNodeKey = $key]"/>
				</ul>
			</xsl:if>
			
			</li>
	
	</xsl:template>
	
	<xsl:template match="TreeNode" mode="list">
		
		<li>
			<span class="marginleft">
				<xsl:value-of select="nodeHierarchy"/>
			</span>
		</li>
		
	</xsl:template>
	
</xsl:stylesheet>