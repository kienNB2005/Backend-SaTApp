# --- BƯỚC 1: Build ứng dụng Java 21 bằng Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file cấu hình maven trước để cache dependencies (tăng tốc build các lần sau)
COPY pom.xml .

# Tải trước tất cả các thư viện/dependencies về để lưu cache (không tải lại khi sửa code)
RUN mvn dependency:go-offline -B

# Copy source code và tiến hành build package (bỏ qua chạy thử nghiệm test)
COPY src ./src
RUN mvn package -DskipTests

# --- BƯỚC 2: Chạy ứng dụng bằng JRE 21 siêu nhẹ ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose port (Spring Boot thường chạy cổng 8080)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
