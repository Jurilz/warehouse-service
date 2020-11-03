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
    resolveAll(promises)
    .then(() => r.return(200, JSON.stringify(orders)))
    .catch(error => r.return(error.stack));
}

function resolveAll(promises) {
    return new Promise((resolve, reject) => {
        var n = promises.length;
        var rs = Array(n);
        var done = () => {
            if (--n === 0) {
                resolve(rs);
            }
        };
        promises.forEach((p, i) => {
            p.then((x) => { rs[i] = x; }, reject).then(done);
        });
    });
}

