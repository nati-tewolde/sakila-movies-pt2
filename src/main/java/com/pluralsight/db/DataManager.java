package com.pluralsight.db;

import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> getActorsByName(String firstName, String lastName) {
        List<Actor> actors = new ArrayList<>();
        String query = """
                SELECT a.actor_id, a.first_name, a.last_name
                FROM Actor a
                WHERE a.first_name = ? AND a.last_name = ?;
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            try (ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    int actorId = results.getInt(1);
                    String actorFirstName = results.getString(2);
                    String actorLastName = results.getString(3);

                    actors.add(new Actor(actorId, actorFirstName, actorLastName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }

    public List<Film> getFilmsByActorId(int actorId) {
        List<Film> films = new ArrayList<>();
        String query = """
                SELECT f.film_id, f.title, f.description, f.release_year, f.length
                FROM Film f
                JOIN film_actor fa ON f.film_id = fa.film_id
                JOIN actor a ON fa.actor_id = a.actor_id
                WHERE a.actor_id = ?;
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, actorId);
            try (ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    int filmId = results.getInt(1);
                    String title = results.getString(2);
                    String description = results.getString(3);
                    int releaseYear = results.getInt(4);
                    int length = results.getInt(5);

                    films.add(new Film(filmId, title, description, releaseYear, length));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }
}
