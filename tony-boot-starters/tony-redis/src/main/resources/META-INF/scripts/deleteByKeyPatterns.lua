local cursor="0";
local num = 0;

for index, keyPattern in pairs(KEYS) do
    repeat
        local scanResult = redis.call("SCAN", cursor, "MATCH", keyPattern);
        local keys = scanResult[2];
        for i = 1, #keys do
            local key = keys[i];
            redis.call("DEL", key);
            num = num + 1;
        end;
        cursor = scanResult[1];
    until cursor == "0";
end
return num;
