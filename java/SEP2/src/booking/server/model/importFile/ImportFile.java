package booking.server.model.importFile;

import booking.server.persistene.Persistence;
import booking.server.model.overlapCheck.OverlapChecker;
import booking.shared.objects.Booking;
import booking.shared.objects.Overlap;
import booking.shared.objects.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportFile
{
    public static ImportFileResult importFile(String fileContent, User activeUser, Persistence persistence)
    {
        ImportFileResult result = new ImportFileResult();

        //
        // Parse datafil
        //
        List<ImportFileRow> rows = ImportFileCsvReader.readCsvFile(result, fileContent, persistence, activeUser);
        if (!result.isOk())
        {
            return result;
        }

        //
        // Grupper efter dato
        //
        Map<LocalDate, List<ImportFileRow>> groupedByDate = new HashMap<>();
        for (ImportFileRow row : rows)
        {
            // NOTE(rune): Overlap check kan isoleres pr. dato, da samme booking aldrig dækker flere dage.
            LocalDate date = row.getBooking().getInterval().getDate();

            if (groupedByDate.containsKey(date) == false)
            {
                groupedByDate.put(date, new ArrayList<>());
            }

            groupedByDate.get(date).add(row);
        }

        //
        // Check efter overlap for hver dato. Finder først alle relevante bookinger,
        // og sender dem derefter videre til OverlapChecker.checkOverlap
        //
        for (List<ImportFileRow> thisDateRows : groupedByDate.values())
        {
            List<Booking> thisDateBookings = new ArrayList<>(thisDateRows.size());
            for (ImportFileRow row : thisDateRows)
            {
                thisDateBookings.add(row.getBooking());
            }

            List<Booking> relevantBookings = OverlapChecker.getRelevantBookings(
                thisDateBookings,
                persistence
            );

            for (ImportFileRow thisDateRow : thisDateRows)
            {
                if (thisDateRow.isOverlapAllowed() == false)
                {
                    List<Overlap> overlaps = OverlapChecker.getOverlaps(
                        thisDateRow.getBooking(),
                        relevantBookings,
                        persistence
                    );

                    for (Overlap overlap : overlaps)
                    {
                        result.addOverlap(new ImportFileOverlap(
                            thisDateRow.getRow(),
                            overlap
                        ));
                    }
                }
            }
        }

        //
        // Hvis ingen fejl/overlap kan vi godt indsætte
        //
        if (result.isOk())
        {
            for (ImportFileRow row : rows)
            {
                persistence.createBooking(
                    activeUser,
                    row.getBooking().getRoom(),
                    row.getBooking().getInterval(),
                    row.getBooking().getUserGroup()
                );
            }
        }

        return result;
    }
}
