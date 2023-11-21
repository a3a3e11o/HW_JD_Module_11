package org.example;

import org.example.crud.ClientCrudService;
import org.example.crud.PlanetCrudService;
import org.example.crud.TicketCrudService;
import org.example.entity.Client;
import org.example.entity.Planet;
import org.example.entity.Ticket;
import org.example.util.HibernateUtil;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/megasoft", "postgres", "123").load();
        flyway.migrate();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        TicketCrudService ticketCrudService = new TicketCrudService(sessionFactory);

        Client client = new Client("TestClient");
        Planet fromPlanet = new Planet("TEST1", "TestPlanet1");
        Planet toPlanet = new Planet("TEST2", "TestPlanet2");

        ClientCrudService clientCrudService = new ClientCrudService(sessionFactory);
        clientCrudService.save(client);

        PlanetCrudService planetCrudService = new PlanetCrudService(sessionFactory);
        planetCrudService.save(fromPlanet);
        planetCrudService.save(toPlanet);

        Ticket ticket = new Ticket(Timestamp.valueOf("2023-11-21 12:00:00"), client, fromPlanet, toPlanet);
        ticketCrudService.save(ticket);

        Long ticketId = ticket.getId();
        Ticket retrievedTicket = ticketCrudService.getById(ticketId);
        System.out.println("Retrieved ticket: " + retrievedTicket);

        retrievedTicket.setCreatedAt(Timestamp.valueOf("2023-11-21 14:00:00"));
        ticketCrudService.update(retrievedTicket);

        Ticket updatedTicket = ticketCrudService.getById(ticketId);
        System.out.println("Updated ticket: " + updatedTicket);

        ticketCrudService.delete(ticketId);
        System.out.println("Ticket deleted");

        sessionFactory.close();
    }
}
