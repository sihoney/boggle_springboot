INSERT INTO users (user_name, nickname, email, password) 
VALUES ('이강인', '강잉뉴', 'kang@naver.com', '1234');
INSERT INTO users (user_name, nickname, email, password) 
VALUES ('우가르테', '우갈', 'ugarte@naver.com', '1234');
INSERT INTO users (user_name, nickname, email, password) 
VALUES ('하키미', '키미', 'hakimi@naver.com', '1234');

INSERT INTO emotion (emotion_id, emotion_name) VALUES (0, '분노');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (1, '슬픔');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (2, '고통');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (3, '불안');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (4, '창피');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (5, '기쁨');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (6, '사랑');
INSERT INTO emotion (emotion_id, emotion_name) VALUES (7, '바람');


INSERT INTO font (font_name) VALUES ('maru-buri');
INSERT INTO font (font_name) VALUES ('nanum-pen');
INSERT INTO font (font_name) VALUES ('nanum-gothic-eco');
INSERT INTO font (font_name) VALUES ('nanum-myeongjo');
INSERT INTO font (font_name) VALUES ('NanumGaRamYeonGgoc');


INSERT INTO wallpaper (wallpaper_name) VALUES ('Blackthorn.png');
INSERT INTO wallpaper (wallpaper_name) VALUES ('Blue.png');
INSERT INTO wallpaper (wallpaper_name) VALUES ('Cat.png');
INSERT INTO wallpaper (wallpaper_name) VALUES ('Clouds.png');
INSERT INTO wallpaper (wallpaper_name) VALUES ('Dubrovnik.png');

INSERT INTO playlist (playlist_name, user_id) 
VALUES ('강잉뉴01', 1);
INSERT INTO playlist (playlist_name, user_id) 
VALUES ('우갈02', 2);
INSERT INTO playlist (playlist_name, user_id) 
VALUES ('키미03', 3);