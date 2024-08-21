import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';
import API from '../apis/api';

const useAuthStore = create((set) => ({
  accessToken: getCookie('accessToken'),
  refreshToken: getCookie('refreshToken'),
  userData: JSON.parse(localStorage.getItem('userData') || '{}'),
  isLogin: !!getCookie('accessToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    setCookie('accessToken', accessToken, 60); // 1시간
    setCookie('refreshToken', refreshToken, 7 * 24 * 60); // 일주일
    set({ accessToken, refreshToken, isLogin: true });
  },

  // 유저 정보 저장 (localStorage에 저장)
  setUserData: (userData) => {
    localStorage.setItem('userData', JSON.stringify(userData));
    set({ userData });
  },

  // 토큰 및 유저 정보 삭제 (localStorage에서 삭제)
  clearToken: () => {
    eraseCookie('accessToken');
    eraseCookie('refreshToken');
    localStorage.removeItem('userData');
    set({ accessToken: null, refreshToken: null, isLogin: false, userData: null });
  },

  // 아이디 가져오기
  getSearchId: () => {
    const userData = JSON.parse(localStorage.getItem('userData') || '{}');
    return userData ? userData.searchId : null;
  },

  // 자동 로그인 시도
  autoLogin: async () => {
    const refreshToken = getCookie('refreshToken');
    if (refreshToken) {
      try {
        const response = await API.post('/auth/refresh', { refreshToken });
        const newAccessToken = response.data.split(' : ')[1];
        setCookie('accessToken', newAccessToken, 60);
        set({ accessToken: newAccessToken, isLogin: true });
      } catch (error) {
        console.error('자동 로그인 실패:', error);
        set({ isLogin: false });
        eraseCookie('refreshToken');
      }
    }
  },
}));

export default useAuthStore;
