package com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "client_id") // Coloca la clave foránea en la tabla Address
    @JoinTable(name = "tbl_clientes_to_direcciones", 
               joinColumns = @JoinColumn(name = "id_cliente"), 
               inverseJoinColumns = @JoinColumn(name = "id_direcciones"),
               uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"})) // mapear la relación a una tabla existente 
    private List<Address> addresses;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client") // mappedBy indica que la relación está mapeada por el campo 'client' en la entidad Invoice
    private List<Invoice> invoices;

    public Client() {
        addresses = new ArrayList<>();
        invoices = new ArrayList<>();
    }

    public Client(String name, String lastname) {
        this();
        this.name = name;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return "{id=" + id + 
                ", name=" + name + 
                ", lastname=" + lastname + 
                ", addresses=" + addresses + 
                ", invoices=" + invoices +
                "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void imprimeFormat() {
        System.out.println("");
        System.out.println("========================================");
        System.out.println("Datos del Cliente");
        System.out.println("-----------------");
        System.out.println("Id: " + this.id);
        System.out.println("Cliente: " + this.name + " " + this.lastname);
        System.out.println("");
        System.out.println("Direcciones ");
        System.out.println("-----------");
        
        if (addresses.isEmpty()) {
            System.out.println("    No hay direcciones asociadas.");
        } else {
            addresses.forEach(address -> {
                System.out.println("    Id: " + address.getId() + " - " + address.getStreet() + " " + address.getNumber());
            });
        }

        System.out.println("========================================");
        System.out.println("");
    }

}
