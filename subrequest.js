function get_orders_with_places(r) {
    r.subrequest('/orders', { method: 'GET' } , 
    function(res) {
        if (res.status != 200) {
            r.return(res.uri);
            return;
        }
        return res;
    })
    .then(response => get_places(response.responseBody))
    .then(reply => r.return(reply.status, reply.responseBody))
    .catch(_ => r.return(500));
}

function get_places(orders) {
    for (var i = 0; i < orders.length; i++) {
        orders[i].warehousePlaces = [];
        r.subrequest('/location/' + orders[i].productName, {method: 'GET'}, function(response) {
            if (response.status != 200) {
                r.return(response.uri);
                return;
            }
            orders[i].warehousePlaces.push(response.responseBody);
        });
    }
    return orders;
}
