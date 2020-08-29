<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id='stateDatabaseUser' class='programranje.yatospace.server.basic.bean.SessionBean' scope='session'></jsp:useBean>

<% request.setCharacterEncoding("UTF-8"); %>
<% response.setCharacterEncoding("UTF-8"); %>

<c:if test='${not stateDatabaseUser.isLogged(pageContext.session)}'>
	<c:out value='${stateDatabaseUser.getInfo().setUserName(param["username"])}'></c:out>
	<c:out value='${stateDatabaseUser.setPassword(param["password"])}'></c:out>
	<c:out value='${stateDatabaseUser.getInfo().setDataBaseSvcName(param["database"])}'></c:out>
	<c:out value='${stateDatabaseUser.getInfo().setDataBase(param["address"])}'></c:out>
	<c:out value='${stateDatabaseUser.login(pageContext.session)}'></c:out>
</c:if>

<c:redirect url = "../index.jsp"/>