import java.util.List;

// 사용자 정보를 관리하는 클래스
public class ChatRoomData extends Thread {
    String moimSeq;
    List<ClientThread.UserData> joinedUserList;


    public ChatRoomData(String moimSeq, List<ClientThread.UserData> joinedUserList) {
        try {
            this.moimSeq = moimSeq;
            this.joinedUserList = joinedUserList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChatRoomData() {

    }

    // 사용자로부터 메세지를 수신받는 스레드
    public void run() {

    }

    public String getMoimSeq() {
        return moimSeq;
    }

    public void setMoimSeq(String moimSeq) {
        this.moimSeq = moimSeq;
    }

    public List<ClientThread.UserData> getJoinedUserList() {
        return joinedUserList;
    }

    public void setJoinedUserList(List<ClientThread.UserData> joinedUserList) {
        this.joinedUserList = joinedUserList;
    }
}
