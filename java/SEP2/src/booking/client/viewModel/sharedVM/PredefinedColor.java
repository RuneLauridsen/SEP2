package booking.client.viewModel.sharedVM;

import booking.client.model.ArgbIntConverter;

public class PredefinedColor
{
    private static PredefinedColor[] predefinedColors;

    private final String name;
    private final int argb;

    private PredefinedColor(String name, int r, int g, int b)
    {
        this.name = name;
        this.argb = ArgbIntConverter.argbToInt(r, g, b);
    }

    public int getArgb()
    {
        return argb;
    }

    public static PredefinedColor[] getPredefinedColors()
    {
        if (predefinedColors == null)
        {
            predefinedColors = new PredefinedColor[]
                {
                    new PredefinedColor("Red", 243, 131, 131),
                    new PredefinedColor("Blue", 130, 137, 243),
                    new PredefinedColor("Yellow", 250, 250, 100),
                    new PredefinedColor("Orange", 255, 178, 61),
                    new PredefinedColor("Green", 141, 238, 127),
                    new PredefinedColor("Purple", 214, 142, 236),
                    new PredefinedColor("Pink", 255, 134, 211),
                    new PredefinedColor("Mint", 162, 255, 255),
                    new PredefinedColor("Gray", 222, 222, 222),
                };
        }

        return predefinedColors;
    }

    public static PredefinedColor getPredefinedColorByArgb(int argb)
    {
        for (PredefinedColor color : getPredefinedColors())
        {
            if (color.getArgb() == argb)
            {
                return color;
            }
        }

        return null;
    }

    @Override public String toString()
    {
        return name;
    }
}
