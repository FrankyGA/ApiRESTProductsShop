// Función para obtener todos los usuarios
function getAllUsers() {
    fetch('http://localhost:8080/users') // Hacer una petición GET a la ruta http://localhost:8080/users
        .then(response => response.json()) // Convertir la respuesta a JSON
        .then(users => {
            // Generar el HTML para mostrar los usuarios en una tabla
            const usersHtml = users.map(user => `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>
                                <button class="btn btn-sm btn-primary" onclick="editUser(${user.id}, '${user.name}')">Edit</button>
                                <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                            </td>
                        </tr>
                    `).join('');

            // Mostrar los usuarios en la tabla
            document.getElementById('usersBody').innerHTML = usersHtml;
        })
        .catch(error => {
            console.error('Error fetching users:', error);
            // Mostrar un mensaje de error si ocurre un error al obtener los usuarios
            document.getElementById('usersBody').innerHTML = `<tr><td colspan="3" class="text-center">Error fetching users. Please try again later.</td></tr>`;
        });
}

// Función para crear un nuevo usuario
function createUser(event) {
    event.preventDefault(); // Prevenir el envío del formulario por defecto

    const name = document.getElementById('name').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, password })
    })
        .then(response => {
            if (response.ok) {
                // Actualizar la lista de usuarios después de crear uno nuevo
                getAllUsers();
                // Limpiar los campos del formulario después de crear un nuevo usuario
                document.getElementById('name').value = '';
                document.getElementById('password').value = '';
            } else {
                throw new Error('Failed to create user');
            }
        })
        .catch(error => {
            console.error('Error creating user:', error);
            alert('Failed to create user. Please try again.');
        });
}

// Función para editar un usuario
function editUser(id, name) {
    const newName = prompt('Enter new name', name);
    const newPassword = prompt('Enter new password', '');
    if (newName && newPassword) {
        fetch(`http://localhost:8080/users/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: newName, password: newPassword })
        })
            .then(response => {
                if (response.ok) {
                    getAllUsers();
                } else {
                    return response.json().then(error => { throw new Error(error.message); });
                }
            })
            .catch(error => {
                console.error('Error editing user:', error);
                alert('Failed to edit user. Please try again.');
            });
    }
}

// Función para eliminar un usuario
function deleteUser(id) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`http://localhost:8080/users/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    getAllUsers();
                } else {
                    return response.json().then(error => { throw new Error(error.message); });
                }
            })
            .catch(error => {
                console.error('Error deleting user:', error);
                alert('Failed to delete user. Please try again.');
            });
    }
}

// Llamar a la función para obtener todos los usuarios cuando la página se carga
getAllUsers();

// Agregar un evento para manejar el envío del formulario de creación de usuario
document.getElementById('createUserForm').addEventListener('submit', createUser);