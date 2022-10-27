import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientThread extends Thread {

    private List<ClientThread> clientThreadList; // 클라이언트 소켓 정보 보관
    private List<UserData> userDataList; // 상용자 정보 보관
    private List<ChatRoomData> chatRoomList;

    private Socket socket;

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientThread(Socket socket, List<ClientThread> clientThreadList, List<UserData> userDataList) throws IOException {
        this.socket = socket;
        this.clientThreadList = clientThreadList;
        this.userDataList = userDataList;

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
//        ChatMember chatMember = null;
        super.run();
        try {
            // 클라이언트 → 서버 : JSON형식의 String 값 받아서, 객체로 변환한다 (Gson 라이브러리 사용)

            // 클라이언트로부터 첫번째 연결 후 메세지를 받으면, 해당 스레드가 시작된다.
            // 이 부분을 채팅방 접속시로 바꿔야 겠다.
            // 즉, 클라이언트에서 소켓 연결되는 시점에 바로 메세지를 보내지 말고, 채팅방에 들어가면 메세지를 보내라.


            String msgFromClient = bufferedReader.readLine();
            System.out.println("(ClientThread) 1. (최초 로그인 시)클라이언트에게 받은 메세지 : " + msgFromClient);
            Gson gson = new Gson();
            ChatData chatData = gson.fromJson(msgFromClient, ChatData.class);
            String userName = chatData.getUserName();
            String joinedMoimSeqList = chatData.getJoinedMoimSeqList();
            String userSeq = chatData.getUserSeq();

            System.out.println("(ClientThread) 3. new UserData(userName, joindMoimSeqList, userSeq, socket, bufferedReader, printWriter) 객체 생성");
            System.out.println("(ClientThread) 3. 사용한 매개변수, userName : " + userName);
            System.out.println("(ClientThread) 3. 사용한 매개변수, joinedMoimSeqList : " + joinedMoimSeqList);
            System.out.println("(ClientThread) 3. 사용한 매개변수, userSeq : " + userSeq);
            System.out.println("(ClientThread) 3. 사용한 매개변수, socket : " + socket);
            System.out.println("(ClientThread) 3. 사용한 매개변수, bufferedReader : " + bufferedReader);
            System.out.println("(ClientThread) 3. 사용한 매개변수, printWriter : " + printWriter);

            UserData userData = new UserData();

            for(int i = 0; i < userDataList.size(); i++){
                UserData previouseUserData = new UserData();
                previouseUserData = userDataList.get(i);
                if(previouseUserData.getUserSeq().equals(userSeq)){

                    userData = new UserData(previouseUserData.getUserName(), previouseUserData.getJoinedMoimSeqList(), previouseUserData.getUserSeq(), "0" , socket, bufferedReader, printWriter);
                    userDataList.remove(previouseUserData); // 이전거는 없앤다.
                }
            }

            userData = new UserData(userName, joinedMoimSeqList, userSeq, "0", socket, bufferedReader, printWriter);
            System.out.println("(ClientThread) 4. userData를 userDataList에 추가");

            userDataList.add(userData);
            System.out.println("(ClientThread) 5. UserDatalist 개수 : " + userDataList.size());
//            Notify_JOIN(chatMember); // 해당 채팅방에 접속해서 메세지를 읽었음을 전송함.
            System.out.println("(ClientThread) 6. userData 스레드 동작 요청");
            userData.start();

//            // 요청서를 보내고 notification 띄우기 위한 Thread
//            UserClass2 user2 = new UserClass2(nickName, id_userSeq, socket, bufferedReader, printWriter);
//            user_list2.add(user2);
//            user2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 사용자 정보를 관리하는 클래스
    public class UserData extends Thread {
        String userName;
        String joinedMoimSeqList;
        String userSeq;

        String chatRoomSee;
        Socket socket;

        BufferedReader bufferedReader;
        PrintWriter printWriter;

        public UserData(String userName, String joinedMoimSeqList, String userSeq, String chatRoomSee,
                        Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
            try {
                this.userName = userName;
                this.joinedMoimSeqList = joinedMoimSeqList;
                this.userSeq = userSeq;
                this.chatRoomSee = chatRoomSee;
                this.socket = socket;
                this.bufferedReader = bufferedReader;
                this.printWriter = printWriter;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public UserData(String userName, String userSeq){

        }

        public UserData(){

        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getJoinedMoimSeqList() {
            return joinedMoimSeqList;
        }

        public String getUserSeq() {
            return userSeq;
        }

        public void setUserSeq(String userSeq) {
            this.userSeq = userSeq;
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        // 사용자로부터 메세지를 수신받는 스레드
        public void run() {
            try {
                while (true) {
                    System.out.println("(UserData) 0. (로그인 이후) 클라이언트에게 메세지 수신 대기");
                    String msgFromClient = bufferedReader.readLine();
                    System.out.println("(UserData) 1. (로그인 이후) 클라이언트에게 받은 메세지 : " + msgFromClient);

                    // [채팅방에서 메세지 보내기를 통해 받아온 값이라면]
                    Gson gson = new Gson();
                    ChatData chatData = gson.fromJson(msgFromClient, ChatData.class);

                    String command = chatData.getCommand();
                    String userSeq = chatData.getUserSeq();
                    String userProfileImage = chatData.getUserProfileImage();
                    String userName = chatData.getUserName();
                    String time = chatData.getTime();
                    String msg = chatData.getMsg();
                    String moimSeq = chatData.getMoimSeq();
                    String chatRoomSeq = chatData.getChatRoomSeq();

                    joinedMoimSeqList = chatData.getJoinedMoimSeqList();
                    chatRoomSee = chatData.getChatRoomSee(); // 채팅방을 보고 있지 않다.

                    System.out.println("(UserData) 2. command 값에 따라 if문 동작");
                    System.out.println("(UserData) 2. command : " + command);
                    System.out.println("(UserData) 2. 모임방 번호, moimSeq : " + moimSeq);
                    System.out.println("(UserData) 2. 발송인 번호, userSeq : " + userSeq);

                    // case 2. 채팅방에 참여하거나, 채팅방을 나가는 경우
                    if (command.equals("JOIN") || command.equals("moimLeft")) {
                        // 접속한 채팅방의 정보를 가져온다. 어떻게 가져오지? -> 반복문을 사용해서
                        System.out.println("(UserData/JOIN or moimLeft: msg) 모임가입 또는 모임 탈퇴 시");
                        int viewType = 4; // 일반 텍스트 전송 시! (아무 조건 없음)
                        // JDBC 연결 부분
                        try {
                            Connection connection = DriverManager.getConnection("jdbc:mysql://3.39.194.226:3306/somoim", "peter", "peter225!@");
                            // SQL 1. 채팅 메세지를 저장하는 쿼리
                            String sql = "INSERT INTO chatMsg (moimSeq, userSeq, msg, userName, userProfileImage, viewType) VALUES ( "
                                    +
                                    moimSeq
                                    + ", " +
                                    userSeq
                                    + ", '" +
                                    msg
                                    + "', '" +
                                    userName
                                    + "', '" +
                                    userProfileImage
                                    + "', " +
                                    viewType
                                    + ")";
                            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                            pstmt.setString(1, "room");
//                            pstmt.setString(2,"jdbc test");

                            int resultCnt = pstmt.executeUpdate(); //영향을 받은 row의 개수를 반환한다
                            int msgSeq = 0; // 방금 생성한 채팅의 고유값
                            //삽입된 id를 확인한다
                            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int inserted_id = generatedKeys.getInt(1);
                                    msgSeq = inserted_id;
                                    System.out.println("(UserData/JOIN or moimLeft: msg) 2. chatMsg 테이블에 행 추가 완료! chatSeq : "+ inserted_id);
                                }
                                else {
                                    throw new SQLException("(UserData/command: msg) 2. Error: No id returned");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        chatData.setViewType(4);
                        oneChat(chatData);
                    }
                    // case 3. 클라이언트의 메세지를 받는 일반적인 상황
                    else if (command.equals("msg")) {
                        int viewType = 1; // 일반 텍스트 전송 시! (아무 조건 없음)

                        System.out.println("(UserData/command: msg) 1. JDBC로 DB에 메세지 저장");
                        System.out.println("(UserData/command: msg) 1.  moimSeq : " + moimSeq);
                        System.out.println("(UserData/command: msg) 1.  userSeq : " + userSeq);
                        System.out.println("(UserData/command: msg) 1.  userName : " + userName);
                        System.out.println("(UserData/command: msg) 1.  userProfileImage : " + userProfileImage);
                        System.out.println("(UserData/command: msg) 1.  viewType : " + viewType);

                        // JDBC 연결 부분
                        try {
                            Connection connection = DriverManager.getConnection("jdbc:mysql://3.39.194.226:3306/somoim", "peter", "peter225!@");

                            // SQL 1. 채팅 메세지를 저장하는 쿼리
                            String sql5 = "INSERT INTO chatMsg (moimSeq, userSeq, msg, userName, userProfileImage, viewType) VALUES ( "
                                    + moimSeq + ", " + userSeq + ", '" + msg + "', '" + userName + "', '"
                                    + userProfileImage + "', " + viewType + ")";
                            PreparedStatement pstmt = connection.prepareStatement(sql5, Statement.RETURN_GENERATED_KEYS);
//                            pstmt.setString(1, "room");
//                            pstmt.setString(2,"jdbc test");

                            int resultCnt = pstmt.executeUpdate(); //영향을 받은 row의 개수를 반환한다
                            int msgSeq = 0; // 방금 생성한 채팅의 고유값
                            //삽입된 id를 확인한다
                            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int inserted_id = generatedKeys.getInt(1);
                                    msgSeq = inserted_id;
                                    System.out.println("(UserData/command: msg) 2. chatMsg 테이블에 행 추가 완료! chatSeq : "+ inserted_id);
                                }
                                else {
                                    throw new SQLException("(UserData/command: msg) 2. Error: No id returned");
                                }
                            }

                            // 메세지를 보낸다
                            oneChat(chatData);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    // case 4. 채팅방 화면을 보기 시작할 때
                    else if (command.equals("chatRoomSee")) {

                        System.out.println("(UserData - (4) command 값이 chatRoomSee 경우) 채팅방 화면을 보기 시작, chatRoomSee : " + chatRoomSee + " (0 → 1로 변경되어야 함)");

//                        oneChat(chatData);
                        // 클라이언트 측에 새로고침을 하라는 명령을 주면 어떨까?

//                        clientThreadList.remove(ClientThread.this);
//                        userDataList.remove(this);
//                            socket.close(); // 확인필요
//                        System.out.println("user_list.size() = " + userDataList.size());
//                            break;
                    }

                    // case 5. 채팅방 화면을 보지않기 시작할 때
                    else if (command.equals("chatRoomNoSee")) {

                        System.out.println("(UserData - (5) command 값이 chatNoRoomSee 경우) 채팅방 화면을 보지않기 시작, chatRoomSee : " + chatRoomSee + " (1 → 0으로 변경되어야 함)");

//                        oneChat(chatData);
                        // 클라이언트 측에 새로고침을 하라는 명령을 주면 어떨까?

//                        clientThreadList.remove(ClientThread.this);
//                        userDataList.remove(this);
//                            socket.close(); // 확인필요
//                        System.out.println("user_list.size() = " + userDataList.size());
//                            break;
                    }



                    // case 4. 클라이언트가 채팅방에서 나갔을때
                    else if (command.equals("EXIT")) {
                        System.out.println("(UserData - (4) command 값이 EXIT 경우)");

                        int number_of_guests_prev = userDataList.size();
                        String guest_name = this.userName;

                        userDataList.remove(this);
                        System.out.println(guest_name+" is removed from guestList. Number of Guests : "+number_of_guests_prev+" -> "+ userDataList.size());

////                        oneChat(chatData);
//                        clientThreadList.remove(ClientThread.this);
//                        .remove(this);
//                            socket.close(); // 확인필요
//                        System.out.println("user_list.size() = " + userDataList.size());
//                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("readLine() error:");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String ex = sw.toString();

                System.out.println(ex);


                try {
                    //소켓이 끊기면, readline 시 null이 뜬다 -> NullPointerException 발생

                    //이 손님을 손님 목록에서 삭제한다

                    int number_of_guests_prev = userDataList.size();
                    String guest_name = this.userName;

                    userDataList.remove(this);
                    System.out.println(guest_name+" is removed from guestList. Number of Guests : "+number_of_guests_prev+" -> "+ userDataList.size());

//                this.interrupt(); 이거 해야되나??


                    //-> 해당 사용자를 inactive 상태로 간주한다
//                server.broadcastToRoomExceptMe(roomId, "inactive/"+username+" is not reading messages.", id);

//                server.removeGuestFromRoom(roomId, this);
//                server.removeGuestFromLobby(this);
                } catch (Exception e1) {
                    StringWriter sww = new StringWriter();
                    e.printStackTrace(new PrintWriter(sww));
                    String exx = sww.toString();

                    System.out.println(exx);
                }
            }


        }


        public void setJoinedMoimSeqList(String moimSeq) {
            this.joinedMoimSeqList = moimSeq;
        }


//        // 사용자 정보를 관리하는 클래스
//        public class ChatRoomData extends Thread {
//            String moimSeq;
//            List<ObjectThread.UserData> joinedUserList;
//
//
//            public ChatRoomData(String moimSeq, List<ObjectThread.UserData> joinedUserList) {
//                try {
//                    this.moimSeq = moimSeq;
//                    this.joinedUserList = joinedUserList;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public ChatRoomData() {
//
//            }
//
//            // 사용자로부터 메세지를 수신받는 스레드
//            public void run() {
//
//            }
//
//            public String getMoimSeq() {
//                return moimSeq;
//            }
//
//            public void setMoimSeq(String moimSeq) {
//                this.moimSeq = moimSeq;
//            }
//
//            public List<UserData> getJoinedUserList() {
//                return joinedUserList;
//            }
//
//            public void setJoinedUserList(List<UserData> joinedUserList) {
//                this.joinedUserList = joinedUserList;
//            }
//        }
    }

    // 채팅 보내는 메서드
    public void oneChat(ChatData chatData) throws IOException {
        System.out.println("(oneChat()) 1. 소켓연결된 클라이언트 수 : " + userDataList.size());
        int i = 1;
        for(UserData user : userDataList){
            try{
                System.out.println("(oneChat()) 2. " + i++ + "번째");
                System.out.println("(oneChat()) 3. (발신자) userSeq : " +chatData.getUserSeq() + " / 발송한 모임방 : " + chatData.getMoimSeq());
                System.out.println("(oneChat()) 4. (수신자) userSeq : " + user.userSeq + " / 참여중인 모임방 : " + user.getJoinedMoimSeqList());
                if(!user.userSeq.equals(chatData.getUserSeq())){
                    String socketUserJoinedMoimSeqList = user.joinedMoimSeqList;
                    String sendingUserMoimSeq = chatData.getMoimSeq();
                    if(socketUserJoinedMoimSeqList.contains(sendingUserMoimSeq)){
                        Gson gson = new Gson();
                        String userJson = gson.toJson(chatData);
                        user.printWriter.println(userJson);
                        System.out.println("(oneChat()) 5. 클라이언트로 전송한 값, chatData : " + userJson);
                    } else {
                        System.out.println("(oneChat()) 5. 수신자가 모임방에 참여하지 않은 경우, 전송하지 않음");
                    }
                } else {
                    System.out.println("(oneChat()) 5. 발신자가 본인인 경우, 전송하지 않음");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void MsgMoimLeft(ChatData chatData) throws IOException {
        System.out.println("(MsgMoimLeft(ChatData chatData)) 1. userDataList.size = " + userDataList.size());
        System.out.println("(MsgMoimLeft(ChatData chatData)) 2. 아래 조건에 해당되는 클라이언트에게만 보낸다");
        System.out.println("(MsgMoimLeft(ChatData chatData)) -- 조건(1) 본인에게 보내지 않는다");
        System.out.println("(MsgMoimLeft(ChatData chatData)) -- 조건(2) user.joinedMoimSeqList에 moimSeq가 없으면 보내지 않는다");
        for(UserData user : userDataList){
            try{
                System.out.println("user.joinedMoimSeqList(소켓통신중인 joinedMoimSeqList) : " + user.joinedMoimSeqList + " / chatData.getMoimSeq(msg를 보낸 모임번호) : " + chatData.getMoimSeq());
                if(user.joinedMoimSeqList.contains(chatData.getMoimSeq())){
                    System.out.println(user.userName + "에게는 메세지를 보냈다");
                    System.out.println(user.userName + "는 모임에 포함되어있다");
                    String socketUserJoinedMoimSeqList = user.joinedMoimSeqList;
                    String sendingUserJoinedMoimSeqList = chatData.getMoimSeq();
                    System.out.println("socketUserJoinedMoimSeqList : " + socketUserJoinedMoimSeqList + " / sendingUserJoinedMoimSeqList : " + sendingUserJoinedMoimSeqList);
                    System.out.println("socketUserJoinedMoimSeqList.contains(sendingUserJoinedMoimSeqList) : " + socketUserJoinedMoimSeqList.contains(sendingUserJoinedMoimSeqList + ";"));

                } else {
                    System.out.println(user.userName + "에게는 메세지를 보내지 않았다");
                    System.out.println(user.userName + "는 모임에 포함되지 않았다");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void Notify_JOIN(ChatData chatData) throws IOException {
        System.out.println("Notify_JOIN : user_list.size() = " + userDataList.size());
        for(UserData user : userDataList){
            try{
                if(user.joinedMoimSeqList != null){
                    if(!user.userSeq.equals(chatData.getSenderSeq())){ //소켓 정보에 담긴 계정 고유값과 메세지를 보낸 사람의 값이  다른 경우에만 동작한다
                        Gson gson = new Gson();
                        String userJson = gson.toJson(chatData);
                        user.printWriter.println(userJson);
                        System.out.println("Notify_JOIN : JSON 형태로 보내는 값 = " + userJson);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void sendNotificationInfo(ChatData chatData) throws IOException {
        for(UserData user : userDataList){
            try{
                if(!user.userSeq.equals(chatData.getUserSeq())){ // 메세지를 받는 Thread의 userSeq와 메세지를 서버로 보낸 userSeq가 다르다면, 메세지를 보내라.
                    Gson gson = new Gson();
                    String userJson = gson.toJson(chatData);
                    user.printWriter.println(userJson);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
