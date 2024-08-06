import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';

const useAuthStore = create((set) => ({
  accessToken: getCookie('accessToken'),
  refreshToken: getCookie('refreshToken'),
  userData: JSON.parse(getCookie('userData') || '{}'),
  isLogin: !!getCookie('accessToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    setCookie('accessToken', accessToken, 60); // 1시간
    setCookie('refreshToken', refreshToken, 7 * 24 * 60); // 일주일
    set({ accessToken, refreshToken, isLogin: true });
    console.log('토큰 쿠키 저장 완료! : ', accessToken, refreshToken);
  },

  // 유저 정보 저장
  setUserData: (userData) => {
    setCookie('userData', JSON.stringify(userData), 7 * 24 * 60); // 일주일
    set({ userData });
    console.log('유저정보 저장:', userData)
  },

  // 토큰 삭제
  clearToken: () => {
    eraseCookie('accessToken');
    eraseCookie('refreshToken');
    set({ accessToken: null, refreshToken: null, isLogin: false });
    console.log('토큰 쿠키 삭제 완료!');
  },

  // 유저 정보 삭제
  clearUserData: () => {
    eraseCookie('userData');
    set({ userData: null });
    console.log('유저정보 쿠키 삭제 완료!');
  },

  // 아이디 가져오기
  getSearchId: () => {
    const userData = JSON.parse(getCookie('userData') || '{}');
    console.log('아이디:', userData.searchId);
    return userData ? userData.searchId : null;
  },
}));

export default useAuthStore;