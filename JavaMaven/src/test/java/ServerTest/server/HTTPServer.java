package ServerTest.server;
import java.io.*;
import java.net.*;

public class HTTPServer {
    public static void main(String args[]){
        int port;
        ServerSocket serverSocket;
        try {
            port=Integer.parseInt(args[0]);
        } catch (Exception e) {
            port = 8080;
//            e.printStackTrace();
        }

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器监听端口:" + serverSocket.getLocalPort());
            while(true){
                try {
                    final Socket socket = serverSocket.accept();
                    System.out.println("建立一个新的TCP连接"+socket.getInetAddress()+":"+socket.getPort());
                    service(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void service(Socket socket) throws Exception{
        InputStream socketIn = socket.getInputStream();
        Thread.sleep(500);
        int size=socketIn.available();
        byte[] buffer=new byte[size];
        socketIn.read(buffer);
        String request=new String(buffer);
        System.out.println(request);

        String firstLineOfRequest=request.substring(0,request.indexOf("\r\n"));
        String[] parts=firstLineOfRequest.split(" ");
        String uri=parts[1];
        System.out.println("uri:"+uri);
        String contentType;
        if(uri.indexOf("html")!=-1||uri.indexOf("htm")!=-1){
            contentType = "text/html";
        }else if(uri.indexOf("jpg")!=-1||uri.indexOf("jpeg")!=-1) {
            contentType = "image/jpeg";
        }else if(uri.indexOf("gif")!=-1){
            contentType = "image/gif";
        }else{
            contentType="application/octet-stream";
        }

        String responseFirsrLine="HTTP/1.1 200 OK\r\n";
        String responseHeader="Content-Type:"+contentType+"\r\n\r\n";
//        File file=new File("E:\\code\\javaWeb\\JavaMaven\\src\\test\\java\\ServerTest\\server\\"+uri);
//        InputStream in=new FileInputStream(file) ;
        InputStream in = HTTPServer.class.getResourceAsStream(uri);

        OutputStream socketOut=socket.getOutputStream();
        socketOut.write(responseFirsrLine.getBytes());
        socketOut.write(responseHeader.getBytes());
        socketOut.write("想不到吧".getBytes());
        int len=0;
        buffer=new byte[128];
        while((len=in.read(buffer))!=-1)
            socketOut.write(buffer,0,len);
        Thread.sleep(1000);
        socket.close();
    }
}
