package Project_Invento.demo.application.service;

import Project_Invento.demo.domain.model.AutomationExecution;
import Project_Invento.demo.domain.model.ExecutionStatus;
import Project_Invento.demo.dto.DocumentInput;
import Project_Invento.demo.dto.EtlResponseDto;
import Project_Invento.demo.dto.ExecutionResponseDto;
import Project_Invento.demo.infrastructore.exception.exceptions.EtlProcessingException;
import Project_Invento.demo.infrastructore.exception.exceptions.InvalidFileException;
import Project_Invento.demo.ports.out.AutomationExecutionRepositoryPort;
import Project_Invento.demo.ports.out.etl.EtlProcessorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExecutionService {

    private final EtlProcessorPort processorPort;
    private final AutomationExecutionRepositoryPort repositoryPort;

        public ExecutionResponseDto validateFile(DocumentInput file) {

            log.info("Starting file validation: {}", file.fileName());

            if (file.isEmpty()) {
                log.warn("Empty file received");
                throw new InvalidFileException("File cannot be empty");
            }

            String fileName = file.fileName();

            if (fileName == null || !fileName.toLowerCase().endsWith(".docx")) {
                log.warn("Invalid file type: {}", fileName);
                throw new InvalidFileException("Only .docx files are allowed");
            }

            log.info("File validated successfully: {}", fileName);

            AutomationExecution model = new AutomationExecution();
            model.setFileName(file.fileName());
            model.setCreatedAt(LocalDateTime.now());
            model.setStatus(ExecutionStatus.RECEIVED);


            AutomationExecution automationExecution = repositoryPort.save(model);


            try {

                processorPort.processEtl(file);

                automationExecution.setStatus(ExecutionStatus.COMPLETED);
                automationExecution.setFinishedAt(LocalDateTime.now());

                automationExecution = repositoryPort.save(automationExecution);

            } catch (Exception e) {

                String errorMessage = e.getMessage();

                automationExecution.setStatus(ExecutionStatus.FAILED);
                automationExecution.setFinishedAt(LocalDateTime.now());
                automationExecution.setErrorMessage(errorMessage);

                repositoryPort.save(automationExecution);

                throw new EtlProcessingException(
                        errorMessage,
                        e
                );
            }

            return ExecutionResponseDto.builder()
                    .id(automationExecution.getId())
                    .fileName(automationExecution.getFileName())
                    .status(automationExecution.getStatus())
                    .createdAt(automationExecution.getCreatedAt())
                    .build();
        }
}