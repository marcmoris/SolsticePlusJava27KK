<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", -1);


	HttpSession httpSession = request.getSession();
	httpSession.removeAttribute("userInfo");
	httpSession.invalidate();


	String strRedirection = "";
	if(request.getParameter("redirect") != null && !request.getParameter("redirect").equals(""))
		strRedirection = request.getParameter("redirect");

	response.sendRedirect(strRedirection);
%>
