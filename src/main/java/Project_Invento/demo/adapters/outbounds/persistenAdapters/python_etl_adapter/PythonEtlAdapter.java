package Project_Invento.demo.adapters.outbounds.persistenAdapters.python_etl_adapter;

import Project_Invento.demo.dto.DocumentInput;
import Project_Invento.demo.dto.EtlResponseDto;
import Project_Invento.demo.infrastructore.config.WebClientConfig;
import Project_Invento.demo.ports.out.etl.EtlProcessorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;

@Component
@RequiredArgsConstructor
public class PythonEtlAdapter implements EtlProcessorPort {

    private final WebClientConfig clientConfig;

    @Override
    public EtlResponseDto processEtl(DocumentInput file) {

        ByteArrayResource byteResource = new ByteArrayResource(file.content()) {
            @Override
            public String getFilename() {
                return file.fileName();
            }
        };

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder
                .part("file", byteResource)
                .contentType(MediaType.parseMediaType(file.contentType()));


        return clientConfig.pythonWebClient()
                    .post()
                    .uri("/etl/process")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve( )
                    .bodyToMono(EtlResponseDto.class)
                    .block();
        }
}
