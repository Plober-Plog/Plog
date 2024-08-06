import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';

const useAuthStore = create((set) => ({
  // 상태 초기화
  accessToken: getCookie('accessToken'),
  refreshToken: getCookie('refreshToken'),
  userData: JSON.parse(getCookie('userData')),
  isLogin: !!getCookie('accessToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    console.log('토큰 저장 함수 호출:', accessToken, refreshToken);
    setCookie('accessToken', accessToken, 60); // 1시간 동안 유효
    setCookie('refreshToken', refreshToken, 7 * 24 * 60); // 7일 동안 유효
    set({ accessToken, refreshToken, isLogin: true });
  },

  // 유저 정보 저장
  setUserData: (userData) => {
    console.log('유저 정보 저장 함수 호출:', userData);
    setCookie('userData', JSON.stringify(userData), 7 * 24 * 60); // 7일 동안 유효
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

  // searchId 가져오기
  getSearchId: () => {
    const userData = JSON.parse(getCookie('userData'));
    return userData ? userData.searchId : null;
  },
}));

export default useAuthStore;
