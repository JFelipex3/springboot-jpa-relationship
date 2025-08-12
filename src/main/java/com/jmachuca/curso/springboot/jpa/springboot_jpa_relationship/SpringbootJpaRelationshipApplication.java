package com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Address;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Client;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.ClientDetails;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.ClientDetailsRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		oneToOneBidireccionalFindById();
	}

	@Transactional
	public void oneToOneBidireccionalFindById() {

		Optional<Client> optionalClient = clientRepository.findOne(2L);

		optionalClient.ifPresent(client -> {
			ClientDetails clientDetails = new ClientDetails(true, 5000);
		
			client.setClientDetails(clientDetails);
			
			clientRepository.save(client);

			System.out.println(client);
		});
	}

	@Transactional
	public void oneToOneBidireccional() {

		Client client = new Client("Erba", "Pura");

		ClientDetails clientDetails = new ClientDetails(true, 5000);
		
		client.setClientDetails(clientDetails);
		
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void oneToOneFindById() {
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		Optional<Client> clientOptional = clientRepository.findOne(2L);
		clientOptional.ifPresent(client -> {
			client.setClientDetails(clientDetails);
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}

	@Transactional
	public void oneToOne() {
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);
		
		Client client = new Client("Erba", "Pura");
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void removeInvoiceBidireccionalFindById() {
		
		Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent( client -> {
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de la oficina", 8000L);

			Set<Invoice> invoices = new HashSet<>();
			invoices.add(invoice1);
			invoices.add(invoice2);

			client.setInvoices(invoices);

			// Como es una relación bidireccional, debemos establecer el cliente en cada factura
			invoice1.setClient(client);
			invoice2.setClient(client);

			clientRepository.save(client); // El cliente contiene las facturas, por lo que al guardar el cliente, también se guardan las facturas

			System.out.println("Cliente guardado: " + client);
		});

		Optional<Client> optionalClien2 = clientRepository.findOne(1L);

		optionalClien2.ifPresent(client -> {
			Optional<Invoice> optionalInvoice = invoiceRepository.findById(2L);

			optionalInvoice.ifPresent(invoice -> {
				client.removeInvoice(invoice);

				clientRepository.save(client);

				System.out.println("Factura eliminada: " + invoice);
				System.out.println("Cliente actualizado: " + client);
			});
		});
	}

	@Transactional
	public void oneToManyInvoiceBidireccional() {
		Client client = new Client("Fran", "Moras");

		Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
		Invoice invoice2 = new Invoice("Compras de la oficina", 8000L);

		Set<Invoice> invoices = new HashSet<>();
		invoices.add(invoice1);
		invoices.add(invoice2);

		client.setInvoices(invoices);

		// Como es una relación bidireccional, debemos establecer el cliente en cada factura
		invoice1.setClient(client);
		invoice2.setClient(client);

		clientRepository.save(client); // El cliente contiene las facturas, por lo que al guardar el cliente, también se guardan las facturas

		System.out.println("Cliente guardado: " + client);

	}

	@Transactional
	public void oneToManyInvoiceBidireccionalFindById() {
		
		Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent( client -> {
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de la oficina", 8000L);

			Set<Invoice> invoices = new HashSet<>();
			invoices.add(invoice1);
			invoices.add(invoice2);

			client.setInvoices(invoices);

			// Como es una relación bidireccional, debemos establecer el cliente en cada factura
			invoice1.setClient(client);
			invoice2.setClient(client);

			clientRepository.save(client); // El cliente contiene las facturas, por lo que al guardar el cliente, también se guardan las facturas

			System.out.println("Cliente guardado: " + client);
		});
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

		Optional<Client> optionalClient = clientRepository.findById(3L);

		optionalClient.ifPresentOrElse(existingClient -> {
			existingClient.getAddresses().remove(address1);

			clientRepository.save(existingClient);

			System.out.println("Cliente actualizado: " + existingClient);
		}, () -> {
			System.out.println("El cliente no existe");
		});
	}

	@Transactional
	public void removeAddressFindByIdClient(Long idCliente) {
		Optional<Client> optionalClient = clientRepository.findById(idCliente);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El vergel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);

			client.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));

			clientRepository.save(client);

			System.out.println("Cliente guardado: " + client);

			Optional<Client> optionalClient2 = clientRepository.findOneWithAddresses(idCliente);

			optionalClient2.ifPresent(existingClient -> {
				existingClient.getAddresses().remove(address2);

				clientRepository.save(existingClient);

				System.out.println("Cliente actualizado: " + existingClient);
			});

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

			client.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));

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
