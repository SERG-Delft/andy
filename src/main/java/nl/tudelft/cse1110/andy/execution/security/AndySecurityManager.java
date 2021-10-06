package nl.tudelft.cse1110.andy.execution.security;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.net.URLPermission;
import java.security.Permission;
import java.security.SecurityPermission;
import java.util.PropertyPermission;
import java.util.logging.LoggingPermission;

public class AndySecurityManager extends SecurityManager {
    public AndySecurityManager() {
        super();
    }

    @Override
    public void checkPermission(Permission perm) {
        boolean untrusted = false;
        boolean mockitoInternal = false;
        boolean jdkInternalLoader = false;
        for (StackTraceElement elem : Thread.currentThread().getStackTrace()) {
            if (elem.getClassName().startsWith("org.mockito.internal")) {
                mockitoInternal = true;
            }
            if (elem.getClassName().startsWith("jdk.internal.") && !untrusted) {
                jdkInternalLoader = true;
            }
            if (elem.getClassName().startsWith("delft.")) {
                untrusted = true;
            }
        }

        if (!untrusted) {
            return;
        }

        if (checkPermissionsUntrusted(perm, mockitoInternal, jdkInternalLoader)) return;

        var ex = new SecurityException("Operation not permitted: " +
                                       perm.getClass().getName() + " " +
                                       perm.getName() + " " +
                                       perm.getActions());
        ex.printStackTrace();
        throw ex;
    }

    private boolean checkPermissionsUntrusted(Permission perm, boolean mockitoInternal, boolean jdkInternalLoader) {
        if (checkFilePermission(perm, mockitoInternal, jdkInternalLoader)) return true;

        if (checkPropertyPermission(perm)) return true;

        if (checkRuntimePermission(perm, mockitoInternal)) return true;

        if (checkLoggingPermission(perm)) return true;

        if (checkReflectPermission(perm)) return true;

        if (checkSecurityPermission(perm)) return true;

        if (checkNetworkPermissions(perm)) return true;
        return false;
    }

    private boolean checkNetworkPermissions(Permission perm) {
        if (perm instanceof NetPermission || perm instanceof URLPermission || perm instanceof SocketPermission) {
            return true;
        }
        return false;
    }

    private boolean checkSecurityPermission(Permission perm) {
        if (perm instanceof SecurityPermission) {
            if (perm.getName().startsWith("putProviderProperty.") ||
                perm.getName().startsWith("getProperty.")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkReflectPermission(Permission perm) {
        if (perm instanceof ReflectPermission) {
            if (perm.getName().equals("suppressAccessChecks")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLoggingPermission(Permission perm) {
        if (perm instanceof LoggingPermission) {
            if (perm.getName().equals("control")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRuntimePermission(Permission perm, boolean mockitoInternal) {
        if (perm instanceof RuntimePermission) {
            if (mockitoInternal && checkMockitoInternalRuntimePermission(perm)
                || checkReflectionRuntimePermissions(perm)
                || perm.getName().equals("loggerFinder")
                || perm.getName().equals("createClassLoader")
                || perm.getName().equals("createSecurityManager")
                || checkNetworkRuntimePermissions(perm)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkReflectionRuntimePermissions(Permission perm) {
        return perm.getName().equals("getProtectionDomain")
               || perm.getName().equals("accessDeclaredMembers")
               || perm.getName().equals("getClassLoader");
    }

    private boolean checkNetworkRuntimePermissions(Permission perm) {
        return perm.getName().equals("selectorProvider")
               || perm.getName().equals("accessClassInPackage.sun.net")
               || perm.getName().equals("loadLibrary.extnet")
               || perm.getName().startsWith("accessClassInPackage.sun.security.");
    }

    private boolean checkMockitoInternalRuntimePermission(Permission perm) {
        return perm.getName().equals("accessSystemModules") ||
               perm.getName().equals("accessClassInPackage.sun.misc") ||
               perm.getName().startsWith("accessClassInPackage.") ||
               perm.getName().equals("reflectionFactoryAccess");
    }

    private boolean checkPropertyPermission(Permission perm) {
        if (perm instanceof PropertyPermission) {
            if (perm.getActions().equals("read") ||
                perm.getName().equals("*")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFilePermission(Permission perm, boolean mockitoInternal, boolean jdkInternalLoader) {
        if (perm instanceof FilePermission) {
            if (perm.getActions().equals("read") &&
                !perm.getName().endsWith(".java") &&
                (checkFilePermissionAllowedExtensions(perm) ||
                 checkFilePermissionAllowedPaths(perm)) &&
                !perm.getName().contains("..") &&
                (mockitoInternal || jdkInternalLoader)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFilePermissionAllowedExtensions(Permission perm) {
        return perm.getName().endsWith(".jar") ||
               perm.getName().endsWith(".class") ||
               perm.getName().endsWith("net.properties");
    }

    private boolean checkFilePermissionAllowedPaths(Permission perm) {
        return perm.getName().contains("META-INF") ||
               perm.getName().contains("jdk") ||
               perm.getName().contains("jre") ||
               perm.getName().contains("jvm") ||
               perm.getName().contains("mockito");
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        this.checkPermission(perm);
    }
}
