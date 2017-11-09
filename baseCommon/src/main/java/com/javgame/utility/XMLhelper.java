
package com.javgame.utility;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.javgame.pay.OrderResponse;

import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**  XML  解析 帮助类 ，并支持通用的解析方法
 * @author apple
 *
 */
public class XMLhelper {
	
    private static final String TAG = XMLhelper.class.getSimpleName();
    
    /**
     * 解析订单返回值
     * @param content
     * @return
     */
    public static OrderResponse parserOrderResponse(String content) {
    	OrderResponse orderResponse = new OrderResponse();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				if(event == XmlPullParser.START_TAG){
					String nodeName=parser.getName();
					if(TextUtils.equals("status",nodeName)){
						orderResponse.setStatus(Integer.valueOf(parser.nextText())); 
					}else if(TextUtils.equals("msg",nodeName)){
						orderResponse.setMsg(parser.nextText()); 
					}else if(TextUtils.equals("expand",nodeName)){
						orderResponse.setExpand(parser.nextText()); 
					}
				}
				event = parser.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(TAG,"parse xml error",e);
		}catch (IOException e) {
			Log.e(TAG,"read xml error",e);
		}catch(NumberFormatException e){
			Log.e(TAG,"parse string to int error",e);
		}
		return orderResponse;
	}
    
    /**
     * 解析服务端返回的xml格式
     * @param content
     * @return
     */
    public static Map<String,String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String nodeName=parser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					if("xml".equals(nodeName)==false && "info".equals(nodeName) == false){
						xml.put(nodeName,parser.nextText());
					}
					break;
				}
				event = parser.next();
			}
			
			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}
    
    /*public static OrderResponse parserOrderResponse(String response) {
        OrderResponse orderResponse = new OrderResponse();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));
            Element root = document.getDocumentElement();
            orderResponse.setStatus(Integer.parseInt(getElementText(root,"status")));
            orderResponse.msg =  getElementText(root,"msg");
            orderResponse.expand =  getElementText(root,"expand");
        } catch (Exception e) {
            Log.e(TAG, " parserOrderResponse error response = " + response , e);
            orderResponse.status = OrderResponse.ERROR;
            orderResponse.msg = "系统错误，client parser xml error" ;
        }
        return orderResponse;
    }*/

    
    /*public static NotifyResponse parserNotifyResponse(String response) {
        NotifyResponse notifyResponse = new NotifyResponse();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));
            Element root = document.getDocumentElement();
            notifyResponse.status = Integer.parseInt(  getElementText(root,"status"));
            notifyResponse.msg =  getElementText(root,"msg");
        } catch (Exception e) {
            Log.e(TAG, " parserNotifyResponse error response = " + response , e);
            notifyResponse.status = OrderResponse.ERROR;
            notifyResponse.msg = "系统错误，client parser xml error" ;
        }
        return notifyResponse;
    }*/
    
    /*public static boolean parserOrderStatus(String response) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));
            Element root = document.getDocumentElement();
            boolean success = "1".equals(getElementText(root,"status"));
            return success ;
        } catch (Exception e) {
            Log.e(TAG, " parserOrderResponse error response = " + response , e);
        }
        return false;

    }*/
    


    /*public static String createOrderXML(Order order) {
        String xml = null;
        try {
            XmlSerializer serializer = Xml.newSerializer();
            Writer output = new StringWriter();
            // we set the FileOutputStream as output for the serializer, using
            // UTF-8 encoding
            serializer.setOutput(output);
            // Write <?xml declaration with encoding (if encoding not null) and
            // standalone flag (if standalone not null)
            serializer.startDocument(null, null);
            // set indentation option
            
        
           //serializer.setFeature("http://xmlpull.org/v1/doc/features.html#process-docdecl", true);
            //serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", false);
            // start a tag called "root"
            serializer.startTag(null, "info");
            Log.e(TAG,  " " + order ) ;
            Log.e(TAG,  " " + order.codepayid ) ;
            addEcement(serializer, PayHelper.Codepayid, order.codepayid);
            addEcement(serializer, PayHelper.Systype, PayHelper.Android);
            addEcement(serializer, PayHelper.Paytype, order.payTypeString);
            addEcement(serializer, PayHelper.OrderID, order.orderID);
            addEcement(serializer, PayHelper.Status, 1);
            addEcement(serializer, PayHelper.amount, order.price);
            addEcement(serializer, "remark", order.remark);
            serializer.endTag(null, "info");
            serializer.endDocument();
            // write xml data into the FileOutputStream
            serializer.flush();
            // finally we close the file stream
            xml = output.toString();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
            Log.e(TAG, "createOrderXML error", e);
        }
        
        xml = removeDeclare(xml);
        if (PayHelper.DEBUG){
            Log.d(TAG, " OrderXML " + xml);
        }
        return xml;
    }*/

    /*public static String removeDeclare(String xml) {
        int index =  xml.indexOf("?>") ;
        if (index <0){
            return xml ;
        }else{
            return xml.substring(index + 2,xml.length());
        }
    }*/

    public static void addEcement(XmlSerializer serializer, String name, float price)
            throws Exception {
        addEcement(serializer, name, String.valueOf(price));
    }

    public static void addEcement(XmlSerializer serializer, String name, String value)
            throws Exception {
        if (value ==null){
            value = "" ;
        }
        serializer.startTag(null, name);
        serializer.text(value);
        serializer.endTag(null, name);
    }
    
    public static String getElementText(Element root, String name) {
        try {
            Element ele = (Element)root.getElementsByTagName(name).item(0) ;
            return ele.getFirstChild().getNodeValue().trim();
        } catch (Exception e) {
                return "" ;
        }
    }

}
