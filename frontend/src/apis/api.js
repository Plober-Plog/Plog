import axios from 'axios';
import useAuthStore from './store';

const API = axios.create({
  baseURL: 'https://i11b308.p.ssafy.io/api',
});

API.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default API;
