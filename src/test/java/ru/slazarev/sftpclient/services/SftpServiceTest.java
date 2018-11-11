package ru.slazarev.sftpclient.services;

import org.apache.sshd.common.io.mina.MinaServiceFactoryFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.slazarev.sftpclient.dto.SftpInfoDto;
import ru.slazarev.sftpclient.service.SftpService;
import ru.slazarev.sftpclient.service.SftpServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class SftpServiceTest {

    private SshServer server;

    private SftpService sftpService;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String workingDirection;

    private String fileName = "sendSftpTest.xml";

    @Before
    public void setUp() throws Exception {
        host = "localhost";
        port = 2255;
        username = "userNameTest";
        password = "passwordTest";
        workingDirection= "./";
        sftpService = new SftpServiceImpl();
        server = defaultSshServer();
    }

    @Test
    public void sendFile() throws Exception {
        SftpInfoDto sftpInfo = new SftpInfoDto(host, port, username, password, workingDirection);
        assertNotNull(sftpInfo);
        File sendFile = new File(getClass().getResource(fileName).toURI());
        assertTrue(sendFile.isFile());
        InputStream sendFileInputStream = new FileInputStream(sendFile);
        assertDoesNotThrow(() -> sftpService.sendFile(sendFileInputStream, sendFile.getName(), sftpInfo));
        assertTrue(new File(workingDirection + fileName).delete());
        server.stop();
        server.close();
    }

    private SshServer defaultSshServer() throws IOException {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(port);
        sshServer.setPasswordAuthenticator((serverUsername, serverPassword, serverSession) ->
                serverUsername.equals(username) && serverPassword.equals(password));
        SimpleGeneratorHostKeyProvider provider = new SimpleGeneratorHostKeyProvider();
        sshServer.setKeyPairProvider(provider);
        sshServer.setHost(host);
        sshServer.setCommandFactory(new ScpCommandFactory());
        sshServer.setIoServiceFactoryFactory(new MinaServiceFactoryFactory());
        sshServer.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
        sshServer.start();
        return sshServer;
    }
}