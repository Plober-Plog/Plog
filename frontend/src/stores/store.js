import create from 'zustand';

const useAuthStore = create((set) => ({
  // 토큰
  token: localStorage.getItem('token'),
  // 토큰 저장
  setToken: (token) => {
    console.log('토큰 저장 함수 호출:', token);
    set({ token });
    localStorage.setItem('token', token);
  },
  // 토큰 삭제
  clearToken: () => {
    set({ token: null });
    localStorage.removeItem('token');
  },
}));

export default useAuthStore;