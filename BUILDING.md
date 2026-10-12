# Building Open ePlatform

The build is a single Gradle multi-project build. Every Java project in the
repository (the framework, plus each interface, module and query) is a
subproject; the layout of the sources is untouched, so the Eclipse projects keep
working alongside it.

## Prerequisites

- A JDK 17 or later to run Gradle. The sources themselves are compiled for Java
  8 (`javaVersion` in `gradle.properties`; override with `-PjavaVersion=11`).
- The OpenHierarchy jars, see [libs/README.md](libs/README.md). They are not
  published to any public repository and are not part of this repository, so
  they have to be built from an OpenHierarchy Subversion checkout and dropped
  into `libs/`. Without them only projects that do not touch OpenHierarchy code
  will compile. All other third party dependencies are resolved from Maven
  Central.

## Commands

    gradlew build                          # compile and jar every project
    gradlew :FlowEngine:build              # a single project
    gradlew projects                       # list the projects
    gradlew openHierarchyDependencyReport  # which OpenHierarchy jars are needed, and which are present

The wrapper downloads Gradle itself on first use, so no local Gradle install is
needed. On Linux and macOS use `./gradlew`.

Tasks that only make sense on one machine - deploying the jars into a servlet
container, copying them somewhere - belong in a `local.gradle` in the root of
this repository, which `build.gradle` applies if it is there and git ignores.
Nothing in the shared build should depend on paths that only exist on the
machine it was written on.

## Layout

`build.gradle` in the root configures all subprojects: the source layout
inherited from Eclipse (sources and resources share one `src` directory), the
Java release, and the dependencies of each project. Subprojects have no build
files of their own — add new projects to `settings.gradle` and their
dependencies to `moduleDependencies` in `build.gradle`.

The repository carries no IDE project files. Gradle is the definition of the
build, and IDE metadata — Eclipse's `.classpath`/`.project`/`.settings`, IntelliJ's
`.idea` — is ignored, so import this as a Gradle project in whatever you use.
