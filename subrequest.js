function get_places(r) {
    r.subrequest('/orders', { method: 'GET' }, function(res) {
        if (res.status != 200) {
            r.return(res.uri);
            return;
        }
        var orders = res.responseBody;
        for (var i = 0; i < orders.length; i++) {
            order[i].warehousePlaces = [];
            r.subrequest('/location/' + orders[i].productName, {method: 'GET'}, function(response) {
                if (response.status != 200) {
                    r.return(response.uri);
                    return;
                }
                order[i].warehousePlaces.push(response.responseBody);
            });
        }
        // var json = JSON.parse(res.responseBody);
        // var json = res.responseBody;
        r.return(200, orders);
    });
}