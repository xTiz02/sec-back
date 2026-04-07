package com.prd.seccontrol.util;

import java.util.List;

public class SEConstants {
  public static final String API_VERSION_V1 = "/v1";
  public static final String API_VERSION_V2 = "/v2";

  public static final String SECURE_ENDPOINT_PREFIX = "/secure/api";

  public static final String SECURE_BASE_ENDPOINT = SECURE_ENDPOINT_PREFIX + API_VERSION_V1;
  public static final String SECURE_BASE_ENDPOINT_V2 = SECURE_ENDPOINT_PREFIX + API_VERSION_V2;

  public static final String PUBLIC_BASE_ENDPOINT = "/public/api" + API_VERSION_V1;

  public static final String DATE_FORMAT = "dd/MM/yyyy";

  public static final String PERU_LOCAL_TIMEZONE = "-05:00";
  public static final String INTERCEPTED_JSON = "INTERCEPTED_JSON";

  public static final Long INTERVAL_DAYS = 2L;

  // in minutes
  public static final Long ENTRY_AVAILABLE_TIME = 15L;
  public static final Long ENTRY_MAX_MARK_TIME = 120L;
  public static final Long ENTRY_TOLERANCE = 5L;

  public static final Long BREAK_TIME = 45L;
  public static final Long BREAK_EXIT_TOLERANCE = 5L;
  public static final Long BREAK_AUTO_CLOSE_TIME_BEFORE_EXIT = 120L;

  public static final Long EXIT_AVAILABLE_TIME = 15L;


  public static String S3_UPLOADS_BUCKET_NAME = "flit-uploads";

  public static String S3_ACCESS_KEY_ID = System.getProperty("S3_ACCESS_KEY_ID");

  public static String S3_SECRET_KEY_ACCESS = System.getProperty("S3_SECRET_KEY_ACCESS");

  public static String S3_REGION = System.getProperty("S3_REGION"); // aws.region = us-east-2

  public static final String S3_ASSISTANCE_FOLDER = "assistance";

  public static final List<List<Object>> endpointsMatcher = List.of(
      List.of("/guard-assistance/mark$","POST","Marca una asistencia para un turno específico.", true),
      List.of("/client-contract$","POST","Crea un nuevo contrato para un cliente.", true),
      List.of("/client-contract/\\d+$","PUT","Actualiza un contrato existente.", true),
      List.of("/client-contract/\\d+$","DELETE","Elimina un contrato existente.", true),
      List.of("/client$","POST","Crea un nuevo cliente.", true),
      List.of("/client/\\d+$","PUT","Actualiza un cliente existente.", true),
      List.of("/client/\\d+$","DELETE","Elimina un cliente existente.", true),
      List.of("/employee$","POST","Crea un nuevo empleado.", true),
      List.of("/employee/\\d+$","PUT","Actualiza un empleado existente.", true),
      List.of("/employee/\\d+$","DELETE","Elimina un empleado existente.", true),
      List.of("/external-guard$","POST","Agrega un nuevo guardia externo.", true),
      List.of("/external-guard/\\d+$","PUT","Actualiza un guardia externo existente.", true),
      List.of("/external-guard/\\d+$","DELETE","Elimina un guardia externo existente.", true),
      List.of("/guard$","POST","Agrega un nuevo guardia.", true),
      List.of("/guard/\\d+$","PUT","Actualiza un guardia existente.", true),
      List.of("/guard/\\d+$","DELETE","Elimina un guardia existente.", true),
      List.of("/contract-schedule/assign-weekly-turns$","POST","Asigna turnos semanales a un contrato.", true),
      List.of("/date-guard-unity-assignment/bulk-free-days$","POST","Marca días libres para múltiples asignaciones de guardias.", true),
      List.of("/date-guard-unity-assignment/vacation$","POST","Marca vacaciones para una asignación de guardia específica.", true),
      List.of("/date-guard-unity-assignment/\\d+$","DELETE","Elimina una asignación de guardia específica.", true),
      List.of("/date-guard-unity-assignment/vacation/\\d+$","DELETE","Elimina una marca de vacaciones para una asignación de guardia específica.", true),
      List.of("/guard-extra-hours$","POST","Crea un nuevo registro de horas extras para un turno de un guardia.", true),
      List.of("/guard-request/late-justification$","POST","Justifica una llegada tarde para un turno específico.", true),
      List.of("/schedule-exception$","POST","Agrega una nueva excepción de horario para un turno.", true),
      List.of("/schedule-exception/\\d+$","DELETE","Elimina una excepción de horario específica.", true),
      List.of("/schedule-exception/special-service$","POST","Agrega una nueva excepción de horario para un servicio especial.", true),
      List.of("/schedule-monthly/generate-month$","POST","Genera un nuevo horario mensual para un contrato específico.", true),
      List.of("/security-profiles$","POST","Crea un nuevo perfil de seguridad.", true),
      List.of("/security-profiles/\\d+$","PUT","Actualiza un perfil de seguridad existente.", true),
      List.of("/security-profiles/\\d+$","DELETE","Elimina un perfil de seguridad existente.", true),
      List.of("/security-profiles/\\d+/views$","PUT","Actualiza las vistas asociadas a un perfil de seguridad.", true),
      List.of("/security-profiles/\\d+/endpoints","PUT","Actualiza los endpoints asociados a un perfil de seguridad.", true),
      List.of("/user$","POST","Crea un nuevo usuario.", false),
      List.of("/user/\\d+$","PUT","Actualiza un usuario existente.", true),
      List.of("/user/\\d+$","DELETE","Elimina un usuario existente.", true),
      List.of("/special-service-unity$","POST","Crea una nueva unidad de servicio especial.", true),
      List.of("/special-service-unity/\\d+$","PUT","Actualiza una unidad de servicio especial existente.", true),
      List.of("/special-service-unity/\\d+$","DELETE","Elimina una unidad de servicio especial existente.", true),
      List.of("/special-service-schedule$","POST","Crea un nuevo horario para un servicio especial.", true),
      List.of("/turn-template$","POST","Crea una nueva plantilla de turno.", true),
      List.of("/turn-template/\\d+$","PUT","Actualiza una plantilla de turno existente.", true),
      List.of("/turn-template/\\d+$","DELETE","Elimina una plantilla de turno existente.", true),
      List.of("/unity$","POST","Crea una nueva unidad.", true),
      List.of("/unity/\\d+$","PUT","Actualiza una unidad existente.", true),
      List.of("/unity/\\d+$","DELETE","Elimina una unidad existente.", true),
      List.of("/storage/schedule/upload-file/validate$","POST","Valida un archivo Excel de horario mensual sin crear asignaciones.", false),
      List.of("/storage/schedule/upload-file/create$","POST","Crea asignaciones de guardias a partir de un archivo Excel de horario mensual validado.", false)
  );
}
