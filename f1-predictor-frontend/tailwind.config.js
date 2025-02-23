/** @type {import('tailwindcss').Config} */
const plugin = require('tailwindcss/plugin');

const darkMode = true;

module.exports = {
    prefix: 'tw-',
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    safelist: [
        {
            pattern: /-team-/
        },
    ],
    theme: {
        extend: {
            colors: {
                // Custom colors
                primary: {
                    light: darkMode ? '#4a5568' : '#6d7481',
                    DEFAULT: darkMode ? '#2d3748' : '#bac6d7',
                    dark: darkMode ? '#1a202c' : '#e7eff5',
                }
                ,
                team: {
                    RBR: '#3671C6',
                    MCL: '#F58020',
                    FER: '#F91536',
                    MEC: '#6CD3BF',
                    AST: '#358C75',
                    VRB: '#6692FF',
                    HAA: '#B6BABD',
                    ALP: '#2293D1',
                    WIL: '#37BEDD',
                    KIC: '#52E252',
                }
                ,
                f1: {
                    red: '#D11A00',
                    black: darkMode ? '#15151E' : '#f7fafc',
                    white: darkMode ? '#c9c3c3' : '#15151E',
                },
            },
            height: {
                18: '4.5rem',
            },
            width: {
                '200%': '200%',
                200: '50rem',
                150: '37.5rem',
                50: '12.5rem',
            },
            padding: {
                '100%': '100%',
                'full': '100vh',
            },
            borderWidth: {
                1: '1px',
            },
            gradientColorStopPositions: {
                '200%': '200%',
            }
        },
    },
    plugins: [
        plugin(function ({addBase, theme}) {
            const colors = theme('colors');
            const variables = Object.entries(colors).reduce((acc, [key, value]) => {
                if (typeof value === 'string') {
                    acc[`--color-${key}`] = value;
                } else {
                    Object.entries(value).forEach(([shade, shadeValue]) => {
                        acc[`--color-${key}-${shade}`] = shadeValue;
                    });
                }
                return acc;
            }, {});

            addBase({
                ':root': variables,
            });
        }),
    ],
}