<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:variable name="java.addFileCollectionBreadCrumbText">Lägg till filsamling</xsl:variable>
	<xsl:variable name="java.updateFileCollectionBreadCrumbText">Uppdatera filsamling: </xsl:variable>
	<xsl:variable name="java.unnamedFileCollectionText">Namnlös filsamling</xsl:variable>
	
	<xsl:variable name="i18n.validation.requiredField" select="'Du måste fylla i fältet'"/>
	<xsl:variable name="i18n.validation.invalidFormat" select="'Felaktigt format på fältet'"/>
	<xsl:variable name="i18n.validation.tooShort" select="'För kort innehåll i fältet'"/>
	<xsl:variable name="i18n.validation.tooLong" select="'För långt innehåll i fältet'"/>	
	<xsl:variable name="i18n.validation.unknownError" select="'Okänt fel på fältet'"/>
	<xsl:variable name="i18n.unknownFault" select="'Ett okänt fel har uppstått'"/>
		
	<xsl:variable name="i18n.fileCollections">Filsamlingar</xsl:variable>
	
	<xsl:variable name="i18n.name">Namn</xsl:variable>
	<xsl:variable name="i18n.posted">Skapad</xsl:variable>
	<xsl:variable name="i18n.poster">Skapad av</xsl:variable>
	<xsl:variable name="i18n.fileCount">Filer</xsl:variable>
	<xsl:variable name="i18n.noFileCollectionsFound">Inga filsamlingar hittades</xsl:variable>
	<xsl:variable name="i18n.addFileCollection">Lägg till filsamling</xsl:variable>
	<xsl:variable name="i18n.editFileCollection">Uppdatera filsamling</xsl:variable>
	<xsl:variable name="i18n.deleteFileCollection">Vill du ta bort filsamlingen</xsl:variable>
	<xsl:variable name="i18n.AddFileCollection.header">Lägg till filsamling</xsl:variable>
	<xsl:variable name="i18n.addFileCollection.button">Lägg till</xsl:variable>
	<xsl:variable name="i18n.UpdateFileCollection.header">Uppdatera filsamling</xsl:variable>
	<xsl:variable name="i18n.updateFileCollection.button">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.notRequiredField">(ej obligatoriskt)</xsl:variable>
	<xsl:variable name="i18n.files">Filer</xsl:variable>
	<xsl:variable name="i18n.deleteFileText">Ta bort fil</xsl:variable>
	<xsl:variable name="i18n.unNamedFileCollection">Namnlös filsamling</xsl:variable>
	<xsl:variable name="i18n.deletetionDate">Borttagningsdatum</xsl:variable>
	<xsl:variable name="i18n.requestedFileCollectionNotFound">Den begärda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.updateFailedFileCollectionNotFound">Den begärda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.deleteFailedFileCollectionNotFound">Den begärda filsamlingen hittades inte</xsl:variable>
	<xsl:variable name="i18n.noFilesInCollection">Den här filsamlingen innehåller inga filer.</xsl:variable>
	<xsl:variable name="i18n.fileCollectionURL">Kopiera denna länk och skicka den till den person som du vill dela dessa filer till</xsl:variable>
	<xsl:variable name="i18n.uploadLimit1">Du kan ladda upp en eller flera filer, maximalt</xsl:variable>
	<xsl:variable name="i18n.uploadLimit2">MB åt gången.</xsl:variable>
	<xsl:variable name="i18n.fileSizeLimitExceeded">Maximal uppladdningsstorlek överskriden</xsl:variable>
	<xsl:variable name="i18n.downloadAllFilesAsZip">Ladda ner alla filer</xsl:variable>
	
	<xsl:variable name="i18n.accessConfigDescription">Ange vilka personer eller grupper som ska få åtkomst till denna filsamling.</xsl:variable>
	<xsl:variable name="i18n.Groups">Grupper</xsl:variable>
	<xsl:variable name="i18n.Users">Användare</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionText">Ange ett namn på din filsamling, detta namn kommer att visas som rubrik för den eller de filer du delar till mottagaren. Ladda därefter upp de filer som du vill skicka till mottagaren genom att klicka på "Välj filer" knappen.</xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part1">Filerna nedan kommer att tas bort om </xsl:variable>
	<xsl:variable name="i18n.FilesWillBeRemoved.part2"> dagar.</xsl:variable>
	<xsl:variable name="i18n.Persons">Personer</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifier">Personnummer</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierFormatted">Personnummer (YYYYMMDDXXXX)</xsl:variable>
	<xsl:variable name="i18n.NoPersonsYet">Inga personer upplagda ännu</xsl:variable>
	<xsl:variable name="i18n.DuplicateID">Personnummer redan tillagt</xsl:variable>
	<xsl:variable name="i18n.InvalidID">Ogiltigt personnummer</xsl:variable>
	<xsl:variable name="i18n.UnknownError">Ett okänt fel inträffade</xsl:variable>
	<xsl:variable name="i18n.AddPerson">Lägg till person</xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.1">Samtliga filsamlingar och filer som skickas via denna tjänst raderas automatiskt efter </xsl:variable>
	<xsl:variable name="i18n.ListFileCollectionsInfo.part4.2"> dagar, om du inte själv väljer att radera dessa tidigare.</xsl:variable>
	<xsl:variable name="i18n.SecureCollection">Kräv säker inloggning med e-legitimation (t.ex. Mobilt BankID) för att läsa denna filsamling</xsl:variable>
	<xsl:variable name="i18n.SecureFileCollectionURL">Kopiera denna länk och skicka den till den person som du vill dela dessa filer till, kom ihåg att påminna mottagare om att välja inloggning med E-legitimation</xsl:variable>
	<xsl:variable name="i18n.Continue">Fortsätt</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfo">Säker filsamling</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionLoggedIn">Du är redan inloggad med en osäker inloggning. För att visa filsamlingen, logga ut och öppna länken igen för att logga in med e-legitimation.</xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part1">Den här informationen är skickad säkert och kräver att du loggar in med e-legitimation innan du kan ta del av den. Ansvaret för att informationen hanteras säkert även efter nedladdningen ligger på dig som användare. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.1">Filerna kommer automatiskt att tas bort efter </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part2.2"> dagar. </xsl:variable>
	<xsl:variable name="i18n.SecureCollectionInfoText.part3">Klicka på Fortsätt för att gå vidare och logga in med e-legitimation.</xsl:variable>
	<xsl:variable name="i18n.CitizenIdentifierHelp">Ange personnummer på de personer som ska få läsa denna filsamling</xsl:variable>
	<xsl:variable name="i18n.NameCollection">Namnge filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UploadFiles">Välj fil eller filer</xsl:variable>
	<xsl:variable name="i18n.AccessHeader">Välj säkerhet för åtkomst till filerna</xsl:variable>
	<xsl:variable name="i18n.CopyLink">Kopiera länk</xsl:variable>
	<xsl:variable name="i18n.LinkCopied">Länken har kopierats</xsl:variable>
	<xsl:variable name="i18n.PasswordCopied">Lösenordet har kopierats till klippbordet. Kom ihåg att spara det!</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionAndGetLink">Lägg till filsamlingen och skapa länken till filerna</xsl:variable>
	<xsl:variable name="i18n.AddFileCollectionFinishText">Om du har lagt till alla filer som du vill skicka samt valt vilka som skall kunna komma åt filerna, så kan du klicka på "Lägg till" här nedan. Efter att du klickat på knappen får du en länk till filsamlingen.</xsl:variable>
	<xsl:variable name="i18n.UsersAndGroups">Användare och grupper</xsl:variable>
	<xsl:variable name="i18n.PublicCollection">Publik filsamling, tillgänglig för alla som har tillgång till länken</xsl:variable>
	<xsl:variable name="i18n.InvalidSecurityMode">Ogiltigt val av säkerhet för filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollection">Kryptera filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptFileCollectionText">Kryptera filsamlingen för extra säkerhet. Endast personer med lösenordet kommer att kunna hämta filerna.</xsl:variable>
	<xsl:variable name="i18n.EncryptionPassword">Lösenord för kryptering</xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part1"> (Längden på lösenordet skall vara </xsl:variable>
	<xsl:variable name="i18n.EncryptionMinLenght.Part2"> tecken)</xsl:variable>
	<xsl:variable name="i18n.Generate">Generera</xsl:variable>
	<xsl:variable name="i18n.Copy">Kopiera</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollection">Krypterad filsamling</xsl:variable>
	<xsl:variable name="i18n.EncryptedFileCollectionText">Filerna i denna filsamling är krypterade. För att kunna visa och hämta dem så behöver du ange lösenordet nedan.</xsl:variable>
	<xsl:variable name="i18n.Password">Lösenord</xsl:variable>
	<xsl:variable name="i18n.ShowFiles">Visa filer</xsl:variable>
	<xsl:variable name="i18n.InvalidPassword">Du har angett ett ogiltigt lösenord</xsl:variable>
	<xsl:variable name="i18n.Encrypted">Krypterad</xsl:variable>
	<xsl:variable name="i18n.AllowsCollaboration">Tillåt samarbete, personer med tillgång till filsamlingen kommer att kunna ladda upp filer.</xsl:variable>
	<xsl:variable name="i18n.Uploading">Laddar upp...</xsl:variable>
	<xsl:variable name="i18n.CollaborationCollectionText">Du har möjlighet att bidra med filer till denna filsamling.</xsl:variable>
	<xsl:variable name="i18n.UploadFile">Ladda upp</xsl:variable>
	<xsl:variable name="i18n.SelectedFileTooBig">Den valda filen är för stor</xsl:variable>
	<xsl:variable name="i18n.SelectFile">Välj fil att ladda upp</xsl:variable>
	<xsl:variable name="i18n.ImportedUsers">Behöriga användare</xsl:variable>
	<xsl:variable name="i18n.accessConfigDescription.hiddenGroupAccess">Ange vilka personer som ska få åtkomst till denna filsamling</xsl:variable>
	<xsl:variable name="i18n.AutomaticDelete">Automatisk gallring</xsl:variable>
	<xsl:variable name="i18n.SetUserKeepDays">Ange en egen gallringstid för filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UserKeepDays">Antal dagar att behålla filsamlingen</xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.1">Filer som skickas in via denna tjänst raderas automatiskt efter </xsl:variable>
	<xsl:variable name="i18n.UserKeepDaysInfo.part.2"> dagar, om du inte själv väljer att radera dessa tidigare. Här ges du möjlighet att ändra antalet dagar innan den automatiska borttagningen.</xsl:variable>
	<xsl:variable name="i18n.MaximumNumberOfKeepDays">Maximalt antal dagar är</xsl:variable>
	<xsl:variable name="i18n.AllowCollaborationItem">Tillåt samarbete</xsl:variable>
</xsl:stylesheet>
