package com.company.student.app.service.mapper;

import com.company.student.app.dto.submission.SubmissionResponse;
import com.company.student.app.model.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SubmissionMapper {

    @Mapping(target = "assignmentTitle", source = "submission.assignment.title")
    public abstract SubmissionResponse mapToResponse(Submission submission);
}
