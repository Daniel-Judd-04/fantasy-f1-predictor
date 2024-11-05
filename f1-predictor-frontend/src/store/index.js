import {createStore} from 'vuex';

export default createStore({
    state: {
        drivers: [],
        constructors: [],
        circuits: [],
        grandsPrix: [],
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
                console.log('Drivers', drivers);
            } catch (error) {
                console.error(error);
            }
        },
        async fetchConstructors({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/constructors`);
                if (!response.ok) throw new Error("Failed to fetch constructors");
                let constructors = await response.json();

                // Sort constructors based on total driver points using getConstructorPoints
                constructors = constructors.sort((a, b) => b.fantasyPoints - a.fantasyPoints);

                commit('setConstructors', constructors);
                console.log('Constructors', constructors);
            } catch (error) {
                console.error(error);
            }
        },
        async fetchCircuits({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/circuits`);
                if (!response.ok) throw new Error("Failed to fetch circuits");
                let circuits = await response.json();

                commit('setCircuits', circuits);
                console.log('Circuits', circuits);
            } catch (error) {
                console.error(error);
            }
        },
        async fetchGrandsPrix({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/grands-prix`);
                if (!response.ok) throw new Error("Failed to fetch grands prix");
                let grandsPrix = await response.json();

                commit('setGrandsPrix', grandsPrix);
                console.log('Grands Prix', grandsPrix);
            } catch (error) {
                console.error(error);
            }
        },
        // eslint-disable-next-line no-unused-vars
        async updateDriver({commit}, driver) {
            try {
                const response = await fetch(`http://localhost:8081/api/drivers`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        driverId: driver.driverId,
                        fullName: driver.fullName,
                        points: driver.points,
                        fantasyPoints: driver.fantasyPoints,
                        fantasyPrice: driver.fantasyPrice,
                    }),
                });
                if (response.ok) {
                    return true;
                }
            } catch (error) {
                console.error(error);
            }
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
    },
});
