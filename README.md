# nexus-report-plugin

Download a statistics report on your repositories.

## Installation
Copy the ".kar" file in the "deploy" directory on the Nexus server and restart the server.

## Permission
The users need the **"nx-report-download"** permission to perform this action.
You can create a new role with it or add it to an existing one.
Of course, an administrator have this permission by default.

## Usage
If one of yours rôles have the permission "nx-report-download", you should see a **"Report"** button (1) on the left side menu.
When you click on it a list of your granted repositories will be displayed. You have the possibility to sort it (2) or filter it (3) with the field at the top-right.
When you click on a repository of the list, if will download the report associated to this repository.

## Example

<img src="https://zupimages.net/up/20/36/egx2.png" width="900"/>