<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ------nav------ -->
<div id="nav" class="clearfix">
	<ul class="nav nav-tabs">
	<c:choose>
		<c:when test="${param.path eq 'mybook'}">
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/mybook">"${nickname }"님의 서평</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/taste">취향저격</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>			
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/analyze">통계</a></li>
		</c:when>
		<c:when test="${param.path eq 'taste'}">
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/mybook">"${nickname }"님의 서평</a></li>
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/taste">취향저격</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/analyze">통계</a></li>
		</c:when>
		<c:when test="${param.path eq 'playlist'}">
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/mybook">"${nickname }"님의 서평</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/taste">취향저격</a></li>
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/analyze">통계</a></li>
		</c:when>
		<c:when test="${param.path eq 'analyze'}">
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/mybook">"${nickname }"님의 서평</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/taste">취향저격</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/analyze">통계</a></li>
		</c:when>			
	</c:choose>
	</ul>
</div>
<!-- ------nav------ -->