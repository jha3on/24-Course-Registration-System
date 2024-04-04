-- http://redisgate.kr/redis/command/lua.php
local current = tonumber(redis.call('get', KEYS[1]))
if current - ARGV[1] >= 0 then
    redis.call('decrby', KEYS[1], ARGV[1])
    return true
else
    return false
end