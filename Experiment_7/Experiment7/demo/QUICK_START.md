# Quick Start Guide - HealthHub JPA Application

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Steps to Run

1. **Navigate to project directory**
```bash
cd demo
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Verify startup**
```
Application started successfully on port 8080
========== Initializing HealthHub Sample Data ==========
Creating sample patients...
Created 3 patients
Created 3 doctors
Created 3 appointments
Created 3 prescriptions
========== Sample Data Initialization Complete ==========
```

---

## Testing the API

### Method 1: Using curl

```bash
# Get all patients (with pagination)
curl "http://localhost:8080/api/v1/patients?page=0&size=10"

# Search patients
curl "http://localhost:8080/api/v1/patients/search?term=raj"

# Get all doctors
curl "http://localhost:8080/api/v1/doctors?page=0&size=10"

# Get available doctors
curl "http://localhost:8080/api/v1/doctors/available?maxAppointments=10"

# Get all appointments (naive - demonstrates N+1)
curl "http://localhost:8080/api/v1/appointments/naive-all"

# Get all appointments (optimized)
curl "http://localhost:8080/api/v1/appointments/optimized-all"

# Run performance test
curl "http://localhost:8080/api/v1/appointments/performance-test"

# Get prescriptions
curl "http://localhost:8080/api/v1/prescriptions/active?page=0&size=10"
```

### Method 2: Using Postman

1. Create new collection: "HealthHub API"
2. Create requests for each endpoint
3. Set Authorization and Content-Type headers
4. Test pagination with different page numbers
5. Observe N+1 problem in console logs

### Method 3: Using HTTP Client (IntelliJ)

Create a file `api-test.http`:

```http
### Get all patients
GET http://localhost:8080/api/v1/patients?page=0&size=10
Content-Type: application/json

### Create patient
POST http://localhost:8080/api/v1/patients
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "Patient",
  "email": "test@example.com",
  "phoneNumber": "9999999999",
  "dateOfBirth": "1990-01-01",
  "medicalHistory": "None",
  "address": "Test Address"
}

### N+1 Problem Demo
GET http://localhost:8080/api/v1/appointments/naive-all
Content-Type: application/json

### Optimized Query
GET http://localhost:8080/api/v1/appointments/optimized-all
Content-Type: application/json

### Performance Test
GET http://localhost:8080/api/v1/appointments/performance-test
Content-Type: application/json
```

---

## Key Testing Scenarios

### 1. Pagination Testing

```bash
# First page
curl "http://localhost:8080/api/v1/patients?page=0&size=5"

# Second page
curl "http://localhost:8080/api/v1/patients?page=1&size=5"

# With sorting
curl "http://localhost:8080/api/v1/patients?page=0&size=10&sort=id,desc"
curl "http://localhost:8080/api/v1/patients?page=0&size=10&sort=firstName,asc"
```

### 2. Search & Filter Testing

```bash
# Search by name
curl "http://localhost:8080/api/v1/patients/search?term=raj"

# Filter by specialization
curl "http://localhost:8080/api/v1/doctors/specialization/Cardiology"

# Filter by experience
curl "http://localhost:8080/api/v1/doctors/experience?minYears=8&maxYears=15"

# Combined filter
curl "http://localhost:8080/api/v1/doctors/filter?specialization=Cardiology&minExp=10"
```

### 3. N+1 Problem Analysis

**Watch the Console Logs When Calling:**

```bash
# THIS ENDPOINT CAUSES N+1 PROBLEM - Watch logs!
curl "http://localhost:8080/api/v1/appointments/naive-all"
```

Expected log output:
```
Hibernate: select appointment0_.id as id1_0_, ...  # 1 query for appointments
Hibernate: select patient0_.id as id1_1_, ...     # N queries for patients
Hibernate: select doctor0_.id as id1_2_, ...      # N queries for doctors
```

```bash
# THIS ENDPOINT IS OPTIMIZED - Single query!
curl "http://localhost:8080/api/v1/appointments/optimized-all"
```

Expected log output:
```
Hibernate: select distinct appointment0_.id as id1_0_, patient1_.id as id1_1_, doctor2_.id as id1_2_, ...
# Single joined query with all data!
```

### 4. Performance Comparison

```bash
curl "http://localhost:8080/api/v1/appointments/performance-test"
```

Response shows:
```json
{
  "naiveQueryTime": 245,
  "optimizedQueryTime": 12,
  "recordsCount": 50,
  "timeSaved": 233,
  "improvementPercentage": 95.1
}
```

---

## H2 Database Console

Access the H2 database web console:

**URL:** `http://localhost:8080/h2-console`

**Login Credentials:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)
- Driver Class: `org.h2.Driver`

**Execute SQL Queries:**
```sql
-- View patient data
SELECT * FROM patient;

-- View appointments
SELECT * FROM appointment;

-- Check indexes
SELECT * FROM information_schema.indexes 
WHERE table_name = 'PATIENT';

-- Count records
SELECT COUNT(*) FROM patient;
SELECT COUNT(*) FROM appointment;
SELECT COUNT(*) FROM doctor;
SELECT COUNT(*) FROM prescription;
```

---

## Monitoring Performance

### Enable SQL Logging

Edit `application.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Check Query Execution Time

Console logs show timing information:
```
Hibernate: ...  # Shows the SQL query
-- execution time: milliseconds
```

### Hibernate Statistics

Can be enabled in `application.properties`:
```properties
spring.jpa.properties.hibernate.generate_statistics=true
```

Then add logging:
```properties
logging.level.org.hibernate.stat=DEBUG
```

---

## Sample API Response - Performance Test

```json
{
  "naiveQueryTime": 245,
  "optimizedQueryTime": 12,
  "recordsCount": 50,
  "timeSaved": 233,
  "improvementPercentage": 95.10204081632654
}
```

**Interpretation:**
- ⚠️ Naive approach (N+1 problem): **245ms** to fetch 50 appointments
- ✅ Optimized approach (JOIN FETCH): **12ms** to fetch 50 appointments
- 🚀 Performance improvement: **95%** faster!

---

## Common Issues & Solutions

### Issue: Port 8080 already in use
**Solution:** Change port in `application.properties`
```properties
server.port=8081
```

### Issue: Database not found
**Solution:** Check H2 is added to dependencies in `pom.xml`

### Issue: No sample data on startup
**Solution:** 
1. Check `DataInitializer.java` has `@Component` annotation
2. Check logs for initialization messages
3. Restart application

### Issue: SQL logs not showing
**Solution:**
1. Verify `spring.jpa.show-sql=true` in `application.properties`
2. Check logging level: `logging.level.org.hibernate.SQL=DEBUG`
3. Restart application

### Issue: H2 Console Access Denied
**Solution:**
1. Ensure `spring.h2.console.enabled=true`
2. Use correct URL: `http://localhost:8080/h2-console`
3. Clear browser cache and try again

---

## Project Structure for Reference

```
src/
└── main/
    ├── java/com/example/demo/
    │   ├── entity/
    │   │   ├── Patient.java
    │   │   ├── Doctor.java
    │   │   ├── Appointment.java
    │   │   └── Prescription.java
    │   ├── repository/
    │   │   ├── PatientRepository.java
    │   │   ├── DoctorRepository.java
    │   │   ├── AppointmentRepository.java
    │   │   └── PrescriptionRepository.java
    │   ├── service/
    │   │   ├── PatientService.java
    │   │   ├── DoctorService.java
    │   │   ├── AppointmentService.java
    │   │   ├── PrescriptionService.java
    │   │   └── impl/
    │   │       ├── PatientServiceImpl.java
    │   │       ├── DoctorServiceImpl.java
    │   │       ├── AppointmentServiceImpl.java
    │   │       └── PrescriptionServiceImpl.java
    │   ├── controller/
    │   │   ├── PatientController.java
    │   │   ├── DoctorController.java
    │   │   ├── AppointmentController.java
    │   │   └── PrescriptionController.java
    │   ├── dto/
    │   │   ├── PatientDTO.java
    │   │   ├── DoctorDTO.java
    │   │   ├── AppointmentDTO.java
    │   │   └── PrescriptionDTO.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java
    │   ├── DataInitializer.java
    │   └── DemoApplication.java
    └── resources/
        ├── application.properties
        └── OPTIMIZATION_ASSIGNMENT.md
```

---

## Next Steps

1. ✅ Run the application
2. ✅ Test basic endpoints
3. ✅ Compare `/naive-all` vs `/optimized-all`
4. ✅ Run performance test
5. ✅ Monitor SQL logs
6. ✅ Analyze query execution plans
7. ✅ Scale up data and retest

---

## Contact & Support

For questions or issues:
1. Check console logs for error messages
2. Verify all dependencies in `pom.xml`
3. Ensure Java 17+ is installed
4. Review the `OPTIMIZATION_ASSIGNMENT.md` for detailed concepts

Happy Testing! 🚀
