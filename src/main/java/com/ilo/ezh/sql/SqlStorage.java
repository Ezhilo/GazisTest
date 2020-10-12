package com.ilo.ezh.sql;

import com.ilo.ezh.User;
import com.ilo.ezh.exceptions.NotExistException;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class SqlStorage {

    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    public User getUserByLogin(String login) {
        return sqlHelper.execute("SELECT * FROM users WHERE LOGIN = ?",
                ps -> {
                    ps.setString(1, login);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return new User(rs.getInt("id"), rs.getString("login"), rs.getString("surname"));
                    } else {
                        throw new NotExistException("user with login " + login + " not found");
                    }
                });
    }

    public void updateUserSurname(User user, String surname) {

        sqlHelper.execute("UPDATE users SET SURNAME = ? WHERE LOGIN = ?",
                ps -> {
                    ps.setString(1, surname);
                    ps.setString(2, user.getLogin());
                    if (ps.executeUpdate() != 1) {
                        throw new NotExistException("UPDATE aborted: user with login " + user.getLogin() + " not found");
                    }
                    return null;
                });

    }

    public void saveUserToDB(User user) {
        sqlHelper.execute("INSERT INTO users (id, login, surname) VALUES (?,?,?)",
                ps -> {
                    ps.setInt(1, user.getId());
                    ps.setString(2, user.getLogin());
                    ps.setString(3, user.getSurname());
                    ps.execute();
                    return null;
                });

    }

    public void clearUsersTable() {
        sqlHelper.execute("DELETE FROM users");
    }

}
