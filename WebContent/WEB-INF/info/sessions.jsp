<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id='stateDatabaseUser' class='programranje.yatospace.server.basic.bean.SessionBean' scope='session'></jsp:useBean>
<jsp:useBean id='databaseBean' class='programranje.yatospace.server.basic.bean.DatabaseBean' scope='session'></jsp:useBean>

<br>

ПРЕГЛЕД СЕСИЈА

<br><br>
<form name='sessions_info'>
	<div><font size='2px'>Административна сесија : </font></div>
	<input type='text' name='username' value='${pageContext.session.id}' readonly/><br>
	<c:if test='${stateDatabaseUser.isLoggedUserProfile(pageContext.session)}'>
		<div><font size='2px'>Корисничка сесија : </font></div>
		<input type='text' name='database' value='${stateDatabaseUser.userProfileSessionId(pageContext.session)}' readonly/><br>
	</c:if>
	<c:if test="${stateDatabaseUser.isLoggedDatabaseProfile(pageContext.session) and databaseBean.synchronizeObjectFoo(pageContext.session).isDatabaseUser()}">
		<div><font size='2px'>Базе података : </font></div>
		<input type='text' name='username' value='${pageContext.session.id}' readonly/><br>
	</c:if>
	<br><br>
</form>