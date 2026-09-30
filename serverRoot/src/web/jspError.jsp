<%@ page isErrorPage="true" import="java.util.*, solstice.custom.IUserInfo" %>
<%

   /**
    * Cette page définit le portail d'accueil pour toutes les applications web de la SIQ. Pour
    * pouvoir y accèder, l'utilisateur doit d'abord s'être connecté. S'il ne l'est pas, on le
    * renvoit à la page de login.
    */
    
    // On récupère les informations sur la connexion
    IUserInfo userInfo = (IUserInfo)session.getAttribute("userInfo");
    String html;
    if(userInfo != null)
    {
       // L'utilisateur s'est connecté, on crée alors la page selon les applications auxquelles
       // il a droit. Ces doits sont définis dans les politiques de sécurités du userInfo
       %>


<html>
<head>
	<title>Erreurs</title>
	<STYLE type="text/css">
/* Titre de l'application */
.apptitle { FONT-WEIGHT: bolder; FONT-SIZE: 24px; COLOR: black; FONT-FAMILY: arial; TEXT-DECORATION: none }

/* Titre de page */
.pagetitle {FONT-WEIGHT: bolder; FONT-SIZE: 16px; COLOR:#253049; FONT-FAMILY: arial; TEXT-DECORATION: none }

/* Titre de section */
.sectitle {FONT-WEIGHT: bolder; font-style: normal;FONT-SIZE: 11px; COLOR:#4D4D26; FONT-FAMILY: tahoma; TEXT-DECORATION: none }

/* Le text de l'organisation' */
.siqname {FONT-WEIGHT: bolder; FONT-SIZE: 12px;  FONT-FAMILY: arial; TEXT-DECORATION: none}

/* Ligne décoratrice en début et fin de document 6D6D36 */
.line{background-color:#4D4D26; height: 4px}
	</STYLE>

</head>
<body >
		<table width="900" align="center" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<img src="/images/LogoBAN.jpg" width="211" height="100" border="0">
				</td>
				<td align="center" valign="middle">
					<img src="/images/SIQBAN.GIF" width="450" height="57" border="0">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" valign="middle">
					<div class="apptitle">Page d'erreur</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="line"></td>
			</tr>
			<tr>
				<td colspan="2"><br><br><div class="apptitle">Une erreur est survenue.</div>
				</td>
			</tr>
			<tr>
				<td colspan="2"><br>Si l'erreur persiste, veuillez contacter l'administrateur du système de paie.
				</td>
			</tr>
			<tr>
				<td colspan="2"><br><br><a href="javascript:history.back()">Cliquez ici pour retourner en arrière.</a>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div style="padding-top: 20px" class="siqname">%%Organisation%%</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="line"> </td>
			</tr>
		</table>
	</body>
</html>
       
       <%
    }
    else
    {
       // L'utilisateur n'est pas connecté. On le redirige vers le login
       response.sendRedirect("/solstice/login.jsp");
    }

%>