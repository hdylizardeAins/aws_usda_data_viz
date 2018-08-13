import Vue from 'vue';
import axios from 'axios';

var analyticsStore = {
    state: {
        analytics: [
            {
                name: "No Plot",
                description: "Does not perform any analysis of data.",
                columns: [],
                xVars: [],
                yVars: [],
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: true,
                multiSelectY: true,
                selected: false,
                imagePath: "" 
            },
            {
                name: "Plot",
                description: "A simple scatter plot.",
                //TODO: Load from back-end in the future
                columns: ["Year","USGECropPercentCorn","ValuePerAcre","OperatingCostsPerAcre","SeedCostsPerAcre"],
                xVars: [],
                yVars: [],
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: true,
                multiSelectY: false,
                selected: false,
                imagePath: ""
            },
            {
                name: "Regression",
                description: "A linear regression.",
                //TODO:  Load from back-end in the future
                columns: ["Year","USGECropPercentCorn","ValuePerAcre","OperatingCostsPerAcre","SeedCostsPerAcre"],
                xVars: [],
                yVars: [],
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false,
                imagePath: ""
            },
            {
                name: "Summary",
                description: "A summary of the data using a matrix.",
                columns: [],
                xVars: [],
                yVars: [],
                multiSelectX: true,
                multiSelectY: true,
                selected: false,
                imagePath: ""
            },
            {
                name: "TBD",
                description: "Currently unsupported.",
                unselectable: true,
                selected: false,
                imagePath: ""
            }
        ]
    },
    getters: {
        analytics: state => state.analytics,
        selectedAnalytics: state => state.analytics.filter( a => a.selected) || []
    },
    mutations: {
        updateAnalyticsSelection: function (state, analyticNames) {
            state.analytics.forEach(a => {
                a.selected = analyticNames.includes(a.name);
            });
        },
        updateAnalytic: function(state, newAnalytic) {
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
    },
    actions: {
        requestAnalyticExec: function(context, payload) {
            axios.get('/analytics/' + payload.analytic.name.toLowerCase(), {
                params: {
                    inputFile: payload.datasetFilePath
                }
            })
            .then(function (response) {
                payload.analytic.imagePath = response.data.outputFile;
                context.commit('updateAnalytic', payload.analytic);
                callback.success(config, response);
            })
            .catch(function (error) {
                console.log(error); //TODO: figure out error handling
            });
        }
    }
};

export default analyticsStore;