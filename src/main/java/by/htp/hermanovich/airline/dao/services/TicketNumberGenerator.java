package by.htp.hermanovich.airline.dao.services;

import java.util.Random;

/**
 * Description: This class describes actions to generate an unique ticket number.
 *
 * Created by Yauheni Hermanovich on 24.07.2017.
 */
public class TicketNumberGenerator {
    private volatile static TicketNumberGenerator instance;
    private static String charString = "abcdefghijklmnopqrstuvwxyz";
    private static final int ticketNumberLength = 6;

    public TicketNumberGenerator() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static TicketNumberGenerator getInstance() {
        if (instance == null) {
            synchronized (TicketNumberGenerator.class) {
                if (instance == null) {
                    instance = new TicketNumberGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * This method generates an unique ticket number for the client.
     *
     * @return  - an unique string that means an unique ticket number.
     */
    public String generateTicketNumber() {
        Random random = new Random();
        char[] number = new char[ticketNumberLength];
        for (int i = 0; i < ticketNumberLength; i++) {
            number[i] = charString.charAt(random.nextInt(charString.length()));
        }
        return String.valueOf(number).toUpperCase();
    }
}