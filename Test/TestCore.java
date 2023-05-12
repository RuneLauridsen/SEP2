import booking.core.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCore {

    //TODO Database skal også testes og sættes ind (Sprøg), GITHub



    @Test
    public void ConstructorOfUser()
    {
        List<RoomType> userBob = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,userBob);
        User bob = new User(1,"Bob","",123456,student);

        assertEquals(bob, new User(1,"Bob","",123456,student));
        assertNotEquals(bob, new User(2,"Jens","",234567,student));
    }

    @Test
    public void BookingCreate()
    {
        List<RoomType> groupList = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,groupList);
        User bob = new User(1,"Bob","",123456,student);

        RoomType group = new RoomType(1,"Group");
        Room C0207 = new Room(1,"C02.07",15,4,7,"",group);

        BookingInterval bookingInterval = new BookingInterval(LocalDate.of(2023,5,11), LocalTime.of(9,15),LocalTime.of(16,30));
        Booking booking1 = new Booking(1,bookingInterval,C0207,bob);

        assertEquals(bob,booking1.getUser());
        assertEquals(1,(booking1.getId()));
        assertEquals(bookingInterval,booking1.getInterval());
    }


    @Test
    public void UserMaxRooms()
    {
        List<RoomType> groupList = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,groupList);
        User bob = new User(1,"Bob","",123456,student);

        RoomType group = new RoomType(1,"Group");
        Room C0207 = new Room(1,"C02.07",15,4,7,"",group);
        Room C0208 = new Room(2,"C02.08",15,4,7,"",group);

        BookingInterval bookingInterval = new BookingInterval(LocalDate.now(), LocalTime.now(),LocalTime.of(20,30));
        Booking booking1 = new Booking(1,bookingInterval,C0207,bob);
        Booking booking2 = new Booking(1,bookingInterval,C0208,bob);

        //TODO Ikke færdig, Sæt databasen ind så kan man tjekke den.
    }

    @Test
    public void Course()
    {
        Course SDJ2 = new Course(1,"SDJ",4);

        assertEquals(SDJ2.getId(),1);
        assertEquals(SDJ2.getName(),"SDJ");
        assertEquals(SDJ2.getTimeSlotCount(),4);
    }

    @Test
    public void RoomTypesGroup()
    {
        RoomType group = new RoomType(1,"Group");
        Room C0207 = new Room(1,"C02.07",15,4,8,"",group);

        assertEquals(C0207.getType(),group);
    }

    @Test
    public void RoomTypesCoWorker()
    {
        RoomType coWorker = new RoomType(2,"Co-Worker");
        Room C0206 = new Room(1,"C02.06",15,4,8,"",coWorker);

        assertEquals(C0206.getType(),coWorker);
    }

    @Test
    public void RoomTypesClassRoom()
    {
        RoomType classroom = new RoomType(3,"Classroom");
        Room C0203 = new Room(1,"C02.03",15,4,8,"",classroom);

        assertEquals(C0203.getType(),classroom);

    }

    @Test
    public void RoomTypesBa()
    {
        RoomType Bahlor = new RoomType(4, "Baherlor");
    }


    @Test
    public void UserGroup()
    {
        List<User> SDJClass = new ArrayList<>();
        Course SDJ = new Course(1,"SDJ",4);
        UserGroup SW = new UserGroup(1,"SW",SDJ,SDJClass);
    }

    @Test
    public void BookingOverlap()
    {

    }

    @Test
    public void jjkk()
    {

    }



}
