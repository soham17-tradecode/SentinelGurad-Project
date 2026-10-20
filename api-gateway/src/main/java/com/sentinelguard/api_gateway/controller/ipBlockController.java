package com.sentinelguard.api_gateway.controller;

import com.sentinelguard.api_gateway.rate_limit.ipBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ip-blocks")
public class ipBlockController {

    private final ipBlockService ipBlockService;

    @PostMapping("/{ip}")
    public ResponseEntity<Map<String ,String>> blocksIp(@PathVariable String ip)
    {

        ipBlockService.blockIp(ip);

        return ResponseEntity.ok(Map.of("message","Ip address manually blocked","ip",ip));

    }
    @DeleteMapping("/{ip}")
    public ResponseEntity<Map<String ,String>> unBlocksIp(@PathVariable String ip)
    {
        ipBlockService.unBlockIp(ip);

        return ResponseEntity.ok(Map.of("message","Manually ip blocked removed","ip",ip));

    }

    @GetMapping("/{ip}")
    public ResponseEntity<Map<String , Object>> checkIp(@PathVariable String ip)
    {

        return ResponseEntity.of(Optional.of(Map.of("ip", ip, "blocked", ipBlockService.isIpBlocked(ip))));
    }




}
