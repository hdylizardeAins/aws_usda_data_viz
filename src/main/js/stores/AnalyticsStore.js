import Vue from 'vue';
import axios from 'axios';

var analyticsStore = {
    state: {
        analytics: [
            {
                displayName: "Scatter Plot Matrix",
                name: "Plot",
                category: "I want to see the data organized",
                description:"This visualization uses the R scatter plot function to plot two-dimensional data, as well as matrices of plots for visualizing more dimensions. It can be used to quickly find related features.",
                link: "https://www.rdocumentation.org/packages/graphics/versions/3.5.1/topics/plot",
                requiresXY: false,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: true,
                multiSelectY: false,
                selected: false
            },
            {
                displayName: "Box Plot",
                name: "Boxplot",
                category: "I want to see the data organized",
                description: "This visualization shows the minimum, maximum, median, and quartiles of a variable for different categories.",
                link: "https://www.rdocumentation.org/packages/graphics/versions/3.5.1/topics/boxplot",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                // TODO: update description and link
                displayName: "Pie Chart",
                name: "Piechart",
                category: "I want to see the data organized",
                description: "This visualization shows the minimum, maximum, median, and quartiles of a variable for different categories.",
                link: "https://www.rdocumentation.org/packages/graphics/versions/3.5.1/topics/boxplot",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false,
                unselectable: true,
                disabled: true
            },
            {
                displayName: "Linear Regression",
                name: "Regression",
                category: "I want to see predictions",
                description:"This analytic creates a linear regression, helpful for predicting future trends and relationships between variables.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.5.1/topics/lm",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                // TODO: update description and link
                displayName: "Machine Learning Regression",
                name: "machinelearningregression",
                category: "I want to see predictions",
                description:"This analytic creates a linear regression, helpful for predicting future trends and relationships between variables.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.5.1/topics/lm",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                unselectable: true,
                selected: false,
                disabled: true
            },
            {
                // TODO: update description and link
                displayName: "Classification",
                name: "classification",
                category: "I want to see predictions",
                description:"This analytic creates a linear regression, helpful for predicting future trends and relationships between variables.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.5.1/topics/lm",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                unselectable: true,
                selected: false,
                disabled: true
            },
            {
                // TODO: update description and link
                displayName: "Hypothesis Testing",
                name: "hypothesistesting",
                category: "I want to see predictions",
                description:"This analytic creates a linear regression, helpful for predicting future trends and relationships between variables.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.5.1/topics/lm",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                unselectable: true,
                selected: false,
                disabled: true
            },
            {
                displayName: "Trend-Line",
                name: "Trend-Line",
                category: "I want to see trends in this data",
                description: "This visualization shows a trend-line on a scatter plot, which shows the average of a trend over time.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.4.3/topics/scatter.smooth",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                displayName: "Correlation Heat Map",
                name: "Correlation",
                category: "I want to see trends in this data",
                description:"This visualization shows how well pairs of variables are correlated using a heat map.",
                link: "https://cran.r-project.org/web/packages/ggcorrplot/index.html",
                requiresXY: false,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                // TODO: update description and link
                displayName: "Clustering",
                name: "clustering",
                category: "I want to see trends in this data",
                description:"This visualization shows how well pairs of variables are correlated using a heat map.",
                link: "https://cran.r-project.org/web/packages/ggcorrplot/index.html",
                requiresXY: false,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                unselectable: true,
                selected: false,
                disabled: true
            },
            {
                displayName: "Custom R Script",
                name: "TBD",
                category: "I need a new type of insight", //TODO: this had <strong> tags around insight, but that was just rendered as plain text
                unselectable: true,
                selected: false,
                disabled: true
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