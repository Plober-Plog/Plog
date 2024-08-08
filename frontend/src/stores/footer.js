import { create } from 'zustand';

const useButtonStore = create((set) => ({
  activeButton: 'home',
  setActiveButton: (button) => set({ activeButton: button }),
}));

export default useButtonStore;