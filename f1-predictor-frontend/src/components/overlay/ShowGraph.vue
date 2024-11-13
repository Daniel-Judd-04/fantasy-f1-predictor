<script>
import {mapGetters} from "vuex";
import CloseButton from "@/components/common/CloseButton.vue";
import {getConstructor} from "@/utils/common";
import {Chart as ChartJS} from "chart.js";

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
      let pointsPerRace = [12, 25, 0, 8, 12, 26, 13, 0, 2]; // Example data
      let cumulativePoints = [];
      for (let i = 0; i <= this.allGrandsPrix.length; i++) {
        labels.push(`Race ${i + 1}`);
        // pointsPerRace.push(this.overlayObject.results.)
      }

      for (let i = 0; i < pointsPerRace.length; i++) {
        if (i === 0) {
          cumulativePoints.push(pointsPerRace[i]);
          continue;
        }
        cumulativePoints.push(cumulativePoints[i - 1] + pointsPerRace[i]);
      }


      const primaryLight = getTailwindColor('--color-primary-light');

      return {
        labels: labels,
        datasets: [
          {
            label: 'Points Per Race',
            fill: false,
            borderColor: primaryLight,
            borderWidth: 1,
            yAxisID: 'y',
            tension: 0.4,
            data: pointsPerRace
          },
          {
            label: 'Cumulative Points',
            fill: true,
            borderColor: primaryLight,
            borderWidth: 2,
            yAxisID: 'y1',
            tension: 0.4,
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
          },
          customLegend: {
            position: 'left', // Custom plugin handles legend placement
          },
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
        },
        layout: {
          padding: {
            top: 30,
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
  mounted() {
    const white = getTailwindColor('--color-f1-white');
    // Custom plugin definition
    const customLegendPlugin = {
      id: 'customLegend',
      beforeDraw(chart) {
        const {ctx} = chart;
        const datasets = chart.data.datasets;

        // Left axis (y)
        const yLegend = datasets.find(dataset => dataset.yAxisID === 'y');
        if (yLegend) {
          ctx.save();
          ctx.font = '12px Arial';
          ctx.fillStyle = white;
          ctx.fillText(yLegend.label, 0, chart.chartArea.top - 20);
          ctx.restore();
        }

        // Right axis (y1)
        const y1Legend = datasets.find(dataset => dataset.yAxisID === 'y1');
        if (y1Legend) {
          ctx.save();
          ctx.font = '12px Arial';
          ctx.fillStyle = white;
          ctx.fillText(y1Legend.label, chart.width - 100, chart.chartArea.top - 20);
          ctx.restore();
        }
      },
    };

    // Register the plugin locally for this chart instance
    ChartJS.register(customLegendPlugin);
  },
  methods: {
    exit() {
      this.$emit('exit');
    }
  }
}
</script>

<template>
  <div :class="[`tw-to-${getGradientColour}`]"
       class="tw-bg-primary-dark tw-w-1/2 tw-border-2 tw-border-primary-light tw-bg-gradient-to-bl tw-to-200% tw-from-primary-dark tw-drop-shadow-2xl tw-rounded-lg tw-flex tw-flex-col tw-text-f1-white">
    <div class="hover-parent tw-p-2 tw-flex tw-justify-center tw-items-center tw-gap-2">
      <div class="tw-text-2xl tw-font-bold tw-pl-2 tw-pt-0.5 tw-mr-auto">
        {{ overlayObject.fullName }}
      </div>
      <CloseButton class="hover-child tw-transition-opacity" @close="exit">
        <span class="material-icons">close</span>
      </CloseButton>
    </div>
    <div class="tw-bg-primary-dark tw-p-4 tw-rounded-b-lg">
      <CustomChart class="tw-h-96" type="line" :data="getChartData" :options="chartOptions"/>
    </div>
  </div>
</template>

<style scoped>

</style>