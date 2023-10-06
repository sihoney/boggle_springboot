-- 유저
CREATE TABLE `boggle`.`users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `user_name` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(200) NOT NULL,
    `email` VARCHAR(500) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `user_profile` VARCHAR(500),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 감정
CREATE TABLE `boggle`.`emotion` (
	`emotion_id` INT NOT NULL PRIMARY KEY, 
	`emotion_name` VARCHAR(100) 
);

-- 폰트
CREATE TABLE `boggle`.`font` (
	`font_id` INT AUTO_INCREMENT NOT NULL PRIMARY KEY, 
	`font_name` VARCHAR(100)
);

-- 배경화면
CREATE TABLE `boggle`.`wallpaper` (
	`wallpaper_id` INT AUTO_INCREMENT NOT NULL PRIMARY KEY, 
	`wallpaper_name` VARCHAR(100) 
);

-- 장르
CREATE TABLE `boggle`.`genre` (
	`genre_id` INT PRIMARY KEY NOT NULL,
	`genre_name` VARCHAR(100)
);

-- 책
CREATE TABLE `boggle`.`book` (
	`isbn` BIGINT UNSIGNED PRIMARY KEY NOT NULL,
	`book_name` VARCHAR(500) NOT NULL,
	`author` VARCHAR(200),
	`book_url` VARCHAR(500),
	`cover_url` VARCHAR(500),
	`genre_id` INT, 
	FOREIGN KEY (`genre_id`) REFERENCES `genre`(`genre_id`)
);

-- 음악
CREATE TABLE `boggle`.`music` (
	`music_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	`music_name` VARCHAR(200),
	`artist` VARCHAR(200),
	`music_path` VARCHAR(500),
	`emotion_id` INT,
	FOREIGN KEY(`emotion_id`) REFERENCES `emotion`(`emotion_id`)
);

-- 서평
CREATE TABLE `boggle`.`review` (
	`review_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	`content` VARCHAR(1000),
	`user_id` INT NOT NULL,
	`isbn` BIGINT UNSIGNED NOT NULL,
	`emotion_id` INT NOT NULL,
	`font_id` INT,
	`wallpaper_id` INT,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`),
	FOREIGN KEY(`isbn`) REFERENCES `book`(`isbn`),
	FOREIGN KEY(`emotion_id`) REFERENCES `emotion`(`emotion_id`),
	FOREIGN KEY(`font_id`) REFERENCES `font`(`font_id`),
	FOREIGN KEY(`wallpaper_id`) REFERENCES `wallpaper`(`wallpaper_id`)
);

-- 플레이리스트 playlist
CREATE TABLE playlist (
    playlist_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, 
    playlist_name VARCHAR(200),
    user_id INT, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`)
);

-- 플레이리스트_서평 review_playlist
CREATE TABLE review_playlist (
    review_playlist_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, 
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    playlist_id INT NOT NULL, 
    review_id INT NOT NULL, 
    FOREIGN KEY(`playlist_id`) REFERENCES `playlist`(`playlist_id`),
    FOREIGN KEY(`review_id`) REFERENCES `review`(`review_id`)
);

-- 플레이리스트_유저 playlist_user
CREATE TABLE playlist_user (
    playlist_user_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, 
    user_id INT NOT NULL, 
    playlist_id INT NOT NULL,
    FOREIGN KEY(`playlist_id`) REFERENCES `playlist`(`playlist_id`),
    FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`)
);


CREATE TABLE review_user (
	review_user_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	user_id INT NOT NULL, 
	review_id INT NOT NULL, 
	added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`),
	FOREIGN KEY(`review_id`) REFERENCES `review`(`review_id`)
);
