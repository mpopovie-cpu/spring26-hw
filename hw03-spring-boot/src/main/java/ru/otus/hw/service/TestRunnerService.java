package ru.otus.hw.service;

import ru.otus.hw.domain.Student;

public interface TestRunnerService {
    void run();
    void runForStudent(Student student);
}
