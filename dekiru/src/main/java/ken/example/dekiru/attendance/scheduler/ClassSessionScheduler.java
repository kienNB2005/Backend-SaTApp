package ken.example.dekiru.attendance.scheduler;

import ken.example.dekiru.attendance.entity.Attendance;
import ken.example.dekiru.attendance.entity.ClassSession;
import ken.example.dekiru.attendance.repository.AttendanceRepository;
import ken.example.dekiru.attendance.repository.ClassSessionRepository;
import ken.example.dekiru.student.entity.Student;
import ken.example.dekiru.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClassSessionScheduler {

    private final ClassSessionRepository classSessionRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh") // Runs at midnight every day
    @Transactional
    public void closeOpenSessions() {
        log.info("Running scheduled task to close open and scheduled class sessions...");
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // 1. Đóng các buổi học đang 'open' (giảng viên quên đóng)
        List<ClassSession> openSessions = classSessionRepository.findByStatusAndSessionDateBefore(ClassSession.Status.open, today);
        if (!openSessions.isEmpty()) {
            for (ClassSession session : openSessions) {
                session.setStatus(ClassSession.Status.closed);
                session.setClosedAt(now);
                session.setQrToken(null);
                session.setQrExpiresAt(null);
            }
            classSessionRepository.saveAll(openSessions);
            log.info("Successfully closed {} open class sessions.", openSessions.size());
        } else {
            log.info("No open class sessions found to close.");
        }

        // 2. Xử lý các buổi học 'scheduled' bị bỏ quên (giảng viên không mở điểm danh)
        List<ClassSession> scheduledSessions = classSessionRepository.findByStatusAndSessionDateBefore(ClassSession.Status.scheduled, today);
        if (!scheduledSessions.isEmpty()) {
            List<Attendance> newAttendances = new ArrayList<>();
            for (ClassSession session : scheduledSessions) {
                // Lấy danh sách sinh viên của lớp học
                String adminClassCode = session.getSchedule().getAdminClass().getCode();
                List<Student> students = studentRepository.findAllByAdminClass_Code(adminClassCode);
                
                for (Student student : students) {
                    Attendance attendance = Attendance.builder()
                            .classSession(session)
                            .student(student)
                            .status(Attendance.Status.present) // Mặc định có mặt để bảo vệ quyền lợi sinh viên
                            .isLate(false)
                            .leftEarly(false)
                            .build();
                    newAttendances.add(attendance);
                }

                // Cập nhật trạng thái buổi học
                session.setStatus(ClassSession.Status.closed);
                session.setClosedAt(now);
            }
            // Lưu toàn bộ điểm danh và cập nhật buổi học
            attendanceRepository.saveAll(newAttendances);
            classSessionRepository.saveAll(scheduledSessions);
            
            log.info("Successfully closed {} scheduled class sessions and marked {} students as present.", scheduledSessions.size(), newAttendances.size());
        } else {
            log.info("No scheduled class sessions found in the past.");
        }
    }
}

