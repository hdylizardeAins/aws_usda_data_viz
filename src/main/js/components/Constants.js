const aPort = "8080";
const getAUrlPrefix = function(){
      return window.location.protocol + "//" + window.location.hostname + ":" + aPort + "/";
}
const username = "Jane Doe";
export default {
    apachePort: aPort,
    getApacheUrlPrefix: getAUrlPrefix,
    username: username
}