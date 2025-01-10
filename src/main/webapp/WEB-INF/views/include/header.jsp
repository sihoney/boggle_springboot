<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<!-- ------헤더------ -->
<div id="header">
	<a href="${pageContext.request.contextPath}/boggle"> 
		<img src="${pageContext.request.contextPath}/resources/static/images/logo/logo2.png">
	</a>

	<sec:authentication var="auth" property="principal" />
		
	<sec:authorize access="isAuthenticated()">
	    <!-- 로그인한 사용자에게만 보이는 콘텐츠 -->			    
		<div id="header-dropdown" class="dropdown ">
			<button id="dLabel" class="header-dLabe" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<img id="header-img-icon" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${auth.profileUrl}" class="img-circle" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'"> 
				<span id="user-nickname">${auth.nickname }</span> 
				<span class="caret"></span>
			</button>

			<div id="header-dropdown-menu" class="dropdown-menu dropdown-menu-right txt-center" role="menu" aria-labelledby="dLabel">
				<div>
					<img id="header-menu-img" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${auth.profileUrl }" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'">
					<div class="margin-tb-10">
						<div>${auth.nickname}</div>
						<div>${authUser.userename}</div>
					</div>
					<div>
						<a id="btn-logout" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
					</div>
				</div>
				
				<form action="/review_write" method="get">
					<div class="write-postion">
						<button onclick = "location.href = '${pageContext.request.contextPath}/${auth.nickname}/mybook'" id="header-btn-write" type="button">내 서재</button>
						<button onclick = "location.href = '${pageContext.request.contextPath}/write'" id="header-btn-write" type="button">기록하기</button>
					</div>
				</form>
			</div>
		</div>
	</sec:authorize>
	
	<sec:authorize access="!isAuthenticated()">
	    <!-- 로그아웃한 사용자에게만 보이는 콘텐츠 -->
		<ul class="list-unstyled">
			<li><a class="btn btn-link" role="button" href="${pageContext.request.contextPath}/joinForm">회원가입</a></li>
			<li><a class="btn btn-link" role="button" href="${pageContext.request.contextPath}/loginForm">로그인</a></li>
		</ul>
	</sec:authorize>

<%--	<c:choose>
		<c:when test="${sessionScope.authUser eq null}">
			<!-- 로그인전 -->
			<ul class="list-unstyled">
				<li><a class="btn btn-link" role="button" href="${pageContext.request.contextPath}/joinForm">회원가입</a></li>
				<li><a class="btn btn-link" role="button" href="${pageContext.request.contextPath}/loginForm">로그인</a></li>
			</ul>
		</c:when>

 		<c:otherwise>
			<!-- 로그인후 -->
			<div id="header-dropdown" class="dropdown ">
				<button id="dLabel" class="header-dLabe" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<img id="header-img-icon" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${authUser.userProfile }" class="img-circle" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'"> 
					<span id="user-nickname">${authUser.nickname }</span> 
					<span class="caret"></span>
				</button>

				<div id="header-dropdown-menu" class="dropdown-menu dropdown-menu-right txt-center" role="menu" aria-labelledby="dLabel">
					<div>
						<img id="header-menu-img" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${authUser.userProfile }" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'">
						<div class="margin-tb-10">
							<div>${authUser.userName}</div>
							<div>${authUser.email}</div>
						</div>
						<div>
							<a id="btn-logout" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
						</div>
					</div>
					
					<form action="/review_write" method="get">
						<div class="write-postion">
							<button onclick = "location.href = '${pageContext.request.contextPath}/${authUser.nickname}/mybook'" id="header-btn-write" type="button">내 서재</button>
							<button onclick = "location.href = '${pageContext.request.contextPath}/write'" id="header-btn-write" type="button">기록하기</button>
						</div>
					</form>
				</div>
			</div>
		</c:otherwise>
	</c:choose> --%>
	

</div>
<!-- ------헤더------ -->