import axios from 'axios';
import useAuthStore from '../stores/store';
import { useNavigate } from 'react-router-dom';

const API = axios.create({
  baseURL: 'https://i11b308.p.ssafy.io/api',
});


// 토큰 인터셉터
API.interceptors.request.use(
  (config) => {
    const { accessToken } = useAuthStore.getState();
    console.log('현재 토큰: ', accessToken);

    if (accessToken) {
      config.headers.Authorization =  accessToken;
      console.log('headers에 토큰 추가 :', config.headers.Authorization);
    }
    return config;
  },
  (error) => Promise.reject(error)
);


// 리프레시 토큰 확인
API.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 엑세스 토큰 만료시
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // 리프레시 토큰 있으면 엑세스 토큰 다시 저장
        const { refreshToken } = useAuthStore.getState();
        const refreshTokenResponse = await axios.post('https://i11b308.p.ssafy.io/api/auth/refresh-token', { refreshToken });
        const { accessToken: newAccessToken } = refreshTokenResponse.data;
        useAuthStore.getState().setToken(newAccessToken, refreshToken);
        originalRequest.headers['Authorization'] = newAccessToken;
        return axios(originalRequest);
      } catch (refreshError) {
        // 만료됐으면 로그인 페이지로 이동
        useAuthStore.getState().clearToken();
        useNavigate()('/login');
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default API;
