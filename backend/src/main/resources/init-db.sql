-- 테이블이 존재하면 삭제
DROP TABLE IF EXISTS plant;
DROP TABLE IF EXISTS other_plant_type;
DROP TABLE IF EXISTS plant_type;
DROP TABLE IF EXISTS image;

-- 테이블 생성
CREATE TABLE image
(
    image_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255)
);

CREATE TABLE plant_type
(
    plant_type_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    plant_name         VARCHAR(255) DEFAULT '',
    guide              VARCHAR(255) DEFAULT '',
    image_id           BIGINT,
    water_interval     INT          DEFAULT 3,
    repot_interval     INT          DEFAULT 14,
    fertilize_interval INT          DEFAULT 7,
    repot_mid          INT          DEFAULT 3,
    water_mid          INT          DEFAULT 3,
    fertilize_mid      INT          DEFAULT 3,
    FOREIGN KEY (image_id) REFERENCES image (image_id)
);

CREATE TABLE other_plant_type
(
    other_plant_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plant_name          VARCHAR(255) DEFAULT ""
);

CREATE TABLE plant
(
    plant_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_id            BIGINT,
    plant_type_id       BIGINT,
    other_plant_type_id BIGINT,
    nickname            VARCHAR(255) NOT NULL,
    bio                 TEXT,
    birth_date          DATE         NOT NULL,
    dead_date           DATE,
    has_notified        BOOLEAN      NOT NULL DEFAULT TRUE,
    fixed               INT          NOT NULL DEFAULT 255,
    is_deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    water_date          DATE,
    fertilize_date      DATE,
    repot_date          DATE,
    FOREIGN KEY (image_id) REFERENCES image (image_id),
    FOREIGN KEY (plant_type_id) REFERENCES plant_type (plant_type_id),
    FOREIGN KEY (other_plant_type_id) REFERENCES other_plant_type (other_plant_type_id)
);

-- 데이터 삽입
INSERT INTO plant_type (plant_type_id, plant_name)
VALUES (1, 'Dummy');

INSERT INTO other_plant_type (other_plant_type_id, plant_name)
VALUES (1, 'Dummy');

INSERT INTO image (image_url)
VALUES ('testURL');

-- 삽입할 데이터
INSERT INTO plant_type (plant_name, image_id) VALUES ('몬스테라', 1);
INSERT INTO other_plant_type (plant_name) VALUES ('기타식물');
