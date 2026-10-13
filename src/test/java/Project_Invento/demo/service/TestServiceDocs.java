package Project_Invento.demo.service;


import Project_Invento.demo.application.service.ExecutionService;
import Project_Invento.demo.domain.model.AutomationExecution;
import Project_Invento.demo.domain.model.ExecutionStatus;
import Project_Invento.demo.dto.DocumentInput;
import Project_Invento.demo.dto.EtlResponseDto;
import Project_Invento.demo.dto.ExecutionResponseDto;
import Project_Invento.demo.infrastructore.exception.exceptions.InvalidFileException;
import Project_Invento.demo.ports.out.AutomationExecutionRepositoryPort;
import Project_Invento.demo.ports.out.etl.EtlProcessorPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceDocs {


    @Mock
    EtlProcessorPort processorPort;

    @InjectMocks
    ExecutionService executionService;

    @Mock
    AutomationExecutionRepositoryPort repositoryPort;


    @Test
    @DisplayName(" verifica se o service lanca a expetion que nao deixa arquivos vazios passarem ")
    void testExceptionWhenisNull() {


        DocumentInput doc = DocumentInput.builder()
                .fileName(null)
                .contentType(null)
                .content(null)
                .build();


        assertThrows(InvalidFileException.class,
                () -> executionService.validateFile(doc));

    }

    @Test
    @DisplayName("testa a entrada de arquivos que nao seja .docx")
    void testExeptionWhenFileisNotDocx() {



        DocumentInput doc = DocumentInput.builder()
                .fileName("file.xlsx")
                .contentType(null)
                .content("Conteudo".getBytes())
                .build();

        var exception = assertThrows(InvalidFileException.class,
                () -> executionService.validateFile(doc));

        assertEquals(
                "Only .docx files are allowed",
                exception.getMessage()
        );


    }

    @Test
    void testPersistensinPort() {

        // testar a persistencia pela porta

        DocumentInput doc = DocumentInput.builder()                          // cria o file
                .fileName("file.docx")
                .contentType("sla")
                .content("Conteudo".getBytes())
                .build();

        AutomationExecution automation = new AutomationExecution();

        when(repositoryPort.save(any(AutomationExecution.class))).thenReturn(automation);

        EtlResponseDto dto = EtlResponseDto.builder()
                .fileName("file.docx")
                .contentType("sla")
                .status("recebido")
                        .build();

        when(processorPort.processEtl(doc)).thenReturn(dto);


        ExecutionResponseDto act = executionService.validateFile(doc);

        assertEquals(ExecutionStatus.COMPLETED , act.getStatus());


        verify(repositoryPort, times(2)).save(any(AutomationExecution.class));
        verify(processorPort).processEtl(doc);

    }

}
