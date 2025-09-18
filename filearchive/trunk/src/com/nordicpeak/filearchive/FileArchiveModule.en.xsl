<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:variable name="java.addFileCollectionBreadCrumbText">Add folder</xsl:variable>
	<xsl:variable name="java.updateFileCollectionBreadCrumbText">Update folder: </xsl:variable>
	<xsl:variable name="java.unnamedFileCollectionText">Unnamed folder</xsl:variable>
		
	<xsl:variable name="i18n.validation.requiredField" select="'You need to fill in the field'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Invalid value in field'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'Too short content in field'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'Too long content in field'"/>		
	<xsl:variable name="i18n.validation.unknownError" select="'Unknown problem validating field'"/>
	<xsl:variable name="i18n.unknownFault" select="'An unknown fault has occurred'"/>
		
	<xsl:variable name="i18n.fileCollections">Folders</xsl:variable>
	
	<xsl:variable name="i18n.name">Name</xsl:variable>
	<xsl:variable name="i18n.posted">Created</xsl:variable>
	<xsl:variable name="i18n.poster">By</xsl:variable>
	<xsl:variable name="i18n.fileCount">Files</xsl:variable>
	<xsl:variable name="i18n.noFileCollectionsFound">No folders found</xsl:variable>
	<xsl:variable name="i18n.addFileCollection">Add folder</xsl:variable>
	<xsl:variable name="i18n.editFileCollection">Update folder</xsl:variable>
	<xsl:variable name="i18n.deleteFileCollection">Delete folder</xsl:variable>
	<xsl:variable name="i18n.AddFileCollection.header">Add folder</xsl:variable>
	<xsl:variable name="i18n.addFileCollection.button">Add</xsl:variable>
	<xsl:variable name="i18n.UpdateFileCollection.header">Update folder</xsl:variable>
	<xsl:variable name="i18n.updateFileCollection.button">Save changes</xsl:variable>
	<xsl:variable name="i18n.notRequiredField">(not required)</xsl:variable>
	<xsl:variable name="i18n.files">Files</xsl:variable>
	<xsl:variable name="i18n.deleteFileText">Delete file</xsl:variable>
	<xsl:variable name="i18n.unNamedFileCollection">Unnamed folder</xsl:variable>
	<xsl:variable name="i18n.deletetionDate">Deletion date</xsl:variable>
	<xsl:variable name="i18n.requestedFileCollectionNotFound">The requested folder was not found</xsl:variable>
	<xsl:variable name="i18n.updateFailedFileCollectionNotFound">The requested folder was not found</xsl:variable>
	<xsl:variable name="i18n.deleteFailedFileCollectionNotFound">The requested folder was not found</xsl:variable>
	<xsl:variable name="i18n.noFilesInCollection">This folder contains no files</xsl:variable>
	<xsl:variable name="i18n.fileCollectionURL">URL to this folder</xsl:variable>
	<xsl:variable name="i18n.uploadLimit1">You can upload one or many files but only </xsl:variable>
	<xsl:variable name="i18n.uploadLimit2">MB at a time.</xsl:variable>
	<xsl:variable name="i18n.fileSizeLimitExceeded">Maximum upload size exceeded</xsl:variable>
	<xsl:variable name="i18n.downloadAllFilesAsZip">Download all files</xsl:variable>
	
	<xsl:variable name="i18n.accessConfigDescription">Select which users and groups that should be allowed to access this folder.</xsl:variable>
	<xsl:variable name="i18n.Groups">Groups</xsl:variable>
	<xsl:variable name="i18n.Users">Users</xsl:variable>
	<xsl:variable name="i18n.Persons">Persons</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifier">Citizen identifier</xsl:variable>
	<xsl:variable name="i18n.NoPersonsYet">No persons added yet</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierFormatted">Citizen identifer (YYYYMMDD-XXXX)</xsl:variable>
	<xsl:variable name="i18n.DuplicateID">Citizen identifier already added</xsl:variable>
	<xsl:variable name="i18n.InvalidID">Invalid citizen identifier</xsl:variable>
	<xsl:variable name="i18n.UnknownError">An unknown error occurred</xsl:variable>
	<xsl:variable name="i18n.AddPerson">Add Person</xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part1">Files below will be removed in </xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part2"> days</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionText">Name this file collection. The name will be visible for the recipients of this collection</xsl:variable>
	<xsl:variable name="i18n.SecureCollection">Use secure login (social security number) to allow reading this file collection</xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.1">All collections will be removed automatically after</xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.2">if you don't choose to delete them earlier</xsl:variable>
	<xsl:variable name="i18n.SecureFileCollectionURL">URL to this folder, remember to tell the recipients to use secure login</xsl:variable>
	<xsl:variable name="i18n.Continue">Conitnue</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfo">Secure collection</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionLoggedIn">You're already logged in with an unsecure login method. Please log out and reload this page.</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierHelp">Type citizen identifiers for persons allowed to read this collection</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part1">This collection is marked as secure and you are required to use a secure login to read it. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.1">Files will be automatically removed after </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.2"> days. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part3">Click on continue to login with a secure login method.</xsl:variable>
	<xsl:variable name="i18n.NameCollection">Name collection</xsl:variable>
	<xsl:variable name="i18n.UploadFiles">Upload files</xsl:variable>
	<xsl:variable name="i18n.AccessHeader">Choose access control</xsl:variable>
	<xsl:variable name="i18n.CopyLink">Copy link</xsl:variable>
	<xsl:variable name="i18n.LinkCopied">Link has been copied</xsl:variable>
	<xsl:variable name="i18n.PasswordCopied">Password has been copied</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionAndGetLink">Add file collection and create link to files</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionFinishText">If you have added all files you want to send and selected who will be able to access the files, you can click "Add" below. In the next step you will get a link to the file collection.</xsl:variable>
	<xsl:variable name="i18n.UsersAndGroups">Users and groups</xsl:variable>
	<xsl:variable name="i18n.PublicCollection">Public file collection, accessible for all who have access to the link</xsl:variable>
	<xsl:variable name="i18n.InvalidSecurityMode">Invalid choice of access control for file collection</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollection">Encrypt file collection</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollectionText">Encrypt the file collection for extra security. Only persons with the password will be able to open it.</xsl:variable>
	<xsl:variable name="i18n.EncryptionPassword">Lösenord för kryptering</xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part1"> (Minimum length for password is </xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part2"> characters)</xsl:variable>
	<xsl:variable name="i18n.Generate">Generate</xsl:variable>
	<xsl:variable name="i18n.Copy">Copy</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollection">Encrypted file collection</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollectionText">The files in this collection are encrypted. To be able to fetch them, you need to type the password below.</xsl:variable>
	<xsl:variable name="i18n.Password">Password</xsl:variable>
	<xsl:variable name="i18n.ShowFiles">Show files</xsl:variable>
	<xsl:variable name="i18n.InvalidPassword">The password is invalid</xsl:variable>
	<xsl:variable name="i18n.Encrypted">Encrypted</xsl:variable>
	<xsl:variable name="i18n.AllowsCollaboration">Allows collaboration, users with access to the collection will be able to upload files</xsl:variable>
	<xsl:variable name="i18n.Uploading">Uploading...</xsl:variable>
	<xsl:variable name="i18n.CollaborationCollectionText">You can add files to this file collection.</xsl:variable>
	<xsl:variable name="i18n.UploadFile">Upload file</xsl:variable>
	<xsl:variable name="i18n.SelectedFileTooBig">The selected file is too big</xsl:variable>
	<xsl:variable name="i18n.SelectFile">Select file to upload</xsl:variable>
	<xsl:variable name="i18n.ImportedUsers">Imported users</xsl:variable>
	<xsl:variable name="i18n.accessConfigDescription.hiddenGroupAccess">Select the persons who's supposed to access this file collection</xsl:variable>
	<xsl:variable name="i18n.AutomaticDelete">Automatic deletion</xsl:variable>
	<xsl:variable name="i18n.SetUserKeepDays">Set an own time for deletion of the file collection</xsl:variable>
	<xsl:variable name="i18n.UserKeepDays">Number of days to keep the file collection</xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.1">Files will be automatically removed after </xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.2"> days if you don't choose to delete them earlier. Here you are given the opportunity to set the number of days before automatic removal.</xsl:variable>
	<xsl:variable name="i18n.MaximumNumberOfKeepDays">Maximum number of days is</xsl:variable>
</xsl:stylesheet>
