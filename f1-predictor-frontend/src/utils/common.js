import store from '@/store';

export function getFlagURL(country) {
    if (country == null || country === "") return null;
    const formattedNationality = country.replace(/ /g, '+');

    const baseURL = 'https://fantasy-f1-predictor-images.s3.eu-north-1.amazonaws.com/flags/';

    return `${baseURL}${formattedNationality}.svg`;
}

export function getConciseName(name) {
    if (name == null || name === "") return null;
    return name.substring(0, 1) + '. ' + name.split(' ').slice(-1);
}

const mockDriver = {}

export function getDriver(driverID) {
    if (!driverID) return mockDriver;

    // Access the drivers directly from the store
    const drivers = store.getters.allDrivers;
    return drivers.find(driver => driver.driverId === driverID) || mockDriver;
}


const mockConstructor = {
    shortName: 'null',
}

export function getConstructor(constructorID) {
    if (!constructorID) return mockConstructor;
    // Access the constructors directly from the store
    const constructors = store.getters.allConstructors;
    return constructors.find(constructor => constructor.constructorId === constructorID) || mockConstructor;
}

export function getConstructorPoints(constructorId) {
    const drivers = store.getters.allDrivers;
    let points = 0;
    for (const driver of drivers) {
        if (driver.constructor === constructorId) {
            points += driver.points;
        }
    }
    return points;
}

const mockCircuit = {}

export function getCircuitById(circuitId) {
    const circuits = store.getters.allCircuits;
    for (const circuit of circuits) {
        if (circuit.circuitId === circuitId) {
            return circuit;
        }
    }
    return mockCircuit;
}

export function sort(array, sortBy) {
    if (sortBy === 'points') {
        if (array[0].points) {
            return array.sort((a, b) => b.points - a.points);
        }
        return array.sort((a, b) => getConstructorPoints(b.constructorId) - getConstructorPoints(a.constructorId));
    } else if (sortBy === 'fantasyPoints') {
        return array.sort((a, b) => b.fantasyPoints - a.fantasyPoints);
    } else if (sortBy === 'fantasyPrice') {
        return array.sort((a, b) => b.fantasyPrice - a.fantasyPrice);
    }
    console.error('Invalid sorting method: ' + sortBy, 'For array: ', array);
    return null;
}