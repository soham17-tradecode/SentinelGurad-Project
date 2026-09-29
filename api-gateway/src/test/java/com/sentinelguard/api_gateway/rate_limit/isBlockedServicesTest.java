package com.sentinelguard.api_gateway.rate_limit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.redis.core.ValueOperations;
@ExtendWith(MockitoExtension.class)
public class isBlockedServicesTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ipBlockService ipBlockService;

    @Test
    void isIpBlocked_shouldReturnWhenIpIsBlocked()
    {


        when(redisTemplate.hasKey("blocked_ip:127.0.0.1"))
                .thenReturn(true);
        boolean result = ipBlockService.isIpBlocked("127.0.0.1");

        assertTrue(result);

        verify(redisTemplate).hasKey("blocked_ip:127.0.0.1");
    }

    @Test
    void blockIp()
    {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        ipBlockService.blockIp("127.0.0.1");
        verify(valueOperations).set("blocked_ip:127.0.0.1","blocked");

    }
    @Test
    void unBlockedIp()
    {
        ipBlockService.unBlockIp("127.0.0.1");
        verify(redisTemplate).delete("blocked_ip:127.0.0.1");
    }
}
