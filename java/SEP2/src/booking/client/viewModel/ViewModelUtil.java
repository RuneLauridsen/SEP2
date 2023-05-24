package booking.client.viewModel;

import booking.server.model.importFile.ImportFileError;
import booking.server.model.importFile.ImportFileOverlap;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.Overlap;
import booking.shared.objects.User;

import java.util.List;

public class ViewModelUtil
{
    public static String getImportFileResultDisplayText(ImportFileResult result)
    {
        StringBuilder message = new StringBuilder();
        message.append("Bookings not imported:");
        message.append("\n");

        for (ImportFileError error : result.getErrors())
        {
            message.append(error);
            message.append("\n");
        }

        for (ImportFileOverlap overlap : result.getOverlaps())
        {
            message.append(overlap);
            message.append("\n");

            for (User user : overlap.getOverlap().getUsers())
            {
                message.append("\t");
                message.append(user);
                message.append("\n");
            }
        }

        return message.toString();
    }

    public static String getOverlapsDisplayText(List<Overlap> overlaps)
    {
        StringBuilder message = new StringBuilder();
        message.append("Overlap with existing booking(s):");
        message.append("\n");

        for (Overlap overlap : overlaps)
        {
            message.append(overlap);
            message.append("\n");

            for (User user : overlap.getUsers())
            {
                message.append("\t");
                message.append(user);
                message.append("\n");
            }
        }

        return message.toString();
    }
}
