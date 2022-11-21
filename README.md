# PyBuilder
Plugin for PyCharm, which can help you build your project with using differenet 
components from remote repositories and publish your current project (compenent) with using of version control

## Files format
PyBuilder works with two files: **link.properties** and **version.properties**
### link.properties
In this file you need to write link for your git-component, which you want to use in current project:
```
<component_name>=<component_link>
component1=https:\\github.com\\your_repo
...
```

### version.properties
This file contains information about versions and branches for components and current project.
Full string structure:
```
<component_name>=<version>:<name_of_branch_or_directory>
component1=1.2.3:release
```
In this example **component1** will be loaded from branch **release/1.2.3**
Supported feature to use
#### Loading component from master
```
<component_name>
component1
```
#### Loading component from branch
```
<component_name>=<name_of_branch>
component1=your_branch_name
```

## Tasks
This plugin has 3 groups of tasks:
* [Build](#build)
* [Publish](#publish)
* [Utils](#utils)

### Build

#### build
This task will build your project with remote-components which are specified in properties files. 
All of this components will be installed into your **selected python interpreter**. 
If component has properties files with list of dependencies - this task also load them.

To find loaded dependecies open *python_interpreter_path/Libs/site-packages*.

#### clean
This task remove all loaded dependencies wich are specified in properties files.
### Publish
#### publish
This task push your current commits and changes to remote repository of this project.
***Current project will be published to branch or directory wich are specified in version.properties*** 
[version.properties](#versionproperties)

#### publishToLocal
This task copy current project to your local interpreter site-packages path.

#### publishToRelease
This task push your current commits and changes to release-breaches. 
**It can create new release-branches on remote repository.**
### Utils
#### resetInterpreter
This task helps you set python interpreter wich you want to use in developing
