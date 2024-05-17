// Función para obtener todos los productos
function getAllProducts() {
    fetch('http://localhost:8080/products') // Realiza una solicitud GET para obtener todos los productos desde el servidor
        .then(response => response.json()) // Convierte la respuesta a formato JSON
        .then(products => { // Procesa los productos obtenidos
            // Genera el HTML para mostrar los productos en una tabla
            const productsHtml = products.map(product => `
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td>${product.brand}</td>
                            <td>${product.price}</td>
                            <td>
                                <button class="btn btn-sm btn-primary" onclick="editProduct(${product.id}, '${product.name}', '${product.brand}', ${product.price})">Edit</button>
                                <button class="btn btn-sm btn-danger" onclick="deleteProduct(${product.id})">Delete</button>
                            </td>
                        </tr>
                    `).join('');

            // Actualiza el cuerpo de la tabla en el HTML con los productos
            document.getElementById('productsBody').innerHTML = productsHtml;
        })
        .catch(error => {
            console.error('Error fetching products:', error); // Muestra un mensaje de error si falla la solicitud
            document.getElementById('productsBody').innerHTML = `<tr><td colspan="5" class="text-center">Error fetching products. Please try again later.</td></tr>`;
        });
}

// Función para crear un nuevo producto
function createProduct(event) {
    event.preventDefault(); // Previene el envío del formulario por defecto

    const name = document.getElementById('productName').value; // Obtiene el nombre del producto desde el formulario
    const brand = document.getElementById('productBrand').value; // Obtiene la marca del producto desde el formulario
    const price = document.getElementById('productPrice').value; // Obtiene el precio del producto desde el formulario

    // Realiza una solicitud POST para crear un nuevo producto en el servidor
    fetch('http://localhost:8080/products', {
        method: 'POST', // Utiliza el método POST
        headers: {
            'Content-Type': 'application/json' // Especifica el tipo de contenido como JSON
        },
        body: JSON.stringify({ name, brand, price }) // Convierte los datos del producto a formato JSON y los envía en el cuerpo de la solicitud
    })
        .then(response => {
            if (response.ok) { // Verifica si la respuesta indica que la solicitud fue exitosa
                getAllProducts(); // Obtiene nuevamente todos los productos después de crear uno nuevo
                document.getElementById('productName').value = ''; // Limpia el campo de nombre del producto en el formulario
                document.getElementById('productBrand').value = ''; // Limpia el campo de marca del producto en el formulario
                document.getElementById('productPrice').value = ''; // Limpia el campo de precio del producto en el formulario
            } else {
                throw new Error('Failed to create product'); // Lanza un error si la solicitud falla
            }
        })
        .catch(error => {
            console.error('Error creating product:', error); // Muestra un mensaje de error si falla la solicitud
            alert('Failed to create product. Please try again.'); // Muestra una alerta al usuario
        });
}

// Función para editar un producto
function editProduct(id, name, brand, price) {
    const newName = prompt('Enter new name', name); // Solicita al usuario el nuevo nombre del producto
    const newBrand = prompt('Enter new brand', brand); // Solicita al usuario la nueva marca del producto
    const newPrice = prompt('Enter new price', price); // Solicita al usuario el nuevo precio del producto
    if (newName && newBrand && newPrice) { // Verifica si se proporcionaron valores válidos para el nombre, la marca y el precio
        // Realiza una solicitud PUT para actualizar el producto en el servidor
        fetch(`http://localhost:8080/products/${id}`, {
            method: 'PUT', // Utiliza el método PUT
            headers: {
                'Content-Type': 'application/json' // Especifica el tipo de contenido como JSON
            },
            body: JSON.stringify({ name: newName, brand: newBrand, price: newPrice }) // Convierte los nuevos datos del producto a formato JSON y los envía en el cuerpo de la solicitud
        })
            .then(response => {
                if (response.ok) { // Verifica si la respuesta indica que la solicitud fue exitosa
                    getAllProducts(); // Obtiene nuevamente todos los productos después de editar uno
                } else {
                    return response.json().then(error => { throw new Error(error.message); }); // Lanza un error si la solicitud falla y devuelve un mensaje de error desde el servidor
                }
            })
            .catch(error => {
                console.error('Error editing product:', error); // Muestra un mensaje de error si falla la solicitud
                alert('Failed to edit product. Please try again.'); // Muestra una alerta al usuario
            });
    }
}

// Función para eliminar un producto
function deleteProduct(id) {
    // Solicita confirmación al usuario antes de eliminar el producto
    if (confirm('Are you sure you want to delete this product?')) {
        // Realiza una solicitud DELETE para eliminar el producto del servidor
        fetch(`http://localhost:8080/products/${id}`, {
            // Utiliza el método DELETE
            method: 'DELETE'
        })
            .then(response => {
                // Verifica si la respuesta indica que la solicitud fue exitosa
                if (response.ok) {
                    // Obtiene nuevamente todos los productos después de eliminar uno
                    getAllProducts();
                } else {
                    // Sino retorna error
                    return response.json().then(error => { throw new Error(error.message); });
                }
            })
            .catch(error => {
                console.error('Error deleting product:', error);
                alert('Failed to delete product. Please try again.');
            });
    }
}

// Llamar a la función para obtener todos los productos cuando la página se carga
getAllProducts();

// Agregar un evento para manejar el envío del formulario de creación de producto
document.getElementById('createProductForm').addEventListener('submit', createProduct);





