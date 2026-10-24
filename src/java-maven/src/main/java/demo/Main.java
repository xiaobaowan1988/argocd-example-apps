package demo;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        // 版本号由构建时写进镜像的 /app/version.txt 提供(流水线注入的 VERSION 构建参数)
        Path vf = Path.of("/app/version.txt");
        String version = Files.exists(vf) ? Files.readString(vf).trim() : "dev";
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", ex -> {
            byte[] b = ("{\"service\":\"java-api\",\"version\":\"" + version + "\",\"java\":\"" + System.getProperty("java.version") + "\"}").getBytes();
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(200, b.length);
            try (var o = ex.getResponseBody()) { o.write(b); }
        });
        server.createContext("/healthz", ex -> {
            byte[] b = "ok".getBytes();
            ex.sendResponseHeaders(200, b.length);
            try (var o = ex.getResponseBody()) { o.write(b); }
        });
        server.start();
    }
}
