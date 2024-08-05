import create from 'zustand';

const useAuthStore = create((set) => ({
  // 토큰
  accessToken: localStorage.getItem('accessToken'),
  refreshToken: localStorage.getItem('refreshToken'),

  // 토큰 저장
  setToken: (accessToken, refreshToken) => {
    set({ accessToken, refreshToken });
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  },

  // 토큰 삭제
  clearToken: () => {
    set({ accessToken: null, refreshToken: null });
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  },
}));

export default useAuthStore;
