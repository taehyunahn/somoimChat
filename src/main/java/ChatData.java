import java.io.Serializable;


public class ChatData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String command;
    private String userSeq;
    private String userProfileImage;
    private String userName;
    private String time;
    private String msg;
    private String orderType;
    private String moimSeq;

    private int viewType;

    private String messageCount;
    private String chatRoomNumber;

    private String senderSeq;
    private String senderName;
    private String senderProfileImage;

    private String chatRoomSeq;
    private String joinedMoimSeqList;

    private String unreadCount;

    private String chatRoomSee;





    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(String userSeq) {
        this.userSeq = userSeq;
    }


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getChatRoomNumber() {
        return chatRoomNumber;
    }

    public void setChatRoomNumber(String chatRoomNumber) {
        this.chatRoomNumber = chatRoomNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMoimSeq() {
        return moimSeq;
    }

    public void setMoimSeq(String moimSeq) {
        this.moimSeq = moimSeq;
    }

    public String getSenderSeq() {
        return senderSeq;
    }

    public void setSenderSeq(String senderSeq) {
        this.senderSeq = senderSeq;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public void setSenderProfileImage(String senderProfileImage) {
        this.senderProfileImage = senderProfileImage;
    }

    public String getChatRoomSeq() {
        return chatRoomSeq;
    }

    public void setChatRoomSeq(String chatRoomSeq) {
        this.chatRoomSeq = chatRoomSeq;
    }

    public String getJoinedMoimSeqList() {
        return joinedMoimSeqList;
    }

    public void setJoinedMoimSeqList(String joinedMoimSeqList) {
        this.joinedMoimSeqList = joinedMoimSeqList;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getChatRoomSee() {
        return chatRoomSee;
    }

    public void setChatRoomSee(String chatRoomSee) {
        this.chatRoomSee = chatRoomSee;
    }
}
