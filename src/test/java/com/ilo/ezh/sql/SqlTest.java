package com.ilo.ezh.sql;

import com.ilo.ezh.Config;
import com.ilo.ezh.User;
import com.ilo.ezh.exceptions.NotExistException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SqlTest {

    private static final SqlStorage sqlStorage = Config.get().getSqlStorage();
    private static final User USER1 = new User(100000, "Admin", "Adminov");
    private static final User USER2 = new User(100001, "User", "Userov");
    private static final User USERWRONG = new User(100002, "Wrong", "Wrongov");

    @Before
    public void setUp() {
        sqlStorage.clearUsersTable();
        sqlStorage.saveUserToDB(USER1);
        sqlStorage.saveUserToDB(USER2);
    }

    @Test
    public void getExistUserByLogin() {
        User userFromDB = sqlStorage.getUserByLogin(USER1.getLogin());
        assertEquals(USER1, userFromDB);
    }

    @Test
    public void getNotExistUserByLogin() {
        assertThrows(NotExistException.class, () -> sqlStorage.getUserByLogin(USERWRONG.getLogin()));
    }

    @Test
    public void updateExistUserSurname() {

        String newSurname = "NewSurname";
        sqlStorage.updateUserSurname(USER2, newSurname);
        User userFromDB = sqlStorage.getUserByLogin(USER2.getLogin());
        assertEquals(newSurname, userFromDB.getSurname());

    }

    @Test
    public void updateNotExistUserSurname() {

        assertThrows(NotExistException.class, () -> sqlStorage.updateUserSurname(USERWRONG, "NewSurname"));

    }

}