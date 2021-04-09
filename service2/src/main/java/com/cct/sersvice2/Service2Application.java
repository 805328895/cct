package com.cct.sersvice2;

import com.cct.rpc.server.CctRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Service2Application {

    public static void main(String[] args) {
        SpringApplication.run(Service2Application.class, args);
        CctRpcServer server = new CctRpcServer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.start(1009, "com.cct.sersvice2");
            }
        }).start();

    }

}
