function get_orders_with_places(r) {
    r.subrequest('/orders')
    .then(response => get_places(JSON.parse(response.responseBody), r))
    .catch(error => r.return(error.stack));
}

function get_places(orders, r) {
    var promises = [];
    for (var i = 0; i < orders.length; i++) {
        orders[i].warehousePlaces = [];
        promises.push(r.subrequest('/location/' + orders[i].productName)
        .then(function(response) {
            var pallets = response.responseBody;
            for (pallet in pallets) {
                orders[i].warehousePlaces.push(pallet.storageLocation);
            }
        })
        );
    }
    Promise.all(promises).then(() => r.return(200, JSON.stringify(orders)));
}