<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>view page</title>
    
    <!-- font(수정 전) -->
    <link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-pen.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-gothic-eco.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-myeongjo.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/NanumGaRamYeonGgoc.css" rel="stylesheet">	
	
	<style>
		* {
			padding: 0;
			margin: 0;
			box-sizing: border-box;
		}
		
		body {
			margin: 0;
		}
		
		video {
			opacity: 0.8;
			width: 100vw;
			height: 100vh;
			object-fit: cover;
			position: absolute;
			top: 0;
			left: 0;
			z-index: -1;
		}
		
		.container {
			height: 100vh;
		    width: 100vw;
		    display: flex;
		    flex-direction: column;
		    justify-content: center;
		    align-items: center;
		}
		
		.container_after {
			  width: 100%;
			  height: 100%;
			  position: absolute;
			  top: 0;
			  left: 0;
			  z-index: -1;
			  opacity: 0.5;
		}
		
		.reviewContent {
			width: 70%;
		    text-align: center;
		    font-size: 25px;
		    line-height: 3rem;
		    color: #fff;
		}
		
		.nickname {
			padding: 1.5rem 0;
			color: #fff;
		}
	</style>
</head>
<body>
	<div class="container">
		<p class="reviewContent" style="font-family: ${review.fontEntity.fontName}">
			${review.content }
		</p>
		<p class="nickname">${review.nickname }</p>
	</div>
	<video src="${pageContext.request.contextPath }/resources/static/images/review_card/${fn:split(review.wallpaperEntity.wallpaperName, '.')[0]}.mp4" autoplay loop muted></video>
</body>
</html>