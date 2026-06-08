package ken.example.dekiru.attendance.controller;

import ken.example.dekiru.attendance.dto.ApproveSessionRequest;
import ken.example.dekiru.attendance.dto.CancelSessionRequest;
import ken.example.dekiru.attendance.dto.ClassSessionRequestResponse;
import ken.example.dekiru.attendance.dto.MakeupSessionRequest;
import ken.example.dekiru.attendance.service.ClassSessionRequestService;
import ken.example.dekiru.common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session-requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassSessionRequestController {

    ClassSessionRequestService requestService;

    // --- LECTURER API ---
    @PostMapping("/{sessionId}/cancel")
    @PreAuthorize("hasRole('LECTURER')")
    public ApiResponse<ClassSessionRequestResponse> createCancelRequest(
            @PathVariable Long sessionId,
            @RequestBody CancelSessionRequest request) {
        ClassSessionRequestResponse result = requestService.createCancelRequest(sessionId, request);
        return ApiResponse.success(result, "Đã gửi yêu cầu hủy buổi học");
    }

    @PostMapping("/{originalSessionId}/makeup")
    @PreAuthorize("hasRole('LECTURER')")
    public ApiResponse<ClassSessionRequestResponse> createMakeupRequest(
            @PathVariable Long originalSessionId,
            @RequestBody MakeupSessionRequest request) {
        ClassSessionRequestResponse result = requestService.createMakeupRequest(originalSessionId, request);
        return ApiResponse.success(result, "Đã gửi yêu cầu tạo lịch học bù");
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('LECTURER')")
    public ApiResponse<List<ClassSessionRequestResponse>> getMyRequests() {
        List<ClassSessionRequestResponse> result = requestService.getMyRequests();
        return ApiResponse.success(result, "Lấy danh sách thành công");
    }

    // --- ADMIN API ---
    @GetMapping("/admin/pending-cancels")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ClassSessionRequestResponse>> getPendingCancelRequests() {
        List<ClassSessionRequestResponse> result = requestService.getPendingCancelRequests();
        return ApiResponse.success(result, "Lấy danh sách thành công");
    }

    @GetMapping("/admin/pending-makeups")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ClassSessionRequestResponse>> getPendingMakeupRequests() {
        List<ClassSessionRequestResponse> result = requestService.getPendingMakeupRequests();
        return ApiResponse.success(result, "Lấy danh sách thành công");
    }

    @PostMapping("/admin/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassSessionRequestResponse> approveRequest(
            @PathVariable Long id,
            @RequestParam String type) { // type = "cancel" or "makeup"
        ClassSessionRequestResponse result = requestService.approveRequest(id, type);
        return ApiResponse.success(result, "Phê duyệt thành công");
    }

    @PostMapping("/admin/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassSessionRequestResponse> rejectRequest(
            @PathVariable Long id,
            @RequestParam String type, // type = "cancel" or "makeup"
            @RequestBody ApproveSessionRequest request) {
        ClassSessionRequestResponse result = requestService.rejectRequest(id, type, request);
        return ApiResponse.success(result, "Đã từ chối yêu cầu");
    }
}
