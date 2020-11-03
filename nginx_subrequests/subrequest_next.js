function get_orders_with_places(r) {
    r.subrequest('/orders')
    .then(response => get_places(JSON.parse(response.responseBody), r))
    .catch(error => r.return(500, error.name + " " + error.message + "\n" + error.stack));
}

function get_places(orders, r) {
    var promises = [];
    for (var i = 0; i < orders.length; i++) {
        orders[i].warehousePlaces = [];
        promises.push(r.subrequest('/location/' + orders[i].productName)
        .then(response => orders[i].warehousePlaces.push(response.responseBody)));
    }
    Promise.all(promises).then(() => r.return(200, JSON.stringify(orders)));
}