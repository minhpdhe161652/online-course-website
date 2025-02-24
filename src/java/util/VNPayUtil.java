package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayUtil {

    private static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_TMN_CODE = "T9XOSIVL"; // Thay bằng mã TMN
    private static final String VNP_HASH_SECRET = "QXFI9H6EJDP18BHF0QU88JFRXOXHUOSQ"; // Thay bằng mã bí mật
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_CURRENCY = "VND";
    private static final String VNP_ORDER_TYPE = "270000";
    private static final String VNP_LOCALE = "vn"; // Ngôn ngữ giao diện VNPay

    public static String getPaymentUrl(int transactionID, double amount, String returnUrl, String ipAddress)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf((long) (amount * 100)));
        vnpParams.put("vnp_CurrCode", VNP_CURRENCY);
        vnpParams.put("vnp_TxnRef", String.valueOf(transactionID));
        vnpParams.put("vnp_OrderInfo", "Thanh toan khoa hoc " + transactionID);
        vnpParams.put("vnp_Locale", VNP_LOCALE);
        vnpParams.put("vnp_OrderType", VNP_ORDER_TYPE);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", ipAddress); // Bổ sung địa chỉ IP
        vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Sắp xếp tham số theo key alphabet
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        // Tạo query string
        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = URLEncoder.encode(vnpParams.get(fieldName), "UTF-8");
            if (query.length() > 0) {
                query.append("&");
                hashData.append("&");
            }
            query.append(fieldName).append("=").append(value);
            hashData.append(fieldName).append("=").append(value);
        }

        // Ký dữ liệu với secret key (HMAC-SHA512)
        String secureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return VNP_PAY_URL + "?" + query.toString();
    }

    private static String hmacSHA512(String key, String data) {
    try {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(data.getBytes());

        // Chuyển đổi sang Hex (chuỗi 128 ký tự)
        StringBuilder hexHash = new StringBuilder();
        for (byte b : hashBytes) {
            hexHash.append(String.format("%02x", b));
        }
        return hexHash.toString();
    } catch (Exception e) {
        throw new RuntimeException("Error while hashing data", e);
    }
}

}
