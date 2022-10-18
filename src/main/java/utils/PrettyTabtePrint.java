package utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PrettyTabtePrint {

    public static String processResultSet(ResultSet rs) throws Exception {
        StringBuilder sb = new StringBuilder();

        ResultSetMetaData rsmd = rs.getMetaData();
        int totalCols = rsmd.getColumnCount();
        int[] colCounts = new int[totalCols];
        String[] colLabels = new String[totalCols];
        for (int i = 0; i < totalCols; i++) {
            colCounts[i] = rsmd.getColumnDisplaySize(i + 1);
            colLabels[i] = rsmd.getColumnLabel(i + 1);
            if (colLabels[i].length() > colCounts[i]) {
                colLabels[i] = colLabels[i].substring(0, colCounts[i]);
            }
            sb.append(String.format("| %" + colCounts[i] + "s ", colLabels[i]));
        }
        sb.append("|\n");

        String horizontalLine = getHorizontalLine(colCounts);
        while (rs.next()) {
            sb.append(horizontalLine);
            for (int i = 0; i < totalCols; i++) {
                sb.append(String.format("| %" + colCounts[i] + "s ", rs.getString(i + 1)));
            }
            sb.append("|\n");

        }

        return (getHorizontalLine(colCounts) + sb.toString());
    }


    private static String getHorizontalLine(int[] colCounts) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < colCounts.length; i++) {
            sb.append("+");
            for (int j = 0; j < colCounts[i] + 2; j++) {
                sb.append("-");
            }
        }
        sb.append("+\n");

        return sb.toString();
    }
}
