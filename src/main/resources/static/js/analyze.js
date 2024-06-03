let PERIOD_BTNS;
let NICKNAME;

window.addEventListener("DOMContentLoaded", async function(){

	let pathname = decodeURIComponent(window.location.pathname);
	NICKNAME = pathname.split("/")[1];

	PERIOD_BTNS = document.querySelectorAll(".btn-period");
	PERIOD_BTNS.forEach(periodBtn => {
		periodBtn.onclick = clickPeriodBtn
	})
	
	updateAnalyze("week");
})

async function updateAnalyze(_period) {
	let emotionBox = document.getElementById("tag_box");
	let genreBox = document.getElementById("category_box");
	let reviewCountBox = document.getElementById("review_box");
	let bestReviewBox = document.getElementById("best_box");	
	let analyzeBox = document.getElementById("analyze_box");
	
	let newAnalyze = await getNewAnalyzeByPeriod(NICKNAME, _period);
	
	console.log(newAnalyze)
	
	if(newAnalyze.totalCount == 0) {		
		emotionBox.style.display = "none";
		genreBox.style.display = "none";
		reviewCountBox.style.display = "none";
		bestReviewBox.style.display = "none";	
			
		
		let div = document.createElement("div");
		div.innerHTML = "작성된 서평이 없습니다.";
		div.id = "message_empty_review";
		analyzeBox.insertBefore(div, emotionBox);
		/*
		let message = document.createTextNode("작성된 서평이 없습니다.");
		message.id = "message_empty_review";
		analyzeBox.insertBefore(message, emotionBox);*/		
	} else {
		let message = document.getElementById("message_empty_review");
		if(message) {message.remove();}
		
		emotionBox.style.display = "";
		genreBox.style.display = "";
		reviewCountBox.style.display = "";
		bestReviewBox.style.display = "";
		
		//updateGenreAnalyze(newAnalyze);
		//updateGenreGraph(newAnalyze);
		updateEmotionAnalyze(newAnalyze.emotionList);
		updateTotalCount(newAnalyze.totalCount, newAnalyze.reviewCountList);
		updateBestReview(newAnalyze.topReview);			
	}	
}

async function clickPeriodBtn(){

	PERIOD_BTNS.forEach(btn => { 
		btn.classList.replace("btn_active", "btn_none");
	})
	this.classList.add("btn_active");	
	
	updateAnalyze(this.id);
}

async function getNewAnalyzeByPeriod(nickname, period){
	
	try {
		let response = await fetch(`/analyze?nickname=${nickname}&period=${period}`, {
			method: "GET",
			headers: {
				"Content-Type" : "application/json"
			}
		})
		
		let data = await response.json();
		
		return data;
	} catch(error) {
		console.log(error);
	}
}

function updateBestReview(review) {
	if(review != null) {

		let bookName = document.getElementById("book-name");
		let reviewContent = document.getElementById("review-content");
		let likeCount = document.getElementById("like-count");
		let reviewCreatedAt = document.getElementById("review-createdat");
		let emotionName = document.getElementById("tag_btn");
		
		bookName.innerHTML = review[0].bookName;
		reviewContent.innerHTML = review[0].content;
		likeCount.innerHTML = review[0].likeCount;
		reviewCreatedAt.innerHTML = review[0].createdAt;	
		emotionName.innerHTML = review[0].emotionName;	
	}
}

function updateTotalCount(totalCount, reviewCountList) {

	let reviewCount = document.getElementById("review-count");
	
	reviewCount.innerHTML = `${totalCount}회`;
	
	let xValues = [];
	let yValues = [];
	let barColors = ["red", "green","blue","orange","brown"];
	
	for(let i = 0; i < reviewCountList.length; i++) {
		xValues.push(reviewCountList[i].period);
		yValues.push(reviewCountList[i].reviewCount);
	}
/*	
    let chart = Chart.getChart("myChart-bar");
    chart.data.labels = reviewCountList.map(item => item.period);
    chart.data.datasets[0].data = reviewCountList.map(item => item.reviewCount);
    chart.update();*/

	let chartBar = new Chart("myChart-bar", {
	  type: "bar",
	  data: {
	    labels: xValues,
	    datasets: [{
	      backgroundColor: barColors,
	      data: yValues
	    }]
	  },
	  options: {
	    legend: {display: false},
	    title: {
	      display: false,
	      text: "World Wine Production 2018"
	    }
	  }
	});
}

function updateEmotionAnalyze(rankingList) {
	
	const classList = ["red", "blue-3", "blue-2", "blue-1", "gray-s"]
	const fontClassList = ["font_red", "font_blue_3", "font_blue_2", "font_blue_1", "content-font-color"];
	
	let progressEmotion = document.getElementById("progress-emotion");
	let bartxt = document.getElementById("bartxt-box");
	let firstEmotion = document.getElementById("emotion-first");
	let firstEmotionPercentage = document.getElementById("emotion-first-percentage");
	
	progressEmotion.innerHTML = "";
	bartxt.innerHTML = "";
	
	firstEmotion.innerHTML = rankingList[0].emotionName;
	firstEmotionPercentage.innerHTML = rankingList[0].percentage;

	let ul = document.createElement("ul");
	
	for(let i = 0; i < rankingList.length; i++) {		
	
		let div = document.createElement("div");
		let span = document.createElement("span");
		let span2 = document.createElement("span");
		let li = document.createElement("li");
				
		div.classList.add("progress-bar", classList[i]);
		div.style.width = rankingList[i].percentage;
		
		span.classList.add("sr-only");
		span.innerHTML = `${rankingList[i].percentage}% Complete`;
		
		li.classList.add(fontClassList[i]);
		span2.innerHTML = rankingList[i].emotionName;
		
		li.appendChild(span2);
		ul.appendChild(li);
		
		div.appendChild(span);
		progressEmotion.appendChild(div);
	}	
	
	bartxt.appendChild(ul);
}
/*
function updateGenreAnalyze(rankingList) {
	
	const classList = ["blue-3", "blue-2", "blue-1", "gray-m"];
	
	let rankingElement = document.getElementById("ranking-genre");

	rankingElement.innerHTML = '';

	for(let i = 0; i < rankingList.length; i++) {		
		
		let div = document.createElement("div");
		let span = document.createElement("span");
				
		div.classList.add("txt-m", "margin-b-15");
		span.classList.add("label", "label-primary", "r-20");
		span.classList.add(classList[i]);
		
		span.innerHTML = `${i + 1}위`;
		let genreName = document.createTextNode(` ${rankingList[i].genreName} `);	
			
		div.appendChild(span);
		div.appendChild(genreName);
		
		// 1위인 경우
		if(i === 0) {			
			const span2 = document.createElement("span");
		
			div.classList.add("txt-b")
			span2.classList.add("font_blue_2")
		
			span2.innerHTML = rankingList[i].percentage;
			
			div.appendChild(span2);
		}	
		
		rankingElement.appendChild(div);		
	}	
}

function updateGenreGraph(rankingList) {
	//let graphElement = document.getElementById("chart-genre");
	
	let xValues = [];
	let yValues = []; 
	
	for(let i = 0; i < rankingList.length; i++) {
		xValues.push(rankingList[i].genreName);
		yValues.push(rankingList[i].percentage);	
	}
	
	console.log(xValues)
	console.log(yValues)
	
	let barColors = [
	  "#b91d47",
	  "#00aba9",
	  "#2b5797",
	  "#e8c3b9",
	  "#1e7145"
	];
	
	new Chart("chart-genre", {
	  type: "pie",
	  data: {
	    labels: xValues,
	    datasets: [{
	      backgroundColor: barColors,
	      data: yValues
	    }]
	  },
	  options: {
	    title: {
	      display: false,
	      text: "World Wide Wine Production 2018"
	    }
	  }
	});
}
*/