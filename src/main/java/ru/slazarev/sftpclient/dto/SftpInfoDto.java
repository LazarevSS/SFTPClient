package ru.slazarev.sftpclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SftpInfoDto {

    @ApiModelProperty(value = "Адрес sftp-сервера(DNS-name or ip-address)", example = "192.168.3.69")
    @JsonProperty(value = "host", required = true)
    private String host;

    @ApiModelProperty(value = "Порт sftp-сервера", example = "22")
    @JsonProperty(value = "port", required = true)
    private Integer port;

    @ApiModelProperty(value = "Имя пользователя для авторизации sftp-сервера", example = "tester")
    @JsonProperty(value = "username", required = true)
    private String username;

    @ApiModelProperty(value = "Пароль для авторизации sftp-сервера", example = "password123")
    @JsonProperty(value = "password", required = true)
    private String password;

    @ApiModelProperty(value = "Директория sftp-сервера для передачи данных", example = "/home/ipsenpharatestssh/inbox/SOM_SALES_SHIPMENT")
    @JsonProperty(value = "workingDirection", required = true)
    private String workingDirection;
}

