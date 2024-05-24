$(document).ready(function () {
    // Función para cargar clientes
    function loadClients() {
        fetch("http://localhost:8080/clients")
            .then(response => response.json())
            .then(clients => {
                $('#clientSelect').empty();
                $('#clientSelect').append(`<option value="" selected disabled>Select a Client</option>`);
                clients.forEach(client => {
                    $('#clientSelect').append(`
                        <option value="${client.id}">${client.name}</option>
                    `);
                });
            })
            .catch(error => {
                console.error('Error fetching clients:', error);
                alert('Failed to fetch clients. Please try again later.');
            });
    }

    // Cargar clientes al cargar la página
    loadClients();

    // Manejar el clic en el botón "Display Orders"
    $('#displayOrdersBtn').click(function () {
        const clientId = $('#clientSelect').val();
        if (!clientId) {
            alert('Please select a client.');
            return;
        }

        // Limpiar el contenedor de la tabla de pedidos
        $('#orderTableContainer').empty();

        // Obtener los pedidos del cliente seleccionado
        fetch(`http://localhost:8080/orders?clientId=${clientId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(orders => {

                orders.forEach(order => {
                    // Crear una nueva tabla para cada pedido
                    const table = `
                        <div class="container mt-5">
                            <h2>Order ID: ${order.id}</h2>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Brand</th>
                                        <th>Quantity</th>
                                        <th>Price (€)</th>
                                    </tr>
                                </thead>
                                <tbody id="orderTableBody_${order.id}">
                                    <!-- Order details will be loaded here -->
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <th colspan="3">Total:</th>
                                        <th>${order.total.toFixed(2)} €</th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    `;

                    // Agregar la tabla al contenedor
                    $('#orderTableContainer').append(table);

                    // Llenar la tabla con los detalles del pedido
                    order.items.forEach(item => {
                        $(`#orderTableBody_${order.id}`).append(`
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.brand}</td>
                                <td>${item.quantity}</td>
                                <td>${(item.price).toFixed(2)} €</td>
                            </tr>
                        `);
                    });
                });
            })
            .catch(error => {
                console.error('Error fetching orders:', error);
                alert('Failed to fetch orders for the selected client.');
            });
    });
});


