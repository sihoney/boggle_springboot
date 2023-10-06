<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>

<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title>playlist</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/hot-playlist.css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/asset/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/asset/bootstrap/js/bootstrap.js"></script>
</head>
<!--header-->

<body>
    <div id="wrap">
        <!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		

        <!-- ------nav------ -->
        <div id="nav" class="clearfix">
		      <ul class="nav nav-tabs">
		          <li role="presentation"><a href="${pageContext.request.contextPath}/mybook">내 서평</a></li>
		          <li role="presentation" class="active"><a href="${pageContext.request.contextPath}/taste_main">취향저격</a></li>
		          <!--세션 아이디와 사이트아이디 같을때-->
		          <li role="presentation"><a href="${pageContext.request.contextPath}/analyze">통계</a></li>
		      </ul>
		      <!-- 세션아이디랑 다를때는
		      <ul class="nav nav-tabs">
		          <li role="presentation"><a href="">'유저이름'님의 서평</a></li>
		          <li role="presentation" class="active"><a href="${pageContext.request.contextPath}/taste_main">취향저격</a></li>
		      </ul>	       
		       -->
 		</div>
       <!-- ------nav------ -->


        <!-- ------nav2------ -->
		<ul id="nav2" class="nav nav-pills">
			<!-- 세션아이디와 비교, 다를경우 '이름님의 취향' -->
			<!-- <li role="presentation" class="active"><a href="">'유저이름'님의 취향</a></li> -->
			<li role="presentation"><a href="${pageContext.request.contextPath}/taste_main">my 취향</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/review">좋아요한 서평</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/main_book">관심가는 책</a></li>
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/like_playlist">플레이리스트</a></li>
		</ul>

        <div class="contents" class="clearfix">
            <div class="index">
                <p>오늘의 추천 !<br> 플레이리스트&#x1F601;</p>
            </div>

            <div class="columns">
                <div id="columns_first" class="clearfix">
                    <div class="text-name">
                        <p id="name">출근할 때 즐기는 <br>에너지 충전 플레이리스트</p>
                    </div>

                    <div>
                        <span class="glyphicon glyphicon-heart" id="desc" aria-hidden="true"></span>
                        <span id="desc">16.2k</span>
                        <span class="glyphicon glyphicon-user" id="desc" aria-hidden="true"></span>
                        <span id="desc">책책책책을 읽읍시다</span>
                    </div>

                </div>
            </div>

            <div class="columns">
                <div id="columns_sec" class="clearfix">
                    <div class="text-name">
                        <p id="name">그래 가보자고<br>의욕 뿜뿜 플레이리스트</p>
                    </div>

                    <div>
                        <span class="glyphicon glyphicon-heart" id="desc" aria-hidden="true"></span>
                        <span id="desc">16.2k</span>
                        <span class="glyphicon glyphicon-user" id="desc" aria-hidden="true"></span>
                        <span id="desc">책책책책을 읽읍시다</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="columns">
            <div id="columns_thrd" class="clearfix">
                <div class="text-name">
                    <p id="name">나만 이런 게 아니었어<br>공감 꾹꾹 플레이리스트</p>
                </div>

                <div>
                    <span class="glyphicon glyphicon-heart" id="desc" aria-hidden="true"></span>
                    <span id="desc">16.2k</span>
                    <span class="glyphicon glyphicon-user" id="desc" aria-hidden="true"></span>
                    <span id="desc">하루에한권이라도</span>
                </div>

            </div>
        </div>


        <!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

    </div>

</body>

</html>