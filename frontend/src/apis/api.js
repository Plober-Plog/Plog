import axios from 'axios';
import useAuthStore from '../stores/store';

const API = axios.create({
  baseURL: 'https://i11b308.p.ssafy.io/api',
});

API.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    console.log('현재 토큰:', token);
    if (token) {
      config.headers.Authorization = token;
      console.log('헤더에 토큰 추가:', config.headers.Authorization);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// TODO [진아영] 리프레시 토큰
// API.interceptors.response.use(
//   (response) => response,
//   async (error) => {
//     const originalRequest = error.config;
//     if (error.response.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true;
//       try {
//         const refreshTokenResponse = await axios.post('https://i11b308.p.ssafy.io/api/user/refresh', {}, { withCredentials: true });
//         const newAccessToken = refreshTokenResponse.headers['authorization'];
//         useAuthStore.getState().setToken(newAccessToken);
//         axios.defaults.headers.common['Authorization'] = newAccessToken;
//         originalRequest.headers['Authorization'] = newAccessToken;
//         return axios(originalRequest);
//       } catch (refreshError) {
//         useAuthStore.getState().clearToken();
//         navigate('/login');
//         return Promise.reject(refreshError);
//       }
//     }
//     return Promise.reject(error);
//   }
// );

export default API;
