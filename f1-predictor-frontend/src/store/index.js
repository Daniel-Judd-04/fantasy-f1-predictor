import {createStore} from 'vuex';
import {getConstructor, getDriver, sort} from "@/utils/common";

const maxStage = 5;
const baseUrl = 'http://localhost:8081/api/';

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
        updateDrivers(state, driver) {
            const index = state.drivers.findIndex(d => d.driverId === driver.driverId);
            if (index !== -1) {
                state.drivers[index] = driver;
            }
        },
        updateConstructors(state, constructor) {
            const index = state.constructors.findIndex(c => c.constructorId === constructor.constructorId);
            if (index !== -1) {
                state.constructors[index] = constructor;
            }
        },
        updateCircuits(state, circuit) {
            const index = state.circuits.findIndex(c => c.circuitId === circuit.circuitId);
            if (index !== -1) {
                state.circuits[index] = circuit;
            }
        },
        updateGrandsPrix(state, grandPrix) {
            const index = state.grandsPrix.findIndex(gp => gp.grandPrixId === grandPrix.grandPrixId);
            if (index !== -1) {
                state.grandsPrix[index] = grandPrix;
            }
        },
        updateUserTeams(state, userTeam) {
            const index = state.userTeams.findIndex(t => t.name === userTeam.name && t.owner === userTeam.owner);
            if (index !== -1) {
                state.userTeams[index] = userTeam;
            }
        },
        deleteDrivers(state, driver) {
            state.drivers = state.drivers.filter(d => d.driverId !== driver.id);
        },
        deleteConstructors(state, constructor) {
            state.constructors = state.constructors.filter(c => c.constructorId !== constructor.id);
        },
        deleteCircuits(state, circuit) {
            state.circuits = state.circuits.filter(c => c.circuitId !== circuit.id);
        },
        deleteGrandsPrix(state, grandPrix) {
            state.grandsPrix = state.grandsPrix.filter(gp => gp.grandPrixId !== grandPrix.id);
        },
        deleteUserTeams(state, userTeam) {
            state.userTeams = state.userTeams.filter(t => t.name !== userTeam.name && t.owner !== userTeam.owner);
        }
    },
    actions: {
        async fetchDrivers({commit}) {
            try {
                const response = await fetch(`${baseUrl}drivers`);
                if (!response.ok) throw new Error("Failed to fetch drivers");
                let drivers = await response.json();

                // Sort drivers by points in descending order
                sort(drivers, 'Fantasy Points');

                commit('setDrivers', drivers);
                console.log("Drivers:", drivers);
                return {
                    title: "Drivers fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 1,
                    maxStage: maxStage,
                };
            } catch (error) {
                return {
                    title: "Error fetching drivers",
                    message: error,
                    success: false,
                    stage: 1,
                    maxStage: maxStage,
                };
            }
        },
        async fetchConstructors({commit}) {
            try {
                const response = await fetch(`${baseUrl}constructors`);
                if (!response.ok) throw new Error("Failed to fetch constructors");
                let constructors = await response.json();

                // Use saved setting in the future
                sort(constructors, 'Fantasy Points');

                commit('setConstructors', constructors);
                console.log("Constructors:", constructors);
                return {
                    title: "Constructors fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 2,
                    maxStage: maxStage,
                };
            } catch (error) {
                return {
                    title: "Error fetching constructors",
                    message: error,
                    success: false,
                    stage: 2,
                    maxStage: maxStage,
                };
            }
        },
        async fetchCircuits({commit}) {
            try {
                const response = await fetch(`${baseUrl}circuits`);
                if (!response.ok) throw new Error("Failed to fetch circuits");
                let circuits = await response.json();

                commit('setCircuits', circuits);
                console.log("Circuits:", circuits);
                return {
                    title: "Circuits fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 3,
                    maxStage: maxStage,
                };
            } catch (error) {
                return {
                    title: "Error fetching circuits",
                    message: error,
                    success: false,
                    stage: 3,
                    maxStage: maxStage,
                };
            }
        },
        async fetchGrandsPrix({commit}) {
            try {
                const response = await fetch(`${baseUrl}grands-prix`);
                if (!response.ok) throw new Error("Failed to fetch grands prix");
                let grandsPrix = await response.json();

                grandsPrix = grandsPrix.sort((a, b) => a.round - b.round).sort((a, b) => a.season - b.season);

                commit('setGrandsPrix', grandsPrix);
                console.log("Grands Prix:", grandsPrix);
                return {
                    title: "Grands Prix fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 4,
                    maxStage: maxStage,
                };
            } catch (error) {
                return {
                    title: "Error fetching grands prix",
                    message: error,
                    success: false,
                    stage: 4,
                    maxStage: maxStage,
                };
            }
        },
        async fetchUserTeams({commit}) {
            try {
                const response = await fetch(`${baseUrl}user-teams`);
                if (!response.ok) throw new Error("Failed to fetch user teams");
                let userTeams = await response.json();

                for (const team of userTeams) {
                    team.drivers = Array.from(team.drivers).sort((a, b) => getDriver(b).fantasyPoints - getDriver(a).fantasyPoints);
                    team.constructors = Array.from(team.constructors).sort((a, b) => getConstructor(b).fantasyPoints - getConstructor(a).fantasyPoints);
                }

                commit('setUserTeams', userTeams);
                console.log("User Teams:", userTeams);
                return {
                    title: "User Teams fetched",
                    message: "Data fetched successfully with status: " + response.status,
                    success: true,
                    stage: 5,
                    maxStage: maxStage,
                };
            } catch (error) {
                return {
                    title: "Error fetching user teams",
                    message: error,
                    success: false,
                    stage: 5,
                    maxStage: maxStage,
                };
            }
        },
        async fetchRecommendedTeams({commit}, data) {
            try {
                commit('setRecommendedTeams', []);
                console.log(data);
                const response = await fetch(`${baseUrl}recommended-teams/${data.limit}/${data.chips}/${data.teamId}/${data.grandPrixId}`);
                if (!response.ok) throw new Error("Failed to fetch recommended teams");
                let teams = await response.json();

                commit('setRecommendedTeams', teams);
                console.log("Recommended Teams:", teams);
                return true;
            } catch (error) {
                console.error("Error fetching recommended teams", error);
            }
            return false;
        },
        async update({commit}, object) {
            const objectType = object.objectType;
            delete object.objectType;
            try {
                const response = await fetch(`${baseUrl}${objectType}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(object),
                });
                if (response.ok) {
                    console.log("Updated " + object.fullName, response);
                    commit(`update${objectType.split('-').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join('')}`, object);
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
            console.warn(`Failed to update ${objectType}: ${object.fullName}`);
            return false;
        }
        ,
        // eslint-disable-next-line no-unused-vars
        async add({commit}, object) {
            const objectType = object.objectType;
            delete object.objectType;
            try {
                console.log("Adding " + object);
                const response = await fetch(`${baseUrl}${objectType}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(object),
                });
                if (response.ok) {
                    console.log("Added " + object.fullName, response);
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
            console.warn(`Failed to add ${objectType}: ${object.fullName}`);
            return false;
        }
        ,
        async delete({commit}, object) {
            const objectType = object.objectType;
            delete object.objectType;
            try {
                const response = await fetch(`${baseUrl}${objectType}/id=${object.id}`, {
                    method: 'DELETE',
                });
                if (response.ok) {
                    console.log(`Deleted ${objectType}: ${object.id}` + object, response);
                    commit(`delete${objectType.split('-').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join('')}`, object);
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
            console.warn(`Failed to delete ${objectType}: ${object.id}`);
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
            return state.grandsPrix
        },
        userTeams(state) {
            return state.userTeams;
        },
        recommendedTeams(state) {
            return state.recommendedTeams;
        }
    },
});
