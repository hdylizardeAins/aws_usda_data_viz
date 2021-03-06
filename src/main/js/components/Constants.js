const aPort = "8080";
const getAUrlPrefix = function(){
      return window.location.protocol + "//" + window.location.hostname + ":" + aPort + "/";
}

const socialDirResource = "social/";

const username = "Jane Doe";

//nav tab indices
const discussIdx = "1";
const analyzeIdx = "2";
const visualizeIdx = "3";

//Default topics
const topics = ["Corn Trends"];

export default {
    apachePort: aPort,
    getApacheUrlPrefix: getAUrlPrefix,
    socialDirResource: socialDirResource,
    username: username,
    navIndices: {
        discuss: discussIdx,
        analyze: analyzeIdx,
        visualize: visualizeIdx
    },
    topics: topics
}