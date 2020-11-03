function get_orders_with_places(r) {
    r.subrequest('/orders')
    .then(response => get_places(JSON.parse(response.responseBody), r))
    .then(orders => r.return(200, JSON.stringify(orders)))
    .catch(_ => r.return(500));
}

function get_places(orders, r) {
    var promises = [];
    for (var i = 0; i < orders.length; i++) {
        orders[i].warehousePlaces = [];
        r.subrequest('/location/' + orders[i].productName)
        .then(response => orders[i].warehousePlaces.push(response.responseBody));
    }
    return orders;
}