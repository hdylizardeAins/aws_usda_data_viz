import Vue from 'vue';
import Vuex from 'vuex';
import DatasetsStore from './DatasetsStore.js';
import AnalyticsStore from './AnalyticsStore.js';
import ExecutionsStore from './ExecutionsStore.js';
import ChatStore from './ChatStore.js';
import SelectedImageStore from './SelectedImageStore.js';
import DoughnutStore from './DoughnutStore.js';
import BarChartStore from './BarChartStore.js';
import LineGraphStore from './LineGraphStore.js';

Vue.use(Vuex);

var store = new Vuex.Store({
   modules: {
       datasetsStore: DatasetsStore,
       analyticsStore: AnalyticsStore,
       executionsStore: ExecutionsStore,
       chatStore: ChatStore,
       selectedImageStore: SelectedImageStore,
       doughnutStore: DoughnutStore,
       barChartStore: BarChartStore,
       lineGraphStore: LineGraphStore
   }
});

export default store;