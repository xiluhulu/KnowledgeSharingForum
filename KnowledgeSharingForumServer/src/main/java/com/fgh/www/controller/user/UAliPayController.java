package com.fgh.www.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fgh.www.common.config.AliPayCongig;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Orders;
import com.fgh.www.service.IOrdersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//跨域问题
@CrossOrigin
@RestController
@RequestMapping("/user-api/alipay")
public class UAliPayController {
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";
    @Autowired
    private IOrdersService orderService;
    @Resource
    private AliPayCongig alipayConfig;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    /**
     * 支付宝支付接口
     * @param orders
     * @return
     * @throws IOException
     */
    @PostMapping("/pay")
    public R pay(@RequestBody Orders orders) throws IOException {
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);
        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
//        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        // 订单编号
        bizContent.put("out_trade_no", orders.getOrderNo());
        // 订单金额
        bizContent.put("total_amount", orders.getAmountPaid());
        // 支付的商品名称
        bizContent.put("subject", orders.getProductName());
        // 固定配置
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:8080/person/myOrder");
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", form);
    }

    @PostMapping("/notify")  // 注意z这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        System.out.println("=========支付宝异步回调========");
        try {
            // 将请求参数转换为Map结构（键值对）
            Map<String, String> params = convertParams(request);
            // 验证交易状态是否为"TRADE_SUCCESS"
            if (!"TRADE_SUCCESS".equals(params.get("trade_status"))) {
                System.out.println("收到非成功状态通知: {}"+ params.get("trade_status"));
                return "failure"; // 非成功状态直接返回失败
            }
            // 基础校验：检查商户订单号是否存在
            String tradeNo = params.get("out_trade_no");
            if (StringUtils.isBlank(tradeNo)) {
                System.out.println("订单号为空: {}"+params);
                return "failure";
            }
            // 支付宝签名验证（核心安全校验）
            String sign = params.get("sign"); // 获取签名值
            // 生成待验签字符串（按支付宝规则排序参数）
            String content = AlipaySignature.getSignCheckContentV1(params);
            // 使用RSA256算法验证签名
            boolean checkSignature = AlipaySignature.rsa256CheckContent(
                    content,
                    sign,
                    alipayConfig.getAlipayPublicKey(), // 从配置获取支付宝公钥
                    "UTF-8"
            );
            // 验签失败处理
            if (!checkSignature) {
                System.out.println("验签失败，疑似非法请求 | 订单号:{}"+tradeNo);
                return "failure"; // 防止伪造请求
            }
            // 所有验证通过，处理支付成功逻辑
            orderService.setAlipaySerialNumberAndOrderStatus( params.get("trade_no"),tradeNo);
            // 必须返回success告知支付宝停止发送通知
            return "success";
        } catch (Exception e) {
            // 捕获所有异常并记录错误堆栈
            System.out.println("处理支付通知异常");
            return "failure"; // 返回failure会触发支付宝重试机制
        }
    }
    /**
     * 将HTTP请求参数转换为Map<String, String>
     * @param request HTTP请求对象
     * @return 参数Map（处理多值参数为逗号分隔字符串）
     */
    // 将HTTP请求参数转换为Map<String, String>
    private Map<String, String> convertParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        // 遍历所有参数（包括multi-value参数）
        request.getParameterMap().forEach((key, values) ->
                // 使用Apache Commons的StringUtils拼接多值参数
                params.put(key, StringUtils.join(values, ",")));
        return params;
    }
}
