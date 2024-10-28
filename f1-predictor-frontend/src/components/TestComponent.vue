<script>
// import axios from 'axios';

export default {
  name: 'TestComponent',
  props: {
    driverCode: {
      type: String,
      required: true
    }
  },
  watch: {
    driverCode: {
      immediate: true,
      handler: 'fetchDriverInfo'
    }
  },
  async created() {
    await this.fetchDriverInfo();
  },
  data() {
    return {
      driver: {team: {}}
    };
  },
  methods: {
    async fetchDriverInfo() {
      if (!this.driverCode) return;
      try {
        const response = await fetch(`http://localhost:8081/api/drivers/${this.driverCode}`);
        if (!response.ok) throw new Error("Failed to fetch driver info");
        this.driver = await response.json();
      } catch (error) {
        console.error(error);
      }
    }
  },
};
</script>

<template>
  <div :class="['tw-mb-1', 'tw-text-f1-black', `tw-bg-team-${driver.team.shortName}`]">
    <h2>Driver for {{ driver.team.shortName }}:</h2>
    <div>
      <p>{{ driver.shortName }}</p>
      <p>{{ driver.name }}</p>
      <p>{{ driver.nationality }}</p>
    </div>
  </div>
</template>

<style scoped>

</style>