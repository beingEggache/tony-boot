local batchSize = tonumber(ARGV[1]) or 100
local count = tonumber(ARGV[2]) or 1000
local total = 0

for index, keyPattern in ipairs(KEYS) do
    local cursor = "0"
    local num = 0
    local batch = {}
    repeat
        local scanResult = redis.call("SCAN", cursor, "MATCH", keyPattern, "COUNT", count)
        local keys = scanResult[2]
        for i = 1, #keys do
            table.insert(batch, keys[i])
            if #batch == batchSize then
                redis.call("DEL", unpack(batch))
                num = num + #batch
                batch = {}
            end
        end
        cursor = scanResult[1]
    until cursor == "0"
    if #batch > 0 then
        redis.call("DEL", unpack(batch))
        num = num + #batch
    end
    total = total + num
end
return total
