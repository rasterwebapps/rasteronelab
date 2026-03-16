/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // LIS Brand Colors
        primary: {
          50: '#e8f5e9',
          100: '#c8e6c9',
          200: '#a5d6a7',
          300: '#81c784',
          400: '#66bb6a',
          500: '#4caf50',  // Main brand green
          600: '#43a047',
          700: '#388e3c',
          800: '#2e7d32',
          900: '#1b5e20',
        },
        secondary: {
          500: '#1565c0',  // Medical blue
          600: '#0d47a1',
        },
        // Department Colors
        biochemistry: '#2196f3',
        hematology: '#f44336',
        microbiology: '#4caf50',
        histopathology: '#9c27b0',
        serology: '#ff9800',
        molecular: '#00bcd4',
      },
      fontFamily: {
        sans: ['Inter', 'Roboto', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      spacing: {
        '18': '4.5rem',
        '88': '22rem',
      }
    },
  },
  plugins: [],
  // Dark mode support
  darkMode: 'class',
}
