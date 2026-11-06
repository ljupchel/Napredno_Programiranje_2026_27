package labs.lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// ===================== Main: command interpreter =====================
//
// Reads commands from standard input (one per line) and calls the matching constructor / method.
//
//   LOT <capacity>                  new labs.lab1.ParkingLot(capacity)
//   NEW_HOURLY <plate> <minutes>    new labs.lab1.HourlyTicket(plate, minutes)   -> prints type, plate, minutes, price()
//   NEW_DAILY  <plate> <minutes>    new labs.lab1.DailyTicket(plate, minutes)    -> prints type, plate, minutes, price()
//   ADD_HOURLY <plate> <minutes>    lot.addTicket(new labs.lab1.HourlyTicket(plate, minutes))
//   ADD_DAILY  <plate> <minutes>    lot.addTicket(new labs.lab1.DailyTicket(plate, minutes))
//   ADD_NULL                        lot.addTicket(null)
//   COUNT                           lot.getTicketCount()
//   REVENUE                         lot.totalRevenue()
//   END                             stop reading
//
// In <plate>, the word NULL means a null plate and the word EMPTY means the empty string "".

// ===================== Part A: solution classes =====================

/*
TODO: your code here
*/

class LotFullException extends Exception {
    public LotFullException() {
    }
}


abstract class Ticket {
    private final String plate;
    private final int minutes;

    protected Ticket(String plate, int minutes) {
        if (plate == null || plate.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.plate = plate;
        if (minutes <= 0) {
            throw new IllegalArgumentException();
        }
        this.minutes = minutes;
    }

    public String getPlate() {
        return plate;
    }

    public int getMinutes() {
        return minutes;
    }

    public abstract double price();

    @Override
    public String toString() {
        return String.format("%s %d", plate, minutes);
    }
}

class HourlyTicket extends Ticket {

    public HourlyTicket(String plate, int minutes) {
        super(plate, minutes);
    }

    @Override
    public double price() {
        double hours = (getMinutes() + 59) / 60;
        return hours * 50;
    }
}

class DailyTicket extends Ticket {
    public DailyTicket(String plate, int minutes) {
        super(plate, minutes);
    }

    @Override
    public double price() {
        int days = (getMinutes() + 1439) / 1440;
        return days * 300;
    }
}

class ParkingLot {
    private Ticket[] tickets;
    private int ticketCount;
    private int capacity;

    public ParkingLot(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.tickets = new Ticket[capacity];
        this.ticketCount = 0;
    }

    public void addTicket(Ticket ticket) throws LotFullException {
        if (ticket == null) {
            throw new IllegalArgumentException();
        } else {
            if (ticketCount >= capacity) {
                throw new LotFullException();
            } else {
                tickets[ticketCount] = ticket;
                ticketCount++;
            }
        }
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public double totalRevenue() {
        double sum = 0;
        if (ticketCount == 0) {
            return 0.0;
        } else {
            for (int i = 0; i < ticketCount; i++) {
                sum += tickets[i].price();
            }
        }
        return sum;
    }
}



public class Lab1 {
    private static ParkingLot lot = null;

    private static String plateArg(String token) {
        if (token.equals("NULL")) return null;
        if (token.equals("EMPTY")) return "";
        return token;
    }

    private static void addToLot(Ticket ticket) {
        // 'ticket' may be null (ADD_NULL) or the constructor may already have failed (handled by the caller)
        try {
            lot.addTicket(ticket);
            System.out.println("Added");
        } catch (LotFullException e) {
            System.out.println("labs.lab1.LotFullException");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            String cmd = p[0];
            if (cmd.equals("END")) break;

            switch (cmd) {
                case "LOT":
                    try {
                        lot = new ParkingLot(Integer.parseInt(p[1]));
                        System.out.println("Lot created (capacity " + p[1] + ")");
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException");
                    }
                    break;

                case "NEW_HOURLY":
                case "NEW_DAILY":
                    try {
                        Ticket t = cmd.equals("NEW_HOURLY")
                                ? new HourlyTicket(plateArg(p[1]), Integer.parseInt(p[2]))
                                : new DailyTicket(plateArg(p[1]), Integer.parseInt(p[2]));
                        System.out.println((cmd.equals("NEW_HOURLY") ? "labs.lab1.HourlyTicket " : "labs.lab1.DailyTicket ")
                                + t.getPlate() + " " + t.getMinutes() + " " + t.price());
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException");
                    }
                    break;

                case "ADD_HOURLY":
                case "ADD_DAILY":
                    if (lot == null) {
                        System.out.println("No lot created");
                        break;
                    }
                    Ticket ticket;
                    try {
                        ticket = cmd.equals("ADD_HOURLY")
                                ? new HourlyTicket(plateArg(p[1]), Integer.parseInt(p[2]))
                                : new DailyTicket(plateArg(p[1]), Integer.parseInt(p[2]));
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException");
                        break;
                    }
                    addToLot(ticket);
                    break;

                case "ADD_NULL":
                    if (lot == null) {
                        System.out.println("No lot created");
                    } else {
                        addToLot(null);
                    }
                    break;

                case "COUNT":
                    System.out.println(lot == null ? "No lot created" : "Count: " + lot.getTicketCount());
                    break;

                case "REVENUE":
                    System.out.println(lot == null ? "No lot created" : "Revenue: " + lot.totalRevenue());
                    break;

                default:
                    System.out.println("Unknown command");
            }
        }
    }
}