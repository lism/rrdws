package cc.co.llabor.system; 

import java.util.logging.Logger;

import cc.co.llabor.mail.MailHandlerServlet;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 *         <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 * Creation: 03.09.2010::09:57:16<br>
 */
public class ExitTrappedException extends SecurityException {
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -4173081033038672726L; 

	private static final Logger log = Logger.getLogger(ExitTrappedException.class.getName());
	static SecurityManager smOld = null;
	private static boolean reseted = false;
	static {
		try{
			smOld = System.getSecurityManager();
			forbidSystemExitCall();
			reseted = true;
			log.info( "--------- exitVM WILL BE IGNORED! -------------");
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	public static void forbidSystemExitCall() {
		
		final SecurityManager securityManager = new SecurityManager() {
			public void checkPermission(java.security.Permission permission) {
				if ("exitVM".equals(permission.getName())) {
					log.warning("exitVM IGNORED!");
					throw new ExitTrappedException();					
				}else if (smOld!=null){
					smOld.checkPermission(permission);
				}
			}
		};
		try{
			System.setSecurityManager(securityManager);
		}catch(Throwable e){
			String msg = e.getMessage();
			log.warning(msg);
			e.printStackTrace();
		}
	}
	public static void enableSystemExitCall() {
		
		try{
			System.setSecurityManager(smOld);
		}catch(Throwable e){
			String msg = e.getMessage();
			log.warning(msg);
			e.printStackTrace();
		}		
	}
	public static boolean isReseted() {
			return reseted;
	}
}