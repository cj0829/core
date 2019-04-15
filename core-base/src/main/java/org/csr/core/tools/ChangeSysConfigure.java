package org.csr.core.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.csr.core.util.ObjUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ChangeSysConfigure {

	final String fileName = File.separator + "configure.properties";

	final String persistence = File.separator + "/META-INF/persistence.xml";

	private static Map<String, String> url = new HashMap<String, String>();
	private static Map<String, String> driverClassName = new HashMap<String, String>();
	private static Map<String, String> dialect = new HashMap<String, String>();
	private static Map<String, String> database = new HashMap<String, String>();
	private static Map<String, String> cache_factory_class = new HashMap<String, String>();

	static {
		url.put("mysql","jdbc:mysql://{hostname}:3306/{dbName}?useUnicode=true&characterEncoding=utf-8");
		url.put("oracle", "jdbc:oracle:thin:@{hostname}:1521:{dbName}");
		driverClassName.put("mysql", "com.mysql.jdbc.Driver");
		driverClassName.put("oracle", "oracle.jdbc.driver.OracleDriver");
		
		dialect.put("mysql", "org.hibernate.dialect.MySQL5Dialect");
		dialect.put("oracle", "org.hibernate.dialect.Oracle10gDialect");
		
		database.put("mysql", "MYSQL");
		database.put("oracle", "ORACLE");

		cache_factory_class.put("redis", "org.hibernate.cache.redis.RedisRegionFactory");
		cache_factory_class.put("ehcache","org.hibernate.cache.ehcache.EhCacheRegionFactory");

	}

	String databaseType;
	String hostname;
	String dbName;
	String username;
	String password;
	String second_level_cache;
	String query_cache;
	String factory_class;


	public ChangeSysConfigure(String databaseType, String hostname, String dbName,
			String username, String password, String second_level_cache,
			String query_cache, String factory_class) {
		this.databaseType = databaseType;
		this.hostname = hostname;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
		this.second_level_cache = second_level_cache;
		this.query_cache = query_cache;
		this.factory_class = factory_class;
	}

	public static void main(String[] args) throws Exception {
		String filePath = System.getProperty("filePath");
//		String filePath ="D:/workspace/work2016-w/exam-web/src/main/resources";
		String databaseType = System.getProperty("databaseType");
		String hostname = System.getProperty("hostname");
		String dbName = System.getProperty("dbName");
		String username = System.getProperty("username");
		String password = System.getProperty("password");
		String second_level_cache = System.getProperty("second_level_cache");
		String query_cache = System.getProperty("query_cache");
		String factory_class = System.getProperty("factory_class");
		ChangeSysConfigure db = new ChangeSysConfigure(databaseType, hostname, dbName, username,password, second_level_cache, query_cache, factory_class);
		db.changeConfigure(filePath);
		db.changePersistence(filePath);

		System.exit(0);
	}

	private void changeConfigure(String filePath) throws FileNotFoundException,IOException {
		SequencedProperties prop = new SequencedProperties();
		prop.load(new FileInputStream(new File(filePath + fileName)));
		prop.put("jdbc.driverClassName", driverClassName.get(databaseType));
		String dburl = url.get(databaseType).replace("{hostname}", hostname).replace("{dbName}", dbName);
		System.out.println(dburl);
		prop.put("jdbc.url", dburl);
		prop.put("jdbc.username", username);
		prop.put("jdbc.password", password);
		prop.put("jpa.database", database.get(databaseType));
		prop.store(new FileOutputStream(new File(filePath + fileName)),databaseType);
	}

	private void changePersistence(String filePath)throws FileNotFoundException, IOException {
		DocumentBuilderFactory dvf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dvf.newDocumentBuilder();
			Document doc = (Document) db.parse(new File(filePath+persistence));
			Element root = doc.getDocumentElement();
			NodeList propertiesNodeList = root.getElementsByTagName("properties");
			if (ObjUtil.isNotEmpty(propertiesNodeList)) {
				for (int i = 0; i < propertiesNodeList.getLength(); i++) {
					Node propertiesNode = propertiesNodeList.item(i);
					if (propertiesNode.hasChildNodes()) {
						NodeList propertyNodeList = propertiesNode.getChildNodes();
						boolean isUpdate = false;
						for (int n = 0; n < propertyNodeList.getLength(); n++) {
							Node propertyNode = propertyNodeList.item(n);
							
							if (propertyNode.hasAttributes()) {
								Node attrName = propertyNode.getAttributes().getNamedItem("name");
								if(ObjUtil.isEmpty(attrName)){
									continue;
								}
								if ("hibernate.dialect".equals(attrName.getNodeValue())) {
									isUpdate=true;
									Node attrValue = propertyNode.getAttributes().getNamedItem("value");

									if (ObjUtil.isEmpty(attrValue)) {
									} else {
										attrValue.setNodeValue(dialect.get(databaseType));
									}
									System.out.println("修改hibernate.dialect="+dialect.get(databaseType));
								}
								
								if (ObjUtil.isNotEmpty(second_level_cache) && "hibernate.cache.use_second_level_cache".equals(attrName.getNodeValue())) {
									isUpdate=true;
									Node attrValue = propertyNode.getAttributes().getNamedItem("value");
									if (ObjUtil.isEmpty(attrValue)) {
									} else {
										attrValue.setNodeValue(second_level_cache);
									}
									System.out.println("修改hibernate.cache.use_second_level_cache"+second_level_cache);
								}
								
								if (ObjUtil.isNotEmpty(query_cache) && "hibernate.cache.use_query_cache".equals(attrName.getNodeValue())) {
									isUpdate=true;
									Node attrValue = propertyNode.getAttributes().getNamedItem("value");

									if (ObjUtil.isEmpty(attrValue)) {

									} else {
										attrValue.setNodeValue(query_cache);
									}
									System.out.println("修改hibernate.cache.region.factory_class="+query_cache);
								}
								
								if (ObjUtil.isNotEmpty(factory_class) && "hibernate.cache.region.factory_class".equals(attrName.getNodeValue())) {
									isUpdate=true;
									Node attrValue = propertyNode.getAttributes().getNamedItem("value");

									if (ObjUtil.isEmpty(attrValue)) {

									} else {
										attrValue.setNodeValue(cache_factory_class.get(factory_class));
									}
									
									System.out.println("修改hibernate.cache.region.factory_class="+cache_factory_class.get(factory_class));
								}
							}
						}
						if(isUpdate){
							System.out.println("修改xml配置文件");
							TransformerFactory tf = TransformerFactory.newInstance();
							Transformer transformer = tf.newTransformer();
							//添加xml 头信息
				            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				            //头的换行
				            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
							DOMSource source = new DOMSource(doc);
							PrintWriter pw = new PrintWriter(new FileOutputStream(filePath+persistence));
							StreamResult result = new StreamResult(pw);
							// 将xml写到文件中
							transformer.transform(source, result);
							System.out.println("生成XML文件成功!");
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
