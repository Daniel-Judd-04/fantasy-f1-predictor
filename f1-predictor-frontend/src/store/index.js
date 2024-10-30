import {createStore} from 'vuex';

export default createStore({
    state: {
        drivers: [],
        teams: [],
    },
    mutations: {
        setDrivers(state, drivers) {
            state.drivers = drivers;
        },
        setTeams(state, teams) {
            state.teams = teams;
        },
    },
    actions: {
        async fetchDrivers({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/drivers`);
                if (!response.ok) throw new Error("Failed to fetch drivers");
                const tempDrivers = await response.json();

                // Process driver data here, as you've done in the previous setup
                for (let i = 0; i < tempDrivers.length; i++) {
                    if (typeof tempDrivers[i] === 'number') {
                        const teammate = tempDrivers.find(
                            driver => driver?.team?.drivers?.some(d => d.id === tempDrivers[i])
                        );

                        if (teammate) {
                            const fullDriverData = teammate.team.drivers.find(d => d.id === tempDrivers[i]);
                            tempDrivers[i] = fullDriverData;

                            if (typeof tempDrivers[i].team === 'number') {
                                tempDrivers[i].team = teammate.team;
                            }
                        } else {
                            const fallbackResponse = await fetch(`http://localhost:8081/api/drivers/id=${tempDrivers[i]}`);
                            if (!fallbackResponse.ok) throw new Error("Failed to fetch driver info");
                            tempDrivers[i] = await fallbackResponse.json();

                            if (typeof tempDrivers[i].team === 'number') {
                                const teamResponse = await fetch(`http://localhost:8081/api/teams/id=${tempDrivers[i].team}`);
                                if (teamResponse.ok) {
                                    tempDrivers[i].team = await teamResponse.json();
                                }
                            }
                        }

                        for (let j = 0; j < tempDrivers[i].team.drivers.length; j++) {
                            if (typeof tempDrivers[i].team.drivers[j] !== 'number') {
                                tempDrivers[i].team.drivers[j] = tempDrivers[i].team.drivers[j].id;
                            }
                        }
                    }
                }

                console.log(tempDrivers);

                commit('setDrivers', tempDrivers);
            } catch (error) {
                console.error(error);
            }
        },
        async fetchTeams({commit}) {
            try {
                const response = await fetch(`http://localhost:8081/api/teams`);
                if (!response.ok) throw new Error("Failed to fetch teams");
                const tempTeams = await response.json();

                // Process team data here, as you've done in the previous setup
                for (let i = 0; i < tempTeams.length; i++) {
                    if (typeof tempTeams[i].drivers[0] === 'number') {
                        const driverResponse = await fetch(`http://localhost:8081/api/drivers/id=${tempTeams[i].drivers[0]}`);
                        if (!driverResponse.ok) throw new Error("Failed to fetch driver info");
                        tempTeams[i].drivers[0] = await driverResponse.json();
                    }

                    if (typeof tempTeams[i].drivers[1] === 'number') {
                        const driverResponse = await fetch(`http://localhost:8081/api/drivers/id=${tempTeams[i].drivers[1]}`);
                        if (!driverResponse.ok) throw new Error("Failed to fetch driver info");
                        tempTeams[i].drivers[1] = await driverResponse.json();
                    }
                }

                console.log(tempTeams);

                commit('setTeams', tempTeams);
            } catch (error) {
                console.error(error);
            }
        },
    },
    getters: {
        allDrivers(state) {
            return state.drivers;
        },
        allTeams(state) {
            return state.teams;
        },
    },
});
