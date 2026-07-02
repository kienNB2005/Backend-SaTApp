package ken.example.dekiru.attendance.service;

import ken.example.dekiru.attendance.entity.CheckoutEvent;
import ken.example.dekiru.attendance.repository.AttendanceRepository;
import ken.example.dekiru.attendance.repository.CheckoutEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutEventService {
    private final CheckoutEventRepository checkoutEventRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceSseService attendanceSseService;

    @Transactional
    public void closeExpiredCheckoutEvent(Long eventId) {
        CheckoutEvent event = checkoutEventRepository.findById(eventId)
                .orElse(null);

        if (event == null || event.getClosedAt() != null) {
            return;
        }

        attendanceRepository.markLeftEarlyForSession(
                event.getClassSession().getId(),
                event
        );

        event.setClosedAt(LocalDateTime.now());
        checkoutEventRepository.save(event);

        attendanceSseService.pushLeftEarlyUpdate(event.getClassSession().getId());
    }
}