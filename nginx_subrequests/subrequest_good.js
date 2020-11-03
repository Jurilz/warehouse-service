function get_orders_with_places(r) {

    r.subrequest('/orders', { method: 'GET'} , function(res) {
        if (res.status != 200) {
            r.return(res.uri);
            return;
        }
    })
    .then(reply => get_places(reply.responseBody))
    .then(reply => r.return(reply.status, reply.responseBody))

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
    r.return(200, orders);
    }
}

export default {get_orders_with_places};
