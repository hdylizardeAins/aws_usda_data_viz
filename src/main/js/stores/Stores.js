import Vue from 'vue';
import Vuex from 'vuex';
import DatasetsStore from './DatasetsStore.js';
import AnalyticsStore from './AnalyticsStore.js';
import ExecutionsStore from './ExecutionsStore.js';

Vue.use(Vuex);

var store = new Vuex.Store({
   modules: {
       datasetsStore: DatasetsStore,
       analyticsStore: AnalyticsStore,
       executionsStore: ExecutionsStore
   }
});

export default store;