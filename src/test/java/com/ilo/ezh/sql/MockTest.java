package com.ilo.ezh.sql;

import com.ilo.ezh.User;
import com.ilo.ezh.exceptions.NotExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Mock
    SqlStorage sqlStorage;

    String newSurname = "NewSurname";
    User USER1 = new User(100000, "Admin", "Adminov");
    User USER2 = new User(100001, "User", "Userov");
    User USER2UPDATED = new User(100001, "User", "NewSurname");
    User USERWRONG = new User(100002, "Wrong", "Wrongov");

    @Before
    public void setUp() {

        when(sqlStorage.getUserByLogin(USER1.getLogin())).thenReturn(USER1);
        when(sqlStorage.getUserByLogin(USERWRONG.getLogin())).thenThrow(NotExistException.class);

        when(sqlStorage.getUserByLogin(USER2.getLogin())).thenReturn(USER2UPDATED);

        doThrow(NotExistException.class).when(sqlStorage).updateUserSurname(eq(USERWRONG), any(String.class));

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

        sqlStorage.updateUserSurname(USER2, newSurname);
        User userFromDB = sqlStorage.getUserByLogin(USER2.getLogin());
        assertEquals(newSurname, userFromDB.getSurname());

    }

    @Test
    public void updateNotExistUserSurname() {

        assertThrows(NotExistException.class, () -> sqlStorage.updateUserSurname(USERWRONG, "NewSurname"));

    }

}