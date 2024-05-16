// Función para obtener todos los productos
function getAllProducts() {
    fetch('http://localhost:8080/products')
        .then(response => response.json())
        .then(products => {
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

            document.getElementById('productsBody').innerHTML = productsHtml;
        })
        .catch(error => {
            console.error('Error fetching products:', error);
            document.getElementById('productsBody').innerHTML = `<tr><td colspan="5" class="text-center">Error fetching products. Please try again later.</td></tr>`;
        });
}

// Función para crear un nuevo producto
function createProduct(event) {
    event.preventDefault();

    const name = document.getElementById('productName').value;
    const brand = document.getElementById('productBrand').value;
    const price = document.getElementById('productPrice').value;

    fetch('http://localhost:8080/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, brand, price })
    })
        .then(response => {
            if (response.ok) {
                getAllProducts();
                document.getElementById('productName').value = '';
                document.getElementById('productBrand').value = '';
                document.getElementById('productPrice').value = '';
            } else {
                throw new Error('Failed to create product');
            }
        })
        .catch(error => {
            console.error('Error creating product:', error);
            alert('Failed to create product. Please try again.');
        });
}

// Función para editar un producto
function editProduct(id, name, brand, price) {
    const newName = prompt('Enter new name', name);
    const newBrand = prompt('Enter new brand', brand);
    const newPrice = prompt('Enter new price', price);
    if (newName && newBrand && newPrice) {
        fetch(`http://localhost:8080/products/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: newName, brand: newBrand, price: newPrice })
        })
            .then(response => {
                if (response.ok) {
                    getAllProducts();
                } else {
                    return response.json().then(error => { throw new Error(error.message); });
                }
            })
            .catch(error => {
                console.error('Error editing product:', error);
                alert('Failed to edit product. Please try again.');
            });
    }
}

// Función para eliminar un producto
function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        fetch(`http://localhost:8080/products/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    getAllProducts();
                } else {
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





