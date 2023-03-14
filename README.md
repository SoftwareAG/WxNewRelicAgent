# WxNewRelicAgent

webMethods package for integrating webMethods Integration Server with [New Relic](https://newrelic.com)

This package configures the New Relic java agent to ensure that webMethods services are reported as transactions.
Includes tracing of custom context id's to help pinpoint individual business transactions.
Also supports distributed tracing across http/https connections, and JMS and native (UM) messaging.

## Setup

Import the package into your Integration Server packages directory
e.g.

```
$ cd /${SAG_HOME}/IntegrationServer/packages
```
or 
```
$ cd /${SAG_HOME}/IntegrationServer/instances/${INSTANCE}/packages
```

If your packages directory is already under version control

```
$ git submodule add https://github.com/SoftwareAG/WxNewRelicAgent.git WxNewRelicAgent
```

or if you are not, then simply clone the repository

```
$ git clone https://github.com/SoftwareAG/WxNewRelicAgent.git
```

Then restart your runtime server. Open the admin portal and click on Packages -> Management. Then click on the home button of this package.
The screen shows if the connection is already established and the last few lines of the agent log file.
Below that it shows the current configuration of your webMethods runtime and also indicates if the server is not yet configured.

In your case the server will show as not yet configured so click on the merge button to add the New Relic java agent setup to the webMethods configuration file, but first check for any discrepancies and that any sequence numbers are not already in use. Once you have clicked on the merge button the page will ask you to restart the server or in the case of a webMethods edge (Microservices Runtime) to stop and then restart the server.

Don't restart your server yet as you still need to configure the connection to your New Relic tenant. Refer to the section below.

*NOTE:* A copy of the configuration file is made before the update, you can find it in the same directory as the config file with the same name and the extension .bak 

After restarting the page should show an "Online" status once the server has restarted and after you have configured your New Relic settings as described below.

The configuration section will now instead propose a "restore" button that will allow you to remove the New Relic java agent if required.

## Configure the connection with your New Relic tenant license.

You can edit the agent config file directly via 
```
[SAG_HOME]/IntegrationServer/instances/default/packages/WxNewRelicAgent/resources/newrelic/newrelic.yml
```

You can sign up for a free New Relic account here

[New Relic signup page](https://www.newrelic/signup/)
______________________
These tools are provided as-is and without warranty or support. They do not constitute part of the Software AG product suite. Users are free to use, fork and modify them, subject to the license agreement. While Software AG welcomes contributions, we cannot guarantee to include every contribution in the master project.
