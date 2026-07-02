package ken.example.dekiru.attendance.scheduler;

import ken.example.dekiru.attendance.service.CheckoutEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class CheckoutDeadlineScheduler {
    private final TaskScheduler taskScheduler;
    private final CheckoutEventService checkoutEventService;

    public void scheduleClose(Long eventId, LocalDateTime deadlineAt) {
        Instant instant = deadlineAt
                .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                .toInstant();

        taskScheduler.schedule(
                () -> checkoutEventService.closeExpiredCheckoutEvent(eventId),
                instant
        );
    }
}