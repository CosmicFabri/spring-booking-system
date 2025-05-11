package com.spring.spring_booking_system.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.repositories.BookingRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingReminderService {
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public BookingReminderService(BookingRepository bookingRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    // It executes hourly (cron = "0 0 * * * *")
    @Scheduled(cron = "0 0 * * * *")
    public void checkAndSendReminders() {
        System.out.println("Checking for bookings to remind");

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalTime currentHour = LocalTime.now().withNano(0);

        List<Booking> bookings = bookingRepository.findByDayAndStartHour(tomorrow, currentHour);

        for (Booking booking : bookings) {
            String spaceName = booking.getSpace().getName();
            emailService.sendReminderEmail(booking.getUser(), booking, spaceName);
            System.out.println("Reminder sent to " + booking.getUser().getEmail());
        }
    }
}
