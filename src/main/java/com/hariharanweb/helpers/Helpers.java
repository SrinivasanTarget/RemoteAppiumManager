package com.hariharanweb.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Helpers {

    public static boolean isPortAvailable(int port) throws IOException {
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            return false;
        } catch (IOException e) {
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }


    /* * Generates Random ports
    * Used during starting appium server
    */
    public static int getAvailablePort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        socket.setReuseAddress(true);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    public static String excuteProcess(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        // get std output
        BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        String allLine = "";
        int i = 1;
        while ((line = r.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            if (line.contains("Console LogLevel: debug") && line.contains("Complete")) {
                break;
            }
            i++;
        }
        return allLine;
    }

    public static String getHostMachineIpAddress() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        return socket.getLocalAddress().toString()
                .replace("/", "");
    }

    public int getPid(Process process) {

        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }
}
