<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CELAR Orchestrator calls</title>
<style>
table
{
border-collapse: collapse;
border: 1px solid black;
}
th
{
width:400px;
border:1px solid black;
}
td
{
border-left:1px solid black;
}
</style>
</head>
<body>
<h2>Orchestrator Calls</h2>
<p>This page summarizes the provided calls for the CELAR orchestrator.  The page will be refreshed gradually.</p>
<table>
<tr>
<th style="width:50px;">No.</th>
<th>Call Name</th>
<th>Development Status *</th>
<th>URI</th>
</tr>
<tr>
<td>1</td>
<td><a href="iaas/resources/?info">Get Provided Resources</a></td>
<td>DUMMY</td>
<td>/orchestrator/iaas/resources/</td>
</tr>
<tr>
<td>2</td>
<td><a href="iaas/quotas/?info">Get User's quotas</a></td>
<td>DUMMY</td>
<td>/orchestrator/iaas/quotas/</td>
</tr>
<tr>
<td>3</td>
<td><a href="iaas/actions/?info">Get IaaS's provided actions</a></td>
<td>DUMMY</td>
<td>/orchestrator/iaas/actions/</td>
</tr>
<tr>
<td>4</td>
<td><a href="iaas/probes/?info">Get probes provided from IaaS</a></td>
<td>DUMMY</td>
<td>/orchestrator/iaas/probes/</td>
</tr>
<tr>
<td>5</td>
<td><a href="metrics/insert/?info">Insert monitoring metrics</a></td>
<td>DUMMY</td>
<td>/orchestrator/metrics/insert/</td>
</tr>
<tr>
<td>6</td>
<td><a href="metrics/get/?info">Get Monitoring metrics</a></td>
<td>DUMMY</td>
<td>/orchestrator/metrics/get/</td>
</tr>
<tr>
<td>7</td>
<td><a href="probes/upload/?info">Upload custom monitoring probes</a></td>
<td>DUMMY</td>
<td>/orchestrator/probes/upload/</td>
</tr>
<tr>
<td>8</td>
<td><a href="deployment/deploy/?info">Deploy an application</a></td>
<td>FUNCTIONAL</td>
<td>/orchestrator/deployment/deploy/</td>
</tr>
<tr>
<td>9</td>
<td><a href="deployment/smartdeploy/?info">(Smart) deploy an application</a></td>
<td>DUMMY</td>
<td>/orchestrator/deployment/smartdeploy/</td>
</tr>
<tr>
<td>10</td>
<td><a href="deployment/shutdown/?info">Shut down an application</a></td>
<td>DUMMY</td>
<td>/orchestrator/deployment/shutdown/</td>
</tr>
<tr>
<td>11</td>
<td><a href="deployment/resize/?info">Perform a resizing action</a></td>
<td>FUNCTIONAL</td>
<td>/orchestrator/deployment/resize/</td>
</tr>
<tr>
<td>12</td>
<td><a href="deployment/resizestatus/?info">Status of a resizing action</a></td>
<td>FUNCTIONAL</td>
<td>/orchestrator/deployment/resizestatus/</td>
</tr>
<tr>
<td>13</td>
<td><a href="deployment/getconf/?info">Get configuration</a></td>
<td>DUMMY</td>
<td>/orchestrator/deployment/getconf/</td>
</tr>
<tr>
<td>14</td>
<td><a href="action/gethistory/?info">Get action history</a></td>
<td>DUMMY</td>
<td>/orchestrator/action/gethistory/</td>
</tr>
</table>
<br/>
<span>For more details please select the name of the call you are interested in.</span><br/>
<span>*: One of: NOT IMPLEMENTED, DUMMY, PENDING, READY</span>
</body>
</html>