package net.sehales.ts3sm.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.StringTokenizer;

import javafx.beans.property.ListProperty;

public class ConnectionStorage {
    private ListProperty<ConnectionInfo> connections;
    private Path                         storagePath;

    public ConnectionStorage(ListProperty<ConnectionInfo> connections) {
        this.connections = connections;
        storagePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"), "connections.data");
    }

    public void load() throws IOException {
        if (!Files.exists(storagePath)) {
            return;
        }
        InputStream in = Base64.getDecoder().wrap(Files.newInputStream(storagePath));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        reader.lines().forEach(value -> {
            StringTokenizer tokens = new StringTokenizer(value, ";;");
            connections.add(new ConnectionInfo(tokens.nextToken(), tokens.nextToken(), tokens.nextToken(), tokens.nextToken(), tokens.hasMoreTokens() ? tokens.nextToken() : ""));
        });
        reader.close();
        in.close();
    }

    public void save() throws IOException {
        OutputStream out = Base64.getEncoder().wrap(Files.newOutputStream(storagePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        for (ConnectionInfo info : connections) {
            writer.write(info.getName() + ";;" + info.getAddress() + ";;" + info.getPort() + ";;" + info.getUsername() + ";;" + info.getPassword());
            writer.newLine();
        }
        writer.close();
        out.close();
    }
}
