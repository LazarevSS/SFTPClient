package ru.slazarev.sftpclient.service;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.sftp.SFTPEngine;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.springframework.stereotype.Service;
import ru.slazarev.sftpclient.dto.SftpInfoDto;
import org.apache.commons.io.FileUtils;
import ru.slazarev.sftpclient.utils.SFTPEngineTransitive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class SftpServiceImpl implements SftpService {

    @Override
    public void sendFile(InputStream inputStream, String fileName, SftpInfoDto sftpInfo) throws Exception {
        try (SSHClient ssh = new SSHClient()) {
            connectAndAuthPassword(ssh, sftpInfo);
            try (SFTPClient sftp = createSFTPClient((new SFTPEngine(ssh)).init())) {
                File file = createFileFromInputStream(fileName, inputStream);
                sftp.put(new FileSystemFile(file), sftpInfo.getWorkingDirection());
            }
        }
    }

    @Override
    public void sendFileForTransit(InputStream inputStream, String fileName, SftpInfoDto sftpInfo) throws Exception {
        try (SSHClient ssh = new SSHClient()) {
            connectAndAuthPassword(ssh, sftpInfo);
            try (SFTPClient sftp = createSFTPClient((new SFTPEngineTransitive(ssh).init()))) {
                File file = createFileFromInputStream(fileName, inputStream);
                sftp.put(new FileSystemFile(file), sftpInfo.getWorkingDirection());
            }
        }
    }

    private SFTPClient createSFTPClient(SFTPEngine engine) {
        return new SFTPClient(engine);
    }

    private void connectAndAuthPassword(SSHClient ssh, SftpInfoDto sftpInfo) throws IOException {
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(sftpInfo.getHost(), sftpInfo.getPort());
        ssh.authPassword(sftpInfo.getUsername(), sftpInfo.getPassword());
    }

    private File createFileFromInputStream(String fileName, InputStream inputStream) throws IOException {
        File file = new File(fileName);
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }
}
