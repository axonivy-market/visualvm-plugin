package ch.ivyteam.ivy.visualvm.model;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.openide.util.Exceptions;

public class IvyJmxConstant {

  public static final class IvyServer {
    public static class Server {
      public static ObjectName NAME;
      public static final String KEY_VERSION = "version";
      public static final String KEY_BUILD_DATE = "buildDate";
      public static final String KEY_APPLICATION_NAME = "applicationName";
      public static final String KEY_DEVELOPER_MODE = "developerMode";
      public static final String KEY_RELEASE_CANDIDATE = "releaseCandidate";
      public static final String KEY_INSTALLATION_DIRECTORY = "installationDirectory";
      public static final String KEY_LICENSE_PARAMETERS = "licenceParameters";

      static {
        try {
          NAME = new ObjectName("Xpert.ivy Server:type=Server");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }

    public static final class SecurityManager {
      public static ObjectName NAME;
      public static final String KEY_LICENSED_USERS = "licensedUsers";
      public static final String KEY_LICENSED_SESSIONS = "licensedSessions";
      public static final String KEY_SESSIONS = "sessions";

      static {
        try {
          NAME = new ObjectName("Xpert.ivy Server:type=Security Manager");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }

    public static final class RichDialogExecution {
      public static ObjectName NAME;
      public static final String KEY_RD_SESSIONS = "richDialogSessions";

      static {
        try {
          NAME = new ObjectName("Xpert.ivy Server:type=Rich Dialog Execution Manager");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }

    public static final class DatabasePersistency {
      public static ObjectName NAME;
      public static final String KEY_PRODUCT_NAME = "databaseProductName";
      public static final String KEY_PRODUCT_VERSION = "databaseProductVersion";
      public static final String KEY_IVY_SYSDB_VERSION = "version";
      public static final String KEY_CONNECTION_URL = "connectionUrl";
      public static final String KEY_DRIVER_NAME = "driverName";
      public static final String KEY_USERNAME = "userName";

      static {
        try {
          NAME = new ObjectName("Xpert.ivy Server:type=Database Persistency Service");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }
  }

  public static final class JavaLang {
    public static final class OperatingSystem {
      public static ObjectName NAME;
      public static final String KEY_NAME = "Name";
      public static final String KEY_ARCH = "Arch";

      static {
        try {
          NAME = new ObjectName("java.lang:type=OperatingSystem");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }

    public static final class Runtime {
      public static ObjectName NAME;
      public static final String KEY_NAME = "Name";

      static {
        try {
          NAME = new ObjectName("java.lang:type=Runtime");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }
  }

  public static final class Ivy {
    public static final class Connector {
      public static ObjectName PATTERN;
      public static final String KEY_PROTOCOL = "protocol";
      public static final String KEY_PORT = "port";
      public static final String KEY_SCHEME = "scheme";

      static {
        try {
          PATTERN = new ObjectName("ivy:type=Connector,port=*");
        } catch (MalformedObjectNameException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

    }
  }
}
