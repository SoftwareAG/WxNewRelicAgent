<!DOCTYPE HTML>
<html>
<head>
<title>newrelic agent interceptor</title>
    <link rel="stylesheet" type="text/css" href="/WmRoot/webMethods.css">
    <link rel="stylesheet" type="text/css" href="/WmRoot/top.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <link rel="stylesheet" type="text/css" href="delite.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">

<script>
  function expandshrink(content) {
          
    if (content.style.maxHeight){    
      content.style.maxHeight = null;
    } else {          
      content.style.maxHeight = content.scrollHeight + "px";
    }   
  }
</script>
</head>
<body>
  <header class="no-margin no-padding main-head">
    <a class="brand" href="/">
      <div style="display: inline-flex;">
        <img src="images/sag-primary-logo-dark-rgb.png" height="32px" style="margin-top: 10px"/>
      </div>
    </a>
    <div class="wrapper" style="margin-left:auto; margin-right:12px;">
      <h1 class="dlt-links" style="margin-right: 40px; margin-top: 10px">New Relic Agent</legend>
    </div>
  </header>
  <div style="margin: 20px; margin-top: 64px;">
    <div style="padding: 20px">
        %ifvar actionType -notempty%
          <div style="margin-bottom: 20px; padding: 10px; background-color: pink; min-height: 50px; border-left: 1px solid red">
            %ifvar actionType equals('restore')%
              %invoke wx.newrelic.agent.config:restore%
                %ifvar success equals('true')%
                Successfully restored configuration, %ifvar serverType equals('IS')%Please restart the server to disable the interceptor.%else%stop the server and restart%endif%
                <button type="submit" class="dlt-button dlt-button-primary" style="float: right; background-color: red; font-size: 14px" onClick="setAction('reboot')">%ifvar serverType equals('IS')%restart%else%shutdown%endif%</button>
                %else%
                Ouch, no backup file found!
                %endif%
              %endinvoke%
            %endif%
            %ifvar actionType equals('merge')%
              %invoke wx.newrelic.agent.config:merge%
                %ifvar success equals('true')%
                  %value configFileName% has been updated, %ifvar serverType equals('IS')%Please restart the server to enable the interceptor.%else%stop the server and restart%endif%
                  <button type="submit" class="dlt-button dlt-button-primary" style="float: right; background-color: red; font-size: 14px" onClick="setAction('reboot')">%ifvar serverType equals('IS')%restart%else%shutdown%endif%</button>
                %endif%
              %endinvoke%
            %endif%
            %ifvar actionType equals('reboot')%
              %invoke wx.newrelic.agent.config:reboot%
                xxxxx %value message%
              %endinvoke%
            %endif%
          </div>
        %endif%
        %invoke wx.newrelic.agent.config:isConnected%
          <div class="banner">Status: 
            %ifvar isConnected equals('true')%
              <b>Online</b> <i style="color: blue" class="fas fa-link"></i>
            %else%
              <b>offline</b> <i style="color: red" class="fas fa-exclamation-circle"></i>
            %endif%
            <a style="float:right; color: white" href="/invoke/wx.newrelic.agent.config:download">download <i class="fas fa-file-download"></i></a>
          </div>
          <div class="contentx">
            Started at:<b>%value lastConnectionStatus%</b>
            <textarea id="status"  class="dlt-text-input dlt-textarea" name="status" style="margin-top: 10px; margin-bottom: 10px; width: 100%; height: 150px" readonly>%value logFile%</textarea>
            <p style="margin-top: -10px; font-size: x-small">%value matchedFile%</p>
            <a href="." class="dlt-button dlt-button-primary" style="float: right; background-color: blue; color: white">refresh</a>
          </div>
        %endinvoke%
      </div>
      <div style="padding: 20px">
        <div class="banner">Configuration: 
          %invoke wx.newrelic.agent.config:isConfigured%
            %ifvar isConfigured equals('true')%
              <b>done</b> <i class="fas fa-check"></i>
            %else%
              <b>to do</b> <i style="color: red" class="fas fa-check"></i>
            %endif% 
          %endinvoke%
        </div>
        <div class="contentx">
          <div style="min-height: 50px">
              %invoke wx.newrelic.agent.config:get%
              <form id="form" action=".">
              <input type="hidden" name="serverType" value="%value serverType%">
              <input type="hidden" name="actionType">

              <b>%value configFileName% (%ifvar isConfigured equals('true')%current configuration%else%to be updated%endif%)</b>
              <textarea id="config"  class="dlt-text-input dlt-textarea" name="configFile" style="margin-top: 10px; margin-bottom: 10px; width: 100%; height: 400px">%value configFile%</textarea>
              %ifvar isConfigured equals('true')%
                <button type="submit" class="dlt-button dlt-button-primary" style="float: right;" onClick="setAction('restore')">restore</button>
               Clicking on the restore button will reset the configuration to a backup made before the last update.
              %else%
                <b style="pading-top: 20px">e2e additions to be appended to the above file</b>
                <textarea name="configFileAddition" style="margin-top: 10px; margin-bottom: 10px; width: 100%; height: 80px">%value configFileAddition%</textarea>
                <button type="submit" class="dlt-button dlt-button-primary" style="float: right; background-color: red;" onClick="setAction('merge')">merge</button>
                Clicking on the merge button will update the runtime configuration to include the lines for the newrelic interceptor startup. Please verify that there are no discrepancies <br>i.e. that the sequence numbers 601 & 602 are not already used and that you have not already added these lines to the file above.
              %endif%
              </form>
              %endinvoke%
          </div>
        </div>
      </div>
    </div>
    <div style="padding: 20px; margin-left: 150px; margin-right: 150px; background-color: #eee">
      Please remember to configure your New Relic tenant configuration via the file <br><br>
      <i>[SAG_HOME]/IntegrationServer/instances/default/packages/WxNewrelicAgent/resources/newrelic/newrelic.xml</i>
      <div style="width: 100%; margin-top: 20px">
        At a minimum you need to set the license_key to ensure that the agent can connect to your tenant
      </div>
      <div style="margin-top: 20px">
        This package is using the New Relic java agent version 8.0, Click here for <a href="https://docs.newrelic.com/docs/release-notes/agent-release-notes/java-release-notes/java-agent-800/">Release notes and download</a>
      </div>
      <div style="margin-top: 20px">
        Sign up for a New Relic free trial at <a href="https://newrelic.com/signup">https://www.newrelic.com/signup</a>
      </div>
  <script>
  
    var textarea1 = document.getElementById('status');
    textarea1.scrollTop = textarea1.scrollHeight;
  
    var textarea2 = document.getElementById('config');
    textarea2.scrollTop = textarea2.scrollHeight;

    function setAction(action) {
      
      var frm = document.getElementById('form');
      frm.actionType.value = action;
    }
</script>
</body>
</html>