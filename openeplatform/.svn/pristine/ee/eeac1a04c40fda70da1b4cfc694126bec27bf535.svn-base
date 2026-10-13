<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/xsl/CommonCoreTemplates.xsl" />
	<xsl:include href="classpath://se/unlogic/hierarchy/core/xsl/Errors.sv.xsl" />
	
	<xsl:template match="document">
	
		<xsl:text disable-output-escaping='yes'><![CDATA[<!DOCTYPE html>]]></xsl:text>
		
		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7"><![endif]-->]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8"><![endif]-->]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->]]></xsl:text>
		
			<head>
				<meta http-equiv="Content-Type" content= "text/html; charset=ISO-8859-1" />
				<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
				
				<meta name="keywords" content="RIGES, Open e-Platform" />
				<meta name="description" content="Open e-Platform är en e-tjänsteplattform utvecklad av RIGES-projektet" />
				
				<title>
					<xsl:if test="title">	
						<xsl:value-of select="title"/>
						<xsl:text> - </xsl:text>
					</xsl:if>
					<xsl:text>Open ePlatform</xsl:text>
				</title>
			
				<link rel="shortcut icon" href="{/document/requestinfo/contextpath}/favicon.ico"/>
			    
				<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/global.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/header.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/layout.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/modules.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/interface.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/footer.css" />
		  		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/openhierarchy.css" />
		  		
		  		<script type="text/javascript" src="{/document/requestinfo/contextpath}/js/vendor/modernizr-2.6.2.min.js" />
				
				<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/global/jquery/jquery.js"/>
				
				<script type="text/javascript" src="{/document/requestinfo/contextpath}/js/init-modernizr.js"></script>
				
				<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if lt IE 7]>]]></xsl:text>
					<p class="chromeframe">Du använder en <strong>gammal</strong> webbläsare. Vänligen <a href="http://browsehappy.com/">uppgradera din webbläsare</a> eller <a href="http://www.google.com/chromeframe/?redirect=true">aktivera Google Chrome Frame</a> för att förbättra upplevelsen.</p>
				<xsl:text disable-output-escaping="yes"><![CDATA[<![endif]-->]]></xsl:text>
				
				<xsl:apply-templates select="links/link"/>
			   	<xsl:apply-templates select="scripts/script[src != '/static/global/jquery/jquery.js']" />

				<xsl:if test="not(scripts/script[src='/static/global/jquery/jquery-ui.js'])">
				    <script type="text/javascript" src="{/document/requestinfo/contextpath}/static/global/jquery/jquery-ui.js"/>
				</xsl:if>

				<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if lt IE 8]>]]></xsl:text>
					<script type="text/javascript" src="https://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE8.js"></script>
				<xsl:text disable-output-escaping="yes"><![CDATA[<![endif]-->]]></xsl:text>
				
			 	<script type="text/javascript" src="{/document/requestinfo/contextpath}/js/fastclick.js"></script>
			  	<script type="text/javascript" src="{/document/requestinfo/contextpath}/js/init-menus.js"></script>

			</head>
			
			<body>
				
				<header>
				  	<div class="top container">
				  		<xsl:if test="backgroundsModuleResponses/response/slots[starts-with(slot,'header.')]">
							<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'header.')]">
								<xsl:value-of select="HTML" disable-output-escaping="yes"/>
							</xsl:for-each>
						</xsl:if>
				  	</div>  	
				  	<nav>
				  		<div class="container">
				  			<a class="only-mobile" href="#" id="toggle-primary" data-icon-before="L">Meny</a>
				 			<ul class="primary">
				 				
				 				<xsl:if test="section/visibleInMenu = 'true'">
									<li>
										<xsl:if test="not(menus/menu/menuitem[itemType = 'SECTION']/menu) and not(menus/menu/menuitem[itemType = 'SECTION']/selected)">
											<xsl:attribute name="class">active</xsl:attribute>
										</xsl:if>
										<a href="{/document/requestinfo/contextpath}/" title="{section/description}">
											<xsl:value-of select="section/name" />
										</a>
									</li>
								</xsl:if>
								
								<xsl:apply-templates select="menus/menu/menuitem[itemType='SECTION'][position() = 1]" mode="sections" />
								
								<xsl:if test="not(user)">
									<li><a href="{/document/requestinfo/contextpath}/minasidor" title="Mina sidor">Mina sidor<i data-icon-before="u" title="Mina sidor kräver inloggning." class="vertical-align-middle"></i></a></li>
								</xsl:if>
								
								<xsl:apply-templates select="menus/menu/menuitem[itemType='SECTION'][position() > 1]" mode="sections" />
				 				
				 			</ul>
				 			<div class="user">
						  		<xsl:choose>
									<xsl:when test="user">
										<div id="logged-in-menu" class="dd logged-in">
								  			<a>
								  				<h2 data-icon-after="u">
								  					<span>
									  					<xsl:value-of select="user/firstname" />
														<xsl:text>&#160;</xsl:text> 
														<xsl:value-of select="user/lastname" />
													</span>
												</h2>
								  				<div class="toggler"><i>_</i></div>
								  			</a>
								  			
								  			<div id="logged-in-submenu" class="submenu">
							  					<ul>
						 							<li>
						 								<a href="{/document/requestinfo/contextpath}/mysettings" title="Mina uppgifter">
						 									<span class="icon arrow"><i data-icon-before=">"></i></span>
						 									<span class="text">Mina uppgifter</span>
						 								</a>
													</li>
													<li>
						 								<a href="{/document/requestinfo/contextpath}/myorganizations" title="Mina företag">
						 									<span class="icon arrow"><i data-icon-before=">"></i></span>
						 									<span class="text">Mina företag</span>
						 								</a>
													</li>
						 							<li class="bordered-link">
						 								<a href="{/document/requestinfo/contextpath}/logout" title="Logga ut">Logga ut</a>
						 							</li>
						 						</ul>
							  				</div>
							  			</div>
						        	</xsl:when>
						        	<xsl:otherwise>
						        		<a href="{/document/requestinfo/uri}?triggerlogin=1" class="logged-out" title="Logga in"><i>u</i>Logga in</a>
						        	</xsl:otherwise>
					        	</xsl:choose>
					        	<xsl:if test="backgroundsModuleResponses/response/slots[starts-with(slot,'authentication.')]">
									<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'authentication.')]">
										<xsl:value-of select="HTML" disable-output-escaping="yes"/>
									</xsl:for-each>
								</xsl:if>
						  	</div>
						  	<xsl:if test="backgroundsModuleResponses/response/slots[starts-with(slot,'sectionmenu-content-container.')]">
								<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'sectionmenu-content-container.')]">
									<xsl:value-of select="HTML" disable-output-escaping="yes"/>
								</xsl:for-each>
							</xsl:if>
				  		</div>
			 		</nav>
			 	</header>
				
				<xsl:variable name="leftContentVisible">
					<xsl:choose>
						<xsl:when test="backgroundsModuleResponses/response/slots[starts-with(slot,'left-content-container.')]">true</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<xsl:variable name="rightContentVisible">
					<xsl:choose>
						<xsl:when test="backgroundsModuleResponses/response/slots[starts-with(slot,'right-content-container.')]">true</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<div class="main container" role="main">
					
					<div class="top-content">
						<xsl:if test="backgroundsModuleResponses/response/slots[starts-with(slot,'top-content-container.')]">
							<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'top-content-container.')]">
								<xsl:value-of select="HTML" disable-output-escaping="yes"/>
							</xsl:for-each>
						</xsl:if>
					</div>
					
					<xsl:if test="$leftContentVisible = 'true'">
						
						<aside class="first">
							
							<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'left-content-container.')]">
								<xsl:value-of select="HTML" disable-output-escaping="yes"/>
							</xsl:for-each>
						
						</aside>
						
					</xsl:if>
					
					<div class="content-wide">
					
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="$rightContentVisible = 'false' and $leftContentVisible = 'false'">content-wide </xsl:when>
								<xsl:otherwise>
									<xsl:if test="$rightContentVisible = 'true'">content </xsl:if>
									<xsl:if test="$leftContentVisible = 'true'">content omega</xsl:if>
								</xsl:otherwise>
							</xsl:choose>						
						</xsl:attribute>
					
						<xsl:apply-templates select="errors" />
						<xsl:apply-templates select="moduleXMLResponse" />
						<xsl:value-of select="moduleHTMLResponse" disable-output-escaping="yes"/>
						<xsl:value-of select="moduleTransformedResponse" disable-output-escaping="yes"/>
				
					</div>
					
					<xsl:if test="$rightContentVisible = 'true'">
						
						<aside>
							
							<xsl:for-each select="backgroundsModuleResponses/response[starts-with(slots/slot,'right-content-container.')]">
								<xsl:value-of select="HTML" disable-output-escaping="yes"/>
							</xsl:for-each>
								
						</aside>
						
					</xsl:if>
				
				</div>
				
				<footer>
	  			</footer>
				
			</body>
		
		<xsl:text disable-output-escaping="yes"><![CDATA[</html>]]></xsl:text>	
		
	</xsl:template>

	<xsl:template match="menuitem">
	
		<xsl:param name="menuLevel" select="1"/>
	
		<xsl:variable name="padding-left">
			<xsl:choose>
				<xsl:when test="$menuLevel > 1">
					<xsl:value-of select="(($menuLevel - 1) * 15)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'15'"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<li>	
	
			<xsl:if test="itemType='TITLE' and $menuLevel = 1">
				<xsl:attribute name="style"/>
			</xsl:if>
	
		 	<xsl:attribute name="class">
		 		
				<xsl:choose>
					<xsl:when test="itemType='MENUITEM'">
						<xsl:text>menuitem</xsl:text>
					</xsl:when>
					<xsl:when test="itemType='SECTION'">
						<xsl:text>section</xsl:text>
					</xsl:when>
					<xsl:when test="itemType='TITLE' and $menuLevel > 1">
						<xsl:text>title</xsl:text>
					</xsl:when>
					<xsl:when test="itemType='TITLE'">
						<xsl:text>title-expanded-menu</xsl:text>
						
						<xsl:if test="$menuLevel = 1 and position() = 1">
							<xsl:text> first</xsl:text>
						</xsl:if>
					</xsl:when>
					<xsl:when test="itemType='BLANK'">
						<xsl:text>blank</xsl:text>
					</xsl:when>				
				</xsl:choose>
		 		
		 		<xsl:if test="$menuLevel > 1">
		 			<xsl:text> child</xsl:text>
		 		</xsl:if>
		 		
		 		<xsl:if test="selected or menu">
			 		<xsl:text>-selected selected</xsl:text>
		 		</xsl:if>
	
		 		<xsl:if test="$menuLevel = 1 and (position() = last() or following-sibling::menuitem[position()=1][itemType = 'TITLE' or itemType = 'BLANK'])">
			 		<xsl:text> last</xsl:text>
		 		</xsl:if>	
			 		
		 	</xsl:attribute>	
	
			<xsl:choose>
				<xsl:when test="url">
		
					<a title="{description}" style="padding-left: {$padding-left}px;">
						
						<xsl:attribute name="href">
						
							<xsl:choose>
								<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
									<xsl:value-of select="/document/requestinfo/contextpath"/>
									<xsl:value-of select="url"/>												
								</xsl:when>
								
								<xsl:when test="urlType='FULL'">
									<xsl:value-of select="url"/>				
								</xsl:when>
							</xsl:choose>
							
						</xsl:attribute>
						
						<xsl:value-of select="name"/>					
					</a>
					
					<xsl:if test="itemType='SECTION'">
						<span />
					</xsl:if>
					
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:value-of select="name"/>
					
					<xsl:if test="itemType='BLANK'">
						<xsl:text>&#160;</xsl:text> 					
					</xsl:if>
					
				</xsl:otherwise>				
			</xsl:choose>
			
		</li>
		
		<xsl:apply-templates select="menu/menuitem">
			<xsl:with-param name="menuLevel" select="$menuLevel + 1"/>
		</xsl:apply-templates>
					
	</xsl:template>
	
	<xsl:template match="menuitem" mode="sections">

		<xsl:param name="submenu" select="false()" />
		
		<xsl:variable name="slotURL" select="url" />

		<xsl:choose>
			<xsl:when test="/document/backgroundsModuleResponses/response[slots/slot=$slotURL]">
				<xsl:value-of select="/document/backgroundsModuleResponses/response[slots/slot=$slotURL]/HTML" disable-output-escaping="yes"></xsl:value-of>
			</xsl:when>
			<xsl:otherwise>
				
				<xsl:variable name="childs" select="menu/menuitem" />
				
				<li>
					
					<xsl:choose>
						<xsl:when test="url">
							
							<xsl:attribute name="class">
								<xsl:if test="menu/menuitem[selected] or selected">
									<xsl:text>active selected</xsl:text>
								</xsl:if>
								<xsl:if test="$childs">
									<xsl:text> dd</xsl:text>
								</xsl:if>
							</xsl:attribute>
							
							<xsl:if test="$childs">
								<div class="marker"></div>
							</xsl:if>
							
							<a title="{description}">				
							
								<xsl:if test="not($childs)">
									<xsl:choose>
										<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
											<xsl:attribute name="href"><xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="url"/></xsl:attribute>				
										</xsl:when>			
										<xsl:when test="urlType='FULL'">
											<xsl:attribute name="href"><xsl:value-of select="url"/></xsl:attribute>				
										</xsl:when>
									</xsl:choose>
								</xsl:if>
								
								<xsl:if test="$submenu">
									<span class="icon arrow"><i data-icon-before=">"></i></span>
								</xsl:if>
								
								<span>
									
									<xsl:if test="$submenu">
										<xsl:attribute name="class">text</xsl:attribute>
									</xsl:if>
									
									<xsl:value-of select="name"/>
									
									<xsl:if test="$childs">
										<span class="icon">_</span>
									</xsl:if>
										
								</span>
							
							</a>
							
							<xsl:if test="$childs">
								<div class="submenu">
									<ul>
										<xsl:apply-templates select="$childs" mode="sections">
											<xsl:with-param name="submenu" select="true()" />
										</xsl:apply-templates>
									</ul>
								</div>
							</xsl:if>
							
						</xsl:when>
						<xsl:otherwise>
						
							<xsl:attribute name="class">no-url</xsl:attribute>
						
							<xsl:value-of select="name"/>
							
							<xsl:if test="itemType='BLANK'">
								<xsl:text>&#160;</xsl:text> 					
							</xsl:if>
							
						</xsl:otherwise>
					</xsl:choose>
					
				</li>
						
			</xsl:otherwise>			
		</xsl:choose>
	
	</xsl:template>

	<xsl:template match="moduleXMLResponse">
	
		<xsl:apply-templates mode="module" />
		
	</xsl:template>
	
</xsl:stylesheet>