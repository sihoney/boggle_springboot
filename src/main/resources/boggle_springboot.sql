/* 유저 */
CREATE TABLE "MY_SCHEMA"."TABLE" (
	"COL5" <지정 되지 않음> NOT NULL, /* 유저번호 */
	"COL" <지정 되지 않음>, /* 이름 */
	"COL2" <지정 되지 않음>, /* 아이디 */
	"COL3" <지정 되지 않음> /* 전화번호 */
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE" IS '유저';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE"."COL5" IS '유저번호';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE"."COL" IS '이름';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE"."COL2" IS '아이디';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE"."COL3" IS '전화번호';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_TABLE"
	ON "MY_SCHEMA"."TABLE" (
		"COL5" ASC
	);

ALTER TABLE "MY_SCHEMA"."TABLE"
	ADD
		CONSTRAINT "PK_TABLE"
		PRIMARY KEY (
			"COL5"
		);

/* 게시판 */
CREATE TABLE "MY_SCHEMA"."TABLE2" (
	"COL" <지정 되지 않음> NOT NULL, /* 게시글번호 */
	"COL6" <지정 되지 않음>, /* 회원번호 */
	"COL2" <지정 되지 않음>, /* 제목 */
	"COL3" <지정 되지 않음>, /* 내용 */
	"COL4" <지정 되지 않음>, /* 조회수 */
	"COL5" <지정 되지 않음> /* 작성일 */
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE2" IS '게시판';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL" IS '게시글번호';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL6" IS '회원번호';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL2" IS '제목';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL3" IS '내용';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL4" IS '조회수';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE2"."COL5" IS '작성일';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_TABLE2"
	ON "MY_SCHEMA"."TABLE2" (
		"COL" ASC
	);

ALTER TABLE "MY_SCHEMA"."TABLE2"
	ADD
		CONSTRAINT "PK_TABLE2"
		PRIMARY KEY (
			"COL"
		);

/* users */
CREATE TABLE "MY_SCHEMA"."users" (
	"user_no" NUMBER AUTO_INCREMENT PRIMARY KEY NOT NULL, /* user_no */
	"user_name" VARCHAR2(100), /* 이름 */
	"nickname" VARCHAR2(200), /* 닉네임 */
	"email" VARCHAR2(500), /* 이메일 */
	"password" VARCHAR2(100), /* 비밀번호 */
	"user_profile" VARCHAR2(500), /* 프로필 이미지 경로 */
	"join_date" DATE /* 가입날짜 */
);

COMMENT ON TABLE "MY_SCHEMA"."users" IS 'users';

COMMENT ON COLUMN "MY_SCHEMA"."users"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."users"."user_name" IS '이름';

COMMENT ON COLUMN "MY_SCHEMA"."users"."nickname" IS '닉네임';

COMMENT ON COLUMN "MY_SCHEMA"."users"."email" IS '이메일';

COMMENT ON COLUMN "MY_SCHEMA"."users"."password" IS '비밀번호';

COMMENT ON COLUMN "MY_SCHEMA"."users"."user_profile" IS '프로필 이미지 경로';

COMMENT ON COLUMN "MY_SCHEMA"."users"."join_date" IS '가입날짜';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_users"
	ON "MY_SCHEMA"."users" (
		"user_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."users"
	ADD
		CONSTRAINT "PK_users"
		PRIMARY KEY (
			"user_no"
		);

/* book_category */
CREATE TABLE "MY_SCHEMA"."TABLE4" (
	"COL" <지정 되지 않음> NOT NULL, /* 책 카테고리 no */
	"COL3" <지정 되지 않음> /* 책 카테고리 이름 */
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE4" IS 'book_category';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE4"."COL" IS '책 카테고리 no';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE4"."COL3" IS '책 카테고리 이름';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_TABLE4"
	ON "MY_SCHEMA"."TABLE4" (
		"COL" ASC
	);

ALTER TABLE "MY_SCHEMA"."TABLE4"
	ADD
		CONSTRAINT "PK_TABLE4"
		PRIMARY KEY (
			"COL"
		);

/* user_book_category */
CREATE TABLE "MY_SCHEMA"."TABLE5" (
	"user_no" NUMBER, /* user_no */
	"COL2" <지정 되지 않음>, /* 책 카테고리 no */
	"COL3" <지정 되지 않음> /* 책 카테고리 이름 */
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE5" IS 'user_book_category';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE5"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE5"."COL2" IS '책 카테고리 no';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE5"."COL3" IS '책 카테고리 이름';

/* book_info(API) */
CREATE TABLE "MY_SCHEMA"."books" (
	"book_no" NUMBER NOT NULL, /* ISBN */
	"book_title" VARCHAR2(500), /* 책 제목 */
	"author" VARCHAR2(200), /* 작가 */
	"book_url" VARCHAR2(500), /* 책 url */
	"genre_no" NUMBER, /* 장르 번호 */
	"cover_url" VARCHAR2(500) /* 책커버 */
);

COMMENT ON TABLE "MY_SCHEMA"."books" IS 'book_info(API)';

COMMENT ON COLUMN "MY_SCHEMA"."books"."book_no" IS 'ISBN';

COMMENT ON COLUMN "MY_SCHEMA"."books"."book_title" IS '책 제목';

COMMENT ON COLUMN "MY_SCHEMA"."books"."author" IS '작가';

COMMENT ON COLUMN "MY_SCHEMA"."books"."book_url" IS '책 url';

COMMENT ON COLUMN "MY_SCHEMA"."books"."genre_no" IS '장르 번호';

COMMENT ON COLUMN "MY_SCHEMA"."books"."cover_url" IS '책커버';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_books"
	ON "MY_SCHEMA"."books" (
		"book_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."books"
	ADD
		CONSTRAINT "PK_books"
		PRIMARY KEY (
			"book_no"
		);

/* 서평 */
CREATE TABLE "MY_SCHEMA"."review" (
	"review_no" NUMBER NOT NULL, /* 서평 no */
	"user_no" NUMBER, /* user_no */
	"book_no" NUMBER, /* ISBN */
	"style_no" NUMBER, /* 스타일 no */
	"review_content" VARCHAR2(1000), /* 내용 */
	"review_date" DATE /* 작성일 */
);

COMMENT ON TABLE "MY_SCHEMA"."review" IS '서평';

COMMENT ON COLUMN "MY_SCHEMA"."review"."review_no" IS '서평 no';

COMMENT ON COLUMN "MY_SCHEMA"."review"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."review"."book_no" IS 'ISBN';

COMMENT ON COLUMN "MY_SCHEMA"."review"."style_no" IS '스타일 no';

COMMENT ON COLUMN "MY_SCHEMA"."review"."review_content" IS '내용';

COMMENT ON COLUMN "MY_SCHEMA"."review"."review_date" IS '작성일';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_review"
	ON "MY_SCHEMA"."review" (
		"review_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."review"
	ADD
		CONSTRAINT "PK_review"
		PRIMARY KEY (
			"review_no"
		);

/* tag */
CREATE TABLE "MY_SCHEMA"."TABLE8" (
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE8" IS 'tag';

/* 감정tag */
CREATE TABLE "MY_SCHEMA"."emotion" (
	"emo_no" NUMBER NOT NULL, /* 감정 no */
	"emo_name" VARCHAR2(100) /* 감정 이름 */
);

COMMENT ON TABLE "MY_SCHEMA"."emotion" IS '감정tag';

COMMENT ON COLUMN "MY_SCHEMA"."emotion"."emo_no" IS '감정 no';

COMMENT ON COLUMN "MY_SCHEMA"."emotion"."emo_name" IS '감정 이름';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_emotion"
	ON "MY_SCHEMA"."emotion" (
		"emo_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."emotion"
	ADD
		CONSTRAINT "PK_emotion"
		PRIMARY KEY (
			"emo_no"
		);

/* 좋아요 리스트(유저) */
CREATE TABLE "MY_SCHEMA"."TABLE10" (
	"COL" <지정 되지 않음> /* 새 컬럼 */
);

COMMENT ON TABLE "MY_SCHEMA"."TABLE10" IS '좋아요 리스트(유저)';

COMMENT ON COLUMN "MY_SCHEMA"."TABLE10"."COL" IS '새 컬럼';

/* 좋아요한 사용자(조인) */
CREATE TABLE "MY_SCHEMA"."review_user" (
	"review_user_no" NUMBER NOT NULL, /* 번호 */
	"user_no" NUMBER, /* user_no */
	"review_no" NUMBER, /* 서평 no */
	"review_like_date" DATE /* 좋아요한 시간 */
);

COMMENT ON TABLE "MY_SCHEMA"."review_user" IS '좋아요한 사용자(조인)';

COMMENT ON COLUMN "MY_SCHEMA"."review_user"."review_user_no" IS '번호';

COMMENT ON COLUMN "MY_SCHEMA"."review_user"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."review_user"."review_no" IS '서평 no';

COMMENT ON COLUMN "MY_SCHEMA"."review_user"."review_like_date" IS '좋아요한 시간';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_review_user"
	ON "MY_SCHEMA"."review_user" (
		"review_user_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."review_user"
	ADD
		CONSTRAINT "PK_review_user"
		PRIMARY KEY (
			"review_user_no"
		);

/* 스타일(컬러) */
CREATE TABLE "MY_SCHEMA"."style" (
	"style_no" NUMBER NOT NULL, /* 스타일 no */
	"emo_no" NUMBER, /* 감정 no */
	"style_name" VARCHAR2(200) /* 스타일 이름 */
);

COMMENT ON TABLE "MY_SCHEMA"."style" IS '스타일(컬러)';

COMMENT ON COLUMN "MY_SCHEMA"."style"."style_no" IS '스타일 no';

COMMENT ON COLUMN "MY_SCHEMA"."style"."emo_no" IS '감정 no';

COMMENT ON COLUMN "MY_SCHEMA"."style"."style_name" IS '스타일 이름';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_style"
	ON "MY_SCHEMA"."style" (
		"style_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."style"
	ADD
		CONSTRAINT "PK_style"
		PRIMARY KEY (
			"style_no"
		);

/* 장르 */
CREATE TABLE "MY_SCHEMA"."genre" (
	"genre_no" NUMBER NOT NULL, /* 장르 번호 */
	"genre_name" VARCHAR2(100) /* 장르 이름 */
);

COMMENT ON TABLE "MY_SCHEMA"."genre" IS '장르';

COMMENT ON COLUMN "MY_SCHEMA"."genre"."genre_no" IS '장르 번호';

COMMENT ON COLUMN "MY_SCHEMA"."genre"."genre_name" IS '장르 이름';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_genre"
	ON "MY_SCHEMA"."genre" (
		"genre_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."genre"
	ADD
		CONSTRAINT "PK_genre"
		PRIMARY KEY (
			"genre_no"
		);

/* 플래이리스트 */
CREATE TABLE "MY_SCHEMA"."playlist" (
	"playlist_no" NUMBER NOT NULL, /* 플레이리스트 번호 */
	"user_no" NUMBER, /* user_no */
	"playlist_date" DATE, /* 등록시간 */
	"playlist_name" VARCHAR2(200) /* 플레이리스트 제목(사용자가지정함) */
);

COMMENT ON TABLE "MY_SCHEMA"."playlist" IS '플래이리스트';

COMMENT ON COLUMN "MY_SCHEMA"."playlist"."playlist_no" IS '플레이리스트 번호';

COMMENT ON COLUMN "MY_SCHEMA"."playlist"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."playlist"."playlist_date" IS '등록시간';

COMMENT ON COLUMN "MY_SCHEMA"."playlist"."playlist_name" IS '플레이리스트 제목(사용자가지정함)';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_playlist"
	ON "MY_SCHEMA"."playlist" (
		"playlist_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."playlist"
	ADD
		CONSTRAINT "PK_playlist"
		PRIMARY KEY (
			"playlist_no"
		);

/* songs */
CREATE TABLE "MY_SCHEMA"."music" (
	"music_no" NUMBER NOT NULL, /* 노래 no */
	"emo_no" NUMBER, /* 감정 no */
	"music_title" VARCHAR2(200), /* 노래 제목 */
	"artist" VARCHAR2(200), /* 가수 */
	"music_path" VARCHAR2(1000) /* 음악 파일(경로) */
);

COMMENT ON TABLE "MY_SCHEMA"."music" IS 'songs';

COMMENT ON COLUMN "MY_SCHEMA"."music"."music_no" IS '노래 no';

COMMENT ON COLUMN "MY_SCHEMA"."music"."emo_no" IS '감정 no';

COMMENT ON COLUMN "MY_SCHEMA"."music"."music_title" IS '노래 제목';

COMMENT ON COLUMN "MY_SCHEMA"."music"."artist" IS '가수';

COMMENT ON COLUMN "MY_SCHEMA"."music"."music_path" IS '음악 파일(경로)';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_music"
	ON "MY_SCHEMA"."music" (
		"music_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."music"
	ADD
		CONSTRAINT "PK_music"
		PRIMARY KEY (
			"music_no"
		);

/* 해당 책을 좋아요한 사용자(bookmark) */
CREATE TABLE "MY_SCHEMA"."book_user" (
	"book_user_no" NUMBER NOT NULL, /* 북마크번호 */
	"book_no" NUMBER, /* ISBN */
	"user_no" NUMBER, /* user_no */
	"bookmark_date" DATE /* 북마크한 시간 */
);

COMMENT ON TABLE "MY_SCHEMA"."book_user" IS '해당 책을 좋아요한 사용자(bookmark)';

COMMENT ON COLUMN "MY_SCHEMA"."book_user"."book_user_no" IS '북마크번호';

COMMENT ON COLUMN "MY_SCHEMA"."book_user"."book_no" IS 'ISBN';

COMMENT ON COLUMN "MY_SCHEMA"."book_user"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."book_user"."bookmark_date" IS '북마크한 시간';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_book_user"
	ON "MY_SCHEMA"."book_user" (
		"book_user_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."book_user"
	ADD
		CONSTRAINT "PK_book_user"
		PRIMARY KEY (
			"book_user_no"
		);

/* 회원이 좋아하는 장르 */
CREATE TABLE "MY_SCHEMA"."user_genre" (
	"user_genre_no" NUMBER NOT NULL, /* 번호 */
	"genre_no" NUMBER, /* 장르 번호 */
	"user_no" NUMBER /* user_no */
);

COMMENT ON TABLE "MY_SCHEMA"."user_genre" IS '회원이 좋아하는 장르';

COMMENT ON COLUMN "MY_SCHEMA"."user_genre"."user_genre_no" IS '번호';

COMMENT ON COLUMN "MY_SCHEMA"."user_genre"."genre_no" IS '장르 번호';

COMMENT ON COLUMN "MY_SCHEMA"."user_genre"."user_no" IS 'user_no';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_user_genre"
	ON "MY_SCHEMA"."user_genre" (
		"user_genre_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."user_genre"
	ADD
		CONSTRAINT "PK_user_genre"
		PRIMARY KEY (
			"user_genre_no"
		);

/* 플래이리스트서평 */
CREATE TABLE "MY_SCHEMA"."playlist_review" (
	"playlist_review_no" NUMBER NOT NULL, /* 번호 */
	"playlist_no" NUMBER, /* 플레이리스트 번호 */
	"review_no" NUMBER, /* 서평 no */
	"playlist_review_date" DATE /* 등록시간 */
);

COMMENT ON TABLE "MY_SCHEMA"."playlist_review" IS '플래이리스트서평';

COMMENT ON COLUMN "MY_SCHEMA"."playlist_review"."playlist_review_no" IS '번호';

COMMENT ON COLUMN "MY_SCHEMA"."playlist_review"."playlist_no" IS '플레이리스트 번호';

COMMENT ON COLUMN "MY_SCHEMA"."playlist_review"."review_no" IS '서평 no';

COMMENT ON COLUMN "MY_SCHEMA"."playlist_review"."playlist_review_date" IS '등록시간';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_playlist_review"
	ON "MY_SCHEMA"."playlist_review" (
		"playlist_review_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."playlist_review"
	ADD
		CONSTRAINT "PK_playlist_review"
		PRIMARY KEY (
			"playlist_review_no"
		);

/* 회원플레이리스트 */
CREATE TABLE "MY_SCHEMA"."user_playlist" (
	"user_playlist_no" NUMBER NOT NULL, /* 번호 */
	"user_no" NUMBER, /* user_no */
	"playlist_no" NUMBER /* 플레이리스트 번호 */
);

COMMENT ON TABLE "MY_SCHEMA"."user_playlist" IS '회원플레이리스트';

COMMENT ON COLUMN "MY_SCHEMA"."user_playlist"."user_playlist_no" IS '번호';

COMMENT ON COLUMN "MY_SCHEMA"."user_playlist"."user_no" IS 'user_no';

COMMENT ON COLUMN "MY_SCHEMA"."user_playlist"."playlist_no" IS '플레이리스트 번호';

CREATE UNIQUE INDEX "MY_SCHEMA"."PK_user_playlist"
	ON "MY_SCHEMA"."user_playlist" (
		"user_playlist_no" ASC
	);

ALTER TABLE "MY_SCHEMA"."user_playlist"
	ADD
		CONSTRAINT "PK_user_playlist"
		PRIMARY KEY (
			"user_playlist_no"
		);

ALTER TABLE "MY_SCHEMA"."TABLE2"
	ADD
		CONSTRAINT "FK_TABLE_TO_TABLE2"
		FOREIGN KEY (
			"COL6"
		)
		REFERENCES "MY_SCHEMA"."TABLE" (
			"COL5"
		);

ALTER TABLE "MY_SCHEMA"."TABLE5"
	ADD
		CONSTRAINT "FK_users_TO_TABLE5"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."TABLE5"
	ADD
		CONSTRAINT "FK_TABLE4_TO_TABLE5"
		FOREIGN KEY (
			"COL2"
		)
		REFERENCES "MY_SCHEMA"."TABLE4" (
			"COL"
		);

ALTER TABLE "MY_SCHEMA"."TABLE5"
	ADD
		CONSTRAINT "FK_TABLE4_TO_TABLE52"
		FOREIGN KEY (
			"COL2"
		)
		REFERENCES "MY_SCHEMA"."TABLE4" (
			"COL"
		);

ALTER TABLE "MY_SCHEMA"."books"
	ADD
		CONSTRAINT "FK_genre_TO_books"
		FOREIGN KEY (
			"genre_no"
		)
		REFERENCES "MY_SCHEMA"."genre" (
			"genre_no"
		);

ALTER TABLE "MY_SCHEMA"."review"
	ADD
		CONSTRAINT "FK_books_TO_review"
		FOREIGN KEY (
			"book_no"
		)
		REFERENCES "MY_SCHEMA"."books" (
			"book_no"
		);

ALTER TABLE "MY_SCHEMA"."review"
	ADD
		CONSTRAINT "FK_users_TO_review"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."review"
	ADD
		CONSTRAINT "FK_style_TO_review"
		FOREIGN KEY (
			"style_no"
		)
		REFERENCES "MY_SCHEMA"."style" (
			"style_no"
		);

ALTER TABLE "MY_SCHEMA"."review_user"
	ADD
		CONSTRAINT "FK_users_TO_review_user"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."review_user"
	ADD
		CONSTRAINT "FK_review_TO_review_user"
		FOREIGN KEY (
			"review_no"
		)
		REFERENCES "MY_SCHEMA"."review" (
			"review_no"
		);

ALTER TABLE "MY_SCHEMA"."style"
	ADD
		CONSTRAINT "FK_emotion_TO_style"
		FOREIGN KEY (
			"emo_no"
		)
		REFERENCES "MY_SCHEMA"."emotion" (
			"emo_no"
		);

ALTER TABLE "MY_SCHEMA"."playlist"
	ADD
		CONSTRAINT "FK_users_TO_playlist"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."music"
	ADD
		CONSTRAINT "FK_emotion_TO_music"
		FOREIGN KEY (
			"emo_no"
		)
		REFERENCES "MY_SCHEMA"."emotion" (
			"emo_no"
		);

ALTER TABLE "MY_SCHEMA"."book_user"
	ADD
		CONSTRAINT "FK_books_TO_book_user"
		FOREIGN KEY (
			"book_no"
		)
		REFERENCES "MY_SCHEMA"."books" (
			"book_no"
		);

ALTER TABLE "MY_SCHEMA"."book_user"
	ADD
		CONSTRAINT "FK_users_TO_book_user"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."user_genre"
	ADD
		CONSTRAINT "FK_genre_TO_user_genre"
		FOREIGN KEY (
			"genre_no"
		)
		REFERENCES "MY_SCHEMA"."genre" (
			"genre_no"
		);

ALTER TABLE "MY_SCHEMA"."user_genre"
	ADD
		CONSTRAINT "FK_users_TO_user_genre"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."playlist_review"
	ADD
		CONSTRAINT "FK_playlist_TO_playlist_review"
		FOREIGN KEY (
			"playlist_no"
		)
		REFERENCES "MY_SCHEMA"."playlist" (
			"playlist_no"
		);

ALTER TABLE "MY_SCHEMA"."playlist_review"
	ADD
		CONSTRAINT "FK_review_TO_playlist_review"
		FOREIGN KEY (
			"review_no"
		)
		REFERENCES "MY_SCHEMA"."review" (
			"review_no"
		);

ALTER TABLE "MY_SCHEMA"."user_playlist"
	ADD
		CONSTRAINT "FK_users_TO_user_playlist"
		FOREIGN KEY (
			"user_no"
		)
		REFERENCES "MY_SCHEMA"."users" (
			"user_no"
		);

ALTER TABLE "MY_SCHEMA"."user_playlist"
	ADD
		CONSTRAINT "FK_playlist_TO_user_playlist"
		FOREIGN KEY (
			"playlist_no"
		)
		REFERENCES "MY_SCHEMA"."playlist" (
			"playlist_no"
		);