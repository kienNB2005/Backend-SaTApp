package ken.example.dekiru.attendance.mapper;

import ken.example.dekiru.academic.entity.AdministrativeClass;
import ken.example.dekiru.academic.entity.Lecturer;
import ken.example.dekiru.academic.entity.Room;
import ken.example.dekiru.academic.entity.Subject;
import ken.example.dekiru.attendance.dto.ClassSessionRequestResponse;
import ken.example.dekiru.attendance.entity.ClassSession;
import ken.example.dekiru.attendance.entity.ClassSessionRequest;
import ken.example.dekiru.schedule.entity.Schedule;
import ken.example.dekiru.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassSessionRequestMapper {

    ClassSessionRequestResponse toResponse(ClassSessionRequest entity);

    List<ClassSessionRequestResponse> toResponseList(List<ClassSessionRequest> entities);

    // --- Sub-type mappings (MapStruct tự dùng khi gặp kiểu tương ứng) ---

    ClassSessionRequestResponse.LecturerInfo toLecturerInfo(Lecturer lecturer);

    @Mapping(source = "fullName", target = "fullName")
    ClassSessionRequestResponse.UserInfo toUserInfo(User user);

    ClassSessionRequestResponse.ClassSessionInfo toClassSessionInfo(ClassSession classSession);

    ClassSessionRequestResponse.ScheduleInfo toScheduleInfo(Schedule schedule);

    ClassSessionRequestResponse.SubjectInfo toSubjectInfo(Subject subject);

    @Mapping(source = "code", target = "code")
    ClassSessionRequestResponse.AdminClassInfo toAdminClassInfo(AdministrativeClass adminClass);

    ClassSessionRequestResponse.RoomInfo toRoomInfo(Room room);
}
