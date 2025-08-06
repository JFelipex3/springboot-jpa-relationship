package com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Address;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Client;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		removeAddress();
	}

	@Transactional
	public void removeAddress() {
		Client client = new Client("Fran", "Moras");

		Address address1 = new Address("El vergel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println("Cliente guardado: " + client);
		client.imprimeFormat();

		Optional<Client> optionalClient = clientRepository.findById(3L);

		optionalClient.ifPresentOrElse(existingClient -> {
			existingClient.getAddresses().remove(address1);

			clientRepository.save(existingClient);

			System.out.println("Cliente actualizado: " + existingClient);
			existingClient.imprimeFormat();
		}, () -> {
			System.out.println("El cliente no existe");
		});
	}

	@Transactional
	public void oneToManyCreateClient() {
		Client client = new Client("Fran", "Moras");

		Address address1 = new Address("El vergel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println("Cliente guardado: " + client);
	}

	@Transactional
	public void oneToManyFindByIdClient(Long idCliente) {
		Optional<Client> optionalClient = clientRepository.findById(idCliente);

		optionalClient.ifPresentOrElse(client -> {
			Address address1 = new Address("El vergel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);

			client.setAddresses(Arrays.asList(address1, address2));

			clientRepository.save(client);

			System.out.println("Cliente guardado: " + client);

		}, () -> {
			System.out.println("El cliente no existe");
		});
	}

	@Transactional
	public void manyToOneCreateClient() {

		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Compras de Oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);

		System.out.println(invoiceDB);
	}

	@Transactional
	public void manyToOneFindByIdClient(Long clientId) {

		Optional<Client> optionalClient = clientRepository.findById(clientId);

		optionalClient.ifPresentOrElse(client -> {
			Invoice invoice = new Invoice("Compras de Oficina", 2000L);
			invoice.setClient(client);
			Invoice invoiceDB = invoiceRepository.save(invoice);

			System.out.println(invoiceDB);

		}, () -> {
			System.out.println("El cliente no existe");
		});
	}

}
