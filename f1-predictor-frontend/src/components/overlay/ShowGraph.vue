<script>
import {mapGetters} from "vuex";
import CloseButton from "@/components/common/CloseButton.vue";
import {getConstructor, getGrandPrix, isDriver} from "@/utils/common";

function getTailwindColor(varName) {
  return getComputedStyle(document.documentElement).getPropertyValue(varName).trim();
}

export default {
  name: 'ShowGraph',
  components: {CloseButton},
  props: {
    overlayObject: {
      type: Object,
      required: true,
    }
  },
  computed: {
    ...mapGetters(['allGrandsPrix']),
    getChartData() {
      const season = this.getLatestSeason();

      const grandsPrix = Array.from(this.allGrandsPrix.filter(gp => gp.season === season)).sort((a, b) => a.round - b.round);

      let labels = [];
      let pointsPerRace = [];
      let cumulativePoint = 0;
      let cumulativePoints = [];
      for (let i = 0; i < grandsPrix.length; i++) {
        labels.push(`${grandsPrix[i].fullName}`);
        const results = this.overlayObject.raceResults.filter(result => result.grandPrix === grandsPrix[i].grandPrixId);
        if (results.length > 0) {
          let totalPoints = 0;
          for (let result of results) {
            totalPoints += this.convertResultToPoints(result);
          }
          pointsPerRace.push(totalPoints);
          cumulativePoint += pointsPerRace[i];
          cumulativePoints.push(cumulativePoint);
        } else {
          pointsPerRace.push(0);
          cumulativePoints.push(null);
        }
      }

      const primaryLight = getTailwindColor('--color-primary-light');
      return {
        labels: labels,
        datasets: [
          {
            label: 'Points Per Race',
            fill: false,
            type: 'bar',
            borderColor: primaryLight,
            borderWidth: 2,
            yAxisID: 'y',
            data: pointsPerRace
          },
          {
            label: 'Cumulative Points',
            fill: false,
            type: 'line',
            borderColor: primaryLight,
            borderWidth: 2,
            yAxisID: 'y1',
            tension: 0,
            data: cumulativePoints
          }
        ]
      };
    },
    chartOptions() {
      const primary = getTailwindColor('--color-primary-DEFAULT');
      const white = getTailwindColor('--color-f1-white');
      return {
        stacked: false,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false,
          }
        },
        scales: {
          x: {
            ticks: {
              display: false,
            },
            grid: {
              color: primary
            },
          },
          y: {
            suggestedMin: 0,
            suggestedMax: isDriver(this.overlayObject) ? 30 : 50,
            type: 'linear',
            display: true,
            position: 'left',
            ticks: {
              color: white
            },
            grid: {
              color: primary
            }
          },
          y1: {
            suggestedMin: 0,
            suggestedMax: 100,
            type: 'linear',
            display: true,
            position: 'right',
            ticks: {
              color: white
            },
            grid: {
              drawOnChartArea: false,
              color: primary
            }
          }
        }
      };
    },
    getGradientColour() {
      if (Object.prototype.hasOwnProperty.call(this.overlayObject, 'driverId')) {
        return 'team-' + getConstructor(this.overlayObject.constructor).shortName;
      }
      return 'team-' + this.overlayObject.shortName;
    },
  },
  methods: {
    convertResultToPoints(result) {
      if (result.position > 10 || result.position === 0) return 0;
      const points = [25, 18, 15, 12, 10, 8, 6, 4, 2, 1]; // Verify this
      return points[result.position - 1] + (result.fastestLap ? 1 : 0);
    },
    getLatestSeason() {
      let latestSeason = 0;
      for (let i = 0; i < this.overlayObject.raceResults.length; i++) {
        const grandPrix = getGrandPrix(this.overlayObject.raceResults[i].grandPrix);
        if (grandPrix.season > latestSeason) {
          latestSeason = grandPrix.season;
        }
      }
      return latestSeason;
    },
    exit() {
      this.$emit('exit');
    }
  }
}
</script>

<template>
  <div :class="[`tw-to-${getGradientColour}`]"
       class="tw-bg-primary-dark tw-w-1/2 tw-bg-gradient-to-bl tw-to-200% tw-from-primary-dark tw-drop-shadow-2xl tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ overlayObject.fullName }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
    </div>
    <div class="tw-bg-primary-dark tw-border-t-1 tw-border-primary-light tw-p-4 tw-pt-2 tw-rounded-b-lg">
      <div class="tw-flex tw-justify-between">
        <div class="tw-text-sm tw-mt-1">{{ getChartData.datasets[0].label }}</div>
        <div class="">{{ getLatestSeason() }} Race Results</div>
        <div class="tw-text-sm tw-mt-1">{{ getChartData.datasets[1].label }}</div>
      </div>
      <CustomChart class="tw-h-96" type="line" :data="getChartData" :options="chartOptions"/>
    </div>
  </div>
</template>

<style scoped>

</style>