import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';

const useAuthStore = create((set) => ({
  accessToken: getCookie('accessToken'),
  refreshToken: getCookie('refreshToken'),
  userData: JSON.parse(getCookie('userData') || '{}'),
  isLogin: !!getCookie('accessToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    setCookie('accessToken', accessToken, 60);
    setCookie('refreshToken', refreshToken, 7 * 24 * 60);
    set({ accessToken, refreshToken, isLogin: true });
  },

  // 유저 정보 저장
  setUserData: (userData) => {
    setCookie('userData', JSON.stringify(userData), 7 * 24 * 60);
    set({ userData });
  },

  // 토큰 삭제
  clearToken: () => {
    eraseCookie('accessToken');
    eraseCookie('refreshToken');
    set({ accessToken: null, refreshToken: null, isLogin: false });
  },

  // 유저 정보 삭제
  clearUserData: () => {
    eraseCookie('userData');
    set({ userData: null });
  },

  // 아이디 가져오기
  getSearchId: () => {
    const userData = JSON.parse(getCookie('userData') || '{}');
    return userData ? userData.searchId : null;
  },
}));

export default useAuthStore;