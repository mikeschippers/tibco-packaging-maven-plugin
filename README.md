# TIBCO Packaging Maven Plugin

## Intro
The TIBCO Packaging Maven Plugin enable you to automate the build of your TIBCO BusinessWorks 5 Artifacts.

## Goals

| Phase | Goal | Description |
|----------|----------|-------------|
| initialize | init | Generates the Alias Library, DesigntimeLibrary and updates the .DesignerPrefs |
| prepare-package| validate | Validates the project |
| package | ear | Builds the Enterprise Archive Resource (ear) |
| package | lib | Builds the Project Library (projlib) |

## Settings
Add the following profile to your Maven settings.xml

    <profiles> 
        <profile>
            <id>tibco</id>
            <properties>
                <designerHome>/PATH/TO/TIBCO/designer/5.XX</designerHome>		
                <traHome>/PATH/TO/TIBCO/tra/5.XX</traHome>		
            </properties>
        </profile>  		
    </profiles>
    <activeProfiles>
        <activeProfile>tibco</activeProfile>
    </activeProfiles>

## Configuration Properties 
| Property | Description |
|----------|-------------|
| projectDirectory | Relative path to the project |
| validate| Indicates whether the project has to be validated before the build (default is true) |
| archive | Relative path to the Enterprise Archive |
| lib | Relative path to the Library Builder |

**Example**

    <configuration>
        <projectDirectory>${project.basedir}/src/bw/sample-project</projectDirectory>
        <archive>sample-project.archive</archive>
        <validate>true</validate>
        <lib>LibraryBuilder.libBuilder</lib>
    </configuration>


## Contributing
Want to help improving this Maven plugin? Feel free to create a pull request or to fork this repository. If you find any problems please fill out an issue.