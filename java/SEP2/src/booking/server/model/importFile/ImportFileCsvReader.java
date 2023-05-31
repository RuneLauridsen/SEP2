package booking.server.model.importFile;

import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceException;
import booking.shared.objects.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ImportFileCsvReader
{
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static List<ImportFileRow> readCsvFile(ImportFileResult result, String fileContent, Persistence persistence, User activeUser) throws PersistenceException
    {
        List<ImportFileRow> parsedRows = new ArrayList<>();

        Scanner scanner = null;

        try
        {
            //
            // Klasse/hold hashmap
            //
            Map<String, UserGroup> groupMap = new HashMap<>();
            List<UserGroup> groups = persistence.getUserGroups();
            for (UserGroup group : groups)
            {
                groupMap.put(group.getName(), group);
            }

            //
            // Parse alle linjer og gem i parsedRows liste.
            //
            int rowIndex = 0;
            scanner = new Scanner(fileContent);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                rowIndex++;

                // NOTE: Skip første linje med headers
                if (rowIndex == 1)
                {
                    continue;
                }

                if (line.trim().length() == 0)
                {
                    continue;
                }

                // NOTE: https://stackoverflow.com/a/13939902, -1 så vi beholder tomme celler.
                String[] cells = line.split(",", -1);
                if (cells.length != 6)
                {
                    result.addError(new ImportFileError(rowIndex, "Must have exactly 6 columns, but found " + cells.length));
                    continue;
                }

                String roomString = cells[0];
                String dateString = cells[1];
                String startTimeString = cells[2];
                String endTimeString = cells[3];
                String groupString = cells[4];
                String allowOverlapString = cells[5];

                Room room = null;
                BookingInterval interval;
                UserGroup group = null;
                boolean isOverlapAllowed = false;

                //
                // Lokale
                //
                room = persistence.getRoom(roomString, null);
                if (room == null)
                {
                    result.addError(new ImportFileError(rowIndex, "Room not found"));
                    continue;
                }

                //
                // Date + tid
                //
                try
                {
                    interval = new BookingInterval(
                        LocalDate.parse(dateString, dateFormatter),
                        LocalTime.parse(startTimeString, timeFormatter),
                        LocalTime.parse(endTimeString, timeFormatter)
                    );
                }
                catch (DateTimeParseException e)
                {
                    result.addError(new ImportFileError(rowIndex, "Invalid date or time."));
                    continue;
                }

                //
                // Klasse/hold
                //
                if (groupString.length() != 0)
                {
                    group = groupMap.getOrDefault(groupString, null);
                    if (group == null)
                    {
                        result.addError(new ImportFileError(rowIndex, "Group not found."));
                        continue;
                    }
                }

                //
                // Tillad overlap
                //
                if (allowOverlapString.length() != 0)
                {
                    isOverlapAllowed = true;
                }

                parsedRows.add(
                    new ImportFileRow(
                        rowIndex,
                        isOverlapAllowed,
                        new Booking(
                            // NOTE: Bruger negative id, for at sikre at ny id ikke clasher med id fra database.
                            rowIndex + Integer.MIN_VALUE,
                            interval,
                            room,
                            activeUser,
                            group
                        )
                    )
                );
            }
        }
        finally
        {
            if (scanner != null)
            {
                scanner.close();
            }
        }

        return parsedRows;
    }
}
