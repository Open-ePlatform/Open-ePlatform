För Open ePlatform är öppenhet, återanvändbarhet och standarder viktiga ingredienser. Nedan finns en övergripande teknisk beskrivning av plattformen, informationen kommer att utökas och bli mer detaljerad när plattformen publiceras.

Java och OpenHierarchy
Open ePlatform är utvecklad i Java, på ett ramverk som heter OpenHierarchy. 

Open ePlatform körs med Apache Tomcat som applikationsserver, detta innebär att plattformen kan köras på en mängd olika operativsystem.

Open ePlatform innehåller en Service Provider med SAML 2.0 stöd så att e-id inloggning kan ske mot vilken e-id leverantör som helst så länge de stöder SAML 2.0 standarden. 


Beroenden
Källkoden till Open ePlatform har ett antal beroenden som krävs för att koden ska kunna kompileras, dessa redovisas nedan.

Java
Källkoden är till stora delar skriven i programspråket Java och för att kompilera majoriteten av koden den krävs Java SE 1.6 eller nyare version. Några enstaka projekt delar av utanför kärnan dock kräver Java SE 1.7 eller nyare version.

OpenHierarchy
Open ePlatform baserat på OpenHierarchy plattformen och den krävs både för att kompilera koden och köra applikationen.

Koden i utvecklingsrepositoriet byggs generellt sett alltid mot senaste trunk revision av OpenHierarchy och dess beroenden.

Koden i releaserepositoriet byggs mot den revision av OpenHierarchy som uppgetts i samband med releasen.

OpenHierarchy moduler
Utöver de moduler som följer med i standardpaketering av OpenHierarchy plattformen så krävs följande moduler från OpenHierarchy repositoriet för att kompilera koden och köra applikationen.

MinimalUser (svn://svn.openhierarchy.org/openhierarchy/modules/minimaluser)
SAMLLoginProvider (svn://svn.openhierarchy.org/openhierarchy/modules/SAMLLoginProvider)
SiteProfile (svn://svn.openhierarchy.org/openhierarchy/modules/siteprofile)
WSModule (svn://svn.openhierarchy.org/openhierarchy/modules/WSModule)
Koden i utvecklingsrepositoriet byggs generellt sett alltid mot senaste trunk revision av dessa moduler.

Koden i releaserepositoriet byggs mot den revision av OpenHierarchy revision repositoriet som uppgetts i samband med releasen.

Bibliotek och utility projekt
Källkoden för Open ePlatform är beroende av ett antal bibliotek och utility projekt. Dessa hittas i följande källkodsrepository:

svn://svn.unlogic.se/utils
Koden i utvecklingsrepositoriet byggs generellt sett alltid mot senaste trunk revision av detta repository.

Koden i releaserepositoriet byggs mot den revision av detta revision repository som uppgetts i samband med releasen.
