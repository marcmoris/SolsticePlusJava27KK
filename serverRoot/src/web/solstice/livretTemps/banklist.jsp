<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrbanklist">
<html>
	<head>
		<title>Autres soldes</title>
		<link rel="stylesheet" type="text/css" href="styles/siq.css">
	</head>
	<body>
		<mt:grid>
			<mt:customQuery orderBy="Credits.Name">
				select Employee.Name as Employee_Name,Credits.value, Credits.Name as Credits_Name, SUM( PMOVEMENTVARIATION ) Solde, UOM.Value as Unit
				from P_Employee Employee inner join (P_Credits_Movement Movement  inner join (P_Credits Credits inner join P_UOM UOM
				on Credits.P_UOM_ID = UOM.P_UOM_ID)
				on Movement.P_Credits_ID = Credits.P_Credits_ID)
				on Employee.P_Employee_ID = Movement.P_Employee_ID
				where Movement.PMovementDate <= (select StartDate from P_Period where P_Period_ID = <%= request.getParameter("periodId") %>)
				and Employee.P_Employee_ID = <%= request.getParameter("employeeId") %>
				group by Employee.Name, Credits.Value, Credits.Name, UOM.Value
			</mt:customQuery>
			
			<mt:field id="Employee_Name" name="Employ&eacute;" type="String"/>
			<mt:field id="value" name="Code" type="String"/>
			<mt:field id="Credits_Name" name="Banque" type="String"/>
			<mt:fieldGroup name="Solde">
				<mt:field id="Solde" type="Number" format="#0.00"/>
				<mt:field id="Unit" type="String"/>
			</mt:fieldGroup>
		</mt:grid>
	</body>
</html>
</mt:securityCheck>
