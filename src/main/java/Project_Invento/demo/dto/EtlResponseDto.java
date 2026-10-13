package Project_Invento.demo.dto;

import lombok.Builder;

@Builder
public record EtlResponseDto(String fileName, String contentType, String status) {

}
