package com.dkbmc.ifcm.module.mapping;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.dto.common.SalesforceResponse;
import com.dkbmc.ifcm.dto.mapping.*;
import com.dkbmc.ifcm.dto.source.SRC1001;
import com.dkbmc.ifcm.dto.source.SRC1101;
import com.dkbmc.ifcm.dto.target.TGT1001;
import com.dkbmc.ifcm.dto.target.TGT1101;
import com.dkbmc.ifcm.module.api.rest.SalesforceAPI;
import com.dkbmc.ifcm.module.common.file.FileHandler;
import com.dkbmc.ifcm.module.common.file.FileTypeDef;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.HandlerCreator;
import com.dkbmc.ifcm.module.common.message.MessageConst;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.module.common.utils.IdGenerator;
import com.dkbmc.ifcm.module.common.utils.MetaUtils;
import com.dkbmc.ifcm.module.common.utils.SystemChecker;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.dkbmc.ifcm.module.core.ModuleOperation;
import com.dkbmc.ifcm.module.data.convert.DataConverter;
import com.dkbmc.ifcm.module.data.load.CSVDataLoader;
import com.dkbmc.ifcm.module.data.load.IDataLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.logging.LogLevel;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 화면의 각 서비스에 매칭되는 프로세스 제공 서비스는 화면의 파라미터를 전달하고 프로세스 결과값을 받아 화면에 리턴한다.
 * <p>
 * 모듈 명세 : 각 함수는 Service Id와 동일하게 명명, public 으로 제공
 *
 * @author DY
 * @category Module Facade, singleton
 */

public class MappingProcess {
	private ModuleOperation operation;
	private MappingResource mappingResource;
	private FieldMappingResource fieldMappingResource;
	private FileHandler fileHandler;
	private MetaUtils metaUtil;
	private IDataLoader dataLoader;
	private DataConverter converter;
	private ObjectMapper objectMapper;
	// private final MappingUpdate update;
	private ResponseDTO response;
	private SalesforceAPI salesforceAPI;
	private String mapId, srcId, tgtId;
	private List<Map<String, String>> tgtSys = new ArrayList<>();
	private Map<String, Object> userInfo;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	LocalDateTime now;

	private MappingProcess() {
		operation = new ModuleOperation();
		operation.startup();
		mappingResource = new MappingResource();
		metaUtil = new MetaUtils();
		try {
			loadMappingResource();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class MappingProcessHelper {
		private static final MappingProcess mappingProcess = new MappingProcess();
	}
    
    public static MappingProcess getInstance() {
		return MappingProcessHelper.mappingProcess;
	}
	
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		salesforceAPI = new SalesforceAPI(objectMapper);
	}

	// Get Client Info
	public ResponseDTO wa000SVC000(Map<String, Object> request) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0000", null);
		response = new ResponseDTO();
		if (request != null) {
			userInfo = (Map<String, Object>) request;
			operation.setUserInfo(userInfo);
			response.setResult("200");
			response.setReturn_msg(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "0200"));
		} else {
			response.setResult("500");
			response.setReturn_msg(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "0500"));
		}
		return response;
	}

	// Mapping List
	public ResponseDTO wa000SVC001() {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0001", null);
		response = new ResponseDTO();
		Map<String, Object> rcvCmnItemList = new HashMap<>();
		rcvCmnItemList.put("system", operation.getModuleResource().getSys4001());
		rcvCmnItemList.put("category", operation.getModuleResource().getCat1001());
		response.setResult(rcvCmnItemList);
		return response;
	}

	/**
	 * 매핑 리스트 호출
	 *
	 * @param request 검색 요건
	 * @return 매핑리스트
	 */
	public ResponseDTO wa100SVC101(Map<String, Object> request) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0002", null);
		response = new ResponseDTO();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		List<Map<String, Object>> map1002 = new ArrayList<>();
		try {
			loadMappingResource();
			map1002 = objectMapper.convertValue(mappingResource.getMap1002(), new TypeReference<>() {
			});
			// 검색
			if (request.size() > 0) { // 검색 요건이 있을 경우에만 검색
				// 샐랙트 옵션값이 있으면 우선 셀렉트 값으로 필터링 한다.
				for (Map.Entry<String, Object> entry : request.entrySet()) {
					if (!entry.getKey().contains("dt") && !entry.getKey().equals("search_category")
							&& !entry.getKey().equals("search_txt")) {
						map1002 = map1002.stream()
								.filter(map -> !entry.getValue().equals("all")
										? ((String) map.get(entry.getKey())).contains(entry.getValue().toString())
										: ((String) map.get(entry.getKey())).matches((String) map.get(entry.getKey())))
								.collect(Collectors.toList());
					}
				}
				// 샐랙트 옵션값으로 필터링 한 후 검색어와, 날짜로 필터링하여 리스트화 한다.
				map1002 = map1002.stream()
						.filter(map -> ((String) map.get(request.get("search_category").toString()))
								.toLowerCase().contains(request.get("search_txt").toString().toLowerCase()))
						.toList().stream()
						.filter(map -> (LocalDateTime.parse(map.get("last_modified_datetime").toString(), formatter)
								.toLocalDate()
								.isEqual(ChronoLocalDate.from(LocalDate.parse(request.get("start_dt").toString())))
								|| LocalDateTime.parse(map.get("last_modified_datetime").toString(), formatter)
										.toLocalDate()
										.isAfter(ChronoLocalDate
												.from(LocalDate.parse(request.get("start_dt").toString()))))
								&& (LocalDateTime.parse(map.get("last_modified_datetime").toString(), formatter)
										.toLocalDate().isBefore(
												ChronoLocalDate.from(LocalDate.parse(request.get("end_dt").toString())))
										|| LocalDateTime.parse(map.get("last_modified_datetime").toString(), formatter)
												.toLocalDate()
												.isEqual(ChronoLocalDate
														.from(LocalDate.parse(request.get("end_dt").toString())))))
						.collect(Collectors.toList());
			}
			response.setResult(map1002);
		} catch (Exception e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1101", e.getMessage());
		}

		return response;
	}

	public ResponseDTO wa100SVC401(String mapping_id) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0003", null);
		response = new ResponseDTO();
		try {
			loadMappingResource();
			setRequiredIds(mapping_id);

			List<MAP1001> map1001 = mappingResource.getMap1001();
			List<MAP1002> map1002 = mappingResource.getMap1002();
			List<MAP1201> map1201 = mappingResource.getMap1201();
			List<MAP1301> map1301 = mappingResource.getMap1301();

			boolean deleteResult = false;

			Object[] mapIds = operation.getProperties().getMAPPING_FILE_IDS().split(ModuleConst.MODULE_DIV_REGEX);
			Object[] fieldIds = operation.getProperties().getMAPPING_FIELD_IDS().split(ModuleConst.MODULE_DIV_REGEX);
			// 맵핑 필드 파일 삭제
			for (int i = 0; i < fieldIds.length; i++) {
				operation.setResourcePathForFile(operation.getModuleResource().getSys1001(), fieldIds[i]);
				if (i == ModuleConst.MODULE_DIGIT_0) { // map1101
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + mapId);
				}
				if (i == ModuleConst.MODULE_DIGIT_1) { // map1102
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + mapId);
				}
				if (i == ModuleConst.MODULE_DIGIT_2) { // src1001
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + mapId);
				}
				if (i == ModuleConst.MODULE_DIGIT_3) { // src1101
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + srcId);
				}
				if (i == ModuleConst.MODULE_DIGIT_4) { // tgt1001
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + mapId);
				}
				if (i == ModuleConst.MODULE_DIGIT_5) { // tgt1101
					deleteResult = metaUtil.delMetaData(operation.getFileDirPath() + tgtId);
				}
				if (!deleteResult) {
					break;
				}
			}
			// 맵핑 파일 리스트 삭제
			if (deleteResult) {
				for (int i = 0; i < mapIds.length; i++) {
					operation.setResourcePathForFile(operation.getModuleResource().getSys1001(), mapIds[i]);
//                    List<Map<String, Object>> deletedList;
					if (i == ModuleConst.MODULE_DIGIT_0) { // map1001
						map1001 = objectMapper.convertValue(metaUtil.deleteList(map1001, mapId), new TypeReference<>() {
						});
						deleteResult = metaUtil.setMetaData(operation.getFilePath(), map1001);
					}
					if (i == ModuleConst.MODULE_DIGIT_1) { // map1002
						map1002 = objectMapper.convertValue(metaUtil.deleteList(map1002, mapId), new TypeReference<>() {
						});
						deleteResult = metaUtil.setMetaData(operation.getFilePath(), map1002);
					}
					if (i == ModuleConst.MODULE_DIGIT_2) { // map1201
						List<String> deletePaths = new ArrayList<>();
						fileHandler = new HandlerCreator().create(FileTypeDef.FileType.CSV);
						map1201.stream().filter(map -> map.getMap_id().equals(mapId)).forEach(map -> {
							deletePaths.add(map.getFile_path().substring(ModuleConst.MODULE_DIGIT_0,
									map.getFile_path().indexOf(mapId) + mapId.length()));
						});
						deletePaths.stream().distinct().forEach(path -> fileHandler.delete(path));
						map1201 = objectMapper.convertValue(metaUtil.deleteList(map1201, mapId), new TypeReference<>() {
						});
						deleteResult = metaUtil.setMetaData(operation.getFilePath(), map1201);
					}
					if (i == ModuleConst.MODULE_DIGIT_3) { // map1301
						map1301 = objectMapper.convertValue(metaUtil.deleteList(map1301, mapId), new TypeReference<>() {
						});
						deleteResult = metaUtil.setMetaData(operation.getFilePath(), map1301);
					}
					if (!deleteResult) {
						break;
					}
				}
			}
			if (deleteResult) {
				response.setResult("200");
				response.setReturn_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "1400"), "맵핑"));
			} else {
				response.setError_code("500");
				response.setError_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "2400"), "맵핑"));
			}
		} catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException
				| InstantiationException | ClassNotFoundException e) {
			response.setError_code("500");
			response.setError_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "2400"), "맵핑"));
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1401", e.getMessage());
		}
		return response;
	}

	public void wa100SVC501(String mapping_id) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0004", null);
		response = new ResponseDTO();
		fileHandler = new HandlerCreator().create(FileType.CSV);
		try {
			loadMappingResource();
			setRequiredIds(mapping_id);
			String filePath = (String) metaUtil.getMetaEntityItemValue(
					mappingResource.getMap1201().stream()
							.filter(map -> map.getFile_division().equals(ModuleConst.FILE_DIV_DOWNLOAD)).toList(),
					"map" + ModuleConst.MODULE_JOIN_REGEX + ModuleConst.MODULE_KEY_ID, mapId, "file_path");
			fileHandler.open(filePath, FileUseType.READ);
			fileHandler.download(filePath, mapId);
			fileHandler.close(FileUseType.DOWNLOAD);
		} catch (Exception e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1501", e.getMessage());
		}
	}

	// Mapping List - Data Upload
	@SuppressWarnings("unchecked")
	public ResponseDTO wa100SVC601(MultipartFile file, String mapping_id) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0005", null);
		response = new ResponseDTO();
		// setting map_id, src_id, tgt_id value
		try {
			setRequiredIds(mapping_id);
			loadMappingResource();
			String file_path = saveCSVForm(file, mapId);
			if (file_path != null) {

				String srcSysCode = (String) metaUtil.getMetaEntityItemValue(mappingResource.getMap1002(),
						ModuleConst.MODULE_KEY_ID, mapId, MappingConst.KEY_STR_SRC + ModuleConst.MODULE_JOIN_REGEX
								+ MappingConst.KEY_STR_SYS + ModuleConst.MODULE_JOIN_REGEX + MappingConst.KEY_STR_CODE);

				String tgtSysCode = (String) metaUtil.getMetaEntityItemValue(mappingResource.getMap1002(),
						ModuleConst.MODULE_KEY_ID, mapId, MappingConst.KEY_STR_TGT + ModuleConst.MODULE_JOIN_REGEX
								+ MappingConst.KEY_STR_SYS + ModuleConst.MODULE_JOIN_REGEX + MappingConst.KEY_STR_CODE);

				String srcSystem = (String) metaUtil.getMetaEntityItemValue(operation.getModuleResource().getSys4001(),
						MappingConst.KEY_STR_CODE, srcSysCode, MappingConst.KEY_STR_SYSTEM);

				String tgtSystem = (String) metaUtil.getMetaEntityItemValue(operation.getModuleResource().getSys4001(),
						MappingConst.KEY_STR_CODE, tgtSysCode, MappingConst.KEY_STR_SYSTEM);

				String tgtObj = (String) metaUtil.getMetaEntityItemValue(mappingResource.getMap1002(),
						ModuleConst.MODULE_KEY_ID, mapId, MappingConst.KEY_STR_TGT + ModuleConst.MODULE_JOIN_REGEX
								+ MappingConst.KEY_STR_OBJ + ModuleConst.MODULE_JOIN_REGEX + MappingConst.KEY_STR_NAME);
				//__debug__
				SystemChecker.getInstance().startTime();
				
				// load field mapping resource list
				loadFieldMappingResource();
				loadMappingUsingSystem(srcSysCode, tgtSysCode);
				setTgtSystem(tgtSysCode);
				
				//__debug__
				SystemChecker.getInstance().finishTime();
				MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", "load mapping resource-" + SystemChecker.getInstance().elapsedTime());
				
				//__debug__
				SystemChecker.getInstance().startTime();
				SystemChecker.getInstance().startMemory();
				
				converter = new DataConverter(srcSystem, tgtSystem, fieldMappingResource.getMap1102(),
						operation.getProperties().getCONVERT_DATETIME_PATTERN());

				fileHandler = new HandlerCreator().create(FileType.CSV);
				fileHandler.open(file_path, FileUseType.READ);
				String header = fileHandler.read(FileContentType.HEADER, FileReadType.ROW);
				ArrayList<String> dataEntries = fileHandler.read(FileContentType.DATA, FileReadType.LOAD);
				//System.out.println("read data rows : " + dataEntries.size());
				fileHandler.close(FileUseType.READ);

				dataLoader = new CSVDataLoader(header, dataEntries, converter);
				dataLoader.loadData();

				// insert
				Map<Integer, Object> convertRequest = pagingRequestMap(dataLoader.getConversionEntries(), 200);

				//  List<SalesforceResponse.Insert> insertionFailed = new ArrayList<>(); //업로드 실패 레코드
				//__debug__
				SystemChecker.getInstance().finishTime();
				SystemChecker.getInstance().finishMemory();
				MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", "load & conversion upload data-" + SystemChecker.getInstance().elapsedTime());
				MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", "load & conversion upload data-" + SystemChecker.getInstance().memoryUsage());
				
				//__debug__
				SystemChecker.getInstance().startTime();
				
				Boolean upload = false; 
				for (int index = ModuleConst.MODULE_DIGIT_0; index < convertRequest.size(); index++) {
					// insertionFailed.addAll(salesforceAPI.CompositeInsert(salesforceAPI.GetToken(tgtSys), tgtSys, tgtObj, (List<Map<String, Object>>) convertRequest.get(index))); // 업로드 실패 레코드들을 받아오는 코드
					upload = salesforceAPI.CompositeInsert(salesforceAPI.GetToken(tgtSys), tgtSys, tgtObj,
							(List<Map<String, Object>>) convertRequest.get(index));
					MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", String.format("upsert size %d", convertRequest.size()));
				}
				
				if (upload) {
					response.setResult("200");
					response.setReturn_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "1000"), "업로드"));
				} else {
					response.setError_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "1000"), "업로드"));
					response.setError_code("500");
					MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", String.format("mapId : %s", mapId));
				}
				
				//__debug__
				SystemChecker.getInstance().finishTime();
				MessageHandler.getInstance().setModuleMessage(LogLevel.DEBUG.name(), "1601", "upsert target object-" + SystemChecker.getInstance().elapsedTime());
				
			}
		} catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException
				| ClassNotFoundException | IOException | SQLException e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1601", e.getMessage());
		}
		fieldMappingResource.clearResource();

		// release resource
		return response;
	}

	// Mapping Configuration
	public ResponseDTO wa110SVC101(String mapping_id) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0006", null);
		response = new ResponseDTO();

		try {
			setRequiredIds(mapping_id);
			loadFieldMappingResource();
			Map<String, Object> svc101 = new HashMap<>();
			List<MAP1002> map1002 = mappingResource.getMap1002().stream().filter(obj -> obj.getId().equals(mapId))
					.collect(Collectors.toList());
			svc101.put("basicInfo", map1002);
			svc101.put("fieldMappingList", fieldMappingResource.getMap1102());
			response.setResult(svc101);

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException
				| ClassNotFoundException | IOException e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1101", e.getMessage());
		}

		return response;
	}

	public ResponseDTO wa110SVC601(String srcSysCode, String tgtSysCode, String tgtObjName) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0008", null);
		response = new ResponseDTO();
		fieldMappingResource = new FieldMappingResource();
		try {
			loadMappingUsingSystem(srcSysCode, tgtSysCode);
			setTgtSystem(tgtSysCode);
			SalesforceResponse.Token token = salesforceAPI.GetToken(tgtSys);
			response.setResult(salesforceAPI.GetMeta(token, tgtSys, tgtObjName));
			response.setReturn_msg("타겟 오브젝트의 정보를 가져오는데 성공하였습니다.");

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException
				| ClassNotFoundException | IOException e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1601", e.getMessage());
		}

		return response;

	}

	@SuppressWarnings("unchecked")
	public ResponseDTO wa110SVC201(Map<String, Object> request) {
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0007", null);
		
		response = new ResponseDTO();
		boolean isInsert = !((Map<String, Object>) request.get("basicInfo")).containsKey("map_id");
		now = LocalDateTime.parse(LocalDateTime.now().format(formatter));

		List<String> tgtFldIds = new ArrayList<>(), srcFldIds = new ArrayList<>(), map1101Ids = new ArrayList<>();

		try {
			loadMappingResource();
			List<MAP1001> map1001 = mappingResource.getMap1001();
			List<MAP1002> map1002 = mappingResource.getMap1002();
			List<MAP1201> map1201 = mappingResource.getMap1201();
			List<MAP1301> map1301 = mappingResource.getMap1301();

			if (isInsert) {
				mapId = IdGenerator.getInstance().getId();
				srcId = IdGenerator.getInstance().getId();
				tgtId = IdGenerator.getInstance().getId();
			} else {
				setRequiredIds((String) ((Map<String, Object>) request.get("basicInfo")).get("map_id"));
			}
			int index, cnt;
			boolean saveResult = false;

			Map<String, Object> basicInfo = (Map<String, Object>) request.get("basicInfo");
			List<Map<String, Object>> fieldMappingList = (List<Map<String, Object>>) request.get("fieldMappingList");

			Object[] fieldIds = operation.getProperties().getMAPPING_FIELD_IDS().split(ModuleConst.MODULE_DIV_REGEX);
			Object[] mapIds = operation.getProperties().getMAPPING_FILE_IDS().split(ModuleConst.MODULE_DIV_REGEX);

			if (basicInfo != null && basicInfo.size() > 0 && fieldMappingList != null && fieldMappingList.size() > 0) {
				for (index = ModuleConst.MODULE_DIGIT_0; index < fieldIds.length; index++) {
					operation.setResourcePathForFile(operation.getModuleResource().getSys1001(), fieldIds[index]);
					if (index == ModuleConst.MODULE_DIGIT_0) { // map1101
						List<MAP1101> map1101 = new ArrayList<>();
						for (Map<String, Object> field : fieldMappingList) {

							String id, srcFldId, tgtFldId;

							id = field.containsKey("id") ? field.get("id").equals("") || field.get("id").equals("null")
									? IdGenerator.getInstance().getId()
									: (String) field.get("id") : IdGenerator.getInstance().getId();
							srcFldId = field.containsKey("src_fld_id")
									? field.get("src_fld_id").equals("") || field.get("src_fld_id").equals("null")
											? IdGenerator.getInstance().getId()
											: (String) field.get("src_fld_id")
									: IdGenerator.getInstance().getId();
							tgtFldId = field.containsKey("tgt_fld_id")
									? field.get("tgt_fld_id").equals("") || field.get("tgt_fld_id").equals("null")
											? IdGenerator.getInstance().getId()
											: (String) field.get("tgt_fld_id")
									: IdGenerator.getInstance().getId();

							srcFldIds.add(srcFldId);
							tgtFldIds.add(tgtFldId);

							map1101Ids.add(id);
							MAP1101 requestMap = new MAP1101(id, mapId, Integer.parseInt((String) field.get("order")),
									srcFldId, tgtFldId, createdDateTime(basicInfo), now);
							map1101.add(requestMap);
						}

						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + mapId, map1101);

					}
					if (index == ModuleConst.MODULE_DIGIT_1) { // map1102
						List<MAP1102> map1102 = new ArrayList<>();
						cnt = ModuleConst.MODULE_DIGIT_0;
						for (Map<String, Object> field : fieldMappingList) {
							MAP1102 requestMap = new MAP1102(map1101Ids.get(cnt), mapId,
									Integer.parseInt((String) field.get("order")), srcFldIds.get(cnt),
									(String) field.get("src_fld_name"), (String) field.get("src_fld_type"),
									Integer.parseInt((String) field.get("src_fld_max_length")),
									Boolean.parseBoolean((String) field.get("src_fld_nullable")),
									Integer.parseInt((String) field.get("src_fld_precision")),
									Integer.parseInt((String) field.get("src_fld_digits")), tgtFldIds.get(cnt),
									(String) field.get("tgt_fld_name"), (String) field.get("tgt_fld_type"),
									Integer.parseInt((String) field.get("tgt_fld_max_length")),
									Boolean.parseBoolean((String) field.get("tgt_fld_nullable")),
									Integer.parseInt((String) field.get("tgt_fld_precision")),
									Integer.parseInt((String) field.get("tgt_fld_digits")), createdDateTime(field),
									now);
							map1102.add(requestMap);
							cnt++;
						}
						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + mapId, map1102);
					}
					if (index == ModuleConst.MODULE_DIGIT_2) { // src1001
						List<SRC1001> src1001 = new ArrayList<>();
						SRC1001 requestSrcObj = new SRC1001(srcId, mapId, (String) basicInfo.get("src_obj_name"),
								(String) basicInfo.get("src_sys_code"), createdDateTime(basicInfo), now);
						src1001.add(requestSrcObj);
						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + mapId, src1001);
					}
					if (index == ModuleConst.MODULE_DIGIT_3) { // src1101
						List<SRC1101> src1101 = new ArrayList<>();
						cnt = ModuleConst.MODULE_DIGIT_0;
						for (Map<String, Object> field : fieldMappingList) {
							SRC1101 requestSrcField = new SRC1101(srcFldIds.get(cnt), srcId,
									(String) field.get("src_fld_name"), (String) field.get("src_fld_type"),
									Integer.parseInt((String) field.get("src_fld_max_length")),
									Boolean.parseBoolean((String) field.get("src_fld_nullable")),
									Integer.parseInt((String) field.get("src_fld_precision")),
									Integer.parseInt((String) field.get("src_fld_digits")), createdDateTime(field),
									now);
							src1101.add(requestSrcField);
							cnt++;
						}
						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + srcId, src1101);
					}
					if (index == ModuleConst.MODULE_DIGIT_4) { // tgt1001
						List<TGT1001> tgt1001 = new ArrayList<>();
						TGT1001 requestTgtObj = new TGT1001(tgtId, mapId, (String) basicInfo.get("tgt_obj_name"),
								(String) basicInfo.get("tgt_sys_code"), createdDateTime(basicInfo), now);
						tgt1001.add(requestTgtObj);
						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + mapId, tgt1001);
					}
					if (index == ModuleConst.MODULE_DIGIT_5) { // tgt1101
						List<TGT1101> tgt1101 = new ArrayList<>();
						cnt = ModuleConst.MODULE_DIGIT_0;
						for (Map<String, Object> field : fieldMappingList) {
							TGT1101 requestTgtField = new TGT1101(tgtFldIds.get(cnt), tgtId,
									(String) field.get("tgt_fld_name"), (String) field.get("tgt_fld_type"),
									Integer.parseInt((String) field.get("tgt_fld_max_length")),
									Boolean.parseBoolean((String) field.get("tgt_fld_nullable")),
									Integer.parseInt((String) field.get("tgt_fld_precision")),
									Integer.parseInt((String) field.get("tgt_fld_digits")), createdDateTime(field),
									now);
							tgt1101.add(requestTgtField);
							cnt++;
						}
						saveResult = metaUtil.setMetaData(operation.getFileDirPath() + tgtId, tgt1101);
					}
					if (!saveResult) {
						break;
					}
				}
				if (saveResult) {
					for (index = ModuleConst.MODULE_DIGIT_0; index < mapIds.length; index++) {
						operation.setResourcePathForFile(operation.getModuleResource().getSys1001(), mapIds[index]);
						if (index == ModuleConst.MODULE_DIGIT_0) { // map1001
							final MAP1001 requestMap = new MAP1001(mapId, (String) basicInfo.get("cat_code"),
									(String) basicInfo.get("name"), (String) basicInfo.get("description"), srcId, tgtId,
									(String) basicInfo.get("creator"), now, now);
							if (isInsert) {
								map1001.add(requestMap);
							} else {
								map1001.stream().filter(map -> map.getId().equals(mapId)).forEach((map) -> {
									map.setCat_code(requestMap.getCat_code());
									map.setName(requestMap.getName());
									map.setDescription(requestMap.getDescription());
									map.setLast_modified_datetime(now);
								});
							}

							saveResult = metaUtil.setMetaData(operation.getFilePath(), map1001);
						}
						if (index == ModuleConst.MODULE_DIGIT_1) { // map1002
							final MAP1002 requestMap = new MAP1002(mapId, (String) basicInfo.get("cat_code"),
									(String) basicInfo.get("cat_name"), (String) basicInfo.get("name"),
									(String) basicInfo.get("description"), srcId,
									(String) basicInfo.get("src_sys_code"), (String) basicInfo.get("src_sys_name"),
									(String) basicInfo.get("src_obj_name"), tgtId,
									(String) basicInfo.get("tgt_sys_code"), (String) basicInfo.get("tgt_sys_name"),
									(String) basicInfo.get("tgt_obj_name"), (String) basicInfo.get("creator"), now,
									now);
							if (isInsert) {
								map1002.add(requestMap);
							} else {
								map1002.stream().filter(map -> map.getId().equals(mapId)).forEach((map) -> {
									map.setCat_code(requestMap.getCat_code());
									map.setCat_name(requestMap.getCat_name());
									map.setName(requestMap.getName());
									map.setDescription(requestMap.getDescription());
									map.setSrc_obj_name(requestMap.getSrc_obj_name());
									map.setLast_modified_datetime(now);
								});
							}

							saveResult = metaUtil.setMetaData(operation.getFilePath(), map1002);

						}
						if (index == ModuleConst.MODULE_DIGIT_2) { // map1201
							String downloadPath = ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX,
									ModuleConst.MODULE_EMPTY_REGEX) + File.separator + ModuleConst.FILE_DIV_DOWNLOAD
									+ File.separator + ModuleConst.FILE_TYPE_CSV + File.separator + mapId;

							MAP1201 requestMap = new MAP1201(IdGenerator.getInstance().getId(),
									ModuleConst.FILE_DIV_DOWNLOAD, ModuleConst.FILE_TYPE_CSV, downloadPath, mapId, now,
									now);

							String[] srcFldName = new String[fieldMappingList.size()];

							String header;

							cnt = ModuleConst.MODULE_DIGIT_0;
							for (Map<String, Object> fields : fieldMappingList) {
								srcFldName[cnt] = ((String) fields.get("src_fld_name"));
								cnt++;
							}

							if (isInsert) {
								map1201.add(requestMap);
							} else {
								// String uploadPath = (String) metaUtil.getMetaEntityItemValue(map1201,
								// "map_id", mapId, "file_path");
								map1201.stream().filter(map -> map.getMap_id().equals(mapId))
										.forEach(map -> map.setLast_modified_datetime(now));
							}

							header = String.join(ModuleConst.MODULE_COMMA_REGEX, srcFldName);
							boolean fileWrite = writeCSVHeader(downloadPath, header);
							if (fileWrite) {
								saveResult = metaUtil.setMetaData(operation.getFilePath(), map1201);
							} else {
								saveResult = false;
							}
						}

						if (index == ModuleConst.MODULE_DIGIT_3) { // map1301
							final MAP1301 requestMap = new MAP1301(IdGenerator.getInstance().getId(),
									(String) basicInfo.get("src_sys_code"), (String) basicInfo.get("cat_code"),
									(String) basicInfo.get("cat_name"), mapId, now, now);
							if (isInsert) {
								map1301.add(requestMap);
							} else {
								map1301.stream().filter(map -> map.getMap_id().equals(mapId)).forEach(map -> {
									map.setSys_code(requestMap.getSys_code());
									map.setCat_code(requestMap.getCat_code());
									map.setCat_name(requestMap.getCat_name());
									map.setLast_modified_datetime(now);
								});
							}
							saveResult = metaUtil.setMetaData(operation.getFilePath(), map1301);
						}
						if (!saveResult) {
							break;
						}
					}
				}
				if (saveResult) {
					String returnMsg;
					if (isInsert) {
						returnMsg = String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "1200"), "맵핑");
					} else {
						returnMsg = String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "1300"), "맵핑");
					}
					response.setResult("200");
					response.setReturn_msg(returnMsg);
				} else {
					response.setError_code("500");
					response.setError_msg(String.format(MessageHandler.getInstance().setReturnMessage(MessageConst.RETURN_MSG_TYPE, "2200"), "맵핑"));
				}
			} else {
				response.setError_code("400");
				response.setError_msg("잘못된 요청입니다.");
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException
				| IOException | ClassNotFoundException e) {
			MessageHandler.getInstance().setModuleMessage(LogLevel.ERROR.name(), "1201", e.getMessage());
		}
		
		return response;
	}

	private void loadMappingResource()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException, IOException {
		Object[] recordId = operation.getProperties().getMAPPING_FILE_IDS().split(ModuleConst.MODULE_DIV_REGEX);
		for (int index = ModuleConst.MODULE_DIGIT_0; index < recordId.length; index++) {
			operation.setResourcePathForFile(operation.getModuleResource().getSys1001(), recordId[index]);

			if (index == ModuleConst.MODULE_DIGIT_0) { // MAP1001
				mappingResource
						.setMap1001(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));
				// __DEBUG__System.out.println(resource.getMap1001());
			}

			if (index == ModuleConst.MODULE_DIGIT_1) { // MAP1002
				mappingResource
						.setMap1002(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));
				// __DEBUG__System.out.println(resource.getMap1002());
			}

			if (index == ModuleConst.MODULE_DIGIT_2) { // MAP1201
				mappingResource
						.setMap1201(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));
				// __DEBUG__System.out.println(resource.getMap1201());
			}

			if (index == ModuleConst.MODULE_DIGIT_3) { // MAP1301
				mappingResource
						.setMap1301(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));
				// __DEBUG__System.out.println(resource.getMap1301());
			}
		}
	}

	private void loadFieldMappingResource()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException, IOException {
		fieldMappingResource = new FieldMappingResource();

		// MAP1101
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getMap1101().getClass().getName(), mapId);
				MAP1101.class.getSimpleName().toLowerCase(), mapId);
		fieldMappingResource
				.setMap1101(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

		// MAP1102
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getMap1102().getClass().getName(), mapId);
				MAP1102.class.getSimpleName().toLowerCase(), mapId);
		fieldMappingResource
				.setMap1102(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

		// src1001
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getSrc1001().getClass().getName(), mapId);
				SRC1001.class.getSimpleName().toLowerCase(), mapId);
		fieldMappingResource
				.setSrc1001(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

		// src1101
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getSrc1101().getClass().getName(), srcId);
				SRC1101.class.getSimpleName().toLowerCase(), srcId);
		fieldMappingResource
				.setSrc1101(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

		// tgt1001
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getTgt1001().getClass().getName(), mapId);
				TGT1001.class.getSimpleName().toLowerCase(), mapId);
		fieldMappingResource
				.setTgt1001(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

		// tgt1101
		operation.setResourcePathForDirectory(operation.getModuleResource().getSys1001(),
//                fieldMappingResource.getTgt1101().getClass().getName(), tgtId);
				TGT1101.class.getSimpleName().toLowerCase(), tgtId);
		fieldMappingResource
				.setTgt1101(operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));

	}

	private void loadMappingUsingSystem(String src_sys_code, String tgt_sys_code)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException, IOException {
		if (src_sys_code.equals(MappingConst.MANUAL_SYS_CODE)) {
			// pass
		} else {
			reflectUseSystemResource(src_sys_code);
		}
		reflectUseSystemResource(tgt_sys_code);
	}

	private void reflectUseSystemResource(String system_code)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException, IOException {
		operation.setResourcePathForFile(operation.getModuleResource().getSys1001(),
				metaUtil.getMetaEntityItemValue(operation.getModuleResource().getSys1001(),
						ModuleConst.MODULE_KEY_FILENAME, MappingConst.KEY_STR_SYS + system_code,
						ModuleConst.MODULE_KEY_ID));

		String methodName = ModuleConst.REFLECTION_METHOD_SET + MappingConst.KEY_STR_SYS.substring(0, 1).toUpperCase()
				+ MappingConst.KEY_STR_SYS.substring(1) + system_code;
		fieldMappingResource.getClass().getMethod(methodName, List.class).invoke(fieldMappingResource,
				operation.getResourceList(metaUtil.getMetaEntityClass(operation.getClassPath())));
	}

	private void setRequiredIds(String mapping_id) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		mapId = mapping_id;
		srcId = (String) metaUtil.getMetaEntityItemValue(mappingResource.getMap1002(), ModuleConst.MODULE_KEY_ID, mapId,
				MappingConst.KEY_STR_SRC + ModuleConst.MODULE_JOIN_REGEX + ModuleConst.MODULE_KEY_ID);
		tgtId = (String) metaUtil.getMetaEntityItemValue(mappingResource.getMap1002(), ModuleConst.MODULE_KEY_ID, mapId,
				MappingConst.KEY_STR_TGT + ModuleConst.MODULE_JOIN_REGEX + ModuleConst.MODULE_KEY_ID);
	}

	private void setTgtSystem(String sysCode) {
		switch (sysCode) {
		case "4101" -> tgtSys = objectMapper.convertValue(fieldMappingResource.getSys4101(), new TypeReference<>() {
		});
		}
	}

	private Map<Integer, Object> pagingRequestMap(List<Object> request, int cnt) {

		int listSize = request.size() / cnt + 1;
		int startIndex = 0;
		int endIndex = cnt;
		List<Map<String, Object>> requestList = objectMapper.convertValue(request, new TypeReference<>() {
		});

		Map<Integer, Object> pagingRequestMap = new HashMap<>();

		for (int i = 0; i < listSize; i++) {
			List<Map<String, Object>> splitList;
			if (i < listSize - 1) {
				splitList = requestList.subList(startIndex, endIndex);
				startIndex += cnt;
				endIndex += cnt;
			} else {
				endIndex = requestList.size();
				splitList = requestList.subList(startIndex, endIndex);
			}
			pagingRequestMap.put(i, splitList);
		}
		return pagingRequestMap;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private <T> String convertToCSV(List<T> original) {
		List<Map<String, Object>> list = objectMapper.convertValue(original, new TypeReference<>() {
		});

		List<String> headers = list.stream()
				.flatMap(map -> ((Map<String, Object>) map.get("records")).keySet().stream()).distinct().toList();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < headers.size(); i++) {
			sb.append(headers.get(i));
			sb.append(i == headers.size() - 1 ? "\n" : ",");
		}
		for (Map<String, Object> map : list) {
			Map<String, Object> records = (Map<String, Object>) map.get("records");
			for (int i = 0; i < headers.size(); i++) {
				sb.append(records.get(headers.get(i)));
				sb.append(i == headers.size() - 1 ? "\n" : ",");
			}
		}
		return sb.toString();
	}
	
	// Upload Form
	public String saveCSVForm(MultipartFile file, String mapId) throws IOException {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		now = LocalDateTime.parse(LocalDateTime.now().format(formatter));

		Object[] mapIds = operation.getProperties().getMAPPING_FILE_IDS().split(ModuleConst.MODULE_DIV_REGEX);

		String fileId = IdGenerator.getInstance().getId();

		String filePath = ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX,
				ModuleConst.MODULE_EMPTY_REGEX) + File.separator + ModuleConst.FILE_DIV_UPLOAD + File.separator
				+ ModuleConst.FILE_TYPE_CSV + File.separator + mapId + File.separator + now.format(timeFormatter)
				+ File.separator + fileId;
		String content = new String(file.getBytes(), StandardCharsets.UTF_8);

		try {
			operation.setResourcePathForFile(operation.getModuleResource().getSys1001(),
					mapIds[ModuleConst.MODULE_DIGIT_2]);

			List<MAP1201> map1201 = mappingResource.getMap1201();
			MAP1201 requestMap = new MAP1201(IdGenerator.getInstance().getId(), ModuleConst.FILE_DIV_UPLOAD,
					ModuleConst.FILE_TYPE_CSV, filePath, mapId, now, now);

			boolean result = writeCSVHeader(filePath, content);

			if (result) {
				map1201.add(requestMap);
				metaUtil.setMetaData(operation.getFilePath(), map1201);
			} else {
				filePath = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public LocalDateTime createdDateTime(Map<String, Object> field) {
		LocalDateTime createdDateTime;
		if (field.containsKey("created_datetime")) {
			createdDateTime = LocalDateTime.parse((String) field.get("created_datetime"), formatter);
		} else {
			createdDateTime = now;
		}
		return createdDateTime;
	}

	public boolean writeCSVHeader(String file_path, String data) {
		fileHandler = new HandlerCreator().create(FileType.CSV);
		fileHandler.open(file_path, FileUseType.WRITE);
		boolean result = fileHandler.write(FileContentType.HEADER, FileTypeDef.FileWriteType.DECRYPTION, data,
				ModuleConst.MODULE_DIGIT_0);
		fileHandler.close(FileUseType.WRITE);
		return result;
	}
}
