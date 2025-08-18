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
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Course;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Student;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.ClientDetailsRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.CourseRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;
import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories.StudentRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		manyToManyRemoveCreate();
	}

	public void manyToManyCreateBidirectional() {

		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Java Master", "Andrés");
		Course course2 = new Course("Spring Boot", "Andrés");

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println("Estudiantes guardados: ");
		System.out.println(student1);
		System.out.println(student2);
	}

	public void manyToManyRemoveCreate() {

		/* 
		 	Hibernate: insert into students (lastname,name) values (?,?)
			Hibernate: insert into courses (instructor,name) values (?,?)
			Hibernate: insert into students (lastname,name) values (?,?)
			Hibernate: insert into courses (instructor,name) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
		*/

		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Java Master", "Andrés");
		Course course2 = new Course("Spring Boot", "Andrés");

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println("Estudiantes guardados: ");
		System.out.println(student1);
		System.out.println(student2);

		Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(4L);

		if(studentOptionalDb.isPresent()) {
			Student studentDb = studentOptionalDb.get();
			
			Optional<Course> courseOptionalDb = courseRepository.findById(3L);

			if (courseOptionalDb.isPresent()) {
				Course courseDb = courseOptionalDb.get();

				studentDb.getCourses().remove(courseDb);

				studentRepository.save(studentDb);

				System.out.println("Estudiante actualizado: " + studentDb);
			}
		}
	}

	public void manyToManyRemoveFind() {

		/* 
		 	Hibernate: select s1_0.id,s1_0.lastname,s1_0.name from students s1_0 where s1_0.id=?
			Hibernate: select s1_0.id,s1_0.lastname,s1_0.name from students s1_0 where s1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)

			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: delete from tbl_alumnos_cursos where alumno_id=? and curso_id=?
		*/

		Optional<Student> studentOptional1 = studentRepository.findById(1L);
		Optional<Student> studentOptional2 = studentRepository.findById(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findById(1L).orElse(new Course("Java Master", "Andrés"));
		Course course2 = courseRepository.findById(2L).orElse(new Course("Spring Boot", "Andrés"));

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println("Estudiantes guardados: ");
		System.out.println(student1);
		System.out.println(student2);

		Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(1L);

		if(studentOptionalDb.isPresent()) {
			Student studentDb = studentOptionalDb.get();
			
			Optional<Course> courseOptionalDb = courseRepository.findById(2L);

			if (courseOptionalDb.isPresent()) {
				Course courseDb = courseOptionalDb.get();

				studentDb.getCourses().remove(courseDb);

				studentRepository.save(studentDb);

				System.out.println("Estudiante actualizado: " + studentDb);
			}
		}
	}

	public void manyToManyFind() {

		/* 
		 	Hibernate: select s1_0.id,s1_0.lastname,s1_0.name from students s1_0 where s1_0.id=?
			Hibernate: select s1_0.id,s1_0.lastname,s1_0.name from students s1_0 where s1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select c1_0.id,c1_0.instructor,c1_0.name from courses c1_0 where c1_0.id=?
			Hibernate: select s1_0.id,c1_0.alumno_id,c1_1.id,c1_1.instructor,c1_1.name,s1_0.lastname,s1_0.name from students s1_0 left join tbl_alumnos_cursos c1_0 on s1_0.id=c1_0.alumno_id left join courses c1_1 on c1_1.id=c1_0.curso_id where s1_0.id=?
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)
			Hibernate: insert into tbl_alumnos_cursos (alumno_id,curso_id) values (?,?)
		*/

		Optional<Student> studentOptional1 = studentRepository.findById(1L);
		Optional<Student> studentOptional2 = studentRepository.findById(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findById(1L).orElse(new Course("Java Master", "Andrés"));
		Course course2 = courseRepository.findById(2L).orElse(new Course("Spring Boot", "Andrés"));

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println("Estudiantes guardados: ");
		System.out.println(student1);
		System.out.println(student2);
	}

	public void manyToManyCreate() {

		/* 
		 	Hibernate: insert into students (lastname,name) values (?,?)
			Hibernate: insert into courses (instructor,name) values (?,?)
			Hibernate: insert into students (lastname,name) values (?,?)
			Hibernate: insert into courses (instructor,name) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
			Hibernate: insert into students_courses (student_id,courses_id) values (?,?)
		*/

		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Java Master", "Andrés");
		Course course2 = new Course("Spring Boot", "Andrés");

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println("Estudiantes guardados: ");
		System.out.println(student1);
		System.out.println(student2);
	}

	@Transactional
	public void oneToOneBidireccionalFindById() {

		/* 
			Hibernate: select c1_0.id,a1_0.id_cliente,a1_1.id,a1_1.number,a1_1.street,cd1_0.id,cd1_0.points,cd1_0.premium,i1_0.client_id,i1_0.id,i1_0.description,i1_0.total,c1_0.lastname,c1_0.name from clients c1_0 left join invoices i1_0 on c1_0.id=i1_0.client_id left join tbl_clientes_to_direcciones a1_0 on c1_0.id=a1_0.id_cliente left join addresses a1_1 on a1_1.id=a1_0.id_direcciones left join client_details cd1_0 on c1_0.id=cd1_0.id_cliente where c1_0.id=?
			Hibernate: select c1_0.id,a1_0.id_cliente,a1_1.id,a1_1.number,a1_1.street,cd1_0.id,cd1_0.points,cd1_0.premium,c1_0.lastname,c1_0.name from clients c1_0 left join tbl_clientes_to_direcciones a1_0 on c1_0.id=a1_0.id_cliente left join addresses a1_1 on a1_1.id=a1_0.id_direcciones left join client_details cd1_0 on c1_0.id=cd1_0.id_cliente where c1_0.id=?
			Hibernate: select i1_0.client_id,i1_0.id,i1_0.description,i1_0.total from invoices i1_0 where i1_0.client_id=?
			Hibernate: insert into client_details (id_cliente,points,premium) values (?,?,?)
		*/

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
