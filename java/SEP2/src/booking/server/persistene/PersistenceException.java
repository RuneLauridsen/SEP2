package booking.server.persistene;

// NOTE(rune): Noget går galt i Persistence. Vi kunne også bare have brugt SQLException,
// mens hvis vi en da skifter til noget andet end en SQL database, er der brug for denne
// PersistenceException, som kan dække alle slags persistence lag.
public class PersistenceException extends Exception
{
    public PersistenceException(Throwable cause)
    {
        super(cause);
    }
}
