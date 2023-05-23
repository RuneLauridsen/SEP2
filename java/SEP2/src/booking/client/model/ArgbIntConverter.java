package booking.client.model;

import javafx.scene.paint.Color;

public class ArgbIntConverter
{

  public static int argbToInt(int r, int g, int b)
  {
    int a = 0xff;

    int argb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
    return argb;
  }

  public static Color intToColor(int argb)
  {
    int a = (argb >> 24) & 0xff;
    int r = (argb >> 16) & 0xff;
    int g = (argb >> 8) & 0xff;
    int b = (argb >> 0) & 0xff;

    double af = 1.0;
    double rf = (double) r / 256.0;
    double gf = (double) g / 256.0;
    double bf = (double) b / 256.0;

    return new Color(rf, gf, bf, af);
  }
}
