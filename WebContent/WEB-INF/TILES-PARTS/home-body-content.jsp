<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id='stateDatabaseUser' class='programranje.yatospace.server.basic.bean.SessionBean' scope='session'></jsp:useBean>
<jsp:useBean id='databaseBean' class='programranje.yatospace.server.basic.bean.DatabaseBean' scope='session'></jsp:useBean>
<br>

<c:if test="${stateDatabaseUser.isLogged(pageContext.session)}">
	<c:if test="${databaseBean.isDBUser(pageContext.session)}">
		<c:if test="${databaseBean.isDBAdmin(pageContext.session)}">
			<c:if test="${databaseBean.isDBRoot(pageContext.session)}">
				<jsp:include page='/WEB-INF/content/db-root-user.jsp'></jsp:include>
			</c:if>
			<c:if test="${!databaseBean.isDBRoot(pageContext.session)}">
				<jsp:include page='/WEB-INF/content/db-admin-user.jsp'></jsp:include>
			</c:if>
		</c:if>
		<c:if test="${!databaseBean.isDBAdmin(pageContext.session)}">
			<jsp:include page='/WEB-INF/content/db-classic-user.jsp'></jsp:include>
		</c:if>
	</c:if>
	<c:if test="${!databaseBean.isDBUser(pageContext.session)}">
		<jsp:include page='/WEB-INF/content/standard-user.jsp'></jsp:include>
	</c:if>
</c:if>
<c:if test="${!stateDatabaseUser.isLogged(pageContext.session)}">
	КОРИСНИК НИЈЕ ПРИЈАВЉЕН.
</c:if>