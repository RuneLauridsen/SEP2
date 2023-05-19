import booking.client.model.HashingEncrypter;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHashing
{
    @Test void testHash() throws NoSuchAlgorithmException
    {
        String text0 = "1234";
        String hash0 = HashingEncrypter.encrypt(text0);

        String text1 = "abc";
        String hash1 = HashingEncrypter.encrypt(text1);

        assertEquals(hash0, "015C1B5BD250CD4280B65B58B1BE50D5");
        assertEquals(hash1, "1081D018BC52CF305616BFFDA861FFF2");
    }
}
