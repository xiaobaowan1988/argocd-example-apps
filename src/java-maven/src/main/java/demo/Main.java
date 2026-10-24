package demo;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        // 版本号由构建时写进镜像的 /app/version.txt 提供(流水线注入的 VERSION 构建参数)
        Path vf = Path.of("/app/version.txt");
        String version = Files.exists(vf) ? Files.readString(vf).trim() : "dev";
        // profile / greeting 来自 Maven profile 过滤后的 application.properties(构建脚本里 mvn -P<profile> 决定)
        Properties props = new Properties();
        try (var in = Main.class.getResourceAsStream("/application.properties")) { props.load(in); }
        String profile = props.getProperty("profile"), greeting = props.getProperty("greeting");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", ex -> {
            byte[] b = ("{\"service\":\"java-api\",\"version\":\"" + version + "\",\"profile\":\"" + profile + "\",\"greeting\":\"" + greeting + "\",\"java\":\"" + System.getProperty("java.version") + "\"}").getBytes();
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
