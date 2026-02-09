# CQF Tooling

## Overview
CQF Tooling is a Java-based multi-module Maven project providing tooling for CDS (Clinical Decision Support) use cases, including Implementation Guide and Measure authoring for FHIR (Fast Healthcare Interoperability Resources).

## Project Architecture
- **Language**: Java 11 (compiled with `maven.compiler.release=11`)
- **Build System**: Maven (multi-module reactor build)
- **Runtime**: GraalVM CE 22.3.1 (OpenJDK 19)

### Modules
- `tooling/` - Core library with all tooling operations
- `tooling-cli/` - Command-line interface (main entry point: `org.opencds.cqf.tooling.cli.Main`)
- `tooling-ui/` - JavaFX desktop UI (`org.opencds.cqf.tooling.ui.HierarchyUI`)

### Key Dependencies
- HAPI FHIR v8.0.0
- CQL Engine v3.27.0
- Spring Boot v2.1.5 (for packaging)
- JavaFX v11.0.2 (UI module)

## Build & Run
```bash
# Build all modules (skip tests)
mvn package -DskipTests -Dcheckstyle.skip=true

# Run CLI with an operation
java -jar tooling-cli/target/tooling-cli-3.11.0-SNAPSHOT.jar -<OperationName> [args...]
```

## CLI Operations
The CLI supports many operations including:
- `-QdmToQiCore` - QDM to QiCore mapping generation
- `-CqlToSTU3Library` / `-CqlToR4Library` - CQL to FHIR Library conversion
- `-BundleResources` - Bundle resources into a single FHIR Bundle
- `-RefreshStu3Measure` / `-RefreshR4Measure` - Refresh FHIR Measures
- `-ProcessAcceleratorKit` - WHO accelerator kit processing
- And many more (see Main.java for full documentation)

## Recent Changes
- 2026-02-09: Initial Replit setup - configured Java/Maven build environment

## User Preferences
- None recorded yet
