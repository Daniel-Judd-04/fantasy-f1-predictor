import store from '@/store';
import {format, parseISO} from "date-fns";

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

    const drivers = store.getters.allDrivers;
    return drivers.find(driver => driver.driverId === driverID) || mockDriver;
}

export function getDriverFromShortName(shortName) {
    if (!shortName) return mockDriver;

    const drivers = store.getters.allDrivers;
    return drivers.find(driver => driver.shortName === shortName) || mockDriver;
}


const mockConstructor = {
    shortName: 'null',
}

export function getConstructor(constructorID) {
    if (!constructorID) return mockConstructor;

    const constructors = store.getters.allConstructors;
    return constructors.find(constructor => constructor.constructorId === constructorID) || mockConstructor;
}

export function getConstructorFromShortName(shortName) {
    if (!shortName) return mockConstructor;

    const constructors = store.getters.allConstructors;
    return constructors.find(constructor => constructor.shortName === shortName) || mockConstructor;
}

export function getConstructorPoints(constructorId) {
    let points = 0;
    if (!getConstructor(constructorId).drivers) {
        return 0;
    }
    for (const driverId of getConstructor(constructorId).drivers) {
        points += getDriver(driverId).points;
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

export function getGrandPrix(grandPrixId) {
    const grandsPrix = store.getters.allGrandsPrix;
    for (const grandPrix of grandsPrix) {
        if (grandPrix.grandPrixId === grandPrixId) {
            return grandPrix;
        }
    }
    return null;
}

export function getUserTeam(teamId) {
    return store.getters.userTeams.find(team => team.teamId === parseInt(teamId));
}

export function sort(array, sortBy) {
    sortBy = sortBy.split(' ').join('-').toLowerCase();
    if (sortBy === 'points') {
        if (array[0].points) {
            return array.sort((a, b) => b.points - a.points);
        }
        return array.sort((a, b) => getConstructorPoints(b.constructorId) - getConstructorPoints(a.constructorId));
    } else if (sortBy === 'fantasy-points') {
        return array.sort((a, b) => b.fantasyPoints - a.fantasyPoints);
    } else if (sortBy === 'fantasy-price') {
        return array.sort((a, b) => b.fantasyPrice - a.fantasyPrice);
    }
    console.error('Invalid sorting method: ' + sortBy, 'For array: ', array);
    return null;
}

export function isDriver(abstractObject) {
    if (!abstractObject) {
        return false;
    }
    return Object.prototype.hasOwnProperty.call(abstractObject, 'driverId');
}

export function isConstructor(abstractObject) {
    if (!abstractObject) {
        return false;
    }
    return Object.prototype.hasOwnProperty.call(abstractObject, 'constructorId');
}

export function isUserTeam(abstractObject) {
    if (!abstractObject) {
        return false;
    }
    return Object.prototype.hasOwnProperty.call(abstractObject, 'teamId');
}

export function isGrandPrix(abstractObject) {
    if (!abstractObject) {
        return false;
    }
    return Object.prototype.hasOwnProperty.call(abstractObject, 'grandPrixId');
}

export function isCircuit(abstractObject) {
    if (!abstractObject) {
        return false;
    }
    return Object.prototype.hasOwnProperty.call(abstractObject, 'circuitId');
}

export function getObjectId(abstractObject) {
    if (isDriver(abstractObject)) {
        return abstractObject.driverId;
    } else if (isConstructor(abstractObject)) {
        return abstractObject.constructorId;
    } else if (isUserTeam(abstractObject)) {
        return abstractObject.teamId;
    } else if (isGrandPrix(abstractObject)) {
        return abstractObject.grandPrixId;
    } else if (isCircuit(abstractObject)) {
        return abstractObject.circuitId;
    }
    return null;
}

export function getObjectType(abstractObject, plural = false) {
    if (isDriver(abstractObject)) {
        return 'Driver' + (plural ? 's' : '');
    } else if (isConstructor(abstractObject)) {
        return 'Constructor' + (plural ? 's' : '');
    } else if (isUserTeam(abstractObject)) {
        return 'User Team' + (plural ? 's' : '');
    } else if (isGrandPrix(abstractObject)) {
        return plural ? 'Grands Prix' : 'Grand Prix';
    } else if (isCircuit(abstractObject)) {
        return 'Circuit' + (plural ? 's' : '');
    }
    return '?';
}

export function getTailwindColor(varName) {
    return getComputedStyle(document.documentElement).getPropertyValue(varName).trim();
}

export function getTeamColour(object) {
    if (isDriver(object) || isConstructor(object)) {
        const constructorShortName = isDriver(object) ? getConstructor(object.constructor).shortName : object.shortName;
        return 'team-' + constructorShortName;
    }
    return 'f1-red';
}

export function getTimezones() {
    const timeZones = Intl.supportedValuesOf('timeZone'); // Get IANA time zones
    return timeZones.map((timeZone) => {
        const offset = getTimezoneOffset(timeZone);
        return {
            value: timeZone,
            label: `UTC${offset >= 0 ? '+' : ''}${offset.replace('.', ':')} (${timeZone.replace('_', ' ')})`,
        };
    });
}

function getTimezoneOffset(timeZone) {
    const now = new Date();
    const formatter = new Intl.DateTimeFormat('en-US', {
        timeZone,
        hour12: false,
        hour: '2-digit',
        minute: '2-digit',
    });
    const parts = formatter.formatToParts(now);
    const hour = parseInt(parts.find((p) => p.type === 'hour').value, 10);
    const minute = parseInt(parts.find((p) => p.type === 'minute').value, 10);
    const offsetInMinutes = hour * 60 + minute - now.getUTCHours() * 60 - now.getUTCMinutes();
    return (offsetInMinutes / 60).toFixed(2); // Offset in hours
}

export function formatDate(dateString) {
    if (!dateString) {
        return "No date";
    }
    const date = parseISO(dateString);
    return `${getOrdinalSuffix(format(date, 'd'))} ${format(date, 'MMMM yyyy')}`;
}

export function getOrdinalSuffix(num) {
    if (num > 3 && num < 21) return `${num}th`; // Handle 11th to 19th
    switch (num % 10) {
        case 1:
            return `${num}st`;
        case 2:
            return `${num}nd`;
        case 3:
            return `${num}rd`;
        default:
            return `${num}th`;
    }
}

export function calculateTransfers(team, comparativeTeam) {
    if (!team || !comparativeTeam) return 0;
    
    let transfers = 0;
    for (let driverId of team.drivers) {
        if (!comparativeTeam.drivers.includes(driverId)) {
            transfers++;
        }
    }
    for (let constructorId of team.constructors) {
        if (!comparativeTeam.constructors.includes(constructorId)) {
            transfers++;
        }
    }
    return transfers;
}