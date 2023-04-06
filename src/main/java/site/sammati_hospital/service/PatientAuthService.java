package site.sammati_hospital.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PatientAuthService {
    private static final Integer EXPIRE_MIN = 10;
    private static LoadingCache<String, String> otpCache;

    public PatientAuthService()
    {
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return null;
                    }
                });
    }

    public static String genString(String pid) {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        otpCache.put(pid, generatedString);
        return generatedString;
    }

    public static String getStrByPID(String pid)
    {
        String otp = otpCache.getIfPresent(pid);
        if(otp==null)
            return "-99";
        else return otp;
    }

    public static void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }
}