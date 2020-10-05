package kyh.labs.lab4;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TollFeeCalculatorTests {

    @Test
    void correctFeeRangeOfDay() {
        LocalDateTime date = LocalDateTime.parse("2020-10-02 05:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime date2 = LocalDateTime.parse("2020-10-02 06:45", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime date3 = LocalDateTime.parse("2020-10-02 06:51", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime[] dates = new LocalDateTime[3];
        dates[0] = date;
        dates[1] = date2;
        dates[2] = date3;
        assertEquals(13, TollFeeCalculator.getTotalFeeCost(dates),
                "Passings outside of the assigned time range were counted");
    }

    @Test
    void feeForOnlySingleDay() {
        LocalDateTime date = LocalDateTime.parse("2020-10-01 10:34", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime date2 = LocalDateTime.parse("2020-10-02 10:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime[] dates = new LocalDateTime[2];
        dates[0] = date;
        dates[1] = date2;
        assertEquals(8, TollFeeCalculator.getTotalFeeCost(dates),
                "Fees for multiple days were counted");
    }

    @Test
    void chronologicalOrder() {
        LocalDateTime date = LocalDateTime.parse("2020-10-02 10:34", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime date2 = LocalDateTime.parse("2020-10-02 10:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime[] dates = new LocalDateTime[2];
        dates[0] = date;
        dates[1] = date2;
        assertEquals(8, TollFeeCalculator.getTotalFeeCost(dates),
                "Non-chronological dates were handled incorrectly");
    }

    @Test
    void dateTimeParse() {
        File f = new File("./testData/incorrectDataFormat.txt");
        boolean result;
        try {
            result = f.createNewFile();
            if(result) {
                System.out.println(f.getCanonicalPath());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(f);
            writer.write("2020-10-02 12;29, 2020-10-02 15/29");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DateTimeParseException e = null;

        try {
            new TollFeeCalculator("testData/incorrectDataFormat.txt");
        } catch(DateTimeParseException d) {
            e = d;
        }

        assertNull(e, "The provided data was incorrectly formatted");
    }

    @Test
    void arrayIndexBounds() {
        File f = new File("./testData/singleEntryFile.txt");
        boolean result;
        try {
            result = f.createNewFile();
            if(result) {
                System.out.println(f.getCanonicalPath());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(f);
            writer.write("2020-10-02 15:29");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayIndexOutOfBoundsException e = null;

        try {
            new TollFeeCalculator("testData/singleEntryFile.txt");
        } catch(ArrayIndexOutOfBoundsException a) {
            e = a;
        }

        assertNull(e, "The array index was out of bounds");
    }

    @Test
    void noSuchElementInFile() {
        File f = new File("./testData/emptyFile.txt");
        boolean result;
        try {
            result = f.createNewFile();
            if(result) {
                System.out.println(f.getCanonicalPath());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NoSuchElementException e = null;
        try {
            new TollFeeCalculator("testData/emptyFile.txt");
        } catch(NoSuchElementException n) {
            e = n;
        }

        assertNull(e, "The provided file was empty");
    }

    @Test
    void costForMultiplePassings() {
        LocalDateTime[] newDates = new LocalDateTime[2];
        LocalDateTime d = LocalDateTime.parse("2020-10-02 15:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d2 = LocalDateTime.parse("2020-10-02 15:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        newDates[0] = d;
        newDates[1] = d2;
        assertEquals(18, TollFeeCalculator.getTotalFeeCost(newDates),
                "Returned incorrect fee for dates within an hour of each other");
    }

    @Test
    void maxValueOfDayTotal() {
        LocalDateTime[] newDates = new LocalDateTime[11];
        LocalDateTime d = LocalDateTime.parse("2020-10-02 06:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d2 = LocalDateTime.parse("2020-10-02 07:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d3 = LocalDateTime.parse("2020-10-02 08:07", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d4 = LocalDateTime.parse("2020-10-02 09:08", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d5 = LocalDateTime.parse("2020-10-02 10:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d6 = LocalDateTime.parse("2020-10-02 11:10", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d7 = LocalDateTime.parse("2020-10-02 12:11", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d8 = LocalDateTime.parse("2020-10-02 13:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d9 = LocalDateTime.parse("2020-10-02 14:13", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d10 = LocalDateTime.parse("2020-10-02 15:14", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime d11 = LocalDateTime.parse("2020-10-02 16:15", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        newDates[0] = d;
        newDates[1] = d2;
        newDates[2] = d3;
        newDates[3] = d4;
        newDates[4] = d5;
        newDates[5] = d6;
        newDates[6] = d7;
        newDates[7] = d8;
        newDates[8] = d9;
        newDates[9] = d10;
        newDates[10] = d11;

        assertEquals(60, TollFeeCalculator.getTotalFeeCost(newDates),
                "Returned a value greater than the max value for a day");
    }

    @Test
    void minValueOfDayTotal() {
        LocalDateTime[] dates = new LocalDateTime[1];
        LocalDateTime date1 = LocalDateTime.parse("2020-10-02 15:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        dates[0] = date1;
        assertEquals(13, TollFeeCalculator.getTotalFeeCost(dates),
                "Returned a higher value than the total fee cost");
    }

    @Test
    void correctFeePerTime() {
        LocalDateTime date1 = LocalDateTime.parse("2020-10-02 15:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertEquals(13, TollFeeCalculator.getTollFeePerPassing(date1),
                "Returned incorrect value for the date that was checked");

        LocalDateTime date2 = LocalDateTime.parse("2020-10-02 09:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertEquals(8, TollFeeCalculator.getTollFeePerPassing(date2),
                "Returned incorrect value for the date that was checked");
    }
}
