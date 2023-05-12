import booking.core.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCore {

    //TODO Database skal også testes og sættes ind (Sprøg)
    //TODO Skemalægger del. Hvis der skabes duplicate af unikke værdier skal der være fejl


    //Course og Room

    @Test
    public void Course()
    {
        Course SDJ2 = new Course(1,"SDJ",4);

        assertEquals(SDJ2.getId(),1);
        assertEquals(SDJ2.getName(),"SDJ");
        assertEquals(SDJ2.getTimeSlotCount(),4);
    }
    //TODO Test unik id og ingen duplicate

    @Test
    public void RoomTypesGroup()
    {
        RoomType group = new RoomType(1,"Group");
        Room C0207 = new Room(1,"C02.07",15,4,8,"",group);

        assertEquals(C0207.getType(),group);
    }
    //TODO Test unik id og ingen duplicate

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
    public void RoomTypesBachelor()
    {
        RoomType bachelor = new RoomType(4, "Bachelor");
        Room C0606 = new Room(5,"C06.06",15,4,8,"",bachelor);

        assertEquals(C0606.getType(),bachelor);
    }




    //Booking

    @Test
    public void RemoveBooking()
    {

    }

    @Test
    public void RemoveBookingWhenThereIsNone()
    {

    }

    @Test
    public void BookingIntervalOverlap()
    {

        BookingInterval bookingInterval = new BookingInterval(LocalDate.of(2023,5,12), LocalTime.of(9,15),LocalTime.of(16,30));
        BookingInterval bookingInterval1 = new BookingInterval(LocalDate.of(2023,5,12), LocalTime.of(8,15),LocalTime.of(15,30));
        BookingInterval bookingInterval2 = new BookingInterval(LocalDate.of(2023,5,12), LocalTime.of(8,15),LocalTime.of(17,30));
        BookingInterval bookingInterval3 = new BookingInterval(LocalDate.of(2023,5,12), LocalTime.of(10,15),LocalTime.of(15,30));
        BookingInterval bookingInterval4 = new BookingInterval(LocalDate.of(2023,5,12), LocalTime.of(10,15),LocalTime.of(17,30));

        assertFalse(bookingInterval.isOverlapWith(bookingInterval1));
        assertFalse(bookingInterval.isOverlapWith(bookingInterval2));
        assertFalse(bookingInterval.isOverlapWith(bookingInterval3));
        assertFalse(bookingInterval.isOverlapWith(bookingInterval4));


    }

    @Test
    public void BookingMaksRooms()
    {
        List<RoomType> groupList = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,groupList);
        User bob = new User(1,"Bob","",123456,student);

        RoomType group = new RoomType(1,"Group");
        Room C0207 = new Room(1,"C02.07",15,4,7,"",group);
        Room C0208 = new Room(2,"C02.08",15,4,7,"",group);
        Room C0209a = new Room(2,"C02.09a",15,4,7,"",group);

        BookingInterval bookingInterval = new BookingInterval(LocalDate.now(), LocalTime.now(),LocalTime.of(20,30));
        Booking booking1 = new Booking(1,bookingInterval,C0207,bob);
        Booking booking2 = new Booking(1,bookingInterval,C0208,bob);

        assertThrows(IndexOutOfBoundsException.class, () -> new Booking(1,bookingInterval,C0209a,bob));
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


    //User and UserGroup

    @Test
    public void RemoveUserFromUserGroup()
    {
        // Kan vi det? Hvis så tjek andre del af slette
    }

    @Test
    public void AddUserToUserGroup()
    {
        Course SDJ = new Course(1,"SDJ",8);

        List<RoomType> groupList= new ArrayList();
        UserType student = new UserType(1,"Student",false,false,2,groupList);
        List<User> SW = new ArrayList<>();

        User bob = new User(1,"Bob","",123456,student);
        UserGroup SWSDJ = new UserGroup(1,"SW-SDJ",SDJ,SW);

        //Ikke færdig da jeg ikke er sikker på hvordan det fungere

    }

    @Test
    public void AddMoreUsersToUserGroup()
    {

    }

    @Test
    public void UserGroup()
    {
        List<User> SDJClass = new ArrayList<>();
        Course SDJ = new Course(1,"SDJ",4);
        UserGroup SW = new UserGroup(1,"SW",SDJ,SDJClass);

        assertEquals(SDJ, SW.getCourse());
        assertEquals(SDJClass,SW.getUsers());
    }

    @Test
    public void ConstructorOfUserType()
    {
        List<RoomType> userBob = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,userBob);

        assertEquals(student, new UserType(1,"student",false,false,2,userBob));
        assertNotEquals(student, new UserType(2,"student",false,false,2,userBob));
    }
    //TODO Test unik id og ingen duplicate

    @Test
    public void ConstructorOfUser()
    {
        List<RoomType> userBob = new ArrayList<>();
        UserType student = new UserType(1,"student",false,false,2,userBob);
        User bob = new User(1,"Bob","",123456,student);

        assertEquals(bob, new User(1,"Bob","",123456,student));
        assertNotEquals(bob, new User(2,"Jens","",234567,student));
    }
    //TODO Test unik id og ingen duplicate


}
