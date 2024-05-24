package com.spring.productsshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.model.Client;
import com.spring.productsshop.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    // Inyección de dependencias de ClientRepository
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Obtiene todos los clientes de la base de datos.
     *
     * @return Lista de todos los clientes.
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return Un Optional que contiene el cliente si se encuentra, de lo contrario está vacío.
     */
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param client Cliente a crear.
     * @return El cliente creado.
     */
    @Transactional
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Actualiza un cliente existente en la base de datos.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param id             ID del cliente a actualizar.
     * @param updatedClient  Objeto que contiene los nuevos datos del cliente.
     * @return El cliente actualizado.
     * @throws ResourceNotFoundException si el cliente con el ID especificado no se encuentra.
     */
    @Transactional
    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    // Actualiza los campos del cliente con los nuevos datos
                    client.setName(updatedClient.getName());
                    client.setAddress(updatedClient.getAddress());
                    client.setAge(updatedClient.getAge());
                    // Guarda el cliente actualizado en la base de datos
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    /**
     * Actualiza solo el nombre de un cliente existente en la base de datos.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param id      ID del cliente a actualizar.
     * @param newName Nuevo nombre del cliente.
     * @return El cliente actualizado.
     * @throws ResourceNotFoundException si el cliente con el ID especificado no se encuentra.
     */
    @Transactional
    public Client updateClientName(Long id, String newName) {
        return clientRepository.findById(id)
                .map(client -> {
                    // Actualiza solo el nombre del cliente
                    client.setName(newName);
                    // Guarda el cliente actualizado en la base de datos
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param id ID del cliente a eliminar.
     * @throws ResourceNotFoundException si el cliente con el ID especificado no se encuentra.
     */
    @Transactional
    public void deleteClient(Long id) {
        // Verifica si el cliente existe en la base de datos
        if (clientRepository.existsById(id)) {
            // Elimina el cliente de la base de datos
            clientRepository.deleteById(id);
        } else {
            // Lanza una excepción si el cliente no se encuentra
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
    }
}

