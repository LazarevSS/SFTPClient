package ru.slazarev.sftpclient.utils;

import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.connection.channel.direct.SessionFactory;
import net.schmizz.sshj.sftp.FileAttributes;
import net.schmizz.sshj.sftp.PacketType;
import net.schmizz.sshj.sftp.Request;
import net.schmizz.sshj.sftp.Response;
import net.schmizz.sshj.sftp.SFTPEngine;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SFTPEngineTransitive extends SFTPEngine {

    public SFTPEngineTransitive(SessionFactory ssh) throws SSHException {
        super(ssh);
    }

    @Override
    public void setAttributes(String path, FileAttributes attrs) throws IOException {
        this.doRequest(this.newRequest(PacketType.SETSTAT)
            .putString(path, this.sub.getRemoteCharset())
            .putFileAttributes(attrs))
            .ensurePacketTypeIs(PacketType.STATUS)
            .ensureStatusIs(Response.StatusCode.NO_SUCH_FILE);
    }

    private Response doRequest(Request req) throws IOException {
        return this.request(req).retrieve((long)this.getTimeoutMs(), TimeUnit.MILLISECONDS);
    }


}
