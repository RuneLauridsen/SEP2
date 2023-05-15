import booking.shared.objects.BookingInterval;

import java.time.LocalDate;
import java.time.LocalTime;

public class toDelete
{
  public static void main(String[] args)
  {
   /* int x;
    String string = "";

    for (x = 7; x < 16; x++)
    {
      for (int i = 0; i < 60; i = i+ 15)
      {
        if (i ==0)
          string = string +"\""+x+":00\",";
        else
          string = string +"\""+x+":"+i+"\",";
      }
    }
    System.out.println(string);*/

    BookingInterval bookingInterval = new BookingInterval(LocalDate.of(2023,05,12),LocalTime.of(7,0,0,0),LocalTime.of(16,0,0,0));
    System.out.println(bookingInterval.getDate().equals(LocalDate.now()) && bookingInterval.isOverlapWith(LocalTime.now()));
  }
}
