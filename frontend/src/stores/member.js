import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';

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
    console.log('토큰 쿠키 저장 완료! : ', accessToken, refreshToken);
  },

  // 유저 정보 저장 (localStorage에 저장)
  setUserData: (userData) => {
    localStorage.setItem('userData', JSON.stringify(userData));
    set({ userData });
    console.log('유저정보 저장 (localStorage):', userData);
  },

  // 유저 정보 삭제 (localStorage에서 삭제)
  clearToken: () => {
    eraseCookie('accessToken');
    eraseCookie('refreshToken');
    localStorage.removeItem('userData');
    set({ accessToken: null, refreshToken: null, isLogin: false, userData: null });
    console.log('유저 정보 삭제 완료!');
  },

  // 아이디 가져오기
  getSearchId: () => {
    const userData = JSON.parse(localStorage.getItem('userData') || '{}');
    console.log('아이디:', userData.searchId);
    return userData ? userData.searchId : null;
  },
}));

export default useAuthStore;