package Project_Invento.demo.dto;

import lombok.Builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Builder
public record DocumentInput(

        String fileName,
        String contentType,
        byte[] content
) {
    

    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    public long size() {
        return content == null ? 0 : content.length;
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(
                content == null ? new byte[0] : content
        );
    }
}