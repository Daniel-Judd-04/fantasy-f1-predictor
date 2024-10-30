export function getFlagURL(country) {
    if (country == null || country === "") return null;
    const formattedNationality = country.replace(/ /g, '+');

    const baseURL = 'https://fantasy-f1-predictor-images.s3.eu-north-1.amazonaws.com/flags/';

    return `${baseURL}${formattedNationality}.svg`;
}