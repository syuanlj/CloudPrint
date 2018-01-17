package com.fujitsu.baidu.waimai.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;

public class Test {
	/**
	 * 合作方唯一标示，由百度外卖分配
	 */


//	public static final String SOURCE = "";
	/**
	 * 合作方密钥，由百度外卖分配
	 */
//	public static final String SECRET = "";
//	public static final String SOURCE = "";
//	public static final String SECRET = "";

	public static final String SOURCE = "apitest";
	public static final String SECRET = "2xzbezxOmiI6tr3U";

	public static final String SHOPID = "";
	public static final String BAIDUSHOPID = "3086577816971585872";

	public static final String CMD ="shop.get";
	/**
	 * 签名计算的样例
	 * @param args
	 */
	public static void main(String[] args) {

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Cmd.class, new CmdSerializer())
				.registerTypeAdapter(Shop.class, new ShopSerializer())
				.disableHtmlEscaping()
				.create();

		String requestShopGet = cmdShopGetRequest(gson);  //shop.get


		//==================== 发送http请求 ==========================//
		String result = HttpRequest.sendPost("http://182.61.30.232:8087/", requestShopGet);
		System.out.print(result);


	}

	/**
	 * 计算MD5
	 * @param input
	 * @return
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext.toUpperCase();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 把中文转成Unicode码
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str){
		String result="";
		for (int i = 0; i < str.length(); i++){
			int chr1 = (char) str.charAt(i);
			if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
				result+="\\u" + Integer.toHexString(chr1);
			}else{
				result+=str.charAt(i);
			}
		}
		return result;
	}

	public static String cmdShopGetRequest(Gson gson){
		//准备body数据
		Shop shop = new Shop();
		shop.setShopId(SHOPID);
		shop.setbaiduShopId(BAIDUSHOPID);
		//shop.setName("测试商户<>http://abc.com/a.jpg");

		Cmd cmd = new Cmd();
		cmd.setCmd(CMD);
		cmd.setSource(SOURCE);
		cmd.setSecret(SECRET);
		cmd.setTicket(UUID.randomUUID().toString().toUpperCase());
		cmd.setTimestamp((int)(System.currentTimeMillis() / 1000));
		cmd.setVersion(3);
		cmd.setBody(shop);
		cmd.setSign(null);
		cmd.setEncrypt("");
//		String signJson = gson.toJson(cmd);
//		//对所有/进行转义
//	//	signJson = signJson.replace("/", "\\/");
//		//中文字符转为unicode
//		signJson = chinaToUnicode(signJson);
//		System.out.println(signJson);
//		String sign = getMD5(signJson);
		String bodyJson = gson.toJson(shop);
		//准备生成请求数据，此处注意secret不参与传递，故设置为null
//		cmd.setSign(sign);

		//	String requestJson = gson.toJson(cmd);
		//对所有/进行转义
		bodyJson = bodyJson.replace("/", "\\/");
		//中文字符转为unicode
		//	requestJson = chinaToUnicode(requestJson);
		System.out.print(cmd.getCmd());
		String  md5Input = "";
		md5Input+="body="+bodyJson+"&";
		md5Input+="cmd="+String.valueOf(cmd.getCmd())+"&";
		md5Input+="encrypt="+String.valueOf(cmd.getEncrypt())+"&";
		md5Input+="secret="+String.valueOf(cmd.getSecret())+"&";
		md5Input+="source="+String.valueOf(cmd.getSource())+"&";
		md5Input+="ticket="+String.valueOf(cmd.getTicket())+"&";
		md5Input+="timestamp="+String.valueOf(cmd.getTimestamp())+"&";
		md5Input+="version="+String.valueOf(cmd.getVersion());

		System.out.print(md5Input);
		System.out.println('\n');
		String sign = getMD5(md5Input);
		cmd.setSign(sign);

		String  requestParam = "";
		requestParam+="cmd="+String.valueOf(cmd.getCmd())+"&";
		requestParam+="version="+String.valueOf(cmd.getVersion())+"&";
		requestParam+="timestamp="+String.valueOf(cmd.getTimestamp())+"&";
		requestParam+="ticket="+String.valueOf(cmd.getTicket())+"&";
		requestParam+="source="+String.valueOf(cmd.getSource())+"&";
		requestParam+="sign="+String.valueOf(cmd.getSign())+"&";
		requestParam+="body="+bodyJson+"&";
		requestParam+="encrypt="+String.valueOf(cmd.getEncrypt());
		return requestParam;

	}

}
    
