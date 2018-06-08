<script type='text/javascript' src='http://www.bing.com/api/maps/mapcontrol?callback=GetMap' async defer></script>
<script type='text/javascript'>
    function GetMap() {
        var map = new Microsoft.Maps.Map('#myMap', {
            credentials: 'Ag_HTZ0dzutQBT9DkiB79tTAqIhQoANivypnL6tm9ctbGq7TRM44H8o-_1rHQh9O',
            mapTypeId: Microsoft.Maps.MapTypeId.aerial,
            showLocateMeButton: false,
            showMapTypeSelector: false
        });


        var paths = httpGet('http://www.hey-bro.com/kml/polylines.json')
        var obj = JSON.parse(paths)
        for (i = 0; i < obj['polylines'].length; i++) {

            var p = decode(obj['polylines'][i])
            //Create a polyline
            var line = new Microsoft.Maps.Polyline(p, {
                strokeColor: 'blue',
                strokeThickness: 2
            });

            //Add the polyline to map
            map.entities.push(line);
        }
    }

    function httpGet(theUrl) {
        var xmlHttp = null;

        xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", theUrl, false);
        xmlHttp.send(null);
        return xmlHttp.responseText;
    }

    // source: http://doublespringlabs.blogspot.com.br/2012/11/decoding-polylines-from-google-maps.html
    function decode(encoded) {

        // array that holds the points

        var points = []
        var index = 0,
            len = encoded.length;
        var lat = 0,
            lng = 0;
        while (index < len) {
            var b, shift = 0,
                result = 0;
            do {

                //finds ascii                                                                                    
                //and substract it by 63
                b = encoded.charAt(index++).charCodeAt(0) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);


            var dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++).charCodeAt(0) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            var dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            points.push({
                latitude: (lat / 1E5),
                longitude: (lng / 1E5)
            })

        }
        return points
    }
</script>