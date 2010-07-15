package ws.rrd.mem;

import java.io.IOException;
import java.io.OutputStream; 
import java.util.HashMap;
import java.util.Properties;

import com.lowagie.text.pdf.codec.Base64.InputStream;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  14.04.2010::10:50:13<br> 
 */
public class MemoryFileCache {
	static{
		 // try to load default cache conf
		 if (System.getProperty("com.google.appengine.runtime.version")==null){
			 //net.sf.jsr107cache.CacheFactory=ws.rrd.cache.BasicCacheFactory
			 try{
				 java.io.InputStream in = MemoryFileCache.class.getClassLoader().getResourceAsStream("jcache.properties");
				 Properties prTmp = new Properties();
				 prTmp.load(in);
				 String key = "net.sf.jsr107cache.CacheFactory";
				 String val = prTmp.getProperty(key);
				 System.setProperty(key, val);
			 }catch(Exception e){
				 e.printStackTrace();
			 } 
		 }		
	}
                    
	 public static MemoryFileItem get (String name) throws IOException{
		Cache cache = getCache();
		MemoryFileItem retval = (MemoryFileItem) cache.get(name);
		 if (retval ==null){ // try to restore parts
			  for (int i=0;cache.get(name+"::"+i)!=null;){
				 MemoryFileItem next = (MemoryFileItem)cache.get(name+"::"+i);				 
				 if (i==0 ){
					 retval = new MemoryFileItem (next.fileName,next.contentType,next.isFormField(),next.fileName, 0);
				 }
				 retval.getOutputStream().write(next.get());
				 i++;
			 }
			 retval.flush();
		 }
		 return retval;
	 }
	 static int MAX_SIZE = 64*1024;
	 static int MAX_BUFF_SIZE = MAX_SIZE;
	 public static String put (MemoryFileItem  item) throws IOException{
		 Cache cache = getCache();
		 String name = item.getName();
		 byte[] bs = item.get();
		 
		if (bs.length < MAX_SIZE){
			 cache.put(name,item);
		 }else{ //SPLIT
			 int done = 0;
			 for (int i=0;done<bs.length ;i++){
				 String nameTmp = item.getName()+"::"+i;
				 MemoryFileItem itemNext =  new MemoryFileItem (item.fileName,item.contentType,item.isFormField(),item.fileName, 0);
				 OutputStream outputStream = itemNext.getOutputStream();
				 outputStream.write(bs, done,Math.min( MAX_BUFF_SIZE, bs.length-done ));
				 itemNext.flush();
				 done += MAX_BUFF_SIZE;
				 try{
					 cache.put(nameTmp,itemNext);
				 }catch(Throwable e){
					 e.printStackTrace();
					 throw new IOException (e);
				 }
			 }
		 }
		 return name;
	 }

	public static Cache getCache()   { 
		CacheManager cm = CacheManager.getInstance();
		Cache retval = cm.getCache ("rrd");
		if (retval == null)
		synchronized (CacheManager.class) { 
			if (retval == null)
			try {
				CacheFactory cacheFactory;
				cacheFactory = cm.getCacheFactory();
				HashMap props = new HashMap();
				Cache cacheTmp;
				cacheTmp = cacheFactory.createCache(props);
				cm.registerCache("rrd", cacheTmp);
				retval = cacheTmp;
			} catch (CacheException e) { 
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} 
		return  retval; 
	}

}


 