<img src="https://zupimages.net/up/20/37/uwy6.png" width="80"/>

# nexus-report-plugin
[![Build Status](https://travis-ci.org/groupe-edf/nexus-report-plugin.svg?branch=master)](https://travis-ci.org/github/groupe-edf/nexus-report-plugin)
[![Coverage Status](https://coveralls.io/repos/github/groupe-edf/nexus-report-plugin/badge.svg?branch=master)](https://coveralls.io/github/groupe-edf/nexus-report-plugin?branch=master)

Download a statistics report of your repositories.

## Build
You don't need to build the code to install the plugin, you can find the .kar file in the last release.

If you want to build the plugin by yourself, you can check it out and run `mvn clean install -PbuildKar` in nexus-report-plugin.

## Installation
Copy the .kar file in `deploy/` folder of your Nexus server and restart the server.

## Permission
The users needs the **"nx-report-download"** permission to perform this action.
You can create a new role with it or add it to an existing one.
Of course, an administrator has this permission by default.

## Usage
If one of yours roles has the permission "nx-report-download", you should see a **"Report"** button (1) on the left side menu.
When you click on it a list of your granted repositories will be displayed. You have the possibility to sort it (2) or filter it (3) with the field at the top-right.
When you click on a repository of the list, it will download the report associated to this repository.

## Example

<img src="https://zupimages.net/up/20/36/egx2.png" width="900"/>