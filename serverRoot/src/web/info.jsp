<html>
<head><title>View Session JSP </title></head>
<body>
<h2>Session Info From A JSP</h2>

<%
    out.println( "<BR>Your machine's address is " );
    out.println( request.getRemoteHost());
%>

<h2>Bonjour <%
out.println( request.getRemoteUser());
out.println( System.getProperty("user.name")); 
%>

</h2>
<br>
The session id: 

<c:out value="${pageContext.session.id}"/>

<h3>Session date values formatted as Dates</h3>

<jsp:useBean id="timeValues" class="java.util.Date"/>

<c:set target="${timeValues}" value="${pageContext.session.creationTime}" property="time"/>
The creation time: <fmt:formatDate value="${timeValues}" type="both" dateStyle="medium" />

<br><br>

<c:set target="${timeValues}" value="${pageContext.session.lastAccessedTime}" property="time"/>
The last accessed time:  

<fmt:formatDate value="${timeValues}" type="both" dateStyle="short" />

<c:out value="${timeValues}"/>



</body>
</html>