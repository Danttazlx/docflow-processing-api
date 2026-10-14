package Project_Invento.demo.adapters.inbouds.contoller;

import Project_Invento.demo.application.service.ExecutionService;
import Project_Invento.demo.dto.DocumentInput;
import Project_Invento.demo.dto.ExecutionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/executions")
@RequiredArgsConstructor
public class ProcessingController {

    private final ExecutionService serviceAutomation;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ExecutionResponseDto createExecution (
            @RequestParam("file") MultipartFile file) throws IOException {

        DocumentInput documentInput = new DocumentInput(

                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );

        return serviceAutomation.validateFile(documentInput);
    }


}
