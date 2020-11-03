function get_orders_with_places(r) {
    r.subrequest('/orders')
    .then(response => new Promise(get_places(JSON.parse(response.responseBody))))
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