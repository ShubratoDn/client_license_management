package com.license.manager.config;

public class Constants {

	public static final Long ROLE_USER = 1001L;
	public static final Long ROLE_ADMIN = 1002L;
	
	public static final String ADMIN_SECRET = "MYADMIN";
	
	
	public static final String TRANSACTION_TYPE_PURCHASE = "PURCHASE";
	public static final String TRANSACTION_TYPE_RENEWAL = "RENEWAL";
	public static final String TRANSACTION_TYPE_REFUND = "REFUND";
	
	
	public static final String PAYMENT_STATUS_SUCCESSFUL = "SUCCESSFUL";
	public static final String PAYMENT_STATUS_PENDING = "PENDING";
	public static final String PAYMENT_STATUS_FAILED = "FAILED";
	
	
	public static final String NOTIFICATION_TYPE_EXPIRE_SOON = "EXPIRE_SOON";
	public static final String NOTIFICATION_TYPE_EXPIRED = "EXPIRED";
	public static final String NOTIFICATION_TYPE_RENEWAL = "RENEWAL";
}
