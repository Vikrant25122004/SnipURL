package com.SnipURL.SnipURL.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Redis_Service {
    @Autowired
    private RedisTemplate redisTemplate;
    public <T> T get(String key, Class<T> entityclass) throws JsonProcessingException {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(entityclass==String.class){
                return entityclass.cast(o);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(),entityclass);
        }
        catch (Exception e){
            log.error("Exception" + e);
            return null;
        }
    }
    public void setLog(String key,Object o, Long ttl){
        try{
            redisTemplate.opsForValue().set(key, o.toString(), TimeUnit.SECONDS.ordinal());
        }
        catch (Exception e){
            log.error("Exception", e);

        }
    }

}
