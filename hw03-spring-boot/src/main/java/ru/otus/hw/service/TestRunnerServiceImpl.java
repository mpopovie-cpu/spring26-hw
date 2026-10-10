package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;
    private final StudentService studentService;
    private final LocalizedIOService ioService;

    @Override
    public void run() {
        Student student = studentService.createStudent();
        runForStudent(student);
    }

    @Override
    public void runForStudent(Student student) {
        TestResult result = testService.executeTestFor(student);
        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");
        ioService.printFormattedLine("Student: %s %s", student.firstName(), student.lastName());
        ioService.printFormattedLine("Answered questions count: %d", result.getAnsweredQuestions().size());
        ioService.printFormattedLine("Right answers count: %d", result.getRightAnswersCount());
        
        if (result.getRightAnswersCount() >= 3) { // Example pass condition
            ioService.printLineLocalized("ResultService.passed.test");
        } else {
            ioService.printLineLocalized("ResultService.fail.test");
        }
    }
}
