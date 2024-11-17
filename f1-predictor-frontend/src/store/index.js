import {createStore} from 'vuex';
import {sort} from "@/utils/common";

export default createStore({
    state: {
        drivers: [],
        constructors: [],
        circuits: [],
        grandsPrix: [],
        userTeams: [],
        recommendedTeams: [],
    },
    mutations: {
        setDrivers(state, drivers) {
            state.drivers = drivers;
        },
        setConstructors(state, constructors) {
            state.constructors = constructors;
        },
        setCircuits(state, circuits) {
            state.circuits = circuits;
        },
        setGrandsPrix(state, grandsPrix) {
            state.grandsPrix = grandsPrix;
        },
        setUserTeams(state, userTeams) {
            state.userTeams = userTeams;
        },
        setRecommendedTeams(state, recommendedTeams) {
            state.recommendedTeams = recommendedTeams;
        },
        addDriver(state, driver) {
            state.drivers.push(driver);
        },
        addConstructor(state, constructor) {
            state.constructors.push(constructor);
        },
    },
    actions: {
        async fetchDrivers({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/drivers`);
                if (!response.ok) throw new Error("Failed to fetch drivers");
                let drivers = await response.json();

                // Sort drivers by points in descending order
                drivers = drivers.sort((a, b) => b.fantasyPoints - a.fantasyPoints);

                commit('setDrivers', drivers);
                console.log("Drivers:", drivers);
                return {
                    title: "Drivers fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 1,
                };
            } catch (error) {
                return {
                    title: "Error fetching drivers",
                    message: error,
                    success: false,
                    stage: 0,
                };
            }
        },
        async fetchConstructors({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/constructors`);
                if (!response.ok) throw new Error("Failed to fetch constructors");
                let constructors = await response.json();

                // Use saved setting in the future
                sort(constructors, 'fantasyPoints');

                commit('setConstructors', constructors);
                console.log("Constructors:", constructors);
                return {
                    title: "Constructors fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 2,
                };
            } catch (error) {
                return {
                    title: "Error fetching constructors",
                    message: error,
                    success: false,
                    stage: 1,
                };
            }
        },
        async fetchCircuits({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/circuits`);
                if (!response.ok) throw new Error("Failed to fetch circuits");
                let circuits = await response.json();

                commit('setCircuits', circuits);
                console.log("Circuits:", circuits);
                return {
                    title: "Circuits fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 3,
                };
            } catch (error) {
                return {
                    title: "Error fetching circuits",
                    message: error,
                    success: false,
                    stage: 2,
                };
            }
        },
        async fetchGrandsPrix({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/grands-prix`);
                if (!response.ok) throw new Error("Failed to fetch grands prix");
                let grandsPrix = await response.json();

                commit('setGrandsPrix', grandsPrix);
                console.log("Grands Prix:", grandsPrix);
                return {
                    title: "Grands Prix fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 4,
                };
            } catch (error) {
                return {
                    title: "Error fetching grands prix",
                    message: error,
                    success: false,
                    stage: 3,
                };
            }
        },
        async fetchUserTeams({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/teams/user`);
                if (!response.ok) throw new Error("Failed to fetch user teams");
                let userTeams = await response.json();

                commit('setUserTeams', userTeams);
                console.log("User Teams:", userTeams);
                return {
                    title: "User Teams fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 5,
                };
            } catch (error) {
                return {
                    title: "Error fetching user teams",
                    message: error,
                    success: false,
                    stage: 4,
                };
            }
        },
        async fetchRecommendedTeams({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/teams/recommended`);
                if (!response.ok) throw new Error("Failed to fetch recommended teams");
                let recommendedTeams = await response.json();

                commit('setRecommendedTeams', recommendedTeams);
                console.log("Recommended Teams:", recommendedTeams);
                return {
                    title: "Recommended Teams fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 6,
                };
            } catch (error) {
                return {
                    title: "Error fetching recommended teams",
                    message: error,
                    success: false,
                    stage: 5,
                };
            }
        },
        // eslint-disable-next-line no-unused-vars
        async update({commit}, object) {
            try {
                let objectType = '';
                if (object.constructorId) {
                    objectType = 'constructor';
                } else if (object.driverId) {
                    objectType = 'driver';
                } else {
                    throw new Error('Object type not recognized');
                }

                const body = {
                    fullName: object.fullName,
                    country: object.country,
                    fantasyPoints: object.fantasyPoints,
                    fantasyPrice: object.fantasyPrice,
                    active: object.active,
                };
                if (objectType === 'driver') {
                    body.driverId = object.driverId;
                    body.points = object.points;
                } else if (objectType === 'constructor') {
                    body.constructorId = object.constructorId;
                }
                const response = await fetch(`http://localhost:8081/api/${objectType}s`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(body),
                });
                if (response.ok) {
                    console.log("Updated " + object.fullName);
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
            console.warn("Failed to update: " + object.fullName);
            return false;
        },
        // eslint-disable-next-line no-unused-vars
        async add({commit}, object) {
            const objectType = object.constructorId ? 'driver' : 'constructor';
            try {
                const body = {
                    fullName: object.fullName,
                    shortName: object.shortName,
                    country: object.country,
                    fantasyPrice: object.fantasyPrice,
                    active: object.active,
                };
                if (objectType === 'driver') {
                    body.constructorId = object.constructorId;
                    body.carNumber = object.carNumber;
                }
                const response = await fetch(`http://localhost:8081/api/${objectType}s`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(body),
                });
                if (response.ok) {
                    console.log("Added " + object.fullName, response);
                    if (objectType === 'driver') {
                        commit('addDriver', await response.json());
                    } else if (objectType === 'constructor') {
                        commit('addConstructor', await response.json());
                    }
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
            console.warn("Failed to add " + objectType + ": " + object.fullName);
            return false;
        }
    },
    getters: {
        allDrivers(state) {
            return state.drivers;
        },
        allConstructors(state) {
            return state.constructors;
        },
        allCircuits(state) {
            return state.circuits;
        },
        allGrandsPrix(state) {
            return state.grandsPrix;
        },
        userTeams(state) {
            return state.userTeams;
        },
        recommendedTeams(state) {
            return state.recommendedTeams;
        }
    },
});
