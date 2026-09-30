<html>
	<head>
		<meta name="vs_targetSchema" content="http://schemas.microsoft.com/intellisense/ie5">
		<link rel="stylesheet" type="text/css" href="styles/siq.css">
		<title>SIQ - Erreur</title>
		<style>
			body {
				margin: 0px;
				padding: 0px;
			}
			
			li {
				color: red;
			}
		</style>
	</head>
	<body>
		<table width="900" align="center" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<img src="images/SQIipr.jpg" width="211" height="100" border="0">
				</td>
				<td align="center" valign="middle">
					<img src="images/SIQBAN.GIF" width="450" height="57" border="0">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" valign="middle">
					<div class="apptitle"><font color="red">Erreur</font></div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="line"></td>
			</tr>
			<tr>
				<td colspan="2">
					<ul>
<%

	String[] errors = request.getParameterValues("erreurs");
	for(int i = 0; i < errors.length; i++)
	{%>
	
						<li><%= errors[i] %></li>
		
<%	}

%>
					</ul>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div style="padding-top: 20px" class="siqname">Soci&eacute;t&eacute; immobili&egrave;re du Qu&eacute;bec</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="line"> </td>
			</tr>
		</table>
	</body>
</html>
