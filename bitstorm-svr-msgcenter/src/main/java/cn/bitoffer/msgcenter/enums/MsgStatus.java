package cn.bitoffer.msgcenter.enums;

public enum MsgStatus {
    Pending(1),
    Processiong(2),
    Succeed(3),
    Failed(4);

    private MsgStatus(int status) {
        this.status = status;
    }
    private int status;

    public int getStatus() {
        return this.status;
    }
}
