import Vue from 'vue';
import axios from 'axios';

var analyticsStore = {
    state: {
        analytics: [
            {
                name: "Plot",
                description: "A simple scatter plot.",
                requiresXY: false,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: true,
                multiSelectY: false,
                selected: false
            },
            {
                name: "Regression",
                description: "A linear regression.",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                name: "Summary",
                description: "A summary of the data using a matrix.",
                requiresXY: false,
                multiSelectX: true,
                multiSelectY: true,
                selected: false
            },
            {
                name: "TBD",
                description: "Currently unsupported.",
                unselectable: true,
                selected: false
            }
        ]
    },
    getters: {
        analytics: state => state.analytics,
        selectedAnalytics: state => state.analytics.filter(a => a.selected) || []
    },
    mutations: {
        /**
         * Updates the "selected" status of all the analytics
         */
        updateAnalyticsSelection: function (state, analyticNames) {
            state.analytics.forEach(a => {
                a.selected = analyticNames.includes(a.name);
            });
        },
        updateAnalytic: function (state, newAnalytic) {
            let existAnalyticIndex = state.analytics.findIndex((item) => {
                return item.name === newAnalytic.name;
            });

            // Copy fields that exist in the new analytic and the existing one
            if (existAnalyticIndex >= 0) {
                for (let key in newAnalytic) {
                    if (state.analytics[existAnalyticIndex].hasOwnProperty(key)) {
                        Vue.set(state.analytics[existAnalyticIndex], key, newAnalytic[key]);
                    }
                }
            }
        }
    }
};

export default analyticsStore;