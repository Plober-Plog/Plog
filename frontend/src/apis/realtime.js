import axios from 'axios';
import { getCookie } from '../utils/cookieUtils';

const API_REALTIME_URL = process.env.REACT_APP_REALTIME_BASE_URL;
const REALTIME_API = axios.create({
    baseURL: API_REALTIME_URL
});
// 토큰 인터셉터
REALTIME_API.interceptors.request.use(
    (config) => {
      const accessToken = getCookie('accessToken');
  
      if (accessToken) {
        config.headers.Authorization = accessToken;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );
  
export default REALTIME_API;