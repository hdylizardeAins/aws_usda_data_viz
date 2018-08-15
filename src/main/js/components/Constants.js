const aPort = "8080";
const getAUrlPrefix = function(){
      return window.location.protocol + "//" + window.location.hostname + ":" + aPort + "/";
}
export default {
    apachePort: aPort,
    getApacheUrlPrefix: getAUrlPrefix
}