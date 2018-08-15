const aPort = "8080";
const getAUrlPrefix = function(){
      return window.location.protocol + "//" + window.location.hostname + ":" + aPort + "/";
}
const aDatasetFolder = "data/"
export default {
    apachePort: aPort,
    getApacheUrlPrefix: getAUrlPrefix,
    apacheDatasetFolder: aDatasetFolder
}