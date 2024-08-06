import { create } from 'zustand';

const useButtonStore = create((set) => ({
  activeButton: null,
  setActiveButton: (button) => set({ activeButton: button }),
}));

export default useButtonStore;
