wrk.method = "POST"

wrk.body = "{\"to\":\"xxxx069@qq.com\",\"subject\":\"发货通知\",\"templateId\":\"1a0562c0-8431-4b4d-9125-838510c0f51f\",\"templateData\":{\"user_name\":\"老虎\",\"order_id\":\"12345678\"},\"priority\":3}"

wrk.headers["Content-Type"] = "application/json"

function request()
        return wrk.format('POST',nil,headers,body)
end

