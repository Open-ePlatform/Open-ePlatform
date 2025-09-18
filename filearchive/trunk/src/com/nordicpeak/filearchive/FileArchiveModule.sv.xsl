<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:variable name="java.addFileCollectionBreadCrumbText">L�gg till filsamling</xsl:variable>
	<xsl:variable name="java.updateFileCollectionBreadCrumbText">Uppdatera filsamling: </xsl:variable>
	<xsl:variable name="java.unnamedFileCollectionText">Namnl�s filsamling</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format p� f�ltet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'F�r kort inneh�ll i f�ltet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Ok�nt fel p� f�ltet'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett ok�nt fel har uppst�tt'"/>
		
	<xsl:variable name="i18n.fileCollections">Filsamlingar</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.posted">Skapad</xsl:variable>
	<xsl:variable name="i18n.poster">Skapad av</xsl:variable>
	<xsl:variable name="i18n.fileCount">Filer</xsl:variable>
	<xsl:variable name="i18n.noFileCollectionsFound">Inga filsamlingar hittades</xsl:variable>
	<xsl:variable name="i18n.addFileCollection">L�gg till filsamling</xsl:variable>
	<xsl:variable name="i18n.editFileCollection">Uppdatera filsamling</xsl:variable>
	<xsl:variable name="i18n.deleteFileCollection">Vill du ta bort filsamlingen</xsl:variable>
	<xsl:variable name="i18n.AddFileCollection.header">L�gg till filsamling</xsl:variable>
	<xsl:variable name="i18n.addFileCollection.button">L�gg till</xsl:variable>
	<xsl:variable name="i18n.UpdateFileCollection.header">Uppdatera filsamling</xsl:variable>
	<xsl:variable name="i18n.updateFileCollection.button">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.notRequiredField">(ej obligatoriskt)</xsl:variable>
	<xsl:variable name="i18n.files">Filer</xsl:variable>
	<xsl:variable name="i18n.deleteFileText">Ta bort fil</xsl:variable>
	<xsl:variable name="i18n.unNamedFileCollection">Namnl�s filsamling</xsl:variable>
	<xsl:variable name="i18n.deletetionDate">Borttagningsdatum</xsl:variable>
	<xsl:variable name="i18n.requestedFileCollectionNotFound">Den beg�rda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.updateFailedFileCollectionNotFound">Den beg�rda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.deleteFailedFileCollectionNotFound">Den beg�rda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.noFilesInCollection">Den h�r filsamlingen inneh�ller inga filer.</xsl:variable>
	<xsl:variable name="i18n.fileCollectionURL">Kopiera denna l�nk och skicka den till den person som du vill dela dessa filer till</xsl:variable>
	<xsl:variable name="i18n.uploadLimit1">Du kan ladda upp en eller flera filer, maximalt</xsl:variable>
	<xsl:variable name="i18n.uploadLimit2">MB �t g�ngen.</xsl:variable>
	<xsl:variable name="i18n.fileSizeLimitExceeded">Maximal uppladdningsstorlek �verskriden</xsl:variable>
	<xsl:variable name="i18n.downloadAllFilesAsZip">Ladda ner alla filer</xsl:variable>
	
	<xsl:variable name="i18n.accessConfigDescription">Ange vilka personer eller grupper som ska f� �tkomst till denna filsamling.</xsl:variable>
	<xsl:variable name="i18n.Groups">Grupper</xsl:variable>
	<xsl:variable name="i18n.Users">Anv�ndare</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionText">Ange ett namn p� din filsamling, detta namn kommer att visas som rubrik f�r den eller de filer du delar till mottagaren. Ladda d�refter upp de filer som du vill skicka till mottagaren genom att klicka p� "V�lj filer" knappen.</xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part1">Filerna nedan kommer att tas bort om </xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part2"> dagar.</xsl:variable>
	<xsl:variable name="i18n.Persons">Personer</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifier">Personnummer</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierFormatted">Personnummer (YYYYMMDDXXXX)</xsl:variable>
	<xsl:variable name="i18n.NoPersonsYet">Inga personer upplagda �nnu</xsl:variable>
	<xsl:variable name="i18n.DuplicateID">Personnummer redan tillagt</xsl:variable>
	<xsl:variable name="i18n.InvalidID">Ogiltigt personnummer</xsl:variable>
	<xsl:variable name="i18n.UnknownError">Ett ok�nt fel intr�ffade</xsl:variable>
	<xsl:variable name="i18n.AddPerson">L�gg till person</xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.1">Samtliga filsamlingar och filer som skickas via denna tj�nst raderas automatiskt efter </xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.2"> dagar, om du inte sj�lv v�ljer att radera dessa tidigare.</xsl:variable>
	<xsl:variable name="i18n.SecureCollection">Kr�v s�ker inloggning med e-legitimation (t.ex. Mobilt BankID) f�r att l�sa denna filsamling</xsl:variable>
	<xsl:variable name="i18n.SecureFileCollectionURL">Kopiera denna l�nk och skicka den till den person som du vill dela dessa filer till, kom ih�g att p�minna mottagare om att v�lja inloggning med E-legitimation</xsl:variable>
	<xsl:variable name="i18n.Continue">Forts�tt</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfo">S�ker filsamling</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionLoggedIn">Du �r redan inloggad med en os�ker inloggning. F�r att visa filsamlingen, logga ut och �ppna l�nken igen f�r att logga in med e-legitimation.</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part1">Den h�r informationen �r skickad s�kert och kr�ver att du loggar in med e-legitimation innan du kan ta del av den. Ansvaret f�r att informationen hanteras s�kert �ven efter nedladdningen ligger p� dig som anv�ndare. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.1">Filerna kommer automatiskt att tas bort efter </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.2"> dagar. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part3">Klicka p� Forts�tt f�r att g� vidare och logga in med e-legitimation.</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierHelp">Ange personnummer p� de personer som ska f� l�sa denna filsamling</xsl:variable>
	<xsl:variable name="i18n.NameCollection">Namnge filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UploadFiles">V�lj fil eller filer</xsl:variable>
	<xsl:variable name="i18n.AccessHeader">V�lj s�kerhet f�r �tkomst till filerna</xsl:variable>
	<xsl:variable name="i18n.CopyLink">Kopiera l�nk</xsl:variable>
	<xsl:variable name="i18n.LinkCopied">L�nken har kopierats</xsl:variable>
	<xsl:variable name="i18n.PasswordCopied">L�senordet har kopierats till klippbordet. Kom ih�g att spara det!</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionAndGetLink">L�gg till filsamlingen och skapa l�nken till filerna</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionFinishText">Om du har lagt till alla filer som du vill skicka samt valt vilka som skall kunna komma �t filerna, s� kan du klicka p� "L�gg till" h�r nedan. Efter att du klickat p� knappen f�r du en l�nk till filsamlingen.</xsl:variable>
	<xsl:variable name="i18n.UsersAndGroups">Anv�ndare och grupper</xsl:variable>
	<xsl:variable name="i18n.PublicCollection">Publik filsamling, tillg�nglig f�r alla som har tillg�ng till l�nken</xsl:variable>
	<xsl:variable name="i18n.InvalidSecurityMode">Ogiltigt val av s�kerhet f�r filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollection">Kryptera filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollectionText">Kryptera filsamlingen f�r extra s�kerhet. Endast personer med l�senordet kommer att kunna h�mta filerna.</xsl:variable>
	<xsl:variable name="i18n.EncryptionPassword">L�senord f�r kryptering</xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part1"> (L�ngden p� l�senordet skall vara </xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part2"> tecken)</xsl:variable>
	<xsl:variable name="i18n.Generate">Generera</xsl:variable>
	<xsl:variable name="i18n.Copy">Kopiera</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollection">Krypterad filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollectionText">Filerna i denna filsamling �r krypterade. F�r att kunna visa och h�mta dem s� beh�ver du ange l�senordet nedan.</xsl:variable>
	<xsl:variable name="i18n.Password">L�senord</xsl:variable>
	<xsl:variable name="i18n.ShowFiles">Visa filer</xsl:variable>
	<xsl:variable name="i18n.InvalidPassword">Du har angett ett ogiltigt l�senord</xsl:variable>
	<xsl:variable name="i18n.Encrypted">Krypterad</xsl:variable>
	<xsl:variable name="i18n.AllowsCollaboration">Till�t samarbete, personer med tillg�ng till filsamlingen kommer att kunna ladda upp filer.</xsl:variable>
	<xsl:variable name="i18n.Uploading">Laddar upp...</xsl:variable>
	<xsl:variable name="i18n.CollaborationCollectionText">Du har m�jlighet att bidra med filer till denna filsamling.</xsl:variable>
	<xsl:variable name="i18n.UploadFile">Ladda upp</xsl:variable>
	<xsl:variable name="i18n.SelectedFileTooBig">Den valda filen �r f�r stor</xsl:variable>
	<xsl:variable name="i18n.SelectFile">V�lj fil att ladda upp</xsl:variable>
	<xsl:variable name="i18n.ImportedUsers">Beh�riga anv�ndare</xsl:variable>
	<xsl:variable name="i18n.accessConfigDescription.hiddenGroupAccess">Ange vilka personer som ska f� �tkomst till denna filsamling</xsl:variable>
	<xsl:variable name="i18n.AutomaticDelete">Automatisk gallring</xsl:variable>
	<xsl:variable name="i18n.SetUserKeepDays">Ange en egen gallringstid f�r filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UserKeepDays">Antal dagar att beh�lla filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.1">Filer som skickas in via denna tj�nst raderas automatiskt efter </xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.2"> dagar, om du inte sj�lv v�ljer att radera dessa tidigare. H�r ges du m�jlighet att �ndra antalet dagar innan den automatiska borttagningen.</xsl:variable>
	<xsl:variable name="i18n.MaximumNumberOfKeepDays">Maximalt antal dagar �r</xsl:variable>
	<xsl:variable name="i18n.AllowCollaborationItem">Till�t samarbete</xsl:variable>
</xsl:stylesheet>
