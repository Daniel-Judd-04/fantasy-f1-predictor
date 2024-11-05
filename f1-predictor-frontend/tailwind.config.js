/** @type {import('tailwindcss').Config} */
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
                    light: '#4a5568',
                    DEFAULT: '#2d3748',
                    dark: '#1a202c',
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
                    red: '#FF1E00',
                    black: '#15151E',
                    white: '#e7e1e1',
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
    plugins: [],
}

