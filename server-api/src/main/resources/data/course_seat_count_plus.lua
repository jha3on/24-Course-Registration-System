-- http://redisgate.kr/redis/command/lua.php
local current = tonumber(redis.call('get', KEYS[1]))
redis.call('incrby', KEYS[1], ARGV[1])
return true