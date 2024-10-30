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
                    MER: '#6CD3BF',
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
                    white: '#FFFFFF',
                },
            }
        },
    },
    plugins: [],
}

