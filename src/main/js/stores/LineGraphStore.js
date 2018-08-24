import Vue from 'vue';
import axios from 'axios';

var lineGraphStore = {
    state: {
        data: {
            labels: [1945,
                1949,
                1954,
                1959,
                1964,
                1969,
                1974,
                1978,
                1982,
                1987,
                1992,
                1997,
                2002,
                2007,
                2012
                ],                
            datasets: [
                {
                    label: 'Cropland',
                    borderColor: '#41B883',
                    data: [450693,
                        477838,
                        465327,
                        457483,
                        443801,
                        471707,
                        464702,
                        470480,
                        468888,
                        463580,
                        459654,
                        454691,
                        441273,
                        407908,
                        391524
                        
                        ]
                },
                {
                    label: 'Forest use land',
                    borderColor: '#E46651',
                    data: [601717,
                        605570,
                        615384,
                        610863,
                        611823,
                        602768,
                        598475,
                        583085,
                        567168,
                        557828,
                        558545,
                        552066,
                        559135,
                        576037,
                        538566
                        ]
                },
                {
                    label: 'Special use land',
                    borderColor: '#00D8FF',
                    data: [  85019,
                        87050,
                        91637,
                        97282,
                        115306,
                        112269,
                        113409,
                        123005,
                        127279,
                        135273,
                        136388,
                        141759,
                        152776,
                        169300,
                        168679
                        
                     ]
                },
                {
                    label: 'Urban land',
                    borderColor: 'blue',
                    data: [  15012,
                        18283,
                        18561,
                        27121,
                        29162,
                        30840,
                        34556,
                        44218,
                        49649,
                        55908,
                        57960,
                        64292,
                        59193,
                        60167,
                        69441
                        
                     ]
                }
            ]
        }
    },
    mutations: {
        updateLineGraphData: function(state, newData){
            Vue.set(state, "data", newData);
        }
    }
};

export default lineGraphStore;