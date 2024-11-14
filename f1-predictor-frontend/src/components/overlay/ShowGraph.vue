<script>
import {mapGetters} from "vuex";
import CloseButton from "@/components/common/CloseButton.vue";
import {getConstructor} from "@/utils/common";

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

      let labels = [];
      for (let i = 0; i <= this.allGrandsPrix.length; i++) {
        labels.push(`Race ${i + 1}`);
        // pointsPerRace.push(this.overlayObject.results.)
      }

      let pointsPerRace = [];
      for (let i = 0; i < this.overlayObject.raceResults.length; i++) {
        pointsPerRace.push(this.convertResultToPoints(this.overlayObject.raceResults[i]));
      }

      let cumulativePoints = [];
      for (let i = 0; i < pointsPerRace.length; i++) {
        if (i === 0) {
          cumulativePoints.push(pointsPerRace[i]);
          continue;
        }
        cumulativePoints.push(cumulativePoints[i - 1] + pointsPerRace[i]);
      }


      const primaryLight = getTailwindColor('--color-primary-light');
      // const white = getTailwindColor('--color-f1-white');
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
              color: white
            },
            grid: {
              color: primary
            }
          },
          y: {
            suggestedMin: 0,
            suggestedMax: 30,
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
      if (result.position > 10) return 0;
      const points = [25, 18, 15, 12, 10, 8, 6, 4, 2, 1]; // Verify this
      console.log(result, points[result.position - 1], (result.fastestLap ? 1 : 0));
      return points[result.position - 1] + (result.fastestLap ? 1 : 0);
    },
    exit() {
      this.$emit('exit');
    }
  }
}
</script>

<template>
  <div :class="[`tw-to-${getGradientColour}`]"
       class="tw-bg-primary-dark tw-w-1/2 tw-bg-gradient-to-bl tw-to-200% tw-from-primary-dark tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ overlayObject.fullName }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
    </div>
    <div class="tw-bg-primary-dark tw-border-t-1 tw-border-primary-light tw-p-4 tw-rounded-b-lg">
      <div class="tw-flex tw-justify-between tw-text-sm">
        <div>{{ getChartData.datasets[0].label }}</div>
        <div>{{ getChartData.datasets[1].label }}</div>
      </div>
      <CustomChart class="tw-h-96" type="line" :data="getChartData" :options="chartOptions"/>
    </div>
  </div>
</template>

<style scoped>

</style>