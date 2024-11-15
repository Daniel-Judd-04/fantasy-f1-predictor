<script>
import CompactTeamDisplay from "@/components/display/CompactTeamDisplay.vue";

export default {
  name: 'RecommendedTeamsContainer',
  components: {CompactTeamDisplay},
  props: {
    recommendedTeams: {
      type: Array,
      required: true
    },
    userTeams: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      comparativeTeam: this.userTeams[0],
    }
  },
  watch: {
    userTeams() {
      this.comparativeTeam = this.userTeams[0];
    }
  },
  methods: {
    updateComparativeTeam() {
      this.comparativeTeam = this.userTeams.find(team => `${team.teamName}-${team.teamOwner}` === document.getElementById('comparativeTeam').value);
      this.$emit('updateComparativeTeam', this.comparativeTeam);
    },
  }
}
</script>

<template>
  <div class="tw-w-full tw-h-full tw-flex tw-flex-col tw-bg-gradient-to-bl tw-to-f1-red tw-from-primary-dark tw-to-200% tw-border-primary-light tw-border-2 tw-rounded-lg">
    <div class="tw-flex tw-gap-2 tw-border-b-1 tw-border-primary-light tw-p-2 tw-text-f1-white">
      <div class="tw-font-bold tw-mt-0.5 tw-text-xl tw-mr-auto">
        Recommended Fantasy Teams
      </div>
      <div class="tw-flex tw-items-center tw-mr-0.5">
        Compare with:
        <select id="comparativeTeam" @change="updateComparativeTeam"
                class="tw-w-64 tw-ml-2 tw-px-2 tw-appearance-none tw-border-1 tw-border-primary-light tw-bg-primary-light tw-bg-opacity-5 tw-text-f1-white tw-text-center">
          <option :value="`${team.teamName}-${team.teamOwner}`" v-for="team in userTeams" :key="team.code">
            {{ `${team.teamName} (${team.teamOwner})` }}
          </option>
        </select>
      </div>
    </div>
    <div class="tw-bg-primary-dark tw-rounded-b-lg tw-overflow-hidden tw-h-full tw-text-f1-white">
      <div v-if="recommendedTeams.length === 0" class="tw-flex tw-items-center tw-justify-center tw-h-full">
        <div>Loading...</div>
      </div>
      <div class="tw-flex tw-flex-col tw-px-2 tw-overflow-hidden" v-if="recommendedTeams.length > 0 && comparativeTeam">
        <div class="tw-flex tw-gap-2 tw-border-x-1 tw-border-x-primary-dark tw-border-b-0 tw-border-b-primary-light tw-pt-1 tw-font-medium">
          <div class="tw-w-1/12 tw-border-r-1 tw-border-primary-light">Value</div>
          <div class="tw-w-5/12">Statistics</div>
          <div class="tw-flex tw-w-6/12 tw-gap-1">
            <div class="tw-w-7/12 tw-border-l-1 tw-border-primary-light">Drivers</div>
            <div class="tw-w-4/12 tw-border-x-1 tw-border-primary-light">Constructors</div>
            <div class="tw-w-1/12">Diff</div>
          </div>
        </div>
        <div class="tw-overflow-hidden">
          <div class="hide-scrollbar tw-overflow-y-scroll tw-scroll-smooth">
            <div class="tw-h-full tw-flex tw-flex-col tw-gap-2">
              <CompactTeamDisplay v-for="team in recommendedTeams" :key="team.code" :team="team" :comparative-team="comparativeTeam"/>
              <CompactTeamDisplay v-for="team in recommendedTeams" :key="team.code" :team="team" :comparative-team="comparativeTeam"/>
              <CompactTeamDisplay v-for="team in recommendedTeams" :key="team.code" :team="team" :comparative-team="comparativeTeam"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.hide-scrollbar {
  /*FireFox*/
  scrollbar-width: none;
  /*IE10+*/
  -ms-overflow-style: -ms-autohiding-scrollbar;
}

.hide-scrollbar::-webkit-scrollbar {
  /*Chrome, Safari, Edge*/
  display: none;
}
</style>