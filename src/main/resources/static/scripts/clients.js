// Función para obtener todos los clientes
function getAllClients() {
    fetch('http://localhost:8080/clients') // Realiza una solicitud GET para obtener todos los clientes desde el servidor
        .then(response => response.json()) // Convierte la respuesta a formato JSON
        .then(clients => { // Procesa los clientes obtenidos
            // Genera el HTML para mostrar los clientes en una tabla
            const clientsHtml = clients.map(client => `
                <tr>
                    <td>${client.id}</td>
                    <td>${client.name}</td>
                    <td>${client.address}</td>
                    <td>${client.age}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="editClient(${client.id}, '${client.name}', '${client.address}', ${client.age})">Edit</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteClient(${client.id})">Delete</button>
                    </td>
                </tr>
            `).join('');

            // Actualiza el cuerpo de la tabla en el HTML con los clientes
            document.getElementById('clientsBody').innerHTML = clientsHtml;
        })
        .catch(error => {
            console.error('Error fetching clients:', error); // Muestra un mensaje de error si falla la solicitud
            document.getElementById('clientsBody').innerHTML = `<tr><td colspan="5" class="text-center">Error fetching clients. Please try again later.</td></tr>`;
        });
}

// Función para crear un nuevo cliente
function createClient(event) {
    event.preventDefault(); // Previene el envío del formulario por defecto

    const name = document.getElementById('clientName').value; // Obtiene el nombre del cliente desde el formulario
    const address = document.getElementById('clientAddress').value; // Obtiene la dirección del cliente desde el formulario
    const age = document.getElementById('clientAge').value; // Obtiene la edad del cliente desde el formulario

    // Realiza una solicitud POST para crear un nuevo cliente en el servidor
    fetch('http://localhost:8080/clients', {
        method: 'POST', // Utiliza el método POST
        headers: {
            'Content-Type': 'application/json' // Especifica el tipo de contenido como JSON
        },
        body: JSON.stringify({ name, address, age }) // Convierte los datos del cliente a formato JSON y los envía en el cuerpo de la solicitud
    })
    .then(response => {
        if (response.ok) { // Verifica si la respuesta indica que la solicitud fue exitosa
            getAllClients(); // Obtiene nuevamente todos los clientes después de crear uno nuevo
            document.getElementById('clientName').value = ''; // Limpia el campo de nombre del cliente en el formulario
            document.getElementById('clientAddress').value = ''; // Limpia el campo de dirección del cliente en el formulario
            document.getElementById('clientAge').value = ''; // Limpia el campo de edad del cliente en el formulario
        } else {
            throw new Error('Failed to create client'); // Lanza un error si la solicitud falla
        }
    })
    .catch(error => {
        console.error('Error creating client:', error); // Muestra un mensaje de error si falla la solicitud
        alert('Failed to create client. Please try again.'); // Muestra una alerta al cliente
    });
}

// Función para editar un cliente
function editClient(id, name, address, age) {
    const newName = prompt('Enter new name', name); // Solicita al usuario el nuevo nombre del cliente
    const newAddress = prompt('Enter new address', address); // Solicita al usuario la nueva dirección del cliente
    const newAge = prompt('Enter new age', age); // Solicita al usuario la nueva edad del cliente
    if (newName && newAddress && newAge) { // Verifica si se proporcionaron valores válidos para el nombre, la dirección y la edad
        // Realiza una solicitud PUT para actualizar el cliente en el servidor
        fetch(`http://localhost:8080/clients/${id}`, {
            method: 'PUT', // Utiliza el método PUT
            headers: {
                'Content-Type': 'application/json' // Especifica el tipo de contenido como JSON
            },
            body: JSON.stringify({ name: newName, address: newAddress, age: newAge }) // Convierte los nuevos datos del cliente a formato JSON y los envía en el cuerpo de la solicitud
        })
        .then(response => {
            if (response.ok) { // Verifica si la respuesta indica que la solicitud fue exitosa
                getAllClients(); // Obtiene nuevamente todos los clientes después de editar uno
            } else {
                return response.json().then(error => { throw new Error(error.message); }); // Lanza un error si la solicitud falla y devuelve un mensaje de error desde el servidor
            }
        })
        .catch(error => {
            console.error('Error editing client:', error); // Muestra un mensaje de error si falla la solicitud
            alert('Failed to edit client. Please try again.'); // Muestra una alerta al usuario
        });
    }
}

// Función para eliminar un cliente
function deleteClient(id) {
    // Solicita confirmación al usuario antes de eliminar el cliente
    if (confirm('Are you sure you want to delete this client?')) {
        // Realiza una solicitud DELETE para eliminar el cliente del servidor
        fetch(`http://localhost:8080/clients/${id}`, {
            method: 'DELETE' // Utiliza el método DELETE
        })
        .then(response => {
            if (response.ok) { // Verifica si la respuesta indica que la solicitud fue exitosa
                getAllClients(); // Obtiene nuevamente todos los clientes después de eliminar uno
            } else {
                return response.json().then(error => { throw new Error(error.message); }); // Lanza un error si la solicitud falla y devuelve un mensaje de error desde el servidor
            }
        })
        .catch(error => {
            console.error('Error deleting client:', error); // Muestra un mensaje de error si falla la solicitud
            alert('Failed to delete client. Please try again.'); // Muestra una alerta al usuario
        });
    }
}

// Llamar a la función para obtener todos los clientes cuando la página se carga
getAllClients();

// Agregar un evento para manejar el envío del formulario de creación de cliente
document.getElementById('createClientForm').addEventListener('submit', createClient);