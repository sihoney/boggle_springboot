const CONSTANTS = {
  MAX_REVIEW_LENGTH: 200,
  MODES: {
    EDIT: "수정하기",
    CREATE: "기록하기"
  },
  PAGINATION: {
    PREV: '«',
    NEXT: '»'
  },
  SELECTORS: {
    SEARCH_FORM: '#searchbook-form',
    SEARCH_QUERY: '#searchbook-query',
    MODAL_PLAYLIST: '#modal-playlist',
    PAGINATION: '.pagination',
    SELECTED_BOOK: '.selected-book'
  }
};

// 책 검색 관리 클래스
class BookSearchManager {
  constructor() {
    this.page = 1;
    this.query = '';
    this.reviewMap = {};
    this.initializeEventHandlers();
  }

  initializeEventHandlers() {
    // 검색 폼 제출 이벤트
    $(CONSTANTS.SELECTORS.SEARCH_FORM).on('submit', (e) => this.handleSearch(e));
    
    // 페이지네이션 클릭 이벤트
    $(CONSTANTS.SELECTORS.PAGINATION).on('click', '.page-item', (e) => this.handlePagination(e));
    
    // 책 선택 이벤트
    $(CONSTANTS.SELECTORS.MODAL_PLAYLIST).on('click', '.list', (e) => this.handleBookSelection(e));
  }

  handleSearch(event) {
    event.preventDefault();
    this.query = $(CONSTANTS.SELECTORS.SEARCH_QUERY).val();
    this.page = 1;
    this.searchAndRenderBooks();
  }

  handlePagination(event) {
    event.preventDefault();
    const clickedPage = $(event.currentTarget).text().trim();
    
    this.page = this.calculateNewPage(clickedPage);
    this.searchAndRenderBooks();
  }

  calculateNewPage(clickedPage) {
    switch(clickedPage) {
      case CONSTANTS.PAGINATION.PREV:
        return Math.max(1, parseInt(this.page) - 1);
      case CONSTANTS.PAGINATION.NEXT:
        return parseInt(this.page) + 1;
      default:
        return parseInt(clickedPage);
    }
  }

  async searchAndRenderBooks() {
    try {
      const response = await this.searchBooks();
      
      if (response.success) {
        this.clearCurrentResults();
        this.renderSearchResults(response.data);
      } else {
        console.error("검색 실패:", response.errorCode);
        alert("책 검색에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("검색 오류:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  }

  async searchBooks() {
    try {
      const response = await fetch(`/searchbook?query=${this.query}&page=${this.page}`);
      return response.json();
    } catch (error) {
      console.error("API 호출 실패:", error);
      throw error;
    }
  }

  clearCurrentResults() {
    $(CONSTANTS.SELECTORS.MODAL_PLAYLIST).empty();
    $(CONSTANTS.SELECTORS.PAGINATION).empty();
  }

  renderSearchResults(data) {
    const {
      bookList,
      totalPages,
      startPage,
      endPage,
      currentPage
    } = data;

    this.renderBookList(bookList);
    this.renderPagination(totalPages, startPage, endPage, currentPage);
  }

  renderBookList(books) {
    const bookListHtml = books.map(book => this.createBookItemHtml(book)).join('');
    $(CONSTANTS.SELECTORS.MODAL_PLAYLIST).html(bookListHtml);
  }

  createBookItemHtml(book) {
    return `
      <li class="list" data-dismiss="modal"
          data-cover="${book.cover}"
          data-author="${book.author}"
          data-title="${book.title}"
          data-isbn="${book.isbn13}"
          data-url="${book.link}"
          data-category="${book.categoryName}"
          data-category-id="${book.categoryId}">
        <div class="book-img-container">
          <img src="${book.cover}" alt="${book.title}" class="img-thumbnail">
        </div>
        <div class="info-container">
          <button class="book-title">${book.title}</button>
          <div class="book-author">${book.author}</div>
        </div>
      </li>
    `;
  }

  renderPagination(totalPages, startPage, endPage, currentPage) {
    let paginationHtml = '';

    // 이전 페이지 버튼
    if (currentPage > 1) {
      paginationHtml += this.createPaginationButton(CONSTANTS.PAGINATION.PREV, false);
    }

    // 페이지 번호 버튼
    for (let i = startPage; i <= endPage; i++) {
      paginationHtml += this.createPaginationButton(i, i === currentPage);
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages) {
      paginationHtml += this.createPaginationButton(CONSTANTS.PAGINATION.NEXT, false);
    }

    $(CONSTANTS.SELECTORS.PAGINATION).html(paginationHtml);
  }

  createPaginationButton(text, isActive) {
    return `
      <li class="page-item${isActive ? ' active' : ''}">
        <a class="page-link" href="#">${text}</a>
      </li>
    `;
  }

  handleBookSelection(event) {
    const $selectedBook = $(event.currentTarget);
    this.reviewMap = $selectedBook.data();

    this.updateUIAfterBookSelection();
  }

  updateUIAfterBookSelection() {
    // 선택 섹션 숨기기
    $('.jumbotron').hide();
    
    // 선택된 책 정보 표시
    this.renderSelectedBook();
    
    // 진행 바 업데이트
    this.updateProgressBar();
    
    // 삭제 버튼 이벤트 설정
    this.setupDeleteButton();
  }

  renderSelectedBook() {
    const selectedBookHtml = `
      <div id="contents" class="clearfix selected-book" data-isbn="${this.reviewMap.isbn}">
        <div id="select_box">
          <div id="bookVo">
            <img id="book_img" src="${this.reviewMap.cover}" alt="${this.reviewMap.title}" class="img-thumbnail">
            <div id="book_detail">
              <h1>${this.reviewMap.title}</h1>
              <h3>저자 ${this.reviewMap.author}</h3>
              <div id="book_review"></div>
            </div>
            <button id="btn_delete" type="button" class="btn btn-light">삭제</button>
          </div>
        </div>
      </div>
    `;

    $('.selectbook-header').after(selectedBookHtml);
  }

  updateProgressBar() {
    $('.progressbar li:first-child').addClass('active');
  }

  setupDeleteButton() {
    $('#btn_delete').on('click', () => {
      $(CONSTANTS.SELECTORS.SELECTED_BOOK).remove();
      $('.jumbotron').show();
      $('.progressbar li').removeClass('active');
    });
  }
}

// 리뷰 관리 클래스
class ReviewManager {
  constructor(bookSearchManager) {
    this.reviewId = null;
    this.nickname = null;
    this.mode = null;
	this.bookSearchManager = bookSearchManager;
    this.init();
  }

  init() {
    this.nickname = $("#user-nickname").text();
    this.initializeEventHandlers();
    this.checkMode();
  }

  initializeEventHandlers() {
    // 감정 버튼 이벤트
    $("#btn_mood").children().each((_, btn) => {
      $(btn).on('click', () => this.handleEmotionClick(btn));
    });

    // 스타일 버튼 이벤트
    $(".btn_style").each((_, btn) => {
      $(btn).on('click', () => this.handleStyleClick(btn));
    });

    // 폰트 버튼 이벤트
    $(".btn_font").each((_, btn) => {
      $(btn).on('click', (e) => this.handleFontClick(e, btn));
    });

    // 텍스트영역 자동 높이 조절
    this.initializeTextAreaHandlers();

    // 저장/취소 버튼 이벤트
    this.initializeActionButtons();
  }

  checkMode() {
    const urlParams = new URLSearchParams(window.location.search);
    this.reviewId = urlParams.get("reviewId");
    
    if (this.reviewId) {
      this.mode = CONSTANTS.MODES.EDIT;
      this.bookSearchManager.reviewMap.reviewId = this.reviewId;
      this.initializeEditMode();
    } else {
      this.mode = CONSTANTS.MODES.CREATE;
      if (urlParams.has("isbn")) {
        this.setProgressBar(1);
      }
    }
  }

  initializeEditMode() {
    for (let i = 1; i <= 4; i++) {
      this.setProgressBar(i);
    }
  }

  handleEmotionClick(button) {
    $("#btn_mood").children().removeClass("active");
    $(button).addClass("active");
    
    if (this.bookSearchManager.reviewMap) {
      this.bookSearchManager.reviewMap.emotionId = button.id;
    }
    
    this.setProgressBar(2);
  }

  handleStyleClick(button) {
    const $button = $(button);
    const wallpaperId = $button.data("wallpaperid");
    const wallpaperUrl = $button.css("background-image");
    
    $("#review_box")
      .css("background-image", wallpaperUrl)
      .css("background-size", "cover");
    
    if (this.bookSearchManager.reviewMap) {
      this.bookSearchManager.reviewMap.wallpaperId = wallpaperId;
    }
    
    if (this.bookSearchManager.reviewMap.fontId) {
      this.setProgressBar(3);
    }
  }

  handleFontClick(event, button) {
    event.preventDefault();
    
    const $button = $(button);
    const fontId = $button.data("fontid");
    const fontName = $button.text();
    
    $("#review_textarea").css("font-family", fontName);
    
    if (this.bookSearchManager.reviewMap) {
      this.bookSearchManager.reviewMap.fontId = fontId;
    }
    
    if (this.bookSearchManager.reviewMap.wallpaperId) {
      this.setProgressBar(3);
    }
  }

  initializeTextAreaHandlers() {
    const $textarea = $("#review_textarea");
    
    $textarea.on("input", function() {
      this.style.height = 'auto';
      this.style.height = `${this.scrollHeight}px`;
    });

    $textarea.on("keyup", (e) => this.handleTextAreaKeyup(e));
  }

  handleTextAreaKeyup(event) {
    const $textarea = $(event.target);
    const length = $textarea.val().length;
    
    if (length > 0) {
      $("#limit-text").text(`${length}/${CONSTANTS.MAX_REVIEW_LENGTH}`);
      this.setProgressBar(4);
    } else if (length > CONSTANTS.MAX_REVIEW_LENGTH) {
      $textarea.val($textarea.val().substring(0, CONSTANTS.MAX_REVIEW_LENGTH));
    } else {
      this.setProgressBar(-4);
    }
  }

  setProgressBar(stageNum) {
    const $progressbar = $(".progressbar li");
    
    if (stageNum > 4) {
      $progressbar.addClass("active");
    } else if (stageNum > 0) {
      $progressbar.eq(stageNum - 1).addClass("active");
    } else if (stageNum < 0) {
      $progressbar.eq(Math.abs(stageNum) - 1).removeClass("active");
    } else {
      $progressbar.removeClass("active");
    }
  }

  async saveReview() {
    const reviewContent = $("#review_textarea").val();
    
    if (!reviewContent || !this.isReadyToPost()) {
      alert("입력하지 않은 부분이 있습니다");
      return;
    }

	this.bookSearchManager.reviewMap.reviewContent = reviewContent;
    
    try {
      const response = await this.postReview();
      this.handleSaveSuccess(response);

    } catch (error) {
      console.error("리뷰 저장 실패:", error);
      alert("저장에 실패했습니다. 다시 시도해주세요.");
    }
  }

  async postReview() {
    const url = "/reviews";
    const method = this.mode === CONSTANTS.MODES.EDIT ? "PUT" : "POST";
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.bookSearchManager.reviewMap)
    });

    if (!response.ok) {
	  const errorText = await response.text(); // Use text() instead of json() for error handling
      throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
    }
    
	const responseData = await response.json(); // Read the response body only once
    return responseData.data;
  }

  handleSaveSuccess(data) {
    this.bookSearchManager.reviewMap.reviewId = data;
    const modalText = this.mode === CONSTANTS.MODES.EDIT ? 
      "수정되었습니다. 플레이리스트에 추가하시겠습니까?" : 
      "저장되었습니다. 플레이리스트에 추가하시겠습니까?";
    
    $(".modal_question>p").text(modalText);
    $(".modal_question")
      .removeClass("unstaged")
      .addClass("opaque");
  }

  isReadyToPost() {
    return $(".progressbar li").toArray()
      .every(item => item.classList.contains("active"));
  }

  initializeActionButtons() {
    $("#btn_admit").on("click", () => this.saveReview());
    $("#btn_cancle").on("click", () => history.back());
  }
}

// 플레이리스트 관리 클래스
class PlaylistManager {
  constructor(reviewManager) {
    this.reviewManager = reviewManager;
    this.initializeEventHandlers();
  }

  initializeEventHandlers() {
    $(".mqBtn").on("click", (e) => this.handleModalResponse(e));
    $(".modal_myply_btn").on("click", () => this.navigateToMyReviews());
  }

  async handleModalResponse(event) {
    const $modal = $(".modal_question");
    $modal.removeClass("opaque");
    
    $modal.one("transitionend", function() {
      $modal.addClass("unstaged");
    });

    const answer = $(event.target).val();
    
    if (answer === "yes") {
      await this.showPlaylistModal();
    } else {
      this.navigateToMyReviews();
    }
  }

  async showPlaylistModal() {
    try {
      const playlists = await this.fetchPlaylists();
      this.renderPlaylists(playlists);
      
      $(".modal_ply_ul").on("click", ".list", (e) => this.handlePlaylistSelection(e));
      
      $(".modal_myply")
        .removeClass("unstaged")
        .addClass("opaque");
    } catch (error) {
      console.error("플레이리스트 로드 실패:", error);
      alert("플레이리스트를 불러오는데 실패했습니다.");
    }
  }

  async fetchPlaylists() {
    const response = await fetch('/getMyPlaylist');
    if (!response.ok) {
      throw new Error('플레이리스트 조회 실패');
    }
    return response.json();
  }

  renderPlaylists(playlists) {
    const playlistHtml = playlists.map(playlist => `
      <li class="list" data-playlist-id="${playlist.playlistId}">
        <div class="info-container">
          <div class="playlist-title">${playlist.playlistName}</div>
        </div>
      </li>
    `).join('');
    
    $(".modal_ply_ul").html(playlistHtml);
  }

  async handlePlaylistSelection(event) {
    const $target = $(event.currentTarget);
    $target.addClass("selected");
    
    const playlistId = $target.data("playlistid");
    
    try {
      await this.addReviewToPlaylist(playlistId);
      this.showSuccessMessage();
    } catch (error) {
      console.error("플레이리스트 추가 실패:", error);
      alert("플레이리스트에 추가하는데 실패했습니다.");
    }
  }

  async addReviewToPlaylist(playlistId) {
    const response = await fetch(
      `/playlists/${playlistId}/reviews/${this.reviewManager.reviewMap.reviewId}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    if (!response.ok) {
      throw new Error('플레이리스트 추가 실패');
    }
    
    return response.json();
  }

  showSuccessMessage() {
    const $msgModal = $(".msg_modal");
    $msgModal
      .removeClass("unstaged")
      .addClass("opaque");
    
    setTimeout(() => {
      $msgModal.removeClass("opaque");
      $msgModal.one("transitionend", () => {
        $msgModal.addClass("unstaged");
      });
    }, 2000);
  }

  navigateToMyReviews() {
    location.href = '/my/reviews/' + this.reviewManager.nickname;
  }
}

// 애플리케이션 초기화
$(document).ready(() => {
  const bookSearchManager = new BookSearchManager();
  const reviewManager = new ReviewManager(bookSearchManager);
  const playlistManager = new PlaylistManager(reviewManager);
});