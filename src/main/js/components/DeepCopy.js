export default function deepCopy(obj) {
    var copy;

    if(typeof obj === "object") {
        if (obj === null) {
            // null => null
            copy = null;
        } else {
            switch (Object.prototype.toString.call(obj)) {
                case "[object Array]":
                    /*
                    It's an array, create a new array with
                    deep copies of the entries
                    */
                    copy = obj.map(deepCopy);
                    break;
                case "[object Date]":
                    // Clone the date
                    copy = new Date(obj);
                    break;
                case "[object RegExp]":
                    // Clone the RegExp
                    copy = new RegExp(obj);
                    break;
                // ...probably a few others
                default:
                    /*
                    Some other kind of object, deep-copy its
                    properties into a new object
                    */
                    copy = Object.keys(obj).reduce(function(reducedObject, key) {
                        reducedObject[key] = deepCopy(obj[key]);
                        return reducedObject;
                    }, {});
                    break;
            }
        }
    } else {
        // It's a primitive, copy via assignment
        copy = obj;
    }
    return copy;
}
