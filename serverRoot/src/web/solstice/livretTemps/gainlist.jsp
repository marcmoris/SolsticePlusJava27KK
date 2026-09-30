<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrgainlist">
<html>
	<head>
		<title>Descriptif des codes de gain</title>
		<link rel="stylesheet" type="text/css" href="styles/siq.css">
	</head>
	<body>
		<mt:grid>
			<mt:customQuery orderBy="name">
				select value, rtrim(name) + ' (' + rtrim(value) + ')' as name, description
				from P_Gain
				where IsActive = 'Y'
				And Visible = 'Y' 
				<% if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("livradmingainlist")) { %>
						and Accessible = 'Y'					
				<%}%>
			</mt:customQuery>
			<mt:field id="value" name="Code" type="String"/>
			<mt:field id="name" name="Nom" type="String"/>
			<mt:field id="description" name="Description" type="String"/>
		</mt:grid>
	</body>
</html>
</mt:securityCheck>
