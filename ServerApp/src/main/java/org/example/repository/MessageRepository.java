package org.example.repository;

import org.example.utility.DBConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;

public class MessageRepository {
    public Long create(LocalDateTime time, String text) {
        String sqlAddNewMessage = "INSERT INTO messages (time, text) VALUES (?,?);";
        long id = -1L;
        try (Connection connection = DBConnectionManager.connect();
             PreparedStatement statement = connection.prepareStatement(sqlAddNewMessage, Statement.RETURN_GENERATED_KEYS)) {
            statement.setTimestamp(1, Timestamp.valueOf(time));
            statement.setString(2, text);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys != null && keys.next()) {
                    id = keys.getLong(1);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return id;
    }

    public int getMessagesCount() {
        int count = 0;
        String sqlGetCount = "SELECT COUNT(*) AS message_count FROM messages;";
        try (Connection connection = DBConnectionManager.connect();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(sqlGetCount);
            if (result.next()){
                count = result.getInt("message_count");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return count;
    }
}
