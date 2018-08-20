import Vue from 'vue';
import Vuex from 'vuex';
import DatasetsStore from './DatasetsStore.js';
import AnalyticsStore from './AnalyticsStore.js';
import ExecutionsStore from './ExecutionsStore.js';
import ChatStore from './ChatStore.js';
import SelectedImageStore from './SelectedImageStore.js';

Vue.use(Vuex);

var store = new Vuex.Store({
   modules: {
       datasetsStore: DatasetsStore,
       analyticsStore: AnalyticsStore,
       executionsStore: ExecutionsStore,
       chatStore: ChatStore,
       selectedImageStore: SelectedImageStore
   }
});

export default store;