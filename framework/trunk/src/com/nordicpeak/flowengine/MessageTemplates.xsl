<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:variable name="imgPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="FlowInstance" mode="externalMessage">
	
		<xsl:if test="../NoContactWay">
			<span class="italic">
				<xsl:value-of select="$i18n.NewMessageWarningNoContactWay"/>
			</span>
		</xsl:if>
		
		<label class="required" for="message"><xsl:value-of select="$i18n.Message" /></label>
		<xsl:apply-templates select="../validationError[fieldName = 'externalmessage']" />
		<textarea id="message" name="externalmessage" data-requiredmessage="{$i18n.ValidationError.ExternalMessageRequired}"  class="full" rows="10" value="{../requestparameters/parameter[name = 'externalmessage']/value}"/>
		
		<xsl:if test="not(Flow/hideExternalMessageAttachments = 'true')">
		
			<div class="heading-wrapper">
				<label><xsl:value-of select="$i18n.AttachFiles" /></label>
			</div>
			
			<script>
				imagePath = '<xsl:value-of select="/Document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/Document/module/sectionID"/>/<xsl:value-of select="/Document/module/moduleID"/>/pics';
				deleteFile = '<xsl:value-of select="$i18n.DeleteFile" />';
			</script>
			
			<xsl:apply-templates select="../validationError[messageKey = 'FileSizeLimitExceeded' or messageKey = 'UnableToParseRequest']" />
			
			<div class="full">
				
				<div class="upload clearboth">
					<span class="btn btn-upload btn-blue">
						<xsl:value-of select="$i18n.ChooseFiles" />
						<input id="external-message" type="file" name="attachments" multiple="multiple" size="55" class="qloader externalmessages bigmarginbottom" />
					</span>
					<span><xsl:value-of select="$i18n.MaximumFileSize" />: <xsl:value-of select="../FormattedMaxFileSize" /></span>
				</div>
				
				<ul id="external-message-qloader-filelist" class="files" />
				
			</div>
			
		</xsl:if>
	
	</xsl:template>

	<xsl:template match="ExternalMessage" mode="admin">
	
		<xsl:variable name="messageTypeText">
			<xsl:call-template name="getExternalMessageTypeText"/>
		</xsl:variable>
	
		<xsl:call-template name="createAdminMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="attachments" select="attachments/ExternalMessageAttachment" />
			<xsl:with-param name="prefix" select="'messages'" />
			<xsl:with-param name="messageTypeText" select="$messageTypeText" />
		</xsl:call-template>
		
	</xsl:template>	
	
	<xsl:template match="InternalMessage" mode="admin">
	
		<xsl:variable name="messageTypeText">
			<xsl:call-template name="getInternalMessageTypeText"/>
		</xsl:variable>
	
		<xsl:call-template name="createAdminMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="attachments" select="attachments/InternalMessageAttachment" />
			<xsl:with-param name="prefix" select="'notes'" />
			<xsl:with-param name="messageTypeText" select="$messageTypeText" />
			<xsl:with-param name="postedByManager" select="'true'" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="createAdminMessage">
		
		<xsl:param name="message" />
		<xsl:param name="attachments" />
		<xsl:param name="prefix" />
		<xsl:param name="messageTypeText" />
		<xsl:param name="postedByManager" select="$message/postedByManager" />
		<xsl:param name="repliesEnabled" select="false()" />
		
		<li id="{$prefix}-{$message/messageID}" class="official" data-message-id="{$message/messageID}">
			
			<xsl:attribute name="class">
			
				<!-- TODO check if this message is unread -->
				<xsl:if test="false()">unread</xsl:if>
				
				<xsl:choose>
					<xsl:when test="$postedByManager = 'true' or ../../../user/userID = poster/userID"> me</xsl:when>
					<xsl:otherwise>	official</xsl:otherwise>
				</xsl:choose>
				
			</xsl:attribute>

			<div class="user">
				<figure><img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/profile-standard.png" /></figure>
			</div>
			
			<xsl:if test="$repliesEnabled and $postedByManager = 'false'">
				<a href="#" class="btn btn-blue btn-right bigmarginright reply_message"><i data-icon-before="+"></i><xsl:value-of select="$i18n.ReplyMessage" /></a>
			</xsl:if>
						
			<div class="message" data-messageid="{$message/messageID}">
				
				<xsl:value-of select="$messageTypeText" />
							
				<xsl:if test="$repliesEnabled and $message/QuotedMessage">
							
					<div class="quote bigmarginbottom">
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="$message/QuotedMessage/message" />
						</xsl:call-template>
						
						<span class="author">
							<xsl:call-template name="printUser">
								<xsl:with-param name="user" select="$message/QuotedMessage/poster" />
							</xsl:call-template>
							
						 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="$message/QuotedMessage/added" /></span>
						</span>
					</div>
					<br/>
					
				</xsl:if>
			
				<div>
				<xsl:choose>
					<xsl:when test="$postedByManager = 'true'">
						<xsl:call-template name="replaceLineBreaksAndLinks">
							<xsl:with-param name="string" select="$message/message"/>
							<xsl:with-param name="target" select="'_blank'"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="$message/message" />
						</xsl:call-template>					
					</xsl:otherwise>
				</xsl:choose>
				</div>
				
				<xsl:if test="ViewFragmentExtension">
					<xsl:for-each select="ViewFragmentExtension/ViewFragment">
						<xsl:value-of select="HTML" disable-output-escaping="yes"/>
					</xsl:for-each>
				</xsl:if>
				
				<div class="author">
					<i data-icon-before="m"/>
					<xsl:call-template name="printUser">
						<xsl:with-param name="user" select="$message/poster" />
					</xsl:call-template>
				 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="$message/added" /></span>
				 </div>
				 
				 <xsl:if test="$message/readReceiptEnabled = 'true'">
				 
				 	<xsl:choose>
				 		<xsl:when test="$message/readReceipts">
				 		
							<xsl:call-template name="showReadReceipts">
								<xsl:with-param name="messageID" select="$message/messageID" />
							</xsl:call-template>
				 		
				 		</xsl:when>
				 		<xsl:otherwise>
				 		
		 					<div class="read-receipt-requested">
		 					
	 							<img class="marginright" src="{$imgPath}/check.png" alt="" />
	 							
						 		<xsl:value-of select="$i18n.ReadReceipt.Requested" />
						 		
						 		<a class="btn btn-light display-inline-block bigmarginleft" href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/disablereadreceipt/{$message/messageID}" onclick="return confirm('{$i18n.ReadReceipt.Disable.Confirm}')">
						 			<img class="marginright" src="{$imgPath}/delete.png" alt="" />
						 			<xsl:value-of select="$i18n.ReadReceipt.Disable" />
						 		</a>
						 		
						 	</div>
						 	
				 		</xsl:otherwise>
				 	</xsl:choose>
				 
				 </xsl:if>
				 
				 <xsl:if test="$attachments">
					<div class="files">
						<xsl:apply-templates select="$attachments" />
					</div>
				</xsl:if>
				
			</div>
			
			<div class="marker"></div>
			
		</li>
	
	</xsl:template>
	
	<xsl:template match="ExternalMessage" mode="user">
	
		<xsl:call-template name="createUserMessage">
			<xsl:with-param name="message" select="." />
			<xsl:with-param name="hideUsername" select="''" />
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="createUserMessage">
	
		<xsl:param name="message" />
		<xsl:param name="hideManagerDetails" select="false()" />
		<xsl:param name="repliesEnabled" select="false()" />
		<xsl:param name="hideUsername" select="''" />
	
		<li id="messages-{$message/messageID}" data-message-id="{$message/messageID}">
		
			<xsl:attribute name="class">
			
				<!-- TODO check if this message is unread -->
				<xsl:if test="false()">unread</xsl:if>
			
				<xsl:choose>
					<xsl:when test="$message/postedByManager = 'false'"> me</xsl:when>
					<xsl:otherwise> official</xsl:otherwise>
				</xsl:choose>
					
			</xsl:attribute>

			<div class="user">
				<figure><img alt="" src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/profile-standard.png" /></figure>
			</div>
			
			<xsl:if test="$repliesEnabled and $message/postedByManager = 'true'">
				<a href="#" class="btn btn-blue btn-right bigmarginright reply_message"><i data-icon-before="+"></i><xsl:value-of select="$i18n.ReplyMessage" /></a>
			</xsl:if>
			
			<div class="message" data-messageid="{$message/messageID}">
			
				<xsl:choose>
					<xsl:when test="$message/noReadReceiptAccess">
					
						<div class="read-receipt-info">
						
							<xsl:value-of select="$i18n.ReadReceiptInfo" />
							
							<div class="text-align-center">
								<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/addreadreceipt/{$message/messageID}" class="btn btn-green" onclick="return confirm('{$i18n.ReadReceiptInfo.Accept.Confirm}')">
									<xsl:value-of select="$i18n.ReadReceiptInfo.Accept" />
								</a>
							</div>
						
						</div>
					
						<div class="blur-text">
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque nec sagittis sem, ut tristique urna. Morbi rhoncus lacinia finibus. Aenean in erat maximus, congue enim eu, sollicitudin ipsum. Curabitur auctor pharetra odio in elementum. 
							<br/><br/>
							Sed convallis ac est quis rutrum. Duis sit amet condimentum ex. Maecenas vitae augue a metus ultrices interdum vel ac nisi. Aliquam erat volutpat.
						</div>
							
					</xsl:when>
					<xsl:otherwise>
					
						<div>
							<xsl:call-template name="getExternalMessageTypeText"/>
						
							<xsl:if test="$repliesEnabled and $message/QuotedMessage">
							
								<div class="quote bigmarginbottom">
									<xsl:call-template name="replaceLineBreak">
										<xsl:with-param name="string" select="$message/QuotedMessage/message" />
									</xsl:call-template>
									
									<span class="author">
										<xsl:call-template name="printUser">
											<xsl:with-param name="user" select="$message/QuotedMessage/poster" />
											<xsl:with-param name="hideUsername" select="$hideUsername"/>
										</xsl:call-template>
										
									 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="$message/QuotedMessage/added" /></span>
									</span>
								</div>
								<br/>
								
							</xsl:if>
						
							<xsl:choose>
								<xsl:when test="$message/postedByManager = 'true'">
									<xsl:call-template name="replaceLineBreaksAndLinks">
										<xsl:with-param name="string" select="$message/message"/>
										<xsl:with-param name="target" select="'_blank'"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="replaceLineBreak">
										<xsl:with-param name="string" select="$message/message" />
									</xsl:call-template>					
								</xsl:otherwise>
							</xsl:choose>
						</div>
					
						<xsl:if test="ViewFragmentExtension">
							<xsl:for-each select="ViewFragmentExtension/ViewFragment">
								<xsl:value-of select="HTML" disable-output-escaping="yes"/>
							</xsl:for-each>
						</xsl:if>
					
					</xsl:otherwise>
				</xsl:choose>
			
				<div class="author">
				
					<i data-icon-before="m"/>
					
					<xsl:choose>
						<xsl:when test="$message/postedByManager = 'true' and $hideManagerDetails = 'true'">
						
							<xsl:value-of select="$i18n.PostedByManager"/>
						
						</xsl:when>
						<xsl:otherwise>

							<xsl:call-template name="printUser">
								<xsl:with-param name="user" select="$message/poster" />
								<xsl:with-param name="hideUsername" select="$hideUsername"/>
							</xsl:call-template>
						
						</xsl:otherwise>
					</xsl:choose>
					
				 	<span class="time"><xsl:text>&#160;·&#160;</xsl:text><xsl:value-of select="$message/added" /></span>
				 	
				 </div>
				 
		 		<xsl:if test="$message/readReceipts">
		 		
					<xsl:call-template name="showReadReceipts">
						<xsl:with-param name="messageID" select="$message/messageID" />
					</xsl:call-template>
		 		
		 		</xsl:if>
				 
				 <xsl:if test="$message/attachments/ExternalMessageAttachment">
					<div class="files">
						<xsl:apply-templates select="$message/attachments/ExternalMessageAttachment"/>
					</div>
				</xsl:if>
				
			</div>
			
			<div class="marker"></div>
			
		</li>
	
	</xsl:template>
	
	<xsl:template match="ExternalMessageAttachment">
		
		<xsl:variable name="url">
			<xsl:choose>
				<xsl:when test="../../noReadReceiptAccess">
					<xsl:text>#</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/externalattachment/<xsl:value-of select="../../messageID" />/<xsl:value-of select="attachmentID" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="text">
			<xsl:choose>
				<xsl:when test="../../noReadReceiptAccess">
					<span class="blur-text">loremipsumdolor.sit</span>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="filename" /><xsl:text>&#160;</xsl:text><span class="size">(<xsl:value-of select="FormatedSize" />)</span>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<a href="{$url}" class="btn btn-file">
			
			<xsl:if test="../../noReadReceiptAccess">
				<xsl:attribute name="class">btn btn-file disabled</xsl:attribute>
			</xsl:if>
		
			<i data-icon-before="d"></i>
			
			<xsl:copy-of select="$text" />
		</a>
		
	</xsl:template>
	
	<xsl:template match="InternalMessageAttachment">
		
		<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/internalattachment/{../../messageID}/{attachmentID}" class="btn btn-file"><i data-icon-before="d"></i><xsl:value-of select="filename" /><xsl:text>&#160;</xsl:text><span class="size">(<xsl:value-of select="FormatedSize" />)</span></a>
		
	</xsl:template>
	
	<xsl:template name="showReadReceipts">
		<xsl:param name="messageID" />
	
		<div class="read-receipts-wrapper">
			
 			<div class="bigmargintop">
	 			<button class="read-receipts btn btn-light">
						<img class="marginright" src="{$imgPath}/check.png" alt="" />
	 				<xsl:value-of select="$i18n.ReadReceipts"/>
	 			</button>
 			</div>
 			
				<div class="hidden">

				<div class="read-receipts-modal errands-wrapper nopadding nomargin" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/getreadreceipts/{$messageID}">
					
					<h1><xsl:value-of select="$i18n.ReadReceipts" /></h1>
					
					<table class="full coloredtable oep-table" cellspacing="0"></table>									
					
				</div>
				
			</div>
			
			<div class="hidden">
				<table>
					<tr class="read-receipt-template">
						<td class="icon">
							<img src="{$imgPath}/check.png" alt="" />
						</td>
						<td class="user" data-title="{$i18n.ReadReceipt.Table.User}"></td>
						<td class="read" data-title="{$i18n.ReadReceipt.Table.Date}"></td>
						<td data-title="{$i18n.ReadReceipt.Table.Description}">
							<xsl:value-of select="$i18n.ReadReceipt.Read" />
						</td>
					</tr>
				</table>
			</div>
			
			<div class="hidden">
				<table>
					<tr class="attachment-download-template">
						<td class="icon">
							<img src="{$imgPath}/download.png" alt="" />
						</td>
						<td class="user" data-title="{$i18n.ReadReceipt.Table.User}"></td>
						<td class="downloaded" data-title="{$i18n.ReadReceipt.Table.Date}"></td>
						<td data-title="{$i18n.ReadReceipt.Table.Description}">
							<xsl:value-of select="$i18n.ReadReceipt.Downloaded" />
							<xsl:text>: </xsl:text>
							<span class="attachment-filename"></span>
						</td>
					</tr>
				</table>
			</div>
			
		</div>
			
	</xsl:template>
	
	<xsl:template match="ExternalMessageReadReceipt">
		
		<tr>
			<td class="icon">
				<img src="{$imgPath}/check.png" alt="" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.User}">
				<xsl:value-of select="user/firstname" />
				<xsl:text> </xsl:text> 
				<xsl:value-of select="user/lastname" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.Date}">
				<xsl:value-of select="read" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.Description}">
				<xsl:value-of select="$i18n.ReadReceipt.Read" />
			</td>
		</tr>
		
		<xsl:apply-templates select="AttachmentDownloads/ExternalMessageReadReceiptAttachmentDownload" />
		
		
	</xsl:template>
	
	<xsl:template match="ExternalMessageReadReceiptAttachmentDownload">
		
		<tr>
			<td class="icon">
				<img src="{$imgPath}/download.png" alt="" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.User}">
				<xsl:value-of select="../../user/firstname" />
				<xsl:text> </xsl:text> 
				<xsl:value-of select="../../user/lastname" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.Date}">
				<xsl:value-of select="downloaded" />
			</td>
			<td data-title="{$i18n.ReadReceipt.Table.Description}">
				<xsl:value-of select="$i18n.ReadReceipt.Downloaded" />
				<xsl:text>: </xsl:text>
				<xsl:value-of select="attachmentFilename" />
			</td>
		</tr>
		
		
	</xsl:template>
	
	<xsl:template name="sortMessagesButton">
		<xsl:param name="type" select="'external'" />
		<xsl:param name="defaultOrder" select="'ASC'" />
		
		<div class="sortMessages display-inline-block pointer bigmarginbottom">

			<xsl:attribute name="data-type"><xsl:value-of select="$type" /></xsl:attribute>
			
			<strong class="marginright"><xsl:value-of select="$i18n.Message.Date" /></strong>		
			
			<div class="floatright">
			
				<i class="display-inline-block">
					<xsl:if test="not($defaultOrder = 'DESC')">
						<xsl:attribute name="class">display-inline-block flipped</xsl:attribute>
					</xsl:if>
					<xsl:text>_</xsl:text>
				</i>
		
			</div>
				
		</div>
	
	</xsl:template>	
	
	<xsl:template name="getExternalMessageTypeText" />

	<xsl:template name="getInternalMessageTypeText" />
	
	<xsl:template match="validationError[messageKey='FileSizeLimitExceeded']">
	
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part1"/>
					<xsl:value-of select="filename"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part2"/>
					<xsl:value-of select="size"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part3"/>
					<xsl:value-of select="maxFileSize"/>
					<xsl:value-of select="$i18n.FileSizeLimitExceeded.part4"/>
				</strong>
			</span>
		</div>
	
	</xsl:template>
	
	<xsl:template match="validationError[messageKey='UnableToParseRequest']">
	
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:value-of select="$i18n.ValidationError.UnableToParseRequest" />
				</strong>
			</span>
		</div>
		
	</xsl:template>						
	
	<xsl:template match="validationError[fieldName='externalmessage']">
		
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageRequired" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageToShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.ValidationError.ExternalMessageToLong" />
						</xsl:when>
					</xsl:choose>
				</strong>
			</span>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError[fieldName='internalmessage']">
		
		<div class="info-box error">
			<span>
				<strong data-icon-before="!">
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageRequired" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageToShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.ValidationError.InternalMessageToLong" />
						</xsl:when>
					</xsl:choose>
				</strong>
			</span>
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>