import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { getCookie, setCookie, eraseCookie } from '../utils/cookieUtils';
import useAuthStore from '../stores/member';

<<<<<<< HEAD
const API = axios.create({
  baseURL: 'https://i11b308.p.ssafy.io/api',
=======
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

const API = axios.create({
  baseURL: API_BASE_URL,
>>>>>>> master
});

// 토큰 인터셉터
API.interceptors.request.use(
  (config) => {
    const accessToken = getCookie('accessToken');
<<<<<<< HEAD
    console.log('현재 토큰 :', accessToken);

    if (accessToken) {
      config.headers.Authorization = accessToken;
      console.log('headers에 토큰 추가 :', config.headers.Authorization);
=======

    if (accessToken) {
      config.headers.Authorization = accessToken;
>>>>>>> master
    }
    return config;
  },
  (error) => Promise.reject(error)
);

<<<<<<< HEAD
=======

>>>>>>> master
// 리프레시 토큰 확인
API.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 엑세스 토큰 만료시
<<<<<<< HEAD
    if (error.response && error.response.status === 408 && !originalRequest._retry) {
=======
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
>>>>>>> master
      originalRequest._retry = true;

      try {
        // 리프레시 토큰 있으면 엑세스 토큰 다시 저장
        const refreshToken = getCookie('refreshToken');
<<<<<<< HEAD
        const refreshTokenResponse = await axios.post('https://i11b308.p.ssafy.io/api/auth/refresh-token', { refreshToken });
        const { accessToken: newAccessToken } = refreshTokenResponse.data;
        setCookie('accessToken', newAccessToken, 60); // 새로운 엑세스 토큰 1시간 유효
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
=======
        const refreshTokenResponse = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
        const newAccessToken = refreshTokenResponse.data.split(' : ')[1];
        setCookie('accessToken', newAccessToken, 60); // 새로운 엑세스 토큰 1시간 유효
        originalRequest.headers.Authorization = newAccessToken;
>>>>>>> master
        return axios(originalRequest);
      } catch (refreshError) {
        // 만료됐으면 로그인 페이지로 이동
        eraseCookie('accessToken');
        eraseCookie('refreshToken');
<<<<<<< HEAD
        eraseCookie('userData');
        useAuthStore.getState().clearToken();
        useAuthStore.getState().clearUserData();
=======
        useAuthStore.getState().clearToken();
>>>>>>> master
        useNavigate()('/login');
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default API;
