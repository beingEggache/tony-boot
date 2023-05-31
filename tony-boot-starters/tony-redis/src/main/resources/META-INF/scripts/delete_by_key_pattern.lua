local keys = redis.call('keys', KEYS[1])
for i,k in ipairs(keys) do
    local res = redis.call('del', k)
end
