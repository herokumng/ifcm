package com.dkbmc.ifcm.module.common.message;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dkbmc.ifcm.dto.system.SYS2001;
import com.dkbmc.ifcm.dto.system.SYS3001;
import com.dkbmc.ifcm.dto.system.SYS3002;
import com.dkbmc.ifcm.module.common.log.Logger;
import com.dkbmc.ifcm.module.common.utils.IdGenerator;
import com.dkbmc.ifcm.module.common.utils.MetaUtils;
import com.dkbmc.ifcm.module.core.ModuleConst;

/**
 * @see 1. Input Module Processing Infom, Debugging Message & Custom Message ->
 *      Throwing to Logging 2. 기존 저장된 Return Message 등을 가져와 사용.
 *
 *      모듈 지정 호출 : MessageHandler.getInstance().setModuleMessage(sys3001.type(LogTypeDef.LogLevel.name()), sys3001.code, null or "메세지" );
 *      커스텀 호출(e.g. Debugging) : MessageHandler.getInstance().setCustomMessage(임의 지정 null일 경우 내부 정의 값 사용(sys3002.type)
 *      																		,임의 지정 null일 경우 내부 정의 값 사용(sys3002.action)
 *      																		, 임의지정 OR sys3001.type , 임의지정 OR sys3001.code, "메세지" );
 *
 * @author DY
 *
 */
public class MessageHandler {
	private List<SYS3001> sys3001; // type, code, message
	private List<SYS3002> sys3002; // type, action
	private MetaUtils metaUtil;
	private Map<String, Object> searchCondition;
	private Logger logger;
	private String creator, client, session;
	
	private MessageHandler() {
		metaUtil = new MetaUtils();
	}

	private static class MessageHelper {
		private static final MessageHandler messageHandler = new MessageHandler();
	}

	public static MessageHandler getInstance() {
		return MessageHelper.messageHandler;
	}
	
	public void setLogResource(List<SYS3001> sys3001_list, List<SYS3002> sys3002_list, Logger init_logger) {
		sys3001 = sys3001_list;
		sys3002 = sys3002_list;
		logger = init_logger;
	}
	
	public void setUserInfo(Map<String, Object> user_info) {
		creator = String.valueOf(user_info.get("creator"));
		client = String.valueOf(user_info.get("client"));
		session = String.valueOf(user_info.get("session"));
	}
	
	public void setCustomMessage(String type, String action, String message_type, String message_code, Object message) {
		Map<String, String> callerModule = getCallerModulePath();

		String classPath = callerModule.get(MessageConst.TRACE_KEY_CLASS);
		String methodName = callerModule.get(MessageConst.TRACE_KEY_METHOD);

		setMessageToLogForm((type != null ? type : getModuleType(classPath, methodName)),
							(action != null ? action : getModuleAction(classPath, methodName)),
							message_type,
							message_code,
							message,
							callerModule.get(MessageConst.TRACE_KEY_CLASS) + MessageConst.TRACE_PATH_SEPARATOR
							+ callerModule.get(MessageConst.TRACE_KEY_METHOD) + MessageConst.TRACE_PATH_SEPARATOR
							+ callerModule.get(MessageConst.TRACE_KEY_LINE));
	}

	public void setModuleMessage(String message_type, String message_code, Object message) {
		Map<String, String> callerModule = getCallerModulePath();

		String classPath = callerModule.get(MessageConst.TRACE_KEY_CLASS);
		String methodName = callerModule.get(MessageConst.TRACE_KEY_METHOD);

		setMessageToLogForm(getModuleType(classPath, methodName),
							getModuleAction(classPath, methodName),
							message_type,
							message_code,
							(message == null ? getMessage(message_type, message_code) : message),
							callerModule.get(MessageConst.TRACE_KEY_CLASS) + MessageConst.TRACE_PATH_SEPARATOR
						  + callerModule.get(MessageConst.TRACE_KEY_METHOD) + MessageConst.TRACE_PATH_SEPARATOR
						  + callerModule.get(MessageConst.TRACE_KEY_LINE));
	}

	public String getMessage(String message_type, String message_code) {
		String message = null;
		searchCondition = new HashMap<>();
		searchCondition.put(ModuleConst.MODULE_KEY_TYPE, message_type.toLowerCase());
		searchCondition.put(ModuleConst.MODULE_KEY_CODE, message_code);

		try {
			message = (String) metaUtil.getMetaEntityItemValue(sys3001, searchCondition, MessageConst.FIELD_KEY_MESSAGE);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	// Alert Or service return message
	public String setReturnMessage(String type, String code) {
		return getMessage(type, code);
	}

	private void setMessageToLogForm(String type, String action, String message_type, String message_code,
									 Object message, String path_detail) {
		SYS2001 logForm = new SYS2001();

		logForm.setId(IdGenerator.getInstance().getId());
		logForm.setDate_time(LocalDateTime.now());
		logForm.setType(type);
		logForm.setAction(action);
		logForm.setMsg_type(message_type);
		logForm.setMsg_code(message_code);
		logForm.setMessage(message);
		logForm.setPath_detail(path_detail);
		logForm.setCreator(creator);
		logForm.setClient(client);
		logForm.setSession(session);
		// Logging, log File 처리 호출
		logger.printLog(message_type, logForm.getLoggingForm());
		logger.writeLog(message_type, logForm.getSavingForm());
	}
	
	private String getModuleType(String class_name, String method_name) {
		return getTypeActionValue(class_name, method_name, ModuleConst.MODULE_KEY_TYPE);
	}

	private String getModuleAction(String class_name, String method_name) {
		return getTypeActionValue(class_name, method_name, MessageConst.FIELD_KEY_ACTION);
	}

	private String getTypeActionValue(String class_name, String method_name, String find_key) {
		String itemValue = null;
		searchCondition = new HashMap<>();
		searchCondition.put(MessageConst.TRACE_KEY_CLASS, class_name);
		searchCondition.put(MessageConst.TRACE_KEY_METHOD, method_name);

		try {
			itemValue = (String) metaUtil.getMetaEntityItemValue(sys3002, searchCondition, find_key);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemValue;
	}

	private Map<String, String> getCallerModulePath() {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		Map<String, String> callerModule = null;
		for (int index = ModuleConst.MODULE_DIGIT_0; index < stackTrace.length; index++) {
			if (index == ModuleConst.MODULE_DIGIT_2) {
				callerModule = new HashMap<>();
				callerModule.put(MessageConst.TRACE_KEY_CLASS, stackTrace[index].getClassName());
				callerModule.put(MessageConst.TRACE_KEY_METHOD, stackTrace[index].getMethodName());
				callerModule.put(MessageConst.TRACE_KEY_LINE, String.valueOf(stackTrace[index].getLineNumber()));
			}
		}
		return callerModule;
	}

}