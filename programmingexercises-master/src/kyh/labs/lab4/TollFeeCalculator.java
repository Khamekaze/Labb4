package kyh.labs.lab4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length]; //Removed -1 from length
            for(int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the inputfile is" + getTotalFeeCost(dates));
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        } catch(NoSuchElementException e) {
            System.err.println("No such element " + inputFile);
        } catch(DateTimeParseException e) {
            System.err.println("Date time format incorrect " + inputFile);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Array index out of bounds " + inputFile);
        }
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int day = intervalStart.getDayOfMonth();
        int hour = intervalStart.getHour();
        for(LocalDateTime date: dates) {
            if(date.getDayOfMonth() == day) {
                System.out.println(date.toString());
                if(date.getHour() >= 6 && intervalStart.getHour() < 6) {
                    intervalStart = date;
                }
                long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
                if(diffInMinutes >= 60) {
                    totalFee += getTollFeePerPassing(date);
                    intervalStart = date;
                } else if(diffInMinutes <= -60) {
                    totalFee += getTollFeePerPassing(date);
                } else {
                    if(date.equals(intervalStart)) {
                        totalFee += getTollFeePerPassing(date);
                    } else {
                        if(getTollFeePerPassing(date) > getTollFeePerPassing(intervalStart)) {
                            int difference = getTollFeePerPassing(date) - getTollFeePerPassing(intervalStart);
                            totalFee += difference;
                        }
                    }
                }
            }
        }
        return Math.min(totalFee, 60);
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 8 && minute >= 30 && minute <= 59) return 8; //Added condition for check between 08:30 and 8:59
        else if (hour >= 9 && hour <= 14 && minute <= 59) return 8; //Cleaned up condition
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 30 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("testData/Lab4.txt");
    }
}
