// 상수 정의
const CONSTANTS = {
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

// 애플리케이션 초기화
$(document).ready(() => {
  	new BookSearchManager();
});


