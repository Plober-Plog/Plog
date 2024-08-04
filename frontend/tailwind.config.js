/** @type {import('tailwindcss').Config} */
module.exports = {
  // 이걸 빼면 바닐라랑 똑같이 됨!
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {
      maxWidth: {
        'custom': '600px',
      },
      fontFamily: {
        'suit': ['SUIT-Regular', 'sans-serif'],
      },
    },
  },
  plugins: [],
};