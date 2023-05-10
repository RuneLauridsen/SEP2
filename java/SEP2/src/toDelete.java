public class toDelete
{
  public static void main(String[] args)
  {
    int x;
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
    System.out.println(string);
  }
}
