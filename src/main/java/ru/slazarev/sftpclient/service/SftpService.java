package ru.slazarev.sftpclient.service;

import ru.slazarev.sftpclient.dto.SftpInfoDto;

import java.io.InputStream;

public interface SftpService {
    void sendFile(InputStream inputStream, String fileName, SftpInfoDto sftpInfoDto) throws Exception;

    void sendFileForTransit(InputStream inputStream, String fileName, SftpInfoDto sftpInfoDto) throws Exception;
}
