package ch.ivyteam.ivy.visualvm.model;

import java.util.logging.Logger;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class IvyJmxConstant {
    private static final Logger LOGGER = Logger.getLogger(IvyJmxConstant.class.getName());

    private static ObjectName initObjectName(String name) {
        ObjectName result = null;
        try {
            result = new ObjectName(name);
        } catch (MalformedObjectNameException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return result;
    }

    public static final class IvyServer {

        public static final class Server {

            public static final ObjectName NAME = initObjectName("Xpert.ivy Server:type=Server");
            public static final String KEY_VERSION = "version";
            public static final String KEY_BUILD_DATE = "buildDate";
            public static final String KEY_APPLICATION_NAME = "applicationName";
            public static final String KEY_DEVELOPER_MODE = "developerMode";
            public static final String KEY_RELEASE_CANDIDATE = "releaseCandidate";
            public static final String KEY_INSTALLATION_DIRECTORY = "installationDirectory";
            public static final String KEY_LICENSE_PARAMETERS = "licenceParameters";

            public static final class License {

                public static final String KEY_HOST_NAME = "host.name";
                public static final String KEY_SERVER_ELEMENTS_LIMIT = "server.elements.limit";
                public static final String KEY_LICENSEE_ORGANISATION = "licencee.organisation";
                public static final String KEY_LICENSEE_INDIVIDUAL = "licencee.individual";
                public static final String KEY_LICENSE_VALID_FROM = "licence.valid.from";
                public static final String KEY_LICENSE_KEYVERSION = "licence.keyversion";
                public static final String KEY_LICENSE_VALID_UNTIL = "licence.valid.until";
                public static final String KEY_SERVER_RIA = "server.ria";
                public static final String KEY_SERVER_USERS_LIMIT = "server.users.limit";
                public static final String KEY_SERVER_SESSIONS_LIMIT = "server.sessions.limit";
            }

        }

        public static final class SecurityManager {

            public static final ObjectName NAME = initObjectName("Xpert.ivy Server:type=Security Manager");
            public static final String KEY_LICENSED_USERS = "licensedUsers";
            public static final String KEY_LICENSED_SESSIONS = "licensedSessions";
            public static final String KEY_SESSIONS = "sessions";
        }

        public static final class RichDialogExecution {

            public static final ObjectName NAME = initObjectName(
                    "Xpert.ivy Server:type=Rich Dialog Execution Manager");
            public static final String KEY_RD_SESSIONS = "richDialogSessions";

        }

        public static final class DatabasePersistency {

            public static final ObjectName NAME = initObjectName(
                    "Xpert.ivy Server:type=Database Persistency Service");
            public static final String KEY_PRODUCT_NAME = "databaseProductName";
            public static final String KEY_PRODUCT_VERSION = "databaseProductVersion";
            public static final String KEY_IVY_SYSDB_VERSION = "version";
            public static final String KEY_CONNECTION_URL = "connectionUrl";
            public static final String KEY_DRIVER_NAME = "driverName";
            public static final String KEY_USERNAME = "userName";
            public static final String KEY_USED_CONNECTION = "usedConnections";
            public static final String KEY_MAX_CONNECTION = "maxConnections";
            public static final String KEY_OPEN_CONNECTION = "openConnections";
            public static final String KEY_TRANSACTION_NUMBER = "transactions";
            public static final String KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS
                    = "transactionsTotalExecutionTimeInMicroSeconds";
            public static final String KEY_TRANSACTION_MAX_EXE_TIME_IN_MS
                    = "transactionsMaxExecutionTimeInMicroSeconds";
            public static final String KEY_TRANSACTION_MAX_EXE_DELTA_TIME_IN_MS
                    = "transactionsMaxExecutionTimeDeltaInMicroSeconds";
            public static final String KEY_TRANSACTION_MIN_EXE_DELTA_TIME_IN_MS
                    = "transactionsMinExecutionTimeDeltaInMicroSeconds";
            public static final String KEY_TRANSACTION_MIN_EXE_TIME_IN_MS
                    = "transactionsMinExecutionTimeInMicroSeconds";
        }

        public static final class ExternalDatabase {
            public static final String NAME_PATTERN
                    = "Xpert.ivy Server:type=External Database,application=%s,environment=%s,name=%s";
            public static final String KEY_PRODUCT_NAME = "databaseProductName";
            public static final String KEY_PRODUCT_VERSION = "databaseProductVersion";
            public static final String KEY_IVY_SYSDB_VERSION = "version";
            public static final String KEY_CONNECTION_URL = "connectionUrl";
            public static final String KEY_DRIVER_NAME = "driverName";
            public static final String KEY_USERNAME = "userName";
            public static final String KEY_USED_CONNECTION = "usedConnections";
            public static final String KEY_MAX_CONNECTION = "maxConnections";
            public static final String KEY_OPEN_CONNECTION = "openConnections";
            public static final String KEY_TRANSACTION_NUMBER = "transactions";
            public static final String KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS
                    = "transactionsTotalExecutionTimeInMicroSeconds";
            public static final String KEY_TRANSACTION_MAX_EXE_TIME_IN_MS
                    = "transactionsMaxExecutionTimeInMicroSeconds";
            public static final String KEY_TRANSACTION_MAX_EXE_DELTA_TIME_IN_MS
                    = "transactionsMaxExecutionTimeDeltaInMicroSeconds";
            public static final String KEY_TRANSACTION_MIN_EXE_DELTA_TIME_IN_MS
                    = "transactionsMinExecutionTimeDeltaInMicroSeconds";
            public static final String KEY_TRANSACTION_MIN_EXE_TIME_IN_MS
                    = "transactionsMinExecutionTimeInMicroSeconds";
        }
    }

    public static final class JavaLang {

        public static final class OperatingSystem {

            public static final ObjectName NAME = initObjectName("java.lang:type=OperatingSystem");
            public static final String KEY_NAME = "Name";
            public static final String KEY_ARCH = "Arch";
        }

        public static final class Runtime {

            public static final ObjectName NAME = initObjectName("java.lang:type=Runtime");
            public static final String KEY_NAME = "Name";
        }
    }

    public static final class Ivy {

        public static final class Connector {

            public static final ObjectName PATTERN = initObjectName("ivy:type=Connector,port=*");
            public static final String KEY_PROTOCOL = "protocol";
            public static final String KEY_PORT = "port";
            public static final String KEY_SCHEME = "scheme";
        }

        public static final class Processor {

            public static final ObjectName PATTERN = initObjectName("ivy:type=GlobalRequestProcessor,name=*");
            public static final String KEY_REQUEST_COUNT = "requestCount";
            public static final String KEY_ERROR_COUNT = "errorCount";
            public static final String KEY_PROCESS_TIME = "processingTime";
        }

        public static final class Manager {

            public static final ObjectName PATTERN = initObjectName(
                    "ivy:type=Manager,context=*,host=localhost");
            public static final String KEY_SESSION_COUNTER = "sessionCounter";
        }
    }

}
