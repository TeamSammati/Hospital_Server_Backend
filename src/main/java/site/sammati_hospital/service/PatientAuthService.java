package site.sammati_hospital.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PatientAuthService {
    private static final Integer EXPIRE_MIN = 5;
    private static LoadingCache<String, Integer> otpCache;

    public PatientAuthService()
    {
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }

    public static Integer generateOTP(String key)
    {
        Random random = new Random();
        int OTP = (100000 + random.nextInt(900000))%10;
        otpCache.put(key, OTP);

        return OTP;
    }

    public static String getOPTByKey(String key)
    {
        Integer otp = otpCache.getIfPresent(key);
        if(otp==null)
            return "-99";
        else return otp.toString();
    }

    public static void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }
}