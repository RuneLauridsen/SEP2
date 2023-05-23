import booking.client.model.ArgbIntConverter;
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

    /*
    BookingInterval bookingInterval = new BookingInterval(LocalDate.of(2023,05,12),LocalTime.of(7,0,0,0),LocalTime.of(16,0,0,0));
    System.out.println(bookingInterval.getDate().equals(LocalDate.now()) && bookingInterval.isOverlapWith(LocalTime.now()));*/

    System.out.println("Red"+ ArgbIntConverter.argbToInt(243, 131, 131) );
    System.out.println("blue"+ ArgbIntConverter.argbToInt(130,137,243));
    System.out.println("yellow" + ArgbIntConverter.argbToInt(250,250,100));
    System.out.println("Orange"+ ArgbIntConverter.argbToInt(255,178,61));
    System.out.println("Green"+ ArgbIntConverter.argbToInt(141,238,127));
    System.out.println("Purple"+ ArgbIntConverter.argbToInt(214,142,236));
    System.out.println("Pink"+ ArgbIntConverter.argbToInt(255,134,211));
    System.out.println("Mint"+ ArgbIntConverter.argbToInt(162,255,255));
    System.out.println("Gray"+ ArgbIntConverter.argbToInt(222,222,222));
  }
}
