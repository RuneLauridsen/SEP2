package booking.client.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingEncrypter
{


    private static String[] hexTable;

    private static String[] buildHexTable()
    {
        String[] hexTable = new String[256];

        for(int i = 0; i < hexTable.length; i++)
        {
            String iHex = String.format("%1$02X", i);
            hexTable[i] = iHex;
        }

        return hexTable;
    }

    public static String encrypt (String input) throws NoSuchAlgorithmException
    {
        if(hexTable == null)
        {
            hexTable = buildHexTable();
        }

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder(32);
        for(int i = 0; i < messageDigest.length; i++)
        {
            hexString.append(hexTable[messageDigest[i] + 128]);
        }

        return hexString.toString();
    }
}
