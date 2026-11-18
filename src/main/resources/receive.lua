local stock = tonumber(redis.call("GET", KEYS[1]))
if stock == nil or stock <= 0 then
    return 0
end
local result = redis.call("set",KEYS[2],ARGV[1],"NX","EX",10)
if result then
    redis.call("decr",KEYS[1])
    return 1
end
    return 2