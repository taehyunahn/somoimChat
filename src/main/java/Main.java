import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new MainServer();
    }

    static class MainServer
    {
        private ServerSocket serverSocket; // 서버소켓
        private List<ClientThread> clientThreadList;
        public static List<ClientThread.UserData> userDatalist;
        public static List<ChatRoomData> chatRoomList;

        public MainServer(){
            try{
                System.out.println("(MainServer) 1. 서버소켓 객체 생성");
                serverSocket= new ServerSocket (5003);
                System.out.println("(MainServer) 2. clientThreadList 생성");
                clientThreadList = new ArrayList<ClientThread>();
                System.out.println("(MainServer) 3. userDatalist 생성");


                userDatalist = new ArrayList<ClientThread.UserData>();
                System.out.println("(MainServer) 4. chatRoomList 생성");


                chatRoomList = new ArrayList<ChatRoomData>();

                while(true){
                    System.out.println("(MainServer) 5. while문 안에서 클라이언트 소켓 연결 요청 대기");
                    Socket socket = serverSocket.accept(); // 다른 소켓 연결을 위해 대기하는 부분

                    // 연결된 시점에 hashmap으로 저장
                    // 이름을 key로 하고, value로...

                    System.out.println("(MainServer) 6. 클라이언트 소켓 연결됨 → new ClientThread(socket, clientThreadList, userDatalist) 객체 생성");

                    ClientThread clientThread = new ClientThread(socket, clientThreadList, userDatalist);  //스레드를 생성한 것이랑 동일함!
//                    ClientThread clientThread = new ClientThread(socket, clientThreadList, userDatalist);  //스레드를 생성한 것이랑 동일함!


                    System.out.println("(MainServer) 7. clientThread 동작 요청");
                    clientThread.start();  //스레드 시작- 스레드 실행
                    System.out.println("(MainServer) 8. 동작된 clientThread를 clientThreadList에 추가");
                    clientThreadList.add(clientThread);  //핸들러를 담음( 이 리스트의 개수가 클라이언트의 갯수!!)
                    System.out.println("(MainServer) 9. clientThreadList 개수 : " + clientThreadList.size());
                }//while
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
