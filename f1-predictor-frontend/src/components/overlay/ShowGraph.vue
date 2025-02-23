<script>
import {mapGetters} from "vuex";
import {getConstructor, getGrandPrix, getTailwindColor, isDriver} from "@/utils/common";
import OverlayHeader from "@/components/common/OverlayHeader.vue";
import {OverlayData} from "@/utils/classes";


export default {
  name: 'ShowGraph',
  components: {OverlayHeader},
  props: {
    overlayData: {
      type: OverlayData,
      required: true,
      default: () => (new OverlayData({}, [], null)),
    }
  },
  data() {
    return {
      currentSeason: this.getMaxSeason()
    }
  },
  computed: {
    ...mapGetters(['allGrandsPrix', 'allDrivers', 'allConstructors']),
    getChartData() {
      const grandsPrix = Array.from(this.allGrandsPrix.filter(gp => gp.season === this.currentSeason)).sort((a, b) => new Date(a.startTime) - new Date(b.startTime));

      let labels = [];
      let pointsPerRace = [];
      let cumulativePoint = 0;
      let cumulativePoints = [];
      for (let i = 0; i < grandsPrix.length; i++) {
        labels.push(`${i + 1} - ${grandsPrix[i].fullName}`);
        const results = this.overlayData.overlayObject.results.filter(result => result.grandPrix === grandsPrix[i].grandPrixId && result.sessionType !== 'Q');
        if (results.length > 0) {
          let totalPoints = 0;
          for (let result of results) totalPoints += result.points;
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
            suggestedMax: isDriver(this.overlayData.overlayObject) ? 35 : 60, // Max for driver is 34 and for constructor is 59
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
            suggestedMax: this.getMaxCumulativePoints(),
            type: 'linear',
            display: true,
            position: 'right',
            ticks: {
              color: white
            },
            grid: {
              drawOnChartArea: false,
            }
          }
        }
      };
    },
    getGradientColour() {
      if (Object.prototype.hasOwnProperty.call(this.overlayData.overlayObject, 'driverId')) {
        return 'team-' + getConstructor(this.overlayData.overlayObject.constructor).shortName;
      }
      return 'team-' + this.overlayData.overlayObject.shortName;
    },
  },
  methods: {
    getMaxCumulativePoints() {
      let maxCumulativePoints = 0;
      if (isDriver(this.overlayData.overlayObject)) {
        for (let driver of this.allDrivers) {
          const maxForDriver = driver.results.filter(result => getGrandPrix(result.grandPrix).season === this.currentSeason).reduce((acc, result) => acc + result.points, 0);
          if (maxForDriver > maxCumulativePoints) {
            maxCumulativePoints = maxForDriver;
          }
        }
      } else {
        for (let constructor of this.allConstructors) {
          const maxForConstructor = constructor.results.filter(result => getGrandPrix(result.grandPrix).season === this.currentSeason).reduce((acc, result) => acc + result.points, 0);
          if (maxForConstructor > maxCumulativePoints) {
            maxCumulativePoints = maxForConstructor;
          }
        }
      }
      console.log("Max Cumulative Points: " + maxCumulativePoints);
      return maxCumulativePoints;
    },
    getMaxSeason() {
      let latestSeason = 0;
      for (let i = 0; i < this.overlayData.overlayObject.results.length; i++) {
        const grandPrix = getGrandPrix(this.overlayData.overlayObject.results[i].grandPrix);
        if (grandPrix.season > latestSeason) {
          latestSeason = grandPrix.season;
        }
      }
      return latestSeason;
    },
    getMinSeason() {
      let earliestSeason = 9999;
      for (let i = 0; i < this.overlayData.overlayObject.results.length; i++) {
        const grandPrix = getGrandPrix(this.overlayData.overlayObject.results[i].grandPrix);
        if (grandPrix.season < earliestSeason) {
          earliestSeason = grandPrix.season;
        }
      }
      return earliestSeason;
    },
    previousSeason() {
      if (this.currentSeason > this.getMinSeason()) {
        this.currentSeason--;
      }
    },
    nextSeason() {
      if (this.currentSeason < this.getMaxSeason()) {
        this.currentSeason++;
      }
    },
    exitAll() {
      this.$emit('exitAll');
    }
  }
}
</script>

<template>
  <div :class="[`tw-to-${getGradientColour}`]"
       class="tw-bg-primary-dark tw-w-1/2 tw-bg-gradient-to-bl tw-to-200% tw-from-primary-dark tw-drop-shadow-2xl tw-outline tw-outline-1 -tw-outline-offset-1 tw-outline-primary-light tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <OverlayHeader @exit="exitAll" :country="overlayData.overlayObject.country">{{ overlayData.overlayObject.fullName }}</OverlayHeader>
    <div class="tw-bg-primary-dark tw-border-t-1 tw-border-primary-light tw-p-4 tw-pt-2 tw-rounded-b-lg">
      <div class="tw-flex tw-justify-between">
        <div class="tw-text-sm tw-mt-1">{{ getChartData.datasets[0].label }}</div>
        <div class="tw-flex tw-gap-4">
          <div @click="previousSeason" :class="[`${currentSeason > getMinSeason() ? '' : 'tw-opacity-50 tw-pointer-events-none'}`]"
               class="tw-cursor-pointer tw-transition-opacity tw-flex tw-items-center">
            <span class="material-symbols-outlined tw-text-2xl tw-font-light tw-leading-none">keyboard_arrow_left</span>
          </div>
          <div class="tw-font-bold">{{ currentSeason }} Race Results</div>
          <div @click="nextSeason" :class="[`${currentSeason < getMaxSeason() ? '' : 'tw-opacity-50 tw-pointer-events-none'}`]"
               class="tw-cursor-pointer tw-transition-opacity tw-flex tw-items-center">
            <span class="material-symbols-outlined tw-text-2xl tw-font-light tw-leading-none">keyboard_arrow_right</span>
          </div>
        </div>
        <div class="tw-text-sm tw-mt-1">{{ getChartData.datasets[1].label }}</div>
      </div>
      <CustomChart class="tw-h-96" type="line" :data="getChartData" :options="chartOptions"/>
    </div>
  </div>
</template>

<style scoped>

</style>