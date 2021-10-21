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
        /*
         * Determine whether the current code should be trusted based on the package name.
         * If the code is trusted, all permissions are automatically granted.
         * As the solution file appears in the stacktrace of certain internal JDK methods as well as Mockito,
         * check if this is the case and grant some more dangerous permissions.
         */
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

        /*
         * If this method returns without throwing an exception, the requested permission will be granted.
         * If a SecurityException is thrown, it will be denied.
         */

        // If the code is trusted, grant all permissions
        if (!untrusted) {
            return;
        }

        // Determine if the currently requested permission should be granted
        if (checkPermissionsUntrusted(perm, mockitoInternal, jdkInternalLoader)) return;

        // If the currently requested permission should not be allowed,
        // throw an exception to block the execution
        var ex = new SecurityException("Operation not permitted: " +
                                       perm.getClass().getName() + " " +
                                       perm.getName() + " " +
                                       perm.getActions());

        // Print the stack trace to make debugging easier
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
        // Allow network access as it is needed for Selenium tests
        // External connections are not blocked here as they are prevented via other means
        if (perm instanceof NetPermission || perm instanceof URLPermission || perm instanceof SocketPermission) {
            return true;
        }
        return false;
    }

    private boolean checkSecurityPermission(Permission perm) {
        // Allow property access as it is needed in order to execute the tests
        if (perm instanceof SecurityPermission) {
            if (perm.getName().startsWith("putProviderProperty.") ||
                perm.getName().startsWith("getProperty.")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkReflectPermission(Permission perm) {
        // Allow overriding access checks via reflection as it is needed in order to execute the tests
        // Using reflection in solution code is blocked via other means
        if (perm instanceof ReflectPermission) {
            if (perm.getName().equals("suppressAccessChecks")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLoggingPermission(Permission perm) {
        // Allow log control permissions as they are needed in order to execute the tests
        if (perm instanceof LoggingPermission) {
            if (perm.getName().equals("control")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRuntimePermission(Permission perm, boolean mockitoInternal) {
        // Allow various runtime permissions as they are needed in order to execute the tests
        if (perm instanceof RuntimePermission) {
            if (mockitoInternal && checkMockitoInternalRuntimePermission(perm)
                || checkReflectionRuntimePermissions(perm)
                || perm.getName().equals("loggerFinder")
                || perm.getName().equals("createClassLoader")
                || perm.getName().equals("createSecurityManager")
                || perm.getName().equals("accessSystemModules")
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
        // Allow those permissions only if they are requested by internal Mockito classes
        return perm.getName().equals("accessSystemModules") ||
               perm.getName().equals("accessClassInPackage.sun.misc") ||
               perm.getName().startsWith("accessClassInPackage.") ||
               perm.getName().equals("reflectionFactoryAccess") ||
               perm.getName().equals("localeServiceProvider"); // Required when running via Docker
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
        // Allow reading certain files based on their path and extension.
        // This is needed in order to execute the tests.
        // Block writing or executing files.
        if (perm instanceof FilePermission) {
            if (perm.getActions().equals("read") &&
                !perm.getName().endsWith(".java") &&
                (
                        (
                                checkFilePermissionAllowedExtensions(perm) ||
                                checkFilePermissionAllowedPaths(perm)
                        )
                        && (mockitoInternal || jdkInternalLoader) ||
                        (perm.getName().contains("/repository/"))
                )
                && !perm.getName().contains("..")) {
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
