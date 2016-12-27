package defresult;

import org.springframework.stereotype.Service;

/**
 * Created by User on 27.12.2016.
 */
@Service
public class SecureAppService {

    public String method(String str){
        return str.toUpperCase();
    }
}
