# libs

Drop-in directory for jars that are not available from Maven Central. Every jar
placed here is added to the compile and runtime classpath of every project.

Open ePlatform is built on top of OpenHierarchy, which is not published to a
public Maven repository and lives in Subversion:

    svn://svn.openhierarchy.org/openhierarchy/framework
    svn://svn.openhierarchy.org/openhierarchy/modules
    svn://svn.openhierarchy.org/openhierarchy/interfaces

The Eclipse projects reference it (and a few of its modules) directly as
workspace projects:

| Eclipse project     | Used by                                                    |
|---------------------|------------------------------------------------------------|
| `OpenHierarchy`     | FlowEngine and most modules                                 |
| `Cron4JUtils`       | FlowEngine                                                  |
| `SiteProfile`       | FlowEngine, BaseMapQuery                                    |
| `WSModule`          | FlowEngineIntegrationCallback                               |
| `SAMLLoginProvider` | MinimalUserSAMLAdapter                                      |
| `MinimalUser`       | MinimalUserSAMLAdapter                                      |

Build those projects from an OpenHierarchy checkout and copy the resulting jars
here. Gradle cannot fetch them for you: its repositories understand Maven and
Ivy layouts, not Subversion, and its source dependencies only support Git. Run
`gradlew openHierarchyDependencyReport` to see which jars are currently present
and which projects need them.

Jars in this directory are ignored by git.
