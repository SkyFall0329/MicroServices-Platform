package cn.bitoffer.msgcenter.tools;

public interface RateLimitService {

    boolean isRequestAllowed(String sourceId,int channel,boolean isTimerMsg);
}
