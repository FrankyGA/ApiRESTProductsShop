$(document).ready(function () {
    let productsList = [];

    // Función para cargar productos
    function loadProducts() {
        fetch("http://localhost:8080/products")
            .then(response => response.json())
            .then(products => {
                productsList = products;
                $('#products').empty();
                products.forEach(product => {
                    $('#products').append(`
                        <div class="col-sm-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text">Brand: ${product.brand}</p>
                                    <p class="card-text">Price: $${product.price.toFixed(2)}</p>
                                    <input type="number" class="form-control quantity" data-product-id="${product.id}" placeholder="Quantity">
                                </div>
                            </div>
                        </div>
                    `);
                });
            })
            .catch(error => {
                console.error('Error fetching products:', error);
                alert('Failed to fetch products. Please try again later.');
            });
    }

    // Cargar productos al cargar la página
    loadProducts();

    // Manejar el clic en el botón de envío de orden
    $('#submitOrderBtn').click(function () {
        const orderItems = [];
        let total = 0;
        document.querySelectorAll('.quantity').forEach(quantityInput => {
            const productId = quantityInput.dataset.productId;
            const quantity = parseInt(quantityInput.value);
            if (quantity > 0) {
                const product = productsList.find(p => p.id == productId);
                const price = product.price;
                const itemTotal = price * quantity;
                total += itemTotal;
                orderItems.push({
                    productId,
                    quantity,
                    price,
                    itemTotal,
                    name: product.name,
                    brand: product.brand
                });
            }
        });

        // Mostrar resumen del pedido
        $('#orderSummary').empty();
        orderItems.forEach(item => {
            $('#orderSummary').append(`
                <p>${item.name} (${item.brand}) - ${item.quantity} x $${item.price.toFixed(2)} = $${item.itemTotal.toFixed(2)}</p>
            `);
        });
        $('#orderSummary').append(`<h4>Total: $${total.toFixed(2)}</h4>`);
        $('#orderSummarySection').show();
    });

    // Manejar el clic en el botón de reinicio de la página
    $('#resetPageBtn').click(function () {
        const orderItems = [];
        let total = 0;
        document.querySelectorAll('.quantity').forEach(quantityInput => {
            const productId = quantityInput.dataset.productId;
            const quantity = parseInt(quantityInput.value);
            if (quantity > 0) {
                const product = productsList.find(p => p.id == productId);
                const price = product.price;
                const itemTotal = price * quantity;
                total += itemTotal;
                orderItems.push({
                    productId,
                    quantity,
                    price,
                    itemTotal
                });
            }
        });

        const orderDTO = {
            items: orderItems,
            total: total
        };

        // Enviar la solicitud POST al servidor para guardar el pedido
        fetch("http://localhost:8080/orders", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderDTO)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Order saved successfully');
                } else {
                    throw new Error('Failed to save order');
                }
            })
            .catch(error => {
                console.error('Error saving order:', error);
                alert('Failed to save order. Please try again.');
            })
            .finally(() => {
                // Recargar la página
                location.reload();
            });
    });
});





