# BOM Generator
Generates `BOM.csv` and `THIRD_PARTY_NOTE.md` files for Open Source publication
of projects, following the standard provided by CSI Piemonte.

### Prerequisites for execution
The following prerequisites are necessary for the execution:
- for Node.js projects: the `package.json` and the `package-lock.json` files
must be present in the original project
- for Maven projects: the `pom.xml` must be readable; furthermore, a Maven
installation is required (TODO)

### Configuration
The configuration of the project must be set in the file
`/src/main/resources/config.properties` by cloning the template file
`/src/main/resources/config.template.properties` and correctly populating the
properties. In particular:
- `projectPath`: the path to the source project in the file system; correctly
tested as an absolute file
- `projectName`: the name of the project, to separate the output of multiple
projects

### Instructions for execution
(Note: as of now, it's only just working from an IDE. WIP for an executable JAR)
First, install the dependencies via `npm install`.

Run the Main class to execute the code.

### Output
The project produces its output in the `/output` folder, or more specificately:
- `/output/csv/BOM-${projectName}.csv` is the BOM file created
- `/output/markdown/THIRD_PARTY_NOTE-${projectName}.md` is the Third Party
Note file created

### TODOs
- [ ] provide more robust logic for handling packages
- [ ] generate executable JAR
- [ ] read properties from CLI
- [ ] Ivy integration
- [ ] Handle other languages (Python, PHP, ...)
