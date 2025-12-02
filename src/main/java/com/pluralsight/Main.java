package com.pluralsight;

import com.pluralsight.db.DataManager;
import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            if (args.length != 2) {
                System.out.println("This application needs a Username and Password too run!");
                System.exit(1);
            }

            String username = args[0];
            String password = args[1];

            try (BasicDataSource dataSource = new BasicDataSource()) {
                dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
                dataSource.setUsername(username);
                dataSource.setPassword(password);

                DataManager dataManager = new DataManager(dataSource);

                System.out.print("\nPlease enter the first name of the actor: ");
                String firstName = scanner.nextLine();

                System.out.print("\nPlease enter the last name of the actor: ");
                String lastName = scanner.nextLine();

                List<Actor> actorList = dataManager.getActorsByName(firstName, lastName);
                if (actorList.isEmpty()) {
                    System.out.println("No matches!");
                } else {
                    actorList.forEach(System.out::println);
                }

                System.out.print("\nPlease enter an Actor ID:");
                int actorId = scanner.nextInt();

                List<Film> filmList = dataManager.getFilmsByActorId(actorId);
                if (filmList.isEmpty()) {
                    System.out.println("No matches!");
                } else {
                    filmList.forEach(System.out::println);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
