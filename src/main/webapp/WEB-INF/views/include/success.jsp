<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>success</title>
	
    <script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
	
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
    <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/source.css"> --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
</head>
<body>
	<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
	
	<h1>성공적으로 처리되었습니다.</h1>
	
    <!-- footer -->
	<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
</body>
</html>