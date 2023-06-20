package io.project.todoapp.config;

import io.project.todoapp.controller.TaskController;
import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.model.Task;
import io.project.todoapp.repository.SemesterRepository;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.security.user.UserRepository;
import io.project.todoapp.service.SemesterService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static java.lang.Math.round;

@Component
@AllArgsConstructor
public class DataInitializerV2 implements CommandLineRunner
{
    private final Logger logger = LoggerFactory.getLogger(DataInitializerV2.class);
    private final SemesterRepository semesterRepository;
    private final TaskController taskController;
    private final UserRepository userRepository;

    @Override
    public void run(String... args)
    {
        try
        {
            logger.info("EXECUTING : DataInitializerV2");

            FileInputStream fis = new FileInputStream("Backend\\todoapp\\init_data.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            ArrayList<Task> tasks = new ArrayList<>(); // done
            ArrayList<Long> tasksSubjectsIds = new ArrayList<>();
            ArrayList<Long> tasksSemestersIds = new ArrayList<>();
            ArrayList<Subject> subjects = new ArrayList<>(); // done ?
            ArrayList<Long> subjectsIds = new ArrayList<>();
            ArrayList<Semester> semesters = new ArrayList<>(); // done
            ArrayList<Long> semestersIds = new ArrayList<>();
            ArrayList<User> users = new ArrayList<>();

            {
                // tasks
                XSSFSheet sheet = workbook.getSheetAt(0);
                long id = 0L;
                long semesterId = 0L;
                long subjectId = 0L;
                String name = "";
                String description = "";
                boolean done = Boolean.FALSE;

                int rowN = 0;
                for (Row row : sheet)
                {
                    if (rowN == 0)
                    {
                        rowN++;
                        continue;
                    }

                    int cellN = 0;
                    for (Cell cell : row)
                    {
                        switch (cellN)
                        {
                            case 0 -> id = round(cell.getNumericCellValue());
                            case 1 -> semesterId = round(cell.getNumericCellValue());
                            case 2 -> subjectId = round(cell.getNumericCellValue());
                            case 3 -> name = cell.getStringCellValue();
                            case 4 -> description = cell.getStringCellValue();
                            case 5 -> done = cell.getBooleanCellValue();
                        }
                        cellN++;
                    }
                    tasks.add(new Task(id, 0L, 0L, name, description, done));
                    tasksSubjectsIds.add(subjectId);
                    tasksSemestersIds.add(semesterId);
                    logger.info("ID: " + id + ", Semester ID: " + semesterId + ", Subject ID: " + subjectId + ", Name: " + name + ", Description: " + description + ", Done: " + done);
                }
            }

            {
                // subjects
                XSSFSheet sheet = workbook.getSheetAt(1);
                long id = 0L;
                String name = "";
                int ectsPoints = 0;
                ArrayList<Long> taskIds = new ArrayList<Long>();

                int rowN = 0;
                for (Row row : sheet)
                {
                    if (rowN == 0)
                    {
                        rowN++;
                        continue;
                    }

                    int cellN = 0;
                    for (Cell cell : row)
                    {
                        switch (cellN)
                        {
                            case 0 -> id = round(cell.getNumericCellValue());
                            case 1 -> name = cell.getStringCellValue();
                            case 2 -> ectsPoints = (int) round(cell.getNumericCellValue());
                            default -> taskIds.add(round(cell.getNumericCellValue()));
                        }
                        cellN++;
                    }

                    subjects.add(new Subject(id, name, ectsPoints, new ArrayList<>(), true));
                    subjectsIds.add(id);
                    logger.info(id + " " + name + " " + ectsPoints + " " + taskIds);

                    taskIds.clear();
                }
            }

            {
                // semesters
                XSSFSheet sheet = workbook.getSheetAt(2);
                long id = 0L;
                int number = 0;
                int year = 0;
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                ArrayList<Long> subjectIds = new ArrayList<Long>();

                int rowN = 0;
                for (Row row : sheet)
                {
                    if (rowN == 0)
                    {
                        rowN++;
                        continue;
                    }

                    int cellN = 0;
                    for (Cell cell : row)
                    {
                        switch (cellN)
                        {
                            case 0 -> id = round(cell.getNumericCellValue());
                            case 1 -> number = (int) round(cell.getNumericCellValue());
                            case 2 -> year = (int) round(cell.getNumericCellValue());
                            case 3 -> startDate = cell.getDateCellValue().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            case 4 -> endDate = cell.getDateCellValue().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            default -> subjectIds.add(round(cell.getNumericCellValue()));
                        }
                        cellN++;
                    }

                    ArrayList<Subject> subjectsToAdd = new ArrayList<>();
                    for (int i = 0; i < subjectsIds.size(); i++)
                    {
                        if (subjectIds.contains(subjectsIds.get(i)))
                        {
                            subjectsToAdd.add(subjects.get(i));
                        }
                    }

                    semesters.add(new Semester(id, number, year, subjectsToAdd, startDate, endDate));
                    semestersIds.add(id);
                    logger.info(id + " " + number + " " + year + " " + startDate + " " + endDate + " " + subjectIds);

                    subjectIds.clear();
                }
            }

            {
                // users
                XSSFSheet sheet = workbook.getSheetAt(3);
                long id = 0L;
                String firstName = "";
                String lastName = "";
                String email = "";
                String password = "";
                long actualSemesterId = 0L;
                Role role = Role.STUDENT;

                int rowN = 0;
                for (Row row : sheet)
                {
                    if (rowN == 0)
                    {
                        rowN++;
                        continue;
                    }

                    int cellN = 0;
                    for (Cell cell : row)
                    {
                        switch (cellN)
                        {
                            case 0 -> id = round(cell.getNumericCellValue());
                            case 1 -> firstName = cell.getStringCellValue();
                            case 2 -> lastName = cell.getStringCellValue();
                            case 3 -> email = cell.getStringCellValue();
                            case 4 -> password = cell.getStringCellValue();
                            case 5 -> actualSemesterId = round(cell.getNumericCellValue());
                            case 6 -> role = switch (cell.getStringCellValue())
                            {
                                case "cp" -> Role.CLASS_PRESIDENT;
                                case "a" -> Role.ADMIN;
                                default -> Role.STUDENT;
                            };
                        }
                        cellN++;
                    }

                    Semester semesterToAdd = new Semester();
                    for (int i = 0; i < semestersIds.size(); i++)
                    {
                        if (semestersIds.get(i) == actualSemesterId)
                        {
                            semesterToAdd = semesters.get(i);
                        }
                    }

                    users.add(new User(id, firstName, lastName, email, password, semesterToAdd, role));
                    logger.info(id + " " + firstName + " " + lastName + " " + email + " " + password + " " + actualSemesterId + " " + role);
                }
            }

            workbook.close();
            fis.close();

            // DODAWANIE DO BAZY
            for (Semester sem : semesters)
            {
                semesterRepository.save(sem);
            }
            for (User usr : users)
            {
                userRepository.save(usr);
            }
            for (int i = 0; i < tasks.size(); i++)
            {
                taskController.addNewTask(tasks.get(i), tasksSemestersIds.get(i), tasksSubjectsIds.get(i));
            }
        }
        catch (Exception e)
        {
            logger.error("Error occurred: " + e.getMessage());
        }
    }
}
