function get_orders_with_places(r) {
    r.subrequest('/orders')
    .then(response => get_places(JSON.parse(response.responseBody), r))
    .catch(error => r.return(error.stack));
}

function get_places(orders, r) {
    if (orders.length === 0) {
        // return new Promise.resolve([]);
        r.return(200, JSON.stringify([]));
    }
    var on_loaction = (order, response) => {
        var pallets = JSON.parse(response.responseBody);
        order.warehousePlaces = pallets.map((x) => x.storageLocation);
    }
    return resolveAll(
        orders.map((x) => {
            x.warehousePlaces = [];
            return r.subrequest('/location/' + x.productName)
            .then(on_loaction.bind(null, x));
        })
    ).then(() => r.return(200, JSON.stringify(orders)));
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
            p.then((x) => { rs[i] = x; }, reject).then(done).catch(error => r.return(error.stack));
        });
    });
}

