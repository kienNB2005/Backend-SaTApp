package ken.example.dekiru.attendance.dto;

import ken.example.dekiru.attendance.entity.RequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassSessionRequestResponse {
    Long id;
    LecturerInfo lecturer;
    ClassSessionInfo classSession;
    RequestStatus cancelStatus;
    RequestStatus makeupStatus;
    String cancelReason;
    LocalDate makeupDate;
    Integer makeupPeriodStart;
    Integer makeupPeriodEnd;
    RoomInfo makeupRoom;
    LocalDateTime createdAt;
    String rejectReason;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LecturerInfo {
        String lecturerCode;
        UserInfo user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserInfo {
        String fullName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ClassSessionInfo {
        Long id;
        LocalDate sessionDate;
        Byte actualPeriodStart;
        Byte actualPeriodEnd;
        ScheduleInfo schedule;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ScheduleInfo {
        SubjectInfo subject;
        AdminClassInfo adminClass;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SubjectInfo {
        String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AdminClassInfo {
        String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoomInfo {
        String code;
    }
}
