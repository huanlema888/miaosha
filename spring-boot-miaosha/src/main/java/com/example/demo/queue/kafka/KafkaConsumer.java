package com.example.demo.queue.kafka;

import com.example.demo.common.enums.SeckillStatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Result;
import com.example.demo.common.redis.RedisUtil;
import com.example.demo.common.webSocket.WebSocketServer;
import com.example.demo.service.ISeckillService;

/**
 * 消费者 spring-kafka 2.0 + 依赖JDK8
 */
@Component
public class KafkaConsumer {
	@Autowired
	private ISeckillService seckillService;

	@Autowired
	private RedisUtil redisUtil;
    /**
     * 监听seckill主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message){
		/**
		 * 收到通道的消息之后执行秒杀操作
		 */
		String[] array = message.split(";");
    	if(redisUtil.getValue(array[0])==null){
    		Result result = seckillService.startSeckilAopLock(Long.parseLong(array[0]), Long.parseLong(array[1]));
			if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
    			WebSocketServer.sendInfo("秒杀成功", array[0]);
    		}else{
    			WebSocketServer.sendInfo("秒杀失败", array[0]);
    			redisUtil.cacheValue(array[0], "ok");
    		}
    	}else{
    		WebSocketServer.sendInfo(array[0], "秒杀失败");
    	}
    }
}