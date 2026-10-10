package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@ShellComponent
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;
    private final StudentService studentService;

    @ShellMethod(key = "test", value = "Run student testing")
    public void runTest(@ShellOption(defaultValue = "John Doe") String studentName) {
        // This is a simple implementation for the shell command
        // In a real scenario, you'd parse the name into first and last
        String[] parts = studentName.split(" ", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";
        
        Student student = new Student(firstName, lastName);
        testRunnerService.runForStudent(student);
    }
}
