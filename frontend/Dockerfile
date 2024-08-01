# 단계 1: 빌드 단계
FROM node:16-alpine AS build

WORKDIR /app

# 의존성 설치
COPY package.json yarn.lock ./
RUN yarn install

# 소스 코드 복사 및 빌드
COPY . .
RUN yarn build

# 단계 2: 실행 단계
FROM nginx:alpine

# 빌드 결과물을 Nginx의 기본 경로로 복사
COPY --from=build /app/build /usr/share/nginx/html

# Nginx 설정 파일을 복사 (옵션, 필요 시 설정)
# COPY nginx.conf /etc/nginx/nginx.conf

# 기본 포트를 노출
EXPOSE 3000

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]