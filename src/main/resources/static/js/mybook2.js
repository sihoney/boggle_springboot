// Constants and State Management
const STATE = {
  userId: null,
  sort: 'createdAt,desc',
  page: 0,
  emotionName: null,
  isSameUser: false
};

// API Service
const ReviewAPI = {
  async getReviewsAndPaging(page, sort, emotionName) {
    try {
      const path = `/reviews?userId=${STATE.userId}&emotionName=${emotionName}&page=${page}&sort=${sort}`;
      const response = await fetch(path);
      return response.json();
    } catch (error) {
      console.error('Failed to fetch reviews:', error);
      window.location.href = '/user/loginForm';
      return null;
    }
  },

  async toggleLike(reviewId) {
    try {
      const response = await fetch(`/reviews/${reviewId}/likes`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ reviewId })
      });
      return response;
    } catch (error) {
      console.error('Failed to toggle like:', error);
      return null;
    }
  },

  async deleteReview(reviewId) {
    try {
      const response = await fetch(`/review/${reviewId}`, {
        method: 'DELETE'
      });
      return response;
    } catch (error) {
      console.error('Failed to delete review:', error);
      return null;
    }
  }
};

// UI Components
const ReviewUI = {
  renderReviewList(reviews) {
    const reviewsList = document.querySelector('#rvlist');
    reviewsList.innerHTML = '';

    reviews.forEach(review => {
      const reviewElement = this.createReviewElement(review);
      reviewsList.appendChild(reviewElement);
    });
  },

  createReviewElement(review) {
    const div = document.createElement('div');
    div.className = 'reviews';
    div.id = `r${review.reviewId}`;

    div.innerHTML = `
      <div class="reviews-header">
        <div class="left">
          <p><a href="/books/${review.bookId}">${review.bookEntity.title}</a></p>
        </div>
        ${STATE.isSameUser ? `
          <div class="right">
            <a href="/reviews/edit?reviewId=${review.reviewId}">수정</a>
            <a class="delete" data-reviewid="${review.reviewId}">삭제</a>
          </div>
        ` : ''}
      </div>
      <div class="reviews-content">
        <p>${review.content}</p>
        <span class="label label-default">${review.emotionEntity.emotionName}</span>
      </div>
      <div class="reviews-footer">
        <div class="left likecontrol">
          <span class="like glyphicon ${review.likeByAuthUser ? 'glyphicon-heart' : 'glyphicon-heart-empty'}"
                data-reviewid="${review.reviewId}"></span>
          <span class="likecnt" data-likecount="${review.likeCount}">${review.likeCount}</span>
          <span>${review.createdAt}</span>
        </div>
        ${this.createDropdownMenu(review)}
      </div>
    `;

    return div;
  },

  createDropdownMenu(review) {
    return `
      <div class="right">
        <div class="dropup">
          <a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            더보기 <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
            ${review.likeByAuthUser ? `
              <li role="presentation">
                <a class="add_pli" data-userno="${review.userId}" data-reviewno="${review.reviewId}">
                  플레이리스트에 추가<span id="plus">+</span>
                </a>
              </li>
              <li role="presentation" class="divider"></li>
            ` : ''}
            <li role="presentation">
              <a id="shr_review" role="menuitem">서평 공유하기
                <span class="glyphicon glyphicon-share"></span>
              </a>
            </li>
            <li role="presentation" class="divider"></li>
            <li role="presentation">
              <a role="menuitem" target="_blank" href="/reviews/${review.reviewId}">
                이미지 미리보기<span class="glyphicon glyphicon-save"></span>
              </a>
            </li>
          </ul>
        </div>
      </div>
    `;
  },

  renderPagination(startPage, endPage) {
    const paginationContainer = document.createElement('div');
    paginationContainer.id = 'mybook_paging';
    
    paginationContainer.innerHTML = `
      <ul>
        <li>◀</li>
        ${Array.from({ length: endPage - startPage + 1 }, (_, i) => 
          `<li>${startPage + i}</li>`
        ).join('')}
        <li>▶</li>
      </ul>
    `;

    const content = document.querySelector('#content');
    const oldPagination = document.querySelector('#mybook_paging');
    if (oldPagination) {
      oldPagination.remove();
    }
    content.appendChild(paginationContainer);
  }
};

// Event Handlers
class ReviewEventHandler {
  static async initialize() {
    STATE.userId = document.querySelector('#username').dataset.userid;
    STATE.isSameUser = document.querySelector('#profile-title').textContent === '내 서재';

    this.setupEventListeners();
    await this.loadInitialData();
  }


  static setupEventListeners() {
    // Sort handlers
    document.querySelector('#latest-order').addEventListener('click', () => this.handleSort('createdAt,desc'));
    document.querySelector('#oldest-order').addEventListener('click', () => this.handleSort('createdAt,asc'));
    document.querySelector('#best-order').addEventListener('click', () => this.handleSort('likeCount,desc'));
    // Emotion filter
    document.querySelector('#dropdown-menu-emotion').addEventListener('click', event => {
		const li = event.target.closest('li');
	    if (!li) return; // li가 아닌 다른 곳을 클릭한 경우 무시
	    
	    const emotion = event.target.textContent;

/*	    // 기존 선택된 항목의 스타일 제거
	    this.querySelectorAll('li').forEach(item => {
	        item.classList.remove('selected');
	    });
	    // 클릭된 항목 스타일 적용
	    li.classList.add('selected');*/
	    
	    // API 호출 또는 다른 처리
	    this.handleEmotionFilter(emotion);		
    });

    // Review list actions
    document.querySelector('#rvlist').addEventListener('click', event => {
      const target = event.target;
      if (target.classList.contains('like')) {
        this.handleLike(target);
      } else if (target.classList.contains('delete')) {
        this.handleDelete(target);
      }
    });

    // Pagination
    document.querySelector('#content').addEventListener('click', event => {
      if (event.target.closest('#mybook_paging')) {
        this.handlePagination(event);
      }
    });
  }


  static async loadInitialData() {
    await this.fetchAndRenderReviews(STATE.page, STATE.sort, STATE.emotionName);
  }


  static async handleSort(sortOrder) {
    STATE.sort = sortOrder;
    STATE.page = 0;
    STATE.emotionName = null;
    await this.fetchAndRenderReviews(STATE.page, STATE.sort, STATE.emotionName);
  }


  static async handleEmotionFilter(emotion) {
    STATE.emotionName = emotion;
    STATE.page = 0;
    await this.fetchAndRenderReviews(STATE.page, 'createdAt,desc', STATE.emotionName);
  }


  static async handleLike(target) {
    const reviewId = target.dataset.reviewid;
    const response = await ReviewAPI.toggleLike(reviewId);
    
    if (response) {
      const likeCountSpan = target.nextElementSibling;
      const currentCount = parseInt(likeCountSpan.dataset.likecount);
      
      if (target.classList.contains('glyphicon-heart')) {
        target.classList.replace('glyphicon-heart', 'glyphicon-heart-empty');
        likeCountSpan.textContent = currentCount - 1;
        likeCountSpan.dataset.likecount = currentCount - 1;
      } else {
        target.classList.replace('glyphicon-heart-empty', 'glyphicon-heart');
        likeCountSpan.textContent = currentCount + 1;
        likeCountSpan.dataset.likecount = currentCount + 1;
      }
    }
  }


  static async handleDelete(target) {
    const reviewId = target.dataset.reviewid;
    const response = await ReviewAPI.deleteReview(reviewId);
    
    if (response) {
      alert('서평이 삭제되었습니다! :-)');
      await this.fetchAndRenderReviews(1, 'createdAt,desc', null);
    }
  }


  static async handlePagination(event) {
    if (event.target.tagName === 'LI') {
      STATE.page = parseInt(event.target.textContent);
      await this.fetchAndRenderReviews(STATE.page, STATE.sort, STATE.emotionName);
    }
  }


  static async fetchAndRenderReviews(page, sort, emotionName) {
    const data = await ReviewAPI.getReviewsAndPaging(page, sort, emotionName);
    if (data) {
      ReviewUI.renderReviewList(data.reviewList);
      ReviewUI.renderPagination(data.startPage, data.endPage);
    }
  }
}

// Initialize the application
document.addEventListener('DOMContentLoaded', () => ReviewEventHandler.initialize());