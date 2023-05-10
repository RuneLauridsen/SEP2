package booking;

import booking.core.Booking;
import booking.core.User;
import booking.core.UserType;
import booking.database.DatabaseHandler;
import javafx.application.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RunApp
{
    public static void main(String[] args)
    {
        Application.launch(App.class, args);
    }
}
