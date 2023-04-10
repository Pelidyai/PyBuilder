# PyBuilder

Plugin for PyCharm, which can help you build your project with using different
components from remote repositories and publish your current project (component) with using of version control

_Nexus_ it is a storage git-repository in the context of this plugin

## Files format

Reserved names:

* **nexus_link** - name of link for your nexus-repository
  PyBuilder works with two files:
  **link.properties** and **version.properties**

### link.properties

In this file you need to write link
for your git-component or nexus-repository
with name "nexus_link", which you want to use in current project:

```
<component_name>=<component_link>
nexus_link=https:\\github.com\\your_nexus_repo
component1=https:\\github.com\\your_repo
...
```

### version.properties

This file contains information
about versions and branches for
components and current project.
Full string structure:

```
<component_name>=<version>
component1=1.2.3
component2=3.2.1feature
```

In this example **component1**
will be loaded from branch
**component1/1.2.3** at nexus

### Supported files feature to use

#### Loading component from master

```
<component_name>
component1
```

If the component doesn't have its own link you will load
nexus master

## Tasks

This plugin has 3 groups of tasks:

* [Build](#build)
* [Publish](#publish)
* [Utils](#utils)

### Build

#### build

This task will build your project with remote-components which
are specified in properties files.
All of this components will be installed into your
**selected python interpreter**.
If component has properties files with list of dependencies -
this task also load them.

To find loaded dependencies open
*python_interpreter_path/Libs/site-packages*.

#### clean

This task remove all loaded dependencies which are specified
in properties files.

### Publish

#### publishToLocal

This task copy current project to your local interpreter
site-packages path. **This task also copy your local changes.**

#### publish

This task push only your current **commits** to remote
repository of this project or specified nexus.
_You cannot publish existing versions of component_

**It can create new release-branches on remote repository.**
***Current project will be published to branch or directory
which are specified in version.properties***
[version.properties](#versionproperties)

#### republish

This task push only your current **commits** to remote repository.
_You can rewrite existing versions of component_

**It can create new release-branches on remote repository.**

### Utils

#### resetInterpreter

This task helps you set python interpreter which you want to use
in developing
