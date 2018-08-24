import Vue from 'vue';
import axios from 'axios';

var barChartStore = {
    state: {
        data: {
            labels: [2000,
                2001,
                2002,
                2003,
                2004,
                2005,
                2006,
                2007,
                2008,
                2009,
                2010,
                2011,
                2012,
                2013,
                2014,
                2015,
                2016,
                2017],                
            datasets: [
                {
                    label: 'Value per Acre',
                    backgroundColor: '#41B883',
                    data: [246.67,
                        266.92,
                        312.82,
                        319.62,
                        362.35,
                        260.43,
                        351.87,
                        468.94,
                        629.36,
                        561.22,
                        689.39,
                        837.77,
                        802.55,
                        720.51,
                        603.18,
                        612.6,
                        620.23,
                        621.49
                        ]
                },
                {
                    label: 'Operating Costs per Acre',
                    backgroundColor: '#E46651',
                    data: [164.99, 
                        162.3,  
                        145.48, 
                        161.16, 
                        175.94, 
                        186.37, 
                        205.98, 
                        228.99, 
                        295.69, 
                        295.01, 
                        286.41, 
                        332.33, 
                        349.59, 
                        355.6,  
                        356.92, 
                        333.8,  
                        345.9,  
                        337.07,
                        ]
                },
                {
                    label: 'Seed Costs per Acre',
                    backgroundColor: '#00D8FF',
                    data: [  30.02,
                        32.34,
                       31.84,
                       34.83,
                       36.82,
                      40.47,
                      43.55,
                      49.04,
                      60.02,
                      78.92,
                      81.58,
                      84.37,
                      92.04,
                     97.59,
                      101.04,
                     101.62,
                     100.10,
                      98.84
                     ]
                }
            ]
        }
    }
};

export default barChartStore;