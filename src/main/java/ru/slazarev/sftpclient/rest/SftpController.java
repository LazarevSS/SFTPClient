package ru.slazarev.sftpclient.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.slazarev.sftpclient.dto.SftpInfoDto;
import ru.slazarev.sftpclient.service.SftpService;

@Api(description = "Контроллер для работы с sftp-протоколом")
@RestController
@RequiredArgsConstructor
public class SftpController {

    private final SftpService sftpService;

    @ApiOperation(
        value = "Отправить файл sftp-серверу"
    )
    @PutMapping("/sftp/sendFile/")
    public ResponseEntity<String> send(@ApiParam(value = "Файл для передачи", required = true)
                           @RequestParam("file") MultipartFile file,
                                       @ApiParam(value = "Адрес sftp-сервера(DNS-name or ip-address)", required = true)
                           @RequestParam("host") String host,
                                       @ApiParam(value = "Порт sftp-сервера", required = true)
                           @RequestParam("port") Integer port,
                                       @ApiParam(value = "Имя пользователя для авторизации sftp-сервера", required = true)
                           @RequestParam("user") String user,
                                       @ApiParam(value = "Пароль для авторизации sftp-сервера", required = true)
                           @RequestParam("password") String password,
                                       @ApiParam(value = "Директория sftp-сервера для передачи данных", required = true)
                           @RequestParam("workingDirection") String workingDirection
                       ) throws Exception {
        SftpInfoDto sftpInfoDto = new SftpInfoDto(host, port, user, password, workingDirection);
        sftpService.sendFile(file.getInputStream(), file.getOriginalFilename(), sftpInfoDto);
        return new ResponseEntity<>(
            "File successfully sent to server: " + host,
            HttpStatus.OK);
    }

    @ApiOperation(
        value = "Отправить файл транзитному sftp-серверу(удаляет файл сразу после получения)"
    )
    @PutMapping("/sftp/sendFileForTransit/")
    public ResponseEntity<String> sendForTransit(@ApiParam(value = "Файл для передачи", required = true)
                                       @RequestParam("file") MultipartFile file,
                                                 @ApiParam(value = "Адрес sftp-сервера(DNS-name or ip-address)", required = true)
                                       @RequestParam("host") String host,
                                                 @ApiParam(value = "Порт sftp-сервера", required = true)
                                       @RequestParam("port") Integer port,
                                                 @ApiParam(value = "Имя пользователя для авторизации sftp-сервера", required = true)
                                       @RequestParam("user") String user,
                                                 @ApiParam(value = "Пароль для авторизации sftp-сервера", required = true)
                                       @RequestParam("password") String password,
                                                 @ApiParam(value = "Директория sftp-сервера для передачи данных", required = true)
                                       @RequestParam("workingDirection") String workingDirection
    ) throws Exception {
        SftpInfoDto sftpInfoDto = new SftpInfoDto(host, port, user, password, workingDirection);
        sftpService.sendFileForTransit(file.getInputStream(), file.getOriginalFilename(), sftpInfoDto);
        return new ResponseEntity<>(
            "File successfully sent to server: " + host,
            HttpStatus.OK);
    }
}
