import { create } from 'zustand';
import { setCookie, getCookie, eraseCookie } from '../utils/cookieUtils';
<<<<<<< HEAD
=======
import API from '../apis/api';
>>>>>>> master

const useAuthStore = create((set) => ({
  accessToken: getCookie('accessToken'),
  refreshToken: getCookie('refreshToken'),
<<<<<<< HEAD
  userData: JSON.parse(getCookie('userData') || '{}'),
=======
  userData: JSON.parse(localStorage.getItem('userData') || '{}'),
>>>>>>> master
  isLogin: !!getCookie('accessToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    setCookie('accessToken', accessToken, 60); // 1시간
    setCookie('refreshToken', refreshToken, 7 * 24 * 60); // 일주일
    set({ accessToken, refreshToken, isLogin: true });
<<<<<<< HEAD
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
=======
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
>>>>>>> master
  },

  // 아이디 가져오기
  getSearchId: () => {
<<<<<<< HEAD
    const userData = JSON.parse(getCookie('userData') || '{}');
    console.log('아이디:', userData.searchId);
    return userData ? userData.searchId : null;
  },
}));

export default useAuthStore;
=======
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
>>>>>>> master
